package puzzle.lianche.task;

import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

@Component
public class UploadUserTask {

    @Scheduled(cron="0/5 * * * * ?")
    public void upload(){

    }
}
