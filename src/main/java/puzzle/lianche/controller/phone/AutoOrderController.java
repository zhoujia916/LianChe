package puzzle.lianche.controller.phone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.entity.*;
import puzzle.lianche.service.*;
import puzzle.lianche.utils.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "phoneAutoOrderController")
@RequestMapping(value = "/phone/autoorder")
public class AutoOrderController extends BaseController {

    @Autowired
    private IAutoCarService autoCarService;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    @Autowired
    private IAutoOrderService autoOrderService;

    @Autowired
    private IAutoOrderCarService autoOrderCarService;


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
            if(order == null){
                result.setCode(-1);
                result.setMsg("取消订单不能为空!");
                return result;
            }
            if(order.getOrderId() == null || order.getOrderId() <= 0){
                result.setCode(-1);
                result.setMsg("取消订单不能为空!");
                return result;
            }
            if(order.getBuyerId() == null || order.getBuyerId() <= 0){
                result.setCode(-1);
                result.setMsg("取消订单用户不能为空!");
                return result;
            }
            int buyerId = order.getBuyerId();
            int sellerId = order.getSellerId();
            order = autoOrderService.query(order.getOrderId(), null);
            if(order == null){
                result.setCode(-1);
                result.setMsg("取消订单不能为空!");
                return result;
            }
            if(order.getBuyerId() != buyerId && sellerId != order.getSellerId()){
                result.setCode(-1);
                result.setMsg("您没有取消该订单的权限!");
                return result;
            }
            if(order.getOrderStatus() != Constants.OS_SUBMIT && order.getOrderStatus() != Constants.OS_ACCEPT &&
               order.getOrderStatus() != Constants.OS_EXECUTE){
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
     * 支付订金(买家支付订金和卖家支付订金逻辑不同)
     * @param order
     * @return
     */
    @RequestMapping(value = "/deposit.do")
    @ResponseBody
    public Result deposit(AutoOrder order){
        Result result = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            if(order == null || order.getOrderId() == null || order.getOrderId() <= 0){
                result.setCode(-1);
                result.setMsg("订单不能为空！");
                return result;
            }
            if(order.getBuyerId() != null && order.getBuyerId() > 0){
                int buyerId = order.getBuyerId();
                order = autoOrderService.query(order.getOrderId(), null);
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
                if(!autoOrderService.doDeposit(order)){
                    result.setCode(1);
                    result.setMsg("支付订金操作失败！");
                    return result;
                }
            }
            else if(order.getSellerId() != null && order.getSellerId() > 0){
                int sellerId = order.getSellerId();
                order = autoOrderService.query(order.getOrderId(), null);
                if(order == null){
                    result.setCode(-1);
                    result.setMsg("该订单不存在！");
                    return result;
                }
                if(order.getSellerId() != sellerId){
                    result.setCode(-1);
                    result.setMsg("您不能支付该订单订金！");
                    return result;
                }
                if(order.getOrderStatus() != Constants.OS_SUBMIT ||
                   order.getPayStatus() != Constants.PS_WAIT_SELLER_DEPOSIT ||
                   order.getShipStatus() != Constants.SS_UNSHIP){
                    result.setCode(-1);
                    result.setMsg("该订单不能支付订金！");
                    return result;
                }
                if(!autoOrderService.doDeposit(order)){
                    result.setCode(1);
                    result.setMsg("支付订金操作失败！");
                    return result;
                }
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
     * 卖家同意订单
     * @param order
     * @return
     */
    @RequestMapping(value = "/accept.do")
    @ResponseBody
    public Result accept(AutoOrder order){
        Result result = new Result();
        try{
            if(order.getSellerId() != null && order.getSellerId() > 0){
                int sellerId = order.getSellerId();
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
            if(order.getCars() == null || order.getCars().size() <= 0){
                result.setCode(-1);
                result.setMsg("车源不能为空！");
                return result;
            }
            if(order.getBuyerId() == null || order.getBuyerId() <= 0){
                result.setCode(-1);
                result.setMsg("买家不能为空！");
                return result;
            }

            AutoOrderCar orderCar = order.getCars().get(0);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("carId", orderCar.getCarId());
            AutoCar car = autoCarService.query(map);
            if(car == null){
                result.setCode(-1);
                result.setMsg("车源不能为空！");
                return result;
            }

            if(StringUtil.isNullOrEmpty(orderCar.getCarAttrId()) || StringUtil.isNullOrEmpty(orderCar.getCarAttr())){
                result.setCode(-1);
                result.setMsg("请选择外观和内饰！");
                return result;
            }

            map.clear();
            map.put("carId", orderCar.getCarId());
            map.put("carAttrIds", orderCar.getCarAttrId());
            List<AutoCarAttr> attrs = autoCarAttrService.queryList(map);
            if(attrs == null || attrs.size() != 2){
                result.setCode(-1);
                result.setMsg("请选择外观和内饰！");
                return result;
            }
            if(orderCar.getCarNumber() == null || orderCar.getCarNumber() <= 0 || orderCar.getCarNumber() >= car.getSurplusNumber()){
                result.setCode(-1);
                result.setMsg("预订数量不正确！");
                return result;
            }
            //region Caculte price
            double price = car.getOfficalPrice();
            double salePrice = car.getSalePriceType() == Constants.AUTO_CAR_SALE_PRICE_TYPE_MONEY ?
                    car.getSaleAmount() :
                    (price * car.getSaleAmount() / 100);
            if(car.getQuoteType() == Constants.AUTO_CAR_QUOTE_TYPE_UP){
                price += salePrice;
            }
            else if(car.getQuoteType() == Constants.AUTO_CAR_QUOTE_TYPE_DOWN){
                price -= salePrice;
            }
            if(orderCar.getHasParts() == Constants.AUTO_CAR_HAS_PARTS_YES){
                price += car.getPartsPrice();
            }
            price += attrs.get(0).getAttrPrice() + attrs.get(1).getAttrPrice();

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
     * @return
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(AutoOrder order, Page page){
        Result result = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            if(page == null || page.getPageIndex() < 0 || page.getPageSize() < 0){
                result.setCode(-1);
                result.setMsg("分页参数不能为空！");
                return result;
            }
            if(order == null){
                result.setCode(-1);
                result.setMsg("查询参数不能为空！");
                return result;
            }
            if((order.getBuyerId() == null || order.getBuyerId() == 0) &&
               (order.getSellerId() != null || order.getSellerId() == 0)){
                result.setCode(-1);
                result.setMsg("查询用户不能为空！");
                return result;
            }
            if(order.getBuyerId() != null && order.getBuyerId() > 0){
                map.put("buyerId", order.getBuyerId());
                if(order.getClientStatus().equals(Constants.CS_UNDEPOSIT)){
                    map.put("orderStatus", Constants.OS_SUBMIT);
                    map.put("payStatus", Constants.PS_WAIT_BUYER_DEPOSIT);
                    map.put("shipStatus", Constants.SS_UNSHIP);
                }
                else if(order.getClientStatus().equals(Constants.CS_DEPOSIT)){
                    map.put("orderStatusList", new int[] { Constants.OS_SUBMIT, Constants.OS_ACCEPT, Constants.OS_EXECUTE});
                    map.put("payStatusList", new int[] { Constants.PS_BUYER_PAY_DEPOSIT, Constants.PS_WAIT_SELLER_DEPOSIT, Constants.PS_SELLER_PAY_DEPOSIT } );
                    map.put("shipStatus", Constants.SS_UNSHIP);
                }
                else if(order.getClientStatus().equals(Constants.CS_SUCCESS)){
                    map.put("orderStatusList", new int[] { Constants.OS_SUCCESS });
                    map.put("payStatusList", new int[] { Constants.PS_WAIT_RETURN_DEPOSIT, Constants.PS_SYSTEM_RETURN_DEPOSIT } );
                    map.put("shipStatus", Constants.SS_SHIPED);
                }
                else if(order.getClientStatus().equals(Constants.CS_CANCEL)) {
                    map.put("orderStatus", Constants.OS_CANCEL);
                }
            }
            else if(order.getBuyerId() != null && order.getSellerId() > 0){
                map.put("sellerId", order.getSellerId());
                if(order.getClientStatus().equals(Constants.CS_UNDEPOSIT)){
                    map.put("orderStatus", Constants.OS_ACCEPT);
                    map.put("payStatus", Constants.PS_WAIT_SELLER_DEPOSIT);
                    map.put("shipStatus", Constants.SS_UNSHIP);
                }
                else if(order.getClientStatus().equals(Constants.CS_DEPOSIT)){
                    map.put("orderStatus", Constants.OS_EXECUTE);
                    map.put("payStatusList", Constants.PS_SELLER_PAY_DEPOSIT );
                    map.put("shipStatus", Constants.SS_UNSHIP);
                }
                else if(order.getClientStatus().equals(Constants.CS_SUCCESS)){
                    map.put("orderStatusList", new int[] { Constants.OS_SUCCESS });
                    map.put("payStatusList", new int[] { Constants.PS_WAIT_RETURN_DEPOSIT, Constants.PS_SYSTEM_RETURN_DEPOSIT } );
                    map.put("shipStatus", Constants.SS_SHIPED);
                }
                else if(order.getClientStatus().equals(Constants.CS_CANCEL)){
                    map.put("orderStatus", Constants.OS_CANCEL);
                }

            }
            List<AutoOrder> orders = autoOrderService.queryList(map, page);
            result.setData(orders);
            result.setTotal(page.getTotal());

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
                result.setMsg("查询用户不能为空！");
                return result;
            }
            if(order.getBuyerId() != null && order.getBuyerId() > 0){
                int buyerId = order.getBuyerId();
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
            order.setCars(autoOrderCarService.queryList(map));
        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("查看订单详情信息出错！");
            logger.error(result.getMsg() + e.getMessage());
        }
        return result;
    }
}