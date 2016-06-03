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
@Table(name="auto_car_pic")
public class AutoCarPic implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoCarPic(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoCarPic{");
        sb.append("picId=").append(picId);
        sb.append(", carId=").append(carId);
        sb.append(", path=").append(path);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="pic_id", nullable=true)
	private Integer picId;
	@Column(name="car_id", nullable=true)
	private Integer carId;
	@Column(name="path", nullable=true, length=255)
	private String path;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getPicId(){
		return picId;
	}
	
	public void setPicId(Integer picId){
		this.picId = picId;
	}
		
	public Integer getCarId(){
		return carId;
	}
	
	public void setCarId(Integer carId){
		this.carId = carId;
	}
		
	public String getPath(){
		return path;
	}
	
	public void setPath(String path){
		this.path = path;
	}
		
}
