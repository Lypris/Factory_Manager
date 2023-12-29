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
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.StatutOperateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Statut;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

@Route(value = "statut", layout = MainLayout.class)
@PageTitle("Statut | M3 Application")
public class StatutView extends VerticalLayout {

    Grid<StatutOperateur> grid = new Grid<>(StatutOperateur.class);
    TextField filterText = new TextField();
    StatutForm form;
    CrmService service;

    public StatutView(CrmService service) {
        this.service = service;
        addClassName("contact-view");
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
        form = new StatutForm(service.findAllOperateurs(null) , service.findAllStatuses());
        form.setWidth("25em");
        form.addSaveListener(this::saveStatut);
        form.addDeleteListener(this::deleteStatut);
        form.addCloseListener(e -> closeEditor());
    }
    private void saveStatut(StatutForm.SaveEvent event) {
        service.saveStatutOperateur(event.getStatutOperateur());
        updateList();
        closeEditor();
    }

    private void deleteStatut(StatutForm.DeleteEvent event) {
        service.deleteStatutOperateur(event.getStatutOperateur());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("statut-operateur-grid");
        grid.setSizeFull();
        //on retire les colonnes inutiles
        grid.removeAllColumns();
        grid.addColumn(statutOperateur -> {
                    Operateur operateur = statutOperateur.getOperateur();
                    return (operateur != null) ? operateur.getNom() : "";
                })
                .setHeader("Nom").setSortable(true);
        grid.addColumn(statutOperateur -> {
                    Operateur operateur = statutOperateur.getOperateur();
                    return (operateur != null) ? operateur.getPrenom() : "";
                })
                .setHeader("Prénom").setSortable(true);
        grid.addColumn(statutOperateur -> {
                    Statut statut = statutOperateur.getStatut();
                    return (statut != null) ? statut.getName() : "";
                })
                .setHeader("Etat").setSortable(true);

        grid.addColumn(new ComponentRenderer<>(statutOperateur -> {
                    VaadinIcon icon = IconUtils.determineIconOperateur(statutOperateur.getStatut().getName());
                    Span badge = new Span(IconUtils.createIcon(icon),
                            new Span(statutOperateur.getStatut().getName()));
                    IconUtils.applyStyleForStatut(badge, statutOperateur.getStatut().getName());
                    return badge;
                }))

                .setHeader("Statut")
                .setSortable(true)
                .setKey("etat")
                .setComparator(Comparator.comparing(statutOperateur -> statutOperateur.getStatut().getName()))
                .setFlexGrow(0);


        grid.addColumn(statutOperateur -> {
                    return statutOperateur.getDebut().toString();
                })
                .setHeader("Date de début").setSortable(true)
                .setComparator(Comparator.comparing(StatutOperateur::getDebut))
                .setRenderer(new TextRenderer<>(statutOperateur -> {
                    // Personnalisez le format de date selon vos besoins
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    return statutOperateur.getDebut().format(formatter);
                }));
        grid.addColumn(statutOperateur -> {
                    return statutOperateur.getFin().toString();
                })
                .setHeader("Date de fin").setSortable(true)
                .setComparator(Comparator.comparing(StatutOperateur::getDebut))
                .setRenderer(new TextRenderer<>(statutOperateur -> {
                    // Personnalisez le format de date selon vos besoins
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    return statutOperateur.getDebut().format(formatter);
                }));

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editStatutOperateur(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Ajouter statut");
        addContactButton.addClickListener(click -> addStatutOperateur());
        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
    public void editStatutOperateur(StatutOperateur statutOperateur) {
        if (statutOperateur == null) {
            closeEditor();
        } else {
            form.setStatut(statutOperateur);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setStatut(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addStatutOperateur() {
        grid.asSingleSelect().clear();
        editStatutOperateur(new StatutOperateur());
    }

    private void updateList() {
        grid.setItems(service.findAllStatutOperateurs(filterText.getValue()));
    }

}
