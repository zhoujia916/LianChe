package puzzle.lianche.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import puzzle.lianche.Constants;
import puzzle.lianche.entity.AutoOrder;
import puzzle.lianche.service.IAutoOrderService;
import puzzle.lianche.task.TimeoutTask;
import puzzle.lianche.task.utils.EventTypeEnum;
import puzzle.lianche.task.utils.TaskEvent;
import puzzle.lianche.utils.ConvertUtil;

import java.util.*;

@Component("initTask")
public class InitTask implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private TimeoutTask timeoutTask;

    @Autowired
    private IAutoOrderService autoOrderService;

    @Autowired
    private InitConfig initConfig;


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderStatus", Constants.OS_SUBMIT);
        map.put("payStatusList", Constants.PS_WAIT_BUYER_DEPOSIT + "," + Constants.PS_BUYER_PAY_DEPOSIT);
        map.put("shipStatus", Constants.SS_UNSHIP);
        List<AutoOrder> list = autoOrderService.queryList(map);
        Calendar calendar = Calendar.getInstance();
        int period = ConvertUtil.toInt(initConfig.getProperty("order.lockMinute"));
        if(list != null && list.size() > 0){
            for(AutoOrder item : list){
                TaskEvent taskEvent = new TaskEvent();
                taskEvent.setEventId((long) item.getOrderId());
                taskEvent.setBizId(item.getOrderSn());
                taskEvent.setEventType(EventTypeEnum.AUTO_RELEASE_ORDER);
                long executeTime = 0;
                if(item.getPayStatus() == Constants.PS_WAIT_BUYER_DEPOSIT){
                    executeTime = item.getSellerPayTime();
                }
                if(item.getPayStatus() ==  Constants.PS_BUYER_PAY_DEPOSIT){
                    executeTime = item.getBuyerPayTime();
                }
                if(executeTime > System.currentTimeMillis()) {
                    calendar.setTimeInMillis(executeTime);
                    calendar.add(Calendar.MINUTE, period);
                    taskEvent.setExecuteTime(calendar.getTimeInMillis());
                }
            }
        }
    }
}
