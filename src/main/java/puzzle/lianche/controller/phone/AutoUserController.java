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
    private IAutoOrderCarService autoOrderCarService;

    @Autowired
    private IAutoOrderService autoOrderService;

    @Autowired
    private IAutoFeedbackService autoFeedbackService;

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
            result.setMsg("发送短信验证码出错！");
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
                        autoUser.setBirth(ConvertUtil.toLong(new Date()));
                        autoUser.setSortOrder(0);
                        if(autoUserService.insert(autoUser)){
                            JSONArray array=new JSONArray();
                            JSONObject jsonObject=new JSONObject();
                            jsonObject.put("userId",autoUser.getUserId());
                            array.add(jsonObject);
                            result.setData(array);
                        }else{
                            result.setCode(1);
                            result.setMsg("注册失败！");
                        }
                    }
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错！");
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
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("userId",user.getUserId());
                    jsonObject.put("userName",user.getUserName());
                    jsonObject.put("userAvatar",user.getUserAvatar());
                    jsonObject.put("points",user.getPoint());
                    jsonObject.put("phone",user.getPhone());
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
            result.setMsg("会员登录验证出错！");
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
    public Result retrievePassWord(AutoUser autoUser){
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
                            result.setMsg("修改新密码失败！");
                        }
                    }
                }else{
                    result.setCode(1);
                    result.setMsg("不存在该用户！");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作找回密码时出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 修改密码
     * @param userId
     * @param oldPassWord
     * @param newPassWord
     * @return
     */
    @RequestMapping(value = "/updatePassWord.do")
    @ResponseBody
    public Result updatePassWord(Integer userId,String oldPassWord,String newPassWord){
        Result result=new Result();
        try{
            if(StringUtil.isNullOrEmpty(oldPassWord)){
                result.setCode(-1);
                result.setMsg("原密码不能为空！");
            }else if(oldPassWord.length()<6 || oldPassWord.length()>20){
                result.setCode(-1);
                result.setMsg("原密码长度必须在6-20范围内！");
            }else if(StringUtil.isNullOrEmpty(newPassWord)){
                result.setCode(-1);
                result.setMsg("新密码不能而空！");
            }else if(newPassWord.length()<6 || newPassWord.length()>20) {
                result.setCode(-1);
                result.setMsg("新密码长度必须在6-20范围内！");
            }else{
                String password= EncryptUtil.MD5(oldPassWord);
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("userId",userId);
                AutoUser autoUser=autoUserService.query(map);
                if(password.equalsIgnoreCase(autoUser.getPassword())){
                    AutoUser user=new AutoUser();
                    user.setPassword(EncryptUtil.MD5(newPassWord));
                    user.setUserId(userId);
                    if(!autoUserService.update(user)){
                        result.setCode(1);
                        result.setMsg("修改密码出错！");
                    }
                }else{
                    result.setCode(1);
                    result.setMsg("原密码错误！");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作修改密码时出错！");
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
                        result.setMsg("完善个人信息失败！");
                    }
                }else{
                    autoUserProfile.setProfileId(userProfile.getProfileId());
                    if(!autoUserProfileService.update(autoUserProfile)){
                        result.setCode(1);
                        result.setMsg("完善个人信息失败！");
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

    /**
     * 查看会员中心
     * @param user
     * @return
     */
    @RequestMapping(value = "/memberCenter.do")
    @ResponseBody
    public Result memberCenter(AutoUser user){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("userId",user.getUserId());
            AutoUser autoUser=autoUserService.query(map);
            if(autoUser!=null){
                //加载个人资料
                JSONArray array=new JSONArray();
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("points",autoUser.getPoint());
                jsonObject.put("orderNumber",autoUser.getOrderNumber());
                jsonObject.put("carNumber",autoUser.getCarNumber());
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

    /**
     * 查看用户收藏
     * @param user
     * @return
     */
    @RequestMapping(value = "/collection.do")
    @ResponseBody
    public Result collection(AutoUser user,Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("userId",ConvertUtil.toString(user.getUserId()));
            List<AutoCollect> autoCollectList=autoCollectService.queryList(map);
            if(autoCollectList!=null && autoCollectList.size()>0){
                List<Integer> carIdList=new ArrayList<Integer>();
                for(AutoCollect autoCollect:autoCollectList ){
                    carIdList.add(autoCollect.getTargetId());
                }
                map.clear();
                map.put("carIdList",carIdList);
                List<AutoCar> carList=autoCarService.queryList(map,page);
                if(carList!=null && carList.size()>0){
                    JSONArray array=new JSONArray();
                    for(int i=0;i<carList.size();i++){
                        JSONObject jsonObject=new JSONObject();
                        map.clear();
                        map.put("carPicId", carList.get(i).getCarId());
                        AutoCarPic carPic=autoCarPicService.query(map);
//                        map.clear();
//                        map.put("byCarId",carList.get(i).getCarId());
//                        List<AutoCarAttr> carAttrList=autoCarAttrService.queryList(map);
                        jsonObject.put("pic",carPic.getPath());
//                        if(carAttrList.size()>1){
//                            jsonObject.put("attrValue","多色");
//                        }else{
//                            jsonObject.put("attrValue",carAttrList.get(i).getAttrValue());
//                        }
                        jsonObject.put("attrValue","珍珠白");
                        map.clear();
                        map.put("userId",carList.get(i).getAddUserId());
                        AutoUser autoUser=autoUserService.query(map);
                        jsonObject.put("carId",carList.get(i).getCarId());
                        jsonObject.put("carName",carList.get(i).getCarName());
                        jsonObject.put("titleName",carList.get(i).getCatName());
                        jsonObject.put("officalPrice",carList.get(i).getOfficalPrice());
                        jsonObject.put("quoteType",carList.get(i).getQuoteType());
                        jsonObject.put("quotePrice",carList.get(i).getSaleAmount());
                        jsonObject.put("status",carList.get(i).getStatus());
                        jsonObject.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(autoCollectList.get(i).getAddTime()),"MM-dd HH:mm"));
                        if(autoUser.getStatus()==Constants.AUTO_USER_STATUS_AUTHENTICATIONADOPT){
                            jsonObject.put("isAuthenticate",true);
                        }else{
                            jsonObject.put("isAuthenticate",false);
                        }
                        array.add(jsonObject);
                    }
                    result.setData(array);
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("查看我的收藏出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 删除我的收藏
     * @param carId
     * @return
     */
    @RequestMapping(value = "/deleteCollection.do")
    @ResponseBody
    public Result deleteCollection(Integer carId){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("carId",carId);
            if(!autoCollectService.delete(map)){
                result.setCode(1);
                result.setMsg("删除我的收藏失败！");
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作删除我的收藏出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 查看消息中心
     * @param user
     * @return
     */
    @RequestMapping(value = "/messageCount.do")
    @ResponseBody
    public Result messageCount(AutoUser user){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("toUserId",user.getUserId());
            List<AutoMsg> msgList=autoMsgService.queryList(map);
            if(msgList!=null && msgList.size()>0){
                JSONArray array=new JSONArray();
                for(AutoMsg msg:msgList){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("msgId",msg.getMsgId());
                    jsonObject.put("msgTitle",msg.getMsgTitle());
                    jsonObject.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(msg.getAddTime()),Constants.DATE_FORMAT));
                    array.add(jsonObject);
                }
                result.setData(array);
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("查看消息中心出错！");
        }
        return result;
    }

    /**
     * 查看消息详细信息
     * @param msgId
     * @return
     */
    @RequestMapping(value = "/message.do")
    @ResponseBody
    public Result message(Integer msgId){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("msgId",msgId);
            AutoMsg msg=autoMsgService.query(map);
            if(msg!=null){
                msg.setViewCount(msg.getViewCount()+1);
                autoMsgService.update(msg);
                JSONArray array=new JSONArray();
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("msgId",msg.getMsgId());
                jsonObject.put("msgTitle",msg.getMsgTitle());
                if(msg.getFromUserName()==null && msg.getFromRealName()==null){
                    jsonObject.put("formUserName","系统");
                }else if(msg.getFromRealName()==null){
                    jsonObject.put("formUserName",msg.getFromUserName());
                }else if(msg.getFromUserName()!=null && msg.getFromRealName()!=null){
                    jsonObject.put("formUserName",msg.getFromRealName());
                }
                jsonObject.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(msg.getAddTime()),"MM-dd HH:mm:ss"));
                jsonObject.put("viewCount",msg.getViewCount());
                jsonObject.put("msgContent",msg.getMsgContent());
                array.add(jsonObject);
                result.setData(array);
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("查看消息详细信息出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 查看我的销车
     * @param order
     * @param page
     * @return
     */
    @RequestMapping(value = "/carSource.do")
    @ResponseBody
    public Result carSource(AutoOrder order,Page page){
        Result result=new Result();
        try{
            if(order==null){
                result.setCode(-1);
                result.setMsg("查看我的销车失败！");
            }else if(page==null){
                result.setCode(-1);
                result.setMsg("查看我的销车失败！");
            }else{
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("userId",order.getSellerId());
                if(order.getClientStatus().equals(Constants.CS_UNDEPOSIT)){
                    String sql = " ao.order_id is null"
                            + " or (ao.order_status = " + Constants.OS_SUBMIT
                            + " and ao.pay_status = " + Constants.PS_WAIT_BUYER_DEPOSIT
                            + " and ao.ship_status = " + Constants.SS_SHIPED + ")";
                    map.put("filter", sql);
                }
                else if(order.getClientStatus().equals(Constants.CS_DEPOSIT)){
                    map.put("orderStatusList",
                            Constants.OS_SUBMIT + "," +
                                    Constants.OS_EXECUTE);
                    map.put("payStatusList",
                            Constants.PS_BUYER_PAY_DEPOSIT + "," +
                                    Constants.PS_WAIT_SELLER_DEPOSIT + "," +
                                    Constants.PS_SELLER_PAY_DEPOSIT);
                    map.put("shipStatus", Constants.SS_UNSHIP);
                }
                else if(order.getClientStatus().equals(Constants.CS_SUCCESS)){
                    map.put("orderStatus", Constants.OS_SUCCESS);
                    map.put("payStatusList",
                            Constants.PS_SELLER_PAY_DEPOSIT+ "," +
                                    Constants.PS_WAIT_RETURN_DEPOSIT+ "," +
                                    Constants.PS_SYSTEM_RETURN_DEPOSIT);
                    map.put("shipStatus", Constants.SS_SHIPED);
                }
                else if(order.getClientStatus().equals(Constants.CS_CANCEL)){
                    map.put("orderStatus", Constants.OS_CANCEL);
                }
                List<AutoCar> carList = autoCarService.queryOrderList(map, page);
                if(carList!=null && carList.size()>0){
                    List<Integer> carIdList=new ArrayList<Integer>();
                    List<Integer> orderIdList=new ArrayList<Integer>();
                    for(AutoCar car:carList ){
                        carIdList.add(car.getCarId());
                    }
                    map.clear();
                    map.put("carIdList",carIdList);
                    List<AutoOrderCar> orderCarList=autoOrderCarService.queryList(map);
                    for(AutoOrderCar orderCar:orderCarList){
                        orderIdList.add(orderCar.getOrderId());
                    }
                    map.clear();
                    map.put("orderIdList",orderIdList);
                    List<AutoOrder> orderList=autoOrderService.queryList(map);
                    if(carList!=null && carList.size()>0){
                        JSONArray array=new JSONArray();
                        for(int i=0;i<carList.size();i++){
                            JSONObject jsonObject=new JSONObject();
                            map.clear();
                            map.put("carPicId", carList.get(i).getCarId());
                            AutoCarPic carPic=autoCarPicService.query(map);
//                            map.clear();
//                            map.put("carAttrId",carList.get(i).getCarId());
//                            List<AutoCarAttr> carAttrList=autoCarAttrService.queryList(map);
                            jsonObject.put("pic",carPic.getPath());
//                            if(carAttrList.size()>1){
//                                jsonObject.put("attrValue","多色");
//                            }else{
//                                jsonObject.put("attrValue",carAttrList.get(i).getAttrValue());
//                            }
                            jsonObject.put("attrValue","珍珠白");
                            map.clear();
                            map.put("userId",order.getSellerId());
                            AutoUser autoUser=autoUserService.query(map);
                            jsonObject.put("carId",carList.get(i).getCarId());
                            jsonObject.put("carName",carList.get(i).getCarName());
                            jsonObject.put("titleName",carList.get(i).getCatName()+" "+carList.get(i).getModelName());
                            jsonObject.put("officalPrice",carList.get(i).getOfficalPrice());
                            jsonObject.put("quoteType",carList.get(i).getQuoteType());
                            jsonObject.put("quotePrice",carList.get(i).getSaleAmount());
                            jsonObject.put("status",carList.get(i).getStatus());
                            for(int j=0;j<orderCarList.size();j++){
                                if(carList.get(i).getCarId()==orderCarList.get(j).getCarId()) {
                                    jsonObject.put("addTime", ConvertUtil.toString(ConvertUtil.toDate(orderList.get(i).getAddTime()), "MM-dd HH:mm"));
                                    break;
                                }
                            }
                            if(autoUser.getStatus()==Constants.AUTO_USER_STATUS_AUTHENTICATIONADOPT){
                                jsonObject.put("isAuthenticate",true);
                            }else{
                                jsonObject.put("isAuthenticate",false);
                            }
                            array.add(jsonObject);
                        }
                        result.setData(array);
                    }
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("查看我的销车出错！");
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
            }else{
                feedback.setAddTime(ConvertUtil.toLong(new Date()));
                if(!autoFeedbackService.insert(feedback)){
                    result.setCode(1);
                    result.setMsg("功能反馈失败！");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作功能反馈出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}