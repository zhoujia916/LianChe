package puzzle.lianche.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.SystemMenu;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.service.ISystemMenuService;
import puzzle.lianche.utils.CommonUtil;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value="adminSystemMenuController")
@RequestMapping(value = {"/admin/systemmenu"})
public class SystemMenuController extends ModuleController {

    @Autowired
    private ISystemMenuService systemMenuService;
    @Autowired
    private ISystemMenuActionService systemMenuActionService;


    @RequestMapping(value = {"/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_SYSTEM_MENU;
    }

    @RequestMapping(value = { "/tree.do" })
    @ResponseBody
    public Result tree(){
        Result result = new Result();
        Map<String,Object> map = new HashMap<String, Object>();
        if(StringUtil.isNotNullOrEmpty(request.getParameter("parentId"))){
            map.put("parentId", ConvertUtil.toInt(request.getParameter("parentId")));
        }
        try {
            List<SystemMenu> menus = systemMenuService.queryList(map);
            if(menus != null && menus.size() > 0) {
                List<SystemMenuAction> actions = systemMenuActionService.queryList(null);
                for(SystemMenu menu : menus){
                    for(SystemMenuAction action : actions){
                        if(menu.getMenuId() == action.getMenuId()){
                            if(menu.getActions() == null){
                                menu.setActions(new ArrayList<SystemMenuAction>());
                            }
                            menu.getActions().add(action);
                        }
                    }
                }
                menus = CommonUtil.showMenuTree(menus, 0);
                result.setData(menus);
            }
        }
        catch (Exception e){
            result.setCode(1);
            result.setMsg("获取菜单信息出错！");
            logger.error("获取菜单信息出错: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
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
            List<SystemMenu> menus = systemMenuService.queryList(map);
            if(menus != null && menus.size() > 0) {
                for(SystemMenu menu : menus){
                    map.clear();
                    map.put("menuId", menu.getMenuId());
                    menu.setActions(systemMenuActionService.queryList(map));
                    if(menu.getActions() != null && menu.getActions().size() > 0){
                        StringBuffer code = new StringBuffer();
                        StringBuffer name = new StringBuffer();
                        for(SystemMenuAction action : menu.getActions()){
                            if(code.length() == 0){
                                code.append(action.getActionCode());
                                name.append(action.getActionName());
                            }else{
                                code.append(",");
                                name.append(",");
                                code.append(action.getActionCode());
                                name.append(action.getActionName());
                            }
                        }
                        menu.setActionCode(code.toString());
                        menu.setActionName(name.toString());
                    }
                }
                result.setData(menus);
            }
        }
        catch (Exception e){
            result.setCode(1);
            result.setMsg("获取菜单信息出错！");
            logger.error("获取菜单信息出错: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = {"/action.do"})
    @ResponseBody
    public Result action(String action, SystemMenu menu){
        Result result = new Result();
        try {
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)) {
                if (!systemMenuService.insert(menu)) {
                    result.setCode(1);
                    result.setMsg("保存菜单信息失败！");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!systemMenuService.update(menu)){
                    result.setCode(1);
                    result.setMsg("保存菜单信息失败！");
                }
            }
            else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String,Object> map = new HashMap<String, Object>();
                if(StringUtil.isNotNullOrEmpty(request.getParameter("id"))){
                    map.put("menuId", request.getParameter("id"));
                }
                else if(StringUtil.isNotNullOrEmpty(request.getParameter("ids"))){
                    map.put("menuIds", request.getParameter("ids"));
                }
                if(!systemMenuService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除菜单信息失败！");
                }
            }
        }
        catch (Exception e){
            result.setCode(-1);
            result.setMsg("系统处理出错！");
            logger.error("系统处理出错: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    //region
    public List<SystemMenu> addSubMenu(List<SystemMenu> list, int parentId, String type){
        List<SystemMenu> newList = new ArrayList<SystemMenu>();
        for (SystemMenu item : list) {
            if (item.getParentId() == parentId) {
                if(type.equalsIgnoreCase("select")){
                    int level = getLevel(list, item);
                    if(level == 0){
                        item.setMenuName("|-" + item.getMenuName());
                    }else{
                        item.setMenuName("|-" + StringUtil.padLeft(level * 2, "-") + item.getMenuName());
                    }
                    newList.add(item);
                    List<SystemMenu> children = addSubMenu(list, item.getMenuId(), type);
                    for(int i = 0; i < children.size(); i++){
                        newList.add(children.get(i));
                    }
                } else if (type.equalsIgnoreCase("list")) {
                    newList.add(item);
                    List<SystemMenu> children = addSubMenu(list, item.getMenuId(), type);
                    for(int i = 0; i < children.size(); i++){
                        newList.add(children.get(i));
                    }
                }
                else if (type.equalsIgnoreCase("tree")) {
                    List<SystemMenu> children = addSubMenu(list, item.getMenuId(), type);
                    item.setChildren(children);
                    newList.add(item);
                }

            }
        }
        return newList;
    }

    public int getLevel(List<SystemMenu> list, SystemMenu menu){
        if(menu.getParentId() == 0){
            return 0;
        }
        int level = 0;
        boolean hasParent = true;
        while(hasParent){
            hasParent = false;
            for (SystemMenu item : list) {
                if (menu.getParentId() == item.getMenuId()) {
                    level++;
                    menu = item;
                    hasParent = true;
                    break;
                }
            }
        }
        return level;
    }
    //endregion
}
