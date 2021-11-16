package ADT.MultiInterval;

public class NonPeriodicMultiIntervalSet<L> extends MultiIntervalSetDecorator<L> implements MultiIntervalSet<L> {

    // Constructor
    public NonPeriodicMultiIntervalSet(MultiIntervalSet<L> multiIntervalSet) {
        super(multiIntervalSet);
    }

    public boolean checkPeriodic() {
        return false;
    }
}
