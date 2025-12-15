package arayuz.paneller;

import veritabani.KutuphaneIslemleri;
import tasarimdesenleri.singleton.VeritabaniBaglantisi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class KullaniciYonetimPaneli extends JPanel {
    private KutuphaneIslemleri islemler = new KutuphaneIslemleri();
    private DefaultTableModel model;
    private JTable table;
    private boolean adminModu;

    public KullaniciYonetimPaneli(boolean adminModu) {
        this.adminModu = adminModu;
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField tAra = new JTextField(15);
        JButton bAra = new JButton("Ara"), bEkle = new JButton("Ekle"), bGuncelle = new JButton("Güncelle"), bSil = new JButton("Sil"), bGecmis = new JButton("Geçmiş");
        top.add(new JLabel("Ara:")); top.add(tAra); top.add(bAra); top.add(bEkle); top.add(bGuncelle); top.add(bSil); top.add(bGecmis);

        model = new DefaultTableModel(new String[]{"ID", "Rol", "TC", "Ad", "Soyad", "Mail", "Tel"}, 0);
        table = new JTable(model);
        bAra.addActionListener(e -> listele(tAra.getText()));
        bEkle.addActionListener(e -> ekleFormu());
        bGuncelle.addActionListener(e -> guncelleFormu());
        bSil.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r!=-1 && JOptionPane.showConfirmDialog(this, "Silinsin mi?", "Onay", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                try {
                    VeritabaniBaglantisi.getInstance().getConnection().createStatement().executeUpdate("DELETE FROM kullanicilar WHERE kullanici_id=" + model.getValueAt(r, 0));
                    listele("");
                } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Hata: Geçmiş kaydı olabilir."); }
            }
        });
        bGecmis.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r!=-1) gecmisGoster((int)model.getValueAt(r,0), (String)model.getValueAt(r,3));
        });
        add(top, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER);
        listele("");
    }

    private void listele(String ara) {
        model.setRowCount(0);
        ResultSet rs = islemler.kullanicilariListele(adminModu, ara);
        try { while(rs!=null && rs.next()) model.addRow(new Object[]{rs.getInt("kullanici_id"), rs.getString("rol_adi"), rs.getString("tc_kimlik_no"), rs.getString("ad"), rs.getString("soyad"), rs.getString("eposta"), rs.getString("telefon")}); } catch(Exception e){}
    }

    private void ekleFormu() {
        JTextField tTc=new JTextField(), tAd=new JTextField(), tSoyad=new JTextField(), tMail=new JTextField(), tTel=new JTextField();
        JComboBox<String> cmbRol=new JComboBox<>(adminModu ? new String[]{"Personel", "Üye"} : new String[]{"Üye"});
        JPanel p = new JPanel(new GridLayout(6,2));
        p.add(new JLabel("Rol:")); p.add(cmbRol); p.add(new JLabel("TC:")); p.add(tTc);
        p.add(new JLabel("Ad:")); p.add(tAd); p.add(new JLabel("Soyad:")); p.add(tSoyad);
        p.add(new JLabel("Mail:")); p.add(tMail); p.add(new JLabel("Tel:")); p.add(tTel);
        if(JOptionPane.showConfirmDialog(this, p, "Ekle", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
            islemler.kullaniciEkle(tTc.getText(), tAd.getText(), tSoyad.getText(), tTel.getText(), tMail.getText(), cmbRol.getSelectedItem().equals("Personel")?2:3);
            listele("");
        }
    }

    private void guncelleFormu() {
        int r = table.getSelectedRow();
        if(r==-1) return;
        JTextField tAd=new JTextField((String)model.getValueAt(r,3)), tSoyad=new JTextField((String)model.getValueAt(r,4)), tMail=new JTextField((String)model.getValueAt(r,5)), tTel=new JTextField((String)model.getValueAt(r,6));
        JPasswordField tPas = new JPasswordField();
        JPanel p = new JPanel(new GridLayout(5,2));
        p.add(new JLabel("Ad:")); p.add(tAd); p.add(new JLabel("Soyad:")); p.add(tSoyad);
        p.add(new JLabel("Mail:")); p.add(tMail); p.add(new JLabel("Tel:")); p.add(tTel);
        p.add(new JLabel("Şifre:")); p.add(tPas);
        if(JOptionPane.showConfirmDialog(this, p, "Güncelle", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
            islemler.kullaniciGuncelle((int)model.getValueAt(r,0), tAd.getText(), tSoyad.getText(), tTel.getText(), tMail.getText(), new String(tPas.getPassword()));
            listele("");
        }
    }

    private void gecmisGoster(int id, String ad) {
        JDialog d = new JDialog((Frame)null, ad + " Geçmişi", true);
        d.setSize(600,400); d.setLocationRelativeTo(this);
        DefaultTableModel m = new DefaultTableModel(new String[]{"Kitap", "Veriliş", "Teslim", "İade", "Ceza"}, 0);
        try {
            ResultSet rs = islemler.uyeOduncListesiGetir(id);
            while(rs!=null && rs.next()) m.addRow(new Object[]{rs.getString("kitap_adi"), rs.getDate("verilis_tarihi"), rs.getDate("son_teslim_tarihi"), rs.getDate("iade_tarihi")==null?"İade Edilmedi":rs.getDate("iade_tarihi"), rs.getDouble("ceza_tutari")});
        } catch(Exception e){}
        d.add(new JScrollPane(new JTable(m))); d.setVisible(true);
    }
}