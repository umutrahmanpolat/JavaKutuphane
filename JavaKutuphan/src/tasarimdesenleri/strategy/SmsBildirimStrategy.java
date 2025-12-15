package tasarimdesenleri.strategy;

public class SmsBildirimStrategy implements BildirimStrategy {
    @Override
    public void bildirimGonder(int kullaniciId, String mesaj) {
        System.out.println("📱 [SMS] -> Kullanıcı ID: " + kullaniciId);
        System.out.println("   Mesaj: " + mesaj);
    }
}