package puzzle.lianche.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import puzzle.lianche.entity.AutoOrder;
import puzzle.lianche.service.IAutoOrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RealeseLockCarJob {

    @Autowired
    private IAutoOrderService autoOrderService;

    //@Scheduled(cron="* 0/30 * * * ?")
    public void execute(){
        Map<String, Object> map = new HashMap<String, Object>();
        List<AutoOrder> list = autoOrderService.queryList(map);
        autoOrderService.realeseLock(list);
    }
}
