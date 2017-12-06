-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Creato il: Lug 27, 2017 alle 15:50
-- Versione del server: 10.1.16-MariaDB
-- Versione PHP: 5.6.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `buyhub`
--
CREATE DATABASE IF NOT EXISTS `buyhub` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `buyhub`;

-- --------------------------------------------------------

--
-- Struttura della tabella `coordinates`
--
-- Creazione: Lug 27, 2017 alle 13:41
--

CREATE TABLE `coordinates` (
  `id` int(11) NOT NULL,
  `id_shop` int(11) NOT NULL,
  `latitude` decimal(10,6) NOT NULL,
  `longitude` decimal(10,6) NOT NULL,
  `address` text COLLATE utf8_bin NOT NULL,
  `opening_hours` text COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Struttura della tabella `messages`
--
-- Creazione: Lug 27, 2017 alle 13:28
--

CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `id_review` int(11) NOT NULL,
  `id_owner` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `validation_date` timestamp NULL DEFAULT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `id_validation` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Struttura della tabella `notifications`
--
-- Creazione: Lug 27, 2017 alle 13:28
--

CREATE TABLE `notifications` (
  `id` int(11) NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `date_creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(1) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Struttura della tabella `pictures`
--
-- Creazione: Lug 27, 2017 alle 13:28
--

CREATE TABLE `pictures` (
  `id` int(11) NOT NULL,
  `name` varchar(256) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `path` text COLLATE utf8_bin NOT NULL,
  `id_owner` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Struttura della tabella `pictures_products`
--
-- Creazione: Lug 27, 2017 alle 13:28
--

CREATE TABLE `pictures_products` (
  `id_product` int(11) NOT NULL,
  `id_picture` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Struttura della tabella `pictures_reviews`
--
-- Creazione: Lug 27, 2017 alle 13:28
--

CREATE TABLE `pictures_reviews` (
  `id_picture` int(11) NOT NULL,
  `id_review` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Struttura della tabella `pictures_shops`
--
-- Creazione: Lug 27, 2017 alle 13:28
--

CREATE TABLE `pictures_shops` (
  `id_shop` int(11) NOT NULL,
  `id_picture` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Struttura della tabella `products`
--
-- Creazione: Lug 27, 2017 alle 13:28
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(256) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `id_shop` int(11) NOT NULL,
  `category` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Struttura della tabella `reviews`
--
-- Creazione: Lug 27, 2017 alle 13:28
--

CREATE TABLE `reviews` (
  `id` int(11) NOT NULL,
  `id_product` int(11) NOT NULL,
  `id_creator` int(11) NOT NULL,
  `global_value` int(11) NOT NULL,
  `quality` int(11) NOT NULL,
  `service` int(11) NOT NULL,
  `value_for_money` int(11) NOT NULL,
  `title` varchar(256) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `date_creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Struttura della tabella `shops`
--
-- Creazione: Lug 27, 2017 alle 13:28
--

CREATE TABLE `shops` (
  `id` int(11) NOT NULL,
  `name` varchar(256) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `website` varchar(256) COLLATE utf8_bin NOT NULL,
  `id_owner` int(11) NOT NULL,
  `shipment` text COLLATE utf8_bin,
	`validity` int(11) NOT NULL DEFAULT 0,
  `shipment_costs` decimal(10,2) DEFAULT 0,
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Struttura della tabella `users`
--
-- Creazione: Lug 27, 2017 alle 13:28
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(128) COLLATE utf8_bin NOT NULL,
  `password` char(32) COLLATE utf8_bin NOT NULL,
  `first_name` varchar(256) COLLATE utf8_bin NOT NULL,
  `last_name` varchar(256) COLLATE utf8_bin NOT NULL,
  `email` varchar(128) COLLATE utf8_bin NOT NULL,
  `capability` int(11) NOT NULL DEFAULT '0',
  `avatar` text COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `coordinates`
--
ALTER TABLE `coordinates`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_shop` (`id_shop`);

--
-- Indici per le tabelle `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_owner` (`id_owner`),
  ADD KEY `id_review` (`id_review`),
  ADD KEY `id_validation` (`id_validation`);

--
-- Indici per le tabelle `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_user` (`id_user`);

--
-- Indici per le tabelle `pictures`
--
ALTER TABLE `pictures`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pictures_ibfk_2` (`id_owner`);

--
-- Indici per le tabelle `pictures_products`
--
ALTER TABLE `pictures_products`
  ADD PRIMARY KEY (`id_product`,`id_picture`),
  ADD KEY `id_picture` (`id_picture`);

--
-- Indici per le tabelle `pictures_reviews`
--
ALTER TABLE `pictures_reviews`
  ADD PRIMARY KEY (`id_picture`,`id_review`),
  ADD KEY `id_review` (`id_review`);

--
-- Indici per le tabelle `pictures_shops`
--
ALTER TABLE `pictures_shops`
  ADD PRIMARY KEY (`id_shop`,`id_picture`),
  ADD KEY `id_picture` (`id_picture`);

--
-- Indici per le tabelle `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_shop` (`id_shop`);

--
-- Indici per le tabelle `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_creator` (`id_creator`),
  ADD UNIQUE KEY `id_product_id_creator` (`id_product`, `id_creator`),
  ADD KEY `id_product` (`id_product`);

--
-- Indici per le tabelle `shops`
--
ALTER TABLE `shops`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_owner` (`id_owner`);

--
-- Indici per le tabelle `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `coordinates`
--
ALTER TABLE `coordinates`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `pictures`
--
ALTER TABLE `pictures`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `shops`
--
ALTER TABLE `shops`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la tabella `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `coordinates`
--
ALTER TABLE `coordinates`
  ADD CONSTRAINT `coordinates_ibfk_1` FOREIGN KEY (`id_shop`) REFERENCES `shops` (`id`);

--
-- Limiti per la tabella `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`id_owner`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`id_review`) REFERENCES `reviews` (`id`),
  ADD CONSTRAINT `messages_ibfk_3` FOREIGN KEY (`id_validation`) REFERENCES `users` (`id`);

--
-- Limiti per la tabella `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`);

--
-- Limiti per la tabella `pictures`
--
ALTER TABLE `pictures`
  ADD CONSTRAINT `pictures_ibfk_2` FOREIGN KEY (`id_owner`) REFERENCES `users` (`id`);

--
-- Limiti per la tabella `pictures_products`
--
ALTER TABLE `pictures_products`
  ADD CONSTRAINT `pictures_products_ibfk_1` FOREIGN KEY (`id_picture`) REFERENCES `pictures` (`id`),
  ADD CONSTRAINT `pictures_products_ibfk_2` FOREIGN KEY (`id_product`) REFERENCES `products` (`id`);

--
-- Limiti per la tabella `pictures_reviews`
--
ALTER TABLE `pictures_reviews`
  ADD CONSTRAINT `pictures_reviews_ibfk_1` FOREIGN KEY (`id_picture`) REFERENCES `pictures` (`id`),
  ADD CONSTRAINT `pictures_reviews_ibfk_2` FOREIGN KEY (`id_review`) REFERENCES `reviews` (`id`);

--
-- Limiti per la tabella `pictures_shops`
--
ALTER TABLE `pictures_shops`
  ADD CONSTRAINT `pictures_shops_ibfk_1` FOREIGN KEY (`id_picture`) REFERENCES `pictures` (`id`),
  ADD CONSTRAINT `pictures_shops_ibfk_2` FOREIGN KEY (`id_shop`) REFERENCES `shops` (`id`);

--
-- Limiti per la tabella `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`id_shop`) REFERENCES `shops` (`id`);

--
-- Limiti per la tabella `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`id_creator`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`id_product`) REFERENCES `products` (`id`);

--
-- Limiti per la tabella `shops`
--
ALTER TABLE `shops`
  ADD CONSTRAINT `shops_ibfk_2` FOREIGN KEY (`id_owner`) REFERENCES `users` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int(11),
  `shop_id` int(11),
  `shipment` text NOT NULL,
  `shipment_costs` decimal(10,2),
  `paid` int(1) DEFAULT 0,
  FOREIGN KEY (`user_id`) REFERENCES  `users` (`id`),
  FOREIGN KEY (`shop_id`) REFERENCES  `shops` (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `orders_products` (
    `order_id` int(11) NOT NULL AUTO_INCREMENT,
    `product_id` int(11) NOT NULL,
    `quantity` int(11),
    `price` decimal(10,2) NOT NULL,
    FOREIGN KEY (`order_id`) REFERENCES  `orders` (`id`),
    FOREIGN KEY (`product_id`) REFERENCES  `products` (`id`),
    PRIMARY KEY (`order_id`, `product_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
