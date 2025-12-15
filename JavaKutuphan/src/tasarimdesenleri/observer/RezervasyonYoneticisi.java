package tasarimdesenleri.observer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import tasarimdesenleri.strategy.BildirimService;
import tasarimdesenleri.singleton.VeritabaniBaglantisi;
import veritabani.KutuphaneIslemleri;
import modeller.Uye;

public class RezervasyonYoneticisi implements RezervasyonYayinlayici {

    @Override
    public void gozlemciEkle(Uye uye) {}

    @Override
    public void gozlemciKaldir(Uye uye) {}

    @Override
    public void gozlemcileriBilgilendir(int kitapId) {
        BildirimService servis = new BildirimService();
        KutuphaneIslemleri islemler = new KutuphaneIslemleri();

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();

            String sql = "SELECT r.rezervasyon_id, r.kullanici_id, k.bildirim_tercihi, b.kitap_adi " +
                    "FROM rezervasyonlar r " +
                    "JOIN kullanicilar k ON r.kullanici_id=k.kullanici_id " +
                    "JOIN kitaplar b ON r.kitap_id=b.kitap_id " +
                    "WHERE r.kitap_id=? AND r.durum='Aktif'";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, kitapId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                int kullaniciId = rs.getInt("kullanici_id");
                String tercih = rs.getString("bildirim_tercihi");
                String kitapAdi = rs.getString("kitap_adi");

                String bildirimMesaji = "MÜJDE! '"+kitapAdi+"' kitabı iade edildi!";
                servis.tercihineGoreGonder(kullaniciId, tercih, bildirimMesaji);

                conn.createStatement().executeUpdate("UPDATE rezervasyonlar SET durum='Tamamlandı' WHERE rezervasyon_id=" + rs.getInt("rezervasyon_id"));
            }
        } catch(Exception e){ e.printStackTrace(); }
    }
}