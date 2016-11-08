package com.andreassavva.waterreminder;

public class CupSizeChooserItem {

    private String cupSizeText;
    private int cupSizeIcon;

    public CupSizeChooserItem(String cupSizeText, int cupSizeIcon) {
        this.cupSizeText = cupSizeText;
        this.cupSizeIcon = cupSizeIcon;
    }

    public String getCupSizeText() {
        return cupSizeText;
    }

    public int getCupSizeIcon() {
        return cupSizeIcon;
    }
}
