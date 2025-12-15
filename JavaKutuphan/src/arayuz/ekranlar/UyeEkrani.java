package arayuz.ekranlar;

import arayuz.paneller.KitapYonetimPaneli;
import arayuz.paneller.ProfilPaneli;
import modeller.Kullanici;
import veritabani.KutuphaneIslemleri;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class UyeEkrani extends TemelEkran {
    private Kullanici aktifKullanici;
    private KutuphaneIslemleri islemler = new KutuphaneIslemleri();
    private DefaultTableModel oduncModel;
    private DefaultTableModel bildirimModel;

    public UyeEkrani(Kullanici kullanici) {
        super("HOŞGELDİNİZ " + kullanici.getAd().toUpperCase());
        this.aktifKullanici = kullanici;
        arayuzuOlustur();
    }

    @Override
    protected void arayuzuOlustur() {
        setLayout(new BorderLayout());
        add(cikisPaneliniGetir(), BorderLayout.NORTH);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Kitap Arama & Rezervasyon", new KitapYonetimPaneli(false, aktifKullanici.getId()));
        tabbedPane.addTab("Ödünçlerim", createOduncPanel());
        tabbedPane.addTab("Bildirimler", createBildirimPanel());
        tabbedPane.addTab("Profilim", new ProfilPaneli(aktifKullanici));
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createOduncPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] colNames = {"Kitap", "Alış", "Son Teslim", "Durum", "Ceza"};
        oduncModel = new DefaultTableModel(colNames, 0);
        JTable table = new JTable(oduncModel);
        JButton btnYenile = new JButton("Yenile");
        btnYenile.addActionListener(e -> oduncleriYukle());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnYenile, BorderLayout.SOUTH);
        oduncleriYukle();
        return panel;
    }

    private void oduncleriYukle() {
        oduncModel.setRowCount(0);
        ResultSet rs = islemler.uyeOduncListesiGetir(aktifKullanici.getId());
        double gunlukCeza = islemler.ayarGetir("gunluk_ceza_tl");
        double baslangicCeza = islemler.ayarGetir("baslangic_cezasi_tl");

        try {
            while (rs != null && rs.next()) {
                java.sql.Date teslim = rs.getDate("son_teslim_tarihi");
                java.sql.Date iade = rs.getDate("iade_tarihi");
                String durum; double gosterilecekCeza = 0.0;

                if (iade != null) {
                    durum = "İade Edildi (" + iade + ")";
                    gosterilecekCeza = rs.getDouble("ceza_tutari");
                } else {
                    long gecikme = ChronoUnit.DAYS.between(teslim.toLocalDate(), LocalDate.now());
                    if (gecikme > 0) {
                        durum = "GECİKMİŞ (" + gecikme + " Gün)";
                        gosterilecekCeza = baslangicCeza + (gecikme * gunlukCeza);
                    } else {
                        durum = "Süresi Var";
                    }
                }
                oduncModel.addRow(new Object[]{ rs.getString("kitap_adi"), rs.getDate("verilis_tarihi"), teslim, durum, gosterilecekCeza > 0 ? gosterilecekCeza + " TL" : "-" });
            }
        } catch (Exception e) {}
    }

    private JPanel createBildirimPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        bildirimModel = new DefaultTableModel(new String[]{"Tarih", "Mesaj", "Durum"}, 0);
        JButton btnYenile = new JButton("Yenile / Okundu Yap");
        btnYenile.addActionListener(e -> bildirimleriYukle());
        panel.add(new JScrollPane(new JTable(bildirimModel)), BorderLayout.CENTER);
        panel.add(btnYenile, BorderLayout.SOUTH);
        bildirimleriYukle();
        return panel;
    }

    private void bildirimleriYukle() {
        bildirimModel.setRowCount(0);
        ResultSet rs = islemler.bildirimleriGetir(aktifKullanici.getId());
        try {
            while(rs != null && rs.next()) {
                bildirimModel.addRow(new Object[]{ rs.getString("tarih"), rs.getString("mesaj"), rs.getInt("okundu_mu") == 1 ? "Okundu" : "YENİ" });
            }
            islemler.bildirimleriOkunduYap(aktifKullanici.getId());
        } catch(Exception e){}
    }
}