import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;

public class Yılanoyunu {

    private static final int satır = 20;
    private static final int sütun = 20;

    // PENCERE
    JFrame frame;

    // PANELLER
    PanelYönetimi panels;

    // BUTONLAR
    Button butons;

    // TABLO
    JLabel[][] tablo = new JLabel[satır][sütun];

    // YILAN İLE İLGİLİ
    Yılan yılan;
    Timer timer;
    int mevcutHız = 225;
    boolean yonDegisti = false;

    // SÜRE GÖSTERİMİ
    JLabel sure;
    int saniye = 0;
    int milisaniyeSayaci = 0;
    int dakika = 0;

    // YEMEK İLE İLGİLİ
    Yem cilek;
    int toplamCilek = 0;

    // KONTROL
    KontrolSınıfı kontrol;
    KlavyeTusları tus;

    // SKOR TABLOSU
    String kullanıcıAd;
    SkorManager skorManager;
    DefaultTableModel tabloModel;
    JLabel skor;

    public Yılanoyunu() {

        // YARDIMCI SINIFLAR
        butons = new Button();
        butons.game = this;
        kontrol = new KontrolSınıfı();
        skorManager = new SkorManager();

        // PENCERE OLUŞTURMA
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ---------------------------PANEL İŞLEMLERİ---------------------------
        panels = new PanelYönetimi(satır, sütun, tablo, ColorSınıfı.renk1, ColorSınıfı.renk2, ColorSınıfı.renk3);

        // SKOR GÖSTERİMİ
        skor = new JLabel("Skor: " + toplamCilek);
        panels.üstPanel.add(skor);

        // SÜRE EKLEME
        sure = new JLabel(String.format("Süre: %02d:%02d", dakika, saniye));
        panels.üstPanel.add(sure);

        // ------SKOR TABLOSU-------
        tabloModel = new DefaultTableModel();
        tabloModel.addColumn("Kullanıcı Adı");
        tabloModel.addColumn("Skor");

        JTable skorTablosu = new JTable(tabloModel);
        skorTablosu.setEnabled(false);
        skorTablosu.setRowHeight(15);
        skorTablosu.getTableHeader().setPreferredSize(new Dimension(30, 10));

        JScrollPane scrollPane = new JScrollPane(skorTablosu);
        scrollPane.setPreferredSize(new Dimension(200, 450));

        JPanel skortabloPanel = new JPanel(new GridBagLayout());
        skortabloPanel.setOpaque(false);
        skortabloPanel.add(scrollPane);

        // -----------SKOR PANELİ------------
        panels.skorPanel.add(skortabloPanel, BorderLayout.CENTER);
        skorManager.skorTablosunuGuncelle(tabloModel);

        // -----------YILAN VE YEMEK OLUŞTURMA-----------
        yılan = new Yılan();
        cilek = new Yem();
        timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oyunGuncelle();
            }

        });

        // ÜST PANEL -BUTON EKLEME
        panels.üstPanel.add(butons.cıkısÜst);
        panels.üstPanel.add(butons.tekrarBtn);

        

        // SKOR BUTON PANELİ
        JPanel skorbtnPanel = new JPanel();
        skorbtnPanel.setOpaque(false);
        skorbtnPanel.add(butons.geriBtn);
        skorbtnPanel.add(butons.temizleBtn);

        // GİRİŞ MENU BUTON PANELİ
        JPanel menubtnPanel = new JPanel();
        menubtnPanel.setOpaque(false);
        menubtnPanel.add(butons.cıkısMenu);
        menubtnPanel.add(butons.oynaBtn);
        menubtnPanel.add(butons.skorBtn);

        // BUTON PANELLERİNİ EKLEME
        panels.menuPanel.add(menubtnPanel);
        panels.skorPanel.add(skorbtnPanel, BorderLayout.SOUTH);

        // PENCEREYE PANELLERİ EKLEME
        frame.add(panels.üstPanel, BorderLayout.NORTH);
        frame.add(panels.merkezPanel, BorderLayout.CENTER);

        // KLAVYE YÖNETİMİ
        tus = new KlavyeTusları(yılan, this);
        panels.panel.addKeyListener(tus);
        panels.panel.setFocusable(true);

        panels.show("MENU");
        panels.üstPanel.setVisible(false);

        frame.pack();
        frame.setLocationRelativeTo(null); // null:merkeze göre konumlandırır
        frame.setVisible(true);

        panels.panel.requestFocusInWindow();

        cilek.cilekÜret(satır, sütun, yılan.getGovde());

    }

    public void oyunGuncelle() {
        boolean eat = cilek.cilekYendiMi(yılan.getBas());

        yılan.hareketEt(eat);

        if (kontrol.duvaraCarptiMi(yılan.getBas(), satır, sütun) || kontrol.kendineCarpTiMi(yılan.getGovde())) {
            timer.stop();
            JOptionPane.showMessageDialog(frame, "Oyun Bitti :(");
            oyunBitti();
            return;
        }

        if (eat) {
            toplamCilek++;
            skor.setText("Skor: " + toplamCilek);
            cilek.cilekÜret(satır, sütun, yılan.getGovde());
        }

        hızGüncelle();
        ciz();
        yonDegisti = false;

        milisaniyeSayaci += timer.getDelay();
        if (milisaniyeSayaci >= 1000) {
            saniye++;
            milisaniyeSayaci = 0;
            if (saniye >= 60) {
                dakika++;
                saniye = 0;
            }
            sure.setText(String.format("Süre: %02d:%02d", dakika, saniye));

        }
    }

    void temizle() {
        for (int a = 0; a < satır; a++) {
            for (int b = 0; b < sütun; b++) {
                tablo[a][b].setBackground(ColorSınıfı.renk1);
                tablo[a][b].setIcon(null);
            }
        }

    }

    void ciz() {
        temizle();
        Color govdeRenk;

        if (toplamCilek >= 15) {
            govdeRenk = ColorSınıfı.yılanRenk3;
        } else if (toplamCilek >= 8 && toplamCilek < 15) {
            govdeRenk = ColorSınıfı.yılanRenk2;
        } else {
            govdeRenk = ColorSınıfı.yılanRenk;
        }

        Color kafaRengi = govdeRenk.darker();
        List<Point> govde = yılan.getGovde();

        for (int i = 0; i < govde.size(); i++) {
            Point p = govde.get(i);

            if (i == 0) {
                tablo[p.y][p.x].setBackground(kafaRengi);
            } else {
                tablo[p.y][p.x].setBackground(govdeRenk);
            }
        }

        tablo[cilek.cilekY][cilek.cilekX].setIcon(cilek.yeniCilek);
    }

    void oyunSıfırla() {
        timer.stop();

        saniye = 0;
        milisaniyeSayaci = 0;
        dakika = 0;
        sure.setText(String.format("Süre: %02d:%02d", dakika, saniye));

        yılan = new Yılan();
        tus.setYılan(yılan);

        toplamCilek = 0;
        skor.setText("Skor: " + toplamCilek);

        mevcutHız = 200;
        timer.setDelay(mevcutHız);

        cilek.cilekÜret(satır, sütun, yılan.getGovde());
        ciz();

        panels.üstPanel.setVisible(true);

        panels.show("OYUN");

        timer.start();
        panels.panel.requestFocusInWindow();

    }

    void hızGüncelle() {

        if (toplamCilek >= 15) {
            mevcutHız = 85;
        } else if (toplamCilek >= 8 && toplamCilek < 15) {
            mevcutHız = 100;
        } else {
            mevcutHız = 200;
        }
        timer.setDelay(mevcutHız);
    }

    void oyunBitti() {

        skorManager.skorEkle(kullanıcıAd, toplamCilek);
        skorManager.skorTablosunuGuncelle(tabloModel);

        panels.üstPanel.setVisible(false);

        panels.show("MENU");

        frame.revalidate(); // paneller değiştiği için hangi panelin nerede duracağını belirler.
        frame.repaint(); // ekranı baştan çizer.
    }

}
