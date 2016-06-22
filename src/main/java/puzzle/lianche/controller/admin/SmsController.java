package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.*;
import puzzle.lianche.push.SmsPush;
import puzzle.lianche.service.IAutoSmsService;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminSmsController")
@RequestMapping (value = "/admin/sms")
public class SmsController extends ModuleController{

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
        return Constants.UrlHelper.ADMIN_SMS;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(){
        Result result=new Result();
        try{
            Map map=new HashMap();
            map.put("smsType",request.getParameter("smsType"));
            if(request.getParameter("status")!=null && request.getParameter("status")!=""){
                map.put("status", ConvertUtil.toInt(request.getParameter("status")));
            }
            String pageIndex=request.getParameter("pageIndex");
            String pageSize=request.getParameter("pageSize");
            Page page=new Page();
            page.setPageIndex(ConvertUtil.toInt(pageIndex));
            page.setPageSize(ConvertUtil.toInt(pageSize));
            List<AutoSms> list=autoSmsService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoSms sms:list){
                    JSONObject jsonObject=JSONObject.fromObject(sms);
                    jsonObject.put("smsType",Constants.AUTO_SMS_TYPE.get(sms.getSmsType()));
                    jsonObject.put("status",Constants.AUTO_SMS_STATUS.get(sms.getStatus()));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取短信信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoSms autoSms){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                Map map=new HashMap();
                map.put("userId",autoSms.getToUserId());
                List<AutoUser> userList=autoUserService.queryList(map);
                List<String> phone=new ArrayList<String>();
                for(AutoUser user:userList){
                    phone.add(user.getPhone());
                }
                //调用接口，将返回的结果放到isSuccess里去判断是否发送成功
                List<AutoSms> list=new ArrayList<AutoSms>();
                for(String smsPhone:phone){
                    map.clear();
                    map.put("content",autoSms.getSmsContent());
                    if(SmsPush.isSuccess(SmsPush.send(SmsPush.CODE_SENDMSG, smsPhone, "你该还钱了！"))){
                        AutoSms sms=new AutoSms();
                        sms.setSmsType(Constants.DEFAULT_SMS_TYPE);
                        sms.setStatus(Constants.SMS_STATUS_TRUE);
                        sms.setPhone(smsPhone);
                        sms.setSmsContent(autoSms.getSmsContent());
                        list.add(sms);
                    }else{
                        AutoSms sms=new AutoSms();
                        sms.setSmsType(Constants.DEFAULT_SMS_TYPE);
                        sms.setStatus(Constants.SMS_STATUS_FALSE);
                        sms.setPhone(smsPhone);
                        sms.setSmsContent(autoSms.getSmsContent());
                        list.add(sms);
                    }
                }
                if(!autoSmsService.insertBatch(list)){
                    result.setCode(1);
                    result.setMsg("添加短信信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE, "添加短信信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map map=new HashMap();
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
                    result.setMsg("删除短信信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除短信信息");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作短信信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
