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
@Table(name="auto_user_profile")
public class AutoUserProfile implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoUserProfile(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoUserProfile{");
        sb.append("profileId=").append(profileId);
        sb.append(", userId=").append(userId);
        sb.append(", realName=").append(realName);
        sb.append(", shopName=").append(shopName);
        sb.append(", phone=").append(phone);
        sb.append(", shopType=").append(shopType);
        sb.append(", shopDesc=").append(shopDesc);
        sb.append(", shopBrands=").append(shopBrands);
        sb.append(", shopBase=").append(shopBase);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="profile_id", unique=true)
	private Integer profileId;
	@Column(name="user_id", unique=true)
	private Integer userId;
	@Column(name="real_name", nullable=true, length=20)
	private String realName;
	@Column(name="shop_name", nullable=true, length=50)
	private String shopName;
	@Column(name="phone", nullable=true, length=20)
	private String phone;
	@Column(name="shop_type", nullable=true)
	private Integer shopType;
	@Column(name="shop_desc", nullable=true)
	private String shopDesc;
	@Column(name="shop_brands", nullable=true, length=200)
	private String shopBrands;
	@Column(name="shop_base", nullable=true, length=300)
	private String shopBase;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getProfileId(){
		return profileId;
	}
	
	public void setProfileId(Integer profileId){
		this.profileId = profileId;
	}
		
	public Integer getUserId(){
		return userId;
	}
	
	public void setUserId(Integer userId){
		this.userId = userId;
	}

    public String getRealName(){
		return realName;
	}
	
	public void setRealName(String realName){
		this.realName = realName;
	}
		
	public String getShopName(){
		return shopName;
	}
	
	public void setShopName(String shopName){
		this.shopName = shopName;
	}
		
	public String getPhone(){
		return phone;
	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}
		
	public Integer getShopType(){
		return shopType;
	}
	
	public void setShopType(Integer shopType){
		this.shopType = shopType;
	}
		
	public String getShopDesc(){
		return shopDesc;
	}
	
	public void setShopDesc(String shopDesc){
		this.shopDesc = shopDesc;
	}
		
	public String getShopBrands(){
		return shopBrands;
	}
	
	public void setShopBrands(String shopBrands){
		this.shopBrands = shopBrands;
	}
		
	public String getShopBase(){
		return shopBase;
	}
	
	public void setShopBase(String shopBase){
		this.shopBase = shopBase;
	}
		
}
