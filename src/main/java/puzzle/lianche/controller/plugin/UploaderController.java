package puzzle.lianche.controller.plugin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.plugin.ueditor.Processor;
import puzzle.lianche.plugin.ueditor.UploadFile;

@Controller(value = "pluginUploaderController")
@RequestMapping(value = {"/uploader"})
public class UploaderController extends BaseController {
    @RequestMapping(value = {""})
    public void index(){
        UploadFile uploadFile = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(session.getServletContext());
        if(multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

            MultipartFile file = multiRequest.getFile("upfile");

            String rootPath = session.getServletContext().getRealPath("");
            String relativePath = request.getContextPath();
            String contentPath = getParameter("type") + "\\";
            String savePath = rootPath + "\\upload\\" + contentPath;
            String url = relativePath + "/upload/" + contentPath;

            try {
                uploadFile = new UploadFile();
                uploadFile.setFileName(file.getOriginalFilename());
                uploadFile.setContentType(file.getContentType());
                uploadFile.setFileSize(file.getSize());
                uploadFile.setInputStream(file.getInputStream());
                uploadFile.setSavePath(savePath);
                uploadFile.setUrl(url);
            }
            catch (Exception e){
            }
        }

        Processor processor = new Processor();
        processor.process(request, response, uploadFile);

    }
}
