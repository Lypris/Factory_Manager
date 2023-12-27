package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.StatutOperateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Statut;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operateur;

import java.util.List;

public class StatutForm extends FormLayout {
    ComboBox<Operateur> operateurComboBox = new ComboBox<>("Opérateur");
    ComboBox<Statut> statutComboBox = new ComboBox<>("Statut");
    DateTimePicker debut = new DateTimePicker("Début");
    DateTimePicker fin = new DateTimePicker("Fin");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    BeanValidationBinder<StatutOperateur> binder = new BeanValidationBinder<>(StatutOperateur.class);

    public StatutForm(List<Operateur> operateurs,List<Statut> statuts) {
        binder.bindInstanceFields(this);
        addClassName("Statut-form");
        operateurComboBox.setItems(operateurs);
        operateurComboBox.setItemLabelGenerator(operateur -> operateur.getNom() + " " + operateur.getPrenom());
        statutComboBox.setItems(statuts);
        statutComboBox.setItemLabelGenerator(Statut::getName);

        add(operateurComboBox,
                debut,
                fin,
                statutComboBox,
                createButtonsLayout());
    }
    public void setStatut(StatutOperateur statutOperateur) {
        binder.setBean(statutOperateur);
        if (statutOperateur != null){
            // Récupérer l'opérateur du statutOperateur et le sélectionner dans le ComboBox
            Operateur operateur = statutOperateur.getOperateur();
            if (operateur != null) {
                operateurComboBox.setValue(operateur);
            }

            // Récupérer le statut du statutOperateur et le sélectionner dans le ComboBox
            Statut statut = statutOperateur.getStatut();
            if (statut != null) {
                statutComboBox.setValue(statut);
            }
        }

    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }


    // Events
    public static abstract class StatutFormEvent extends ComponentEvent<StatutForm> {
        private StatutOperateur statutOperateur;

        protected StatutFormEvent(StatutForm source, StatutOperateur statutOperateur) {
            super(source, false);
            this.statutOperateur = statutOperateur;
        }

        public StatutOperateur getStatutOperateur() {
            return statutOperateur;
        }
    }

    public static class SaveEvent extends StatutFormEvent {
        SaveEvent(StatutForm source, StatutOperateur statutOperateur) {
            super(source, statutOperateur);
        }
    }

    public static class DeleteEvent extends StatutFormEvent {
        DeleteEvent(StatutForm source, StatutOperateur statutOperateur) {
            super(source, statutOperateur);
        }

    }

    public static class CloseEvent extends StatutFormEvent {
        CloseEvent(StatutForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            StatutOperateur statutOperateur = binder.getBean();

            // Récupérer l'opérateur sélectionné dans le ComboBox
            Operateur selectedOperateur = operateurComboBox.getValue();
            if (selectedOperateur != null) {
                statutOperateur.setOperateur(selectedOperateur);
            } else {
                // TODO: Gérer le cas où aucun opérateur n'est sélectionné
            }

            // Récupérer le statut sélectionné dans le ComboBox
            Statut selectedStatut = statutComboBox.getValue();
            if (selectedStatut != null) {
                statutOperateur.setStatut(selectedStatut);
            } else {
                // TODO : Gérer le cas où aucun statut n'est sélectionné
            }

            fireEvent(new SaveEvent(this, statutOperateur));
        }
    }
}