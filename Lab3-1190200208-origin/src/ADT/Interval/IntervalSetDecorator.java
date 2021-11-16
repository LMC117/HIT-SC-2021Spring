package ADT.Interval;

import java.util.Set;

public abstract class IntervalSetDecorator<L> implements IntervalSet<L> {
    protected final IntervalSet<L> intervalSet;

    // Constructor
    public IntervalSetDecorator(IntervalSet<L> intervalSet) {
        this.intervalSet = intervalSet;
    }

    @Override
    public void insert(long start, long end, L label) {
        intervalSet.insert(start, end, label);
    }

    @Override
    public Set<L> labels() {
        return intervalSet.labels();
    }

    @Override
    public boolean remove(L label) {
        return intervalSet.remove(label);
    }

    @Override
    public long start(L label) {
        return intervalSet.start(label);
    }

    @Override
    public long end(L label) {
        return intervalSet.end(label);
    }

    @Override
    public boolean isEmpty() {
        return intervalSet.isEmpty();
    }

    @Override
    public boolean checkBlank() {
        return intervalSet.checkBlank();
    }

    @Override
    public boolean checkOverlap() {
        return intervalSet.checkOverlap();
    }
}
