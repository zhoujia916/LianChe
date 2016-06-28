package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.*;
import puzzle.lianche.service.IAutoCarAttrService;
import puzzle.lianche.service.IAutoCarService;
import puzzle.lianche.service.IAutoOrderService;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "adminAutoOrderController")
@RequestMapping(value = "/admin/autoorder")
public class AutoOrderController extends ModuleController {

    @Autowired
    private IAutoOrderService autoOrderService;

    @Autowired
    private IAutoUserService autoUserService;

    @Autowired
    private IAutoCarService autoCarService;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    @RequestMapping(value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions=getActions();
        List<AutoUser> users=autoUserService.queryList(null);
        List<AutoCar> cars=autoCarService.queryList(null);
        this.setModelAttribute("actions", actions);
        this.setModelAttribute("users",users);
        this.setModelAttribute("cars",cars);
        return Constants.UrlHelper.ADMIN_AUTO_ORDER;
    }

    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            String orderStatus=request.getParameter("orderStatus");
            String payStatus=request.getParameter("payStatus");
            String shipStatus=request.getParameter("shipStatus");
            String beginTime=request.getParameter("beginTime");
            String endTime=request.getParameter("endTime");
            if(StringUtil.isNotNullOrEmpty(orderStatus)){
                map.put("orderStatus",orderStatus);
            }
            if(StringUtil.isNotNullOrEmpty(payStatus)){
                map.put("payStatus",payStatus);
            }
            if(StringUtil.isNotNullOrEmpty(shipStatus)){
                if(ConvertUtil.toInt(shipStatus)>-1){
                    map.put("shipStatus",ConvertUtil.toInt(shipStatus));
                }
            }
            if(StringUtil.isNotNullOrEmpty(beginTime)){
                map.put("beginTime",ConvertUtil.toLong(ConvertUtil.toDateTime(beginTime + " 00:00:00")));
            }
            if(StringUtil.isNotNullOrEmpty(endTime)){
                map.put("endTime",ConvertUtil.toLong(ConvertUtil.toDateTime(endTime + " 23:59:59")));
            }
            Page page=new Page();
            page.setPageIndex(ConvertUtil.toInt(request.getParameter("pageIndex")));
            page.setPageSize(ConvertUtil.toInt(request.getParameter("pageSize")));
            List<AutoOrder> list=autoOrderService.queryOrder(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoOrder order:list){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("orderId",order.getOrderId());
                    jsonObject.put("carName",order.getCarName());
                    jsonObject.put("orderStatus",Constants.ORDER_STATUS.get(order.getOrderStatus()));
                    jsonObject.put("payStatus",Constants.PAY_STATUS.get(order.getPayStatus()));
                    jsonObject.put("shipStatus",Constants.SHIP_STATUS.get(order.getShipStatus()));
                    jsonObject.put("sellerName",order.getSellerName());
                    if(order.getSellerRealName()!=null){
                        jsonObject.put("sellerName",order.getSellerName()+"("+order.getSellerRealName()+")");
                    }
                    jsonObject.put("buyerName",order.getBuyerName());
                    if(order.getBuyerRealName()!=null){
                        jsonObject.put("buyerName",order.getBuyerName()+"("+order.getBuyerRealName()+")");
                    }
                    jsonObject.put("carNumber",order.getCarNumber());
                    jsonObject.put("price",order.getAmount());
                    jsonObject.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(order.getAddTime())));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取订单信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoOrder autoOrder){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("carId", autoOrder.getCar().getCarId());
                AutoCar car = autoCarService.query(map);
                double price = car.getOfficalPrice();
                map.clear();
                map.put("carId", autoOrder.getCar().getCarId());
                map.put("carAttrId", autoOrder.getCar().getCarAttrId());
                AutoCarAttr attr = autoCarAttrService.query(map);
                double salePrice = attr.getSalePriceType() == Constants.AUTO_CAR_SALE_PRICE_TYPE_MONEY ?
                        attr.getSaleAmount() :
                        (price * attr.getSaleAmount() / 100);
                if(attr.getQuoteType() == Constants.AUTO_CAR_QUOTE_TYPE_UP){
                    price += salePrice;
                }
                else if(attr.getQuoteType() == Constants.AUTO_CAR_QUOTE_TYPE_DOWN){
                    price -= salePrice;
                }
                if(autoOrder.getCar().getHasParts() == Constants.AUTO_CAR_HAS_PARTS_YES){
                    price += car.getPartsPrice();
                }
                autoOrder.setOrderSn(autoOrderService.createSn(ConvertUtil.toString(autoOrder.getBuyerId())));
                autoOrder.setOrderStatus(Constants.OS_SUBMIT);
                autoOrder.setPayStatus(Constants.PS_WAIT_BUYER_DEPOSIT);
                autoOrder.setShipStatus(Constants.SS_UNSHIP);
                autoOrder.setSellerId(car.getAddUserId());
                autoOrder.setSellerDeposit(0);
                autoOrder.setBuyerDeposit(0);
                autoOrder.setShipTime(0);
                autoOrder.setAmount(price * autoOrder.getCar().getCarNumber());
                autoOrder.setAddTime(ConvertUtil.toLong(new Date()));
                autoOrder.getCar().setCarPrice(price);
                autoOrder.getCar().setSendNumber(0);
                autoOrder.setCar(autoOrder.getCar());
                autoOrder.setPutTime(ConvertUtil.toLong(ConvertUtil.toDateTime(autoOrder.getPutTimeString()+" 23:59:59")));
                if(!autoOrderService.insert(autoOrder)){
                    result.setCode(1);
                    result.setData("保存订单信息出错！");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"保存订单信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String, Object> map=new HashMap<String, Object>();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("orderId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] orderIds=ids.split(",");
                    map.put("orderIds",orderIds);
                }
                if(!autoOrderService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除订单出错！");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除订单信息");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作订单信息出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 查看车源属性
     * @param carId
     * @return
     */
    @RequestMapping(value = "/queryCarAttr.do")
    @ResponseBody
    public JSONArray queryCarAttr(Integer carId){
        JSONArray array=new JSONArray();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("carId",carId);
            List<AutoCarAttr> list=autoCarAttrService.queryList(map);
            if(list!=null && list.size()>0){
                for(AutoCarAttr carAttr:list){
                    JSONObject object=new JSONObject();
                    object.put("carAttrId",carAttr.getCarAttrId());
                    object.put("attrValue","外观:"+carAttr.getOutsideColor()+"-内饰:"+carAttr.getInsideColor());
                    object.put("surplusNumber",carAttr.getSurplusNumber());
                    object.put("addUserId",carAttr.getAddUserId());
                    array.add(object);
                }
            }
        }catch(Exception e){
            logger.error("查看车源属性出错！"+e.getMessage());
        }
        return array;
    }
}
