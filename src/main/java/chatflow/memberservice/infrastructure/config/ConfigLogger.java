package chatflow.memberservice.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfigLogger implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value("${spring.jpa.properties.dialect}")
    private String dialect;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("""
                Datasource Configuration:
                - URL: {}
                - Username: {}
                - Driver Class Name: {}
                JPA Configuration:
                - Hibernate ddl-auto: {}
                - Hibernate dialect: {}
                """, datasourceUrl, datasourceUsername, driverClassName, ddlAuto, dialect);
    }
}
