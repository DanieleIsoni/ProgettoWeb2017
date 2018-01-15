from bs4 import BeautifulSoup
import requests
import random as rnd

id_negozi=[8,25,17]
cat=18
id_prodotto=266
id_rev=1835
fname="prodotti.ok"


from random import randrange
from datetime import timedelta
import datetime as dt

def random_date(start, end):
    """
    This function will return a random datetime between two datetime
    objects.
    """
    delta = end - start
    int_delta = (delta.days * 24 * 60 * 60) + delta.seconds
    random_second = randrange(int_delta)
    return start + timedelta(seconds=random_second)


def scarica_prodotto(url):
    global id_rev
    # add header
    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36'}
    r = requests.get(url, headers=headers)
    #print(r.content)
    print("------------------------------------------>FETCHED")
    soup = BeautifulSoup(r.content, "lxml")
    print("------------------------------------------>LOADED")
    titolo=str(soup.select("#productTitle")[0].text.encode("utf-8")).strip()
    #prezzo=str(soup.select("#priceblock_ourprice")[0].text).strip()[4:].replace(",",".")
    prezzo=str(soup.select('span[id*="priceblock_saleprice"], span[id*="priceblock_ourprice"]')[0].text).strip()[4:].replace(",",".")
    features=soup.select("#feature-bullets > ul > li")[1:]
    desc=""
    for f in features:
        desc+="- "+f.text.encode("utf-8").strip()+"\r\n"


    reviews=soup.select('div[id*="customer_review-"]')
    recensioni=[]
    for r in reviews:
        rTitle=str(r.select('a[class*="review-title"]')[0].text.encode("utf-8"))
        value=int( r.select('i[class*="review-rating"]')[0].text[0])
        #print(value)
        rScore1=rnd.randint(max(2,value-2),min(5,value+2))
        rScore2=rnd.randint(max(2,value-2),min(5,value+2))
        rScore3=rnd.randint(max(2,value-2),min(5,value+2))
        rScore4=rnd.randint(max(2,value-2),min(5,value+2))



        rdesc=str(r.select('span[class*="review-text"]')[0].text.encode("utf-8"))
        #print(rdesc[-4:])
        if(rdesc.endswith("Leggi di pi\xc3\xb9")):
            rdesc=rdesc[:-13]

        d1 = dt.datetime.strptime('1/1/2016 1:30 PM', '%m/%d/%Y %I:%M %p')
        d2 = dt.datetime.strptime('1/1/2018 4:50 AM', '%m/%d/%Y %I:%M %p')

        rdata= random_date(d1, d2)

        #print(rScore1, rScore2, rScore3, rScore4)
        rTitle=rTitle.replace("'", "\\'")
        rdesc=rdesc.replace("'", "\\'")
        rev={'titolo': rTitle, 'value': [rScore1, rScore2, rScore3, rScore4], 'desc': rdesc, 'utente': str(rnd.randint(53,100)), 'data': str(rdata)}
        #print(rev)
        recensioni.append(rev)



    img=r.select('#landingImage')
    print("------------------------------->"+str(img))


    id_negozio=rnd.choice(id_negozi)
    titolo=titolo.replace("'", "\\'")
    desc=desc.replace("'", "\\'")
    sql_p="("+str(id_prodotto)+", '"+titolo+"', '"+desc+"', '"+str(prezzo)+"', '"+str(id_negozio)+"', '"+str(cat)+"'),"
    sql_r=""
    for r in recensioni:
        sql_r= sql_r+"('"+str(id_rev)+"', '"+str(id_prodotto)+"', '"+str(rnd.randint(54,100))+"', '"+str(r["value"][0])+"', '"+str(r["value"][1])+"', '"+str(r["value"][2])+"', '"+str(r["value"][3])+"','"+r["titolo"]+"','"+r["desc"]+"','"+str(r["data"])+"'),\r\n"
        id_rev+=1
        print(id_prodotto, id_rev)
#    print(sql_p)
    return (sql_p, sql_r, str(id_prodotto)+", "+url)

#(p,r,c)=scarica_prodotto("https://www.amazon.it/Seagate-ST1000DM010-HDD-Sata-Grigio/dp/B01LY65EVG")
#print("PRODOTTO:")
#print(p)
#print("REVIEW:")
#print(r)
#id_prodotto+=1
#print(scarica_prodotto("https://www.amazon.it/Seagate-ST1000DM010-HDD-Sata-Grigio/dp/B01LY65EVG"))


prodotti=""
recensioni=""
corrispondenze=""
ps=open("prodotti.sql", "w")
rs=open("recensioni.sql", "w")
cs=open("corrispondenze", "w")

with open(fname) as fp:
    line = fp.readline()
    while line:
        try:
            (p,r,c)=scarica_prodotto(line)
            id_prodotto+=1
            prodotti+="\n"+p
            recensioni +="\n" +r
            corrispondenze += '\n' +c
            ps.write(p+"\n")
            rs.write(r+"\n")
            cs.write(c+"\n")

        except:
            print("Un qualche errore, scappo via")
        line = fp.readline()
        print(prodotti+"\n\n\n\n"+recensioni)


print(prodotti)
print("\n\n\n\n\n\n")
print(recensioni)
print("\n\n\n\n\n\n")
print(corrispondenze)
print("\n\n\n\n\n\n")

ps.close()
rs.close()
cs.close()
