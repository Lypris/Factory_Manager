/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.factory_manager.view;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Operateur;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.StatutOperateur;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.service.CrmService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Factory Manager")
public class DashboardView extends VerticalLayout {
    private final CrmService service;
    private List<StatutOperateur> statutOperateurs = new ArrayList<>();
    private List<EtatMachine> etatMachines = new ArrayList<>();

    public DashboardView(CrmService service) {
        this.service = service;
        this.setSizeFull();
        this.setId("dashboard-view");
        HorizontalLayout firstRow = createFirstRow();
        HorizontalLayout secondRow = createSecondRow();
        HorizontalLayout thirdRow = createThirdRow();
        firstRow.setHeight("20em");
        add(firstRow, secondRow, thirdRow);
    }

    private HorizontalLayout createThirdRow() {
        HorizontalLayout thirdRow = new HorizontalLayout();
        thirdRow.setSizeFull();

        //graphique des commandes terminées sur l'année
        ApexCharts completedOrdersPerMonthChart = createCompletedOrdersPerMonthChart();
        completedOrdersPerMonthChart.setId("chart-section");
        completedOrdersPerMonthChart.setWidthFull();

        //graphique des machines
        ApexCharts machineChart = createMachineChart();
        machineChart.setId("chart-section");
        machineChart.setWidthFull();

        thirdRow.add(completedOrdersPerMonthChart, machineChart);

        return thirdRow;
    }

    private HorizontalLayout createSecondRow(){
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.setSizeFull();

        VerticalLayout dataInfoLayout = createDataInfoLayout();
        dataInfoLayout.setMargin(false);
        dataInfoLayout.setPadding(false);
        dataInfoLayout.setWidth("1100px");

        //graphique des employés
        ApexCharts operateurChart = createOccupationChart();
        operateurChart.setId("chart-section");
        operateurChart.setWidthFull();

        //graphique des machines
        ApexCharts machineChart = createMachineOccupationChart();
        machineChart.setId("chart-section");
        machineChart.setWidthFull();



        firstRow.add(dataInfoLayout, operateurChart, machineChart);

        return firstRow;
    }

    private ApexCharts createMachineChart() {
        LinkedHashMap<String, Double> etatMachinePourcentageMap = new LinkedHashMap<>();
        List<Machine> machines = service.findAllMachines(null);
        for (Machine m : machines) {
            etatMachines.addAll(service.findAllEtatMachineFinisByMachine(m));
        }
        // Calculer le pourcentage de chaque statut
        Double enPannePourcentage = calculateEtatTime("en panne", etatMachines);
        Double enMarchePourcentage = calculateEtatTime("en marche", etatMachines);
        Double enMaintenancePourcentage = calculateEtatTime("disponible", etatMachines);
        etatMachinePourcentageMap.put("en panne", enPannePourcentage);
        etatMachinePourcentageMap.put("en marche", enMarchePourcentage);
        etatMachinePourcentageMap.put("disponible", enMaintenancePourcentage);

        String[] etatNames = etatMachinePourcentageMap.keySet().toArray(new String[0]);
        Double[] etatPercentage = etatMachinePourcentageMap.values().toArray(new Double[0]);

        ApexCharts chart = new ApexChartsBuilder()
                .withChart(ChartBuilder.get().withType(Type.PIE).build())
                .withTitle(TitleSubtitleBuilder.get().withText("Historique des états des machines").build())
                .withLabels(etatNames)
                .withSeries(etatPercentage)
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.RIGHT)
                        .build())
                .build();
        return chart;
    }

    private ApexCharts createOperateurChart() {
        LinkedHashMap<String, Double> etatOperateurPourcentageMap = new LinkedHashMap<>();
        List<Operateur> operateurs = service.findAllOperateurs(null);
        for (Operateur o : operateurs) {
            statutOperateurs.addAll(service.findAllStatutOperateurFinisByOperateur(o));
        }
        // Calculer le pourcentage de chaque statut
        Double presentPourcentage = calculateStatutTime("présent",statutOperateurs);
        Double absentPourcentage = calculateStatutTime("absent", statutOperateurs);
        Double congéPourcentage = calculateStatutTime("en congé",statutOperateurs);
        etatOperateurPourcentageMap.put("disponible", presentPourcentage);
        etatOperateurPourcentageMap.put("absent", absentPourcentage);
        etatOperateurPourcentageMap.put("en congé", congéPourcentage);

        String[] statutNames = etatOperateurPourcentageMap.keySet().toArray(new String[0]);
        Double[] statutPercentage = etatOperateurPourcentageMap.values().toArray(new Double[0]);

        ApexCharts chart = new ApexChartsBuilder()
                .withChart(ChartBuilder.get().withType(Type.BAR).build())
                .withTitle(TitleSubtitleBuilder.get().withText("Historique des statuts des opérateurs").build())
                .withLabels(statutNames)
                .withSeries(statutPercentage)
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.RIGHT)
                        .build())
                .build();
        return chart;
    }

    private ApexCharts createOccupationChart() {
        LinkedHashMap<String, Double> occupationRates = getCurrentOccupationRates();

        String[] categories = occupationRates.keySet().toArray(new String[0]);
        Double[] percentages = occupationRates.values().toArray(new Double[0]);

        ApexCharts chart = new ApexChartsBuilder()
                .withChart(ChartBuilder.get().withType(Type.PIE).build())
                .withTitle(TitleSubtitleBuilder.get().withText("États actuels des opérateurs").build())
                .withLabels(categories)
                .withSeries(percentages)
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.RIGHT)
                        .build())
                .build();
        return chart;
    }
    private ApexCharts createMachineOccupationChart(){
        LinkedHashMap<String, Double> occupationRates = getCurrentMachineOccupationRates();

        String[] categories = occupationRates.keySet().toArray(new String[0]);
        Double[] percentages = occupationRates.values().toArray(new Double[0]);

        ApexCharts chart = new ApexChartsBuilder()
                .withChart(ChartBuilder.get().withType(Type.PIE).build())
                .withTitle(TitleSubtitleBuilder.get().withText("États actuels des machines").build())
                .withLabels(categories)
                .withSeries(percentages)
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.RIGHT)
                        .build())
                .build();
        return chart;
    }

    private HorizontalLayout createFirstRow(){
        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.setId("title-section");
        secondRow.setSizeFull();
        H1 title = new H1("Tableau de bord");
        title.getElement().getStyle().set("margin", "auto");
        secondRow.add(title);
        secondRow.setAlignItems(Alignment.CENTER);
        return secondRow;
    }
    private VerticalLayout createDataInfoLayout() {
        VerticalLayout dataInfoLayout = new VerticalLayout();

        VerticalLayout upperLayout = new VerticalLayout();
        upperLayout.setSizeFull();
        upperLayout.setId("info-section");

        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setSizeFull();
        bottomLayout.setId("chart-section");
        dataInfoLayout.add(upperLayout, bottomLayout);

        H1 dataInfoTitleLabel = new H1("Informations sur l'entreprise");
        dataInfoTitleLabel.getElement().getStyle().set("font-size", "20px");
        dataInfoTitleLabel.getStyle().set("margin-top", "20px");
        dataInfoTitleLabel.getStyle().set("margin-bottom", "20px");
        dataInfoTitleLabel.getElement().getStyle().set("margin-left", "auto");
        dataInfoTitleLabel.getElement().getStyle().set("margin-right", "auto");

        // Utilise HorizontalLayout pour aligner sur sa valeur
        HorizontalLayout operateurCountLayout = new HorizontalLayout();
        Text operateurCountLabel = new Text("Nombre d'employés : ");
        Text operateurCountValue = new Text(String.valueOf(service.countOperateurs()));
        operateurCountLayout.add(operateurCountLabel, operateurCountValue);
        operateurCountLayout.setId("text-section");

        HorizontalLayout machineCountLayout = new HorizontalLayout();
        Text machineCountLabel = new Text("Nombre de machines : ");
        Text machineCountValue = new Text(String.valueOf(service.countMachines()));
        machineCountLayout.add(machineCountLabel, machineCountValue);
        machineCountLayout.setId("text-section");

        HorizontalLayout commandeCountLayout = new HorizontalLayout();
        Text commandeCountLabel = new Text("Nombre de commandes : ");
        Text commandeCountValue = new Text(String.valueOf(service.countCommandes()));
        commandeCountLayout.add(commandeCountLabel, commandeCountValue);
        commandeCountLayout.setId("text-section");

        HorizontalLayout produitCountLayout = new HorizontalLayout();
        Text produitCountLabel = new Text("Nombre de produits : ");
        Text produitCountValue = new Text(String.valueOf(service.countProduits()));
        produitCountLayout.add(produitCountLabel, produitCountValue);
        produitCountLayout.setId("text-section");

        // Ajout des HorizontalLayouts au dataInfoContentLayout
        VerticalLayout dataInfoContentLayout = new VerticalLayout();
        dataInfoContentLayout.setPadding(false);
        dataInfoContentLayout.setSpacing(false);

        H1 MoreInfoTitleLabel = new H1("Chiffre d'affaire");
        MoreInfoTitleLabel.getStyle().set("margin-top", "20px");
        MoreInfoTitleLabel.getStyle().set("margin-bottom", "10px");
        MoreInfoTitleLabel.getElement().getStyle().set("margin-left", "auto");
        MoreInfoTitleLabel.getElement().getStyle().set("margin-right", "auto");
        MoreInfoTitleLabel.getElement().getStyle().set("font-size", "20px");

        HorizontalLayout chiffreAffaireLayout = new HorizontalLayout();
        Text chiffreAffaireValue = new Text(String.valueOf(service.getChiffreAffaireAnnuel()));
        Text chiffreAffaireUnit = new Text(" € (en " + LocalDateTime.now().getYear() + ")");
        chiffreAffaireLayout.add(chiffreAffaireValue, chiffreAffaireUnit);
        chiffreAffaireLayout.setId("chiffre-affaire-section");
        // Configure le layout
        chiffreAffaireLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER); // Centrer verticalement dans le layout
        chiffreAffaireLayout.setJustifyContentMode(JustifyContentMode.CENTER); // Centrer horizontalement dans le layout
        chiffreAffaireLayout.setWidthFull(); // Utilise toute la largeur disponible pour le centrage horizontal

        chiffreAffaireLayout.add(chiffreAffaireValue, chiffreAffaireUnit);
        chiffreAffaireLayout.setId("chiffre-affaire-section");

        // Ajoute les composants au layout de datas
        dataInfoContentLayout.add(operateurCountLayout, machineCountLayout, commandeCountLayout, produitCountLayout);
        upperLayout.add(dataInfoTitleLabel, dataInfoContentLayout);
        bottomLayout.add(MoreInfoTitleLabel, chiffreAffaireLayout);

        return dataInfoLayout;
    }

    //méthode qui calcule le temps total d'un statut
    private Double calculateStatutTime(String statut, List<StatutOperateur> statuts) {
        Double total = 0.0;
        for (StatutOperateur s : statuts) {
            if (s.getStatut().getName().equals(statut)) {
                total += Duration.between(s.getDebut(), s.getFin()).toMinutes();
            }
        }
        return total;
    }

    //méthode qui calcule le temps total d'un état de machine
    private Double calculateEtatTime(String etat, List<EtatMachine> etats) {
        Double total = 0.0;
        if (etats == null || etats.isEmpty()) {
            return total;
        }
        for (EtatMachine s : etats) {
            if (s.getEtat().getDes().equals(etat)) {
                total += Duration.between(s.getDebut(), s.getFin()).toMinutes();
            }
        }
        return total;
    }

    public LinkedHashMap<String, Double> getCurrentOccupationRates() {
        LinkedHashMap<String, Double> occupationRates = new LinkedHashMap<>();
        List<StatutOperateur> allStatutOperateurs = service.findAllStatutOperateurs(null);

        // Initier les compteurs pour chaque catégorie
        double presentCount = 0;
        double absentCount = 0;
        double congeCount = 0;

        // Obtenir la date et l'heure actuelle
        LocalDateTime now = LocalDateTime.now();

        // Parcourir la liste des statuts et incrémenter les compteurs
        for (StatutOperateur statutOperateur : allStatutOperateurs) {
            // Assure-toi que le statut actuel est considéré
            if (statutOperateur.getDebut().isBefore(now) && (statutOperateur.getFin() == null || statutOperateur.getFin().isAfter(now))) {
                switch (statutOperateur.getStatut().getName()) {
                    case "disponible":
                        presentCount++;
                        break;
                    case "absent":
                        absentCount++;
                        break;
                    case "en congé":
                        congeCount++;
                        break;
                }
            }
        }

        // Calculer le pourcentage pour chaque catégorie
        double total = presentCount + absentCount + congeCount;
        if (total > 0) {
            occupationRates.put("Présent", (presentCount / total) * 100);
            occupationRates.put("Absent", (absentCount / total) * 100);
            occupationRates.put("En Congé", (congeCount / total) * 100);
        }

        return occupationRates;
    }
    public LinkedHashMap<String, Double> getCurrentMachineOccupationRates(){
        LinkedHashMap<String, Double> occupationRates = new LinkedHashMap<>();
        List<EtatMachine> allEtatMachines = service.findAllEtatMachines(null);
        double enPanneCount = 0;
        double enMarcheCount = 0;
        double disponibleCount = 0;
        LocalDateTime now = LocalDateTime.now();
        for (EtatMachine etatMachine : allEtatMachines) {
            if (etatMachine.getDebut().isBefore(now) && (etatMachine.getFin() == null || etatMachine.getFin().isAfter(now))) {
                switch (etatMachine.getEtat().getDes()) {
                    case "en panne":
                        enPanneCount++;
                        break;
                    case "en marche":
                        enMarcheCount++;
                        break;
                    case "disponible":
                        disponibleCount++;
                        break;
                }
            }
        }
        double total = enPanneCount + enMarcheCount + disponibleCount;
        if (total > 0) {
            occupationRates.put("En Panne", (enPanneCount / total) * 100);
            occupationRates.put("En Marche", (enMarcheCount / total) * 100);
            occupationRates.put("Disponible", (disponibleCount / total) * 100);
        }
        return occupationRates;
    }


    private ApexCharts createOperatorStatusPerMonthChart() {
        // Ces maps vont contenir le nombre d'opérateurs par statut pour chaque mois
        Map<Integer, Long> presentCountPerMonth = new TreeMap<>();
        Map<Integer, Long> absentCountPerMonth = new TreeMap<>();
        Map<Integer, Long> congeCountPerMonth = new TreeMap<>();

        // Initialise les maps avec les mois (de 1 à 12) et le compteur à 0
        for (int i = 1; i <= 12; i++) {
            presentCountPerMonth.put(i, 0L);
            absentCountPerMonth.put(i, 0L);
            congeCountPerMonth.put(i, 0L);
        }

        // Imaginons que tu as une méthode pour obtenir tous les statuts opérateurs avec leur date
        List<StatutOperateur> allStatutOperateurs = service.findAllStatutOperateurs(null);

        // Parcours la liste et comptabilise les statuts par mois
        for (StatutOperateur statut : allStatutOperateurs) {
            // Ici, on suppose que tu as une méthode pour obtenir le mois de la date du statut
            int month = statut.getDebut().getMonthValue();
            String statutName = statut.getStatut().getName();
            presentCountPerMonth.computeIfPresent(month, (key, value) -> value + ("disponible".equals(statutName) ? 1 : 0));
            absentCountPerMonth.computeIfPresent(month, (key, value) -> value + ("absent".equals(statutName) ? 1 : 0));
            congeCountPerMonth.computeIfPresent(month, (key, value) -> value + ("en congé".equals(statutName) ? 1 : 0));
        }

        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez"};

        List<Double> presentPerMonth = presentCountPerMonth.values().stream().map(Long::doubleValue).collect(Collectors.toList());
        List<Double> absentPerMonth = absentCountPerMonth.values().stream().map(Long::doubleValue).collect(Collectors.toList());
        List<Double> congePerMonth = congeCountPerMonth.values().stream().map(Long::doubleValue).collect(Collectors.toList());

        ApexCharts operatorStatusPerMonthChart = new ApexChartsBuilder()
                .withChart(ChartBuilder.get()
                        .withType(Type.BAR)
                        .build())
                .withTitle(TitleSubtitleBuilder.get().withText("Statuts des opérateurs par mois").build())
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get()
                                .withHorizontal(false)
                                .withColumnWidth("55%")
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false).build())
                .withStroke(StrokeBuilder.get()
                        .withShow(true)
                        .withWidth(2.0)
                        .withColors("transparent")
                        .build())
                .withSeries(
                        new Series<>("Présent", presentPerMonth.toArray()),
                        new Series<>("Absent", absentPerMonth.toArray()),
                        new Series<>("En Congé", congePerMonth.toArray())
                )
                .withXaxis(XAxisBuilder.get().withCategories(monthNames).build())
                .build();
        return operatorStatusPerMonthChart;
    }

    private ApexCharts createCompletedOrdersPerMonthChart() {
        // Utiliser TreeMap pour garder les mois en ordre
        Map<Integer, Long> completedOrdersCountPerMonth = new TreeMap<>();

        // Initialiser le compteur pour chaque mois de l'année
        for (int i = 1; i <= 12; i++) {
            completedOrdersCountPerMonth.put(i, 0L);
        }

        // méthode pour obtenir toutes les commandes terminées
        List<Commande> allCompletedOrders = service.findAllCommandeTerminee();

        // Parcours la liste et comptabilise les commandes terminées par mois
        for (Commande commande : allCompletedOrders) {
            // Vérifie si la commande est terminée
            if (commande.getFin() != null) {
                int month = commande.getFin().getMonthValue();
                completedOrdersCountPerMonth.computeIfPresent(month, (key, value) -> value + 1);
            }
        }

        String[] monthNames = {"Jan", "Fév", "Mars", "Avr", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Dec"};

        List<Double> completedOrdersPerMonth = completedOrdersCountPerMonth.values().stream()
                .map(Long::doubleValue)
                .collect(Collectors.toList());

        ApexCharts completedOrdersPerMonthChart = new ApexChartsBuilder()
                .withChart(ChartBuilder.get()
                        .withType(Type.BAR)
                        .build())
                .withTitle(TitleSubtitleBuilder.get().withText("Commandes terminées par mois").build())
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get()
                                .withHorizontal(false)
                                .withColumnWidth("55%")
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false).build())
                .withStroke(StrokeBuilder.get()
                        .withShow(true)
                        .withWidth(2.0)
                        .withColors("transparent")
                        .build())
                .withSeries(new Series<>("Commandes Terminées", completedOrdersPerMonth.toArray()))
                .withXaxis(XAxisBuilder.get().withCategories(monthNames).build())
                .build();
        return completedOrdersPerMonthChart;
    }


}
