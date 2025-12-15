-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1:3306
-- Üretim Zamanı: 12 Ara 2025, 19:52:16
-- Sunucu sürümü: 9.1.0
-- PHP Sürümü: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `kutuphane`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `bildirimler`
--

DROP TABLE IF EXISTS `bildirimler`;
CREATE TABLE IF NOT EXISTS `bildirimler` (
  `bildirim_id` int NOT NULL AUTO_INCREMENT,
  `kullanici_id` int NOT NULL,
  `mesaj` text NOT NULL,
  `tarih` datetime DEFAULT CURRENT_TIMESTAMP,
  `okundu_mu` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`bildirim_id`),
  KEY `kullanici_id` (`kullanici_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `kitaplar`
--

DROP TABLE IF EXISTS `kitaplar`;
CREATE TABLE IF NOT EXISTS `kitaplar` (
  `kitap_id` int NOT NULL AUTO_INCREMENT,
  `kitap_adi` varchar(200) COLLATE utf8mb3_turkish_ci NOT NULL,
  `yazar` varchar(100) COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `isbn` varchar(20) COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `kategori` varchar(50) COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `yayinevi` varchar(100) COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `baski_yili` int DEFAULT NULL,
  `stok_adedi` int DEFAULT '1',
  `raf_konumu` varchar(50) COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `durum` varchar(20) COLLATE utf8mb3_turkish_ci DEFAULT 'Rafta',
  PRIMARY KEY (`kitap_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;

--
-- Tablo döküm verisi `kitaplar`
--

INSERT INTO `kitaplar` (`kitap_id`, `kitap_adi`, `yazar`, `isbn`, `kategori`, `yayinevi`, `baski_yili`, `stok_adedi`, `raf_konumu`, `durum`) VALUES
(1, 'Sefiller', 'Victor Hugo', '9781234567890', 'Roman', 'İş Bankası', 2020, 5, 'A-101', 'Rafta'),
(2, 'Nutuk', 'Mustafa Kemal Atatürk', '9789876543210', 'Tarih', 'Yapı Kredi', 2019, 0, 'B-202', 'Rafta'),
(3, 'Harry Potter ve Felsefe Taşı', 'J.K. Rowling', '9784561237890', 'Fantastik', 'YKY', 2021, 2, 'C-303', 'Rafta'),
(4, 'Suç ve Ceza', 'Fyodor Dostoyevski', '9787894561230', 'Klasik', 'Can Yayınları', 2018, 0, 'A-102', 'Rafta'),
(5, 'Yüzüklerin Efendisi', 'J.R.R. Tolkien', '9781112223330', 'Fantastik', 'Metis', 1954, 0, 'C-304', 'Rafta'),
(6, 'Dönüşüm', 'Franz Kafka', '9785556667770', 'Modern', 'İletişim', 1915, 0, 'A-103', 'Rafta'),
(7, 'Küçük Prens', 'Antoine de Saint-Exupéry', '9781234567800', 'Çocuk', 'Can Yayınları', 1943, 8, 'Z-900', 'Rafta'),
(8, '1984', 'George Orwell', '9781234567811', 'Distopya', 'Can Yayınları', 1949, 10, 'D-401', 'Rafta'),
(9, 'Hayvan Çiftliği', 'George Orwell', '9781234567822', 'Distopya', 'Can Yayınları', 1945, 0, 'D-402', 'Rafta'),
(10, 'Sineklerin Tanrısı', 'William Golding', '9781234567833', 'Roman', 'İş Bankası', 1954, 3, 'A-104', 'Rafta'),
(11, 'Cesur Yeni Dünya', 'Aldous Huxley', '9781234567844', 'Distopya', 'İthaki', 1932, 4, 'D-403', 'Rafta'),
(12, 'Simyacı', 'Paulo Coelho', '9781234567855', 'Felsefe', 'Can Yayınları', 1988, 3, 'F-601', 'Rafta'),
(13, 'Satranç', 'Stefan Zweig', '9781234567866', 'Kısa Öykü', 'İş Bankası', 1942, 5, 'A-105', 'Rafta'),
(14, 'Kürk Mantolu Madonna', 'Sabahattin Ali', '9781234567877', 'Roman', 'Yapı Kredi', 1943, 7, 'B-203', 'Rafta'),
(15, 'Şeker Portakalı', 'José Mauro de Vasconcelos', '9781234567888', 'Çocuk', 'Can Yayınları', 1968, 9, 'Z-901', 'Rafta');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `kullanicilar`
--

DROP TABLE IF EXISTS `kullanicilar`;
CREATE TABLE IF NOT EXISTS `kullanicilar` (
  `kullanici_id` int NOT NULL AUTO_INCREMENT,
  `tc_kimlik_no` varchar(11) COLLATE utf8mb3_turkish_ci NOT NULL,
  `kullanici_adi` varchar(50) COLLATE utf8mb3_turkish_ci NOT NULL,
  `eposta` varchar(100) COLLATE utf8mb3_turkish_ci NOT NULL,
  `sifre` varchar(255) COLLATE utf8mb3_turkish_ci NOT NULL,
  `ad` varchar(50) COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `soyad` varchar(50) COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `telefon` varchar(20) COLLATE utf8mb3_turkish_ci DEFAULT NULL,
  `rol_id` int DEFAULT NULL,
  `kayit_tarihi` datetime DEFAULT CURRENT_TIMESTAMP,
  `bildirim_tercihi` varchar(20) COLLATE utf8mb3_turkish_ci DEFAULT 'Uygulama',
  PRIMARY KEY (`kullanici_id`),
  UNIQUE KEY `tc_kimlik_no` (`tc_kimlik_no`),
  UNIQUE KEY `kullanici_adi` (`kullanici_adi`),
  UNIQUE KEY `eposta` (`eposta`),
  KEY `rol_id` (`rol_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;

--
-- Tablo döküm verisi `kullanicilar`
--

INSERT INTO `kullanicilar` (`kullanici_id`, `tc_kimlik_no`, `kullanici_adi`, `eposta`, `sifre`, `ad`, `soyad`, `telefon`, `rol_id`, `kayit_tarihi`, `bildirim_tercihi`) VALUES
(1, '11111111111', 'admin', 'admin@kutuphane.com', '12345', 'Fatih', 'Yönetici', '5551111111', 1, '2025-12-12 22:51:30', 'Uygulama'),
(2, '22222222222', 'personel', 'personel@kutuphane.com', '12345', 'Ahmet', 'Personel', '5552222222', 2, '2025-12-12 22:51:30', 'Uygulama'),
(3, '33333333333', 'uye_ayse', 'ayse@mail.com', '12345', 'Ayşe', 'Cezalı', '5553333333', 3, '2025-12-12 22:51:30', 'E-Posta'),
(4, '44444444444', 'uye_mehmet', 'uye_mehmet@mail.com', '12345', 'Mehmet', 'Dolu', '5554444444', 3, '2025-12-12 22:51:30', 'Uygulama'),
(5, '55555555555', 'uye_zeynep', 'uye_zeynep@mail.com', '12345', 'Zeynep', 'Rezerveci', '5555555555', 3, '2025-12-12 22:51:30', 'Uygulama'),
(6, '66666666666', 'uye_can', 'uye_can@mail.com', '12345', 'Can', 'Yeni', '5556666666', 3, '2025-12-12 22:51:30', 'SMS'),
(7, '77777777777', 'ali.yilmaz', 'ali.yilmaz@mail.com', '12345', 'Ali', 'Yılmaz', '5557777777', 3, '2025-12-12 22:51:30', 'Uygulama'),
(8, '88888888888', 'buse.kaya', 'buse.kaya@mail.com', '12345', 'Buse', 'Kaya', '5558888888', 3, '2025-12-12 22:51:30', 'E-Posta'),
(9, '99999999999', 'deniz.celik', 'deniz.celik@mail.com', '12345', 'Deniz', 'Çelik', '5559999999', 3, '2025-12-12 22:51:30', 'SMS'),
(10, '10101010101', 'emre.ozturk', 'emre.ozturk@mail.com', '12345', 'Emre', 'Öztürk', '5550101010', 3, '2025-12-12 22:51:30', 'Uygulama'),
(11, '11111111110', 'gizem.aydin', 'gizem.aydin@mail.com', '12345', 'Gizem', 'Aydın', '5551111110', 3, '2025-12-12 22:51:30', 'Uygulama'),
(12, '12121212121', 'hakan.sahin', 'hakan.sahin@mail.com', '12345', 'Hakan', 'Şahin', '5551212121', 3, '2025-12-12 22:51:30', 'E-Posta'),
(13, '13131313131', 'irem.yildiz', 'irem.yildiz@mail.com', '12345', 'İrem', 'Yıldız', '5551313131', 3, '2025-12-12 22:51:30', 'SMS'),
(14, '14141414141', 'jale.eren', 'jale.eren@mail.com', '12345', 'Jale', 'Eren', '5551414141', 3, '2025-12-12 22:51:30', 'Uygulama'),
(15, '15151515151', 'kemal.dogan', 'kemal.dogan@mail.com', '12345', 'Kemal', 'Doğan', '5551515151', 3, '2025-12-12 22:51:30', 'Uygulama');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `oduncislemleri`
--

DROP TABLE IF EXISTS `oduncislemleri`;
CREATE TABLE IF NOT EXISTS `oduncislemleri` (
  `islem_id` int NOT NULL AUTO_INCREMENT,
  `kullanici_id` int DEFAULT NULL,
  `kitap_id` int DEFAULT NULL,
  `verilis_tarihi` date NOT NULL,
  `son_teslim_tarihi` date NOT NULL,
  `iade_tarihi` date DEFAULT NULL,
  `ceza_tutari` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`islem_id`),
  KEY `kullanici_id` (`kullanici_id`),
  KEY `kitap_id` (`kitap_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;

--
-- Tablo döküm verisi `oduncislemleri`
--

INSERT INTO `oduncislemleri` (`islem_id`, `kullanici_id`, `kitap_id`, `verilis_tarihi`, `son_teslim_tarihi`, `iade_tarihi`, `ceza_tutari`) VALUES
(1, 3, 4, '2025-11-17', '2025-12-02', NULL, 0.00),
(2, 4, 5, '2025-12-12', '2025-12-27', NULL, 0.00),
(3, 4, 6, '2025-12-12', '2025-12-27', NULL, 0.00),
(4, 4, 9, '2025-12-12', '2025-12-27', NULL, 0.00);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `rezervasyonlar`
--

DROP TABLE IF EXISTS `rezervasyonlar`;
CREATE TABLE IF NOT EXISTS `rezervasyonlar` (
  `rezervasyon_id` int NOT NULL AUTO_INCREMENT,
  `kullanici_id` int NOT NULL,
  `kitap_id` int NOT NULL,
  `rezervasyon_tarihi` datetime DEFAULT CURRENT_TIMESTAMP,
  `durum` varchar(20) DEFAULT 'Aktif',
  PRIMARY KEY (`rezervasyon_id`),
  KEY `kullanici_id` (`kullanici_id`),
  KEY `kitap_id` (`kitap_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Tablo döküm verisi `rezervasyonlar`
--

INSERT INTO `rezervasyonlar` (`rezervasyon_id`, `kullanici_id`, `kitap_id`, `rezervasyon_tarihi`, `durum`) VALUES
(1, 5, 2, '2025-12-10 22:51:31', 'Aktif'),
(2, 6, 3, '2025-12-11 22:51:31', 'Aktif');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `roller`
--

DROP TABLE IF EXISTS `roller`;
CREATE TABLE IF NOT EXISTS `roller` (
  `rol_id` int NOT NULL AUTO_INCREMENT,
  `rol_adi` varchar(50) COLLATE utf8mb3_turkish_ci NOT NULL,
  PRIMARY KEY (`rol_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;

--
-- Tablo döküm verisi `roller`
--

INSERT INTO `roller` (`rol_id`, `rol_adi`) VALUES
(1, 'Yönetici'),
(2, 'Personel'),
(3, 'Üye');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `sistemayarlari`
--

DROP TABLE IF EXISTS `sistemayarlari`;
CREATE TABLE IF NOT EXISTS `sistemayarlari` (
  `ayar_anahtari` varchar(50) COLLATE utf8mb3_turkish_ci NOT NULL,
  `ayar_degeri` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`ayar_anahtari`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_turkish_ci;

--
-- Tablo döküm verisi `sistemayarlari`
--

INSERT INTO `sistemayarlari` (`ayar_anahtari`, `ayar_degeri`) VALUES
('baslangic_cezasi_tl', 5.00),
('gunluk_ceza_tl', 2.00),
('max_kitap_sayisi', 3.00),
('odunc_suresi_gun', 15.00);

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `kullanicilar`
--
ALTER TABLE `kullanicilar`
  ADD CONSTRAINT `kullanicilar_ibfk_1` FOREIGN KEY (`rol_id`) REFERENCES `roller` (`rol_id`);

--
-- Tablo kısıtlamaları `oduncislemleri`
--
ALTER TABLE `oduncislemleri`
  ADD CONSTRAINT `oduncislemleri_ibfk_1` FOREIGN KEY (`kullanici_id`) REFERENCES `kullanicilar` (`kullanici_id`),
  ADD CONSTRAINT `oduncislemleri_ibfk_2` FOREIGN KEY (`kitap_id`) REFERENCES `kitaplar` (`kitap_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
