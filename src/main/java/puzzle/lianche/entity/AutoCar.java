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
@Table(name="auto_car")
public class AutoCar implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoCar(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoCar{");
        sb.append("carId=").append(carId);
        sb.append(", carName=").append(carName);
        sb.append(", brandId=").append(brandId);
        sb.append(", brandCatId=").append(brandCatId);
        sb.append(", brandModelId=").append(brandModelId);
        sb.append(", officalPrice=").append(officalPrice);
        sb.append(", totalNumber=").append(totalNumber);
        sb.append(", lockNumber=").append(lockNumber);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", area=").append(area);
        sb.append(", addTime=").append(addTime);
        sb.append(", addUserId=").append(addUserId);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", carType=").append(carType);
        sb.append(", status=").append(status);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="car_id", unique=true)
	private Integer carId;
	@Column(name="car_name", nullable=true, length=255)
	private String carName;
	@Column(name="brand_id", nullable=true)
	private Integer brandId;
	@Column(name="brand_cat_id", nullable=true)
	private Integer brandCatId;
	@Column(name="brand_model_id", nullable=true)
	private Integer brandModelId;
	@Column(name="offical_price", nullable=true)
	private Float officalPrice;
	@Column(name="total_number", nullable=true)
	private Integer totalNumber;
	@Column(name="lock_number", nullable=true)
	private Integer lockNumber;
    @Column(name="surplus_number", nullable=true)
    private Integer surplusNumber;
	@Column(name="province", nullable=true)
	private Integer province;
	@Column(name="city", nullable=true)
	private Integer city;
	@Column(name="area", nullable=true)
	private Integer area;
    @Column(name="has_parts", nullable=true)
    private Integer hasParts;
    @Column(name="parts", nullable=true)
    private String parts;
    @Column(name="parts_price", nullable=true)
    private Float partsPrice;
	@Column(name="add_time", nullable=true)
	private Long addTime;
    @Column(name="add_user_type", nullable = true)
    private Integer addUserType;
    @Column(name="add_user_id", nullable=true)
	private Integer addUserId;
	@Column(name="start_date", nullable=true)
	private Long startDate;
	@Column(name="end_date", nullable=true)
	private Long endDate;
	@Column(name="car_type", nullable=true)
	private Integer carType;
	@Column(name="status", nullable=true)
	private Integer status;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getCarId(){
		return carId;
	}
	
	public void setCarId(Integer carId){
		this.carId = carId;
	}
		
	public String getCarName(){
		return carName;
	}
	
	public void setCarName(String carName){
		this.carName = carName;
	}
		
	public Integer getBrandId(){
		return brandId;
	}
	
	public void setBrandId(Integer brandId){
		this.brandId = brandId;
	}
		
	public Integer getBrandCatId(){
		return brandCatId;
	}
	
	public void setBrandCatId(Integer brandCatId){
		this.brandCatId = brandCatId;
	}
		
	public Integer getBrandModelId(){
		return brandModelId;
	}
	
	public void setBrandModelId(Integer brandModelId){
		this.brandModelId = brandModelId;
	}
		
	public Float getOfficalPrice(){
		return officalPrice;
	}
	
	public void setOfficalPrice(Float officalPrice){
		this.officalPrice = officalPrice;
	}
		
	public Integer getTotalNumber(){
		return totalNumber;
	}
	
	public void setTotalNumber(Integer totalNumber){
		this.totalNumber = totalNumber;
	}
		
	public Integer getLockNumber(){
		return lockNumber;
	}
	
	public void setLockNumber(Integer lockNumber){
		this.lockNumber = lockNumber;
	}
		
	public Integer getProvince(){
		return province;
	}
	
	public void setProvince(Integer province){
		this.province = province;
	}
		
	public Integer getCity(){
		return city;
	}
	
	public void setCity(Integer city){
		this.city = city;
	}
		
	public Integer getArea(){
		return area;
	}
	
	public void setArea(Integer area){
		this.area = area;
	}
		
	public Long getAddTime(){
		return addTime;
	}
	
	public void setAddTime(Long addTime){
		this.addTime = addTime;
	}
		
	public Integer getAddUserId(){
		return addUserId;
	}
	
	public void setAddUserId(Integer addUserId){
		this.addUserId = addUserId;
	}
		
	public Long getStartDate(){
		return startDate;
	}
	
	public void setStartDate(Long startDate){
		this.startDate = startDate;
	}
		
	public Long getEndDate(){
		return endDate;
	}
	
	public void setEndDate(Long endDate){
		this.endDate = endDate;
	}
		
	public Integer getCarType(){
		return carType;
	}
	
	public void setCarType(Integer carType){
		this.carType = carType;
	}
		
	public Integer getStatus(){
		return status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}

    public Integer getSurplusNumber() {
        return surplusNumber;
    }

    public void setSurplusNumber(Integer surplusNumber) {
        this.surplusNumber = surplusNumber;
    }

    public Integer getHasParts() {
        return hasParts;
    }

    public void setHasParts(Integer hasParts) {
        this.hasParts = hasParts;
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public Float getPartsPrice() {
        return partsPrice;
    }

    public void setPartsPrice(Float partsPrice) {
        this.partsPrice = partsPrice;
    }
    public Integer getAddUserType() {
        return addUserType;
    }

    public void setAddUserType(Integer addUserType) {
        this.addUserType = addUserType;
    }

    //新增属性
    private String brandName;
    private String catName;
    private String modelName;
    private String userName;
    private String realName;
    private String beginTimeString;
    private String endTimeString;

    public String getBeginTimeString() {
        return beginTimeString;
    }

    public void setBeginTimeString(String beginTimeString) {
        this.beginTimeString = beginTimeString;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
