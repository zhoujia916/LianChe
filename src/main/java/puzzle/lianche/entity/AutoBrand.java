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
@Table(name="auto_brand")
public class AutoBrand implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoBrand(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoBrand{");
        sb.append("brandId=").append(brandId);
        sb.append(", brandName=").append(brandName);
        sb.append(", brandLogo=").append(brandLogo);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="brand_id", unique=true)
	private Integer brandId;
	@Column(name="brand_name", nullable=true, length=50)
	private String brandName;
	@Column(name="brand_logo", nullable=true, length=255)
	private String brandLogo;
    @Column(name="brand_logo2", nullable=true, length=255)
    private String brandLogo2;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getBrandId(){
		return brandId;
	}
	
	public void setBrandId(Integer brandId){
		this.brandId = brandId;
	}
		
	public String getBrandName(){
		return brandName;
	}
	
	public void setBrandName(String brandName){
		this.brandName = brandName;
	}
		
	public String getBrandLogo(){
		return brandLogo;
	}
	
	public void setBrandLogo(String brandLogo){
		this.brandLogo = brandLogo;
	}

    public String getBrandLogo2() {
        return brandLogo2;
    }

    public void setBrandLogo2(String brandLogo2) {
        this.brandLogo2 = brandLogo2;
    }

    public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}
}
