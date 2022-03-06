-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: dbHost
-- Creato il: Mar 04, 2022 alle 04:12
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

--
-- Dump dei dati per la tabella `classe`
--

INSERT INTO `classe` (`id`, `nome_classe`, `longitudine`, `latitudine`) VALUES
(1, '1A', 30, 30),
(2, '3A', 32, 32),
(3, '4A', 33, 33);

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

--
-- Dump dei dati per la tabella `docente`
--

INSERT INTO `docente` (`matricola`, `nome`, `cognome`, `user_id`, `password`) VALUES
(1, 'Valerio', 'Conte', 'user1', '*668425423DB5193AF921380129F465A6425216D0'),
(2, 'Giuseppe', 'Garibaldi', 'user2', '*DC52755F3C09F5923046BD42AFA76BD1D80DF2E9'),
(3, 'Antonio', 'Seppia', 'user3', '*40C3E7D386A2FADBDF69ACEBE7AA4DC3C723D798'),
(4, 'Carlo', 'Maria', 'user4', '*F97AEB38B3275C06D822FC9341A2151642C81988');

-- --------------------------------------------------------

--
-- Struttura della tabella `indicatore`
--

CREATE TABLE `indicatore` (
  `id` bigint(20) NOT NULL,
  `nome_indicatore` varchar(255) NOT NULL,
  `id_insegnamento` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `indicatore`
--

INSERT INTO `indicatore` (`id`, `nome_indicatore`, `id_insegnamento`) VALUES
(1, 'Indicatore1', 1),
(5, 'Indicatore1', 2),
(13, 'Indicatore1', 3),
(2, 'Indicatore2', 1),
(6, 'Indicatore2', 2),
(14, 'Indicatore2', 3),
(3, 'Indicatore3', 1),
(7, 'Indicatore3', 2),
(15, 'Indicatore3', 3),
(4, 'Indicatore4', 1),
(8, 'Indicatore4', 2),
(16, 'Indicatore4', 3);

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

--
-- Dump dei dati per la tabella `insegnamento`
--

INSERT INTO `insegnamento` (`id`, `id_classe`, `id_docente`, `nome_insegnamento`) VALUES
(1, 1, 1, 'Italiano'),
(2, 1, 1, 'Storia'),
(3, 1, 2, 'Geografia'),
(4, 1, 2, 'Scienze'),
(5, 1, 3, 'Matematica'),
(6, 2, 1, 'Italiano'),
(7, 2, 1, 'Storia'),
(8, 2, 2, 'Geografia'),
(9, 2, 2, 'Scienze'),
(11, 2, 3, 'Chimica'),
(10, 2, 4, 'Matematica'),
(15, 3, 1, 'Geografia'),
(13, 3, 1, 'Italiano'),
(14, 3, 1, 'Storia'),
(12, 3, 4, 'Matematica'),
(16, 3, 4, 'Scienze');

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

--
-- Dump dei dati per la tabella `studente`
--

INSERT INTO `studente` (`matricola`, `nome`, `cognome`, `id_classe`) VALUES
(1, 'Marco', 'Speranza', 1),
(2, 'Mario', 'Rossi', 1),
(3, 'Gianluca', 'Trivoli', 1),
(4, 'Salvo', 'Rapisardi', 1);

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
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT per la tabella `docente`
--
ALTER TABLE `docente`
  MODIFY `matricola` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT per la tabella `indicatore`
--
ALTER TABLE `indicatore`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT per la tabella `insegnamento`
--
ALTER TABLE `insegnamento`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT per la tabella `studente`
--
ALTER TABLE `studente`
  MODIFY `matricola` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

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
