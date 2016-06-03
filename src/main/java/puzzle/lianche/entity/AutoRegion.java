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
@Table(name="auto_region")
public class AutoRegion implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoRegion(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoRegion{");
        sb.append("regionId=").append(regionId);
        sb.append(", parentId=").append(parentId);
        sb.append(", regionName=").append(regionName);
        sb.append(", regionType=").append(regionType);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="region_id", unique=true)
	private Integer regionId;
	@Column(name="parent_id")
	private Integer parentId;
	@Column(name="region_name", length=120)
	private String regionName;
	@Column(name="region_type")
	private Integer regionType;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getRegionId(){
		return regionId;
	}
	
	public void setRegionId(Integer regionId){
		this.regionId = regionId;
	}
		
	public Integer getParentId(){
		return parentId;
	}
	
	public void setParentId(Integer parentId){
		this.parentId = parentId;
	}
		
	public String getRegionName(){
		return regionName;
	}
	
	public void setRegionName(String regionName){
		this.regionName = regionName;
	}
		
	public Integer getRegionType(){
		return regionType;
	}
	
	public void setRegionType(Integer regionType){
		this.regionType = regionType;
	}
		
}
