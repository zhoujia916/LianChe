package puzzle.lianche.controller.plugin.ueditor;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;

public class Config {
    protected static final Logger log = LoggerFactory.getLogger(Config.class);

    private static boolean noCache = true;
    private static JSONObject items;
    private static JSONObject BuildItems(HttpServletRequest request)
    {
        StringBuffer sb = new StringBuffer();
        try {
            String path = request.getSession().getServletContext().getRealPath("") + "/ueditor/config.json";
            log.info("Config Path:" + path);
            File file = new File(path);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "utf-8");//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
            } else {
            }
        }
        catch (Exception e){
        }
        return JSONObject.fromObject(sb.toString());
    }

    public static JSONObject getItems(HttpServletRequest request)
    {
        if (noCache || items == null)
        {
            items = BuildItems(request);
        }
        return items;
    }


    public static String[] getStringList(HttpServletRequest request, String key)
    {
        Object[] result = getItems(request).getJSONArray(key).toArray();
        String[] list = new String[result.length];
        for(int i = 0; i < result.length; i++){
            list[i] = result[i].toString();
        }
        return list;
    }

    public static String getString(HttpServletRequest request, String key)
    {
        return getItems(request).getString(key);
    }

    public static int getInt(HttpServletRequest request, String key)
    {
        return getItems(request).getInt(key);
    }
}
