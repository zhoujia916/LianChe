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
@Table(name="auto_attr")
public class AutoAttr implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoAttr(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoAttr{");
        sb.append("attrId=").append(attrId);
        sb.append(", attrName=").append(attrName);
        sb.append(", attrInputType=").append(attrInputType);
        sb.append(", attrType=").append(attrType);
        sb.append(", attrValues=").append(attrValues);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="attr_id", unique=true)
	private Integer attrId;
	@Column(name="attr_name", nullable=true, length=50)
	private String attrName;
	@Column(name="attr_input_type", nullable=true)
	private Integer attrInputType;
	@Column(name="attr_type", nullable=true)
	private Integer attrType;
	@Column(name="attr_values", nullable=true, length=500)
	private String attrValues;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getAttrId(){
		return attrId;
	}
	
	public void setAttrId(Integer attrId){
		this.attrId = attrId;
	}
		
	public String getAttrName(){
		return attrName;
	}
	
	public void setAttrName(String attrName){
		this.attrName = attrName;
	}
		
	public Integer getAttrInputType(){
		return attrInputType;
	}
	
	public void setAttrInputType(Integer attrInputType){
		this.attrInputType = attrInputType;
	}
		
	public Integer getAttrType(){
		return attrType;
	}
	
	public void setAttrType(Integer attrType){
		this.attrType = attrType;
	}
		
	public String getAttrValues(){
		return attrValues;
	}
	
	public void setAttrValues(String attrValues){
		this.attrValues = attrValues;
	}
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}

    //新增属性
    private String typeString;

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }
}
