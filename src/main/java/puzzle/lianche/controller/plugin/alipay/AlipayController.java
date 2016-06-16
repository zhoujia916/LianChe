package puzzle.lianche.controller.plugin.alipay;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.controller.plugin.alipay.config.AlipayConfig;
import puzzle.lianche.controller.plugin.alipay.sign.RSA;
import puzzle.lianche.controller.plugin.alipay.util.AlipayCore;
import puzzle.lianche.entity.AutoCar;
import puzzle.lianche.entity.AutoOrder;
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
    private IAutoCarService autoCarService;

    /**
     * 申请生成支付链接
     * @param order
     */
    @RequestMapping(value = {"/alipay"})
    public void payment(AutoOrder order){
        if(order == null || order.getOrderId() == null ||
          (order.getOrderId() <= 0 && StringUtil.isNullOrEmpty(order.getOrderSn()))){
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
            if(order == null){
                Result result = new Result();
                result.setCode(-1);
                result.setMsg("该订单不存在！");
                this.writeJson(result);
                return;
            }
            if(buyerId != order.getBuyerId() && sellerId != order.getSellerId()){
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
            payinfo.put("total_fee", "\"" + ConvertUtil.toString(order.getAmount()) + "\"");
            // 必填，商品详情
            payinfo.put("body", "\"" + car.getCarName() + "\"");
            // 可选，未付款交易的超时时间
//            payinfo.put("it_b_pay", "\"3d\"");

            String orderInfo = AlipayCore.createLinkString(payinfo);

            // 对订单做RSA 签名(目前缺少private_key)
            String sign = RSA.sign(orderInfo, getKey("private"), AlipayConfig.input_charset); //支付宝提供的Config.cs
            //仅需对sign做URL编码
            sign = URLEncoder.encode(sign, "utf-8");

            String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"" + AlipayConfig.sign_type + "\"";

            this.writeText(payInfo);
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
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
            if(order.getPayStatus() != Constants.PS_WAIT_BUYER_DEPOSIT &&
               order.getPayStatus() != Constants.PS_WAIT_SELLER_DEPOSIT){
                if(order.getAmount() != ConvertUtil.toDouble(totalFee)){
                    return;
                }
                if(autoOrderService.doDeposit(order)) {
                    this.writeText("success");
                }
            }
        }
        catch (Exception e){
        }
    }

    public String getKey(String type){
        String key = null;
        if(type.equals("private")){
            key = AlipayConfig.private_key;
            if(StringUtil.isNullOrEmpty(key)){
                String path = System.getProperty("user.dir") + "\\WEB-INF\\classes\\alipaykey\\pkcs8_rsa_private_key.pem";
                key = FileUtil.readFile(path);
                AlipayConfig.private_key = key;
            }
        }
        else if(type.equals("public")){
            key = AlipayConfig.public_key;
            if(StringUtil.isNullOrEmpty(key)){
                String path = System.getProperty("user.dir") + "\\WEB-INF\\classes\\alipaykey\\rsa_public_key.pem";
                key = FileUtil.readFile(path);
                AlipayConfig.public_key = key;
            }

        }
        return key;
    }
}
