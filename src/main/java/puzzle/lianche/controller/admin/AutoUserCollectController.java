package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.AutoCollect;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.IAutoCollectService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminAutoUserCollectController")
@RequestMapping (value = "/admin/usercollect")
public class AutoUserCollectController extends ModuleController {

    @Autowired
    private IAutoCollectService autoCollectService;

    @RequestMapping (value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_AUTO_USER_COLLECTION;
    }

    @RequestMapping (value = "/index/{userId}")
    public String show(@PathVariable String userId){
       this.setModelAttribute("userId",userId);
       return Constants.UrlHelper.ADMIN_AUTO_USER_COLLECTION;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoCollect autoCollect){
        Result result=new Result();
        try{
            Map map=new HashMap();
            map.put("carName",autoCollect.getCarName());
            map.put("userName",autoCollect.getUserName());
            String pageIndex=request.getParameter("pageIndex");
            String pageSize=request.getParameter("pageSize");
            Page page = new Page();
            page.setPageIndex(ConvertUtil.toInt(pageIndex));
            page.setPageSize(ConvertUtil.toInt(pageSize));
            if(!autoCollect.getUserString().equalsIgnoreCase("null")){
                map.put("userId",autoCollect.getUserString());
            }
            List<AutoCollect> list=autoCollectService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoCollect collect:list){
                    JSONObject jsonObject=JSONObject.fromObject(collect);
                    jsonObject.put("targetType",Constants.AUTO_COLLECT_TYPE.get(collect.getTargetType()));
                    jsonObject.put("addTime", ConvertUtil.toString(ConvertUtil.toDate(collect.getAddTime())));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("获取用户收藏信息出错");
            logger.error(e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoCollect autoCollect){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map map=new HashMap();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("collectId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] collectIds=ids.split(",");
                    map.put("collectIds",collectIds);
                }
                if(!autoCollectService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除用户收藏信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除特定的用户收藏信息");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作用户收藏信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
