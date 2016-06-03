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
@Table(name="system_menu_action")
public class SystemMenuAction implements Serializable{ 
	/**
	* Constructor
	*/
	public SystemMenuAction(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("SystemMenuAction{");
        sb.append("actionId=").append(actionId);
        sb.append(", actionName=").append(actionName);
        sb.append(", menuId=").append(menuId);
        sb.append(", actionConfig=").append(actionConfig);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="action_id", unique=true)
	private Integer actionId;
    @Column(name="action_code", nullable=true, length=50)
    private String actionCode;
	@Column(name="action_name", nullable=true, length=50)
	private String actionName;



    @Column(name="link_url", nullable=true, length=500)
    private String linkUrl;
    @Column(name="action_ico", nullable=true, length=100)
    private String actionIco;
	@Column(name="menu_id", nullable=true)
	private Integer menuId;
	@Column(name="action_config", nullable=true, length=21845)
	private String actionConfig;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getActionId(){
		return actionId;
	}
	
	public void setActionId(Integer actionId){
		this.actionId = actionId;
	}

    public String getActionCode(){
        return actionCode;
    }

    public void setActionCode(String actionCode){
        this.actionCode = actionCode;
    }

	public String getActionName(){
		return actionName;
	}
	
	public void setActionName(String actionName){
		this.actionName = actionName;
	}
		
	public Integer getMenuId(){
		return menuId;
	}
	
	public void setMenuId(Integer menuId){
		this.menuId = menuId;
	}
		
	public String getActionConfig(){
		return actionConfig;
	}
	
	public void setActionConfig(String actionConfig){
		this.actionConfig = actionConfig;
	}
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public String getActionIco() {
        return actionIco;
    }

    public void setActionIco(String actionIco) {
        this.actionIco = actionIco;
    }
		
}
