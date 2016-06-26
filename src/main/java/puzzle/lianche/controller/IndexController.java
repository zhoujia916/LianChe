package puzzle.lianche.controller;

import puzzle.lianche.Constants;
import puzzle.lianche.utils.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController extends BaseController {
    @RequestMapping(value = {"/index"})
    public String index(){
        logger.info(request.getRequestURI());
        logger.error("something error");
        setModelAttribute("test", "testString");
        return Constants.UrlHelper.ADMIN_SYSTEM_INDEX;
    }

    @ResponseBody
    @RequestMapping(value = "/login.do")
    public Result login(String username, String password){
        Result result = new Result();

        return result;
    }

    @RequestMapping(value = "/logout.do")
    public String logout(){
        this.setCurrentUser(null);
        return redirect(Constants.UrlHelper.ADMIN_SYSTEM_LOGIN);
    }
}
