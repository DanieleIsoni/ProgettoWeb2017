INSERT INTO `users` (`id`, `username`, `password`, `first_name`, `last_name`, `email`, `capability`) VALUES
(1, 'frossi', '5f4dcc3b5aa765d61d8327deb882cf99', 'Franco', 'Rossi', 'franco@rossi.com', 0),
(2, 'MFederico', '5f4dcc3b5aa765d61d8327deb882cf99', 'Federico', 'Mariani', 'fmariani@orimariani.it', 0),
(3, 'imbotte45', '5f4dcc3b5aa765d61d8327deb882cf99', 'Andrea', 'Imbotte', 'imbotte.andrea@gmail.com', 0),
(4, 'bondop', '5f4dcc3b5aa765d61d8327deb882cf99', 'Paolo', 'Bondolin', 'bondop@gmail.com', 0),
(5, 'brutema', '5f4dcc3b5aa765d61d8327deb882cf99', 'Emanuele', 'Bruttin', 'brutema@gmail.com', 0),
(6, 'APerin', 'dc647eb65e6711e155375218212b3964', 'Alberto', 'Perin', 'aper@gmail.com', 0),
(7, 'marcolin', '5f4dcc3b5aa765d61d8327deb882cf99', 'Marco', 'Berdin', 'marcolin12@gmail.com', 0),
(8, 'Pietron', '5f4dcc3b5aa765d61d8327deb882cf99', 'Pietro', 'Nardon', 'pietron43@yahoo.com', 0),
(9, 'fuffina43', '5f4dcc3b5aa765d61d8327deb882cf99', 'Luisa', 'Marangon', 'fuffina@libero.it', 0),
(10, 'LupoAlberto', '5f4dcc3b5aa765d61d8327deb882cf99', 'Alberto', 'Bisteccon', 'lupoalberto@gmail.com', 0),
(11, 'AlfaRomeoDriver78', '5f4dcc3b5aa765d61d8327deb882cf99', 'Michele', 'Briscolon', 'michele.briscolon@yahoo.com', 0),
(12, 'CalogeroP', '5f4dcc3b5aa765d61d8327deb882cf99', 'Calogero Pasquale', 'Vittorio', 'calogerop.vittorio@gmail.com', 0),
(13, 'Lauretta', '5f4dcc3b5aa765d61d8327deb882cf99', 'Laura', 'Piccoli', 'lauretta12@gmail.com', 0),
(14, 'Astronauta32', '5f4dcc3b5aa765d61d8327deb882cf99', 'Maria Luisa', 'Filiberto', 'mariaf@gmail.com', 0),
(15, 'Barbagianni', '5f4dcc3b5aa765d61d8327deb882cf99', 'Gianni', 'Filiberto', 'barbagianni@gmail.com', 0),
(16, 'Astrubale', '5f4dcc3b5aa765d61d8327deb882cf99', 'Augusto', 'Peron', 'augustoP@gmail.com', 0),
(17, 'AnnibaleIlNero', '5f4dcc3b5aa765d61d8327deb882cf99', 'Annibale', 'Moretti', 'annibale45@gmail.com', 0),
(18, 'Barbarossa', '5f4dcc3b5aa765d61d8327deb882cf99', 'Roberto', 'Barben', 'barbarossa@gmail.com', 0),
(19, 'TeslaCoil', '5f4dcc3b5aa765d61d8327deb882cf99', 'Andrea', 'Colletti', 'colletti.andrea@gmail.com', 0),
(20, 'Radio43', '5f4dcc3b5aa765d61d8327deb882cf99', 'Marta', 'Andreoli', 'marta_andreoli@gmail.com', 0),
(21, 'abete12', '5f4dcc3b5aa765d61d8327deb882cf99', 'Anna', 'Betesso', 'annab67@gmail.com', 0),
(22, 'pasquale43', '5f4dcc3b5aa765d61d8327deb882cf99', 'Pasquale', 'Pietrabianca', 'PPietrabianca@gmail.com', 0);

INSERT INTO `pictures` (`id`, `name`, `description`, `path`, `id_owner`) VALUES
(1, 'StanleyTylon3m', 'Flessometro Stanley Tylon 3m', 'UploadedContent/7ae87bb4-0965-44bb-91f0-12b05c879da3.jpg', 1),
(2, 'StanleyTylon3m', 'Flessometro Stanley Tylon 3m', 'UploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg', 1),
(3, 'Tartaruga Nuvolari', 'Tartaruga d’oro realizzata per l’occasione dal gioielliere milanese Mario Buccellati.', 'UploadedContent/95ff4467-0c93-4c92-adc5-65fddad91f7d.jpg', 2),
(4, 'Tartaruga Nuvolari su libro', 'Tartaruga d’oro realizzata per l’occasione dal gioielliere milanese Mario Buccellati.', 'UploadedContent/2cf9786b-7ae9-4b04-ad39-0a54a25a13ca.jpg', 2),
(5, 'Tartaruga Nuvolari su libro', 'Tartaruga d’oro realizzata per l’occasione dal gioielliere milanese Mario Buccellati.', 'UploadedContent/f8ebcd50-bc7e-456e-9278-3f6700730f99.jpg', 2),
 (6, 'Ferramenta Del Corso 1', 'Interno', 'UploadedContent/4f96ab51-25b7-4f61-9ee6-b45bf2a006f0.jpg', '1');

INSERT INTO `shops` (`id`, `name`, `description`, `website`, `id_owner`, `id_creator`,`shipment`) VALUES
(1, 'Ferramenta del Corso', 'Antica ferramenta, aperta nel 1882, ancora oggi punto di riferimento per tutta la comunità di Thiene', 'http://www.ferramentadelcorso.com', 1, 1,''),
(2, 'Orologeria Oreficeria Mariani', 'Negozio sito nel centro storico a Schio(VI) gestito da tre generazioni, prima dal nostro nonno Mariani Alfredo, maestro orologiaio, passato poi ai nostri genitori Mariani Bianca (maestra d\'ottica dipolomata alla scuola L.D.V. di Fiesole (Firenze) e Mariani Imerio (allievo del nonno Alfredo).\r\nTradizione e professionalità si uniscono per fornirvi i migliori prodotti al giusto prezzo.', 'http://www.orimariani.it', 2, 2,''),
(3, 'Ottica Centrale', 'Da 20 anni il punto di riferimento per occhiali e lenti a contatto, uniamo la nostra esperienza con l\'innovazione dei prodotti per assicurarvi il meglio.', 'www.otticacentralemalo.com', 3, 3,'Spedizione 3 giorni Bartolini assicurata'),
(4, 'Coltelleria Pasubio', 'La Coltelleria Pasubio, azienda nata a Torrebelvicino nel 1968 e specializzata da oltre 45 anni nella coltelleria e negli articoli per casalinghi, offre alla propria clientela una vasta gamma di prodotti di ottima qualità e delle migliori marche.\r\n\r\nGrazie ad un suo laboratorio interno e all’abilità e all’esperienza dei titolari nell’affilare lame di ogni genere, (forbici, coltelli di tutti i tipi, tronchesini, tosatrici, sgorbie per calli ecc..) la Coltelleria Pasubio è oggi un punto di riferimento a livello mondiale per privati e professionisti che vogliono acquistare strumenti efficienti ed estremamente taglienti.', 'www.coltelleriapasubio.it', 4, 4,'Spedizione con corriere 24 ore inclusa nel prezzo');

INSERT INTO `products` (`id`, `name`, `description`, `price`, `id_shop`, `category`) VALUES
(1, 'Flessometro Stanley Tylon 3m', 'Cassa in ABS antiurto, molto compatta. Rivestimento completo in gomma antiscivolo. Nastro di qualità professionale con verniciatura polimerizzata e rivestito con pellicola in mat.sintetico Tylon ™ (spessore 0,14 mm). Clip di aggancio.', '15.00', 1, 0),
(2, 'Spilla Tazio Nuvolari', 'Nel 1932 Il Vate Gabriele D’Annunzio incontra al Vittoriale il pilota Mantovano Tazio Nuvolari.\r\n\r\nOltre a foto ed elogi per le vittorie, fa dono a Nuvolari di una tartaruga d’oro realizzata per l’occasione dal gioielliere milanese Mario Buccellati.\r\n\r\nIl dono viene accompagnato da una frase paradossale: “All’uomo più veloce, l’animale più lento“.\r\nDivenne essa presto l’emblema, il simbolo del pilota Mantovano. Le Sue magliette riportavano sempre tale simbolo, cosi come la propria carta intestata recava in alto a sinistra una piccola tartaruga di colore rosso.\r\nQuesto oggetto più unico che raro, appartenuto per 50 anni al nostro caro amico Rossano Palozzi, è oggi pronto a trovare un nuovo proprietario. Un piccolo pezzo di leggenda destinato ad acquistare velocemente gran valore.', '20000.00', 2, 11),

(3, 'TeckNet Pro Mouse Senza Fili, 2600DPI, Durata delle batterie di 24 Mesi, 2.4G', 'Godetevi tutta l\'affidabilità e la precisione di un mouse con filo e la libertà e il comfort di un wireless con l\'incredibile TeckNet Pro. Questo semplice mouse è progettato con in mente l\'affidabilità, la facilità d\'uso e il comfort per l\'utente.La durata delle batterie fino a 18 Mesi ti consente di risparmiare tempo e denaro nel pieno rispetto dell\'ambiente eliminando praticamente la necessità di sostituire le batterie. Un indicatore di stato segnala il livello di carica delle batterie.
Il piccolo ricevitore senza fili può rimanere inserito nel notebook anche quando lo si trasporta. Inoltre puoi aggiungere con facilità mouse o tastiere compatibili.Risparmierai tempo grazie al tasto che ti consente di passare da un\'applicazione all\'altra e ai pulsanti Avanti e Indietro.
Nessun software, nessun inconveniente: basta semplicemente collegare il ricevitore nano e il mouse è subito pronto per l\'uso.', '10.00', 3, 11),
(4, 'Samsung C24F396 Monitor Curvo, 24\'\' Full HD, 1920 x 1080, 60 Hz, 4 ms,', 'Curvatura eccezionale per un\'esperienza più coinvolgente Vivi un\'esperienza visiva incredibilmente coinvolgente con il monitor Samsung piu\' curvo di sempre. Avvolgendo il campo visivo come in un cinema iMax, lo schermo curvo (dotato di raggio di curvatura pari a 1800R) amplia il campo visivo incrementando la percezione di profondità. Che si tratti di un film o di un gioco adrenalinico, la profonda curvatura dello schermo Samsung ti consente di immergerti completamente nei tuoi contenuti multimediali. Schermo curvo e modalità Eye Saver per il massimo comfort visivo \r\n Schermo curvo: l\'eccezionale curvatura da 1800R dello schermo permette ai tuoi occhi di visualizzare l\'intero display in maniere uniforme affaticando meno la vista. \r\n Modalità Eye Saver: riducendo le emissioni di luce blu, che stimolano maggiormente la retina rispetto alle lunghezze d\'onda di altri colori, la modalità Eye Saver riduce l\'affaticamento degli occhi e assicura un\'esperienza visiva più confortevole \r\n Flicker Free: riduce lo sfarfallio consentendoti di lavorare e giocare più a lungo all\'insegna del massimo comfort Qualità dell\'immagine superiore \r\n Contrasto: il pannello VA assicura un contrasto di 3000:1 permettendoti di ottenere neri più intensi, bianchi più brillanti e immagini ancora più nitide e vivaci. \r\n Perdite di luminosità assente: Il display curvo assicura una perdita di luminosità ai bordi della cornice minima garantendo neri più uniformi sull\'intera area visiva. Gioca al meglio delle tue possibilità grazie ad AMD FreeSync \r\n AMD FreeSync: grazie alla sincronizzazione dinamica tra monitor e fotogrammi del contenuto, la tecnologia AMD FreeSync consente di ridurre al minimo le interruzioni e i ritardi permettendo di giocare al meglio delle proprie possibilità \r\n Game Mode: ottimizzando istantaneamente i colori e il contrasto dello schermo, la ""Game Mode"" ti garantisce sempre la massima esperienza di gioco Design extra slim \r\n Cornice ultrasottile pari a solo 11.9mm', '200.00', 3, 11),
(5, 'Pioneer MVH-390BT 200W Bluetooth Black car media receiver - car media receivers (4.0 channels, AM,FM, LCD, Front, Black, 200 W)', 'More music, more Bluetooth.

The new, short chassis MVH-390BT is designed to stream music from your Bluetooth devices in crystal clear audio quality thanks to the Advanced Sound Retriever for Bluetooth. It also allows you to make and receive hands free phone calls while driving, you can even store the phone numbers of your family and friends as pre-set buttons for quick, easy and safer dialling. On top, you can even connect up to two Bluetooth devices simultaneously!

But there is more than just Bluetooth... Connect your iPod, iPhone, Android smartphone and other USB devices to your car speakers via the front USB or Aux-in. Of course, this car stereo also allows you to tune into your favourite radio stations using the FM/AM tuner.

Enjoy pristine sound from the MVH-390BT\'s built-in amplifiers that delivers 4 x 50W of pure, proven MOSFET power. For even more power, you can use the two RCA pre-outs to hook up another stereo component, like a subwoofer or an extra amplifier for the rear speakers.

Siri Eyes Free

Make use of your iPhone\'s Siri functionality through your unit\'s external microphone and the speakers of your vehicle

Compatible with Android

connect and listen to music stored on your device without the need for an app

Works with iPod / iPhone

With iPod and iPhone Direct Control, you can manage your i-device directly from your car dashboard and enjoy superior sound.

Advanced Remote Control App (ARC)

Pioneer’s Advanced Remote Control (Pioneer ARC) app converts your compatible iPhone or Android smartphone into a powerful touchscreen remote control, which allows you to manage and customise the features of your compatible Pioneer car stereo system in an easy to use way.

Bluetooth

maximise connectivity for hands-free calling and easy wireless audio streaming', '115.00', 3, 11),
(6, 'Verbatim DVD-R 16x Speed, Confezione da 100', 'Campana Da 100 Dvd-R 4.7Gb 16X Serigrafati', '25.00', 3, 11),
(7, 'SanDisk Ultra Fit Unità Flash, USB 3.0 da 16 GB con Velocità fino a 130 MB/sec', 'Questa unità a basso profilo, dall\'ingombro molto ridotto, offre grande spazio di archiviazione e consente di trasferire con velocità file multimediali tra più dispositivi. Ora potrete trasferire un intero film in meno di 30 secondi, usufruire di velocità di scrittura fino a cinque volte superiori rispetto a quelle consentite dalle unità USB 2.0 standard e fare affidamento su una periferica di archiviazione sempre disponibile, progettata per restare ottimamente inserita. Il software SanDisk SecureAccess incluso garantisce la protezione dei file privati tramite password e sistema di crittografia a 128 bit.', '10.00', 3, 11),
(8, 'Garmin Etrex 10 Gps Portatile, Schermo B/N 2,2", Colore: Giallo, Nero', 'Solo Garmin è in grado di migliorare eTrex. Il nuovo eTrex 10 conserva le funzionalità principali, la struttura robusta, la lunga autonomia della batteria e il rapporto qualità/prezzo che hanno reso eTrex il più diffuso dispositivo GPS sul mercato. E\' stata migliorata l\'interfaccia utente ed è stato aggiunto il basemap mondiale, il geocaching informatizzato, e resi compatibili gli accessori per montaggio su manubrio. Stesso nome. Stessa qualità. Tuttavia, eTrex 10 è un dispositivo completamente nuovo, che solo Garmin poteva offrire. Visualizzazione del percorso Il dispositivo eTrex 10 è dotato di un display monocromatico 2.2\'\' facile da leggere in qualsiasi condizione di illuminazione. Robusto e impermeabile, eTrex 10 è progettato per resistere agli elementi atmosferici. L\'interfaccia facile da utilizzare consente di passare più tempo all\'aria aperta, riducendo i tempi di ricerca delle informazioni. eTrex 10 è dotato della resistenza necessaria per resistere ad elementi quali polvere, umidità o acqua, nessuno dei quali rappresenta una sfida per questo formidabile navigatore. Divertimento eTrex 10 supporta i file GPX di geocaching per il download delle geocache e dei dettagli direttamente sull\'unità. Per iniziare l\'avventura con il geocaching, visitare OpenCaching.com. eTrex 10 memorizza e visualizza le informazioni chiave, inclusa posizione, terreno, difficoltà, suggerimenti e descrizioni; in questo modo, non sarà più necessario inserire manualmente le coordinate e stampare le mappe. È sufficiente caricare il file GPX sull\'unità e avviare la ricerca di cache. Mantenimento della posizione Con il ricevitore GPS ad alta sensibilità, abilitato WAAS e le previsioni satellitari HotFix, eTrex 10 individua la posizione rapidamente e in modo preciso e la mantiene in aree coperte e in zone montuose. Il vantaggio è evidente: quando ci si trova nel fitto di un bosco o semplicemente vicino a edifici alti, eTrex 10 sarà in grado di elaborare un percorso nel momento più opportuno. Rilevazioni avanzate La nuova serie eTrex è la prima in grado di rilevare contemporaneamente sia i satelliti GPS che GLONASS. GLONASS è un sistema sviluppato dalla Federazione Russa che sarà completamente operativo nel 2012. Quando si utilizzano i satelliti GLONASS, il tempo impiegato dal ricevitore per rilevare una posizione è (in media) del 20 per cento minore rispetto al GPS. Inoltre, quando si utilizzano sia i satelliti GPS che GLONASS, il ricevitore è in grado di ricevere segnali da 24 satelliti in più rispetto al solo sistema GPS', '107.00', 3, 11),
(9, 'AUKEY Caricatore da Muro con 3 Porte 30W / 6A , con AiPower per iPhone X / 8 / 8 Plus / 7, Samsung, HTC, LG e Altri Dispositivi Alimentati da USB (Nero)', 'Tecnologia Adattiva AiPower
Ottimizzato per mantenere l\'efficienza della tua batteria pur consentendo un\'elevata velocità di ricarica, AiPower si adatta perfettamente per fornire l\'uscita di corrente ottimale per ogni dispositivo USB da ricaricare. Le porte USB intelligenti AiPower erogano la corrente massima supportata da ogni dispositivo fino a 2,4A ciascuna, i tuoi apparecchi elettronici si ricaricheranno alla massima velocità in tutta sicurezza.

Protezione Globale AUKEY
L\'elettronica sofisticata e i circuiti di sicurezza integrati proteggono i tuoi dispositivi dalle eccessive correnti di ricarica, dal surriscaldamento e dalla sovraccarica.

Caricatore Universale
Progettato per ricaricare i dispositivi USB più diffusi, dagli iPhone ai telefoni Android, tablet, macchine fotografiche, altoparlanti Bluetooth, cuffie e molto altro. Qualunque sia il tuo dispositivo con ricarica USB, la nostra batteria è quella che fa per te.

2 Anni di Garanzia
Che sia il tuo primo acquisto AUKEY o che tu sia un nostro cliente affezionato, ti saremo sempre accanto: tutti i prodotti AUKEY sono infatti assicurati da una garanzia di 24 mesi.
', '16.00', 3, 11),
(10, 'Raspberry PI 3 Model B Scheda madre CPU 1.2 GHz Quad Core, 1 GB RAM', '
- A 1.2GHz 64-bit quad-core ARMv8 CPU

- 802.11n Wireless LAN

- Bluetooth 4.1

- Bluetooth Low Energy (BLE)

Come il Pi 2, ha:

- 1GB RAM

- 4 USB ports

- 40 GPIO pins

- Porta Full HDMI

- Porta Ethernet

- Combined 3.5mm audio jack and composite video

- Camera interface (CSI)

- Display interface (DSI)

- Micro SD card slot (now push-pull rather than push-push)

- VideoCore IV 3D graphics core', '35.00', 3, 11),

(11, 'GoPro HERO5 Black Videocamera Subacquea 4K, Fino a 10 m, Sensore CMOS da 12 MP, Nero', 'La HERO5 Black offre un equilibrio tra prestazioni e praticità, grazie al video 4K, ai comandi vocali, all\'intuitivo touch screen e al design impermeabile. Con QuikStories, HERO5 Black invia automaticamente i tuoi filmati sul telefono, dove vengono trasformati in splendidi video già montati, pronti da condividere. E con un abbonamento a GoPro Plus, puoi caricare automaticamente foto e video sul Cloud, per potervi accedere ovunque e in qualsiasi momento.2 Unite ai fluidi video stabilizzati, all\'audio impeccabile, alle foto di qualità professionale e al GPS integrato, queste caratteristiche rendono HERO5 Black lo strumento ideale per raccontare la tua storia.', '400.00', 3, 11);


INSERT INTO `reviews` (`id`, `id_product`, `id_creator`, `global_value`, `quality`, `service`, `value_for_money`, `title`, `description`, `date_creation`) VALUES
(1, 1, 13, 0, 5, 5, 5, 'Ottimo metro', 'Ottimo prodotto, ben realizzato, robusto, rivestito in gomma, nastro metallico abbastanza largo con numeri ben visibili, riesce a non flettersi fino a 90 cm di estensione dovendo effettuare una misurazione \"in aria\" senza poter agganciare il dentino iniziale.\r\nQuindi tutto bene e grazie alla Ferramenta del Corso ad un prezzo stracciato.', '2017-07-11 10:40:38'),
(2, 1, 14, 5, 5, 5, 5, 'Fantastico', 'Ottimo metro, ne ho acquistato uno per uso casalingo, come prodotto \"plus\"\r\n3 metri x 13mm di larghezza del nastro.\r\nIl metro è molto solido e con un sistema di scorrimento e raccolta molto fluido.\r\nIl rivestimento è molto piacevole al tatto e protegge il prodotto da eventuali urti.\r\nIl metro è provvisto di un gancetto per poterlo agganciare ad una cinta o una tasca ( che volendo puó essere rimosso ).', '2017-07-11 10:43:46'),
(3, 1, 15, 5, 4, 4, 5, 'La Ferrari dei metri', 'Ricoperto in gomma che ne facilita la presa è di dimensioni contenute non ostante i 3 metri disponibili. Il nastro è stretto e quindi non è in grado di autosostenersi se non per brevi lunghezze. Molto scorrevole è dotato di blocco . Non ha la finestrella per cui non è possibile effettuare misure \"interne\" ma direi che a questo prezzo se non si hanno esigenze \"professionali\" è un\'ottimo articolo.', '2017-07-11 10:44:58');



INSERT INTO `pictures_products` (`id_product`, `id_picture`) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4),
(2, 5);

INSERT INTO `coordinates` (`id`,`id_shop`, `latitude`, `longitude`, `address`, `opening_hours`) VALUES
(1,1, '45.705959', '11.479617', 'Piazza Chilesotti, 17\r\n36016\r\nThiene (VI)\r\nItalia','Lun-Dom 9:00-19:00'),
(2,1, '45.714211', '11.359345', 'Via Camillo Benso Cavour 18\r\n36015 Schio (VI)\r\nItalia','Lun-Ven 8:00-12:30, 15:00-19:30'),
(3,2, '45.688891', '11.605844', 'Via Maragnole 33,\r\n36042 Breganze\r\n(VI)','Lun-Ven 8:30-13:00'),
(4,2, '45.715733', '11.403583', 'Via Lago di Levico, 9\r\n36015 Schio (VI)',''),
(5,3, '45.667745', '11.404100', 'Via Schio, 12\r\n36034 Malo (VI)','Mart-Sab 8:00-20:00'),
(6,4, '45.715502', '11.317440', 'Viale Pasubio, 33\r\n36036 Torrebelvicino VI', 'Lun-Mar-Ven 8:30-12:30\r\nGio-Sab 9:00- 19:00');


INSERT INTO `pictures_shops` (`id_shop`, `id_picture`) VALUES ('1', '6');
