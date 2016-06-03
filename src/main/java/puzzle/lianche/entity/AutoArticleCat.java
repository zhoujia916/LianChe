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
@Table(name="auto_article_cat")
public class AutoArticleCat implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoArticleCat(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoArticleCat{");
        sb.append("catId=").append(catId);
        sb.append(", catName=").append(catName);
        sb.append(", parentId=").append(parentId);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="cat_id", unique=true)
	private Integer catId;
	@Column(name="cat_name", nullable=true, length=50)
	private String catName;
	@Column(name="parent_id", nullable=true)
	private Integer parentId;
    @Column(name="sort_order", nullable=true)
    private Integer templateId;
    @Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getCatId(){
		return catId;
	}
	
	public void setCatId(Integer catId){
		this.catId = catId;
	}
		
	public String getCatName(){
		return catName;
	}
	
	public void setCatName(String catName){
		this.catName = catName;
	}
		
	public Integer getParentId(){
		return parentId;
	}
	
	public void setParentId(Integer parentId){
		this.parentId = parentId;
	}

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}

    public List<AutoArticleCat> getChildren() {
        return children;
    }

    public void setChildren(List<AutoArticleCat> children) {
        this.children = children;
    }

    private List<AutoArticleCat> children;

    private String parentName;

    public String getParentName(){
        return parentName;
    }

    public void setParentName(String parentName){
        this.parentName = parentName;
    }
}
