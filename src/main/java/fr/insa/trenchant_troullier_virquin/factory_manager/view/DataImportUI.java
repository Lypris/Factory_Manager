package fr.insa.trenchant_troullier_virquin.factory_manager.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.DataGenerator;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.service.CrmService;

@Route(value = "data-import", layout = MainLayout.class)
public class DataImportUI extends VerticalLayout {
    private DataGenerator dataGenerator;

    public DataImportUI(CrmService service) {
        dataGenerator = new DataGenerator(service);

        Button importButton = new Button("Importer les données");
        importButton.addClickListener(event -> {
            String filePath = "src/main/resources/operateur.txt"; // Remplace par le chemin de ton fichier
            dataGenerator.generateDataFromFile(filePath);
            String filePath2 = "src/main/resources/type_operation.txt";
            dataGenerator.generateTypeOperationFromFile(filePath2);
            String filePath3 = "src/main/resources/machine.txt";
            dataGenerator.generateMachineFromFile(filePath3);
            importButton.setEnabled(false);
        });

        add(importButton);
    }

}

