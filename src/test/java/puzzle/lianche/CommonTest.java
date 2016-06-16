package puzzle.lianche;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import puzzle.lianche.controller.plugin.wxpay.util.WxPayData;
import puzzle.lianche.entity.*;
import puzzle.lianche.mapper.SqlMapper;
import puzzle.lianche.push.SmsPush;
import puzzle.lianche.service.*;
import puzzle.lianche.service.impl.AutoBrandServiceImpl;
import puzzle.lianche.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-*.xml"})
public class CommonTest {

    @Test
    public void testWxpay(){
        WxPayData data = new WxPayData();
        String xml = "<xml>\n" +
                "   <appid>wx2421b1c4370ec43b</appid>\n" +
                "   <attach>支付测试</attach>\n" +
                "   <body>APP支付测试</body>\n" +
                "   <mch_id>10000100</mch_id>\n" +
                "   <nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>\n" +
                "   <notify_url>http://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php</notify_url>\n" +
                "   <out_trade_no>1415659990</out_trade_no>\n" +
                "   <spbill_create_ip>14.23.150.211</spbill_create_ip>\n" +
                "   <total_fee>1</total_fee>\n" +
                "   <trade_type>APP</trade_type>\n" +
                "   <sign>0CB01533B8C1EF103065174F50BCA001</sign>\n" +
                "</xml>";
        try {
            data.fromXml(xml);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("ok");
    }
}