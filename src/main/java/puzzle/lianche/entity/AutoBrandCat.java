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
@Table(name="auto_brand_cat")
public class AutoBrandCat implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoBrandCat(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoBrandCat{");
        sb.append("catId=").append(catId);
        sb.append(", brandId=").append(brandId);
        sb.append(", catName=").append(catName);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="cat_id", unique=true)
	private Integer catId;
	@Column(name="brand_id", nullable=true)
	private Integer brandId;
	@Column(name="cat_name", nullable=true, length=50)
	private String catName;
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
		
	public Integer getBrandId(){
		return brandId;
	}
	
	public void setBrandId(Integer brandId){
		this.brandId = brandId;
	}
		
	public String getCatName(){
		return catName;
	}
	
	public void setCatName(String catName){
		this.catName = catName;
	}
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}

    //新增属性

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }
    private String brandName;
    private String brandString;
    private String brands;

    public String getBrandString() {
        return brandString;
    }

    public void setBrandString(String brandString) {
        this.brandString = brandString;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

}
