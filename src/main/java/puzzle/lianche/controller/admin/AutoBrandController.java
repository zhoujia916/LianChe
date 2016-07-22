package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.controller.plugin.uploader.PathFormatter;
import puzzle.lianche.entity.AutoBrand;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.IAutoBrandService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminAutoBrandController")
@RequestMapping (value = "/admin/autobrand")
public class AutoBrandController extends ModuleController {

    @Autowired
    private IAutoBrandService autoBrandService;

    @RequestMapping (value = {"/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_AUTO_BRAND;
    }

    @RequestMapping(value = "/add")
    public String add(){
        this.setModelAttribute("action",Constants.PageHelper.PAGE_ACTION_CREATE);
        return Constants.UrlHelper.ADMIN_AUTO_BRAND_SHOW;
    }

    @RequestMapping(value = "/edit/{brandId}")
    public String edit(@PathVariable Integer brandId){
        if(brandId != null && brandId > 0){
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("brandId",brandId);
            AutoBrand brand=autoBrandService.query(map);
            if(brand != null){
                this.setModelAttribute("brand",brand);
            }
        }
        this.setModelAttribute("action",Constants.PageHelper.PAGE_ACTION_UPDATE);
        return Constants.UrlHelper.ADMIN_AUTO_BRAND_SHOW;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoBrand autoBrand,Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoBrand != null){
                if(StringUtil.isNotNullOrEmpty(autoBrand.getBrandName())){
                    map.put("brandName",autoBrand.getBrandName());
                }
            }
            List<AutoBrand> list=autoBrandService.queryList(map,page);
            if(list != null && list.size() > 0){
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
            result.setMsg("获取品牌信息失败");
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
                String brandLogo = saveBrandLogo();
                if(StringUtil.isNotNullOrEmpty(brandLogo)){
                    autoBrand.setBrandLogo(brandLogo);
                }
                //新增
                if(!autoBrandService.insert(autoBrand)){
                    result.setCode(1);
                    result.setMsg("添加品牌信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加品牌信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                String brandLogo = saveBrandLogo();
                if(StringUtil.isNotNullOrEmpty(brandLogo)){
                    autoBrand.setBrandLogo(brandLogo);
                }
                //修改
                if(!autoBrandService.update(autoBrand)){
                    result.setCode(1);
                    result.setMsg("修改品牌信息失败");
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
                    result.setMsg("删除品牌信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除品牌信息");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作品牌信息失败");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    public String saveBrandLogo(){

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(session.getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            MultipartFile pic = multiRequest.getFile("file");
            if(pic!=null) {

                String typePath = "brand";

                String rootPath = session.getServletContext().getRealPath("") + "/" + request.getContextPath();
                String relativePath = request.getContextPath();
                String relativeUrl = relativePath + "/upload/" + typePath + "/";

                String saveName = PathFormatter.format(pic.getOriginalFilename(), "{yy}{MM}{dd}/{HH}{mm}{ss}{rand:6}");
                String saveDir = rootPath + "upload/" + typePath + "/" + saveName.substring(0, saveName.lastIndexOf('/') + 1);
                relativeUrl += saveName.substring(0, saveName.lastIndexOf('/') + 1);
                saveName = saveName.substring(saveName.lastIndexOf("/") + 1);
                try {
                    File dir = new File(saveDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    FileOutputStream fos = new FileOutputStream(saveDir + saveName);
                    fos.write(pic.getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String url = request.getScheme() + "://" + request.getServerName();
                if (request.getServerPort() != 80) {
                    url += ":" + request.getServerPort();
                }
                url += relativeUrl + saveName;

                return url;
            }
        }

        return null;
    }
}
