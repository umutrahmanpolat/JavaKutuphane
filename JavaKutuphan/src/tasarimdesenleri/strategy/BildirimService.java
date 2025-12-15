package tasarimdesenleri.strategy;

public class BildirimService {
    private BildirimStrategy strateji;

    public void tercihineGoreGonder(int kullaniciId, String tercih, String mesaj) {
        switch (tercih) {
            case "SMS":
                strateji = new SmsBildirimStrategy();
                break;
            case "E-Posta":
                strateji = new EpostaBildirimStrategy();
                break;
            case "Uygulama":
            default:
                strateji = new UygulamaBildirimStrategy();
                break;
        }
        strateji.bildirimGonder(kullaniciId, mesaj);
    }
}