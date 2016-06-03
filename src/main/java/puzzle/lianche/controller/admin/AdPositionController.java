package puzzle.lianche.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.AutoAdPosition;
import puzzle.lianche.entity.SystemAuthority;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.entity.SystemUser;
import puzzle.lianche.service.IAutoAdPositionService;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "admin/adposition")
@RequestMapping (value = "/admin/position")
public class AdPositionController extends ModuleController{
    @Autowired
    private IAutoAdPositionService autoAdPositionService;

    @Autowired
    private ISystemMenuActionService systemMenuActionService;

    @RequestMapping (value = {"/","/index"})
    public String index(){
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
        return Constants.UrlHelper.ADMIN_ADPOSITION;
    }

    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(AutoAdPosition autoAdPosition){
        Result result=new Result();
        try{
            Map map=new HashMap();
            map.put("positionName",autoAdPosition.getPositionName());
            String pageIndex=request.getParameter("pageIndex");
            String pageSize=request.getParameter("pageSize");
            Page page = new Page();
            page.setPageIndex(ConvertUtil.toInt(pageIndex));
            page.setPageSize(ConvertUtil.toInt(pageSize));
            List<AutoAdPosition> list=autoAdPositionService.queryList(map,page);
            if(list!=null && list.size()>0){
                result.setData(list);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取广告位置信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoAdPosition autoAdPosition){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!autoAdPositionService.insert(autoAdPosition)){
                    result.setCode(1);
                    result.setMsg("插入广告位置信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"��ӹ��λ����Ϣ");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!autoAdPositionService.update(autoAdPosition)){
                    result.setCode(1);
                    result.setMsg("修改广告位置信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"�޸�ָ���Ĺ��λ����Ϣ");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map map=new HashMap();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("positionId", ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] positionIds=ids.split(",");
                    map.put("positionIds",positionIds);
                }
                if(!autoAdPositionService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除广告位置信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"ɾ���ض��Ĺ��λ����Ϣ");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作广告位置信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
