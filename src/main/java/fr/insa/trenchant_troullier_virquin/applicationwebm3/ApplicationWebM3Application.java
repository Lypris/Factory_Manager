package fr.insa.trenchant_troullier_virquin.applicationwebm3;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.theme.Theme;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.BDD.Gestion;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@Theme("applicationwebm3")

public class ApplicationWebM3Application extends SpringBootServletInitializer implements AppShellConfigurator{

    public static void main(String[] args) {

/////// Pour passer du mode console au mode web, il faut inverser les commentaires sur les deux lignes suivantes ///////

        //Gestion.debut();
        SpringApplication.run(ApplicationWebM3Application.class, args);
    }

}