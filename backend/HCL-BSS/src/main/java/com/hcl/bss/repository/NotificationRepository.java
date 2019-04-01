package com.hcl.bss.repository;

import com.hcl.bss.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author- Aditya gupta
 */
public interface NotificationRepository extends JpaRepository<Notification, Long>{
    public Notification findBySubscriptionId(String subscriptionId);

    /*@Query(name = "Select * from TB_NOTIFICATION_TRANSACTION where NOTIFICATIONS_SENT_COUNT=?1", nativeQuery = true)
    public List<Notification> findAll(int count);*/
}
