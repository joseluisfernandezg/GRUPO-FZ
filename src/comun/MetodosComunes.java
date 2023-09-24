//
//  Metodos Comunes  
//
package comun;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MetodosComunes {

  private static Robot robot;
  public static DecimalFormatSymbols simbolo = new DecimalFormatSymbols();

  // Version
  public static String version() {
    String ver = "Version 14.08.23";
    return ver;
  }

  // dia Hoy
  public static int getDiaHoy() {
    GregorianCalendar calendario = new GregorianCalendar();
    int dia = calendario.get(Calendar.DAY_OF_MONTH);
    return dia;
  }

  // Mes Hoy
  public static int getMesHoy() {
    GregorianCalendar calendario = new GregorianCalendar();
    int mes = calendario.get(Calendar.MONTH) + 1;
    return mes;
  }

  // Ano Hoy
  public static int getAnoHoy() {
    GregorianCalendar calendario = new GregorianCalendar();
    int ano = calendario.get(Calendar.YEAR);
    return ano;
  }

  // Fecha Hoy (DD-MM-YYYY)
  public static String dmyhoy() {
    Date Hoy = new Date();
    SimpleDateFormat fd = new SimpleDateFormat("dd-MM-yyyy");
    String fecd = fd.format(Hoy);
    return fecd;
  }

  // Fecha Hoy (YYYYMMDD)
  public static String ymdhoy() {
    Date Hoy = new Date();
    SimpleDateFormat fd = new SimpleDateFormat("yyyyMMdd");
    String fecd = fd.format(Hoy);
    return fecd;
  }

  // Retorna Dia semana hoy
  public static String diasem() {
    String[] sem = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
    GregorianCalendar calendario = new GregorianCalendar();
    int nrodia = calendario.get(GregorianCalendar.DAY_OF_WEEK);
    String diasem = sem[nrodia - 1];
    return diasem;
  }

  // Retorna dia semana fecha YYYYMMDD (lun,mar,mie,jue,vie,sab,dom)
  public static String GetDiaSem(String fec) {
    String diaS = "0";
    if (isNumeric(fec) && fec.length() == 8) {
      SimpleDateFormat fs = new SimpleDateFormat("EEEE");
      java.util.Date date = null;
      DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
      try {
        date = formatter.parse(fec);
      } catch (ParseException ex) {
        Logger.getLogger(MetodosComunes.class
                .getName()).log(Level.SEVERE, null, ex);
      }
      diaS = fs.format(date);
    }
    return diaS;
  }

  //Convierte fecha ddmmaaaa a dd-mm-aaaamm
  public static String dmy_dmy(String fec) {
    if (fec.length() == 8) {
      fec = fec.substring(0, 2) + "-" + fec.substring(2, 4) + "-" + fec.substring(fec.length() - 4, fec.length());
    }
    return fec;
  }

  //Convierte fecha ddmmaaaa a aaaammdd
  public static String dmy_ymd(String fec) {
    if (fec.length() == 8) {
      fec = fec.substring(fec.length() - 4, fec.length()) + fec.substring(2, 4) + fec.substring(0, 2);
    }
    return fec;
  }

  //Convierte fecha ddmmaaaa a aa-mm
  public static String dmy_yymm(String fec) {
    if (fec.length() == 8) {
      fec = fec.substring(fec.length() - 2, fec.length()) + "-" + fec.substring(2, 4);
    }
    return fec;
  }

  //Convierte fecha aaaammdd a dd/mm/aaaa
  public static String ymd_dmy(String fec) {
    if (fec.length() >= 8) {
      fec = fec.substring(fec.length() - 2, fec.length()) + "-" + fec.substring(4, 6) + "-" + fec.substring(0, 4);
    }
    return fec;
  }

  //Convierte fecha aaaammdd a dd/mm/aa 
  public static String ymd_dmyc(String fec) {
    if (fec.length() >= 8) {
      fec = fec.substring(fec.length() - 2, fec.length()) + "-" + fec.substring(4, 6) + "-" + fec.substring(2, 4);
    }
    return fec;
  }

  //obtiene dia de la fecha aaaammdd
  public static String ymd_dd(String fec) {
    String dd = "00";
    if (fec.length() >= 8) {
      dd = fec.substring(fec.length() - 2, fec.length());
    }
    return dd;
  }

  //obtiene mes de la fecha aaaammdd
  public static String ymd_mm(String fec) {
    String mm = "00";
    if (fec.length() >= 8) {
      mm = fec.substring(4, 6);
    }
    return mm;
  }

  //obtiene ano de la fecha aaaammdd
  public static String ymd_yy(String fec) {
    String yy = "0000";
    if (fec.length() >= 8) {
      yy = fec.substring(0, 4);
    }
    return yy;
  }

  // dia Hoy
  public static int ddhoy() {
    GregorianCalendar calendario = new GregorianCalendar();
    int dia = calendario.get(Calendar.DAY_OF_MONTH);
    return dia;
  }

  // Mes Hoy
  public static int mmhoy() {
    GregorianCalendar calendario = new GregorianCalendar();
    int mes = calendario.get(Calendar.MONTH) + 1;
    return mes;
  }

  // Ano Hoy
  public static int yyhoy() {
    GregorianCalendar calendario = new GregorianCalendar();
    int ano = calendario.get(Calendar.YEAR);
    return ano;
  }

  // Fecha Hrmm Hoy (yyyyMMddHHmm)
  public static String ymdhoyhhmm() {
    Date Hoy = new Date();
    SimpleDateFormat fd = new SimpleDateFormat("yyyyMMddHHmm");
    String fech = fd.format(Hoy);
    return fech;
  }

  // SUMA / RESTAS DIAS A UNA FECHA
  public static java.util.Date sumaDias1(java.util.Date fecha, int dias) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(fecha);
    cal.add(Calendar.DAY_OF_YEAR, dias);
    return cal.getTime();
  }

  public static String getFecDias(String fec, int dias) {
    fec = fec.replace("O", "0");
    fec = fec.replace("|", "0");
    if (isNumeric(fec) && fec.length() == 8) {
      try {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        Date fd = df.parse(fec);
        cal.setTime(fd);
        cal.add(Calendar.DAY_OF_YEAR, dias);
        fd = cal.getTime();
        fec = df.format(fd);
      } catch (ParseException ex) {
        Logger.getLogger(MetodosComunes.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return fec;
  }

  // Dias entre dos Fechas 
  public static int getdiasFec(String fec1, String fec2) {
    fec1 = fec1.replace("O", "0");
    fec2 = fec2.replace("O", "0");
    fec1 = fec1.replace("|", "1");
    fec2 = fec2.replace("|", "1");
    long dias = 0, dif = 0;
    if (isNumeric(fec1) && isNumeric(fec2)) {
      if (fec1.length() == 8 && fec2.length() == 8) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
          Date f1 = df.parse(fec1);
          Date f2 = df.parse(fec2);
          dif = f1.getTime() - f2.getTime();
          dias = dif / (1000 * 60 * 60 * 24);
        } catch (ParseException ex) {
          Logger.getLogger(MetodosComunes.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    } else {
      System.out.println("no numeric fec1=" + fec1 + ",fec2=" + fec2);
    }
    return (int) dias;
  }

  public static boolean isvalidFec(String fec, int indhoy) {
    boolean valfec = false;
    if (fec.length() == 8) {
      if (isNumeric(fec)) {
        String anx = fec.substring(0, 4);
        String mex = fec.substring(4, 6);
        String dix = fec.substring(fec.length() - 2, fec.length());

        int ano = Integer.valueOf(anx);
        int mes = Integer.valueOf(mex);
        int dia = Integer.valueOf(dix);

        if (validarDia(dia)) {
          valfec = true;
          if (validarMes(dia, mes)) {
            valfec = true;
            if (validarAno(dia, mes, ano)) {
              valfec = true;
              // Fecha mayor a Hoy
              if (indhoy == 1) {
                valfec = true;
              } else {
                if (fec.compareTo(ymdhoy()) <= 0) {
                  valfec = true;
                } else {
                  valfec = false;
                }
              }

            } else {
              valfec = false;
            }
          } else {
            valfec = false;
          }
        } else {
          valfec = false;
        }
      }
    }
    return valfec;
  }

  //Método para validar el día
  private static boolean validarDia(int dia) {
    if (dia == 0) {
      return false;
    } else if (dia > 31) {
      return false;
    } else if (dia < 0) {
      return false;
    } else {
      return true; //Día correcto
    }
  }

  private static boolean validarMes(int dia, int mes) {
    if (mes == 0) {
      return false;
    } else if (mes < 0) {
      return false;
    } else if (mes > 12) {
      return false;
    } else if (dia > 29 && mes == 2) {
      return false;
    } else if (dia == 31) {
      //Inaceptable para determinados meses
      switch (mes) {
        case 2:
        case 4:
        case 6:
        case 9:
        case 11:
          return false;
        default:
          return true;
      }
    } else {
      return true;
    }
  }

  private static boolean validarAno(int dia, int mes, int anio) {
    if (anio == 0) {
      return false;
    } else if (mes == 2 && dia == 29) {
      if (esBisiesto(anio)) {
        return true;
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  private static boolean esBisiesto(int anio) {
    return (anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0));
  }

  // Nombre del mes
  public static String mm_name(int nmes) {
    String[] meses = new String[]{
      "Enero",
      "Febrero",
      "Marzo",
      "Abril",
      "Mayo",
      "Junio",
      "Julio",
      "Agosto",
      "Septiembre",
      "Octubre",
      "Noviembre",
      "Diciembre"};
    String mes = meses[nmes];
    return mes;
  }

//Suma o resta las horas recibidos a la fecha  
  public static Date sumarRestarHorasFecha(Date fecha, int horas) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(fecha);             // Configuramos la fecha que se recibe
    calendar.add(Calendar.HOUR, horas);  // numero de horas a añadir, o restar en caso de horas<0
    return calendar.getTime();           // Devuelve el objeto Date con las nuevas horas añadidas
  }

  // Obtiene formato HH:MM pm/am
  public static String HoraHHMM(String Fec) {
    String hora = "";
    if (Fec.length() == 8 && isNumeric(Fec)) {
      try {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        Date hor = df.parse(Fec);
        Date min = df.parse(Fec);
        Date seg = df.parse(Fec);
        //Date hor = new Date();
        //Date min = new Date();
        //Date seg = new Date();
        String h = String.valueOf(hor.getHours());
        String m = String.valueOf(min.getMinutes());
        String s = String.valueOf(seg.getSeconds());

        if (h.length() == 1) {
          h = '0' + h;
        }
        if (m.length() == 1) {
          m = '0' + m;
        }
        hora = laHora(h) + ":" + m + " " + meridiano(h);
      } catch (ParseException ex) {
        Logger.getLogger(MetodosComunes.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return (hora);
  }

  // establecerHora
  public static String laHora(String ho) { // Convierte la hora militar
    int a = Integer.parseInt(ho);
    String horas[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};
    String retorno = "";
    if (a == 0) {
      retorno = "12";
    } else if (a >= 13 && a <= 23) {
      retorno = horas[a - 13];
    } else {
      retorno = ho;
    }
    return (retorno);
  }// hora

  //Meridiano Hora
  public static String meridiano(String ho) { // Establece si la hora es pm o am
    int b = Integer.parseInt(ho);
    String retorno = "";
    if (b >= 12 && b <= 23) {
      retorno = "pm";
    } else {
      retorno = "am";
    }
    return (retorno);
  }

  // Dias/horas/minutos/segundos  entre dos Fechas 
  public static String DifHrMiSe(String fec1, String fec2) {

    int dias = 0;
    int horas = 0;
    int minutos = 0;
    int segundos = 0;
    String hhmmsg = "";
    if (isNumeric(fec1) && isNumeric(fec2)) {
      if (fec1.length() == 8 && fec2.length() == 8) {
        try {
          fec1 = fec1.trim();
          fec2 = fec2.trim();
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
          Date fechaIni = dateFormat.parse(fec1);
          Date fechaFin = dateFormat.parse(fec2);
          int diferencia = (int) ((fechaFin.getTime() - fechaIni.getTime()) / 1000);
          if (diferencia > 86400) {
            dias = (int) Math.floor(diferencia / 86400);
            diferencia = diferencia - (dias * 86400);
          }
          if (diferencia > 3600) {
            horas = (int) Math.floor(diferencia / 3600);
            diferencia = diferencia - (horas * 3600);
          }
          if (diferencia > 60) {
            minutos = (int) Math.floor(diferencia / 60);
            diferencia = diferencia - (minutos * 60);
          }
          segundos = diferencia;
          String hr = String.valueOf(horas);
          if (hr.length() == 1) {
            hr = "0" + hr;
          }
          String mi = String.valueOf(minutos);
          if (mi.length() == 1) {
            mi = "0" + mi;
          }
          String sg = String.valueOf(segundos);
          if (sg.length() == 1) {
            sg = "0" + sg;
          }
          hhmmsg = hhmmsg + hr + ":" + mi + ":" + sg;
        } catch (ParseException ex) {
          Logger.getLogger(MetodosComunes.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return hhmmsg;
  }

  public static boolean isNumeric(String cadena) {    // Valida si es nmerico
    if (cadena.matches("[0-9]+(\\.[0-9][0-9]?)?")) {
      return true;
    } else {
      return false;
    }
  }

  // Fijar tamaño imagen en label
  public static void pintarImagen(JLabel lbl, String rut) {
    ImageIcon imageIcon = new ImageIcon(new ImageIcon(rut).getImage().getScaledInstance(lbl.getWidth(), lbl.getWidth(), Image.SCALE_DEFAULT));
    lbl.setIcon(imageIcon);
  }

  /*
  // Valida que sea numerico
  public static boolean isNumeric(String cadena) {  // Valida si es nmerico
    try {
      Double.parseDouble(cadena);
      return true;
    } catch (NumberFormatException nfe) {
      return false;
    }
  }
   */
  //Obtiene Cociente
  public static int GetCociente(int Dividendo, int Divisor) {
    int p = 1, q = 0;
    if (Dividendo >= Divisor) {
      while ((Dividendo - Divisor) >= q) {
        q = Divisor * p;
        p++;
      }
    }
    return (p - 1);
  }

  public static String num = "";

  public static String DivDec(long dividendo, long divisor) {

    num = "";
    assert (dividendo >= 0 && divisor > 0);
    // Primero la parte entera
    long cocienteEntero = dividendo / divisor;
    // Ahora la parte decimal
    dividendo %= divisor;
    List<Long> listaDividendos = new ArrayList<>();
    List<Long> listaCocientes = new ArrayList<>();
    dividendo *= 10;
    while (!listaDividendos.contains(dividendo)) {
      if (dividendo == 0) {
        imprimeNumeroSinInfinitosDecimales(
                cocienteEntero, listaCocientes);
        //return;
      }
      listaDividendos.add(dividendo);
      listaCocientes.add(dividendo / divisor);
      dividendo %= divisor;
      dividendo *= 10;
    }
    // Localizar donde empieza la parte periódica
    long indiceInicioPeriodo = listaDividendos.indexOf(dividendo);
    // Imprimimos la parte entera y el separador decimal

    //System.out.print(cocienteEntero);
    num = Long.toString(cocienteEntero);

    //System.out.print(",");
    num = num + ",";

    //num="";
    // Imprimir la parte no periódica
    int i = 0;
    for (; i < indiceInicioPeriodo; ++i) {
      //System.out.print(listaCocientes.get(i).longValue());

      num = num + Long.toString(listaCocientes.get(i).longValue());

    }
    // Imprimir un subrayado para indicar el inicio de la parte periódica
    //System.out.print("_");
    // Imprimir la parte periódica

    for (; i < listaCocientes.size(); ++i) {
      //System.out.print(listaCocientes.get(i).longValue());
      //System.out.print(listaCocientes.get(i).longValue());

      num = num + Long.toString(listaCocientes.get(i).longValue());
      num = num + Long.toString(listaCocientes.get(i).longValue());

    }
    // Y unos puntitos para indicar que se repiten
    String x = num.substring(num.length() - 1, num.length());
    if (x.equals(",")) {
      num = num + "00";
    }
    return num;
  }

  // Formato Monto moneda Español #.##0,00
  public static String MtoEs(double Imp, int dec) {
    String MtoF = "0,00";
    //simbolo.setDecimalSeparator('.');   // Formato En
    //simbolo.setGroupingSeparator(',');  // Formato En
    simbolo.setDecimalSeparator(',');     // Formato Es
    simbolo.setGroupingSeparator('.');    // Formato Es
    // 2 Decimales
    if (dec == 2) {
      if (Imp != 0.0) {
        DecimalFormat df = new DecimalFormat("###,##0.00", simbolo);
        MtoF = df.format(Imp).trim();
        if (MtoF.indexOf(',') == -1) {
          MtoF = MtoF + ",00";
        } else {
          String N = MtoF.substring(MtoF.indexOf(',') + 1, MtoF.length());
          if (N.length() == 1) {
            MtoF = MtoF + "0";
          }
        }
      }
      MtoF = MtoF.replace("-0,00", "0,00").replace("0,00.00", "0,00");
    } else {
      // 1 Decimal
      if (dec == 1) {
        MtoF = "0,0";
        if (Imp != 0.0) {
          DecimalFormat df = new DecimalFormat("###,##0.0", simbolo);
          MtoF = df.format(Imp).trim();
          if (MtoF.indexOf(',') == -1) {
            MtoF = MtoF + ".0";
          }
        }
        MtoF = MtoF.replace("-0,0", "0,0").replace("0,00.0", "0,0");
      } else {
        // 0 Decimal
        if (dec == 0) {
          MtoF = "0";
          if (Imp != 0.0) {
            DecimalFormat df = new DecimalFormat("###,##0", simbolo);
            MtoF = df.format(Imp).trim();
          }
          MtoF = MtoF.replace("-0 ", "0 ");
        } else {
          MtoF = "0";
          DecimalFormat df = new DecimalFormat("###0", simbolo);
          if (Imp != 0.0) {
            MtoF = df.format(Imp).trim();
          }
        }
      }
    }
    return MtoF;
  }

  //Convierte formato Monto (En/Es) a double 
  public static String GetCurrencyDouble(String sVal) {
    String dec = ".";
    if (sVal.length() >= 3) {
      dec = sVal.substring(sVal.length() - 3, sVal.length() - 2);
    }
    if (dec.equals(",")) {
      sVal = sVal.replace(".", "");
      sVal = sVal.replace(",", ".");
    } else {
      sVal = sVal.replace(",", "");
    }
    if (sVal.equals("")) {
      sVal = "0.00";
    }
    return sVal;
  }

  //Devuelve valor numerico
  public static Double GetMtoDouble(String Imp) {
    Imp = Imp.trim();
    if (Imp == null || Imp.equals("")) {
      Imp = "0.00";
    } else {
      Imp = Imp.replace("'", "");
      String mto = Imp.replace(",", "");
      if (mto.length() == 1) {
        mto = mto + ".00";
      }
      if (Imp.equals("-")) {
        Imp = "0,00";
      }
      if (!isNumeric(mto.trim())) {
        Imp = "0,00";
      }
    }
    return Double.parseDouble(GetFmtoMto(Imp));
  }

  //Convierte formato Ingles a double 
  public static String GetFmtoMto(String sVal) {
    sVal = sVal.replace(",", "");
    if (sVal.equals("")) {
      sVal = "0,00";
    }
    return sVal;
  }

  public static void imprimeNumeroSinInfinitosDecimales(
          long cocienteEntero, List<Long> listaCocientes) {
    num = Long.toString(cocienteEntero);
    num = num + ",";
    for (long n : listaCocientes) {
      //System.out.print(n);
      num = num + Long.toString(n);
    }
  }

  // Inicia Palabras en Mayuscula
  public static String InicMayuscula(String vax) {
    char[] caracteres = vax.toCharArray();
    caracteres[0] = Character.toUpperCase(caracteres[0]);
    for (int i = 0; i < vax.length() - 2; i++) {
      if (caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ',') {
        caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1]);
      }
    }
    vax = new String(caracteres);
    return vax;
  }

  public static boolean isFileInUsed(String Fil) {
    boolean isFileUnlocked = false;
    File archivoXLS = new File(Fil);
    try {
      org.apache.commons.io.FileUtils.touch(archivoXLS);
      isFileUnlocked = false;
    } catch (IOException ex) {
      isFileUnlocked = true;
      Logger.getLogger(MetodosComunes.class.getName()).log(Level.SEVERE, null, ex);
    }
    return isFileUnlocked;
  }

  // Valida si existe archivo
  public static boolean FileExist(String Fil) {
    String fileExcel = Fil;
    File archivoXLS = new File(fileExcel);
    if (archivoXLS.exists()) {
      if (archivoXLS.canWrite()) {
        //System.out.println("puede grabar");
      }
      return true;
    } else {
      return false;
    }
  }

  // Pregunta si se puede grabar el archivo
  public static boolean FileSave(String fil) {
    int inde = 0;
    File FilePdf = new File(fil);
    if (FilePdf.exists()) {
      if (!FilePdf.delete()) {
        System.out.println("no se pudo borrar!");
        String tit = "* AVISO *";
        long tim = 1500;
        String vax = "Cierre el " + fil;
        Mensaje msg = new Mensaje(vax, tit, tim, null);
        inde = 1;
      }
    }
    if (inde == 0) {
      return true;
    } else {
      return false;
    }
  }

  // Copiar Archivo 
  public static boolean CopyFile(String org, String des) {
    int ind = 0;
    FileChannel inputChannel = null;
    FileChannel outputChannel = null;
    try {
      inputChannel = new FileInputStream(org).getChannel();
      outputChannel = new FileOutputStream(des).getChannel();
      outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
    } catch (FileNotFoundException ex) {
      ind = 1;
      Logger.getLogger(MetodosComunes.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      ind = 1;
      Logger.getLogger(MetodosComunes.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      try {
        inputChannel.close();
        outputChannel.close();

      } catch (IOException ex) {
        ind = 1;
        Logger.getLogger(MetodosComunes.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    if (ind == 0) {
      return true;
    } else {
      return false;
    }
  }

  // Valida ip
  public static boolean ValidaIP(String IP) {
    int i = 0, ind = 0;
    String pingCommand = "ping -n 3 " + IP;
    ProcessBuilder builder = new ProcessBuilder(new String[]{"cmd.exe", "/C", pingCommand});
    try {
      Process newProcess = builder.start();
      BufferedReader in = new BufferedReader(new InputStreamReader(newProcess.getInputStream()));
      String inputLine = in.readLine();
      i = 1;
      while (inputLine != null && i <= 3) {
        if (inputLine.length() > 0) {
          if (inputLine.contains("tiempo=") || inputLine.contains("tiempo<") || inputLine.contains("time=") || inputLine.contains("time<")) {
            System.out.println(IP + " - si responde!");
            ind = 0;
            break;
          }
          if (i >= 3) {
            if (inputLine.contains("unreachable") || inputLine.contains("not find host") || inputLine.contains("inaccesible") || inputLine.contains("out")) {
              //JOptionPane.showMessageDialog(this, AppLocal.getIntString("NO HAY CONEXION A INTERNET a "+ip+" , NO SE PUEDE ENVIAR CORREOS "), AppLocal.getIntString("** AVISO **"), JOptionPane.INFORMATION_MESSAGE);
              System.err.println("No se pudo establecer conexión web a traves de " + IP);
              ind = 1;
              break;
            }
          }
        }
        inputLine = in.readLine();
      }
    } catch (IOException ex) {
      System.out.println(IP + " - no responde!");
    }

    if (ind == 0) {
      return true;
    } else {
      return false;
    }
  }

  // Valida Email valido
  public static boolean validaEmail(String eml) {
    eml = eml.trim();
    Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    Matcher mather = pattern.matcher(eml);
    if (mather.find() == true) {
      System.out.println("**El email " + eml + "  ingresado es válido.");
      return true;
    } else {
      System.out.println("**El email " + eml + " ingresado es inválido.");
      return false;
    }
  }

  //pulsa tecla Tabulador (Tab->)
  public static void TeclaAltTab() {
    try {
      robot = new Robot();
      robot.delay(50);
      robot.keyPress(KeyEvent.VK_TAB);
      robot.delay(50);
      robot.keyRelease(KeyEvent.VK_TAB);
    } catch (AWTException ex) {
      ex.printStackTrace();
    }
  }
}
