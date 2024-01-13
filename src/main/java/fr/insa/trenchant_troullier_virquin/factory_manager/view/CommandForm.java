package fr.insa.trenchant_troullier_virquin.factory_manager.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.service.CrmService;

import java.util.List;

public class CommandForm extends FormLayout {

    static TextField ref = new TextField("Référence");
    static TextField des = new TextField("Description");
    Button addProduct = new Button("Définir les produits");

    Button save = new Button("Enregistrer");
    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");
    CrmService service;
    BeanValidationBinder<Commande> binder = new BeanValidationBinder<>(Commande.class);

    public CommandForm(List<Produit> produits, CrmService service) {
        this.service = service;
        this.ref.setValueChangeMode(ValueChangeMode.LAZY);
        this.des.setValueChangeMode(ValueChangeMode.LAZY);
        binder.bindInstanceFields(this);
        addClassName("Commande-form");
        add(ref, des, addProduct,
                createButtonsLayout());
        addProduct.addClickListener(e -> {
            DialogDefCommande dialogAddProduct = new DialogDefCommande(binder.getBean(), produits, service);
            dialogAddProduct.open();
            configureDialog(dialogAddProduct);
        });
    }


    public void setCommande(Commande commande) {
        binder.setBean(commande);
        // Vérifier le statut de la commande et cacher le bouton si nécessaire
        addProduct.setVisible(commande == null || (!"En cours".equalsIgnoreCase(commande.getStatut()) && !"Terminée".equalsIgnoreCase(commande.getStatut())));
        // renommer le bouton en "Modifier les produits" si la commande a déjà des produits
        if (commande != null && !service.findAllProduitByCommande(commande).isEmpty()) {
            addProduct.setText("Modifier les produits");
            addProduct.setIcon(VaadinIcon.EDIT.create());
        } else {
            addProduct.setText("Définir les produits");
            addProduct.setIcon(VaadinIcon.PLUS.create());
        }
        if (binder.getBean() != null && binder.getBean().getRef().isEmpty()){
            delete.setEnabled(false);
        }else{
            this.delete.setEnabled(true);
        }
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new CommandForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> {
            fireEvent(new CommandForm.CloseEvent(this));
        });
        
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid() && champTextPlein()));
        return new HorizontalLayout(save, delete, close);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(CommandForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<CommandForm.SaveEvent> listener) {
        return addListener(CommandForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CommandForm.CloseEvent> listener) {
        return addListener(CommandForm.CloseEvent.class, listener);
    }

    private void configureDialog(DialogDefCommande dialogAddProduct) {
        dialogAddProduct.setSizeFull();
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            Commande commande = binder.getBean();
            commande.setStatut("En attente");
            fireEvent(new CommandForm.SaveEvent(this, commande));
        }
    }

    public void suppCommande() {

        fireEvent(new CommandForm.DeleteEvent(this, binder.getBean()));
    }

    private boolean champTextPlein() {
        if (!this.ref.isEmpty() && !this.des.isEmpty()){
            return(true);
        }else{
            return(false);
        }    
    }

    // Events
    public static abstract class CommandFormEvent extends ComponentEvent<CommandForm> {
        private final Commande commande;

        protected CommandFormEvent(CommandForm source, Commande commande) {
            super(source, false);
            this.commande = commande;
        }

        public Commande getCommande() {
            return commande;
        }
    }

    public static class SaveEvent extends CommandForm.CommandFormEvent {
        SaveEvent(CommandForm source, Commande commande) {
            super(source, commande);
        }
    }

    public static class DeleteEvent extends CommandForm.CommandFormEvent {
        DeleteEvent(CommandForm source, Commande commande) {
            super(source, commande);
        }

    }

    public static class CloseEvent extends CommandForm.CommandFormEvent {
        CloseEvent(CommandForm source) {
            super(source, null);
        }
    }

}
