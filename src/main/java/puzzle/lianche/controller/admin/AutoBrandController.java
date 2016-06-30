package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.AutoBrand;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.IAutoBrandService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminAutoBrandController")
@RequestMapping (value = "/admin/autobrand")
public class AutoBrandController extends ModuleController {

    @Autowired
    private IAutoBrandService autoBrandService;

    @RequestMapping (value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_AUTO_BRAND;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoBrand autoBrand,Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoBrand!=null){
                if(StringUtil.isNotNullOrEmpty(autoBrand.getBrandName())){
                    map.put("brandName",autoBrand.getBrandName());
                }
            }
            List<AutoBrand> list=autoBrandService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoBrand brand:list){
                    JSONObject jsonObject=JSONObject.fromObject(brand);
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取品牌信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoBrand autoBrand){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                //新增
                if(!autoBrandService.insert(autoBrand)){
                    result.setCode(1);
                    result.setMsg("添加品牌信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加品牌信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                //修改
                if(!autoBrandService.update(autoBrand)){
                    result.setCode(1);
                    result.setMsg("修改品牌信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"修改品牌信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String, Object> map=new HashMap<String, Object>();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("brandId", id);
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] brandIds=ids.split(",");
                    map.put("brandIds",brandIds);
                }
                if(!autoBrandService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除品牌信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除特定的品牌信息");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作品牌信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
