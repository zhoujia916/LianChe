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
import puzzle.lianche.entity.AutoAd;
import puzzle.lianche.service.IAutoAdService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "phoneAutoArticleController")
@RequestMapping(value = "/phone/autoarticle")
public class AutoArticleController extends BaseController {

    @Autowired
    private IAutoAdService autoAdService;

    /**
     * 获取广告列表
     * @param autoAd
     * @return
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(AutoAd autoAd){
        Result result = new Result();
        try{
            if(autoAd == null || autoAd.getAdPositionId() == null || autoAd.getAdPositionId() == 0){
                result.setCode(-1);
                result.setMsg("类型不能为空");
                return result;
            }
            Page page = new Page();
            page.setPageIndex(1);
            //2,3,4 对应特卖会  首页幻灯  首页通知
            if(autoAd.getAdPositionId().equals(2)){
                page.setPageSize(2);
            }
            else if(autoAd.getAdPositionId().equals(3)){
                page.setPageSize(6);
            }
            else if(autoAd.getAdPositionId().equals(4)){
                page.setPageSize(3);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("adPositionId", autoAd.getAdPositionId());
            map.put("curTime", ConvertUtil.toLong(new Date()));
            map.put("status", Constants.AUTO_AD_STATUS_VALID);
            List<AutoAd> list = autoAdService.queryList(map, page);
            if(list != null && !list.isEmpty()){
                JSONArray jsonArray = new JSONArray();
                for(AutoAd item : list){
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put("pic", item.getPic());
                    jsonItem.put("title", item.getTitle());
                    jsonItem.put("link", item.getAdLink());
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
     * 广告详情页面
     * @return
     */
    @RequestMapping(value = "/{adId}")
    public String show(@PathVariable Integer adId){
        if(adId != null && adId > 0){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("adId", adId);
            AutoAd autoAd = autoAdService.query(map);
            this.setModelAttribute("autoAd", autoAd);
        }
        return Constants.UrlHelper.PHONE_ARTICLE_SHOW;
    }
}