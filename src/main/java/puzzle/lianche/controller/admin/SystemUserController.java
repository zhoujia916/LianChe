package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.*;
import puzzle.lianche.service.*;
import puzzle.lianche.utils.*;

import java.util.*;


@Controller(value="adminSystemUserControll")
@RequestMapping(value = "/admin/systemuser")
public class SystemUserController extends ModuleController {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemRoleService systemRoleService;

    @Autowired
    private ISystemUserGroupService systemUserGroupService;

    @Autowired
    private ISystemDeptService systemDeptService;

    @Autowired
    private ISystemMenuActionService systemMenuActionService;

    @RequestMapping(value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        List<SystemRole> roleList=systemRoleService.queryList(null);
        List<SystemUserGroup> userGroupList=systemUserGroupService.queryList(null);
        List<SystemDept> deptList=systemDeptService.queryList(null);
        this.setModelAttribute("userGroupList",userGroupList);
        this.setModelAttribute("deptList",addSubDept(deptList,0,"select"));
        this.setModelAttribute("roleList",roleList);
        return Constants.UrlHelper.ADMIN_SYSTEM_USER;
    }

    /**
     * 查询systemuser信息
     * @return
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(SystemUser systemUser,Page page){
        Result result=new Result();
        StringBuffer str=new StringBuffer();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(systemUser!=null){
                if(systemUser.getRoleId()!=null && ConvertUtil.toInt(systemUser.getRoleId())>0){
                    map.put("roleId",ConvertUtil.toInt(systemUser.getRoleId()));
                }
                if(systemUser.getGroupId()!=null && ConvertUtil.toInt(systemUser.getGroupId())>0){
                    map.put("groupId",ConvertUtil.toInt(systemUser.getGroupId()));
                }
                if(systemUser.getDeptId()!=null && ConvertUtil.toInt(systemUser.getDeptId())>0){
                    map.put("deptId",ConvertUtil.toInt(systemUser.getDeptId()));
                }
                if(systemUser.getUserName()!=null && systemUser.getUserName()!=""){
                    map.put("userName",systemUser.getUserName());
                }
            }
            List<SystemUser> list=systemUserService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(SystemUser user:list){
                    JSONObject jsonObject=JSONObject.fromObject(user);
                    jsonObject.put("sexName",Constants.MAP_USER_SEX.get(user.getSex()));
                    if(user.getRoles().size()>0){
                        for(int i=0;i<user.getRoles().size();i++){
                            str.append(user.getRoles().get(i).getRoleName());
                            if(user.getRoles().size()-i>1){
                                str.append("、");
                            }
                        }
                        jsonObject.put("roleName",str.toString());
                        str.setLength(0);
                    }
                    else{
                        jsonObject.put("roleName","");
                    }
                    if(user.getGroups().size()>0){
                        for(int i=0;i<user.getGroups().size();i++){
                            str.append(user.getGroups().get(i).getGroupName());
                            if(user.getGroups().size()-i>1){
                                str.append("、");
                            }
                        }
                        jsonObject.put("groupName",str.toString());
                        str.setLength(0);
                    }
                    else{
                        jsonObject.put("groupName","");
                    }
                    if(user.getDepts().size()>0){
                        for(int i=0;i<user.getDepts().size();i++){
                            str.append(user.getDepts().get(i).getDeptName());
                            if(user.getDepts().size()-i>1){
                                str.append("、");
                            }
                        }
                        jsonObject.put("deptName",str.toString());
                        str.setLength(0);
                    }
                    else{
                        jsonObject.put("deptName","");
                    }
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("查询用户信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/action.do")
    @ResponseBody
    public Result action(String action,SystemUser systemUser){
        Result result=new Result();
        Map<String, Object> map=new HashMap<String, Object>();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                systemUser.setPassword(EncryptUtil.MD5(systemUser.getPassword()));
                systemUser.setBirthday(ConvertUtil.toLong(new Date()));
                systemUser.setUserType(Constants.DEFAULT_USER_TYPE);
                if(!systemUserService.insert(systemUser)){
                    result.setCode(1);
                    result.setMsg("添加用户信息出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE, "添加用户信息");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(systemUser.getPassword()!=null && systemUser.getPassword()!=""){
                    systemUser.setPassword(EncryptUtil.MD5(systemUser.getPassword()));
                }
                if(!systemUserService.update(systemUser)){
                    result.setCode(1);
                    result.setMsg("修改用户信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE, "修改指定的用户信息");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("userId", ConvertUtil.toInt(id));
                }
                else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] userIds=ids.split(",");
                    map.put("userIds",userIds);
                }
                if(!systemUserService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除用户信息失败");
                }
                else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE, "删除特定的用户信息");
                }
            }
            else{
                result.setCode(-1);
                result.setMsg("参数出错");
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作用户信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    //region
    public List<SystemDept> addSubDept(List<SystemDept> list, int   parentId, String type){
        List<SystemDept> newList = new ArrayList<SystemDept>();
        for (SystemDept item : list) {
            if (item.getParentId() == parentId) {
                if(type.equalsIgnoreCase("select")){
                    int level = getLevel(list, item);
                    if(level == 0){
                        item.setDeptName("|-" + item.getDeptName());
                    }else{
                        item.setDeptName("|-" + StringUtil.padLeft(level * 2, '-') + item.getDeptName());
                    }
                    newList.add(item);
                    List<SystemDept> children = addSubDept(list, item.getDeptId(), type);
                    for(int i = 0; i < children.size(); i++){
                        newList.add(children.get(i));
                    }
                } else if (type.equalsIgnoreCase("list")) {
                    newList.add(item);
                    List<SystemDept> children = addSubDept(list, item.getDeptId(), type);
                    for(int i = 0; i < children.size(); i++){
                        newList.add(children.get(i));
                    }
                }
                else if (type.equalsIgnoreCase("tree")) {
                    List<SystemDept> children = addSubDept(list, item.getDeptId(), type);
                    item.setChildren(children);
                    newList.add(item);
                }

            }
        }
        return newList;
    }

    public int getLevel(List<SystemDept> list, SystemDept dept){
        if(dept.getParentId() == 0){
            return 0;
        }
        int level = 0;
        boolean hasParent = true;
        while(hasParent){
            hasParent = false;
            for (SystemDept item : list) {
                if (dept.getParentId() == item.getDeptId()) {
                    level++;
                    dept = item;
                    hasParent = true;
                    break;
                }
            }
        }
        return level;
    }
    //endregion
}
