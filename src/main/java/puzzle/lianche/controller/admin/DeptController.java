package puzzle.lianche.controller.admin;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.SystemAuthority;
import puzzle.lianche.entity.SystemDept;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.entity.SystemUser;
import puzzle.lianche.service.ISystemDeptService;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value="adminDeptController")
@RequestMapping(value = {"/admin/dept"})
public class DeptController extends ModuleController {

    @Autowired
    private ISystemDeptService systemDeptService;

    @Autowired
    private ISystemMenuActionService systemMenuActionService;


    @RequestMapping(value = {"/", "/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_DEPT;
    }

    @RequestMapping(value = {"/show/{id}"})
    public String show(@PathVariable Integer id){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("deptId", id);
        SystemDept dept = systemDeptService.query(map);
        this.setModelAttribute("dept", dept);
        return Constants.UrlHelper.ADMIN_DEPT;
    }

    @RequestMapping(value = { "/list.do" })
    @ResponseBody
    public Result list(){
        Result result = new Result();
        Map<String,Object> map = new HashMap<String, Object>();
        if(StringUtil.isNotNullOrEmpty(request.getParameter("parentId"))){
            map.put("parentId", ConvertUtil.toInt(request.getParameter("parentId")));
        }
        if(StringUtil.isNotNullOrEmpty(request.getParameter("deptName"))){
            map.put("deptName", request.getParameter("deptName"));
        }
        try {
            List<SystemDept> list = systemDeptService.queryList(map);
            if(list != null && list.size() > 0) {
                result.setData(list);
            }
        }
        catch (Exception e){
            result.setCode(1);
            result.setMsg("获取部门信息出错！");
            logger.error("获取部门信息出错: " + e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = {"/action.do"})
    @ResponseBody
    public Result action(String action, SystemDept dept){
        Result result = new Result();
        try {
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!systemDeptService.insert(dept)){
                    result.setCode(1);
                    result.setMsg("保存部门信息失败！");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(systemDeptService.update(dept)){
                    JSONObject item = JSONObject.fromObject(dept);
                    item.put("isLeaf", false);
                    item.put("expanded", true);
                    item.put("loaded", true);
                    result.setData(item);
                }else{
                    result.setCode(1);
                    result.setMsg("保存部门信息失败！");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String,Object> map = new HashMap<String, Object>();
                if(StringUtil.isNotNullOrEmpty(request.getParameter("id"))){
                    map.put("deptId", request.getParameter("id"));
                }
                else if(StringUtil.isNotNullOrEmpty(request.getParameter("ids"))){
                    String[] deptIds=request.getParameter("ids").split(",");
                    map.put("deptIds", deptIds);
                }
                if(!systemDeptService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除部门信息失败！");
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
}
