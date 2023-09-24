package gestionFZ;

import comun.Mensaje;
import static comun.MetodosComunes.FileSave;
import static comun.MetodosComunes.dmyhoy;
import static comun.MetodosComunes.ymd_dmy;
import static gestionFZ.Menu.btnCar;
import static gestionFZ.Menu.jMsg;
import static gestionFZ.Menu.jrutExc;
import static gestionFZ.Registro_CatalogoProductos.labMsgL;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFRow;

public class Reporte_ExcelListaPrecios {

  private String nomexc = "";
  private String feclis = "";
  private int fil = 0, indexc = 0;
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

  public Reporte_ExcelListaPrecios(String fec, int ind) {
    indexc = ind;
    feclis = fec;
    nomexc = "ListaPrecios_" + feclis + ".xlsx";
    String Fil = "rep/exc/" + nomexc;

    if (FileSave(Fil)) {
      if (indexc == 0) {
        jMsg.setText("- Generando Excel " + nomexc);
      } else {
        labMsgL.setText("- Generando Excel " + nomexc);
      }
      StyleExcel();
      String vax = "";
      Sheet sheet = Libro.getSheet(vax);  // ( poi-3.9 )

      //  LISTA DE PRECIOS IMPORTADORA
      vax = "CatalogoPrecios";
      creaHoja(vax, "A4:I4");
      HeaderResExcel();
      DetallResExcel();

      // Graba Archivo Excel
      creaReporteExcel(nomexc);

    } else {
      if (indexc == 0) {
        jMsg.setText("- Cierre Excel " + nomexc);
      } else {
        labMsgL.setText("- Generando Excel " + nomexc);
      }
    }

  }

  // Crear Excel
  public void creaReporteExcel(String NomExc) {
    //Presenta Excel
    String Fil = "rep/exc/" + NomExc;
    archivoXLS = new File(Fil);
    try {
      FileOutputStream archivo = new FileOutputStream(archivoXLS);
      Libro.write(archivo);
      archivo.flush();
      archivo.close();
      // muestra en pantalla excel
      //Desktop.getDesktop().open(archivoXLS);
      if (indexc == 0) {
        jMsg.setText("");
        btnCar.setEnabled(true);
        jMsg.setForeground(new java.awt.Color(153, 0, 0));
        jMsg.setText(" - Listo, se proceso - REVISE!");
        jrutExc.setBackground(Color.green);
        jMsg.setForeground(new java.awt.Color(0, 0, 102));

      } else {
        Desktop.getDesktop().open(archivoXLS);
        labMsgL.setText(" - Listo!");
      }

      if (verErrorCarga()) {
        jMsg.setText("- Hay Precios en Cero, revise Excel");
        jrutExc.setBackground(Color.red);
        jMsg.setForeground(new java.awt.Color(0, 0, 102));

        ImageIcon icon = new ImageIcon(getClass().getResource("/img/ojo.png"));
        String tit = "* AVISO *";
        long tim = 1000;
        Toolkit.getDefaultToolkit().beep();
        String vax = "- HAY PRECIOS EN CERO EN LA LISTA\n- REVICE EN EXCEL GENERADO\n";
        Mensaje msg = new Mensaje(vax, tit, tim, icon);

      }

      // Excel esta abierto  
    } catch (IOException ex) {
      ImageIcon icon = new ImageIcon(getClass().getResource("/img/aut.jpg"));
      String msg = "- CIERRE EL EXCEL\n ( " + nomexc + " )\nPARA PODER EJECUTAR\nEL PROCESO";
      String[] options = {" PULSA ENTER PARA CERRAR "};
      JOptionPane.showOptionDialog(null, msg, "***** AVISO ****", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, icon, options, options[0]);
    }
  }

  // verifica precios en cero
  public boolean verErrorCarga() {
    int can = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT count(*) can from listaprecios "
        + "where (pum=0 and pud>0) or (pum>0 and pud=0) ";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        can = rs.getInt("can");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    if (can > 0) {
      return true;
    } else {
      return false;
    }
  }

  // Detalle ResumenExcel
  public void DetallResExcel() {

    int can = 0;
    ConexionSQL bdsql = new ConexionSQL();
    Connection con = bdsql.Conectar();
    try {
      Statement st = con.createStatement();
      Statement st2 = con.createStatement();
      String sql = "Select dep,cop,nom,pum,pud,stk,pag,sta "
        + "from listaprecios "
        + "order by dep,sta,cop";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        String dep = rs.getString("dep");
        String cop = rs.getString("cop");
        String nom = rs.getString("nom");
        String sta = rs.getString("sta");
        double pum = rs.getDouble("pum");
        double pud = rs.getDouble("pud");
        double stk = rs.getDouble("stk");

        if (sta.equals("0")) {
          sta = "Activo";
        } else {
          sta = "Inactivo";
        }

        int i = dep.indexOf("-");
        if (i >= 0) {
          dep = dep.substring(i + 1, dep.length());
        }

        String emp = "", ref = "";
        String sql2 = "SELECT tx1,ref "
          + "FROM empaqueproducto "
          + "where cop='" + cop + "'";
        ResultSet rs2 = st2.executeQuery(sql2);
        while (rs2.next()) {
          emp = rs2.getString("tx1");
          ref = rs2.getString("ref");
        }

        fil = fil + 1;
        fila = Hoja.createRow(fil);

        // Departamento
        int j = 0;
        Detail = fila.createCell(j);
        if (sta.equals("Inactivo")) {
          Detail.setCellStyle(cellStyleCPR);
        } else {
          Detail.setCellStyle(cellStyleI);
        }
        Detail.setCellValue(dep);

        j = 1;
        // codigo
        Detail = fila.createCell(j);
        if (sta.equals("Inactivo")) {
          Detail.setCellStyle(cellStyleCPR);
        } else {
          Detail.setCellStyle(cellStyleC);
        }
        Detail.setCellValue(cop);

        j = 2;
        // descripcion
        Detail = fila.createCell(j);
        if (sta.equals("Inactivo")) {
          Detail.setCellStyle(cellStyleCPR);
        } else {
          Detail.setCellStyle(cellStyleI);
        }
        Detail.setCellValue(nom);

        j = 3;
        // empaque
        Detail = fila.createCell(j);
        if (sta.equals("Inactivo")) {
          Detail.setCellStyle(cellStyleCPR);
        } else {
          Detail.setCellStyle(cellStyleI);
        }
        Detail.setCellValue(emp);

        j = 4;
        // referencia p.u.
        Detail = fila.createCell(j);
        if (sta.equals("Inactivo")) {
          Detail.setCellStyle(cellStyleCPR);
        } else {
          Detail.setCellStyle(cellStyleI);
        }
        Detail.setCellValue(ref);

        // P.mayor
        j = 5;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleD);
        Detail.setCellValue(pum);

        // P.detal
        j = 6;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleD);
        Detail.setCellValue(pud);

        // Stock
        j = 7;
        Detail = fila.createCell(j);
        Detail.setCellStyle(cellStyleD0);
        Detail.setCellValue(stk);

        // Status
        j = 8;
        Detail = fila.createCell(j);
        if (sta.equals("Inactivo")) {
          Detail.setCellStyle(cellStyleCPR);
        } else {
          Detail.setCellStyle(cellStyleD0);
        }
        Detail.setCellValue(sta);

        can++;
      }

      fil = fil + 1;
      fila = Hoja.createRow(fil);

      int j = 0;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue("Cant => ");

      // tcantidad
      j = 1;
      Detail = fila.createCell(j);
      Detail.setCellStyle(cellStyleBCG);
      Detail.setCellValue(can);

      con.close(); // cerramos la conexion

      //ajustaHoja(4);
      for (int w = 0; w <= 8; w++) {
        Hoja.setHorizontallyCenter(true);
        if (w == 1) {
          Hoja.setDefaultColumnWidth(40);
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
    if (indexc == 0) {
      jMsg.setText(" - Generando Hoja " + Hoj);
    }
    Hoja = (SXSSFSheet) Libro.createSheet(Hoj);
    Hoja.createFreezePane(0, 4);   // inmoviliza panel
    Hoja.setAutoFilter(CellRangeAddress.valueOf(Fil));   // Aplica Filtro 
    Hoja.setDisplayGridlines(false);
  }

  // Header Excel Empresas
  public void HeaderResExcel() {

    setImagen(0, 0, 0.8); // Inserta Imagen  LogoCia.jpg
    fil = 1;
    fila = Hoja.createRow(fil);

    Header = fila.createCell(2);
    Header.setCellStyle(cellStyleBCT);
    Header.setCellValue("LISTA DE PRECIOS    AL   " + ymd_dmy(feclis) + " ");

    Header = fila.createCell(5);
    Header.setCellStyle(cellStyleC);
    Header.setCellValue(dmyhoy());

    fil = 2;
    fila = Hoja.createRow(fil);
    Header = fila.createCell(2);
    Header.setCellStyle(cellStyleD);
    Header.setCellValue(" ");

    Header = fila.createCell(3);
    Header.setCellStyle(cellStyleI);
    Header.setCellValue(" ");

    fil = 3;
    fila = Hoja.createRow(fil);
    String VH[] = {"Departamento", "   Codigo     ", "Descripcion", "Empaque", "Referencia", "  P.Mayor", "  P.Detal  ", "Existencia", "Estatus"};

    int c = VH.length;
    for (int i = 0; i < c; i++) {
      Header = fila.createCell(i);
      Header.setCellStyle(cellStyleBCA);     // Azul oscuro font blanca
      if (i == 0 || i == 2 || i == 3) {
        Header.setCellStyle(cellStyleBIA);   // Azul oscuro font blanca izq
      }
      if (i == 7) {
        Header.setCellStyle(cellStyleBCV);   // Verde font blanca
      }
      Header.setCellValue(VH[i] + "         ");
    }
  }

  // Inserta Imagen en celda Excel
  public void setImagen(int pos1, int pos2, double tam) {
    try {
      /* Read input PNG / JPG Image into FileInputStream Object*/
      InputStream my_banner_image = new FileInputStream("imgcia/LogoCia.png");
      /* Convert picture to be added into a byte array */
      byte[] bytes = IOUtils.toByteArray(my_banner_image);
      /* Add Picture to Workbook, Specify picture type as PNG and Get an Index */
      int my_picture_id = Libro.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
      /* Close the InputStream. We are ready to attach the image to workbook now */
      my_banner_image.close();
      /* Create the drawing container */
      XSSFDrawing drawing = (XSSFDrawing) Hoja.createDrawingPatriarch();
      /* Create an anchor point */
      XSSFClientAnchor my_anchor = new XSSFClientAnchor();
      /* Define top left corner, and we can resize picture suitable from there */
      my_anchor.setCol1(pos1);
      my_anchor.setRow1(pos2);
      /* Invoke createPicture and pass the anchor point and ID */
      XSSFPicture my_picture = drawing.createPicture(my_anchor, my_picture_id);
      /* Call resize method, which resizes the image */
      //my_picture.resize();
      my_picture.resize(tam);

    } catch (FileNotFoundException ex) {
      Logger.getLogger(Reporte_ExcelListaPrecios.class
        .getName()).log(Level.SEVERE, null, ex);

    } catch (IOException ex) {
      Logger.getLogger(Reporte_ExcelListaPrecios.class
        .getName()).log(Level.SEVERE, null, ex);
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
    cellStyleD0.setDataFormat(format.getFormat("#,##0.##"));
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

    /* grillas
    cellStyleC.setBorderTop(HSSFCellStyle.BORDER_THIN);
    cellStyleC.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    cellStyleC.setBorderRight(HSSFCellStyle.BORDER_THIN);
    cellStyleC.setBorderBottom(HSSFCellStyle.BORDER_THIN);
     */
  }

}
