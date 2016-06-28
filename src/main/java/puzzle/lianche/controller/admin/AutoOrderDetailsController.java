package puzzle.lianche.controller.admin;

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
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/admin/autoorderdetails")
@Controller(value = "/adminAutoOrderDetailsController")
public class AutoOrderDetailsController extends ModuleController {
    @Autowired
    private IAutoOrderService autoOrderService;

    @Autowired
    private IAutoCarService autoCarService;

    @Autowired
    private IAutoOrderCarService autoOrderCarService;

    @Autowired
    private IAutoUserService autoUserService;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    @RequestMapping (value = "/index/{orderId}")
    public String show(@PathVariable String orderId){
        if(StringUtil.isNotNullOrEmpty(orderId)){
            Map map=new HashMap();
            map.put("orderId",orderId);
            AutoOrder order=autoOrderService.query(map);
            order.setAddTimeString(ConvertUtil.toString(ConvertUtil.toDate(order.getAddTime())));
            order.setPutTimeString(ConvertUtil.toString(ConvertUtil.toDate(order.getPutTime()),Constants.DATE_FORMAT));
            order.setOrderStatusString(Constants.ORDER_STATUS.get(order.getOrderStatus()));
            order.setPayStatusString(Constants.PAY_STATUS.get(order.getPayStatus()));
            order.setShipStatusString(Constants.SHIP_STATUS.get(order.getShipStatus()));
            List<String> list=autoOrderService.queryOperate(order,Constants.ORDER_USER_ADMIN);
            if(list!=null && list.size()>0){
                List<String> orderActions=new ArrayList<String>();
                for(String action:list){
                    orderActions.add(Constants.OO_OPERATE.get(action));
                }
                this.setModelAttribute("orderActions",orderActions);
            }
            map.clear();
            map.put("userId",order.getBuyerId());
            AutoUser buyerUser=autoUserService.query(map);
            buyerUser.setShopTypeString(Constants.MAP_AUTO_COLLECT_TYPE.get(buyerUser.getShopType()));
            map.clear();
            map.put("userId",order.getSellerId());
            AutoUser sellerUser=autoUserService.query(map);
            sellerUser.setShopTypeString(Constants.MAP_AUTO_COLLECT_TYPE.get(sellerUser.getShopType()));
            map.clear();
            map.put("orderId",order.getOrderId());
            AutoCar car=autoCarService.query(map);
            car.setCarTypeString(Constants.MAP_AUTO_CAR_TYPE.get(car.getCarType()));
            map.clear();
            map.put("attrCarId",car.getCarId());
            map.put("carAttrId",order.getCarAttrId());
            AutoCarAttr carAttr=autoCarAttrService.query(map);
            carAttr.setOutsideColor("外观:"+carAttr.getOutsideColor()+"-内饰:"+carAttr.getInsideColor());
            this.setModelAttribute("orderId",orderId);
            this.setModelAttribute("order",order);
            this.setModelAttribute("car",car);
            this.setModelAttribute("buyerUser",buyerUser);
            this.setModelAttribute("sellerUser",sellerUser);
            this.setModelAttribute("carAttr",carAttr);
        }
        return Constants.UrlHelper.ADMIN_ORDER_DETAILS;
    }

    @RequestMapping("/action.do")
    @ResponseBody
    public Result action(String action,Integer orderId){
        Result result=new Result();
        Map map=new HashMap();
        map.put("orderId",orderId);
        AutoOrder order=autoOrderService.query(map);
        try{
            boolean flg=false;
            if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_CANCEL)){
                flg=autoOrderService.doCancel(order);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_PAYMENT)){
                flg=autoOrderService.doDeposit(order);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_UNPAYMENT)){
                order.setPayStatus(Constants.PS_WAIT_BUYER_DEPOSIT);
                flg=autoOrderService.update(order);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_ACCEPT)){
                flg=autoOrderService.doAccept(order);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_UNACCEPT)){
                flg=autoOrderService.doReject(order);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_REJECT)){
                flg=autoOrderService.doReject(order);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_RECEIVE)){
                flg=autoOrderService.doReceive(order);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_NOTIFY_RECEIVE)){
                flg=autoOrderService.doNotify(order);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_RETURN_BUYER_DEPOSIT)){
                Integer type=0;
                if(order.getPayStatus()==Constants.PS_BUYER_PAY_DEPOSIT) {
                    type=Constants.ORDER_USER_BUYER;
                }
                if(order.getPayStatus()==Constants.PS_BUYER_PAY_DEPOSIT && order.getPayStatus()==Constants.PS_SELLER_PAY_DEPOSIT){
                    type=Constants.ORDER_USER_ALL;
                }
                flg = autoOrderService.doReturnDeposit(order, type);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_RETURN_SELLER_DEPOSIT)){
                Integer type=0;
                if(order.getPayStatus()==Constants.PS_SELLER_PAY_DEPOSIT) {
                    type=Constants.ORDER_USER_SELLER;
                }
                if(order.getPayStatus()==Constants.PS_SELLER_PAY_DEPOSIT && order.getPayStatus()==Constants.PS_BUYER_PAY_DEPOSIT){
                    type=Constants.ORDER_USER_ALL;
                }
                flg = autoOrderService.doReturnDeposit(order, type);
            }
            if(flg){
                insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改订单详情");
            }else{
                result.setCode(1);
                result.setMsg("修改订单相关状态出错！");
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("执行订单操作出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
