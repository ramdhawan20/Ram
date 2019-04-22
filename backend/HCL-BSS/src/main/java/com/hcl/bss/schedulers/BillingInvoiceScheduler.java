package com.hcl.bss.schedulers;

import com.hcl.bss.constants.ApplicationConstants;
import com.hcl.bss.domain.Subscription;
import com.hcl.bss.domain.SubscriptionRatePlan;
import com.hcl.bss.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class will run a batch to check if the subscriptions are due for billing and change billing dates accordingly.
 * @Author- Aditya Gupta
 */

@Component
public class BillingInvoiceScheduler {

    @Value("${app.billing.invoice.days}")
    private int daysBeforeToBePicked;
    @Autowired
    public SubscriptionRepository subscriptionRepository;

    @Value("${app.subscription.renewal.days}")
    private int renewalDays;
    @Scheduled(cron="0 0 0 * * ?")
    public void updateBillingInvoice() {
        // get all the active subscriptions
        List<Subscription> subscriptions = subscriptionRepository.findByIsActive(ApplicationConstants.BillingInvoicing.ACTIVE.getActiveStatus());
        // filtering all the eligible subscriptions and changing the billing date
        List<Subscription> updatedSubscriptions = subscriptions.stream().filter(subscription -> getSubscriptionBillingDate(subscription)).collect(Collectors.toList());
        subscriptionRepository.saveAll(updatedSubscriptions);
    }

    /**
     * This will calculate the subscription is due for billing in the next2 days
     *
     * @param subscription
      * @return true/false
     */
    private boolean getSubscriptionBillingDate(Subscription subscription) {
        LocalDate currentDate = LocalDate.now();//LocalDate.of(2019,04,20);//
        LocalDate subLastBillingDate = null;
        Set<SubscriptionRatePlan> subRatePlans = subscription.getSubscriptionRatePlans();
        Optional<SubscriptionRatePlan> subscriptionRatePlanOptional = subRatePlans.stream().findFirst();
        SubscriptionRatePlan subRatePlan = subscriptionRatePlanOptional.get();
        String billingFrequency = subRatePlan.getRatePlan().getBillingFrequency();
        BigDecimal billingCycleTerm = subRatePlan.getRatePlan().getBillingCycleTerm();
        LocalDate nextBillingDate = subscription.getNextBillingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate renewalDate = subscription.getSubscriptionEndDate().toLocalDateTime().toLocalDate().minusDays(renewalDays);

        if(nextBillingDate.minusDays(daysBeforeToBePicked).equals(currentDate) && renewalDate.isAfter(nextBillingDate)){
            if (subscription.getLastBillingDate() != null) {
                subLastBillingDate = subscription.getLastBillingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (currentDate.isAfter(subLastBillingDate) && currentDate.plusDays(daysBeforeToBePicked).equals(subscription.getNextBillingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
                    //Date nextBillDate = Date.from(nextBillingDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    subscription.setLastBillingDate(subscription.getNextBillingDate());
                    subscription.setNextBillingDate(getNextBillingDate(billingCycleTerm,billingFrequency,nextBillingDate));
                    return true;
                }
            } else {
                subscription.setLastBillingDate(subscription.getNextBillingDate());
                Date nextBillDate = getNextBillingDate(billingCycleTerm,billingFrequency,nextBillingDate);
                subscription.setNextBillingDate(nextBillDate);
                return true;
            }
        }
        return false;
    }

    private Date getNextBillingDate(BigDecimal billingCycleTerm,String billingFrequency, LocalDate nextBillingDate){
        switch (billingFrequency) {
            case "WEEK":
                nextBillingDate = nextBillingDate.plusWeeks(billingCycleTerm.intValue());
                break;
            case "MONTH":
                nextBillingDate = nextBillingDate.plusMonths(billingCycleTerm.intValue());
                break;
            case "YEAR":
                nextBillingDate = nextBillingDate.plusYears(billingCycleTerm.intValue());
                break;
        }
        return Date.from(nextBillingDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
