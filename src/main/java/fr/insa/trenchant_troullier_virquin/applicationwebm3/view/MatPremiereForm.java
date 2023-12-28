/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.MatPremiere;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

/**
 *
 * @author laelt
 */
public class MatPremiereForm extends FormLayout {
    TextField ref = new TextField("Référence");
    TextField des = new TextField("Description");
    
    Button save = new Button("Enregistrer");

    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");
    //UploadHelper upload = new UploadHelper();
    CrmService service;

    BeanValidationBinder<MatPremiere> binder = new BeanValidationBinder<>(MatPremiere.class);
    
    public MatPremiereForm(CrmService service) {
        binder.bindInstanceFields(this);
        addClassName("MatPremiere-form");

        add(ref,
                des,
                createButtonsLayout());
    }
    
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new MatPremiereForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new MatPremiereForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    
    }
    
    public void setMatPremiere(MatPremiere matPre) {
        binder.setBean(matPre);
    }
    private void validateAndSave() {
        if (binder.isValid()) {
            MatPremiere matPre = binder.getBean();
            fireEvent(new MatPremiereForm.SaveEvent(this, matPre));
        }
    }
    
    public static class SaveEvent extends MatPremiereFormEvent {
        SaveEvent(MatPremiereForm source, MatPremiere matPre) {
            super(source, matPre);
        }
    }
    public static class DeleteEvent extends MatPremiereFormEvent {
        DeleteEvent(MatPremiereForm source, MatPremiere matPre) {
            super(source, matPre);
        }
    }
    public static class CloseEvent extends MatPremiereFormEvent {
        CloseEvent(MatPremiereForm source) {
            super(source, null);
        }
    }
    
    //Gestion Evenement
    public static abstract class MatPremiereFormEvent extends ComponentEvent<MatPremiereForm> {
        private MatPremiere matPre;

        protected MatPremiereFormEvent(MatPremiereForm source, MatPremiere matPre) {
            super(source, false);
            this.matPre = matPre;
        }

        public MatPremiere getMatPremiere() {
            return matPre;
        }
    }
    
    public Registration addSaveListener(ComponentEventListener<MatPremiereForm.SaveEvent> listener) {
        return addListener(MatPremiereForm.SaveEvent.class, listener);
    }
    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(MatPremiereForm.DeleteEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(MatPremiereForm.CloseEvent.class, listener);
    }
}
