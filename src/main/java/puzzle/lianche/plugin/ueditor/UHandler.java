package puzzle.lianche.plugin.ueditor;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;

public abstract class UHandler {
    public UHandler(ServletRequest request, ServletResponse response){
        this.request = (HttpServletRequest) request;
        this.response = (HttpServletResponse) response;
    }

    protected void writeJson(JSONObject result)
    {
        writeText(result.toString());
    }

    protected void writeText(String result)
    {
        try {
            String jsonpCallback = request.getParameter("callback");
            if (jsonpCallback == null || jsonpCallback.trim().length() == 0) {
                response.setCharacterEncoding("utf-8"); //防止乱码
                response.setHeader("Content-Type", "text/plain");
                response.getWriter().print(result);//向页面端返回结果信息
            } else {
                response.setHeader("Content-Type", "application/javascript");
                response.getWriter().print(jsonpCallback + "(" + result + ")");//向页面端返回结果信息
            }
        }
        catch (Exception e){

        }
    }

    public abstract void process();

    public HttpServletRequest request;

    public HttpServletResponse response;
}
