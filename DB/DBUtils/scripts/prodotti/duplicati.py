import random as rnd
presenti=[]
with open('recensioni.sql', 'rt') as f:
    while True:
        line = f.readline()

        if not line: break
        #print(line)
    
        spl= line.split(',')
        if(len(spl)>2):
            s=spl[1]+ "," +spl[2]
            if(s in presenti):
                print(s)
            presenti.append(s)        
    '''    while(s in presenti):
                s=spl[1]+", '"+str(rnd.randint(53,100))+"'"
                #print("--------->"+s)                
                if not s in presenti:
                    break;
            presenti.append(s)
            l=spl[0]+","+s
            print(l+line[len(l):])
        else:
            print line                    
        '''
