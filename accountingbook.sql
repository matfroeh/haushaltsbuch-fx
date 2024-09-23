-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 02. Nov 2023 um 12:16
-- Server-Version: 10.4.28-MariaDB
-- PHP-Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `accountingbook`
--
CREATE DATABASE IF NOT EXISTS `accountingbook` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `accountingbook`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `accountingbook2023`
--

CREATE TABLE `accountingbook2023` (
  `id` int(11) NOT NULL,
  `type` text NOT NULL,
  `interval_of_reoccurrence` int(11) NOT NULL,
  `amount` double NOT NULL,
  `date` date NOT NULL,
  `category` text NOT NULL,
  `description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `accountingbook2023`
--

INSERT INTO `accountingbook2023` (`id`, `type`, `interval_of_reoccurrence`, `amount`, `date`, `category`, `description`) VALUES
(37, 'Ausgaben', 0, 31, '2023-10-05', 'Haushalt', 'Reinigungsmittel'),
(38, 'Wiederkehrende Einnahmen', 1, 2000, '2023-09-01', 'Gehalt', 'Oracle AG'),
(40, 'Ausgaben', 0, 129.89, '2023-09-09', 'Lebensmittel', 'Wochenendeinkauf'),
(41, 'Ausgaben', 0, 69.99, '2023-09-13', 'Kleidung', 'Schuhe'),
(43, 'Wiederkehrende Ausgaben', 1, 38, '2023-09-05', 'Haushalt', 'Telefon u. Internet'),
(44, 'Wiederkehrende Ausgaben', 3, 38.04, '2023-09-15', 'Versicherung', 'Rechtsschutz'),
(45, 'Einnahmen', 0, 6.01, '2023-09-15', 'Sonstiges', 'Pfandrückgaben'),
(51, 'Ausgaben', 0, 68.86, '2023-10-26', 'Lebensmittel', ''),
(54, 'Ausgaben', 0, 1333.56, '2023-10-31', 'Haushalt', ''),
(56, 'Ausgaben', 0, 12345, '2023-08-16', 'Lebensmittel', 'Testausgabe'),
(59, 'Ausgaben', 0, 76, '2023-01-01', 'Sonstiges', 'Taxi'),
(62, 'Wiederkehrende Einnahmen', 3, 4568.5, '2023-10-27', 'Gehalt', 'Urlaubsgeld'),
(76, 'Wiederkehrende Ausgaben', 3, 50, '2023-07-15', 'Haushalt', 'Waschmittel'),
(77, 'Wiederkehrende Ausgaben', 3, 50, '2023-10-15', 'Haushalt', 'Waschmittel'),
(82, 'Wiederkehrende Einnahmen', 1, 2000, '2023-10-01', 'Gehalt', 'Oracle AG'),
(84, 'Wiederkehrende Ausgaben', 1, 38, '2023-10-05', 'Haushalt', 'Telefon u. Internet'),
(90, 'Einnahmen', 0, 444.44, '2022-01-01', 'Gehalt', 'Test2022'),
(95, 'Ausgaben', 0, 500, '2023-11-01', 'Haushalt', 'Handwerker'),
(96, 'Ausgaben', 0, 38.35, '2023-11-01', 'Angelzubehör', 'Köder'),
(98, 'Ausgaben', 0, 123, '2021-11-12', 'Kleidung', 'testSortOrder'),
(100, 'Einnahmen', 0, 123, '2023-11-02', 'Gehalt', 'testSortOrder');

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `accountingbook2023`
--
ALTER TABLE `accountingbook2023`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `accountingbook2023`
--
ALTER TABLE `accountingbook2023`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=110;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
