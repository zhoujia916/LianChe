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
    public Integer getSellerId() {
        return sellerId;
    }
    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }
    @Column(name="seller_id", nullable=true)
    private Integer sellerId;
    @Column(name="buyer_id", nullable=true)
    private Integer buyerId;
    @Column(name="seller_deposit", nullable=true)
    private double sellerDeposit;
    @Column(name="buyer_deposit", nullable=true)
    private double buyerDeposit;
    public Integer getPayMethod() {
        return payMethod;
    }
    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }
    @Column(name="pay_method", nullable=true)
    private Integer payMethod;
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Column(name="remark", nullable=true)
    private String remark;
    public String getStatusRemark() {
        return statusRemark;
    }
    public void setStatusRemark(String statusRemark) {
        this.statusRemark = statusRemark;
    }
    @Column(name="status_remark", nullable=true)
    private String statusRemark;
    public String getOrderRemark() {
        return orderRemark;
    }
    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }
    @Column(name="order_remark", nullable=true)
    private String orderRemark;
	
	
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

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public double getSellerDeposit() {
        return sellerDeposit;
    }

    public void setSellerDeposit(double sellerDeposit) {
        this.sellerDeposit = sellerDeposit;
    }

    public double getBuyerDeposit() {
        return buyerDeposit;
    }

    public void setBuyerDeposit(double buyerDeposit) {
        this.buyerDeposit = buyerDeposit;
    }

    public Integer getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(Integer clientStatus) {
        this.clientStatus = clientStatus;
    }

    private Integer clientStatus;


}
