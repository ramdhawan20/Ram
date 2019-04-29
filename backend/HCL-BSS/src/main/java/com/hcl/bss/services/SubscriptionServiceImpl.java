package com.hcl.bss.services;

import static com.hcl.bss.constants.ApplicationConstants.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hcl.bss.domain.Address;
import com.hcl.bss.domain.Customer;
import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.Subscription;
import com.hcl.bss.domain.SubscriptionRatePlan;
import com.hcl.bss.dto.AddressDto;
import com.hcl.bss.dto.CustomerDto;
import com.hcl.bss.dto.MultipleRatePlanDto;
import com.hcl.bss.dto.ResponseDto;
import com.hcl.bss.dto.SubscriptionDetailDto;
import com.hcl.bss.dto.SubscriptionDto;
import com.hcl.bss.dto.SubscriptionInOut;
import com.hcl.bss.dto.SubscriptionRatePlanDto;
import com.hcl.bss.exceptions.CustomSubscriptionException;
import com.hcl.bss.repository.AddressRepository;
import com.hcl.bss.repository.CustomerRepository;
import com.hcl.bss.repository.ProductRepository;
import com.hcl.bss.repository.SubscriptionRepository;
import com.hcl.bss.repository.specification.SubscriptionSpecification;

/**
 * This is SubscriptionServiceImpl will handle calls related to subscriptions
 *
 * @author- Vinay Panwar
 */
@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductRepository productRepository;
//    private BigDecimal remainingCycle=BigDecimal.ZERO;
//    private Double totalAmount = (double) 0;
    
	DateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);
	//DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY);

 	@Override
	public List<SubscriptionDto> findAllSubscription(SubscriptionInOut subscriptionIn, Pageable pageable, HttpServletResponse response){
		LOGGER.info("<-----------------------Start findAllSubscription() method in SubscriptionServiceImpl.java------------------------>");
		List<Subscription> subscriptionList = null;
		Date fromDate = null;
		Date toDate = null;

		/*End:- Defining specification for filter */

		try {
			if (subscriptionIn.getFromDateStr() != null && !"".equalsIgnoreCase(subscriptionIn.getFromDateStr())
					&& subscriptionIn.getToDateStr() != null && !"".equalsIgnoreCase(subscriptionIn.getToDateStr())) {
				fromDate = dateFormat.parse(subscriptionIn.getFromDateStr());
				toDate = dateFormat.parse(subscriptionIn.getToDateStr());
			}
			
			/*Start:- Defining specification for filter */
			Specification<Subscription> specId = (subscriptionIn.getSubscriptionId() != null
					&& !"".equalsIgnoreCase(subscriptionIn.getSubscriptionId())
							? Specification.where(
									SubscriptionSpecification.hasSubscriptionId(subscriptionIn.getSubscriptionId()))
							: null);
			Specification<Subscription> specStatus = (subscriptionIn.getStatus() != null
					&& !"".equalsIgnoreCase(subscriptionIn.getStatus())
							? Specification.where(SubscriptionSpecification.hasStatus(subscriptionIn.getStatus()))
							: null);
			Specification<Subscription> specDate = (fromDate != null && toDate != null
							? Specification.where(SubscriptionSpecification.hasStartDate(fromDate, toDate)) : null);
			
			Specification<Subscription> specPlanName = (subscriptionIn.getPlanName() != null
					&& !"".equalsIgnoreCase(subscriptionIn.getPlanName())
							? Specification.where(SubscriptionSpecification.hasPlanName(subscriptionIn.getPlanName()))
							: null);
			Specification<Subscription> specCustomerName = (subscriptionIn.getCustomerName() != null
					&& !"".equalsIgnoreCase(subscriptionIn.getCustomerName())
							? Specification
									.where(SubscriptionSpecification.hasCustomerName(subscriptionIn.getCustomerName()))
							: null);
			/*End:- Defining specification for filter */
			if(null != pageable) {
			Page<Subscription> subscriptionPages = subscriptionRepository.findAll(
					Specification.where(specId).and(specStatus).and(specPlanName).and(specCustomerName).and(specDate),
					pageable);
			subscriptionList = subscriptionPages.getContent();
			subscriptionIn.setTotalPages(subscriptionPages.getTotalPages());
			subscriptionIn.setLastPage(subscriptionPages.isLast());
			}
			else {
				subscriptionList = subscriptionRepository.findAll(
						Specification.where(specId).and(specStatus).and(specPlanName).and(specCustomerName).and(specDate));
			}

			if (subscriptionList != null && subscriptionList.size() > 0) {
				

				return convertSubscriptionsToDto(subscriptionList);
			}
			return null;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}finally {
			LOGGER.info("<------------Start finally in findAllSubscription() method in SubscriptionServiceImpl.java------------------>");
			fromDate = null;
			toDate = null;
			LOGGER.info("<------------End finally in findAllSubscription() method in SubscriptionServiceImpl.java------------------>");
			LOGGER.info("<-----------------------End findAllSubscription() method in SubscriptionServiceImpl.java---------------------->");
		}
	}

	private List<SubscriptionDto> convertSubscriptionsToDto(List<Subscription> subsList) throws ParseException{
		LOGGER.info("<--------------Start convertSubscriptionsToDto() method in SubscriptionServiceImpl.java---------------------->");
		List<SubscriptionDto> subscriptionDtoList = new ArrayList<>();
		SubscriptionDto subscriptionDto = null;
		List <MultipleRatePlanDto> ratePlanList = null;
		MultipleRatePlanDto multipleRatePlanDto = null;

		for(Subscription subscription : subsList) {
			subscriptionDto = new SubscriptionDto();
			subscriptionDto.setSubscriptionId(subscription.getSubscriptionId());
			
			Optional<Customer> customerOpt = customerRepository.findById(subscription.getCustomerId());
			Customer customer = customerOpt.get();
			
			subscriptionDto.setCustomerName(customer.getFirstName());
			subscriptionDto.setEmail(customer.getEmail());
			ratePlanList = new ArrayList<>();
			
			for(SubscriptionRatePlan susRatePlan : subscription.getSubscriptionRatePlans()) {
				multipleRatePlanDto = new MultipleRatePlanDto();
				
				multipleRatePlanDto.setPlanName(susRatePlan.getRatePlan().getRatePlanDescription());
				multipleRatePlanDto.setPrice(susRatePlan.getPrice());
				//if(null != ratePlanList) {
				ratePlanList.add(multipleRatePlanDto);
				//}
				multipleRatePlanDto = null;
			}
			
			subscriptionDto.setRatePlanList(ratePlanList);
			subscriptionDto.setStatus(subscription.getStatus());
/*			subscriptionDto.setCreatedDate(subscription.getCreatedDate());
			subscriptionDto.setActivatedDate(subscription.getActivationDate());
			subscriptionDto.setLastBillDate(subscription.getSubscriptionStartDate());
			subscriptionDto.setNextBillDate(subscription.getSubscriptionEndDate());
*/
			if(subscription.getCreatedDate() != null)
				subscriptionDto.setCreatedDate(dateFormat.format(subscription.getCreatedDate()));
			if(subscription.getActivationDate() != null)
				subscriptionDto.setActivatedDate(dateFormat.format(subscription.getActivationDate()));
			if(subscription.getSubscriptionStartDate() != null)
				subscriptionDto.setLastBillDate(dateFormat.format(subscription.getSubscriptionStartDate()));
			if(subscription.getSubscriptionEndDate() != null)
				subscriptionDto.setNextBillDate(dateFormat.format(subscription.getSubscriptionEndDate()));
			
			subscriptionDtoList.add(subscriptionDto);
			subscriptionDto = null;
			ratePlanList = null;
		}
		ratePlanList = null;
		subsList = null;
		LOGGER.info("<--------------End convertSubscriptionsToDto() method in SubscriptionServiceImpl.java---------------------->");

		return subscriptionDtoList;
	}

	@Override
	public CustomerDto findSubscriptionDetail(String subId) {
		// TODO Auto-generated method stub
		
			    CustomerDto response = new CustomerDto();
				Subscription subscription = subscriptionRepository.findBySubscriptionId(subId);
				if(subscription==null) {
					throw new CustomSubscriptionException(100,"No Subscription Found");
				}
				Customer customer = customerRepository.findById(subscription.getCustomerId()).get();
				if(customer==null) {
					throw new CustomSubscriptionException(100,"Customer not found");
				}
				if(customer.getFirstName()!=null)
					response.setFirstName(customer.getFirstName());
				else
					response.setFirstName(HYPHEN);
				if(customer.getLastName()!=null)
					response.setLastName(customer.getLastName());
				else
					response.setLastName(HYPHEN);
				if(customer.getEmail()!=null)
					response.setEmailAddress(customer.getEmail());
				else
					response.setEmailAddress(HYPHEN);
				response.setBillingAdress(covertAddressEntityToDto(addressRepository.findById(customer.getBillTo()).get()));
				response.setShippingAdress(covertAddressEntityToDto(addressRepository.findById(customer.getSoldTo()).get()));
				response.setSubscriptionDto(convertSubscriptioEntityToDto(subscription));
				return response;
	}

	private AddressDto covertAddressEntityToDto(Address adress) {
		// TODO Auto-generated method stub
		AddressDto addressDto = new AddressDto();
		if(adress.getLine1()!=null)
			addressDto.setLine1(adress.getLine1());
		else
			addressDto.setLine1(HYPHEN);
		if(adress.getLine2()!=null)
			addressDto.setLine2(adress.getLine2());
		else
			addressDto.setLine2(HYPHEN);
		if(adress.getCity()!=null)
			addressDto.setCity(adress.getCity());
		else
			addressDto.setCity(HYPHEN);
		if(adress.getState()!=null)
			addressDto.setState(adress.getState());
		else
			addressDto.setState(HYPHEN);
		if(adress.getZipcode()!=null)
			addressDto.setZipcode(adress.getZipcode());
		else
			addressDto.setZipcode(HYPHEN);
		addressDto.setCountry(adress.getCountry().getCountryName());
		return addressDto;
	}
// This is used for subscription detail service
	private SubscriptionDetailDto convertSubscriptioEntityToDto(Subscription subscription) {
		// TODO Auto-generated method stub
		Set<SubscriptionRatePlan> subRatePlans = subscription.getSubscriptionRatePlans(); 
		SubscriptionDetailDto subscriptionDto = new SubscriptionDetailDto();
		subscriptionDto.setSubscriptionId(subscription.getSubscriptionId());
		subscriptionDto.setStatus(subscription.getStatus());
		if(subscription.getNextBillingDate()!=null)
			subscriptionDto.setNextBillDate(this.getStringDate(new Date(subscription.getNextBillingDate().getTime())));
		else
			subscriptionDto.setNextBillDate(NOT_APPLICABLE);
		if(subscription.getCreatedDate()!=null)
			subscriptionDto.setCreatedOn(this.getStringDate(new Date(subscription.getCreatedDate().getTime())));
		else
			subscriptionDto.setCreatedOn(NOT_APPLICABLE);
		if(subscription.getActivationDate()!=null)
			subscriptionDto.setActivationDate(this.getStringDate(new Date(subscription.getActivationDate().getTime())));
		else
			subscriptionDto.setCreatedOn(NOT_APPLICABLE);
		if(subscription.getLastBillingDate()!=null)
			subscriptionDto.setLastBillDate(this.getStringDate(new Date(subscription.getLastBillingDate().getTime())));
		else
			subscriptionDto.setLastBillDate(NOT_APPLICABLE);
		if(subscription.getSubscriptionEndDate()!=null)
			subscriptionDto.setExpireOn(this.getStringDate(new Date(subscription.getSubscriptionEndDate().getTime())));
		else
			subscriptionDto.setExpireOn(NOT_APPLICABLE);
		if(subRatePlans!=null && !subRatePlans.isEmpty()) {
			subscriptionDto.setProductPlanList(covertProductRatePlanListEntityToDto(subRatePlans));
			for(SubscriptionRatePlan subRatePlan : subRatePlans) {
				if(productRepository.getOne(subRatePlan.getProduct()).getParent()==null) {
					if(subRatePlan.getRatePlan().getExpireAfter().intValue()==999) {
						subscriptionDto.setRenewsForever(true);
//						subscriptionDto.setSubscriptionDuration(Integer.toString((subRatePlan.getRatePlan().getBillingCycleTerm().intValue()*this.remainingCycle.intValue()))+" "+subRatePlan.getRatePlan().getBillingFrequency());
						subscriptionDto.setSubscriptionDuration(Integer.toString((subRatePlan.getRatePlan().getBillingCycleTerm().intValue()))+" "+subRatePlan.getRatePlan().getBillingFrequency());
						if(subRatePlan.getRatePlan().getCurrency()!=null)
							subscriptionDto.setTotalAmount(subRatePlan.getRatePlan().getCurrency().getCurrencyCode()+" "+Double.toString(subscription.getAmount()));
						else
							throw new CustomSubscriptionException(100,"RatePlan not mapped with currency");
					}
					else {
						subscriptionDto.setRenewsForever(false);
//						if(this.remainingCycle!=BigDecimal.ZERO)
							subscriptionDto.setRemainingCycles(subRatePlan.getRatePlan().getExpireAfter());
							subscriptionDto.setSubscriptionDuration(Integer.toString((subRatePlan.getRatePlan().getBillingCycleTerm().intValue()))+" "+subRatePlan.getRatePlan().getBillingFrequency());
						if(subRatePlan.getRatePlan().getCurrency()!=null)
							subscriptionDto.setTotalAmount(subRatePlan.getRatePlan().getCurrency().getCurrencyCode()+" "+Double.toString(subscription.getAmount()));
						else
							throw new CustomSubscriptionException(100,"RatePlan not mapped with currency");
					}
				}
			}
			if(subscription.getStatus().equalsIgnoreCase("CANCELLED"))
				subscriptionDto.setCancelDate(subscription.getCancelDate().toString());
		}
		else
			throw new CustomSubscriptionException(100,"No Product/RatePlan associated with subscription");
//		subscriptionDto.setTotalAmount(this.totalAmount);
		return subscriptionDto;
	}

	private List<SubscriptionRatePlanDto> covertProductRatePlanListEntityToDto(Set<SubscriptionRatePlan> subRateplanList){
		// TODO Auto-generated method stub
		try{
			List<SubscriptionRatePlanDto> subProductRatePlanDtoList = new ArrayList<SubscriptionRatePlanDto>();
			for(SubscriptionRatePlan subRatePlan: subRateplanList) {
				SubscriptionRatePlanDto subscriptionRatePlanDto = new SubscriptionRatePlanDto();
				if(subRatePlan.getRatePlan()!=null) {
					subscriptionRatePlanDto.setRateplan(subRatePlan.getRatePlan().getRatePlanId());
					subscriptionRatePlanDto.setRateplanDesc(subRatePlan.getRatePlan().getRatePlanDescription());
					if(subRatePlan.getRatePlan().getPricingScheme().equals(VOLUME))
						subscriptionRatePlanDto.setRate(subRatePlan.getRatePlan().getCurrency().getCurrencyCode()+" "+Double.toString(subRatePlan.getRatePlanVolume().getPrice()));
					else if(subRatePlan.getRatePlan().getPricingScheme().equals(UNIT))
						subscriptionRatePlanDto.setRate(subRatePlan.getRatePlan().getCurrency().getCurrencyCode()+" "+Double.toString(subRatePlan.getRatePlan().getPrice()));
				}
				Product product = productRepository.findById(subRatePlan.getProduct()).get(); 
				subscriptionRatePlanDto.setProductName(product.getProductDispName());
				if(product.getParent()==null)
					subscriptionRatePlanDto.setIsParent(true);
				else
					subscriptionRatePlanDto.setIsParent(false);
				subscriptionRatePlanDto.setAmount(subRatePlan.getRatePlan().getCurrency().getCurrencyCode()+" "+Double.toString(subRatePlan.getPrice()));
				subscriptionRatePlanDto.setQuantity(subRatePlan.getQuantity());
//				subscriptionRatePlanDto.setTax(subRatePlan.gett); //tax is not handled rightnow
				subscriptionRatePlanDto.setBillFrequency(subRatePlan.getBillingFrequency());
				subProductRatePlanDtoList.add(subscriptionRatePlanDto);
//				this.totalAmount = this.totalAmount + subRatePlan.getPrice();			
			}
			return subProductRatePlanDtoList;
		}
		catch (Exception e) {
			// TODO: handle exception
			throw new CustomSubscriptionException(200,"Internal Server Error");
		}
	}
	
	private String getStringDate(Date dateToFormat) {
		String formatedDate = dateFormat.format(dateToFormat);
		return formatedDate;
	}
	
	@Override
	public ResponseDto cancelSubscription(String subId) {
		
		Subscription subscription = subscriptionRepository.findBySubscriptionId(subId);
		ResponseDto responseDto = new ResponseDto();	
		if(subscription!=null) {
			subscription.setStatus("CANCELLED");
			subscription.setIsActive(0);
//			subscription.setSubscriptionEndDate(Timestamp.valueOf((LocalDateTime.now())));
			subscription.setCancelDate(LocalDate.now());
			subscriptionRepository.save(subscription);
			responseDto.setMessage("Subscription Cancelled Successfully");
			responseDto.setResponseCode(200);
			responseDto.setResponseStatus("Successfull");
			return responseDto;
		}
		else
			throw new CustomSubscriptionException(100,"Subscription not found");
	}

	@Override
	public List<String> getLastSubscriptionIds() {
		return subscriptionRepository.getLastSubscriptionIds();
	}
}
