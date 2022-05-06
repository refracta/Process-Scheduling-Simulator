package kr.ac.koreatech.os.pss.scheduler;

import java.util.Arrays;

public enum ScheduleMethod {
    FCFS("FCFS (First Come First Service)"), RR("RR (Round Robin)"), SPN("SPN (Shortest Process Next)"), SRTN("SRTN (Shortest Remaining Time Next)"), HRRN("HRRN (High Response Ratio Next)"), RR2Q("RR2Q (RR with Run-Ready Queue)"), GMRL("GMRL (Give More Rice cake to Loser)");

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
