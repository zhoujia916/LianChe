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
@Table(name="auto_car_attr")
public class AutoCarAttr implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoCarAttr(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoCarAttr{");
        sb.append("carAttrId=").append(carAttrId);
        sb.append(", carId=").append(carId);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="car_attr_id", unique=true)
	private Integer carAttrId;
	@Column(name="car_id", nullable=true)
	private Integer carId;
	@Column(name="attr_id", nullable=true)
    private Integer outsideColor;
    private Integer insideColor;
    private Integer quoteType;
    private Integer salePriceType;
    private Double saleAmount;
    private Integer totalNumber;

    private Integer lockNumber;

    private Integer surplusNumber;

    private Double price;






	
	
	/**
	* Getter and Setter
	*/
	public Integer getCarAttrId(){
		return carAttrId;
	}
	
	public void setCarAttrId(Integer carAttrId){
		this.carAttrId = carAttrId;
	}
		
	public Integer getCarId(){
		return carId;
	}
	
	public void setCarId(Integer carId){
		this.carId = carId;
	}

    public Integer getOutsideColor() {
        return outsideColor;
    }

    public void setOutsideColor(Integer outsideColor) {
        this.outsideColor = outsideColor;
    }

    public Integer getInsideColor() {
        return insideColor;
    }

    public void setInsideColor(Integer insideColor) {
        this.insideColor = insideColor;
    }

    public Integer getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(Integer quoteType) {
        this.quoteType = quoteType;
    }



    public Integer getSalePriceType() {
        return salePriceType;
    }

    public void setSalePriceType(Integer salePriceType) {
        this.salePriceType = salePriceType;
    }



    public Double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Double saleAmount) {
        this.saleAmount = saleAmount;
    }



    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }



    public Integer getLockNumber() {
        return lockNumber;
    }

    public void setLockNumber(Integer lockNumber) {
        this.lockNumber = lockNumber;
    }

    public Integer getSurplusNumber() {
        return surplusNumber;
    }

    public void setSurplusNumber(Integer surplusNumber) {
        this.surplusNumber = surplusNumber;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

		
}
