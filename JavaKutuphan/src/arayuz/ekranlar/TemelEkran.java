package arayuz.ekranlar;

import javax.swing.*;
import java.awt.*;

public abstract class TemelEkran extends JFrame {
    public TemelEkran(String baslik) {
        setTitle(baslik);
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    protected abstract void arayuzuOlustur();
    protected JPanel cikisPaneliniGetir() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        JPanel sagPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton btnCikis = new JButton("Çıkış Yap");
        btnCikis.setBackground(new Color(220, 53, 69));
        btnCikis.setForeground(Color.WHITE);
        btnCikis.setFont(new Font("Arial", Font.BOLD, 12));
        btnCikis.setFocusPainted(false);
        btnCikis.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Çıkış yapılsın mı?", "Çıkış", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                new GirisEkrani().setVisible(true);
                this.dispose();
            }
        });
        sagPanel.add(btnCikis);
        panel.add(sagPanel, BorderLayout.EAST);
        return panel;
    }
}