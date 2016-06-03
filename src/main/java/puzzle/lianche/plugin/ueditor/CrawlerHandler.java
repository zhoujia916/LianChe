package puzzle.lianche.plugin.ueditor;

import net.sf.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.List;

public class CrawlerHandler extends UHandler
{
    private String[] sources;
    public String[] getSources(){ return  this.sources; }
    public void setCrawlers(String[] sources){ this.sources = sources; }
    private Crawler[] crawlers;
    public Crawler[] getCrawlers(){ return  this.crawlers; }
    public void setCrawlers(Crawler[] crawlers){ this.crawlers = crawlers; }

    public CrawlerHandler(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    private class Crawler
    {
        public String sourceUrl;
        public String getSourceUrl(){ return  this.sourceUrl; }
        public void setSourceUrl(String sourceUrl){ this.sourceUrl = sourceUrl; }
        public String serverUrl;
        public String getServerUrl(){ return  this.serverUrl; }
        public void setServerUrl(String serverUrl){ this.serverUrl = serverUrl; }
        public String state;
        public String getState(){ return  this.state; }
        public void setState(String state){ this.state = state; }

        public Crawler(String sourceUrl)
        {
            this.sourceUrl = sourceUrl;
        }

        public Crawler fetch()
        {
            int statusCode = 0;
            String statusMessage = null;
            StringBuffer result = new StringBuffer();
            InputStream in = null;
            OutputStream out = null;
            BufferedReader r;
            Map<String, List<String>> map;
            try {
                URL url = new URL(sourceUrl);
                // 打开和URL之间的连接
                HttpURLConnection  connection = (HttpURLConnection)url.openConnection();
                // 设置过期时间
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);
                // 设置通用的请求属性
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

                // 建立实际的连接
                connection.connect();
                // 获取响应状态码
                statusCode = connection.getResponseCode();
                statusMessage = connection.getResponseMessage();
                // 获取所有响应头字段
                map = connection.getHeaderFields();

                if(statusCode != 200){
                    state = "Url returns " + statusCode + ", " + statusMessage;
                    return this;
                }
                if (map != null || map.get("Content-Type").indexOf("image") == -1)
                {
                    state = "Url is not an image";
                    return this;
                }

                serverUrl = PathFormatter.format(sourceUrl, Config.getString(request, "catcherPathFormat"));
                String savePath = "";
//                if (!Directory.Exists(Path.GetDirectoryName(savePath)))
//                {
//                    Directory.CreateDirectory(Path.GetDirectoryName(savePath));
//                }


                // 定义 BufferedReader输入流来读取URL的响应)
                in = connection.getInputStream();
                out = new FileOutputStream(savePath);

//                int count = 0;
//                while (count == 0) {
//                    count = in.available();
//                }
                int length;
                byte[] buffer = new byte[4 * 1024];
                while ((length = in.read(buffer, 0, buffer.length)) > 0) {
                    out.write(buffer,0,length);
                }
                state = "SUCCESS";

            } catch (Exception e) {
                e.printStackTrace();
                state = "抓取错误：" + e.getMessage();
            }
            // 使用finally块来关闭输入流
            finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if(out != null){
                        out.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return this;
        }
    }

    public void process()
    {
        sources = request.getParameter("source").split(",");
        if (sources == null || sources.length == 0)
        {
            JSONObject object = new JSONObject();
            object.put("state", "参数错误：没有指定抓取源");
            writeJson(object);
        }
        crawlers = new Crawler[sources.length];
        JSONObject[] list = new JSONObject[sources.length];
        for(int i = 0; i < sources.length; i++){
            crawlers[i] = new Crawler(sources[i]).fetch();
            list[i] = new JSONObject();
            list[i].put("state", crawlers[i].state);
            list[i].put("source", crawlers[i].sourceUrl);
            list[i].put("url", crawlers[i].serverUrl);
        }
        JSONObject object = new JSONObject();
        object.put("state", "SUCCESS");
        object.put("list", list);
        writeJson(object);
    }
}

