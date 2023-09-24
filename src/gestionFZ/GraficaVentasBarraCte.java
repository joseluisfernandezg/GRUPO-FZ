//  Grafica Barras 2D -
package gestionFZ;

import com.toedter.calendar.JDateChooser;
import comun.Mensaje;
import static comun.MetodosComunes.FileExist;
import static comun.MetodosComunes.MtoEs;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import modelo.ConexionSQL;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;
import org.jfree.data.time.DateRange;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import static gestionFZ.Consulta_EstadoCuentaR.GrfVen;

class BarraPanel extends JPanel {

  static Color COLOR_SERIE_1 = new Color(255, 128, 64);  //Naranja
  static Color COLOR_SERIE_2 = new Color(28, 84, 140);   //Azul
  static Color COLOR_SERIE_3 = new Color(0, 102, 51); //Verde
  static String nco = "0";
  List<JFreeChart> charts;

  public BarraPanel(java.awt.LayoutManager layoutmanager) {
    super(layoutmanager);
    charts = new ArrayList<JFreeChart>();
  }

  public void addChart(JFreeChart jfreechart) {
    charts.add(jfreechart);
  }
}

public class GraficaVentasBarraCte extends ApplicationFrame {

  public static int cant = 0;
  public static String TiT = "", Fed = "", Feh = "";
  public static double valm = 0, tov = 0;
  public static JButton JSale;

  public GraficaVentasBarraCte(String fed, String feh) {
    super(TiT);
    TiT = "Ventas Clientes $ ";
    TiT = TiT.trim();
    Fed = fed;
    Feh = feh;

    setUndecorated(true); // oculta barra titulo cierre x

    //setDefaultCloseOperation(3);
    setContentPane(createBarraPanel());

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        GrfVen.dispose();
      }
    });

    // Pulsa x cerrar ventana
    this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        GrfVen.dispose();
      }
    });

    JSale.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        GrfVen.dispose();
      }
    });

  }

  //private static final long serialVersionUID = 1L;
  static class GraficaVentas extends BarraPanel implements ChangeListener {

    //private static final long serialVersionUID = 1L;
    JScrollBar scroller;
    SlidingCategoryDataset dataset;

    private final ImageIcon icon;

    private CategoryDataset createDataset() {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();

      Calendar fecha = Calendar.getInstance(); // obtiene la fecha actual
      fecha.add(Calendar.DATE, -1); //
      JDateChooser FecD = new JDateChooser();
      FecD.setDate(fecha.getTime());
      FecD.setDateFormatString(" dd-MM-yyyy");
      SimpleDateFormat fd = new SimpleDateFormat("yyyyMMdd");
      String Fec = fd.format(FecD.getCalendar().getTime());
      SimpleDateFormat fs = new SimpleDateFormat("EEEE");
      try {
        ConexionSQL mysql = new ConexionSQL();
        Connection cnn = mysql.Conectar();
        if (cnn != null) {
          Statement st = cnn.createStatement();
          ResultSet rs = null;
          valm = 0;
          nco = "0";

          String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.TempCli ("
                  + "coc  varchar(10),"
                  + "cli  varchar(50),"
                  + "tno  double) "
                  + "NOT LOGGED ON COMMIT PRESERVE ROWS";
          st.execute(sql);

          sql = "insert into SESSION.TempCli "
                  + "SELECT coc,'',"
                  + "sum(tne-tdn) tno "
                  + "FROM notaent "
                  + "where fne between '" + Fed + "' and '" + Feh + "' "
                  + "group by coc";
          st.execute(sql);

          sql = "insert into SESSION.TempCli "
                  + "SELECT coc,'',"
                  + "sum(tnc*-1) tno "
                  + "FROM notaCred "
                  + "where fnc between '" + Fed + "' and '" + Feh + "' "
                  + "group by coc";
          st.execute(sql);

          // Nusca nro recibo pago
          sql = "UPDATE SESSION.TempCli "
                  + "Set cli=(select nom from clientes where clientes.coc=SESSION.TempCli.coc)";

          st.execute(sql);

          sql = "Select cli,coc,tno from SESSION.TempCli "
                  + "order by tno desc ";

          String cli = "", coc = "";
          double ven = 0;
          cant = 0;
          tov=0;
          int ind = 0;
          rs = st.executeQuery(sql);
          while (rs.next()) {
            if (rs.getString("cli") != null) {
              cli = rs.getString("cli".trim());
              coc = rs.getString("coc".trim());
              ven = rs.getDouble("tno");
              tov = tov + ven;
              //ven = getdiasFec(Fec, fef);
              if (ind == 0) {
                valm = ven;
                ind = 1;
              }
              if (ven > 0) {
                dataset.addValue(ven, cli, coc);
                cant = cant + 1;
              }
            }
          }
        }
        cnn.close();

        if (cant == 0) {
          String tit = "** AVISO **";
          int tim = 2000;
          ImageIcon icon = new ImageIcon(getClass().getResource("/img/search.png"));
          String vax = "NO HAY DATOS\nPARA ESTA SELECCION\n";
          Mensaje msg = new Mensaje(vax, tit, tim, icon);
        }

      } catch (SQLException ex) {
        Logger.getLogger(GraficaVentasBarraCte.class.getName()).log(Level.SEVERE, null, ex);
      }
      return dataset;
    }

    private JFreeChart createChart2D(CategoryDataset dataset) {

      Font font0 = new Font("Dialog", Font.BOLD, 20);  // titulo eje x
      Font font1 = new Font("Dialog", Font.BOLD, 20);  // titulo eje y
      Font font2 = new Font("Plain", Font.BOLD, 14);   // 
      Font font3 = new Font("Arial", Font.BOLD, 9);    // 

      //JFreeChart chart = ChartFactory.createBarChart("GraficaVentasBarraCte",
      Fed = Fed.trim();
      Feh = Feh.trim();
      String Fe1 = Fed.substring(6, Fed.length()) + '-' + Fed.substring(4, 6) + '-' + Fed.substring(0, 4);
      String Fe2 = Feh.substring(6, Feh.length()) + '-' + Feh.substring(4, 6) + '-' + Feh.substring(0, 4);
      String vax3 = " ";
      String vax2 = " del  " + Fe1 + "  al  " + Fe2 + " ";
      String vax = "Clientes  (" + cant + ")     Total Venta $ = "+MtoEs(tov,2);

      // create the chart...
      //JFreeChart chart = ChartFactory.createBarChart(  // 2D
      JFreeChart chart = ChartFactory.createBarChart3D( // 3D
              TiT + vax2,
              vax,
              vax3 + TiT,
              dataset,
              PlotOrientation.VERTICAL,
              false,
              true,
              true);

      //chart.setBackgroundPaint(new java.awt.Color(204, 204, 200));  	 // Color de Fondo de la Grafica
      chart.setBackgroundPaint(Color.WHITE);  	 // Color de Fondo de la Grafica
      chart.getTitle().setPaint(new java.awt.Color(0, 0, 102));        //Azul);
      chart.setAntiAlias(true);   // anti-aliased
      if (!FileExist("imgcia/logoimp2.png")) {
        try {
          BufferedImage img = ImageIO.read(new File("imgcia/logoimp2.png"));
          chart.setBackgroundImage(img);
        } catch (IOException ex) {
          Logger.getLogger(GraficaVentasBarraCte.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      CategoryPlot plot = chart.getCategoryPlot();  // get a reference to the plot for further customisation...
      //plot.setBackgroundPaint(Color.LIGHT_GRAY);  // color del fondo del histograma 
      plot.setBackgroundPaint(Color.WHITE);         // color del fondo del histograma 
      plot.setOrientation(PlotOrientation.VERTICAL);
      plot.getDomainAxis().setLabelFont(font0);     // titulo Eje X
      plot.setRangeGridlinePaint(Color.BLACK);      // color de las líneas horizontales a partir del eje Y
      plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

      //plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
      //plot.setOutlineVisible(false);                // se oculta el recuadro del histograma
      //plot.setAxisOffset(new RectangleInsets(0D, 0D, 0D, 0D));// se elimina el margen entre los ejes y el chart
      if (FileExist("imgcia/logofz.jpg")) {
        BufferedImage img = null;
        try {
          img = ImageIO.read(new File("imgcia/logofz.jpg"));
        } catch (IOException ex) {
          Logger.getLogger(GraficaVentasBarraCte.class.getName()).log(Level.SEVERE, null, ex);
        }
        plot.setBackgroundImage(img);
      }

      ValueAxis axis = plot.getRangeAxis();
      axis.setLabelFont(font1);                   // titulo Eje Y
      axis.setLabelPaint(Color.BLACK);

      //tamaño font categorias Eje x
      CategoryAxis axisDomain = plot.getDomainAxis();

      //axisDomain.setLowerMargin(0.05);
      axisDomain.setTickLabelFont(font2);
      axisDomain.setLabelPaint(new java.awt.Color(102, 0, 0));

      //tamaño font Series Eje Y
      ValueAxis axisRange = plot.getRangeAxis();
      axisRange.setTickLabelFont(font2);
      axisRange.setLabelPaint(new java.awt.Color(102, 0, 0));
      //axisRange.setTickMarkInsideLength(1000000);
      //axisRange.setUpperMargin(1);

      double num = 10 - (valm % 10);  // ultimo numero
      valm = Math.floor(valm) + num;

      axisRange.setRange(0, valm);  // fjar valores Eje Y  (0 - M)

      //Solo valores enteros
      NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();
      valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); //Solo valores enteros

      //valueAxis.setTickUnit(new NumberTickUnit(1));  // de uno en 1
      //valueAxis.setNumberFormatOverride(new DecimalFormat("0.0%"));  // valores %
      //valueAxis.setNumberFormatOverride(new DecimalFormat("0.0"));
      // barra renderer especial para mostrar cada "category" en una sola barra con capas superpuestas
      BarRenderer renderer = (BarRenderer) plot.getRenderer();
      renderer.setBarPainter(new StandardBarPainter());
      renderer.setShadowVisible(true);
      //renderer.setItemMargin(-0.6D); // se ajusta el ancho de las barras
      //renderer.setBaseItemLabelsVisible(true);

      renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
      renderer.setBaseItemLabelFont(font3);

      //renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("#0.00#")));
      renderer.setBaseItemLabelFont(new Font("Arial", Font.BOLD, 9));

      renderer.setItemLabelsVisible(true);  // coloca valor en el tope de la barra
      //renderer.setDrawBarOutline(false);

      plot.setRenderer(renderer);
      //axisRange.setAutoRange(false);
      axisRange.setRange(new DateRange(0, valm));
      axis.setLowerMargin(0.05);  // reduce the default margins
      axis.setUpperMargin(0.05);
      //axis.setAutoRangeMinimumSize(0.5);
      //axis.setFixedAutoRange(10);
      //axis.centerRange(0);  // Separa barra + de - barra
      /*
       final BufferedImage bi = new BufferedImage(SIZE, SIZE,BufferedImage.TYPE_INT_RGB);
       final Graphics2D bg = bi.createGraphics();
       bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
       */

      renderer.setSeriesPaint(0, COLOR_SERIE_1);    // Nja
      if (nco.equals("0")) {
        renderer.setSeriesPaint(0, COLOR_SERIE_1);  // Nja
      }
      if (nco.equals("1")) {
        renderer.setSeriesPaint(0, COLOR_SERIE_2);  // Azul
      }
      if (nco.equals("9")) {
        renderer.setSeriesPaint(0, COLOR_SERIE_3);  // Verde
      }

      // Color de las barras
      if (nco.length() == 0) {
        renderer.setSeriesPaint(0, COLOR_SERIE_1); // Nja
        renderer.setSeriesPaint(1, COLOR_SERIE_2); // Azul
        renderer.setSeriesPaint(2, COLOR_SERIE_3); // Verde
      }
      //renderer.setSeriesPaint(0, Color.GREEN);

      return chart;
    }

    public void stateChanged(ChangeEvent changeevent) {
      if (dataset.getColumnCount() > 1) {
        dataset.setFirstCategoryIndex(scroller.getValue());
      } else {
        dataset.setFirstCategoryIndex(0);
      }
    }

    public GraficaVentas() {
      super(new BorderLayout());

      JSale = new JButton();
      icon = new ImageIcon(getClass().getResource("/img/salir2.png"));
      JSale.setIcon(icon);
      JSale.setText("Salir");
      //JSale.setBounds(100, 100, 100, 30);
      JSale.setForeground(Color.BLACK);
      JSale.setMnemonic('S');
      JSale.setToolTipText("Salir ( Alt - S )");
      JSale.setFont(new java.awt.Font("arial", java.awt.Font.BOLD, 12));
      JSale.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

      dataset = new SlidingCategoryDataset(createDataset(), 0, 15); //Cantidad barras x pantalla

      JFreeChart jfreechart = createChart2D(dataset);
      CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
      //SlidingCategoryDataset categorydataset = new SlidingCategoryDataset(createDataset(), 0, 20);

      CategoryAxis categoryaxis = categoryplot.getDomainAxis();
      categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);

      //categoryplot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
      addChart(jfreechart);
      ChartPanel chartpanel = new ChartPanel(jfreechart);
      chartpanel.setPreferredSize(new Dimension(900, 500));
      chartpanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY));
      if (cant > 0) {
        int cn = 15;
        if (cant <= 15) {
          cn = cant;
        }
        scroller = new JScrollBar(JScrollBar.HORIZONTAL, 0, cn, 0, cant);
        add(chartpanel);
        scroller.getModel().addChangeListener(this);
        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(scroller);
        //jpanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        jpanel.setBackground(Color.LIGHT_GRAY);
        add(jpanel, "South");
        add(JSale, "North");
        //Línea 1
        Border bordejpanel = new TitledBorder(new EtchedBorder(), "<─ Scroll ─>  Esc=Salir");
        //Se agrega un borde vacio alrededor de los componentes.  
        /*
         jpanel.setBorder(BorderFactory.createEmptyBorder(
         30, //arriba  
         30, //izquierda  
         10, //abajo  
         30) //derecha  
         );
         */
        //Línea 2
        jpanel.setBorder(bordejpanel);
      }
    }
  }

  public static JPanel createBarraPanel() {
    return new GraficaVentas();
  }

  public static void main(String args[]) {
    GraficaVentasBarraCte VentasBarra = new GraficaVentasBarraCte("20220101", "20220304");
    VentasBarra.pack();
    RefineryUtilities.centerFrameOnScreen(VentasBarra);
    VentasBarra.setVisible(true);
  }
}
