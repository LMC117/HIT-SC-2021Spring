package SubADT.ProcessSchedule;

import java.util.Objects;

public class Process {
    private final long ID; // 进程ID
    private final String name; // 进程名称
    private final long minTime; // 最短执行时间
    private final long maxTime; // 最长执行时间

    public Process(long ID, String name, long minTime, long maxTime) {
        this.ID = ID;
        this.name = name;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public long getMinTime() {
        return minTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Process process = (Process) o;
        return ID == process.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
