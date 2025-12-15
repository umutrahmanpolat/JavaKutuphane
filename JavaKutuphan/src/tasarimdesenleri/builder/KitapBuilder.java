package tasarimdesenleri.builder;

import modeller.Kitap;

public class KitapBuilder {
    private int kitapId;
    private String kitapAdi;
    private String yazar;
    private String isbn;
    private String kategori;
    private String yayinevi;
    private int baskiYili;
    private int stokAdedi;
    private String rafKonumu;

    public KitapBuilder() {}

    public KitapBuilder setId(int id) { this.kitapId = id; return this; }
    public KitapBuilder setAd(String ad) { this.kitapAdi = ad; return this; }
    public KitapBuilder setYazar(String yazar) { this.yazar = yazar; return this; }
    public KitapBuilder setIsbn(String isbn) { this.isbn = isbn; return this; }
    public KitapBuilder setKategori(String kategori) { this.kategori = kategori; return this; }
    public KitapBuilder setYayinevi(String yayinevi) { this.yayinevi = yayinevi; return this; }
    public KitapBuilder setBaskiYili(int yil) { this.baskiYili = yil; return this; }
    public KitapBuilder setStok(int stok) { this.stokAdedi = stok; return this; }
    public KitapBuilder setRaf(String raf) { this.rafKonumu = raf; return this; }

    public int getKitapId() { return kitapId; }
    public String getKitapAdi() { return kitapAdi; }
    public String getYazar() { return yazar; }
    public String getIsbn() { return isbn; }
    public String getKategori() { return kategori; }
    public String getYayinevi() { return yayinevi; }
    public int getBaskiYili() { return baskiYili; }
    public int getStokAdedi() { return stokAdedi; }
    public String getRafKonumu() { return rafKonumu; }

    public Kitap build() {
        return new Kitap(this);
    }
}