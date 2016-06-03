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
@Table(name="auto_collect")
public class AutoCollect implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoCollect(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoCollect{");
        sb.append("collectId=").append(collectId);
        sb.append(", userId=").append(userId);
        sb.append(", targetType=").append(targetType);
        sb.append(", targetId=").append(targetId);
        sb.append(", addTime=").append(addTime);
        sb.append(", status=").append(status);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="collect_id", unique=true)
	private Integer collectId;
	@Column(name="user_id", nullable=true)
	private Integer userId;
	@Column(name="target_type", nullable=true)
	private Integer targetType;
	@Column(name="target_id", nullable=true)
	private Integer targetId;
	@Column(name="add_time", nullable=true)
	private Long addTime;
	@Column(name="status", nullable=true)
	private Integer status;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getCollectId(){
		return collectId;
	}
	
	public void setCollectId(Integer collectId){
		this.collectId = collectId;
	}
		
	public Integer getUserId(){
		return userId;
	}
	
	public void setUserId(Integer userId){
		this.userId = userId;
	}
		
	public Integer getTargetType(){
		return targetType;
	}
	
	public void setTargetType(Integer targetType){
		this.targetType = targetType;
	}
		
	public Integer getTargetId(){
		return targetId;
	}
	
	public void setTargetId(Integer targetId){
		this.targetId = targetId;
	}
		
	public Long getAddTime(){
		return addTime;
	}
	
	public void setAddTime(Long addTime){
		this.addTime = addTime;
	}
		
	public Integer getStatus(){
		return status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}

//    新增属性
	private String carName;
    private String userName;
    private String userString;
    private String realName;

    public String getUserString() {
        return userString;
    }

    public void setUserString(String userString) {
        this.userString = userString;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
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
