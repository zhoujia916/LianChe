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
@Table(name="auto_pay")
public class AutoPay implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoPay(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoPay{");
        sb.append("payId=").append(payId);
        sb.append(", amount=").append(amount);
        sb.append(", fromAccount=").append(fromAccount);
        sb.append(", fromUserId=").append(fromUserId);
        sb.append(", fromAccountType=").append(fromAccountType);
        sb.append(", toAccount=").append(toAccount);
        sb.append(", toUserId=").append(toUserId);
        sb.append(", toAccountType=").append(toAccountType);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", addTime=").append(addTime);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="pay_id", unique=true)
	private Integer payId;
	@Column(name="amount", nullable=true)
	private Float amount;
	@Column(name="from_account", nullable=true, length=50)
	private String fromAccount;
	@Column(name="from_user_id", nullable=true)
	private Integer fromUserId;
	@Column(name="from_account_type", nullable=true)
	private Integer fromAccountType;
	@Column(name="to_account", nullable=true, length=50)
	private String toAccount;
	@Column(name="to_user_id", nullable=true)
	private Integer toUserId;
	@Column(name="to_account_type", nullable=true)
	private Integer toAccountType;
	@Column(name="type", nullable=true)
	private Integer type;
	@Column(name="status", nullable=true)
	private Integer status;
	@Column(name="add_time", nullable=true)
	private Long addTime;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getPayId(){
		return payId;
	}
	
	public void setPayId(Integer payId){
		this.payId = payId;
	}
		
	public Float getAmount(){
		return amount;
	}
	
	public void setAmount(Float amount){
		this.amount = amount;
	}
		
	public String getFromAccount(){
		return fromAccount;
	}
	
	public void setFromAccount(String fromAccount){
		this.fromAccount = fromAccount;
	}
		
	public Integer getFromUserId(){
		return fromUserId;
	}
	
	public void setFromUserId(Integer fromUserId){
		this.fromUserId = fromUserId;
	}
		
	public Integer getFromAccountType(){
		return fromAccountType;
	}
	
	public void setFromAccountType(Integer fromAccountType){
		this.fromAccountType = fromAccountType;
	}
		
	public String getToAccount(){
		return toAccount;
	}
	
	public void setToAccount(String toAccount){
		this.toAccount = toAccount;
	}
		
	public Integer getToUserId(){
		return toUserId;
	}
	
	public void setToUserId(Integer toUserId){
		this.toUserId = toUserId;
	}
		
	public Integer getToAccountType(){
		return toAccountType;
	}
	
	public void setToAccountType(Integer toAccountType){
		this.toAccountType = toAccountType;
	}
		
	public Integer getType(){
		return type;
	}
	
	public void setType(Integer type){
		this.type = type;
	}
		
	public Integer getStatus(){
		return status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}
		
	public Long getAddTime(){
		return addTime;
	}
	
	public void setAddTime(Long addTime){
		this.addTime = addTime;
	}
		
}
