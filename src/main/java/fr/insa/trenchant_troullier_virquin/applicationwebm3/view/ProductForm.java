package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;

public class ProductForm extends FormLayout {
    TextField ref = new TextField("Référence");
    TextField des = new TextField("Description");
    TextField prix = new TextField("Prix");

    Button save = new Button("Enregistrer");
    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");

    //TODO : Zone pour upload une image, à voir si on peut le faire
    BeanValidationBinder<Produit> binder = new BeanValidationBinder<>(Produit.class);

    public ProductForm() {
        binder.bindInstanceFields(this);
        addClassName("product-form");

        add(ref,
                des,
                prix,
                createButtonsLayout());
    }
    public void setProduit(Produit produit) {
        binder.setBean(produit);
    }
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new ProductForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new ProductForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    //Gestion des évènements
    public static abstract class ProductFormEvent extends ComponentEvent<ProductForm> {
        private Produit produit;

        protected ProductFormEvent(ProductForm source, Produit produit) {
            super(source, false);
            this.produit = produit;
        }

        public Produit getProduit() {
            return produit;
        }
    }
    public static class SaveEvent extends ProductFormEvent {
        SaveEvent(ProductForm source, Produit produit) {
            super(source, produit);
        }
    }
    public static class DeleteEvent extends ProductFormEvent {
        DeleteEvent(ProductForm source, Produit produit) {
            super(source, produit);
        }
    }
    public static class CloseEvent extends ProductFormEvent {
        CloseEvent(ProductForm source) {
            super(source, null);
        }
    }
    public Registration addSaveListener(ComponentEventListener<ProductForm.SaveEvent> listener) {
        return addListener(ProductForm.SaveEvent.class, listener);
    }
    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(ProductForm.DeleteEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(ProductForm.CloseEvent.class, listener);
    }
    private void validateAndSave() {
        binder.validate().isOk();
        fireEvent(new ProductForm.SaveEvent(this, binder.getBean()));
    }

}
