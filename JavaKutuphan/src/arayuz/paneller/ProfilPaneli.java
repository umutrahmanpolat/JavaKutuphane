package arayuz.paneller;

import modeller.Kullanici;
import veritabani.KutuphaneIslemleri;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class ProfilPaneli extends JPanel {
    private KutuphaneIslemleri islemler = new KutuphaneIslemleri();
    private Kullanici aktifKullanici;
    private JTextField tAd, tSoyad, tTc, tTel, tMail;
    private JPasswordField tSifre;
    private JComboBox<String> cmbBildirim;

    public ProfilPaneli(Kullanici kullanici) {
        this.aktifKullanici = kullanici;
        setLayout(new BorderLayout());
        JPanel p = new JPanel(new GridLayout(8, 2, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20,50,20,50));

        tTc = new JTextField(); tTc.setEditable(false);
        tAd = new JTextField(); tAd.setEditable(false);
        tSoyad = new JTextField(); tSoyad.setEditable(false);
        tTel = new JTextField(); tMail = new JTextField(); tSifre = new JPasswordField();
        cmbBildirim = new JComboBox<>(new String[]{"Uygulama", "E-Posta", "SMS"});

        p.add(new JLabel("TC:")); p.add(tTc); p.add(new JLabel("Ad:")); p.add(tAd);
        p.add(new JLabel("Soyad:")); p.add(tSoyad); p.add(new JLabel("Tel:")); p.add(tTel);
        p.add(new JLabel("Mail:")); p.add(tMail); p.add(new JLabel("Bildirim:")); p.add(cmbBildirim);
        p.add(new JLabel("Şifre:")); p.add(tSifre);

        JButton btn = new JButton("GÜNCELLE");
        btn.addActionListener(e -> {
            if(islemler.kullaniciGuncelle(aktifKullanici.getId(), tAd.getText(), tSoyad.getText(), tTel.getText(), tMail.getText(), new String(tSifre.getPassword()), (String)cmbBildirim.getSelectedItem()))
                JOptionPane.showMessageDialog(this, "Güncellendi");
        });
        p.add(new JLabel("")); p.add(btn);

        try {
            ResultSet rs = islemler.kullaniciBilgileriniGetir(aktifKullanici.getId());
            if(rs!=null && rs.next()) {
                tTc.setText(rs.getString("tc_kimlik_no")); tAd.setText(rs.getString("ad")); tSoyad.setText(rs.getString("soyad"));
                tTel.setText(rs.getString("telefon")); tMail.setText(rs.getString("eposta"));
                cmbBildirim.setSelectedItem(rs.getString("bildirim_tercihi"));
            }
        } catch(Exception e){}
        add(new JLabel("PROFİLİM", SwingConstants.CENTER), BorderLayout.NORTH); add(p, BorderLayout.CENTER);
    }
}