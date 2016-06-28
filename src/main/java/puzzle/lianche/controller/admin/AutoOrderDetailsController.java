package puzzle.lianche.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.AutoCar;
import puzzle.lianche.entity.AutoCarAttr;
import puzzle.lianche.entity.AutoOrder;
import puzzle.lianche.service.IAutoCarAttrService;
import puzzle.lianche.service.IAutoCarService;
import puzzle.lianche.service.IAutoOrderService;
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
    private IAutoCarAttrService autoCarAttrService;

    @RequestMapping (value = "/index/{orderId}")
    public String show(@PathVariable String orderId){
        if(StringUtil.isNotNullOrEmpty(orderId)){
            Map map=new HashMap();
            map.put("orderId",orderId);
            AutoOrder order=autoOrderService.query(map);
            order.setAddTimeString(ConvertUtil.toString(ConvertUtil.toDate(order.getAddTime())));
            order.setPutTimeString(ConvertUtil.toString(ConvertUtil.toDate(order.getPutTime())));
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
            map.put("orderId",order.getOrderId());
            AutoCar car=autoCarService.query(map);
            car.setCarTypeString(Constants.MAP_AUTO_CAR_TYPE.get(car.getCarType()));
            map.clear();
            map.put("attrCarId",car.getCarId());
            List<AutoCarAttr> attrList=autoCarAttrService.queryList(map);
            if(attrList!=null && attrList.size()>0) {
                int totalNumber = 0;
                int lockNumber = 0;
                int surplusNumber = 0;
                for (AutoCarAttr attrs : attrList) {
                    totalNumber += attrs.getTotalNumber();
                    lockNumber += attrs.getLockNumber();
                    surplusNumber += attrs.getSurplusNumber();
                }
                AutoCarAttr carAttr=new AutoCarAttr();
                carAttr.setTotalNumber(totalNumber);
                carAttr.setLockNumber(lockNumber);
                carAttr.setSurplusNumber(surplusNumber);
                this.setModelAttribute("carAttr",carAttr);
            }
            this.setModelAttribute("orderId",orderId);
            this.setModelAttribute("order",order);
            this.setModelAttribute("car",car);
        }
        return Constants.UrlHelper.ADMIN_ORDER_DETAILS;
    }

    @RequestMapping("/action.do")
    @ResponseBody
    public Result action(String action,Integer orderId){
        Result result=new Result();
        AutoOrder order=new AutoOrder();
        order.setOrderId(orderId);
        try{
            if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_CANCEL)){
                order.setOrderStatus(Constants.OS_CANCEL);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_PAYMENT)){
                order.setPayStatus(Constants.PS_BUYER_PAY_DEPOSIT);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_UNPAYMENT)){
                order.setPayStatus(Constants.PS_WAIT_BUYER_DEPOSIT);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_ACCEPT)){
//                order.setPayStatus(Constants.);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_UNACCEPT)){

            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_REJECT)){

            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_RECEIVE)){
                order.setShipStatus(Constants.SS_UNSHIP);
            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_NOTIFY_RECEIVE)){

            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_RETURN_BUYER_DEPOSIT)){

            }else if(Constants.OO_ACTIONS.get(action).equalsIgnoreCase(Constants.OO_RETURN_SELLER_DEPOSIT)){

            }
            if(autoOrderService.update(order)){
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
