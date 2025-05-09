package org.example.util;

import lombok.Getter;
import org.example.entity.Project;
import org.example.entity.Student;
import org.example.entity.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.sql.DataSource;

public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory;

    static {
        try {
            // 1. 配置HikariCP
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:mysql://119.91.52.251:3306/ormtest?useSSL=false&serverTimezone=UTC");
            hikariConfig.setUsername("test");
            hikariConfig.setPassword("www.2005");
            hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
            hikariConfig.setMaximumPoolSize(10);
            hikariConfig.setMinimumIdle(5);
            hikariConfig.setIdleTimeout(30000);
            hikariConfig.setConnectionTimeout(30000);

            DataSource dataSource = new HikariDataSource(hikariConfig);

            // 2. 配置Hibernate
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");

            // 注册实体类
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Project.class);
            configuration.addAnnotatedClass(Student.class);

            // 3. 将DataSource交给Hibernate
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .applySetting("hibernate.connection.datasource", dataSource)
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("SessionFactory初始化失败：" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

}

