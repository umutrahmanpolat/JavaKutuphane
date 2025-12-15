package veritabani;

import java.sql.*;
import java.time.LocalDate;
import modeller.Kullanici;
import modeller.Uye;
import modeller.Personel;
import modeller.Yonetici;
import modeller.Kitap;
import tasarimdesenleri.builder.KitapBuilder;
import tasarimdesenleri.singleton.VeritabaniBaglantisi;
import tasarimdesenleri.strategy.BildirimService;
import tasarimdesenleri.observer.RezervasyonYoneticisi;

public class KutuphaneIslemleri {

    public Kullanici girisYap(String girisVerisi, String sifre) {
        Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
        Kullanici girisYapanKullanici = null;
        if (conn == null) return null;

        String sql = "SELECT k.kullanici_id, k.ad, k.soyad, r.rol_adi FROM kullanicilar k JOIN roller r ON k.rol_id = r.rol_id WHERE (k.tc_kimlik_no = ? OR k.eposta = ? OR k.kullanici_adi = ?) AND k.sifre = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, girisVerisi); pstmt.setString(2, girisVerisi);
            pstmt.setString(3, girisVerisi); pstmt.setString(4, sifre);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("kullanici_id");
                String ad = rs.getString("ad");
                String soyad = rs.getString("soyad");
                String rol = rs.getString("rol_adi");
                if (rol.equals("Üye")) girisYapanKullanici = new Uye(id, ad, soyad);
                else if (rol.equals("Yönetici")) girisYapanKullanici = new Yonetici(id, ad, soyad);
                else girisYapanKullanici = new Personel(id, ad, soyad);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return girisYapanKullanici;
    }

    public boolean kitapEkle(Kitap kitap) {
        String sql = "INSERT INTO kitaplar (kitap_adi, yazar, isbn, kategori, yayinevi, baski_yili, stok_adedi, raf_konumu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement(sql);
            pstmt.setString(1, kitap.getKitapAdi()); pstmt.setString(2, kitap.getYazar());
            pstmt.setString(3, kitap.getIsbn()); pstmt.setString(4, kitap.getKategori());
            pstmt.setString(5, kitap.getYayinevi()); pstmt.setInt(6, kitap.getBaskiYili());
            pstmt.setInt(7, kitap.getStokAdedi()); pstmt.setString(8, kitap.getRafKonumu());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean kitapGuncelle(int id, String ad, String yazar, String isbn, String kat, String yay, int yil, int stok, String raf) {
        Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
        String sql = "UPDATE kitaplar SET kitap_adi=?, yazar=?, isbn=?, kategori=?, yayinevi=?, baski_yili=?, stok_adedi=?, raf_konumu=? WHERE kitap_id=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ad); pstmt.setString(2, yazar); pstmt.setString(3, isbn);
            pstmt.setString(4, kat); pstmt.setString(5, yay); pstmt.setInt(6, yil);
            pstmt.setInt(7, stok); pstmt.setString(8, raf); pstmt.setInt(9, id);

            int sonuc = pstmt.executeUpdate();
            if (sonuc > 0 && stok > 0) {
                RezervasyonYoneticisi yayinci = new RezervasyonYoneticisi();
                yayinci.gozlemcileriBilgilendir(id);
            }
            return sonuc > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean kitapSil(int id) {
        try {
            return VeritabaniBaglantisi.getInstance().getConnection().createStatement().executeUpdate("DELETE FROM kitaplar WHERE kitap_id = " + id) > 0;
        } catch (SQLException e) { return false; }
    }

    public ResultSet kitaplariFiltreliAra(String kriter, String aramaMetni) {
        String dbSutun = switch (kriter) {
            case "Yazar" -> "yazar";
            case "ISBN" -> "isbn";
            case "Kategori" -> "kategori";
            case "Yayınevi" -> "yayinevi";
            case "Raf" -> "raf_konumu";
            default -> "kitap_adi";
        };
        try {
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement("SELECT * FROM kitaplar WHERE " + dbSutun + " LIKE ?");
            pstmt.setString(1, "%" + aramaMetni + "%");
            return pstmt.executeQuery();
        } catch (SQLException e) { return null; }
    }

    public Kitap kitapGetir(int id) {
        try {
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement("SELECT * FROM kitaplar WHERE kitap_id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new KitapBuilder().setId(rs.getInt("kitap_id")).setAd(rs.getString("kitap_adi")).setStok(rs.getInt("stok_adedi")).build();
            }
        } catch (SQLException e) { }
        return null;
    }

    public ResultSet kullanicilariListele(boolean yoneticiModu, String arama) {
        String sql = "SELECT k.*, r.rol_adi FROM kullanicilar k JOIN roller r ON k.rol_id = r.rol_id WHERE 1=1";
        if (!yoneticiModu) sql += " AND k.rol_id = 3";
        if (!arama.isEmpty()) sql += " AND (k.ad LIKE ? OR k.soyad LIKE ? OR k.tc_kimlik_no LIKE ?)";
        try {
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement(sql);
            if (!arama.isEmpty()) { String s = "%" + arama + "%"; pstmt.setString(1, s); pstmt.setString(2, s); pstmt.setString(3, s); }
            return pstmt.executeQuery();
        } catch (SQLException e) { return null; }
    }

    public boolean kullaniciEkle(String tc, String ad, String soyad, String tel, String mail, int rolId) {
        try {
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement("INSERT INTO kullanicilar (tc_kimlik_no, ad, soyad, telefon, eposta, kullanici_adi, sifre, rol_id) VALUES (?, ?, ?, ?, ?, ?, '1234', ?)");
            pstmt.setString(1, tc); pstmt.setString(2, ad); pstmt.setString(3, soyad); pstmt.setString(4, tel); pstmt.setString(5, mail);
            pstmt.setString(6, ad.toLowerCase() + "." + soyad.toLowerCase()); pstmt.setInt(7, rolId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean kullaniciGuncelle(int id, String ad, String soyad, String tel, String mail, String sifre, String bildirimTercihi) {
        try {
            StringBuilder sql = new StringBuilder("UPDATE kullanicilar SET ad=?, soyad=?, telefon=?, eposta=?, bildirim_tercihi=?");
            if (sifre != null && !sifre.isEmpty()) sql.append(", sifre=?");
            sql.append(" WHERE kullanici_id=?");
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement(sql.toString());
            int i = 1;
            pstmt.setString(i++, ad); pstmt.setString(i++, soyad); pstmt.setString(i++, tel); pstmt.setString(i++, mail); pstmt.setString(i++, bildirimTercihi);
            if (sifre != null && !sifre.isEmpty()) pstmt.setString(i++, sifre);
            pstmt.setInt(i, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean kullaniciGuncelle(int id, String ad, String soyad, String tel, String mail, String sifre) {
        return kullaniciGuncelle(id, ad, soyad, tel, mail, sifre, "Uygulama");
    }

    public ResultSet kullaniciBilgileriniGetir(int kullaniciId) {
        try {
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement("SELECT tc_kimlik_no, ad, soyad, eposta, telefon, bildirim_tercihi FROM kullanicilar WHERE kullanici_id = ?");
            pstmt.setInt(1, kullaniciId);
            return pstmt.executeQuery();
        } catch (SQLException e) { return null; }
    }

    private int getAktifOduncSayisi(int uyeId) {
        try {
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement("SELECT COUNT(*) FROM oduncislemleri WHERE kullanici_id = ? AND iade_tarihi IS NULL");
            pstmt.setInt(1, uyeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {}
        return 0;
    }

    public boolean kitapOduncVer(int uyeId, int kitapId) {
        Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
        Kitap kitap = kitapGetir(kitapId);
        if (kitap == null || !kitap.oduncVerilebilirMi()) return false;

        int maxKitap = (int) ayarGetir("max_kitap_sayisi");
        int suankiDurum = getAktifOduncSayisi(uyeId);
        if (suankiDurum >= maxKitap) return false;

        int gunSure = (int) ayarGetir("odunc_suresi_gun");

        try {
            PreparedStatement pstmtStok = conn.prepareStatement("UPDATE kitaplar SET stok_adedi = stok_adedi - 1 WHERE kitap_id = ?");
            pstmtStok.setInt(1, kitapId);
            pstmtStok.executeUpdate();

            String sql = "INSERT INTO oduncislemleri (kullanici_id, kitap_id, verilis_tarihi, son_teslim_tarihi) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY))";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, uyeId);
            pstmt.setInt(2, kitapId);
            pstmt.setInt(3, gunSure);

            BildirimService servis = new BildirimService();
            ResultSet rs = kullaniciBilgileriniGetir(uyeId);

            if(rs.next()) {
                String tercih = rs.getString("bildirim_tercihi");
                String bildirimMesaji = "'" + kitap.getKitapAdi() + "' ödünç alındı. İyi okumalar!";
                servis.tercihineGoreGonder(uyeId, tercih, bildirimMesaji);
            }
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean kitapIadeAl(int uyeId, int kitapId) {
        Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
        try {
            String sqlSelect = "SELECT son_teslim_tarihi FROM oduncislemleri WHERE kullanici_id = ? AND kitap_id = ? AND iade_tarihi IS NULL";
            PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect);
            pstmtSelect.setInt(1, uyeId); pstmtSelect.setInt(2, kitapId);
            ResultSet rs = pstmtSelect.executeQuery();

            double ceza = 0.0;
            if (rs.next()) {
                LocalDate sonTeslim = rs.getDate("son_teslim_tarihi").toLocalDate();
                long gecikme = java.time.temporal.ChronoUnit.DAYS.between(sonTeslim, LocalDate.now());
                if (gecikme > 0) {
                    ceza = ayarGetir("baslangic_cezasi_tl") + (gecikme * ayarGetir("gunluk_ceza_tl"));
                }
            } else return false;

            String sqlUpdate = "UPDATE oduncislemleri SET iade_tarihi = CURDATE(), ceza_tutari = ? WHERE kullanici_id = ? AND kitap_id = ? AND iade_tarihi IS NULL";
            PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
            pstmtUpdate.setDouble(1, ceza); pstmtUpdate.setInt(2, uyeId); pstmtUpdate.setInt(3, kitapId);

            if (pstmtUpdate.executeUpdate() > 0) {
                conn.createStatement().executeUpdate("UPDATE kitaplar SET stok_adedi = stok_adedi + 1 WHERE kitap_id = " + kitapId);

                // OBSERVER ÇAĞRISI (Stok artınca)
                RezervasyonYoneticisi yayinci = new RezervasyonYoneticisi();
                yayinci.gozlemcileriBilgilendir(kitapId);

                return true;
            }
            return false;
        } catch (SQLException e) { return false; }
    }

    public ResultSet aktifOduncleriListele(String arama) {
        try {
            String sql = "SELECT o.islem_id, k.kullanici_id, b.kitap_id, k.ad, k.soyad, b.kitap_adi, o.verilis_tarihi, o.son_teslim_tarihi FROM oduncislemleri o JOIN kullanicilar k ON o.kullanici_id = k.kullanici_id JOIN kitaplar b ON o.kitap_id = b.kitap_id WHERE o.iade_tarihi IS NULL";
            if (!arama.isEmpty()) sql += " AND (k.ad LIKE ? OR b.kitap_adi LIKE ?)";
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement(sql);
            if (!arama.isEmpty()) { pstmt.setString(1, "%"+arama+"%"); pstmt.setString(2, "%"+arama+"%"); }
            return pstmt.executeQuery();
        } catch (SQLException e) { return null; }
    }

    public ResultSet uyeOduncListesiGetir(int kullaniciId) {
        try {
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement("SELECT k.kitap_adi, o.verilis_tarihi, o.son_teslim_tarihi, o.iade_tarihi, o.ceza_tutari FROM oduncislemleri o JOIN kitaplar k ON o.kitap_id = k.kitap_id WHERE o.kullanici_id = ?");
            pstmt.setInt(1, kullaniciId);
            return pstmt.executeQuery();
        } catch (SQLException e) { return null; }
    }

    public boolean rezerveEt(int kullaniciId, int kitapId) {
        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            if(conn.createStatement().executeQuery("SELECT * FROM rezervasyonlar WHERE kullanici_id="+kullaniciId+" AND kitap_id="+kitapId+" AND durum='Aktif'").next()) return false;
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO rezervasyonlar (kullanici_id, kitap_id) VALUES (?, ?)");
            pstmt.setInt(1, kullaniciId); pstmt.setInt(2, kitapId);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public void bildirimEkle(int kullaniciId, String mesaj) {
        try {
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement("INSERT INTO bildirimler (kullanici_id, mesaj) VALUES (?, ?)");
            pstmt.setInt(1, kullaniciId); pstmt.setString(2, mesaj);
            pstmt.executeUpdate();
        } catch(Exception e){}
    }

    public ResultSet bildirimleriGetir(int kullaniciId) {
        try {
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement("SELECT mesaj, tarih, okundu_mu FROM bildirimler WHERE kullanici_id=? ORDER BY okundu_mu ASC, tarih DESC");
            pstmt.setInt(1, kullaniciId);
            return pstmt.executeQuery();
        } catch(Exception e){ return null; }
    }

    public void bildirimleriOkunduYap(int kullaniciId) {
        try { VeritabaniBaglantisi.getInstance().getConnection().createStatement().executeUpdate("UPDATE bildirimler SET okundu_mu=1 WHERE kullanici_id=" + kullaniciId); } catch(Exception e){}
    }

    public double ayarGetir(String anahtar) {
        try {
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement("SELECT ayar_degeri FROM sistemayarlari WHERE ayar_anahtari=?");
            pstmt.setString(1, anahtar);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) return rs.getDouble("ayar_degeri");
        } catch(Exception e){}
        return 0;
    }

    public boolean ayarGuncelle(String anahtar, double deger) {
        try {
            PreparedStatement pstmt = VeritabaniBaglantisi.getInstance().getConnection().prepareStatement("UPDATE sistemayarlari SET ayar_degeri=? WHERE ayar_anahtari=?");
            pstmt.setDouble(1, deger); pstmt.setString(2, anahtar);
            return pstmt.executeUpdate() > 0;
        } catch(Exception e){ return false; }
    }
}