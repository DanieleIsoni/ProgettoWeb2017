INSERT INTO `users` (`id`, `username`, `password`, `first_name`, `last_name`, `email`, `capability`) VALUES
(1, 'frossi', '5f4dcc3b5aa765d61d8327deb882cf99', 'Franco', 'Rossi', 'franco@rossi.com', 0),
(2, 'MFederico', '5f4dcc3b5aa765d61d8327deb882cf99', 'Federico', 'Mariani', 'fmariani@orimariani.it', 0),
(3, 'imbotte45', '5f4dcc3b5aa765d61d8327deb882cf99', 'Andrea', 'Imbotte', 'imbotte.andrea@gmail.com', 0),
(4, 'bondop', '5f4dcc3b5aa765d61d8327deb882cf99', 'Paolo', 'Bondolin', 'bondop@gmail.com', 0),
(5, 'brutema', '5f4dcc3b5aa765d61d8327deb882cf99', 'Emanuele', 'Bruttin', 'brutema@gmail.com', 0);

INSERT INTO `pictures` (`id`, `name`, `description`, `path`, `id_owner`) VALUES
(1, 'StanleyTylon3m', 'Flessometro Stanley Tylon 3m', 'uploadedContent/7ae87bb4-0965-44bb-91f0-12b05c879da3.jpg', 1),
(2, 'StanleyTylon3m', 'Flessometro Stanley Tylon 3m', 'uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg', 1),
(3, 'Tartaruga Nuvolari', 'Tartaruga d’oro realizzata per l’occasione dal gioielliere milanese Mario Buccellati.', 'uploadedContent/95ff4467-0c93-4c92-adc5-65fddad91f7d.jpg', 2),
(4, 'Tartaruga Nuvolari su libro', 'Tartaruga d’oro realizzata per l’occasione dal gioielliere milanese Mario Buccellati.', 'uploadedContent/2cf9786b-7ae9-4b04-ad39-0a54a25a13ca.jpg', 2),
(5, 'Tartaruga Nuvolari su libro', 'Tartaruga d’oro realizzata per l’occasione dal gioielliere milanese Mario Buccellati.', 'uploadedContent/f8ebcd50-bc7e-456e-9278-3f6700730f99.jpg', 2);

INSERT INTO `shops` (`id`, `name`, `description`, `website`, `id_owner`, `id_creator`, `global_value`) VALUES
(1, 'Ferramenta del Corso', 'Antica ferramenta, aperta nel 1882, ancora oggi punto di riferimento per tutta la comunità di Thiene', 'http://www.ferramentadelcorso.com', 1, 1, 1),
(2, 'Orologeria Oreficeria Mariani', 'Negozio sito nel centro storico a Schio(VI) gestito da tre generazioni, prima dal nostro nonno Mariani Alfredo, maestro orologiaio, passato poi ai nostri genitori Mariani Bianca (maestra d\'ottica dipolomata alla scuola L.D.V. di Fiesole (Firenze) e Mariani Imerio (allievo del nonno Alfredo).\r\nTradizione e professionalità si uniscono per fornirvi i migliori prodotti al giusto prezzo.', 'http://www.orimariani.it', 2, 2, 0);

INSERT INTO `products` (`id`, `name`, `description`, `price`, `id_shop`, `category`) VALUES
(1, 'Flessometro Stanley Tylon 3m', 'Cassa in ABS antiurto, molto compatta. Rivestimento completo in gomma antiscivolo. Nastro di qualità professionale con verniciatura polimerizzata e rivestito con pellicola in mat.sintetico Tylon ™ (spessore 0,14 mm). Clip di aggancio.', '15.00', 1, 0),
(2, 'Spilla Tazio Nuvolari', 'Nel 1932 Il Vate Gabriele D’Annunzio incontra al Vittoriale il pilota Mantovano Tazio Nuvolari.\r\n\r\nOltre a foto ed elogi per le vittorie, fa dono a Nuvolari di una tartaruga d’oro realizzata per l’occasione dal gioielliere milanese Mario Buccellati.\r\n\r\nIl dono viene accompagnato da una frase paradossale: “All’uomo più veloce, l’animale più lento“.\r\nDivenne essa presto l’emblema, il simbolo del pilota Mantovano. Le Sue magliette riportavano sempre tale simbolo, cosi come la propria carta intestata recava in alto a sinistra una piccola tartaruga di colore rosso.\r\nQuesto oggetto più unico che raro, appartenuto per 50 anni al nostro caro amico Rossano Palozzi, è oggi pronto a trovare un nuovo proprietario. Un piccolo pezzo di leggenda destinato ad acquistare velocemente gran valore.', '20000.00', 2, 0);


INSERT INTO `reviews` (`id`, `id_product`, `id_creator`, `global_value`, `quality`, `service`, `value_for_money`, `title`, `description`, `date_creation`) VALUES
(1, 1, 3, 0, 5, 5, 5, 'Ottimo metro', 'Ottimo prodotto, ben realizzato, robusto, rivestito in gomma, nastro metallico abbastanza largo con numeri ben visibili, riesce a non flettersi fino a 90 cm di estensione dovendo effettuare una misurazione \"in aria\" senza poter agganciare il dentino iniziale.\r\nQuindi tutto bene e grazie alla Ferramenta del Corso ad un prezzo stracciato.', '2017-07-11 10:40:38'),
(2, 1, 5, 5, 5, 5, 5, 'Fantastico', 'Ottimo metro, ne ho acquistato uno per uso casalingo, come prodotto \"plus\"\r\n3 metri x 13mm di larghezza del nastro.\r\nIl metro è molto solido e con un sistema di scorrimento e raccolta molto fluido.\r\nIl rivestimento è molto piacevole al tatto e protegge il prodotto da eventuali urti.\r\nIl metro è provvisto di un gancetto per poterlo agganciare ad una cinta o una tasca ( che volendo puó essere rimosso ).', '2017-07-11 10:43:46'),
(3, 1, 4, 5, 4, 4, 5, 'La Ferrari dei metri', 'Ricoperto in gomma che ne facilita la presa è di dimensioni contenute non ostante i 3 metri disponibili. Il nastro è stretto e quindi non è in grado di autosostenersi se non per brevi lunghezze. Molto scorrevole è dotato di blocco . Non ha la finestrella per cui non è possibile effettuare misure \"interne\" ma direi che a questo prezzo se non si hanno esigenze \"professionali\" è un\'ottimo articolo.', '2017-07-11 10:44:58');



INSERT INTO `pictures_products` (`id_product`, `id_picture`) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4),
(2, 5);

INSERT INTO `coordinates` (`id`, `latitude`, `longitude`, `address`) VALUES
(1, '45.705959', '11.479617', 'Piazza Chilesotti, 17\r\n36016\r\nThiene (VI)\r\nItalia'),
(2, '45.714211', '11.359345', 'Via Camillo Benso Cavour 18\r\n36015 Schio (VI)\r\nItalia'),
(3, '45.688891', '11.605844', 'Via Maragnole 33,\r\n36042 Breganze\r\n(VI)'),
(4, '45.715733', '11.403583', 'Via Lago di Levico, 9\r\n36015 Schio (VI)');

INSERT INTO `shops_coordinates` (`id_shop`, `id_coordinate`) VALUES
(1, 1),
(1, 4),
(2, 2),
(1, 3);