#!/bin/bash
tsv="TSV/"
json="JSON/"
for ag in $(ls TSV/)
do
	tsvPath=$tsv$ag
	jsonPath=$json${ag//.tsv/.json}
	#echo $tsvPath
	#echo $jsonPath
	tsvtojson -t $tsvPath -j $jsonPath -c columnNames.txt -o jobsInfo -v -e encoding.txt
done
