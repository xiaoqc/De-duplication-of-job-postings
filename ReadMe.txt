1. Tika crawler, you can find it in file TikaCrawler

	How to build environment:
	load the project into eclipse

	There are several parameters in it.
		1) String fileD
			fileD is the path of tsv files. 
			If the files are under disk C and file TSV
			then fileD = "C:\\TSV\\", for windows.
			fileD = "C:/TSV/" for mac.   
		2) String jobEntryFile
			jobEntryFile indicates the directory for output json files, one job per json file.
			If the files are under disk C and file JOB
			then fileD = "C:\\JOB\\", for windows.
			fileD = "C:/JOB/" for mac.  
		3) parameters of function TSVToJSON in public HW1()
			The default is TSVToJSON(true, "20121106");
			The first parameter is to enable the de-duplication of same job postings
			The second parameter is the start date of input file, such as the first file is computrabajo-ar-20121106.tsv, the start data is 20121106
		4) parameter of function crawFile in public HW1()
			The default is crawlFile(true);
			the parameter is to enable the identify the near duplicates.

	You can also use comand line to execute it with two parameters, one for fileD, onf for jobEntryFile.

	External libraries    
		Besides tika-app-1.6.jar,
		org.json.jar is also needed.
		You can find it in lib file.
		You need to add these two jars to the project.
		Operation: right click project --- property --- Java Build Path --- Add External Jars --- choose these two jars.

	How to run: just click Run in eclipse.



2. ETLlib Crawler, you can find it in file PythonCrawler

	How to use the python crawler
		1: open terminal
		2: cd dir to python crawler folder
		3: type python crawler.py [tsvFolderPath] [jsonFolderPath] [colHeader.txtFilePath] [d] [v]
			eg: python crawler.py tsv/ json/ colHeader.txt d v
	How to use the python reduplication
		1: open terminal
		2: cd dir to python deduplication folder
		3: type python DeDuplication.py [number of json files] [json folder dir]
			eg: python DeDuplication.py 123 json/ 

