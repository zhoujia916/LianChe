package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.AutoAttr;
import puzzle.lianche.entity.SystemAuthority;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.entity.SystemUser;
import puzzle.lianche.service.IAutoAttrService;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value="adminAttrController")
@RequestMapping (value="/admin/attr")
public class AttrController extends ModuleController {

    @Autowired
    private IAutoAttrService autoAttrService;

    @Autowired
    private ISystemMenuActionService systemMenuActionService;

    @RequestMapping(value = {"/","/index"})
    public String index()
    {
        if(getCurrentUser() != null){
            SystemUser user = (SystemUser)getCurrentUser();
            if(user.getAuthorities() != null) {
                String url = request.getRequestURI().replace(request.getContextPath() + "/admin/", "");
                int menuId = 0;
                List<Integer> actionIds = new ArrayList<Integer>();
                for (SystemAuthority authority : user.getAuthorities()) {
                    if(authority.getMenuUrl()!=null && authority.getMenuUrl()!="null"){
                        if(authority.getMenuUrl().equals(url) && authority.getTargetType() == 1){
                            menuId = authority.getTargetId();
                            break;
                        }
                    }
                }
                for (SystemAuthority item : user.getAuthorities()) {
                    if(item.getTargetType() == 2){
                        actionIds.add(item.getTargetId());
                    }
                }
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("menuId", menuId);
                map.put("actionIds", actionIds);
                List<SystemMenuAction> actions = systemMenuActionService.queryList(map);
                this.setModelAttribute("actions", actions);
            }
        }
        return Constants.UrlHelper.ADMIN_ATTR;
    }

    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(AutoAttr autoAttr){
        Result result=new Result();
        try{
            Map map=new HashMap();
            map.put("attrName",autoAttr.getAttrName());
            String pageIndex=request.getParameter("pageIndex");
            String pageSize=request.getParameter("pageSize");
            Page page = new Page();
            page.setPageIndex(ConvertUtil.toInt(pageIndex));
            page.setPageSize(ConvertUtil.toInt(pageSize));
            if(autoAttr.getTypeString()!=null && autoAttr.getTypeString()!=""){
                map.put("typeString",autoAttr.getTypeString());
            }
            List<AutoAttr> list=autoAttrService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoAttr attr:list){
                    JSONObject jsonObject=JSONObject.fromObject(attr);
                    jsonObject.put("attrInputTypeName",Constants.AUTO_ATTR_TYPE.get(attr.getAttrInputType()));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取车源属性信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoAttr autoAttr){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!autoAttrService.insert(autoAttr)){
                    result.setCode(1);
                    result.setMsg("添加车源属性信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加车源属性信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!autoAttrService.update(autoAttr)){
                    result.setCode(1);
                    result.setMsg("修改车源属性信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改车源属性信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map map=new HashMap();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("attrId", ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] attrIds=ids.split(",");
                    map.put("attrIds",attrIds);
                }
                if(!autoAttrService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除车源属性信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除特定的车源属性信息");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作车源属性信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
