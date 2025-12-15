package arayuz.paneller;

import modeller.Kitap;
import tasarimdesenleri.builder.KitapBuilder;
import veritabani.KutuphaneIslemleri;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class KitapYonetimPaneli extends JPanel {
    private KutuphaneIslemleri islemler = new KutuphaneIslemleri();
    private DefaultTableModel kitapModel;
    private JTable kitapTable;
    private boolean yoneticiModu;
    private int aktifKullaniciId;
    private JComboBox<String> cmbKriter;
    private JTextField tAra;

    public KitapYonetimPaneli(boolean yoneticiModu, int kullaniciId) {
        this.yoneticiModu = yoneticiModu;
        this.aktifKullaniciId = kullaniciId;
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbKriter = new JComboBox<>(new String[]{"Kitap Adı", "Yazar", "ISBN", "Kategori", "Yayınevi", "Raf"});
        tAra = new JTextField(15);
        JButton bAra = new JButton("Ara");
        topPanel.add(new JLabel("Kriter:")); topPanel.add(cmbKriter); topPanel.add(tAra); topPanel.add(bAra);

        if (yoneticiModu) {
            JButton bEkle = new JButton("Ekle");
            JButton bGuncelle = new JButton("Güncelle");
            JButton bSil = new JButton("Sil");
            topPanel.add(bEkle); topPanel.add(bGuncelle); topPanel.add(bSil);
            bEkle.addActionListener(e -> formuAc(null));
            bGuncelle.addActionListener(e -> {
                int r = kitapTable.getSelectedRow();
                if (r != -1) {
                    Object[] d = new Object[9];
                    for(int i=0; i<9; i++) d[i] = kitapModel.getValueAt(r, i);
                    formuAc(d);
                }
            });
            bSil.addActionListener(e -> {
                int r = kitapTable.getSelectedRow();
                if (r != -1 && JOptionPane.showConfirmDialog(this, "Silinsin mi?", "Onay", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    islemler.kitapSil((int) kitapModel.getValueAt(r, 0));
                    listele();
                }
            });
        } else {
            JButton bRezerve = new JButton("Rezerve Et");
            topPanel.add(bRezerve);
            bRezerve.addActionListener(e -> {
                int r = kitapTable.getSelectedRow();
                if (r != -1) {
                    String durum = (String) kitapModel.getValueAt(r, 7);
                    if (durum.contains("Rafta")) JOptionPane.showMessageDialog(this, "Kitap zaten rafta.");
                    else {
                        if (islemler.rezerveEt(aktifKullaniciId, (int) kitapModel.getValueAt(r, 0)))
                            JOptionPane.showMessageDialog(this, "Rezerve edildi.");
                        else JOptionPane.showMessageDialog(this, "Zaten rezervasyonunuz var.");
                    }
                }
            });
        }

        String[] cols = {"ID", "Ad", "Yazar", "ISBN", "Kategori", "Yayınevi", "Yıl", "Durum", "Raf"};
        kitapModel = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        kitapTable = new JTable(kitapModel);
        bAra.addActionListener(e -> listele());
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(kitapTable), BorderLayout.CENTER);
        listele();
    }

    private void listele() {
        kitapModel.setRowCount(0);
        ResultSet rs = islemler.kitaplariFiltreliAra((String) cmbKriter.getSelectedItem(), tAra.getText());
        try {
            while(rs != null && rs.next()) {
                int stok = rs.getInt("stok_adedi");
                Kitap k = new KitapBuilder().setStok(stok).build();
                kitapModel.addRow(new Object[]{
                        rs.getInt("kitap_id"), rs.getString("kitap_adi"), rs.getString("yazar"),
                        rs.getString("isbn"), rs.getString("kategori"), rs.getString("yayinevi"),
                        rs.getInt("baski_yili"), k.getDurumMetni() + " (" + stok + ")", rs.getString("raf_konumu")
                });
            }
        } catch(Exception e){}
    }

    private void formuAc(Object[] data) {
        boolean guncelle = (data != null);
        JTextField tAd = new JTextField(guncelle?(String)data[1]:"");
        JTextField tYazar = new JTextField(guncelle?(String)data[2]:"");
        JTextField tIsbn = new JTextField(guncelle?(String)data[3]:"");
        JTextField tKat = new JTextField(guncelle?(String)data[4]:"");
        JTextField tYay = new JTextField(guncelle?(String)data[5]:"");
        JTextField tYil = new JTextField(guncelle?data[6].toString():"");
        JTextField tStok = new JTextField();
        JTextField tRaf = new JTextField(guncelle?(String)data[8]:"");

        JPanel p = new JPanel(new GridLayout(8,2));
        p.add(new JLabel("Ad:")); p.add(tAd); p.add(new JLabel("Yazar:")); p.add(tYazar);
        p.add(new JLabel("ISBN:")); p.add(tIsbn); p.add(new JLabel("Kategori:")); p.add(tKat);
        p.add(new JLabel("Yayınevi:")); p.add(tYay); p.add(new JLabel("Yıl:")); p.add(tYil);
        p.add(new JLabel("Stok:")); p.add(tStok); p.add(new JLabel("Raf:")); p.add(tRaf);

        if(JOptionPane.showConfirmDialog(this, p, guncelle?"Güncelle":"Ekle", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
            try {
                int yil = Integer.parseInt(tYil.getText()), stok = Integer.parseInt(tStok.getText());
                if(guncelle) islemler.kitapGuncelle((int)data[0], tAd.getText(), tYazar.getText(), tIsbn.getText(), tKat.getText(), tYay.getText(), yil, stok, tRaf.getText());
                else islemler.kitapEkle(new KitapBuilder().setAd(tAd.getText()).setYazar(tYazar.getText()).setIsbn(tIsbn.getText()).setKategori(tKat.getText()).setYayinevi(tYay.getText()).setBaskiYili(yil).setStok(stok).setRaf(tRaf.getText()).build());
                listele();
            } catch(Exception e){ JOptionPane.showMessageDialog(this, "Hata."); }
        }
    }
}