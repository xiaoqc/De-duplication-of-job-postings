#!/usr/bin
#unpack large json file to small individual jsons
import json
import os
import sys

#the input and output folder directory
outputDir = '/Users/jixin/Documents/CSCI572/HW1/JSONs'
inputDir = '/Users/jixin/Documents/CSCI572/HW1/JSON/'

#Generating the output folder
if not os.path.exists(outputDir):
	print 'dosent exists!';
	try:
		os.mkdir(outputDir);
	except:
		print 'ERROR, cannot create the output folder!';
		sys.exit();
else :
	print 'Output folder exists!';

index = 1;

def unpack(inputDir, fileName):
	global index;
	fileDir = inputDir + fileName;
	print fileDir;
	f = file(fileDir);
	data = json.load(f);
	for i in data['jobsInfo']:
		outputFile = outputDir + '/' + str(index) + '.json';
		#print 'Writing file : ' + outputFile;
		f2 = open(outputFile, 'w');
		f2.write(json.dumps(i));
		f2.close();
		index += 1;

for i in os.listdir(inputDir):
	print str(i);
	if '.json' in str(i):
		unpack(inputDir, str(i));
		print 'Done unpacking: ' + str(i);

