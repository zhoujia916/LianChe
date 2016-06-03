package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.*;
import puzzle.lianche.service.IAutoArticleCatService;
import puzzle.lianche.service.IAutoArticleService;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.*;

@Controller (value = "adminArticleController")
@RequestMapping (value = "/admin/article")
public class ArticleController extends ModuleController {

    @Autowired
    private IAutoArticleService autoArticleService;

    @Autowired
    private IAutoArticleCatService autoArticleCatService;

    @Autowired
    private ISystemMenuActionService systemMenuActionService;

    @RequestMapping (value = {"/","/index"})
    public String index(){

        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);

        List<AutoArticleCat> autoArticleCatList=autoArticleCatService.queryList(null);
        this.setModelAttribute("autoArticleCatList",addSubAutoArticle(autoArticleCatList,0,"select"));
        return Constants.UrlHelper.ADMIN_AUTO_ARTICLE;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoArticle autoArticle){
        Result result=new Result();
        List<AutoArticle> articleList=new ArrayList<AutoArticle>();
        try{
            Map map=new HashMap();
            map.put("title",autoArticle.getTitle());
            String pageIndex=request.getParameter("pageIndex");
            String pageSize=request.getParameter("pageSize");
            Page page = new Page();
            page.setPageIndex(ConvertUtil.toInt(pageIndex));
            page.setPageSize(ConvertUtil.toInt(pageSize));
            if(autoArticle.getBeginTimeString()!=null && autoArticle.getBeginTimeString()!=""){
                map.put("beginTime",ConvertUtil.toLong(ConvertUtil.toDate(autoArticle.getBeginTimeString()+" 00:00:00")));
            }
            if(autoArticle.getEndTimeString()!=null && autoArticle.getEndTimeString()!=""){
                map.put("endTime",ConvertUtil.toLong(ConvertUtil.toDate(autoArticle.getEndTimeString()+" 23:59:59")));
            }
            if(autoArticle.getStatusString()!=null &&autoArticle.getStatusString()!=""){
                map.put("autoArticleStatus",autoArticle.getStatusString());
            }
            if(autoArticle.getCatName()!=null && autoArticle.getCatName()!=""){
                map.put("catNames",autoArticle.getCatName());
            }
            List<AutoArticle> list=autoArticleService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getAdminName()!=null && list.get(i).getUserName()==null){
                        list.get(i).setName(list.get(i).getAdminName());
                        articleList.add(list.get(i));
                    }else if(list.get(i).getAdminName()==null && list.get(i).getUserName()!=null){
                        list.get(i).setName(list.get(i).getUserName());
                        articleList.add(list.get(i));
                    }
                }
                for(AutoArticle article:articleList){
                    JSONObject jsonObject=JSONObject.fromObject(article);
                    jsonObject.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(article.getAddTime())));
                    jsonObject.put("status",Constants.AUTO_ARTICLE_STATUS.get(article.getStatus()));
                    jsonObject.put("addUserType",Constants.AUTO_ARTICLE_USER_TYPE.get(article.getAddUserType()));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取文章信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoArticle autoArticle){
        Result result=new Result();
        Map map=new HashMap();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                autoArticle.setAddUserType(2);
                autoArticle.setAddUserId(1);
                autoArticle.setStatus(1);
                autoArticle.setAddTime(ConvertUtil.toLong(new Date()));
                if(!autoArticleService.insert(autoArticle)){
                    result.setCode(1);
                    result.setMsg("添加文章时出错");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加文章信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!autoArticleService.update(autoArticle)){
                    result.setCode(1);
                    result.setMsg("修改文章信息时出错");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改特定的文章信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                //删除
                String id =request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("articleId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] articleId=ids.split(",");
                    map.put("articleIds",articleId);
                }
                if(!autoArticleService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除文章信息出错");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除特定的文章信息");
                }
            }else{
                result.setCode(-1);
                result.setMsg("参数出错");
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作文章信息时出错!");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    //region
    public List<AutoArticleCat> addSubAutoArticle(List<AutoArticleCat> list, int   parentId, String type){
        List<AutoArticleCat> newList = new ArrayList<AutoArticleCat>();
        for (AutoArticleCat item : list) {
            if (item.getParentId() == parentId) {
                if(type.equalsIgnoreCase("select")){
                    int level = getLevel(list, item);
                    if(level == 0){
                        item.setCatName("|-" + item.getCatName());
                    }else{
                        item.setCatName("|-" + StringUtil.padLeft(level * 2, '-') + item.getCatName());
                    }
                    newList.add(item);
                    List<AutoArticleCat> children = addSubAutoArticle(list, item.getCatId(), type);
                    for(int i = 0; i < children.size(); i++){
                        newList.add(children.get(i));
                    }
                } else if (type.equalsIgnoreCase("list")) {
                    newList.add(item);
                    List<AutoArticleCat> children = addSubAutoArticle(list, item.getCatId(), type);
                    for(int i = 0; i < children.size(); i++){
                        newList.add(children.get(i));
                    }
                }
                else if (type.equalsIgnoreCase("tree")) {
                    List<AutoArticleCat> children = addSubAutoArticle(list, item.getCatId(), type);
                    item.setChildren(children);
                    newList.add(item);
                }

            }
        }
        return newList;
    }

    public int getLevel(List<AutoArticleCat> list, AutoArticleCat autoArticleCat){
        if(autoArticleCat.getParentId() == 0){
            return 0;
        }
        int level = 0;
        boolean hasParent = true;
        while(hasParent){
            hasParent = false;
            for (AutoArticleCat item : list) {
                if (autoArticleCat.getParentId() == item.getCatId()) {
                    level++;
                    autoArticleCat = item;
                    hasParent = true;
                    break;
                }
            }
        }
        return level;
    }
    //endregion
}
