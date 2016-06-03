package puzzle.lianche.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="auto_article_template")
public class AutoArticleTemplate implements Serializable{
	/**
	* Constructor
	*/
	public AutoArticleTemplate(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoArticleTemplate{");
        sb.append("templateId=").append(templateId);
        sb.append(", name=").append(name);
        sb.append(", content=").append(content);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="template_id", unique=true)
	private Integer templateId;
	@Column(name="template_name", nullable=true, length=50)
	private String name;
	@Column(name="parent_id", nullable=true)
	private String content;
    @Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getTemplateId(){
		return templateId;
	}
	
	public void setTemplateId(Integer templateId){
		this.templateId = templateId;
	}
		
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
		
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}

    public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}
}
