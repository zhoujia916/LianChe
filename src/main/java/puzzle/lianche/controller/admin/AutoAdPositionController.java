package puzzle.lianche.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.controller.plugin.ueditor.UEditorController;
import puzzle.lianche.entity.AutoAdPosition;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.IAutoAdPositionService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "adminAutoAdPosition")
@RequestMapping (value = "/admin/autoadposition")
public class AutoAdPositionController extends ModuleController{
    @Autowired
    private IAutoAdPositionService autoAdPositionService;

    @RequestMapping (value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions=getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_AUTO_AD_POSITION;
    }

    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(AutoAdPosition autoAdPosition,Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoAdPosition!=null) {
                if(StringUtil.isNotNullOrEmpty(autoAdPosition.getPositionName()))
                map.put("positionName", autoAdPosition.getPositionName());
            }
            List<AutoAdPosition> list=autoAdPositionService.queryList(map,page);
            if(list!=null && list.size()>0){
                result.setData(list);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取广告位置信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoAdPosition autoAdPosition){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!autoAdPositionService.insert(autoAdPosition)){
                    result.setCode(1);
                    result.setMsg("插入广告位置信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"��ӹ��λ����Ϣ");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!autoAdPositionService.update(autoAdPosition)){
                    result.setCode(1);
                    result.setMsg("修改广告位置信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"�޸�ָ���Ĺ��λ����Ϣ");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String, Object> map=new HashMap<String, Object>();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("positionId", ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] positionIds=ids.split(",");
                    map.put("positionIds",positionIds);
                }
                if(!autoAdPositionService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除广告位置信息失败");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"ɾ���ض��Ĺ��λ����Ϣ");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作广告位置信息失败");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
