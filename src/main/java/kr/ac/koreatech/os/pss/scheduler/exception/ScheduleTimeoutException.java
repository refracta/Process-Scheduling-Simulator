package kr.ac.koreatech.os.pss.scheduler.exception;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.List;

public class ScheduleTimeoutException extends RuntimeException {
    public ScheduleTimeoutException(int maxTime, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        super(getErrorMessage(maxTime, cores, processes, scheduleData));
    }

    private static String getErrorMessage(int maxTime, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        StringBuilder sb = new StringBuilder();
        sb.append("Schedule times cannot exceed " + maxTime + "." + "\n");
        sb.append("\t" + "Cores: " + cores + "\n");
        sb.append("\t" + "Processes: " + processes + "\n");
        sb.append("\t" + "ScheduleData: " + scheduleData);
        return sb.toString();
    }
}
