package arayuz.paneller;

import veritabani.KutuphaneIslemleri;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class OduncIslemPaneli extends JPanel {
    private KutuphaneIslemleri islemler = new KutuphaneIslemleri();
    private DefaultTableModel model;
    private JTable table;

    public OduncIslemPaneli() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField tAra = new JTextField(15);
        JButton bAra = new JButton("Ara"), bVer = new JButton("Ödünç Ver"), bAl = new JButton("İade Al");
        top.add(new JLabel("Ara:")); top.add(tAra); top.add(bAra); top.add(bVer); top.add(bAl);

        model = new DefaultTableModel(new String[]{"IslemID", "UyeID", "KitapID", "Uye", "Kitap", "Verilis", "SonTeslim", "Durum"}, 0);
        table = new JTable(model);
        bAra.addActionListener(e -> listele(tAra.getText()));
        bVer.addActionListener(e -> oduncVer());
        bAl.addActionListener(e -> iadeAl());
        add(top, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER);
        listele("");
    }

    private void listele(String ara) {
        model.setRowCount(0);
        ResultSet rs = islemler.aktifOduncleriListele(ara);
        double gunluk = islemler.ayarGetir("gunluk_ceza_tl");
        double baslangic = islemler.ayarGetir("baslangic_cezasi_tl");
        try {
            while(rs!=null && rs.next()) {
                long gecikme = ChronoUnit.DAYS.between(rs.getDate("son_teslim_tarihi").toLocalDate(), LocalDate.now());
                String durum = "Normal";
                if(gecikme > 0) durum = "GECİKMİŞ (" + (baslangic + gecikme * gunluk) + " TL)";
                model.addRow(new Object[]{rs.getInt("islem_id"), rs.getInt("kullanici_id"), rs.getInt("kitap_id"), rs.getString("ad")+" "+rs.getString("soyad"), rs.getString("kitap_adi"), rs.getDate("verilis_tarihi"), rs.getDate("son_teslim_tarihi"), durum});
            }
        } catch(Exception e){}
    }

    private void oduncVer() {
        JTextField u = new JTextField(), k = new JTextField();
        JPanel p = new JPanel(new GridLayout(2,2)); p.add(new JLabel("Uye ID:")); p.add(u); p.add(new JLabel("Kitap ID:")); p.add(k);
        if(JOptionPane.showConfirmDialog(this, p, "Ödünç Ver", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
            try {
                if(islemler.kitapOduncVer(Integer.parseInt(u.getText()), Integer.parseInt(k.getText()))) { JOptionPane.showMessageDialog(this, "Başarılı"); listele(""); }
                else JOptionPane.showMessageDialog(this, "Hata: Stok yok veya limit dolu.");
            } catch(Exception e){ JOptionPane.showMessageDialog(this, "Sayı giriniz."); }
        }
    }

    private void iadeAl() {
        int r = table.getSelectedRow();
        if(r!=-1 && JOptionPane.showConfirmDialog(this, "İade alınsın mı?", "Onay", JOptionPane.YES_NO_OPTION)==JOptionPane.OK_OPTION) {
            if(islemler.kitapIadeAl((int)model.getValueAt(r,1), (int)model.getValueAt(r,2))) { JOptionPane.showMessageDialog(this, "İade alındı."); listele(""); }
        }
    }
}