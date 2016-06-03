package puzzle.lianche.utils;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpUtil {
	public static final String HTTP_GET = "GET";
	public static final String HTTP_POST = "POST";
	public static final String HTTP_ERROR = "ERROR";
	private static final String POST_BOUNDARY = "------unibon-WxAPIBoundary------";
	private static final String POST_NEWLINE = "\r\n";
	private static final String POST_MARK = "--";
	private static final int HTTP_STATUS_OK = 200;

	private MyX509TrustManager xtm = new MyX509TrustManager();
    private MyHostnameVerifier hnv = new MyHostnameVerifier();
    private Map<String,String> headers;
    private String method;
    private String url;
    private Map<String,String> datas;
    private Map<String,File> files;
    private String bodyXml;
    private int responseCode;
    private String responseMessage;
    private Map<String, List<String>> responseHeaders;
    private String response;
    private String responseCharset;

    public HttpUtil(){

    }

    public HttpUtil(String url){
    	this.method = HTTP_GET;
    	this.url = url;
    }

    public HttpUtil(String url, Map<String, File> files){
    	this.method = HTTP_POST;
    	this.url = url;
    	this.files = files;
    }

    public HttpUtil(String url, Map<String, String> datas, Map<String, File> files){
    	this.method = HTTP_POST;
    	this.url = url;
    	this.datas = datas;
    	this.files = files;
    }

    public HttpUtil(String method, String url){
    	this.method = method;
    	this.url = url;
    }

    public HttpUtil(String method, String url, Map<String, String> datas){
    	this.method = method;
    	this.url = url;
    	this.datas = datas;
    }

    public HttpUtil(String method, String url, Map<String, String> headers, Map<String, String> datas) {
    	this.method = method;
    	this.url = url;
    	this.headers = headers;
    	this.datas = datas;
    }
    
    private void initializeSSL(){
    	SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            X509TrustManager[] xtmArray = new X509TrustManager[] {xtm};
            sslContext.init(null, xtmArray, new SecureRandom());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        if (sslContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
    }
    
    public String processMap(Map<String,String> map){
    	StringBuffer params = new StringBuffer();
    	try{
    		if(map != null && !map.isEmpty()){
				for (String key : map.keySet()) {
					params.append("&");
					params.append(URLEncoder.encode(key, "UTF-8"));
					params.append("=");
					params.append(URLEncoder.encode(map.get(key), "UTF-8"));
				}
				params = params.delete(0, 1);
    		}
    	}catch(Exception e){
    		
    	}
    	return params.toString();
    }

	public boolean request(){
        StringBuffer result = new StringBuffer();
        InputStream in = null;
        OutputStream out = null;
        BufferedReader reader = null;
        URLConnection connection = null;
        try {
        	StringBuffer sbUrl = new StringBuffer(url);
        	if(method.equals(HTTP_GET)){
        		if(datas != null && datas.size() > 0){
        			if(url.indexOf("?") == -1){
        				sbUrl.append("?");
        				sbUrl.append(processMap(datas));
        			}else{
        				sbUrl.append("&");
        				sbUrl.append(processMap(datas));
        			}
        		}
        	}
            URL httpUrl = new URL(sbUrl.toString());
            
            if(url.startsWith("https")){
            	initializeSSL();
            	connection = (HttpsURLConnection)httpUrl.openConnection();
            	((HttpsURLConnection)connection).setRequestMethod(method);
            }else{
            	connection = (HttpURLConnection)httpUrl.openConnection();
            	((HttpURLConnection)connection).setRequestMethod(method);
            }
            
            // 设置过期时间
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            // 设置通用的请求属性
            connection.setRequestProperty("Accept", "*/*");
//            connection.setRequestProperty("Connection", "Keep-Alive");
//            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.137 Safari/537.36");
            
            if(headers != null && headers.size() > 0){
            	for (String key : headers.keySet()) {
            		connection.setRequestProperty(key, headers.get(key));
				}
            }
            connection.setDoInput(true); 
            if(method.equals(HTTP_POST)){
            	connection.setDoOutput(true);
            	if(bodyXml != null && bodyXml.length() != 0){
            		connection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
            	}
            	else if(files != null && files.size() != 0){
            		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + POST_BOUNDARY);
            	}
            	else if(datas != null && datas.size() != 0){
            		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            	}
            }
            else if(method.equals(HTTP_GET)){
            	connection.setDoOutput(false);
            }
            // 建立实际的连接
            connection.connect();
            
            if(method.equals(HTTP_POST)){
            	out = connection.getOutputStream();
            	if(bodyXml != null && bodyXml.length() != 0){
            		out.write(bodyXml.getBytes());
            	}
            	else if(files != null && files.size() != 0){
            		StringBuffer strBuffer = new StringBuffer();
            		if(datas != null && datas.size() != 0){
	            		for(String key : datas.keySet()){
	            			strBuffer.append(POST_NEWLINE).append(POST_MARK).append(POST_BOUNDARY).append(POST_NEWLINE);
	            			strBuffer.append("Content-Disposition: form-data; name=\"" + key + "\"" + POST_NEWLINE);
	            			strBuffer.append(POST_NEWLINE);
	            			strBuffer.append(datas.get(key));  
	            		}
	            		out.write(strBuffer.toString().getBytes("utf-8"));
            		}  
            		
            		byte[] buffer = new byte[1024];
            		FileInputStream fis;
            		File file;
            		String contentType = "";
            		Map<String,String> mimeMapping = new HashMap<String,String>(){
            			{
	            			put(".jpg", "image/jpeg");
	            			put(".jpeg", "image/jpeg");
	            			put(".png", "image/png");
	            			put(".bmp", "image/bmp");
	            			put(".gif", "image/gif");
	            			
	            			put(".mp3", "audio/mp3");
	            			put(".wma", "audio/x-ms-wma");
	            			put(".wav", "audio/wav");
	            			put(".amr", "audio/amr");
	            			
	            			put(".mp4", "video/mpeg4");
            			}
            		};
            		for (String key : files.keySet()) { 
            			file = files.get(key);
            			if(strBuffer.length() > 0){
                			strBuffer.delete(0,	strBuffer.length() - 1);
                		}
            			
                        String ext = file.getName().substring(file.getName().lastIndexOf('.')).toLowerCase();
                        contentType = mimeMapping.containsKey(ext) ? mimeMapping.get(ext) : "application/octet-stream";
                        
                        strBuffer.append(POST_NEWLINE).append(POST_MARK).append(POST_BOUNDARY).append(POST_NEWLINE);  
                        strBuffer.append("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\"" + POST_NEWLINE);  
                        strBuffer.append("Content-Type: " + contentType + POST_NEWLINE);  
                        strBuffer.append(POST_NEWLINE);  
        
                        out.write(strBuffer.toString().getBytes("utf-8"));  
                        fis = new FileInputStream(file);  
                        int length = 0;  
                        while ((length = fis.read(buffer)) != -1) {  
                            out.write(buffer, 0, length);  
                        }  
                    } 
                    out.write((POST_NEWLINE + POST_MARK + POST_BOUNDARY + POST_MARK + POST_NEWLINE).getBytes());
            	}
            	else if(datas != null && datas.size() != 0){
            		out.write(processMap(datas).getBytes());
            	}
            	out.flush();  
                out.close();  
            }
            
            
            
            // 获取响应状态码
            if(url.startsWith("https")){
            	responseCode = ((HttpsURLConnection)connection).getResponseCode();
            	responseMessage = ((HttpsURLConnection)connection).getResponseMessage();
            }else{
            	responseCode = ((HttpURLConnection)connection).getResponseCode();
            	responseMessage = ((HttpURLConnection)connection).getResponseMessage();
            }
            //
            // 获取所有响应头字段
            responseHeaders = connection.getHeaderFields();

            String charset = null;
            if(responseCharset != null){
                charset = responseCharset;
            }
            else if(responseHeaders.containsKey("Charset")){
                charset = responseHeaders.get("Charset").get(0);
            }
            else if(responseHeaders.containsKey("Content-Type")){
                String[] contentType = responseHeaders.get("Content-Type").get(0).split(";");
                if(contentType.length == 2){
                    charset = contentType[1].split("=")[1];
                }
            }
            if(charset == null){
                charset = "utf-8";
            }

            in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in , charset));
    		String line = reader.readLine(); 
    		while(line != null){ 
        		result.append(line);
        		line = reader.readLine(); 
    		} 
    		response = result.toString();
    		if(url.startsWith("https")){
            	((HttpsURLConnection)connection).disconnect();
            }else{
            	((HttpURLConnection)connection).disconnect();
            }
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
                if(out != null){
                	out.close();
                }
                if(reader != null){
                	reader.close();
                }
                if(connection != null){
	                if(url.startsWith("https")){
	                	((HttpsURLConnection)connection).disconnect();
	                }else{
	                	((HttpURLConnection)connection).disconnect();
	                }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	public Map<String,String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String,String> headers) {
		this.headers = headers;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String,File> getFiles() {
		return files;
	}

	public void setFiles(Map<String,File> files) {
		this.files = files;
	}

	public void addInput(String name, String value){
		if(datas == null){
			datas = new HashMap<String, String>();
		}
		datas.put(name, value);
	}
	
	public void addFile(String name, File file){
		if(files == null){
			files = new HashMap<String, File>();
		}
		files.put(name, file);
	}
	
	public int getResponseCode() {
		return responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}
	
	public String getResponse() {
		return response;
	}

	public String getBodyXml() {
		return bodyXml;
	}

	public void setBodyXml(String bodyXml) {
		this.bodyXml = bodyXml;
	}

    public String getResponseCharset() {
        return responseCharset;
    }

    public void setResponseCode(String responseCharset) {
        this.responseCharset = responseCharset;
    }


    public Map<String, List<String>> getResponseHeaders() {
		return responseHeaders;
	}

	private class MyHostnameVerifier implements HostnameVerifier {

	    public boolean verify(String hostname, SSLSession session) {
	        return true;
	    }
	}
	
	private class MyX509TrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	    
	    
	}
}
