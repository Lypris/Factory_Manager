package fr.insa.trenchant_troullier_virquin.factory_manager.data.entity;

import jakarta.validation.constraints.NotEmpty;

public class Poste extends AbstractEntity {
    @NotEmpty
    private String reference = "";

    @NotEmpty
    private String description = "";

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
