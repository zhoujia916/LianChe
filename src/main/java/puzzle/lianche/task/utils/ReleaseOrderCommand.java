package puzzle.lianche.task.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReleaseOrderCommand implements ICommand{

    private Logger logger = LoggerFactory.getLogger(ReleaseOrderCommand.class);

    @Override
    public void exec(TaskEvent taskEvent, ITaskFuture taskFuture) {
        try{

        }
        finally {
            taskFuture.finish();
        }
    }

    @Override
    public String getEventType() {
        return null;
    }
}
