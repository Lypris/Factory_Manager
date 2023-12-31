package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.util.List;

@Route(value = "machine-stats", layout = MainLayout.class)
@PageTitle("Statistiques des Machines | M3 Application")
public class MachineStatsView extends VerticalLayout {

    private CrmService service;
    private ComboBox<Machine> machineComboBox;
    private Chart chart;

    public MachineStatsView(CrmService service) {
        this.service = service;
        setupMachineComboBox();
        setupChart();
        add(machineComboBox, chart);
    }

    private void setupMachineComboBox() {
        machineComboBox = new ComboBox<>("Sélectionner une machine");
        machineComboBox.setItems(service.findAllMachines(""));
        machineComboBox.setItemLabelGenerator(Machine::getRef);
        machineComboBox.addValueChangeListener(e -> updateChart());
    }

    private void setupChart() {
        chart = new Chart(ChartType.PIE);
        // Configuration supplémentaire du graphique si nécessaire
    }

    private void updateChart() {
        Machine selectedMachine = machineComboBox.getValue();
        if (selectedMachine != null) {
            List<EtatMachine> etats = service.findAllEtatMachineByMachine(selectedMachine);
            // Transforme les états en données pour le graphique
            // et met à jour le graphique
        }
    }
}

