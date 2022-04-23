package kr.ac.koreatech.os.pss.scheduler.utils;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.AbstractProcess;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 스케줄 결과 확인을 위한 출력 유틸리티
 *
 * @author refracta
 */
public class PrintUtils {
    /**
     * 좌측 패딩 처리용 함수
     *
     * @param target 패딩 처리할 문자열
     * @param length 문자열 최대 길이
     * @return 공백 패딩 처리된 문자열
     */
    private static String padLeftZeros(String target, int length) {
        if (target.length() >= length) {
            return target;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - target.length()) {
            sb.append(' ');
        }
        sb.append(target);

        return sb.toString();
    }

    /**
     * 스케줄 데이터를 받아 스케줄 결과와 전력 사용량을 출력한다.
     *
     * @param scheduleData     출력할 스케줄 데이터
     * @param onlyCoreSchedule 코어 스케줄 결과만 출력할지에 대한 여부 (거짓일 경우 전력 사용량도 출력됨)
     */
    public static void printScheduleData(ScheduleData scheduleData, boolean onlyCoreSchedule) {
        List<AbstractCore> cores = new ArrayList<>(scheduleData.getCores());
        cores.sort(Comparator.comparingInt(AbstractCore::getId));
        for (int i = 0; i < cores.size(); i++) {
            AbstractCore currentCore = cores.get(i);
            List<DefaultProcess> coreSchedule = scheduleData.getSchedule().get(currentCore);
            double powerUsage = scheduleData.getPowerUsage(currentCore);
            System.out.println("Core[" + i + ":" + padLeftZeros(currentCore.getClass().getSimpleName().replace("Core", ""), 11) + "]" + ": " + "[" + coreSchedule.stream().map(AbstractProcess::getName).collect(Collectors.joining(", ")) + "]: " + powerUsage + "W");
        }
        if (!onlyCoreSchedule) {
            System.out.println("Total power usage: " + scheduleData.getTotalPowerUsage() + "W");
            System.out.println("Result processes:");
            System.out.println(scheduleData.getResultProcesses().stream().map(p -> "\t" + p.toString()).collect(Collectors.joining("\n")));
            System.out.println("Average response time: " + scheduleData.getAverageResponseTime());
        }
    }

    /**
     * 스케줄 데이터를 받아 스케줄 결과와 전력 사용량을 출력한다.
     *
     * @param scheduleData 출력할 스케줄 데이터
     */
    public static void printScheduleData(ScheduleData scheduleData) {
        printScheduleData(scheduleData, false);
    }
}
