package fr.insa.trenchant_troullier_virquin.factory_manager.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Operateur;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Statut;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.StatutOperateur;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.service.CrmService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import static com.vaadin.flow.component.grid.Grid.*;

@Route(value = "statuts", layout = MainLayout.class)
@PageTitle("Statut | Factory Manager")
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
        form = new StatutForm(service.findAllOperateurs(null), service.findAllStatuses(), service);
        form.setWidth("25em");
        form.addSaveListener(this::saveStatut);
        form.addDeleteListener(this::deleteStatut);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveStatut(StatutForm.SaveEvent event) {

        //logique pour mettre à jour la date de fin du statut précédent
        StatutOperateur statutOperateur = event.getStatutOperateur();
        //on récupère la liste de statuts de l'opérateur
        List<StatutOperateur> statutOperateurs = service.findAllStatutOperateurByOperateur(statutOperateur.getOperateur());
        // si un statut est déjà en cours, on le met à jour
        if(statutOperateurs != null) {
            for (StatutOperateur statutOperateur1 : statutOperateurs) {
                if (statutOperateur1.getFin() == null) {
                    statutOperateur1.setFin(LocalDateTime.now());
                    service.saveStatutOperateur(statutOperateur1);
                }
            }
        }
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
                .setHeader("Date de début").setSortable(true).setKey("debut")
                //on trie les dates de début par ordre décroissant (le plus récent en premier)
                .setComparator(Comparator.comparing(StatutOperateur::getDebut))
                .setRenderer(new TextRenderer<>(statutOperateur -> {
                    // Personnalisez le format de date selon vos besoins
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    return statutOperateur.getDebut().format(formatter);
                }));
        grid.addColumn(statutOperateur -> {
                    return statutOperateur.getFin().toString();
                })
                .setHeader("Date de fin")
                .setComparator(Comparator.comparing(StatutOperateur::getFin))
                .setRenderer(new TextRenderer<>(statutOperateur -> {
                    if (statutOperateur.getFin() == null) {
                        return "indéterminée";
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    return statutOperateur.getFin().format(formatter);
                }));
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editStatutOperateur(event.getValue()));
        //on trie les dates de début par ordre décroissant
        grid.sort(List.of(new GridSortOrder<>(grid.getColumnByKey("debut"), SortDirection.DESCENDING)));

    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Ajouter statut");
        addContactButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        addContactButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addContactButton.addClickListener(click -> addStatutOperateur());
        Button goToOperateurButton = new Button("Liste des Opérateurs");
        goToOperateurButton.setIcon(new Icon(VaadinIcon.USERS));
        goToOperateurButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate("operateurs")));
        var toolbar = new HorizontalLayout(filterText, addContactButton, goToOperateurButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editStatutOperateur(StatutOperateur statutOperateur) {
        if (statutOperateur == null) {
            closeEditor();
        } else {
            if (statutOperateur.getFin() == null) {
                form.setStatut(statutOperateur);
                form.setVisible(true);
                addClassName("editing");
            }
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
