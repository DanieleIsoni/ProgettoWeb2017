SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `buyhub`
--
CREATE DATABASE IF NOT EXISTS `buyhub` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `buyhub`;

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

ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);


ALTER TABLE `users`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

CREATE TABLE `shops` (
    `id` int(11) NOT NULL,
    `name` varchar(256) COLLATE utf8_bin NOT NULL,
    `description` text COLLATE utf8_bin NOT NULL,
    `website` varchar(256) COLLATE utf8_bin NOT NULL,
    `id_owner` int(11) NOT NULL,
    `shipment` text COLLATE utf8_bin,
    `validity` int(11) NOT NULL DEFAULT '0',
    `shipment_costs` decimal(10,2) DEFAULT '0.00'
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE `shops`
    ADD PRIMARY KEY (`id`),
    ADD KEY `id_owner` (`id_owner`),
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1,
    ADD CONSTRAINT `shops_ibfk_2` FOREIGN KEY (`id_owner`) REFERENCES `users` (`id`);




CREATE TABLE `coordinates` (
  `id` int(11) NOT NULL,
  `id_shop` int(11) NOT NULL,
  `latitude` decimal(10,6) NOT NULL,
  `longitude` decimal(10,6) NOT NULL,
  `address` text COLLATE utf8_bin NOT NULL,
  `opening_hours` text COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE `coordinates`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_shop` (`id_shop`),
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1,
  ADD CONSTRAINT `coordinates_ibfk_1` FOREIGN KEY (`id_shop`) REFERENCES `shops` (`id`);




  CREATE TABLE `orders` (
    `id` int(11) NOT NULL,
    `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id` int(11) DEFAULT NULL,
    `shop_id` int(11) DEFAULT NULL,
    `shipment` text COLLATE utf8_bin NOT NULL,
    `shipment_costs` decimal(10,2) DEFAULT NULL,
    `paid` int(1) DEFAULT '0'
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

  ALTER TABLE `orders`
    ADD PRIMARY KEY (`id`),
    ADD KEY `user_id` (`user_id`),
    ADD KEY `shop_id` (`shop_id`),
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3,
    ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`);



    CREATE TABLE `tickets` (
      `id` int(11) NOT NULL,
      `order_id` int(11) NOT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=latin1;


    ALTER TABLE `tickets`
      ADD PRIMARY KEY (`id`),
      ADD KEY `order_id` (`order_id`),
      MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
      ADD CONSTRAINT `tickets_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);



CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `ticket_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `content` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ticket_id` (`ticket_id`),
  ADD KEY `user_id` (`user_id`),
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`),
  ADD CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

CREATE TABLE `notifications` (
  `id` int(11) NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `date_creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(1) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_user` (`id_user`),
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`);



  CREATE TABLE `products` (
    `id` int(11) NOT NULL,
    `name` varchar(256) COLLATE utf8_bin NOT NULL,
    `description` text COLLATE utf8_bin NOT NULL,
    `price` decimal(10,2) NOT NULL,
    `id_shop` int(11) NOT NULL,
    `category` int(11) NOT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

  ALTER TABLE `products`
    ADD PRIMARY KEY (`id`),
    ADD KEY `id_shop` (`id_shop`),
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1,
    ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`id_shop`) REFERENCES `shops` (`id`);



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

  ALTER TABLE `reviews`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `id_product_id_creator` (`id_product`,`id_creator`),
    ADD KEY `id_creator` (`id_creator`),
    ADD KEY `id_product` (`id_product`),
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2,
    ADD CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`id_creator`) REFERENCES `users` (`id`),
    ADD CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`id_product`) REFERENCES `products` (`id`);




CREATE TABLE `orders_products` (
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


ALTER TABLE `orders_products`
  ADD PRIMARY KEY (`order_id`,`product_id`),
  ADD KEY `product_id` (`product_id`),
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1,
  ADD CONSTRAINT `orders_products_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `orders_products_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

CREATE TABLE `pictures` (
  `id` int(11) NOT NULL,
  `name` varchar(256) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `path` text COLLATE utf8_bin NOT NULL,
  `id_owner` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE `pictures`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pictures_ibfk_2` (`id_owner`),
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1,
  ADD CONSTRAINT `pictures_ibfk_2` FOREIGN KEY (`id_owner`) REFERENCES `users` (`id`);


CREATE TABLE `pictures_products` (
  `id_product` int(11) NOT NULL,
  `id_picture` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE `pictures_products`
  ADD PRIMARY KEY (`id_product`,`id_picture`),
  ADD KEY `id_picture` (`id_picture`),
  ADD CONSTRAINT `pictures_products_ibfk_1` FOREIGN KEY (`id_picture`) REFERENCES `pictures` (`id`),
  ADD CONSTRAINT `pictures_products_ibfk_2` FOREIGN KEY (`id_product`) REFERENCES `products` (`id`);


CREATE TABLE `pictures_reviews` (
  `id_picture` int(11) NOT NULL,
  `id_review` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE `pictures_reviews`
  ADD PRIMARY KEY (`id_picture`,`id_review`),
  ADD KEY `id_review` (`id_review`),
  ADD CONSTRAINT `pictures_reviews_ibfk_1` FOREIGN KEY (`id_picture`) REFERENCES `pictures` (`id`),
  ADD CONSTRAINT `pictures_reviews_ibfk_2` FOREIGN KEY (`id_review`) REFERENCES `reviews` (`id`);

CREATE TABLE `pictures_shops` (
  `id_shop` int(11) NOT NULL,
  `id_picture` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE `pictures_shops`
  ADD PRIMARY KEY (`id_shop`,`id_picture`),
  ADD KEY `id_picture` (`id_picture`),
  ADD CONSTRAINT `pictures_shops_ibfk_1` FOREIGN KEY (`id_picture`) REFERENCES `pictures` (`id`),
  ADD CONSTRAINT `pictures_shops_ibfk_2` FOREIGN KEY (`id_shop`) REFERENCES `shops` (`id`);
