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
import puzzle.lianche.entity.SystemAuthority;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.entity.SystemUser;
import puzzle.lianche.entity.SystemUserGroup;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.service.ISystemUserGroupService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "adminUserGroupController")
@RequestMapping(value = "/admin/usergroup")
public class UserGroupController  extends ModuleController {

    @Autowired
    private ISystemUserGroupService userGroupService;

    @Autowired
    private ISystemMenuActionService systemMenuActionService;

    @RequestMapping(value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_USER_GROUP;
    }

    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(){
        Result result=new Result();
        try{
            insertLog(Constants.PageHelper.PAGE_ACTION_SELECT, "查看用户组信息");
            Map map=new HashMap();
            map.put("groupName",request.getParameter("groupName"));
            String pageIndex=request.getParameter("pageIndex");
            String pageSize=request.getParameter("pageSize");
            Page page = new Page();
            page.setPageIndex(ConvertUtil.toInt(pageIndex));
            page.setPageSize(ConvertUtil.toInt(pageSize));
            List<SystemUserGroup> userGroupList=userGroupService.queryList(map,page);
            if(userGroupList!=null && userGroupList.size()>0){
                JSONArray array=new JSONArray();
                for(SystemUserGroup userGroup:userGroupList){
                    JSONObject jsonObject=JSONObject.fromObject(userGroup);
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("查看用户组信息时出错");
            logger.error(result.getMsg());
        }
        return result;
    }

    @RequestMapping(value = "/action.do")
    @ResponseBody
    public Result action(String action,SystemUserGroup systemUserGroup){
        Result result=new Result();
        Map map=new HashMap();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!userGroupService.insert(systemUserGroup)){
                    result.setCode(1);
                    result.setMsg("添加用户组出错");
                }
                else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加用户组信息");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!userGroupService.update(systemUserGroup)){
                    result.setCode(1);
                    result.setMsg("修改用户组出错");
                }
                else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改指定的用户组信息");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("groupId", ConvertUtil.toInt(id));
                }
                else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] groupIds=ids.split(",");
                    map.put("groupIds",groupIds);
                }
                if(!userGroupService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除用户组失败");
                }
                else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除指定的用户组信息");
                }
            }
            else{
                result.setCode(-1);
                result.setMsg("参数出错");
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作用户组信息时出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
