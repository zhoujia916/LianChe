package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.SystemAuthority;
import puzzle.lianche.entity.SystemLog;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.entity.SystemUser;
import puzzle.lianche.service.ISystemLogService;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "adminLogControll")
@RequestMapping(value = "/admin/log")
public class LogController extends ModuleController{

    @Autowired
    private ISystemLogService systemLogService;

    @RequestMapping(value = {"/","/index"})
    public String index()
    {
        List<SystemMenuAction> actions=getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_LOG;
    }

    /**
     * 查看日志信息
     * @param systemLog
     * @return
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(SystemLog systemLog)
    {
        Result result=new Result();
        try{
            insertLog(Constants.PageHelper.PAGE_ACTION_SELECT,"查看日志信息");
            Map map=new HashMap();
            map.put("logIp",systemLog.getLogIp());
            String pageIndex=request.getParameter("pageIndex");
            String pageSize=request.getParameter("pageSize");
            Page page = new Page();
            page.setPageIndex(ConvertUtil.toInt(pageIndex));
            page.setPageSize(ConvertUtil.toInt(pageSize));
            if(systemLog.getLogTypeId()!=null && systemLog.getLogTypeId()!=""){
                map.put("logType",systemLog.getLogTypeId());
            }
            if(systemLog.getBeginTimeString()!=null && systemLog.getBeginTimeString()!=""){
                map.put("beginTime",ConvertUtil.toLong(ConvertUtil.toDate(systemLog.getBeginTimeString()+" 00:00:00")));
            }
            if(systemLog.getEndTimeString()!=null && systemLog.getEndTimeString()!=""){
                map.put("endTime",ConvertUtil.toLong(ConvertUtil.toDate(systemLog.getEndTimeString()+" 23:59:59")));
            }
            List<SystemLog> list=systemLogService.queryList(map,page);
            if(list!=null&&list.size()>0){
                JSONArray array=new JSONArray();
                for(SystemLog log:list){
                    JSONObject jsonObject=JSONObject.fromObject(log);
                    jsonObject.put("logType",Constants.MAP_LOG_TYPE.get(log.getLogType()));
                    //得到时间
                    jsonObject.put("logTime",ConvertUtil.toString(ConvertUtil.toDate(log.getLogTime())));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取日志信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/action.do")
    @ResponseBody
    public Result action(String action){
        Result result=new Result();
        Map map=new HashMap();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("logId",ConvertUtil.toInt(id));
                }
                else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] logIds=ids.split(",");
                    map.put("logIds",logIds);
                }
                if(!systemLogService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除日志信息失败");
                }
                else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除指定的日志信息");
                }
            }else{
                result.setCode(-1);
                result.setMsg("参数出错");
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作日志信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
