This is the new adligo build/make system.

to regenerate the xml .java io files in
org.adligo.fabricate.xml.io and it's child packages
you may need to delete the directories with the auto generated code
xjc.sh src/org/adligo/fabricate/xml -d src

To Build it run ant (package or deploy),
note if your using eclipse, you will need to
down load ant 1.9.4 unzip it and set ANT_HOME
using the eclipse preferences/ant/Runtime/Ant Home button. 


To Use It;
Setup the JAVA_HOME and FABRICATE_HOME environment variable
which should point to a uncompressed copy of fabricate.

Add fab (the shell script) to your path.

To run in default mode simply type
fab
in a project_group directory (with a fabricate.xml file)
ie project_group.adligo.org
which will down load the projects under the output directory
and do the work

Development mode allows you to run
fabricate on a directory with checked out 
projects, to do this run;
fab dev
all projects in the same directory
as the project group directory 
will get compiled etc.


Note the *v1_0 xml name spaces are subject to change until the v1_0 version
of fabricate is released.