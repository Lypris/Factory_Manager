package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.LocalDateTime;
import java.util.*;

@Route(value = "machine-stats", layout = MainLayout.class)
@PageTitle("Statistiques des Machines | M3 Application")
public class MachineStatsView extends VerticalLayout {

    private final CrmService service;
    private ComboBox<Machine> machineComboBox;
    private ApexCharts chart;
    private List<EtatMachine> etatMachines = new ArrayList<>();

    public MachineStatsView(CrmService service) {
        this.service = service;
        this.setSizeFull();
        setupMachineComboBox();
        HorizontalLayout firstRow = createFirstRow();

        add(machineComboBox, firstRow);
    }
    private HorizontalLayout createFirstRow(){
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.setSizeFull();
/*
        VerticalLayout dataInfoLayout = createDataInfoLayout();
        dataInfoLayout.setMargin(false);
        dataInfoLayout.setPadding(false);
        dataInfoLayout.setWidth("1100px");
*/
        ApexCharts machinePieChart = createVendorPieChart();
        machinePieChart.setId("chart-section");
        machinePieChart.setWidthFull();


        firstRow.add(machinePieChart);

        return firstRow;
    }

    private void setupMachineComboBox() {
        machineComboBox = new ComboBox<>("Sélectionner une machine");
        machineComboBox.setItems(service.findAllMachines(""));
        machineComboBox.setItemLabelGenerator(Machine::getRef);
        machineComboBox.addValueChangeListener(e -> {
            Machine selectedMachine = machineComboBox.getValue();
            if(selectedMachine != null){
                this.etatMachines = service.findAllEtatMachineFinisByMachine(selectedMachine);
            }
        });
    }
    private void updateChart() {


        // crée un LinkedHashMap with key = description de l'état and value = pourcentage de cet état
        LinkedHashMap<String, Double> etatPourcentageMap = new LinkedHashMap<>();
        etatPourcentageMap.put("Disponible", 30.0);
        etatPourcentageMap.put("En panne", 30.0);
        etatPourcentageMap.put("En marche", 40.0);

        String[] etatDescriptions = etatPourcentageMap.keySet().toArray(new String[0]);
        Double [] etatPercentage = etatPourcentageMap.values().toArray(new Double[0]);

        chart.updateSeries(new Series<>(etatPercentage));
    }
    private long getDurationInMinutes(LocalDateTime start, LocalDateTime end) {
        return java.time.Duration.between(start, end).toMinutes();
    }
/*
    private Map<String, Long> calculateEtatDurations(List<EtatMachine> etatMachines) {
        Map<String, Long> etatDurations = new HashMap<>();
        // Logique pour calculer la durée de chaque état
        for (EtatMachine etatMachine : etatMachines) {
            String etat = etatMachine.getEtat().getDes();
            if (etatDurations.containsKey(etat)) {
                long duration = etatDurations.get(etat);
                duration += getDurationInMinutes(etatMachine.getDebut(), etatMachine.getFin());
                etatDurations.put(etat, duration);
            } else {
                etatDurations.put(etat, getDurationInMinutes(etatMachine.getDebut(), etatMachine.getFin()));
            }
        }
        return etatDurations;
    }

 */
    private ApexCharts createVendorPieChart() {
        // crée un LinkedHashMap with key = description de l'état and value = pourcentage de cet état
        LinkedHashMap<String, Double> etatPourcentageMap = new LinkedHashMap<>();
        etatPourcentageMap.put("Disponible", 30.0);
        etatPourcentageMap.put("En panne", 30.0);
        etatPourcentageMap.put("En marche", 40.0);

        String[] etatDescriptions = etatPourcentageMap.keySet().toArray(new String[0]);
        Double [] etatPercentage = etatPourcentageMap.values().toArray(new Double[0]);

        // Configuration de base du graphique
        ApexCharts chart = ApexChartsBuilder.get().withChart(ChartBuilder.get().withType(Type.PIE).build())
                .withTitle(TitleSubtitleBuilder.get().withText("Répartition des états de la machine").build())
                .withSeries(etatPercentage)
                .withLabels(etatDescriptions)
                .withLegend(LegendBuilder.get().withPosition(Position.RIGHT).build())
                // Ajouter d'autres configurations comme les titres, les couleurs, etc.
                .build();

        //updateChart(); // Mettre à jour le graphique avec les données initiales
        return chart;
    }

/*
    private VerticalLayout createDataInfoLayout(){
        VerticalLayout dataInfoLayout = new VerticalLayout();

        VerticalLayout upperLayout = new VerticalLayout();
        upperLayout.setSizeFull();
        upperLayout.setId("chart-section");

        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setSizeFull();
        bottomLayout.setId("chart-section");
        dataInfoLayout.add(upperLayout, bottomLayout);

        Label dataInfoTitleLabel = new Label("Informationen über die Daten");
        dataInfoTitleLabel.getElement().getStyle().set("font-family", "Helvetica, Arial, sans-serif");
        dataInfoTitleLabel.getElement().getStyle().set("font-size", "14px");
        dataInfoTitleLabel.getElement().getStyle().set("font-weight", "900");

        VerticalLayout dataInfoContentLayout = new VerticalLayout();
        dataInfoContentLayout.setSpacing(false);
        dataInfoContentLayout.setPadding(false);

        Label coursesCountLabel = new Label("# Kurse: " + courses.size());
        Label differentTopicsCountLabel = new Label("# Kurtstitel: " + getDifferentTopicsCount());
        Label differentVendorsCountLabel = new Label("# Anbieter: " + getDifferentVendorsCount());
        Label differentCitiesCountLabel = new Label("# Städte: " + getDifferentCitiesCount());
        Label differentBundeslandCountLabel = new Label("# Bundesländer: " + getDifferentBundeslandCount());
        Label averageWordCountInCourseTitlesLabel = new Label("Ø Wörter in Titeln: " + getAverageWordCountInCourseTitles());
        Label averageWordCountInCourseDescriptionLabel = new Label("Ø Wörter in Beschreibungen: " + getAverageWordCountInCourseDescription());

        Label openMapTitleLabel = new Label("Standorte der Kurse");
        openMapTitleLabel.getElement().getStyle().set("font-family", "Helvetica, Arial, sans-serif");
        openMapTitleLabel.getElement().getStyle().set("font-size", "14px");
        openMapTitleLabel.getElement().getStyle().set("font-weight", "900");

        Button openMapButton = new Button("Karte öffnen");
        openMapButton.getElement().getStyle().set("margin", "auto");

        // when button is clicked a new dialog window is opened
        openMapButton.addClickListener(this::openDialog);

        dataInfoContentLayout.add(coursesCountLabel, differentTopicsCountLabel, differentVendorsCountLabel,
                differentCitiesCountLabel, differentBundeslandCountLabel, averageWordCountInCourseTitlesLabel,
                averageWordCountInCourseDescriptionLabel);
        upperLayout.add(dataInfoTitleLabel, dataInfoContentLayout);
        bottomLayout.add(openMapTitleLabel, openMapButton);

        return dataInfoLayout;
    }
*/

}

