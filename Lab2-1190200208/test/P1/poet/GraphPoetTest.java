/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {

    // Testing strategy

    /*
     * Testing strategy
     *
     * testPoem():
     * 采用不同的语料，测试能否得到正确的输出
     *
     * testToString():
     * 用不同用例测试toString方法的正确性
     */

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // tests

    /*
     * testPoem():
     * 采用不同的语料，测试能否得到正确的输出
     */
    @Test
    public void testPoem1() throws IOException {
        String filePath = "src/P1/poet/mugar-omni-theater.txt";
        String input = "Test the system.";
        String AccOutput = "Test of the system.";
        GraphPoet graphPoet = new GraphPoet(new File(filePath));
        assertEquals(AccOutput, graphPoet.poem(input));
    }

    @Test
    public void testPoem2() throws IOException {
        String filePath = "src/P1/poet/mugar-omni-theater.txt";
        String input = "Test of Mugar Omni Theater system.";
        String AccOutput = "Test of the Mugar Omni Theater sound system.";
        GraphPoet graphPoet = new GraphPoet(new File(filePath));
        assertEquals(AccOutput, graphPoet.poem(input));
    }

    @Test
    public void testPoem3() throws IOException {
        String filePath = "src/P1/poet/Where no man has gone before.txt";
        String input = "Seek to explore new and exciting synergies!";
        String AccOutput = "Seek to explore strange new life and exciting synergies!";
        GraphPoet graphPoet = new GraphPoet(new File(filePath));
        assertEquals(AccOutput, graphPoet.poem(input));
    }

    @Test
    public void testPoem4() throws IOException {
        String filePath = "src/P1/poet/Sonnet I to X.txt";
        String input = "Pointing to each his thunder, rain and wind.";
        String AccOutput = "Pointing to each in his thunder, rain and wind.";
        GraphPoet graphPoet = new GraphPoet(new File(filePath));
        assertEquals(AccOutput, graphPoet.poem(input));
    }

    @Test
    public void testPoem5() throws IOException {
        String filePath = "src/P1/poet/hello goodbye.txt";
        String input = "hello, goodbye!!!!!";
        String AccOutput = "hello, hello goodbye!!!!!";
        GraphPoet graphPoet = new GraphPoet(new File(filePath));
        assertEquals(AccOutput, graphPoet.poem(input));
    }

    /*
     * testToString():
     * 用不同用例测试toString方法的正确性
     */
    @Test
    public void testToString() throws IOException {
        String filePath = "src/P1/poet/mugar-omni-theater.txt";
        GraphPoet graphPoet = new GraphPoet(new File(filePath));
        String AccOutput = "this->is (1)\n" +
                "is->a (1)\n" +
                "a->test (1)\n" +
                "test->of (1)\n" +
                "of->the (1)\n" +
                "the->mugar (1)\n" +
                "mugar->omni (1)\n" +
                "omni->theater (1)\n" +
                "theater->sound (1)\n" +
                "sound->system (1)\n";
        assertEquals(AccOutput, graphPoet.toString());
    }
}
