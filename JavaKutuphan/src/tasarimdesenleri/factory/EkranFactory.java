package tasarimdesenleri.factory;

import arayuz.ekranlar.AdminEkrani;
import arayuz.ekranlar.PersonelEkrani;
import arayuz.ekranlar.UyeEkrani;
import modeller.Kullanici;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class EkranFactory {
    public static void ekranAc(Kullanici kullanici) {
        if (kullanici == null) return;
        JFrame ekran = null;
        String rol = kullanici.getRolAdi();

        switch (rol) {
            case "Yönetici":
                ekran = new AdminEkrani(kullanici);
                break;
            case "modeller.Personel":
                ekran = new PersonelEkrani(kullanici);
                break;
            case "Üye":
                ekran = new UyeEkrani(kullanici);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Tanımsız Rol: " + rol);
                return;
        }
        if (ekran != null) ekran.setVisible(true);
    }
}