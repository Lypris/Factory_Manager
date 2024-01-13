package fr.insa.trenchant_troullier_virquin.factory_manager.view;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.service.CrmService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Route(value = "machine-stats", layout = MainLayout.class)
@PageTitle("Statistiques des Machines | Factory Manager")
public class MachineStatsView extends VerticalLayout {

    private final CrmService service;
    private ComboBox<Machine> machineComboBox;
    private ApexCharts chart;
    private List<EtatMachine> etatMachines = new ArrayList<>();
    LinkedHashMap<String, Double> etatPourcentageMap = new LinkedHashMap<>();

    public MachineStatsView(CrmService service) {
        this.service = service;
        this.setSizeFull();
        HorizontalLayout firstRow = createFirstRow();
        setupMachineComboBox();
        add(machineComboBox, firstRow);
    }
    private HorizontalLayout createFirstRow(){
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.setSizeFull();

        VerticalLayout dataInfoLayout = createDataInfoLayout();
        dataInfoLayout.setMargin(false);
        dataInfoLayout.setPadding(false);
        dataInfoLayout.setWidth("1100px");

        chart = createVendorPieChart();
        chart.setId("chart-section");
        chart.setWidthFull();
        firstRow.add(dataInfoLayout, chart);
        return firstRow;
    }

    private VerticalLayout createDataInfoLayout() {
        //on affiche les informations sur la machine
        VerticalLayout dataInfoLayout = new VerticalLayout();
        dataInfoLayout.setId("chart-section");
        dataInfoLayout.setSpacing(false);
        dataInfoLayout.setPadding(false);
        Text dataInfoTitleLabel = new Text("Informations sur la machine");
        Text machineRefLabel = new Text("Référence: ");
        Text machineDesLabel = new Text("Description: ");
        Text machineTypeOperationLabel = new Text("Type d'opération: ");
        dataInfoLayout.add(dataInfoTitleLabel, machineRefLabel, machineDesLabel, machineTypeOperationLabel);
        return dataInfoLayout;
    }

    private void setupMachineComboBox() {
        machineComboBox = new ComboBox<>("Sélectionner une machine");
        machineComboBox.setItems(service.findAllMachines(""));
        machineComboBox.setItemLabelGenerator(Machine::getRef);
        machineComboBox.addValueChangeListener(e -> {
            Machine selectedMachine = machineComboBox.getValue();
            if(selectedMachine != null){
                this.etatMachines = service.findAllEtatMachineFinisByMachine(selectedMachine);
                updateChart(); // Mise à jour du graphique au lieu de le recréer
            }
        });
    }
    private void updateChart() {
        // Calculer les nouveaux pourcentages
        etatPourcentageMap.put("disponible", calculateEtatPourcentage("disponible"));
        etatPourcentageMap.put("en panne", calculateEtatPourcentage("en panne"));
        etatPourcentageMap.put("en marche", calculateEtatPourcentage("en marche"));

        // Mettre à jour les données du graphique
        String[] etatDescriptions = etatPourcentageMap.keySet().toArray(new String[0]);
        Double [] etatPercentage = etatPourcentageMap.values().toArray(new Double[0]);

        chart.updateSeries(new Series<>(etatPercentage));
        chart.setLabels(etatDescriptions);
        // Tu peux aussi avoir besoin d'appeler une méthode pour rafraîchir le graphique ici
    }

    private Double getDurationInMinutes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0.0; // Retourne 0 si l'une des dates est null
        }
        Duration duration = Duration.between(start, end);
        return (double) duration.toMinutes(); // Convertit la durée en minutes et la retourne en tant que Double
    }

    //méthode qui calcul le pourcentage de temps passé dans un état donné
    private Double calculateEtatPourcentage(String des) {
        Double duration = calculateEtatDurations(des);
        Double totalDuration = calculateEtatDurations("disponible") + calculateEtatDurations("en panne") + calculateEtatDurations("en marche");
        duration =duration / totalDuration * 100;
        return duration;
    }

    // Modifie la méthode de comparaison des chaînes
    private Double calculateEtatDurations(String des) {
        Double duration = 0.0;
        for (EtatMachine etatMachine : etatMachines) {
            if (etatMachine.getEtat().getDes().equals(des)) { // Utilise equals au lieu de ==
                LocalDateTime debut = etatMachine.getDebut();
                LocalDateTime fin = etatMachine.getFin();
                if (fin != null) {
                    duration += getDurationInMinutes(debut, fin);
                } else {
                    duration += getDurationInMinutes(debut, LocalDateTime.now());
                }
            }
        }
        return duration;
    }

    private ApexCharts createVendorPieChart() {
        // Initialisation avec les valeurs par défaut
        etatPourcentageMap.put("disponible", 0.0);
        etatPourcentageMap.put("en panne", 0.0);
        etatPourcentageMap.put("en marche", 0.0);

        String[] etatDescriptions = etatPourcentageMap.keySet().toArray(new String[0]);
        Double [] etatPercentage = etatPourcentageMap.values().toArray(new Double[0]);

        chart = ApexChartsBuilder.get().withChart(ChartBuilder.get().withType(Type.PIE).build())
                .withTitle(TitleSubtitleBuilder.get().withText("Répartition des états de la machine").build())
                .withSeries(etatPercentage)
                .withLabels(etatDescriptions)
                .withLegend(LegendBuilder.get().withPosition(Position.RIGHT).build())
                // Ajouter d'autres configurations comme les titres, les couleurs, etc.
                .build();
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

