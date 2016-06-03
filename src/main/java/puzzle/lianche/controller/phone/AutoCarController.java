package puzzle.lianche.controller.phone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.lianche.controller.BaseController;
import puzzle.lianche.entity.AutoCar;
import puzzle.lianche.entity.AutoCollect;
import puzzle.lianche.entity.AutoOrder;
import puzzle.lianche.entity.AutoUser;
import puzzle.lianche.push.SmsPush;
import puzzle.lianche.service.IAutoCarService;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.utils.EncryptUtil;
import puzzle.lianche.utils.Result;
import puzzle.lianche.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

@Controller(value = "phoneAutoCarController")
@RequestMapping(value = "/phone/autocar")
public class AutoCarController extends BaseController {

    @Autowired
    private IAutoUserService autoUserService;
    @Autowired
    private IAutoCarService autoCarService;


    /**
     * 查看汽车资源列表
     * @param userId 用户ID
     * @param isSale 是否特卖会
     * @return
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(int userId, int isSale){
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
     * 查看汽车资源详情
     * @param carId
     * @return
     */
    @RequestMapping(value = "/show.do")
    @ResponseBody
    public Result show(int userId, int carId){
        Result result = new Result();
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("carId", carId);

            AutoCar car = autoCarService.query(map);
            if(car != null){

            }else{
                result.setCode(1);
                result.setMsg("该车源不存在！");
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }


    /**
     * 新增汽车资源详情
     * @param car
     * @return
     */
    @RequestMapping(value = "/add.do")
    @ResponseBody
    public Result add(AutoCar car){
        Result result = new Result();
        try{
            if(car.getBrandId() == null || car.getBrandId() == 0){
                result.setCode(-1);
                result.setMsg("品牌不能为空！");
            }
            if(car.getBrandCatId() == null || car.getBrandCatId() == 0){
                result.setCode(-1);
                result.setMsg("车系不能为空！");
            }
            if(car.getBrandModelId() == null || car.getBrandModelId() == 0){
                result.setCode(-1);
                result.setMsg("车型不能为空！");
            }
            if(car.getOfficalPrice() == null || car.getOfficalPrice() <= 0){
                result.setCode(-1);
                result.setMsg("指导价不能为空！");
            }

            //校验颜色

        }catch (Exception e){
            result.setCode(1);
            result.setMsg("注册会员信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

}