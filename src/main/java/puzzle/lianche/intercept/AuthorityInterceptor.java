package puzzle.lianche.intercept;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import puzzle.lianche.Constants;
import puzzle.lianche.entity.SystemAuthority;
import puzzle.lianche.entity.SystemMenu;
import puzzle.lianche.entity.SystemUser;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.service.ISystemMenuService;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AuthorityInterceptor extends HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI().replace(request.getContextPath(), "");
        if(isExcludePath(path)){
            return true;
        }
        HttpSession session = request.getSession();
        SystemUser user = (SystemUser)session.getAttribute(Constants.SESSION_ADMIN);
        if(user != null){
            path = path.replace("/admin/", "");
            boolean authorize = false;
            for(String url : user.getUrls()){
                if(path.matches(url)){
                    authorize = true;
                    break;
                }
            }
            if(!authorize){
//                if(isAjaxRequest(request)){
//                    Result result = new Result();
//                    result.setCode(Constants.ResultHelper.RESULT_NOT_AUTHRIZE);
//                    result.setMsg("您的没有权限访问该链接");
//                    response.getWriter().print(JSONObject.fromObject(result));
//                }else {
//
//                }
                String url = request.getContextPath() + "/" + Constants.UrlHelper.ADMIN_SYSTEM_DENY;
                response.sendRedirect(url);
            }
            return authorize;
        }
        return true;
    }


}
