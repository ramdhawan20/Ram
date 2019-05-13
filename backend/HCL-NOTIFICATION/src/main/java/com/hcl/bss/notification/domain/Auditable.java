package com.hcl.bss.notification.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.GeneratorType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.hcl.bss.notification.repository.generator.LoggedUserGenerator;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<U> {
	
	@CreatedBy
	@GeneratorType(type = LoggedUserGenerator.class)
	@Column(name = "CRE_BY")
    private U createdBy;

    @CreatedDate
    @Column(name = "CRE_DT")
    private Date createdDate;

    @Column(name = "UPD_BY")
    @GeneratorType(type = LoggedUserGenerator.class)
    private U lastModifiedBy;

    @LastModifiedDate
    @Column(name = "UPD_DT")
    private Date lastModifiedDate;

	public U getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(U createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public U getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(U lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
