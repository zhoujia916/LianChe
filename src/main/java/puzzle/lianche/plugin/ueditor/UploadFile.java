package puzzle.lianche.plugin.ueditor;

import java.io.File;

public class UploadFile {
    private File file;
    public void setFile(File file){ this.file = file;}
    public File getFile(){ return this.file; }
    private String contentType;
    public void setContentType(String contentType){ this.contentType = contentType;}
    public String getContentType(){ return this.contentType; }
    private String fileName;
    public void setFileName(String fileName){ this.fileName = fileName;}
    public String getFileName(){ return this.fileName; }
    private String savePath;
    public void setSavePath(String savePath){ this.savePath = savePath;}
    public String getSavePath(){ return this.savePath; }
    private String url;
    public void setUrl(String url){ this.url = url;}
    public String getUrl(){ return this.url; }
}
