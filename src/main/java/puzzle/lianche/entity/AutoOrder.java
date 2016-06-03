package puzzle.lianche.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
@Table(name="auto_order")
public class AutoOrder implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoOrder(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoOrder{");
        sb.append("orderId=").append(orderId);
        sb.append(", orderSn=").append(orderSn);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", payStatus=").append(payStatus);
        sb.append(", shipStatus=").append(shipStatus);
        sb.append(", addTime=").append(addTime);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="order_id", unique=true)
	private Integer orderId;
	@Column(name="order_sn", nullable=true, length=50)
	private String orderSn;
	@Column(name="order_status", nullable=true)
	private Integer orderStatus;
	@Column(name="pay_status", nullable=true)
	private Integer payStatus;
	@Column(name="ship_status", nullable=true)
	private Integer shipStatus;
	@Column(name="add_time", nullable=true)
	private Long addTime;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getOrderId(){
		return orderId;
	}
	
	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}
		
	public String getOrderSn(){
		return orderSn;
	}
	
	public void setOrderSn(String orderSn){
		this.orderSn = orderSn;
	}
		
	public Integer getOrderStatus(){
		return orderStatus;
	}
	
	public void setOrderStatus(Integer orderStatus){
		this.orderStatus = orderStatus;
	}
		
	public Integer getPayStatus(){
		return payStatus;
	}
	
	public void setPayStatus(Integer payStatus){
		this.payStatus = payStatus;
	}
		
	public Integer getShipStatus(){
		return shipStatus;
	}
	
	public void setShipStatus(Integer shipStatus){
		this.shipStatus = shipStatus;
	}
		
	public Long getAddTime(){
		return addTime;
	}
	
	public void setAddTime(Long addTime){
		this.addTime = addTime;
	}


    public List<AutoOrderCar> getCars() {
        return cars;
    }

    public void setCars(List<AutoOrderCar> cars) {
        this.cars = cars;
    }

    private List<AutoOrderCar> cars;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private double amount;

    public long getShipTime() {
        return shipTime;
    }

    public void setShipTime(long shipTime) {
        this.shipTime = shipTime;
    }

    private long shipTime;


    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    private String orderRemark;

    public String getStatusRemark() {
        return statusRemark;
    }

    public void setStatusRemark(String statusRemark) {
        this.statusRemark = statusRemark;
    }

    private String statusRemark;
}
