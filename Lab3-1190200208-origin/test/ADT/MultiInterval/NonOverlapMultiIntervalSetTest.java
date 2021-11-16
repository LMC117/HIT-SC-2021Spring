package ADT.MultiInterval;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonOverlapMultiIntervalSetTest {

    /**
     * Tests that assertions are enabled.
     */
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    @Test
    // 测试允许重叠的情况
    public void testCheckOverlap1(){
        MultiIntervalSet<String> MultiIntervalSet = new CommonMultiIntervalSet<>();

        MultiIntervalSet.insert(10,200,"a");
        MultiIntervalSet.insert(20,300,"a");
        MultiIntervalSet.insert(400,500,"a");

        assertTrue(MultiIntervalSet.checkOverlap());
    }

    @Test
    // 测试不允许重叠的情况
    public void testCheckOverlap2(){
        MultiIntervalSet<String> MultiIntervalSet = new CommonMultiIntervalSet<>();

        MultiIntervalSet.insert(10,200,"a");
        MultiIntervalSet.insert(201,300,"a");
        MultiIntervalSet.insert(400,500,"a");

        assertFalse(MultiIntervalSet.checkOverlap());
    }
}
