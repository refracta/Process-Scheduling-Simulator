package kr.ac.koreatech.os.pss.scheduler.data;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.process.impl.EmptyProcess;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * 스케줄링 결과 데이터를 저장하는 클래스
 * 추가적인 결과 데이터는 dataMap에 저장한다. 내부적으로 dataMap을 위임하는 방식으로 구현되었다.
 *
 * @author refracta
 */
public class ScheduleData {
    /**
     * 코어 스케줄 맵을 dataMap에서 가져오기 위해서 내부에서 사용하는 String Key이다.
     */
    private static final String SCHEDULE = "schedule";
    /**
     * 추가적인 결과 데이터를 저장하는 맵
     */
    private final Map<Object, Object> dataMap = new HashMap<>();
    /**
     * 스케줄링이 종료된 프로세스 리스트를 저장하는 리스트
     */
    private final List<DefaultProcess> resultProcesses = new ArrayList<>();

    /**
     * Map&lt;AbstractCore, List&lt;DefaultProcess&gt;&gt; 꼴의 코어 스케줄 맵을 반환하는 획득자(Getter) 함수\
     * List&lt;T extends AbstractProcess&gt;의 예시: [DefaultProcess, DefaultProcess, EmptyProcess, EmptyProcess]
     *
     * @return 코어 스케줄 맵
     */
    public Map<AbstractCore, List<DefaultProcess>> getSchedule() {
        if (!containsKey(SCHEDULE)) {
            put(SCHEDULE, new HashMap<AbstractCore, List<DefaultProcess>>());
        }
        return (Map<AbstractCore, List<DefaultProcess>>) get(SCHEDULE);
    }

    /**
     * 주어진 시간의 프로세스들을 가져온다. (스케줄 맵을 세로로 잘라 가져오는 것과 동일함)
     *
     * @param time 가져올 프로세스의 시간
     * @return 해당 시간의 프로세스 리스트
     */
    public List<DefaultProcess> getProcesses(int time) {
        if (time >= 0) {
            return getSchedule().values().stream().map(coreSchedule -> (Optional<DefaultProcess>) (coreSchedule.size() > time ? Optional.of(coreSchedule.get(time)) : Optional.empty()))
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(toList());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 시작 시간부터 종료 시간까지의 프로세스들을 가져온다. (스케줄 맵을 세로로 잘라 가져오는 것과 동일함)
     *
     * @param startTime 시작 시간
     * @param endTime   종료 시간
     * @return 시작~종료 시간까지의 프로세스 리스트
     */
    public List<DefaultProcess> getProcesses(int startTime, int endTime) {
        List<DefaultProcess> processes = new ArrayList<>();
        for (int t = startTime; t <= endTime; t++) {
            processes.addAll(getProcesses(t));
        }
        return processes;
    }

    /**
     * 실제 프로세스가 스케줄링된 시간을 구한다.
     *
     * @param process 실제 스케줄링 시간을 구할 프로세스
     * @param time    현재 시간
     * @return 프로세스의 실제 스케줄링 시간
     */
    public int getRealBurstTime(DefaultProcess process, int time) {
        return (int) getProcesses(process.getArrivalTime(), time).parallelStream()
                .filter(p -> p.getId() == process.getId()).count();
    }

    /**
     * 스케줄 요청된 코어의 리스트를 반환한다.
     *
     * @return 코어 리스트
     */
    public List<AbstractCore> getCores() {
        return new ArrayList<>(getSchedule().keySet());
    }

    /**
     * 해당 코어의 스케줄 리스트를 반환한다.
     *
     * @param core 스케줄 리스트를 반환할 코어
     * @return 스케줄 리스트
     */
    public List<DefaultProcess> getCoreSchedule(AbstractCore core) {
        return getSchedule().get(core);
    }

    /**
     * 코어의 스케줄 맵을 기반으로 전력 사용량의 총계를 가져온다.
     * 프로세스가 EmptyProcess이면 대기 전력이 아니면 실행 전력으로 계산이 이루어진다.
     *
     * @param core 전력 사용량을 가져올 코어
     * @return 스케줄된 전력 사용량 총계
     */
    public double getPowerUsage(AbstractCore core) {
        List<DefaultProcess> coreSchedule = getCoreSchedule(core);
        return coreSchedule.stream().mapToDouble(process -> process instanceof EmptyProcess ? core.getStandbyPower() : core.getRunningPower()).sum();
    }

    /**
     * 모든 코어의 스케줄 맵을 기반으로 모든 코어의 전력 사용량 총계를를 더한 값을 가져온다.
     * 프로세스가 EmptyProcess이면 대기 전력이 아니면 실행 전력으로 계산이 이루어진다.
     *
     * @return 전체 코어의 전력 사용량
     */
    public double getTotalPowerUsage() {
        return getCores().stream().mapToDouble(this::getPowerUsage).sum();
    }

    /**
     * 스케쥴링에 사용된 코어 중 성능 코어의 개수를 가져온다.
     *
     * @return 스케쥴링에 사용된 성능 중 성능 코어의 개수
     */
    public int getNumPerformanceCores() {
        return getSchedule().keySet().stream().mapToInt(process -> process instanceof PerformanceCore ? 1 : 0).reduce(0, (acc, e) -> acc + e);
    }

    /**
     * 스케쥴링에 사용된 코어 중 효율 코어의 개수를 가져온다.
     *
     * @return 스케쥴링 사용된 효율 중 성능 코어의 개수
     */
    public int getNumEfficiencyCores() {
        return getSchedule().keySet().stream().mapToInt(process -> process instanceof EfficiencyCore ? 1 : 0).reduce(0, (acc, e) -> acc + e);
    }

    /**
     * 스케줄링이 종료된 프로세스 리스트를 저장하는 리스트를 가져온다.
     *
     * @return 스케줄링이 종료된 프로세스 리스트
     */
    public List<DefaultProcess> getResultProcesses() {
        return resultProcesses;
    }

    /**
     * 스케줄 요청된 프로세스들의 평균 응답 시간을 가져온다.
     *
     * @return 스케줄 요청된 프로세스들의 평균 응답 시간
     */
    public double getAverageResponseTime() {
        return getResultProcesses().stream().mapToDouble(DefaultProcess::getTurnaroundTime).average().getAsDouble();
    }

    public int size() {
        return dataMap.size();
    }

    public boolean isEmpty() {
        return dataMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return dataMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return dataMap.containsValue(value);
    }

    public Object get(Object key) {
        return dataMap.get(key);
    }

    public Object put(Object key, Object value) {
        return dataMap.put(key, value);
    }

    public Object remove(Object key) {
        return dataMap.remove(key);
    }

    public void putAll(Map<?, ?> m) {
        dataMap.putAll(m);
    }

    public void clear() {
        dataMap.clear();
    }

    public Set<Object> keySet() {
        return dataMap.keySet();
    }

    public Collection<Object> values() {
        return dataMap.values();
    }

    public Set<Map.Entry<Object, Object>> entrySet() {
        return dataMap.entrySet();
    }

    public Object getOrDefault(Object key, Object defaultValue) {
        return dataMap.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super Object, ? super Object> action) {
        dataMap.forEach(action);
    }

    public void replaceAll(BiFunction<? super Object, ? super Object, ?> function) {
        dataMap.replaceAll(function);
    }

    public Object putIfAbsent(Object key, Object value) {
        return dataMap.putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value) {
        return dataMap.remove(key, value);
    }

    public boolean replace(Object key, Object oldValue, Object newValue) {
        return dataMap.replace(key, oldValue, newValue);
    }

    public Object replace(Object key, Object value) {
        return dataMap.replace(key, value);
    }

    public Object computeIfAbsent(Object key, Function<? super Object, ?> mappingFunction) {
        return dataMap.computeIfAbsent(key, mappingFunction);
    }

    public Object computeIfPresent(Object key, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        return dataMap.computeIfPresent(key, remappingFunction);
    }

    public Object compute(Object key, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        return dataMap.compute(key, remappingFunction);
    }

    public Object merge(Object key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        return dataMap.merge(key, value, remappingFunction);
    }
}
