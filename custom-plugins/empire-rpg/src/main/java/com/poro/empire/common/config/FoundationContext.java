package com.poro.empire.common.config;

import com.poro.empire.common.db.ConnectionProvider;
import com.poro.empire.common.db.DatabaseBootstrapper;
import com.poro.empire.common.db.TransactionHelper;
import com.poro.empire.common.logging.CommonPluginLogger;
import com.poro.empire.common.registry.RegistryBootstrapper;
import com.poro.empire.common.time.TimeProvider;

public record FoundationContext(
        CommonConfig config,
        TimeProvider timeProvider,
        CommonPluginLogger logger,
        ConnectionProvider connectionProvider,
        TransactionHelper transactionHelper,
        RegistryBootstrapper registryBootstrapper,
        DatabaseBootstrapper databaseBootstrapper
) {
}
