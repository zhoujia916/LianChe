package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.*;
import puzzle.lianche.service.IAutoMsgService;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.*;

@Controller (value = "adminMsgController")
@RequestMapping (value = "/admin/msg")
public class MsgController extends ModuleController {

    @Autowired
    private IAutoMsgService autoMsgService;

    @Autowired
    private IAutoUserService autoUserService;

    @Autowired
    private ISystemMenuActionService systemMenuActionService;

    @RequestMapping (value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        List<AutoUser> list=autoUserService.queryList(null);
        this.setModelAttribute("userList",list);
        return Constants.UrlHelper.ADMIN_AUTO_MSG;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoMsg autoMsg){
        Result result=new Result();
        try{
            Map map=new HashMap();
            String pageIndex=request.getParameter("pageIndex");
            String pageSize=request.getParameter("pageSize");
            Page page = new Page();
            page.setPageIndex(ConvertUtil.toInt(pageIndex));
            page.setPageSize(ConvertUtil.toInt(pageSize));
            map.put("toUserName",autoMsg.getToUserName());
            map.put("msgType",autoMsg.getMsgType());
            map.put("status",autoMsg.getStatus());
            if("系统".equalsIgnoreCase(autoMsg.getFromUserName())){
                map.put("fromUserName",null);
            }else{
                map.put("fromUserName",autoMsg.getFromUserName());
            }
            if(autoMsg.getBeginTimeString()!=null && autoMsg.getBeginTimeString()!=""){
                map.put("beginTime",ConvertUtil.toLong(ConvertUtil.toDate(autoMsg.getBeginTimeString() + " 00:00:00")));
            }
            if(autoMsg.getEndTimeString()!=null && autoMsg.getEndTimeString()!=""){
                map.put("endTime",ConvertUtil.toLong(ConvertUtil.toDate(autoMsg.getEndTimeString()+" 23:59:59")));
            }
            List<AutoMsg> list=autoMsgService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoMsg msg:list){
                    if(msg.getFromUserName()==null && msg.getFromRealName()==null){
                        msg.setFromUserName("系统");
                        msg.setFromRealName("系统");
                    }
                    JSONObject jsonObject=JSONObject.fromObject(msg);
                    jsonObject.put("msgType",Constants.AUTO_MSG_TYPE.get(msg.getMsgType()));
                    jsonObject.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(msg.getAddTime())));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("获取消息信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoMsg autoMsg){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                List<AutoMsg> list=new ArrayList<AutoMsg>();
                String[] toUserIds=autoMsg.getToUserIds().split(",");
                for(String toUserId:toUserIds){
                    AutoMsg msg=new AutoMsg();
                    msg.setMsgTitle(autoMsg.getMsgTitle());
                    msg.setMsgAuthor(autoMsg.getMsgAuthor());
                    msg.setMsgContentType(autoMsg.getMsgContentType());
                    msg.setMsgContent(autoMsg.getMsgContent());
                    msg.setMsgType(0);
                    msg.setFromUserId(0);
                    msg.setStatus(0);
                    msg.setAddTime(ConvertUtil.toLong(new Date()));
                    msg.setViewCount(0);
                    if(autoMsg.getMsgContentType()==2){
                        msg.setMsgUrl(autoMsg.getMsgContent());
                    }
                    msg.setToUserId(ConvertUtil.toInt(toUserId));
                    list.add(msg);
                }
                if(!autoMsgService.insertBatch(list)){
                    result.setCode(1);
                    result.setMsg("添加消息记录时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"新增消息记录");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map map=new HashMap();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("msgId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] msgIds=ids.split(",");
                    map.put("msgIds",msgIds);
                }
                if(!autoMsgService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除消息信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除指定的消息记录信息");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作消息信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
