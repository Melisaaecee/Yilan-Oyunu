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

public class SkorFileManager {
    
    File file = new File("skorlar.txt");
    Map<String, List<Integer>> skorTablosuVeri = new HashMap<>();

    Map<String, List<Integer>> skorlarıDosyadanOku() {

        if (!file.exists())
            return skorTablosuVeri; // dosya yoksa çık

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
        }
        return skorTablosuVeri;
    }

    void skoruDosyayaYaz(String isim, int skor) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(file, true))) {
            w.write(isim + ":" + skor);
            w.newLine();
        } catch (IOException e) {
            System.out.println("Skor kaydedilmedi!");

        }
    }

    void skorlariTemizle() {
        skorTablosuVeri.clear();

        try (BufferedWriter w = new BufferedWriter(new FileWriter(file, false))) {
            w.write(""); // Dosyanın içini boşaltır
        } catch (IOException e) {
            System.out.println("Skorlar silinirken hata oluştu!");
        }
    }
}
