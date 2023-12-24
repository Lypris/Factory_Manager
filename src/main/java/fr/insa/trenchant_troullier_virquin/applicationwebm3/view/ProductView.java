package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.TypeOperation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.io.ByteArrayInputStream;

@Route(value = "product", layout = MainLayout.class)
@PageTitle("Produits | M3 Application")
public class ProductView extends VerticalLayout {

    Grid<Produit> grid = new Grid<>(Produit.class);
    TextField filterText = new TextField();
    ProductForm form;
    TypeOperationForm formTypeOperation;
    CrmService service;

    public ProductView(CrmService service) {
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
        HorizontalLayout content = new HorizontalLayout(grid, form, formTypeOperation);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setFlexGrow(1, formTypeOperation);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    private void configureForm() {
        form = new ProductForm(service.findAllTypeOperation());
        form.setWidth("35em");
        form.addSaveListener(this::saveProduct);
        form.addDeleteListener(this::deleteProduct);
        form.addCloseListener(e -> closeEditor());

        formTypeOperation = new TypeOperationForm();
        formTypeOperation.setWidth("35em");
        formTypeOperation.addSaveListener(this::saveTypeOperation);
        formTypeOperation.addDeleteListener(this::deleteTypeOperation);
        formTypeOperation.addCloseListener(e -> closeEditor());
    }
    private void saveProduct(ProductForm.SaveEvent event) {
        service.saveProduit(event.getProduit());
        updateList();
        closeEditor();
    }
    private void deleteProduct(ProductForm.DeleteEvent event) {
        service.deleteProduit(event.getProduit());
        updateList();
        closeEditor();
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
    private void configureGrid() {
        grid.addClassName("operateur-grid");
        grid.setSizeFull();
        grid.removeColumnByKey("id");
        grid.removeColumnByKey("version");
        grid.removeColumnByKey("ref");
        grid.removeColumnByKey("des");
        grid.removeColumnByKey("prix");
        grid.removeColumnByKey("image");
        grid.addColumn(Produit::getRef).setHeader("Référence").setSortable(true);
        grid.addColumn(Produit::getDes).setHeader("Description").setSortable(true);
        grid.addColumn(Produit::getPrix).setHeader("Prix").setSortable(true);
        //TODO : Ajouter une colonne pour l'image
        grid.addColumn(createProductImageRenderer()).setHeader("Image");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editProduit(event.getValue()));
    }

    private Renderer<Produit> createProductImageRenderer() {
        return new ComponentRenderer<>(produit -> {
            Image image = new Image();
            if (produit.getImage() != null) {
                StreamResource resource = new StreamResource("image.png", () -> new ByteArrayInputStream(produit.getImage()));
                image.setSrc(resource);
                //set height to 0,1 em
                image.setHeight("5em");
            }
            return image;
        });
    }

    private void editProduit(Produit produit) {
        if (produit == null) {
            closeEditor();
        } else {
            form.setProduit(produit);
            form.setVisible(true);
            addClassName("editing");
            formTypeOperation.setTypeOperation(null);
            formTypeOperation.setVisible(false);
        }
    }
    private void closeEditor() {
        form.setProduit(null);
        form.setVisible(false);
        removeClassName("editing");

        formTypeOperation.setTypeOperation(null);
        formTypeOperation.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter par référence ou description...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.setWidth("25em");
        filterText.addValueChangeListener(e -> updateList());
        Button addProductButton = new Button("Ajouter", click -> addProduct());
        Button addTypeOperationButton = new Button("Ajouter un type d'opération", click -> addTypeOperation());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addProductButton, addTypeOperationButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
    private void updateList() {
        grid.setItems(service.findAllProduits(filterText.getValue()));
    }
    private void addProduct() {
        grid.asSingleSelect().clear();
        editProduit(new Produit());
    }
    public void addTypeOperation() {
        editTypeOperation(new TypeOperation());

    }
    private void editTypeOperation(TypeOperation typeOperation) {
        if (typeOperation == null) {
            closeEditor();
        } else {
            formTypeOperation.setTypeOperation(typeOperation);
            formTypeOperation.setVisible(true);
            addClassName("editing");
            form.setProduit(null);
            form.setVisible(false);
        }
    }

}
