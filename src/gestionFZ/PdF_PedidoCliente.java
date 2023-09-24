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
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import static comun.MetodosComunes.FileExist;
import static comun.MetodosComunes.FileSave;
import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.ymd_dmy;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import modelo.ConexionSQL;
import modelo.Importadora;
import static gestionFZ.Menu.VecPar;
import static gestionFZ.Registro_PedidoCliente.indprm;
import static gestionFZ.Registro_PedidoCliente.labMsgP;
import static gestionFZ.Registro_PedidoCliente.labRutPdfPed;

public class PdF_PedidoCliente {

  PdfPTable tabPdf;  // Declarar  tabPdf 
  Paragraph titPdf;  // Declarar titPdf

  int pag = 0;
  int lin = 0;
  int des = 0;   // Destino 0=Cliente, 1=Grupo Faz
  double cnp = 0;

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
        new PdF_PedidoCliente(15940, 1);
      }
    });
  }

  public PdF_PedidoCliente(int npe, int ind) {

    des = ind;
    String tx1 = "";
    String tx2 = "";
    String tx3 = "";
    String tx4 = "";
    String tx5 = "";
    String tx6 = "";
    String tx7 = "";
    String tx8 = "";
    String tx9 = "";
    String tx10 = "";
    String tx11 = "";
    String tx12 = "";
    String tx13 = "";
    String tx14 = "";
    String tx15 = "";

    //Lista Parametros
    for (int i = 0; i < VecPar.length; i++) {
      //  Observaciones Documento Pedido
      if (i == 3) {
        tx1 = "\n  " + VecPar[i].trim() + "\n";
      }
      if (i == 4) {
        tx2 = "\n  " + VecPar[i].trim() + "\n";
      }
      if (i == 5) {
        tx3 = "\n  " + VecPar[i].trim();
      }
      if (i == 6) {
        tx4 = "\n  " + VecPar[i].trim() + "\n";
      }
      if (i == 7) {
        tx5 = "\n  " + VecPar[i].trim() + "\n";
      }
      if (i == 8) {
        tx6 = "\n  " + VecPar[i].trim() + "\n";
      }
      if (i == 9) {
        tx7 = "\n  " + VecPar[i].trim();
      }
      if (i == 10) {
        tx8 = "\n  " + VecPar[i].trim() + "\n";
      }
      if (i == 11) {
        tx9 = "\n  " + VecPar[i].trim() + "\n";
      }
      if (i == 12) {
        tx10 = "\n  " + VecPar[i].trim();
      }
      if (i == 13) {
        tx11 = "\n  " + VecPar[i].trim();
      }
      if (i == 14) {
        tx12 = "\n  " + VecPar[i].trim();
      }
      if (i == 15) {
        tx13 = "\n  " + VecPar[i].trim() + "\n\n";
      }
      if (i == 16) {
        tx14 = "\n  " + VecPar[i].trim();
      }
      if (i == 17) {
        tx15 = "\n  " + VecPar[i].trim();
      }
    }

    // campos importadora
    Importadora imp = new Importadora();
    double dcr = imp.getDiasCred();    // Dias credito (30 dias)

    // Campos Vendedor
    String cov = imp.getCodVend();     // Codigo Vendedor

    // Campos Header (pedidoH)
    String coc = "";  // codigo cliente
    String noc = "";  // nombre cliente
    String obs = "";  // Observacion pedido
    String fep = "";  // fecha pedido
    String fac = "";  // factura
    String tip = "";  // tipo cliente
    double por = 0;   // porcentaje descuento
    double ppm = 0;   // Porcentaje Promocion
    double dic = 0;   // dias credito

    // Campos Clientes
    String dir = "";  // direccion
    String tlf = "";  // tlf
    String rif = "";  // rif
    String cto = "";  // contacto
    String cpg = "";  // Copa (0=credito,1=prepago
    String edo = "";  // Estado
    String ciu = "";  // Ciudad

    String Tip = "";  // tipo cliente

    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      //Datos Header pedido
      String sql = "SELECT coc,"
              + "(select nom from clientes where clientes.coc=pedidoH.coc) noc,"
              + "(select dic from clientes where clientes.coc=pedidoH.coc) dic,"
              + "fep,fel,fac,obs,por,ppm "
              + "FROM pedidoH "
              + "where npe =  " + npe + " ";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        coc = rs.getString("coc");
        noc = rs.getString("noc");
        fep = rs.getString("fep");
        fac = rs.getString("fac");
        por = rs.getDouble("por");
        ppm = rs.getDouble("ppm");
        dic = rs.getDouble("dic");
        obs = rs.getString("obs");
        obs = obs.toUpperCase().trim();
        if (obs.length() > 104) {
          String obx = obs.substring(0, 104).trim();
          int i = obx.length();
          while (i > 0) {
            if (obx.substring(i - 1, i).equals(" ")) {
              break;
            }
            i--;
          }
          obs = obs.substring(0, i) + "\n\n                         " + obs.substring(i, obs.length());
        }
      }
      if (ppm > 0) {
        por = ppm;
      }
      fep = ymd_dmy(fep);
      //Datos Cliente
      sql = "SELECT coc,rif,dir,edo,ciu,tlf,con,tip,cop,dic "
              + "FROM clientes "
              + "where coc =  '" + coc + "'";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        rif = rs.getString("rif");
        tip = rs.getString("tip");
        edo = rs.getString("edo");
        ciu = rs.getString("ciu");
        dir = rs.getString("dir");
        ciu = rs.getString("ciu");
        edo = rs.getString("edo");
        tlf = rs.getString("tlf");
        cto = rs.getString("con");
        cpg = rs.getString("cop");
        dic = rs.getDouble("dic");
      }

      if (edo.length() > 0 && dir.indexOf(" EDO") == -1) {
        edo = ", Edo. " + edo;
      } else {
        edo = "";

      }
      if (ciu.length() > 0 && dir.indexOf(ciu) == -1) {
        ciu = ", " + ciu;
      } else {
        ciu = "";
      }

      //dir = dir + edo.trim() + " " + ciu.trim();
      Tip = "CLIENTE MAYORISTA";
      if (tip.equals("1")) {
        Tip = "CLIENTE DETAL";
      }

      if (cpg.equals("0")) {
        cpg = " CREDITO " + MtoEs(dic, 0) + " Dias";
      } else {
        cpg = " - PREPAGO -";
      }

      if (tlf.length() > 22) {
        tlf = tlf.substring(0, 22);
      }

      if (cto.length() > 22) {
        cto = cto.substring(0, 22);
      }

      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_Clientes.class.getName()).log(Level.SEVERE, null, ex);
    }
    String nox = noc.trim();
    nox = nox.replace(" ", "");
    nox = nox.replace("  ", "");
    nox = nox.replace(",", "");
    nox = nox.replace("C.A.", "");
    nox = nox.replace(".", "");
    nox = nox.replace("(", "").replace(")", "");
    nox = nox.replace(" CA ", "");
    nox = nox.replace(" SA ", "");
    nox = nox.replace(" DE ", "");
    nox = nox.replace("  ", "");
    nox = nox.replace(" ", "");
    if (nox.length() > 12) {
      nox = nox.substring(0, 12);
    }
    String fil = "rep/pdf/pedidos/Pedido_Cliente_" + npe + "_" + nox + ".pdf";
    if (des == 1) {
      fil = "rep/pdf/pedidos/Pedido_GrupoFZ_" + npe + "_" + nox + ".pdf";
    }
    System.out.println(" fil=" + fil);

    if (FileSave(fil)) {

      labMsgP.setText("- Generando Lista - espere.. ");
      // Se crea el documento
      Document documento = new Document(PageSize.LETTER, 20, 20, 10, 10);      //  Hoja Vertical
      // Se crea el OutputStream para el fichero donde queremos dejar el pdf.
      FileOutputStream ficheroPdf = null;
      try {
        ficheroPdf = new FileOutputStream(fil);
      } catch (FileNotFoundException ex) {
        Logger.getLogger(PdF_PedidoCliente.class.getName()).log(Level.SEVERE, null, ex);
      }

      try {
        // Se asocia el documento al OutputStream y se indica que el espaciado entre
        // lineas sera de 20. Esta llamada debe hacerse antes de abrir el documento
        PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);

        // Se abre el documento.
        documento.open();

        // tabPdf 1 -  Coloca header Logo Grupo FZ
        //
        tabPdf = new PdfPTable(1);
        tabPdf.setWidthPercentage(100);        // Datos de porcentaje a la tabPdf (tamaño ancho)
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
        } catch (Exception e) {
          e.printStackTrace();
        }
        documento.add(Chunk.NEWLINE);
        documento.add(tabPdf);
        tabPdf.flushContent();

        tabPdf = new PdfPTable(3);
        tabPdf.setWidthPercentage(100);
        tabPdf.setWidths(new float[]{10f, 60f, 30f});
        tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);  // .BOX  (cuadro)
        BaseColor colBas = WebColors.getRGBColor("#A00000");   // Azul

        noc = "\n" + noc + "\n";

        // CLIENTE - No. PEDIDO
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.BLACK));   // colBas
        titPdf.add("\nCLIENTE     :\n");
        tabPdf.addCell(titPdf);
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.NORMAL, BaseColor.BLACK));
        titPdf.add(noc);
        tabPdf.addCell(titPdf);

        tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 10, Font.BOLD, BaseColor.BLACK));
        titPdf.add("        PEDIDO No.     ");
        titPdf.setFont(FontFactory.getFont("tahome", 11, Font.BOLD, BaseColor.RED));
        titPdf.add(npe + "");

        // Celda
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        //cell.setCalculatedHeight(10);
        colBas = WebColors.getRGBColor("#EAEDED");   // gris
        cell.setBackgroundColor(colBas);
        cell.setBorder(1);
        cell.setPaddingBottom(10);
        //cell.setPaddingTop(5);
        cell.addElement(titPdf);
        tabPdf.addCell(cell);

        //tabPdf.addCell(titPdf);
        documento.add(tabPdf);
        tabPdf.flushContent();

        // DIRECCION - FECHA
        tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.BLACK));
        titPdf.add("\nDIRECCION: \n");
        tabPdf.addCell(titPdf);
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 7, Font.NORMAL, BaseColor.BLACK));
        titPdf.add("\n" + dir + "\n");
        tabPdf.addCell(titPdf);

        tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 7, Font.BOLD, BaseColor.BLACK));
        titPdf.add("\n              FECHA :   ");
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.NORMAL, BaseColor.BLACK));
        titPdf.add(fep + "\n");
        tabPdf.addCell(titPdf);
        documento.add(tabPdf);
        tabPdf.flushContent();

        // TELEFONO - CONTACTo
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.BLACK));   // colBas
        titPdf.add("\nTELEFONO: \n");
        tabPdf.addCell(titPdf);
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.NORMAL, BaseColor.BLACK));
        titPdf.add("\n" + tlf);
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.BLACK));   // colBas
        titPdf.add("         TIPO DE CLIENTE       :  ");
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.NORMAL, BaseColor.BLUE));   // colBas
        titPdf.add(Tip);
        tabPdf.addCell(titPdf);

        tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 7, Font.BOLD, BaseColor.BLACK));
        titPdf.add("\n      CONTACTO :   ");
        titPdf.setFont(FontFactory.getFont("tahome", 6, Font.NORMAL, BaseColor.BLACK));
        titPdf.add(cto + "\n\n");
        tabPdf.addCell(titPdf);
        documento.add(tabPdf);
        tabPdf.flushContent();

        // RIF - COND PAGO - CODIGO VENEDEDOR
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.BLACK));   // colBas
        titPdf.add("\nRIF              :\n");
        tabPdf.addCell(titPdf);

        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.NORMAL, BaseColor.BLACK));
        titPdf.add("\n" + rif);
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.BLACK));   // colBas
        titPdf.add("           CONDICION DE PAGO :  ");
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.NORMAL, BaseColor.BLUE));
        titPdf.add(cpg);
        tabPdf.addCell(titPdf);

        tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 7, Font.BOLD, BaseColor.BLACK));
        titPdf.add("\n  Cod. VENDED :   ");
        titPdf.setFont(FontFactory.getFont("tahome", 9, Font.BOLD, BaseColor.BLACK));
        titPdf.add(cov + "\n");
        tabPdf.addCell(titPdf);

        documento.add(tabPdf);
        tabPdf.flushContent();

        tabPdf = new PdfPTable(1);
        tabPdf.setWidthPercentage(100);
        tabPdf.setWidths(new float[]{100f});
        tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.BLACK));
        titPdf.add("\nNOTA          :    ");
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.BLUE));
        titPdf.add(obs);
        tabPdf.addCell(titPdf);
        documento.add(tabPdf);
        tabPdf.flushContent();

        // 
        // ------------ Detalle Productos Pedido -------------------
        //
        pag = 1;
        headerProductos(documento);

        if (des == 0) {
          tabPdf = new PdfPTable(6);
          tabPdf.setWidthPercentage(100);
          tabPdf.setWidths(new float[]{15f, 70f, 10f, 10f, 12f, 15f});  // Datos del ancho de cada columna.
        } else {
          tabPdf = new PdfPTable(4);
          tabPdf.setWidthPercentage(100);
          tabPdf.setWidths(new float[]{15f, 80f, 12f, 10f});
        }
        tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        //documento.add(Chunk.NEWLINE);
        String cop = "";
        String dep = "";
        String unm = "";
        double can = 0;
        double pum = 0;
        double pud = 0;
        double prc = 0;
        double pop = 0;
        double tot = 0;
        double tog = 0;

        ConexionSQL bdsql = new ConexionSQL();
        Connection con = bdsql.Conectar();
        try {
          Statement st = con.createStatement();
          String sql = "SELECT cop,dep,unm,can,prm,prd,por "
                  + "FROM pedidoD "
                  + "where npe =  " + npe + " ";

          ResultSet rs = st.executeQuery(sql);
          while (rs.next()) {
            cop = rs.getString("cop");
            dep = rs.getString("dep");
            unm = rs.getString("unm");
            can = rs.getDouble("can");
            pum = rs.getDouble("prm");
            pud = rs.getDouble("prd");
            pop = rs.getDouble("por");

            String vax = "";
            String pox = "";
            if (pop > 0) {
              pox = MtoEs(pop, 2).replace(",00", "");
              vax = "                            -  (  DESCUENTO PROMOCION " + pox + " %  )   -";
            }
            cnp = cnp + can;

            // Codigo producto
            titPdf = new Paragraph();
            titPdf.setFont(FontFactory.getFont("verdana", 7, Font.NORMAL, BaseColor.BLACK));
            titPdf.add(cop);
            titPdf.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell();
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.BOX);
            cell.addElement(titPdf);
            tabPdf.addCell(cell);

            titPdf = new Paragraph();
            titPdf.setFont(FontFactory.getFont("verdana", 7, Font.NORMAL, BaseColor.BLACK));
            titPdf.add("  " + dep);
            if (indprm == 1) {
              titPdf.setFont(FontFactory.getFont("verdana", 6, Font.BOLD, BaseColor.GRAY));
              titPdf.add("\n " + vax + "\n");
            }
            titPdf.setAlignment(Element.ALIGN_LEFT);
            cell = new PdfPCell();
            cell.setVerticalAlignment(Element.ALIGN_LEFT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.BOX);
            cell.addElement(titPdf);
            cell.setPaddingBottom(5);
            tabPdf.addCell(cell);

            // Cantidad
            String cax = " " + MtoEs(can, 2).replace(",00", "") + " ";
            titPdf = new Paragraph();
            titPdf.setFont(FontFactory.getFont("verdana", 7, Font.NORMAL, BaseColor.BLACK));
            titPdf.setAlignment(Element.ALIGN_CENTER);
            titPdf.add(cax);
            cell = new PdfPCell();
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.BOX);
            cell.addElement(titPdf);
            tabPdf.addCell(cell);

            // Empaque
            titPdf = new Paragraph();
            titPdf.setFont(FontFactory.getFont("verdana", 7, Font.NORMAL, BaseColor.BLACK));
            titPdf.setAlignment(Element.ALIGN_CENTER);
            titPdf.add(unm);
            cell = new PdfPCell();
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.BOX);
            cell.addElement(titPdf);
            tabPdf.addCell(cell);

            if (des == 0) {

              //  documento.newPage();
              prc = pum;       // Precio Mayor
              if (tip.equals("1")) {
                prc = pud;     // Precio Detal
              }

              if (pop > 0) {
                //  descuento Promocion producto
                prc = prc - (prc * (pop / 100));
              } else {
                prc = prc - (prc * por);
              }

              String mox = MtoEs(prc, 2);
              mox = GetCurrencyDouble(mox);
              prc = GetMtoDouble(mox);

              // Precio
              titPdf = new Paragraph();
              titPdf.setAlignment(Element.ALIGN_CENTER);
              titPdf.setFont(FontFactory.getFont("verdana", 7, Font.NORMAL, BaseColor.BLACK));
              if (des == 0) {
                titPdf.add(MtoEs(prc, 2));
              } else {
                titPdf.add("");
              }
              cell = new PdfPCell();
              cell.setVerticalAlignment(Element.ALIGN_CENTER);
              cell.setHorizontalAlignment(Element.ALIGN_CENTER);
              cell.setBorder(PdfPCell.BOX);
              cell.addElement(titPdf);
              tabPdf.addCell(cell);

              // % Desc
              /*
              titPdf = new Paragraph();
              titPdf.setAlignment(Element.ALIGN_CENTER);
              titPdf.setFont(FontFactory.getFont("verdana", 7, Font.NORMAL, BaseColor.BLACK));
              if (des == 0) {
                titPdf.add(MtoEs(por, 2).replace(",00", ""));
              } else {
                titPdf.add("");
              }
              cell = new PdfPCell();
              cell.setVerticalAlignment(Element.ALIGN_CENTER);
              cell.setHorizontalAlignment(Element.ALIGN_CENTER);
              cell.setBorder(PdfPCell.BOX);
              cell.addElement(titPdf);
              tabPdf.addCell(cell);
               */
              // total
              tot = can * prc;
              tog = tog + tot;

              titPdf = new Paragraph();
              titPdf.setAlignment(Element.ALIGN_CENTER);
              titPdf.setFont(FontFactory.getFont("verdana", 7, Font.NORMAL, BaseColor.BLACK));
              if (des == 0) {
                titPdf.add(MtoEs(tot, 2));
              } else {
                titPdf.add("");
              }
              //tabPdf.addCell(titPdf);
              cell = new PdfPCell();
              cell.setVerticalAlignment(Element.ALIGN_CENTER);
              cell.setHorizontalAlignment(Element.ALIGN_CENTER);
              cell.setBorder(PdfPCell.BOX);
              cell.addElement(titPdf);
              tabPdf.addCell(cell);

            }

            //Añadimos la tabPdf "tabPdf" al documento "documento".
            documento.add(tabPdf);
            tabPdf.flushContent();
            //int pag = documento.getPageNumber();

            lin++;

            int max = 19;
            if (pag > 1) {
              max = 30;
            }

            if (indprm == 1) {
              max = 11;
              if (pag > 1) {
                max = 17;
              }
            }

            if (lin > max) {
              lin = 0;
              documento.newPage();
              pag++;
              headerProductos(documento);
              if (des == 0) {
                tabPdf = new PdfPTable(6);
                tabPdf.setWidthPercentage(100);
                tabPdf.setWidths(new float[]{15f, 70f, 10f, 10f, 12f, 15f});  // Datos del ancho de cada columna.
              } else {
                tabPdf = new PdfPTable(4);
                tabPdf.setWidthPercentage(100);
                tabPdf.setWidths(new float[]{15f, 80f, 12f, 10f});
              }
              tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
              tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
              tabPdf.getDefaultCell().setBorder(PdfPCell.BOX);  // .BOX  (cuadro)
            }

          }

          tabPdf = new PdfPTable(3);
          tabPdf.setWidthPercentage(100);
          tabPdf.setWidths(new float[]{90f, 10f, 15f});
          tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
          tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
          tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

          if (indprm == 0) {
            if (lin <= 19) {
              lin = 0;
              //documento.newPage();
              pag++;
              //headerProductos(documento);
            }
          } else {
            if (lin <= 11) {
              lin = 0;
              pag++;
            }
          }

          SaltoLinea();

          tabPdf = new PdfPTable(1);
          tabPdf.setWidthPercentage(100);
          tabPdf.setWidths(new float[]{100f});
          tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_UNDEFINED);
          tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
          tabPdf.getDefaultCell().setBorder(PdfPCell.BOX);  // .BOX  (cuadro)

          titPdf = new Paragraph();
          if (des == 0) {
            titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.BLACK));
            titPdf.add("\n  TOTAL PEDIDO #         ");
            titPdf.setFont(FontFactory.getFont("verdana", 8, Font.BOLD, BaseColor.BLACK));
            titPdf.add(MtoEs(tog, 2) + "                                               ");
          }
          titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.BLACK));
          if (des == 0) {
            titPdf.add("CANT PRODUCTOS  =  ");
          } else {
            tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            titPdf.add("\nCANT PRODUCTOS  =  ");
          }
          titPdf.setFont(FontFactory.getFont("verdana", 8, Font.BOLD, BaseColor.BLACK));
          titPdf.add(MtoEs(cnp, 2).replace(",00", "") + "\n\n");
          tabPdf.addCell(titPdf);

          tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

          titPdf = new Paragraph();
          titPdf.setFont(FontFactory.getFont("tahome", 9, Font.BOLD, BaseColor.BLACK));
          titPdf.add(tx1);
          titPdf.setFont(FontFactory.getFont("tahome", 9, Font.BOLD, BaseColor.BLACK));
          titPdf.add(tx2);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));
          titPdf.add(tx3);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));
          titPdf.add(tx4);
          titPdf.setFont(FontFactory.getFont("tahome", 9, Font.BOLD, BaseColor.BLACK));
          titPdf.add(tx5);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK));
          titPdf.add(tx6);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));
          titPdf.add(tx7);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));
          titPdf.add(tx8);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK));
          titPdf.add(tx9);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));
          titPdf.add(tx10);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));
          titPdf.add(tx11);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));
          titPdf.add(tx12);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));
          titPdf.add(tx13);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));
          titPdf.add(tx14);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));
          titPdf.add(tx15);
          titPdf.setFont(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));

          tabPdf.addCell(titPdf);

          documento.add(tabPdf);
          tabPdf.flushContent();

          st.close();
          con.close(); // cerramos la conexion

          // caso errores sql
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }

        //SaltoLinea();
        //SaltoLinea();
        String vax = "FIN";
        //IncluirTituloBold(vax, 8);
        //documento.add(titPdf);

        // Cerrar documento
        documento.close();

        labMsgP.setText("- Reporte Generado !");
        labRutPdfPed.setBackground(Color.green);

      } catch (DocumentException ex) {
        Logger.getLogger(PdF_PedidoCliente.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      labMsgP.setText("- CIERRE EL PDF  -");
    }
  }

  public void headerProductos(Document documento) {
    try {
      if (des == 0) {
        tabPdf = new PdfPTable(6);
        tabPdf.setWidthPercentage(100);
        tabPdf.setWidths(new float[]{15f, 70f, 10f, 10f, 12f, 15f});  // Datos del ancho de cada columna.
      } else {

        tabPdf = new PdfPTable(4);
        tabPdf.setWidthPercentage(100);
        tabPdf.setWidths(new float[]{15f, 80f, 12f, 10f});
      }

      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);  // .BOX  (cuadro)

      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
      titPdf.add("");
      tabPdf.addCell(titPdf);

      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
      titPdf.add("");
      tabPdf.addCell(titPdf);

      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
      titPdf.add("");
      tabPdf.addCell(titPdf);

      if (des == 0) {
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
        titPdf.add("");
        tabPdf.addCell(titPdf);

        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
        titPdf.add("");
        tabPdf.addCell(titPdf);

      }
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 8, Font.NORMAL, BaseColor.GRAY));
      titPdf.add("   Pag  " + pag + "\n\n");
      tabPdf.addCell(titPdf);

      tabPdf.getDefaultCell().setBorder(PdfPCell.BOX);  // .BOX  (cuadro)
      BaseColor colBas = WebColors.getRGBColor("#1F618D");   // Azul
      tabPdf.getDefaultCell().setBackgroundColor(colBas);

      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
      titPdf.add("CODIGO\n");
      tabPdf.addCell(titPdf);

      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
      titPdf.add("PRODUCTO\n");
      tabPdf.addCell(titPdf);

      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
      titPdf.add("CANT\n");
      tabPdf.addCell(titPdf);

      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
      titPdf.add("UM\n");
      tabPdf.addCell(titPdf);

      if (des == 0) {
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
        titPdf.add("PRECIO #\n");
        tabPdf.addCell(titPdf);

        //titPdf = new Paragraph();
        //titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
        //titPdf.add("%DESC\n");
        //tabPdf.addCell(titPdf);
        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
        titPdf.add("TOTAL #\n");
        tabPdf.addCell(titPdf);
      }
      documento.add(tabPdf);
      tabPdf.flushContent();

    } catch (DocumentException ex) {
      Logger.getLogger(PdF_PedidoCliente.class
              .getName()).log(Level.SEVERE, null, ex);
    }

  }

  // Salto Linea
  public void SaltoLinea() {
    titPdf = new Paragraph();
    titPdf.add("\n");
  }

}

//String tox = String.format("%-20s", MtoEs(tog, 2)+"  ");
//LineSeparator ls = new LineSeparator();
//documento.add(new Chunk(ls));
//documento.add(new Chunk("/n"));
// Tabla 2
//

/*
            Paragraph col = new Paragraph("\n\n - CATALOGO DE PRODUCTOS - ( " + Tip + " )\n\n\n  " + dex + " ");
            col.getFont().setStyle(Font.BOLD);
            col.getFont().setSize(10);
            col.setAlignment(Element.ALIGN_MIDDLE);
            tabPdf.addCell(col);
 */
//tabPdf.getDefaultCell().setBorder(1);
//tabPdf.getDefaultCell().getFixedHeight();
//tabPdf.getDefaultCell().getFollowingIndent();
//tabPdf.setTotalWidth(500);
//tabPdf.getDefaultCell().setMinimumHeight(4);
//tabPdf.addCell(tabPdf.getDefaultCell());
//PdfPCell blankCell = new PdfPCell();
//blankCell.setMinimumHeight(15);
//tabPdf.addCell(blankCell);
// Document document = new Document(PageSize.A4, left, right, top, bottom);
// Document documento = new Document(PageSize.LETTER_LANDSCAPE, 20, 20, 20, 20);
// Document documento = new Document(new Rectangle(792, 612));  // (LANDSCAPE)
// Document documento = new Document(PageSize.LETTER.rotate(), 20, 20, 20, 20); // Hoja Horizontal
/*
              //float[] columnWidths = new float[]{15f, 55f, 15f, 15f};
              //tabPdf.setWidths(columnWidths);
 */
//documento.add(Chunk.NEWLINE);
/*
        tabPdf.setWidthPercentage(100);
        tabla = new PdfPTable(1);
        tabPdf.setWidths(new float[]{100f});
        tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        tabPdf.getDefaultCell().setBorder(PdfPCell.NO_BORDER);  // .BOX  (cuadro)

        titPdf = new Paragraph();
        titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.BLACK));   // colBas
        titPdf.add("_____________________________________________________________________________________________________");
        tabPdf.addCell(titPdf);
        documento.add(tabla);
        tabPdf.flushContent();
 */
