@echo off
cd /d %~dp0

REM Start Mobile Banking App
echo Starting Mobile Banking App...
start /b npm run start-mobile

REM Start Financial Planner App
echo Starting Financial Planner App...
start /b npm run start-planner

REM Start Investment Dashboard App
echo Starting Investment Dashboard App...
start /b npm run start-invest

REM Wait for user input to close
echo.
echo Press any key to stop the servers...
pause >nul

REM Kill all node processes
taskkill /f /im node.exe
