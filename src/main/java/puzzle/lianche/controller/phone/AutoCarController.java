package puzzle.lianche.controller.phone;

import net.sf.json.JSON;
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
    private IAutoBrandService autoBrandService;

    @Autowired
    private IAutoBrandCatService autoBrandCatService;

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
            if(car.getSort() != null && car.getSort() == 1){
                map.put("sort", " au.points desc ");
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

            List<AutoCar> list = autoCarService.queryList(map, page);
            if(list != null && list.size() > 0){
                JSONArray jsonArray = new JSONArray();
                for(AutoCar item : list){
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put("carId", item.getCarId());
                    jsonItem.put("carName", item.getCarName());
                    jsonItem.put("addTime", ConvertUtil.toString(new Date(item.getAddTime()),Constants.DATE_FORMAT));
                    jsonItem.put("officalPrice", item.getOfficalPrice());
                    jsonItem.put("collectCount", item.getCollectCount());

                    map.clear();
                    map.put("carId", item.getCarId());

                    JSONArray jsonAttrArray = new JSONArray();
                    List<AutoCarAttr> attrs = autoCarAttrService.queryList(map);
                    if(attrs != null && !attrs.isEmpty()) {
                        for (AutoCarAttr attrItem : attrs) {
                            JSONObject jsonAttrItem = new JSONObject();
                            jsonAttrItem.put("outsideColor", attrItem.getOutsideColor());
                            jsonAttrItem.put("quoteType", attrItem.getQuoteType());
                            jsonAttrItem.put("salePriceType", attrItem.getSalePriceType());
                            jsonAttrItem.put("saleAmount", attrItem.getSaleAmount());
                            jsonAttrArray.add(jsonAttrItem);
                        }
                    }
                    jsonItem.put("attrs", jsonAttrArray);

                    String pic = null;
                    List<AutoCarPic> pics = autoCarPicService.queryList(map);
                    if(pics != null && !pics.isEmpty()) {
                        pic = pics.get(0).getPath();
                    }
                    jsonItem.put("pic", pic);
                    int isAuth = item.getAddUserStatus() == Constants.AUTO_USER_STATUS_AUTHENTICATIONADOPT ? 1 : 0;
                    jsonItem.put("addUserAuth", isAuth);

                    jsonArray.add(jsonItem);
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
            List<AutoCarPic> pics = autoCarPicService.queryList(map);
            if(pics != null){
                car.setPics(pics);
            }
            List<AutoCarAttr> attrs = autoCarAttrService.queryList(map);
            if(attrs != null){
                car.setAttr(attrs);
            }
            map.clear();
            map.put("userId", car.getAddUserId());
            car.setUser(autoUserService.query(map));
            result.setData(car);
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
            if(StringUtil.isNullOrEmpty(car.getBeginTimeString())){
                result.setCode(-1);
                result.setMsg("开始时间不能为空！");
                return result;
            }
            if(!StringUtil.isDate(car.getBeginTimeString()) || !StringUtil.isDate(car.getBeginTimeString())){
                result.setCode(-1);
                result.setMsg("开始时间不正确！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(car.getEndTimeString()) || !StringUtil.isDate(car.getEndTimeString())){
                result.setCode(-1);
                result.setMsg("结束时间不能为空！");
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
            car.setStartDate(ConvertUtil.toLong(ConvertUtil.toDateTime(car.getBeginTimeString() + " 00:00:00")));
            car.setEndDate(ConvertUtil.toLong(ConvertUtil.toDateTime(car.getEndTimeString() + " 23:59:59")));
            car.setStatus(Constants.AUTO_CAR_STATUS_ON);
            car.setCarType(Constants.AUTO_CAR_TYPE_COMMON);
            for(int i = 0; i < car.getAttr().size(); i++){
                AutoCarAttr carAttr = car.getAttr().get(i);
                car.getAttr().get(i).setLockNumber(0);
                car.getAttr().get(i).setSurplusNumber(carAttr.getTotalNumber());
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
                car.getAttr().get(i).setPrice(price);
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
}