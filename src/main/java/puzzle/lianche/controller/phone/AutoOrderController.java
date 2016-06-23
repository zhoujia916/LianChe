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

import java.util.*;

@Controller(value = "phoneAutoOrderController")
@RequestMapping(value = "/phone/autoorder")
public class AutoOrderController extends BaseController {

    @Autowired
    private IAutoCarService autoCarService;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    @Autowired
    private IAutoCarPicService autoCarPicService;

    @Autowired
    private IAutoOrderService autoOrderService;

    @Autowired
    private IAutoOrderCarService autoOrderCarService;

    @Autowired
    private IAutoUserService autoUserService;


    /**
     * 取消订单
     * @param order
     * @return
     */
    @RequestMapping(value = "/cancel.do")
    @ResponseBody
    public Result cancel(AutoOrder order){
        Result result = new Result();
        try{
            if(order == null || ((order.getOrderId() == null && order.getOrderId() <= 0) && StringUtil.isNullOrEmpty(order.getOrderSn()))){
                result.setCode(-1);
                result.setMsg("订单不能为空!");
                return result;
            }
            if(order.getBuyerId() == null || order.getBuyerId() <= 0){
                result.setCode(-1);
                result.setMsg("用户不能为空!");
                return result;
            }
            int buyerId = order.getBuyerId();
            AutoUser find = autoUserService.query(buyerId, null);
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
            order = autoOrderService.query(order.getOrderId(), order.getOrderSn());
            if(order == null){
                result.setCode(-1);
                result.setMsg("该订单不存在!");
                return result;
            }
            if(order.getBuyerId() != buyerId){
                result.setCode(-1);
                result.setMsg("您没有取消该订单的权限!");
                return result;
            }
            //只有卖家未处理(接受或取消时)可取消
            if(order.getOrderStatus() != Constants.OS_SUBMIT){
                result.setCode(-1);
                result.setMsg("该订单不能取消！");
                return result;
            }
            if(!autoOrderService.doCancel(order)){
                result.setCode(1);
                result.setMsg("取消订单出错");
                return result;
            }

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("取消订单出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 通知系统买家已经支付订金
     * @param order
     * @return
     */
    @RequestMapping(value = "/deposit.do")
    @ResponseBody
    public Result deposit(AutoOrder order){
        Result result = new Result();
        try{
            //region Check Input
            if(order == null || (order.getOrderId() == null && StringUtil.isNullOrEmpty(order.getOrderSn())) ){
                result.setCode(-1);
                result.setMsg("订单不能为空！");
                return result;
            }
            if(order.getBuyerId() == null && order.getBuyerId() == 0){
                result.setCode(-1);
                result.setMsg("买家不能为空！");
                return result;
            }
            int buyerId = order.getBuyerId();
            AutoUser find = autoUserService.query(buyerId, null);
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
            order = autoOrderService.query(order.getOrderId(), order.getOrderSn());
            if(order == null){
                result.setCode(-1);
                result.setMsg("该订单不存在！");
                return result;
            }
            if(order.getBuyerId() != buyerId){
                result.setCode(-1);
                result.setMsg("您不能支付该订单订金！");
                return result;
            }
            if(order.getOrderStatus() != Constants.OS_SUBMIT ||
                order.getPayStatus() != Constants.PS_WAIT_BUYER_DEPOSIT ||
                order.getShipStatus() != Constants.SS_UNSHIP){
                result.setCode(-1);
                result.setMsg("该订单不能支付订金！");
                return result;
            }
            //endregion
            if(!autoOrderService.doDeposit(order)){
                result.setCode(1);
                result.setMsg("支付订金操作失败！");
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("支付订金操作失败！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }


    /**
     * 卖家拒绝订单
     * @param order
     * @return
     */
    @RequestMapping(value = "/reject.do")
    @ResponseBody
    public Result reject(AutoOrder order){
        Result result = new Result();
        try{
            if(order.getSellerId() != null && order.getSellerId() > 0){
                int sellerId = order.getSellerId();
                AutoUser find = autoUserService.query(sellerId, null);
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
                order = autoOrderService.query(order.getOrderId(), null);
                if(order == null){
                    result.setCode(-1);
                    result.setMsg("该订单不存在！");
                    return result;
                }
                if(order.getSellerId() != sellerId){
                    result.setCode(-1);
                    result.setMsg("您不能拒绝该订单！");
                    return result;
                }
                if(order.getOrderStatus() != Constants.OS_SUBMIT ||
                        order.getPayStatus() != Constants.PS_BUYER_PAY_DEPOSIT ||
                        order.getShipStatus() != Constants.SS_UNSHIP){
                    result.setCode(-1);
                    result.setMsg("该订单不能拒绝！");
                    return result;
                }
                if(!autoOrderService.doReject(order)){
                    result.setCode(1);
                    result.setMsg("拒绝订单操作失败！");
                    return result;
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("拒绝订单操作失败！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 卖家同意订单(已支付订金)
     * @param order
     * @return
     */
    @RequestMapping(value = "/accept.do")
    @ResponseBody
    public Result accept(AutoOrder order){
        Result result = new Result();
        try{
            if(order.getSellerId() == null || order.getSellerId() == 0){
                result.setCode(-1);
                result.setMsg("卖家不能为空！");
                return result;
            }
            int sellerId = order.getSellerId();
            AutoUser find = autoUserService.query(sellerId, null);
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
            order = autoOrderService.query(order.getOrderId(), null);
            if(order == null){
                result.setCode(-1);
                result.setMsg("该订单不存在！");
                return result;
            }
            if(order.getSellerId() != sellerId){
                result.setCode(-1);
                result.setMsg("您不能同意该订单！");
                return result;
            }
            if(order.getOrderStatus() != Constants.OS_SUBMIT ||
                    order.getPayStatus() != Constants.PS_BUYER_PAY_DEPOSIT ||
                    order.getShipStatus() != Constants.SS_UNSHIP){
                result.setCode(-1);
                result.setMsg("该订单不能同意！");
                return result;
            }
            if(!autoOrderService.doAccept(order)){
                result.setCode(1);
                result.setMsg("同意订单操作失败！");
                return result;
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("同意订单操作失败！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }


    /**
     * 买家确认收货
     * @param order
     * @return
     */
    @RequestMapping(value = "/receive.do")
    @ResponseBody
    public Result receive(AutoOrder order){
        Result result = new Result();
        try{
            if(order == null){
                result.setCode(-1);
                result.setMsg("订单不能为空！");
                return result;
            }
            if(order.getBuyerId() == null || order.getBuyerId() == 0) {
                result.setCode(-1);
                result.setMsg("买家不能为空！");
                return result;
            }
            int buyerId = order.getBuyerId();
            AutoUser find = autoUserService.query(buyerId, null);
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
            order = autoOrderService.query(order.getOrderId(), null);
            if(order == null){
                result.setCode(-1);
                result.setMsg("该订单不存在！");
                return result;
            }
            if(order.getBuyerId() != buyerId){
                result.setCode(-1);
                result.setMsg("您不能确认该订单收货！");
                return result;
            }
            if(order.getOrderStatus() != Constants.OS_EXECUTE ||
                    order.getPayStatus() != Constants.PS_SELLER_PAY_DEPOSIT ||
                    order.getShipStatus() != Constants.SS_UNSHIP){
                result.setCode(-1);
                result.setMsg("该订单不能确认收货！");
                return result;
            }
            if(!autoOrderService.doReceive(order)){
                result.setCode(1);
                result.setMsg("订单收货操作失败！");
                return result;
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("订单收货操作失败！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 卖家通知买家确认收货
     * @param order
     * @return
     */
    @RequestMapping(value = "/notify.do")
    @ResponseBody
    public Result notify(AutoOrder order){
        Result result = new Result();
        try{
            if(order == null){
                result.setCode(-1);
                result.setMsg("该订单不存在！");
                return result;
            }
            if(order.getSellerId() == null || order.getSellerId() == 0) {
                result.setCode(-1);
                result.setMsg("卖家不能为空！");
                return result;
            }
            int sellerId = order.getSellerId();
            AutoUser find = autoUserService.query(sellerId, null);
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
            order = autoOrderService.query(order.getOrderId(), null);
            if(order == null){
                result.setCode(-1);
                result.setMsg("该订单不存在！");
                return result;
            }
            if(order.getSellerId() != sellerId){
                result.setCode(-1);
                result.setMsg("您不能通知该订单收货！");
                return result;
            }
            if(order.getOrderStatus() != Constants.OS_EXECUTE ||
                    order.getPayStatus() != Constants.PS_SELLER_PAY_DEPOSIT ||
                    order.getShipStatus() != Constants.SS_UNSHIP){
                result.setCode(-1);
                result.setMsg("该订单不能通知收货！");
                return result;
            }
            if(!autoOrderService.doNotify(order)){
                result.setCode(1);
                result.setMsg("通知订单收货操作失败！");
                return result;
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("通知订单收货操作失败！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }


    /**
     * 提交订单信息
     * @param order 订单数据
     * @return
     */
    @RequestMapping(value = "/add.do")
    @ResponseBody
    public Result add(AutoOrder order){
        Result result = new Result();
        try{
            if(order == null){
                result.setCode(-1);
                result.setMsg("订单数据不能为空！");
                return result;
            }
            if(order.getCar() == null){
                result.setCode(-1);
                result.setMsg("车源不能为空！");
                return result;
            }
            if(order.getBuyerId() == null || order.getBuyerId() <= 0){
                result.setCode(-1);
                result.setMsg("买家不能为空！");
                return result;
            }
            AutoUser find = autoUserService.query(order.getBuyerId(), null);
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

            AutoOrderCar orderCar = order.getCar();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("carId", orderCar.getCarId());
            AutoCar car = autoCarService.query(map);
            if(car == null){
                result.setCode(-1);
                result.setMsg("车源不能为空！");
                return result;
            }

            if(StringUtil.isNullOrEmpty(orderCar.getCarAttrId())){
                result.setCode(-1);
                result.setMsg("请选择外观和内饰！");
                return result;
            }

            map.clear();
            map.put("carId", orderCar.getCarId());
            map.put("carAttrId", orderCar.getCarAttrId());
            AutoCarAttr attr = autoCarAttrService.query(map);
            if(attr == null){
                result.setCode(-1);
                result.setMsg("请选择外观和内饰！");
                return result;
            }


            //region Caculte price
            double price = car.getOfficalPrice();
            double salePrice = attr.getSalePriceType() == Constants.AUTO_CAR_SALE_PRICE_TYPE_MONEY ?
                               attr.getSaleAmount() :
                               (price * attr.getSaleAmount() / 100);
            if(attr.getQuoteType() == Constants.AUTO_CAR_QUOTE_TYPE_UP){
                price += salePrice;
            }
            else if(attr.getQuoteType() == Constants.AUTO_CAR_QUOTE_TYPE_DOWN){
                price -= salePrice;
            }
            if(orderCar.getHasParts() == Constants.AUTO_CAR_HAS_PARTS_YES){
                price += car.getPartsPrice();
            }


            //endregion
            if(price != orderCar.getCarPrice()){
                result.setCode(-1);
                result.setMsg("提交金额不正确！");
                return result;
            }

            //region Init Order Attr
            order.setOrderSn(autoOrderService.createSn(ConvertUtil.toString(order.getBuyerId())));

            order.setOrderStatus(Constants.OS_SUBMIT);
            order.setPayStatus(Constants.PS_WAIT_BUYER_DEPOSIT);
            order.setShipStatus(Constants.SS_UNSHIP);
            order.setSellerId(car.getAddUserId());
            order.setSellerDeposit(0);
            order.setBuyerDeposit(0);
            order.setShipTime(0);
            order.setAmount(price * orderCar.getCarNumber());
            order.setAddTime(ConvertUtil.toLong(new Date()));


            orderCar.setCarPrice(price);
            orderCar.setSendNumber(0);
            orderCar.setHasParts(Constants.AUTO_CAR_HAS_PARTS_NO);
            order.setCar(orderCar);
            //endregion

            if(!autoOrderService.insert(order)){
                result.setCode(1);
                result.setData("保存订单信息出错！");
            }

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("保存订单信息出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 查询订单列表
     * @param order
     * @param page
     * @return
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(AutoOrder order,Page page){
        Result result = new Result();
        try{
            if(order ==null || order.getBuyerId() == null || order.getBuyerId() == 0 ||
                    order.getClientStatus() == null){
                result.setCode(-1);
                result.setMsg("查询参数不正确！");
                return result;
            }
            AutoUser find = autoUserService.query(order.getBuyerId(), null);
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
            map.put("buyerId", order.getBuyerId());
            if(order.getClientStatus().equals(Constants.CS_UNDEPOSIT)){
                map.put("orderStatus", Constants.OS_SUBMIT);
                map.put("payStatus", Constants.PS_WAIT_BUYER_DEPOSIT);
                map.put("shipStatus", Constants.SS_UNSHIP);
            }
            else if(order.getClientStatus().equals(Constants.CS_DEPOSIT)){
                map.put("orderStatusList", Constants.OS_SUBMIT + "," + Constants.OS_ACCEPT + "," + Constants.OS_EXECUTE);
                map.put("payStatusList", Constants.PS_BUYER_PAY_DEPOSIT + "," + Constants.PS_WAIT_SELLER_DEPOSIT + "," + Constants.PS_SELLER_PAY_DEPOSIT);
                map.put("shipStatus", Constants.SS_UNSHIP);
            }
            else if(order.getClientStatus().equals(Constants.CS_SUCCESS)){
                map.put("orderStatusList", Constants.OS_SUCCESS);
                map.put("payStatusList", Constants.PS_SELLER_PAY_DEPOSIT + "," + Constants.PS_WAIT_RETURN_DEPOSIT + "," + Constants.PS_SYSTEM_RETURN_DEPOSIT );
                map.put("shipStatus", Constants.SS_SHIPED);
            }
            else if(order.getClientStatus().equals(Constants.CS_CANCEL)) {
                map.put("orderStatus", Constants.OS_CANCEL);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("curTime",System.currentTimeMillis());
            List<AutoCar> carList = autoCarService.queryOrderList(map,page);

            if(carList!=null && !carList.isEmpty()){
                JSONArray array=new JSONArray();
                for(AutoCar car:carList){
                    JSONObject object=new JSONObject();
                    object.put("collectId",car.getCollectId());
                    object.put("carId",car.getCarId());
                    object.put("carName",car.getCarName());
                    object.put("brandName",car.getBrandName());
                    object.put("catName",car.getCatName());
                    object.put("officalPrice",(car.getOfficalPrice()));
                    map.clear();
                    map.put("carPicId", car.getCarId());
                    AutoCarPic carPic=autoCarPicService.query(map);
                    if(carPic!=null){
                        object.put("pic",carPic.getPath());
                    }
                    map.clear();
                    map.put("attrCarId",car.getCarId());
                    List<AutoCarAttr> attrList=autoCarAttrService.queryList(map);
                    if(attrList!=null && attrList.size()>0){
                        List<AutoCarAttr> carAttrList=new ArrayList<AutoCarAttr>();
                        for(AutoCarAttr attrs:attrList){
                            carAttrList.add(attrs);
                        }
                        object.put("attrs",carAttrList);
                    }
                    object.put("status",car.getStatus());
                    object.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(car.getAddTime()),"MM-dd HH:mm"));
                    if(car.getUserStatus()==Constants.AUTO_USER_STATUS_AUTH_SUCCESS){
                        object.put("addUserAuth",1);
                    }else{
                        object.put("addUserAuth",0);
                    }
                    array.add(object);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("查询订单列表信息出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }


    /**
     * 查看订单详情
     * @param order
     * @return
     */
    @RequestMapping(value = "/show.do")
    @ResponseBody
    public Result show(AutoOrder order) {
        Result result = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if(order == null || order.getOrderId() == null){
                result.setCode(-1);
                result.setMsg("订单不能为空！");
                return result;
            }
            if((order.getBuyerId() == null || order.getBuyerId() == 0) &&
                    (order.getSellerId() != null || order.getSellerId() == 0)){
                result.setCode(-1);
                result.setMsg("用户不能为空！");
                return result;
            }

            if(order.getBuyerId() != null && order.getBuyerId() > 0){
                int buyerId = order.getBuyerId();
                AutoUser find = autoUserService.query(buyerId, null);
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

                order = autoOrderService.query(order.getOrderId(), null);
                if(order == null){
                    result.setCode(1);
                    result.setMsg("该订单不存在！");
                    return result;
                }
                if(buyerId != order.getBuyerId()){
                    result.setCode(1);
                    result.setMsg("您没有权限查看该订单！");
                    return result;
                }
            }
            else if(order.getSellerId() != null && order.getSellerId() > 0){
                int sellerId = order.getSellerId();
                AutoUser find = autoUserService.query(sellerId, null);
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
                order = autoOrderService.query(order.getOrderId(), null);
                if(order == null){
                    result.setCode(1);
                    result.setMsg("该订单不存在！");
                    return result;
                }
                if(sellerId != order.getSellerId()){
                    result.setCode(1);
                    result.setMsg("您没有权限查看该订单！");
                    return result;
                }
            }
            map.put("orderId", order.getOrderId());
            AutoOrderCar orderCar = autoOrderCarService.query(map);
            order.setCar(orderCar);
            result.setData(order);
        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("查看订单详情信息出错！");
            logger.error(result.getMsg() + e.getMessage());
        }
        return result;
    }
}