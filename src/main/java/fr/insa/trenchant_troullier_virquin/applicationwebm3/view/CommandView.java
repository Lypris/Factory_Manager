package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "commande", layout = MainLayout.class)
@PageTitle("Commande | M3 Application")
public class CommandView extends VerticalLayout {
    Grid<Commande> grid = new Grid<>(Commande.class);
    TextField filterText = new TextField();
    CommandForm form;
    CrmService service;

    public CommandView(CrmService service) {
        this.service = service;
        addClassName("commande-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new CommandForm(service.findAllProduits(null), service);
        form.setWidth("25em");
        form.addSaveListener(this::saveCommande);
        form.addDeleteListener(this::deleteCommande);
        form.addCloseListener(e -> closeEditor());
    }
    private void saveCommande(CommandForm.SaveEvent event) {
        service.saveCommande(event.getCommande());
        event.getCommande().creatExemplairesAssociate(service);
        updateList();
        closeEditor();
    }

    private void deleteCommande(CommandForm.DeleteEvent event) {
        service.deleteCommande(event.getCommande());
        updateList();
        closeEditor();
    }

    public void deleteCommandeNonDefini(List<Commande> commande) {
        for (Commande c : commande) {
            if (c.getStatut().isEmpty() || c.getDes().isEmpty() || c.getRef().isEmpty()) {
                service.deleteCommande(c);
                updateList();
                closeEditor();
            }
        }
    }
    private void configureGrid() {
        grid.addClassNames("Command-grid");
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
        grid.asSingleSelect().addValueChangeListener(event ->
                editCommande(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filtrer les commandes...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCommandeButton = new Button("Ajouter une Commandes");
        addCommandeButton.addClickListener(click -> addCommande());
        var toolbar = new HorizontalLayout(filterText, addCommandeButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
    public void editCommande(Commande commande) {
        if (commande == null) {
            closeEditor();
        } else {
            service.saveCommande(commande);
            form.setCommande(commande);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setCommande(null);
        form.setVisible(false);
        removeClassName("editing");
        deleteCommandeNonDefini(service.findAllCommande(null));
    }

    private void addCommande() {
        grid.asSingleSelect().clear();
        editCommande(new Commande());
    }

    private void updateList() {
        grid.setItems(service.findAllCommande(filterText.getValue()));
    }
}