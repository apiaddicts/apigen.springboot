package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.functions;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.spi.MetadataBuilderInitializer;
import org.hibernate.dialect.*;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.query.sqm.function.FunctionKind;
import org.hibernate.query.sqm.function.PatternBasedSqmFunctionDescriptor;
import org.hibernate.query.sqm.produce.function.internal.PatternRenderer;

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

        String regexFunc = null;
        if (PostgreSQLDialect.class.isAssignableFrom(dialectClass)) {
            regexFunc = REGEXP_FUNCTION_POSTGRESQL;
            log.debug("Apigen functions initialized for PostgreSQL in dialect: {}", dialectClass);
        } else if (H2Dialect.class.isAssignableFrom(dialectClass)) {
            regexFunc = REGEXP_FUNCTION_STANDARD;
            log.debug("Apigen functions initialized for H2 in dialect: {}", dialectClass);
        } else if (OracleDialect.class.isAssignableFrom(dialectClass)) {
            regexFunc = REGEXP_FUNCTION_STANDARD;
            log.debug("Apigen functions initialized for Oracle in dialect: {}", dialectClass);
        } else if (MySQLDialect.class.isAssignableFrom(dialectClass)) {
            regexFunc = REGEXP_FUNCTION_STANDARD;
            log.debug("Apigen functions initialized for MySQL in dialect: {}", dialectClass);
        }

        if (regexFunc == null) {
            log.error("Dialect {} not supported natively by Apigen, functions {} not defined, consult documentation to extend your own dialect", REGEXP_FUNCTION, dialectClass);
        } else {
            metadataBuilder.applySqlFunction(REGEXP_FUNCTION,
                    new PatternBasedSqmFunctionDescriptor(new PatternRenderer(regexFunc), null,
                            null, null, REGEXP_FUNCTION, FunctionKind.NORMAL, null));
        }
    }
}
