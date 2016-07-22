package puzzle.lianche.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import puzzle.lianche.task.utils.*;
import java.util.*;

public class TimeoutTask {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private TreeSet<TaskEvent> task = new TreeSet<TaskEvent>();
    private volatile boolean taskCheckRunning = false;
    private static Map<EventTypeEnum, ICommand> commandMap;

    private static TimeoutTask instance;

    private TaskCheck taskCheck;

    private TimeoutTask(){

    }

    public static synchronized TimeoutTask newInstance(){
        if(instance != null){
            instance = new TimeoutTask();
        }
        return instance;
    }

    static {
        commandMap = new HashMap<EventTypeEnum, ICommand>();
        commandMap.put(EventTypeEnum.AUTO_RELEASE_ORDER, new ReleaseOrderCommand());
    }

    public void register(TaskEvent taskEvent) {
        if (taskEvent == null) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info("注册超时任务:" + taskEvent.getEventId());
        }
        if (!task.isEmpty()) {
            TaskEvent first = task.first();
            if (taskEvent.getExecuteTime() < first.getExecuteTime() && taskCheck != null) {
                taskCheck.cancel();
            }
        }
        task.add(taskEvent);
        startTaskCheck();

    }

    public synchronized void unRegister(TaskEvent taskEvent) {
        if (taskEvent == null) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info("取消超时任务,taskEvent=" + taskEvent.getEventId());
        }
        if (!task.remove(taskEvent)) {
            logger.warn("取消超时任务，忽略，任务已经处理,taskEvent=" + taskEvent.getEventId());
            return;
        }
        logger.warn("取消超时任务成功，taskEvent=" + taskEvent.getEventId());
        //如果任务是当前在进行中
        if (taskCheck != null && taskCheck.getCurrentTaskEvent().equals(taskEvent)) {
            taskCheck.cancel();
            startTaskCheck();
            logger.warn("取消超时任务成功，当前任务在进行中，taskEvent=" + taskEvent.getEventId());
        }

    }

    public synchronized void stop(TaskEvent taskEvent) {
        if (taskEvent == null) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info("停止超时任务,taskEvent=" + taskEvent.getEventId());
        }

        //如果任务是当前在进行中
        if (taskCheck != null && taskCheck.getCurrentTaskEvent().equals(taskEvent)) {
            taskCheck.cancel();
            startTaskCheck();
            logger.warn("停止超时任务成功，当前任务在进行中，taskEvent=" + taskEvent.getEventId());
        } else {
            task.remove(taskEvent);
            logger.warn("停止超时任务成功，taskEvent=" + taskEvent.getEventId());
        }

    }

    public synchronized void startTaskCheck() {
        if (!taskCheckRunning) {
            taskCheckRunning = true;
            taskCheck = new TaskCheck();
            taskCheck.setDaemon(true);
            taskCheck.start();
        }
    }


    public class TaskCheck extends Thread {

        private Timer timer = new Timer();
        private TaskEvent currentTaskEvent;

        public TaskEvent getCurrentTaskEvent() {
            return currentTaskEvent;
        }

        public void cancel() {
            try {
                if (timer != null) {
                    timer.cancel();
                    taskCheckRunning = false;
                    logger.info("task cancel,task=" + currentTaskEvent.getEventId());
                }
                this.interrupt();
            } catch (Exception e) {
            }
        }

        @Override
        public void run() {
            if (task.isEmpty()) {
                taskCheckRunning = false;
                return;
            }

            final TaskEvent taskEvent = task.first();
            currentTaskEvent = taskEvent;
            long time = taskEvent.getExecuteTime();
            if (time - System.currentTimeMillis() <= 0) {
                ICommand cmd = commandMap.get(taskEvent.getEventType());
                try {
                    cmd.exec(taskEvent, new ITaskFuture() {
                        @Override
                        public void finish() {
                            taskCheckRunning = false;
                            task.pollFirst();
                            startTaskCheck();

                        }
                    });
                } catch (Exception e) {
                }
            } else {
                try {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ICommand cmd = commandMap.get(taskEvent.getEventType());
                            cmd.exec(taskEvent, new ITaskFuture() {
                                @Override
                                public void finish() {
                                    taskCheckRunning = false;
                                    task.pollFirst();
                                    startTaskCheck();
                                }
                            });
                        }
                    }, time - System.currentTimeMillis());

                } catch (Exception e) {
                }
            }
        }
    }


    public void setCommandMap(Map<EventTypeEnum, ICommand> commandMap) {
        this.commandMap = commandMap;
    }
}
