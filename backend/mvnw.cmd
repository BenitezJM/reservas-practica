@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------

@echo off
setlocal

set "DIR=%~dp0"
set "MAVEN_VERSION=3.9.9"
set "MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%-bin\apache-maven-%MAVEN_VERSION%"

if exist "%MAVEN_HOME%\bin\mvn.cmd" (
    goto run
)

echo Downloading Maven %MAVEN_VERSION%...
powershell -NoProfile -ExecutionPolicy Bypass -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $dest = '%TEMP%\apache-maven-%MAVEN_VERSION%-bin.zip'; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile $dest; Expand-Archive -Path $dest -DestinationPath '%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%-bin' -Force; Remove-Item $dest -Force }"

:run
if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Error: Could not find or install Maven at %MAVEN_HOME%
    exit /b 1
)

call "%MAVEN_HOME%\bin\mvn.cmd" %*
