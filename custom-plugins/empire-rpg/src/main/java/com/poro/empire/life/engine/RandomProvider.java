package com.poro.empire.life.engine;

public interface RandomProvider {
    double nextDouble();

    int nextInt(int boundExclusive);
}
