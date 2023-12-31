package fr.insa.trenchant_troullier_virquin.applicationwebm3;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@EnableVaadin
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@Theme("applicationwebm3")

public class ApplicationWebM3Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationWebM3Application.class, args);
    }

}