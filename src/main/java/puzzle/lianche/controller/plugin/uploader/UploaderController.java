package puzzle.lianche.controller.plugin.uploader;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        Result result = new Result();

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(session.getServletContext());
        if(multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

            MultipartFile file = multiRequest.getFile("upfile");

            String rootPath = session.getServletContext().getRealPath("");
            String relativePath = request.getContextPath();
            String typePath = getParameter("type");
            String savePath = rootPath + "upload\\" + typePath + "\\";
            String relativeUrl = relativePath + "/upload/" + typePath + "/";

            String saveName = PathFormatter.format(file.getOriginalFilename(), "{yy}{MM}{dd}\\{hh}{mm}{rand:6}");

            String dirName = savePath + saveName.substring(0, saveName.lastIndexOf('\\'));
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
                url += relativeUrl + saveName.replace("\\", "/");


                result.setData(url);

            } catch (Exception e) {
                result.setCode(1);
                result.setMsg("上传文件失败！");
                e.printStackTrace();
            }
        }
        return result;
    }

    //region uploadBase64
    public Result handleUpload(String file, String typePath, int width, int height) throws Exception{
        Result result = new Result();

        String rootPath = session.getServletContext().getRealPath("");
        String relativePath = request.getContextPath();
        String savePath = rootPath + "\\upload\\" + typePath + "\\";
        String relativeUrl = relativePath + "/upload/" + typePath + "/";

        String saveExt = (file.startsWith("data:image/png;") ? "png" :
                         file.startsWith("data:image/jpg;") ? "jpg" :
                         file.startsWith("data:image/jpeg;") ? "jpeg" : "");

        file = file.substring(("data:image/" + saveExt + ";base64,").length());

        file = file.replaceAll(" ", "\n");
        saveExt = "." + saveExt;

        String saveName = PathFormatter.format("test" + saveExt, "{yy}{MM}{dd}\\{HH}{mm}{ss}{rand:6}");
        String dirName = savePath + saveName.substring(0, saveName.lastIndexOf('\\'));

        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(file);
        for(int i = 0; i < b.length; ++i){
            if(b[i] < 0){
                b[i] += 256;
            }
        }
        //生成jpeg图片
        OutputStream out = new FileOutputStream(savePath + saveName);
        out.write(b);
        out.flush();
        out.close();

        ImageUtil.zoomImage(savePath + saveName, savePath + saveName, 640, 160);

        String url = request.getScheme() + "://" + request.getServerName();
        if(request.getServerPort() != 80){
            url += ":" + request.getServerPort();
        }
        url += relativeUrl + saveName.replace("\\", "/");

        result.setData(url);

        return result;
    }
    //endregion

    @ResponseBody
    @RequestMapping(value = {"/uploader/car"})
    public Result uploadCar(@RequestParam("file") String file){
        Result result = new Result();

        String rootPath = session.getServletContext().getRealPath("");
        String relativePath = request.getContextPath();
        String typePath = getParameter("type");
        String savePath = rootPath + "\\upload\\" + typePath + "\\";
        String relativeUrl = relativePath + "/upload/" + typePath + "/";

        String saveExt =
                (file.startsWith("data:image/png;") ? "png" :
                 file.startsWith("data:image/jpg;") ? "jpg" :
                 file.startsWith("data:image/jpeg;") ? "jpeg" : "");

        file = file.substring(("data:image/" + saveExt + ";base64,").length());

        file = file.replaceAll(" ", "\n");
        saveExt = "." + saveExt;

        String saveName = PathFormatter.format("test" + saveExt, "{yy}{MM}{dd}\\{HH}{mm}{ss}{rand:6}");
        String dirName = savePath + saveName.substring(0, saveName.lastIndexOf('\\'));

        try {
            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(file);
            for(int i = 0; i < b.length; ++i){
                if(b[i] < 0){
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            OutputStream out = new FileOutputStream(savePath + saveName);
            out.write(b);
            out.flush();
            out.close();

            ImageUtil.zoomImage(savePath + saveName, savePath + saveName, 640, 160);

            String url = request.getScheme() + "://" + request.getServerName();
            if(request.getServerPort() != 80){
                url += ":" + request.getServerPort();
            }
            url += relativeUrl + saveName.replace("\\", "/");

            result.setData(url);
        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("文件上传失败！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = {"/uploader/userauth"})
    public Result uploadUserAuth(@RequestParam("file") String file){
        Result result = new Result();

        String rootPath = session.getServletContext().getRealPath("");
        String relativePath = request.getContextPath();
        String typePath = getParameter("type");
        String savePath = rootPath + "\\upload\\" + typePath + "\\";
        String relativeUrl = relativePath + "/upload/" + typePath + "/";

        String saveExt =
                (file.startsWith("data:image/png;") ? "png" :
                file.startsWith("data:image/jpg;") ? "jpg" :
                file.startsWith("data:image/jpeg;") ? "jpeg" : "");

        file = file.substring(("data:image/" + saveExt + ";base64,").length());

        file = file.replaceAll(" ", "\n");
        saveExt = "." + saveExt;

        String saveName = PathFormatter.format("test" + saveExt, "{yy}{MM}{dd}\\{HH}{mm}{ss}{rand:6}");
        String dirName = savePath + saveName.substring(0, saveName.lastIndexOf('\\'));

        try {
            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(file);
            for(int i = 0; i < b.length; ++i){
                if(b[i] < 0){
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            OutputStream out = new FileOutputStream(savePath + saveName);
            out.write(b);
            out.flush();
            out.close();

            String url = request.getScheme() + "://" + request.getServerName();
            if(request.getServerPort() != 80){
                url += ":" + request.getServerPort();
            }
            url += relativeUrl + saveName.replace("\\", "/");

            result.setData(url);
        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("文件上传失败！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = {"/uploader/useravatar"})
    public Result uploadUserAvatar(@RequestParam("file") String file){
        Result result = new Result();

        String rootPath = session.getServletContext().getRealPath("");
        String relativePath = request.getContextPath();
        String typePath = getParameter("type");
        String savePath = rootPath + "\\upload\\" + typePath + "\\";
        String relativeUrl = relativePath + "/upload/" + typePath + "/";

        String saveExt =
                (file.startsWith("data:image/png;") ? "png" :
                        file.startsWith("data:image/jpg;") ? "jpg" :
                                file.startsWith("data:image/jpeg;") ? "jpeg" : "");

        file = file.substring(("data:image/" + saveExt + ";base64,").length());

        file = file.replaceAll(" ", "\n");
        saveExt = "." + saveExt;

        String saveName = PathFormatter.format("test" + saveExt, "{yy}{MM}{dd}\\{HH}{mm}{ss}{rand:6}");
        String dirName = savePath + saveName.substring(0, saveName.lastIndexOf('\\'));

        try {
            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(file);
            for(int i = 0; i < b.length; ++i){
                if(b[i] < 0){
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            OutputStream out = new FileOutputStream(savePath + saveName);
            out.write(b);
            out.flush();
            out.close();

            String url = request.getScheme() + "://" + request.getServerName();
            if(request.getServerPort() != 80){
                url += ":" + request.getServerPort();
            }
            url += relativeUrl + saveName.replace("\\", "/");

            result.setData(url);
        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("文件上传失败！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = {"/uploader/feedback"})
    public Result uploadFeedback(@RequestParam("file") String file){
        Result result = new Result();

        String rootPath = session.getServletContext().getRealPath("");
        String relativePath = request.getContextPath();
        String typePath = getParameter("type");
        String savePath = rootPath + "\\upload\\" + typePath + "\\";
        String relativeUrl = relativePath + "/upload/" + typePath + "/";

        String saveExt =
              (file.startsWith("data:image/png;") ? "png" :
              file.startsWith("data:image/jpg;") ? "jpg" :
              file.startsWith("data:image/jpeg;") ? "jpeg" : "");

        file = file.substring(("data:image/" + saveExt + ";base64,").length());

        file = file.replaceAll(" ", "\n");
        saveExt = "." + saveExt;

        String saveName = PathFormatter.format("test" + saveExt, "{yy}{MM}{dd}\\{HH}{mm}{ss}{rand:6}");
        String dirName = savePath + saveName.substring(0, saveName.lastIndexOf('\\'));

        try {
            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(file);
            for(int i = 0; i < b.length; ++i){
                if(b[i] < 0){
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            OutputStream out = new FileOutputStream(savePath + saveName);
            out.write(b);
            out.flush();
            out.close();

            String url = request.getScheme() + "://" + request.getServerName();
            if(request.getServerPort() != 80){
                url += ":" + request.getServerPort();
            }
            url += relativeUrl + saveName.replace("\\", "/");

            result.setData(url);
        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("文件上传失败！");
        }
        return result;
    }
}
