package com.hcl.bss.schedulers;

import com.hcl.bss.domain.*;
import com.hcl.bss.repository.*;
import com.hcl.bss.util.SubscriptionUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import static com.hcl.bss.constants.ApplicationConstants.*;

/**
 * This class will run a scheduler to pick data
 * from temp table to create subscriptions
 *
 * @author- Aditya gupta
 */
@Component
public class SubscriptionScheduler {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ErrorLogRepository orderErrorsRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private RatePlanVolumeRepository ratePlanVolumeRepository;

    @Autowired
    private SubscriptionUtility subscriptionUtility;
/*
    @Autowired
    private ErrorLogRepository errorLogRepository;*/

    @Autowired
    private BatchLogRepository batchLogRepository;

    private Long companyId;

    private Product product;
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    private Long billToId;

    public void setBillId(Long billToId) {
        this.billToId = billToId;
    }
    private Long soldToId;

    public void setSoldId(Long soldToId) {
        this.soldToId = soldToId;
    }

    private Timestamp runDownBatch;

    @Scheduled(cron="0 0 0 * * ?")
    public void runSubscriptionBatch(){
        //1.read the data from the temp table
        //2. persist into db for creating subscription
        //3. update the table for isSuccess
        readData();
    }

    private void readData(){
        runDownBatch = new Timestamp(System.currentTimeMillis());
        List<Order> orders = orderRepository.findAll();
        for(Order order: orders){
            if(STATUS_SUCCESS.equals(order.getStatus()) || FAIL_STATUS.equals(order.getStatus())){
                continue;
            }
            else {
                validateOrderData(order);
                Subscription subscription = createSubscription(order);
                //boolean isAddOnProduct = order.getParentId()!=null?true:false;
                boolean isAddOnProduct = product.getParent()!=null?true:false;
                if(!isAddOnProduct)
                    createCustomerAccount(order, subscription);
                else
                    subscriptionRepository.save(subscription);
                reset();
            }
        }
    }

    @Transactional(rollbackOn = {Exception.class})
    private void createCustomerAccount(Order order, Subscription subscription){
        try{
            Customer customer= null;
            //check if subscription is for the same order
            //i.e. multiple subscription for same order
            List<BatchLog> batchLogs = batchLogRepository.findByOrderNumber(order.getOrderNumber());

            //dont create new customer and assign the subscription to the existing customer
            //get the subscription from order and get the customer Id from there

            if(batchLogs!=null && batchLogs.size()>0){
                for(BatchLog log : batchLogs) {
                    if (log.getSubscriptionId() == null)
                        continue;
                    Subscription sub = subscriptionRepository.findBySubscriptionId(log.getSubscriptionId());
                    if (sub != null && subscription!=null) {
                        customer = customerRepository.findBySubscriptions(sub);
                        Set<Subscription> subscriptions = new HashSet<>();
                        subscriptions.add(subscription);
                        customer.setSubscriptions(subscriptions);
                        //subscription.setCustomer(customer);
                        break;
                    }
                }
            }
            else{
                customer = new Customer();
                if(order.getIsCorporate()==1){
                    createCompany(order);
                    customer.setCompanyId(companyId);
                }
                else{
                    //individual customer
                    persistAddress(order);
                }
                customer.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                customer.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                customer.setFirstName(order.getBillToFirstName());
                customer.setLastName(order.getBillToLastName());
                customer.setEmail(order.getBillToEmail());
                customer.setPhone(order.getBillToPhone());
                customer.setBillTo(billToId);
                customer.setSoldTo(soldToId);
                Set<Subscription> subscriptions = new HashSet<>();
                subscriptions.add(subscription);
                customer.setSubscriptions(subscriptions);
            }
            if(!FAIL_STATUS.equals(order.getStatus())){
                customerRepository.save(customer);
                order.setStatus(STATUS_SUCCESS);
                orderRepository.save(order);
                updateBatchLog(order,"-", subscription.getSubscriptionId());
            }
        }
        catch(Exception ex){
            SubscriptionUtility.updateErrLog("Customer could not be created : "+ex.getMessage(), orderErrorsRepository);
            ex.printStackTrace();
        }
    }

    private void createCompany(Order order){
        try{
            Company company = new Company();
            company.setCompanyName(order.getCompanyName());
            persistAddress(order);
            company.setBillToId(billToId);
            company.setSoldToId(soldToId);

            company.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            company.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            Company comp = companyRepository.save(company);
            companyId = comp.getId();

        }
        catch(Exception ex){
            SubscriptionUtility.updateErrLog("Company could not be created : "+ex.getMessage(),orderErrorsRepository);
        }
    }


    private void persistAddress(Order order){
        try{
            Address billToAddr = new Address();
            Address soldToAddr = new Address();
            billToAddr.setLine1(order.getBillToAddrLine1());
            billToAddr.setLine2(order.getBillToAddrLine2());
            billToAddr.setCity(order.getBillToCity());
            billToAddr.setState(order.getBillToState());
            billToAddr.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            billToAddr.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            Country country = entityManager.find(Country.class,order.getBillToCountry());
            if(country==null){
                updateOrder(order, "Country: "+ order.getBillToCountry() + " is not configured");
                return;
            }
            billToAddr.setCountry(country);

            soldToAddr.setLine1(order.getSoldToAddrLine1());
            soldToAddr.setLine2(order.getSoldToAddrLine2());
            soldToAddr.setCity(order.getSoldToCity());
            soldToAddr.setState(order.getSoldToState());
            soldToAddr.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            soldToAddr.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            soldToAddr.setCountry(country);

            billToAddr = addressRepository.save(billToAddr);
            soldToAddr = addressRepository.save(soldToAddr);
            billToId = billToAddr.getId();
            soldToId = soldToAddr.getId();
        }
        catch(Exception ex){
            updateOrder(order,"Bill To and Sold To could not be persisted due to:"+ ex.getMessage());
        }
    }

    /**
     * This method will validate the order data
     * @param order
     */
    private void validateOrderData(Order order){
        validateProduct(order);
        validateRatePlan(order);
        validatePriceQuantity(order);
    }

    /**
     * This method will create subscription
     * @param order
     */
    @Transactional(rollbackOn = {Exception.class})
    private Subscription createSubscription(Order order){
        try {
            Long productId = order.getProductId();
            //Long parentId = null;
            /*if(productId!=null){
                Optional<Product> productOptional = productRepository.findById(productId);
                Product currentProduct = productOptional.get();
                parentId = currentProduct.getParent();
            }*/
            Product parentProduct = product.getParent();
            /**
             * if the product is a base product or a bundle product
             */
            if(parentProduct==null){

                return populateSubscription(order);
            }
            /**
             * check if the product is an addon product
             */
            else if(parentProduct!=null){
                List<BatchLog> batchLogs = batchLogRepository.findByOrderNumber(order.getOrderNumber());
                if(batchLogs!=null && batchLogs.size()>0){
                    for(BatchLog log : batchLogs) {
                        if (log.getSubscriptionId() == null)
                            continue;
                        Subscription sub = subscriptionRepository.findBySubscriptionId(log.getSubscriptionId());
                        SubscriptionRatePlan subscriptionRatePlan = createSubscriptionRatePlan(order);
                        Set<SubscriptionRatePlan> subRatePlans = new HashSet<>();
                        subRatePlans.add(subscriptionRatePlan);
                        sub.setSubscriptionRatePlans(subRatePlans);
                        return sub;
                    }
                }
            }
            /**
             * check if the product is bundle product
             */
           /* else if(order.getBundleId()!=null){
                //if()
                return populateSubscription(order);
            }*/
            return null;
        }
        catch(Exception ex){
            updateOrder(order,"Subscription could not be created "+ex.getMessage());
            return null;
        }
    }

    private Subscription populateSubscription(Order order) {
        Subscription subscription = new Subscription();
        OrderSource orderSource = entityManager.find(OrderSource.class, order.getOrderSourceCode());
        if (orderSource != null)
            subscription.setOrderSourceCode(orderSource.getOrderSourceCode());
        else {
            updateOrder(order,"Order Source: "+order.getOrderSourceCode() + " is not configured");
        }

        boolean isBundledProduct = verifyBundleProduct(order.getProductId());
        if(isBundledProduct){

        }
        else{

        }

        //creating subscription rate plan
        Set<SubscriptionRatePlan> subscriptionRatePlans = new HashSet<>();
        SubscriptionRatePlan subRatePlan = createSubscriptionRatePlan(order);
        subscriptionRatePlans.add(subRatePlan);
        subscription.setSubscriptionRatePlans(subscriptionRatePlans);


        subscription.setAutorenew(order.getAutoRenew());
        subscription.setIsActive(1);
        // to be discussed --where to get this info from
        String transactionCode = "NEW";
        subscription.setTransactionReasonCode(transactionCode);
        subscription.setSubscriptionStartDate(new Timestamp(System.currentTimeMillis()));
        // to be discussed --where to get this info from
        subscription.setStatus("ACTIVE");
        Calendar cal = Calendar.getInstance();
        //Date today = cal.getTime();
        Integer billCycle = order.getBillingCycle();
        String billFrequency = order.getBillingFrequency();


        BigDecimal expireAfter = subRatePlan.getRatePlan().getExpireAfter();
        String frequencyCode = subRatePlan.getRatePlan().getBillingFrequency();
        BigDecimal billingCycleTerm = subRatePlan.getRatePlan().getBillingCycleTerm();
        /*int expireAfter = subRatePlan.getRatePlan().getExpireAfter();
        String frequencyCode = subRatePlan.getRatePlan().getFrequencyCode();
        int billingCycleTerm = subRatePlan.getRatePlan().getBillingCycleTerm();*/
        int subEndTerm = expireAfter.intValue()*billingCycleTerm.intValue();

        // check for evergreen subscription
        if(billCycle==null || billFrequency == null || "".equals(billFrequency)){
            //cal.set(9999,11,31);
            //subscription.setSubscriptionEndDate(new Timestamp(cal.getTimeInMillis()));
        }
        else{
            LocalDate subscriptionEndDate = null;
            LocalDate currentDate = LocalDate.now();
            LocalDate nextBillingDate = null;
            switch(billFrequency){
                case "WEEK":
                    //cal.add(Calendar.WEEK_OF_YEAR, billCycle);
                    nextBillingDate = currentDate.plusWeeks(billingCycleTerm.longValue());
                    //nextBillingDate = currentDate.plusWeeks(billingCycleTerm);
                    subscriptionEndDate = currentDate.plusWeeks(subEndTerm);
                    break;
                case "MONTH":
                    //cal.add(Calendar.MONTH, billCycle);
                    nextBillingDate = currentDate.plusMonths(billingCycleTerm.longValue());
                    //nextBillingDate = currentDate.plusMonths(billingCycleTerm);
                    subscriptionEndDate = currentDate.plusMonths(subEndTerm);
                    break;
                case "YEAR":
                    //cal.add(Calendar.YEAR, billCycle);
                    nextBillingDate = currentDate.plusYears(billingCycleTerm.longValue());
                    //nextBillingDate = currentDate.plusYears(billingCycleTerm);
                    subscriptionEndDate = currentDate.plusYears(subEndTerm);
                    break;
            }
            subscription.setSubscriptionEndDate(Timestamp.valueOf(subscriptionEndDate.atStartOfDay()));
            subscription.setNextBillingDate(Timestamp.valueOf(nextBillingDate.atStartOfDay()));
            //if(expireAfter.intValue() == 9999){
            if(expireAfter.intValue() == 9999){
                cal.set(9999,11,31);
                subscription.setSubscriptionEndDate(new Timestamp(cal.getTimeInMillis()));
            }
        }

        subscription.setActivationDate(new Timestamp(System.currentTimeMillis()));

        String subscriptionId =subscriptionUtility.generateSubscriptionId(order);
        if(subscriptionId!=null)
            subscription.setSubscriptionId(subscriptionId);

        //SubscriptionRatePlan subscriptionRatePlan = createSubscriptionRatePlan(order);
        //subscription.setSubscriptionRatePlan(subscriptionRatePlan);

        return subscription;
    }

    /**
     * Creates the susbcription rate plan transaction for the subscription
     * @param order
     * @return subscription rate plan transaction
     */
    private SubscriptionRatePlan createSubscriptionRatePlan(Order order){
        SubscriptionRatePlan subRatePlan = new SubscriptionRatePlan();
        subRatePlan.setBillingCycle(order.getBillingCycle());
        subRatePlan.setQuantity(order.getQuantity());
        PricingScheme pricingScheme = entityManager.find(PricingScheme.class, order.getPricingSchemeCode());
        if(pricingScheme== null){
            updateOrder(order, "Pricing Scheme Code: "+order.getPricingSchemeCode()+ " is not configured");
        }
        else{
            subRatePlan.setBillingCycle(order.getBillingCycle());
            subRatePlan.setBillingFrequency(order.getBillingFrequency());
            RatePlan ratePlan = entityManager.find(RatePlan.class, order.getRatePlanId());
            //subRatePlan.setRatePlan(ratePlan.getId()); ////Commented By: Vinay
            subRatePlan.setRatePlan(ratePlan); //Added By: Vinay
            subRatePlan.setProduct(order.getProductId());
            subRatePlan.setPricingScheme(pricingScheme);
            subRatePlan.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            subRatePlan.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            int quantity = order.getQuantity();
            Long ratePlanUid = ratePlan.getId();
            if(VOLUME.equals(pricingScheme.getPricingSchemeCode())){
                if(quantity<0){
                    updateOrder(order, order.getQuantity()+ " is less than zero");
                    return subRatePlan;
                }

                List<RatePlanVolume> ratePlanVolumes = ratePlanVolumeRepository.findByRatePlan(ratePlanUid);
                if(ratePlanVolumes == null || ratePlanVolumes.size()==0){
                    updateOrder(order, "Rate Plan Volume is not configured for the Rate Plan:"+ratePlan.getRatePlanId());
                    return subRatePlan;
                }
                RatePlanVolume ratePlanVolume = null;
                for(RatePlanVolume rpv: ratePlanVolumes){
                    ratePlanVolume = rpv;
                    if(rpv.getStartQty()< quantity && rpv.getEndQty()> quantity){
                        double price = rpv.getPrice()* quantity;
                        subRatePlan.setPrice(price);
                        subRatePlan.setRatePlanVolume(rpv);
                        //break;
                        return subRatePlan;
                    }
                }
                //if qty is greater than the highest tier than default to highest tier price
                subRatePlan.setPrice(quantity * ratePlanVolume.getPrice());
                subRatePlan.setRatePlanVolume(ratePlanVolume);
            }
            else if(UNIT.equals(pricingScheme.getPricingSchemeCode())){
                if(quantity<0){
                    updateOrder(order, order.getQuantity()+ " is less than zero");
                    return subRatePlan;
                }
                double price = ratePlan.getPrice()* quantity;
                subRatePlan.setPrice(price);
                //return subRatePlan;
            }
        }
        return subRatePlan;
    }

    /**
     * This method checks if the product present in Order is configured in the product table
     * @param order
     */
    private boolean validateProduct(Order order){
        Optional<Product> productOptional = productRepository.findById(order.getProductId());
        if(!productOptional.isPresent()){

            return updateOrder(order, "Product:"+ order.getProductId()+ " is not configured");
            //return updateOrder(order, "Product:", order.getProductId());
        }
        System.out.println("Product "+ productOptional.get().getProductDispName()+" is present");
        // product is found
        product = productOptional.get();
        return true;

    }

    private boolean updateOrder(Order order, String str) {
        order.setStatus(FAIL_STATUS);
        BatchLog error = updateBatchLog(order, str);
        batchLogRepository.save(error);
        orderRepository.save(order);
        return false;
    }

    /**
     * This method will add entry in batch_run_log for a given orderId
     * @param order
     * @param msg
     * @return BatchLog
     */
    private void updateBatchLog(Order order, String msg, String subscriptionId){

        BatchLog batchLog = new BatchLog();
        batchLog.setErrorDesc(msg);
        batchLog.setOrderNumber(order.getOrderNumber());
        batchLog.setStatus(STATUS_SUCCESS);
        batchLog.setRunDownDate(runDownBatch);
        batchLog.setSubscriptionId(subscriptionId);
        batchLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        batchLog.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        batchLogRepository.save(batchLog);
        //return batchLog;
    }

    /**
     * This method will add entry in batch_run_log for a given orderId
     * @param order
     * @param msg
     * @return BatchLog
     */
    private BatchLog updateBatchLog(Order order, String msg){

        BatchLog batchLog = new BatchLog();
        batchLog.setErrorDesc(msg);
        batchLog.setOrderNumber(order.getOrderNumber());
        batchLog.setStatus(FAIL_STATUS);
        batchLog.setRunDownDate(runDownBatch);
        batchLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        batchLog.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        return batchLog;
    }
    /**
     * This method will check if the rateplan is configured in the database.
     * @param order
     * @return
     */
    private boolean validateRatePlan(Order order){
        RatePlan ratePlan = entityManager.find (RatePlan.class,order.getRatePlanId());
        if(ratePlan==null){
            return updateOrder(order, "Rate plan:"+ order.getRatePlanId()+ " is not configured");
        }
        else if(ratePlan.getIsActive()== 0){
            return updateOrder(order, "Rate plan:"+ order.getRatePlanId()+ " is inactive");
        }
        //System.out.println("Rateplan "+ ratePlan.getRatePlanId()+" is present");
        return true;
    }

    /**
     * This method will check if the price and quantity columns are populated
     * @param order
     * @return
     */
    private boolean validatePriceQuantity(Order order){
        PricingScheme pricingScheme = entityManager.find(PricingScheme.class, order.getPricingSchemeCode());
        int qty = order.getQuantity();
        if(qty<0){
            BatchLog error = updateBatchLog(order,"Qty:"+qty + " should be greater than zeo");
            batchLogRepository.save(error);
        }

        if(pricingScheme==null){
            return updateOrder(order,"Pricing Scheme:"+ order.getPricingSchemeCode()+" is not configured" );
        }
        return true;
    }

    /**
     * This method will reset the ids for next record processing
     */
    private void reset(){
        billToId = 0L;
        soldToId = 0L;
        companyId = 0L;
    }

    private boolean verifyBundleProduct(Long productId){
        return product.getIsBundleProduct()==1? true:false;
    }
}