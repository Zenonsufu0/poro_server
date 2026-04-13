package com.poro.empire.common.db;

import com.poro.empire.common.result.Result;

import java.sql.Connection;

public final class NoopMigrationEntryPoint implements MigrationEntryPoint {
    @Override
    public Result<Void> migrate(Connection connection) {
        return Result.success();
    }
}
