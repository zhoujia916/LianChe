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
@Table(name="auto_sms")
public class AutoSms implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoSms(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoSms{");
        sb.append("smsId=").append(smsId);
        sb.append(", smsType=").append(smsType);
        sb.append(", smsContent=").append(smsContent);
        sb.append(", code=").append(code);
        sb.append(", phone=").append(phone);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="sms_id", unique=true)
	private Integer smsId;
	@Column(name="sms_type", nullable=true)
	private Integer smsType;
	@Column(name="sms_content", nullable=true, length=500)
	private String smsContent;
	@Column(name="code", nullable=true, length=10)
	private String code;
	@Column(name="phone", nullable=true, length=20)
	private String phone;
    private Integer status;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getSmsId(){
		return smsId;
	}
	
	public void setSmsId(Integer smsId){
		this.smsId = smsId;
	}
		
	public Integer getSmsType(){
		return smsType;
	}
	
	public void setSmsType(Integer smsType){
		this.smsType = smsType;
	}
		
	public String getSmsContent(){
		return smsContent;
	}
	
	public void setSmsContent(String smsContent){
		this.smsContent = smsContent;
	}
		
	public String getCode(){
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
		
	public String getPhone(){
		return phone;
	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    //新增属性
    private String toUserId;

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
}
