package puzzle.lianche.plugin.ueditor;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class UploadHandler extends UHandler {
    private static final int Success = 0;
    private static final int SizeLimitExceed = -1;
    private static final int TypeNotAllow = -2;
    private static final int FileAccessError = -3;
    private static final int NetworkError = -4;
    private static final int Unknown = 1;

    private class UploadResult
    {
        private int uploadState;
        public int getUploadState(){
            return this.uploadState;
        }
        public void setUploadState(int uploadState){
            this.uploadState = uploadState;
        }
        private String url;
        public String getUrl(){
            return url;
        }
        public void setUrl(String url){
            this.url = url;
        }
        private String originFileName;
        public String getOriginFileName(){
            return originFileName;
        }
        public void setOriginFileName(String originFileName){
            this.originFileName = originFileName;
        }
        private String errorMessage;
        public String getErrorMessage(){
            return errorMessage;
        }
        public void setErrorMessage(String errorMessage){
            this.errorMessage = errorMessage;
        }
    }




    private UploadConfig config;
    public UploadConfig getConfig(){
        return this.config;
    }
    public void setConfig(UploadConfig config){
        this.config = config;
    }

    private UploadResult result;
    public UploadResult getResult(){
        return this.result;
    }
    public void setResult(UploadResult result){
        this.result = result;
    }

    private UploadFile uploadFile;
    public UploadFile getUploadFile(){ return  this.uploadFile; }
    public void setUploadFile(UploadFile uploadFile){ this.uploadFile = uploadFile; }

    public UploadHandler(HttpServletRequest request, HttpServletResponse response, UploadConfig config, UploadFile file)
    {
        super(request, response);
        this.config = config;
        this.uploadFile = file;
        this.result = new UploadResult();
        this.result.setUploadState(Unknown);
    }

    public void process()
    {
        byte[] uploadFileBytes = null;
        String uploadFileName = uploadFile.getFileName();

        if (!checkFileType())
        {
            this.result.setUploadState(TypeNotAllow);
            writeResult();
            return;
        }
        if (!checkFileSize())
        {
            this.result.setUploadState(SizeLimitExceed);
            writeResult();
            return;
        }
        uploadFileBytes = new byte[(int)uploadFile.getFile().length()];
        try
        {
            FileInputStream fis = new FileInputStream(uploadFile.getFile());
            fis.read(uploadFileBytes, 0, uploadFileBytes.length);
        }
        catch (Exception e)
        {
            this.result.setUploadState(NetworkError);
            writeResult();
        }

        result.setOriginFileName(uploadFileName);

        String saveName = PathFormatter.format(uploadFileName, config.getPathFormat());
        try
        {
            String dirName = uploadFile.getSavePath() + "/" + saveName.substring(0, saveName.lastIndexOf('/'));
            File dir = new File(dirName);
            if(!dir.exists()){
                dir.mkdirs();
            }
            File file = new File(uploadFile.getSavePath() + "/" + saveName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(uploadFileBytes);
            fos.close();
            if(uploadFile.getUrl() == null || uploadFile.getUrl().trim().length() == 0) {
                result.setUrl(request.getScheme() + "://" + request.getServerName());
                if (request.getServerPort() != 80)
                    result.setUrl(result.getUrl() + ":" + request.getServerPort());
                this.result.setUrl(result.getUrl() + uploadFile.getSavePath());
            }else{
                this.result.setUrl(uploadFile.getUrl() + saveName);
            }
            this.result.setUploadState(Success);
        }
        catch (Exception e)
        {
            this.result.setUploadState(FileAccessError);
            this.result.setErrorMessage(e.getMessage());
        }
        finally
        {
            writeResult();
        }
    }

    private void writeResult()
    {
        JSONObject object = new JSONObject();
        object.put("state", getStateMessage(result.getUploadState()));
        object.put("url", result.getUrl());
        object.put("title", result.getOriginFileName());
        object.put("original", result.getOriginFileName());
        object.put("error", result.getErrorMessage());
        this.writeJson(object);
    }

    private String getStateMessage(int state)
    {
        switch (state)
        {
            case Success:
                return "SUCCESS";
            case FileAccessError:
                return "文件访问出错，请检查写入权限";
            case SizeLimitExceed:
                return "文件大小超出服务器限制";
            case TypeNotAllow:
                return "不允许的文件格式";
            case NetworkError:
                return "网络错误";
        }
        return "未知错误";
    }


    private boolean checkFileType()
    {
        String fileName = uploadFile.getFileName();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') );
        for(String ext : config.getAllowExtensions()){
            if(ext.equals(fileExtension)){
                return  true;
            }
        }
        return  false;
    }

    private boolean checkFileSize()
    {
        return uploadFile.getFile().length() < config.getSizeLimit();
    }
}
