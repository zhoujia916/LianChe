package puzzle.lianche.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.sf.json.JSONObject;

public class WXUtil {
	private static String ACCESSTOKEN;
	private static long timeout;
	private static String APPID;
	private static String APPSECRET;
	private static String ACCOUNT;

    private static Map<String, String> WX_API_URI = new HashMap<String,String>(){
        {
            //获取AccessToken
            put("GET_ACCESS_TOKEN", "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=#APPID#&secret=#APPSECRET#");
            //获取自动回复规则
            put("GET_AUTOREPLY", "https://api.weixin.qq.com/cgi-bin/get_current_autoreply_info?access_token=#ACCESS_TOKEN#");
            //获取自定义菜单
            put("GET_MENU", "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=#ACCESS_TOKEN#");
            //自定义菜单
            put("CREATE_MENU", "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=#ACCESS_TOKEN#");
            //获取用户信息
            put("GET_USER_INFO", "https://api.weixin.qq.com/cgi-bin/user/info?access_token=#ACCESS_TOKEN#&openid=#OPENID#&lang=zh_CN");
            //新增永久素材
            put("ADD_MATERIAL", "http://api.weixin.qq.com/cgi-bin/material/add_material?access_token=#ACCESS_TOKEN#&type=#TYPE#");
            //新增永久图文素材
            put("ADD_NEWS", "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=#ACCESS_TOKEN#");
        }
    };
	
	static {   
        Properties prop = new Properties();   
        InputStream in = Object.class.getResourceAsStream("/wx.properties");   
        try {   
            prop.load(in);   
            APPID = prop.getProperty("APPID").trim();   
            APPSECRET = prop.getProperty("APPSECRET").trim();  
            ACCOUNT = prop.getProperty("ACCOUNT").trim();  
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
        checkAccessToken();
    }  
	
	//内容格式参考 http://mp.weixin.qq.com/wiki/14/bb5031008f1494a59c6f71fa0f319c66.html
	public static String getUserInfo(String openId){
		checkAccessToken();
		String url = WX_API_URI.get("GET_USER_INFO");
		url = url.replaceAll("#ACCESS_TOKEN#", ACCESSTOKEN);
		url = url.replaceAll("#OPENID#", openId);
		HttpUtil http = new HttpUtil(url);
		return http.request() ? http.getResponse() : null;
	}
	
	//内容格式参考 http://mp.weixin.qq.com/wiki/7/7b5789bb1262fb866d01b4b40b0efecb.html
	public static String getAutoReply(){
		checkAccessToken();
		String url = WX_API_URI.get("GET_AUTOREPLY");
		url = url.replaceAll("#ACCESS_TOKEN#", ACCESSTOKEN);
		HttpUtil http = new HttpUtil(url);
		return http.request() ? http.getResponse() : null;
	}
	
	//内容格式参考 http://mp.weixin.qq.com/wiki/16/ff9b7b85220e1396ffa16794a9d95adc.html
	public static String getMenu(){
		checkAccessToken();
		String url = WX_API_URI.get("GET_MENU");
		url = url.replaceAll("#ACCESS_TOKEN#", ACCESSTOKEN);
		HttpUtil http = new HttpUtil(url);
		return http.request() ? http.getResponse() : null;
	}
	
	//内容格式参考 http://mp.weixin.qq.com/wiki/13/43de8269be54a0a6f64413e4dfa94f39.html
	public static String createMenu(){
		checkAccessToken();
		String url = WX_API_URI.get("CREATE_MENU");
		url = url.replaceAll("#ACCESS_TOKEN#", ACCESSTOKEN);
		HttpUtil http = new HttpUtil(url);
		return http.request() ? http.getResponse() : null;
	}
	
	//被动回复消息
	public static void replayMsg(String content, String openId){
		
	}
	
	//新增用户素材 http://mp.weixin.qq.com/wiki/14/7e6c03263063f4813141c3e17dd4350a.html
	//image,voice,video,thumb
	public static String addMaterial(File file, String type){
		checkAccessToken();
		String url = WX_API_URI.get("ADD_MATERIAL");
		url = url.replaceAll("#ACCESS_TOKEN#", ACCESSTOKEN);
		url = url.replaceAll("#Type#", type);
		HttpUtil http = new HttpUtil(HttpUtil.HTTP_POST, url);
        http.addFile("media", file);
		return http.request() ? http.getResponse() : null;
	}
	
	//内容格式参考 http://mp.weixin.qq.com/wiki/11/0e4b294685f817b95cbed85ba5e82b8f.html
	private static void checkAccessToken(){
		Date now = new Date();
		if(StringUtil.isNullOrEmpty(ACCESSTOKEN) || timeout == 0 || now.getTime() >= timeout){
			String url = WX_API_URI.get("GET_ACCESS_TOKEN");
			url = url.replaceAll("#APPID#", APPID);
			url = url.replaceAll("#APPSECRET#", APPSECRET);
			HttpUtil http = new HttpUtil(url);
			String response = http.request() ? http.getResponse() : null;
			if(!StringUtil.isNullOrEmpty(response)){
				JSONObject result = JSONObject.fromObject(response);
				if(result != null){
					ACCESSTOKEN = result.get("access_token").toString();
					long expire = Long.parseLong(result.get("expires_in").toString());
					timeout = now.getTime() + expire * 60000;
				}
			}
		}
	}
	
	public static String getAccessToken(){
		checkAccessToken();
		return ACCESSTOKEN;
	}
}
