package API;

import ADT.Interval.CommonIntervalSet;
import ADT.Interval.IntervalSet;
import ADT.MultiInterval.CommonMultiIntervalSet;
import ADT.MultiInterval.MultiIntervalSet;
import org.junit.Test;
import API.APIs;

import static org.junit.Assert.assertEquals;

public class APIsTest {

    /**
     * Tests that assertions are enabled.
     */
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    @Test
    public void testSimilarity() {
        APIs<String> api = new APIs<>();
        MultiIntervalSet<String> s1 = new CommonMultiIntervalSet<>();
        MultiIntervalSet<String> s2 = new CommonMultiIntervalSet<>();

        s1.insert(0, 5, "A");
        s1.insert(10, 20, "B");
        s1.insert(20, 25, "A");
        s1.insert(25, 30, "B");

        s2.insert(0, 5, "C");
        s2.insert(10, 20, "B");
        s2.insert(20, 35, "A");

        assertEquals(0.42857, api.Similarity(s1, s2), 1e-5);
    }

    @Test
    // 测试intervalSet的冲突比例
    public void testCalcConflictRatio1() {
        APIs<String> api = new APIs<>();
        IntervalSet<String> intervalSet = new CommonIntervalSet<>();

        intervalSet.insert(1, 100, "A");
        intervalSet.insert(1, 10, "B");
        intervalSet.insert(51, 55, "C");
        intervalSet.insert(96, 100, "D");

        assertEquals(0.2,api.calcConflictRatio(intervalSet),1e-5);
    }

    @Test
    // 测试MultiIntervalSet的冲突比例
    public void testCalcConflictRatio2() {
        APIs<String> api = new APIs<>();
        MultiIntervalSet<String> multiIntervalSet = new CommonMultiIntervalSet<>();

        multiIntervalSet.insert(1, 100, "A");
        multiIntervalSet.insert(1, 10, "B");
        multiIntervalSet.insert(51, 55, "C");
        multiIntervalSet.insert(96, 100, "C");

        assertEquals(0.2,api.calcConflictRatio(multiIntervalSet),1e-5);
    }

    @Test
    // 测试intervalSet的空闲时间比例
    public void testCalcFreeTimeRatio1() {
        APIs<String> api = new APIs<>();
        IntervalSet<String> intervalSet = new CommonIntervalSet<>();

        intervalSet.insert(1, 2, "A");
        intervalSet.insert(4, 5, "B");

        assertEquals(0.2, api.calcFreeTimeRatio(intervalSet), 1e-5);
    }

    @Test
    // 测试MultiIntervalSet的空闲时间比例
    public void testCalcFreeTimeRatio2() {
        APIs<String> api = new APIs<>();
        MultiIntervalSet<String> multiIntervalSet = new CommonMultiIntervalSet<>();

        multiIntervalSet.insert(1, 1, "A");
        multiIntervalSet.insert(2, 2, "A");
        multiIntervalSet.insert(4, 5, "B");

        assertEquals(0.2, api.calcFreeTimeRatio(multiIntervalSet), 1e-5);
    }
}
