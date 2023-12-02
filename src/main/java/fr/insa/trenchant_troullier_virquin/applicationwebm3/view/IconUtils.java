package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class IconUtils {

    public static VaadinIcon determineIcon(String description) {
        // Exemple : associer différentes descriptions à différentes icônes
        switch (description.toLowerCase()) {
            case "éteinte":
                return VaadinIcon.CLOCK;
            case "en marche":
                return VaadinIcon.CHECK;
            case "en panne":
                return VaadinIcon.WARNING;
            // Ajoutez d'autres cas selon vos besoins
            default:
                return VaadinIcon.QUESTION;
        }
    }
    public static void applyStyleForEtat(Span element, String etat) {
        switch (etat) {
            case "éteinte":
                element.getElement().getThemeList().add("badge");
                break;
            case "en marche":
                element.getElement().getThemeList().add("badge success");
                break;
            case "en panne":
                element.getElement().getThemeList().add("badge error");
                break;
            default:
                // Style par défaut si l'état n'est pas reconnu
                element.getElement().getThemeList().add("badge contrast");
                break;
        }
    }
    public static VaadinIcon determineIconOperateur(String description) {
        // Exemple : associer différentes descriptions à différentes icônes
        switch (description.toLowerCase()) {
            case "en congé":
                return VaadinIcon.CLOCK;
            case "présent":
                return VaadinIcon.CHECK;
            case "absent":
                return VaadinIcon.WARNING;
            // Ajoutez d'autres cas selon vos besoins
            default:
                return VaadinIcon.QUESTION;
        }
    }
    public static void applyStyleForStatut(Span element, String name) {
        switch (name) {
            case "en congé":
                element.getElement().getThemeList().add("badge");
                break;
            case "présent":
                element.getElement().getThemeList().add("badge success");
                break;
            case "absent":
                element.getElement().getThemeList().add("badge error");
                break;
            default:
                // Style par défaut si l'état n'est pas reconnu
                element.getElement().getThemeList().add("badge contrast");
                break;
        }
    }
    public static Icon createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }
}
