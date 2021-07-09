package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.functions;

import ch.qos.logback.core.db.dialect.OracleDialect;
import ch.qos.logback.core.db.dialect.PostgreSQLDialect;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.spi.MetadataBuilderInitializer;
import org.hibernate.dialect.*;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.type.StandardBasicTypes;

@Slf4j
public class ApigenFunctionsMetadataBuilderInitializer implements MetadataBuilderInitializer {

    public static final String REGEXP_FUNCTION = "apigen_regexp";
    private static final String REGEXP_FUNCTION_STANDARD = "REGEXP_LIKE(?1,?2)";
    private static final String REGEXP_FUNCTION_POSTGRESQL = "?1 ~ ?2";

    @Override
    public void contribute(MetadataBuilder metadataBuilder, StandardServiceRegistry serviceRegistry) {

        final JdbcEnvironment jdbcEnvironment = serviceRegistry.getService(JdbcEnvironment.class);
        final Dialect dialect = jdbcEnvironment.getDialect();
        final Class dialectClass = dialect.getClass();

        if (PostgreSQLDialect.class.isAssignableFrom(dialectClass) || PostgreSQL81Dialect.class.isAssignableFrom(dialectClass)) {
            metadataBuilder.applySqlFunction(REGEXP_FUNCTION, new SQLFunctionTemplate(StandardBasicTypes.BOOLEAN, REGEXP_FUNCTION_POSTGRESQL));
            log.debug("Apigen functions initialized for PostgreSQL in dialect: {}", dialectClass);
        } else if (H2Dialect.class.isAssignableFrom(dialectClass)) {
            metadataBuilder.applySqlFunction(REGEXP_FUNCTION, new SQLFunctionTemplate(StandardBasicTypes.BOOLEAN, REGEXP_FUNCTION_STANDARD));
            log.debug("Apigen functions initialized for H2 in dialect: {}", dialectClass);
        } else if (OracleDialect.class.isAssignableFrom(dialectClass) || Oracle8iDialect.class.isAssignableFrom(dialectClass)) {
            metadataBuilder.applySqlFunction(REGEXP_FUNCTION, new SQLFunctionTemplate(StandardBasicTypes.BOOLEAN, REGEXP_FUNCTION_STANDARD));
            log.debug("Apigen functions initialized for Oracle in dialect: {}", dialectClass);
        } else if (MySQLDialect.class.isAssignableFrom(dialectClass)) {
            metadataBuilder.applySqlFunction(REGEXP_FUNCTION, new SQLFunctionTemplate(StandardBasicTypes.BOOLEAN, REGEXP_FUNCTION_STANDARD));
            log.debug("Apigen functions initialized for MySQL in dialect: {}", dialectClass);
        } else {
            log.error("Dialect {} not supported natively by Apigen, functions {} not defined, consult documentation to extend your own dialect", REGEXP_FUNCTION, dialectClass);
        }
    }
}
