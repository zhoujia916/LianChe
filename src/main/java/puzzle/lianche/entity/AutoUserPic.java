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
@Table(name="auto_user_pic")
public class AutoUserPic implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoUserPic(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoUserPic{");
        sb.append("picId=").append(picId);
        sb.append(", userId=").append(userId);
        sb.append(", picType=").append(picType);
        sb.append(", picPath=").append(picPath);
        sb.append(", addTime=").append(addTime);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="pic_id", unique=true)
	private Integer picId;
	@Column(name="user_id", nullable=true)
	private Integer userId;
	@Column(name="pic_type", nullable=true)
	private Integer picType;
	@Column(name="pic_path", nullable=true, length=255)
	private String picPath;
	@Column(name="add_time", nullable=true)
	private Long addTime;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getPicId(){
		return picId;
	}
	
	public void setPicId(Integer picId){
		this.picId = picId;
	}
		
	public Integer getUserId(){
		return userId;
	}
	
	public void setUserId(Integer userId){
		this.userId = userId;
	}
		
	public Integer getPicType(){
		return picType;
	}
	
	public void setPicType(Integer picType){
		this.picType = picType;
	}
		
	public String getPicPath(){
		return picPath;
	}
	
	public void setPicPath(String picPath){
		this.picPath = picPath;
	}
		
	public Long getAddTime(){
		return addTime;
	}
	
	public void setAddTime(Long addTime){
		this.addTime = addTime;
	}
		
}
