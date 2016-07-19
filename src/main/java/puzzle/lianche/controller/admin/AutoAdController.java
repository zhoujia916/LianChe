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
import puzzle.lianche.entity.AutoAd;
import puzzle.lianche.entity.AutoAdPosition;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.IAutoAdPositionService;
import puzzle.lianche.service.IAutoAdService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminAutoAdController")
@RequestMapping (value = "/admin/autoad")
public class AutoAdController extends ModuleController {

    @Autowired
    private IAutoAdService autoAdService;

    @Autowired
    private IAutoAdPositionService autoAdPositionService;

    @RequestMapping (value = {"/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        List<AutoAdPosition> list=autoAdPositionService.queryList(null);
        this.setModelAttribute("list",list);
        return Constants.UrlHelper.ADMIN_AUTO_AD;
    }

    @RequestMapping (value = {"/add"})
    public String add(){
        List<AutoAdPosition> positionList = autoAdPositionService.queryList(null);
        this.setModelAttribute("positionList", positionList);
        this.setModelAttribute("action", Constants.PageHelper.PAGE_ACTION_CREATE);
        return Constants.UrlHelper.ADMIN_AUTO_AD_SHOW;
    }

    @RequestMapping (value = {"/edit/{adId}"})
    public String edit(@PathVariable Integer adId){
        if(adId != null && adId > 0){
            List<AutoAdPosition> positionList = autoAdPositionService.queryList(null);
            this.setModelAttribute("positionList", positionList);

            AutoAd ad = autoAdService.query(adId);
            if(ad != null){
                ad.setBeginTimeString(ConvertUtil.toString(ConvertUtil.toDate(ad.getStartDate()), Constants.DATE_FORMAT));
                ad.setEndTimeString(ConvertUtil.toString(ConvertUtil.toDate(ad.getEndDate()), Constants.DATE_FORMAT));
            }
            this.setModelAttribute("ad", ad);
            this.setModelAttribute("action", Constants.PageHelper.PAGE_ACTION_UPDATE);
        }
        return Constants.UrlHelper.ADMIN_AUTO_AD_SHOW;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoAd autoAd,Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoAd!=null) {
                if (autoAd.getAdPositionId() != null && autoAd.getAdPositionId() > 0) {
                    map.put("adPositionId", autoAd.getAdPositionId());
                }
                if (StringUtil.isNotNullOrEmpty(autoAd.getBeginTimeString())) {
                    map.put("startDate", ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getBeginTimeString() + " 00:00:00")));
                }
                if (StringUtil.isNotNullOrEmpty(autoAd.getEndTimeString())) {
                    map.put("endDate", ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getEndTimeString() + " 23:59:59")));
                }
            }
            List<AutoAd> list=autoAdService.queryList(map,page);
            if(list != null && list.size() > 0){
                JSONArray array=new JSONArray();
                for(AutoAd ad:list){
                    JSONObject jsonObject=JSONObject.fromObject(ad);
                    jsonObject.put("startDate",ConvertUtil.toString(ConvertUtil.toDate(ad.getStartDate()),"yyyy-MM-dd"));
                    jsonObject.put("endDate",ConvertUtil.toString(ConvertUtil.toDate(ad.getEndDate()),"yyyy-MM-dd"));
                    jsonObject.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(ad.getAddTime())));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("获取广告信息出错");
            logger.error(result.getMsg()+e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action, AutoAd autoAd){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                autoAd.setAddTime(ConvertUtil.toLong(new Date()));
                autoAd.setStartDate(ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getBeginTimeString()+" 00:00:00")));
                autoAd.setEndDate(ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getEndTimeString()+" 23:59:59")));
                String pic = savePic();
                if(StringUtil.isNotNullOrEmpty(pic)) {
                    autoAd.setPic(pic);
                }
                if(!autoAdService.insert(autoAd)){
                    result.setCode(1);
                    result.setMsg("添加广告信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加广告信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                autoAd.setStartDate(ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getBeginTimeString()+" 00:00:00")));
                autoAd.setEndDate(ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getEndTimeString()+" 23:59:59")));
                String pic = savePic();
                if(StringUtil.isNotNullOrEmpty(pic)) {
                    autoAd.setPic(pic);
                }else{
                    if(StringUtil.isNullOrEmpty(autoAd.getPic())){
                        autoAd.setPic("");
                    }
                }
                if(!autoAdService.update(autoAd)){
                    result.setCode(1);
                    result.setMsg("修改广告信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改广告信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String, Object> map=new HashMap<String, Object>();
                String id = request.getParameter("id");
                String ids = request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("adId", ConvertUtil.toInt(id));
                }
                else if(StringUtil.isNotNullOrEmpty(ids)){
                    map.put("adIds",ids);
                }
                if(!autoAdService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除广告信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除广告信息失败");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作广告信息失败");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    public String savePic(){

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(session.getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            MultipartFile pic = multiRequest.getFile("file");

            String typePath = "ad";

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

        return null;
    }
}
