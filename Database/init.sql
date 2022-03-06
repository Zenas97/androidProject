GRANT ALL PRIVILEGES ON *.* TO 'admin'@'%';
FLUSH PRIVILEGES;

-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: dbHost
-- Creato il: Mar 02, 2022 alle 02:16
-- Versione del server: 10.4.20-MariaDB-1:10.4.20+maria~focal
-- Versione PHP: 7.4.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `android-project`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `classe`
--

CREATE TABLE `classe` (
  `id` bigint(20) NOT NULL,
  `nome_classe` varchar(255) NOT NULL,
  `longitudine` double DEFAULT NULL,
  `latitudine` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `docente`
--

CREATE TABLE `docente` (
  `matricola` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `cognome` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `indicatore`
--

CREATE TABLE `indicatore` (
  `id` bigint(20) NOT NULL,
  `nome_indicatore` varchar(255) NOT NULL,
  `id_insegnamento` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `insegnamento`
--

CREATE TABLE `insegnamento` (
  `id` bigint(20) NOT NULL,
  `id_classe` bigint(20) NOT NULL,
  `id_docente` bigint(20) NOT NULL,
  `nome_insegnamento` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `studente`
--

CREATE TABLE `studente` (
  `matricola` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `cognome` varchar(255) NOT NULL,
  `id_classe` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `valori_valutazione`
--

CREATE TABLE `valori_valutazione` (
  `id_valutazione` bigint(20) NOT NULL,
  `id_indicatore` bigint(20) NOT NULL,
  `valore` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `valutazione`
--

CREATE TABLE `valutazione` (
  `id` bigint(20) NOT NULL,
  `id_insegnamento` bigint(20) NOT NULL,
  `id_studente` bigint(20) NOT NULL,
  `quadrimestre` enum('Primo','Secondo') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `classe`
--
ALTER TABLE `classe`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `docente`
--
ALTER TABLE `docente`
  ADD PRIMARY KEY (`matricola`);

--
-- Indici per le tabelle `indicatore`
--
ALTER TABLE `indicatore`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nome_indicatore` (`nome_indicatore`,`id_insegnamento`),
  ADD KEY `id_insegnamento` (`id_insegnamento`);

--
-- Indici per le tabelle `insegnamento`
--
ALTER TABLE `insegnamento`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_classe` (`id_classe`,`id_docente`,`nome_insegnamento`),
  ADD KEY `id_docente` (`id_docente`);

--
-- Indici per le tabelle `studente`
--
ALTER TABLE `studente`
  ADD PRIMARY KEY (`matricola`),
  ADD KEY `id_classe` (`id_classe`);

--
-- Indici per le tabelle `valori_valutazione`
--
ALTER TABLE `valori_valutazione`
  ADD PRIMARY KEY (`id_valutazione`,`id_indicatore`),
  ADD KEY `id_indicatore` (`id_indicatore`);

--
-- Indici per le tabelle `valutazione`
--
ALTER TABLE `valutazione`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_insegnamento` (`id_insegnamento`,`id_studente`,`quadrimestre`),
  ADD KEY `id_studente` (`id_studente`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `classe`
--
ALTER TABLE `classe`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `docente`
--
ALTER TABLE `docente`
  MODIFY `matricola` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `indicatore`
--
ALTER TABLE `indicatore`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `insegnamento`
--
ALTER TABLE `insegnamento`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `studente`
--
ALTER TABLE `studente`
  MODIFY `matricola` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `valutazione`
--
ALTER TABLE `valutazione`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `indicatore`
--
ALTER TABLE `indicatore`
  ADD CONSTRAINT `indicatore_ibfk_1` FOREIGN KEY (`id_insegnamento`) REFERENCES `insegnamento` (`id`);

--
-- Limiti per la tabella `insegnamento`
--
ALTER TABLE `insegnamento`
  ADD CONSTRAINT `insegnamento_ibfk_1` FOREIGN KEY (`id_classe`) REFERENCES `classe` (`id`),
  ADD CONSTRAINT `insegnamento_ibfk_2` FOREIGN KEY (`id_docente`) REFERENCES `docente` (`matricola`);

--
-- Limiti per la tabella `studente`
--
ALTER TABLE `studente`
  ADD CONSTRAINT `studente_ibfk_1` FOREIGN KEY (`id_classe`) REFERENCES `classe` (`id`);

--
-- Limiti per la tabella `valori_valutazione`
--
ALTER TABLE `valori_valutazione`
  ADD CONSTRAINT `valori_valutazione_ibfk_1` FOREIGN KEY (`id_indicatore`) REFERENCES `indicatore` (`id`),
  ADD CONSTRAINT `valori_valutazione_ibfk_2` FOREIGN KEY (`id_valutazione`) REFERENCES `valutazione` (`id`);

--
-- Limiti per la tabella `valutazione`
--
ALTER TABLE `valutazione`
  ADD CONSTRAINT `valutazione_ibfk_1` FOREIGN KEY (`id_insegnamento`) REFERENCES `insegnamento` (`id`),
  ADD CONSTRAINT `valutazione_ibfk_2` FOREIGN KEY (`id_studente`) REFERENCES `studente` (`matricola`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
