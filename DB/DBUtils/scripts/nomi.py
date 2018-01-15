import csv
import random
import urllib.request
mails=["gmail.com","yahoo.com","virgilio.it","outlook.com","apple.com"]
sbj=["cats", "nature","technics","transport"]

'''
i=23
with open('nomi.csv', 'rt') as csvfile:
     nomi = csv.reader(csvfile, delimiter=',',quotechar='\'')
     for row in nomi:
         url = 'http://lorempixel.com/600/600/'+random.choice(sbj)+'/'
         urllib.request.urlretrieve(url, "avatars/"+row[2]+".jpeg")
         mail=row[2]+"@"+random.choice(mails)
         sql='('+str(i)+', \''+row[2]+'\''+', \'5f4dcc3b5aa765d61d8327deb882cf99\', \''+row[0]+'\', \''+row[1]+'\', \''+mail+'\', 1, \'UploadedContent/avatars/'+row[2]+'.jpeg\'),'
         print(sql)
         i+=1
'''

nomi = [
"Aturneve1941.jpeg",
"Coice1991.jpeg",
"Hiseld.jpeg",
"Lingthe.jpeg",
"Peat1969.jpeg",
"Shopmed1970.jpeg",
"Whoodger.jpeg",
"APerin.jpeg",
"AlfaRomeoDriver78.jpeg",
"AnnibaleIlNero.jpeg",
"Astronauta32.jpeg",
"Astrubale.jpeg",
"Barbagianni.jpeg",
"Barbarossa.jpeg",
"CalogeroP.jpeg",
"Lauretta.jpeg",
"LupoAlberto.jpeg",
"Radio43.jpeg",
"Raut1976.jpeg",
"TeslaCoil.jpeg",
"abete12.jpeg",
"admin.jpeg",
"bondop.jpeg",
"brutema.jpeg",
"fuffina43.jpeg",
"marcolin.jpeg",
"negozio.jpeg",
"pasquale43.jpeg",
"pietron.jpeg",
"utente.jpeg"]
for row in nomi:
    url = 'http://lorempixel.com/600/600/'+random.choice(sbj)+'/'
    urllib.request.urlretrieve(url, "avatars/"+row)
    print(row)
