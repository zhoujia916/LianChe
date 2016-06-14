package puzzle.lianche.controller.plugin;


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
import puzzle.lianche.utils.StringUtil;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller(value = "pluginAlipayController")
public class AlipayController extends BaseController {

    @Autowired
    private IAutoOrderService autoOrderService;
    @Autowired
    private IAutoOrderCarService autoOrderCarService;
    @Autowired
    private IAutoCarService autoCarService;

    //region PHP process
//    // 获取支付金额
//    $amount='';
//    if($_SERVER['REQUEST_METHOD']=='POST'){
//        $amount=$_POST['total'];
//    }else{
//        $amount=$_GET['total'];
//    }
//    $total = floatval($amount);
//    if(!$total){
//        $total = 1;
//    }
//    // 对签名字符串转义
//    function createLinkstring($para) {
//        $arg  = "";
//        while (list ($key, $val) = each ($para)) {
//            $arg.=$key.'="'.$val.'"&';
//        }
//        //去掉最后一个&字符
//        $arg = substr($arg,0,count($arg)-2);
//        //如果存在转义字符，那么去掉转义
//        if(get_magic_quotes_gpc()){$arg = stripslashes($arg);}
//        return $arg;
//    }
//    // 签名生成订单信息
//    function rsaSign($data) {
//        $priKey = "-----BEGIN RSA PRIVATE KEY-----
//        生成密钥时获取的私钥字符串，直接使用pem文件的完整字符串
//                -----END RSA PRIVATE KEY-----";
//        $res = openssl_get_privatekey($priKey);
//        openssl_sign($data, $sign, $res);
//        openssl_free_key($res);
//        $sign = base64_encode($sign);
//        $sign = urlencode($sign);
//        return $sign;
//    }
//// 支付宝合作者身份ID，以2088开头的16位纯数字
//    $partner = "%支付宝PartnerID%";
//// 支付宝账号
//    $seller_id = '%支付宝账号，通常为邮箱地址%';
//// 商品网址
//    $base_path = urlencode('http://www.dcloud.io/helloh5/');
//// 异步通知地址
//    $notify_url = urlencode('http://demo.dcloud.net.cn/payment/alipay/notify.php');
//// 订单标题
//    $subject = 'DCloud项目捐赠';
//// 订单详情
//    $body = 'DCloud致力于打造HTML5最好的移动开发工具，包括终端的Runtime、云端的服务和IDE，同时提供各项配套的开发者服务。';
//// 订单号，示例代码使用时间值作为唯一的订单ID号
//    $out_trade_no = date('YmdHis', time());
//    $parameter = array(
//            'service'        => 'mobile.securitypay.pay',   // 必填，接口名称，固定值
//                    'partner'        => $partner,           // 必填，合作商户号
//            '_input_charset' => 'UTF-8',                    // 必填，参数编码字符集
//                    'out_trade_no'   => $out_trade_no,      // 必填，商户网站唯一订单号
//            'subject'        => $subject,                   // 必填，商品名称
//            'payment_type'   => '1',                        // 必填，支付类型
//                    'seller_id'      => $seller_id,         // 必填，卖家支付宝账号
//            'total_fee'      => $total,                     // 必填，总金额，取值范围为[0.01,100000000.00]
//            'body'           => $body,                      // 必填，商品详情
//            'it_b_pay'       => '1d',                       // 可选，未付款交易的超时时间
//                    'notify_url'     => $notify_url,        // 可选，服务器异步通知页面路径
//            'show_url'       => $base_path                  // 可选，商品展示网站
//    );
////生成需要签名的订单
//    $orderInfo = createLinkstring($parameter);
////签名
//    $sign = rsaSign($orderInfo);
//    //生成订单
//    echo $orderInfo.'&sign="'.$sign.'"&sign_type="RSA"';
    //endregion

    @RequestMapping(value = {"/alipay"})
    public void index(AutoOrder order){
        if(order == null || order.getOrderId() == null || order.getOrderId() <= 0 || StringUtil.isNullOrEmpty(order.getOrderSn()))
            return;
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            order = autoOrderService.query(order.getOrderId(), order.getOrderSn());
            if(order != null) {
                map.put("orderId", order.getOrderId());
                AutoCar car = autoCarService.query(map);
                if (car != null) {

                    Map<String, String> payinfo = new HashMap<String, String>();
                    // 必填，接口名称，固定值
                    payinfo.put("service", "\"mobile.securitypay.pay\"");
                    // 必填，合作商户号
                    payinfo.put("partner", "\"" + AlipayConfig.partner + "\"");
                    // 可选，买家ID
                    payinfo.put("seller_id", "\"" + AlipayConfig.seller + "\"");
                    // 必填，商户网站唯一订单号
                    payinfo.put("out_trade_no", "\"" + order.getOrderSn() + "\"");
                    // 必填，商品名称
                    payinfo.put("subject", "\"" + car.getCarName() + "\"");
                    // 必填，商品详情
                    payinfo.put("body", "\"" + "一车网订金:" + car.getCarName() + "\"");
                    // 必填，总金额，取值范围为[0.01,100000000.00]
                    payinfo.put("total_fee", "\"" + ConvertUtil.toString(order.getAmount()) + "\"");
                    // 可选，服务器异步通知页面路径
                    payinfo.put("notify_url", "\"" + AlipayConfig.notify_url + "\"");
                    // 必填，支付类型
                    payinfo.put("payment_type", "\"1\"");
                    // 必填，参数编码字符集
                    payinfo.put("_input_charset", "\"UTF-8\"");
                    // 可选，未付款交易的超时时间
//                    payinfo.put("it_b_pay", "\"30m\"");

                    String orderInfo = AlipayCore.createLinkString(payinfo);


                    // 对订单做RSA 签名
                    String sign = RSA.sign(orderInfo, AlipayConfig.private_key, AlipayConfig.input_charset); //支付宝提供的Config.cs
                    //仅需对sign做URL编码
                    sign = URLEncoder.encode(sign, "utf-8");
                    String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";

                    this.writeText(payInfo);
                }
            }

        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }

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
            if((order.getOrderStatus() == Constants.OS_SUBMIT && order.getPayStatus() == Constants.PS_WAIT_BUYER_DEPOSIT) ||
               (order.getOrderStatus() == Constants.OS_SUBMIT && order.getPayStatus() == Constants.PS_WAIT_SELLER_DEPOSIT)){
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
}
