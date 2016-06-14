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
import puzzle.lianche.entity.AutoCar;
import puzzle.lianche.entity.SystemAuthority;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.entity.SystemUser;
import puzzle.lianche.service.IAutoCarService;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminCarController")
@RequestMapping (value = "/admin/car")
public class CarController extends ModuleController {

    @Autowired
    private IAutoCarService autoCarService;

    @Autowired
    private ISystemMenuActionService systemMenuActionService;

    @RequestMapping(value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_CAR;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(AutoCar autoCar){
        Result result=new Result();
        try{
            Map map=new HashMap();
            map.put("carName",autoCar.getCarName());
            map.put("carType",autoCar.getCarType());
            String pageIndex=request.getParameter("pageIndex");
            String pageSize=request.getParameter("pageSize");
            Page page = new Page();
            page.setPageIndex(ConvertUtil.toInt(pageIndex));
            page.setPageSize(ConvertUtil.toInt(pageSize));
            if(autoCar.getBeginTimeString()!=null && autoCar.getBeginTimeString()!=""){
                map.put("startDate",ConvertUtil.toLong(ConvertUtil.toDate(autoCar.getBeginTimeString() + " 00:00:00")));
            }
            if(autoCar.getEndTimeString()!=null && autoCar.getEndTimeString()!=""){
                map.put("endDate",ConvertUtil.toLong(ConvertUtil.toDate(autoCar.getEndTimeString() + " 23:59:59")));
            }
            List<AutoCar> list=autoCarService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoCar car:list){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("carId",car.getCarId());
                    jsonObject.put("carName",car.getCarName());
                    jsonObject.put("carType",Constants.MAP_AUTO_CAR_TYPE.get(car.getCarType()));
                    jsonObject.put("brandName",car.getBrandName());
                    jsonObject.put("catName",car.getCatName());
                    jsonObject.put("modelName",car.getModelName());
                    jsonObject.put("officalPrice",car.getOfficalPrice());
                    jsonObject.put("totalNumber",car.getTotalNumber());
                    jsonObject.put("lockNumber",car.getLockNumber());
                    jsonObject.put("surplusNumber",car.getSurplusNumber());
                    jsonObject.put("userName",car.getUserName());
                    jsonObject.put("realName",car.getRealName());
                    jsonObject.put("status",car.getStatus());
                    jsonObject.put("sortOrder",car.getSortOrder());
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取车源信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/action.do")
    @ResponseBody
    public Result action(String action,AutoCar autoCar){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map map=new HashMap();
                String id = getParameter("id");
                String ids = getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("carId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] carIds=ids.split(",");
                    map.put("carIds",carIds);
                }
                if(!autoCarService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除车源信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除特定的车源信息");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作车源信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
