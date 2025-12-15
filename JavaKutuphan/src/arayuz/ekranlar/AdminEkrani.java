package arayuz.ekranlar;

import arayuz.paneller.*;
import modeller.Kullanici;

import javax.swing.*;
import java.awt.*;

public class AdminEkrani extends TemelEkran {
    private Kullanici aktifYonetici;
    public AdminEkrani(Kullanici kullanici) {
        super("YÖNETİCİ PANELİ");
        this.aktifYonetici = kullanici;
        arayuzuOlustur();
    }
    @Override
    protected void arayuzuOlustur() {
        setLayout(new BorderLayout());
        add(cikisPaneliniGetir(), BorderLayout.NORTH);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Kitap Yönetimi", new KitapYonetimPaneli(true, 0));
        tabbedPane.addTab("Kullanıcı Yönetimi", new KullaniciYonetimPaneli(true));
        tabbedPane.addTab("Ödünç / İade", new OduncIslemPaneli());
        tabbedPane.addTab("Sistem Ayarları", new SistemAyarlariPaneli());
        tabbedPane.addTab("Profilim", new ProfilPaneli(aktifYonetici));
        add(tabbedPane, BorderLayout.CENTER);
    }
}