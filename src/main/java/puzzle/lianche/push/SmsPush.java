package puzzle.lianche.push;

import net.sf.json.JSONObject;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.EncryptUtil;
import puzzle.lianche.utils.HttpUtil;
import puzzle.lianche.utils.StringUtil;

public class SmsPush {
    private static final String URL = "https://106.ihuyi.com/webservice/sms.php?method=Submit";
    private static final String ACCOUNT = "cf_oneauto";

    private static final String PASSWORD = "oneauto12345";
    private static final String APIKEY = "c16a640dd7b9973fbdc275dddac65628";
    private static final String HTTP_METHOD = HttpUtil.HTTP_POST;
    public static final int CODE_SENDCODE = 1000;
    public static final int CODE_SENDMSG = 1001;
    private static final String RESULT_OK = "2";

    /**
     * 发送短信消息
     * @param type
     * @param mobile
     * @param info
     * @return
     */
    public static String send(int type, String mobile, String info){
        HttpUtil http = new HttpUtil(HTTP_METHOD, URL);

        long time = System.currentTimeMillis() / 1000;
        String content = null;
        if(type == CODE_SENDCODE){
            content = "您的验证码是：" + info + "。请不要把验证码泄露给其他人。";
//            content = "您在一车上获取的验证码是:[" + info + "],请勿泄露。【一车官网】";
        }
        else if(type == CODE_SENDMSG){
            content = "一车提醒您:" + info + "【一车官网】";
        }

        String result = null;
        if(StringUtil.isNotNullOrEmpty(content)) {
            http.addInput("method", "Submit");
            http.addInput("account", ACCOUNT);
            http.addInput("password", EncryptUtil.MD5(ACCOUNT + APIKEY + mobile + content + time));
            http.addInput("mobile", mobile);
            http.addInput("content", content);
            http.addInput("time", ConvertUtil.toString(time));

            int tryNum = 3;

            while (tryNum > 0) {
                if (http.request()) {
                    result = http.getResponse();
                    tryNum = 0;
                } else {
                    tryNum--;
                }
            }
        }
        return result;
    }


    public static String getError(String response){
        String error = null;
        if(StringUtil.isNotNullOrEmpty(response)){
            JSONObject json = JSONObject.fromObject(response);
            String code = json.get("code").toString().trim();
            if(code.equals("0")){
                error = "提交失败";
            }
            if(code.equals("400")){
                error = "非法ip访问";
            }
            if(code.equals("401") || code.equals("402")){
                error = "帐号或密码为空";
            }
            if(code.equals("403")){
                error = "手机号码不能为空";
            }
            if(code.equals("404")){
                error = "短信内容不能为空";
            }
            if(code.equals("405")){
                error = "用户名或密码不正确";
            }
            if(code.equals("406")){
                error = "手机号码格式不正确";
            }
            if(code.equals("407")){
                error = "短信内容含有敏感字符";
            }
            if(code.equals("408")){
                error = "您的帐户疑被恶意利用，已被自动冻结，如有疑问请与客服联系";
            }
        }
        return error;
    }

    public static boolean isSuccess(String response){
        if(StringUtil.isNotNullOrEmpty(response)){
            JSONObject json = JSONObject.fromObject(response);
            return json.get("code").toString().equals(RESULT_OK);
        }
        return false;
    }
}
