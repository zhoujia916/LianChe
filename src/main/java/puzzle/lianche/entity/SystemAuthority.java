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
@Table(name="system_authority")
public class SystemAuthority implements Serializable{ 
	/**
	* Constructor
	*/
	public SystemAuthority(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("SystemAuthority{");
        sb.append("authorityId=").append(authorityId);
        sb.append(", sourceId=").append(sourceId);
        sb.append(", sourceType=").append(sourceType);
        sb.append(", targetId=").append(targetId);
        sb.append(", targetType=").append(targetType);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="authority_id", unique=true)
	private Integer authorityId;
	@Column(name="source_id", nullable=true)
	private Integer sourceId;
	@Column(name="source_type", nullable=true)
	private Integer sourceType;
	@Column(name="target_id", nullable=true)
	private Integer targetId;
	@Column(name="target_type", nullable=true)
	private Integer targetType;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getAuthorityId(){
		return authorityId;
	}
	
	public void setAuthorityId(Integer authorityId){
		this.authorityId = authorityId;
	}
		
	public Integer getSourceId(){
		return sourceId;
	}
	
	public void setSourceId(Integer sourceId){
		this.sourceId = sourceId;
	}
		
	public Integer getSourceType(){
		return sourceType;
	}
	
	public void setSourceType(Integer sourceType){
		this.sourceType = sourceType;
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


    private String target;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    private String menuUrl;

    public String getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(String menuLink) {
        this.menuLink = menuLink;
    }

    private String menuLink;

    public String getActionLink() {
        return actionLink;
    }

    public void setActionLink(String actionLink) {
        this.actionLink = actionLink;
    }

    private String actionLink;
}
