#!/usr/bin/env python
# -*- coding: utf-8 -*-

from translate import Translator
translator= Translator(to_lang="zh")


def translate(s):
	from googletrans import Translator
	translator = Translator()
	return translator.translate(s, dest='en').text


file = open("category", "r")
en= open("category_en", "w") 
it = open("category_it", "w") 
x=0;
for line in file: 
	it.write("category_"+str(x)+" = "+line[:-1]+"\n") 
	en.write("category_"+str(x)+" = "+translate(line[:-1])+"\n")
	x+=1
it.close()
en.close()
