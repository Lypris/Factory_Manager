package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Route(value = "machine-reliability", layout = MainLayout.class)
@PageTitle("Fiabilité des Machines | Factory Manager")
public class MachineReliabilityView extends VerticalLayout {

    private final CrmService service;
    private ApexCharts reliabilityChart;

    public MachineReliabilityView(CrmService service) {
        this.service = service;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        ComboBox<Machine> machineComboBox = new ComboBox<>("Sélectionner une machine");
        machineComboBox.setWidth("300px");
        machineComboBox.setItemLabelGenerator(Machine::getDes); // Remplacer getName par la méthode appropriée
        machineComboBox.setItems(service.findAllMachines(null));
        machineComboBox.addValueChangeListener(e -> updateChart(e.getValue()));

        add(machineComboBox);
    }

    private void updateChart(Machine selectedMachine) {
        if (reliabilityChart != null) {
            remove(reliabilityChart);
        }
        reliabilityChart = createReliabilityChart(selectedMachine);
        add(reliabilityChart);
    }

    private ApexCharts createReliabilityChart(Machine machine) {
        // Ici, tu dois définir comment tu récupères et traite les données de l'historique d'états de la machine
        List<EtatMachine> etatMachines = service.findAllEtatMachineFinisByMachine(machine);
        LinkedHashMap<String, Double> statePercentages = calculateStateDurations(etatMachines);
        // Créer les séries pour le graphique
        Double[] percentages = statePercentages.values().toArray(new Double[0]);
        String[] states = statePercentages.keySet().toArray(new String[0]);

        return new ApexChartsBuilder()
                .withChart(ChartBuilder.get()
                        .withType(Type.PIE)
                        .build())
                .withTitle(TitleSubtitleBuilder.get()
                        .withText("Fiabilité de la machine " + machine.getDes())
                        .build())
                .withSeries(percentages)
                .withLabels(states)
                .build();
    }
    private LinkedHashMap<String, Double> calculateStateDurations(List<EtatMachine> etatMachines) {
        LinkedHashMap<String, Long> durationMap = new LinkedHashMap<>();
        LinkedHashMap<String, Double> percentageMap = new LinkedHashMap<>();
        long totalDuration = 0;

        // Calculer la durée totale pour chaque état
        for (EtatMachine etatMachine : etatMachines) {
            String etat = etatMachine.getEtat().getDes();
            LocalDateTime start = etatMachine.getDebut();
            LocalDateTime end = etatMachine.getFin() != null ? etatMachine.getFin() : LocalDateTime.now();
            long duration = Duration.between(start, end).toMinutes(); // Durée en minutes
            durationMap.merge(etat, duration, Long::sum);
            totalDuration += duration;
        }

        // Convertir les durées en pourcentages
        if (totalDuration > 0) {
            for (Map.Entry<String, Long> entry : durationMap.entrySet()) {
                double percentage = (entry.getValue() * 100.0) / totalDuration;
                percentageMap.put(entry.getKey(), percentage);
            }
        }

        return percentageMap;
    }
}
