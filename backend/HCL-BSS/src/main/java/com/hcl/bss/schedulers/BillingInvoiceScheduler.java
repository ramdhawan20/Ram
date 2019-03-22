package com.hcl.bss.schedulers;

import com.hcl.bss.constants.ApplicationConstants;
import com.hcl.bss.domain.Subscription;
import com.hcl.bss.domain.SubscriptionRatePlan;
import com.hcl.bss.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Autowired
    public SubscriptionRepository subscriptionRepository;

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
        LocalDate subNextBillingDate = null;
        Set<SubscriptionRatePlan> subRatePlans = subscription.getSubscriptionRatePlan();
        Optional<SubscriptionRatePlan> subscriptionRatePlanOptional = subRatePlans.stream().findFirst();
        SubscriptionRatePlan subRatePlan = subscriptionRatePlanOptional.get();
        String billingFrequency = subRatePlan.getRatePlan().getFrequencyCode();
        int billingCycleTerm = subRatePlan.getRatePlan().getBillingCycleTerm();
        switch (billingFrequency) {
            case "WEEKLY":
                subNextBillingDate = currentDate.plusWeeks(billingCycleTerm).plusDays(2);
                break;
            case "MONTHLY":
                subNextBillingDate = currentDate.plusMonths(billingCycleTerm).plusDays(2);
                break;
            case "ANNUAL":
                subNextBillingDate = currentDate.plusYears(billingCycleTerm).plusDays(2);
                break;
        }
        if (subscription.getLastBillingDate() != null) {
            subLastBillingDate = subscription.getLastBillingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            //if (currentDate.isAfter(subLastBillingDate) && currentDate.plusDays(2).equals(subscription.getNextBillingDate())) {
            if (currentDate.isAfter(subLastBillingDate) && currentDate.plusDays(2).equals(subscription.getNextBillingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
                Date nextBillingDate = Date.from(subNextBillingDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                subscription.setLastBillingDate(subscription.getNextBillingDate());
                subscription.setNextBillingDate(nextBillingDate);
                return true;
            }
            return false;
        } else {
            subscription.setLastBillingDate(subscription.getNextBillingDate());
            Date nextBillingDate = Date.from(subNextBillingDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            subscription.setNextBillingDate(nextBillingDate);
            return true;
        }
    }
}
