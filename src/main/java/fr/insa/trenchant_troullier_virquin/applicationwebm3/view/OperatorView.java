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
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

@Route(value = "operateurs", layout = MainLayout.class)
@PageTitle("Opérateurs | Factory Manager")
public class OperatorView extends VerticalLayout {

    Grid<Operateur> grid = new Grid<>(Operateur.class);
    TextField filterText = new TextField();
    OperateurForm form;
    CrmService service;

    public OperatorView(CrmService service) {
        this.service = service;
        addClassName("operateur-view");
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
        form = new OperateurForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveOperateur);
        form.addDeleteListener(this::deleteOperateur);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveOperateur(OperateurForm.SaveEvent event) {
        service.saveOperateur(event.getOperateur());
        updateList();
        closeEditor();
    }

    private void deleteOperateur(OperateurForm.DeleteEvent event) {
        service.deleteOperateur(event.getOperateur());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("operateur-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Operateur::getNom)
                .setHeader("Nom").setSortable(true);
        grid.addColumn(Operateur::getPrenom)
                .setHeader("Prénom").setSortable(true);
        grid.addColumn(Operateur::getMail)
                .setHeader("Adresse e-mail");
        grid.addColumn(Operateur::getTel)
                .setHeader("Téléphone");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editOperateur(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filtrer par nom...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addOperateurButton = new Button("Ajouter un opérateur");
        addOperateurButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addOperateurButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        addOperateurButton.addClickListener(click -> addOperateur());
        Button goToStatutButton = new Button("Etats des opérateurs");
        goToStatutButton.setIcon(new Icon(VaadinIcon.CALENDAR_USER));
        goToStatutButton.addClickListener(click -> getUI().ifPresent(ui -> ui.navigate("statuts")));
        var toolbar = new HorizontalLayout(filterText, addOperateurButton, goToStatutButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editOperateur(Operateur Operateur) {
        if (Operateur == null) {
            closeEditor();
        } else {
            form.setOperateur(Operateur);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setOperateur(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addOperateur() {
        grid.asSingleSelect().clear();
        editOperateur(new Operateur());
    }

    private void updateList() {
        grid.setItems(service.findAllOperateurs(filterText.getValue()));
    }
}