/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Statut;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.StatutOperateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Appli TP Info")
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
        firstRow.setHeight("15em");
        add(firstRow, secondRow);
    }
    private HorizontalLayout createSecondRow(){
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.setSizeFull();

        VerticalLayout dataInfoLayout = createDataInfoLayout();
        dataInfoLayout.setMargin(false);
        dataInfoLayout.setPadding(false);
        dataInfoLayout.setWidth("1100px");

        //graphique des employés
        ApexCharts operateurChart = createOperateurChart();
        operateurChart.setId("chart-section");
        operateurChart.setWidthFull();

        //graphique des machines
        ApexCharts machineChart = createMachineChart();
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
                .withTitle(TitleSubtitleBuilder.get().withText("États des machines").build())
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
        statutOperateurs = service.findAllStatutOperateurs(null);
        // Calculer le pourcentage de chaque statut
        Double presentPourcentage = calculateStatutTime("présent",statutOperateurs);
        Double absentPourcentage = calculateStatutTime("absent", statutOperateurs);
        Double congéPourcentage = calculateStatutTime("en congé",statutOperateurs);
        etatOperateurPourcentageMap.put("présent", presentPourcentage);
        etatOperateurPourcentageMap.put("absent", absentPourcentage);
        etatOperateurPourcentageMap.put("en congé", congéPourcentage);

        String[] statutNames = etatOperateurPourcentageMap.keySet().toArray(new String[0]);
        Double[] statutPercentage = etatOperateurPourcentageMap.values().toArray(new Double[0]);

        ApexCharts chart = new ApexChartsBuilder()
                .withChart(ChartBuilder.get().withType(Type.PIE).build())
                .withTitle(TitleSubtitleBuilder.get().withText("Statuts des opérateurs").build())
                .withLabels(statutNames)
                .withSeries(statutPercentage)
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
        upperLayout.setId("chart-section");

        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setSizeFull();
        bottomLayout.setId("chart-section");
        dataInfoLayout.add(upperLayout, bottomLayout);

        H1 dataInfoTitleLabel = new H1("Informations sur l'entreprise");
        dataInfoTitleLabel.getElement().getStyle().set("font-size", "20px");
        dataInfoTitleLabel.getStyle().set("margin-top", "20px");
        dataInfoTitleLabel.getStyle().set("margin-bottom", "20px");

        // Utilise HorizontalLayout pour aligner sur sa valeur
        HorizontalLayout operateurCountLayout = new HorizontalLayout();
        Text operateurCountLabel = new Text("Nombre d'employés: ");
        Text operateurCountValue = new Text(String.valueOf(service.countOperateurs()));
        operateurCountLayout.add(operateurCountLabel, operateurCountValue);

        HorizontalLayout machineCountLayout = new HorizontalLayout();
        Text machineCountLabel = new Text("Nombre de machines: ");
        Text machineCountValue = new Text(String.valueOf(service.countMachines()));
        machineCountLayout.add(machineCountLabel, machineCountValue);

        HorizontalLayout commandeCountLayout = new HorizontalLayout();
        Text commandeCountLabel = new Text("Nombre de commandes: ");
        Text commandeCountValue = new Text(String.valueOf(service.countCommandes()));
        commandeCountLayout.add(commandeCountLabel, commandeCountValue);

        HorizontalLayout produitCountLayout = new HorizontalLayout();
        Text produitCountLabel = new Text("Nombre de produits: ");
        Text produitCountValue = new Text(String.valueOf(service.countProduits()));
        produitCountLayout.add(produitCountLabel, produitCountValue);

        // Ajout des HorizontalLayouts au dataInfoContentLayout
        VerticalLayout dataInfoContentLayout = new VerticalLayout();
        dataInfoContentLayout.setPadding(false);
        dataInfoContentLayout.setSpacing(false);

        H1 MoreInfoTitleLabel = new H1("Informations sur les machines");
        MoreInfoTitleLabel.getStyle().set("margin-top", "20px");
        MoreInfoTitleLabel.getStyle().set("margin-bottom", "10px");
        MoreInfoTitleLabel.getElement().getStyle().set("font-size", "20px");

        Button infoButton = new Button("Voir plus de détails");
        infoButton.getElement().getStyle().set("margin", "auto");
        infoButton.getElement().getStyle().set("margin-top", "20px");

        // Ajoute les composants au layout de datas
        dataInfoContentLayout.add(operateurCountLayout, machineCountLayout, commandeCountLayout, produitCountLayout);
        upperLayout.add(dataInfoTitleLabel, dataInfoContentLayout);
        bottomLayout.add(MoreInfoTitleLabel, infoButton);

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


}
