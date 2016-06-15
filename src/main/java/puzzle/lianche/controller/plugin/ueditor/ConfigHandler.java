package puzzle.lianche.controller.plugin.ueditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfigHandler extends UHandler {

    public ConfigHandler(HttpServletRequest request, HttpServletResponse response)
    {
        super(request, response);
    }

    public void process()
    {
        writeJson(Config.getItems(request));
    }
}
