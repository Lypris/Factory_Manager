package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.*;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "production", layout = MainLayout.class)
@PageTitle("Production | M3 Application")
public class ProductionView extends VerticalLayout {
    Grid<Commande> grid = new Grid<>(Commande.class);
    CrmService service;


    public ProductionView(CrmService service) {
        this.service = service;
        addClassName("production-view");
        setSizeFull();
        configureGrid();
        add(getContent());
        updateList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    private void updateList() {
        List<Commande> commandes = service.findAllCommandeEnAttente();
        commandes.addAll(service.findAllCommandeEnCours());
        grid.setItems(commandes);
    }
    private void configureGrid() {
        grid.addClassNames("production-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Commande::getRef)
                .setHeader("Référence").setSortable(true);
        grid.addColumn(Commande::getDes)
                .setHeader("Description").setSortable(true);
        grid.addColumn(Commande -> {
                    LocalDateTime debut = Commande.getDebut();
                    return (debut != null) ? debut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
                })
                .setHeader("Date de début").setSortable(true);
        grid.addColumn(Commande -> {
                    LocalDateTime fin = Commande.getFin();
                    return (fin != null) ? fin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
                })
                .setHeader("Date de fin").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(commande -> {
                    VaadinIcon icon = IconUtils.determineIconCommande(commande.getStatut());
                    Span badge = new Span(IconUtils.createIcon(icon),
                            new Span(commande.getStatut()));
                    IconUtils.applyStyleForStatutCommande(badge, commande.getStatut());
                    return badge;
                }))
                .setHeader("Statut").setSortable(true);
        grid.setItemDetailsRenderer(createCommandeDetailsRenderer());

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private ComponentRenderer<ProductionView.CommandeDetailsLayout, Commande> createCommandeDetailsRenderer() {
        return new ComponentRenderer<>(commande -> {
            ProductionView.CommandeDetailsLayout details = new ProductionView.CommandeDetailsLayout(service, commande);
            details.addClassName("commande-details");
            return details;
        });
    }

    public class CommandeDetailsLayout extends HorizontalLayout {
        private Grid<DefinitionCommande> grid = new Grid<>(DefinitionCommande.class);
        private Commande commande;
        private CrmService service;
        private Button launch = new Button("Lancer la production");


        public CommandeDetailsLayout(CrmService service, Commande commande) {

            this.service = service;
            this.commande = commande;
            addClassName("commande-details");
            setSizeFull();
            configureGridDetails();
            setCommande(service, commande);
            configureLayoutDetails();
            if(commande.getStatut().equals("En cours")){
                launch.setText("Gérer la production");
                launch.setIcon(new com.vaadin.flow.component.icon.Icon(VaadinIcon.TOOLS));
            }
            
        }
        public void configureLayoutDetails(){
            H1 text = new H1(" ");
            this.setWidthFull();
            this.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            this.add(launch);
            this.expand(text);
            this.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);
            launch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            launch.setIcon(new com.vaadin.flow.component.icon.Icon(VaadinIcon.PLAY_CIRCLE));
            launch.addClickListener(e -> {
                //on redirige vers 2 pages différentes selon le statut de la commande
                if(commande.getStatut().equals("En attente")){
                    //on redirige vers la page de lancement de la production
                    getUI().ifPresent(ui -> ui.navigate("lancerproduction/" + commande.getId()));
                    /*
                commande.setStatut("En cours");
                commande.setDebut(LocalDateTime.now());
                service.saveCommande(commande);
                updateList();
                 */
                }
                else if(commande.getStatut().equals("En cours")){
                    getUI().ifPresent(ui -> ui.navigate("GestionProduction/" + commande.getId()));
                }
            });

        }
        public void configureGridDetails(){
            grid.removeAllColumns();
            grid.addColumn(DefinitionCommande->
                    DefinitionCommande.getProduit().getDes());
            grid.addColumn(DefinitionCommande->
                    custominfo(commande, DefinitionCommande.getProduit()));
            grid.getColumns().forEach(col -> {
                col.setAutoWidth(true);
            });
            grid.addClassName("my-grid");
            add(grid);
        }
        private String custominfo(Commande commande, Produit produit) {
            //méthode qui permet d'afficher le nombre d'exemplaires finis pour un produit et une commande donnés
            // par rapport au nombre d'exemplaires commandés
            int NbrExemplaires = service.findAllProdFiniByProduitAndCommande(produit, commande).size();
            int NbrExemplairesCommandes = service.getDefinitionByProduitAndCommandeUnique(produit, commande).getNbr();
            return NbrExemplaires + "/" + NbrExemplairesCommandes +" Exemplaires produits";
        }

        public void setCommande(CrmService service, Commande commande) {
            grid.setItems(service.findAllDefinitionCommandeByCommande(commande));
        }
    }
}