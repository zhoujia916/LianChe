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
@Table(name="auto_feedback")
public class AutoFeedback implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoFeedback(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoFeedback{");
        sb.append("feedbackId=").append(feedbackId);
        sb.append(", content=").append(content);
        sb.append(", pic=").append(pic);
        sb.append(", addTime=").append(addTime);
        sb.append(", addUserId=").append(addUserId);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="feedback_id", unique=true)
	private Integer feedbackId;
	@Column(name="content", nullable=true, length=500)
	private String content;
	@Column(name="pic", nullable=true, length=1000)
	private String pic;
	@Column(name="add_time", nullable=true)
	private Long addTime;
	@Column(name="add_user_id", nullable=true)
	private Integer addUserId;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getFeedbackId(){
		return feedbackId;
	}
	
	public void setFeedbackId(Integer feedbackId){
		this.feedbackId = feedbackId;
	}
		
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
		
	public String getPic(){
		return pic;
	}
	
	public void setPic(String pic){
		this.pic = pic;
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

    //新增属性
    private String userName;
    private String realName;

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
