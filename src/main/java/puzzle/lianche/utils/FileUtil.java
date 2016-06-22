package puzzle.lianche.utils;

import java.io.*;

public class FileUtil {
	public static boolean checkFileType(String fileName, String[] accept){
		if(StringUtil.isNullOrEmpty(fileName)){
			return false;
		}
		if(accept == null || accept.length == 0){
			return true;
		}
		String fileExt = getFileExt(fileName);
		for(String ext : accept){
			if(fileExt.equals(ext)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkFileSize(File file, long size){
		return file.length() <= size;
	}
	
	public static String getFileDir(String fileName){
		int index = fileName.lastIndexOf('/');
		if(index == -1){
			fileName.lastIndexOf('\\');
		}
		return fileName.substring(0, index);
	}
	
	public static String getFileName(String fileName, boolean hasExt){
		int index = fileName.lastIndexOf('/');
		if(index == -1){
			fileName.lastIndexOf('\\');
		}
		return fileName.substring(index);
	}
	
	public static String getFileExt(String fileName){
        int index = fileName.lastIndexOf('/');
        if(index == -1){
            return "";
        }
        return fileName.substring(index + 1);
	}
	
	public static boolean checkExists(String fileName){
		File file = new File(fileName);
		return file.exists();
	}
	
	public static boolean copyFile(String source, String target){
		File file = new File(source);
		return copyFile(file, target);
	}
	
	public static boolean copyFile(File source, String target){
		try{
            if(source.exists()) {

                if (source.isFile()) {
                    checkFilePath(target);
                    FileInputStream fis = new FileInputStream(source);
                    FileOutputStream fos = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    fis.close();
                } else if (source.isDirectory()){
                    checkFilePath(target);
                    File[] files = source.listFiles();
                    for(File file : files){
                        copyFile(file, target + getFileName(file.getName(), true));
                    }
                }
            }
            return true;
		}
		catch(IOException e){
			return false;
		}
	}
	
	public static void deleteFile(String fileName){
		File file = new File(fileName);
		if(file.exists())
			file.delete();
	}
	
	public static void checkFilePath(String filePath)throws IOException{
		File file = new File(filePath);
        if (!file.exists()) {
            if (file.isFile()) {
                file.createNewFile();
            } else if (file.isDirectory()) {
                file.mkdirs();
            }
        }
	}

    public static boolean appendFile(String fileName, String content){
        try {
            checkFilePath(fileName);
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public static String readFile(String fileName){
        StringBuffer result = new StringBuffer();
        try{
            FileInputStream fis = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) > 0) {
                result.append(new String(buffer));
            }
            fis.close();
        }catch (Exception e){

        }
        return result.toString();
    }

    public static boolean createFile(String fileName){
        try {
            checkFilePath(fileName);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
