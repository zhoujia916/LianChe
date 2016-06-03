package puzzle.lianche.push;

import puzzle.lianche.utils.HttpUtil;

import java.util.List;
import java.util.Map;

public class SmsPush {
    private static final String URL = "";
    private static final String ACCOUNT = "";
    private static final String PASSWORD = "";
    private static final String HTTP_METHOD = HttpUtil.HTTP_POST;

    public static String send(int userId, int code, Map<String, Object> data){
        HttpUtil http = new HttpUtil(HTTP_METHOD, URL);
        String message = createMsg(code, data);
        int tryNum = 3;
        String result = null;
        while (tryNum > 0){
            if(http.request()){
                result = http.getResponse();
                tryNum = 0;
            }else{
                tryNum--;
            }
        }
        return result;
    }

    public static String send(String phone, int code, Map<String, Object> data){
        HttpUtil http = new HttpUtil(HTTP_METHOD, URL);
        String message = createMsg(code, data);
        int tryNum = 3;
        String result = null;
        while (tryNum > 0){
            if(http.request()){
                result = http.getResponse();
                tryNum = 0;
            }else{
                tryNum--;
            }
        }
        return result;
    }

    public static String send(List<String> phone, int code, Map<String, Object> data){
        HttpUtil http = new HttpUtil(HTTP_METHOD, URL);
        String message = createMsg(code, data);
        int tryNum = 3;
        String result = null;
        while (tryNum > 0){
            if(http.request()){
                result = http.getResponse();
                tryNum = 0;
            }else{
                tryNum--;
            }
        }
        return result;
    }

    public static String createMsg(int code, Object...params){
        StringBuffer text = new StringBuffer();
        if(code == 1001){

        }
        else if(code == 1001){

        }
        else if(code == 1001){

        }
        else if(code == 1001){

        }
        return text.toString();
    }

    public static String getError(String response){
        return null;
    }

    public static boolean isSuccess(String response){
        return true;
    }
}
