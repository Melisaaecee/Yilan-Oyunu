import java.awt.Image;
import java.awt.Point;
import java.util.List;

import javax.swing.ImageIcon;

public class Yem {
    int cilekX, cilekY;
    ImageIcon yeniCilek;

    Yem() {
        ImageIcon cilek = new ImageIcon("cilek2.png");
        Image image = cilek.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        yeniCilek = new ImageIcon(image);
    }

    public void cilekÜret(int satır, int sütun, List<Point> yılanGövde) {

        boolean yılanınUstunde;
        do { // çileğin yılanın üstünde(gövdesinin herhangi bir yerinde) oluşmasını engelleme

            yılanınUstunde = false;
            cilekX = (int) (Math.random() * sütun);
            cilekY = (int) (Math.random() * satır);

            for (Point y : yılanGövde) { // yılanın gövdesinden bir parçasının çileğin x ve y koordinatına eşit
                                         // olup olmadığına bakarız
                if (y.x == cilekX && y.y == cilekY) {
                    yılanınUstunde = true;
                    break; // eğer bir parçası eşitse diğer parçalarına bakmaya gerek kalmaz, döngüyü
                           // burada sonlandırıp tekrardan çilek için yeni bir koordinat üretilmesini
                           // sağlar.
                }
            }

        } while (yılanınUstunde); // çilek yılanın üstünde ise döngünün başına gider

    }

    public ImageIcon getIcon() {
        return yeniCilek;
    }

    boolean cilekYendiMi( Point yılanBas) {
        if (yılanBas.x == cilekX && yılanBas.y == cilekY) {
            return true;
        }

        return false;
    }

}
