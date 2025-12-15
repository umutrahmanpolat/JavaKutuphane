package tasarimdesenleri.strategy;

public class EpostaBildirimStrategy implements BildirimStrategy {
    @Override
    public void bildirimGonder(int kullaniciId, String mesaj) {
        System.out.println("--------------------------------------------------");
        System.out.println("📧 [E-POSTA] -> Kullanıcı ID: " + kullaniciId);
        System.out.println("   Mesaj: " + mesaj);
        System.out.println("--------------------------------------------------");
    }
}