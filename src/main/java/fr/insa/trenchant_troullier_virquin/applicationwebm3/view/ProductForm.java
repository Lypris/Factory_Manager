package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.TypeOperation;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.List;


public class ProductForm extends FormLayout {
    TextField ref = new TextField("Référence");
    TextField des = new TextField("Description");
    TextField prix = new TextField("Prix");
    Image produitImage = new Image();

    Button save = new Button("Enregistrer");
    Button defO = new Button("Définir les opérations");

    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");
    UploadHelper upload = new UploadHelper();

    BeanValidationBinder<Produit> binder = new BeanValidationBinder<>(Produit.class);

    public ProductForm(List<TypeOperation> typeoperations) {
        binder.bindInstanceFields(this);
        addClassName("product-form");

        add(ref,
                des,
                prix,
                defO,
                upload,
                produitImage,
                createButtonsLayout());
        defO.addClickListener(e -> {
            DialogDefOpp dialogO = new DialogDefOpp(typeoperations);
            dialogO.open();
            ConfigurDialog(dialogO);
        });


    }
    public void ConfigurDialog(Dialog dialog){
        dialog.add();
    }
    public void setProduit(Produit produit) {

        binder.setBean(produit);
        //TODO : Afficher l'image redimensionnée
        produitImage= createProductImageRenderer(produit);

        //upload.reset();
    }
    public Image createProductImageRenderer(Produit produit) {
        Image image = new Image();
        if (produit!=null && produit.getImage() != null) {
            StreamResource resource = new StreamResource("image.png", () -> new ByteArrayInputStream(produit.getImage()));
            image.setSrc(resource);
            return image;
        } else {
            return image;
        }
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
        if (binder.isValid()) {
            Produit produit = binder.getBean();
            byte[] imageData = upload.getImageData();
            produit.setImage(imageData);
            fireEvent(new ProductForm.SaveEvent(this, produit));
        }
    }

}
