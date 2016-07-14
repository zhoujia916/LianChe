package puzzle.lianche.controller.plugin.alipay;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.controller.plugin.alipay.config.AlipayConfig;
import puzzle.lianche.controller.plugin.alipay.sign.RSA;
import puzzle.lianche.controller.plugin.alipay.util.AlipayCore;
import puzzle.lianche.entity.AutoCar;
import puzzle.lianche.entity.AutoCarAttr;
import puzzle.lianche.entity.AutoOrder;
import puzzle.lianche.entity.AutoOrderCar;
import puzzle.lianche.service.IAutoCarAttrService;
import puzzle.lianche.service.IAutoCarService;
import puzzle.lianche.service.IAutoOrderCarService;
import puzzle.lianche.service.IAutoOrderService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.FileUtil;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller(value = "pluginAlipayController")
@RequestMapping(value = {"/plugin"})
public class AlipayController extends BaseController {

    @Autowired
    private IAutoOrderService autoOrderService;

    @Autowired
    private IAutoOrderCarService autoOrderCarService;

    @Autowired
    private IAutoCarService autoCarService;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    /**
     * 申请生成支付链接
     * @param order
     */
    @RequestMapping(value = {"/alipay"})
    @ResponseBody
    public Result payment(AutoOrder order){
        Result result = new Result();
        if(order == null || order.getOrderId() == null ||
          (order.getOrderId() <= 0 && StringUtil.isNullOrEmpty(order.getOrderSn()))){
            result.setCode(-1);
            result.setMsg("订单不能为空！");
            return result;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Integer buyerId = order.getBuyerId();
            Integer sellerId = order.getSellerId();
            order = autoOrderService.query(order.getOrderId(), order.getOrderSn());
            if(order == null){
                result.setCode(-1);
                result.setMsg("该订单不存在！");
                return result;
            }
            if(buyerId != order.getBuyerId() && sellerId != order.getSellerId()){
                result.setCode(-1);
                result.setMsg("您不能支付该笔订单订金！");
                return result;
            }

            if(order.getPayStatus() != Constants.PS_WAIT_BUYER_DEPOSIT &&
                    order.getPayStatus() != Constants.PS_BUYER_PAY_DEPOSIT) {
                result.setCode(-1);
                result.setMsg("该订单不能支付订金！");
                return result;
            }
            map.put("orderId", order.getOrderId());
            AutoCar car = autoCarService.query(map);
            if(car == null){
                result.setCode(-1);
                result.setMsg("该订单没有预订车辆！");
                return result;
            }
            car.setCarName(new String(car.getCarName().getBytes(), AlipayConfig.input_charset));

            Map<String, String> payinfo = new HashMap<String, String>();
            // 必填，接口名称，固定值
            payinfo.put("service", "\"mobile.securitypay.pay\"");
            // 必填，合作商户号
            payinfo.put("partner", "\"" + AlipayConfig.partner + "\"");
            // 必填，参数编码字符集
            payinfo.put("_input_charset", "\"" + AlipayConfig.input_charset + "\"");
            // 必填，服务器异步通知页面路径
            payinfo.put("notify_url", "\"" + AlipayConfig.notify_url + "\"");
            // 必填，商户网站唯一订单号
            payinfo.put("out_trade_no", "\"" + order.getOrderSn() + "\"");
            // 必填，商品名称
            payinfo.put("subject", "\"" + car.getCarName() + "\"");
            // 必填，支付类型
            payinfo.put("payment_type", "\"1\"");
            // 可选，买家ID
            payinfo.put("seller_id", "\"" + AlipayConfig.account + "\"");
            // 必填，总金额，取值范围为[0.01,100000000.00]
            boolean isTest = ConvertUtil.toBool(initConfig.getProperty("test"));
            if(isTest){
                payinfo.put("total_fee", "\"" + ConvertUtil.toDouble(initConfig.getProperty("order.depositTest")) + "\"");
            }else{
                map.clear();
                map.put("orderId", order.getOrderId());
                AutoCarAttr attr = autoCarAttrService.query(map);

                AutoOrderCar orderCar = autoOrderCarService.query(map);

                double price = car.getOfficalPrice();
                if(attr.getQuoteType() == Constants.AUTO_CAR_QUOTE_TYPE_UP){
                    if(attr.getSalePriceType() == Constants.AUTO_CAR_SALE_PRICE_TYPE_MONEY){
                        price += attr.getSaleAmount();
                    }
                    else if(attr.getSalePriceType() == Constants.AUTO_CAR_SALE_PRICE_TYPE_PERCENT){
                        price += price * attr.getSaleAmount() / 100;
                    }
                }
                else if(attr.getQuoteType() == Constants.AUTO_CAR_QUOTE_TYPE_DOWN){
                    if(attr.getSalePriceType() == Constants.AUTO_CAR_SALE_PRICE_TYPE_MONEY){
                        price -= attr.getSaleAmount();
                    }
                    else if(attr.getSalePriceType() == Constants.AUTO_CAR_SALE_PRICE_TYPE_PERCENT){
                        price -= price * attr.getSaleAmount() / 100;
                    }
                }
                if(car.getOrderHasParts() == Constants.AUTO_CAR_HAS_PARTS_YES){
                    price += car.getPartsPrice();
                }

                double depositPercent = ConvertUtil.toDouble(initConfig.getProperty("order.depositPercent"));
                double depositMax = ConvertUtil.toDouble(initConfig.getProperty("order.depositMax"));
                double deposit = Math.min(price * depositPercent, depositMax);

                payinfo.put("total_fee", "\"" + ConvertUtil.toString(deposit * orderCar.getCarNumber()) + "\"");
            }
            // 必填，商品详情
            payinfo.put("body", "\"" + car.getCarName() + "\"");
            // 可选，未付款交易的超时时间
//            payinfo.put("it_b_pay", "\"3d\"");

            String orderInfo = AlipayCore.createLinkString(payinfo);

            // 对订单做RSA 签名(目前缺少private_key)
            String sign = RSA.sign(orderInfo, AlipayConfig.private_key, AlipayConfig.input_charset); //支付宝提供的Config.cs
            //仅需对sign做URL编码
            sign = URLEncoder.encode(sign, "utf-8");

            String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"" + AlipayConfig.sign_type + "\"";

            result.setData(payInfo);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 接收支付回调通知(一般情况下25小时内8次通知，频率一般是2m 10m 10m 1h 2h 6h 15h)
     */
    @RequestMapping(value = {"/alipay/notify"})
    public void inform(){
        try {
            String orderSn = getParameter("out_trade_no");
            String totalFee = getParameter("total_fee");
            if(StringUtil.isNullOrEmpty(orderSn) || StringUtil.isNotNullOrEmpty(totalFee)) {
                return;
            }
            AutoOrder order = autoOrderService.query(null, orderSn);
            if(order == null)
                return;
            if(order.getAmount() != ConvertUtil.toDouble(totalFee)){
                return;
            }
            if(order.getPayStatus() != Constants.PS_WAIT_BUYER_DEPOSIT){
                if(autoOrderService.doDeposit(order)) {
                    this.writeText("success");
                }
            }
            else if(order.getPayStatus() != Constants.PS_WAIT_SELLER_DEPOSIT){
                if(autoOrderService.doAccept(order)) {
                    this.writeText("success");
                }
            }
        }
        catch (Exception e){
        }
    }
}
