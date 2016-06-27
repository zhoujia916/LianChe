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
import puzzle.lianche.entity.AutoCar;
import puzzle.lianche.entity.AutoCarAttr;
import puzzle.lianche.entity.AutoOrder;
import puzzle.lianche.service.IAutoCarAttrService;
import puzzle.lianche.service.IAutoCarService;
import puzzle.lianche.service.IAutoOrderService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/admin/orderdetails")
@Controller(value = "/adminOrderDetailsController")
public class OrderDetailsController extends ModuleController{

    @Autowired
    private IAutoOrderService autoOrderService;

    @Autowired
    private IAutoCarService autoCarService;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    @RequestMapping (value = "/index/{orderId}")
    public String show(@PathVariable String orderId){
        System.out.println("---");
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
            this.setModelAttribute("orderActions",list);
        }
        return Constants.UrlHelper.ADMIN_ORDER_DETAILS;
    }

    @RequestMapping("/action.do")
    @ResponseBody
    public String action(String action,Integer orderId){
        AutoOrder order=new AutoOrder();
        order.setOrderId(orderId);
        try{
            if(action.equalsIgnoreCase(Constants.OO_CANCEL)){
            }else if(action.equalsIgnoreCase(Constants.OO_PAYMENT)){
            }else if(action.equalsIgnoreCase(Constants.OO_UNPAYMENT)){
            }else if(action.equalsIgnoreCase(Constants.OO_ACCEPT)){
            }else if(action.equalsIgnoreCase(Constants.OO_UNACCEPT)){
            }else if(action.equalsIgnoreCase(Constants.OO_REJECT)){
            }else if(action.equalsIgnoreCase(Constants.OO_RECEIVE)){
            }else if(action.equalsIgnoreCase(Constants.OO_NOTIFY_RECEIVE)){
            }else if(action.equalsIgnoreCase(Constants.OO_RETURN_BUYER_DEPOSIT)){
            }else if(action.equalsIgnoreCase(Constants.OO_RETURN_SELLER_DEPOSIT)){
            }
            if(autoOrderService.update(order)){
                return "true";
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return "true";
    }
}
