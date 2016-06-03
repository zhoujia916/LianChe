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
@Table(name="system_log")
public class SystemLog implements Serializable{ 
	/**
	* Constructor
	*/
	public SystemLog(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("SystemLog{");
        sb.append("logId=").append(logId);
        sb.append(", logType=").append(logType);
        sb.append(", logTime=").append(logTime);
        sb.append(", logIp=").append(logIp);
        sb.append(", logMsg=").append(logMsg);
        sb.append(", userId=").append(userId);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="log_id", unique=true)
	private Integer logId;
	@Column(name="log_type", nullable=true)
	private Integer logType;
	@Column(name="log_time", nullable=true)
	private Long logTime;
	@Column(name="log_ip", nullable=true, length=50)
	private String logIp;
	@Column(name="log_msg", nullable=true, length=21845)
	private String logMsg;
	@Column(name="user_id", nullable=true)
	private Integer userId;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getLogId(){
		return logId;
	}
	
	public void setLogId(Integer logId){
		this.logId = logId;
	}
		
	public Integer getLogType(){
		return logType;
	}
	
	public void setLogType(Integer logType){
		this.logType = logType;
	}
		
	public Long getLogTime(){
		return logTime;
	}
	
	public void setLogTime(Long logTime){
		this.logTime = logTime;
	}
		
	public String getLogIp(){
		return logIp;
	}
	
	public void setLogIp(String logIp){
		this.logIp = logIp;
	}
		
	public String getLogMsg(){
		return logMsg;
	}
	
	public void setLogMsg(String logMsg){
		this.logMsg = logMsg;
	}
		
	public Integer getUserId(){
		return userId;
	}

	public void setUserId(Integer userId){
		this.userId = userId;
	}

    //新增属性
    private String userName;
    private String logTypeId;
    private Long beginTime;
    private Long endTime;
    private String beginTimeString;
    private String endTimeString;

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

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

    public String getLogTypeId() {
        return logTypeId;
    }

    public void setLogTypeId(String logTypeId) {
        this.logTypeId = logTypeId;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }
}
