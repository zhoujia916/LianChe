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
@Table(name="system_user")
public class SystemUser implements Serializable{ 
	/**
	* Constructor
	*/
	public SystemUser(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("SystemUser{");
        sb.append("userId=").append(userId);
        sb.append(", loginName=").append(loginName);
        sb.append(", userName=").append(userName);
        sb.append(", userAvatar=").append(userAvatar);
        sb.append(", password=").append(password);
        sb.append(", sex=").append(sex);
        sb.append(", birthday=").append(birthday);
        sb.append(", lastVisitTime=").append(lastVisitTime);
        sb.append(", lastVisitIp=").append(lastVisitIp);
        sb.append(", onlineTime=").append(onlineTime);
        sb.append(", userType=").append(userType);
        sb.append(", title=").append(title);
        sb.append(", status=").append(status);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="user_id", unique=true)
	private Integer userId;
	@Column(name="login_name", unique=true, length=20)
	private String loginName;
	@Column(name="user_name", nullable=true, length=20)
	private String userName;
	@Column(name="user_avatar", nullable=true, length=50)
	private String userAvatar;
	@Column(name="password", nullable=true, length=50)
	private String password;
	@Column(name="sex", nullable=true)
	private Integer sex;
	@Column(name="birthday", nullable=true,columnDefinition="TIMESTAMP")
	private Long birthday;
	@Column(name="last_visit_time", nullable=true)
	private Integer lastVisitTime;
	@Column(name="last_visit_ip", nullable=true, length=50)
	private String lastVisitIp;
	@Column(name="online_time", nullable=true)
	private Integer onlineTime;
	@Column(name="user_type", nullable=true)
	private Integer userType;
	@Column(name="title", nullable=true, length=20)
	private String title;
	@Column(name="status", nullable=true)
	private Integer status;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getUserId(){
		return userId;
	}
	
	public void setUserId(Integer userId){
		this.userId = userId;
	}
		
	public String getLoginName(){
		return loginName;
	}
	
	public void setLoginName(String loginName){
		this.loginName = loginName;
	}
		
	public String getUserName(){
		return userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
		
	public String getUserAvatar(){
		return userAvatar;
	}
	
	public void setUserAvatar(String userAvatar){
		this.userAvatar = userAvatar;
	}
		
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
		
	public Integer getSex(){
		return sex;
	}
	
	public void setSex(Integer sex){
		this.sex = sex;
	}
		
	public Long getBirthday(){
		return birthday;
	}
	
	public void setBirthday(Long birthday){
		this.birthday = birthday;
	}
		
	public Integer getLastVisitTime(){
		return lastVisitTime;
	}
	
	public void setLastVisitTime(Integer lastVisitTime){
		this.lastVisitTime = lastVisitTime;
	}
		
	public String getLastVisitIp(){
		return lastVisitIp;
	}
	
	public void setLastVisitIp(String lastVisitIp){
		this.lastVisitIp = lastVisitIp;
	}
		
	public Integer getOnlineTime(){
		return onlineTime;
	}
	
	public void setOnlineTime(Integer onlineTime){
		this.onlineTime = onlineTime;
	}
		
	public Integer getUserType(){
		return userType;
	}
	
	public void setUserType(Integer userType){
		this.userType = userType;
	}
		
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
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

    //region 扩展属性
    public List<SystemAuthority> getAuthorities() {
        return authorities;
    }
    public void setAuthorities(List<SystemAuthority> authorities) {
        this.authorities = authorities;
    }

    private List<SystemAuthority> authorities;

    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<SystemMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<SystemMenu> menus) {
        this.menus = menus;
    }

    private List<SystemMenu> menus;

    //endregion

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    private String roleId;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    private String deptId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    private String groupId;

    public List<SystemRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SystemRole> roles) {
        this.roles = roles;
    }

    private List<SystemRole> roles;

    public List<SystemDept> getDepts() {
        return depts;
    }

    public void setDepts(List<SystemDept> depts) {
        this.depts = depts;
    }

    private List<SystemDept> depts;

    public List<SystemUserGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<SystemUserGroup> groups) {
        this.groups = groups;
    }

    private List<SystemUserGroup> groups;
}
