package fr.insa.trenchant_troullier_virquin.factory_manager.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.StatutOperateur;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.service.CrmService;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import java.util.List;

@Route(value = "test", layout = MainLayout.class)
@PageTitle("Calendrier des Opérateurs | Factory Manager")
public class CalendarView extends VerticalLayout {

    private final FullCalendar calendar;
    CrmService service;

    public CalendarView(CrmService service) {
        this.service = service;
        setSizeFull();
        calendar = FullCalendarBuilder.create().build();

        addEventToCalendar();
        this.add(calendar);
        this.setFlexGrow(1, calendar);
    }

    private void addEventToCalendar() {
        List<StatutOperateur> statuts = service.findAllStatutOperateurs(null);
        for (StatutOperateur statut : statuts) {
            Entry entry = new Entry();
            entry.setTitle("Statut: " + statut.getStatut().getName() + " - Opérateur: " + statut.getOperateur().getNom());
            entry.setColor("#ff3333");
            entry.setStart(statut.getDebut());
            entry.setEnd(statut.getFin());
            //TODO associer une couleur à chaque statut
            calendar.getEntryProvider().asInMemory().addEntries(entry);
        }
    }
}
