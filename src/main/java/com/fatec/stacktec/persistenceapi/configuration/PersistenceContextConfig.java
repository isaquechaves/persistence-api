package com.fatec.stacktec.persistenceapi.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = {
		"com.fatec.stacktec.persistenceapi.repository"
})
@EnableTransactionManagement
@EnableSpringDataWebSupport
@PropertySource("classpath:application.properties")
@EntityScan({
	"com.fatec.stacktec.persistenceapi.model"
})
class PersistenceContextConfig {
	
	@Autowired
	private Environment env;
	
	private static final String[] ENTITY_PACKAGES = {
			"com.fatec.stacktec.persistenceapi.model"
	};
	
	private static final String PROPERTY_NAME_DB_DRIVER_CLASS = "spring.datasource.driver";
    private static final String PROPERTY_NAME_DB_PASSWORD = "spring.datasource.password";
    private static final String PROPERTY_NAME_DB_URL = "spring.datasource.url";
    private static final String PROPERTY_NAME_DB_USER = "spring.datasource.username";
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "spring.jpa.properties.hibernate.dialect";
    private static final String PROPERTY_NAME_DATABASE_PLATAFORM = "spring.jpa.database-platform";
    private static final String PROPERTY_NAME_HIBERNATE_FORMAT_SQL = "spring.jpa.properties.hibernate.format_sql";
    private static final String PROPERTY_NAME_GENERATE_DDL = "spring.jpa.generate-ddl";
    private static final String PROPERTY_NAME_DATASOURCE_PLATAFORM = "spring.datasource.platform";
    private static final String PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO = "spring.jpa.hibernate.ddl-auto";
    private static final String PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY = "spring.jpa.hibernate.naming.implicit-strategy";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "spring.jpa.show-sql";
    private static final String PROPERTY_GENERATE_DDL = "spring.jpa.generate-ddl";

    private static final String PROPERTY_HIKARI_MAXIMUM_POOL_SIZE = "spring.datasource.hikari.maximum-pool-size";
    private static final String PROPERTY_HIKARI_MINIMUM_IDLE = "spring.datasource.hikari.minimum-idle";
    private static final String PROPERTY_HIKARI_MAXLIFETIME = "spring.datasource.hikari.max-lifetime";
    private static final String PROPERTY_HIKARI_IDLETIMEOUT = "spring.datasource.hikari.idle-timeout";
    private static final String PROPERTY_HIKARI_LEAK_DETECTION_THRESHOLD = "spring.datasource.hikari.leak-detection-threshold";


    /**
     * Creates and configures the HikariCP datasource bean.
     * @param env   The runtime environment of  our application.
     * @return
     */
    @Primary
    @Bean
    DataSource dataSource() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setPoolName("pool-postgres-core");
        dataSourceConfig.setJdbcUrl(env.getRequiredProperty(PROPERTY_NAME_DB_URL));
        dataSourceConfig.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DB_DRIVER_CLASS));
        dataSourceConfig.setUsername(env.getRequiredProperty(PROPERTY_NAME_DB_USER));
        dataSourceConfig.setPassword(env.getRequiredProperty(PROPERTY_NAME_DB_PASSWORD));

        String hikariMaximumPoolSize = env.getProperty(PROPERTY_HIKARI_MAXIMUM_POOL_SIZE);
        dataSourceConfig.setMaximumPoolSize(hikariMaximumPoolSize == null || hikariMaximumPoolSize.isEmpty() ? 50 : Integer.valueOf(hikariMaximumPoolSize));

        String hikariMinimumIdle = env.getProperty(PROPERTY_HIKARI_MINIMUM_IDLE);
        dataSourceConfig.setMinimumIdle(hikariMinimumIdle == null || hikariMinimumIdle.isEmpty() ? 20 : Integer.valueOf(hikariMinimumIdle));

        String hikariMaxLifetime = env.getProperty(PROPERTY_HIKARI_MAXLIFETIME);
        dataSourceConfig.setMaxLifetime(hikariMaxLifetime == null || hikariMaxLifetime.isEmpty() ? 300000 : Integer.valueOf(hikariMaxLifetime));

        String hikariIdleTimeout = env.getProperty(PROPERTY_HIKARI_IDLETIMEOUT);
        dataSourceConfig.setIdleTimeout(hikariIdleTimeout == null || hikariIdleTimeout.isEmpty() ? 240000 : Integer.valueOf(hikariIdleTimeout));

        String hikariLeakDetectionThreshold = env.getProperty(PROPERTY_HIKARI_LEAK_DETECTION_THRESHOLD);
        dataSourceConfig.setLeakDetectionThreshold(hikariLeakDetectionThreshold == null || hikariLeakDetectionThreshold.isEmpty() ? 60000 : Integer.valueOf(hikariLeakDetectionThreshold));

        return new HikariDataSource(dataSourceConfig);
    }

    /**
     * Creates the bean that creates the JPA entity manager factory.
     * @param dataSource    The datasource that provides the database connections.
     * @param env           The runtime environment of  our application.
     * @return
     */
    @Primary
    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment env) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceUnitName("base.manager");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        vendorAdapter.setDatabasePlatform(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PLATAFORM));
        vendorAdapter.setGenerateDdl(Boolean.parseBoolean(env.getRequiredProperty(PROPERTY_GENERATE_DDL)));
        vendorAdapter.setShowSql(Boolean.parseBoolean(env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL)));

        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setPackagesToScan(ENTITY_PACKAGES);

        Properties jpaProperties = new Properties();

        // datasource plataform
        jpaProperties.put(PROPERTY_NAME_DATASOURCE_PLATAFORM, env.getRequiredProperty(PROPERTY_NAME_DATASOURCE_PLATAFORM));

        // database plataform
        jpaProperties.put(PROPERTY_NAME_DATABASE_PLATAFORM, env.getRequiredProperty(PROPERTY_NAME_DATABASE_PLATAFORM));

        //Configures the used database dialect. This allows Hibernate to create SQL
        //that is optimized for the used database.
        //jpaProperties.put(PROPERTY_NAME_HIBERNATE_DIALECT, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));

        // generate DDL
        //jpaProperties.put(PROPERTY_NAME_GENERATE_DDL, env.getRequiredProperty(PROPERTY_NAME_GENERATE_DDL));

        //Specifies the action that is invoked to the database when the Hibernate
        //SessionFactory is created or closed.
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO));

        //Configures the naming strategy that is used when Hibernate creates
        //new database objects and schema elements
        //jpaProperties.put(PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY));

        //If the value of this property is true, Hibernate writes all SQL
        //statements to the console.
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));

        //If the value of this property is true, Hibernate will use prettyprint
        //when it writes SQL to the console.
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_FORMAT_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_FORMAT_SQL));

        jpaProperties.put("org.hibernate.dialect.Dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        jpaProperties.put("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults", "false");
        jpaProperties.put("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation", "true");
        jpaProperties.put("hibernate.jdbc.lob.non_contextual_creation","true");
        //jpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        //jpaProperties.put("hibernate.enable_lazy_load_no_trans", "true");

        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    /**
     * Creates the transaction manager bean that integrates the used JPA provider with the
     * Spring transaction mechanism.
     * @param entityManagerFactory  The used JPA entity manager factory.
     * @return
     */
    @Primary
    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
