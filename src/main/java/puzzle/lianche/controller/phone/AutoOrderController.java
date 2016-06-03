package puzzle.lianche.controller.phone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.entity.AutoOrder;
import puzzle.lianche.entity.AutoUser;
import puzzle.lianche.push.SmsPush;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.utils.EncryptUtil;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

@Controller(value = "phoneAutoOrderController")
@RequestMapping(value = "/phone/autoorder")
public class AutoOrderController extends BaseController {

    @Autowired
    private IAutoUserService autoUserService;
    /**
     * 提交订单信息
     * @param order 订单数据
     * @return
     */
    @RequestMapping(value = "/add.do")
    @ResponseBody
    public Result add(AutoOrder order){
        Result result = new Result();
        try{
            if(order != null){
                result.setCode(-1);
                result.setMsg("订单数据不能为空！");
            }
            if(order.getCars() == null || order.getCars().size() == 0){
                result.setCode(-1);
                result.setMsg("车源不能为空！");
            }


        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    public Result cancel(int orderId){
        Result result = new Result();
        try{

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 支付订金(买家支付订金和卖家支付订金逻辑不同)
     * @param orderId
     * @return
     */
    public Result pay(int orderId){
        Result result = new Result();
        try{

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }


    /**
     * 卖家拒绝订单
     * @param orderId
     * @return
     */
    public Result reject(int sellerId, int orderId){
        Result result = new Result();
        try{

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 买家确认收货
     * @param orderId
     * @return
     */
    public Result receive(int orderId){
        Result result = new Result();
        try{

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    /**
     * 查询订单列表
     * @param userId
     * @return
     */
    public Result list(int userId){
        Result result = new Result();
        try{

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }


    /**
     * 查看订单详情
     * @param orderId
     * @return
     */
    public Result show(int orderId) {
        Result result = new Result();
        try {

        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg() + e.getMessage());
        }
        return result;
    }
}