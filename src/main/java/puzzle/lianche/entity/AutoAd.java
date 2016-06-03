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
@Table(name="auto_ad")
public class AutoAd implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoAd(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoAd{");
        sb.append("adId=").append(adId);
        sb.append(", adPositionId=").append(adPositionId);
        sb.append(", adLink=").append(adLink);
        sb.append(", addTime=").append(addTime);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", status=").append(status);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="ad_id", unique=true)
	private Integer adId;
	@Column(name="ad_position_id", nullable=true)
	private Integer adPositionId;
	@Column(name="ad_link", nullable=true, length=255)
	private String adLink;
	@Column(name="add_time", nullable=true)
	private Long addTime;
	@Column(name="start_date", nullable=true)
	private Long startDate;
	@Column(name="end_date", nullable=true)
	private Long endDate;
	@Column(name="status", nullable=true)
	private Integer status;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getAdId(){
		return adId;
	}
	
	public void setAdId(Integer adId){
		this.adId = adId;
	}
		
	public Integer getAdPositionId(){
		return adPositionId;
	}
	
	public void setAdPositionId(Integer adPositionId){
		this.adPositionId = adPositionId;
	}
		
	public String getAdLink(){
		return adLink;
	}
	
	public void setAdLink(String adLink){
		this.adLink = adLink;
	}
		
	public Long getAddTime(){
		return addTime;
	}
	
	public void setAddTime(Long addTime){
		this.addTime = addTime;
	}
		
	public Long getStartDate(){
		return startDate;
	}
	
	public void setStartDate(Long startDate){
		this.startDate = startDate;
	}
		
	public Long getEndDate(){
		return endDate;
	}
	
	public void setEndDate(Long endDate){
		this.endDate = endDate;
	}
		
	public Integer getStatus(){
		return status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}

    //新增属性
    private String positionName;
    private Integer width;
    private Integer height;
    private String beginTimeString;
    private String endTimeString;
    private String adPositionIdString;

    public String getAdPositionIdString() {
        return adPositionIdString;
    }

    public void setAdPositionIdString(String adPositionIdString) {
        this.adPositionIdString = adPositionIdString;
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

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
