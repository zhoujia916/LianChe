package puzzle.lianche.plugin.ueditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Processor {
    private static final String GetConfig = "config";
    private static final String UploadImage = "uploadimage";
    private static final String UploadScrawl = "uploadscrawl";
    private static final String UploadVideo = "uploadvideo";
    private static final String UploadFile = "uploadfile";
    private static final String ListImage = "listimage";
    private static final String ListFile = "listfile";
    private static final String CatchImage = "catchimage";

    public void process(HttpServletRequest request, HttpServletResponse response, UploadFile file)
    {
        UHandler action = null;
        String actionName = request.getParameter("action");
        if(actionName.equals(GetConfig)){
            ConfigHandler handler = new ConfigHandler(request, response);
            action = handler;
        }
        else if(actionName.equals(UploadImage)){
            UploadConfig config = new UploadConfig();
            config.setAllowExtensions(Config.getStringList(request, "imageAllowFiles"));
            config.setPathFormat(Config.getString(request, "imagePathFormat"));
            config.setSizeLimit(Config.getInt(request, "imageMaxSize"));
            config.setUploadFieldName(Config.getString(request, "imageFieldName"));
            action = new UploadHandler(request, response, config, file);
        }else if(actionName.equals(UploadScrawl)){
            UploadConfig config = new UploadConfig();
            config.setAllowExtensions(new String[] { ".png" });
            config.setPathFormat(Config.getString(request, "scrawlPathFormat"));
            config.setSizeLimit(Config.getInt(request, "scrawlMaxSize"));
            config.setUploadFieldName(Config.getString(request, "scrawlFieldName"));
            config.setBase64(true);
            config.setBase64FileName("scrawl.png");
            action = new UploadHandler(request, response, config, file);
        }else if(actionName.equals(UploadVideo)){
            UploadConfig config = new UploadConfig();
            config.setAllowExtensions(Config.getStringList(request, "videoAllowFiles"));
            config.setPathFormat(Config.getString(request, "videoPathFormat"));
            config.setSizeLimit(Config.getInt(request, "videoMaxSize"));
            config.setUploadFieldName(Config.getString(request, "videoFieldName"));
            action = new UploadHandler(request, response, config, file);
        }else if(actionName.equals(UploadFile)) {
            UploadConfig config = new UploadConfig();
            config.setAllowExtensions(Config.getStringList(request, "fileAllowFiles"));
            config.setPathFormat(Config.getString(request, "filePathFormat"));
            config.setSizeLimit(Config.getInt(request, "fileMaxSize"));
            config.setUploadFieldName(Config.getString(request, "fileFieldName"));
            action = new UploadHandler(request, response, config, file);
        } else if(actionName.equals(ListImage)) {
            action = new ListFileHandler(request, response, Config.getString(request, "imageManagerListPath"), Config.getStringList(request, "imageManagerAllowFiles"));
        } else if(actionName.equals(ListFile)) {
            action = new ListFileHandler(request, response, Config.getString(request, "fileManagerListPath"), Config.getStringList(request, "fileManagerAllowFiles"));
        } else if(actionName.equals(CatchImage)) {
            action = new CrawlerHandler(request, response);
        }
        else {
            action = new NotSupportHandler(request, response);
        }
        action.process();
    }
}
