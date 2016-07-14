package puzzle.lianche.controller.plugin.wxpay.util;

import net.sf.json.JSONObject;
import puzzle.lianche.controller.plugin.wxpay.config.WxConfig;

import java.util.Random;

public class WxPayAPI {


    /**
     * 统一下单
     * @param
     * @throws Exception
     * @return 成功时返回，其他抛异常
     */
    public static WxPayResult unifiedOrder(String outTradeNo, String body, int totalFee, String clientIp) throws Exception{

        WxPayData data = new WxPayData();
        data.setValue("out_trade_no", outTradeNo);
        data.setValue("body", body);
        data.setValue("total_fee", String.valueOf(totalFee));
        data.setValue("trade_type", "APP");
        data.setValue("appid", WxConfig.APPID);
        data.setValue("mch_id", WxConfig.MCHID);
        data.setValue("spbill_create_ip", clientIp);
        data.setValue("notify_url", WxConfig.NOTIFY_URL);
        data.setValue("nonce_str", getNonceStr(32));
        //签名
        data.setSign();

        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String response = postXml(data.toXml(), url, false);

        if(response != null){
            WxPayResult result = WxPayResult.init(response);
            if(result != null && result.getValue("return_code").equals("SUCCESS")){
                data.clear();
                data.setValue("appid", WxConfig.APPID);
                data.setValue("partnerid", WxConfig.MCHID);
                data.setValue("prepayid", result.getValue("prepayid"));
                data.setValue("package", "Sign=WXPay");
                data.setValue("noncestr", getNonceStr(32));
                data.setValue("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
                result.makeSign();
                return result;
            }
        }

        return null;
    }



    /**
     * 查询订单
     * @param transactionId
     * @param outTradeNo
     * @return
     * @throws Exception
     */
    public static WxPayResult orderQuery(String transactionId, String outTradeNo) throws Exception{
        if(transactionId == null && transactionId == "" &&
                outTradeNo == null && outTradeNo == ""){
            throw new Exception("transactionId and outTradeNo不能同时为空！");
        }
        WxPayData data = new WxPayData();
        data.setValue("appid", WxConfig.APPID);
        data.setValue("mch_id", WxConfig.MCHID);
        if(transactionId != null && transactionId != "")
            data.setValue("transaction_id", transactionId);
        else if(outTradeNo != null && outTradeNo != "")
            data.setValue("out_trade_no", outTradeNo);
        data.setValue("nonce_str", getNonceStr(32));
        data.setSign();


        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        String response = postXml(data.toXml(), url, false);

        if(response != null) {
            WxPayResult result = WxPayResult.init(response);
            if (result != null && result.getValue("return_code").equals("SUCCESS")) {
                return result;
            }
        }
        return null;
    }

    /**
     * 关闭订单
     * @param transactionId
     * @param outTradeNo
     * @return
     * @throws Exception
     */
    public static WxPayResult closeOrder(String transactionId, String outTradeNo) throws Exception{
        if(transactionId == null && transactionId == "" &&
                outTradeNo == null && outTradeNo == ""){
            throw new Exception("transactionId and outTradeNo不能同时为空！");
        }
        WxPayData data = new WxPayData();
        data.setValue("appid", WxConfig.APPID);
        data.setValue("mch_id", WxConfig.MCHID);
        if(transactionId != null && transactionId != "")
            data.setValue("transaction_id", transactionId);
        else if(outTradeNo != null && outTradeNo != "")
            data.setValue("out_trade_no", outTradeNo);
        data.setValue("nonce_str", getNonceStr(32));
        data.setSign();


        String url = "https://api.mch.weixin.qq.com/pay/closeorder";
        String response = postXml(data.toXml(), url, false);

        if(response != null) {
            WxPayResult result = WxPayResult.init(response);
            if (result != null && result.getValue("return_code").equals("SUCCESS")) {
                return result;
            }
        }
        return null;
    }

    /**
     * 申请退款
     * @param transactionId
     * @param outTradeNo
     * @return
     * @throws Exception
     */
    public static WxPayResult refundOrder(String transactionId, String outTradeNo, int totalFee) throws Exception{
        if(transactionId == null && transactionId == "" &&
                outTradeNo == null && outTradeNo == ""){
            throw new Exception("transactionId and outTradeNo不能同时为空！");
        }
        WxPayData data = new WxPayData();
        data.setValue("appid", WxConfig.APPID);
        data.setValue("mch_id", WxConfig.MCHID);

        if(transactionId != null && transactionId != "")
            data.setValue("transaction_id", transactionId);
        else if(outTradeNo != null && outTradeNo != "")
            data.setValue("out_trade_no", outTradeNo);
        data.setValue("nonce_str", getNonceStr(32));
        data.setValue("total_fee", String.valueOf(totalFee));
        data.setValue("op_user_id", WxConfig.MCHID);
        data.setSign();


        String url = "https://api.mch.weixin.qq.com/pay/refund";
        String response = postXml(data.toXml(), url, false);
        if(response != null) {
            WxPayResult result = WxPayResult.init(response);
            if (result != null && result.getValue("return_code").equals("SUCCESS")) {
                  return result;
            }
        }
        return null;
    }

    /**
     * 查询退款
     * @param transactionId
     * @param outTradeNo
     * @return
     * @throws Exception
     */
    public static WxPayResult refundQuery(String transactionId, String outTradeNo, int totalFee) throws Exception{
        if(transactionId == null && transactionId == "" &&
                outTradeNo == null && outTradeNo == ""){
            throw new Exception("transactionId and outTradeNo不能同时为空！");
        }
        WxPayData data = new WxPayData();
        data.setValue("appid", WxConfig.APPID);
        data.setValue("mch_id", WxConfig.MCHID);

        if(transactionId != null && transactionId != "")
            data.setValue("transaction_id", transactionId);
        else if(outTradeNo != null && outTradeNo != "")
            data.setValue("out_trade_no", outTradeNo);
        data.setValue("nonce_str", getNonceStr(32));
        data.setSign();


        String url = "https://api.mch.weixin.qq.com/pay/refundquery";
        String response = postXml(data.toXml(), url, false);

        if(response != null) {
            WxPayResult result = WxPayResult.init(response);
            if (result != null && result.getValue("return_code").equals("SUCCESS")) {
                return result;
            }
        }
        return null;
    }







    /**
     *
     * 产生随机字符串，不长于32位
     * @param length
     * @return 产生的随机字符串
     */
    private static String getNonceStr(int length){
        String allChar = "abcdefghijklmnopqrstuvwxyz0123456789";
        String str ="";
        for (int i = 0; i < length; i++ )  {
            int index = new Random().nextInt(allChar.length() -1);
            str += allChar.substring(index, index + 1);
        }
        return str;
    }


    /**
     * 以post方式提交xml到对应的接口url
     * @param xml  需要post的xml数据
     * @param url
     * @param useCert 是否需要证书，默认不需要
     * @throws Exception
     */
    private static String postXml(String xml, String url, boolean useCert) throws Exception{
        HttpProxy proxy = new HttpProxy(url);
        proxy.setBodyXml(xml);
        return proxy.execute() ? proxy.getResponse() : null;
    }

    /**
     * 获取毫秒级别的时间戳
     */
    private static String getMillisecond(){
        return String.valueOf(System.currentTimeMillis());
    }
}
