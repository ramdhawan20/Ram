package com.hcl.bss.schedulers;

import com.hcl.bss.constants.ApplicationConstants;
import com.hcl.bss.domain.*;
import com.hcl.bss.repository.*;
import com.hcl.bss.util.SubscriptionUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.hcl.bss.constants.ApplicationConstants.*;

/**
 * This will be used to renew the subscription as is
 * @author- Aditya Gupta
 */
@Component
public class SubscriptionRenewalScheduler {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SubscriptionUtility subscriptionUtility;

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Autowired
    private RatePlanVolumeRepository ratePlanVolumeRepository;

    @Autowired
    private AppConstantRepository constantRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RenewalBatchLogRepository renewalBatchLogRepository;
    @Value("${app.subscription.renewal.days}")
    private int renewalDays;

    private Timestamp runDownBatch;

    private List<AppConstantMaster> notificationsConstant;
    private Predicate<AppConstantMaster> numOfNotification;

    private Predicate<AppConstantMaster> notificationTrigger;

    @Scheduled(cron="0 0 0 * * ?")
    //@Transactional
    public void runAutorenewSubscriptionsScheduler(){
        runDownBatch = new Timestamp(System.currentTimeMillis());
        //get all customers whose subscriptions are due for renewal.
        List<Customer> customers = customerRepository.findBySubscriptions(BillingInvoicing.ACTIVE.getActiveStatus(),
                ApplicationConstants.Autorenew.AUTORENEW_ON.getAutoRenewStatus());

        //get notification config
        getNotificationConfig();

        //check for auto renew notifications and send accordingly
        Map<String,Set<Customer>> customerMap = sendAutoRenewalNotifications(customers);

        if(customerMap.containsKey("autorenewCustomers") && !customerMap.get("autorenewCustomers").isEmpty()){

            Set<Customer> updatedCustomers = customerMap.get("autorenewCustomers").stream().map(customer -> saveRenewableSubscriptions(customer)).collect(Collectors.toSet());

            //get all customers whose subscription auto renewal is turned off
            List<Customer> nonrenewableCustomers = customerRepository.findBySubscriptions(BillingInvoicing.ACTIVE.getActiveStatus(),
                    ApplicationConstants.Autorenew.AUTORENEW_OFF.getAutoRenewStatus());
            Set<Customer> updatedNonrenewableCustomer = nonrenewableCustomers.stream().map(customer -> updateNonRenewableCustomer(customer)).collect(Collectors.toSet());
            customerRepository.saveAll(updatedCustomers);
            customerRepository.saveAll(updatedNonrenewableCustomer);
            //updateRenewalBatchLog(updatedCustomers,"-");
        }
    }

    /**
     * This will save the renewable subscriptions
     * @param customer
     * @return
     */
    private Customer saveRenewableSubscriptions(Customer customer){
        try{
            List<Subscription> customerSubscriptions = new ArrayList<>(customer.getSubscriptions());
            Set<Subscription> renewableSubscriptions = customerSubscriptions.stream()
                                                            //.filter(currentSubscription -> isSubscriptionRenewable(currentSubscription))
                                                            .map(customerSubscription -> renewSubscription(customerSubscription))
                                                            .collect(Collectors.toSet());
            //update current subscriptions
            Set<Subscription> updateCurrentSubscriptions = customerSubscriptions.stream()
                                                                    //.filter(currentSubscription -> isSubscriptionRenewable(currentSubscription))
                                                                    .map(customerSubscription -> updateCurrentSubscription(customerSubscription))
                                                                    .collect(Collectors.toSet());
            Set<Subscription> allSubscriptions = new HashSet<>();
            allSubscriptions.addAll(renewableSubscriptions);
            allSubscriptions.addAll(updateCurrentSubscriptions);
            customer.setSubscriptions(allSubscriptions);
            return customer;
        }
        catch(Exception e){
            SubscriptionUtility.updateErrLog("Exception occurred while auto renewal for customerId:"+customer.getId()+" inside"
                    +e.getStackTrace()[0].getMethodName(), errorLogRepository);
            return customer;
        }
    }

    /**
     * It checks if the subscription is eligible for autorenew
     * @param subscription
     * @return isSubscriptionRenewable
     */

    /*private boolean isSubscriptionRenewable(Subscription subscription){
        try{
            int maxNotifications = filterNotification(notificationsConstant,numOfNotification);
            int notificationTriggerDays = filterNotification(notificationsConstant,notificationTrigger);
            int notificationsInterval = notificationTriggerDays/maxNotifications;
            LocalDate subscriptionEndDate = subscription.getSubscriptionEndDate().toLocalDateTime().toLocalDate();//LocalDate.now();
            LocalDate currentDate = LocalDate.now();
            if(notification!=null) {
                if (notification.getNotificationsSentCount() == maxNotifications)
                    return true;
                return false;
            }
                //if(notification.getNotificationsSentCount())
            //}
            return currentDate.plusDays(renewalDays).equals(subscriptionEndDate);
        }
        catch(Exception ex){
            SubscriptionUtility.updateErrLog("Exception occurred during auto renewal for subscription:"+subscription.getSubscriptionId()+"inside " +
                    ex.getStackTrace()[0].getMethodName(),errorLogRepository);
            return false;
        }
    }*/


    /**
     * This method will create a new subscription
     * @param subscription
     * @return renewedSubscription
     */
    private Subscription renewSubscription(Subscription subscription){
        //create new subscription
        Subscription renewedSubscription = populateSubscription(subscription);

        //insert into renewal table
        updateRenewalBatchLog(subscription.getSubscriptionId(),"-", "SUCCESS");
        //update the subscription with the new subscription id
        renewedSubscription.setPreviousSubscriptionId(subscription);
        return renewedSubscription;
    }

    /**
     * It will update thw existing subscription
     * @param subscription
     * @return currentSubscription
     */
    private Subscription updateCurrentSubscription(Subscription subscription) {
        subscription.setStatus("RENEWED");
        subscription.setTransactionReasonCode(ApplicationConstants.TransactionReasonCode.EXPIRED_WITH_RENEWAL.toString());
        subscription.setIsActive(0);
        return subscription;
    }

    /**
     * This is used to modify the subscription with the new renewal dates as per the billing frequency and billing cycle term
     * @param subscription
     * @param subRatePlan
     * @return subscription
     */
    private Subscription updateRenewalDates(Subscription subscription, Subscription renewableSubscription, SubscriptionRatePlan subRatePlan){

        LocalDate nextBillingDate = null;
        LocalDate subscriptionStartDate = null;
        LocalDate subscriptionEndDate = null;
        LocalDate currentDate = LocalDate.now();
        try{
            int billingCycleTerm = subRatePlan.getRatePlan().getBillingCycleTerm().intValue();
            int expiresAfter = subRatePlan.getRatePlan().getExpireAfter().intValue();
            //int billingCycleTerm = subRatePlan.getRatePlan().getBillingCycleTerm();
            //int expiresAfter = subRatePlan.getRatePlan().getExpiresAfter();
            //String frequency = subRatePlan.getRatePlan().getBillingFrequency();
            String frequency = subRatePlan.getRatePlan().getBillingFrequency();
            switch(frequency){
                case "ANNUAL":
                    //nextBillingDate = subscription.getNextBillingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusYears(billingCycleTerm);
                    nextBillingDate = currentDate.plusYears(1);
                    subscriptionEndDate = subscription.getSubscriptionEndDate().toLocalDateTime().toLocalDate().plusYears(expiresAfter);
                    break;
                case "MONTHLY":
                    //nextBillingDate = subscription.getNextBillingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusMonths(billingCycleTerm);
                    nextBillingDate = currentDate.plusMonths(1);
                    subscriptionEndDate = subscription.getSubscriptionEndDate().toLocalDateTime().toLocalDate().plusMonths(expiresAfter);
                    break;
                case "WEEKLY":
                    //nextBillingDate = subscription.getNextBillingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusWeeks(billingCycleTerm);
                    nextBillingDate = currentDate.plusWeeks(1);
                    subscriptionEndDate = subscription.getSubscriptionEndDate().toLocalDateTime().toLocalDate().plusWeeks(expiresAfter);
                    break;
            }
            subscriptionStartDate = currentDate.plusDays(renewalDays);
            renewableSubscription.setSubscriptionStartDate(Timestamp.valueOf(subscriptionStartDate.atStartOfDay()));
            renewableSubscription.setSubscriptionEndDate(Timestamp.valueOf(subscriptionEndDate.atStartOfDay()));
            renewableSubscription.setNextBillingDate(Timestamp.valueOf(nextBillingDate.atStartOfDay()));
        }
        catch(Exception e){
            subscriptionUtility.updateErrLog("Could not update renewal dates for subscription:"+ subscription.getSubscriptionId(),errorLogRepository);
        }
        return renewableSubscription;
    }

    /**
     * This method will create a new subscription based on current subscription
     * @param currentSubscription
     * @return subscription
     */
    private Subscription populateSubscription(Subscription currentSubscription){
        Subscription renewedSubscription =new Subscription();
        renewedSubscription.setIsActive(1);
        renewedSubscription.setTransactionReasonCode(ApplicationConstants.TransactionReasonCode.RENEWAL.toString());
        renewedSubscription.setSubscriptionId(subscriptionUtility.generateSubscriptionIdForRenewal(currentSubscription));
        renewedSubscription.setStatus("ACTIVE");

        renewedSubscription.setActivationDate(new Timestamp(System.currentTimeMillis()));
        renewedSubscription.setOrderSourceCode(currentSubscription.getOrderSourceCode());
        renewedSubscription.setAutorenew(ApplicationConstants.Autorenew.AUTORENEW_ON.getAutoRenewStatus());
        Optional<SubscriptionRatePlan> subscriptionRatePlanOptional = currentSubscription.getSubscriptionRatePlans().stream().findFirst();
        if(!subscriptionRatePlanOptional.isPresent()){
            updateRenewalBatchLog(currentSubscription.getSubscriptionId(),"Subscription Rate plan is not present for subscription" ,"FAILURE");
        }
        renewedSubscription = updateRenewalDates(currentSubscription, renewedSubscription, subscriptionRatePlanOptional.get());
        Set<SubscriptionRatePlan> subscriptionRatePlans = getSubscriptionRatePlan(currentSubscription);
        renewedSubscription.setSubscriptionRatePlans(subscriptionRatePlans);
        return renewedSubscription;
    }
    /**
     * Creates the susbcription rate plan transaction for the subscription
     * @param currentSubscription
     * @return subscription rate plan transaction
     */
    private Set<SubscriptionRatePlan> getSubscriptionRatePlan(Subscription currentSubscription){
        Set<SubscriptionRatePlan> renewableSubscriptionRatePlans = new HashSet<>();
        Set<SubscriptionRatePlan> currentSubRatePlans = currentSubscription.getSubscriptionRatePlans();
        if(currentSubRatePlans==null)
            updateRenewalBatchLog(currentSubscription.getSubscriptionId(), "sub rate plan not configured","FAILURE");
        if(currentSubRatePlans.size()==1){
            SubscriptionRatePlan currentSubRatePlan = currentSubRatePlans.stream().findFirst().get();
            renewableSubscriptionRatePlans.add(createSubRatePlan(currentSubRatePlan,currentSubscription.getSubscriptionId()));
            return renewableSubscriptionRatePlans;
        }
        else{
            renewableSubscriptionRatePlans.addAll(currentSubRatePlans.stream()
                    .map(subRatePlan-> createSubRatePlan(subRatePlan,currentSubscription.getSubscriptionId()))
                    .collect(Collectors.toSet()));
            return renewableSubscriptionRatePlans;
        }
    }

    /**
     * This will create a subscription rate plan for the renewed subscription
     * @param currentSubRatePlan
     * @return subscription rate plan
     */
    private SubscriptionRatePlan createSubRatePlan(SubscriptionRatePlan currentSubRatePlan, String subscriptionId){
        SubscriptionRatePlan subRatePlan = new SubscriptionRatePlan();
        PricingScheme pricingScheme = currentSubRatePlan.getPricingScheme();
        subRatePlan.setBillingCycle(currentSubRatePlan.getBillingCycle());
        subRatePlan.setBillingFrequency(currentSubRatePlan.getBillingFrequency());
        RatePlan ratePlan = currentSubRatePlan.getRatePlan();
        subRatePlan.setRatePlan(ratePlan); //Added By: Vinay

        subRatePlan.setProduct(currentSubRatePlan.getProduct());
        subRatePlan.setPricingScheme(pricingScheme);
        subRatePlan.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        subRatePlan.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        int quantity = currentSubRatePlan.getQuantity();
        Long ratePlanUid = ratePlan.getId();
        if(VOLUME.equals(pricingScheme.getPricingSchemeCode())){
            if(quantity<0){
                updateRenewalBatchLog(subscriptionId,"Subscription could not be renewed as quantity is less than 0 ","FAILURE");
                return subRatePlan;
            }
            List<RatePlanVolume> ratePlanVolumes = ratePlanVolumeRepository.findByRatePlan(ratePlanUid);
            if(ratePlanVolumes == null || ratePlanVolumes.size()==0){
                updateRenewalBatchLog(subscriptionId,"Subscription could not be renewed as Rate Plan Volume is not configured for the Rate Plan:"+ratePlan.getRatePlanId(),"FAILURE");
                return subRatePlan;
            }
            RatePlanVolume ratePlanVolume = null;
            for(RatePlanVolume rpv: ratePlanVolumes){
                ratePlanVolume = rpv;
                if(rpv.getStartQty()< quantity && rpv.getEndQty()> quantity){
                    double price = rpv.getPrice()* quantity;
                    subRatePlan.setPrice(price);
                    subRatePlan.setRatePlanVolume(rpv);
                    return subRatePlan;
                }
            }
            //if qty is greater than the highest tier than default to highest tier price
            subRatePlan.setPrice(quantity * ratePlanVolume.getPrice());
            subRatePlan.setRatePlanVolume(ratePlanVolume);
        }
        else if(UNIT.equals(pricingScheme.getPricingSchemeCode())){
            if(quantity<0){
                updateRenewalBatchLog(subscriptionId,"Subscription could not be renewed as quantity is less than 0", "FAILURE");
                //update err
                return subRatePlan;
            }
            double price = ratePlan.getPrice()* quantity;
            subRatePlan.setPrice(price);
        }
        subRatePlan.setQuantity(currentSubRatePlan.getQuantity());
        return subRatePlan;
    }

    /**
     * This will update the existing subscription and mark it appropriately for customer
     * @param nonrenewableCustomer
     * @return
     */
    private Customer updateNonRenewableCustomer(Customer nonrenewableCustomer){
        try{
            Set<Subscription> notToAutorenewSubscriptions = nonrenewableCustomer.getSubscriptions().stream()
                    .map(subscription -> {
                        subscription.setStatus(BillingInvoicing.INACTIVE.toString());
                        subscription.setTransactionReasonCode(ApplicationConstants.TransactionReasonCode.EXPIRED.toString());
                        subscription.setIsActive(0);
                        return subscription;
                    }).collect(Collectors.toSet());
            nonrenewableCustomer.setSubscriptions(notToAutorenewSubscriptions);
            return nonrenewableCustomer;
        }
        catch(Exception ex){
            SubscriptionUtility.updateErrLog("Exception occurred while auto renew for customer Id:"+nonrenewableCustomer.getId(),errorLogRepository);
            return nonrenewableCustomer;
        }

    }

    /**
     * This method will check if any renewal notifications are to be sent or not.
     * @param customers
     */
    private Map<String,Set<Customer>> sendAutoRenewalNotifications(List<Customer> customers) {

        int maxNotifications = filterNotification(notificationsConstant, numOfNotification);
        int notificationTriggerDays = filterNotification(notificationsConstant, notificationTrigger);
        int notificationsInterval = notificationTriggerDays / maxNotifications;
        Set<Customer> notifiedCustomerSet = new HashSet<>();
        Set<Customer> autorenewCustomerSet = new HashSet<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate nextNotificationDate = currentDate.plusDays(notificationsInterval);
        for (Customer customer : customers) {
            Set<Subscription> subscriptions = customer.getSubscriptions();
            for (Subscription subscription : subscriptions) {
                Notification notification = notificationRepository.findBySubscriptionId(subscription.getSubscriptionId());;
                //if the subscription is due for auto renewal and first notification should be sent
                if (subscription.getSubscriptionEndDate().toLocalDateTime().toLocalDate().minusDays(notificationTriggerDays).equals(currentDate) && notification==null) {
                    //send notification
                    //Notification notification = notificationRepository.findBySubscriptionId(subscription.getSubscriptionId());
                    notification = new Notification();
                    notification.setSubscriptionId(subscription.getSubscriptionId());
                    notification.setNotificationsSentCount(1);
                    notification.setLastNotificationDate(new Timestamp(System.currentTimeMillis()));
                    notification.setNextNotificationDate(Timestamp.valueOf(nextNotificationDate.atStartOfDay()));
                    notifiedCustomerSet.add(customer);
                    notificationRepository.save(notification);
                } else {
                    notification = notificationRepository.findBySubscriptionId(subscription.getSubscriptionId());
                    if(notification==null)
                        break;
                    int count = notification.getNotificationsSentCount();
                    //if(count>=0 && count<maxNotifications && notification.getNextNotificationDate().toLocalDateTime().toLocalDate().equals(currentDate)){
                    if (count >= 0 && count < maxNotifications && notification.getNextNotificationDate().toLocalDateTime().toLocalDate().equals(currentDate)) {
                        //if(count>=0 && count<maxNotifications && currentDate.equals(currentDate)){
                        //send notification
                        notification.setNotificationsSentCount(count + 1);
                        notification.setLastNotificationDate(new Timestamp(System.currentTimeMillis()));
                        nextNotificationDate = currentDate.plusDays(notificationsInterval);
                        notification.setNextNotificationDate(Timestamp.valueOf(nextNotificationDate.atStartOfDay()));
                        notifiedCustomerSet.add(customer);
                        notificationRepository.save(notification);
                    } else {
                        //checking if subscription is already renewed then skip it
                        if (!subscription.getTransactionReasonCode().contains("EXPIRED") && notification != null && notification.getNextNotificationDate().toLocalDateTime().toLocalDate().equals(currentDate)) {
                            //if(!subscription.getTransactionReasonCode().contains("EXPIRED") && currentDate.equals(currentDate)){
                            // already max number of notifications have been sent
                            autorenewCustomerSet.add(customer);
                        }
                    }
                }
            }
        }
        Map<String, Set<Customer>> customerMap = new HashMap<>();
        customerMap.put("notifiedCustomers", notifiedCustomerSet);
        customerMap.put("autorenewCustomers", autorenewCustomerSet);
        return customerMap;
    }

    /**
     * This method will fetch value based on AppconstantMaster
     * @param notificationsConstant
     * @param predicate
     * @return
     */
    private int filterNotification(List<AppConstantMaster> notificationsConstant, Predicate<AppConstantMaster> predicate){
        String subAppConstantCode = notificationsConstant.stream().filter(predicate).findFirst().get().getSubAppConstantCode();
        return Integer.valueOf(subAppConstantCode);
    }

    private void updateRenewalBatchLog(String subscriptionId, String msg, String statusFlag){
        RenewalBatchLog batchLog = new RenewalBatchLog();
        batchLog.setErrorDesc(msg);
        batchLog.setStatus(STATUS_SUCCESS.equalsIgnoreCase(statusFlag)?STATUS_SUCCESS:STATUS_FAIL);
        batchLog.setRunDownDate(runDownBatch);
        batchLog.setSubscriptionId(subscriptionId);
        batchLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        batchLog.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        renewalBatchLogRepository.save(batchLog);
    }

    private void getNotificationConfig(){
        notificationsConstant = constantRepository.findByAppConstantCodeContaining("NOTIFICATION");
        numOfNotification = notification-> notification.getAppConstantCode().equals("NUMBER_OF_NOTIFICATION");
        notificationTrigger =notification-> notification.getAppConstantCode().equals("NOTIFICATION_TRIGGER_DAYS");
    }

}
