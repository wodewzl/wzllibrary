
package com.bs.bsims.model;

import java.util.Comparator;

public class SortComparatorVO1 implements Comparator<DangBasicUserInfo> {
    public int compare(DangBasicUserInfo o1, DangBasicUserInfo o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
