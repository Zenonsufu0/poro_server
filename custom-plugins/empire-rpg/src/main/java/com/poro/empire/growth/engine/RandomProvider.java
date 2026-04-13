package com.poro.empire.growth.engine;

public interface RandomProvider {
    double nextDouble();

    int nextInt(int boundExclusive);
}
