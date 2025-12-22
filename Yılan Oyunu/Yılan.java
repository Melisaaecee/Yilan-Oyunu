import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Yılan {

     List<Point> yılan = new ArrayList<>();

     int yonX = 1; // yılan sağa doğru hareket ederek başlar
     int yonY = 0;

     public Yılan() {
          int baslangicX = 10;
          int baslangicY = 10;

          for (int i = 0; i < 3; i++) {
               yılan.add(new Point(baslangicX - i, baslangicY));
          }
     }

     Point getBas() {
          return yılan.get(0);
     }

     Point getKuyruk() {
          return yılan.get(yılan.size() - 1); // yılan boyutu 3 kuyruk index 2
     }

     List<Point> getGovde() {
          return yılan;
     }

     void hareketEt(boolean eat) {
          Point bas = getBas();
          Point yeniBas = new Point(bas.x + yonX, bas.y + yonY);
          yılan.add(0, yeniBas);

          if (!eat) {
               yılan.remove(yılan.size() - 1);
          }

     }

}
