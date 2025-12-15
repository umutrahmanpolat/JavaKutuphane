package tasarimdesenleri.state;

import modeller.Kitap;

public interface KitapState {
    boolean oduncVerilebilirMi();
    String durumAdi();
    void durumGuncelle(Kitap kitap);
}