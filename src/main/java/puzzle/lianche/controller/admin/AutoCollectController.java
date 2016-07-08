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
import puzzle.lianche.entity.AutoCollect;
import puzzle.lianche.entity.AutoUser;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.IAutoCollectService;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.service.impl.AutoUserServiceImpl;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminAutoCollectController")
@RequestMapping (value = "/admin/autocollect")
public class AutoCollectController extends ModuleController {

    @Autowired
    private IAutoCollectService autoCollectService;

    @Autowired
    private IAutoUserService autoUserService;

    @RequestMapping (value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_AUTO_COLLECT;
    }

    @RequestMapping (value = "/index/{userId}")
    public String show(@PathVariable String userId){
       List<SystemMenuAction> actions = getActions();
       Map map=new HashMap();
       map.put("userId",ConvertUtil.toInt(userId));
       AutoUser user=autoUserService.query(map);
       this.setModelAttribute("actions", actions);
       this.setModelAttribute("userId",userId);
       this.setModelAttribute("userName",user.getUserName());
       return Constants.UrlHelper.ADMIN_AUTO_COLLECT;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoCollect autoCollect, Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoCollect != null) {
                if(StringUtil.isNotNullOrEmpty(autoCollect.getCarName())) {
                    map.put("carName", autoCollect.getCarName());
                }
                if(StringUtil.isNotNullOrEmpty(autoCollect.getUserName())) {
                    map.put("userName", autoCollect.getUserName());
                }
                if (StringUtil.isNotNullOrEmpty(autoCollect.getUserString())) {
                    map.put("userId", ConvertUtil.toInt(autoCollect.getUserString()));
                }
            }
            List<AutoCollect> list=autoCollectService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoCollect collect:list){
                    JSONObject jsonObject=JSONObject.fromObject(collect);
                    jsonObject.put("targetType",Constants.MAP_AUTO_COLLECT_TYPE.get(collect.getTargetType()));
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
                Map<String, Object> map=new HashMap<String, Object>();
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
                    result.setMsg("删除用户收藏信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除用户收藏信息");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作用户收藏信息失败");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
