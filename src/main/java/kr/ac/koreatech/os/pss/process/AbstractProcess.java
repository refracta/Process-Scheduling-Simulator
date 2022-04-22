package kr.ac.koreatech.os.pss.process;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 추상 프로세스 클래스
 *
 * @author refracta
 */
public abstract class AbstractProcess implements Cloneable {
    /**
     * 프로세스의 정수 식별자(id)를 중복없이 자동 생성하기 위한 카운터
     */
    private static final AtomicInteger idCount = new AtomicInteger(-1);
    /**
     * 프로세스를 구별하기 위한 정수 식별자 (PID)
     */
    private int id;
    /**
     * 프로세스의 이름
     */
    private String name;

    public AbstractProcess(int id, String name) {
        this.setId(id);
        this.name = name;
    }

    public AbstractProcess(String name) {
        this.id = idCount.incrementAndGet();
        this.name = name;
    }

    public AbstractProcess() {
        this.id = idCount.incrementAndGet();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > idCount.intValue()) {
            idCount.set(id);
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 해당 프로세스가 종료된 프로세스인지의 여부를 반환한다. 자식 클래스에서 구현하여 사용해야 한다.
     *
     * @return 프로세스가 종료되었는지 여부 (부울)
     */
    public abstract boolean isFinished();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractProcess that = (AbstractProcess) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "AbstractProcess{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", finished=" + isFinished() +
                '}';
    }

    @Override
    public AbstractProcess clone() {
        AbstractProcess clone = null;
        try {
            clone = (AbstractProcess) super.clone();
            clone.id = id;
            clone.name = name;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
