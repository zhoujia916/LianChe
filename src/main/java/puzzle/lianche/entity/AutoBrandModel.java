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
@Table(name="auto_brand_model")
public class AutoBrandModel implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoBrandModel(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoBrandModel{");
        sb.append("modelId=").append(modelId);
        sb.append(", brandCatId=").append(brandCatId);
        sb.append(", modelName=").append(modelName);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="model_id", unique=true)
	private Integer modelId;
	@Column(name="brand_cat_id", nullable=true)
	private Integer brandCatId;
	@Column(name="model_name", nullable=true, length=50)
	private String modelName;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getModelId(){
		return modelId;
	}
	
	public void setModelId(Integer modelId){
		this.modelId = modelId;
	}
		
	public Integer getBrandCatId(){
		return brandCatId;
	}
	
	public void setBrandCatId(Integer brandCatId){
		this.brandCatId = brandCatId;
	}
		
	public String getModelName(){
		return modelName;
	}
	
	public void setModelName(String modelName){
		this.modelName = modelName;
	}
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}

    //新增属性
    private String catName;
    private String brandName;
    private String brands;
    private String cats;
    private String catString;

    public String getCatString() {
        return catString;
    }

    public void setCatString(String catString) {
        this.catString = catString;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public String getCats() {
        return cats;
    }

    public void setCats(String cats) {
        this.cats = cats;
    }
}
