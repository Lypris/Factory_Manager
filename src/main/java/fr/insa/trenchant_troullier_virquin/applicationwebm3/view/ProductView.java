package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
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
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.TypeOperation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.io.ByteArrayInputStream;
import java.util.stream.Stream;

@Route(value = "produit", layout = MainLayout.class)
@PageTitle("Produits | M3 Application")
public class ProductView extends VerticalLayout {

    Grid<Produit> grid = new Grid<>(Produit.class);
    TextField filterText = new TextField();
    ProductForm form;

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
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    private void configureForm() {
        form = new ProductForm(service.findAllTypeOperation(), service);
        form.setWidth("35em");
        form.addSaveListener(this::saveProduct);
        form.addDeleteListener(this::deleteProduct);
        form.addCloseListener(e -> closeEditor());
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

    private void configureGrid() {
        grid.addClassName("operateur-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Produit::getRef).setHeader("Référence").setSortable(true);
        grid.addColumn(Produit::getDes).setHeader("Description").setSortable(true);
        grid.addColumn(Produit::getPrix).setHeader("Prix").setSortable(true);
        grid.addColumn(createProductImageRenderer()).setHeader("Image");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editProduit(event.getValue()));
        grid.setItemDetailsRenderer(createPersonDetailsRenderer());
    }

    private ComponentRenderer<ProduitDetails, Produit> createPersonDetailsRenderer() {
        return new ComponentRenderer<>(produit -> {
            ProduitDetails details = new ProduitDetails(service, produit);
            details.addClassName("product-details");
            return details;
        });
    }

    private Renderer<Produit> createProductImageRenderer() {
        return new ComponentRenderer<>(produit -> {
            Image image = new Image();
            if (produit.getImage() != null) {
                StreamResource resource = new StreamResource("image.png", () -> new ByteArrayInputStream(produit.getImage()));
                image.setSrc(resource);
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
        }
    }
    private void closeEditor() {
        form.setProduit(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter par référence ou description...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.setWidth("25em");
        filterText.addValueChangeListener(e -> updateList());
        Button addProductButton = new Button("Ajouter", click -> addProduct());
        Button TypeOperationView = new Button("Voir les types d'opérations", click -> getUI().get().navigate("typeoperation"));
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addProductButton, TypeOperationView);
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

    private static class ProduitDetails extends VerticalLayout {
        //TODO : Afficher les opérations définies pour un produit
        private final Grid<Operation> grid = new Grid<>(Operation.class);
        private Produit produit;
        private CrmService service;


        public ProduitDetails(CrmService service, Produit produit) {
            this.service = service;
            this.produit = produit;
            addClassName("produit-details");
            setSizeFull();
            grid.setColumns("typeOperation.des", "ordre");
            grid.getColumns().forEach(col -> col.setAutoWidth(true));
            add(grid);
            setProduit(service, produit);
        }

        public void setProduit(CrmService service, Produit produit) {
            grid.setItems(service.findOperationByProduit(produit));
        }
    }

}
