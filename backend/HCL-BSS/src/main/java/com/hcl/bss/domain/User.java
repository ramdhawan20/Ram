package com.hcl.bss.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name="tb_user_details")
/*@NamedQueries({
        @NamedQuery(
                name = "findUserById",
                query = "from User a where a.id = :id"
        ),
})*/
public class User implements Serializable {
    @Id
    @Column(name="uidpk")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uidpk_sequence")
    @TableGenerator(
            name = "uidpk_sequence",
            table = "id_gen",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            initialValue = 1000000000,
            allocationSize = 1)
    private int id;
    @Column(name = "user_id") private String userId;
    @Column(name = "password") private String password;
    @Column(name = "role_id") private int roleId;
    @Column(name = "user_first_name") private String userFirstName;
    @Column(name = "user_middle_name")
	private String userMiddleName;
	@Column(name = "user_last_name") private String userLastName;
    @Column(name="last_login") private Timestamp lastLogin;
    @Column(name="is_locked") private  int isLocked;
    @Column(name="upd_by") private  String updatedBy;
    @Column(name="upd_dt") private Timestamp uupdatedDate;
    @Column(name="cre_by")private String createdBy;
    @Column(name="cre_dt")private Timestamp createdDate;

    @OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinTable(name = "TB_USER_ROLE_MAPPING", joinColumns = { @JoinColumn(name = "USER_ID",referencedColumnName = "USER_ID")}, inverseJoinColumns = { @JoinColumn(name = "ROLE_UID",referencedColumnName = "UIDPK") })
    private List<Role> roleList;
  
    public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {

        this.roleId = roleId;
    }

    public String getUserFirstName() {

        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {

        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {

        return userLastName;
    }

    public void setUserLastName(String userLastName) {

        this.userLastName = userLastName;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getIsLocked() {

        return isLocked;
    }

    public void setIsLocked(int isLocked) {
        this.isLocked = isLocked;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {

        this.updatedBy = updatedBy;
    }

    public Timestamp getUupdatedDate() {

        return uupdatedDate;
    }

    public void setUupdatedDate(Timestamp uupdatedDate) {

        this.uupdatedDate = uupdatedDate;
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
	public String getUserMiddleName() {
		return userMiddleName;
	}

	public void setUserMiddleName(String userMiddleName) {
		this.userMiddleName = userMiddleName;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userId=" + userId + ", password=" + password + ", roleId=" + roleId
				+ ", userFirstName=" + userFirstName + ", userMiddleName=" + userMiddleName + ", userLastName="
				+ userLastName + ", lastLogin=" + lastLogin + ", isLocked=" + isLocked + ", updatedBy=" + updatedBy
				+ ", uupdatedDate=" + uupdatedDate + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", roleList=" + roleList + ", getRoleList()=" + getRoleList() + ", getId()=" + getId()
				+ ", getUserId()=" + getUserId() + ", getPassword()=" + getPassword() + ", getRoleId()=" + getRoleId()
				+ ", getUserFirstName()=" + getUserFirstName() + ", getUserLastName()=" + getUserLastName()
				+ ", getLastLogin()=" + getLastLogin() + ", getIsLocked()=" + getIsLocked() + ", getUpdatedBy()="
				+ getUpdatedBy() + ", getUupdatedDate()=" + getUupdatedDate() + ", getCreatedBy()=" + getCreatedBy()
				+ ", getCreatedDate()=" + getCreatedDate() + ", getUserMiddleName()=" + getUserMiddleName()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
}