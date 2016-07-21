package puzzle.lianche.controller;

import puzzle.lianche.Constants;
import puzzle.lianche.task.TimeoutTask;
import puzzle.lianche.task.utils.EventTypeEnum;
import puzzle.lianche.task.utils.TaskEvent;
import puzzle.lianche.utils.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class IndexController extends BaseController {
    @RequestMapping(value = {"/index"})
    public String index(){
        return Constants.UrlHelper.ADMIN_SYSTEM_INDEX;
    }

    @ResponseBody
    @RequestMapping(value = "/login.do")
    public Result login(String username, String password){
        Result result = new Result();

        return result;
    }

    @RequestMapping(value = "/logout.do")
    public String logout(){
        this.setCurrentUser(null);
        return redirect(Constants.UrlHelper.ADMIN_SYSTEM_LOGIN);
    }

    @RequestMapping(value = "/task.do")
    public void task(){
        TaskEvent taskEvent = new TaskEvent();
        taskEvent.setBizId("1");
        taskEvent.setEventId(1l);
        taskEvent.setEventType(EventTypeEnum.AUTO_RELEASE_ORDER);
        taskEvent.setExecuteTime(new Date().getTime() + 3000);



        TaskEvent taskEvent2 = new TaskEvent();
        taskEvent2.setBizId("2");
        taskEvent2.setEventId(2l);
        taskEvent2.setEventType(EventTypeEnum.AUTO_RELEASE_ORDER);
        taskEvent2.setExecuteTime( new Date().getTime() + 5000);


        TaskEvent taskEvent3 = new TaskEvent();
        taskEvent3.setBizId("3");
        taskEvent3.setEventId(3l);
        taskEvent3.setEventType(EventTypeEnum.AUTO_RELEASE_ORDER);
        taskEvent3.setExecuteTime( new Date().getTime() + 6000);


//        TimeoutTask timeoutTask = new TimeoutTask();
//        timeoutTask.register(taskEvent3);
//        timeoutTask.register(taskEvent2);
//        timeoutTask.register(taskEvent);

//        timeoutTask.unRegister(taskEvent2);

        try {
            Thread.sleep(20*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
