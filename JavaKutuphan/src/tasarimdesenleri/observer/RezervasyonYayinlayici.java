package tasarimdesenleri.observer;

import modeller.Uye;

public interface RezervasyonYayinlayici {
    void gozlemciEkle(Uye uye);
    void gozlemciKaldir(Uye uye);
    void gozlemcileriBilgilendir(int kitapId);
}