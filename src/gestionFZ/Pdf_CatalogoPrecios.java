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
import comun.Mensaje;
import static comun.MetodosComunes.FileExist;
import static comun.MetodosComunes.FileSave;
import static comun.MetodosComunes.MtoEs;
import static gestionFZ.Menu.pdfdet;
import static gestionFZ.Menu.pdfmay;
import static gestionFZ.Menu.proimg;
import static gestionFZ.Menu.tamimg;
import static gestionFZ.Registro_CatalogoProductos.labMsgL;
import static gestionFZ.Registro_CatalogoProductos.labRutPdf;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import modelo.ConexionSQL;

public class Pdf_CatalogoPrecios {

  PdfPTable tabPdf;  // Declarar  tabPdf 
  Paragraph titPdf;  // Declarar titPdf
  int conimg = 0;
  int conlin = 0;
  int paguno = 0;
  int pag = 0;
  int can = 0;
  int cag = 0;

  public Pdf_CatalogoPrecios(String tip, String fec, double por, String indexi, String dep) {

    String dpc = dep;
    String tx1 = "";
    String tx2 = "";

    if (por > 0) {
      por = por / 100;
    }

    String Tip = "Mayorista";
    if (tip.equals("D")) {
      Tip = "Cliente";
    }
    if (por > 0) {
      Tip = " * " + Tip + " * ";
    }

    String dpt = dep;
    if (dpt.length() > 0) {
      dpt = dpt + "_";
    }

    String pdf = "ListaPrecios_" + Tip.substring(3, 6) + "_ConFac_" + dpt + fec + "_.pdf";
    if (por == 0) {
      pdf = "ListaPrecios_" + Tip.substring(0, 3) + "_" + dpt + fec + "_.pdf";
    }

    String fil = "rep/pdf/catalogo/" + pdf;

    if (FileSave(fil)) {

      labMsgL.setText("- Generando Lista - espere.. ");
      // Se crea el documento
      Document documento = new Document(PageSize.LETTER, 20, 20, 20, 20);            //  Hoja Vertical
      // Se crea el OutputStream para el fichero donde queremos dejar el pdf.
      FileOutputStream ficheroPdf = null;
      try {
        ficheroPdf = new FileOutputStream(fil);
      } catch (FileNotFoundException ex) {
        Logger.getLogger(Pdf_CatalogoPrecios.class.getName()).log(Level.SEVERE, null, ex);
      }

      try {
        // Se asocia el documento al OutputStream y se indica que el espaciado entre
        PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
        documento.open(); // Se abre el documento.
        ConexionSQL bdsql = new ConexionSQL();
        Connection con = bdsql.Conectar();
        try {
          Statement st = con.createStatement();
          Statement st2 = con.createStatement();
          Statement st3 = con.createStatement();

          String sql = "Select distinct dep,nom,ob1,ob2 "
                  + "from departamento "
                  + "where dep like '%" + dep + "%' "
                  + "order by dep";

          ResultSet rs = st.executeQuery(sql);
          while (rs.next()) {
            dpt = rs.getString("dep");
            dpc = dpt;
            if (rs.getString("nom") != null) {
              dpc = rs.getString("nom");
            }
            tx1 = rs.getString("ob1");
            tx2 = rs.getString("ob2");

            int i = dpc.indexOf("-");
            if (i >= 0) {
              dpc = dpc.substring(i + 1, dpc.length());
            }

            //System.out.println("dpt=" + dpt + " can=" + can);
            //if (can > 3) {
            documento.newPage();
            //}

            conlin = 1;
            conimg = 0;
            paguno = 0;
            pag = 1;
            can = 0;
            //
            //--------------------- tabPdf 1 (Header )
            //

            tabPdf = new PdfPTable(2);
            tabPdf.setWidthPercentage(100);           // Datos de porcentaje a la tabPdf (tamaño ancho)
            tabPdf.setWidths(new float[]{50f, 50f});  // Datos del ancho de cada columna.
            tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);

            String img = "imgdep\\LogoCatalogo.jpg";
            try {
              Image foto = Image.getInstance(img);
              img = "imgdep\\LogoCatalogo.jpg";
              if (FileExist(img)) {
                foto = Image.getInstance(img);
              }
              foto.scaleToFit(20, 20);
              foto.setAlignment(Chunk.ALIGN_CENTER);
              // Alto imagen
              BufferedImage bimg = ImageIO.read(new File(img));
              tabPdf.addCell(foto);

            } catch (Exception e) {
              e.printStackTrace();
            }

            //BaseColor colBas = WebColors.getRGBColor("#A00000");   // Azul
            BaseColor colBas = WebColors.getRGBColor("#117A65");   // Verde

            String tex = "\n\n-  CATALOGO DE PRECIOS  - \n";
            titPdf = new Paragraph();
            titPdf.setFont(FontFactory.getFont("tahome", 14, Font.BOLD, BaseColor.BLACK));
            titPdf.add(tex);
            titPdf.setFont(FontFactory.getFont("verdana", 10, Font.BOLD, BaseColor.GRAY));
            titPdf.add("\n " + Tip + " \n\n");
            titPdf.setFont(FontFactory.getFont("calibri", 9, Font.BOLD, BaseColor.DARK_GRAY));
            //titPdf.add("\n" + Dex + "\n\n");
            // anexos
            titPdf.setFont(FontFactory.getFont("calibri", 8, Font.NORMAL, colBas));
            titPdf.add("\n" + tx1 + "\n");
            titPdf.setFont(FontFactory.getFont("calibri", 8, Font.NORMAL, colBas));
            titPdf.add("\n" + tx2 + "\n");
            tabPdf.addCell(titPdf);

            documento.add(tabPdf);
            tabPdf.flushContent();

            //--------------------- tabPdf 2 (Header)
            //
            //Incluir tabPdf al documento
            tabPdf = new PdfPTable(1);
            tabPdf.setWidthPercentage(100);        // Datos de porcentaje a la tabPdf (tamaño ancho)
            tabPdf.setWidths(new float[]{100f});  // Datos del ancho de cada columna.

            tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            //tabPdf.getDefaultCell().setBorder(0);  // sin border cuadriculo

            try {
              Image foto = Image.getInstance("imgdep\\logoimp.png");
              img = "imgdep/" + dpt.substring(i + 1, dpt.length()) + ".jpg";
              if (FileExist(img)) {
                foto = Image.getInstance(img);
              }
              foto.scaleToFit(24, 24);
              foto.setAlignment(Chunk.ALIGN_CENTER);
              tabPdf.addCell(foto);

              //conimg = conimg + th;
            } catch (Exception e) {
              e.printStackTrace();
            }
            documento.add(tabPdf);
            tabPdf.flushContent();

            //--------------------- tabPdf 3  (Detalle Productos)
            //
            tabPdf = new PdfPTable(4);
            tabPdf.setWidthPercentage(100);
            tabPdf.setWidths(new float[]{14f, 60f, 15f, 25f});  // Datos del ancho de cada columna.
            tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            //colBas = WebColors.getRGBColor("#1F618D");   // Azul
            colBas = WebColors.getRGBColor("#117A65");   // Azul
            tabPdf.getDefaultCell().setBackgroundColor(colBas);

            //tabPdf.setTotalWidth(555f);
            //tabPdf.setLockedWidth(true);
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
            titPdf.add("REF #\n");
            tabPdf.addCell(titPdf);

            titPdf = new Paragraph();
            titPdf.setFont(FontFactory.getFont("tahome", 8, Font.BOLD, BaseColor.WHITE));
            titPdf.add("IMAGEN\n");
            tabPdf.addCell(titPdf);

            //SaltoLinea();
            tabPdf.getDefaultCell().setBackgroundColor(BaseColor.WHITE);

            // ver ultimo lista
            String sql2 = "Select dep,cop,nom "
                    + "from listaprecios "
                    + "where sta='0' "
                    + "and dep='" + dpt + "' "
                    + "order by dep,cop";
            String cox = "";
            ResultSet rs2 = st2.executeQuery(sql2);
            while (rs2.next()) {
              cox = rs2.getString("cop");
            }

            sql2 = "Select dep,cop,nom,pum,pud,stk,pag "
                    + "from listaprecios "
                    + "where sta='0' "
                    + "and dep='" + dpt + "' "
                    + "order by dep,cop";

            rs2 = st2.executeQuery(sql2);
            while (rs2.next()) {
              String cop = rs2.getString("cop");
              String nom = rs2.getString("nom");
              String spg = rs2.getString("pag");
              double pum = rs2.getDouble("pum");
              double pud = rs2.getDouble("pud");
              double stk = rs2.getDouble("stk");

              if (stk < 0) {
                stk = 0;
              }

              // Empaque y referencia P.U.
              double ppd = 0;
              String emp = "", ref = "";
              String sql3 = "SELECT tx1,ref,por "
                      + "FROM empaqueproducto "
                      + "where cop='" + cop + "'";
              ResultSet rs3 = st3.executeQuery(sql3);
              while (rs3.next()) {
                emp = rs3.getString("tx1");
                ref = rs3.getString("ref");
                ppd = rs3.getDouble("por");
              }

              // Aplica porcentaje descuento (5% pp)
              if (ppd == 0 && por > 0) {
                pum = pum - (pum * por);
                pud = pud - (pud * por);
              }

              if (ppd == 1) {
                //System.out.println("nom=" + nom + "pum= "+pum);
              }

              //Salto pagina manual x imagen 
              //if (spg.equals("1")) {
              //documento.newPage();
              //}
              String exi = "";
              if (indexi.equals("S")) {
                exi = "Existencia   " + MtoEs(stk, 0);
              }
              String prc = MtoEs(pum, 2);
              if (tip.equals("D")) {
                prc = MtoEs(pud, 2);
              }

              titPdf = new Paragraph();
              titPdf.setFont(FontFactory.getFont("tahome", 8, Font.NORMAL, BaseColor.BLACK));   // tahome
              titPdf.add("\n\n\n" + cop + "\n\n\n");
              tabPdf.addCell(titPdf);

              titPdf = new Paragraph();
              titPdf.setFont(FontFactory.getFont("tahome", 8, Font.NORMAL, BaseColor.BLACK));
              titPdf.add("\n\n" + nom + "\n\n");

              titPdf.setFont(FontFactory.getFont("tahome", 7, Font.NORMAL, BaseColor.BLACK));
              titPdf.add("\n" + emp + "  -  " + ref + "\n");

              colBas = WebColors.getRGBColor("#117A65");   // Azul
              titPdf.setFont(FontFactory.getFont("calibri", 8, Font.BOLD, colBas));
              titPdf.add("\n" + exi + "\n\n");
              tabPdf.addCell(titPdf);

              titPdf = new Paragraph();
              titPdf.setFont(FontFactory.getFont("tahome", 8, Font.NORMAL, BaseColor.BLACK));
              titPdf.add("\n\n\n" + prc + "\n\n\n");
              tabPdf.addCell(titPdf);

              //System.out.println("1-cop=" + cop + ",conimg=" + conimg);;
              //Añadir image
              try {
                img = "imgprd\\" + cop + ".png";
                if (!FileExist(img)) {
                  img = "imgcia\\SinImagen.png";
                }
                Image foto = Image.getInstance(img);
                foto.scaleToFit(100, 100);
                foto.setAlignment(Chunk.ALIGN_CENTER);
                //tabPdf.addCell(foto);
                PdfPCell cell = new PdfPCell();
                cell.addElement(foto);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                //cell.setCalculatedHeight(90);
                tabPdf.addCell(cell);
                // Alto Celdas
                int th = (int) tabPdf.getRowHeight(0);
                if (th == 0) {
                  th = proimg;
                }
                conimg = conimg + th;
                documento.add(tabPdf);
                tabPdf.flushContent();

                // Salto Pagina
                if (conlin == 3 && paguno == 0) {

                  if (conimg <= tamimg) {
                    //System.out.println("1-cop=" + cop + ",conimg=" + conimg + ",tamimg=" + tamimg);
                  }
                  //System.out.println("dpc=" + dpc + " can=" + can);
                  documento.newPage();
                  titPdfDepartamento(documento, dpc);
                  conlin = 0;
                  paguno = 1;
                  conimg = 0;
                }
                // Salto Pagina
                if (conlin == 7) {
                  if (conimg <= tamimg) {
                    //System.out.println("2-cop=" + cop + ",conimg=" + conimg + ",tamimg=" + tamimg);
                    conlin = 6;
                  } else {
                    if (!cop.equals(cox)) {

                      if (can != 3) {
                        documento.newPage();
                      }
                      titPdfDepartamento(documento, dpc);
                    }
                    conlin = 0;
                    conimg = 0;
                  }
                }

              } catch (Exception e) {
                e.printStackTrace();
              }
              conlin++;
              can++;
              cag++;
            }

            //total dep
            SaltoLinea();
            SaltoLinea();

            String vax = "                            Cantidad Productos   " + can;
            IncluirTituloBold(vax, 8);
            documento.add(titPdf);

          }

          st.close();
          st2.close();
          st2.close();
          con.close(); // cerramos la conexion

          // comprimir programa con 
          // caso errores sql
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }

        // Total general global
        if (dep.length() == 0) {
          documento.newPage();
          titPdfDepartamento(documento, "TOTAL GENERAL PRODUTOS");
          SaltoLinea();
          SaltoLinea();
          String vax = "                            Cantidad General   " + cag + "                            Grupo FZ   ( Maria Delgado )";
          IncluirTituloBold(vax, 8);
          documento.add(titPdf);
        }

        // Cerrar documento
        documento.close();

        ejecutaBatRegsizePDF(pdf);

        if (pdfdet.equals("S") && pdfmay.equals("S")) {
          if (tip.equals("D")) {
            labMsgL.setText("- Listo!");
            labRutPdf.setBackground(Color.green);
            labRutPdf.requestFocus();
          }
        }
        if (pdfdet.equals("N") || pdfmay.equals("N")) {
          labMsgL.setText("- Listo!");
          labRutPdf.setBackground(Color.green);
          labRutPdf.requestFocus();
        }

      } catch (DocumentException ex) {
        Logger.getLogger(Pdf_CatalogoPrecios.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      labMsgL.setText("- Cierre PDF " + fil);

      fec = fec.replace("/", "").replace("-", "");
      ImageIcon icon = new ImageIcon(getClass().getResource("/img/ojo.png"));
      String tit = "* AVISO *";
      long tim = 2000;
      Toolkit.getDefaultToolkit().beep();
      String vax = "Cierre el Pdf  " + fil;
      Mensaje msg = new Mensaje(vax, tit, tim, icon);
    }
  }

  public void ejecutaBatRegsizePDF(String pdf) {

    // Ruta al archivo .bat
    String rutaBat = "C:/Java/0-Produccion/GrupoFZ/bat/resizePDF.bat";
    //String rutaBat = "C:/Dist_GrupoFZ/bat/resizePDF.bat";

    File txt = new File(rutaBat);
    if (txt.exists()) {
      try {

        labMsgL.setText("- Comprimiendo Listado PDF - Espere..");

        // Parámetros a pasar al archivo .bat
        String pr1 = pdf;
        String pr2 = pdf.replace("_.pdf", ".pdf");

        //System.out.println("rutaBat=" + rutaBat + " pr1=" + pr1 + " pr2=" + pr2);

        // Crear el proceso
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", rutaBat, pr1, pr2);

        // Redirigir la salida del proceso a la consola de Java
        processBuilder.inheritIO();

        // Iniciar el proceso
        Process proceso = processBuilder.start();

        // Esperar a que el proceso termine
        int resultado = proceso.waitFor();

        // Imprimir el resultado
        System.out.println("El proceso finalizó con el código de salida: " + resultado);

      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("no existe " + rutaBat);
    }
  }

  public void titPdfDepartamento(Document documento, String dpt) {
    try {
      pag++;
      tabPdf = new PdfPTable(1);
      tabPdf.setWidthPercentage(100);
      tabPdf.setWidths(new float[]{100f});
      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
      BaseColor colBas = WebColors.getRGBColor("#117A65");   // Azul
      tabPdf.getDefaultCell().setBackgroundColor(colBas);
      titPdf = new Paragraph();
      titPdf.setFont(FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.WHITE));
      if (dpt.indexOf("TOTAL GENERAL") >= 0) {
        titPdf.add("    " + dpt);
      } else {
        titPdf.add("      Pag  " + pag + "              " + dpt + "                                                                                                   Grupo FZ     ( Maria Delgado ) ");
      }
      tabPdf.addCell(titPdf);
      documento.add(tabPdf);
      tabPdf.flushContent();

      tabPdf = new PdfPTable(4);
      tabPdf.setWidthPercentage(100);
      tabPdf.setWidths(new float[]{14f, 60f, 15f, 25f});
      tabPdf.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
      tabPdf.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
    } catch (DocumentException ex) {
      Logger.getLogger(Pdf_CatalogoPrecios.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  // linea parrafo Bold
  public void IncluirTituloBold(String txt, int tam) {
    titPdf = new Paragraph();
    titPdf.setFont(FontFactory.getFont("calibri", tam, Font.BOLD, BaseColor.GRAY));
    titPdf.add(txt);
    titPdf.setAlignment(Paragraph.ALIGN_LEFT);
  }

  // Salto Linea
  public void SaltoLinea() {
    titPdf = new Paragraph();
    titPdf.add("\n");
  }

  // Metodo Principal
  public void main(String args[]) {
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
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Registro_EmpaqueProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    /*
     /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Pdf_CatalogoPrecios("D", "06012022", 5, "S", "01-BOMBILLOS");
      }
    });
  }

}

//foto.scaleAbsoluteWidth(20f);
//foto.setRotationDegrees(30);
//BufferedImage bimg = ImageIO.read(new File(img));
//int w = bimg.getWidth();
//int h = bimg.getHeight();
//int pag = documento.getPageNumber();
//int th2 = (int) tabPdf.calculateHeights();
//System.out.println("tt=" + (PageSize.A4.getWidth() - documento.leftMargin() - documento.rightMargin() - tabPdf.getWidthPercentage() / 100));
//tabPdf.setTotalWidth((PageSize.A4.getWidth() - documento.leftMargin()
//System.out.println("cop=" + cop + ",th1=" + th + ", th2=" + th2);
