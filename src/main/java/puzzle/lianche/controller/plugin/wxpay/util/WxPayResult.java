package puzzle.lianche.controller.plugin.wxpay.util;

import java.util.Map;

public class WxPayResult extends WxPayData{
    /**
     *
     * 检测签名
     */
    public boolean checkSign(){
        if(!this.isSignSet()){
            return false;
        }
        String sign = this.makeSign();
        return this.getSign().equals(sign);
    }

    /**
     *
     * 使用数组初始化
     * @param array
     */
    public void fromArray(Map<String, String> array){
        this.map = array;
    }

    /**
     *
     * 使用数组初始化对象
     * @param array
     * @param noCheckSign
     */
    public static WxPayResult initFromArray(Map<String, String> array, boolean noCheckSign)
    {
        WxPayResult result = new WxPayResult();
        result.fromArray(array);
        if(noCheckSign == false){
            result.checkSign();
        }else{
            result.setSign();
        }
        return result;
    }

    /**
     *
     * 设置参数
     * @param key
     * @param value
     */
    public void setData(String key, String value){
        this.map.put(key, value);
    }

    /**
     * 将xml转为array
     * @param xml
     */
    public static WxPayResult init(String xml){
        WxPayResult result = new WxPayResult();
        try {
            result.fromXml(xml);
            if(!result.checkSign()){
                return null;
            }
        }
        catch (Exception e){

        }
        return result;
    }

}
