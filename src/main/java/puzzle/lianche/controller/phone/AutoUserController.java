package puzzle.lianche.controller.phone;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.entity.AutoUser;
import puzzle.lianche.push.SmsPush;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.EncryptUtil;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.*;
import java.lang.Object;

@Controller(value = "phoneAutoUserController")
@RequestMapping(value = "/phone/autouser")
public class AutoUserController extends BaseController {

    @Autowired
    private IAutoUserService autoUserService;
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
                result.setMsg("手机号不能为空！");
            }
            if(StringUtil.isNullOrEmpty(password)){
                result.setCode(-1);
                result.setMsg("密码不能为空！");
            }

            Map<String,Object> map = new HashMap<String, Object>();
            map.put("username", username);
            map.put("password", EncryptUtil.MD5(password));
            AutoUser user = autoUserService.query(map);
            if(user != null){
                //加载个人资料
                result.setData(user);
            }else{
                result.setCode(1);
                result.setMsg("用户名或密码不正确！");
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("会员登录验证出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

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
            }
            if(StringUtil.isNullOrEmpty(keyword)){
                result.setCode(-1);
                result.setMsg("请求参数错误！");
            }

            String code = "";

            Map<String,Object> map = new HashMap<String, Object>();

            map.put("code", code);
            String response = SmsPush.send(phone, 1, map);
            if(SmsPush.isSuccess(response)){
                result.setData(code);
            }else{
                result.setCode(1);
                result.setMsg(SmsPush.getError(response));
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("发送短信验证码出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}