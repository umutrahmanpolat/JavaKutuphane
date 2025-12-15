# 📚 Kütüphane Yönetim Sistemi (Java Swing & MySQL)

## Proje Tanımı

Bu uygulama, bir kütüphane içindeki envanter yönetimini, kullanıcı rollerini ve operasyonel süreçleri yönetmek üzere tasarlanmıştır. Proje, temiz, sürdürülebilir ve esnek bir mimari sağlamak amacıyla **altı temel Tasarım Deseni (Design Patterns)** kullanılarak **Nesne Yönelimli Programlama (OOP)** prensiplerine uygun olarak geliştirilmiştir.

Sistem, ödünç/iade, dinamik ceza hesaplama, stok durumuna göre otomatik durum yönetimi ve kullanıcının tercihine göre adapte olan bir bildirim sistemi sunar.

## 👥 Ekip Arkadaşları / Katılımcılar

Bu proje, aşağıdaki ekip üyeleri tarafından ortaklaşa gerçekleştirilmiştir.

| Ad Soyad              | GitHub Profili |
|:----------------------|  :--- |
| **Resül Güngör**      |  [GitHub Profili](https://github.com/Resul-Gungor) |
| **Umut Rahman Polat** |  [GitHub Profili](https://github.com/umutrahmanpolat) |
| **Sezer Özlem**       |  [GitHub Profili](https://github.com/sezerozlem) |

---

## 🚀 Proje Mimarisi ve Paket Sorumlulukları

Proje, Sorumlulukların Ayrılması (Separation of Concerns) prensibine uyarak modüler bir paket yapısına sahiptir.

| Paket Adı | Sorumluluk Alanı | Uygulanan Ana Desenler |
| :--- | :--- | :--- |
| `tasarimdesenleri.singleton` | Veritabanı Bağlantı Yönetimi | Singleton |
| `tasarimdesenleri.builder` | Nesne Oluşturma Sürecini Yönetme | Builder |
| `tasarimdesenleri.factory` | Kullanıcı Rolüne Göre Ekran Üretimi | Factory |
| `tasarimdesenleri.state` | Kitap Durumuna Göre Davranış Değişimi | State |
| `tasarimdesenleri.strategy` | Bildirim Gönderme Yöntemi Seçimi | Strategy |
| `tasarimdesenleri.observer` | Stok Değişiminde Otomatik Bildirim | Observer |
| `veritabani` | İş Mantığı (Business Logic) ve CRUD İşlemleri | |
| `modeller` | Veri Nesneleri ve Veri Yapısı | |

## 💡 Uygulanan Tasarım Desenleri (6 Adet Detaylı İnceleme)

### I. Yaratımsal Desenler (Creational Patterns)

#### 1. Singleton Pattern
* **Amaç:** Uygulama genelinde veritabanı bağlantısının (kaynak yönetimi) tek bir nesne tarafından yönetilmesini garanti etmek.
* **Konum:** `tasarimdesenleri.singleton.VeritabaniBaglantisi.java`

#### 2. Builder Pattern
* **Amaç:** Çok sayıda parametreye sahip `Kitap` nesnesinin yapılandırılmasını daha temiz ve okunaklı hale getirmek (Method Chaining).
* **Konum:** `tasarimdesenleri.builder.KitapBuilder.java`

### II. Yapısal ve Davranışsal Desenler

#### 3. State Pattern
* **Amaç:** `Kitap` nesnesinin stok durumuna göre (Rafta / Ödünçte) davranışını otomatik olarak değiştirmek.
* **Konum:** `tasarimdesenleri.state` paketi (`RaftaState`, `OduncteState`).
* **Etkileşim:** `Kitap.oduncVerilebilirMi()` metodu, kararı kendi içindeki `KitapState` nesnesine devrederek karmaşık `if-else` yapısını ortadan kaldırır.

#### 4. Strategy Pattern
* **Amaç:** Kullanıcının bildirim tercihine (SMS, E-Posta, Uygulama) göre farklı gönderim algoritmalarını çalışma zamanında seçmek.
* **Konum:** `tasarimdesenleri.strategy` paketi.
* **Etkileşim:** `BildirimServisi`, kullanıcının tercihini okur ve buna uygun Strategy sınıfını (`SmsBildirimStrategy` vb.) çalıştırır.

#### 5. Observer Pattern
* **Amaç:** Bir kitabın stoğu arttığında (Yayıncı), o kitabı rezerve eden tüm üyeleri (Gözlemciler) otomatik olarak bilgilendirmek.
* **Konum:** `tasarimdesenleri.observer.RezervasyonYoneticisi.java`
* **Etkileşim:** `KutuphaneIslemleri.kitapIadeAl` metodu çağrıldığında, `RezervasyonYoneticisi` tetiklenir ve bekleyen tüm rezervasyon sahiplerine bildirim gönderir.

#### 6. Factory Pattern
* **Amaç:** Giriş yapan kullanıcının rolüne göre (Yönetici, Personel, Üye) doğru ana ekranı oluşturma mantığını merkezileştirmek.
* **Konum:** `tasarimdesenleri.factory.EkranFactory.java`

---

## 🛠️ Kurulum ve Test (Çalıştırma Adımları)

### 1. Veritabanı Kurulumu

1.  **MySQL/MariaDB** sunucunuzu çalıştırın.
2.  **`kutuphane`** adında yeni bir veritabanı oluşturun.
3.  Projenin kaynak kodlarıyla birlikte verilen **SQL Scripti'ni** çalıştırarak tüm tablo ve test verilerini yükleyin.
4.  `tasarimdesenleri/singleton/VeritabaniBaglantisi.java` dosyasındaki `USER` ve `PASSWORD` bilgilerinizi güncelleyin.

### 2. Proje Bağımlılıkları

1.  **MySQL Connector/J** JAR dosyasını indirin.
2.  IDE'nizde (IntelliJ IDEA) Proje Yapısı (Project Structure) menüsünden bu JAR dosyasını kütüphane olarak projenize ekleyin.

### 3. Projeyi Çalıştırma

* `Main.java` dosyasını çalıştırın.

## 🔑 Varsayılan Giriş Bilgileri (Test Senaryoları İçin)

| Rol | Kullanıcı Adı | Şifre | Test Senaryosu |
| :--- | :--- | :--- | :--- |
| **Yönetici** | `admin` | `12345` | Sistem ayarları ve tüm yönetim işlemleri. |
| **Personel** | `personel` | `12345` | Ödünç/İade, Kullanıcı/Kitap Yönetimi. |
| **Cezalı Üye**| `uye_ayse` | `12345` | Gecikme Cezası ve Ödünç Limit Kontrolü. |
| **Rezerveci Üye**| `uye_zeynep` | `12345` | Observer Deseni (Rezervasyon Hazırlığı). |