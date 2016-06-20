package puzzle.lianche.controller.plugin.uploader;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.utils.Result;

import java.io.File;
import java.io.FileOutputStream;

@Controller(value = "pluginUploaderController")
@RequestMapping(value = {"/plugin"})
public class UploaderController extends BaseController {

    @RequestMapping(value = {"/uploader"})
    public void index(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(session.getServletContext());
        if(multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

            MultipartFile file = multiRequest.getFile("upfile");

            String rootPath = session.getServletContext().getRealPath("");
            String relativePath = request.getContextPath();
            String typePath = getParameter("type");
            String savePath = rootPath + "\\upload\\" + typePath + "\\";
            String relativeUrl = relativePath + "/upload/" + typePath + "/";

            String saveName = PathFormatter.format(file.getOriginalFilename(), "{yy}{MM}{dd}\\{hh}{mm}{rand:6}");
            Result result = new Result();
            String dirName = savePath + saveName.substring(0, saveName.lastIndexOf('\\'));
            String saveExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            try {
                File dir = new File(dirName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(savePath + saveName);
                fos.write(file.getBytes());
                fos.close();

                String url = request.getScheme() + "://" + request.getServerName();
                if(request.getServerPort() != 80){
                    url += ":" + request.getServerPort();
                }
                url += relativeUrl + saveName + saveExt;


                result.setData(url);

                this.writeJson(result);
            } catch (Exception e) {
                result.setCode(1);
                result.setMsg("上传文件失败！");
            }
        }
    }
}
