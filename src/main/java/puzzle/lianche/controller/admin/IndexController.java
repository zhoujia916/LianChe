package puzzle.lianche.controller.admin;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.*;
import puzzle.lianche.service.*;
import puzzle.lianche.utils.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import java.util.*;

@Controller(value="adminIndexController")
public class IndexController extends ModuleController {
    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemMenuService systemMenuService;

    @Autowired
    private ISystemMenuActionService systemMenuActionService;

    @Autowired
    private IAutoUserService autoUserService;

    //region request page
    @RequestMapping(value = {"", "/", "/admin/", "/admin/login"})
    public String login(){
        String returnUrl = this.getParameter(Constants.UrlHelper.RETURN_URL);
        if(this.getCurrentUser() != null){
            if(StringUtil.isNotNullOrEmpty(returnUrl)){
                return this.redirect(returnUrl);
            }else {
                return this.redirect(Constants.UrlHelper.ADMIN_INDEX);
            }
        }
        String value = this.getCookie(Constants.COOKIE_ADMIN);
        if(StringUtil.isNotNullOrEmpty(value)){
            JSONObject info = JSONObject.fromObject(value);
            this.setModelAttribute("user", info.get("user"));
            this.setModelAttribute("remember", info.getInt("remember"));
        }
        if(StringUtil.isNotNullOrEmpty(returnUrl)){
            this.setModelAttribute("ReturnUrl", returnUrl);
        }
        return Constants.UrlHelper.ADMIN_LOGIN;
    }

    @RequestMapping(value = {"/logout", "/admin/logout", "/admin/logout"})
    public String logout(){
        SystemUser user = (SystemUser)this.getCurrentUser();
        if(user != null) {
            this.setCurrentUser(null);
        }
        return redirect(Constants.UrlHelper.ADMIN_LOGIN);
    }

    @RequestMapping(value = "/admin/index")
    public String index(){
        if(this.getCurrentUser() != null) {
            //region 账户信息
            SystemUser user = (SystemUser)this.getCurrentUser();
            this.setModelAttribute("admin", user);
            //endregion

            //region 待实名认证用户
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", Constants.AUTO_USER_STATUS_WAITAUTHENTICATION);
            Page page = new Page();
            page.setPageIndex(Constants.PageHelper.PAGE_INDEX_FIRST);
            page.setPageSize(Constants.PageHelper.PAGE_SIZE_COMMON);
            List<AutoUser> users = autoUserService.queryList(map, page);
            Result task = new Result();
            task.setTotal(page.getTotal());
            task.setData(users);
            this.setModelAttribute("task", task);
            //endregion


            //region 已经确认订单和车源
//            map.clear();
//            map.put("status", Constants.AUTO_USER_STATUS_WAITAUTHENTICATION);
//            page.setPageIndex(Constants.PageHelper.PAGE_INDEX_FIRST);
//            page.setPageSize(Constants.PageHelper.PAGE_SIZE_COMMON);
//
//            this.setModelAttribute("task", task);
            //endregion
        }
        return Constants.UrlHelper.ADMIN_INDEX;
    }

    @RequestMapping(value = "/admin/main")
    public String main(){
        return Constants.UrlHelper.ADMIN_MAIN;
    }
    //endregion

    //region request action
    @ResponseBody
    @RequestMapping(value = "/admin/sign.do")
    public Result sign(String username, String password, int remember){
        Result result = new Result();
        try{
            username = username.trim();
            password = password.trim();
            if(StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password)){
                result.setCode(-1);
                result.setMsg("用户名和密码不能为空！");
                return result;
            }
            if(!StringUtil.isRangeLength(username, 2, 20) || !StringUtil.isRangeLength(password, 6, 20)){
                result.setCode(-1);
                result.setMsg("用户名或密码不正确！");
                return result;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("loginName", username);
            map.put("password", EncryptUtil.MD5(password));
            SystemUser user = systemUserService.query(map);
            if(user != null){
                //region Check User Status
                if(user.getStatus() == Constants.SYSTEM_USER_STATUS_INVALID){
                    result.setCode(-1);
                    result.setMsg("该账户不能登录，请联系管理员！");
                    return result;
                }
                //endregion

                //region Check User Authority
                List<SystemAuthority> authorities = systemUserService.queryAuthority(user.getUserId());
                if(authorities != null){
                    user.setAuthorities(authorities);

                    //region Set User All Authority URL
                    if(user.getUrls() == null){
                        user.setUrls(new ArrayList<String>());
                    }
                    for(SystemAuthority authority : authorities){
                        if(StringUtil.isNotNullOrEmpty(authority.getMenuUrl())){
                            user.getUrls().add(authority.getMenuUrl());
                        }
                        if(StringUtil.isNotNullOrEmpty(authority.getMenuLink())){
                            String[] links = authority.getMenuLink().split(",");
                            for(String link : links){
                                if(!user.getUrls().contains(link)) {
                                    user.getUrls().add(link);
                                }
                            }
                        }
                        if(StringUtil.isNotNullOrEmpty(authority.getActionLink())){
                            String[] links = authority.getActionLink().split(",");
                            for(String link : links){
                                if(!user.getUrls().contains(link)) {
                                    user.getUrls().add(link);
                                }
                            }
                        }
                    }
                    //endregion

                    //region Set User Authority Menu and Action
                    List<Integer> menuIds = new ArrayList<Integer>();
                    List<Integer> actionIds = new ArrayList<Integer>();
                    for (SystemAuthority item : authorities) {
                        if (item.getTargetType() == 1) {
                            menuIds.add(item.getAuthorityId());
                        } else if (item.getTargetType() == 2) {
                            actionIds.add(item.getAuthorityId());
                        }
                    }

                    map.clear();
                    map.put("menuIds", menuIds);
                    List<SystemMenu> menus = systemMenuService.queryList(map);

                    map.clear();
                    map.put("actionIds", actionIds);
                    List<SystemMenuAction> actions = systemMenuActionService.queryList(map);
                    if (menus != null && menus.size() > 0 && actions != null && actions.size() > 0) {
                        for (SystemMenu menu : menus) {
                            for (SystemMenuAction action : actions) {
                                if(action.getMenuId() == menu.getMenuId()){
                                    if(menu.getActions() == null){
                                        menu.setActions(new ArrayList<SystemMenuAction>());
                                    }
                                    menu.getActions().add(action);
                                }
                            }
                        }
                    }
                    user.setMenus(CommonUtil.showMenuTree(menus, 0));
                    //endregion
                }
                //endregion

                this.setCurrentUser(user);

                //
                if(remember == 1) {
                    JSONObject info = new JSONObject();
                    info.put("user", username);
                    info.put("remember", 1);
                    this.setCookie(Constants.COOKIE_ADMIN, info.toString(), 30 * 24 * 3600);
                }
                result.setCode(0);
            }else{
                result.setCode(1);
                result.setMsg("用户名或密码不正确！");
            }
        }catch(Exception e){
            logger.error("验证用户登录出错:" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/admin/signout.do")
    public String signout(){
        this.setCurrentUser(null);
        return redirect(Constants.UrlHelper.USER_LOGIN);
    }
    //endregion
}
