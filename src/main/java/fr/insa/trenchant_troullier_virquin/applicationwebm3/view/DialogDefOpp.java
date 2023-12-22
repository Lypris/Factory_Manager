package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.model.Typeoperation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.view.ProductForm;

    //    import com.vaadin.demo.domain.DataService;
        import com.vaadin.flow.component.grid.Grid;
        import com.vaadin.flow.component.grid.dataview.GridListDataView;
        import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
        import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
        import com.vaadin.flow.component.grid.dnd.GridDropMode;
        import com.vaadin.flow.component.html.Div;
        import com.vaadin.flow.router.Route;

        import java.util.ArrayList;
        import java.util.List;

public class DialogDefOpp extends Div {

    private Typeoperation draggedItem;

    public DialogDefOpp() {
        List<Typeoperation> typeoperations = null;//A definir// DataService.gettypeoperations(10);
        ArrayList<Typeoperation> typeoperationsPossible = new ArrayList<>(typeoperations);
        ArrayList<Typeoperation> typeoperationsChoisie = new ArrayList<>();

        // tag::snippet[]
        Grid<Typeoperation> grid1 = setupGrid();
        Grid<Typeoperation> grid2 = setupGrid();

        GridListDataView<Typeoperation> dataView1 = grid1.setItems(typeoperationsPossible);
        GridListDataView<Typeoperation> dataView2 = grid2.setItems(typeoperationsChoisie);

        grid1.setDropMode(GridDropMode.ON_GRID);
        grid1.setRowsDraggable(true);
        grid1.addDragStartListener(this::handleDragStart);
        grid1.addDropListener(e -> {
            dataView2.removeItem(draggedItem);
            dataView1.addItem(draggedItem);
        });
        grid1.addDragEndListener(this::handleDragEnd);

        grid2.setDropMode(GridDropMode.ON_GRID);
        grid2.setRowsDraggable(true);
        grid2.addDragStartListener(this::handleDragStart);
        grid2.addDropListener(e -> {
            dataView1.removeItem(draggedItem);
            dataView2.addItem(draggedItem);
        });
        grid2.addDragEndListener(this::handleDragEnd);
        // end::snippet[]

        Div container = new Div(grid1, grid2);
        setContainerStyles(container);

        add(container);
    }

    private static Grid<Typeoperation> setupGrid() {
        Grid<Typeoperation> grid = new Grid<>(Typeoperation.class, false);
        grid.addColumn(Typeoperation::getDes).setHeader("Opération");
        setGridStyles(grid);

        return grid;
    }

    private void handleDragStart(GridDragStartEvent<Typeoperation> e) {
        draggedItem = e.getDraggedItems().get(0);
    }

    private void handleDragEnd(GridDragEndEvent<Typeoperation> e) {
        draggedItem = null;
    }

    private static void setGridStyles(Grid<Typeoperation> grid) {
        grid.getStyle().set("width", "300px").set("height", "300px")
                .set("margin-left", "0.5rem").set("margin-top", "0.5rem")
                .set("align-self", "unset");
    }

    private static void setContainerStyles(Div container) {
        container.getStyle().set("display", "flex").set("flex-direction", "row")
                .set("flex-wrap", "wrap");
    }

}
