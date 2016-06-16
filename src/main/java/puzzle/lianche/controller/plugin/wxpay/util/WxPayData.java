package puzzle.lianche.controller.plugin.wxpay.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import puzzle.lianche.controller.plugin.wxpay.config.WxConfig;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class WxPayData{
    protected Map<String, String> map = new HashMap<String, String>();

    /**
     * 设置签名，详见签名生成算法
     **/
    public void setSign(){
        map.put("sign", makeSign());
    }

    /**
     * 获取签名，详见签名生成算法的值
     * @return 值
     **/
    public String getSign(){
        return map.get("sign");
    }

    /**
     * 判断签名，详见签名生成算法是否存在
     * @return true 或 false
     **/
    public boolean isSignSet(){
        return map.containsKey("sign");
    }

    /**
     * 输出xml字符
     **/
    public String toXml() throws Exception{
        if(map == null && map.isEmpty())
            throw new Exception("数组数据不能为空！");
        StringBuffer xml = new StringBuffer();
        xml.append("<xml>");
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = map.get(key);
            if(value.matches("^\\d+(\\.\\d+)?$")){
                xml.append("<" + key + ">" + map.get(key) + "</" + key + ">");
            }else{
                xml.append("<" + key + "><![CDATA[" + map.get(key) + "]]></" + key + ">");
            }
        }
        xml.append("</xml>");
        return xml.toString();
    }

    public void fromXml(String xml) throws Exception{
        if(xml == null || xml.length() == 0)
            throw new Exception("XML数据异常");
        DefaultHandler handler = new DefaultHandler(){
            private String preTag;


            @Override
            public void startDocument() throws SAXException {
                map.clear();
            }

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes)
                    throws SAXException {
                preTag = qName;//将正在解析的节点名称赋给preTag
            }

            @Override
            public void endElement(String uri, String localName, String qName)
                    throws SAXException {
                preTag = null;
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if(preTag!=null){
                    String content = new String(ch,start,length);
                    if(!"xml".equals(preTag)){
                        map.put(preTag, content);
                    }
                }
            }
        };

        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
        parser.parse(is, handler);
    }

    /**
     * 格式化参数格式化成url参数
     * @return
     */
    public String toUrlParams(){
        StringBuffer url = new StringBuffer();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = map.get(key);
            if(key.equals("sign") && value != ""){
                url.append("&" + key + "=" + value);
            }
        }
        return url.deleteCharAt(0).toString();
    }

    /**
     * 生成签名
     * @return
     */
    public String makeSign(){
        //签名步骤一：按字典序排序参数
        String[] keys = (String[])map.keySet().toArray();
        Arrays.sort(keys);

        String str = "";
        for(String key : keys){
            str += key + "=" + map.get(key) + "&";
        }
        //签名步骤二：在string后加入KEY
        str = str + "key=" + WxConfig.API_KEY;
        str = md5(str).toUpperCase();

        return str;

    }

    private String md5(String input){
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(input.getBytes());
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < md.length; i++) {
                String shaHex = Integer.toHexString(md[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Map<String,String> getMap(){
        return map;
    }

    public void setValue(String key, String value){
        this.map.put(key, value);
    }

    public String getValue(String key){
        return this.map.get(key);
    }

    public boolean hasKey(String key){
        return map.containsKey(key);
    }

    public void clear(){
        map.clear();
    }
}
