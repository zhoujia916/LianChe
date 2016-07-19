package puzzle.lianche.controller.phone;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.entity.AutoArticle;
import puzzle.lianche.service.IAutoArticleService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "phoneAutoArticleController")
@RequestMapping(value = "/phone/autoarticle")
public class AutoArticleController extends BaseController {

    @Autowired
    private IAutoArticleService autoArticleService;

    /**
     * 获取文章列表
     * @param autoArticle
     * @return
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(AutoArticle autoArticle){
        Result result = new Result();
        try{
            if(autoArticle == null || autoArticle.getCatId() == null || autoArticle.getCatId() == 0){
                result.setCode(-1);
                result.setMsg("类型不能为空");
                return result;
            }
            Page page = new Page();
            page.setPageIndex(1);
            //2,3,6 对应特卖会  首页幻灯  首页通知
            if(autoArticle.getCatId().equals(2)){
                page.setPageSize(2);
            }
            else if(autoArticle.getCatId().equals(3)){
                page.setPageSize(6);
            }
            else if(autoArticle.getCatId().equals(4)){
                page.setPageSize(3);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("catId", autoArticle.getCatId());
            map.put("curTime", ConvertUtil.toLong(new Date()));
            map.put("status", Constants.AUTO_AD_STATUS_VALID);
            List<AutoArticle> list = autoArticleService.queryList(map, page);
            if(list != null && !list.isEmpty()){
                JSONArray jsonArray = new JSONArray();
                for(AutoArticle item : list){
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put("cover", item.getCover());
                    jsonItem.put("title", item.getTitle());
                    String link = item.getSourceUrl();
                    if(StringUtil.isNullOrEmpty(link)){
                        link = getHost() + request.getContextPath() + "/phone/autoarticle/" + item.getArticleId();
                    }
                    jsonItem.put("link", link);
                    jsonArray.add(jsonItem);
                }
                result.setData(jsonArray);
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("获取广告列表信息出错");
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * 文章详情页面
     * @return
     */
    @RequestMapping(value = "/{articleId}")
    public String show(@PathVariable Integer articleId){
        if(articleId != null && articleId > 0){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("articleId", articleId);
            AutoArticle article = autoArticleService.query(map);
            this.setModelAttribute("article", article);
        }
        return Constants.UrlHelper.PHONE_ARTICLE_SHOW;
    }
}