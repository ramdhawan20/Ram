package com.hcl.bss.domain;

import com.hcl.bss.repository.generator.LoggedUserGenerator;
import org.hibernate.annotations.GeneratorType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author- Aditya Gupta
 */
@Entity
@Table(name="TB_NOTIFICATION_TRANSACTION")
public class Notification implements Serializable{
    @Column(name = "UIDPK")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uidpk_sequence")
    @TableGenerator(
            name = "uidpk_sequence",
            table = "id_gen",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            initialValue = 1000000000,
            allocationSize = 1)
    private Long id;

    @Column(name="SUBSCRIPTION_ID")
    private String subscriptionId;

    @Column(name="LAST_NOTIFICATION_DATE")
    private Timestamp lastNotificationDate;
    @Column(name="NEXT_NOTIFICATION_DATE")
    private Timestamp nextNotificationDate;
    @Column(name="NOTIFICATIONS_SENT_COUNT")
    private int notificationsSentCount;
    @Column(name = "CRE_BY")
    @GeneratorType(type = LoggedUserGenerator.class)
    private String createdBy;
    @CreatedDate
    @Column(name = "CRE_DT")
    private Timestamp createdDate;
    @Column(name = "UPD_BY")
    @GeneratorType(type = LoggedUserGenerator.class)
    private String updatedBy;
    @LastModifiedDate
    @Column(name = "UPD_DT")
    private Timestamp updatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Timestamp getLastNotificationDate() {
        return lastNotificationDate;
    }

    public void setLastNotificationDate(Timestamp lastNotificationDate) {
        this.lastNotificationDate = lastNotificationDate;
    }

    public Timestamp getNextNotificationDate() {
        return nextNotificationDate;
    }

    public void setNextNotificationDate(Timestamp nextNotificationDate) {
        this.nextNotificationDate = nextNotificationDate;
    }

    public int getNotificationsSentCount() {
        return notificationsSentCount;
    }

    public void setNotificationsSentCount(int notificationsSentCount) {
        this.notificationsSentCount = notificationsSentCount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
