package com.radiantdelta.bankapp.dtos;

import jakarta.validation.constraints.NotEmpty;

public class ChangeDTO {
    @NotEmpty(message = "Change newVal cannot be empty")
    private String newVal;
    @NotEmpty(message = "Change oldVal cannot be empty")
    private String oldVal;

    public String getNewVal() {
        return newVal;
    }

    public String getOldVal() {
        return oldVal;
    }

    public void setNewVal(String newVal) {
        this.newVal = newVal;
    }

    public void setOldVal(String oldVal) {
        this.oldVal = oldVal;
    }
}
