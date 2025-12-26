import java.awt.Point;
import java.util.List;

public class KontrolSınıfı {

      boolean duvaraCarptiMi(Point bas,int satır, int sütun) {

        if (bas.x < 0 || bas.x >= sütun) { // sol duvarın kordinatı (0,0) olduğundan bas.x 0' dan küçük olamaz ve
                                           // sütunun dışına çıkamaz.
            return true; // çarptığı için true
        }

        if (bas.y < 0 || bas.y >= satır) {
            return true;
        }
        return false; // çarpmadığı için false
    }

    
    boolean kendineCarpTiMi(List<Point> yılanGövde) {
        
        for (int i = 1; i < yılanGövde.size(); i++) {
            Point bas = yılanGövde.get(0);
            Point gövde = yılanGövde.get(i);

            if (bas.x == gövde.x && bas.y == gövde.y) {
                return true;
            }
        }

        return false;
    }

}
