package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

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
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

@Route(value = "machines", layout = MainLayout.class)
@PageTitle("Machines | M3 Application")
public class MachineView extends VerticalLayout {

    Grid<Machine> grid = new Grid<>(Machine.class);
    TextField filterText = new TextField();
    MachineForm form;
    CrmService service;

    public MachineView(CrmService service) {
        this.service = service;
        addClassName("machine-view");
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
        form = new MachineForm(service.findAllTypeOperation(), service.findAllEtatPossibleMachines(),service);
        form.setWidth("25em");
        form.addSaveListener(this::saveMachine);
        form.addDeleteListener(this::deleteMachine);
        form.addCloseListener(e -> closeEditor());
    }
    private void saveMachine(MachineForm.SaveEvent event) {
        service.saveMachine(event.getMachine());
        updateList();
        closeEditor();
    }

    private void deleteMachine(MachineForm.DeleteEvent event) {
        service.deleteMachine(event.getMachine());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("Machine-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Machine::getRef)
                .setHeader("Référence").setSortable(true);
        grid.addColumn(Machine::getDes)
                .setHeader("Description").setSortable(true);
        grid.addColumn(Machine::getPuissance)
                .setHeader("Puissance (kW)").setSortable(true);
        grid.addColumn(Machine->Machine.getTypeOperation().getDes())
                .setHeader("Type d'opération").setSortable(true);
        // Ajoute une colonne pour l'état actuel de la machine
        grid.addColumn(new ComponentRenderer<>(Machine -> {
            EtatMachine etatActuelOpt = service.findLastEtatMachineByMachine(Machine);
            if(etatActuelOpt != null){
                    VaadinIcon icon = IconUtils.determineIcon(etatActuelOpt.getEtat().getDes());
                    Span badge = new Span(IconUtils.createIcon(icon),
                            new Span(etatActuelOpt.getEtat().getDes()));
                    IconUtils.applyStyleForEtat(badge, etatActuelOpt.getEtat().getDes());
                    return badge;
            } else {
                return new Span("Aucun état");
            }
                }))
                .setHeader("État actuel")
                .setSortable(true)
                .setKey("etat")
                .setComparator(Comparator.comparing(Machine -> {
                    EtatMachine etatActuelOpt = service.findLastEtatMachineByMachine(Machine);
                    return etatActuelOpt.getEtat().getDes();
                }))
                .setFlexGrow(0);
        grid.addColumn(Machine -> {
                    return service.findLastEtatMachineByMachine(Machine).getDebut().toString();
                })
                .setHeader("Date de début").setSortable(true)
                .setComparator(Comparator.comparing(Machine -> {
                    return service.findLastEtatMachineByMachine(Machine).getDebut();
                }))
                .setRenderer(new TextRenderer<>(Machine -> {
                    // Personnalisez le format de date selon vos besoins
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    return service.findLastEtatMachineByMachine(Machine).getDebut().format(formatter);
                }));
        grid.addColumn(Machine -> {
                    return service.findLastEtatMachineByMachine(Machine).getFin().toString();
                })
                .setHeader("Date de fin").setSortable(true)
                .setComparator(Comparator.comparing(Machine -> {
                    return service.findLastEtatMachineByMachine(Machine).getFin();
                }))
                .setRenderer(new TextRenderer<>(Machine -> {
                    if(service.findLastEtatMachineByMachine(Machine).getFin() == null){
                        return "indeterminée";
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    return service.findLastEtatMachineByMachine(Machine).getFin().format(formatter);
                }));
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editMachine(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filtrer les machines...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addMachineButton = new Button("Ajouter une machine");
        addMachineButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        addMachineButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button goToEtatMachineButton = new Button("Etat des machines");
        goToEtatMachineButton.setIcon(new Icon(VaadinIcon.CALENDAR_CLOCK));
        goToEtatMachineButton.addClickListener(click ->getUI().ifPresent(ui -> ui.navigate("etatmachines")));
        addMachineButton.addClickListener(click -> addMachine());
        var toolbar = new HorizontalLayout(filterText, addMachineButton, goToEtatMachineButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
    public void editMachine(Machine Machine) {
        if (Machine == null) {
            closeEditor();
        } else {
            form.setMachine(Machine);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setMachine(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addMachine() {
        grid.asSingleSelect().clear();
        editMachine(new Machine());
    }

    private void updateList() {
        grid.setItems(service.findAllMachines(filterText.getValue()));
    }
}