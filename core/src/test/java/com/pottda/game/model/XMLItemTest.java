package com.pottda.game.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class XMLItemTest {
    private XMLItem xmlItem;
    private static final String className = "testClass";
    private static final int x = 10;
    private static final int y = 13;
    private static final int orientation = 2;

    @Before
    public void setUp() throws Exception {
        this.xmlItem = new XMLItem(className, x, y, orientation);
    }

    @Test
    public void getClassName() throws Exception {
        assertEquals(className, xmlItem.getClassName());
    }

    @Test
    public void getX() throws Exception {
        assertEquals(x, xmlItem.getX());
    }

    @Test
    public void getY() throws Exception {
        assertEquals(y, xmlItem.getY());
    }

    @Test
    public void getOrientation() throws Exception {
        assertEquals(orientation, xmlItem.getOrientation());
    }

    @Test
    public void random() {
        for (int i = 0; i < 100; i++) {
            for (int j = 100; j > 0; j--) {
                for (int h = 0; h < 4; h++) {
                    int x2 = (int) (Math.random() * i + j + h);
                    int y2 = (int) (Math.random() * j + i + h);
                    int o = (int) (Math.random() * h);

                    xmlItem = new XMLItem(null, x2, y2, o);

                    assertEquals(x2, xmlItem.getX());
                    assertEquals(y2, xmlItem.getY());
                    assertEquals(o, xmlItem.getOrientation());

                    assertFalse(xmlItem.getX() < 0);
                    assertFalse(xmlItem.getY() < 0);
                    assertFalse(xmlItem.getOrientation() < 0);
                }
            }
        }
    }

}