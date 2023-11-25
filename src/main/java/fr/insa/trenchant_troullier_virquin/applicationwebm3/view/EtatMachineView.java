package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatPossibleMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

@Route(value = "etatmachines", layout = MainLayout.class)
@PageTitle("Etat des Machines | M3 Application")
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
        form = new EtatMachineForm(service.findAllMachines(null) , service.findAllEtatPossibleMachines());
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
        //on retire les colonnes inutiles
        grid.removeColumnByKey("id");
        grid.removeColumnByKey("machine");
        grid.removeColumnByKey("version");
        grid.removeColumnByKey("debut");
        grid.removeColumnByKey("fin");
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
        grid.addColumn(EtatMachine -> {
                    EtatPossibleMachine etatPossibleMachine = EtatMachine.getEtat();
                    return (etatPossibleMachine != null) ? etatPossibleMachine.getDes() : "";
                })
                .setHeader("Description l'état en cours").setSortable(true);
        grid.addColumn(EtatMachine -> {
                    return EtatMachine.getDebut().toString();
                })
                .setHeader("Date de début").setSortable(true);
        grid.addColumn(EtatMachine -> {
                    return EtatMachine.getFin().toString();
                })
                .setHeader("Date de fin").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editEtatMachine(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filtrer");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addEtatButton = new Button("Ajouter EtatMachine");
        addEtatButton.addClickListener(click -> addEtatMachine());
        Button addEtatPossibleButton = new Button("Ajouter un état possible");
        addEtatPossibleButton.addClickListener(click -> addEtatPossibleMachine());
        var toolbar = new HorizontalLayout(filterText, addEtatButton,addEtatPossibleButton);
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
        editEtatMachine(new EtatMachine());
    }
    private void addEtatPossibleMachine() {
        // TODO : faire une petite fenêtre pour ajouter un état possible
        EtatPossibleMachineDialog etatPossibleDialog = new EtatPossibleMachineDialog();

        // Ouvrir la boîte de dialogue
        etatPossibleDialog.openDialog();
        etatPossibleDialog.addSaveListener(this::saveEtatPossibleMachine);
        etatPossibleDialog.addCloseListener(e -> closeEditor());
    }
    private void saveEtatPossibleMachine(EtatPossibleMachineDialog.SaveEvent event) {
        service.saveEtatPossibleMachine(event.getEtatPossibleMachine());
        updateList();
        closeEditor();
    }


    private void updateList() {
        grid.setItems(service.findAllEtatMachines(filterText.getValue()));
    }
}