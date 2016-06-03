package puzzle.lianche.plugin.ueditor;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NotSupportHandler extends UHandler
{
    public NotSupportHandler(HttpServletRequest request, HttpServletResponse response)
    {
        super(request, response);
    }


    public void process()
    {
        JSONObject object = new JSONObject();
        object.put("state", "action 参数为空或者 action 不被支持。");
        writeJson(object);
    }
}
