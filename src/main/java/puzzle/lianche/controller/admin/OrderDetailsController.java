package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.AutoOrder;
import puzzle.lianche.service.IAutoOrderService;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "/admin/orderdetails")
@Controller(value = "/adminOrderDetailsController")
public class OrderDetailsController extends ModuleController{

    @Autowired
    private IAutoOrderService autoOrderService;

    @RequestMapping (value = "/index/{orderId}")
    public String show(@PathVariable String orderId){
        System.out.println(orderId);
        this.setModelAttribute("orderId",orderId);
        return Constants.UrlHelper.ADMIN_ORDER_DETAILS;
    }

    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(){
        Result result=new Result();
        try{
            String orderId=request.getParameter("orderId");
            if(StringUtil.isNotNullOrEmpty(orderId)){
                Map map=new HashMap();
                map.put("orderId",orderId);
                AutoOrder order=autoOrderService.query(map);
                if(order!=null){
                    JSONArray array=new JSONArray();
                    JSONObject object=new JSONObject();
                    array.add(object);
                    result.setData(array);
                }
            }else{
                result.setCode(-1);
                result.setMsg("查看订单详情参数出错！");
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取订单详情出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
