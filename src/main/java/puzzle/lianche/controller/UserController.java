package puzzle.lianche.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HeaderParam;


import puzzle.lianche.Constants;
import puzzle.lianche.utils.Result;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import puzzle.lianche.entity.SystemUser;
import puzzle.lianche.service.ISystemUserService;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@Controller("userController")
@RequestMapping("/user")
public class UserController extends BaseController {
    /*
    public UserController(){

    }

    @Autowired
    private ISystemUserService userService;

    @RequestMapping(value="/list")
    public String list(){
        return Constants.UrlHelper.USER_LIST;
    }

    @RequestMapping(value={ "/show/{id}"})
    public String show(@PathVariable Integer id){
        if(id > 0) {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("user_id", id);

                SystemUser user = this.userService.query(map);
                this.setModelAttribute("user", user);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return Constants.UrlHelper.USER_DETAIL;
    }

    @RequestMapping(value="/get/{id}")
    public void get(@PathVariable Integer id){
        if(id > 0) {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("user_id", id);


            }
            catch (Exception e){
            }
        }
    }

    @RequestMapping(value="/save.do", method = RequestMethod.POST)
    public void save(@ModelAttribute("user") SystemUser user){
        try{

        }
        catch (Exception e){

        }
    }

    @ResponseBody
    @RequestMapping(value="/delete.do")
    //@HeaderParam("X-Requested-With:XMLHttpRequest")
    public Result delete(){
        try{

        }
        catch (Exception e){

        }
        return new Result();
    }
    */
}