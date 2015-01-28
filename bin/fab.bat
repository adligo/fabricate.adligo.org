@ECHO OFF
REM This is the fabricate batch script, to execute fabricate on Windows.
REM Licensed by Adligo Inc. 
REM http://www.apache.org/licenses/LICENSE-2.0
REM
REM If your developing this script, make sure to keep Windows
REM new lines at the end of this file when checking into git.

SETLOCAL enabledelayedexpansion
@SET ARGS=%*
	
IF "%FABRICATE_HOME%" ==  "" ( 
	@ECHO "Fabricate requires the FABRICATE_HOME environment variable to be set."
) ELSE (
	
	SET CLASSPATH=%FABRICATE_HOME%/lib/commons-logging-1.2.jar;
	SET CLASSPATH=!CLASSPATH!%FABRICATE_HOME%/lib/httpcore-4.3.2.jar;
	SET CLASSPATH=!CLASSPATH!%FABRICATE_HOME%/lib/httpclient-4.3.5.jar;
	SET CLASSPATH=!CLASSPATH!%FABRICATE_HOME%/lib/fabricate_snapshot.jar
	
	SET ARGS_FROM_SETUP="Empty"
	
	REM @diagram_sync on 1/26/2014 with Overview.seq
	REM main(String [] args) setup Fabricate args.
	for /f "tokens=*" %%i in ('java -cp !CLASSPATH! org.adligo.fabricate.FabricateArgsSetup !ARGS!') do (
		set LINE=%%i
		if "!ARGS_FROM_SETUP!" == "Empty" (
			SET ARGS_FROM_SETUP=!LINE!
		) else (
			@ECHO !LINE!
		)
	)
	IF "!ARGS_FROM_SETUP!" == "END" (
		GOTO END
	)
	
	SET OPTS_FROM_SETUP="Empty"
	
	REM @diagram_sync on 1/26/2014 with Overview.seq
	REM main(String [] args) setup Fabricate opts.
	for /f "tokens=*" %%i in ('java -cp !CLASSPATH! org.adligo.fabricate.FabricateOptsSetup !ARGS_FROM_SETUP!') do (
		set LINE=%%i
		if "!OPTS_FROM_SETUP!" == "Empty" (
			SET OPTS_FROM_SETUP=!LINE!
			
		) else (
			@ECHO !LINE!
		)
	)
	IF "!OPTS_FROM_SETUP!" == "END" (
		GOTO END
	)

	REM @diagram_sync on 1/26/2014 with Overview.seq
	REM main(String [] args) run Fabricate.
	java !OPTIONS_FROM_SETUP! org.adligo.fabricate.Fabricate !ARGS_FROM_SETUP! 
)
:END
ENDLOCAL