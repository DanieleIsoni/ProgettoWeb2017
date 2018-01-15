from bs4 import BeautifulSoup
import urllib
nome="prodotti"
f=open(nome)
#print(f.readlines())
soup = BeautifulSoup(str(f.readlines()), "lxml")
#print(soup)
f.close()
f=open(nome+".prodotti", "w")

for x in soup.select("a"):
    link=x['href']

    if link.startswith("https://www.amazon.it/"):
        try:
            spl=link.split("dp")
            link=spl[0]+"dp/"+spl[1].split("/")[1]
            print(link)
            f.writelines(link+"\n")
        except:
            print("bloh")
    else:
        url=urllib.unquote(link[link.find("&url=")+5:]).decode('utf8')
        if url.startswith("https://www.amazon.it/"):
            spl=url.split("dp")
            link=spl[0]+"dp/"+spl[1].split("/")[1]
            print(link)
            f.writelines(link+"\n")

#cat file |uniq >>file
