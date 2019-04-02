
package com.hcl.bss.util;


import com.hcl.bss.domain.*;
import com.hcl.bss.repository.ErrorLogRepository;
import com.hcl.bss.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hcl.bss.constants.ApplicationConstants.STATUS_SUCCESS;

/**
 * Utility class to be used for subscription creation and auto renewal
 */

@Component
public class SubscriptionUtility {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private ErrorLogRepository errorLogRepository;

    /**
     * This method will generate the subscription id based on the order
     * @param order
     * @return subscriptionId
     */
    public String generateSubscriptionId(Order order){
        try{
            Product product = entityManager.find(Product.class,order.getProductId());
            String sku = product.getSku();
            RatePlan ratePlan = entityManager.find(RatePlan.class,order.getRatePlanId());
            String ratePlanName = ratePlan.getRatePlanId();
            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            StringBuilder builder = new StringBuilder();
            int sequenceNumber = subscriptionRepository.getSubsSeq();
            builder.append(sku).append(ratePlanName).append(month).append(year).append(sequenceNumber);
            return builder.toString();
        }
        catch(Exception ex){
            updateErrLog("Exception occurred while generating subscriptionID:"+ ex.getMessage(), errorLogRepository);
            return null;
        }
    }

    /**
     * This method will generate the subscription id during renewal
     * @param subscription
     * @return subscriptionId
     */
    public String generateSubscriptionIdForRenewal(Subscription subscription){
        try{
            Set<SubscriptionRatePlan> subRatePlans = subscription.getSubscriptionRatePlans();
            List<Long> products = subRatePlans.stream()
                                                    .map(sRatePlan->sRatePlan.getRatePlan().getProducts())
                                                    .flatMap(prods -> prods.stream())
                                                    .filter(prod->prod.getParent()==null).map(p->p.getUidpk())
                                                    .collect(Collectors.toList());
            //Set<Product> products = subRatePlan.getRatePlan().getProducts();

//            Product product = products.stream().findFirst().get();
            String sku="";
            String ratePlanName="";
            for(SubscriptionRatePlan subRatePlan: subRatePlans){
                if(products.contains(subRatePlan.getProduct())){
                //if(subRatePlan.getProduct().equals(product.getUidpk())){
                    ratePlanName=subRatePlan.getRatePlan().getRatePlanId();
                    sku= subRatePlan.getRatePlan().getProducts().stream().filter(pro->pro.getUidpk().equals(subRatePlan.getProduct())).map(pro->pro.getSku()).findFirst().get();
                    break;
                }
            }
            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            StringBuilder builder = new StringBuilder();
            int sequenceNumber = subscriptionRepository.getSubsSeq();
            builder.append(sku).append(ratePlanName).append(month).append(year).append(sequenceNumber);
            return builder.toString();
        }
        catch(Exception ex){
            updateErrLog("Exception occurred during renewal while generating subscriptionID :"+ ex.getMessage(), errorLogRepository);
            return null;
        }
    }
    public static void updateErrLog(String msg, ErrorLogRepository errorLogRepository){
        ErrorLog errLog = new ErrorLog();
        errLog.setDescription(msg);
        errLog.setOperation("To be setup");
        errLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        errLog.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        errorLogRepository.save(errLog);
    }
}

