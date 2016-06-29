package puzzle.lianche.controller.admin;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.*;
import puzzle.lianche.service.*;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import javax.websocket.server.PathParam;
import java.util.*;

@Controller (value = "adminAutoCarController")
@RequestMapping (value = "/admin/autocar")
public class AutoCarController extends ModuleController {

    @Autowired
    private IAutoCarService autoCarService;

    @Autowired
    private IAutoBrandService autoBrandService;

    @Autowired
    private IAutoBrandCatService autoBrandCatService;

    @Autowired
    private IAutoBrandModelService autoBrandModelService;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    @Autowired
    private IAutoCarPicService autoCarPicService;

    @RequestMapping(value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);


        return Constants.UrlHelper.ADMIN_AUTO_CAR;
    }

    @RequestMapping(value = "/show/{carId}")
    public String show(@PathVariable("carId")Integer carId){
        if(carId != null){
            AutoCar car = autoCarService.query(carId);
            car.setBeginTimeString(ConvertUtil.toString(ConvertUtil.toDate(car.getStartDate()), Constants.DATE_FORMAT));
            car.setEndTimeString(ConvertUtil.toString(ConvertUtil.toDate(car.getEndDate()), Constants.DATE_FORMAT));
            this.setModelAttribute("car", car);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("carId", carId);
            List<AutoCarAttr> attrs = autoCarAttrService.queryList(map);
            this.setModelAttribute("attrList", attrs);

            List<AutoCarPic> pics = autoCarPicService.queryList(map);
            this.setModelAttribute("picList", pics);
        }
        List<AutoBrand> brandList = autoBrandService.queryList(null);
        this.setModelAttribute("brandList", brandList);
        return Constants.UrlHelper.ADMIN_AUTO_CAR_SHOW;
    }

    @RequestMapping(value = "/add")
    public String add(){
        List<AutoBrand> brandList = autoBrandService.queryList(null);
        this.setModelAttribute("brandList", brandList);
        return Constants.UrlHelper.ADMIN_AUTO_CAR_ADD;
    }

    @RequestMapping(value = {"/queryBrandCat"})
    @ResponseBody
    public Result queryBrandCat(Integer brandId){
        Result result = new Result();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("brandId", brandId);
            List<AutoBrandCat> modelList = autoBrandCatService.queryList(map);
            result.setData(modelList);
        }
        catch (Exception e){
            result.setCode(1);
            result.setMsg("查询车系列表出错");
        }
        return result;
    }

    @RequestMapping(value = {"/queryBrandModel"})
    @ResponseBody
    public Result queryBrandModel(Integer catId){
        Result result = new Result();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("catId", catId);
            List<AutoBrandModel> modelList = autoBrandModelService.queryList(map);
            result.setData(modelList);
        }
        catch (Exception e){
            result.setCode(1);
            result.setMsg("查询车型列表出错");
        }
        return result;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoCar autoCar, Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("carName", autoCar.getCarName());
            map.put("carType", autoCar.getCarType());
            if(autoCar.getBeginTimeString()!=null && autoCar.getBeginTimeString()!=""){
                map.put("startDate",ConvertUtil.toLong(ConvertUtil.toDateTime(autoCar.getBeginTimeString() + " 00:00:00")));
            }
            if(autoCar.getEndTimeString()!=null && autoCar.getEndTimeString()!=""){
                map.put("endDate",ConvertUtil.toLong(ConvertUtil.toDateTime(autoCar.getEndTimeString() + " 23:59:59")));
            }
            List<AutoCar> list=autoCarService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoCar car:list){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("carId",car.getCarId());
                    jsonObject.put("carName",car.getCarName());
                    jsonObject.put("carType",Constants.MAP_AUTO_CAR_TYPE.get(car.getCarType()));
                    jsonObject.put("brandName",car.getBrandName());
                    jsonObject.put("catName",car.getCatName());
                    jsonObject.put("modelName",car.getModelName());
                    jsonObject.put("officalPrice",car.getOfficalPrice());map.clear();
                    map.put("attrCarId",car.getCarId());
                    List<AutoCarAttr> attrList=autoCarAttrService.queryList(map);
                    if(attrList!=null && attrList.size()>0){
                        int totalNumber=0;
                        int lockNumber=0;
                        int surplusNumber=0;
                        for(AutoCarAttr attrItem : attrList){
                            totalNumber+=attrItem.getTotalNumber();
                            lockNumber+=attrItem.getLockNumber();
                            surplusNumber+=attrItem.getSurplusNumber();

                        }
                        jsonObject.put("totalNumber",totalNumber);
                        jsonObject.put("lockNumber",lockNumber);
                        jsonObject.put("surplusNumber",surplusNumber);

                    }
                    jsonObject.put("attrs", attrList);
                    jsonObject.put("userName",car.getUserName());
                    if(car.getRealName()!=null){
                        jsonObject.put("userName",car.getUserName()+"("+car.getRealName()+")");
                    }
                    jsonObject.put("status", car.getStatus());
                    jsonObject.put("hasParts",car.getHasParts());
                    jsonObject.put("parts",car.getParts());
                    jsonObject.put("partsPrice",car.getPartsPrice());
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取车源信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }




    @RequestMapping(value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoCar autoCar){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)) {
                String spliter = "※";
                //添加多个颜色
                if(StringUtil.isNotNullOrEmpty(autoCar.getOutsideColor()) &&
                        StringUtil.isNotNullOrEmpty(autoCar.getInsideColor()) &&
                        StringUtil.isNotNullOrEmpty(autoCar.getQuoteType()) &&
                        StringUtil.isNotNullOrEmpty(autoCar.getSalePriceType()) &&
                        StringUtil.isNotNullOrEmpty(autoCar.getSaleAmount()) &&
                        StringUtil.isNotNullOrEmpty(autoCar.getTotalNumber())) {
                    List<AutoCarAttr> attrList = new ArrayList<AutoCarAttr>();
                    String[] outsideColors = autoCar.getOutsideColor().split(spliter);
                    String[] insideColors = autoCar.getInsideColor().split(spliter);
                    String[] quoteTypes = autoCar.getQuoteType().split(spliter);
                    String[] salePriceTypes = autoCar.getSalePriceType().split(spliter);
                    String[] saleAmounts = autoCar.getSaleAmount().split(spliter);
                    String[] totalNumbers = autoCar.getTotalNumber().split(spliter);
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
                    autoCar.setAttr(attrList);
                }
                //添加多张图片
                if(StringUtil.isNotNullOrEmpty(autoCar.getPic())){
                    List<AutoCarPic> list = new ArrayList<AutoCarPic>();
                    String[] pics = autoCar.getPic().split(spliter);
                    for(String pic : pics){
                        AutoCarPic autoCarPic = new AutoCarPic();
                        autoCarPic.setPath(pic);
                        list.add(autoCarPic);
                    }
                    autoCar.setPics(list);
                }
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("brandId", autoCar.getBrandId());
                map.put("catId", autoCar.getBrandCatId());
                map.put("modelId", autoCar.getBrandModelId());
                AutoBrandModel model=autoBrandModelService.query(map);
                autoCar.setCarName(model.getBrandName() + model.getCatName() + model.getModelName());
                autoCar.setAddTime(ConvertUtil.toLong(new Date()));
                autoCar.setRefreshTime(autoCar.getAddTime());
                autoCar.setStartDate(ConvertUtil.toLong(ConvertUtil.toDateTime(autoCar.getBeginTimeString() + " 00:00:00")));
                autoCar.setEndDate(ConvertUtil.toLong(ConvertUtil.toDateTime(autoCar.getEndTimeString() + " 23:59:59")));
                autoCar.setStatus(Constants.AUTO_CAR_STATUS_ON);
                autoCar.setCarType(Constants.AUTO_CAR_TYPE_COMMON);
                autoCar.setAddUserId(ConvertUtil.toInt(initConfig.getConfig("addcar_userid").toString()));
                for(int i = 0; i < autoCar.getAttrs().size(); i++){
                    AutoCarAttr carAttr = autoCar.getAttrs().get(i);
                    autoCar.getAttrs().get(i).setLockNumber(0);
                    autoCar.getAttrs().get(i).setSurplusNumber(carAttr.getTotalNumber());
                    double price = autoCar.getOfficalPrice();
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
                    autoCar.getAttrs().get(i).setPrice(price);
                }
                autoCar.setSortOrder(0);
                if(!autoCarService.insert(autoCar)){
                    result.setCode(1);
                    result.setMsg("发布车源信息失败！");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加车源信息");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                String spliter = "※";
                //添加多个颜色
                if(StringUtil.isNotNullOrEmpty(autoCar.getOutsideColor()) &&
                        StringUtil.isNotNullOrEmpty(autoCar.getInsideColor()) &&
                        StringUtil.isNotNullOrEmpty(autoCar.getQuoteType()) &&
                        StringUtil.isNotNullOrEmpty(autoCar.getSalePriceType()) &&
                        StringUtil.isNotNullOrEmpty(autoCar.getSaleAmount()) &&
                        StringUtil.isNotNullOrEmpty(autoCar.getTotalNumber())) {
                    List<AutoCarAttr> attrList = new ArrayList<AutoCarAttr>();
                    String[] outsideColors = autoCar.getOutsideColor().split(spliter);
                    String[] insideColors = autoCar.getInsideColor().split(spliter);
                    String[] quoteTypes = autoCar.getQuoteType().split(spliter);
                    String[] salePriceTypes = autoCar.getSalePriceType().split(spliter);
                    String[] saleAmounts = autoCar.getSaleAmount().split(spliter);
                    String[] totalNumbers = autoCar.getTotalNumber().split(spliter);
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
                    autoCar.setAttr(attrList);
                }
                //添加多张图片
                if(StringUtil.isNotNullOrEmpty(autoCar.getPic())){
                    List<AutoCarPic> list = new ArrayList<AutoCarPic>();
                    String[] pics = autoCar.getPic().split(spliter);
                    for(String pic : pics){
                        AutoCarPic autoCarPic = new AutoCarPic();
                        autoCarPic.setPath(pic);
                        list.add(autoCarPic);
                    }
                    autoCar.setPics(list);
                }
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("brandId", autoCar.getBrandId());
                map.put("catId", autoCar.getBrandCatId());
                map.put("modelId", autoCar.getBrandModelId());
                AutoBrandModel model=autoBrandModelService.query(map);
                autoCar.setCarName(model.getBrandName() + model.getCatName() + model.getModelName());
                autoCar.setStartDate(ConvertUtil.toLong(ConvertUtil.toDateTime(autoCar.getBeginTimeString() + " 00:00:00")));
                autoCar.setEndDate(ConvertUtil.toLong(ConvertUtil.toDateTime(autoCar.getEndTimeString() + " 23:59:59")));
                for(int i = 0; i < autoCar.getAttrs().size(); i++){
                    AutoCarAttr carAttr = autoCar.getAttrs().get(i);
                    autoCar.getAttrs().get(i).setLockNumber(0);
                    autoCar.getAttrs().get(i).setSurplusNumber(carAttr.getTotalNumber());
                    double price = autoCar.getOfficalPrice();
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
                    autoCar.getAttrs().get(i).setPrice(price);
                }
                if(!autoCarService.update(autoCar)){
                    result.setCode(1);
                    result.setMsg("修改车源信息失败！");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改车源信息");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String, Object> map=new HashMap<String, Object>();
                String id = getParameter("id");
                String ids = getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("carId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] carIds=ids.split(",");
                    map.put("carIds",carIds);
                }
                if(!autoCarService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除车源信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除特定的车源信息");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作车源信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
