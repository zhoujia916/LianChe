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
import puzzle.lianche.entity.AutoBrand;
import puzzle.lianche.entity.AutoBrandCat;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.IAutoBrandCatService;
import puzzle.lianche.service.IAutoBrandService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminAutoBrandCatController")
@RequestMapping (value = "/admin/autobrandcat")
public class AutoBrandCatController extends ModuleController {

    @Autowired
    private IAutoBrandService autoBrandService;

    @Autowired
    private IAutoBrandCatService autoBrandCatService;

    @RequestMapping (value = { "/index" })
    public String index(){
        this.showActions();

        List<AutoBrand> brandList = autoBrandService.queryList(null);
        this.setModelAttribute("brandList", brandList);

        return Constants.UrlHelper.ADMIN_AUTO_BRAND_CAT;
    }

    @RequestMapping (value = { "/index/{brandId}" })
    public String index2(@PathVariable String brandId){
        this.showActions();

        List<AutoBrand> brandList = autoBrandService.queryList(null);
        this.setModelAttribute("brandList", brandList);

        this.setModelAttribute("brandId", brandId);

        return Constants.UrlHelper.ADMIN_AUTO_BRAND_CAT;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoBrandCat autoBrandCat,Page page){
        Result result = new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoBrandCat != null) {
                if (autoBrandCat.getBrandId() != null && autoBrandCat.getBrandId() > 0) {
                    map.put("brandId", autoBrandCat.getBrandId());
                }
                if(StringUtil.isNotNullOrEmpty(autoBrandCat.getCatName())) {
                    map.put("catName", autoBrandCat.getCatName());
                }
            }
            List<AutoBrandCat> list= autoBrandCatService.queryList(map, page);
            result.setData(list);
            result.setTotal(page.getTotal());
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取车系信息失败");
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoBrandCat autoBrandCat){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!autoBrandCatService.insert(autoBrandCat)){
                    result.setCode(1);
                    result.setMsg("添加车系信息出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加车系信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!autoBrandCatService.update(autoBrandCat)){
                    result.setCode(1);
                    result.setMsg("修改车薪信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改车系信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String, Object> map=new HashMap<String, Object>();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("catId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] catIds=ids.split(",");
                    map.put("catIds",catIds);
                }
                if(!autoBrandCatService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除车系信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除车系信息");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作车系信息失败");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
