package kr.ac.koreatech.os.test.process;

import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * DefaultProcess 함수 테스트 클래스
 *
 * @author refracta
 */
public class DefaultProcessTest {
    @Test
    public void contiguousProcessesTest() {
        DefaultProcess defaultProcess = new DefaultProcess(0, 5);
        List<DefaultProcess> processes = defaultProcess.getContiguousProcesses(3, 2);
        System.out.println(processes.size());
        System.out.println(processes);
    }
}
