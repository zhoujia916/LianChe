package puzzle.lianche.controller.plugin.ueditor;

public class UploadConfig {
    // 文件命名规则
    public String pathFormat;
    public String getPathFormat(){
        return pathFormat;
    }
    public void setPathFormat(String pathFormat){
        this.pathFormat = pathFormat;
    }

    // 上传表单域名称
    public String uploadFieldName;
    public String getUploadFieldName(){
        return uploadFieldName;
    }
    public void setUploadFieldName(String uploadFieldName){
        this.uploadFieldName = uploadFieldName;
    }

    // 上传大小限制
    public int sizeLimit;
    public int getSizeLimit(){
        return sizeLimit;
    }
    public void setSizeLimit(int sizeLimit){
        this.sizeLimit = sizeLimit;
    }

    // 上传允许的文件格式
    public String[] allowExtensions;
    public String[] getAllowExtensions(){
        return allowExtensions;
    }
    public void setAllowExtensions(String[] allowExtensions){
        this.allowExtensions = allowExtensions;
    }

    // 文件编码
    public boolean base64;
    public boolean getBase64(){
        return this.base64;
    }
    public void setBase64(boolean base64){
        this.base64 = base64;
    }

    public String base64FileName;
    public String getBase64FileName(){
        return base64FileName;
    }
    public void setBase64FileName(String base64FileName){
        this.base64FileName = base64FileName;
    }
}