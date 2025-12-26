import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Button {

    // BUTONLAR
    JButton tekrarBtn;
    JButton cıkısÜst, cıkısMenu;
    JButton skorBtn;
    JButton oynaBtn;
    JButton geriBtn;
    JButton temizleBtn;

    Yılanoyunu game;

    Button(Yılanoyunu game) {
        this.game = game;

        tekrarBtn = new JButton("TEKRAR OYNA");
        cıkısÜst = new JButton("ÇIKIŞ");
        cıkısMenu = new JButton("ÇIKIŞ");
        skorBtn = new JButton("SKORLAR");
        oynaBtn = new JButton("OYNA");
        geriBtn = new JButton("GERİ");
        temizleBtn = new JButton("SKORLARI TEMİZLE");

        tekrarBtn.addActionListener(tekrar);
        cıkısMenu.addActionListener(cikis);
        cıkısÜst.addActionListener(cikis);
        skorBtn.addActionListener(skor);
        oynaBtn.addActionListener(oyna);
        geriBtn.addActionListener(geri);
        temizleBtn.addActionListener(temizle);

        cıkısMenu.setFocusable(false);
        skorBtn.setFocusable(false);
        oynaBtn.setFocusable(false);
        geriBtn.setFocusable(false);
        temizleBtn.setFocusable(false);
    }

    // TEKRAR OYNA BUTONU AKSİYONU
    ActionListener tekrar = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.oyunSıfırla();
        }

    };

    // ÇIKIŞ BUTONU AKSİYONU
    ActionListener cikis = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.timer.stop();
            game.frame.dispose();
        }
    };

    // SKOR BUTONU AKSİYONU
    ActionListener skor = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.skorManager.skorTablosunuGuncelle(game.tabloModel);
            game.panels.üstPanel.setVisible(false);
            game.panels.show("SKORLAR");
        }
    };

    // GERİ BUTONU AKSİYONU
    ActionListener geri = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            game.panels.üstPanel.setVisible(false);
            game.panels.show("MENU");
        }

    };

    ActionListener temizle = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            int cevap = JOptionPane.showConfirmDialog(game.frame, "Tüm skorlar silinecek. Emin misiniz?",
                    "Skorları Temizle", JOptionPane.YES_NO_OPTION);
            if (cevap == JOptionPane.YES_OPTION) {
                game.skorManager.fileManager.skorlariTemizle();
            }
        }
    };

    ActionListener oyna = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.kullanıcıAd = JOptionPane.showInputDialog(game.frame, "İsim giriniz:");
            game.oyunSıfırla();
        }

    };

}