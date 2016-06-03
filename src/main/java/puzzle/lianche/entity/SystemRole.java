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
@Table(name="system_role")
public class SystemRole implements Serializable{ 
	/**
	* Constructor
	*/
	public SystemRole(){
	}

	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("SystemRole{");
        sb.append("roleId=").append(roleId);
        sb.append(", roleName=").append(roleName);
        sb.append(", roleType=").append(roleType);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="role_id", unique=true)
	private Integer roleId;
	@Column(name="role_name", nullable=true, length=20)
	private String roleName;
	@Column(name="role_type", nullable=true)
	private Integer roleType;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getRoleId(){
		return roleId;
	}
	
	public void setRoleId(Integer roleId){
		this.roleId = roleId;
	}
		
	public String getRoleName(){
		return roleName;
	}
	
	public void setRoleName(String roleName){
		this.roleName = roleName;
	}
		
	public Integer getRoleType(){
		return roleType;
	}
	
	public void setRoleType(Integer roleType){
		this.roleType = roleType;
	}
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}

    /**
     * 有参的构造方法
     */
    public SystemRole(Integer roleId,String roleName,Integer roleType,Integer sortOrder){
        this.roleId=roleId;
        this.roleName=roleName;
        this.roleType=roleType;
        this.sortOrder=sortOrder;
    }
}
