package arayuz.paneller;

import veritabani.KutuphaneIslemleri;

import javax.swing.*;
import java.awt.*;

public class SistemAyarlariPaneli extends JPanel {
    private KutuphaneIslemleri islemler = new KutuphaneIslemleri();
    private JTextField tSure, tGunluk, tMax, tBaslangic;

    public SistemAyarlariPaneli() {
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        tSure = new JTextField(); tGunluk = new JTextField(); tMax = new JTextField(); tBaslangic = new JTextField();

        add(new JLabel("Ödünç Süresi (Gün):")); add(tSure);
        add(new JLabel("Günlük Ceza (TL):")); add(tGunluk);
        add(new JLabel("Max modeller.Kitap:")); add(tMax);
        add(new JLabel("Başlangıç Cezası (TL):")); add(tBaslangic);

        JButton btn = new JButton("KAYDET");
        btn.addActionListener(e -> {
            try {
                islemler.ayarGuncelle("odunc_suresi_gun", Double.parseDouble(tSure.getText()));
                islemler.ayarGuncelle("gunluk_ceza_tl", Double.parseDouble(tGunluk.getText()));
                islemler.ayarGuncelle("max_kitap_sayisi", Double.parseDouble(tMax.getText()));
                islemler.ayarGuncelle("baslangic_cezasi_tl", Double.parseDouble(tBaslangic.getText()));
                JOptionPane.showMessageDialog(this, "Kaydedildi");
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Sayı giriniz."); }
        });
        add(new JLabel("")); add(btn);

        tSure.setText(""+islemler.ayarGetir("odunc_suresi_gun"));
        tGunluk.setText(""+islemler.ayarGetir("gunluk_ceza_tl"));
        tMax.setText(""+islemler.ayarGetir("max_kitap_sayisi"));
        tBaslangic.setText(""+islemler.ayarGetir("baslangic_cezasi_tl"));
    }
}