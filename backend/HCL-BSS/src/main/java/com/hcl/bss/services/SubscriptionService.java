package com.hcl.bss.services;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;

import com.hcl.bss.domain.Subscription;
import com.hcl.bss.domain.Subscription1;
import com.hcl.bss.dto.CustomerDto;
import com.hcl.bss.dto.ResponseDto;
import com.hcl.bss.dto.SubscriptionDto;
import com.hcl.bss.dto.SubscriptionInOut;

/**
 * This is SubscriptionService interface for subscriptions
 *
 * @author- Vinay Panwar
 */
public interface SubscriptionService {

    List<SubscriptionDto> findAllSubscription(SubscriptionInOut subscriptionIn, Pageable pageable, HttpServletResponse response);

    CustomerDto findSubscriptionDetail(String subId);
    ResponseDto cancelSubscription(String subId);
}
