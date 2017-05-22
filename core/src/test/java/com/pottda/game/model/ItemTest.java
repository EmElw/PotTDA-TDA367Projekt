package com.pottda.game.model;

import com.pottda.game.model.items.DemoItemA;
import com.pottda.game.model.items.DemoItemB;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        a.setOrientation(2);
        a.setX(2);
        a.setY(5);

        b = new DemoItemB() {
        };
        b.init();
        b.setOrientation(3);
        b.setX(8);
        b.setY(4);
    }

    @Test
    public void rotateTest() {

        List<Integer> positionsA = a.getPositionsAsIntegers(10);
        List<Integer> outputA = a.getOutputAsInteger(10);
        Collections.sort(positionsA);

        List<Integer> positionsB = b.getPositionsAsIntegers(10);
        List<Integer> outputB = b.getOutputAsInteger(10);
        Collections.sort(positionsB);

        // Create the expected values
        List<Integer> rotatedA = new ArrayList<Integer>();
        rotatedA.addAll(Arrays.asList(51, 52, 41, 42));
        Collections.sort(rotatedA);

        List<Integer> rotatedB = new ArrayList<Integer>();
        rotatedB.addAll(Arrays.asList(48, 38, 39, 29));
        Collections.sort(rotatedB);

        Assert.assertEquals(rotatedA, positionsA);
        Assert.assertEquals(rotatedB, positionsB);
        Assert.assertEquals(outputA.get(0), Integer.valueOf(40));
        Assert.assertEquals(outputB.get(0), Integer.valueOf(19));
    }
}
