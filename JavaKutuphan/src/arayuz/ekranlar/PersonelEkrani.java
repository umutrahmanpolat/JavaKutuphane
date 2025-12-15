package arayuz.ekranlar;

import arayuz.paneller.KitapYonetimPaneli;
import arayuz.paneller.KullaniciYonetimPaneli;
import arayuz.paneller.OduncIslemPaneli;
import arayuz.paneller.ProfilPaneli;
import modeller.Kullanici;

import javax.swing.*;
import java.awt.*;

public class PersonelEkrani extends TemelEkran {
    private Kullanici aktifPersonel;
    public PersonelEkrani(Kullanici kullanici) {
        super("PERSONEL PANELİ");
        this.aktifPersonel = kullanici;
        arayuzuOlustur();
    }
    @Override
    protected void arayuzuOlustur() {
        setLayout(new BorderLayout());
        add(cikisPaneliniGetir(), BorderLayout.NORTH);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Kitap Yönetimi", new KitapYonetimPaneli(true, 0));
        tabbedPane.addTab("Üye Yönetimi", new KullaniciYonetimPaneli(false));
        tabbedPane.addTab("Ödünç / İade", new OduncIslemPaneli());
        tabbedPane.addTab("Profilim", new ProfilPaneli(aktifPersonel));
        add(tabbedPane, BorderLayout.CENTER);
    }
}