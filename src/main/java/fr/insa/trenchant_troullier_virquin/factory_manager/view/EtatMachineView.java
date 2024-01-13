package fr.insa.trenchant_troullier_virquin.factory_manager.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.EtatPossibleMachine;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.service.CrmService;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Route(value = "etatmachines", layout = MainLayout.class)
@PageTitle("Etat des Machines | Factory Manager")
public class EtatMachineView extends VerticalLayout {

    Grid<EtatMachine> grid = new Grid<>(EtatMachine.class);
    TextField filterText = new TextField();
    EtatMachineForm form;
    CrmService service;

    public EtatMachineView(CrmService service) {
        this.service = service;
        addClassName("etat-machine-view");
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
        form = new EtatMachineForm(service.findAllMachines(null), service.findAllEtatPossibleMachines(), service);
        form.setWidth("25em");
        form.addSaveListener(this::saveEtatMachine);
        form.addDeleteListener(this::deleteEtatMachine);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveEtatMachine(EtatMachineForm.SaveEvent event) {
        service.saveEtatMachine(event.getEtatMachine());
        updateList();
        closeEditor();
    }

    private void deleteEtatMachine(EtatMachineForm.DeleteEvent event) {
        service.deleteEtatMachine(event.getEtatMachine());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("etat-machine-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(EtatMachine -> {
                    Machine Machine = EtatMachine.getMachine();
                    return (Machine != null) ? Machine.getRef() : "";
                })
                .setHeader("Référence").setSortable(true);
        grid.addColumn(EtatMachine -> {
                    Machine Machine = EtatMachine.getMachine();
                    return (Machine != null) ? Machine.getDes() : "";
                })
                .setHeader("Description").setSortable(true);

        grid.addColumn(new ComponentRenderer<>(etatMachine -> {
                    VaadinIcon icon = IconUtils.determineIcon(etatMachine.getEtat().getDes());
                    Span badge = new Span(IconUtils.createIcon(icon),
                            new Span(etatMachine.getEtat().getDes()));
                    IconUtils.applyStyleForEtat(badge, etatMachine.getEtat().getDes());
                    return badge;
                }))

                .setHeader("Description de l'état")
                .setSortable(true)
                .setKey("etat")
                .setComparator(Comparator.comparing(etatMachine -> etatMachine.getEtat().getDes()))
                .setFlexGrow(0);


        grid.addColumn(EtatMachine -> {
                    return EtatMachine.getDebut().toString();
                })
                .setHeader("Date de début").setSortable(true)
                .setComparator(Comparator.comparing(EtatMachine::getDebut))
                .setRenderer(new TextRenderer<>(EtatMachine -> {
                    // Personnalisez le format de date selon vos besoins
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    return EtatMachine.getDebut().format(formatter);
                }));
        grid.addColumn(EtatMachine -> {
                    return EtatMachine.getFin().toString();
                })
                .setHeader("Date de fin").setSortable(true)
                .setComparator(Comparator.comparing(EtatMachine::getFin))
                .setRenderer(new TextRenderer<>(EtatMachine -> {
                    if (EtatMachine.getFin() == null) {
                        return "indeterminée";
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    return EtatMachine.getFin().format(formatter);
                }));
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editEtatMachine(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filtrer");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addEtatButton = new Button("Ajouter un état");
        addEtatButton.addClickListener(click -> addEtatMachine());
        addEtatButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        addEtatButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button addEtatPossibleButton = new Button("Ajouter un état possible");
        addEtatPossibleButton.addClickListener(click -> addEtatPossibleMachine());
        addEtatPossibleButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        addEtatPossibleButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button goToMachineButton = new Button("Machines");
        goToMachineButton.setIcon(new Icon(VaadinIcon.COGS));
        goToMachineButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate("machines")));
        var toolbar = new HorizontalLayout(filterText, addEtatButton, addEtatPossibleButton, goToMachineButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editEtatMachine(EtatMachine etatMachine) {
        if (etatMachine == null) {
            closeEditor();
        } else {
            form.setEtatMachine(etatMachine);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setEtatMachine(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addEtatMachine() {
        grid.asSingleSelect().clear();
        updateEtatsPossibles();
        editEtatMachine(new EtatMachine());
    }

    private void addEtatPossibleMachine() {
        EtatPossibleMachineDialog etatPossibleDialog = new EtatPossibleMachineDialog();

        // Ouvrir la boîte de dialogue
        etatPossibleDialog.openDialog();
        // Ajouter un listener pour le bouton "Save" en utilisant une lambda expression
        etatPossibleDialog.addSaveListener(event -> {
            saveEtatPossibleMachine(event);
            updateEtatsPossibles();
        });

        etatPossibleDialog.addCloseListener(e -> closeEditor());
    }


    private void updateEtatsPossibles() {
        // Mettre à jour la liste des états possibles dans la vue principale
        List<EtatPossibleMachine> updatedEtatsPossibles = service.findAllEtatPossibleMachines();

        // Mettre à jour la liste des états possibles dans le formulaire
        form.updateEtatsPossibles(updatedEtatsPossibles);
    }

    private void saveEtatPossibleMachine(EtatPossibleMachineDialog.SaveEvent event) {
        service.saveEtatPossibleMachine(event.getEtatPossibleMachine());
        updateList();
        closeEditor();
    }

    private void updateList() {
        //TODO: modifier la méthode updateList pour afficher uniquement les états des machines dont la date de fin est nulle ou supérieure à la date actuelle
        grid.setItems(service.findAllLastEtatMachines(filterText.getValue()));
    }
}