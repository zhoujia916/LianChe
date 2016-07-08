package puzzle.lianche.intercept;

import net.sf.json.JSONObject;
import puzzle.lianche.Constants;
import puzzle.lianche.utils.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;


public class LoginInterceptor extends HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI().replace(request.getContextPath(), "");
        if(isExcludePath(path)){
            return true;
        }
        HttpSession session = request.getSession();
        Object user = session.getAttribute(Constants.SESSION_ADMIN);
        if(user == null){
            if(isAjaxRequest(request)){
                Result result = new Result();
                result.setCode(Constants.ResultHelper.RESULT_NOT_AUTHTICATE);
                result.setMsg("您的登录已过期，请重新登录");
                response.getWriter().print(JSONObject.fromObject(result));
            }else {
                String url = request.getContextPath() + "/" + Constants.UrlHelper.ADMIN_SYSTEM_LOGIN;
                if(!path.contentEquals("/" + Constants.UrlHelper.ADMIN_SYSTEM_INDEX)){
                    url += "?" + Constants.UrlHelper.PARAM_RETURN_URL + "=" + URLEncoder.encode(path, "utf-8");
                }
                response.sendRedirect(url);
            }
            return false;
        }

        return true;
    }


}
