package puzzle.lianche.intercept;

import puzzle.lianche.Constants;

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
            String url = request.getContextPath() + "/" + Constants.UrlHelper.ADMIN_SYSTEM_LOGIN
                         + "?" + Constants.UrlHelper.RETURN_URL + "=" + URLEncoder.encode(path, "utf-8");
            response.sendRedirect(url);

//            String script = "<script type='text/javascript'> window.top.location = '" + Constants.UrlHelper.ADMIN_LOGIN + "';</script>";
//
//            response.getWriter().write(script);

            return false;
        }

        return true;
    }


}
