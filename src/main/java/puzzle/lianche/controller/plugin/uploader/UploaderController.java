package puzzle.lianche.controller.plugin.uploader;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.utils.ImageUtil;
import puzzle.lianche.utils.Result;
import sun.misc.BASE64Decoder;
import java.io.*;

@Controller(value = "pluginUploaderController")
@RequestMapping(value = {"/plugin"})
public class UploaderController extends BaseController {

    @ResponseBody
    @RequestMapping(value = {"/uploader"})
     public Result index(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(session.getServletContext());

        return multipartResolver.isMultipart(request) ? handleBinary() : handleBase64();
    }

//    @ResponseBody
//    @RequestMapping(value = {"/uploader/car"})
//    public Result uploadCar(){
//        return handleBase64();
//    }
//
//    @ResponseBody
//    @RequestMapping(value = {"/uploader/userauth"})
//    public Result uploadUserAuth(){
//        return handleBase64();
//    }
//
//    @ResponseBody
//    @RequestMapping(value = {"/uploader/useravatar"})
//    public Result uploadUserAvatar(){
//        return handleBase64();
//    }
//
//    @ResponseBody
//    @RequestMapping(value = {"/uploader/feedback"})
//    public Result uploadFeedback(){
//        return handleBase64();
//    }



    //region Handle Binary Upload
    public Result handleBinary(){
        Result result = new Result();

        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;


        MultipartFile file = multiRequest.getFile("upfile");
        if(file == null){
            result.setCode(-1);
            result.setMsg("文件不能为空");
            return result;
        }

        String typePath = getParameter("type");

        String rootPath = session.getServletContext().getRealPath("") + "/" + request.getContextPath();
        String relativePath = request.getContextPath();
        String relativeUrl = relativePath + "/upload/" + typePath + "/";

        String saveName = PathFormatter.format(file.getOriginalFilename(), "{yy}{MM}{dd}/{HH}{mm}{ss}{rand:6}");
        String saveDir = rootPath + "upload/" + typePath + "/" + saveName.substring(0, saveName.lastIndexOf('/') + 1);
        relativeUrl += saveName.substring(0, saveName.lastIndexOf('/') + 1);
        saveName = saveName.substring(saveName.lastIndexOf("/") + 1);

        try {
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(saveDir + saveName);
            fos.write(file.getBytes());
            fos.close();

            if(typePath.equalsIgnoreCase("car")) {
                ImageUtil.zoomImage(saveDir + saveName, saveDir + saveName, 600, 360);
            }
            String url = getHost() + relativeUrl + saveName;

            result.setData(url);

        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("上传文件失败！");
            e.printStackTrace();
        }
        return result;
    }
    //endregion

    //region Handle Upload Base64 Image
    public Result handleBase64(){
        Result result = new Result();

        String file = getParameter("file");
        if(file == null || file.equals("")){
            result.setCode(-1);
            result.setMsg("文件不能为空");
            return result;
        }

        String typePath = getParameter("type");
        if(typePath == null || typePath.trim().equals("")){
            typePath = "temp";
        }


        String rootPath = session.getServletContext().getRealPath("") + "/" + request.getContextPath();
        String relativeUrl = request.getContextPath() + "/upload/" + typePath + "/";

        String saveExt = (file.startsWith("data:image/png;") ? "png" :
                file.startsWith("data:image/jpg;") ? "jpg" :
                        file.startsWith("data:image/jpeg;") ? "jpeg" : "");

        file = file.substring(("data:image/" + saveExt + ";base64,").length());

        file = file.replaceAll(" ", "\n");

        saveExt = "." + saveExt;

        String saveName = PathFormatter.format("test" + saveExt, "{yy}{MM}{dd}/{HH}{mm}{ss}{rand:6}");
        String saveDir = rootPath + "upload/" + typePath + "/" + saveName.substring(0, saveName.lastIndexOf('/') + 1);

        relativeUrl += saveName.substring(0, saveName.lastIndexOf('/') + 1);
        saveName = saveName.substring(saveName.lastIndexOf("/") + 1);

        try {
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(file);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            OutputStream out = new FileOutputStream(saveDir + saveName);
            out.write(b);
            out.flush();
            out.close();

            if(typePath.equalsIgnoreCase("car")) {
                ImageUtil.zoomImage(saveDir + saveName, saveDir + saveName, 600, 360);
            }

            String url = getHost() + relativeUrl + saveName.replace("\\", "/");

            result.setData(url);
        }
        catch (Exception e){
            result.setCode(1);
            result.setMsg("上传文件失败！");
            e.printStackTrace();
        }
        return result;
    }
    //endregion
}
