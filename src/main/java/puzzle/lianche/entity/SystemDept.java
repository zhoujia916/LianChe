package puzzle.lianche.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
@Table(name="system_dept")
public class SystemDept implements Serializable{
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("SystemDept{");
        sb.append("deptId=").append(deptId);
        sb.append(", deptName=").append(deptName);
        sb.append(", parentId=").append(parentId);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="dept_id", unique=true)
	private Integer deptId;
	@Column(name="dept_name", nullable=true, length=50)
	private String deptName;
	@Column(name="parent_id", nullable=true)
	private Integer parentId;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    private String parentName;

    public List<SystemDept> getChildren() {
        return children;
    }

    public void setChildren(List<SystemDept> children) {
        this.children = children;
    }

    private List<SystemDept> children;

	
	
	/**
	* Getter and Setter
	*/
	public Integer getDeptId(){
		return deptId;
	}
	
	public void setDeptId(Integer deptId){
		this.deptId = deptId;
	}
		
	public String getDeptName(){
		return deptName;
	}
	
	public void setDeptName(String deptName){
		this.deptName = deptName;
	}

		
	public Integer getParentId(){
		return parentId;
	}
	
	public void setParentId(Integer parentId){
		this.parentId = parentId;
	}
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}
		
}
