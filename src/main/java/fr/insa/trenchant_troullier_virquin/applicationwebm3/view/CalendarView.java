package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.StatutOperateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;
import org.vaadin.stefan.fullcalendar.*;

import java.time.LocalDate;
import java.util.List;

@Route(value = "test", layout = MainLayout.class)
@PageTitle("Calendrier des Opérateurs")
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
