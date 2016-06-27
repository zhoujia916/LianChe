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

    @RequestMapping (value = {"/","/index"})
    public String modelIndex(){
        Map<String, Object> map=new HashMap<String, Object>();
        List<AutoBrandCat> catList=autoBrandCatService.queryList(null);
        if(catList.size()>0){
            StringBuffer str=new StringBuffer();
            for(int i=0;i<catList.size();i++){
                str.append(catList.get(i).getBrandId());
                if(catList.size()-i>1){
                    str.append(",");
                }
            }
            map.put("brandId",str.toString());
        }
        List<AutoBrand> brandList=autoBrandService.queryList(map);
        List<AutoBrandCat> list=autoBrandCatService.queryList(null);
        this.setModelAttribute("catList",catList);
        this.setModelAttribute("brandList",brandList);
        this.setModelAttribute("list",list);
        return Constants.UrlHelper.ADMIN_AUTO_BRAND_MODEL;
    }

    @RequestMapping (value = "/index/{catId}")
    public String show(@PathVariable String catId){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("catId",catId);
        List<AutoBrandCat> catList=autoBrandCatService.queryList(map);
        if(catList.size()>0){
            map.clear();
            StringBuffer str=new StringBuffer();
            for(int i=0;i<catList.size();i++){
                str.append(catList.get(i).getBrandId());
                if(catList.size()-i>1){
                    str.append(",");
                }
            }
            map.put("brandId",str.toString());
        }
        List<AutoBrand> brandList=autoBrandService.queryList(map);
        List<AutoBrandCat> list=autoBrandCatService.queryList(null);
        this.setModelAttribute("catList",catList);
        this.setModelAttribute("brandList",brandList);
        this.setModelAttribute("list",list);
        this.setModelAttribute("catId",catId);
        return Constants.UrlHelper.ADMIN_AUTO_BRAND_MODEL;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result modelList(AutoBrandModel autoBrandModel){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("modelName",autoBrandModel.getModelName());
            String pageIndex=request.getParameter("pageIndex");
            String pageSize=request.getParameter("pageSize");
            Page page = new Page();
            page.setPageIndex(ConvertUtil.toInt(pageIndex));
            page.setPageSize(ConvertUtil.toInt(pageSize));
            if(!autoBrandModel.getCatString().equalsIgnoreCase("null")){
                map.put("catId",autoBrandModel.getCatString());
            }
            if(autoBrandModel.getBrands()!=null && autoBrandModel.getBrands()!=""){
                map.put("brandId",autoBrandModel.getBrands());
            }
            if(autoBrandModel.getCats()!=null && autoBrandModel.getCats()!=""){
                map.put("catId",autoBrandModel.getCats());
            }
            List<AutoBrandModel> list=autoBrandModelService.queryList(map,page);
            if(list!=null && list.size()>0){
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
            result.setMsg("获取车型信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoBrandModel autoBrandModel){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!autoBrandModelService.insert(autoBrandModel)){
                    result.setCode(1);
                    result.setMsg("添加车型信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加车型信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!autoBrandModelService.update(autoBrandModel)){
                    result.setCode(1);
                    result.setMsg("修改车型信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改特定的车型信息");
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
                    result.setMsg("删除车型信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除特定的车型信息");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作车型信息时出错");
        }
        return result;
    }
}