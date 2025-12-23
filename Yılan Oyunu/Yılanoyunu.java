import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;

public class Yılanoyunu implements KeyListener {

    private static final int satır = 20;
    private static final int sütun = 20;
    private static final int kare_boyut = 25;

    // PENCERE
    JFrame frame;

    // PANELLER
    JPanel panel;
    JPanel üstPanel;
    JPanel merkezPanel;
    JPanel skorPanel;
    JPanel menuPanel;

    // TABLO
    JLabel[][] tablo = new JLabel[satır][sütun];

    // BUTONLAR
    JButton tekrarBtn;
    JButton cıkısÜst, cıkısMenu;
    JButton skorBtn;
    JButton oynaBtn;
    JButton geriBtn;
    JButton temizleBtn;

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
    int cilekX, cilekY;
    ImageIcon yeniCilek;
    int toplamCilek = 0;

    // RENKLER
    Color color = new Color(135, 206, 235); //arka plan
    Color color2 = new Color(93, 63, 211);//çizgiler
    Color color3 = new Color(207, 159, 255);//menü arka plan renk 2
    Color yılanRenk = new Color(255, 182, 193); //yılanın ilk hızındaki  rengi
    Color yılanRenk2 = new Color(207, 159, 255); // yılanın orta hızdaki rengi
    Color yılanRenk3 = new Color(0, 0, 139); //yılanın son hızındaki rengi

    // SKOR TABLOSU
    Map<String, List<Integer>> skorTablosuVeri = new HashMap<>();
    String kullanıcıAd;
    JTable skorTablosu;
    DefaultTableModel tabloModel;
    JLabel skor;

    public Yılanoyunu() {
        skorlarıDosyadanOku();

        // PENCERE OLUŞTURMA
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ---------------------------PANEL İŞLEMLERİ---------------------------
        panel = new JPanel(new GridLayout(satır, sütun));
        üstPanel = new JPanel();

        // SKOR GÖSTERİMİ
        skor = new JLabel("Skor: " + toplamCilek);
        üstPanel.add(skor);

        // SÜRE EKLEME
        sure = new JLabel(String.format("Süre: %02d:%02d", dakika, saniye));
        üstPanel.add(sure);

        // ------SKOR TABLOSU-------

        tabloModel = new DefaultTableModel();
        tabloModel.addColumn("Kullanıcı Adı");
        tabloModel.addColumn("Skor");

        skorTablosu = new JTable(tabloModel);
        skorTablosu.setEnabled(false);
        skorTablosu.setRowHeight(15);
        skorTablosu.getTableHeader().setPreferredSize(new Dimension(30, 10));

        JScrollPane scrollPane = new JScrollPane(skorTablosu);
        scrollPane.setPreferredSize(new Dimension(200, 450));

        JPanel tabloPanel = new JPanel(new GridBagLayout());
        tabloPanel.setOpaque(false);
        tabloPanel.add(scrollPane);

        // -----------SKOR PANELİ------------
        skorPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                java.awt.GradientPaint gp = new java.awt.GradientPaint(0, 0, color3, w, h, color2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);
            }
        };
        skorPanel.add(tabloPanel, BorderLayout.CENTER);

        // --------------------GİRİŞ MENU PANELİ---------------
        menuPanel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                java.awt.GradientPaint gp = new java.awt.GradientPaint(0, 0, color3, w, h, color2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);
            }
        };
        menuPanel.setLayout(new GridBagLayout());

        // ------------MERKEZ PANEL-----------
        merkezPanel = new JPanel(new CardLayout());
        merkezPanel.add(panel, "OYUN");
        merkezPanel.add(skorPanel, "SKORLAR");
        merkezPanel.add(menuPanel, "MENU");

        // -----------PROGRAM AÇILINCA ÇIKACAK OLAN PANEL---------
        CardLayout cl = (CardLayout) merkezPanel.getLayout();
        cl.show(merkezPanel, "MENU");

        üstPanel.setVisible(false);

        // -----------YILAN OLUŞTURMA-----------
        yılan = new Yılan();
        timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oyunGuncelle();
            }

        });

        // ----------ÇİLEK(YEMEK) GÖRSELİ EKLEME-----------
        ImageIcon cilek = new ImageIcon("cilek2.png");
        Image image = cilek.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        yeniCilek = new ImageIcon(image);

        /// -------OYUN ALANINI OLUŞTURMA(KARELİ ALAN)--------
        for (int a = 0; a < satır; a++) {
            for (int b = 0; b < sütun; b++) {
                JLabel kare = new JLabel();
                kare.setPreferredSize(new Dimension(kare_boyut, kare_boyut));
                kare.setOpaque(true);
                kare.setBackground(color);
                kare.setBorder(BorderFactory.createLineBorder(color2));

                tablo[a][b] = kare;
                panel.add(kare);
            }
        }

        // -----------------------------BUTON İŞLEMLERİ---------------------

        // TEKRAR OYNA BUTONU AKSİYONU
        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oyunSıfırla();
            }

        };

        // ÇIKIŞ BUTONU AKSİYONU
        ActionListener a2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                frame.dispose();
            }
        };

        // SKOR BUTONU AKSİYONU
        ActionListener a3 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                skorTablosunuGuncelle();
                üstPanel.setVisible(false);

                CardLayout cl3 = (CardLayout) merkezPanel.getLayout();
                cl3.show(merkezPanel, "SKORLAR");
            }
        };

        // GERİ BUTONU AKSİYONU
        ActionListener a4 = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                üstPanel.setVisible(false);
                CardLayout cl4 = (CardLayout) merkezPanel.getLayout();
                cl4.show(merkezPanel, "MENU");
            }

        };

        // ÜST PANEL BUTONLARI
        tekrarBtn = new JButton("TEKRAR OYNA");
        tekrarBtn.addActionListener(a);

        cıkısÜst = new JButton("ÇIKIŞ");
        cıkısÜst.addActionListener(a2);

        üstPanel.add(cıkısÜst);
        üstPanel.add(tekrarBtn);

        // MENU PANELİ BUTONLARI
        cıkısMenu = new JButton("ÇIKIŞ");
        cıkısMenu.addActionListener(a2);

        skorBtn = new JButton("SKORLAR");
        skorBtn.addActionListener(a3);

        oynaBtn = new JButton("OYNA");
        oynaBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kullanıcıAd = JOptionPane.showInputDialog(frame, "İsim giriniz:");
                oyunSıfırla();
            }

        });

        // SKOR PANELİ BUTONU
        geriBtn = new JButton("GERİ");
        geriBtn.addActionListener(a4);
        geriBtn.setFocusable(false);

        temizleBtn = new JButton("SKORLARI TEMİZLE");
        temizleBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int cevap = JOptionPane.showConfirmDialog(frame, "Tüm skorlar silinecek. Emin misiniz?",
                        "Skorları Temizle", JOptionPane.YES_NO_OPTION);
                if (cevap == JOptionPane.YES_OPTION) {
                    skorlariTemizle();
                }
            }

        });

        // SKOR BUTON PANELİ
        JPanel skorbtnPanel = new JPanel();
        skorbtnPanel.setOpaque(false);
        skorbtnPanel.add(geriBtn);
        skorbtnPanel.add(temizleBtn);

        // GİRİŞ MENU BUTON PANELİ
        JPanel menubtnPanel = new JPanel();
        menubtnPanel.setOpaque(false);

        menubtnPanel.add(cıkısMenu);
        menubtnPanel.add(oynaBtn);
        menubtnPanel.add(skorBtn);

        // BUTON PANELLERİNİ EKLEME
        menuPanel.add(menubtnPanel);
        skorPanel.add(skorbtnPanel, BorderLayout.SOUTH);

        // PENCEREYE PANELLERİ EKLEME
        frame.add(üstPanel, BorderLayout.NORTH);
        frame.add(merkezPanel, BorderLayout.CENTER);

        tekrarBtn.setFocusable(false);
        cıkısÜst.setFocusable(false);

        panel.setFocusable(true);
        panel.addKeyListener(this);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        panel.requestFocusInWindow();

        cilekÜret();
        skorTablosunuGuncelle();

    }

    public void cilekÜret() {

        boolean yılanınUstunde;
        do { //çileğin yılanın üstünde(gövdesinin herhangi bir yerinde) oluşmasını engelleme

            yılanınUstunde = false;
            cilekX = (int) (Math.random() * sütun);
            cilekY = (int) (Math.random() * satır);

            for (Point y : yılan.getGovde()) {  // yılanın gövdesinden bir parçasının çileğin x ve y koordinatına eşit olup olmadığına bakarız
                if (y.x == cilekX && y.y == cilekY) {
                    yılanınUstunde = true;
                    break; // eğer bir parçası eşitse diğer parçalarına bakmaya gerek kalmaz, döngüyü burada sonlandırıp tekrardan çilek için yeni bir koordinat üretilmesini sağlar.
                }
            }

        } while (yılanınUstunde); //çilek yılanın üstünde ise döngünün başına gider

        tablo[cilekY][cilekX].setIcon(yeniCilek); //yılanın üstünde değilse do-while döngüsünden çıkar ve çileği oluşturur.
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_RIGHT && yılan.yonX != -1 && !yonDegisti) {
            yılan.yonX = 1;
            yılan.yonY = 0;
            yonDegisti = true;
        }

        if (key == KeyEvent.VK_LEFT && yılan.yonX != 1 && !yonDegisti) {
            yılan.yonX = -1;
            yılan.yonY = 0;
            yonDegisti = true;

        }

        if (key == KeyEvent.VK_UP && yılan.yonY != 1 && !yonDegisti) {
            yılan.yonX = 0;
            yılan.yonY = -1;
            yonDegisti = true;

        }

        if (key == KeyEvent.VK_DOWN && yılan.yonY != -1 && !yonDegisti) {
            yılan.yonX = 0;
            yılan.yonY = 1;
            yonDegisti = true;

        }
        if (key == KeyEvent.VK_P) {
            if (timer.isRunning()) {
                timer.stop();
            } else {
                timer.start();
            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

    public void oyunGuncelle() {
        boolean eat = cilekYendiMi();

        yılan.hareketEt(eat);

        if (duvaraCarptiMi() || kendineCarpTiMi()) {
            timer.stop();
            JOptionPane.showMessageDialog(frame, "Oyun Bitti :(");
            oyunBitti();
            return;
        }

        if (eat) {
            cilekÜret();
        }

        hızGüncelle();
        ciz();
        yonDegisti = false;

        milisaniyeSayaci += mevcutHız;
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
                tablo[a][b].setBackground(color);
                tablo[a][b].setIcon(null);
            }
        }

    }

    void ciz() {
        temizle();
        Color mevcutRenk;

        if (mevcutHız == 100) {
            mevcutRenk = yılanRenk2;
        } else if (mevcutHız == 85) {
            mevcutRenk = yılanRenk3;
        } else {
            mevcutRenk = yılanRenk;
        }

        for (Point p : yılan.getGovde()) {
            tablo[p.y][p.x].setBackground(mevcutRenk);
        }

        tablo[cilekY][cilekX].setIcon(yeniCilek);
    }

    boolean duvaraCarptiMi() {
        Point bas = yılan.getBas();

        if (bas.x < 0 || bas.x >= sütun) { // sol duvarın kordinatı (0,0) olduğundan bas.x 0' dan küçük olamaz ve
                                           // sütunun dışına çıkamaz.
            return true; // çarptığı için true
        }

        if (bas.y < 0 || bas.y >= satır) {
            return true;
        }
        return false; // çarpmadığı için false
    }

    boolean cilekYendiMi() {
        Point bas = yılan.getBas();
        if (bas.x == cilekX && bas.y == cilekY) {
            toplamCilek++;
            skor.setText("Skor: " + toplamCilek);
            return true;
        }

        return false;
    }

    void oyunSıfırla() {
        timer.stop();

        saniye = 0;
        milisaniyeSayaci = 0;
        dakika = 0;
        sure.setText(String.format("Süre: %02d:%02d", dakika, saniye));

        yılan = new Yılan();

        toplamCilek = 0;
        skor.setText("Skor: " + toplamCilek);

        mevcutHız = 225;
        timer.setDelay(mevcutHız);

        cilekÜret();
        ciz();

        üstPanel.setVisible(true);

        CardLayout cl = (CardLayout) merkezPanel.getLayout();
        cl.show(merkezPanel, "OYUN");

        timer.start();
        panel.requestFocusInWindow();

    }

    void hızGüncelle() {

        if (toplamCilek > 15 && mevcutHız != 75) {
            mevcutHız = 85;
            timer.setDelay(mevcutHız);
        } else if (toplamCilek >= 8 && mevcutHız != 100) {
            mevcutHız = 100;
            timer.setDelay(mevcutHız);
        }
    }

    void oyunBitti() {
        skoruDosyayaYaz(kullanıcıAd, toplamCilek);

        skorTablosuVeri.putIfAbsent(kullanıcıAd, new ArrayList<>());
        skorTablosuVeri.get(kullanıcıAd).add(toplamCilek);
        skorTablosunuGuncelle();

        üstPanel.setVisible(false);
        CardLayout cl = (CardLayout) merkezPanel.getLayout();
        cl.show(merkezPanel, "MENU");

        frame.revalidate();
        frame.repaint();
    }

    boolean kendineCarpTiMi() {
        Point bas = yılan.getBas();
        for (int i = 1; i < yılan.getGovde().size(); i++) {
            Point gövde = yılan.getGovde().get(i);

            if (bas.x == gövde.x && bas.y == gövde.y) {
                return true;
            }
        }

        return false;
    }

    void skorTablosunuGuncelle() {
        tabloModel.setRowCount(0);

        for (Map.Entry<String, List<Integer>> entry : skorTablosuVeri.entrySet()) {
            String isim = entry.getKey();
            for (int skor : entry.getValue()) {
                tabloModel.addRow(new Object[] { isim, skor });
            }
        }
    }

    void skoruDosyayaYaz(String isim, int skor) {
        File file = new File("skorlar.txt");

        try (BufferedWriter w = new BufferedWriter(new FileWriter(file, true))) {
            w.write(isim + ":" + skor);
            w.newLine();
        } catch (IOException e) {
            System.out.println("Skor kaydedilmedi!");

        }
    }

    void skorlarıDosyadanOku() {

        File file = new File("skorlar.txt");
        if (!file.exists())
            return; // dosya yoksa çık

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue; // boş satır geç

                String[] parca = line.split(":");

                if (parca.length != 2)
                    continue; // bozuk satır geç

                String isim = parca[0];
                int skor = Integer.parseInt(parca[1]);

                skorTablosuVeri.putIfAbsent(isim, new ArrayList<>());
                skorTablosuVeri.get(isim).add(skor);
            }
        } catch (IOException e) {
            System.out.println("Skor yüklenemedi!");
        }
    }

    void skorlariTemizle() {
        skorTablosuVeri.clear();
        skorTablosunuGuncelle();

        File file = new File("skorlar.txt");

        try (BufferedWriter w = new BufferedWriter(new FileWriter(file, false))) {
            w.write(""); // Dosyanın içini boşaltır
        } catch (IOException e) {
            System.out.println("Skorlar silinirken hata oluştu!");
        }

        JOptionPane.showMessageDialog(frame, "Tüm skorlar temizlendi.");

    }

}
