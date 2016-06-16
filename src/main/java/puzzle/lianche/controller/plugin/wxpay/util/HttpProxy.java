package puzzle.lianche.controller.plugin.wxpay.util;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpProxy {
	private static final int HTTP_STATUS_OK = 200;

	private MyX509TrustManager xtm = new MyX509TrustManager();
    private MyHostnameVerifier hnv = new MyHostnameVerifier();
    private String method;
    private String url;
    private String bodyXml;
    private int responseCode;
    private String response;

    public HttpProxy(String url){
    	this.url = url;
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

	public boolean execute(){
        StringBuffer result = new StringBuffer();
        InputStream in = null;
        OutputStream out = null;
        BufferedReader reader = null;
        HttpsURLConnection connection = null;
        try {
        	StringBuffer sbUrl = new StringBuffer(url);
            URL httpUrl = new URL(sbUrl.toString());
            initializeSSL();
            connection = (HttpsURLConnection)httpUrl.openConnection();
            connection.setRequestMethod(method);
            
            // 设置过期时间
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
            // 建立实际的连接
            connection.connect();

            out = connection.getOutputStream();
            out.write(bodyXml.getBytes("utf-8"));
            out.flush();
            out.close();

            // 获取响应状态码
            responseCode = connection.getResponseCode();

            String charset = "utf-8";

            in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in , charset));
    		String line = reader.readLine(); 
    		while(line != null){ 
        		result.append(line);
        		line = reader.readLine(); 
    		} 
    		response = result.toString();
            return responseCode == HTTP_STATUS_OK;
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
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

	public String getBodyXml() {
		return bodyXml;
	}

	public void setBodyXml(String bodyXml) {
		this.bodyXml = bodyXml;
	}

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponse() {
        return response;
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
