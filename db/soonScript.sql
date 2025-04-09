-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               11.7.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.10.0.7000
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for pujcovna
CREATE DATABASE IF NOT EXISTS `pujcovna` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `pujcovna`;

-- Dumping structure for table pujcovna.fyzycky_stav_vyb
DROP TABLE IF EXISTS `fyzycky_stav_vyb`;
CREATE TABLE IF NOT EXISTS `fyzycky_stav_vyb` (
  `ID_F_stavu` bigint(20) NOT NULL AUTO_INCREMENT,
  `popis` varchar(255) NOT NULL,
  PRIMARY KEY (`ID_F_stavu`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table pujcovna.fyzycky_stav_vyb: ~2 rows (approximately)
DELETE FROM `fyzycky_stav_vyb`;
INSERT INTO `fyzycky_stav_vyb` (`ID_F_stavu`, `popis`) VALUES
	(1, 'Nové'),
	(2, 'Použité'),
	(3, 'Poškozené');

-- Dumping structure for table pujcovna.kategorie_vyb
DROP TABLE IF EXISTS `kategorie_vyb`;
CREATE TABLE IF NOT EXISTS `kategorie_vyb` (
  `ID_kategorie` bigint(20) NOT NULL AUTO_INCREMENT,
  `nazev` varchar(255) NOT NULL,
  PRIMARY KEY (`ID_kategorie`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table pujcovna.kategorie_vyb: ~2 rows (approximately)
DELETE FROM `kategorie_vyb`;
INSERT INTO `kategorie_vyb` (`ID_kategorie`, `nazev`) VALUES
	(1, 'Lyže'),
	(2, 'Snowboardy'),
	(3, 'Helmy');

-- Dumping structure for table pujcovna.rezervace
DROP TABLE IF EXISTS `rezervace`;
CREATE TABLE IF NOT EXISTS `rezervace` (
  `ID_rezervace` bigint(20) NOT NULL AUTO_INCREMENT,
  `datum_rezervace` datetime NOT NULL,
  `datum_vyzvednuti` datetime DEFAULT NULL,
  `datum_vraceni` datetime DEFAULT NULL,
  `ID_zakaznika` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID_rezervace`),
  KEY `ID_zakaznika` (`ID_zakaznika`),
  CONSTRAINT `rezervace_ibfk_1` FOREIGN KEY (`ID_zakaznika`) REFERENCES `zakaznik` (`ID_zakaznika`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table pujcovna.rezervace: ~0 rows (approximately)
DELETE FROM `rezervace`;

-- Dumping structure for table pujcovna.rezervace_vybaveni
DROP TABLE IF EXISTS `rezervace_vybaveni`;
CREATE TABLE IF NOT EXISTS `rezervace_vybaveni` (
  `ID_rezervace` bigint(20) NOT NULL,
  `ID_vybaveni` bigint(20) NOT NULL,
  PRIMARY KEY (`ID_rezervace`,`ID_vybaveni`),
  KEY `ID_vybaveni` (`ID_vybaveni`),
  CONSTRAINT `rezervace_vybaveni_ibfk_1` FOREIGN KEY (`ID_rezervace`) REFERENCES `rezervace` (`ID_rezervace`),
  CONSTRAINT `rezervace_vybaveni_ibfk_2` FOREIGN KEY (`ID_vybaveni`) REFERENCES `vybaveni` (`ID_Vybaveni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table pujcovna.rezervace_vybaveni: ~0 rows (approximately)
DELETE FROM `rezervace_vybaveni`;

-- Dumping structure for table pujcovna.vybaveni
DROP TABLE IF EXISTS `vybaveni`;
CREATE TABLE IF NOT EXISTS `vybaveni` (
  `ID_Vybaveni` bigint(20) NOT NULL AUTO_INCREMENT,
  `nazev` varchar(255) NOT NULL,
  `popis` text DEFAULT NULL,
  `cena` decimal(10,2) NOT NULL,
  `zaloha` decimal(10,2) NOT NULL,
  `Fyzycky_stav_vyb_ID_F_stavu` bigint(20) DEFAULT NULL,
  `Kategorie_vyb_ID_kategorie` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID_Vybaveni`),
  KEY `Fyzycky_stav_vyb_ID_F_stavu` (`Fyzycky_stav_vyb_ID_F_stavu`),
  KEY `Kategorie_vyb_ID_kategorie` (`Kategorie_vyb_ID_kategorie`),
  CONSTRAINT `vybaveni_ibfk_1` FOREIGN KEY (`Fyzycky_stav_vyb_ID_F_stavu`) REFERENCES `fyzycky_stav_vyb` (`ID_F_stavu`),
  CONSTRAINT `vybaveni_ibfk_2` FOREIGN KEY (`Kategorie_vyb_ID_kategorie`) REFERENCES `kategorie_vyb` (`ID_kategorie`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table pujcovna.vybaveni: ~2 rows (approximately)
DELETE FROM `vybaveni`;
INSERT INTO `vybaveni` (`ID_Vybaveni`, `nazev`, `popis`, `cena`, `zaloha`, `Fyzycky_stav_vyb_ID_F_stavu`, `Kategorie_vyb_ID_kategorie`) VALUES
	(1, 'Scott', 'XL', 150.00, 1000.00, 2, 3),
	(2, 'Head', '110 Žluté', 300.00, 2000.00, 2, 1);

-- Dumping structure for table pujcovna.vypujcka
DROP TABLE IF EXISTS `vypujcka`;
CREATE TABLE IF NOT EXISTS `vypujcka` (
  `ID_vypujcka` bigint(20) NOT NULL AUTO_INCREMENT,
  `datum_vypujcky` datetime NOT NULL,
  `datum_vraceni` datetime DEFAULT NULL,
  `vraceno` tinyint(1) NOT NULL,
  `ID_zakaznika` bigint(20) DEFAULT NULL,
  `ID_zam` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID_vypujcka`),
  KEY `ID_zakaznika` (`ID_zakaznika`),
  KEY `ID_zam` (`ID_zam`),
  CONSTRAINT `vypujcka_ibfk_1` FOREIGN KEY (`ID_zakaznika`) REFERENCES `zakaznik` (`ID_zakaznika`),
  CONSTRAINT `vypujcka_ibfk_2` FOREIGN KEY (`ID_zam`) REFERENCES `zamestnanec` (`ID_zam`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table pujcovna.vypujcka: ~0 rows (approximately)
DELETE FROM `vypujcka`;

-- Dumping structure for table pujcovna.vypujcka_vybaveni
DROP TABLE IF EXISTS `vypujcka_vybaveni`;
CREATE TABLE IF NOT EXISTS `vypujcka_vybaveni` (
  `ID_vypujcka` bigint(20) NOT NULL,
  `ID_vybaveni` bigint(20) NOT NULL,
  PRIMARY KEY (`ID_vypujcka`,`ID_vybaveni`),
  KEY `ID_vybaveni` (`ID_vybaveni`),
  CONSTRAINT `vypujcka_vybaveni_ibfk_1` FOREIGN KEY (`ID_vypujcka`) REFERENCES `vypujcka` (`ID_vypujcka`),
  CONSTRAINT `vypujcka_vybaveni_ibfk_2` FOREIGN KEY (`ID_vybaveni`) REFERENCES `vybaveni` (`ID_Vybaveni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table pujcovna.vypujcka_vybaveni: ~0 rows (approximately)
DELETE FROM `vypujcka_vybaveni`;

-- Dumping structure for table pujcovna.zakaznik
DROP TABLE IF EXISTS `zakaznik`;
CREATE TABLE IF NOT EXISTS `zakaznik` (
  `ID_zakaznika` bigint(20) NOT NULL AUTO_INCREMENT,
  `jmeno` varchar(255) NOT NULL,
  `prijmeni` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `telefon` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID_zakaznika`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table pujcovna.zakaznik: ~0 rows (approximately)
DELETE FROM `zakaznik`;

-- Dumping structure for table pujcovna.zamestnanec
DROP TABLE IF EXISTS `zamestnanec`;
CREATE TABLE IF NOT EXISTS `zamestnanec` (
  `ID_zam` bigint(20) NOT NULL AUTO_INCREMENT,
  `jmeno` varchar(255) NOT NULL,
  `prijmeni` varchar(255) NOT NULL,
  `pozice` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID_zam`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table pujcovna.zamestnanec: ~0 rows (approximately)
DELETE FROM `zamestnanec`;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
