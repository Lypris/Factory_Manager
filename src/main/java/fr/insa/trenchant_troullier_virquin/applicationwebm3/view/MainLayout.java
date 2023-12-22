package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.BDD.Gestion;

import java.sql.SQLException;

@PageTitle("Main")
public class MainLayout extends AppLayout {
    private SessionInfo sessionInfo;
    public MainLayout() {
        createHeader();
        createDrawer();
        this.sessionInfo = new SessionInfo();
        try {
            this.sessionInfo.setConBdD(Gestion.connectSurServeur());
        } catch (SQLException ex) {
            H1 info = new H1("Problème BdD : " + ex.getLocalizedMessage());
        }
    }

    private void createHeader() {
        H1 logo = new H1("M3 Application");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        Button logout = new Button("Déconnexion");
        logout.addClickListener(e -> {
            Notification.show("Pas encore implémenté");
        });
        var themeToggle = new Checkbox("Mode sombre");
        themeToggle.addValueChangeListener(e -> {
            setTheme(e.getValue());
        });
        var header = new HorizontalLayout(new DrawerToggle(), logo, themeToggle, logout);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);
        addToNavbar(header);
    }

    public void setTheme(boolean dark) {
        var js = "document.documentElement.setAttribute('theme', $0)";
        getElement().executeJs(js, dark ? Lumo.DARK : Lumo.LIGHT);
    }

    public void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Retour à la vue principal", InitialView.class),
                new RouterLink("Employés", OperatorView.class),
                new RouterLink("Etat des employés", StatutView.class),
                new RouterLink("Machines", MachineView.class),
                new RouterLink("Etat des Machines", EtatMachineView.class),
                new RouterLink("Product", ProductView.class)
        ));
    }
    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }
}
