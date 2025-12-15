package arayuz.ekranlar;

import tasarimdesenleri.factory.EkranFactory;
import modeller.Kullanici;
import veritabani.KutuphaneIslemleri;

import javax.swing.*;
import java.awt.*;

public class GirisEkrani extends JFrame {
    private JTextField txtKullanici;
    private JPasswordField txtSifre;
    private KutuphaneIslemleri islemler = new KutuphaneIslemleri();

    public GirisEkrani() {
        setTitle("Kütüphane Giriş");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JPanel pnlUser = new JPanel();
        pnlUser.add(new JLabel("Kullanıcı Adı / TC: "));
        txtKullanici = new JTextField(15);
        pnlUser.add(txtKullanici);

        JPanel pnlPass = new JPanel();
        pnlPass.add(new JLabel("Şifre: "));
        txtSifre = new JPasswordField(15);
        pnlPass.add(txtSifre);

        JButton btnGiris = new JButton("GİRİŞ YAP");

        add(new JLabel("KÜTÜPHANE SİSTEMİ", SwingConstants.CENTER));
        add(pnlUser); add(pnlPass); add(btnGiris);

        btnGiris.addActionListener(e -> {
            Kullanici kullanici = islemler.girisYap(txtKullanici.getText(), new String(txtSifre.getPassword()));
            if (kullanici != null) {
                JOptionPane.showMessageDialog(this, "Hoşgeldin: " + kullanici.getAd());
                EkranFactory.ekranAc(kullanici);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Hatalı Giriş!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}