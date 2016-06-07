package puzzle.lianche.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.lang.StringBuilder;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="auto_user")
public class AutoUser implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoUser(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoUser{");
        sb.append("userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", password=").append(password);
        sb.append(", birth=").append(birth);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", userAvatar=").append(userAvatar);
        sb.append(", status=").append(status);
        sb.append(", point=").append(point);
        sb.append(", remark=").append(remark);
        sb.append(", addTime=").append(addTime);
        sb.append(", authenticateTime=").append(authenticateTime);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="user_id", unique=true)
	private Integer userId;
	@Column(name="user_name", nullable=true, length=20)
	private String userName;
	@Column(name="password", nullable=true, length=50)
	private String password;
	@Column(name="birth", nullable=true)
	private Long birth;
	@Column(name="phone", nullable=true, length=20)
	private String phone;
	@Column(name="email", nullable=true, length=20)
	private String email;
	@Column(name="user_avatar", nullable=true, length=255)
	private String userAvatar;
	@Column(name="status", nullable=true)
	private Integer status;
	@Column(name="point", nullable=true)
	private Integer point;
	@Column(name="remark", nullable=true, length=500)
	private String remark;
	@Column(name="add_time", nullable=true)
	private Long addTime;
	@Column(name="authenticate_time", nullable=true)
	private Long authenticateTime;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getUserId(){
		return userId;
	}
	
	public void setUserId(Integer userId){
		this.userId = userId;
	}
		
	public String getUserName(){
		return userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
		
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
		
	public Long getBirth(){
		return birth;
	}
	
	public void setBirth(Long birth){
		this.birth = birth;
	}
		
	public String getPhone(){
		return phone;
	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}
		
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
		
	public String getUserAvatar(){
		return userAvatar;
	}
	
	public void setUserAvatar(String userAvatar){
		this.userAvatar = userAvatar;
	}
		
	public Integer getStatus(){
		return status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}
		
	public Integer getPoint(){
		return point;
	}
	
	public void setPoint(Integer point){
		this.point = point;
	}
		
	public String getRemark(){
		return remark;
	}
	
	public void setRemark(String remark){
		this.remark = remark;
	}
		
	public Long getAddTime(){
		return addTime;
	}
	
	public void setAddTime(Long addTime){
		this.addTime = addTime;
	}
		
	public Long getAuthenticateTime(){
		return authenticateTime;
	}
	
	public void setAuthenticateTime(Long authenticateTime){
		this.authenticateTime = authenticateTime;
	}
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}

    //新增属性
    private String realName;
    private String statusString;
    private String shopName;
    private Integer shopType;
    private String shopTypeString;
    private String birthDay;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getShopTypeString() {
        return shopTypeString;
    }

    public void setShopTypeString(String shopTypeString) {
        this.shopTypeString = shopTypeString;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public Integer getShopType() {
        return shopType;
    }

    public void setShopType(Integer shopType) {
        this.shopType = shopType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
