package puzzle.lianche.controller.admin;

import com.gexin.rp.sdk.http.utils.Constant;
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
import puzzle.lianche.entity.AutoBrandModel;
import puzzle.lianche.service.IAutoBrandCatService;
import puzzle.lianche.service.IAutoBrandModelService;
import puzzle.lianche.service.IAutoBrandService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminAutoBrandModelController")
@RequestMapping (value = "/admin/autobrandmodel")
public class AutoBrandModelController extends ModuleController {

    @Autowired
    private IAutoBrandModelService autoBrandModelService;

    @Autowired
    private IAutoBrandCatService autoBrandCatService;

    @Autowired
    private IAutoBrandService autoBrandService;

    @RequestMapping (value = {"/index"})
    public String index(){
        this.showActions();

        List<AutoBrand> brandList = autoBrandService.queryList(null);
        this.setModelAttribute("brandList",brandList);

        return Constants.UrlHelper.ADMIN_AUTO_BRAND_MODEL;
    }

    @RequestMapping (value = "/index/{catId}")
    public String index2(@PathVariable String catId){
        this.showActions();

        List<AutoBrand> brandList = autoBrandService.queryList(null);
        this.setModelAttribute("brandList",brandList);

        if(StringUtil.isNotNullOrEmpty(catId) && StringUtil.isNumber(catId)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("catId", ConvertUtil.toInt(catId));

            AutoBrandCat cat = autoBrandCatService.query(map);
            this.setModelAttribute("cat", cat);

            map.clear();
            map.put("brandId", cat.getBrandId());

            AutoBrand brand = autoBrandService.query(map);
            this.setModelAttribute("brand", brand);

            List<AutoBrandCat> catList = autoBrandCatService.queryList(map);
            this.setModelAttribute("catList", catList);
        }

        return Constants.UrlHelper.ADMIN_AUTO_BRAND_MODEL;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoBrandModel autoBrandModel,Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoBrandModel != null) {
                if(StringUtil.isNotNullOrEmpty(autoBrandModel.getModelName())) {
                    map.put("modelName", autoBrandModel.getModelName());
                }
                if (!autoBrandModel.getCatString().equalsIgnoreCase("null")) {
                    map.put("catId", ConvertUtil.toInt(autoBrandModel.getCatString()));
                }
                if (autoBrandModel.getBrandId() != null && autoBrandModel.getBrandId()>0 ) {
                    map.put("brandId", autoBrandModel.getBrandId());
                }
                if (autoBrandModel.getBrandCatId() != null && autoBrandModel.getBrandCatId() >0) {
                    map.put("catId", autoBrandModel.getBrandCatId());
                }
            }
            List<AutoBrandModel> list=autoBrandModelService.queryList(map,page);
            if(list != null && list.size() > 0){
                JSONArray array=new JSONArray();
                for(AutoBrandModel brandModel:list){
                    JSONObject jsonObject=JSONObject.fromObject(brandModel);
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("获取车型信息失败");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoBrandModel autoBrandModel){
        Result result = new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!autoBrandModelService.insert(autoBrandModel)){
                    result.setCode(1);
                    result.setMsg("添加车型信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加车型信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!autoBrandModelService.update(autoBrandModel)){
                    result.setCode(1);
                    result.setMsg("修改车型信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改车型信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String, Object> map=new HashMap<String, Object>();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("modelId", ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] modelIds=ids.split(",");
                    map.put("modelIds",modelIds);
                }
                if(!autoBrandModelService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除车型信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除车型信息");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作车型信息失败");
        }
        return result;
    }

    @RequestMapping (value = "/queryBrandCat.do")
    @ResponseBody
    public Result queryBrandCat(Integer brandId){
        Result result = new Result();
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            if(brandId != null && brandId > 0){
                map.put("brandId", brandId);
            }
            List<AutoBrandCat> list = autoBrandCatService.queryList(map);
            result.setCode(0);
            result.setData(list);
        }
        catch (Exception e){
            e.printStackTrace();
            result.setCode(Constants.ResultHelper.RESULT_HANDLE_ERROR);
            result.setMsg("查询车源信息出错");

        }
        return result;
    }
}
