@echo off
setlocal EnableDelayedExpansion

REM ==================================================
REM VALIDATE INPUT
REM ==================================================
if "%~1"=="" (
    echo ERROR: Repository URL not provided.
    echo Usage: %~nx0 https://github.com/org/repo.git
    exit /b 1
)

set REPO_URL=%~1

REM ==================================================
REM EXTRACT PROJECT NAME
REM ==================================================
for %%A in (%REPO_URL%) do set REPO_NAME=%%~nA
set REPO_DIR=%REPO_NAME%

REM ==================================================
REM BRANCH & FILE CONFIG
REM ==================================================
set DEV_BASE_BRANCH=dev
set DEV_DOCKER_BRANCH=dev-docker
set UAT_BRANCH=uat

set DEV_FILENAME=application-dev.properties
set UAT_FILENAME=application-uat.properties

set OLD_URL=http://wecore.wenxt:2000
set NEW_URL=http://wecore-uat.wenxt

set TEMP_DEV_FILE=.tmp_application_dev.properties
set TEMP_JENKINS=.tmp_Jenkinsfile

REM ==================================================
REM CLEAN & CLONE
REM ==================================================
if exist "%REPO_DIR%" rmdir /s /q "%REPO_DIR%"

git clone "%REPO_URL%" "%REPO_DIR%" || exit /b 1
cd "%REPO_DIR%" || exit /b 1

git fetch origin --prune

REM ==================================================
REM ENSURE origin/uat EXISTS
REM ==================================================
git ls-remote --exit-code --heads origin %UAT_BRANCH% >nul 2>&1
if errorlevel 1 (
    git checkout -B %UAT_BRANCH% origin/%DEV_BASE_BRANCH% || exit /b 1
    git push origin %UAT_BRANCH%
)

REM ==================================================
REM COPY JENKINSFILE: dev-docker -> uat (WITH REPLACE)
REM ==================================================
git checkout -B %DEV_DOCKER_BRANCH% origin/%DEV_DOCKER_BRANCH% || exit /b 1

if exist Jenkinsfile (
    echo Syncing Jenkinsfile from %DEV_DOCKER_BRANCH% to %UAT_BRANCH%

    copy Jenkinsfile "%TEMP_JENKINS%" >nul

    git checkout -B %UAT_BRANCH% origin/%UAT_BRANCH% || exit /b 1
    copy "%TEMP_JENKINS%" Jenkinsfile >nul
    del "%TEMP_JENKINS%"

    REM -------- SAFE KEYWORD REPLACEMENT --------
    powershell -Command ^
        "$c = Get-Content Jenkinsfile; ^
         $c = $c -replace '192.168.1.185:9002','registry.wenxttech.com'; ^
         $c = $c -replace '13.200.69.122:9002','registry.wenxttech.com'; ^
         $c = $c -replace 'uat-agent-linux-62','uat-agent-linux-143'; ^
         $c = $c -replace 'http://','https://'; ^
         Set-Content Jenkinsfile $c"

    git add Jenkinsfile
    git commit -m "Sync Jenkinsfile from dev-docker and update registry/agent URLs" || echo Jenkinsfile unchanged
    git push origin %UAT_BRANCH%
) else (
    echo No Jenkinsfile found in %DEV_DOCKER_BRANCH%, skipping Jenkinsfile sync
)

REM ==================================================
REM FIND application-dev.properties (ANYWHERE)
REM ==================================================
git checkout -B %DEV_DOCKER_BRANCH% origin/%DEV_DOCKER_BRANCH% || exit /b 1

set DEV_FILE_PATH=

for /f "delims=" %%F in ('git ls-files ^| findstr /i "%DEV_FILENAME%"') do (
    if not defined DEV_FILE_PATH set DEV_FILE_PATH=%%F
)

if not defined DEV_FILE_PATH (
    echo ERROR: %DEV_FILENAME% not found.
    exit /b 1
)

REM Convert Git path to Windows path
set DEV_FILE_PATH=!DEV_FILE_PATH:/=\!
echo Found DEV file: %DEV_FILE_PATH%

copy "%DEV_FILE_PATH%" "%TEMP_DEV_FILE%" >nul || exit /b 1

REM ==================================================
REM APPLY PROPERTIES TO uat
REM ==================================================
git checkout -B %UAT_BRANCH% origin/%UAT_BRANCH% || exit /b 1

for %%D in ("%DEV_FILE_PATH%") do set DEV_DIR=%%~dpD
set UAT_FILE_PATH=%DEV_DIR%%UAT_FILENAME%

copy "%TEMP_DEV_FILE%" "%UAT_FILE_PATH%" >nul || exit /b 1
del "%TEMP_DEV_FILE%"

REM -------- URL REPLACEMENT --------
powershell -Command ^
    "(Get-Content '%UAT_FILE_PATH%') -replace '%OLD_URL%', '%NEW_URL%' | Set-Content '%UAT_FILE_PATH%'"

git add "%UAT_FILE_PATH%"
git commit -m "Sync application-uat.properties from dev-docker and update UAT URL" || echo No changes
git push origin %UAT_BRANCH%

REM ==================================================
REM REMOVE Jenkinsfile FROM ALL NON-UAT BRANCHES
REM ==================================================
for /f %%B in ('git branch -r ^| findstr /v "origin/%UAT_BRANCH%"') do (
    set BR=%%B
    set BR=!BR:origin/=!

    git checkout -B !BR! origin/!BR! >nul 2>&1

    if exist Jenkinsfile (
        del Jenkinsfile
        git add Jenkinsfile
        git commit -m "Remove Jenkinsfile (allowed only in uat)"
        git push origin !BR!
    )
)

git checkout %UAT_BRANCH%

echo ==================================================
echo COMPLETED SUCCESSFULLY FOR %REPO_NAME%
echo ==================================================

endlocal
