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
@Table(name="auto_article")
public class AutoArticle implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoArticle(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoArticle{");
        sb.append("articleId=").append(articleId);
        sb.append(", title=").append(title);
        sb.append(", title2=").append(title2);
        sb.append(", catId=").append(catId);
        sb.append(", cover=").append(cover);
        sb.append(", content=").append(content);
        sb.append(", sourceUrl=").append(sourceUrl);
        sb.append(", addTime=").append(addTime);
        sb.append(", addUserId=").append(addUserId);
        sb.append(", checkTime=").append(checkTime);
        sb.append(", status=").append(status);
        sb.append(", checkUserId=").append(checkUserId);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="article_id", unique=true)
	private Integer articleId;
	@Column(name="title", nullable=true, length=255)
	private String title;
	@Column(name="title2", nullable=true, length=255)
	private String title2;
	@Column(name="cat_id", nullable=true)
	private Integer catId;
	@Column(name="cover", nullable=true, length=255)
	private String cover;
	@Column(name="content", nullable=true, length=21845)
	private String content;
	@Column(name="source_url", nullable=true, length=255)
	private String sourceUrl;
	@Column(name="add_time", nullable=true)
	private Long addTime;
	@Column(name="add_user_id", nullable=true)
	private Integer addUserId;
	@Column(name="check_time", nullable=true)
	private Long checkTime;
	@Column(name="status", nullable=true)
	private Integer status;
	@Column(name="check_user_id", nullable=true)
	private Integer checkUserId;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getArticleId(){
		return articleId;
	}
	
	public void setArticleId(Integer articleId){
		this.articleId = articleId;
	}
		
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
		
	public String getTitle2(){
		return title2;
	}
	
	public void setTitle2(String title2){
		this.title2 = title2;
	}
		
	public Integer getCatId(){
		return catId;
	}
	
	public void setCatId(Integer catId){
		this.catId = catId;
	}
		
	public String getCover(){
		return cover;
	}
	
	public void setCover(String cover){
		this.cover = cover;
	}
		
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
		
	public String getSourceUrl(){
		return sourceUrl;
	}
	
	public void setSourceUrl(String sourceUrl){
		this.sourceUrl = sourceUrl;
	}
		
	public Long getAddTime(){
		return addTime;
	}
	
	public void setAddTime(Long addTime){
		this.addTime = addTime;
	}
		
	public Integer getAddUserId(){
		return addUserId;
	}
	
	public void setAddUserId(Integer addUserId){
		this.addUserId = addUserId;
	}
		
	public Long getCheckTime(){
		return checkTime;
	}
	
	public void setCheckTime(Long checkTime){
		this.checkTime = checkTime;
	}
		
	public Integer getStatus(){
		return status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}
		
	public Integer getCheckUserId(){
		return checkUserId;
	}
	
	public void setCheckUserId(Integer checkUserId){
		this.checkUserId = checkUserId;
	}
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}

    //新增属性
    private String catName;
    private String userName;
    private String name;
    private String beginTimeString;
    private String endTimeString;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeginTimeString() {
        return beginTimeString;
    }

    public void setBeginTimeString(String beginTimeString) {
        this.beginTimeString = beginTimeString;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
