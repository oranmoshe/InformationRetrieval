package com.informationretrieval;

import java.util.Comparator;

public class CustomComparator implements Comparator<indexObejct> {
    @Override
    public int compare(indexObejct o1, indexObejct o2) {
        return o1.name.compareTo(o2.name);
    }
}