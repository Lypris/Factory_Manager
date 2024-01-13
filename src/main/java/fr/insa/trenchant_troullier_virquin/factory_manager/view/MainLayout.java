package fr.insa.trenchant_troullier_virquin.factory_manager.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Main")
public class MainLayout extends AppLayout {
    public MainLayout() {
        createHeader();
        createDrawer();
        this.setId("main-layout");
    }

    private void createHeader() {
        H1 logo = new H1("Factory Manager");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        var themeToggle = new Checkbox("Mode sombre");
        themeToggle.addValueChangeListener(e -> {
            setTheme(e.getValue());
        });
        var header = new HorizontalLayout(new DrawerToggle(), logo, themeToggle);
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

    private void createDrawer() {
        Tabs tabs = new Tabs(
                createTab(VaadinIcon.HOME, "Retour à la vue principale", DashboardView.class),
                createTab(VaadinIcon.USERS, "Employés", OperatorView.class),
                createTab(VaadinIcon.COGS, "Machines", MachineView.class),
                createTab(VaadinIcon.COGS, "Type d'opération", TypeOperationView.class),
                createTab(VaadinIcon.PACKAGE, "Produits", ProductView.class),
                createTab(VaadinIcon.CLIPBOARD, "Commandes", CommandView.class),
                createTab(VaadinIcon.STOCK, "Stock", StockView.class),
                createTab(VaadinIcon.FACTORY, "Production", ProductionView.class),
                createTab(VaadinIcon.BAR_CHART, "Statistiques", DashboardView.class)


        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setFlexGrowForEnclosedTabs(1);
        tabs.setId("tabs");
        addToDrawer(tabs);
    }

    private Tab createTab(VaadinIcon icon, String title, Class<? extends Component> viewClass) {
        Icon vaadinIcon = icon.create();
        vaadinIcon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(vaadinIcon, new Span(title));
        link.setRoute(viewClass);
        link.setTabIndex(-1);

        return new Tab(link);
    }
}
