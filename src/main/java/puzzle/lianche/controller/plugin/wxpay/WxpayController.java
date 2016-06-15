package puzzle.lianche.controller.plugin.wxpay;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import puzzle.lianche.controller.BaseController;

@Controller(value = "pluginWxpayController")
public class WxpayController extends BaseController {

    @RequestMapping(value = {"/wxpay"})
    public void index(){

    }

}
