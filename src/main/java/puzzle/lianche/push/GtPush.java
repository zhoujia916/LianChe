package puzzle.lianche.push;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.TagTarget;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GtPush {

    private static final String APP_ID = "";
    private static final String APP_KEY = "";
    private static final String MASTER_SECRET = "";
    private static final String URL = "http://sdk.open.api.igexin.com/apiex.htm";
    private static final long OFFLINE_EXPIRE_TIME = 1000 * 60 * 30;

    public IPushResult sendAppMsg(AbstractTemplate template){
        IGtPush push = new IGtPush(URL, APP_KEY, MASTER_SECRET);

        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
        List<String> appIds = new ArrayList<String>();
        appIds.add(APP_ID);

        AppMessage message = new AppMessage();
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(OFFLINE_EXPIRE_TIME);

        return push.pushMessageToApp(message);
    }

    public IPushResult sendSingleMsg(AbstractTemplate template, String clientId, String alias){
        IGtPush push = new IGtPush(URL, APP_KEY, MASTER_SECRET);

        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
        SingleMessage message = new SingleMessage();
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(OFFLINE_EXPIRE_TIME);

        Target target = new Target();
        target.setAppId(APP_ID);
        target.setClientId(clientId);
        target.setAlias(alias);

        return push.pushMessageToSingle(message, target);
    }


    public IPushResult sendSingleTagMsg(AbstractTemplate template, String clientId, String tag){
        IGtPush push = new IGtPush(URL, APP_KEY, MASTER_SECRET);

        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
        SingleMessage message = new SingleMessage();
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(OFFLINE_EXPIRE_TIME);

        TagTarget target = new TagTarget();
        target.setAppId(APP_ID);
        target.setClientId(clientId);
        target.setTag(tag);

        return push.pushMessageToSingleByTag(message, target);
    }


    //http://docs.getui.com/server/java/template/#2
    public IPushResult sendLink(String title, String msg, String url) throws IOException {

        // 定义"点击链接打开通知模板"，并设置标题、内容、链接
        LinkTemplate template = new LinkTemplate();
        template.setAppId(APP_ID);
        template.setAppkey(APP_KEY);
        template.setTitle(title);//"欢迎使用个推!"
        template.setText(msg);//"这是一条推送消息~"
        template.setUrl(url);//"http://getui.com"

        return sendAppMsg(template);
    }

    //
    public IPushResult sendNotification(String title, String msg){
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(APP_ID);
        template.setAppkey(APP_KEY);
        template.setTitle(title);
        template.setText(msg);

        return sendAppMsg(template);
    }

    //http://docs.getui.com/server/java/template/#4
    public IPushResult sendTransmission(int transmissionType, String transmissionContent, String[] duration) throws Exception {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(APP_ID);
        template.setAppkey(APP_KEY);
        template.setTransmissionType(transmissionType);
        template.setTransmissionContent(transmissionContent);
        if(duration != null && duration.length == 2)
            template.setDuration(duration[0], duration[1]);

        return sendAppMsg(template);
    }

    //http://docs.getui.com/server/java/template/#3
    public IPushResult sendNotyPopLoad(String title, String msg, String loadUrl, String popTitle, String popContent){
        NotyPopLoadTemplate template = new NotyPopLoadTemplate();
        // 设置APPID与APPKEY
        template.setAppId(APP_ID);
        template.setAppkey(APP_KEY);
        // 设置通知栏标题与内容
        template.setNotyTitle(title);
        template.setNotyContent(msg);
        // 配置通知栏图标
//        template.setNotyIcon("icon.png");
        // 设置通知是否响铃，震动，或者可清除
//        template.setBelled(true);
//        template.setVibrationed(true);
//        template.setCleared(true);
        // 设置弹框标题与内容
        template.setPopTitle(popTitle);
        template.setPopContent(popContent);
        // 设置弹框显示的图片
//        template.setPopImage("");
//        template.setPopButton1("下载");
//        template.setPopButton2("取消");
        // 设置下载标题
//        template.setLoadTitle("下载标题");
//        template.setLoadIcon("file://icon.png");
        //设置下载地址
        template.setLoadUrl(loadUrl);
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        return sendAppMsg(template);
    }

    public String getError(IPushResult result){
        if(result == null)
            return "推送结果为空";
        Map<String, Object> response = result.getResponse();
        if(response == null)
            return "推送结果为空";
        if(response.containsKey("result")){


        }
        return null;
    }

    public boolean isSuccess(IPushResult result){
        if(result == null)
            return false;
        Map<String, Object> response = result.getResponse();
        if(response == null)
            return false;
        if(response.containsKey("result")){
            if(response.get("result").equals("ok")){
                return true;
            }
        }
        return false;
    }
}
