package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.SystemLog;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.ISystemLogService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "adminSystemLogControll")
@RequestMapping(value = "/admin/systemlog")
public class SystemLogController extends ModuleController{

    @Autowired
    private ISystemLogService systemLogService;

    @RequestMapping(value = {"/","/index"})
    public String index()
    {
        List<SystemMenuAction> actions=getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_SYSTEM_LOG;
    }

    /**
     * 查看日志信息
     * @param systemLog
     * @return
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(SystemLog systemLog,Page page)
    {
        Result result=new Result();
        try{
            insertLog(Constants.PageHelper.PAGE_ACTION_SELECT,"查看日志信息");
            Map<String, Object> map=new HashMap<String, Object>();
            if(systemLog!=null) {
                if(StringUtil.isNotNullOrEmpty(systemLog.getUserName())){
                    map.put("userName",systemLog.getUserName());
                }
                if(systemLog.getLogIp()!=null && systemLog.getLogIp()!=""){
                    map.put("logIp", systemLog.getLogIp());
                }
                if (systemLog.getLogType() != null && systemLog.getLogType()>0) {
                    map.put("logType", systemLog.getLogType());
                }
                if (StringUtil.isNotNullOrEmpty(systemLog.getBeginTimeString())) {
                    map.put("beginTime", ConvertUtil.toLong(ConvertUtil.toDateTime(systemLog.getBeginTimeString() + " 00:00:00")));
                }
                if (StringUtil.isNotNullOrEmpty(systemLog.getEndTimeString())) {
                    map.put("endTime", ConvertUtil.toLong(ConvertUtil.toDateTime(systemLog.getEndTimeString() + " 23:59:59")));
                }
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
        Map<String, Object> map=new HashMap<String, Object>();
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
            result.setMsg("操作日志信息失败");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
