package puzzle.lianche.task.utils;

public interface ICommand {

    public void exec(TaskEvent taskEvent, ITaskFuture taskFuture);

    public String getEventType();
}
