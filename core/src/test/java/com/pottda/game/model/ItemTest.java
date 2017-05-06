package com.pottda.game.model;

import com.pottda.game.Util;
import com.pottda.game.model.items.DemoItemA;
import com.pottda.game.model.items.DemoItemB;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test the spatial properties of the Item class
 */
public class ItemTest {

    private Item a;
    private Item b;

    @Before
    public void setUp() {
        a = new DemoItemA() {
        };
        a.init();
        a.orientation = 2;
        a.x = 2;
        a.y = 5;

        b = new DemoItemB() {
        };
        b.init();
        b.orientation = 3;
        b.x = 8;
        b.y = 4;
    }

    @Test
    public void rotateTest() {

        List<Integer> positionsA = a.getPositionsAsIntegers(10);
        Integer outputA = a.getOutputAsInteger(10);
        List<Integer> posoitionsB = b.getPositionsAsIntegers(10);
        Integer outputB = b.getOutputAsInteger(10);

        // Create the expected values
        List<Integer> rotatedA = new ArrayList<Integer>();
        rotatedA.addAll(Arrays.asList(51, 52, 41, 42));
        Util.sortIntegerList(rotatedA, true);   // Initially unsorted

        List<Integer> rotatedB = new ArrayList<Integer>();
        rotatedB.addAll(Arrays.asList(48, 38, 39, 29));
        Util.sortIntegerList(rotatedB, true);

        Assert.assertEquals(rotatedA, positionsA);
        Assert.assertEquals(rotatedB, posoitionsB);
        Assert.assertEquals(outputA, Integer.valueOf(40));
        Assert.assertEquals(outputB, Integer.valueOf(19));
    }
}
