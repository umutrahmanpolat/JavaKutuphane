package tasarimdesenleri.state;

import modeller.Kitap;

public class RaftaState implements KitapState {
    @Override
    public boolean oduncVerilebilirMi() { return true; }
    @Override
    public String durumAdi() { return "Rafta"; }
    @Override
    public void durumGuncelle(Kitap kitap) {
        if (kitap.getStokAdedi() <= 0) {
            kitap.setDurum(new OduncteState());
        }
    }
}