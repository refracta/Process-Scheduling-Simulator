
package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.scheduler.impl.MyScheduler_yk;
import kr.ac.koreatech.os.test.utils.TestScheduleUtils;
import org.junit.jupiter.api.Test;

public class MyScheduler_ykTest {

    @Test
    public void test1WithTQ2AndRQN5(){
        TestScheduleUtils.runWithDefaultTestSet(new MyScheduler_yk(2, 3));
    }

}


