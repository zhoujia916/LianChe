package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.*;
import puzzle.lianche.service.IAutoAdPositionService;
import puzzle.lianche.service.IAutoAdService;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.*;

@Controller (value = "adminAdController")
@RequestMapping (value = "/admin/ad")
public class AdController extends ModuleController {

    @Autowired
    private IAutoAdService autoAdService;

    @Autowired
    private IAutoAdPositionService autoAdPositionService;

    @RequestMapping (value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        List<AutoAdPosition> list=autoAdPositionService.queryList(null);
        this.setModelAttribute("list",list);
        return Constants.UrlHelper.ADMIN_AD;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoAd autoAd){
        Result result=new Result();
        try{
            Map map=new HashMap();
            String pageIndex=request.getParameter("pageIndex");
            String pageSize=request.getParameter("pageSize");
            Page page = new Page();
            page.setPageIndex(ConvertUtil.toInt(pageIndex));
            page.setPageSize(ConvertUtil.toInt(pageSize));
            if(autoAd.getAdPositionIdString()!=null && autoAd.getAdPositionIdString()!=""){
                map.put("adPositionIds",autoAd.getAdPositionIdString());
            }
            if(autoAd.getBeginTimeString()!=null && autoAd.getBeginTimeString()!=""){
                map.put("startDate",ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getBeginTimeString()+" 00:00:00")));
            }
            if(autoAd.getEndTimeString()!=null && autoAd.getEndTimeString()!=""){
                map.put("endDate",ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getEndTimeString() + " 23:59:59")));
            }
            List<AutoAd> list=autoAdService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoAd ad:list){
                    JSONObject jsonObject=JSONObject.fromObject(ad);
                    jsonObject.put("startDate",ConvertUtil.toString(ConvertUtil.toDate(ad.getStartDate())));
                    jsonObject.put("endDate",ConvertUtil.toString(ConvertUtil.toDate(ad.getEndDate())));
                    jsonObject.put("beginTimeString",ConvertUtil.toString(ConvertUtil.toDate(ad.getStartDate()),"yyyy-MM-dd"));
                    jsonObject.put("endTimeString",ConvertUtil.toString(ConvertUtil.toDate(ad.getEndDate()),"yyyy-MM-dd"));
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
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoAd autoAd){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                autoAd.setAddTime(ConvertUtil.toLong(new Date()));
                autoAd.setStartDate(ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getBeginTimeString()+" 00:00:00")));
                autoAd.setEndDate(ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getEndTimeString()+" 23:59:59")));
                autoAd.setStatus(1);
                if(!autoAdService.insert(autoAd)){
                    result.setCode(1);
                    result.setMsg("添加广告信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加广告信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                autoAd.setStartDate(ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getBeginTimeString()+" 00:00:00")));
                autoAd.setEndDate(ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getEndTimeString()+" 23:59:59")));
                if(!autoAdService.update(autoAd)){
                    result.setCode(1);
                    result.setMsg("修改广告信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改广告信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map map=new HashMap();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("adId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] adIds=ids.split(",");
                    map.put("adIds",adIds);
                }
                if(!autoAdService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除广告信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除特定的广告信息");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作广告信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
