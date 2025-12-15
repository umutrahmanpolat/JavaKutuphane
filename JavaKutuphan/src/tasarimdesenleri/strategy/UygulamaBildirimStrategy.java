package tasarimdesenleri.strategy;

import veritabani.KutuphaneIslemleri;

public class UygulamaBildirimStrategy implements BildirimStrategy {
    @Override
    public void bildirimGonder(int kullaniciId, String mesaj) {
        KutuphaneIslemleri islemler = new KutuphaneIslemleri();
        islemler.bildirimEkle(kullaniciId, mesaj);
        System.out.println("🔔 [UYGULAMA] Bildirim DB'ye kaydedildi.");
    }
}