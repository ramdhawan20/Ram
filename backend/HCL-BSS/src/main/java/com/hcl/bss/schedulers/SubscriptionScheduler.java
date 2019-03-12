package com.hcl.bss.schedulers;

import com.hcl.bss.domain.*;
import com.hcl.bss.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Timestamp;
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
    private ErrorLogRepository errorLogRepository;

    @Autowired
    private BatchLogRepository batchLogRepository;

    private Long companyId;

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


    //private static Map<String,Long> dataMap = new HashMap<>();



    private void readData(){
        runDownBatch = new Timestamp(System.currentTimeMillis());
        List<Order> orders = orderRepository.findAll();
/*        orders.forEach(order -> {
                //createCustomerAccount(order);
                createSubscription(order);
            });*/

        for(Order order: orders){
            if(STATUS_SUCCESS.equals(order.getStatus()) || FAIL_STATUS.equals(order.getStatus())){
                continue;
            }
            else {
                validateOrderData(order);
                Subscription subscription = createSubscription(order);
                createCustomerAccount(order, subscription);
                reset();
            }
        }
    }

/*    private void readData(){
        long numOfOrders = orderRepository.count();
        *//**
     * if numOfOrders <1000 ->threads to create 10
     *//*
        ExecutorService service = Executors.newFixedThreadPool(10);
    }*/

    @Transactional(rollbackOn = {Exception.class})
    private void createCustomerAccount(Order order, Subscription subscription){
        try{
            //check if subscription is for the same order
            //i.e. multiple subscription for same order
            List<Order> orders = orderRepository.findAll();
            //if(orders.contains(order) && orders.)
/*            long sameOrderCount = orders.stream().filter(ord -> ord.getOrderNumber()==order.getOrderNumber()).count();
            if(sameOrderCount >1){
                //dont create new customer and assign the subscrpition to the existing customer
                //get the subscription from order and get the customer Id from there

            }*/

            Customer customer = new Customer();
            if(order.getIsCorporate()==1){
                createCompany(order);
                //customer.setCompanyId(dataMap.get(COMPANY_ID));
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


            customer.setSubscriptions(subscription);
            if(!FAIL_STATUS.equals(order.getStatus())){
                Customer cust = customerRepository.save(customer);
                order.setStatus(STATUS_SUCCESS);
                orderRepository.save(order);
                updateBatchLog(order,"-", subscription);
            }
        }
        catch(Exception ex){
            updateErrLog("Customer could not be created : "+ex.getMessage());
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
            updateErrLog("Company could not be created : "+ex.getMessage());
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
            Subscription subscription = new Subscription();
            OrderSource orderSource = entityManager.find(OrderSource.class, order.getOrderSourceCode());
            if (orderSource != null)
                subscription.setOrderSourceCode(orderSource.getOrderSourceCode());
            else {
                updateOrder(order,"Order Source: "+order.getOrderSourceCode() + " is not configured");
            }
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

            // check for evergreen subscription
            if(billCycle==null || billFrequency == null || "".equals(billFrequency)){
                cal.set(9999,11,31);
                subscription.setSubscriptionEndDate(new Timestamp(cal.getTimeInMillis()));
            }
            else{
                switch(billFrequency){
                    case "WEEK":
                        cal.add(Calendar.WEEK_OF_YEAR, billCycle);
                        break;
                    case "MONTH":
                        cal.add(Calendar.MONTH, billCycle);
                        break;
                    case "YEAR":
                        cal.add(Calendar.YEAR, billCycle);
                        break;
                }
                subscription.setSubscriptionEndDate(new Timestamp(cal.getTimeInMillis()));
            }

            subscription.setActivationDate(new Timestamp(System.currentTimeMillis()));
            String subscriptionId = generateSubscriptionId(order);
            subscription.setSubscriptionId(subscriptionId);
            SubscriptionRatePlan subscriptionRatePlan = createSubscriptionRatePlan(order);
            subscription.setSubscriptionRatePlan(subscriptionRatePlan);
            return subscription;
        }
        catch(Exception ex){
            updateOrder(order,"Subscription could not be created "+ex.getMessage());
            return null;
        }
    }


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
            subRatePlan.setRatePlan(ratePlan.getId());
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
        Optional<Product> product = productRepository.findById(order.getProductId());
        if(!product.isPresent()){
            return updateOrder(order, "Product:"+ order.getProductId()+ " is not configured");
            //return updateOrder(order, "Product:", order.getProductId());
        }
        System.out.println("Product "+ product.get().getProductDispName()+" is present");
        // product is found
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
    private BatchLog updateBatchLog(Order order, String msg, Subscription subscription){

        BatchLog batchLog = new BatchLog();
        batchLog.setErrorDesc(msg);
        batchLog.setOrder(order);
        batchLog.setRunDownDate(runDownBatch);
        batchLog.setSubscription(subscription);
        //batchLog.set
        batchLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        batchLog.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        return batchLog;
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
        batchLog.setOrder(order);
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

    private String generateSubscriptionId(Order order){
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
            updateErrLog("Exception occured while generating subscriptionID:"+ ex.getMessage());
            return null;
        }
    }

    private void updateErrLog(String msg){
        ErrorLog errLog = new ErrorLog();
        errLog.setDescription(msg);
        errLog.setOperation("To be setup");
        errLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        errLog.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        errorLogRepository.save(errLog);
    }
    /**
     * This method will reset the ids for next record processing
     */
    private void reset(){
        billToId = 0L;
        soldToId = 0L;
        companyId = 0L;
    }

}
