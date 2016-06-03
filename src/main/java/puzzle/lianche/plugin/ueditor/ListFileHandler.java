package puzzle.lianche.plugin.ueditor;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class ListFileHandler extends UHandler
{
    public static final int Success = 0;
    public static final int InvalidParam = 1;
    public static final int PathNotFound = 4;

    private int start;
    private int size;
    private int total;
    private int state;
    private String pathToList;
    private String[] fileList;
    private String[] searchExtensions;

    public ListFileHandler(HttpServletRequest request, HttpServletResponse response, String pathToList, String[] searchExtensions)
    {
        super(request, response);
        for(int i = 0; i < searchExtensions.length; i++){
            searchExtensions[i] = searchExtensions[i].toLowerCase();
        }
        this.searchExtensions = searchExtensions;
        this.pathToList = pathToList;
    }

    public void process()
    {
        try
        {
            if(request.getParameter("start") == null || request.getParameter("start").trim().length() == 0){
                start = 0;
            }else{
                start = Integer.valueOf(request.getParameter("start"));
            }
            if(request.getParameter("size") == null || request.getParameter("size").trim().length() == 0){
                size = Config.getInt(request, "imageManagerListSize");
            }else{
                size = Integer.valueOf(request.getParameter("size"));
            }
        }
        catch (Exception e)
        {
            state = InvalidParam;
            writeResult();
            return;
        }

        File dir = new File(pathToList);
        if(!dir.exists()){
            state = PathNotFound;
            writeResult();
            return;
        }

        try
        {
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    String extension = name.substring(name.lastIndexOf('.')).toLowerCase();
                    for(String ext : searchExtensions){
                        if(extension.equals(ext)){
                            return true;
                        }
                    }
                    return false;
                }
            };
            String[] list = dir.list(filter);
            total = list.length;
            Arrays.sort(list);
            fileList = new String[size];
            int end = Math.min(list.length, start + size);
            for(int i = start; i < end; i++){
                list[i - start] = pathToList + list[i];
            }
        }
        finally
        {
            writeResult();
        }
    }

    private void writeResult()
    {
        JSONObject object = new JSONObject();
        object.put("state", getStateString());
        object.put("list", fileList == null || fileList.length == 0 ? null : getFileList());
        object.put("start", start);
        object.put("size", size);
        object.put("total", total);
        writeJson(object);
    }

    private String getStateString()
    {
        switch (state)
        {
            case Success:
                return "SUCCESS";
            case InvalidParam:
                return "参数不正确";
            case PathNotFound:
                return "路径不存在";
        }
        return "未知错误";
    }

    private Object[] getFileList()
    {
        Object[] list = new Object[fileList.length];
        for(int i = 0; i < fileList.length; i++)
        {
            JSONObject object = new JSONObject();
            object.put("url", fileList[i]);
            list[i] = object;
        }
        return list;
    }

}
