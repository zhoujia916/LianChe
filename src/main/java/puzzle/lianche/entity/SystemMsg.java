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
@Table(name="system_msg")
public class SystemMsg implements Serializable{ 
	/**
	* Constructor
	*/
	public SystemMsg(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("SystemMsg{");
        sb.append("msgId=").append(msgId);
        sb.append(", content=").append(content);
        sb.append(", addTime=").append(addTime);
        sb.append(", sourceUser=").append(sourceUser);
        sb.append(", targetUser=").append(targetUser);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="msg_id", unique=true)
	private Integer msgId;
	@Column(name="content", nullable=true, length=500)
	private String content;
	@Column(name="add_time", nullable=true)
	private Integer addTime;
	@Column(name="source_user", nullable=true)
	private Integer sourceUser;
	@Column(name="target_user", nullable=true)
	private Integer targetUser;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getMsgId(){
		return msgId;
	}
	
	public void setMsgId(Integer msgId){
		this.msgId = msgId;
	}
		
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
		
	public Integer getAddTime(){
		return addTime;
	}
	
	public void setAddTime(Integer addTime){
		this.addTime = addTime;
	}
		
	public Integer getSourceUser(){
		return sourceUser;
	}
	
	public void setSourceUser(Integer sourceUser){
		this.sourceUser = sourceUser;
	}
		
	public Integer getTargetUser(){
		return targetUser;
	}
	
	public void setTargetUser(Integer targetUser){
		this.targetUser = targetUser;
	}

}
