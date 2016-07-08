package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.entity.SystemRole;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.service.ISystemRoleService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "adminSystemRoleController")
@RequestMapping(value = "/admin/systemrole")
public class SystemRoleController extends ModuleController {

    @Autowired
    private ISystemRoleService systemRoleService;

    @Autowired
    private ISystemMenuActionService systemMenuActionService;

    /**
     * 进入到角色index页面
     * @return
     */
    @RequestMapping(value = {"/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_SYSTEM_ROLE;
    }

    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(SystemRole systemRole,Page page){
        Result result=new Result();
        try{
            insertLog(Constants.PageHelper.PAGE_ACTION_SELECT,"查看角色信息");
            Map<String, Object> map=new HashMap<String, Object>();
            if(systemRole!=null){
                if(systemRole.getRoleType()!=null && systemRole.getRoleType()>0) {
                    map.put("roleType", systemRole.getRoleType());
                }
                if(StringUtil.isNotNullOrEmpty(systemRole.getRoleName())) {
                    map.put("roleName", systemRole.getRoleName());
                }
            }
            List<SystemRole> list=systemRoleService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(SystemRole role:list){
                    JSONObject jsonObject=JSONObject.fromObject(role);
                    jsonObject.put("roleTypeName",Constants.MAP_ROLE_TYPE.get(role.getRoleType()));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("业务出错");
            logger.error(result.getMsg()+e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/action.do")
    @ResponseBody
    public Result action(String action,SystemRole systemRole){
        Result result=new Result();
        Map<String, Object> map=new HashMap<String, Object>();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                systemRole.setRoleType(Constants.DEFAULT_ROLE_TYPE);
                if(!systemRoleService.insert(systemRole)){
                    result.setCode(1);
                    result.setMsg("添加角色出错");
                }
                else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加角色信息");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!systemRoleService.update(systemRole)){
                    result.setCode(1);
                    result.setMsg("修改角色出错");
                }
                else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改指定的角色信息");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("roleId", ConvertUtil.toInt(id));
                }
                else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] roleIds=ids.split(",");
                    map.put("roleIds",roleIds);
                }
                if(!systemRoleService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除角色失败");
                }
                else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除指定的角色信息");
                }
            }
            else{
                result.setCode(-1);
                result.setMsg("参数出错");
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作角色信息失败");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
