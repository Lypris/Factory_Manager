package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.PosteDeTravail;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

@Route(value = "postes-de-travail", layout = MainLayout.class)
@PageTitle("Postes de Travail")
public class PosteDeTravailView extends VerticalLayout {

    private final CrmService service;
    private final Grid<PosteDeTravail> grid = new Grid<>(PosteDeTravail.class);
    private final Button addButton = new Button("Ajouter un poste");
    TextField filterText = new TextField();
    PosteDeTravailForm form;

    public PosteDeTravailView(CrmService service) {
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
        content.setFlexGrow(3, grid);
        content.setFlexGrow(2, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new PosteDeTravailForm(service);
        form.setWidth("35em");
        form.addSaveListener(this::savePosteDeTravail);
        form.addDeleteListener(this::deletePosteDeTravail);
        form.addCloseListener(e -> closeEditor());
    }

    private void savePosteDeTravail(PosteDeTravailForm.SaveEvent event) {
        service.savePosteDeTravail(event.getPosteDeTravail());
        updateList();
        closeEditor();
    }

    private void deletePosteDeTravail(PosteDeTravailForm.DeleteEvent event) {
        //supprimer les habilitations associées
        service.findAllHabilitationByPosteDeTravail(event.getPosteDeTravail()).forEach(habilitation -> service.deleteHabilitation(habilitation));
        //retirer l'identifiant du poste de travail des machines associées
        service.findAllMachineByPosteDeTravail(event.getPosteDeTravail()).forEach(machine -> {
            machine.setPosteDeTravail(null);
            service.saveMachine(machine);
        });
        service.deletePosteDeTravail(event.getPosteDeTravail());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.setColumns("ref", "des");
        //TODO: ajouter les colonnes pour les opérateurs et les machines

        grid.asSingleSelect().addValueChangeListener(evt -> editPosteDeTravail(evt.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filtrer les machines...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addPosteButton = new Button("Ajouter un poste de travail");
        addPosteButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        addPosteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button goToEtatMachineButton = new Button("Etat des machines");
        goToEtatMachineButton.setIcon(new Icon(VaadinIcon.CALENDAR_CLOCK));
        goToEtatMachineButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate("etatmachines")));
        addPosteButton.addClickListener(click -> addPosteDeTravail());
        var toolbar = new HorizontalLayout(filterText, addPosteButton, goToEtatMachineButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editPosteDeTravail(PosteDeTravail posteDeTravail) {
        if (posteDeTravail == null) {
            closeEditor();
        } else {
            form.setPosteDeTravail(posteDeTravail);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setPosteDeTravail(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addPosteDeTravail() {
        grid.asSingleSelect().clear();
        editPosteDeTravail(new PosteDeTravail());
    }

    private void updateList() {
        grid.setItems(service.findAllPosteDeTravail(null));
    }
}
