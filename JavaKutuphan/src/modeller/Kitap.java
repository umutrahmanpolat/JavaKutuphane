package modeller;

import tasarimdesenleri.builder.KitapBuilder;
import tasarimdesenleri.state.KitapState;
import tasarimdesenleri.state.OduncteState;
import tasarimdesenleri.state.RaftaState;

public class Kitap {
    private int kitapId;
    private String kitapAdi;
    private String yazar;
    private String isbn;
    private String kategori;
    private String yayinevi;
    private int baskiYili;
    private int stokAdedi;
    private String rafKonumu;
    private KitapState durum;

    public Kitap(KitapBuilder builder) {
        this.kitapId = builder.getKitapId();
        this.kitapAdi = builder.getKitapAdi();
        this.yazar = builder.getYazar();
        this.isbn = builder.getIsbn();
        this.kategori = builder.getKategori();
        this.yayinevi = builder.getYayinevi();
        this.baskiYili = builder.getBaskiYili();
        this.stokAdedi = builder.getStokAdedi();
        this.rafKonumu = builder.getRafKonumu();

        if (this.stokAdedi > 0) {
            this.durum = new RaftaState();
        } else {
            this.durum = new OduncteState();
        }
    }

    public void setDurum(KitapState yeniDurum) {
        this.durum = yeniDurum;
    }
    public KitapState getDurum() { return durum; }
    public String getDurumMetni() { return durum.durumAdi(); }
    public boolean oduncVerilebilirMi() { return durum.oduncVerilebilirMi(); }

    public void stokGuncelle(int yeniStok) {
        this.stokAdedi = yeniStok;
        this.durum.durumGuncelle(this);
    }

    public int getKitapId() { return kitapId; }
    public String getKitapAdi() { return kitapAdi; }
    public String getYazar() { return yazar; }
    public String getIsbn() { return isbn; }
    public String getKategori() { return kategori; }
    public String getYayinevi() { return yayinevi; }
    public int getBaskiYili() { return baskiYili; }
    public int getStokAdedi() { return stokAdedi; }
    public String getRafKonumu() { return rafKonumu; }
}