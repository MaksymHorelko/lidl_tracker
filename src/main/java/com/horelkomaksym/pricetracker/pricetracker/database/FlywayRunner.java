package com.horelkomaksym.pricetracker.pricetracker.database;

import org.flywaydb.core.Flyway;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class FlywayRunner implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            Flyway.configure()
                    .dataSource((DataSource) bean)
                    .load()
                    .migrate();
        }
        return bean;
    }
}