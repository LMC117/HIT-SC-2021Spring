package ADT.MultiInterval;

import ADT.Interval.IntervalSet;

import java.util.Set;

public abstract class MultiIntervalSetDecorator<L> implements MultiIntervalSet<L> {
    protected final MultiIntervalSet<L> multiIntervalSet;

    // Constructor
    protected MultiIntervalSetDecorator(MultiIntervalSet<L> multiIntervalSet) {
        this.multiIntervalSet = multiIntervalSet;
    }

    @Override
    public boolean isEmpty() {
        return multiIntervalSet.isEmpty();
    }

    @Override
    public void insert(long start, long end, L label) {
        multiIntervalSet.insert(start,end,label);
    }

    @Override
    public Set<L> labels() {
        return multiIntervalSet.labels();
    }

    @Override
    public boolean remove(L label) {
        return multiIntervalSet.remove(label);
    }

    @Override
    public IntervalSet<Integer> intervals(L label) {
        return multiIntervalSet.intervals(label);
    }

    @Override
    public boolean checkBlank() {
        return multiIntervalSet.checkBlank();
    }

    @Override
    public boolean checkOverlap() {
        return multiIntervalSet.checkOverlap();
    }

    @Override
    public boolean checkPeriodic() {
        return multiIntervalSet.checkPeriodic();
    }
}
