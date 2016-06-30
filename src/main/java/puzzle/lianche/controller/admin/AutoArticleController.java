package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.controller.plugin.uploader.PathFormatter;
import puzzle.lianche.entity.AutoArticle;
import puzzle.lianche.entity.AutoArticleCat;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.entity.SystemUser;
import puzzle.lianche.service.IAutoArticleCatService;
import puzzle.lianche.service.IAutoArticleService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@Controller (value = "adminAutoArticleController")
@RequestMapping (value = "/admin/autoarticle")
public class AutoArticleController extends ModuleController {

    @Autowired
    private IAutoArticleService autoArticleService;

    @Autowired
    private IAutoArticleCatService autoArticleCatService;

    @RequestMapping (value = {"/","/index"})
    public String index(){

        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);

        List<AutoArticleCat> autoArticleCatList=autoArticleCatService.queryList(null);
        this.setModelAttribute("autoArticleCatList",addSubAutoArticle(autoArticleCatList,0,"select"));
        return Constants.UrlHelper.ADMIN_AUTO_ARTICLE;
    }



    @RequestMapping (value = "/add")
    public String add(){
        List<AutoArticleCat> cats = autoArticleCatService.queryList(null);
        this.setModelAttribute("catList", cats);
        return Constants.UrlHelper.ADMIN_AUTO_ARTICLE_ADD;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
<<<<<<< HEAD
    public Result list(AutoArticle autoArticle,Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoArticle!=null) {
                if(autoArticle.getTitle()!=null && autoArticle.getTitle()!="") {
                    map.put("title", autoArticle.getTitle());
                }
                if(autoArticle.getName()!=null && autoArticle.getName()!=""){
                    map.put("name", autoArticle.getName());
                }
                if (autoArticle.getBeginTimeString() != null && autoArticle.getBeginTimeString() != "") {
                    map.put("beginTime", ConvertUtil.toLong(ConvertUtil.toDateTime(autoArticle.getBeginTimeString() + " 00:00:00")));
                }
                if (autoArticle.getEndTimeString() != null && autoArticle.getEndTimeString() != "") {
                    map.put("endTime", ConvertUtil.toLong(ConvertUtil.toDateTime(autoArticle.getEndTimeString() + " 23:59:59")));
                }
                if (autoArticle.getStatus() != null && autoArticle.getStatus() >0) {
                    map.put("status", autoArticle.getStatus());
                }
                if (autoArticle.getCatId() != null && autoArticle.getCatId() >0) {
                    map.put("catId", autoArticle.getCatName());
                }
            }
            List<AutoArticle> list=autoArticleService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoArticle article:list){
                    JSONObject jsonObject=JSONObject.fromObject(article);
                    jsonObject.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(article.getAddTime())));
                    jsonObject.put("status",Constants.MAP_AUTO_ARTICLE_STATUS.get(article.getStatus()));
                    array.add(jsonObject);
=======
    public Result list(AutoArticle autoArticle, Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoArticle != null) {
                map.put("title", autoArticle.getTitle());
                if (StringUtil.isNotNullOrEmpty(autoArticle.getBeginTimeString())) {
                    map.put("beginTime", ConvertUtil.toLong(ConvertUtil.toDateTime(autoArticle.getBeginTimeString() + " 00:00:00")));
                }
                if (StringUtil.isNotNullOrEmpty(autoArticle.getEndTimeString())) {
                    map.put("endTime", ConvertUtil.toLong(ConvertUtil.toDateTime(autoArticle.getEndTimeString() + " 23:59:59")));
                }
                if (StringUtil.isNotNullOrEmpty(autoArticle.getStatusString())) {
                    map.put("autoArticleStatus", autoArticle.getStatusString());
                }
                if (StringUtil.isNotNullOrEmpty(autoArticle.getCatName())) {
                    map.put("catNames", autoArticle.getCatName());
                }
            }
            List<AutoArticle> list = autoArticleService.queryList(map,page);
            if(list != null && !list.isEmpty()){
                JSONArray array = new JSONArray();
                for(AutoArticle article : list){
                    JSONObject object = JSONObject.fromObject(article);
                    object.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(article.getAddTime())));
                    object.put("status",Constants.MAP_AUTO_ARTICLE_STATUS.get(article.getStatus()));
                    array.add(object);
>>>>>>> 02c2f1c165a2ffff20780cae043e422a59f69a61
                }
                result.setData(array);
            }

            result.setTotal(page.getTotal());
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
        Map<String, Object> map=new HashMap<String, Object>();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                int userId = 0;
                if(getCurrentUser() != null){
                    userId = ((SystemUser)getCurrentUser()).getUserId();
                }
                autoArticle.setAddUserId(userId);
                autoArticle.setStatus(2);
                autoArticle.setAddTime(ConvertUtil.toLong(new Date()));
                CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(session.getServletContext());
                if(multipartResolver.isMultipart(request)) {
                    MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                    MultipartFile cover = multiRequest.getFile("cover");

                    String rootPath = session.getServletContext().getRealPath("");
                    String relativePath = request.getContextPath();
                    String typePath = "article";
                    String savePath = rootPath + "/upload/" + typePath + "/";
                    String relativeUrl = relativePath + "/upload/" + typePath + "/";
                    String saveName = PathFormatter.format(cover.getOriginalFilename(), "{yy}{MM}{dd}/{hh}{mm}{rand:6}");
                    String dirName = savePath + saveName.substring(0, saveName.lastIndexOf('/'));
                    File dir = new File(dirName);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    FileOutputStream fos = new FileOutputStream(savePath + saveName);
                    fos.write(cover.getBytes());
                    fos.close();

                    String url = request.getScheme() + "://" + request.getServerName();
                    if(request.getServerPort() != 80){
                        url += ":" + request.getServerPort();
                    }
                    url += relativeUrl + saveName;

                    autoArticle.setCover(url);
                }

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
