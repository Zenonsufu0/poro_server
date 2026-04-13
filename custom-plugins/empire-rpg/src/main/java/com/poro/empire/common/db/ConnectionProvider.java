package com.poro.empire.common.db;

import com.poro.empire.common.result.Result;

import java.sql.Connection;

public interface ConnectionProvider {
    Result<Connection> getConnection();
}
