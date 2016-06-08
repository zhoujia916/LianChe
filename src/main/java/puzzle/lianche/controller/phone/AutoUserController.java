package puzzle.lianche.controller.phone;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.entity.AutoSms;
import puzzle.lianche.entity.AutoUser;
import puzzle.lianche.entity.AutoUserProfile;
import puzzle.lianche.push.SmsPush;
import puzzle.lianche.service.IAutoSmsService;
import puzzle.lianche.service.IAutoUserProfileService;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.utils.*;

import java.util.*;
import java.lang.Object;

@Controller(value = "phoneAutoUserController")
@RequestMapping(value = "/phone/autouser")
public class AutoUserController extends BaseController {

    @Autowired
    private IAutoUserService autoUserService;

    @Autowired
    private IAutoUserProfileService autoUserProfileService;

    @Autowired
    private IAutoSmsService autoSmsService;

    /**
     * 发送短信验证码用于注册和找回密码
     * @param phone
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/sendCode.do")
    @ResponseBody
    public Result sendCode(String phone, String keyword){
        Result result = new Result();
        try{
            if(StringUtil.isNullOrEmpty(phone)){
                result.setCode(-1);
                result.setMsg("手机号不能为空！");
            }else if(!StringUtil.isPhone(phone)){
                result.setCode(-1);
                result.setMsg("电话号码格式错误！");
            }else if(StringUtil.isNullOrEmpty(keyword)){
                result.setCode(-1);
                result.setMsg("请求参数错误！");
            }else{
                //得到六位的随机数作为验证码
                String code= CommonUtil.getCode(6);
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("code", code);
                String response = SmsPush.send(phone, 1, map);
                if(SmsPush.isSuccess(response)){
                    AutoSms sms=new AutoSms();
                    if("register".equalsIgnoreCase(keyword)){
                        sms.setSmsType(Constants.SMS_TYPE_REGISTER);
                    }else if("retrieve".equalsIgnoreCase(keyword)){
                        sms.setSmsType(Constants.SMS_TYPE_RETRIEVE);
                    }else if("modify".equalsIgnoreCase(keyword)){
                        sms.setSmsType(Constants.SMS_TYPE_MODIFY);
                    }else if("notice".equalsIgnoreCase(keyword)){
                        sms.setSmsType(Constants.SMS_TYPE_NOTICE);
                    }
                    sms.setSmsContent("发送验证码："+code);
                    sms.setCode(code);
                    sms.setPhone(phone);
                    sms.setStatus(Constants.SMS_STATUS_TRUE);
                    autoSmsService.insert(sms);
                    result.setData(code);
                }else{
                    AutoSms sms=new AutoSms();
                    if("register".equalsIgnoreCase(keyword)){
                        sms.setSmsType(Constants.SMS_TYPE_REGISTER);
                    }else if("retrieve".equalsIgnoreCase(keyword)){
                        sms.setSmsType(Constants.SMS_TYPE_RETRIEVE);
                    }else if("modify".equalsIgnoreCase(keyword)){
                        sms.setSmsType(Constants.SMS_TYPE_MODIFY);
                    }else if("notice".equalsIgnoreCase(keyword)){
                        sms.setSmsType(Constants.SMS_TYPE_NOTICE);
                    }
                    sms.setSmsContent("发送验证码："+code);
                    sms.setCode(code);
                    sms.setPhone(phone);
                    sms.setStatus(Constants.SMS_STATUS_FALSE);
                    autoSmsService.insert(sms);
                    result.setCode(1);
                    result.setMsg(SmsPush.getError(response));
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("发送短信验证码出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 注册会员信息
     * @param autoUser
     * @return
     */
    @RequestMapping(value = "/register.do")
    @ResponseBody
    public Result register(AutoUser autoUser){
        Result result = new Result();
        try{
            if(StringUtil.isNullOrEmpty(autoUser.getUserName())){
                result.setCode(-1);
                result.setMsg("用户名不能为空！");
            }else if(!StringUtil.isPhone(autoUser.getUserName())){
                result.setCode(-1);
                result.setMsg("电话号码格式错误！");
            }else if(StringUtil.isNullOrEmpty(autoUser.getPassword())){
                result.setCode(-1);
                result.setMsg("密码不能为空！");
            }else if(autoUser.getPassword().length()<6 || autoUser.getPassword().length()>20){
                result.setCode(-1);
                result.setMsg("密码长度必须在6-20范围内！");
            }else if(StringUtil.isNullOrEmpty(autoUser.getCode())){
                result.setCode(-1);
                result.setMsg("验证码不能为空！");
            }else if(autoUser.getCode().length()!=6){
                result.setCode(-1);
                result.setMsg("验证码长度错误，应为6位！");
            }else{
                Map<String,Object> map=new HashMap<String, Object>();
                map.put("username",autoUser.getUserName());
                AutoUser user=autoUserService.query(map);
                if(user!=null){
                    result.setCode(1);
                    result.setMsg("该账户已被注册！");
                }else{
                    map.clear();
                    map.put("smsType",Constants.SMS_TYPE_REGISTER);
                    map.put("phone",autoUser.getUserName());
                    map.put("status",Constants.SMS_STATUS_TRUE);
                    AutoSms sms=autoSmsService.query(map);
                    if(!sms.getCode().equalsIgnoreCase(autoUser.getCode())){
                        result.setCode(-1);
                        result.setMsg("验证码错误");
                    }else{
                        autoUser.setPassword(EncryptUtil.MD5(autoUser.getPassword()));
                        autoUser.setUserAvatar("../resource/admin/avatars/profile-pic.jpg");
                        autoUser.setPoint(100);
                        autoUser.setPhone(autoUser.getUserName());
                        autoUser.setSortOrder(0);
                        if(autoUserService.insert(autoUser)){
                            JSONArray array=new JSONArray();
                            JSONObject jsonObject=JSONObject.fromObject(autoUser);
                            jsonObject.put("isAuthenticate",false);
                            array.add(jsonObject);
                            result.setData(array);
                        }else{
                            result.setCode(1);
                            result.setMsg("注册失败");
                        }
                    }
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 会员登录验证
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/login.do")
    @ResponseBody
    public Result login(String username, String password){
        Result result = new Result();
        try{
            username = username.trim();
            password = password.trim();
            if(StringUtil.isNullOrEmpty(username)){
                result.setCode(-1);
                result.setMsg("用户名不能为空！");
            }else if(!StringUtil.isPhone(username)){
                result.setCode(-1);
                result.setMsg("电话号码格式错误！");
            }else if(StringUtil.isNullOrEmpty(password)){
                result.setCode(-1);
                result.setMsg("密码不能为空！");
            }else if(password.length()<6 || password.length()>20){
                result.setCode(-1);
                result.setMsg("密码长度必须在6-20范围内！");
            }else{
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("username", username);
                map.put("password", EncryptUtil.MD5(password));
                AutoUser user = autoUserService.query(map);
                if(user != null){
                    //加载个人资料
                    JSONArray array=new JSONArray();
                    JSONObject jsonObject=JSONObject.fromObject(user);
                    if(user.getStatus()==Constants.AUTO_USER_STATUS_AUTHENTICATIONADOPT){
                        jsonObject.put("isAuthenticate",true);
                    }else{
                        jsonObject.put("isAuthenticate",false);
                    }
                    map.clear();
                    map.put("userId",user.getUserId());
                    AutoUserProfile profile=autoUserProfileService.query(map);
                    if(profile!=null){
                        jsonObject.put("profile",profile);
                    }
                    array.add(jsonObject);
                    result.setData(array);
                }else{
                    result.setCode(1);
                    result.setMsg("用户名或密码不正确！");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("会员登录验证出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 找回密码
     * @param autoUser
     * @return
     */
    @RequestMapping(value = "/retrieve.do")
    @ResponseBody
    public Result retrieve(AutoUser autoUser){
        Result result=new Result();
        try{
            if(StringUtil.isNullOrEmpty(autoUser.getUserName())){
                result.setCode(-1);
                result.setMsg("用户名不能为空！");
            }else if(!StringUtil.isPhone(autoUser.getUserName())){
                result.setCode(-1);
                result.setMsg("电话号码格式错误！");
            }else if(StringUtil.isNullOrEmpty(autoUser.getPassword())){
                result.setCode(-1);
                result.setMsg("密码不能为空！");
            }else if(autoUser.getPassword().length()<6 || autoUser.getPassword().length()>20){
                result.setCode(-1);
                result.setMsg("密码长度必须在6-20范围内！");
            }else if(StringUtil.isNullOrEmpty(autoUser.getCode())){
                result.setCode(-1);
                result.setMsg("验证码不能为空！");
            }else if(autoUser.getCode().length()!=6){
                result.setCode(-1);
                result.setMsg("验证码长度错误，应为6位！");
            }else{
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("username", autoUser.getUserName());
                AutoUser user = autoUserService.query(map);
                if(user != null){
                    map.clear();
                    map.put("smsType",Constants.SMS_TYPE_RETRIEVE);
                    map.put("phone",autoUser.getUserName());
                    map.put("status",Constants.SMS_STATUS_TRUE);
                    AutoSms sms=autoSmsService.query(map);
                    if(!sms.getCode().equalsIgnoreCase(autoUser.getCode())){
                        result.setCode(-1);
                        result.setMsg("验证码错误");
                    }else{
                        autoUser.setUserId(user.getUserId());
                        autoUser.setPassword(EncryptUtil.MD5(autoUser.getPassword()));
                        if(!autoUserService.update(autoUser)){
                            result.setCode(1);
                            result.setMsg("修改新密码失败");
                        }
                    }
                }else{
                    result.setCode(1);
                    result.setMsg("不存在该用户！");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作找回密码时失败");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 完善个人信息
     * @param autoUserProfile
     * @return
     */
    @RequestMapping(value = "/prefectUser.do")
    @ResponseBody
    public Result prefectUser(AutoUserProfile autoUserProfile){
        Result result=new Result();
        try{
            if(StringUtil.isNullOrEmpty(autoUserProfile.getRealName())){
                result.setCode(-1);
                result.setMsg("姓名不能为空！");
            }else if(StringUtil.isNullOrEmpty(autoUserProfile.getPhone())){
                result.setCode(-1);
                result.setMsg("手机号不能为空！");
            }else if(!StringUtil.isPhone(autoUserProfile.getPhone())){
                result.setCode(-1);
                result.setMsg("手机号格式错误！");
            }else if(autoUserProfile.getShopType()==0){
                result.setCode(-1);
                result.setMsg("请选择商家性质！");
            }else if(StringUtil.isNullOrEmpty(autoUserProfile.getShopName())){
                result.setCode(-1);
                result.setMsg("商家名称不能为空！");
            }else if(StringUtil.isNullOrEmpty(autoUserProfile.getShopDesc())){
                result.setCode(-1);
                result.setMsg("详细信息不能为空！");
            }else if(StringUtil.isNullOrEmpty(autoUserProfile.getShopBrands())){
                result.setCode(-1);
                result.setMsg("主营品牌不能为空！");
            }else if(StringUtil.isNullOrEmpty(autoUserProfile.getShopBase())){
                result.setCode(-1);
                result.setMsg("仓库信息不能为空！");
            }else{
                autoUserProfile.setPhone(null);
                Map<String,Object> map=new HashMap<String, Object>();
                map.put("userId",autoUserProfile.getUserId());
                AutoUserProfile userProfile=autoUserProfileService.query(map);
                if(userProfile==null){
                    autoUserProfile.setUserId(autoUserProfile.getUserId());
                    if(!autoUserProfileService.insert(autoUserProfile)){
                        result.setCode(1);
                        result.setMsg("完善个人信息出错！");
                    }
                }else{
                    autoUserProfile.setProfileId(userProfile.getProfileId());
                    if(!autoUserProfileService.update(autoUserProfile)){
                        result.setCode(1);
                        result.setMsg("完善个人信息出错！");
                    }
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作完善个人时信息出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 实名认证
     * @param idNumber
     * @return
     */
    @RequestMapping(value = "/certified.do")
    @ResponseBody
    public Result certified(String idNumber,Integer userId){
        Result result=new Result();
        try{
            if(StringUtil.isNullOrEmpty(idNumber)){
                result.setCode(-1);
                result.setMsg("身份证号码不能为空！");
            }else if(!StringUtil.isIdNumber(idNumber)){
                result.setCode(-1);
                result.setMsg("身份证号码格式出错！");
            }else{
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("userId",userId);
                AutoUserProfile autoUserProfile=autoUserProfileService.query(map);
                if(autoUserProfile==null){
                    AutoUserProfile userProfile=new AutoUserProfile();
                    userProfile.setUserId(userId);
                    userProfile.setIdNumber(idNumber);
                    if(!autoUserProfileService.insert(userProfile)){
                        result.setCode(1);
                        result.setMsg("操作实名认证失败");
                    }
                }else{
                    AutoUserProfile userProfile=new AutoUserProfile();
                    userProfile.setProfileId(autoUserProfile.getProfileId());
                    userProfile.setIdNumber(idNumber);
                    if(!autoUserProfileService.update(userProfile)){
                        result.setCode(1);
                        result.setMsg("操作实名认证失败");
                    }
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作实名认证出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    public Result memberCenter(Integer userId){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("userId",userId);
            AutoUser autoUser=autoUserService.query(map);
            if(autoUser!=null){
                //加载个人资料
                JSONArray array=new JSONArray();
                JSONObject jsonObject=JSONObject.fromObject(autoUser);
                if(autoUser.getStatus()==Constants.AUTO_USER_STATUS_AUTHENTICATIONADOPT){
                    jsonObject.put("isAuthenticate",true);
                }else{
                    jsonObject.put("isAuthenticate",false);
                }
                AutoUserProfile profile=autoUserProfileService.query(map);
                if(profile!=null){
                    jsonObject.put("profile",profile);
                }
                array.add(jsonObject);
                result.setData(array);
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("查看会员中心出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}