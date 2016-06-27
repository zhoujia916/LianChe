package puzzle.lianche.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.AutoArticleCat;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.IAutoArticleCatService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminAutoArticleCatController")
@RequestMapping (value = "/admin/autoarticlecat")
public class AutoArticleCatController extends ModuleController {

    @Autowired
    private IAutoArticleCatService autoArticleCatService;

    @RequestMapping (value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        List<AutoArticleCat> autoArticleCatList=autoArticleCatService.queryList(null);
        this.setModelAttribute("autoArticleCatList",addSubAutoArticle(autoArticleCatList,0,"select"));
        return Constants.UrlHelper.ADMIN_AUTO_ARTICLE_CAT;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoArticleCat autoArticleCat){
        Result result=new Result();
        try{
            List<AutoArticleCat> list=autoArticleCatService.queryByArticleCat(autoArticleCat);
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
    public Result action(String action,AutoArticleCat autoArticleCat){
        Result result=new Result();
        Map<String, Object> map=new HashMap<String, Object>();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!autoArticleCatService.insert(autoArticleCat)){
                    result.setCode(1);
                    result.setMsg("添加文章类型信息时出错");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加文章类型信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!autoArticleCatService.update(autoArticleCat)){
                    result.setCode(1);
                    result.setMsg("修改文章类型信息时出错");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改指定的文章类型信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("catId", ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] catIds=ids.split(",");
                    map.put("catIds",catIds);
                }
                if(!autoArticleCatService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除文章类型信息时出错");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除指定的文章类型信息");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作文章类型信息时出错!");
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

    public int getLevel(List<AutoArticleCat> list, AutoArticleCat menu){
        if(menu.getParentId() == 0){
            return 0;
        }
        int level = 0;
        boolean hasParent = true;
        while(hasParent){
            hasParent = false;
            for (AutoArticleCat item : list) {
                if (menu.getParentId() == item.getCatId()) {
                    level++;
                    menu = item;
                    hasParent = true;
                    break;
                }
            }
        }
        return level;
    }
    //endregion
}
