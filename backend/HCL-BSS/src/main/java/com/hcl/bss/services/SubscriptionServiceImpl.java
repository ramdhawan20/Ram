package com.hcl.bss.services;

import static com.hcl.bss.constants.ApplicationConstants.DD_MM_YYYY;
import static com.hcl.bss.constants.ApplicationConstants.DATE_FORMAT_DD_MM_YYYY;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hcl.bss.domain.Customer;
import com.hcl.bss.domain.Subscription;
import com.hcl.bss.domain.SubscriptionRatePlan;
import com.hcl.bss.dto.MultipleRatePlanDto;
import com.hcl.bss.dto.SubscriptionDto;
import com.hcl.bss.dto.SubscriptionInOut;
import com.hcl.bss.repository.CustomerRepository;
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
			}
			else {
				subscriptionList = subscriptionRepository.findAll(
						Specification.where(specId).and(specStatus).and(specPlanName).and(specCustomerName).and(specDate));
			}

			if (subscriptionList != null && subscriptionList.size() > 0) {
				subscriptionIn.setTotalPages(subscriptionPages.getTotalPages());
				subscriptionIn.setLastPage(subscriptionPages.isLast());

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
			
			for(SubscriptionRatePlan susRatePlan : subscription.getSubscriptionRatePlan()) {
				multipleRatePlanDto = new MultipleRatePlanDto();
				ratePlanList = new ArrayList<>();
				
				multipleRatePlanDto.setPlanName(susRatePlan.getRatePlan().getRatePlanDescription());
				multipleRatePlanDto.setPrice(susRatePlan.getPrice());
				
				ratePlanList.add(multipleRatePlanDto);
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

	
	
}