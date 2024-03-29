package fr.insa.trenchant_troullier_virquin.factory_manager.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.TypeOperation;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.service.CrmService;

import java.io.ByteArrayInputStream;
import java.util.List;


public class ProductForm extends FormLayout {
    TextField ref = new TextField("Référence");
    TextField des = new TextField("Description");
    TextField prix = new TextField("Prix");
    Image produitImage = new Image();
    Button save = new Button("Enregistrer");
    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");
    UploadHelper upload = new UploadHelper();
    CrmService service;
    BeanValidationBinder<Produit> binder = new BeanValidationBinder<>(Produit.class);
    private byte[] originalImageData;

    public ProductForm(List<TypeOperation> typeoperations, CrmService service) {
        this.service = service;
        binder.bindInstanceFields(this);
        addClassName("product-form");
        ref.setValueChangeMode(ValueChangeMode.LAZY);
        des.setValueChangeMode(ValueChangeMode.LAZY);
        prix.setValueChangeMode(ValueChangeMode.LAZY);
        add(ref,
                des,
                prix,
                upload,
                produitImage,
                createButtonsLayout());
        upload.setImageUploadListener(this::updateImage);
    }

    public void setProduit(Produit produit) {
        resetUploadAndImage(); // Réinitialiser l'état avant de définir un nouveau produit
        binder.setBean(produit);
        if (produit != null && produit.getImage() != null) {
            originalImageData = produit.getImage();
            updateImage(produit.getImage());
        }
        if (binder.getBean() != null && binder.getBean().getRef().isEmpty()){
            delete.setEnabled(false);
        }else{
            this.delete.setEnabled(true);
        }
    }

    public void resetUploadAndImage() {
        upload.resetUpload(); // Réinitialise l'état de l'upload
        produitImage.setSrc(""); // Réinitialise l'image affichée
        originalImageData = null;
    }

    private void updateImage(byte[] imageData) {
        if (imageData != null && imageData.length > 0) {
            StreamResource resource = new StreamResource("productImage", () -> new ByteArrayInputStream(imageData));
            produitImage.setSrc(resource);
            //on centre l'image
            produitImage.getStyle().set("margin-left", "auto");
            produitImage.getStyle().set("margin-right", "auto");
            //on redimensionne l'image en CSS en fonction de la largeur de l'écran
            produitImage.getStyle().set("max-width", "70%");
        } else {
            // Restaurer l'image originale si disponible
            if (originalImageData != null) {
                StreamResource resource = new StreamResource("originalImage", () -> new ByteArrayInputStream(originalImageData));
                produitImage.setSrc(resource);
            } else {
                produitImage.setSrc(""); // Pas d'image originale, définir une image par défaut
            }
        }
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> {
            boolean isProductUsedInCommand = service.isProductUsedInCommand(binder.getBean());
            if (isProductUsedInCommand) {
                Notification notification = new Notification("Le produit est utilisé dans une ou plusieurs commandes.");
                notification.setDuration(3000);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            } else {
                fireEvent(new ProductForm.DeleteEvent(this, binder.getBean()));
            }
        });
        close.addClickListener(event -> fireEvent(new ProductForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
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
            // Vérifie si une nouvelle image a été téléversée et n'a pas été supprimée
            if (upload.isImageUploaded()) {
                produit.setImage(upload.getImageData());
            } else if (originalImageData != null) {
                produit.setImage(originalImageData); // Restaurer l'image originale
            }
            fireEvent(new ProductForm.SaveEvent(this, produit));
        }
    }

    //Gestion des évènements
    public static abstract class ProductFormEvent extends ComponentEvent<ProductForm> {
        private final Produit produit;

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
}
