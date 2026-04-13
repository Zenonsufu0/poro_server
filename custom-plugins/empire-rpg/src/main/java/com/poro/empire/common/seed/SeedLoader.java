package com.poro.empire.common.seed;

import com.poro.empire.common.result.Result;

import java.util.List;

public interface SeedLoader<T> {
    String name();

    Result<List<T>> load();
}
