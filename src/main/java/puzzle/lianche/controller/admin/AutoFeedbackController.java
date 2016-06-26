package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.AutoFeedback;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.IAutoFeedbackService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminAutoFeedBackController")
@RequestMapping (value = "/admin/autofeedback")
public class AutoFeedbackController extends ModuleController {

    @Autowired
    private IAutoFeedbackService autoFeedbackService;

    @RequestMapping (value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_AUTO_FEEDBACK;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoFeedback autoFeedback){
        Result result=new Result();
        StringBuffer stringBuffer=new StringBuffer();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("userName",autoFeedback.getUserName());
            String pageIndex=request.getParameter("pageIndex");
            String pageSize=request.getParameter("pageSize");
            Page page = new Page();
            page.setPageIndex(ConvertUtil.toInt(pageIndex));
            page.setPageSize(ConvertUtil.toInt(pageSize));
            List<AutoFeedback> list=autoFeedbackService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoFeedback feedback:list){
                    JSONObject jsonObject=JSONObject.fromObject(feedback);
                    if(feedback.getPic()!=null) {
                        String[] pic = feedback.getPic().split(",");
                        for (int i = 0; i < pic.length; i++) {
                            stringBuffer.append("<img style='width: 30px;height: 30px;' src='" + pic[i] + "'/>");
                            if (pic.length - i > 1) {
                                stringBuffer.append("&nbsp;&nbsp;");
                            }
                        }
                    }
                    jsonObject.put("pic", stringBuffer.toString());
                    jsonObject.put("addTime", ConvertUtil.toString(ConvertUtil.toDate(feedback.getAddTime())));
                    array.add(jsonObject);
                    stringBuffer.setLength(0);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取反馈信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action){
        Result result=new Result();
        Map<String, Object> map=new HashMap<String, Object>();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("feedbackId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] feedbackIds=ids.split(",");
                    map.put("feedbackIds",feedbackIds);
                }
                if(!autoFeedbackService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除反馈信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除指定的反馈信息");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作反馈信息时出错");
        }
        return result;
    }
}
