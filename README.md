# Yilan-Oyunu
Nesne Yönelimli Programlama Dersi Güz Dönemi Proje Ödevi

#  Java Swing Snake Game

Java'nın **Swing** ve **AWT** kütüphaneleri kullanılarak geliştirilmiş, Nesne Yönelimli Programlama (OOP) prensiplerini temel alan klasik bir yılan oyunu projesidir.

---

##  Proje Hakkında
Bu çalışma, bir oyunun sadece görselden ibaret olmadığını, arkasında karmaşık bir sınıf yapısı ve mantıksal ilişkiler barındırdığını göstermek amacıyla tasarlanmıştır. Proje; dinamik zorluk, kalıcı veri saklama ve modüler arayüz yönetimi özelliklerine sahiptir.

###  Temel Özellikler
* **Dinamik Zorluk:** Yılan yem yedikçe hızı kademeli olarak artar.
* **Kalıcı Skor Tablosu:** Skorlar `skorlar.txt` dosyasına kaydedilir, oyun kapansa da silinmez.
* **Modüler Arayüz:** `CardLayout` ile Menü, Oyun ve Skorlar ekranı arasında hızlı geçiş.
* **Kullanıcı Yönetimi:** Her oyun başında alınan isim bilgisi ile kişiselleştirilmiş skor takibi.

---

## Yazılım Mimarisi (Sınıf Yapısı)

Proje, sorumlulukların net ayrımı (Separation of Concerns) ilkesiyle 7 ana sınıfa bölünmüştür:

| Sınıf | Sorumluluk |
| :--- | :--- |
| **Yılanoyunu** | Ana motor; Timer kontrolü, çarpışma tespiti ve koordinasyon. |
| **Yılan** | Yılanın vücut noktaları (`List<Point>`) ve hareket matematiği. |
| **Yem** | Rastgele koordinatlarda yem üretimi ve yenme mantığı. |
| **PanelYönetimi** | Tüm GUI katmanlarının (Panel) organizasyonu. |
| **Button** | Kullanıcı etkileşimi ve buton aksiyonları (`ActionListener`). |
| **SkorManager** | Skor verilerinin mantıksal işlenmesi ve tabloya aktarımı. |
| **SkorFileManager** | Dosya okuma (Input) ve yazma (Output) işlemleri. |

---

## Kontroller ve Oynanış

Oyunu kontrol etmek için aşağıdaki tuşları kullanabilirsiniz:

* **Yukarı:** `Yukarı Ok`
* **Aşağı:** `Aşağı Ok`
* **Sol:** `Sol Ok`
* **Sağ:** `Sağ Ok`

**Oyun Kuralı:** Kenarlara veya kendi vücudunuza çarpmadan çilekleri toplayarak en yüksek puanı hedefleyin!

---

##  Kurulum ve Çalıştırma

1.  **Java Yükle:** Bilgisayarınızda JDK 8 veya üzeri bir sürümün yüklü olduğundan emin olun.
2.  **Derleme:** Terminal veya Komut İstemi üzerinden:
    ```bash
    javac Yılanoyunu.java
    ```
3.  **Çalıştırma:**
    ```bash
    java Yılanoyunu
    ```

---

##  Sınıf İlişkileri (UML Özeti)

* **Composition:** `Yılanoyunu` <>-- `Yılan`, `Yem`, `PanelYönetimi`.
* **Association:** `Button` --> `Yılanoyunu` (Oyun metodlarını tetikler).
* **Realization:** `KlavyeYoneticisi` ..|> `KeyListener`.

