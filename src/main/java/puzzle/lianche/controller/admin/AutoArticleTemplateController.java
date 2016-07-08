package puzzle.lianche.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.AutoArticleTemplate;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.IAutoArticleTemplateService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminAutoArticleTemplateController")
@RequestMapping (value = "/admin/autoarticletemplate")
public class AutoArticleTemplateController extends ModuleController {

    @Autowired
    private IAutoArticleTemplateService autoArticleTemplateService;

    @RequestMapping (value = {"/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_AUTO_ARTICLE_TEMPLATE;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoArticleTemplate autoArticleTemplate){
        Result result=new Result();
        try{
            Page page = new Page();
            if(StringUtil.isNotNullOrEmpty(request.getParameter("pageIndex")) && StringUtil.isNumber(request.getParameter("pageIndex"))){
                page.setPageIndex(ConvertUtil.toInt(request.getParameter("pageIndex")));
            }
            if(StringUtil.isNotNullOrEmpty(request.getParameter("pageSize")) && StringUtil.isNumber(request.getParameter("pageSize"))){
                page.setPageSize(ConvertUtil.toInt(request.getParameter("pageSize")));
            }
            Map<String,Object> map = new HashMap<String, Object>();
            if(autoArticleTemplate!=null){
                if(StringUtil.isNotNullOrEmpty(autoArticleTemplate.getName())){
                    map.put("name",autoArticleTemplate.getName());
                }
            }
            List<AutoArticleTemplate> list = autoArticleTemplateService.queryList(map, page);
            result.setData(list);
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取文章类型信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoArticleTemplate autoArticleTemplate){
        Result result=new Result();
        Map<String, Object> map=new HashMap<String, Object>();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!autoArticleTemplateService.insert(autoArticleTemplate)){
                    result.setCode(1);
                    result.setMsg("添加文章类型信息失败");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加文章类型信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!autoArticleTemplateService.update(autoArticleTemplate)){
                    result.setCode(1);
                    result.setMsg("修改文章类型信息失败");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改指定的文章类型信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("templateId", ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] templateIds=ids.split(",");
                    map.put("templateIds",templateIds);
                }
                if(!autoArticleTemplateService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除文章类型信息失败");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除指定的文章类型信息");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作文章类型信息失败!");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
