package gestionFZ;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import comun.Mensaje;
import static comun.MetodosComunes.FileExist;
import static comun.MetodosComunes.FileSave;
import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.getdiasFec;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymd_dmyc;
import static comun.MetodosComunes.ymdhoy;
import static gestionFZ.Consulta_EstadoCuentaR.labRutEdo;
import static gestionFZ.Registro_PagosClientes.labMsgR;
import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import modelo.ConexionSQL;
import modelo.Importadora;

public class Pdf_EstadoCuenta {

  int nrc = 0;
  int pag = 0;

  double tdo = 0; // total nota $
  double trd = 0; // total ret Bs
  double toi = 0; // total iva
  double tor = 0; // total ret iva 
  double tpa = 0; // total pago  
  double tp$ = 0; // total pago $
  double tpB = 0; // total pago Bs
  double tas = 0; // tasa

  double tgd = 0; // Total Grl Notas$
  double tgr = 0; // Total Grl Ret Bs
  double tgi = 0; // Total Grl Iva  
  double tgp = 0; // Total Grl Pagos
  double tg$ = 0; // Total Saldo $
  double tgB = 0; // Total Saldo Bs Ret Iva

  double tsi$ = 0;  // Saldo Inicial $
  double tsiB = 0;  // Saldo Inicial Bs
  double tsf$ = 0;  // Saldo Final $
  double tsfB = 0;  // Saldo Final Bs

  double tin$ = 0;  // Total ingreso Dolares
  double tinB = 0;  // Total ingreso Bolivares
  double dic = 0;     // dias credito

  String coc = "";  // codigo cliente
  String noc = "";  // nombre cliente
  String rif = "";  // rif cliente
  String tlf = "";  // tlf cliente
  String cpg = "";  // cond pago
  String tip = "";  // tipo
  String fed = "";  // Fecha desde recibo
  String feh = "";  // Fecha hasta recibo
  String tex = "";

  ImageIcon icon;

  PdfPTable tabPdf;   // Declarar tabla 
  Paragraph titPdf;   // Declarar titulo
  Document documento; // Documento Pdf
  PdfPCell cell;      // Celda Tabla

  public Pdf_EstadoCuenta(String cox, String fdx, String fhx) {
    coc = cox;
    if (coc.length() > 0) {
      fed = fdx;
      feh = fhx;
      String fil = "rep/pdf/edocta/EdoCta_" + feh + "_" + coc + ".pdf";
      if (FileSave(fil)) {
        generaEdoCta(fil);
      } else {
        icon = new ImageIcon(getClass().getResource("/img/ojo.png"));
        String tit = "* AVISO *";
        long tim = 1500;
        Toolkit.getDefaultToolkit().beep();
        String vax = "Cierre el PDF EdoCta_" + feh + "_" + coc + ".pdf";
        Mensaje msg = new Mensaje(vax, tit, tim, icon);
        labMsgR.setText("- Cierre PDF EdoCta_" + feh + "_" + coc + ".pdf");
      }
    }
  }

  public void generaEdoCta(String fil) {
    labRutEdo.setOpaque(true);
    // Se crea el documento
    documento = new Document(PageSize.LETTER, 20, 20, 0, 20);            //  Hoja Vertical
    // Se crea el OutputStream para el fichero donde queremos dejar el pdf.
    FileOutputStream ficheroPdf = null;
    try {
      ficheroPdf = new FileOutputStream(fil);
      try {
        // Se asocia el documento al OutputStream y se indica que el espaciado entre
        // lineas sera de 20. Esta llamada debe hacerse antes de abrir el documento
        PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
        // Se abre el documento.
        documento.open();
        // Busca Datos Cliente
        getDatosCte();
        // Busca Saldos Iniciales
        getSaldosCte();
        // Encabezado Reporte
        headerCte();
        // Notas $
        headerDoc();
        detalDoc();
        // Pagos Notas $
        headerPagNot();
        detalPagNot();
        // Pagos Retenciones Bs

        //System.out.println("tgr="+tgr+",tgi="+tgi);
        if (tgi != 0 || tinB < 0) {
          headerPagRet();
          detalPagRet();
        }
        // Totales Ingresos x banco
        if (tin$ != 0 || tinB != 0) {
          totIngresos();
        }
        // Nombre Vendedor
        detVendedor();
        // Cerrar documento
        documento.close();
        labRutEdo.setBackground(Color.green);
      } catch (DocumentException ex) {
        Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
      }
    } catch (FileNotFoundException ex) {
      Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  // Detalle Documentos
  public void detalDoc() {

    try {
      int tpd = 0;
      int nno = 0;
      int nfa = 0;
      String fno = "";
      String fen = "";
      String fpa = "";
      tdo = 0;
      toi = 0;
      trd = 0;
      tgd = 0;
      tgi = 0;
      tgr = 0;

      tabPdf = new PdfPTable(9);
      tabPdf.setWidthPercentage(90);
      tabPdf.setWidths(new float[]{15f, 15f, 16f, 16f, 15f, 15f, 15F, 20f, 20f});

      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setBorder(PdfPCell.BOX);  // .BOX  (cuadro)

      BaseColor colBas = WebColors.getRGBColor("#EAEDED");   // gris

      try {
        ConexionSQL bdsql = new ConexionSQL();
        Connection con = bdsql.Conectar();
        Statement st = con.createStatement();
        ResultSet rs = null;

        String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.Notas ("
                + "tpd  int,"
                + "nno  int,"
                + "nfa  int,"
                + "fno  varchar(08),"
                + "fen  varchar(08),"
                + "fpa  varchar(08),"
                + "tno  Decimal(15,2),"
                + "toi  Decimal(15,2),"
                + "tor  Decimal(15,2),"
                + "nrc  int,"
                + "tbd  Decimal(15,2),"
                + "tbb  Decimal(15,2))"
                + "NOT LOGGED ON COMMIT PRESERVE ROWS";
        st.execute(sql);

        sql = "insert into SESSION.Notas "
                + "SELECT 0,nne nno,nfa,fne fno,fee fen,'' fpa,tne-tdn tno,toi,tor,0,0,0 "
                + "FROM notaent "
                + "where coc = '" + coc + "' "
                + "  and fne between '" + fed + "' and '" + feh + "'";
        st.execute(sql);

        sql = "insert into SESSION.Notas "
                + "SELECT 1,ncr nno,nfa,fnc fno,'' fen,'' fpa,tnc*-1 tno,0 toi,0 tor,0,0,0 "
                + "FROM notaCred "
                + "where coc ='" + coc + "' "
                + "  and fnc between '" + fed + "' and '" + feh + "'";
        st.execute(sql);

        //  + "Set nrc = (select nullif(max(nrc),0) from recibocobroD Where SESSION.Notas.nno=recibocobroD.nno)";
        sql = "UPDATE SESSION.Notas "
                + "Set fpa =  (select nullif(max(fpa),'') from recibocobroD "
                + "Where SESSION.Notas.nno=recibocobroD.nno),"
                + "    nrc =  (select nullif(max(nrc),0)  from recibocobroD "
                + "Where SESSION.Notas.nno=recibocobroD.nno)";
        st.execute(sql);

        // Ajustes saldo a favor
        sql = "insert into SESSION.Notas "
                + "SELECT -2,0 nno,0 nfa,'*SFA*' fno,'' fen,'' fpa,0 tno,0 toi,0 tor,(tpa/tas),0,nrc "
                + "FROM recibocobroP "
                + "where coc ='" + coc + "' "
                + "  and nno=0 "
                + "  and nrc in (select nrc from SESSION.Notas) "
                + "  and tip='5'";
        //st.execute(sql);

        // Saldo Incial 
        if (tsi$ != 0 || tsiB != 0) {
          toi = 0;
          if (tsiB != 0) {
            toi = tsiB * 100 / 25;
          }
          sql = "Insert into SESSION.Notas "
                  + "(tpd,nno,nfa,fno,fen,fpa,tno,toi,tor) "
                  + "VALUES (-1,0,0,'" + fed + "','','S.INIC'," + tsi$ + "," + toi + "," + tsiB + ")";
          st.execute(sql);
        }

        // Detalle Documentos - recibocobroD
        /*
        sql = "SELECT nrc,tno,fpa,toi,tor "
                + "FROM SESSION.Notas ";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          nrc = rs.getInt("nrc");
          fpa = rs.getString("fpa");
          tdo = rs.getDouble("tno");
          toi = rs.getDouble("toi");
          tor = rs.getDouble("tor");
        }
         */
        // Detalle Documentos - recibocobroD
        sql = "SELECT nrc,tpd,nno,nfa,fno,fen,fpa,tno,toi,tor "
                + "FROM SESSION.Notas "
                + "order by fno,tpd,nno";

        tdo = 0;
        toi = 0;
        tor = 0;

        rs = st.executeQuery(sql);
        while (rs.next()) {

          nrc = rs.getInt("nrc");
          tpd = rs.getInt("tpd");
          nno = rs.getInt("nno");
          nfa = rs.getInt("nfa");
          fno = rs.getString("fno");
          fen = rs.getString("fen");
          fpa = rs.getString("fpa");
          tdo = rs.getDouble("tno");
          toi = rs.getDouble("toi");
          tor = rs.getDouble("tor");

          if (tpd == -1) {
            trd = tor;
          } else {
            trd = toi - tor;
          }

          String mox = MtoEs(trd, 2);
          mox = GetCurrencyDouble(mox);
          trd = GetMtoDouble(mox);
          tgd = tgd + tdo;
          tgi = tgi + toi;
          tgr = tgr + trd;

          String dix = "";
          String fee = "";
          String fpg = "";

          if (tpd >= 0) {

            String tix = "NE";
            if (tpd == 1) {
              tix = "NC";
            }

            if (fen == null) {
              fen = "";
            }
            if (fpa == null) {
              fpa = "";
            }

            if (fno.length() > 0) {
              fno = ymd_dmyc(fno);
            }

            if (fen.length() > 0) {
              fee = ymd_dmyc(fen);
            }

            if (fpa.length() > 0) {
              fpg = ymd_dmyc(fpa);
            }

            String fex = fno;
            if (fen.length() > 0) {
              fex = fen;
            }

            int dias = 0;
            if (tpd == 0) {
              String feH = ymdhoy();
              if (fpa.length() == 0) {
                dias = getdiasFec(feH, fex);
              }
              if (fpa.length() > 0) {
                dias = getdiasFec(fpa, fex);
              }
            }

            if (tpd == 0) {
              dix = "" + dias;
            } else {
              if (tpd == 1) {
                dix = "NC";
              }
            }

          } else {
            nfa = 0;
            fno = "";
          }

          // 1- F. Emision
          tex = fno;
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

          // 2- Factura
          tex = "";
          if (nfa != 0) {
            tex = "" + nfa;
          }
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);

          // 3-  Total Iva
          tex = "";
          if (toi != 0) {
            tex = MtoEs(toi, 2);
          }
          addCeldaTB(tex, 6, 0, 12, 3, 0, 0, colBas, 2, 0, 0, 1);

          // 4- Total Ret Iva
          tex = "";
          if (toi != 0) {
            tex = MtoEs(trd, 2);
          }
          addCeldaTB(tex, 6, 0, 16, 3, 0, 0, colBas, 2, 0, 0, 1);

          // 5- F. Recibida
          tex = fee;
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);

          // 6- F. Pago
          tex = fpg;
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);

          // 7- Dias
          tex = dix;
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);

          // 8- Nota  
          tex = "";
          if (nno > 0) {
            tex = "" + nno;
          } else {
            // Saldo Inicial
            if (tpd == -1) {
              tex = "S.INICIAL";
            }
          }
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);

          // 9- Total Nota $
          tex = MtoEs(tdo, 2);
          if (tpd == -1) {
            addCeldaTB(tex, 6, 0, 12, 3, 0, 0, colBas, 2, 1, 0, 1);
          } else {
            addCeldaTB(tex, 6, 0, 12, 3, 0, 0, colBas, 2, 0, 0, 1);
          }

          documento.add(tabPdf);  // spm mierda..

          tabPdf.flushContent();

        }

        //totales
        // 1- Blanco
        tex = "";
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

        // 2- Total
        tex = "TOTAL Bs";
        if (tgi == 0) {
          tex = "";
          colBas = WebColors.getRGBColor("#FDFEFE");   // blanco
        }
        addCeldaTB(tex, 6, 0, 12, 3, 1, 1, colBas, 2, 0, 0, 1);

        // 3-  Total Iva
        tex = MtoEs(tgi, 2);
        if (tgi == 0) {
          tex = "";
        }
        addCeldaTB(tex, 6, 0, 12, 3, 1, 1, colBas, 2, 1, 0, 1);

        // 4- Total Ret Iva
        tex = MtoEs(tgr, 2);
        if (tgi == 0) {
          tex = "";
        }
        addCeldaTB(tex, 6, 0, 16, 3, 1, 1, colBas, 2, 1, 0, 1);

        // 5- 7 Blanco
        tex = "";
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);

        colBas = WebColors.getRGBColor("#EAEDED");   // gris

        // 8- Total
        tex = "TOTAL $";
        addCeldaTB(tex, 6, 0, 0, 3, 1, 1, colBas, 0, 0, 0, 1);

        // 9- Total Nota $
        tex = MtoEs(tgd, 2);
        addCeldaTB(tex, 6, 0, 12, 3, 1, 1, colBas, 2, 1, 0, 1);
        documento.add(tabPdf);
        tabPdf.flushContent();

        rs.close();
        con.close();
      } catch (SQLException ex) {
        Logger.getLogger(Registro_PagosClientes.class.getName()).log(Level.SEVERE, null, ex);
      }

    } catch (DocumentException ex) {
      Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  // linea parrafo Bold
  public void addTitulo(String txt, String tip, int tam, String pos) {
    titPdf = new Paragraph();
    if (tip.equals("B")) {
      titPdf.setFont(FontFactory.getFont("calibri", tam, Font.BOLD, BaseColor.BLACK));
    } else {
      titPdf.setFont(FontFactory.getFont("calibri", tam, Font.NORMAL, BaseColor.BLACK));
    }
    titPdf.add(txt);
    if (pos.equals("C")) {
      titPdf.setAlignment(Paragraph.ALIGN_CENTER);
    }
    if (pos.equals("R")) {
      titPdf.setAlignment(Paragraph.ALIGN_RIGHT);
    }
    if (pos.equals("L")) {
      titPdf.setAlignment(Paragraph.ALIGN_LEFT);
    }
  }

  // Texto Celda Tabla Normal
  public void addCelda(String txt, String tip, int tam, int tab) {
    Paragraph col = new Paragraph(txt);
    if (tip.contains("N")) {
      col.getFont().setStyle(Font.NORMAL);
    } else {
      col.getFont().setStyle(Font.BOLD);
    }
    col.getFont().setSize(tam);
    if (tab == 1) {
      tabPdf.addCell(col);
    }
  }

  // Parametros Celda 
  public void addCeldaTB(
          String tex, // Texto
          int tam, // Tamaño Font
          int cls, // column space
          int padr, // padding spacio derecha
          int padb, // padding spacio bottom
          int box, // cuado box  0=box, -1=No, 1=-
          int cob, // color base 0=si 1=No
          BaseColor colBas,
          int pos, // Posiicon 0=centro, 1=izq, 2=der
          int sty, // Estilo  0=Normal, 1=Negrita
          int col, // Color 0=Negro, 1=Azul, 2=Rojo, 3=Gris
          int tab) {   // No Tabla  1 = tabPdf.addCell(cell);

    // Font Normal
    if (sty == 0) {
      if (col == 0) {
        cell = new PdfPCell(new Paragraph(new Paragraph(new Paragraph(tex,
                FontFactory.getFont("tahome",
                        tam, // tamaño
                        Font.NORMAL, // estilo
                        BaseColor.BLACK))))); // NEGRO
      }
      if (col == 1) {
        cell = new PdfPCell(new Paragraph(new Paragraph(new Paragraph(tex,
                FontFactory.getFont("tahome",
                        tam, // tamaño
                        Font.NORMAL, // estilo
                        BaseColor.BLUE)))));  // AZUL
      }
      if (col == 2) {
        cell = new PdfPCell(new Paragraph(new Paragraph(new Paragraph(tex,
                FontFactory.getFont("tahome",
                        tam, // tamaño
                        Font.NORMAL, // estilo
                        BaseColor.RED)))));   // ROJO
      }
      if (col == 3) {
        cell = new PdfPCell(new Paragraph(new Paragraph(new Paragraph(tex,
                FontFactory.getFont("tahome",
                        tam, // tamaño
                        Font.NORMAL, // estilo
                        BaseColor.GRAY))))); // GRIS
      }
    }

    // Font Bold
    if (sty == 1) {
      if (col == 0) {
        cell = new PdfPCell(new Paragraph(new Paragraph(new Paragraph(tex,
                FontFactory.getFont("tahome",
                        tam, // tamaño
                        Font.BOLD, // estilo
                        BaseColor.BLACK)))));
      }
      if (col == 1) {
        cell = new PdfPCell(new Paragraph(new Paragraph(new Paragraph(tex,
                FontFactory.getFont("tahome",
                        tam, // tamaño
                        Font.BOLD, // estilo
                        BaseColor.BLUE)))));
      }
      if (col == 2) {
        cell = new PdfPCell(new Paragraph(new Paragraph(new Paragraph(tex,
                FontFactory.getFont("tahome",
                        tam, // tamaño
                        Font.BOLD, // estilo
                        BaseColor.RED)))));
      }
      if (col == 3) {
        cell = new PdfPCell(new Paragraph(new Paragraph(new Paragraph(tex,
                FontFactory.getFont("tahome",
                        tam, // tamaño
                        Font.BOLD, // estilo
                        BaseColor.GRAY)))));
      }
    }

    cell.setVerticalAlignment(Element.ALIGN_CENTER);
    if (pos == 0) {
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    }
    if (pos == 1) {
      cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    }
    if (pos == 2) {
      cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    }

    if (cob == 1) {
      cell.setBackgroundColor(colBas);
    }
    if (padr > 0) {
      cell.setPaddingRight(padr);
    }
    if (padb > 0) {
      cell.setPaddingBottom(padb);
    }
    if (cls > 0) {
      cell.setColspan(cls);
    }

    if (box == 0) {
      cell.setBorder(Rectangle.BOX);
    } else {
      if (box == -1) {
        cell.setBorder(Rectangle.NO_BORDER);
      } else {
        cell.setBorder(box);
      }
    }
    // añade celda a la tabla
    if (tab == 1) {
      tabPdf.addCell(cell);
    }
  }

  // Lista Detalle Pagos
  public void detalPagNot() {

    try {

      String fep = "";
      String tip = "";
      String bce = "";
      String bcr = "";
      String ref = "";
      tpa = 0;
      tas = 0;
      tp$ = 0;
      tpB = 0;

      tsi$ = tsi$ * -1;

      //tg$ = tsi$;   // Inicializa con Saldo inical
      tg$ = 0;   // Inicializa con Saldo inical

      int nop = 0, can = 0;
      BaseColor colBas = WebColors.getRGBColor("#FDFEFE");   // blanco

      tabPdf = new PdfPTable(9);
      tabPdf.setWidthPercentage(90);
      tabPdf.setWidths(new float[]{15f, 15f, 15f, 23f, 23f, 35f, 20f, 15f, 20f});

      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setBorder(PdfPCell.BOX);

      try {
        ConexionSQL bdsql = new ConexionSQL();
        Connection con = bdsql.Conectar();
        Statement st = con.createStatement();
        ResultSet rs = null;

        //y.tpd,nno,nfa,fno,fen,tdo,toi,trd,fpa "
        String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.Notas ("
                + "nrc  int,"
                + "tpd  int,"
                + "nno  int,"
                + "nfa  int)"
                + "NOT LOGGED ON COMMIT PRESERVE ROWS";
        st.execute(sql);

        sql = "insert into SESSION.Notas "
                + "SELECT 0,0,nne nno,nfa "
                + "FROM notaent "
                + "where coc = '" + coc + "' "
                + "  and fne between '" + fed + "' and '" + feh + "'";
        st.execute(sql);

        sql = "insert into SESSION.Notas "
                + "SELECT 0,1,ncr nno,nfa "
                + "FROM notaCred "
                + "where coc ='" + coc + "' "
                + "  and fnc between '" + fed + "' and '" + feh + "'";
        st.execute(sql);

        sql = "UPDATE SESSION.Notas "
                + "Set nrc = (select nullif(max(nrc),0) from recibocobroD Where SESSION.Notas.nno=recibocobroD.nno)";
        st.execute(sql);

        // Detalle Pagos recibocobroP
        sql = "SELECT nrc,fep,nno,tip,bce,bcr,ref,tpa,tas  "
                + "FROM recibocobroP "
                + "where tip<>'2' "
                + "  and tip<>'4' "
                + "  and coc = '" + coc + "'"
                //+ "  and fep between '" + fed + "' and '" + feh + "' order by nno";
                + "  and nrc in (select nrc from SESSION.Notas) order by nrc,nno";

        rs = st.executeQuery(sql);
        while (rs.next()) {
          nrc = rs.getInt("nrc");
          nop = rs.getInt("nno");
          fep = rs.getString("fep");
          tip = rs.getString("tip");
          bce = rs.getString("bce");
          bcr = rs.getString("bcr");
          ref = rs.getString("ref");
          tpa = rs.getDouble("tpa");
          tas = rs.getDouble("tas");

          tp$ = tpa / tas;

          // Total Ingresos a Bancos
          if (tas == 1) {
            tin$ = tin$ + tp$;  // Ingreso banco $
          } else {
            tinB = tinB + tpa;
          }

          // Retencion Iva
          tg$ = tg$ + tp$;

          // 1- Fecha pago
          tex = ymd_dmyc(fep);
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

          // 2- Nota Afectada
          tex = "" + nop;
          if (nop == 0) {
            tex = "";
          }
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

          // 3- Recibo
          tex = "" + nrc;
          if (nrc == 0) {
            tex = "";
          }
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

          // 4- Banco emisor
          tex = bce;
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

          // 5- Banco Receptor
          tex = bcr;
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

          // 6-  Referencia
          tex = ref;
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

          // 7- Monto
          tex = "";
          if (tas > 1) {
            tex = MtoEs(tpa, 2);
          }
          addCeldaTB(tex, 6, 0, 12, 3, 0, 0, colBas, 2, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)
          // 8- Tasa
          if (tas > 1) {
            tex = MtoEs(tas, 2);
          } else {
            tex = "";
          }
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

          // 9- Monto $
          tex = MtoEs(tp$, 2);
          addCeldaTB(tex, 6, 0, 12, 3, 0, 0, colBas, 2, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

          documento.add(tabPdf);
          tabPdf.flushContent();

          can++;

          if (can >= 39) {
            headerPagNot();
            can = 0;
          }

        }

        //totales 
        colBas = WebColors.getRGBColor("#EAEDED");   // gris

        // 1-5
        tex = "";
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

        // 6 Saldo
        if (!MtoEs(tgd - tg$, 2).equals("0,00")) {
          colBas = WebColors.getRGBColor("#F9E79F");   // amarillo
        }
        if (MtoEs(tgd - tg$, 2).equals("0,00")) {
          tex = "TOTAL SALDO $";
        } else {
          if ((tgd - tg$) > 0) {
            tex = "SDO PENDIENTE $";
          } else {
            tex = "SALDO A FAVOR $";
          }
        }
        addCeldaTB(tex, 6, 0, 0, 3, 1, 1, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

        // 7- Monto $
        double tot = (tgd - tg$);
        String mox = MtoEs(tot, 2);
        mox = GetCurrencyDouble(mox);
        tot = GetMtoDouble(mox);

        if (tot > 0) {
          tex = MtoEs(tot, 2);
          addCeldaTB(tex, 6, 0, 12, 3, 1, 1, colBas, 2, 1, 2, 1);     // Rojo
        } else {
          tex = MtoEs(tg$ - tgd, 2);
          addCeldaTB(tex, 6, 0, 12, 3, 1, 1, colBas, 2, 1, 0, 1);     // Negro
        }

        colBas = WebColors.getRGBColor("#EAEDED");   // gris

        // 8 T.BASE
        tex = "T.BASE $ ";
        addCeldaTB(tex, 6, 0, 0, 3, 1, 1, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

        // 9- Monto $
        tex = MtoEs(tg$, 2);
        addCeldaTB(tex, 6, 0, 12, 3, 1, 1, colBas, 2, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

        documento.add(tabPdf);

        tabPdf.flushContent();

        rs.close();

        con.close();
      } catch (SQLException ex) {
        Logger.getLogger(Registro_PagosClientes.class.getName()).log(Level.SEVERE, null, ex);
      }

    } catch (DocumentException ex) {
      Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  public void totIngresos() {

    if (tin$ < 0) {
      tin$ = 0;
    }
    if (tinB < 0) {
      tinB = 0;
    }

    try {
      tabPdf = new PdfPTable(8);
      tabPdf.setWidthPercentage(90);
      tabPdf.setWidths(new float[]{35f, 20f, 20f, 20f, 20f, 20f, 20f, 20f});
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
      tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

      BaseColor colBas = WebColors.getRGBColor("#EAEDED");   // gris

      // 1-2
      tex = "\nTOTAL INGRESOS A BANCOS";
      addCeldaTB(tex, 5, 0, 0, 3, -1, 0, colBas, 1, 1, 3, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)
      tex = "";
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);

      // 1
      tex = "Total  $";
      addCeldaTB(tex, 6, 0, 0, 3, -1, 1, colBas, 1, 0, 0, 1);
      // 2- Monto $
      if (MtoEs(tin$, 2).indexOf("9,99") >= 0) {
        tin$ = tin$ + 0.01;
      }
      tex = MtoEs(tin$, 2);
      addCeldaTB(tex, 6, 0, 12, 3, -1, 1, colBas, 2, 1, 0, 1);
      tex = "";
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);

      // 1
      tex = "Total  Bs";
      addCeldaTB(tex, 6, 0, 0, 3, -1, 1, colBas, 1, 0, 0, 1);
      // 2- Monto Bs
      if (MtoEs(tinB, 2).indexOf("9,99") >= 0) {
        tinB = tinB + 0.01;
      }
      tex = MtoEs(tinB, 2);
      addCeldaTB(tex, 6, 0, 12, 3, -1, 1, colBas, 2, 1, 0, 1);
      tex = "";
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);
      addCeldaTB(tex, 6, 0, 0, 3, -1, 0, colBas, 0, 0, 0, 1);

      documento.add(tabPdf);
      tabPdf.flushContent();

    } catch (DocumentException ex) {
      Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  // Lista Detalle Pagos Retenciones Bs
  public void detalPagRet() {

    try {

      String fep = "";
      String bce = "";
      String bcr = "";
      String ref = "";
      tpa = 0;
      tas = 0;
      tp$ = 0;
      tpB = 0;
      tgB = 0;

      tsiB = tsiB * -1;
      //tgB = tsiB;   // Inicializa con Saldo inical

      int nop = 0;

      tabPdf = new PdfPTable(9);
      tabPdf.setWidthPercentage(90);
      tabPdf.setWidths(new float[]{15f, 15f, 15f, 23f, 23f, 35f, 20f, 15f, 20f});

      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setBorder(PdfPCell.BOX);

      BaseColor colBas = WebColors.getRGBColor("#EAEDED");   // gris

      try {
        ConexionSQL bdsql = new ConexionSQL();
        Connection con = bdsql.Conectar();
        Statement st = con.createStatement();
        ResultSet rs = null;

        //y.tpd,nno,nfa,fno,fen,tdo,toi,trd,fpa "
        String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.Notas ("
                + "nrc  int,"
                + "tpd  int,"
                + "nno  int,"
                + "nfa  int)"
                + "NOT LOGGED ON COMMIT PRESERVE ROWS";
        st.execute(sql);

        sql = "insert into SESSION.Notas "
                + "SELECT 0,0,nne nno,nfa "
                + "FROM notaent "
                + "where coc = '" + coc + "' "
                + "  and fne between '" + fed + "' and '" + feh + "'";
        st.execute(sql);

        sql = "insert into SESSION.Notas "
                + "SELECT 0,1,ncr nno,nfa "
                + "FROM notaCred "
                + "where coc ='" + coc + "' "
                + "  and fnc between '" + fed + "' and '" + feh + "'";
        st.execute(sql);

        sql = "UPDATE SESSION.Notas "
                + "Set nrc = (select nullif(max(nrc),0) from recibocobroD Where SESSION.Notas.nno=recibocobroD.nno)";
        st.execute(sql);

        // Detalle Pagos recibocobroP
        sql = "SELECT nrc,fep,nno,tip,bce,bcr,ref,tpa,tas  "
                + "FROM recibocobroP "
                + "where (tip='2' or tip='4') "
                + "  and coc = '" + coc + "'"
                + "  and nrc in (select nrc from SESSION.Notas) "
                + "order by nrc,nno";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          nrc = rs.getInt("nrc");
          fep = rs.getString("fep");
          tip = rs.getString("tip");
          bce = rs.getString("bce");
          bcr = rs.getString("bcr");
          ref = rs.getString("ref");
          tpa = rs.getDouble("tpa");
          tas = rs.getDouble("tas");
          nop = rs.getInt("nno");

          tp$ = tpa / tas;  // $
          tgB = tgB + tpa;  // Bs

          // Total Ingresos a Bancos
          if (tas == 1) {
            tinB = tinB + tpa;
          } else {
            tin$ = tin$ + tp$;  // Ingreso banco $
          }

          // 1- Fecha pago
          tex = "";
          if (fep.length() > 0) {
            tex = ymd_dmyc(fep);
          }
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

          // 2- Factura
          tex = "";
          if (nop > 0) {
            tex = "" + nop;
          }
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);

          // 3- Recibo
          tex = "";
          if (nrc > 0) {
            tex = "" + nrc;
          }
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);

          // 4- Banco emisor
          tex = bce;
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);

          // 5- Banco Receptor
          tex = bcr;
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);

          // 6-  Referencia
          tex = ref;
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);

          // 7- Monto $
          tex = "";
          if (tas > 1) {
            tex = MtoEs(tp$, 2);
          }
          addCeldaTB(tex, 6, 0, 12, 3, 0, 0, colBas, 2, 0, 0, 1);

          // 8- Tasa
          if (tas > 1) {
            tex = MtoEs(tas, 2);
          } else {
            tex = "";
          }
          addCeldaTB(tex, 6, 0, 0, 3, 0, 0, colBas, 0, 0, 0, 1);

          // 9- Monto Bs
          tex = "";
          tex = MtoEs(tpa, 2);
          addCeldaTB(tex, 6, 0, 12, 3, 0, 0, colBas, 2, 0, 0, 1);

          documento.add(tabPdf);
          tabPdf.flushContent();

        }

        //totales
        colBas = WebColors.getRGBColor("#EAEDED");   // gris

        // 1-5
        tex = "";
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)
        addCeldaTB(tex, 6, 0, 0, 3, 1, 0, colBas, 0, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

        // 6 Saldo
        if (!MtoEs(tgr - tgB, 2).equals("0,00")) {
          colBas = WebColors.getRGBColor("#F9E79F");   // amarillo
        }
        if (MtoEs(tgB - tgr, 2).equals("0,00")) {
          tex = "TOTAL SALDO Bs";
        } else {
          if ((tgr - tgB) > 0) {
            tex = "SDO.PENDIENTE Bs";
          } else {
            tex = "SALDO A FAVOR Bs";
          }
        }
        addCeldaTB(tex, 6, 0, 0, 3, 1, 1, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

        double tot = (tgr - tgB);
        String mox = MtoEs(tot, 2);
        mox = GetCurrencyDouble(mox);
        tot = GetMtoDouble(mox);

        if (tot > 0) {
          tex = MtoEs(tot, 2);
          addCeldaTB(tex, 6, 0, 12, 3, 1, 1, colBas, 2, 1, 2, 1);     // Rojo
        } else {
          tex = MtoEs(tgB - tgr, 2);
          addCeldaTB(tex, 6, 0, 12, 3, 1, 1, colBas, 2, 1, 0, 1);     // Negro
        }

        colBas = WebColors.getRGBColor("#EAEDED");   // gris

        // 8 T.BASE
        tex = "T.BASE Bs";
        addCeldaTB(tex, 6, 0, 0, 3, 1, 1, colBas, 0, 0, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

        // 9- Monto $
        tex = MtoEs(tgB, 2);
        addCeldaTB(tex, 6, 0, 12, 3, 1, 1, colBas, 2, 1, 0, 1);  // (tex,tam,cls,padr,padb,box,cob,colBas,pos,sty,col,tab)

        documento.add(tabPdf);

        tabPdf.flushContent();

        rs.close();

        con.close();
      } catch (SQLException ex) {
        Logger.getLogger(Registro_PagosClientes.class.getName()).log(Level.SEVERE, null, ex);
      }

    } catch (DocumentException ex) {
      Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  public void headerDoc() {
    pag = 1;

    try {

      tabPdf = new PdfPTable(9);
      tabPdf.setWidthPercentage(90);
      tabPdf.setWidths(new float[]{15f, 15f, 16, 16f, 15f, 15f, 15F, 20f, 20f});

      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);  // .BOX  (cuadro)

      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("");
      tabPdf.addCell(titPdf);
      tabPdf.addCell(titPdf);
      tabPdf.addCell(titPdf);
      tabPdf.addCell(titPdf);
      tabPdf.addCell(titPdf);
      tabPdf.addCell(titPdf);
      tabPdf.addCell(titPdf);
      tabPdf.addCell(titPdf);
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.NORMAL, BaseColor.GRAY));
      titPdf.add("                 Pag  " + pag + "\n\n");
      tabPdf.addCell(titPdf);
      tabPdf.getDefaultCell().setBorder(PdfPCell.BOX);       // .BOX  (cuadro)

      BaseColor colBas = WebColors.getRGBColor("#138D75");   // Azul #1F618D   verde #73C6B6 
      tabPdf.getDefaultCell().setBackgroundColor(colBas);

      // 1- F.EMISION
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("F.EMISION\n");
      tabPdf.addCell(titPdf);

      // 2- FACTURA
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("FACTURA\n");
      tabPdf.addCell(titPdf);

      // 3- TOTAL IVA Bs
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("T.IVA Bs\n");
      tabPdf.addCell(titPdf);

      // 4- TOTAL RET IVA Bs
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("T.RET IVA\n");
      tabPdf.addCell(titPdf);

      // 5- Fec Rec
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("F.RECIBIDA\n");
      tabPdf.addCell(titPdf);

      // 6- Fec pago
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("F.PAGO\n");
      tabPdf.addCell(titPdf);

      // 7- Dias
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("DIAS\n");
      tabPdf.addCell(titPdf);

      //colBas = WebColors.getRGBColor("#795548");   // Marron  #795548  // #E59866
      //tabPdf.getDefaultCell().setBackgroundColor(colBas);
      // 8- NOTA
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("NOTA\n");
      tabPdf.addCell(titPdf);

      // 9- TOTAL NOTA $
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("T. NOTA $\n");
      tabPdf.addCell(titPdf);

      documento.add(tabPdf);
      tabPdf.flushContent();

    } catch (DocumentException ex) {
      Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void headerCte() {
    try {
      Importadora imp = new Importadora();
      noc = "\n " + noc + "\n";
      String cov = imp.getCodVend();     // Codigo Vendedor
      cov = cov + "\n";
      rif = "\n" + rif;
      double dcr = imp.getDiasCred();
      if (cpg.equals("0")) {
        cpg = " CREDITO " + MtoEs(dic, 0) + " Dias";
      } else {
        cpg = " - PREPAGO -";
      }

      // Tabla Header (Logo Cia)
      tabPdf = new PdfPTable(1);
      tabPdf.setWidthPercentage(90);        // Datos de porcentaje a la tabla (tamaño ancho)
      tabPdf.setWidths(new float[]{100f});   // Datos del ancho de cada columna.
      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);

      String img = "imgcia\\logopedido.jpg";
      try {
        Image foto = Image.getInstance(img);
        if (FileExist(img)) {
          foto = Image.getInstance(img);
        }
        foto.scaleToFit(24, 24);
        foto.setAlignment(Chunk.ALIGN_CENTER);
        tabPdf.addCell(foto);
        documento.add(Chunk.NEWLINE);
        documento.add(tabPdf);
        tabPdf.flushContent();

        // Header Cliente
        tabPdf = new PdfPTable(3);
        tabPdf.setWidthPercentage(90);
        tabPdf.setWidths(new float[]{12f, 58f, 30f});
        tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);  // .BOX  (cuadro)
        BaseColor colBas = WebColors.getRGBColor("#A00000");   // Azul

        // CLIENTE - No. PEDIDO
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 7, Font.BOLD, BaseColor.BLACK));   // colBas
        titPdf.add("\nCLIENTE     :\n");
        tabPdf.addCell(titPdf);

        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 7, Font.BOLD, BaseColor.BLACK));
        titPdf.add(noc);
        tabPdf.addCell(titPdf);

        tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.BLUE));
        titPdf.add("      -  ESTADO  DE  CUENTA  -   ");

        // Celda
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        colBas = WebColors.getRGBColor("#EAEDED");   // gris
        cell.setBackgroundColor(colBas);
        cell.setBorder(1);
        cell.setPaddingBottom(10);
        cell.addElement(titPdf);
        tabPdf.addCell(cell);

        documento.add(tabPdf);
        tabPdf.flushContent();

        // RIF - COND PAGO - CODIGO VENEDEDOR
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 6, Font.NORMAL, BaseColor.BLACK));   // colBas
        titPdf.add("\nRIF               :\n");
        tabPdf.addCell(titPdf);

        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.BLACK));
        titPdf.add(rif);
        titPdf.setFont(FontFactory.getFont("tahome", 6, Font.NORMAL, BaseColor.BLACK));   // colBas
        titPdf.add("           CONDICION DE PAGO :  ");
        titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.BLUE));
        titPdf.add(cpg);
        if (tlf.length() > 0) {
          titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.BLACK));   // colBas
          titPdf.add("          TLF:  ");
          titPdf.setFont(FontFactory.getFont("tahome", 6, Font.NORMAL, BaseColor.BLACK));
          titPdf.add(tlf);
        }
        tabPdf.addCell(titPdf);

        tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 7, Font.BOLD, BaseColor.BLACK));
        titPdf.add("\nDel  " + ymd_dmy(fed) + "  al  " + ymd_dmy(feh));
        titPdf.setFont(FontFactory.getFont("tahome", 7, Font.NORMAL, BaseColor.BLACK));
        tabPdf.addCell(titPdf);
        documento.add(tabPdf);

        tabPdf.flushContent();

      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (DocumentException ex) {
      Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void headerPagNot() {

    try {

      tabPdf = new PdfPTable(1);
      tabPdf.setWidthPercentage(90);
      tabPdf.setWidths(new float[]{100f});

      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
      tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);   // .NO_BORDER

      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("verdana", 5, Font.BOLD, BaseColor.GRAY));
      titPdf.add("\nPAGO NOTA$\n");
      tabPdf.addCell(titPdf);

      documento.add(tabPdf);
      tabPdf.flushContent();

      BaseColor colBas = WebColors.getRGBColor("#616A6B");   // Marron  #795548      Azul #154360   Gris osc  #616A6B

      tabPdf = new PdfPTable(9);
      tabPdf.setWidthPercentage(90);
      tabPdf.setWidths(new float[]{15f, 15f, 15f, 23f, 23f, 35f, 20f, 15f, 20f});

      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setBorder(PdfPCell.BOX);   // .NO_BORDER
      tabPdf.getDefaultCell().setBackgroundColor(colBas);

      // 1 Fec pago
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("F.PAGO\n");
      tabPdf.addCell(titPdf);

      // 2 No Nota
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("NOTA\n");
      tabPdf.addCell(titPdf);

      // 3 No Recibo
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("RECIBO\n");
      tabPdf.addCell(titPdf);

      // 5 Banco Emisor
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("BANCO EMISOR\n");
      tabPdf.addCell(titPdf);

      // 6 banco Receptor
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("BANCO RECEPTOR\n");
      tabPdf.addCell(titPdf);

      // 7 Referencia
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("REFERENCIA\n");
      tabPdf.addCell(titPdf);

      // 7 Importe
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("MONTO Bs\n");
      tabPdf.addCell(titPdf);

      // 8 tasa
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("TASA\n");
      tabPdf.addCell(titPdf);

      // 9 Importe $
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("MONTO $\n");
      tabPdf.addCell(titPdf);

      documento.add(tabPdf);
      tabPdf.flushContent();

    } catch (DocumentException ex) {
      Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  public void headerPagRet() {

    try {

      tabPdf = new PdfPTable(1);
      tabPdf.setWidthPercentage(90);
      tabPdf.setWidths(new float[]{100f});

      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
      tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);   // .NO_BORDER

      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("verdana", 5, Font.BOLD, BaseColor.GRAY));
      titPdf.add("PAGO RETENCION IVA\n");
      tabPdf.addCell(titPdf);

      documento.add(tabPdf);
      tabPdf.flushContent();

      BaseColor colBas = WebColors.getRGBColor("#85929E");   // Naranja #E59866   Gris oscuro  #616A6B   Gris claro #85929E

      tabPdf = new PdfPTable(9);
      tabPdf.setWidthPercentage(90);
      tabPdf.setWidths(new float[]{15f, 15f, 15f, 23f, 23f, 35f, 20f, 15f, 20f});

      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setBorder(PdfPCell.BOX);   // .NO_BORDER
      tabPdf.getDefaultCell().setBackgroundColor(colBas);

      // 1 Fec pago
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("F.PAGO\n");
      tabPdf.addCell(titPdf);

      // 2 No Nota
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("FACTURA\n");
      tabPdf.addCell(titPdf);

      // 3 No Recibo
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("RECIBO\n");
      tabPdf.addCell(titPdf);

      // 4 Banco Emisor
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("BANCO EMISOR\n");
      tabPdf.addCell(titPdf);

      // 5 banco Receptor
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("BANCO RECEPTOR\n");
      tabPdf.addCell(titPdf);

      // 6 Referencia
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("REFERENCIA\n");
      tabPdf.addCell(titPdf);

      // 7 Importe
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("MONTO $\n");
      tabPdf.addCell(titPdf);

      // 8 tasa
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("TASA\n");
      tabPdf.addCell(titPdf);

      // 9 Importe $
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.WHITE));
      titPdf.add("MONTO Ret Bs\n");
      tabPdf.addCell(titPdf);

      documento.add(tabPdf);
      tabPdf.flushContent();

    } catch (DocumentException ex) {
      Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  public void detVendedor() {
    try {
      //Nota -------------------------------------
      //
      Importadora imp = new Importadora();
      tabPdf = new PdfPTable(1);
      tabPdf.setWidthPercentage(90);
      tabPdf.setWidths(new float[]{100f});
      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
      tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);  // .BOX  (cuadro)
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 6, Font.BOLD, BaseColor.GRAY));
      titPdf.add("  " + imp.getNomVend() + "\n\n");
      titPdf.setAlignment(Element.ALIGN_LEFT);
      PdfPCell cell = new PdfPCell();
      cell.setVerticalAlignment(Element.ALIGN_LEFT);
      cell.setHorizontalAlignment(Element.ALIGN_LEFT);
      cell.setBorder(PdfPCell.NO_BORDER);
      cell.addElement(titPdf);
      tabPdf.addCell(cell);
      documento.add(tabPdf);
      tabPdf.flushContent();
    } catch (DocumentException ex) {
      Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  // Salto Linea
  public void SaltoLinea() {
    try {
      titPdf = new Paragraph();
      titPdf.add("\n");
      documento.add(titPdf);
    } catch (DocumentException ex) {
      Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void getDatosCte() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT nom,rif,dir,tip,cop,tlf,dic "
              + "FROM clientes "
              + "where coc =  '" + coc + "'";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        noc = rs.getString("nom");
        rif = rs.getString("rif");
        cpg = rs.getString("cop");
        tip = rs.getString("tip");
        tlf = rs.getString("tlf");
        dic = rs.getDouble("dic");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Pdf_EstadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void getSaldosCte() {

    tpa = 0;   // total pago  
    tp$ = 0;   // total pago $
    tsi$ = 0;  // Saldo Inicial $
    tsiB = 0;  // Saldo Inicial Bs

    String tpp = "0";

    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      //y.tpd,nno,nfa,fno,fen,tdo,toi,trd,fpa "
      String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.Notas ("
              + "nrc  int,"
              + "tpd  int,"
              + "nno  int,"
              + "nfa  int,"
              + "fno  varchar(08),"
              + "fen  varchar(08),"
              + "fpa  varchar(08),"
              + "tno  Decimal(15,2),"
              + "toi  Decimal(15,2),"
              + "tor  Decimal(15,2),"
              + "tbU  Decimal(15,2),"
              + "tbB  Decimal(15,2))"
              + "NOT LOGGED ON COMMIT PRESERVE ROWS";
      st.execute(sql);

      sql = "insert into SESSION.Notas "
              + "SELECT 0,0,nne nno,nfa,fne fno,fee fen,'' fpa,tne-tdn tno,toi,tor,0,0 "
              + "FROM notaent "
              + "where coc = '" + coc + "' "
              + "  and fne < '" + fed + "'";
      st.execute(sql);

      sql = "insert into SESSION.Notas "
              + "SELECT 0,1,ncr nno,nfa,fnc fno,'' fen,'' fpa,tnc*-1 tno,0 toi,0 tor,0,0 "
              + "FROM notaCred "
              + "where coc ='" + coc + "' "
              + "  and fnc < '" + fed + "'";
      st.execute(sql);

      sql = "UPDATE SESSION.Notas "
              + "Set fpa = (select nullif(max(fpa),'') from recibocobroD y Where SESSION.Notas.nno=y.nno),"
              + "    nrc = (select nullif(nrc,0)       from recibocobroD y Where SESSION.Notas.nno=y.nno FETCH FIRST 1 ROWS ONLY)";
      st.execute(sql);

      sql = "insert into SESSION.Notas "
              + "SELECT 0,1,0 nno,0 nfa,'*SF*' fne,'' fen,'' fpa,0tno,0 toi,0 tor, (tpa/tas),0 "
              + "FROM recibocobroP "
              + "where coc ='" + coc + "' "
              + "  and nno=0 "
              + "  and nrc >0 "
              + "  and nrc in (select nrc from SESSION.Notas) "
              + "  and tip='5'";
      st.execute(sql);

      sql = "UPDATE SESSION.Notas "
              + "Set fpa =  (select nullif(max(fpa),'') from recibocobroD y Where SESSION.Notas.nno=y.nno),"
              + "    tbU =  (select nullif(sum(tpa / tas),0) from recibocobroP y Where SESSION.Notas.nno=y.nno and tip<>'2' and tip<>'4'),"
              + "    tbB =  (select nullif(sum(tpa),0) from recibocobroP y Where SESSION.Notas.nfa=y.nno and (tip='2' or tip='4'))";
      st.execute(sql);

      // Saldo Notas
      //
      tp$ = 0;
      tpB = 0;
      sql = "SELECT sum(tno) tno,sum(toi-tor) trd,sum(tbU) tbU, sum(tbB) tbB "
              + "FROM SESSION.Notas ";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        tsi$ = rs.getDouble("tno");
        tsiB = rs.getDouble("trd");
        tp$ = rs.getDouble("tbU");
        tpB = rs.getDouble("tbB");
        tsi$ = tsi$ - tp$;
        tsiB = tsiB - tpB;
      }
      tsf$ = 0;  // Saldo Final $
      tsfB = 0;  // Saldo Final Bs

      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_PagosClientes.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(Consulta_ImagenProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Consulta_ImagenProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Consulta_ImagenProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Consulta_ImagenProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        //new Pdf_EstadoCuenta("297426736", "20210101", "20220212");
        new Pdf_EstadoCuenta("312474440", "20210101", "20220212");
      }
    });
  }

}
