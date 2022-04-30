package kr.ac.koreatech.os.pss.scheduler;

import java.util.Arrays;

public enum ScheduleMethod {
    FCFS("FCFS"), RR("RR"), SPN("SPN"), SRTN("SRTN"), HRRN("HRRN"), Custom1("Custom 1"), Custom2("Custom 2");

    private final String value;

    ScheduleMethod(String value) {
        this.value = value;
    }

    public static ScheduleMethod getEnum(String value) {
        return Arrays.stream(ScheduleMethod.values()).filter(e -> e.getValue() == value).findFirst().get();
    }

    public String getValue() {
        return value;
    }
}