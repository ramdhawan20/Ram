package com.hcl.bss.domain;

import com.hcl.bss.repository.generator.LoggedUserGenerator;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author- Aditya gupta
 */
@Entity
@Table(name="TB_CUSTOMER")
@Proxy(lazy = false)
public class Customer implements Serializable {
    @Id
    @Column(name="UIDPK")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uidpk_sequence")
    @TableGenerator(
            name = "uidpk_sequence",
            table = "id_gen",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            initialValue = 1000000000,
            allocationSize = 1)
    private Long id;

    @Column(name="CUST_FIRST_NAME")
    private String firstName;

    @Column(name="CUST_LAST_NAME")
    private String lastName;

    @Column(name="CUST_EMAIL")
    private String email;

    @Column(name="CUST_PHONE_NO")
    private String phone;

    @JoinColumn(name="BILLTO_ID")
    @Column(name="BILLTO_ID")
    private Long billTo;

    @JoinColumn(name="SOLDTO_ID")
    @Column(name="SOLDTO_ID")
    private Long soldTo;

    @JoinColumn(name="COMP_ID")
    @Column(name="COMP_ID")
    private Long companyId;

    //@OneToMany(mappedBy="customer")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="CUST_ID",referencedColumnName = "UIDPK", nullable = false)
    //@JoinColumn(name="CUST_ID", nullable = false)
    private Set<Subscription> subscriptions = new HashSet<>();


    @GeneratorType(type = LoggedUserGenerator.class)
    @Column(name = "CRE_BY")
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

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Subscription subscription) {
        this.subscriptions.add(subscription);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getBillTo() {
        return billTo;
    }

    public void setBillTo(Long billTo) {
        this.billTo = billTo;
    }

    public Long getSoldTo() {
        return soldTo;
    }

    public void setSoldTo(Long soldTo) {
        this.soldTo = soldTo;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", billTo=" + billTo +
                ", soldTo=" + soldTo +
                ", companyId=" + companyId +
                ", subscriptions=" + subscriptions +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + createdDate +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
