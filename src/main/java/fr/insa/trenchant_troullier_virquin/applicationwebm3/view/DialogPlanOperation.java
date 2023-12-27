package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.data.provider.ListDataProvider;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.TypeOperation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.util.ArrayList;
import java.util.List;
/*
public class DialogPlanOperation extends Dialog {
    private TypeOperation draggedItem;
    CrmService service;
    Grid<TypeOperation> grid1 = setupGrid("Opérations disponibles");
    Grid<Operation> grid2 = setupGridOp("Opérations définies", true);

    ArrayList<TypeOperation> typeoperations = new ArrayList<>();
    ArrayList<Operation> operationsDefini = new ArrayList<>();
    GridListDataView<TypeOperation> dataView1 = grid1.setItems(typeoperations);
    GridListDataView<Operation> dataView2 = grid2.setItems(operationsDefini);

    public DialogPlanOperation (List<TypeOperation> typeOperations, CrmService service, Produit produit) {
        this.service = service;
        this.typeoperations.addAll(typeOperations);
        this.operationsDefini.addAll(service.findOperationByProduit(produit));
        grid1.setDropMode(GridDropMode.ON_GRID);
        grid1.setRowsDraggable(true);
        grid1.addDragStartListener(this::handleDragStart);
        grid1.addDropListener(e -> {
            // L'élément est en train d'être déplacé de grid2 à grid1
            //TODO: supprimer l'opération de la liste des opérations définies
                dataView1.addItem(draggedItem);
            });

        grid1.addDragEndListener(this::handleDragEnd);


        grid2.addDropListener(e -> {
            Operation targetOperation = e.getDropTargetItem().orElse(null);
            GridDropLocation dropLocation = e.getDropLocation();

            //boolean operationWasDroppedOntoItself = draggedItem.equals(targetOperation);

            if (targetOperation == null || operationWasDroppedOntoItself)
                return;
            //on récupère l'opération qui a été déplacée
            //on supprime l'opération de la liste des opérations disponibles

            dataView2.removeItem(draggedItem);

            Operation operation = new Operation(draggedItem); // Créez une nouvelle instance d'Operation à partir de TypeOperation

            if (dropLocation == GridDropLocation.BELOW) {
                dataView2.addItemAfter(operation, targetOperation);
            } else {
                dataView2.addItemBefore(operation, targetOperation);
            }
        });
    }

    private static Grid<TypeOperation> setupGrid(String header) {
        Grid<TypeOperation> grid = new Grid<>(TypeOperation.class);
        grid.removeAllColumns();
        grid.addColumn(TypeOperation::getDes).setHeader(header);
        setGridStyles(grid);
        return grid;
    }
    private static Grid<Operation> setupGridOp(String header, boolean addOrderColumn) {
        Grid<Operation> grid = new Grid<>(Operation.class);
        grid.removeAllColumns();
        if (addOrderColumn) {
            grid.addComponentColumn(item -> createOrderLabel(grid, item.getTypeOperation()))
                    .setHeader("Ordre").setSortable(true).setAutoWidth(true);
        }

        grid.addColumn(Operation ->
                Operation.getTypeOperation().getDes()
        ).setHeader(header);

        setGridStylesOp(grid);
        return grid;
    }
    private static void setGridStyles(Grid<TypeOperation> grid) {
        grid.getStyle().set("width", "300px").set("height", "300px")
                .set("margin-left", "0.5rem").set("margin-top", "0.5rem")
                .set("align-self", "unset");
    }
    private static void setGridStylesOp(Grid<Operation> grid) {
        grid.getStyle().set("width", "300px").set("height", "300px")
                .set("margin-left", "0.5rem").set("margin-top", "0.5rem")
                .set("align-self", "unset");
    }


    private static Component createOrderLabel(Grid<Operation> grid, TypeOperation item) {
        //on récupère la liste des opérations
        ListDataProvider<Operation> dataProvider = (ListDataProvider<Operation>) grid.getDataProvider();
        List<Operation> items = new ArrayList<>(dataProvider.getItems());
        //on récupère l'ordre de l'opération
        int order = items.indexOf(item) + 1;
        H6 label = new H6(String.valueOf(order));
        label.getStyle().set("margin", "auto");
        return label;
    }
    private void configureFooter() {
        Button saveButton = new Button("Enregistrer", (e) -> this.close()); //TODO : Enregistrer les opérations
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);
        saveButton.getStyle().set("margin-right", "auto");
        this.getFooter().add(saveButton);

        Button cancelButton = new Button("Cancel", (e) -> this.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        this.getFooter().add(cancelButton);
    }
    private void handleDragStart(GridDragStartEvent<TypeOperation> e) {
        draggedItem = e.getDraggedItems().get(0);
    }

    private void handleDragEnd(GridDragEndEvent<TypeOperation> e) {
        draggedItem = null;
    }


    private static void setContainerStyles(Div container) {
        container.getStyle().set("display", "flex").set("flex-direction", "row")
                .set("flex-wrap", "wrap");
    }
}
*/