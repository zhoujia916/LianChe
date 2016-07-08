package puzzle.lianche.intercept;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import puzzle.lianche.controller.plugin.alipay.util.httpClient.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class HandlerInterceptor  extends HandlerInterceptorAdapter {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected List<String> excludePath;

    public void setExcludePath(List<String> paths){
        this.excludePath = paths;
    }

    public List<String> getExcludePath(){
        return this.excludePath;
    }

    protected boolean isExcludePath(String path){
        if(excludePath != null && !excludePath.isEmpty()){
            for(String item : excludePath){
                if(path.matches(item)){
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isAjaxRequest(HttpServletRequest request){
        return request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest");
    }
}
