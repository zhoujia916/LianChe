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
@Table(name="auto_msg")
public class AutoMsg implements Serializable{ 
	/**
	* Constructor
	*/
	public AutoMsg(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("AutoMsg{");
        sb.append("msgId=").append(msgId);
        sb.append(", msgTitle=").append(msgTitle);
        sb.append(", msgContent=").append(msgContent);
        sb.append(", msgType=").append(msgType);
        sb.append(", msgContentType=").append(msgContentType);
        sb.append(", msgAuthor=").append(msgAuthor);
        sb.append(", msgUrl=").append(msgUrl);
        sb.append(", addTime=").append(addTime);
        sb.append(", fromUserId=").append(fromUserId);
        sb.append(", toUserId=").append(toUserId);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", status=").append(status);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="msg_id", unique=true)
	private Integer msgId;
	@Column(name="msg_title", nullable=true, length=50)
	private String msgTitle;
	@Column(name="msg_content", nullable=true, length=21845)
	private String msgContent;
	@Column(name="msg_type", nullable=true)
	private Integer msgType;
	@Column(name="msg_content_type", nullable=true)
	private Integer msgContentType;
	@Column(name="msg_author", nullable=true, length=50)
	private String msgAuthor;
	@Column(name="msg_url", nullable=true, length=255)
	private String msgUrl;
	@Column(name="add_time", nullable=true)
	private Long addTime;
	@Column(name="from_user_id", nullable=true)
	private Integer fromUserId;
	@Column(name="to_user_id", nullable=true)
	private Integer toUserId;
	@Column(name="view_count", nullable=true)
	private Integer viewCount;
	@Column(name="status", nullable=true)
	private Integer status;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getMsgId(){
		return msgId;
	}
	
	public void setMsgId(Integer msgId){
		this.msgId = msgId;
	}
		
	public String getMsgTitle(){
		return msgTitle;
	}
	
	public void setMsgTitle(String msgTitle){
		this.msgTitle = msgTitle;
	}
		
	public String getMsgContent(){
		return msgContent;
	}
	
	public void setMsgContent(String msgContent){
		this.msgContent = msgContent;
	}
		
	public Integer getMsgType(){
		return msgType;
	}
	
	public void setMsgType(Integer msgType){
		this.msgType = msgType;
	}
		
	public Integer getMsgContentType(){
		return msgContentType;
	}
	
	public void setMsgContentType(Integer msgContentType){
		this.msgContentType = msgContentType;
	}
		
	public String getMsgAuthor(){
		return msgAuthor;
	}
	
	public void setMsgAuthor(String msgAuthor){
		this.msgAuthor = msgAuthor;
	}
		
	public String getMsgUrl(){
		return msgUrl;
	}
	
	public void setMsgUrl(String msgUrl){
		this.msgUrl = msgUrl;
	}
		
	public Long getAddTime(){
		return addTime;
	}
	
	public void setAddTime(Long addTime){
		this.addTime = addTime;
	}
		
	public Integer getFromUserId(){
		return fromUserId;
	}
	
	public void setFromUserId(Integer fromUserId){
		this.fromUserId = fromUserId;
	}
		
	public Integer getToUserId(){
		return toUserId;
	}
	
	public void setToUserId(Integer toUserId){
		this.toUserId = toUserId;
	}
		
	public Integer getViewCount(){
		return viewCount;
	}
	
	public void setViewCount(Integer viewCount){
		this.viewCount = viewCount;
	}
		
	public Integer getStatus(){
		return status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}

    //新增属性
    private String fromUserName;
    private String fromRealName;
    private String toUserName;
    private String toUserIds;
    private String toRealName;
    private String beginTimeString;
    private String endTimeString;

    public String getToUserIds() {
        return toUserIds;
    }

    public void setToUserIds(String toUserIds) {
        this.toUserIds = toUserIds;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromRealName() {
        return fromRealName;
    }

    public void setFromRealName(String fromRealName) {
        this.fromRealName = fromRealName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getToRealName() {
        return toRealName;
    }

    public void setToRealName(String toRealName) {
        this.toRealName = toRealName;
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
		
}
