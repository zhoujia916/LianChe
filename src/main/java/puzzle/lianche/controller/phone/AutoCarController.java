package puzzle.lianche.controller.phone;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.entity.*;
import puzzle.lianche.service.*;
import puzzle.lianche.utils.*;

import javax.json.*;
import java.util.*;

@Controller(value = "phoneAutoCarController")
@RequestMapping(value = "/phone/autocar")
public class AutoCarController extends BaseController {

    @Autowired
    private IAutoUserService autoUserService;

    @Autowired
    private IAutoCarService autoCarService;

    @Autowired
    private IAutoCarPicService autoCarPicService;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    @Autowired
    private IAutoBrandModelService autoBrandModelService;


    /**
     * 查看汽车资源列表
     * @param car
     * @return
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(AutoCar car, Integer markId){
        Result result = new Result();
        try{
            Page page = new Page();
            page.setPageSize(10);
            Map<String, Object> map = new HashMap<String, Object>();
            if(StringUtil.isNotNullOrEmpty(car.getCarName())){
                map.put("carName", car.getCarName());
            }
            if(car.getBrandId() != null && car.getBrandId()> 0){
                map.put("brandId", car.getBrandId());
            }
            if(StringUtil.isNotNullOrEmpty(car.getBrandIds())){
                map.put("brandIds", car.getBrandIds());
            }
            if(car.getStartPrice() != null && car.getStartPrice() > 0){
                map.put("startPrice", car.getStartPrice());
            }
            if(car.getEndPrice() != null && car.getEndPrice() > 0){
                map.put("endPrice", car.getEndPrice());
            }
            if(car.getCarType() != null){
                map.put("carType", car.getCarType());
            }
            if(car.getViewUserId() != null && car.getViewUserId() > 0){
                map.put("viewUserId", car.getViewUserId());
            }

            map.put("status", Constants.AUTO_CAR_STATUS_ON);
            map.put("curTime", System.currentTimeMillis());
            if(markId != null && markId > 0 && car.getCarId() != null && car.getCarId() > 0){
                if(markId == Constants.PULLREFRESH_UP){
                    String carSql=" and ac.car_id > " + car.getCarId();
                    map.put("filter",carSql);
                }else if(markId == Constants.PULLREFRESH_DOWN){
                    String carSql = " and ac.car_id < " + car.getCarId();
                    map.put("filter",carSql);
                }
            }

            if(car.getSort() != null && car.getSort() == 1){
                map.put("sort", " au.point desc ");
            }
            int officalUserId = ConvertUtil.toInt(initConfig.getConfig("admin_addcar_userid"));
            List<AutoCar> list = autoCarService.queryList(map, page);
            if(list != null && list.size() > 0){
                JSONArray jsonArray = new JSONArray();
                for(AutoCar item : list){
                    int i=0;
                    int carId=0;
                    map.put("attrCarId",item.getCarId());
                    List<AutoCarAttr> attrList=autoCarAttrService.queryList(map);
                    for(AutoCarAttr carAttr:attrList){
                        if(carAttr.getSurplusNumber()==0){
                            i+=1;
                        }
                    }
                    if(i!=attrList.size()){
                        carId=item.getCarId();
                    }
                    if(carId==item.getCarId()){
                        JSONObject jsonItem = new JSONObject();
                        jsonItem.put("isOffical", item.getAddUserId().equals(officalUserId));
                        jsonItem.put("carId", item.getCarId());
                        jsonItem.put("carName", item.getModelName());
                        jsonItem.put("brandName", item.getBrandName());
                        jsonItem.put("catName", item.getCatName());
                        jsonItem.put("pic", item.getPic());
                        if(car.getCarType() == Constants.AUTO_CAR_TYPE_COMMON){
                            jsonItem.put("addTime", ConvertUtil.toString(new Date(item.getRefreshTime()),Constants.DATE_FORMAT));
                        }else{
                            jsonItem.put("addTime", ConvertUtil.toString(ConvertUtil.toDate(item.getRefreshTime()), "yyyy.MM.dd HH:mm"));
                        }

                        jsonItem.put("officalPrice", item.getOfficalPrice());
                        jsonItem.put("collectCount", item.getCollectCount());
                        jsonItem.put("collectId", item.getCollectId() == null ? 0 : item.getCollectId());

                        JSONArray jsonAttrArray = new JSONArray();
                        if(attrList != null && !attrList.isEmpty()) {
                            for (AutoCarAttr attrItem : attrList) {
                                JSONObject jsonAttrItem = new JSONObject();
                                jsonAttrItem.put("outsideColor", attrItem.getOutsideColor());
                                jsonAttrItem.put("quoteType", attrItem.getQuoteType());
                                jsonAttrItem.put("salePriceType", attrItem.getSalePriceType());
                                jsonAttrItem.put("saleAmount", attrItem.getSaleAmount());
                                jsonAttrArray.add(jsonAttrItem);
                            }
                        }
                        jsonItem.put("attrs", jsonAttrArray);

                        int isAuth = item.getAddUserStatus() == Constants.AUTO_USER_STATUS_AUTH_SUCCESS ? 1 : 0;
                        jsonItem.put("addUserAuth", isAuth);

                        jsonArray.add(jsonItem);
                    }
                }
                result.setData(jsonArray);
            }
            result.setTotal(page.getTotal());
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("获取车源列表信息出错");
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return result;
    }


    /**
     * 查看汽车资源详情
     * @param car
     * @return
     */
    @RequestMapping(value = "/show.do")
    @ResponseBody
    public Result show(AutoCar car){
        Result result = new Result();
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("carId", car.getCarId());
            if(car.getViewUserId() != null && car.getViewUserId() > 0){
                map.put("viewUserId", car.getViewUserId());
            }

            car = autoCarService.query(map);
            if(car == null){
                result.setCode(1);
                result.setMsg("该车源不存在！");
                return result;
            }
            int officalUserId = ConvertUtil.toInt(initConfig.getConfig("admin_addcar_userid"));
            JSONObject jsonCar = new JSONObject();
            jsonCar.put("isOffical", car.getAddUserId().equals(officalUserId));
            jsonCar.put("carId", car.getCarId());
            jsonCar.put("carName", car.getModelName());
            jsonCar.put("collectId", car.getCollectId() == null ? 0 : car.getCollectId());
            jsonCar.put("officalPrice", car.getOfficalPrice());

            jsonCar.put("hasParts", car.getHasParts());
            jsonCar.put("parts", car.getParts());
            jsonCar.put("partsPrice", car.getPartsPrice());
            jsonCar.put("brandName", car.getBrandName());
            jsonCar.put("catName", car.getCatName());
            jsonCar.put("modelName", car.getModelName());
            jsonCar.put("remark", car.getRemark());
            jsonCar.put("provinceName", car.getProvinceName());
            jsonCar.put("cityName", car.getCityName());
            jsonCar.put("invoiceType", car.getInvoiceType());


            List<AutoCarPic> pics = autoCarPicService.queryList(map);
            JSONArray jsonCarPicAttray = new JSONArray();
            if(pics != null && !pics.isEmpty()){
                for(AutoCarPic pic : pics){
                    jsonCarPicAttray.add(pic.getPath());
                }
            }
            jsonCar.put("pics", jsonCarPicAttray);
            List<AutoCarAttr> attrs = autoCarAttrService.queryList(map);
            JSONArray jsonCarAttrArray = new JSONArray();
            int totalNumber = 0;
            if(attrs != null && !attrs.isEmpty()){
                for(AutoCarAttr attr : attrs){
                    totalNumber += attr.getSurplusNumber();
                    JSONObject jsonCarAttr = new JSONObject();
                    jsonCarAttr.put("carAttrId", attr.getCarAttrId());
                    jsonCarAttr.put("quoteType", attr.getQuoteType());
                    jsonCarAttr.put("salePriceType", attr.getSalePriceType());
                    jsonCarAttr.put("saleAmount", attr.getSaleAmount());
                    jsonCarAttr.put("outsideColor", attr.getOutsideColor());
                    jsonCarAttr.put("insideColor", attr.getInsideColor());
                    jsonCarAttr.put("totalNumber", attr.getSurplusNumber());
                    jsonCarAttrArray.add(jsonCarAttr);
                }
            }
            jsonCar.put("totalNumber", totalNumber);
            jsonCar.put("attrs", jsonCarAttrArray);

            map.clear();
            map.put("userId", car.getAddUserId());
            AutoUser user = autoUserService.query(map);
            JSONObject jsonUser = new JSONObject();
            if(user != null){
                jsonUser.put("userId", user.getUserId());
                jsonUser.put("userName", user.getStatus() == Constants.AUTO_USER_STATUS_AUTH_SUCCESS ? user.getRealName() : user.getUserName());
                jsonUser.put("userAvatar", user.getUserAvatar());
                jsonUser.put("point", user.getPoint());
                jsonUser.put("shopName", user.getShopName());
                jsonUser.put("isAuth", user.getStatus() == Constants.AUTO_USER_STATUS_AUTH_SUCCESS);
            }
            jsonCar.put("user", jsonUser);


            result.setData(jsonCar);
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("获取车源详情信息出错");
            logger.error(e.getMessage());
        }
        return result;
    }


    /**
     * 新增汽车资源详情(目前缺少UserId验证，次数限制验证)
     * @param car
     * @return
     */
    @RequestMapping(value = "/add.do")
    @ResponseBody
    public Result add(AutoCar car){
        Result result = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            //region Check Input
            if(car == null){
                result.setCode(1);
                result.setMsg("车源不能为空！");
                return result;
            }
            if(car.getAddUserId() == null || car.getAddUserId() == 0){
                result.setCode(1);
                result.setMsg("用户不能为空！");
                return result;
            }
            AutoUser find = autoUserService.query(car.getAddUserId(), null);
            if(find == null){
                result.setCode(1);
                result.setMsg("该用户不存在！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(1);
                result.setMsg("该账户已被禁用！");
                return result;
            }
            if(car.getBrandId() == null || car.getBrandId() == 0){
                result.setCode(-1);
                result.setMsg("请选择车型相关信息！");
                return result;
            }
            if(car.getBrandCatId() == null || car.getBrandCatId() == 0){
                result.setCode(-1);
                result.setMsg("请选择车型相关信息！");
                return result;
            }
            if(car.getBrandModelId() == null || car.getBrandModelId() == 0){
                result.setCode(-1);
                result.setMsg("请选择车型相关信息！");
                return result;
            }
            map.put("brandId", car.getBrandId());
            map.put("catId", car.getBrandCatId());
            map.put("modelId", car.getBrandModelId());
            AutoBrandModel model = autoBrandModelService.query(map);
            if(model == null){
                result.setCode(-1);
                result.setMsg("请选择车型相关信息！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(car.getOutsideColor())){
                result.setCode(-1);
                result.setMsg("外饰颜色不能为空！");
                return result;
            }else if(StringUtil.isNullOrEmpty(car.getInsideColor())){
                result.setCode(-1);
                result.setMsg("内饰颜色不能为空！");
                return result;
            }else if(StringUtil.isNullOrEmpty(car.getQuoteType())){
                result.setCode(-1);
                result.setMsg("优惠量不能为空！");
                return result;
            }else if(StringUtil.isNullOrEmpty(car.getTotalNumber())){
                result.setCode(-1);
                result.setMsg("总数不能为空！");
                return result;
            }

            if(!StringUtil.isDate(car.getBeginTimeString()) || !StringUtil.isDate(car.getBeginTimeString())){
                result.setCode(-1);
                result.setMsg("开始时间不正确！");
                return result;
            }

            if(StringUtil.isNullOrEmpty(car.getEndTimeString()) || !StringUtil.isDate(car.getEndTimeString())){
                result.setCode(-1);
                result.setMsg("结束时间不正确！");
                return result;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Date now = calendar.getTime();
            Date startTime = ConvertUtil.toDate(car.getBeginTimeString());
            Date endTime = ConvertUtil.toDate(car.getEndTimeString());


            if(startTime.after(endTime)){
                result.setCode(-1);
                result.setMsg("开始时间不能大于结束时间！");
                return result;
            }
            if(now.after(startTime)){
                result.setCode(-1);
                result.setMsg("开始时间不能小于今天！");
                return result;
            }
            if(now.after(endTime)){
                result.setCode(-1);
                result.setMsg("结束时间不能小于今天！");
                return result;
            }

            if(((endTime.getTime() - startTime.getTime()) / (24 * 3600 * 1000)) > 90){
                result.setCode(-1);
                result.setMsg("有效期不能超过90天！");
                return result;
            }

            if(car.getHasParts().equals(Constants.AUTO_CAR_HAS_PARTS_YES) && StringUtil.isNullOrEmpty(car.getParts())){
                result.setCode(-1);
                result.setMsg("配件描述不能为空！");
                return result;
            }
            if(car.getHasParts().equals(Constants.AUTO_CAR_HAS_PARTS_YES) && car.getPartsPrice() == 0){
                result.setCode(-1);
                result.setMsg("配件价格不能为空！");
                return result;
            }


            //endregion
            //region Init Attr Info
            String spliter = "※";
            if(StringUtil.isNotNullOrEmpty(car.getOutsideColor()) &&
                StringUtil.isNotNullOrEmpty(car.getInsideColor()) &&
                StringUtil.isNotNullOrEmpty(car.getQuoteType()) &&
                StringUtil.isNotNullOrEmpty(car.getSalePriceType()) &&
                StringUtil.isNotNullOrEmpty(car.getSaleAmount()) &&
                StringUtil.isNotNullOrEmpty(car.getTotalNumber())) {

                List<AutoCarAttr> attrList = new ArrayList<AutoCarAttr>();
                String[] outsideColors = car.getOutsideColor().split(spliter);
                String[] insideColors = car.getInsideColor().split(spliter);
                String[] quoteTypes = car.getQuoteType().split(spliter);
                String[] salePriceTypes = car.getSalePriceType().split(spliter);
                String[] saleAmounts = car.getSaleAmount().split(spliter);
                String[] totalNumbers = car.getTotalNumber().split(spliter);
                for (int i = 0; i < outsideColors.length; i++) {
                    AutoCarAttr autoCarAttr = new AutoCarAttr();
                    autoCarAttr.setOutsideColor(outsideColors[i]);
                    autoCarAttr.setInsideColor(insideColors[i]);
                    autoCarAttr.setQuoteType(ConvertUtil.toInt(quoteTypes[i]));
                    autoCarAttr.setSalePriceType(ConvertUtil.toInt(salePriceTypes[i]));
                    autoCarAttr.setSaleAmount(ConvertUtil.toDouble(saleAmounts[i]));
                    autoCarAttr.setTotalNumber(ConvertUtil.toInt(totalNumbers[i]));
                    attrList.add(autoCarAttr);
                }
                car.setAttr(attrList);
            }

            if(StringUtil.isNotNullOrEmpty(car.getPic())){
                List<AutoCarPic> list = new ArrayList<AutoCarPic>();
                String[] pics = car.getPic().split(spliter);
                for(String pic : pics){
                    AutoCarPic autoCarPic = new AutoCarPic();
                    autoCarPic.setPath(pic);
                    list.add(autoCarPic);
                }
                car.setPics(list);
            }

            car.setCarName(model.getBrandName() + model.getCatName() + model.getModelName());
            car.setAddTime(ConvertUtil.toLong(new Date()));
            car.setRefreshTime(car.getAddTime());
            car.setStartDate(ConvertUtil.toDateTime(car.getBeginTimeString() + " 00:00:00").getTime());
            car.setEndDate(ConvertUtil.toDateTime(car.getEndTimeString() + " 23:59:59").getTime());
            car.setStatus(Constants.AUTO_CAR_STATUS_ON);
            car.setCarType(Constants.AUTO_CAR_TYPE_COMMON);
            for(int i = 0; i < car.getAttrs().size(); i++){
                AutoCarAttr carAttr = car.getAttrs().get(i);
                car.getAttrs().get(i).setLockNumber(0);
                car.getAttrs().get(i).setSurplusNumber(carAttr.getTotalNumber());
                double price = car.getOfficalPrice();
                if(carAttr.getQuoteType() == Constants.AUTO_CAR_QUOTE_TYPE_UP) {
                    if (carAttr.getSalePriceType() == Constants.AUTO_CAR_SALE_PRICE_TYPE_MONEY) {
                        price += carAttr.getSaleAmount();
                    } else if (carAttr.getSalePriceType() == Constants.AUTO_CAR_SALE_PRICE_TYPE_PERCENT) {
                        price += price * carAttr.getSaleAmount() / 100;
                    }
                }
                else if(carAttr.getQuoteType() == Constants.AUTO_CAR_QUOTE_TYPE_DOWN){
                    if (carAttr.getSalePriceType() == Constants.AUTO_CAR_SALE_PRICE_TYPE_MONEY) {
                        price -= carAttr.getSaleAmount();
                    } else if (carAttr.getSalePriceType() == Constants.AUTO_CAR_SALE_PRICE_TYPE_PERCENT) {
                        price -= price * carAttr.getSaleAmount() / 100;
                    }
                }
                car.getAttrs().get(i).setPrice(price);
            }
            car.setSortOrder(0);
            //endregion
            if(!autoCarService.insert(car)){
                result.setCode(1);
                result.setMsg("发布车源信息失败！");
            }

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("发布车源信息出错!");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 删除用户车源
     * @param car
     * @return
     */
    @RequestMapping(value = "/delete.do")
    @ResponseBody
    public Result delete(AutoCar car){
        Result result = new Result();
        try {
            if(car == null || car.getCarId() == null || car.getCarId() == 0){
                result.setCode(1);
                result.setMsg("车源不能为空！");
                return result;
            }
            AutoCar existCar = autoCarService.query(car.getCarId());
            if(existCar == null){
                result.setCode(1);
                result.setMsg("该车源不存在！");
                return result;
            }
            AutoUser existUser = autoUserService.query(car.getAddUserId(), null);
            if(existUser == null){
                result.setCode(1);
                result.setMsg("该用户不存在！");
                return result;
            }
            if(existUser.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(1);
                result.setMsg("该账户已被禁用！");
                return result;
            }
            if(existCar.getAddUserId() != car.getAddUserId()){
                result.setCode(1);
                result.setMsg("您不能删除该车源信息！");
                return result;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("carId", car.getCarId());
            if(!autoCarService.delete(map)){
                result.setCode(1);
                result.setMsg("删除车源信息失败！");
            }
        }
        catch (Exception e){
            result.setCode(1);
            result.setMsg("删除车源信息出错！");
            logger.error(e.getMessage());
        }
        return result;
    }
}