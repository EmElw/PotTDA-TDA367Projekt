package com.pottda.game;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * A collection of various hack-y methods
 */
public class Util {

    /**
     * Sorts a {@code List<Integer>} (alters the list directly)
     * <p>
     * Because of the API levels (android version compatability) of the project, collections
     * don't have built-in sorting methods, which is why this method was created
     *
     * @param list      {@code List<Integer>}
     * @param ascending {@code boolean}
     */
    public static void sortIntegerList(List<Integer> list, final boolean ascending) {
        Integer[] array = new Integer[list.size()];
        array = list.toArray(array);
        Arrays.sort(array, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (o1.compareTo(o2)) *
                        (ascending ? 1 : -1); // If not descending, reverse order
            }
        });
        ListIterator<Integer> it = list.listIterator();
        for (Integer i : array) {
            it.next();
            it.set(i);
        }
    }


    /**
     * Rotates a 2D point by n * pi/2 rad or n * 90 degrees around (0,0)
     * <p>
     * Home-brew rotation function (because vecmath doesn't support 2D matrices?)
     *
     * @param p an {@code int[]} of size 2, where int[0] = x, int[1] = y
     * @param n the rotation, expressed as n multiples of pi/2 rad
     * @return {@code int[]} of size 2
     */
    public int[] rotate(int[] p, int n) {
        int[] returnValue = new int[2];
        returnValue[0] = a[n] * p[0] + b[n] * p[1];
        returnValue[1] = c[n] * p[0] + a[n] * p[1];
        return returnValue;
    }

    /**
     * Rotation matrix for r * pi/2
     * R = [ a[r] b[r] ; c[r] a[r]]
     */
    private static final int[] a = {1, 0, -1, 0};
    private static final int[] b = {0, -1, 0, 1};
    private static final int[] c = {0, 1, 0, -1};
}
