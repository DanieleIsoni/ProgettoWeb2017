#!/usr/bin/env python

it = []
en = []
gen = []
adder = {}

def diff(first, second):
        second = set(second)
        return [item for item in first if item not in second]

with open('buyhubBundle_en.properties') as f:
    for line in f:
        oldl = line
        line = line.replace(" ", "")
        if (line != '\n' and line[0]!='#' and line[0]!='"'):
                en.append(line.split('=')[0])
                adder[line.split('=')[0]] = oldl.split('=')[1].rstrip()


with open('buyhubBundle_it.properties') as f:
    for line in f:
        line = line.replace(" ", "")
        if (line != '\n' and line[0]!='#' and line[0]!='"'):
                it.append(line.split('=')[0])

with open('buyhubBundle.properties') as f:
    for line in f:
        line = line.replace(" ", "")
        if (line != '\n' and line[0]!='#' and line[0]!='"'):
                gen.append(line.split('=')[0])

print "#####DIFF EN IT"
print diff(en,it)
print "#####END"
print "\n#####ADD THIS TO it bundle"
for x in diff(en, it):
    print x+" = "+adder[x]
