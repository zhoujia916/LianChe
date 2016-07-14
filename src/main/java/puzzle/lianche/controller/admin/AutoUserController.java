package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.controller.plugin.uploader.PathFormatter;
import puzzle.lianche.entity.*;
import puzzle.lianche.service.IAutoUserPicService;
import puzzle.lianche.service.IAutoUserProfileService;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.service.impl.AutoUserServiceImpl;
import puzzle.lianche.service.impl.SystemUserServiceImpl;
import puzzle.lianche.utils.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@Controller(value = "adminAutoUserController")
@RequestMapping(value = "/admin/autouser")
public class AutoUserController extends ModuleController {

    @Autowired
    private IAutoUserService autoUserService;

    @Autowired
    private IAutoUserProfileService autoUserProfileService;

    @Autowired
    private IAutoUserPicService autoUserPicService;

    @RequestMapping (value = {"/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_AUTO_USER;
    }

    @RequestMapping (value = {"/add"})
    public String add(){
        this.setModelAttribute("action", Constants.PageHelper.PAGE_ACTION_CREATE);
        return Constants.UrlHelper.ADMIN_AUTO_USER_SHOW;
    }

    @RequestMapping (value = {"/edit/{userId}"})
    public String edit(@PathVariable Integer userId){
        if(userId != null && userId > 0){
            AutoUser user = autoUserService.query(userId, null);
            if(user != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("userId", userId);

                AutoUserProfile profile = autoUserProfileService.query(map);
                user.setProfile(profile);

                if (user.getBirth() != null && user.getBirth() > 0) {
                    user.setBirthString(ConvertUtil.toString(new Date(user.getBirth()), Constants.DATE_FORMAT));
                }

                this.setModelAttribute("user", user);

                List<AutoUserPic> picList = autoUserPicService.queryList(map);
                this.setModelAttribute("picList", picList);
            }
        }
        this.setModelAttribute("action", Constants.PageHelper.PAGE_ACTION_UPDATE);
        return Constants.UrlHelper.ADMIN_AUTO_USER_SHOW;
    }

    @RequestMapping (value = {"/view/{userId}"})
    public String view(@PathVariable Integer userId){
        if(userId != null && userId > 0){
            AutoUser user = autoUserService.query(userId, null);
            if(user != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("userId", userId);

                AutoUserProfile profile = autoUserProfileService.query(map);
                user.setProfile(profile);

                if (user.getBirth() != null && user.getBirth() > 0) {
                    user.setBirthString(ConvertUtil.toString(new Date(user.getBirth()), Constants.DATE_FORMAT));
                }

                this.setModelAttribute("user", user);

                List<AutoUserPic> picList = autoUserPicService.queryList(map);
                this.setModelAttribute("picList", picList);
            }
        }
        this.setModelAttribute("action", Constants.PageHelper.PAGE_ACTION_VIEW);
        return Constants.UrlHelper.ADMIN_AUTO_USER_SHOW;
    }

    /**
     * 获取会员信息
     * @param autoUser
     * @return
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(AutoUser autoUser, Page page){
        Result result = new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoUser != null) {
                if(StringUtil.isNotNullOrEmpty(autoUser.getUserName())) {
                    map.put("userName", autoUser.getUserName());
                }
                if(StringUtil.isNotNullOrEmpty(autoUser.getShopName())) {
                    map.put("shopName", autoUser.getShopName());
                }
                if(autoUser.getStatus() != null && autoUser.getStatus() > 0){
                    map.put("status", autoUser.getStatus());
                }
                if(autoUser.getShopType() != null && autoUser.getShopType() > 0){
                    map.put("shopType", autoUser.getShopType());
                }
            }
            List<AutoUser> list = autoUserService.queryList(map,page);
            if(list != null && list.size() > 0){
                JSONArray array=new JSONArray();
                for(AutoUser user:list){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("userId",user.getUserId());
                    jsonObject.put("userAvatar",user.getUserAvatar());
                    jsonObject.put("userName",user.getUserName());
                    jsonObject.put("realName",user.getRealName());
                    jsonObject.put("shopName",user.getShopName());
                    jsonObject.put("sortOrder",user.getSortOrder());
                    jsonObject.put("password",user.getPassword());
                    jsonObject.put("phone",user.getPhone());
                    jsonObject.put("birth", user.getBirth() != null ? ConvertUtil.toString(ConvertUtil.toDate(user.getBirth()),"yyyy-MM-dd") : "");
                    jsonObject.put("email",user.getEmail());
                    jsonObject.put("point",user.getPoint());
                    jsonObject.put("sortOrder",user.getSortOrder());
                    jsonObject.put("remark",user.getRemark());
                    jsonObject.put("status",Constants.MAP_AUTO_USER_STATUS.get(user.getStatus()));
                    jsonObject.put("shopType", user.getShopType() != null ? Constants.MAP_AUTO_USER_SHOP_TYPE.get(user.getShopType()) : "");
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("获取会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 操作会员信息
     * @param action
     * @param autoUser
     * @return
     */
    @RequestMapping(value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoUser autoUser){
        Result result=new Result();
        Map<String, Object> map=new HashMap<String, Object>();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                AutoUser find = autoUserService.query(null, autoUser.getUserName());
                if(find != null){
                    result.setCode(-1);
                    result.setMsg("该账户已存在！");
                    return result;
                }

                if(StringUtil.isNullOrEmpty(autoUser.getPassword())){
                    autoUser.setPassword("666666");
                }
                autoUser.setPassword(EncryptUtil.MD5(autoUser.getPassword()));
                if(StringUtil.isNotNullOrEmpty(autoUser.getBirthString())){
                    autoUser.setBirth(ConvertUtil.toLong(ConvertUtil.toDate(autoUser.getBirthString())));
                }else{
                    autoUser.setBirth(new Long(0));
                }

                String avatar = saveAvatar();
                if(StringUtil.isNullOrEmpty(avatar)){
                    avatar = getHost() + "/resource/admin/avatars/profile-pic.jpg";
                }
                autoUser.setUserAvatar(avatar);

                autoUser.setAddTime(ConvertUtil.toLong(new Date()));
                autoUser.setPhone(autoUser.getUserName());
                autoUser.setStatus(Constants.AUTO_USER_STATUS_NORMAL);
                autoUser.setSortOrder(0);
                if(!autoUserService.insert(autoUser)){
                    result.setCode(1);
                    result.setMsg("新建会员信息失败");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"新建会员信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(StringUtil.isNotNullOrEmpty(autoUser.getBirthString())){
                    autoUser.setBirth(ConvertUtil.toLong(ConvertUtil.toDate(autoUser.getBirthString()+" 00:00:00")));
                }
                if(!autoUserService.update(autoUser)){
                    result.setCode(1);
                    result.setMsg("更新会员信息失败");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"更新会员信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                String id = request.getParameter("id");
                String ids = request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("userId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] userIds=ids.split(",");
                    map.put("userIds", userIds);
                }
                if(!autoUserService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除会员信息失败");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除会员信息");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }


    /**
     * 实名认证会员信息
     * @param action
     * @param autoUser
     * @return
     */
    @RequestMapping(value = "/auth.do")
    @ResponseBody
    public Result auth(String action, AutoUser autoUser){
        Result result=new Result();
        try{
            autoUser = autoUserService.query(autoUser.getUserId(), null);
            autoUser.setAuthenticateTime(ConvertUtil.toLong(new Date()));
            if(action.equals("accept")){
                autoUser.setStatus(Constants.AUTO_USER_STATUS_AUTH_SUCCESS);
            }
            else if(action.equals("reject")){
                autoUser.setStatus(Constants.AUTO_USER_STATUS_AUTH_FAIL);
            }
            if(!autoUserService.update(autoUser)){
                result.setCode(1);
                result.setMsg("实名认证会员信息失败");
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("实名认证会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    public String saveAvatar(){
        try {
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(session.getServletContext());
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                MultipartFile cover = multiRequest.getFile("file");

                String typePath = "avatar";
                String savePath = session.getServletContext().getRealPath("") + "/upload/" + typePath + "/";
                String relativeUrl = request.getContextPath() + "/upload/" + typePath + "/";
                String saveName = PathFormatter.format(cover.getOriginalFilename(), "{yy}{MM}{dd}/{hh}{mm}{rand:6}");
                String dirName = savePath + saveName.substring(0, saveName.lastIndexOf('/'));
                File dir = new File(dirName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(savePath + saveName);
                fos.write(cover.getBytes());
                fos.close();

                String url = getHost() + relativeUrl + saveName;

                return url;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}