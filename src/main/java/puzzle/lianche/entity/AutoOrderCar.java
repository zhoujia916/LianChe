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
@Table(name="auto_order_car")
public class AutoOrderCar implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoOrderCar(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoOrderCar{");
        sb.append("recId=").append(recId);
        sb.append(", orderId=").append(orderId);
        sb.append(", carId=").append(carId);
        sb.append(", carAttrId=").append(carAttrId);
        sb.append(", carAttr=").append(carAttr);
        sb.append(", carPrice=").append(carPrice);
        sb.append(", carNumber=").append(carNumber);
        sb.append(", sendNumber=").append(sendNumber);
        sb.append(", hasParts=").append(hasParts);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="rec_id", unique=true)
	private Integer recId;
	@Column(name="order_id", nullable=true)
	private Integer orderId;
	@Column(name="car_id", nullable=true)
	private Integer carId;
	@Column(name="car_attr_id", nullable=true, length=255)
	private String carAttrId;
	@Column(name="car_attr", nullable=true, length=1000)
	private String carAttr;
	@Column(name="car_price", nullable=true)
	private Double carPrice;

    @Column(name="car_number", nullable=true)
    private int carNumber;

    @Column(name="send_number", nullable=true)
    private int sendNumber;

    @Column(name="has_parts", nullable=true)
    private int hasParts;

	
	
	/**
	* Getter and Setter
	*/
	public Integer getRecId(){
		return recId;
	}
	
	public void setRecId(Integer recId){
		this.recId = recId;
	}
		
	public Integer getOrderId(){
		return orderId;
	}
	
	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}
		
	public Integer getCarId(){
		return carId;
	}
	
	public void setCarId(Integer carId){
		this.carId = carId;
	}
		
	public String getCarAttrId(){
		return carAttrId;
	}
	
	public void setCarAttrId(String carAttrId){
		this.carAttrId = carAttrId;
	}
		
	public String getCarAttr(){
		return carAttr;
	}
	
	public void setCarAttr(String carAttr){
		this.carAttr = carAttr;
	}
		
	public Double getCarPrice(){
		return carPrice;
	}
	
	public void setCarPrice(Double carPrice){
		this.carPrice = carPrice;
	}



    public Integer getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(int carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(int sendNumber) {
        this.sendNumber = sendNumber;
    }

    public Integer getHasParts() {
        return hasParts;
    }

    public void setHasParts(int hasParts) {
        this.hasParts = hasParts;
    }
		
}
