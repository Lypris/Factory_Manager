package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "production", layout = MainLayout.class)
@PageTitle("Production | M3 Application")
public class ProductionView extends VerticalLayout {
    Grid<Commande> grid = new Grid<>(Commande.class);
    CrmService service;
    ProductionForm form;


    public ProductionView(CrmService service) {
        this.service = service;
        addClassName("production-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getContent());
        updateList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid,form);
        content.setFlexGrow(3, grid);
        content.setFlexGrow(2, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    private void configureForm() {
        form = new ProductionForm();
        form.setWidth("40em");
    }
    private void updateList() {
        grid.setItems(service.findAllCommandeEnAttente());
    }
    private void configureGrid() {
        grid.addClassNames("production-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Commande::getRef)
                .setHeader("Référence").setSortable(true);
        grid.addColumn(Commande::getDes)
                .setHeader("Description").setSortable(true);
        grid.addColumn(Commande -> {
                    LocalDateTime debut = Commande.getDebut();
                    return (debut != null) ? debut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
                })
                .setHeader("Date de début").setSortable(true);
        grid.addColumn(Commande -> {
                    LocalDateTime fin = Commande.getFin();
                    return (fin != null) ? fin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
                })
                .setHeader("Date de fin").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(commande -> {
                    VaadinIcon icon = IconUtils.determineIconCommande(commande.getStatut());
                    Span badge = new Span(IconUtils.createIcon(icon),
                            new Span(commande.getStatut()));
                    IconUtils.applyStyleForStatutCommande(badge, commande.getStatut());
                    return badge;
                }))
                .setHeader("Statut").setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }
}
