package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.data.provider.ListDataProvider;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.TypeOperation;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.util.ArrayList;
import java.util.List;

public class DialogDefOpp extends Dialog {

    private TypeOperation draggedItem;
    CrmService service;
    Grid<TypeOperation> grid1 = setupGrid("Opérations disponibles", false);
    Grid<TypeOperation> grid2 = setupGrid("Opérations définies", true);

    ArrayList<TypeOperation> typeoperations = new ArrayList<>();
    ArrayList<TypeOperation> typeoperationsDefini = new ArrayList<>();
    GridListDataView<TypeOperation> dataView1 = grid1.setItems(typeoperations);
    GridListDataView<TypeOperation> dataView2 = grid2.setItems(typeoperationsDefini);
    Produit produit;
    private Grid<TypeOperation> dragSourceGrid;




    public DialogDefOpp(List<TypeOperation> typeOperations, CrmService service, Produit produit) {
        this.produit = produit;
        this.service = service;
        this.typeoperations.addAll(typeOperations);
        this.typeoperationsDefini.addAll(service.findAllTypeOperationForProduit(produit));

        grid1.setDropMode(GridDropMode.ON_GRID);
        grid1.setRowsDraggable(true);
        grid1.addDragStartListener(this::handleDragStart);
        grid1.addDropListener(e -> {
            if (dragSourceGrid != null && dragSourceGrid.equals(grid2)) {
                // L'élément est en train d'être déplacé de grid2 à grid1
                dataView2.removeItem(draggedItem);
                dataView1.addItem(draggedItem);
            }
            // Réinitialise la source du glisser-déposer après utilisation
            dragSourceGrid = null;
        });

        grid1.addDragEndListener(this::handleDragEnd);

        grid2.setDropMode(GridDropMode.ON_GRID);
        grid2.setRowsDraggable(true);
        grid2.addDragStartListener(this::handleDragStart);
        grid2.addDropListener(e -> {
            dataView2.addItem(draggedItem);
        });
        grid2.addDragEndListener(this::handleDragEnd);

        grid2.setDropMode(GridDropMode.BETWEEN);
        grid2.setRowsDraggable(true);

        grid2.addDragStartListener(e -> draggedItem = e.getDraggedItems().get(0));

        grid2.addDropListener(e -> {
            TypeOperation targetOperation = e.getDropTargetItem().orElse(null);
            GridDropLocation dropLocation = e.getDropLocation();

            boolean operationWasDroppedOntoItself = draggedItem.equals(targetOperation);

            if (targetOperation == null || operationWasDroppedOntoItself)
                return;

            dataView2.removeItem(draggedItem);

            if (dropLocation == GridDropLocation.BELOW) {
                dataView2.addItemAfter(draggedItem, targetOperation);
            } else {
                dataView2.addItemBefore(draggedItem, targetOperation);
            }
        });

        grid2.addDragEndListener(e -> draggedItem = null);

        Div container = new Div(grid1, grid2);
        setContainerStyles(container);

        add(container);
        configureFooter();
    }

    private void configureFooter() {
        Button saveButton = new Button("Enregistrer", (e) -> save());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);
        saveButton.getStyle().set("margin-right", "auto");
        this.getFooter().add(saveButton);

        Button cancelButton = new Button("Cancel", (e) -> this.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        this.getFooter().add(cancelButton);
    }

    private static Grid<TypeOperation> setupGrid(String header, boolean addOrderColumn) {
        Grid<TypeOperation> grid = new Grid<>(TypeOperation.class);
        grid.removeAllColumns();

        if (addOrderColumn) {
            grid.addComponentColumn(item -> createOrderLabel(grid, item))
                    .setHeader("Ordre").setSortable(true).setAutoWidth(true);
        }

        grid.addColumn(TypeOperation::getDes).setHeader(header);

        setGridStyles(grid);
        return grid;
    }

    private static void setGridStyles(Grid<TypeOperation> grid) {
        grid.getStyle().set("width", "300px").set("height", "300px")
                .set("margin-left", "0.5rem").set("margin-top", "0.5rem")
                .set("align-self", "unset");
    }


    private static Component createOrderLabel(Grid<TypeOperation> grid, TypeOperation item) {
        ListDataProvider<TypeOperation> dataProvider = (ListDataProvider<TypeOperation>) grid.getDataProvider();
        List<TypeOperation> items = new ArrayList<>(dataProvider.getItems());

        int order = items.indexOf(item) + 1;
        H6 label = new H6(String.valueOf(order));
        label.getStyle().set("margin", "auto");
        return label;
    }



    private void handleDragStart(GridDragStartEvent<TypeOperation> e) {
        draggedItem = e.getDraggedItems().get(0);
        dragSourceGrid = (Grid<TypeOperation>) e.getSource();
    }

    private void handleDragEnd(GridDragEndEvent<TypeOperation> e) {
        draggedItem = null;
    }


    private static void setContainerStyles(Div container) {
        container.getStyle().set("display", "flex").set("flex-direction", "row")
                .set("flex-wrap", "wrap");
    }


    private void save() {
        //TODO : Enregistrer les opérations définies pour un produit
        //On récupère les types d'opérations définies dans la grid2 et on instancie des Opérations pour le produit
        List<Operation> operations = new ArrayList<>();
        for (TypeOperation typeOperation : typeoperationsDefini) {
            Operation operation = new Operation();
            operation.setProduit(produit);
            operation.setTypeOperation(typeOperation);
            operation.setOrdre(typeoperationsDefini.indexOf(typeOperation));
            operations.add(operation);
            service.saveOperation(operation);
        }
        //On ferme la fenêtre

        this.close();
    }
}
