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
    private OrderErrorsRepository orderErrorsRepository;

    @Autowired
    private RatePlanRepository ratePlanRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private RatePlanVolumeRepository ratePlanVolumeRepository;

    @Scheduled(cron="0 0 0 * * ?")
    public void runSubscriptionBatch(){
        //1.read the data from the temp table
        //2. persist into db for creating subscription
        //3. update the table for isSuccess
        readData();
    }


    private static Map<String,Long> dataMap = new HashMap<>();



    private void readData(){
        List<Order> orders = orderRepository.findAll();
/*        orders.forEach(order -> {
                //createCustomerAccount(order);
                createSubscription(order);
            });*/

        for(Order order: orders){
            if("Pass".equals(order.getStatus()) || "Fail".equals(order.getStatus())){
                break;
            }
            else {
                validateOrderData(order);
                Subscription subscription = createSubscription(order);
                createCustomerAccount(order, subscription);
            }
        }
    }
    @Transactional(rollbackOn = {Exception.class})
    private void createCustomerAccount(Order order, Subscription subscription){
        if(order.getIsCorporate()==1){
            createCompany(order);
        }
        Customer customer = new Customer();
        customer.setFirstName(order.getBillToFirstName());
        customer.setLastName(order.getBillToLastName());
        customer.setEmail(order.getBillToEmail());
        customer.setPhone(order.getBillToPhone());
        customer.setBillTo(dataMap.get("billTo"));
        customer.setSoldTo(dataMap.get("soldTo"));
        customer.setCompanyId(dataMap.get("companyId"));
        customer.setSubscriptions(subscription);
        if(!"Fail".equals(order.getStatus())){
            Customer cust = customerRepository.save(customer);
            order.setStatus("Pass");
            orderRepository.save(order);
        }

        //dataMap.put("customerId", cust.getId());
        //create customer directly
    }

    private void createCompany(Order order){
        Company company = new Company();
        company.setCompanyName(order.getCompanyName());
        //Map<String, Long> addressMap =
        persistAddress(order);
        company.setBillToId(dataMap.get("billTo"));
        company.setSoldToId(dataMap.get("soldTo"));
        Company comp = companyRepository.save(company);
        dataMap.put("companyId",comp.getId());
    }


    private Map<String,Long> persistAddress(Order order){
        Address billToAddr = new Address();
        Address soldToAddr = new Address();
        billToAddr.setLine1(order.getBillToAddrLine1());
        billToAddr.setLine2(order.getBillToAddrLine2());
        billToAddr.setCity(order.getBillToCity());
        billToAddr.setState(order.getBillToState());
        Country country = entityManager.getReference(Country.class,order.getBillToCountry());
        billToAddr.setCountry(country);

        soldToAddr.setLine1(order.getSoldToAddrLine1());
        soldToAddr.setLine2(order.getSoldToAddrLine2());
        soldToAddr.setCity(order.getSoldToCity());
        soldToAddr.setState(order.getSoldToState());
        soldToAddr.setCountry(country);

        Address ad1 = addressRepository.save(billToAddr);
        Address ad2 = addressRepository.save(soldToAddr);
        dataMap.put("billTo", ad1.getId());
        dataMap.put("soldTo", ad2.getId());
        return dataMap;
    }

    /**
     * This method will validate the order data
     * @param order
     */
    private void validateOrderData(Order order){
        /*boolean isProductConfigured = validateProduct(order);
        if(!isProductConfigured)
            return;
        boolean isRatePlanConfigured = validateRatePlan(order);
        if(!isRatePlanConfigured)
            return;*/
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
            //Subscription subscription = new Subscription();
            Subscription subscription = new Subscription();
            OrderSource orderSource = entityManager.find(OrderSource.class, order.getOrderSourceCode());
            if (orderSource != null)
                subscription.setOrderSourceCode(orderSource.getOrderSourceCode());
            else {
                OrderErrors error = addOrderError(order, order.getOrderSourceCode() + " is not configured");
                order.setStatus("Fail");
                orderErrorsRepository.save(error);
                orderRepository.save(order);
            }
            subscription.setAutorenew(order.getAutoRenew());
            subscription.setIsActive(1);
            // to be discussed --where to get this info from
            String transactionCode = "NEW";
            subscription.setTransactionReasonCode(transactionCode);
            subscription.setSubscriptionStartDate(new Timestamp(System.currentTimeMillis()));
            // to be discussed --where to get this info from
            subscription.setStatus("ACTIVE");
            // to be discussed
            //subscription.setSubscriptionEndDate(order.getBillingCycle()+new Timestamp(System.currentTimeMillis()));
            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();
            cal.add(Calendar.YEAR, 1); // to get previous year add -1
            subscription.setSubscriptionEndDate(new Timestamp(cal.getTimeInMillis()));
            subscription.setActivationDate(new Timestamp(System.currentTimeMillis()));
            String subscriptionId = generateSubscriptionId(order);
            subscription.setSubscriptionId(subscriptionId);
            SubscriptionRatePlan subscriptionRatePlan = createSubscriptionRatePlan(order);
            subscription.setSubscriptionRatePlan(subscriptionRatePlan);
            return subscription;
        }
        catch(Exception ex){
            //TODO
        }
        return null;
    }


    private SubscriptionRatePlan createSubscriptionRatePlan(Order order){
        SubscriptionRatePlan subRatePlan = new SubscriptionRatePlan();
        subRatePlan.setBillingCycle(order.getBillingCycle());
        PricingScheme pricingScheme = entityManager.find(PricingScheme.class, order.getPricingSchemeCode());
        if(pricingScheme== null){
            OrderErrors error = addOrderError(order,order.getPricingSchemeCode()+ " is not configured");
            order.setStatus("Fail");
            orderErrorsRepository.save(error);
            orderRepository.save(order);
        }
        else{
            subRatePlan.setPrice(order.getTotalPrice());
            subRatePlan.setBillingCycle(order.getBillingCycle());
            subRatePlan.setBillingFrequency(order.getBillingFrequency());
            RatePlan ratePlan = entityManager.find(RatePlan.class, order.getRatePlanId());
            subRatePlan.setRatePlan(ratePlan.getId());
            subRatePlan.setProduct(order.getProductId());
            subRatePlan.setPricingScheme(pricingScheme);
            if("VOLUME".equals(pricingScheme.getPricingSchemeCode())){
                int quantity = order.getQuantity();
                if(quantity<0){
                    OrderErrors error = addOrderError(order,order.getQuantity()+ " is less than zero");
                    order.setStatus("Fail");
                    orderErrorsRepository.save(error);
                    orderRepository.save(order);
                    return subRatePlan;
                }
                Long ratePlanUid = ratePlan.getId();
                List<RatePlanVolume> ratePlanVolumes = ratePlanVolumeRepository.findByRatePlan(ratePlanUid);
                for(RatePlanVolume rpv: ratePlanVolumes){
                    if(rpv.getStartQty()< quantity && rpv.getEndQty()> quantity){
                        double price = rpv.getPrice()* quantity;
                        subRatePlan.setPrice(price);
                        subRatePlan.setRatePlanVolume(rpv);
                        break;
                    }
                }
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
            order.setStatus("Fail");
            OrderErrors error = addOrderError(order, "Product:"+order.getProductId()+" is not configured");
            orderErrorsRepository.save(error);
            orderRepository.save(order);
            return false;
        }
        System.out.println("Product "+ product.get().getProductName()+" is present");
        // product is found
        return true;

    }

    /**
     * This method will add the errors in the order error table for a given orderId
     * @param order
     * @param errorMsg
     * @return OrderErrors
     */
    private OrderErrors addOrderError(Order order, String errorMsg){

        OrderErrors errors = new OrderErrors();
        errors.setErrorDesc(errorMsg);
        errors.setOrder(order);
        return errors;
    }

    /**
     * This method will check if the rateplan is configured in the database.
     * @param order
     * @return
     */
    private boolean validateRatePlan(Order order){
        RatePlan ratePlan = entityManager.find (RatePlan.class,order.getRatePlanId());
        if(ratePlan==null){
            order.setStatus("Fail");
            OrderErrors error = addOrderError(order,"Rate plan:"+ order.getRatePlanId()+" is not configured");
            orderErrorsRepository.save(error);
            orderRepository.save(order);
            return false;
        }
        System.out.println("Rateplan "+ ratePlan.getRatePlanId()+" is present");
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
            OrderErrors error = addOrderError(order,"Qty:"+qty + " should be greater than zeo");
            orderErrorsRepository.save(error);
        }

        if(pricingScheme==null){
            order.setStatus("Fail");
            OrderErrors error = addOrderError(order,"Pricing Scheme:"+ order.getPricingSchemeCode()+" is not configured");
            orderErrorsRepository.save(error);
            orderRepository.save(order);
            return false;
        }
        return true;
    }

    private String generateSubscriptionId(Order order){
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


}
