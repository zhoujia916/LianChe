package puzzle.lianche.controller.admin;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.*;
import puzzle.lianche.service.*;
import puzzle.lianche.utils.*;

import javax.servlet.http.Cookie;
import java.net.HttpCookie;
import java.util.*;

@Controller(value="adminSystemIndexController")
public class SystemIndexController extends ModuleController {
    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemMenuService systemMenuService;

    @Autowired
    private ISystemMenuActionService systemMenuActionService;

    @Autowired
    private IAutoUserService autoUserService;

    @Autowired
    private IAutoOrderService autoOrderService;

    @Autowired
    private IAutoCarService autoCarService;

    //region request page
    @RequestMapping(value = {"/admin", "/admin/", "/admin/login"})
    public String login(){
        String returnUrl = this.getParameter(Constants.UrlHelper.PARAM_RETURN_URL);
        if(this.getCurrentUser() != null){
            if(StringUtil.isNotNullOrEmpty(returnUrl)){
                return this.redirect(Constants.UrlHelper.ADMIN_SYSTEM_INDEX + returnUrl.replace("/admin/", "#page/"));
            }else {
                return this.redirect(Constants.UrlHelper.ADMIN_SYSTEM_INDEX);
            }
        }
        String value = this.getCookie(Constants.COOKIE_ADMIN);
        if(StringUtil.isNotNullOrEmpty(value)){
            JSONObject info = JSONObject.fromObject(value);
            this.setModelAttribute("user", info.get("user"));
            this.setModelAttribute("remember", info.getInt("remember"));
        }
        if(StringUtil.isNotNullOrEmpty(returnUrl)){
            this.setModelAttribute(Constants.UrlHelper.PARAM_RETURN_URL, Constants.UrlHelper.ADMIN_SYSTEM_INDEX + returnUrl.replace("/admin/", "#page/"));
        }
        return Constants.UrlHelper.ADMIN_SYSTEM_LOGIN;
    }

    @RequestMapping(value = {"/logout", "/admin/logout", "/admin/logout"})
    public String logout(){
        SystemUser user = (SystemUser)this.getCurrentUser();
        if(user != null) {
            this.setCurrentUser(null);
        }
        return redirect(Constants.UrlHelper.ADMIN_SYSTEM_LOGIN);
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
            map.put("status", Constants.AUTO_USER_STATUS_AUTH_WAITCHECK);
            Page page = new Page();
            page.setPageIndex(Constants.PageHelper.PAGE_INDEX_FIRST);
            page.setPageSize(Constants.PageHelper.PAGE_SIZE_COMMON);
            List<AutoUser> users = autoUserService.queryList(map, page);
            Result task = new Result();
            task.setTotal(page.getTotal());
            task.setData(users);
            this.setModelAttribute("task", task);
            //endregion

        }
        return Constants.UrlHelper.ADMIN_SYSTEM_INDEX;
    }

    @RequestMapping(value = "/admin/main")
    public String main(){
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            //region Count Year, Fest, Month, Week
            Calendar calendar = Calendar.getInstance();
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH);
            Integer day = calendar.get(Calendar.DAY_OF_MONTH);
            Integer wday = calendar.get(Calendar.DAY_OF_WEEK);
            Integer miliseconds = calendar.get(Calendar.MILLISECOND);
            Long endTime = calendar.getTimeInMillis();


            map.clear();
            map.put("endTime", endTime);

            calendar.set(year, 0, 1, 0, 0, 0);
            Long startTime = calendar.getTimeInMillis() - miliseconds;

            map.put("startTime", startTime);
            int yearOrderCount = autoOrderService.queryCount(map);
            int yearUserCount = autoUserService.queryCount(map);
            int yearCarCount = autoCarService.queryCount(map);
            this.setModelAttribute("yearOrderCount", yearOrderCount);
            this.setModelAttribute("yearUserCount", yearUserCount);
            this.setModelAttribute("yearCarCount", yearCarCount);

            int quarterStartMonth = month < 3 ? 0 : month < 6 ? 3 : month < 9 ? 6 : 9;

            calendar.set(year, quarterStartMonth, 1, 0, 0, 0);
            startTime = calendar.getTimeInMillis() - miliseconds;
            map.put("startTime", startTime);

            int quarterOrderCount = autoOrderService.queryCount(map);
            int quarterUserCount = autoUserService.queryCount(map);
            int quarterCarCount = autoCarService.queryCount(map);
            this.setModelAttribute("quarterOrderCount", quarterOrderCount);
            this.setModelAttribute("quarterUserCount", quarterUserCount);
            this.setModelAttribute("quarterCarCount", quarterCarCount);

            calendar.set(year, month, 1, 0, 0, 0);
            startTime = calendar.getTimeInMillis() - miliseconds;
            map.put("startTime", startTime);
            int monthOrderCount = autoOrderService.queryCount(map);
            int monthUserCount = autoUserService.queryCount(map);
            int monthCarCount = autoCarService.queryCount(map);
            this.setModelAttribute("monthOrderCount", monthOrderCount);
            this.setModelAttribute("monthUserCount", monthUserCount);
            this.setModelAttribute("monthCarCount", monthCarCount);


            calendar.set(Calendar.DAY_OF_WEEK, 0);
            startTime = calendar.getTimeInMillis() - miliseconds;
            map.put("startTime", startTime);
            int weekOrderCount = autoOrderService.queryCount(map);
            int weekUserCount = autoUserService.queryCount(map);
            int weekCarCount = autoCarService.queryCount(map);
            this.setModelAttribute("weekOrderCount", weekOrderCount);
            this.setModelAttribute("weekUserCount", weekUserCount);
            this.setModelAttribute("weekCarCount", weekCarCount);
        }
        catch (Exception e){

        }
        //endregion
        return Constants.UrlHelper.ADMIN_SYSTEM_MAIN;
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
                        if (item.getTargetType() == Constants.AUTO_AUTHORITY_TARGET_TYPE_MENU) {
                            menuIds.add(item.getTargetId());
                        } else if (item.getTargetType() == Constants.AUTO_AUTHORITY_TARGET_TYPE_ACTION) {
                            actionIds.add(item.getTargetId());
                        }
                    }

                    map.clear();
                    map.put("menuIds", StringUtil.concat(menuIds, ","));
                    List<SystemMenu> menus = systemMenuService.queryList(map);

                    map.clear();
                    map.put("actionIds", StringUtil.concat(actionIds, ","));
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

                    //region Debug Authory
                    StringBuffer authory = new StringBuffer();
                    authory.append("-------------------------------------------------------------------------------");
                    authory.append(System.getProperty(System.getProperty("line.separator")));
                    authory.append("AUTHORY MENU:").append(System.getProperty(System.getProperty("line.separator")));
                    for(SystemMenu menu : menus){
                        authory.append(menu.toString()).append(System.getProperty(System.getProperty("line.separator")));
                    }
                    authory.append("AUTHORY ACTION:").append(System.getProperty(System.getProperty("line.separator")));
                    for(SystemMenuAction action : actions){
                        authory.append(action.toString()).append(System.getProperty(System.getProperty("line.separator")));
                    }
                    authory.append("-------------------------------------------------------------------------------");
                    logger.debug(authory.toString());
                    //endregion
                }
                //endregion

                this.setCurrentUser(user);
                //
                if(remember == 1) {
                    JSONObject info = new JSONObject();
                    info.put("user", username);
                    info.put("remember", 1);
                    this.setCookie(Constants.COOKIE_ADMIN, info.toString(), 30 * 24 * 3600, "/admin/");

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
        return redirect(Constants.UrlHelper.ADMIN_SYSTEM_LOGIN);
    }
    //endregion
}
