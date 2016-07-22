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
    public Integer getBuyerPayMethod() {
        return buyerPayMethod;
    }
    public void setBuyerPayMethod(Integer buyerPayMethod) {
        this.buyerPayMethod = buyerPayMethod;
    }
    @Column(name="buyer_pay_method", nullable=true)
    private Integer buyerPayMethod;
    public Integer getSellerPayMethod() {
        return sellerPayMethod;
    }
    public void setSellerPayMethod(Integer sellerPayMethod) {
        this.sellerPayMethod = sellerPayMethod;
    }
    @Column(name="seller_pay_method", nullable=true)
    private Integer sellerPayMethod;
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name="seller_pay_time", nullable=true)
    private long sellerPayTime;
    public long getSellerPayTime() {
        return sellerPayTime;
    }
    public void setSellerPayTime(long sellerPayTime) {
        this.sellerPayTime = sellerPayTime;
    }
    @Column(name="seller_pay_time", nullable=true)
    private long buyerPayTime;
    public long getBuyerPayTime() {
        return buyerPayTime;
    }
    public void setBuyerPayTime(long buyerPayTime) {
        this.buyerPayTime = buyerPayTime;
    }



    /**
     * 订单操作备注
     */
    @Column(name="remark", nullable=true)
    private String remark;
    public String getStatusRemark() {
        return statusRemark;
    }
    public void setStatusRemark(String statusRemark) {
        this.statusRemark = statusRemark;
    }

    /**
     * 订单状态描述
     */
    @Column(name="status_remark", nullable=true)
    private String statusRemark;
    public String getOrderRemark() {
        return orderRemark;
    }
    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    /**
     * 订单操作状态描述
     */
    @Column(name="order_remark", nullable=true)
    private String orderRemark;
    private String buyerPayNumber;
    public String getBuyerPayNumber() {
        return buyerPayNumber;
    }
    public void setBuyerPayNumber(String buyerPayNumber) {
        this.buyerPayNumber = buyerPayNumber;
    }
    private String sellerPayNumber;
    public String getSellerPayNumber() {
        return sellerPayNumber;
    }
    public void setSellerPayNumber(String sellerPayNumber) {
        this.sellerPayNumber = sellerPayNumber;
    }

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


    public AutoOrderCar getCar() {
        return car;
    }

    public void setCar(AutoOrderCar car) {
        this.car = car;
    }

    private AutoOrderCar car;

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

    private String carName;
    private String sellerName;
    private String sellerRealName;
    private String buyerName;
    private String buyerRealName;
    private Integer carNumber;
    private String addTimeString;
    private Integer hasParts;
    private String orderStatusString;
    private String payStatusString;
    private String shipStatusString;
    private Integer carAttrId;

    public String getCarPic() {
        return carPic;
    }

    public void setCarPic(String carPic) {
        this.carPic = carPic;
    }

    private String carPic;

    public Integer getCarAttrId() {
        return carAttrId;
    }

    public void setCarAttrId(Integer carAttrId) {
        this.carAttrId = carAttrId;
    }

    public String getOrderStatusString() {
        return orderStatusString;
    }

    public void setOrderStatusString(String orderStatusString) {
        this.orderStatusString = orderStatusString;
    }

    public String getPayStatusString() {
        return payStatusString;
    }

    public void setPayStatusString(String payStatusString) {
        this.payStatusString = payStatusString;
    }

    public String getShipStatusString() {
        return shipStatusString;
    }

    public void setShipStatusString(String shipStatusString) {
        this.shipStatusString = shipStatusString;
    }

    public Integer getHasParts() {
        return hasParts;
    }

    public void setHasParts(Integer hasParts) {
        this.hasParts = hasParts;
    }

    public String getAddTimeString() {
        return addTimeString;
    }

    public void setAddTimeString(String addTimeString) {
        this.addTimeString = addTimeString;
    }

    public Integer getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(Integer carNumber) {
        this.carNumber = carNumber;
    }


    public String getPutTimeString() {
        return putTimeString;
    }

    public void setPutTimeString(String putTimeString) {
        this.putTimeString = putTimeString;
    }

    public String putTimeString;

    public Long getPutTime() {
        return putTime;
    }

    public void setPutTime(Long putTime) {
        this.putTime = putTime;
    }

    private Long putTime;

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerRealName() {
        return sellerRealName;
    }

    public void setSellerRealName(String sellerRealName) {
        this.sellerRealName = sellerRealName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerRealName() {
        return buyerRealName;
    }

    public void setBuyerRealName(String buyerRealName) {
        this.buyerRealName = buyerRealName;
    }

    public String getBeginAddTime() {
        return beginAddTime;
    }

    public void setBeginAddTime(String beginAddTime) {
        this.beginAddTime = beginAddTime;
    }

    private String beginAddTime;

    public String getEndAddTime() {
        return endAddTime;
    }

    public void setEndAddTime(String endAddTime) {
        this.endAddTime = endAddTime;
    }

    private String endAddTime;
}
