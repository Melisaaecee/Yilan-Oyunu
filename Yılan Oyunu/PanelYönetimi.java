import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelYönetimi {

    JPanel panel;
    JPanel üstPanel;
    JPanel merkezPanel;
    JPanel skorPanel;
    JPanel menuPanel;
    Button butons;
    private static final int kare_boyut = 25;

    PanelYönetimi(int satır, int sütun, JLabel[][] tablo, Color color, Color color2, Color color3) {

        panel = new JPanel(new GridLayout(satır, sütun));
        üstPanel = new JPanel();

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

        menuPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, color3, w, h, color2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);
            }

        };

        skorPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, color3, w, h, color2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);
            }
        };

        // ------------MERKEZ PANEL-----------
        merkezPanel = new JPanel(new CardLayout());
        merkezPanel.add(panel, "OYUN");
        merkezPanel.add(skorPanel, "SKORLAR");
        merkezPanel.add(menuPanel, "MENU");

    }

    void show(String cardName) {
        CardLayout cl = (CardLayout) merkezPanel.getLayout();
        cl.show(merkezPanel, cardName);

    }
}
