package puzzle.lianche.controller.admin;

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
import puzzle.lianche.service.IAutoCarAttrService;
import puzzle.lianche.service.IAutoCarService;
import puzzle.lianche.service.IAutoOrderService;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import javax.websocket.server.PathParam;
import java.util.*;

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

        this.setModelAttribute("actions", actions);

        return Constants.UrlHelper.ADMIN_AUTO_ORDER;
    }

    @RequestMapping(value = {"/add"})
    public String add(){

        List<AutoUser> users=autoUserService.queryList(null);
        List<AutoCar> cars=autoCarService.queryList(null);
        this.setModelAttribute("userList",users);
        this.setModelAttribute("carList",cars);

        this.setModelAttribute("action", Constants.PageHelper.PAGE_ACTION_CREATE);
        return Constants.UrlHelper.ADMIN_AUTO_ORDER_SHOW;
    }

    @RequestMapping(value = {"/edit/{orderId}"})
    public String edit(@PathVariable Integer orderId){
        if(orderId != null && orderId > 0){

            List<AutoUser> users=autoUserService.queryList(null);
            List<AutoCar> cars=autoCarService.queryList(null);
            this.setModelAttribute("userList",users);
            this.setModelAttribute("carList",cars);

            AutoOrder order = autoOrderService.query(orderId, null);
            if(order != null){
                order.setAddTimeString(ConvertUtil.toString(ConvertUtil.toDate(order.getAddTime())));
                order.setPutTimeString(ConvertUtil.toString(ConvertUtil.toDate(order.getPutTime()),Constants.DATE_FORMAT));
                order.setOrderStatusString(Constants.ORDER_STATUS.get(order.getOrderStatus()));
                order.setPayStatusString(Constants.PAY_STATUS.get(order.getPayStatus()));
                order.setShipStatusString(Constants.SHIP_STATUS.get(order.getShipStatus()));
                List<String> list=autoOrderService.queryOperate(order,Constants.ORDER_USER_ADMIN);
                List<Map> orderActions=new ArrayList<Map>();
                if(list!=null && list.size()>0){
                    for(String action:list){
                        Map actionMap=new HashMap();
                        actionMap.put("key",action);
                        actionMap.put("value",Constants.OO_OPERATE.get(action));
                        orderActions.add(actionMap);
                    }
                }
                AutoUser buyer = autoUserService.query(order.getBuyerId(), null);
                buyer.setShopTypeString(Constants.MAP_AUTO_COLLECT_TYPE.get(buyer.getShopType()));

                AutoUser seller = autoUserService.query(order.getSellerId(), null);
                seller.setShopTypeString(Constants.MAP_AUTO_COLLECT_TYPE.get(seller.getShopType()));

                map.put("orderId",order.getOrderId());
                AutoCar car = autoCarService.query(map);
                car.setCarTypeString(Constants.MAP_AUTO_CAR_TYPE.get(car.getCarType()));
                map.clear();
                map.put("attrCarId",car.getCarId());
                map.put("carAttrId",order.getCarAttrId());
                AutoCarAttr carAttr = autoCarAttrService.query(map);

                this.setModelAttribute("order",order);
                this.setModelAttribute("car", car);
                this.setModelAttribute("buyer", buyer);
                this.setModelAttribute("seller",seller);
                this.setModelAttribute("carAttr", carAttr);
                this.setModelAttribute("operateList", orderActions);
            }
        }

        this.setModelAttribute("action", Constants.PageHelper.PAGE_ACTION_UPDATE);
        return Constants.UrlHelper.ADMIN_AUTO_ORDER_SHOW;
    }

    @RequestMapping(value = {"/view/{orderId}"})
    public String view(@PathVariable Integer orderId){
        if(orderId != null && orderId > 0){
            AutoOrder order = autoOrderService.query(orderId, null);
            if(order != null){
                order.setAddTimeString(ConvertUtil.toString(ConvertUtil.toDate(order.getAddTime())));
                order.setPutTimeString(ConvertUtil.toString(ConvertUtil.toDate(order.getPutTime()),Constants.DATE_FORMAT));
                order.setOrderStatusString(Constants.ORDER_STATUS.get(order.getOrderStatus()));
                order.setPayStatusString(Constants.PAY_STATUS.get(order.getPayStatus()));
                order.setShipStatusString(Constants.SHIP_STATUS.get(order.getShipStatus()));
                List<String> list=autoOrderService.queryOperate(order,Constants.ORDER_USER_ADMIN);
                List<Map> orderActions=new ArrayList<Map>();
                if(list!=null && list.size()>0){
                    for(String action:list){
                        Map actionMap=new HashMap();
                        actionMap.put("key",action);
                        actionMap.put("value",Constants.OO_OPERATE.get(action));
                        orderActions.add(actionMap);
                    }
                }
                AutoUser buyer = autoUserService.query(order.getBuyerId(), null);
                buyer.setShopTypeString(Constants.MAP_AUTO_COLLECT_TYPE.get(buyer.getShopType()));

                AutoUser seller = autoUserService.query(order.getSellerId(), null);
                seller.setShopTypeString(Constants.MAP_AUTO_COLLECT_TYPE.get(seller.getShopType()));

                map.put("orderId",order.getOrderId());
                AutoCar car = autoCarService.query(map);
                car.setCarTypeString(Constants.MAP_AUTO_CAR_TYPE.get(car.getCarType()));
                map.clear();
                map.put("attrCarId",car.getCarId());
                map.put("carAttrId",order.getCarAttrId());
                AutoCarAttr carAttr = autoCarAttrService.query(map);

                this.setModelAttribute("order",order);
                this.setModelAttribute("car", car);
                this.setModelAttribute("buyer", buyer);
                this.setModelAttribute("seller",seller);
                this.setModelAttribute("carAttr", carAttr);
                this.setModelAttribute("operateList", orderActions);
            }
        }

        this.setModelAttribute("action", Constants.PageHelper.PAGE_ACTION_VIEW);
        return Constants.UrlHelper.ADMIN_AUTO_ORDER_SHOW;
    }

    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(AutoOrder autoOrder, Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoOrder != null) {
                if (autoOrder.getOrderStatus() != null && autoOrder.getOrderStatus() > 0) {
                    map.put("orderStatus", autoOrder.getOrderStatus());
                }
                if (autoOrder.getPayStatus() != null && autoOrder.getPayStatus() > 0) {
                    map.put("payStatus", autoOrder.getPayStatus());
                }
                if (autoOrder.getShipStatus() != null && autoOrder.getShipStatus() > 0) {
                    map.put("shipStatus", autoOrder.getShipStatus());
                }
                if (StringUtil.isNotNullOrEmpty(autoOrder.getBeginAddTime())) {
                    map.put("beginTime", ConvertUtil.toLong(ConvertUtil.toDateTime(autoOrder.getBeginAddTime() + " 00:00:00")));
                }
                if (StringUtil.isNotNullOrEmpty(autoOrder.getEndAddTime())) {
                    map.put("endTime", ConvertUtil.toLong(ConvertUtil.toDateTime(autoOrder.getEndAddTime() + " 23:59:59")));
                }
            }
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
                    jsonObject.put("sellerRealName",order.getSellerRealName());
                    jsonObject.put("buyerName",order.getBuyerName());
                    jsonObject.put("buyerRealName",order.getBuyerRealName());
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
                }else{
                    autoOrder.getCar().setHasParts(Constants.AUTO_CAR_HAS_PARTS_NO);
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
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){

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

    @RequestMapping("/operate.do")
    @ResponseBody
    public Result operate(String action,Integer orderId){
        Result result=new Result();
        try{
            AutoOrder order=autoOrderService.query(orderId, null);
            boolean flag=false;
            if(action.equalsIgnoreCase(Constants.OO_CANCEL)){
                flag=autoOrderService.doCancel(order);
            }else if(action.equalsIgnoreCase(Constants.OO_PAYMENT)){
                flag=autoOrderService.doDeposit(order);
            }else if(action.equalsIgnoreCase(Constants.OO_UNPAYMENT)){
                order.setPayStatus(Constants.PS_WAIT_BUYER_DEPOSIT);
                flag=autoOrderService.update(order);
            }else if(action.equalsIgnoreCase(Constants.OO_ACCEPT)){
                flag=autoOrderService.doAccept(order);
            }else if(action.equalsIgnoreCase(Constants.OO_UNACCEPT)){
                flag=autoOrderService.doReject(order);
            }else if(action.equalsIgnoreCase(Constants.OO_REJECT)){
                flag=autoOrderService.doReject(order);
            }else if(action.equalsIgnoreCase(Constants.OO_RECEIVE)){
                flag=autoOrderService.doReceive(order);
            }else if(action.equalsIgnoreCase(Constants.OO_NOTIFY_RECEIVE)){
                flag=autoOrderService.doNotify(order);
            }else if(action.equalsIgnoreCase(Constants.OO_RETURN_BUYER_DEPOSIT)){
                Integer type=0;
                if(order.getPayStatus()==Constants.PS_BUYER_PAY_DEPOSIT) {
                    type=Constants.ORDER_USER_BUYER;
                }
                else if(order.getPayStatus()==Constants.PS_SELLER_PAY_DEPOSIT){
                    type=Constants.ORDER_USER_ALL;
                }
                else if(order.getPayStatus()==Constants.PS_WAIT_SYSTEM_DEPOSIT){
                    type=Constants.ORDER_USER_ALL;
                }
                flag = autoOrderService.doReturnDeposit(order, type);
            }
            else if(action.equalsIgnoreCase(Constants.OO_RETURN_BUYER_DEPOSIT)){
                Integer type=Constants.ORDER_USER_BUYER;
                flag = autoOrderService.doReturnDeposit(order, type);
            }
            else if(action.equalsIgnoreCase(Constants.OO_RETURN_SELLER_DEPOSIT)){
                Integer type=Constants.ORDER_USER_SELLER;
                flag = autoOrderService.doReturnDeposit(order, type);
            }
            if(flag){
                insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改订单详情");
            }else{
                result.setCode(1);
                result.setMsg("执行订单操作失败！");
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("执行订单操作出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }


    /**
     * 查看车源属性
     * @param orderCar
     * @return
     */
    @RequestMapping(value = "/queryCarAttr.do")
    @ResponseBody
    public Result queryCarAttr(AutoCar orderCar){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("carId", orderCar.getCarId());
            orderCar = autoCarService.query(map);
            List<AutoCarAttr> list= autoCarAttrService.queryList(map);
            orderCar.setAttr(list);
            result.setData(orderCar);
        }catch(Exception e){
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
            result.setCode(1);
            result.setMsg("查看车源属性出错");
        }
        return result;
    }
}
