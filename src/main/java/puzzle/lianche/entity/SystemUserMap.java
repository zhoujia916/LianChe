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
@Table(name="system_user_map")
public class SystemUserMap implements Serializable{ 
	/**
	* Constructor
	*/
	public SystemUserMap(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("SystemUserMap{");
        sb.append("mapId=").append(mapId);
        sb.append(", userId=").append(userId);
        sb.append(", targetId=").append(targetId);
        sb.append(", targetType=").append(targetType);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="map_id", unique=true)
	private Integer mapId;
	@Column(name="user_id", nullable=true)
	private Integer userId;
	@Column(name="target_id", nullable=true)
	private Integer targetId;
	@Column(name="target_type", nullable=true)
	private Integer targetType;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getMapId(){
		return mapId;
	}
	
	public void setMapId(Integer mapId){
		this.mapId = mapId;
	}
		
	public Integer getUserId(){
		return userId;
	}
	
	public void setUserId(Integer userId){
		this.userId = userId;
	}
		
	public Integer getTargetId(){
		return targetId;
	}
	
	public void setTargetId(Integer targetId){
		this.targetId = targetId;
	}
		
	public Integer getTargetType(){
		return targetType;
	}
	
	public void setTargetType(Integer targetType){
		this.targetType = targetType;
	}
		
}
