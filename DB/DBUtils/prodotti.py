#(4, 'NEGOZIO', 'DESC.', 'www.buyhub.com', 5, 'Spedizione con corriere 24 ore inclusa nel prezzo', 1, '0.00');

'''
import random
id_owner=5
sped=[("Corriere 24h",10) ,("",0),("PaccoCelere3", 4), ("Spedizione nazionale", 2), ("Posta ordinaria", 1.69)]
desc=["Da anni leader nel settore", "I nostri esperti sono al tuo servizio. Contattaci!", "Un nome, una garanzia", "Non sogni, ma solide realt&agrave;", "Da 20 anni al vostro servizio", "Il cliente prima di tutto"]
result=""
for i in range(5,50):
    descrizione=random.choice(desc)
    print("--------->"+descrizione)
    nome=str(raw_input("nome:"))
    sito="www."+nome+".com"
    id_owner+=1
    (spedizione, costi)=random.choice(sped)
    this="(%i, '%s', '%s', '%s', %i, '%s', %i, %f),\n"%(i, nome, descrizione, sito, id_owner, spedizione, 1,costi)
    print(this+"\n\n--------------\n"+result+"\n-------------------\n")
    result+=this
'''



# (6, 'Ferramenta Del Corso 1', 'Interno', 'UploadedContent/4f96ab51-25b7-4f61-9ee6-b45bf2a006f0.jpg', '1');

'''
names=[
"0c2c8dabf3bb4f160c0d3e4cbdd40af8.jpg",
"0dedc0e523968b6315c9fc5edc281c0a.jpg",
"4bbca51e74ba690f208ee61f392153ed.jpg",
"6dec4a6f4096a2db5748206b8c0fec16.jpg",
"8a3ac146009fc44693ff61098a288199.jpg",
"49c69e01f3e5762847c08884e5f0aeda.jpg",
"805bb9e2e5dae6a4eaef2fcbef8d31b5.jpg",
"1530ce3430e0e66c5864959cec32e8f9.jpg",
"86829b5c83200a9f83812acac8a4fde5.jpg",
"3536519c544f972433becfd69c2d0b32.jpg",
"6301165f516ef8d249c85e7bda05315a.jpg",
"b06953b4207b8154c488ff8af6623e5f.jpg",
"c1babc66e73ddedc53934b5c99591198.jpg",
"dec628bbdcdc3c6fb5f2f318c8f72ee0.jpg",
"e3032d1dd6382ab3c5ddfe86ae98708b.jpg",
"fd569bc302f08a0420fe49d252cfaf1f.jpg",
]
i=7
for x in names:
    #print("("+str(i)+",'FotoNegozio','FotoNegozio','UploadedContent/"+x+"', '1'),")
    print("mv "+str(x)+" "+str(i)+".jpg ")
    i+=1
'''



Ferramenta del Corso
1



Orologeria Oreficeria Mariani
2



Ottica Centrale
3



Coltelleria Pasubio
4



Ferramenta centrale
5



Ferramenta Bugiarda
6



Elettronica 2000
7



Informatica Nuova
8



Gioielleria Bijou
9



Marcanti Acessori
10



Techno3000
11



Il paradiso della brugola
12



ElettroNew
13



LampaOK
14



ChinaImport1990
15



Fooo
16



GiovanniRossi
17



Cartoleria Revolution
18



Givaschi Casalinghi
19



Di tutto e di più
20



Garden2000
21


Fishers paradise
22



PetLandia
23



NewNet
24



SuperMelaStore
25
