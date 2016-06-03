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
@Table(name="system_menu")
public class SystemMenu implements Serializable{ 
	/**
	* Constructor
	*/
	public SystemMenu(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("SystemMenu{");
        sb.append("menuId=").append(menuId);
        sb.append(", menuName=").append(menuName);
        sb.append(", menuUrl=").append(menuUrl);
        sb.append(", parentId=").append(parentId);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="menu_id", unique=true)
	private Integer menuId;
	@Column(name="menu_name", nullable=true, length=20)
	private String menuName;
	@Column(name="menu_url", nullable=true, length=200)
    private String menuUrl;

    @Column(name="link_url", nullable=true, length=500)
    private String linkUrl;
    @Column(name="menu_ico", nullable=true, length=100)
    private String menuIco;
	@Column(name="parent_id", nullable=true)
	private Integer parentId;

    @Column(name="status", nullable=true)
    private Integer status;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;


	
	
	/**
	* Getter and Setter
	*/
	public Integer getMenuId(){
		return menuId;
	}
	
	public void setMenuId(Integer menuId){
		this.menuId = menuId;
	}
		
	public String getMenuName(){
		return menuName;
	}
	
	public void setMenuName(String menuName){
		this.menuName = menuName;
	}
		
	public String getMenuUrl(){
		return menuUrl;
	}
	
	public void setMenuUrl(String menuUrl){
		this.menuUrl = menuUrl;
	}


    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getMenuIco(){
        return menuIco;
    }

    public void setMenuIco(String menuIco){
        this.menuIco = menuIco;
    }

	public Integer getParentId(){
		return parentId;
	}
	
	public void setParentId(Integer parentId){
		this.parentId = parentId;
	}

    public Integer getStatus(){
        return status;
    }

    public void setStatus(Integer status){
        this.status = status;
    }
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}


    //region Extension Field
    private String parentName;

    public String getParentName(){
        return parentName;
    }

    public void setParentName(String parentName){
        this.parentName = parentName;
    }

    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<SystemMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SystemMenu> children) {
        this.children = children;
    }

    private List<SystemMenu> children;

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    private String actionCode;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    private String actionName;

    public List<SystemMenuAction> getActions() {
        return actions;
    }

    public void setActions(List<SystemMenuAction> actions) {
        this.actions = actions;
    }

    private List<SystemMenuAction> actions;

    public SystemMenu getParent() {
        return parent;
    }

    public void setParent(SystemMenu parent) {
        this.parent = parent;
    }

    private SystemMenu parent;

    //endregion
}
