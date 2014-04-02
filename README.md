GeneticMusicComposition
=======================

Senior Research by Stephen Bussey mentored by Dr. Alice Armstrong of Shippensburg University.

Overview
-------

This research project is the work of Stephen Bussey in his senior year at Shippensburg University. The objective of the research, clearly defined in Objective.docx is to "compose music using Genetic and Memetic Algorithms by concurrently and separately processing the rhythmic and tonal components". This is preliminary research that sought to find a novel approach to the music composition problem while learning from the mistakes of previous academics.

Structure
-------

* process - This folder holds files that were used in the research process but do not really reflect the final result of the research
* results - This folder contains all of the results used in the research paper and at conferences
* umls - These contain some source uml files and diagrams used in presentations and posters
* workspace - This is the Java source code of the entire project, including everything needed to run the application
 
Files in Root
* NCUR Poster - Poster for 2014 National Undergraduate Research Conference
* Before Experimentation Paper.docx - The final paper draft given before experimentation began. Focuses very heavily on methods and how the application will work
* Paper - Final paper after experimentation
* Ship Poster - Poster used in the hallways at Ship
* Ship Presentation - Presentation given for final research consideration

Code
-------

The code is all Java based and all source is given. To run the tests, execute the JUnit FullTestSuite.java. Do not run each test individually because the initialization of the random seed must occur. The tests run very quickly.

The source is very heavily documented, so that can be viewed in the source itself. There are 3 runners worth nothing. The `Runner` class is a test runner that isn't used in experimentation. `ThreadedRunner` class is used for the actual serial experiment. This has the ability to execute several jobs in parallel (while each job is done serially) but that threading factor is turned off for experimentation. `MergeRunner` class is the divide & conquer approach which executes a job's components in parallel instead of in serial and then merges results back at the end.

Runners
------

Compiled .jar files are given which will execute the experiment without compiling source. There is a runner.bat file which will execute the jars with a repetition factor of 30. In general the jars are run via:

```
cd non-dc-threaded-runner
java -jar non-dc-threaded-runner.jar experiment 30
```

As seen above, they must be run from the folder they are contained in.
