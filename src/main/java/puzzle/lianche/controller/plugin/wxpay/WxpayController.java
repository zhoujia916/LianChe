package puzzle.lianche.controller.plugin.wxpay;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.controller.plugin.wxpay.config.WxConfig;
import puzzle.lianche.controller.plugin.wxpay.util.WxPayAPI;
import puzzle.lianche.controller.plugin.wxpay.util.WxPayResult;
import puzzle.lianche.entity.AutoCar;
import puzzle.lianche.entity.AutoOrder;
import puzzle.lianche.service.IAutoCarService;
import puzzle.lianche.service.IAutoOrderCarService;
import puzzle.lianche.service.IAutoOrderService;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

@Controller(value = "pluginWxpayController")
public class WxpayController extends BaseController {
    @Autowired
    private IAutoOrderService autoOrderService;

    @Autowired
    private IAutoCarService autoCarService;

    /**
     * 申请生成支付链接
     * @param order
     */
    @RequestMapping(value = {"/alipay"})
    public void payment(AutoOrder order) {
        if (order == null || order.getOrderId() == null ||
                (order.getOrderId() <= 0 && StringUtil.isNullOrEmpty(order.getOrderSn()))) {
            Result result = new Result();
            result.setCode(-1);
            result.setMsg("订单不能为空！");
            this.writeJson(result);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Integer buyerId = order.getBuyerId();
            Integer sellerId = order.getSellerId();
            order = autoOrderService.query(order.getOrderId(), order.getOrderSn());
            if (order == null) {
                Result result = new Result();
                result.setCode(-1);
                result.setMsg("该订单不存在！");
                this.writeJson(result);
                return;
            }
            if (buyerId != order.getBuyerId() && sellerId != order.getSellerId()) {
                Result result = new Result();
                result.setCode(-1);
                result.setMsg("您不能支付该笔订单订金！");
                this.writeJson(result);
                return;
            }
            if(order.getPayStatus() != Constants.PS_WAIT_BUYER_DEPOSIT &&
                    order.getPayStatus() != Constants.PS_WAIT_SELLER_DEPOSIT) {
                Result result = new Result();
                result.setCode(-1);
                result.setMsg("该订单不能支付订金！");
                this.writeJson(result);
                return;
            }
            map.put("orderId", order.getOrderId());
            AutoCar car = autoCarService.query(map);
            if(car == null){
                Result result = new Result();
                result.setCode(-1);
                result.setMsg("该订单没有预订车辆！");
                this.writeJson(result);
                return;
            }
            WxPayResult payResult = WxPayAPI.unifiedOrder(order.getOrderSn(), car.getCarName(), (int)(order.getAmount() * 100), request.getRemoteHost());
            if(payResult == null){
                Result result = new Result();
                result.setCode(-1);
                result.setMsg("微信申请支付操作失败！");
                this.writeJson(result);
                return;
            }

            JSONObject result = new JSONObject();
            result.put("appid", payResult.getValue("appid"));
            result.put("partnerid", payResult.getValue("partnerid"));
            result.put("prepayid", payResult.getValue("prepayid"));
            result.put("package", payResult.getValue("package"));
            result.put("noncestr", payResult.getValue("noncestr"));
            result.put("timestamp", payResult.getValue("timestamp"));
            result.put("sign", payResult.getValue("sign"));
            this.writeText(result.toString());
        }
        catch (Exception e){

        }
    }

    @RequestMapping(value = {"/plugin/wxpay/notify"})
    public void inform(){

    }
}
