package puzzle.lianche.controller.phone;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.entity.*;
import puzzle.lianche.push.SmsPush;
import puzzle.lianche.service.*;
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

    @Autowired
    private IAutoCollectService autoCollectService;

    @Autowired
    private IAutoCarService autoCarService;

    @Autowired
    private IAutoCarPicService autoCarPicService;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    @Autowired
    private IAutoMsgService autoMsgService;

    @Autowired
    private IAutoFeedbackService autoFeedbackService;

    @Autowired
    private IAutoOrderService autoOrderService;

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
                return result;
            }
            if(!StringUtil.isPhone(phone)){
                result.setCode(-1);
                result.setMsg("电话号码格式错误！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(keyword)){
                result.setCode(-1);
                result.setMsg("请求参数错误！");
                return result;
            }

            //得到六位的随机数作为验证码
            String code= CommonUtil.getCode(6);
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("code", code);
            String response = SmsPush.send(SmsPush.CODE_SENDCODE, phone, code);
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
            if(SmsPush.isSuccess(response)) {
                sms.setStatus(Constants.SMS_STATUS_TRUE);
            }else{
                sms.setStatus(Constants.SMS_STATUS_FALSE);
            }
            if(autoSmsService.insert(sms)) {
                result.setData(code);
            }else{
                result.setCode(1);
                result.setMsg(SmsPush.getError(response));
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("发送短信验证码出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 注册会员信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/register.do")
    @ResponseBody
    public Result register(AutoUser user){
        Result result = new Result();
        try {
            if(StringUtil.isNullOrEmpty(user.getUserName())){
                result.setCode(-1);
                result.setMsg("用户名不能为空！");
                return result;
            }
            if(!StringUtil.isPhone(user.getUserName())){
                result.setCode(-1);
                result.setMsg("手机号码格式不正确！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(user.getPassword())){
                result.setCode(-1);
                result.setMsg("密码不能为空！");
                return result;
            }
            if(!StringUtil.isRangeLength(user.getPassword(), 6, 20)){
                result.setCode(-1);
                result.setMsg("密码长度必须在6-20范围内！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(user.getCode())){
                result.setCode(-1);
                result.setMsg("验证码不能为空！");
                return result;
            }
            if(user.getCode().length() != 6){
                result.setCode(-1);
                result.setMsg("验证不正确！");
                return result;
            }

            Map<String,Object> map=new HashMap<String, Object>();
            map.put("username",user.getUserName());
            AutoUser cur = autoUserService.query(0, user.getUserName());
            if(cur != null){
                result.setCode(1);
                result.setMsg("该账户已注册！");
                return result;
            }
            map.clear();
            map.put("smsType",Constants.SMS_TYPE_REGISTER);
            map.put("phone",user.getUserName());
            map.put("status",Constants.SMS_STATUS_TRUE);
            AutoSms sms = autoSmsService.query(map);
            if(!sms.getCode().equalsIgnoreCase(user.getCode())){
                result.setCode(-1);
                result.setMsg("验证码错误");
                return result;
            }

            user.setPassword(EncryptUtil.MD5(user.getPassword()));
            user.setUserAvatar("/resource/admin/avatars/profile-pic.jpg");
            user.setPoint(100);
            user.setPhone(user.getUserName());
            user.setBirth(new Long(0));
            user.setSortOrder(0);
            user.setStatus(Constants.AUTO_USER_STATUS_NORMAL);
            user.setAddTime(ConvertUtil.toLong(new Date()));
            if(autoUserService.insert(user)){
                result.setData(user.getUserId());
            }else{
                result.setCode(1);
                result.setMsg("注册会员信息失败！");
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
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
            if(StringUtil.isNullOrEmpty(username) || !StringUtil.isPhone(username)){
                result.setCode(-1);
                result.setMsg("用户名不正确！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(password) || !StringUtil.isRangeLength(password, 6, 20)){
                result.setCode(-1);
                result.setMsg("密码不正确！");
                return result;
            }

            Map<String,Object> map = new HashMap<String, Object>();
            map.put("userName", username);
            map.put("password", EncryptUtil.MD5(password));
            AutoUser user = autoUserService.query(map);
            if(user == null){
                result.setCode(1);
                result.setMsg("用户名或密码不正确！");
                return result;
            }
            if(user.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(1);
                result.setMsg("该账户被禁用！");
                return result;
            }

            //加载个人资料
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId",user.getUserId());
            jsonObject.put("userName",user.getUserName());
            jsonObject.put("userAvatar",user.getUserAvatar());
            jsonObject.put("point",user.getPoint());
            jsonObject.put("phone",user.getPhone());
            jsonObject.put("isAuth",user.getStatus() == Constants.AUTO_USER_STATUS_AUTH_SUCCESS);
            map.clear();
            map.put("userId",user.getUserId());

            AutoUserProfile profile = autoUserProfileService.query(map);
            if(profile!=null){
                jsonObject.put("profile",profile);
            }
            result.setData(jsonObject);
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("登录验证出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 找回密码
     * @param user
     * @return
     */
    @RequestMapping(value = "/retrievePassword.do")
    @ResponseBody
    public Result retrievePassword(AutoUser user){
        Result result=new Result();
        try{
            if(StringUtil.isNullOrEmpty(user.getUserName())){
                result.setCode(-1);
                result.setMsg("用户名不能为空！");
                return result;
            }
            if(!StringUtil.isPhone(user.getUserName())){
                result.setCode(-1);
                result.setMsg("手机号码格式不正确！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(user.getPassword())){
                result.setCode(-1);
                result.setMsg("密码不能为空！");
                return result;
            }
            if(!StringUtil.isRangeLength(user.getPassword(), 6, 20)){
                result.setCode(-1);
                result.setMsg("密码长度必须在6-20范围内！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(user.getCode()) || user.getCode().length()!=6){
                result.setCode(-1);
                result.setMsg("验证码正确！");
                return result;
            }
            AutoUser find = autoUserService.query(0, user.getUserName());
            if(find == null){
                result.setCode(1);
                result.setMsg("该用户不存在！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(1);
                result.setMsg("该账户已被禁用！");
                return result;
            }
            HashMap<String,Object> map = new HashMap<String, Object>();
            map.clear();
            map.put("smsType",Constants.SMS_TYPE_RETRIEVE);
            map.put("phone",user.getUserName());
            map.put("status",Constants.SMS_STATUS_TRUE);
            AutoSms sms = autoSmsService.query(map);
            if(!sms.getCode().equals(user.getCode())){
                result.setCode(-1);
                result.setMsg("验证码错误!");
                return result;
            }
            user.setUserId(find.getUserId());
            user.setPassword(EncryptUtil.MD5(user.getPassword()));
            if(!autoUserService.update(user)){
                result.setCode(1);
                result.setMsg("修改新密码失败！");
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("修改新密码错误！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 修改密码
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/updatePassword.do")
    @ResponseBody
    public Result updatePassword(Integer userId, String oldPassword, String newPassword){
        Result result = new Result();
        try{
            if(userId == null || userId <= 0){
                result.setCode(-1);
                result.setMsg("用户不能为空！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(oldPassword)){
                result.setCode(-1);
                result.setMsg("原密码不能为空！");
                return result;
            }
            if(!StringUtil.isRangeLength(oldPassword, 6, 20)){
                result.setCode(-1);
                result.setMsg("原密码长度必须在6-20范围内！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(newPassword)){
                result.setCode(-1);
                result.setMsg("新密码不能而空！");
                return result;
            }
            if(!StringUtil.isRangeLength(newPassword, 6, 20)) {
                result.setCode(-1);
                result.setMsg("新密码长度必须在6-20范围内！");
                return result;
            }
            String password= EncryptUtil.MD5(oldPassword);

            AutoUser user = autoUserService.query(userId, null);
            if(user == null){
                result.setCode(-1);
                result.setMsg("该用户不存在！");
                return result;
            }
            if(user.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(-1);
                result.setMsg("该账户被禁用！");
                return result;
            }
            if(!password.equals(user.getPassword())){
                result.setCode(1);
                result.setMsg("原密码错误！");
                return result;
            }
            user.setPassword(EncryptUtil.MD5(newPassword));
            user.setUserId(userId);
            if(!autoUserService.update(user)){
                result.setCode(1);
                result.setMsg("修改密码失败！");
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("修改密码出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 完善个人信息
     * @param profile
     * @return
     */
    @RequestMapping(value = "/updateProfile.do")
    @ResponseBody
    public Result updateProfile(AutoUserProfile profile){
        Result result=new Result();
        try{
            if(StringUtil.isNullOrEmpty(profile.getRealName())){
                result.setCode(-1);
                result.setMsg("姓名不能为空！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(profile.getPhone())){
                result.setCode(-1);
                result.setMsg("手机号不能为空！");
                return result;
            }
            if(!StringUtil.isPhone(profile.getPhone())){
                result.setCode(-1);
                result.setMsg("手机号格式错误！");
                return result;
            }
            if(profile.getShopType()==0){
                result.setCode(-1);
                result.setMsg("请选择商家性质！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(profile.getShopName())){
                result.setCode(-1);
                result.setMsg("商家名称不能为空！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(profile.getShopDesc())){
                result.setCode(-1);
                result.setMsg("详细信息不能为空！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(profile.getShopBrands())){
                result.setCode(-1);
                result.setMsg("主营品牌不能为空！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(profile.getShopBase())){
                result.setCode(-1);
                result.setMsg("仓库信息不能为空！");
                return result;
            }
            AutoUser find = autoUserService.query(profile.getUserId(), null);
            if(find == null){
                result.setCode(-1);
                result.setMsg("该用户不存在！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(-1);
                result.setMsg("该账户被禁用！");
                return result;
            }

            profile.setPhone(null);
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("userId", profile.getUserId());
            AutoUserProfile userProfile = autoUserProfileService.query(map);
            if(userProfile == null){
                if(!autoUserProfileService.insert(profile)){
                    result.setCode(1);
                    result.setMsg("完善个人信息失败！");
                }
            }else{
                profile.setProfileId(userProfile.getProfileId());
                if(!autoUserProfileService.update(profile)){
                    result.setCode(1);
                    result.setMsg("完善个人信息失败！");
                }
            }

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("完善个人时信息出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新个人图像
     * @param user
     * @return
     */
    @RequestMapping(value = "/updateAvatar.do")
    @ResponseBody
    public Result updateAvatar(AutoUser user){
        Result result=new Result();
        try{
            if(user == null || user.getUserId() == null || user.getUserId() == 0){
                result.setCode(-1);
                result.setMsg("用户不能为空！");
                return result;
            }

            if(StringUtil.isNullOrEmpty(user.getUserAvatar())){
                result.setCode(-1);
                result.setMsg("用户图像不能为空！");
                return result;
            }
            AutoUser find = autoUserService.query(user.getUserId(), user.getUserName());
            if(find == null){
                result.setCode(-1);
                result.setMsg("该用户不存在！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(-1);
                result.setMsg("该账户被禁用！");
                return result;
            }
            if(!autoUserService.update(user)){
                result.setCode(1);
                result.setMsg("保存用户图像出错！");
            }

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("保存用户图像出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 申请实名认证
     * @param user
     * @return
     */
    @RequestMapping(value = "/updateAuth.do")
    @ResponseBody
    public Result updateAuth(AutoUser user){
        Result result = new Result();
        try{
            if(user == null || user.getUserId() == null || user.getUserId() == 0){
                result.setCode(-1);
                result.setMsg("用户不能为空！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(user.getIdNumber())){
                result.setCode(-1);
                result.setMsg("身份证号码不能为空！");
                return result;
            }
            if(!StringUtil.isIdNumber(user.getIdNumber())){
                result.setCode(-1);
                result.setMsg("身份证号码格式不正确！");
                return result;
            }
            if(StringUtil.isNullOrEmpty(user.getPic())){
                result.setCode(-1);
                result.setMsg("相关认证图片不能为空！");
                return result;
            }
            AutoUser find = autoUserService.query(user.getUserId(), user.getUserName());
            if(find == null){
                result.setCode(-1);
                result.setMsg("用户不存在！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(-1);
                result.setMsg("该账户被禁用！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_AUTH_WAITCHECK){
                result.setCode(-1);
                result.setMsg("用户账户正在等待实名验证！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_AUTH_SUCCESS){
                result.setCode(-1);
                result.setMsg("用户账户已实名认证！");
                return result;
            }

            String spliter = "※";
            String[] pics = user.getPic().split(spliter);
            List<AutoUserPic> list = new ArrayList<AutoUserPic>();
            for(String pic : pics){
                AutoUserPic userPic = new AutoUserPic();
                userPic.setUserId(user.getUserId());
                userPic.setAddTime(ConvertUtil.toLong(new Date()));
                userPic.setPicPath(pic);
                userPic.setPicType(0);
                list.add(userPic);
            }
            user.setAuthenticateTime(ConvertUtil.toLong(new Date()));
            user.setPics(list);
            user.setStatus(Constants.AUTO_USER_STATUS_AUTH_WAITCHECK);
            if(!autoUserService.update(user)){
                result.setCode(1);
                result.setMsg("保存实名认证信息出错！");
            }

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("保存实名认证信息出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查看会员中心
     * @param user
     * @return
     */
    @RequestMapping(value = "/index.do")
    @ResponseBody
    public Result index(AutoUser user){
        Result result=new Result();
        try{
            if(user == null || user.getUserId() == null || user.getUserId() <= 0){
                result.setCode(-1);
                result.setMsg("用户不能为空！");
                return result;
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", user.getUserId());
            AutoUser find = autoUserService.query(map);
            if (find == null) {
                result.setCode(-1);
                result.setMsg("该用户不存在！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(-1);
                result.setMsg("该账户被禁用！");
                return result;
            }

            //加载个人资料
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId",find.getUserId());
            jsonObject.put("userName",find.getUserName());
            jsonObject.put("userAvatar",find.getUserAvatar());
            jsonObject.put("point",find.getPoint());
            jsonObject.put("phone",find.getPhone());
            jsonObject.put("orderNumber", find.getOrderNumber());
            jsonObject.put("carNumber", find.getCarNumber());
            jsonObject.put("isAuth",find.getStatus() == Constants.AUTO_USER_STATUS_AUTH_SUCCESS);
            map.clear();
            map.put("userId", find.getUserId());

            AutoUserProfile profile = autoUserProfileService.query(map);
            if(profile!=null){
                jsonObject.put("profile",profile);
            }

            result.setData(jsonObject);

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("查看会员中心出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 查看我的收藏
     * @param userId
     * @param page
     * @return
     */
    @RequestMapping(value = "/listCollect.do")
    @ResponseBody
    public Result listCollect(Integer userId, Page page){
        Result result=new Result();
        try{
            if(userId == null || userId <= 0){
                result.setCode(-1);
                result.setMsg("用户不能为空！");
                return result;
            }
            AutoUser find = autoUserService.query(userId, null);
            if(find == null){
                result.setCode(-1);
                result.setMsg("用户不能存在！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(-1);
                result.setMsg("该账户被禁用！");
                return result;
            }

            Map<String, Object> map=new HashMap<String, Object>();
            map.put("userId",userId);
            List<AutoCar> carList = autoCarService.queryUserCollect(map,page);
            if(carList != null && carList.size()>0){
                JSONArray array=new JSONArray();
                for(AutoCar car:carList){
                    JSONObject object=new JSONObject();
                    object.put("collectId",car.getCollectId());
                    object.put("carId",car.getCarId());
                    object.put("carName",car.getCarName());
                    object.put("brandName",car.getBrandName());
                    object.put("catName",car.getCatName());
                    object.put("officalPrice",(car.getOfficalPrice()));
                    map.clear();
                    map.put("carPicId", car.getCarId());
                    AutoCarPic carPic=autoCarPicService.query(map);
                    if(carPic!=null){
                        object.put("pic",carPic.getPath());
                    }
                    map.clear();
                    map.put("attrCarId",car.getCarId());
                    List<AutoCarAttr> attrList=autoCarAttrService.queryList(map);
                    if(attrList!=null && attrList.size()>0){
                        List<AutoCarAttr> carAttrList=new ArrayList<AutoCarAttr>();
                        for(AutoCarAttr attrs:attrList){
                            carAttrList.add(attrs);
                        }
                        object.put("attrs",carAttrList);
                    }
                    object.put("status",car.getStatus());
                    object.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(car.getAddTime()),"MM-dd HH:mm"));
                    object.put("addUserAuth", car.getAddUserStatus() == Constants.AUTO_USER_STATUS_AUTH_SUCCESS);
                    array.add(object);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("查看我的收藏出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除我的收藏
     * @param collect
     * @return
     */
    @RequestMapping(value = "/deleteCollect.do")
    @ResponseBody
    public Result deleteCollect(AutoCollect collect){
        Result result = new Result();
        try{
            if(collect == null || collect.getCollectId() == null || collect.getCollectId() == 0){
                result.setCode(-1);
                result.setMsg("收藏不能为空！");
                return result;
            }
            if(collect.getUserId() == null || collect.getUserId() == 0){
                result.setCode(-1);
                result.setMsg("用户不能为空！");
                return result;
            }
            AutoUser find = autoUserService.query(collect.getUserId(), null);
            if(find == null){
                result.setCode(-1);
                result.setMsg("用户不能存在！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(-1);
                result.setMsg("该账户被禁用！");
                return result;
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("collectId", collect.getCollectId());
            collect = autoCollectService.query(map);
            if(collect.getUserId() != find.getUserId()){
                result.setCode(-1);
                result.setMsg("您不能删除该收藏！");
                return result;
            }

            if (!autoCollectService.delete(map)) {
                result.setCode(1);
                result.setMsg("删除车源收藏失败！");
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("删除车源收藏出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 添加我的收藏
     * @param collect
     * @return
     */
    @RequestMapping(value = "/addCollect.do")
    @ResponseBody
    public Result addCollect(AutoCollect collect){
        Result result=new Result();
        try{
            if(collect == null){
                result.setCode(-1);
                result.setMsg("请求参数错误！");
                return result;
            }
            if(collect.getUserId() == null || collect.getUserId() == 0){
                result.setCode(-1);
                result.setMsg("用户不能为空！");
                return result;
            }
            AutoUser find = autoUserService.query(collect.getUserId(), null);
            if(find == null){
                result.setCode(-1);
                result.setMsg("用户不能为空！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(-1);
                result.setMsg("该账户被禁用！");
                return result;
            }
            if(collect.getTargetId() == null || collect.getTargetId() == 0){
                result.setCode(-1);
                result.setMsg("收藏车源不能为空！");
                return result;
            }
            AutoCar car = autoCarService.query(collect.getTargetId());
            if(car == null){
                result.setCode(-1);
                result.setMsg("收藏车源不存在！");
                return result;
            }
            if(car.getStatus() == Constants.AUTO_CAR_STATUS_OFF){
                result.setCode(-1);
                result.setMsg("该车源已下架，不能收藏！");
                return result;
            }
            collect.setAddTime(ConvertUtil.toLong(new Date()));
            collect.setTargetType(Constants.AUTO_COLLECT_TYPE_CAR);
            collect.setStatus(Constants.AUTO_COLLECT_STATUS_NORMAL);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("targetId", collect.getTargetId());
            map.put("targetType", Constants.AUTO_COLLECT_TYPE_CAR);
            map.put("userId", collect.getUserId());
            AutoCollect autoCollect = autoCollectService.query(map);
            if(autoCollect != null){
                result.setCode(-1);
                result.setMsg("该车源已收藏！");
                return result;
            }

            if (!autoCollectService.insert(collect)) {
                result.setCode(1);
                result.setMsg("添加车源收藏失败！");
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("添加车源收藏出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 查看消息中心
     * @param user
     * @param page
     * @return
     */
    @RequestMapping(value = "/listMessage.do")
    @ResponseBody
    public Result listMessage(AutoUser user, Page page){
        Result result=new Result();
        try{
            if(user == null || user.getUserId() == null || user.getUserId() <= 0){
                result.setCode(-1);
                result.setMsg("用户不能为空！");
                return result;
            }
            AutoUser find = autoUserService.query(user.getUserId(), null);
            if (find == null) {
                result.setCode(-1);
                result.setMsg("该用户不存在！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(-1);
                result.setMsg("该账户被禁用！");
                return result;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("toUserId", user.getUserId());
            List<AutoMsg> msgList = autoMsgService.queryList(map, page);
            if (msgList != null && msgList.size() > 0) {
                JSONArray array = new JSONArray();
                for (AutoMsg msg : msgList) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("msgId", msg.getMsgId());
                    jsonObject.put("msgTitle", msg.getMsgTitle());
                    jsonObject.put("msgStatus", msg.getStatus());
                    jsonObject.put("addTime", ConvertUtil.toString(ConvertUtil.toDate(msg.getAddTime()), Constants.DATE_FORMAT));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("查看消息中心出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查看消息详细信息
     * @param msg
     * @return
     */
    @RequestMapping(value = "/showMessage.do")
    @ResponseBody
    public Result showMessage(AutoMsg msg){
        Result result=new Result();
        try{
            if(msg == null || msg.getMsgId() == null || msg.getMsgId() == 0){
                result.setCode(-1);
                result.setMsg("消息不能为空！");
                return result;
            }
            if(msg.getToUserId() == null || msg.getToUserId() == 0){
                result.setCode(-1);
                result.setMsg("用户不能为空！");
                return result;
            }
            AutoUser find = autoUserService.query(msg.getToUserId(), null);
            if(find == null){
                result.setCode(-1);
                result.setMsg("用户不能为空！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(-1);
                result.setMsg("该账户被禁用！");
                return result;
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("msgId", msg.getMsgId());
            msg = autoMsgService.query(map);
            if(msg == null){
                result.setCode(-1);
                result.setMsg("该消息不存在！");
                return result;
            }
            msg.setViewCount(msg.getViewCount() + 1);
            msg.setStatus(1);
            autoMsgService.update(msg);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgId", msg.getMsgId());
            jsonObject.put("msgTitle", msg.getMsgTitle());
            if (msg.getFromUserName() == null && msg.getFromRealName() == null) {
                jsonObject.put("formUserName", "系统");
            } else if (msg.getFromRealName() == null) {
                jsonObject.put("formUserName", msg.getFromUserName());
            } else if (msg.getFromUserName() != null && msg.getFromRealName() != null) {
                jsonObject.put("formUserName", msg.getFromRealName());
            }
            jsonObject.put("addTime", ConvertUtil.toString(ConvertUtil.toDate(msg.getAddTime()), "MM-dd HH:mm:ss"));
            jsonObject.put("viewCount", msg.getViewCount());
            jsonObject.put("msgContent", msg.getMsgContent());
            result.setData(jsonObject);
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("查看消息详细信息出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查看用户发布的车源
     * @param order
     * @param page
     * @return
     */
    @RequestMapping(value = "/listCar.do")
    @ResponseBody
    public Result listCar(AutoOrder order, Page page){
        Result result=new Result();
        try{
            if(order == null || order.getSellerId() == null || order.getSellerId() <= 0 || order.getClientStatus() == null){
                result.setCode(-1);
                result.setMsg("查询参数错误！");
                return result;
            }
            AutoUser find = autoUserService.query(order.getSellerId(), null);
            if (find == null) {
                result.setCode(-1);
                result.setMsg("该用户不存在！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(-1);
                result.setMsg("该账户被禁用！");
                return result;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("addUserId",order.getSellerId());

            if(order.getClientStatus().equals(Constants.CS_UNDEPOSIT)){
                String sql = " and (ao.order_id is null" +
                             " or (ao.order_status = " + Constants.OS_SUBMIT +
                             " and ao.pay_status = " + Constants.PS_WAIT_BUYER_DEPOSIT +
                             " and ao.ship_status = " + Constants.SS_UNSHIP + "))";
                map.put("filter", sql);
            }
            else if(order.getClientStatus().equals(Constants.CS_DEPOSIT)){
                String sql = " and ao.order_status in(" + Constants.OS_SUBMIT + "," + Constants.OS_EXECUTE + ") " +
                             " and ao.pay_status in(" + Constants.PS_BUYER_PAY_DEPOSIT + "," + Constants.PS_WAIT_SELLER_DEPOSIT + "," + Constants.PS_SELLER_PAY_DEPOSIT + ") " +
                             " and ao.ship_status = " + Constants.SS_UNSHIP;
                map.put("filter", sql);
            }
            else if(order.getClientStatus().equals(Constants.CS_SUCCESS)){
                String sql = " and ao.order_status  = " + Constants.OS_SUCCESS +
                             " and ao.pay_status in(" + Constants.PS_SELLER_PAY_DEPOSIT + "," + Constants.PS_WAIT_SYSTEM_DEPOSIT + "," + Constants.PS_SYSTEM_RETURN_DEPOSIT + ") " +
                             " and ao.ship_status = " + Constants.SS_SHIPED;
                map.put("filter", sql);
            }
            else if(order.getClientStatus().equals(Constants.CS_CANCEL)){
                String sql = " and ao.order_status = " + Constants.OS_CANCEL;
                map.put("filter", sql);
            }
            List<AutoCar> carList = autoCarService.queryOrderCar(map,page);
            if(carList!=null && carList.size()>0){
                JSONArray array=new JSONArray();
                for(AutoCar car:carList){
                    JSONObject object=new JSONObject();
                    object.put("collectId",car.getCollectId());
                    object.put("carId",car.getCarId());
                    object.put("orderId",car.getOrderId());
                    map.clear();
                    map.put("userId",car.getBuyerId());
                    AutoUser user=autoUserService.query(map);
                    object.put("buyerId",car.getBuyerId());
                    object.put("buyerPhone",user.getPhone());
                    object.put("sellerDeposit",0.01);
                    object.put("carName",car.getCarName());
                    object.put("brandName",car.getBrandName());
                    object.put("catName",car.getCatName());
                    object.put("officalPrice",(car.getOfficalPrice()));
                    map.clear();
                    map.put("carPicId", car.getCarId());
                    AutoCarPic carPic=autoCarPicService.query(map);
                    if(carPic!=null){
                        object.put("pic",carPic.getPath());
                    }
                    map.clear();
                    map.put("attrCarId",car.getCarId());
                    List<AutoCarAttr> attrList=autoCarAttrService.queryList(map);
                    if(attrList!=null && attrList.size()>0){
                        List<AutoCarAttr> carAttrList=new ArrayList<AutoCarAttr>();
                        for(AutoCarAttr attrs:attrList){
                            carAttrList.add(attrs);
                        }
                        object.put("attrs",carAttrList);
                    }
                    object.put("status",car.getStatus());
                    object.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(car.getAddTime()),"MM-dd HH:mm"));
                    object.put("addUserAuth", car.getAddUserStatus() == Constants.AUTO_USER_STATUS_AUTH_SUCCESS);


                    List<String> operate = new ArrayList<String>();
                    if(car.getOrderStatus() != null && car.getOrderPayStatus() != null && car.getOrderShipStatus() != null){
                        order = new AutoOrder();
                        order.setOrderStatus(car.getOrderStatus());
                        order.setPayStatus(car.getOrderPayStatus());
                        order.setShipStatus(car.getOrderShipStatus());
                        operate = autoOrderService.queryOperate(order, Constants.ORDER_USER_SELLER);
                    }
                    object.put("operate", operate);
                    array.add(object);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("查看我的销车出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 功能反馈
     * @param feedback
     * @return
     */
    @RequestMapping(value = "/feedback.do")
    @ResponseBody
    public Result feedback(AutoFeedback feedback){
        Result result=new Result();
        try{
            if(StringUtil.isNullOrEmpty(feedback.getContent())){
                result.setCode(-1);
                result.setMsg("反馈意见不能为空！");
                return result;
            }
            AutoUser find = autoUserService.query(feedback.getAddUserId(), null);
            if (find == null) {
                result.setCode(-1);
                result.setMsg("该用户不存在！");
                return result;
            }
            if(find.getStatus() == Constants.AUTO_USER_STATUS_DISABLED){
                result.setCode(-1);
                result.setMsg("该账户被禁用！");
                return result;
            }
            feedback.setAddTime(ConvertUtil.toLong(new Date()));
            if(!autoFeedbackService.insert(feedback)){
                result.setCode(1);
                result.setMsg("提交反馈意见失败！");
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("提交反馈意见出错！");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}