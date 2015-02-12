This is the new adligo build/make system.

The shell is expected to be using UTF-8 Character Encoding;
http://perlgeek.de/en/article/set-up-a-clean-utf8-environment
http://stackoverflow.com/questions/7165108/in-osx-lion-lang-is-not-set-to-utf8-how-fix
http://stackoverflow.com/questions/379240/is-there-a-windows-command-shell-that-will-display-unicode-characters


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

Optionally the FABRICATE_REPOSITORY environment variable
can be se to store the local repository in a location
other than the default ${USER_HOME}/local_repository.

Add fab (the shell script) to your path.

To run in default mode simply type
fab
in a project_group directory (with a fabricate.xml file)
i.e. project_group.adligo.org
which will down load the projects under the output directory
and do the work

Development mode allows you to run
fabricate on a directory with checked out 
projects, to do this run;
fab dev
all projects in the same directory
as the project group directory 
will get compiled etc.

If your using git and want to update a lot of projects you can do;
fab git="pull"

Note the *v1_0 xml name spaces are subject to change until the v1_0 version
of fabricate is released.