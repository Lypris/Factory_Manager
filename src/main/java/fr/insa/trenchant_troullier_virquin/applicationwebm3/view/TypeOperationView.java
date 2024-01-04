package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.TypeOperation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

@Route(value = "typeoperation", layout = MainLayout.class)
@PageTitle("Types d'opérations | M3 Application")
public class TypeOperationView extends VerticalLayout {
    Grid<TypeOperation> grid = new Grid<>(TypeOperation.class);
    TypeOperationForm formTypeOperation;
    CrmService service;

    public TypeOperationView(CrmService service) {
        this.service = service;
        addClassName("product-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        VerticalLayout content = new VerticalLayout(grid, formTypeOperation);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, formTypeOperation);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.addClassName("typeoperation-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn("des").setHeader("Description").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(evt -> editTypeOperation(evt.getValue()));
    }

    private HorizontalLayout getToolbar() {
        Button addTypeOperationButton = new Button("Ajouter un type d'opération", click -> addTypeOperation());
        addTypeOperationButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        addTypeOperationButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button ProductButton = new Button("Voir les produits", click -> getUI().ifPresent(ui -> ui.navigate("produits")));
        ProductButton.setIcon(new Icon(VaadinIcon.PACKAGE));
        HorizontalLayout toolbar = new HorizontalLayout(addTypeOperationButton, ProductButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void configureForm() {
        formTypeOperation = new TypeOperationForm(service);
        formTypeOperation.setWidth("35em");
        formTypeOperation.addSaveListener(this::saveTypeOperation);
        formTypeOperation.addDeleteListener(this::deleteTypeOperation);
        formTypeOperation.addCloseListener(e -> closeEditor());
    }

    private void saveTypeOperation(TypeOperationForm.SaveEvent event) {
        service.saveTypeOperation(event.getTypeOperation());
        updateList();
        closeEditor();
    }

    private void deleteTypeOperation(TypeOperationForm.DeleteEvent event) {
        service.deleteTypeOperation(event.getTypeOperation());
        updateList();
        closeEditor();
    }

    private void addTypeOperation() {
        editTypeOperation(new TypeOperation());

    }

    private void editTypeOperation(TypeOperation typeOperation) {
        if (typeOperation == null) {
            closeEditor();
        } else {
            formTypeOperation.setTypeOperation(typeOperation);
            formTypeOperation.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        formTypeOperation.setTypeOperation(null);
        formTypeOperation.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllTypeOperation());
    }
}
