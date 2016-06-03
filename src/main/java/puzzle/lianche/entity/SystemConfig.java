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
@Table(name="system_config")
public class SystemConfig implements Serializable{ 
	/**
	* Constructor
	*/
	public SystemConfig(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("SystemConfig{");
        sb.append("configId=").append(configId);
        sb.append(", code=").append(code);
        sb.append(", name=").append(name);
        sb.append(", value=").append(value);
        sb.append(", valueType=").append(valueType);
        sb.append(", valueRange=").append(valueRange);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append(", groupId=").append(groupId);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="config_id", unique=true)
	private Integer configId;
	@Column(name="code", nullable=true, length=50)
	private String code;
	@Column(name="name", nullable=true, length=50)
	private String name;
	@Column(name="value", nullable=true, length=21845)
	private String value;
	@Column(name="value_type", nullable=true)
	private Integer valueType;
	@Column(name="value_range", nullable=true, length=21845)
	private String valueRange;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	@Column(name="group_id", nullable=true)
	private Integer groupId;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getConfigId(){
		return configId;
	}
	
	public void setConfigId(Integer configId){
		this.configId = configId;
	}
		
	public String getCode(){
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
		
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
		
	public String getValue(){
		return value;
	}
	
	public void setValue(String value){
		this.value = value;
	}
		
	public Integer getValueType(){
		return valueType;
	}
	
	public void setValueType(Integer valueType){
		this.valueType = valueType;
	}
		
	public String getValueRange(){
		return valueRange;
	}
	
	public void setValueRange(String valueRange){
		this.valueRange = valueRange;
	}
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}
		
	public Integer getGroupId(){
		return groupId;
	}
	
	public void setGroupId(Integer groupId){
		this.groupId = groupId;
	}
		
}
