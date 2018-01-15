import selenium.webdriver as webdriver
import lxml.html as html
import lxml.html.clean as clean
import urllib.request
import uuid
import os

from selenium.webdriver.chrome.options import Options
from bs4 import BeautifulSoup


img_id=251


def download(url):
    browser.get(url)
    content = browser.page_source

    cleaner = clean.Cleaner()
    content = cleaner.clean_html(content)
    doc = html.fromstring(content)
    soup = BeautifulSoup(content, 'html.parser')

    img = soup.find("img",{"id": "landingImage"})['src']

    print(img)
    name=str(uuid.uuid4())+".jpg"
    urllib.request.urlretrieve(img,"product_images/"+name)
    return  "UploadedContent/"+name

chrome_options = Options()
chrome_options.add_argument("--headless")
browser = webdriver.Chrome(chrome_options=chrome_options)
sql_p=""
sql_pp=""
corrispondenze_2=open("corrispondenze_2","wt")

sqlp=open("pictures.sql", "wt")
sqlpp=open("pictures_poducts.sql", "wt")

with open('corrispondenze', 'rt') as f:
    while True:

        line = f.readline()
        if not line: break
        #try:
        spl=line.split(',')
        if len(spl)>1:
            try:
                name=download(spl[1].strip())
                corrispondenze_2.write(spl[0]+", "+spl[1].strip()+", "+name+"\n")
                #(7, 'FotoNegozio', 'FotoNegozio', 'UploadedContent/0c2c8dabf3bb4f160c0d3e4cbdd40af8.jpg', 6),
                sqlp.write(" ('"+str(img_id)+"', 'fotoprodotto','fotoprodotto','"+name+"',1),\n")
                sqlpp.write(
                "('"+spl[0]+"','"+str(img_id)+"'),\n")


                img_id+=1
            except:
                print("Errore nel download, skipped")
    #    except:
    #        print("Error at: "+line)

corrispondenze_2.close()
browser.close()
