package gestionFZ;

import static comun.MetodosComunes.FileSave;
import static comun.MetodosComunes.dmyhoy;
import static comun.MetodosComunes.getdiasFec;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymd_dmyc;
import static comun.MetodosComunes.ymdhoy;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import modelo.ConexionSQL;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import static gestionFZ.Consulta_PagosClientes.labMsgPg;
import static gestionFZ.Consulta_PagosClientes.labRutPdfP;

public class Reporte_ExcelPagos {

  private String nomexc = "", Fed = "", Feh = "";
  private int fil = 0;
  private String ftoimp = "";

  private File archivoXLS;
  private SXSSFWorkbook Libro = new SXSSFWorkbook(2500);   // ( poi-ooxml-3.9 -  poi-ooxml-schemas-3.9 - dom4j-1.6 - xmlbeans)
  private SXSSFSheet Hoja;
  private Row fila;
  private Cell Header, Detail;

  // Font normal negra
  private XSSFCellStyle cellStyleC;
  private XSSFCellStyle cellStyleD;
  private XSSFCellStyle cellStyleD0;
  private XSSFCellStyle cellStyleI;
  // Font normal azul
  private XSSFCellStyle cellStyleCA;
  // Font Bold  Blanca / fondo A,V,G
  private XSSFCellStyle cellStyleBCA;
  private XSSFCellStyle cellStyleBCT;
  private XSSFCellStyle cellStyleBIA;
  private XSSFCellStyle cellStyleBCV;
  private XSSFCellStyle cellStyleBDV;
  private XSSFCellStyle cellStyleBCG;
  private XSSFCellStyle cellStyleBDG;
  private XSSFCellStyle cellStyleBCO;

  private XSSFCellStyle cellStyleVV;
  private XSSFCellStyle cellStyleRR;
  private XSSFCellStyle cellStyleCPV;
  private XSSFCellStyle cellStyleCPR;
  private XSSFCellStyle cellStyleCPN;

  //Iterator cells;
  XSSFRow row;
  XSSFCell cell;

  public Reporte_ExcelPagos(String fed, String feh) {

    Fed = fed;
    Feh = feh;

    nomexc = "Reportepagos_" + fed + "_" + feh + ".xlsx";
    String Fil = "exc/" + nomexc;

    if (FileSave(Fil)) {
      labMsgPg.setText("- Generando Excel " + nomexc);
      StyleExcel();
      String vax = "";
      Sheet sheet = Libro.getSheet(vax);  // ( poi-3.9 )
      //  LISTA DE PRECIOS IMPORTADORA
      vax = "Pagos";
      creaHoja(vax, "A4:I4");
      HeaderResExcel();
      DetallResExcel();
      // Graba Archivo Excel
      creaReporteExcel(nomexc);
    } else {
      labMsgPg.setText("- Cierre Excel " + nomexc);
    }
  }

  // Crear Excel
  public void creaReporteExcel(String NomExc) {
    //Presenta Excel
    String Fil = "exc/" + NomExc;
    archivoXLS = new File(Fil);
    try {
      FileOutputStream archivo = new FileOutputStream(archivoXLS);
      Libro.write(archivo);
      archivo.flush();
      archivo.close();
      // muestra en pantalla excel
      labMsgPg.setText("");
      labMsgPg.setForeground(new java.awt.Color(153, 0, 0));
      labMsgPg.setText(" - Listo!");
      labMsgPg.setForeground(new java.awt.Color(0, 0, 102));
      labRutPdfP.setBackground(Color.green);
      //Desktop.getDesktop().open(archivoXLS);
      // Excel esta abierto  
    } catch (IOException ex) {
      ImageIcon icon = new ImageIcon(getClass().getResource("/img/aut.jpg"));
      String msg = "- CIERRE EL EXCEL\n ( " + nomexc + " )\nPARA PODER EJECUTAR\nEL PROCESO";
      String[] options = {" PULSA ENTER PARA CERRAR "};
      JOptionPane.showOptionDialog(null, msg, "***** AVISO ****", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, icon, options, options[0]);
    }
  }

  // Detalle ResumenExcel
  public void DetallResExcel() {

    int can = 0;
    int nrc = 0;
    int nno = 0;
    int dip = 0;
    int dia = 0;
    String noc = "";
    String frc = "";
    String fno = "";
    String fen = "";
    String fev = "";
    String fpa = "";
    double tno = 0;
    double tgn = 0;

    ConexionSQL bdsql = new ConexionSQL();
    Connection con = bdsql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT "
              + "x.nrc, y.frc,x.coc,(select noc from clientes where x.coc=clientes.coc) noc,"
              + "nno,nfa,fno,fen,fev,fpa,tdo,tdn "
              + "FROM recibocobroD x,recibocobroH y "
              + "where x.nrc=y.nrc "
              + "  and frc between '" + Fed + "' and '" + Feh + "' "
              + "order by noc,x.nrc desc,nno";

      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        nrc = rs.getInt("nrc");
        nno = rs.getInt("nno");
        frc = rs.getString("frc");
        noc = rs.getString("noc");
        fno = rs.getString("fno");
        fen = rs.getString("fen");
        fev = rs.getString("fev");
        fpa = rs.getString("fpa");
        tno = rs.getDouble("tdo");
        if (noc.length() > 40) {
          noc = noc.substring(0, 40);
        }
        noc = noc.toUpperCase();
        tgn = tgn + tno;
        dip = 0;
        dia = 0;

        // Fecha Nota
        String fnx = "";
        if (fno.length() == 8) {
          fnx = ymd_dmy(fno);
        }

        // Fecha Vence
        String fvx = "";
        if (fev.length() == 8) {
          fvx = ymd_dmy(fev);
        }

        // Fecha recibe
        String fex = "";
        if (fen.length() == 8) {
          fex = ymd_dmy(fen);
        }

        // Fecha pago
        String feh = ymdhoy();
        String fpx = "";
        String frx = fno;
        if (fen.length() == 8) {
          frx = fen;
        }
        if (fpa.length() == 8) {
          dip = getdiasFec(fpa, frx);
          fpx = ymd_dmy(fpa);
        } else {
          dip = getdiasFec(feh, frx);
        }

        int div = 0;
        if (fno.length() == 8 && fev.length() == 8) {
          div = getdiasFec(fev, fno);
        }

        if (dip < 0) {
          dip = 0;
        }

        dia = div - dip;
        if (dia > 0) {
          dia = 0;
        }

        if (tno < 0) {
          dip = 0;
          dia = 0;
        }

        if (dia < 0) {
          dia = dia * -1;
        }

        fil = fil + 1;
        fila = Hoja.createRow(fil);

        // Cliente
        int j = 0;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleI);
        Detail.setCellValue(noc);

        j = 1;
        // recibo/Fecha
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(nrc + "   " + ymd_dmyc(frc));

        j = 2;
        // Nota
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(nno);

        j = 3;
        // Fecha nots
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(fnx);

        j = 4;
        // Fecha vence
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(fvx);

        // fecha recibe
        j = 5;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(fex);

        // fecha pago
        j = 6;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(fpx);

        // dias pago
        j = 7;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(dip);

        // dias ret
        j = 8;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(dia);

        // Total Nota
        j = 9;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleD);
        Detail.setCellValue(tno);

        can++;
      }

      fil = fil + 1;
      fila = Hoja.createRow(fil);

      int j = 0;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("TOTAL COBROS => ");

      // cantidad
      j = 1;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      j = 2;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue(can);

      j = 3;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      j = 4;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      j = 5;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      j = 6;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      j = 7;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      j = 8;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      // Total nota$
      j = 9;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBDG);
      Detail.setCellValue(tgn);

      // Pendientes
      fil = fil + 1;
      fila = Hoja.createRow(fil);

      can = 0;
      tgn = 0;

      sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.Notas ("
              + "noc  varchar(50),"
              + "nno  int,"
              + "fno  varchar(10),"
              + "fev  varchar(10),"
              + "fee  varchar(10),"
              + "ton  Decimal(15,2)) "
              + "NOT LOGGED ON COMMIT PRESERVE ROWS";
      st.execute(sql);

      // notaent
      sql = "insert into SESSION.Notas "
              + "SELECT "
              + "(select nom from clientes where clientes.coc=notaent.coc) noc,"
              + "nne nno,fne fno, fev,fee,tne-tdn tno "
              + "FROM notaent "
              + "Where nne not in (select nno from recibocobroD where tpd=0)";
      st.execute(sql);

      // notacred
      sql = "insert into SESSION.Notas "
              + "SELECT "
              + "(select nom from clientes where clientes.coc=notacred.coc) noc,"
              + "ncr nno,fnc fno, '' fev,'' fee, tnc*-1 tno "
              + "FROM notacred "
              + "Where ncr not in (select nno from recibocobroD where tpd=1)";
      st.execute(sql);

      sql = "SELECT noc,nno,fno,fev,fee,ton "
              + "FROM SESSION.Notas "
              + "order by noc,nno";

      rs = st.executeQuery(sql);
      while (rs.next()) {
        noc = rs.getString("noc");
        nno = rs.getInt("nno");
        fno = rs.getString("fno");
        fev = rs.getString("fev");
        fen = rs.getString("fee");
        tno = rs.getDouble("ton");

        tgn = tgn + tno;
        dip = 0;
        dia = 0;

        if (noc.length() > 40) {
          noc = noc.substring(0, 40);
        }
        noc = noc.toUpperCase();

        // Fecha Nota
        String fnx = "";
        if (fno.length() == 8) {
          fnx = ymd_dmy(fno);
        }

        // Fecha Vence
        String fvx = "";
        if (fev.length() == 8) {
          fvx = ymd_dmy(fev);
        }

        // Fecha recibe
        String fex = "";
        if (fen.length() == 8) {
          fex = ymd_dmy(fen);
        }

        // Fecha pago
        String feh = ymdhoy();
        String fpx = "";
        String frx = fno;
        if (fen.length() == 8) {
          frx = fen;
        }
        dip = getdiasFec(feh, frx);

        int div = 0;
        if (fno.length() == 8 && fev.length() == 8) {
          div = getdiasFec(fev, fno);
        }

        if (dip < 0) {
          dip = 0;
        }

        dia = div - dip;
        if (dia > 0) {
          dia = 0;
        }

        if (tno < 0) {
          dip = 0;
          dia = 0;
        }

        if (dia < 0) {
          dia = dia * -1;
        }

        fil = fil + 1;
        fila = Hoja.createRow(fil);

        // Cliente
        j = 0;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleI);
        Detail.setCellValue(noc);

        j = 1;
        // recibo
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue("");

        j = 2;
        // Nota
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(nno);

        j = 3;
        // Fecha nots
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(fnx);

        j = 4;
        // Fecha vence
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(fvx);

        // fecha recibe
        j = 5;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(fex);

        // fecha pago
        j = 6;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(fpx);

        // dias pago
        j = 7;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(dip);

        // dias ret
        j = 8;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleC);
        Detail.setCellValue(dia);

        // Total Nota
        j = 9;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleD);
        Detail.setCellValue(tno);

        can++;

      }

      fil = fil + 1;
      fila = Hoja.createRow(fil);

      j = 0;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("TOTAL PENDIENTES => ");

      // cantidad
      j = 1;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      j = 2;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue(can);

      j = 3;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      j = 4;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      j = 5;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      j = 6;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      j = 7;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      j = 8;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("");

      // Total nota$
      j = 9;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBDG);
      Detail.setCellValue(tgn);

      con.close(); // cerramos la conexion

      //ajustaHoja(4);
      for (int w = 0; w <= 9; w++) {
        Hoja.setHorizontallyCenter(true);
        if (w == 0) {
          Hoja.setDefaultColumnWidth(60);
        } else {
          Hoja.autoSizeColumn(w);
        }
      }

      // caso errores sql
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
    }

  }

  // Crear Hoja Excel-Filtro
  public void creaHoja(String Hoj, String Fil) {
    Hoja = null;
    labMsgPg.setText(" - Generando Hoja " + Hoj);
    Hoja = (SXSSFSheet) Libro.createSheet(Hoj);
    Hoja.createFreezePane(0, 3);   // inmoviliza panel
    //Hoja.setAutoFilter(CellRangeAddress.valueOf(Fil));   // Aplica Filtro 
    Hoja.setDisplayGridlines(false);
  }

  // Header Excel Empresas
  public void HeaderResExcel() {

    fil = 0;
    fila = Hoja.createRow(fil);

    Header = fila.createCell(0);
    Header.setCellStyle(cellStyleBCT);
    Header.setCellValue("REPORTE DE COBROS del   " + ymd_dmy(Fed) + "   al   " + ymd_dmy(Feh));

    Header = fila.createCell(9);
    Header.setCellStyle(cellStyleC);
    Header.setCellValue(dmyhoy());

    fil = 2;
    fila = Hoja.createRow(fil);
    String VH[] = {"Cliente ", "  Recibo     Fecha ", "    Nota   ", "    F.Nota    ", "     F.Vence     ", "    F.Recibe     ", "    F.Pago    ", "   D.Pago  ", "   D.Atraso   ", "  T.Nota $"};

    int c = VH.length;
    for (int i = 0; i < c; i++) {
      Header = fila.createCell(i);
      Header.setCellStyle(cellStyleBCA);     // Azul oscuro font blanca
      if (i == 0) {
        Header.setCellStyle(cellStyleBIA);   // Azul oscuro font blanca izq
      }
      Header.setCellValue(VH[i] + "   ");
    }
  }

  // Estilos Hoja
  public void StyleExcel() {

    ftoimp = "#,##0.00";
    //Formato Decimal 
    Workbook wb = new HSSFWorkbook();
    org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("format sheet");
    DataFormat format = wb.createDataFormat();

    // font letra normal
    XSSFFont font = (XSSFFont) Libro.createFont();
    font.setFontName("Calibri");
    font.setFontHeightInPoints((short) 11);

    // font letra azul
    XSSFFont fontA = (XSSFFont) Libro.createFont();
    fontA.setFontName("Calibri");
    fontA.setFontHeightInPoints((short) 11);
    fontA.setColor(HSSFColor.DARK_TEAL.BLUE.index);

    // font letra roja
    XSSFFont fontR = (XSSFFont) Libro.createFont();
    fontR.setFontName("Calibri");
    fontR.setFontHeightInPoints((short) 11);
    fontR.setColor(HSSFColor.DARK_RED.index);

    // font letra Negrita
    XSSFFont fontB = (XSSFFont) Libro.createFont();
    fontB.setFontName("Calibri");
    fontB.setFontHeightInPoints((short) 11);
    fontB.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

    // font letra Azul Oscuro 
    XSSFFont fontAB = (XSSFFont) Libro.createFont();
    fontAB.setFontName("Calibri");
    fontAB.setColor(HSSFColor.DARK_BLUE.index);
    fontAB.setBold(true);
    fontAB.setFontHeightInPoints((short) 11);
    fontAB.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

    // font letra Blanca
    XSSFFont fontBB = (XSSFFont) Libro.createFont();
    fontBB.setFontName("Calibri");
    fontBB.setColor(HSSFColor.WHITE.index);
    fontBB.setBold(true);
    fontBB.setFontHeightInPoints((short) 11);
    fontBB.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

    // Estilo Font negra Normal ( Centrado )
    cellStyleC = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleC.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleC.setFillBackgroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
    cellStyleC.setDataFormat(format.getFormat("0"));
    cellStyleC.setFont(font);

    // Estilo Font negra Normal ( Derecha )
    cellStyleD = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleD.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
    cellStyleD.setFillBackgroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
    cellStyleD.setDataFormat(format.getFormat(ftoimp));
    cellStyleD.setFont(font);

    // Estilo Font negra Normal ( Derecha )
    cellStyleD0 = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleD0.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
    cellStyleD0.setFillBackgroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
    cellStyleD0.setDataFormat(format.getFormat(ftoimp));
    cellStyleD0.setFont(font);

    // Estilo Font negra Normal ( Izquierda )
    cellStyleI = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleI.setAlignment(XSSFCellStyle.ALIGN_LEFT);
    cellStyleI.setFillBackgroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
    cellStyleI.setFont(font);

    // Estilo Font negra Normal ( Centrado )
    cellStyleCA = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleCA.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleCA.setFillBackgroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
    cellStyleCA.setDataFormat(format.getFormat("0"));
    cellStyleCA.setFont(fontA);

    // Estilo Font Blanca Bold  Fondo Azul Oscuro ( Centrado )
    cellStyleBCT = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleBCT.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleBCT.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    cellStyleBCT.setFillForegroundColor(HSSFColor.TEAL.index);
    cellStyleBCT.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    cellStyleBCT.setWrapText(true);
    cellStyleBCT.setFont(fontBB);

    // Estilo Font Blanca Bold  Fondo Azul Oscuro ( Centrado )
    cellStyleBCA = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleBCA.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleBCA.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    cellStyleBCA.setFillForegroundColor(HSSFColor.DARK_TEAL.index);
    cellStyleBCA.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    cellStyleBCA.setDataFormat(format.getFormat("0"));
    cellStyleBCA.setWrapText(true);
    cellStyleBCA.setFont(fontBB);

    // Estilo Font Blanca Bold  Fondo Azul Oscuro ( Izquierda )
    cellStyleBIA = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleBIA.setAlignment(XSSFCellStyle.ALIGN_LEFT);
    cellStyleBIA.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    cellStyleBIA.setFillForegroundColor(HSSFColor.DARK_TEAL.index);
    cellStyleBIA.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    cellStyleBIA.setDataFormat(format.getFormat("0"));
    cellStyleBIA.setWrapText(true);
    cellStyleBIA.setFont(fontBB);

    // Estilo Font Blanca Bold  Fondo verde ( Centrado )
    cellStyleBCV = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleBCV.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleBCV.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    cellStyleBCV.setFillForegroundColor(HSSFColor.GREEN.index);
    cellStyleBCV.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    cellStyleBCV.setDataFormat(format.getFormat("0"));
    cellStyleBCV.setFont(fontBB);

    // Estilo Font Blanca Bold  Fondo verde ( Centrado )
    cellStyleBDV = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleBDV.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleBDV.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    cellStyleBDV.setFillForegroundColor(HSSFColor.GREEN.index);
    cellStyleBDV.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    cellStyleBDV.setDataFormat(format.getFormat("0"));
    cellStyleBDV.setFont(fontBB);

    // Estilo Font Blanca Bold  Fondo Gris ( Centrado )
    cellStyleBCG = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleBCG.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleBCG.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    cellStyleBCG.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    cellStyleBCG.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    cellStyleBCG.setDataFormat(format.getFormat("0"));
    cellStyleBCG.setFont(fontAB);

    // Estilo Font Blanca Bold  Fondo Gris ( Derecha )
    cellStyleBDG = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleBDG.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
    cellStyleBDG.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    cellStyleBDG.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    cellStyleBDG.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    cellStyleBDG.setDataFormat(format.getFormat(ftoimp));
    cellStyleBDG.setFont(fontAB);

    // Estilo Font Blanca Bold  Fondo Gris ( Centrado )
    cellStyleBCO = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleBCO.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleBCO.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    cellStyleBCO.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
    cellStyleBCO.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    cellStyleBCO.setDataFormat(format.getFormat("0"));
    cellStyleBCO.setFont(fontBB);

    // font letra Verde
    XSSFFont fontVV = (XSSFFont) Libro.createFont();
    fontVV.setFontName("Calibri");
    fontVV.setColor(HSSFColor.GREEN.index);
    fontVV.setBold(true);
    fontVV.setFontHeightInPoints((short) 10);
    fontVV.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

    cellStyleVV = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleVV.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleVV.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
    cellStyleVV.setFillBackgroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
    cellStyleVV.setFillForegroundColor(HSSFColor.WHITE.index);
    cellStyleVV.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    cellStyleVV.setRotation((short) 90);   //30 90 120 270 360
    cellStyleVV.setFont(fontVV);

    XSSFFont fontRJ = (XSSFFont) Libro.createFont();
    fontRJ.setFontName("Calibri");
    fontRJ.setColor(HSSFColor.DARK_RED.index);
    fontRJ.setBold(true);
    fontRJ.setFontHeightInPoints((short) 10);
    fontRJ.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

    cellStyleRR = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleRR.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleRR.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
    cellStyleRR.setFillBackgroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
    cellStyleRR.setFillForegroundColor(HSSFColor.WHITE.index);
    cellStyleRR.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    cellStyleRR.setRotation((short) 90);   //30 90 120 270 360
    cellStyleRR.setFont(fontRJ);

    cellStyleCPV = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleCPV.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleCPV.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
    cellStyleCPV.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
    cellStyleCPV.setFont(font);

    cellStyleCPN = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleCPN.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleCPN.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
    cellStyleCPN.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
    cellStyleCPN.setFont(font);

    cellStyleCPR = (XSSFCellStyle) Libro.createCellStyle();
    cellStyleCPR.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    cellStyleCPR.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
    cellStyleCPR.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
    cellStyleCPR.setFont(fontR);

  }

}
