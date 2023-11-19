/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H3;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Intial | Appli TP Info")
public class InitialView extends VerticalLayout {

    public InitialView() {
        this.add(new H3("intial"));
    }
}
