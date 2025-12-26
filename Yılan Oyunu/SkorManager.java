import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

public class SkorManager {

    public final SkorFileManager fileManager = new SkorFileManager();
    Map<String, List<Integer>> skorTablosuVeri;

    public SkorManager() {
        skorTablosuVeri = fileManager.skorlarıDosyadanOku();
    }

    public void skorEkle(String isim, int skor) {
        skorTablosuVeri.putIfAbsent(isim, new ArrayList<>());
        skorTablosuVeri.get(isim).add(skor);
        fileManager.skoruDosyayaYaz(isim, skor);
    }

   void skorTablosunuGuncelle(DefaultTableModel tabloModel) {
        tabloModel.setRowCount(0);

        for (Map.Entry<String, List<Integer>> entry : skorTablosuVeri.entrySet()) {
            String isim = entry.getKey();
            for (int skor : entry.getValue()) {
                tabloModel.addRow(new Object[] { isim, skor });
            }
        }
    }

 


}
