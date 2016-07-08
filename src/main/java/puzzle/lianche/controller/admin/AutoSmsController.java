package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.AutoSms;
import puzzle.lianche.entity.AutoUser;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.push.SmsPush;
import puzzle.lianche.service.IAutoSmsService;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminAutoSmsController")
@RequestMapping (value = "/admin/autosms")
public class AutoSmsController extends ModuleController{

    @Autowired
    private IAutoSmsService autoSmsService;

    @Autowired
    private IAutoUserService autoUserService;

    @RequestMapping(value = {"/","/index"})
    public String index()
    {
        List<AutoUser> list=autoUserService.queryList(null);
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        this.setModelAttribute("userList",list);
        return Constants.UrlHelper.ADMIN_AUTO_SMS;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoSms autoSms,Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoSms!=null) {
                if(autoSms.getSmsType()!=null && autoSms.getSmsType()>0) {
                    map.put("smsType", autoSms.getSmsType());
                }
                if(autoSms.getStatus()!=null && autoSms.getStatus()>0){
                    map.put("status", autoSms.getStatus());
                }
            }
            List<AutoSms> list=autoSmsService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoSms sms:list){
                    JSONObject jsonObject=JSONObject.fromObject(sms);
                    jsonObject.put("smsType",Constants.MAP_AUTO_SMS_TYPE.get(sms.getSmsType()));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取短信信息失败");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoSms autoSms){
        Result result=new Result();
        try{
//            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
//                Map<String, Object> map=new HashMap<String, Object>();
//                map.put("userId",autoSms.getToUserId());
//                List<AutoUser> userList=autoUserService.queryList(map);
//                List<String> phone=new ArrayList<String>();
//                for(AutoUser user:userList){
//                    phone.add(user.getPhone());
//                }
//                //调用接口，将返回的结果放到isSuccess里去判断是否发送成功
//                List<AutoSms> list=new ArrayList<AutoSms>();
//                for(String smsPhone:phone){
//                    map.clear();
//                    map.put("content",autoSms.getSmsContent());
//                    String response=SmsPush.send(SmsPush.CODE_SENDMSG, smsPhone, "您有一笔订单等待收货确认，订单号为-" + "11111111111111" + "，请尽快处理");
//                    if(SmsPush.isSuccess(response)){
//                        AutoSms sms=new AutoSms();
//                        sms.setSmsType(Constants.DEFAULT_SMS_TYPE);
//                        sms.setStatus(Constants.SMS_STATUS_TRUE);
//                        sms.setPhone(smsPhone);
//                        sms.setSmsContent(autoSms.getSmsContent());
//                        list.add(sms);
//                    }else{
//                        AutoSms sms=new AutoSms();
//                        sms.setSmsType(Constants.DEFAULT_SMS_TYPE);
//                        sms.setStatus(Constants.SMS_STATUS_FALSE);
//                        sms.setPhone(smsPhone);
//                        sms.setSmsContent(autoSms.getSmsContent());
//                        list.add(sms);
//                    }
//                }
//                if(!autoSmsService.insertBatch(list)){
//                    result.setCode(1);
//                    result.setMsg("添加短信信息失败");
//                }else{
//                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE, "添加短信信息");
//                }
//            }else
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String, Object> map=new HashMap<String, Object>();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("smsId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] smsIds=ids.split(",");
                    map.put("smsIds",smsIds);
                }
                if(!autoSmsService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除短信信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除短信信息");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作短信信息失败");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
