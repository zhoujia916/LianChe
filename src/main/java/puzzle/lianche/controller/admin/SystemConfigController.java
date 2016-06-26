package puzzle.lianche.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.SystemConfig;
import puzzle.lianche.service.ISystemConfigService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value="adminSystemConfigController")
@RequestMapping(value = {"/admin/systemconfig"})
public class SystemConfigController extends ModuleController {

    @Autowired
    private ISystemConfigService systemConfigService;


    @RequestMapping(value = {"/", "/index"})
    public String index(){
        return Constants.UrlHelper.ADMIN_SYSTEM_CONFIG;
    }

    @RequestMapping(value = {"/show/{id}"})
    public String show(@PathVariable Integer id){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("configId", id);
        SystemConfig config = systemConfigService.query(map);
        this.setModelAttribute("config", config);

        return Constants.UrlHelper.ADMIN_SYSTEM_CONFIG;
    }

    @RequestMapping(value = { "/list.do" })
    @ResponseBody
    public Result list(){
        Result result = new Result();
        Map<String,Object> map = new HashMap<String, Object>();
        if(StringUtil.isNotNullOrEmpty(request.getParameter("parentId"))){
            map.put("parentId", ConvertUtil.toInt(request.getParameter("parentId")));
        }
        try {
            List<SystemConfig> list = systemConfigService.queryList(map);
            if(list != null && list.size() > 0) {
                result.setData(list);
            }
        }
        catch (Exception e){
            result.setCode(1);
            result.setMsg("获取配置信息出错！");
            logger.error("获取配置信息出错: " + e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = {"/action.do"})
    @ResponseBody
    public Result action(String action, SystemConfig config){
        Result result = new Result();
        try {
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!systemConfigService.insert(config)){
                    result.setCode(1);
                    result.setMsg("保存配置信息失败！");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!systemConfigService.update(config)){
                    result.setCode(1);
                    result.setMsg("保存配置信息失败！");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String,Object> map = new HashMap<String, Object>();
                if(StringUtil.isNotNullOrEmpty(request.getParameter("id"))){
                    map.put("configId", request.getParameter("id"));
                }
                else if(StringUtil.isNotNullOrEmpty(request.getParameter("ids"))){
                    map.put("configIds", request.getParameter("ids"));
                }
                if(!systemConfigService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除配置信息失败！");
                }
            }
        }
        catch (Exception e){
            result.setCode(-1);
            result.setMsg("系统处理出错！");
            logger.error("系统处理出错: " + e.getMessage());
        }

        return result;
    }

    //endregion
}
