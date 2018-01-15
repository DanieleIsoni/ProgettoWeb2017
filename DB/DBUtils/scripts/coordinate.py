import random as r
import pprint
openings=["", "", "", "Lun-Ven 9-18", "Mar-Ven 8-15, Sab 12-19", "Lun-Mar 8-12:30 15-19:30, Sab 9:30-19:30", "Lun-Dom 8:30-20:30"]
vie=["Via Roma", "Via Chiesa", "Corso Como", "Corso Andreotti", "Via della Resistenza", "Via melari", "Via Marcon", "Via San Giuseppe"]
iid=7
id_shop=5

import requests


def reverse(lat,lng):
    GOOGLE_MAPS_API_URL = 'http://maps.googleapis.com/maps/api/geocode/json'

    params = {
        'latlng':str(lat) +','+str(lng),
        'sensor': 'false',
        'region': 'it'
    }

    # Do the request and get the response data
    req = requests.get(GOOGLE_MAPS_API_URL, params=params)
    res = req.json()
    pp = pprint.PrettyPrinter(indent=4)
    #pp.pprint(res['results'][0])
    # Use the first result
    if len(res['results'])>0:
        result = res['results'][0]

        geodata = dict()
        geodata['lat'] = result['geometry']['location']['lat']
        geodata['lng'] = result['geometry']['location']['lng']
        if "Unnamed Road," not in result['formatted_address']:
            geodata['address'] = result['formatted_address']
        else:
            via=r.choice(vie)
            addr=str(result['formatted_address'])
            geodata['address'] = via + ", "+str(r.randint(1,100))+addr[12:]
            print(geodata['address'])
        #print(geodata)
        #print("--------------->"+result['formatted_address'])
        return geodata
    else:
        return None

def read_file(nome):
    c=[]
    with open(nome, 'rt') as f:
        while True:
            line1 = f.readline()
            line2 = f.readline()
            if not line2: break
            lat=line1[23:]
            lng=line2[22:]
            geocode=reverse(lat,lng)
            if geocode != None:
                c.append(geocode)
    return c
'''
coordinates=[]
coordinates.extend(read_file("negozi_vicenza"))
coordinates.extend(read_file("negozi_trento"))
coordinates.extend(read_file("negozi_verona"))
coordinates.extend(read_file("negozi_udine"))
coordinates.extend(read_file("negozi_milano"))
print(coordinates)
coordinates=[]
coordinates.extend(read_file("negozi_bologna"))
print(coordinates)
'''

'''

coordinates=[{'lat': 45.61667, 'lng': 11.26876, 'address': 'Contrada Via, 1, 36078 Valdagno VI, Italy'}, {'lat': 45.70656899999999, 'lng': 11.624973, 'address': 'Via S. Gaetano, 20, 36064 Mason vicentino VI, Italy'}, {'lat': 45.3300896, 'lng': 11.7629409, 'address': 'Via Risorgimento, 4-16, 35036 Montegrotto Terme PD, Italy'}, {'lat': 45.289735, 'lng': 11.467333, 'address': 'Via Salboro, 50, 36026 Poiana maggiore VI, Italy'}, {'lat': 45.4197579, 'lng': 11.4857533, 'address': 'Via Calto, 86, 36020 Villaga VI, Italy'}, {'lat': 45.7681714, 'lng': 11.7159157, 'address': 'Via Conco, 51, 36061 Bassano del Grappa VI, Italy'}, {'lat': 45.3913988, 'lng': 11.440173, 'address': 'Via Monte Palù, 6, 36045 Lonigo VI, Italy'}, {'lat': 45.6083242, 'lng': 11.5688968, 'address': 'Via Chiesa, 145, 36010 Monticello Conte Otto VI, Italy'}, {'lat': 45.789302, 'lng': 11.410031, 'address': 'Via Lombardi Riccardo, 6, 36010 Cogollo del Cengio VI, Italy'}, {'lat': 46.2768841, 'lng': 11.2128868, 'address': "Via del Vino, 48, 39040 Magre' sulla strada del vino BZ, Italy"}, {'lat': 45.8479963, 'lng': 11.2044708, 'address': 'SP138, 38060 Terragnolo TN, Italy'}, {'lat': 46.0756618, 'lng': 11.4453233, 'address': 'SP65, 38050 Ronchi Valsugana TN, Italy'}, {'lat': 46.1462688, 'lng': 10.9516402, 'address': 'Via Roma, 75, 38018 Molveno TN, Italy'}, {'lat': 46.1565966, 'lng': 11.079734, 'address': 'Destra Adige, 14, 38010 Zambana TN, Italy'}, {'lat': 45.9542825, 'lng': 11.3399293, 'address': 'SS349, 38056 Levico Terme TN, Italy'}, {'lat': 46.0551092, 'lng': 10.9271871, 'address': 'SS237, 38072 Calavino TN, Italy'}, {'lat': 46.0433411, 'lng': 10.9596834, 'address': 'Via Lónga, 4, 38072 Calavino TN, Italy'}, {'lat': 45.6147679, 'lng': 10.9828452, 'address': 'SP14a, Grezzana VR, Italy'}, {'lat': 45.5085434, 'lng': 11.3157754, 'address': 'Via Valmora, 37-49, 36071 Arzignano VI, Italy'}, {'lat': 45.2703318, 'lng': 10.7619106, 'address': 'Via Solferino e San Martino, 82, 46048 Roverbella MN, Italy'}, {'lat': 45.6639181, 'lng': 11.0080644, 'address': 'Corso Andreotti, 82, 37020 Erbezzo VR, Italy'}, {'lat': 45.6243033, 'lng': 11.1153773, 'address': 'Contrada Pozze, 37030 Velo veronese VR, Italy'}, {'lat': 45.18489, 'lng': 11.0469, 'address': 'Via Montalto, 1, 37054 Nogara VR, Italy'}, {'lat': 45.4726282, 'lng': 11.2687329, 'address': 'Via Alpone, 84, 37030 Alpone VR, Italy'}, {'lat': 45.9447603, 'lng': 13.1340421, 'address': 'SP7, Talmassons UD, Italy'}, {'lat': 46.1735714, 'lng': 13.0530392, 'address': 'Via S. Martino, 50, 33038 San Daniele del Friuli UD, Italy'}, {'lat': 46.03981, 'lng': 13.26483, 'address': 'Via dei Prati, 42, 33100 Udine UD, Italy'}, {'lat': 46.1431435, 'lng': 13.5473341, 'address': 'Località Cernizza, 12, 33040 San Leonardo UD, Italy'}, {'lat': 45.843094, 'lng': 13.2607399, 'address': 'Località Villaggio Roma, 98, 33050 Villaggio Roma UD, Italy'}, {'lat': 45.8908151, 'lng': 13.110255, 'address': 'Via San Giuseppe, 6, 33030 Talmassons UD, Italy'}, {'lat': 46.0029668, 'lng': 13.3289397, 'address': 'Via Enrico Fermi, 37, 33042 Buttrio UD, Italy'}, {'lat': 45.2565202, 'lng': 9.0571594, 'address': 'SP11, 27020 Marcignago PV, Italy'}, {'lat': 45.6093616, 'lng': 8.963305499999999, 'address': 'Via Palmiro Togliatti, 2, 20027 Rescaldina MI, Italy'}, {'lat': 45.2180419, 'lng': 9.3005918, 'address': 'Via Marzano, 4, 27010 Vistarino PV, Italy'}, {'lat': 45.3642077, 'lng': 9.5241916, 'address': 'SP61, 26010 Dovera CR, Italy'}, {'lat': 45.623914, 'lng': 9.202442999999999, 'address': 'Via Albert Einstein, 10B, 20832 Desio MB, Italy'}, {'lat': 45.4200897, 'lng': 8.8789794, 'address': 'Via Fratelli Bandiera, 3, 20087 Cascinazza MI, Italy'}, {'lat': 45.6093616, 'lng': 8.963305499999999, 'address': 'Via Palmiro Togliatti, 2, 20027 Rescaldina MI, Italy'}, {'lat': 45.342448, 'lng': 9.45463, 'address': 'Via Pizzafuma, 3, 26836 Arcagna LO, Italy'}, {'lat': 45.6482422, 'lng': 8.919651199999999, 'address': 'Via G. Giacchetti, 21055 Gorla minore VA, Italy'}, {'lat': 45.7114926, 'lng': 9.2098329, 'address': 'Via Battaglione Edolo, 40, 20833 Giussano MB, Italy'},{'lat': 44.3846334, 'lng': 11.2818471, 'address': 'Via Colliva, 7, 40037 Pianoro BO, Italy'}, {'lat': 44.38911, 'lng': 11.4758869, 'address': 'Via S. Giorgio, 3, 40064 Castel San Pietro Terme BO, Italy'}, {'lat': 44.580791, 'lng': 11.2546312, 'address': 'Via Stelloni Ponente, 49, 40010 Sala bolognese BO, Italy'}, {'lat': 44.3457792, 'lng': 11.3927859, 'address': 'Via Cà di Gennaro, 6, 40065 Pianoro BO, Italy'}, {'lat': 44.4085209, 'lng': 11.2247051, 'address': 'Via Paramatto, 3, 40037 Sasso Marconi BO, Italy'}, {'lat': 44.5654622, 'lng': 11.1819148, 'address': "Via Pierino Turrini, 7, 40011 Anzola dell'Emilia BO, Italy"}, {'lat': 44.5710894, 'lng': 11.3868414, 'address': 'A13 Bologna - Padova, 40013 Bologna BO, Italy'}, {'lat': 44.39456300000001, 'lng': 11.1774609, 'address': 'Via Rasiglio, 40037 Sasso Marconi BO, Italy'}, {'lat': 44.5141893, 'lng': 11.4893681, 'address': "Via Caduti Per la Liberta', 81, 40055 Castenaso BO, Italy"}, {'lat': 44.6274081, 'lng': 11.2097705, 'address': 'Strada Provinciale 3, 24, 40017 San Giovanni in Persiceto BO, Italy'}, {'lat': 44.4034005, 'lng': 11.3005115, 'address': 'Via Fornace, 40037 Sasso Marconi BO, Italy'}, {'lat': 44.347977, 'lng': 11.2623087, 'address': 'Via Cà Fortuzzi, 40043 Marzabotto BO, Italy'}, {'lat': 44.3228962, 'lng': 11.3862542, 'address': 'Via Monte delle Formiche, 11, 40065 Pianoro BO, Italy'}, {'lat': 44.431096, 'lng': 11.1670534, 'address': 'Via Marzabotto, 2, 40050 Monte San Pietro BO, Italy'}, {'lat': 44.53615, 'lng': 11.48166, 'address': 'Via Veduro, 22, 40055 Castenaso BO, Italy'}, {'lat': 44.5795266, 'lng': 11.1373073, 'address': 'Via Alfonso Sghinolfi, 2, 41013 Castelfranco Emilia MO, Italy'}, {'lat': 44.3865093, 'lng': 11.2546788, 'address': 'Via Battedizzo, 40037 Sasso Marconi BO, Italy'}, {'lat': 44.4065652, 'lng': 11.1432593, 'address': 'Via S. Chierlo, 5, 40050 Monte San Pietro BO, Italy'}]

print(coordinates)


coordinates_id=0
for shop in range(id_shop,32):
    for x in range(r.randint(1, 3)):
        c=coordinates[coordinates_id]
        orari=r.choice(openings)
        this="('"+str(iid)+"','"+str(shop)+"','"+str(c['lat'])+"','"+str(c['lng'])+"','"+str(c['address'])+"', '"+orari+"'),\n"
#        (6,4, '45.715502', '11.317440', 'Viale Pasubio, 33\r\n36036 Torrebelvicino VI', 'Lun-Mar-Ven 8:30-12:30\r\nGio-Sab 9:00- 19:00');
        print(this)
        iid+=1
        coordinates_id+=1
    shop+=1
'''
import re

with open('coordinates', 'rt') as f:
    while True:
        line = f.readline()

        if not line: break
        #print(line)

        matchObj = re.match( r'.*[0-9]{5}.*', line)
        if(matchObj):
            print(matchObj.group())
