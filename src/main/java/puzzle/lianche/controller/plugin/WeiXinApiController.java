package puzzle.lianche.controller.plugin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.utils.EncryptUtil;
import puzzle.lianche.utils.StringUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Controller(value = "pluginWeiXinApiController")
public class WeiXinApiController extends BaseController {

    @RequestMapping(value = {"/weixin"})
    public void index(){
        String signature = this.getParameter("signature");

        String timestamp = this.getParameter("timestamp");
        String nonce = this.getParameter("nonce");
        String echostr = this.getParameter("echostr");
        String token = "";
        //验证消息真实性
        if(check(signature, token, timestamp, nonce)){
            //处理验证消息
            if(!StringUtil.hasNullOrEmpty(new String[]{ signature, timestamp, nonce, echostr})){
                this.writeText(echostr);
            }
            //处理推送消息
            else{
                process();
            }
        }
    }


    //验证消息真实性
    public boolean check(String signature, String token, String timestamp, String nonce){
        String[] array = new String[]{ token, timestamp, nonce };
        Arrays.sort(array);
        StringBuffer text = new StringBuffer();
        for(String item : array){
            text.append(item);
        }
        return EncryptUtil.SHA1(text.toString()).equals(signature);
    }


    //处理推送消息
    public void process(){
        try {
            InputStream in = request.getInputStream();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in);
            String msgType = doc.getElementsByTagName("MsgType").item(0).getTextContent();
            if(!StringUtil.isNullOrEmpty(msgType)){
                if(msgType.equals("subscribe")){
                    receiveSubscribe(doc);
                }
                else if(msgType.equals("unsubscribe")){
                    receiveUnsubscribe(doc);
                }
                else if(msgType.equals("text") || msgType.equals("voice")){
                    receiveMessage(doc);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //接收订阅事件
    public void receiveSubscribe(Document doc){
//        String openId = doc.getElementsByTagName("FromUserName").item(0).getTextContent();
//        String response = WxAPIUtils.getUserInfo(openId);
//        if(!StringUtils.isNullOrEmpty(response)){
//            JSONObject user = JSONObject.fromObject(response);
//            Member memberVO = new Member();
//            memberVO.setOpenId(openId);
//            memberVO.setName(user.get("nickname").toString());
//            memberVO.setAddress(user.get("country").toString() + user.get("province").toString() + user.get("city").toString());
//            memberVO.setHeadImgUrl(user.get("headimgurl").toString());
//            Object unionId = user.get("unionid");
//            if(unionId != null){
//                memberVO.setHeadImgUrl(unionId.toString());
//            }
//            memberVO.setStatus(1);
//            memberVO.setPoint(0);
//            memberService.addMember(memberVO);
//        }
    }

    //接收取消订阅事件
    public void receiveUnsubscribe(Document doc){
//        String openId = doc.getElementsByTagName("FromUserName").item(0).getTextContent();
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("openId", openId);
//        memberService.deleteMember(map);
    }

    //接收用户发送消息(文本或语音)
    public void receiveMessage(Document doc){
//        String keyword = "";
//        String msgType = doc.getElementsByTagName("MsgType").item(0).getTextContent();
//        if(msgType.equals("text")){
//            keyword = doc.getElementsByTagName("Content").item(0).getTextContent();
//        }
//        else if(msgType.equals("voice")){
//            keyword = doc.getElementsByTagName("Recognition").item(0).getTextContent();
//        }
//        String openId = doc.getElementsByTagName("FromUserName").item(0).getTextContent();
//        Reply replyVO = new Reply();
//        replyVO.setKeyword(keyword);
//        replyVO.setOpenId(openId);
//        replyVO.setTime(new Date());
//        replyService.addReply(replyVO);
    }
}
