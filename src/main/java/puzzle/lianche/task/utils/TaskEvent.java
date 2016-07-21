package puzzle.lianche.task.utils;

public class TaskEvent implements Comparable<TaskEvent>  {
    private Long eventId;

    private String bizId;

    private EventTypeEnum eventType;

    private long executeTime;

    public TaskEvent(){

    }

    public TaskEvent(long eventId, String bizId, EventTypeEnum eventType){
        this.eventId = eventId;
        this.bizId = bizId;
        this.eventType = eventType;
    }


    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }


    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public EventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeEnum eventType) {
        this.eventType = eventType;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    @Override
    public int compareTo(TaskEvent o) {
        if (o == null) {
            return 0;
        }
        if(getEventId().longValue()==o.getEventId().longValue()
                && getBizId().equals(o.getBizId())
                && getEventType().equals(o.getEventType())
                && getExecuteTime() == (o.getExecuteTime())){
            return 0;
        }
        if (this.getExecuteTime() > (o.getExecuteTime())) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskEvent)) return false;

        TaskEvent taskEvent = (TaskEvent) o;

        if (executeTime != taskEvent.executeTime) return false;
        if (!bizId.equals(taskEvent.bizId)) return false;
        if (!eventId.equals(taskEvent.eventId)) return false;
        if (!eventType.equals(taskEvent.eventType)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventId.hashCode();
        result = 31 * result + bizId.hashCode();
        result = 31 * result + eventType.hashCode();
        result = 31 * result + (int) (executeTime ^ (executeTime >>> 32));
        return result;
    }
}
