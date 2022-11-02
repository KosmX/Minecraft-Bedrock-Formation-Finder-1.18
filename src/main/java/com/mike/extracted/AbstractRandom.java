package com.mike.extracted;

public interface AbstractRandom {
    public AbstractRandom derive();

    public RandomDeriver createRandomDeriver();

    public void setSeed(long var1);
    default int nextInt() {
        return this.nextInt(32);
    }


    public int nextInt(int var1);

    default public int nextBetween(int min, int max) {
        return this.nextInt(max - min + 1) + min;
    }

    default long nextLong() {
        int i = this.nextInt(32);
        int j = this.nextInt(32);
        long l = (long)i << 32;
        return l + (long)j;
    }

    default boolean nextBoolean() {
        return this.nextInt(1) != 0;
    }


    default float nextFloat() {
        return (float)this.nextInt(24) * 5.9604645E-8F;
    }

    default double nextDouble() {
        int i = this.nextInt(26);
        int j = this.nextInt(27);
        long l = ((long)i << 27) + (long)j;
        return (double)l * 1.1102230246251565E-16;
    }

    public double nextGaussian();

    default public void skip(int count) {
        for (int i = 0; i < count; ++i) {
            this.nextInt();
        }
    }
}
