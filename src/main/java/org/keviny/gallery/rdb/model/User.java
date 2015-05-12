package org.keviny.gallery.rdb.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.keviny.gallery.common.annotation.Ignore;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

@Entity(name = "users")
public class User implements Serializable {
	private static final long serialVersionUID = -7333846185163800869L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
    private Integer id;
    @Column(name = "username", nullable = false, unique = true,insertable = false, updatable = false, length = 30)
    private String username;
    @Column(name = "first_name", length = 10)
    private String firstName;
    @Column(name = "middle_name", length = 10)
    private String middleName;
    @Column(name = "last_name", length = 10)
    private String lastName;
    @Ignore
    @Column(name = "passwd", nullable = false, length = 40)
    private String password;
    @Column(name = "email", nullable = false, unique = true, length = 80)
    private String email;
    @Column(name = "gender", nullable = false)
    private Character gender;
    @Column(name = "icon", length = 80)
    private String icon;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "login_ip", length = 20)
    private String loginIp;
    @Column(name = "login_time")
    private Timestamp loginTime;
    @Column(name = "last_login_ip", length = 20)
    private String lastLoginIp;
    @Column(name = "last_login_time")
    private Timestamp lastLoginTime;
    @Column(name = "register_ip", length = 20)
    private String registerIp;
    @Column(name = "register_time")
    private Timestamp registerTime;
    @Column(name = "update_time")
    private Timestamp updateTime;
    @Column(name = "website", length = 100)
    private String website;
    @Column(name = "country", length = 30)
    private String country;
    @Column(name = "state", length = 30)
    private String state;
    @Column(name = "city", length = 30)
    private String city;
    @Column(name = "district", length = 30)
    private String district;
    @Column(name = "street", length = 50)
    private String street;
    @Column(name = "postcode", length = 10)
    private String postcode;
    @Column(name = "login_count")
    private Integer loginCount;
    @Column(name = "login_failed_count")
    private Integer loginFailedCount;
    @Column(name = "locked", length = 1, nullable = false)
    private Boolean locked;
    @Column(name = "deleted", length = 1, nullable = false)
    private Boolean deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Integer getLoginFailedCount() {
        return loginFailedCount;
    }

    public void setLoginFailedCount(Integer loginFailedCount) {
        this.loginFailedCount = loginFailedCount;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
