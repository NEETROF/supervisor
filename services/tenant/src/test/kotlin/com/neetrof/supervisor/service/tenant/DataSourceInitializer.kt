package com.neetrof.supervisor.service.tenant

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.lifecycle.Startables

class DataSourceInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    private val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:16.0-alpine").apply {
        withDatabaseName("tenantdb")
        withUsername("supervisor_dev")
        withPassword("supervisor_dev")
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        Startables.deepStart(postgreSQLContainer).join()

        TestPropertySourceUtils
            .addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + postgreSQLContainer.jdbcUrl,
                "spring.datasource.username=" + postgreSQLContainer.username,
                "spring.datasource.password=" + postgreSQLContainer.password,
                "spring.jpa.hibernate.ddl-auto=create-drop"
            )
    }
}