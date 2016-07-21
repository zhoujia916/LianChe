package puzzle.lianche;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import puzzle.lianche.task.TimeoutTask;
import puzzle.lianche.task.utils.EventTypeEnum;
import puzzle.lianche.task.utils.TaskEvent;

import java.util.Date;

@WebAppConfiguration
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-*.xml"})
public class TaskTest {

    @Test
    public void releaseOrderTask(){

    }

}