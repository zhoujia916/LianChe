package puzzle.lianche.controller.admin;

import com.fasterxml.jackson.databind.util.BeanUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.*;
import puzzle.lianche.service.*;
import puzzle.lianche.utils.CommonUtil;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value="adminAuthorityController")
@RequestMapping(value = {"/admin/authority"})
public class AuthorityController extends ModuleController {

    @Autowired
    private ISystemAuthorityService systemAuthorityService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemRoleService systemRoleService;

    @Autowired
    private ISystemDeptService systemDeptService;

    @Autowired
    private ISystemUserMapService systemUserMapService;

    @Autowired
    private ISystemMenuService systemMenuService;

    @Autowired
    private ISystemMenuActionService systemMenuActionService;

    @Autowired
    private ISystemUserGroupService systemUserGroupService;

    @RequestMapping(value = {"/", "/index"})
    public String index(){
        List<SystemUser> users = systemUserService.queryList(null);
        List<SystemRole> roles = systemRoleService.queryList(null);
        List<SystemDept> depts = systemDeptService.queryList(null);
        List<SystemUserGroup> groups = systemUserGroupService.queryList(null);
//        List<SystemAuthority> authorities = systemAuthorityService.queryList(null);
//        List<SystemUserMap> maps = systemUserMapService.queryList(null);
//        List<SystemMenu> menus = systemMenuService.queryList(null);
//        List<SystemMenuAction> actions = systemMenuActionService.queryList(null);


        this.setModelAttribute("userList", users);
        this.setModelAttribute("roleList", roles);
        this.setModelAttribute("deptList", depts);
        this.setModelAttribute("groupList", groups);
//        this.setModelAttribute("authorityList", authorities);
//        this.setModelAttribute("mapList", maps);
//        this.setModelAttribute("menuList", menus);
//        this.setModelAttribute("actionList", actions);

        return Constants.UrlHelper.ADMIN_AUTHORITY;
    }

    @RequestMapping(value = {"/show/{id}"})
    public String show(@PathVariable Integer id){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("authorityId", id);
        SystemAuthority authority = systemAuthorityService.query(map);
        this.setModelAttribute("authority", authority);
        return Constants.UrlHelper.ADMIN_AUTHORITY;
    }

    @RequestMapping(value = { "/list.do" })
    @ResponseBody
    public Result list(){
        Result result = new Result();
        Map<String,Object> map = new HashMap<String, Object>();
        if(StringUtil.isNotNullOrEmpty(request.getParameter("sourceId"))){
            map.put("sourceId", ConvertUtil.toInt(request.getParameter("sourceId")));
        }
        if(StringUtil.isNotNullOrEmpty(request.getParameter("sourceType"))){
            map.put("sourceType", ConvertUtil.toInt(request.getParameter("sourceType")));
        }
        try {
            List<SystemAuthority> list = systemAuthorityService.queryList(map);
            result.setData(list);
        }
        catch (Exception e){
            result.setCode(1);
            result.setMsg("获取授权信息出错！");
            logger.error("获取授权信息出错: " + e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = {"/action.do"})
    @ResponseBody
    public Result action(String action, SystemAuthority authority){
        Result result = new Result();
        try {
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!systemAuthorityService.insert(authority)){
                    result.setCode(1);
                    result.setMsg("保存授权信息失败！");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!systemAuthorityService.update(authority)){
                    result.setCode(1);
                    result.setMsg("保存授权信息失败！");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_SAVE)){
                if(!systemAuthorityService.save(authority)){
                    result.setCode(1);
                    result.setMsg("保存授权信息失败！");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String,Object> map = new HashMap<String, Object>();
                if(StringUtil.isNotNullOrEmpty(request.getParameter("id"))){
                    map.put("authorityId", request.getParameter("id"));
                }
                else if(StringUtil.isNotNullOrEmpty(request.getParameter("ids"))){
                    map.put("authorityIds", request.getParameter("ids"));
                }
                if(!systemAuthorityService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除授权信息失败！");
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
