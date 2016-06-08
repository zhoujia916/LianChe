package puzzle.lianche.controller.phone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.entity.*;
import puzzle.lianche.push.SmsPush;
import puzzle.lianche.service.IAutoCarAttrService;
import puzzle.lianche.service.IAutoCarService;
import puzzle.lianche.service.IAutoOrderService;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.EncryptUtil;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "phoneAutoOrderController")
@RequestMapping(value = "/phone/autoorder")
public class AutoOrderController extends BaseController {

    @Autowired
    private IAutoUserService autoUserService;

    @Autowired
    private IAutoCarService autoCarService;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    @Autowired
    private IAutoOrderService autoOrderService;
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
            if(order != null){
                result.setCode(-1);
                result.setMsg("订单数据不能为空！");
                return result;
            }
            if(order.getCars() == null || order.getCars().size() == 0){
                result.setCode(-1);
                result.setMsg("车源不能为空！");
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
            price *= orderCar.getCarNumber();
            //endregion
            if(price != orderCar.getCarPrice()){
                result.setCode(-1);
                result.setMsg("提交金额不正确！");
                return result;
            }

            //region Init Order Attr
            order.setAddTime(ConvertUtil.toLong(new Date()));
            order.setAmount(price);
            order.setOrderStatus(Constants.OS_SUBMIT);
            order.setPayStatus(Constants.PS_WAIT_BUYER_DEPOSIT);
            order.setShipStatus(Constants.SS_UNSHIP);
            order.setOrderSn(autoOrderService.createSn(ConvertUtil.toString(order.getBuyerId())));
            order.setShipTime(0);
            for(int i = 0; i < order.getCars().size(); i++){
                if(order.getCars().get(i).getSendNumber() == null){
                    order.getCars().get(i).setSendNumber(0);
                }
                if(order.getCars().get(i).getHasParts() == null){
                    order.getCars().get(i).setHasParts(Constants.AUTO_CAR_HAS_PARTS_NO);
                }
            }
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
     * 取消订单
     * @param order
     * @return
     */
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
//            if(order.getOrderStatus() == Constants.AUT)
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("取消订单出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 支付订金(买家支付订金和卖家支付订金逻辑不同)
     * @param orderId
     * @return
     */
    public Result pay(int orderId){
        Result result = new Result();
        try{

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }


    /**
     * 卖家拒绝订单
     * @param orderId
     * @return
     */
    public Result reject(int sellerId, int orderId){
        Result result = new Result();
        try{

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 买家确认收货
     * @param orderId
     * @return
     */
    public Result receive(int orderId){
        Result result = new Result();
        try{

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 查询订单列表
     * @param userId
     * @return
     */
    public Result list(int userId){
        Result result = new Result();
        try{

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }


    /**
     * 查看订单详情
     * @param orderId
     * @return
     */
    public Result show(int orderId) {
        Result result = new Result();
        try {

        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg() + e.getMessage());
        }
        return result;
    }
}