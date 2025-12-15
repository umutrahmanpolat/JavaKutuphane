package modeller;

public abstract class Kullanici {
    protected int id;
    protected String ad;
    protected String soyad;
    protected String rolAdi;

    public Kullanici(int id, String ad, String soyad, String rolAdi) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.rolAdi = rolAdi;
    }

    public int getId() { return id; }
    public String getAd() { return ad; }
    public String getSoyad() { return soyad; }
    public String getRolAdi() { return rolAdi; }
}