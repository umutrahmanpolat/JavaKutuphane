package tasarimdesenleri.state;

import modeller.Kitap;

public class OduncteState implements KitapState {
    @Override
    public boolean oduncVerilebilirMi() { return false; }
    @Override
    public String durumAdi() { return "Tükendi / Ödünçte"; }
    @Override
    public void durumGuncelle(Kitap kitap) {
        if (kitap.getStokAdedi() > 0) {
            kitap.setDurum(new RaftaState());
        }
    }
}