package puzzle.lianche.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="auto_car")
public class AutoCar implements Serializable{
    /**
     * Constructor
     */
    public AutoCar(){
    }

    /**
     * Override toString method
     */
    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("AutoCar{");
        sb.append("carId=").append(carId);
        sb.append(", carName=").append(carName);
        sb.append(", brandId=").append(brandId);
        sb.append(", brandCatId=").append(brandCatId);
        sb.append(", brandModelId=").append(brandModelId);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", area=").append(area);
        sb.append(", addTime=").append(addTime);
        sb.append(", addUserId=").append(addUserId);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", carType=").append(carType);
        sb.append(", status=").append(status);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
    }

    /**
     * Fields
     */
    @Column(name="car_id", unique=true)
    private Integer carId;
    @Column(name="car_name", nullable=true, length=255)
    private String carName;
    @Column(name="brand_id", nullable=true)
    private Integer brandId;
    @Column(name="brand_cat_id", nullable=true)
    private Integer brandCatId;
    @Column(name="brand_model_id", nullable=true)
    private Integer brandModelId;
    @Column(name="province", nullable=true)
    private Integer province;
    @Column(name="city", nullable=true)
    private Integer city;
    @Column(name="area", nullable=true)
    private Integer area;
    @Column(name="has_parts", nullable=true)
    private Integer hasParts;
    @Column(name="parts", nullable=true)
    private String parts;
    @Column(name="parts_price", nullable=true)
    private double partsPrice;
    @Column(name="add_time", nullable=true)
    private Long addTime;
    @Column(name="add_user_id", nullable=true)
    private Integer addUserId;
    @Column(name="start_date", nullable=true)
    private Long startDate;
    @Column(name="end_date", nullable=true)
    private Long endDate;
    @Column(name="car_type", nullable=true)
    private Integer carType;
    @Column(name="status", nullable=true)
    private Integer status;
    @Column(name="sort_order", nullable=true)
    private Integer sortOrder;
    @Column(name="remark", nullable=true)
    private String remark;
    private Long refreshTime;

    public Double getOfficalPrice() {
        return officalPrice;
    }

    public void setOfficalPrice(Double officalPrice) {
        this.officalPrice = officalPrice;
    }

    private Double officalPrice;

    public Long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Long refreshTime) {
        this.refreshTime = refreshTime;
    }

    /**
     * Getter and Setter
     */

    public Integer getCarId(){
        return carId;
    }

    public void setCarId(Integer carId){
        this.carId = carId;
    }

    public String getCarName(){
        return carName;
    }

    public void setCarName(String carName){
        this.carName = carName;
    }

    public Integer getBrandId(){
        return brandId;
    }

    public void setBrandId(Integer brandId){
        this.brandId = brandId;
    }

    public Integer getBrandCatId(){
        return brandCatId;
    }

    public void setBrandCatId(Integer brandCatId){
        this.brandCatId = brandCatId;
    }

    public Integer getBrandModelId(){
        return brandModelId;
    }

    public void setBrandModelId(Integer brandModelId){
        this.brandModelId = brandModelId;
    }

    public Integer getProvince(){
        return province;
    }

    public void setProvince(Integer province){
        this.province = province;
    }

    public Integer getCity(){
        return city;
    }

    public void setCity(Integer city){
        this.city = city;
    }

    public Integer getArea(){
        return area;
    }

    public void setArea(Integer area){
        this.area = area;
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

    public Integer getCarType(){
        return carType;
    }

    public void setCarType(Integer carType){
        this.carType = carType;
    }

    public Integer getStatus(){
        return status;
    }

    public void setStatus(Integer status){
        this.status = status;
    }

    public Integer getSortOrder(){
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder){
        this.sortOrder = sortOrder;
    }

    public Integer getHasParts() {
        return hasParts;
    }

    public void setHasParts(Integer hasParts) {
        this.hasParts = hasParts;
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public double getPartsPrice() {
        return partsPrice;
    }

    public void setPartsPrice(double partsPrice) {
        this.partsPrice = partsPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    //新增属性
    private String brandName;
    private String catName;
    private String modelName;
    private String userName;
    private String realName;
    private String beginTimeString;
    private String endTimeString;
    private Integer addUserStatus;
    private Long orderAddTime;
    private Integer orderStatus;
    private Integer orderPayStatus;
    private Integer orderShipStatus;
    private String outsideColor;
    private String insideColor;
    private String quoteType;
    private String salePriceType;
    private String saleAmount;
    private String totalNumber;
    private String path;
    private String carTypeString;

    public String getCarAttrId() {
        return carAttrId;
    }

    public void setCarAttrId(String carAttrId) {
        this.carAttrId = carAttrId;
    }

    private String carAttrId;

    public String getCarTypeString() {
        return carTypeString;
    }

    public void setCarTypeString(String carTypeString) {
        this.carTypeString = carTypeString;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public String getOutsideColor() {
        return outsideColor;
    }

    public void setOutsideColor(String outsideColor) {
        this.outsideColor = outsideColor;
    }

    public String getInsideColor() {
        return insideColor;
    }

    public void setInsideColor(String insideColor) {
        this.insideColor = insideColor;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public String getSalePriceType() {
        return salePriceType;
    }

    public void setSalePriceType(String salePriceType) {
        this.salePriceType = salePriceType;
    }

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Long getOrderAddTime() {
        return orderAddTime;
    }

    public void setOrderAddTime(Long orderAddTime) {
        this.orderAddTime = orderAddTime;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderPayStatus() {
        return orderPayStatus;
    }

    public void setOrderPayStatus(Integer orderPayStatus) {
        this.orderPayStatus = orderPayStatus;
    }

    public Integer getOrderShipStatus() {
        return orderShipStatus;
    }

    public void setOrderShipStatus(Integer orderShipStatus) {
        this.orderShipStatus = orderShipStatus;
    }

    public Integer getAddUserStatus() {
        return addUserStatus;
    }

    public void setAddUserStatus(Integer addUserStatus) {
        this.addUserStatus = addUserStatus;
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public List<AutoCarPic> getPics() {
        return pics;
    }

    public void setPics(List<AutoCarPic> pics) {
        this.pics = pics;
    }

    private List<AutoCarPic> pics;

    public int getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(int invoiceType) {
        this.invoiceType = invoiceType;
    }

    private int invoiceType;

    public String getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(String brandIds) {
        this.brandIds = brandIds;
    }

    private String brandIds;

    public Double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    private Double startPrice;

    public Double getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(double endPrice) {
        this.endPrice = endPrice;
    }

    private double endPrice;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    private Integer sort;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    private String pic;

    public AutoUser getUser() {
        return user;
    }

    public void setUser(AutoUser user) {
        this.user = user;
    }

    private AutoUser user;


    public Integer getViewUserId() {
        return viewUserId;
    }

    public void setViewUserId(Integer viewUserId) {
        this.viewUserId = viewUserId;
    }

    private Integer viewUserId;

    public Integer getCollectId() {
        return collectId;
    }

    public void setCollectId(Integer collectId) {
        this.collectId = collectId;
    }

    private Integer collectId;

    public void setStartPrice(Double startPrice) {
        this.startPrice = startPrice;
    }

    public List<AutoCarAttr> getAttrs() {
        return attrs;
    }

    public void setAttr(List<AutoCarAttr> attrs) {
        this.attrs = attrs;
    }

    private List<AutoCarAttr> attrs;

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    private int collectCount;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    private String provinceName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    private String cityName;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    private Double price;

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    private Integer buyerId;

}
