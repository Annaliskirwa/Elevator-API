package com.lendtech.elevator.util;

import java.util.Iterator;

public class NumberIterator implements Iterator<Integer> {
    private int current;
    private final int end;
    private final int step;

    public NumberIterator(int start, int end, int step) {
        this.current = start;
        this.end = end;
        this.step = step;
    }

    public NumberIterator(int start, int end) {
        this(start, end, start <= end ? 1 : -1);
    }

    @Override
    public boolean hasNext() {
        if (step > 0) {
            return current <= end;
        } else {
            return current >= end;
        }
    }

    @Override
    public Integer next() {
        int result = current;
        current += step;
        return result;
    }
}
