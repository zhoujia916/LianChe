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
@Table(name="auto_car_attr")
public class AutoCarAttr implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoCarAttr(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoCarAttr{");
        sb.append("carAttrId=").append(carAttrId);
        sb.append(", carId=").append(carId);
        sb.append(", attrId=").append(attrId);
        sb.append(", attrValue=").append(attrValue);
        sb.append(", attrPrice=").append(attrPrice);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="car_attr_id", unique=true)
	private Integer carAttrId;
	@Column(name="car_id", nullable=true)
	private Integer carId;
	@Column(name="attr_id", nullable=true)
	private Integer attrId;
	@Column(name="attr_value", nullable=true, length=200)
	private String attrValue;
	@Column(name="attr_price", nullable=true)
	private Integer attrPrice;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getCarAttrId(){
		return carAttrId;
	}
	
	public void setCarAttrId(Integer carAttrId){
		this.carAttrId = carAttrId;
	}
		
	public Integer getCarId(){
		return carId;
	}
	
	public void setCarId(Integer carId){
		this.carId = carId;
	}
		
	public Integer getAttrId(){
		return attrId;
	}
	
	public void setAttrId(Integer attrId){
		this.attrId = attrId;
	}
		
	public String getAttrValue(){
		return attrValue;
	}
	
	public void setAttrValue(String attrValue){
		this.attrValue = attrValue;
	}
		
	public Integer getAttrPrice(){
		return attrPrice;
	}
	
	public void setAttrPrice(Integer attrPrice){
		this.attrPrice = attrPrice;
	}
		
}
