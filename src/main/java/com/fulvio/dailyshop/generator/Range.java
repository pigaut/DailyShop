package com.fulvio.dailyshop.generator;

import java.util.Random;

public class Range extends Amount {

    private final Integer y;

    public Range(int x, Integer y) {
        super(x);
        this.y = y;
    }

    @Override
    public int get() {
        Random random = new Random();
        return random.nextInt(y + 1) + x;
    }

}
