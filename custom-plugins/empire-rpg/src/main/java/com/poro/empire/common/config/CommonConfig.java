package com.poro.empire.common.config;

import java.nio.file.Path;
import java.time.ZoneId;
import java.util.Objects;

public record CommonConfig(
        Path sqlitePath,
        Path seedPath,
        ZoneId defaultZoneId
) {
    public CommonConfig {
        Objects.requireNonNull(sqlitePath, "sqlitePath");
        Objects.requireNonNull(seedPath, "seedPath");
        Objects.requireNonNull(defaultZoneId, "defaultZoneId");
    }

    public String sqliteJdbcUrl() {
        return "jdbc:sqlite:" + sqlitePath.toAbsolutePath();
    }
}
