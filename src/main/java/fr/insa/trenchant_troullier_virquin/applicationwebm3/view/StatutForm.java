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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Statut;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.StatutOperateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class StatutForm extends FormLayout {
    ComboBox<Operateur> operateurComboBox = new ComboBox<>("Opérateur");
    ComboBox<Statut> statutComboBox = new ComboBox<>("Statut");
    DateTimePicker debut = new DateTimePicker("Début");
    DateTimePicker fin = new DateTimePicker("Fin");

    Button save = new Button("Enregistrer");
    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");
    BeanValidationBinder<StatutOperateur> binder = new BeanValidationBinder<>(StatutOperateur.class);
    CrmService service;

    public StatutForm(List<Operateur> operateurs, List<Statut> statuts, CrmService service) {
        binder.bindInstanceFields(this);
        this.service = service;
        addClassName("Statut-form");
        operateurComboBox.setItems(operateurs);
        operateurComboBox.setItemLabelGenerator(operateur -> operateur.getNom() + " " + operateur.getPrenom());
        operateurComboBox.addValueChangeListener(e -> configureDateTimePickers(e.getValue()));

        statutComboBox.setItems(statuts);
        statutComboBox.setItemLabelGenerator(Statut::getName);

        add(operateurComboBox,
                debut,
                fin,
                statutComboBox,
                createButtonsLayout());
    }

    private void configureDateTimePickers(Operateur operateur) {
        List<StatutOperateur> statutOperateurs = service.findAllStatutOperateurByOperateur(operateur);
        if (statutOperateurs != null && !statutOperateurs.isEmpty()) {
            // Trouver la date de fin la plus récente parmi tous les statuts de l'opérateur
            LocalDateTime latestEndDate = statutOperateurs.stream()
                    .map(StatutOperateur::getFin)
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            if (latestEndDate != null) {
                debut.setValue(latestEndDate);
                debut.setReadOnly(true); // Désactiver la modification de la date de début
            } else {
                debut.setReadOnly(false); // Activer la modification si aucun statut précédent ou si le dernier statut est indéterminé
                debut.clear();
            }
        }
    }

    public void setStatut(StatutOperateur statutOperateur) {
        binder.setBean(statutOperateur);
        if (statutOperateur != null) {
            // Récupérer l'opérateur du statutOperateur et le sélectionner dans le ComboBox
            Operateur operateur = statutOperateur.getOperateur();
            if (operateur != null) {
                operateurComboBox.setValue(operateur);
            } else {
                operateurComboBox.setValue(null);
            }
            // Récupérer le statut du statutOperateur et le sélectionner dans le ComboBox
            Statut statut = statutOperateur.getStatut();
            if (statut != null) {
                statutComboBox.setValue(statut);
            } else {
                statutComboBox.setValue(null);
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
        if (binder.isValid()) {
            StatutOperateur statutOperateur = binder.getBean();
            LocalDateTime debutValue = debut.getValue();
            LocalDateTime finValue = fin.getValue();
            Operateur selectedOperateur = operateurComboBox.getValue();
            Statut selectedStatutOperateur = statutComboBox.getValue();
            if (selectedOperateur == null) {
                Notification.show("Veuillez sélectionner un opérateur", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (selectedStatutOperateur == null) {
                Notification.show("Veuillez sélectionner un état à appliquer", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (debutValue == null) {
                Notification.show("Veuillez saisir une date de début", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (finValue != null && debutValue.isAfter(finValue)) {
                Notification.show("Veuillez saisir une date de début antérieure à la date de fin", 3000, Notification.Position.MIDDLE);
                return;
            }
            if (service.findAllStatutOperateurByOperateur(selectedOperateur) != null) {
                for (StatutOperateur etatOperateur1 : service.findAllStatutOperateurByOperateur(selectedOperateur)) {
                    LocalDateTime datefin = etatOperateur1.getFin();
                    LocalDateTime datedebut = etatOperateur1.getDebut();
                    if (datefin!=null &&  datedebut.isBefore(debutValue) && datefin.isAfter(debutValue)) {
                        Notification.show("L'opérateur a déjà un état pour cette période", 3000, Notification.Position.MIDDLE);
                        return;
                    }
                    if (datefin!=null && finValue != null && datedebut.isBefore(finValue) && datefin.isAfter(finValue)) {
                        Notification.show("L'opérateur a déjà un état pour cette période", 3000, Notification.Position.MIDDLE);
                        return;
                    }
                    if (datefin!=null && finValue != null && datedebut.isAfter(debutValue) && datefin.isBefore(finValue)) {
                        Notification.show("L'opérateur a déjà un état pour cette période", 3000, Notification.Position.MIDDLE);
                        return;
                    }
                }
            }
            statutOperateur.setOperateur(selectedOperateur);
            statutOperateur.setStatut(selectedStatutOperateur);
            statutOperateur.setDebut(debutValue);
            statutOperateur.setFin(finValue);
            fireEvent(new SaveEvent(this, statutOperateur));
        }
    }

    // Events
    public static abstract class StatutFormEvent extends ComponentEvent<StatutForm> {
        private final StatutOperateur statutOperateur;

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

}