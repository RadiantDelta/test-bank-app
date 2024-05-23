package com.m1guelsb.springauth.dtos;

public class ChangeDTO {
    private String newVal;
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
