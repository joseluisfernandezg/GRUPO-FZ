@echo off

setlocal

rem Ruta de Ghostscript (Asegúrate de tener gswin64c.exe o gswin32c.exe en esta ubicación)
set "GHOSTSCRIPT_PATH=C:\Program Files\gs\gs10.01.2\bin"

rem Ruta del PDF de entrada y salida
set "INPUT_PDF=C:\Java\0-Produccion\GrupoFZ\rep\pdf\catalogo\%1"
set "OUTPUT_PDF=C:\Java\0-Produccion\GrupoFZ\rep\pdf\catalogo\%2"

 
rem Reducción del tamaño (ajusta este valor según tus necesidades)
set "RESOLUTION=100"  rem Cambiar la resolución de las imágenes a 150 DPI

rem Utilizar Ghostscript para reducir el tamaño del PDF
"%GHOSTSCRIPT_PATH%\gswin64c.exe" -sDEVICE=pdfwrite -dCompatibilityLevel=1.4 -dPDFSETTINGS=/printer -dNOPAUSE -dQUIET -dBATCH -dSAFER -sOutputFile="%OUTPUT_PDF%" -r%RESOLUTION% "%INPUT_PDF%"

IF NOT EXIST %OUTPUT_PDF%  goto paso

IF EXIST %INPUT_PDF%  del %INPUT_PDF%

rem IF NOT EXIST %INPUT_PDF%  ren %OUTPUT_PDF% %INPUT_PDF%

 ren %OUTPUT_PDF% %INPUT_PDF%

:paso

echo Proceso completado.
