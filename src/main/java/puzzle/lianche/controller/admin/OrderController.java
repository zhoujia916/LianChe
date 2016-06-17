package puzzle.lianche.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.Constants;
import puzzle.lianche.controller.ModuleController;
import puzzle.lianche.entity.AutoOrder;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.IAutoOrderService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "adminOrderController")
@RequestMapping(value = "/admin/order")
public class OrderController extends ModuleController {

    @Autowired
    private IAutoOrderService autoOrderService;

    @RequestMapping(value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions=getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_ORDER;
    }

    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(){
        Result result=new Result();
        try{
            Map map=new HashMap();
            Page page=new Page();
            page.setPageIndex(ConvertUtil.toInt(request.getParameter("pageIndex")));
            page.setPageSize(ConvertUtil.toInt(request.getParameter("pageSize")));
            List<AutoOrder> list=autoOrderService.queryOrder(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(AutoOrder order:list){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("orderId",order.getOrderId());
                    jsonObject.put("carName",order.getCarName());
                    jsonObject.put("orderStatus",Constants.ORDER_STATUS.get(order.getOrderStatus()));
                    jsonObject.put("payStatus",Constants.PAY_STATUS.get(order.getPayStatus()));
                    jsonObject.put("shipStatus",Constants.SHIP_STATUS.get(order.getShipStatus()));
                    jsonObject.put("sellerName",order.getSellerName());
                    jsonObject.put("sellerRealName",order.getSellerRealName());
                    jsonObject.put("buyerName",order.getBuyerName());
                    jsonObject.put("buyerRealName",order.getBuyerRealName());
                    jsonObject.put("amount",order.getAmount());
                    jsonObject.put("price",order.getPrice()*order.getAmount());
                    jsonObject.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(order.getAddTime())));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取订单信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}
