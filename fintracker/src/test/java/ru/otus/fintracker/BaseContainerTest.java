package ru.otus.fintracker;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class BaseContainerTest {

    private static final Logger log = LoggerFactory.getLogger(BaseContainerTest.class);
    protected static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("init.sql");

    @BeforeAll
    static void startContainer() {
        POSTGRE_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        log.info("POSTGRE_SQL_CONTAINER.getJdbcUrl:{}", POSTGRE_SQL_CONTAINER.getJdbcUrl());
        log.info("POSTGRE_SQL_CONTAINER.getUsername:{}", POSTGRE_SQL_CONTAINER.getUsername());
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.user", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
    }
}
