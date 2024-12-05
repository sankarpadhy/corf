@echo off
start cmd /k "cd src/main/resources/static/mobile && python -m http.server 3000"
start cmd /k "cd src/main/resources/static/planner && python -m http.server 3001"
start cmd /k "cd src/main/resources/static/invest && python -m http.server 3002"
