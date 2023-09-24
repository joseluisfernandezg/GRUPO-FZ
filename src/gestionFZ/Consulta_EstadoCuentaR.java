package gestionFZ;

import static comun.MetodosComunes.FileExist;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.dmy_ymd;
import static comun.MetodosComunes.getAnoHoy;
import static comun.MetodosComunes.getdiasFec;
import static comun.MetodosComunes.isNumeric;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymdhoy;
import static gestionFZ.Menu.ResSdo;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import modelo.ConexionSQL;
import modelo.SaldosClientes;
import org.jfree.ui.RefineryUtilities;

public class Consulta_EstadoCuentaR extends javax.swing.JFrame {

  private DefaultListModel modelo;

  String Ref = "";
  String Fed = "", Feh = "";
  String fil = "";
  String Ord = "0";
  String fno = "";
  String fre = "";
  String sta = "P";
  String noc = "", coc = "";

  String format = " %1$-40s %2$-10s %3$9s %4$7s %5$14s %6$14s %7$14s %8$14s\n";
  int can = 0;
  int cang = 0;
  int ndi = 0;

  double tno = 0;
  double tre = 0;

  double tsid = 0;
  double tsib = 0;

  double tbd = 0;
  double tbb = 0;

  double tsfd = 0;
  double tsfb = 0;

  double tgd = 0;
  double tgb = 0;

  double tgsd = 0;
  double tgsb = 0;

  public static Consulta_EstadoCuentaD DetSdo; // Resumen Saldo Cartera Clientes
  public static GraficaVentasBarraCte GrfVen;     // Grafica Ventas

  public Consulta_EstadoCuentaR() {
    initComponents();

    setLocationRelativeTo(null); // centramos la ventana en la pantalla
    setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    setLayout(null);

    btnSel.setVisible(false);
    txtBus.setText(coc);

    GregorianCalendar calendario = new GregorianCalendar();
    final SimpleDateFormat ff = new SimpleDateFormat("dd-MM-yyyy");

    Fed = ff.format(calendario.getTime());
    Fed = "01" + Fed.substring(2, Fed.length());

    txtFed.setText(Fed);
    Fed = (txtFed.getText().replace("-", ""));
    Fed = dmy_ymd(Fed).replace("-", "");

    txtFeh.setText(ff.format(calendario.getTime()));
    Feh = (txtFeh.getText().replace("-", ""));
    Feh = dmy_ymd(Feh).replace("-", "");

    String vax = String.format(format, " Cliente", " Codigo", "   C.Notas", "C.Dias", " T.Notas $", " T.Saldo $", " T.Ret Bs", " T.Saldo Bs");
    jTiT.setText(vax);

    modelo = new DefaultListModel();

    btnRep.setEnabled(false);

    jFed.setFocusable(true);
    jFed.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().trim().equals("date")) {
          txtFed.setText(ff.format(jFed.getCalendar().getTime()));
          Fed = (txtFed.getText().replaceAll("-", ""));
          Fed = dmy_ymd(Fed).replace("-", "");
          modelo.clear();
        }
      }
    });

    jFeh.setFocusable(true);
    jFeh.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().trim().equals("date")) {
          txtFeh.setText(ff.format(jFeh.getCalendar().getTime()));
          Feh = (txtFeh.getText().replaceAll("-", ""));
          Feh = dmy_ymd(Feh).replace("-", "");
          modelo.clear();
          btnBus.doClick();
        }
      }
    });

    jList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    jList.setSelectionBackground(Color.CYAN);
    jList.setSelectionForeground(Color.BLUE);

    txtBus.setText(" ");
    txtBus.requestFocus();

    //Seccion Mouse (Doble Click)
    jList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2 && modelo.size() > 0) {
          btnRep.doClick();
          //Seleccionar();
        }
      }
    });

    jList.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index,
              boolean isSelected, boolean cellHasFocus) {
        String nam = "";
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Consulta_EstadoCuentaR.jListColor) {
          Consulta_EstadoCuentaR.jListColor nextRegistro = (Consulta_EstadoCuentaR.jListColor) value;
          nam = nextRegistro.name;
          setText(nam);

          if (nextRegistro.loggedIn) {
            setForeground(new java.awt.Color(153, 0, 51)); // Rojo (Negativo)
          } else {
            if (nam.indexOf("Gastos") > 0) {
              //setForeground(new java.awt.Color(0, 0, 102)); // Azul Oscuro
              setForeground(new java.awt.Color(153, 0, 51)); // Rojo (Negativo)
              //setBackground(Color.LIGHT_GRAY);
            } else {
              if (nam.indexOf("------") == -1 && nam.indexOf("───") == -1) {
                //setForeground(new java.awt.Color(0, 0, 102)); // Azul Oscuro
                setForeground(Color.BLACK);
              } else {
                setForeground(Color.BLACK);
              }
            }
          }

          if (nam.indexOf("TOTAL") >= 0) {
            setBackground(Color.GRAY);
            //setForeground(new java.awt.Color(0, 0, 102)); // Azul Oscuro
            setForeground(Color.WHITE); // Azul Oscuro
          }
          // setForeground(new java.awt.Color(153, 0, 102)); // Rojo (Negativo)

          if (isSelected) {
            Ref = nam;
            //setBackground(getBackground().darker());
            //setBackground(Color.ORANGE);
          }
        } else {
          setText(null);
        }
        return c;
      }
    });

    cbTip.setSelectedIndex(2);
    sta = "T";
    Fed = (getAnoHoy() - 1) + "0101";
    txtFed.setText(ymd_dmy(Fed));

    //ListaDatos();
    txtBus.requestFocus();

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        ResSdo.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        ResSdo.dispose();
      }
    });

  }

  // Selecciona Registro
  public void Seleccionar() {
    if (Ref.length() >= 51) {
      String nom = Ref.substring(0, 41).trim();
      String cod = Ref.substring(41, 51).trim();
      if (DetSdo != null) {
        DetSdo.dispose();
      }
      DetSdo = new Consulta_EstadoCuentaD(cod, nom, Fed, Feh);   // Saldos Clientes
      DetSdo.setVisible(true);
      DetSdo.setExtendedState(NORMAL);
      DetSdo.setVisible(true);
    }
  }

  //Color Jlist
  static class jListColor {

    String name = "NN";
    boolean loggedIn = false;

    public jListColor(String name, double mto, boolean loggedIn) {
      this.name = name;
      this.loggedIn = loggedIn;
      if (mto < 0) {
        this.loggedIn = true;
      }
    }
  }

  // Lista Datos
  public void ListaDatos() {

    fil = txtBus.getText().toUpperCase().trim();
    modelo.clear();
    noc = "";
    coc = "";
    tno = 0;
    tre = 0;
    tsid = 0;
    tsib = 0;
    tbd = 0;
    tbb = 0;
    tsfd = 0;
    tsfb = 0;

    tgd = 0;
    tgb = 0;

    tgsd = 0;
    tgsb = 0;

    can = 0;
    cang = 0;

    /*
    ObservableList<SaldosClientes> obsSaldoCte;
    SaldosClientes s = new SaldosClientes();
    obsSaldoCte = s.getMovClienteR(Ord, sta, fil, coc, Fed, Feh);
    for (SaldosClientes mov : obsSaldoCte) {
      can = mov.getCan();
    }
     */
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      String vax1 = "", vax2 = "";

      if (sta.equals("P")) {
        vax1 = " and nne not in (select nno from recibocobroD where tpd=0) ";
        vax2 = " and ncr not in (select nno from recibocobroD where tpd=1) ";
      } else {
        if (sta.equals("C")) {
          vax1 = " and nne in (select nno from recibocobroD where tpd=0) ";
          vax2 = " and ncr in (select nno from recibocobroD where tpd=1) ";
        }
      }

      String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.SaldosD ("
              + "noc  varchar(50),"
              + "coc  varchar(10),"
              + "nno  int,"
              + "can  int,"
              + "tno  Decimal(15,2),"
              + "tre  Decimal(15,2),"
              + "nrc  int)"
              + "NOT LOGGED ON COMMIT PRESERVE ROWS";
      st.execute(sql);

      sql = "insert into SESSION.SaldosD "
              + "SELECT '' noc,"
              + "coc,nne nno,1 cant,sum(tne-tdn) tno,sum(toi-tor) tre,0 nrc "
              + "FROM notaent "
              + "where fne between '" + Fed + "' and '" + Feh + "' "
              + vax1
              + "  and (noc like '%" + fil + "%' or coc like '%" + fil + "%')"
              + "group by coc,nne";
      st.execute(sql);

      sql = "insert into SESSION.SaldosD "
              + "SELECT '' noc,"
              + "coc,ncr nno,1 cant,sum(tnc*-1) tno,0 tre,0 nrc "
              + "FROM notaCred "
              + "where fnc between '" + Fed + "' and '" + Feh + "' "
              + vax2
              + "  and (noc like '%" + fil + "%' or coc like '%" + fil + "%')"
              + "group by coc,ncr ";
      st.execute(sql);

      // Nusca nro recibo pago
      sql = "UPDATE SESSION.SaldosD "
              + "Set noc=(select nom from clientes where clientes.coc=SESSION.SaldosD.coc),"
              + "nrc = (select nullif(nrc,0) from recibocobroD y Where SESSION.SaldosD.nno=y.nno)";
      st.execute(sql);

      sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.SaldosR ("
              + "noc  varchar(50),"
              + "coc  varchar(10),"
              + "nrc  int,"
              + "can  int,"
              + "tno  Decimal(15,2),"
              + "tre  Decimal(15,2),"
              + "tbd  Decimal(15,2),"
              + "tbb  Decimal(15,2)) "
              + "NOT LOGGED ON COMMIT PRESERVE ROWS";
      st.execute(sql);

      sql = "insert into SESSION.SaldosR "
              + "SELECT noc,coc,nrc,sum(can) can,sum(tno) tno,sum(tre) tre,0,0 "
              + "FROM SESSION.SaldosD "
              + "group by noc,coc,nrc";
      st.execute(sql);

      sql = "UPDATE SESSION.SaldosR "
              + "Set tbd = (select nullif(sum(tpa/tas),0) from recibocobroP y Where SESSION.SaldosR.nrc=y.nrc and tip<>'2' and tip<>'4'),"
              + "    tbb = (select nullif(sum(tpa),0)     from recibocobroP y Where SESSION.SaldosR.nrc=y.nrc and (tip='2' or tip='4'))";
      st.execute(sql);

      String vax = "Order by tno desc";
      if (Ord.equals("1")) {
        vax = "Order by noc";
      }

      sql = "Select noc,coc,sum(can) can,sum(tno) tno,sum(tre) tre,sum(tbd) tbd,sum(tbb) tbb "
              + "from SESSION.SaldosR "
              + "Group by noc,coc "
              + vax;

      rs = st.executeQuery(sql);
      while (rs.next()) {
        can = rs.getInt("can");
        noc = rs.getString("noc");
        coc = rs.getString("coc");
        tno = rs.getDouble("tno");
        tre = rs.getDouble("tre");
        tbd = rs.getDouble("tbd");
        tbb = rs.getDouble("tbb");

        if (noc.length() > 35) {
          noc = noc.substring(0, 35);
        }
        noc = noc.toUpperCase();

        tsid = 0;
        tsib = 0;

        tsfd = tno - tbd;   // total $
        tsfb = tre - tbb;   // total Ret Bs

        getDiasPend(coc);

        getSaldoInicial(coc);

        String tx1 = MtoEs(tno, 0);
        tsfd = tsfd + tsid;
        String txd = MtoEs(tsfd, 0);
        tgsd = tgsd + tsfd;

        String txr = MtoEs(tre, 0);
        tgsb = tgsb + tsfb;
        String txb = MtoEs(tsfb, 0);

        tgd = tgd + tno;
        tgb = tgb + tre;
        tgsb = tgsb + tsfb;
        cang = cang + can;

        vax = String.format(format, noc, coc, can, ndi, tx1, txd, txr, txb);
        modelo.addElement(new Consulta_EstadoCuentaR.jListColor(vax, 0, false));
      }

      jList.setModel(modelo);
      btnRep.setEnabled(false);
      if (!modelo.isEmpty()) {
        btnRep.setEnabled(true);
        lanCan.setText("Cant =  " + modelo.size());
        String tx1 = MtoEs(tgd, 0);
        String txd = MtoEs(tgsd, 0);
        String txr = MtoEs(tgb, 0);
        String txb = MtoEs(tgsb, 0);
        vax = String.format(format, "", "TOTAL", cang, "", tx1, txd, txr, txb);
        modelo.addElement(new Consulta_EstadoCuentaR.jListColor(vax, 1, false));
        int i = modelo.getSize() - 1;
        jList.setSelectedIndex(i);
        jList.ensureIndexIsVisible(i);
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Consulta_EstadoCuentaR.class.getName()).log(Level.SEVERE, null, ex);
    }
    txtBus.requestFocus();
  }

  // Saldos Inicales
  public void getSaldoInicial(String cox) {
    double tno = 0;
    double tre = 0;
    tno = 0;
    tre = 0;
    tbd = 0;
    tbb = 0;
    tsid = 0;
    tsib = 0;
    ObservableList<SaldosClientes> obsSaldoCte;
    SaldosClientes s = new SaldosClientes();
    obsSaldoCte = s.getSaldoIncialClienteR(cox, Fed);
    for (SaldosClientes sdo : obsSaldoCte) {
      tsid = sdo.getTsid();
      tsib = sdo.getTsib();
    }
  }

  public void getDiasPend(String coc) {
    ndi = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      // Calculo dias Pendientes
      String sql = "SELECT fne,fee "
              + "FROM notaent "
              + "where coc = '" + coc + "' "
              + "  and nne not in (select nno from recibocobroD where tpd=0) "
              + "order by fne "
              + "FETCH FIRST 1 ROWS ONLY";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        fno = rs.getString("fne");
        fre = rs.getString("fee");
        String feh = ymdhoy();
        if (fre == null) {
          fre = "";
        }
        if (fre.length() == 8) {
          ndi = getdiasFec(feh, fre);
        } else {
          ndi = getdiasFec(feh, fno);
        }
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_PagosClientes.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
   * content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel3 = new javax.swing.JLabel();
    jTiT = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jList = new javax.swing.JList();
    jLabel1 = new javax.swing.JLabel();
    jFed = new com.toedter.calendar.JDateChooser();
    txtFed = new javax.swing.JTextField();
    jLabel2 = new javax.swing.JLabel();
    jFeh = new com.toedter.calendar.JDateChooser();
    txtFeh = new javax.swing.JTextField();
    Cliente1 = new javax.swing.JLabel();
    txtBus = new javax.swing.JTextField();
    btnBus = new javax.swing.JButton();
    btnSal = new javax.swing.JButton();
    lanCan = new javax.swing.JLabel();
    btnSel = new javax.swing.JButton();
    jLabel7 = new javax.swing.JLabel();
    cbTip = new javax.swing.JComboBox();
    btnRep = new javax.swing.JButton();
    labRutEdo = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    cbOrd = new javax.swing.JCheckBox();
    jButton1 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/clientes.png"))); // NOI18N
    jLabel3.setText(" RESUMEN  - ESTADOS DE CUENTAS CLIENTES ");
    jLabel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));

    jTiT.setBackground(new java.awt.Color(0, 102, 102));
    jTiT.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
    jTiT.setForeground(new java.awt.Color(255, 255, 255));
    jTiT.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
    jTiT.setOpaque(true);

    jList.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
    jList.setOpaque(false);
    jList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jListMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(jList);

    jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel1.setForeground(new java.awt.Color(0, 0, 102));
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Del");

    jFed.setForeground(new java.awt.Color(0, 0, 102));
    jFed.setToolTipText("Seleccione Dia a Procesar");
    jFed.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
    jFed.setPreferredSize(new java.awt.Dimension(42, 10));

    txtFed.setBackground(new java.awt.Color(252, 247, 228));
    txtFed.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtFed.setForeground(new java.awt.Color(51, 0, 153));
    txtFed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtFed.setText(" ");
    txtFed.setFocusable(false);
    txtFed.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtFedMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtFedMouseReleased(evt);
      }
    });
    txtFed.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtFedActionPerformed(evt);
      }
    });
    txtFed.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtFedKeyReleased(evt);
      }
    });

    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(0, 51, 102));
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setText("al");

    jFeh.setForeground(new java.awt.Color(0, 0, 102));
    jFeh.setToolTipText("Seleccione Dia a Procesar");
    jFeh.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
    jFeh.setPreferredSize(new java.awt.Dimension(85, 10));
    jFeh.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jFehMouseClicked(evt);
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jFehMouseExited(evt);
      }
    });

    txtFeh.setBackground(new java.awt.Color(252, 247, 228));
    txtFeh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtFeh.setForeground(new java.awt.Color(51, 0, 153));
    txtFeh.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtFeh.setText(" ");
    txtFeh.setFocusable(false);
    txtFeh.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtFehMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtFehMouseReleased(evt);
      }
    });
    txtFeh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtFehActionPerformed(evt);
      }
    });
    txtFeh.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtFehKeyReleased(evt);
      }
    });

    Cliente1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    Cliente1.setForeground(new java.awt.Color(51, 51, 102));
    Cliente1.setText(" Cliente");

    txtBus.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtBus.setToolTipText("Buscar Clientes");
    txtBus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtBus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtBus.setOpaque(false);
    txtBus.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtBusMouseClicked(evt);
      }
    });
    txtBus.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtBusActionPerformed(evt);
      }
    });
    txtBus.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtBusKeyReleased(evt);
      }
    });

    btnBus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnBus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    btnBus.setText("Buscar");
    btnBus.setPreferredSize(new java.awt.Dimension(77, 30));
    btnBus.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnBusActionPerformed(evt);
      }
    });

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
      }
    });

    lanCan.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    lanCan.setForeground(new java.awt.Color(51, 51, 102));
    lanCan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lanCan.setText(" ");
    lanCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    btnSel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ok2.png"))); // NOI18N
    btnSel.setText("Seleccionar");
    btnSel.setPreferredSize(new java.awt.Dimension(77, 28));
    btnSel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSelActionPerformed(evt);
      }
    });

    jLabel7.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel7.setForeground(new java.awt.Color(51, 51, 102));
    jLabel7.setText("Estatus");

    cbTip.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    cbTip.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pendientes", "Cobradas", "Todas" }));
    cbTip.setMinimumSize(new java.awt.Dimension(93, 25));
    cbTip.setPreferredSize(new java.awt.Dimension(35, 28));
    cbTip.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbTipItemStateChanged(evt);
      }
    });
    cbTip.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbTipActionPerformed(evt);
      }
    });

    btnRep.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnRep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pdf0.png"))); // NOI18N
    btnRep.setText("Estado Cuenta");
    btnRep.setToolTipText("Estado de Cuenta Cliente");
    btnRep.setPreferredSize(new java.awt.Dimension(77, 28));
    btnRep.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnRepActionPerformed(evt);
      }
    });

    labRutEdo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labRutEdo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rutpdf.png"))); // NOI18N
    labRutEdo.setToolTipText("Ruta Estados de Cuenta  generados en PDF");
    labRutEdo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
    labRutEdo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labRutEdo.setPreferredSize(new java.awt.Dimension(43, 30));
    labRutEdo.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labRutEdoMouseClicked(evt);
      }
    });

    jLabel12.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel12.setForeground(new java.awt.Color(51, 51, 102));
    jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel12.setText("Ordena x Nombre");
    jLabel12.setToolTipText("Cliente con factura");

    cbOrd.setBackground(new java.awt.Color(240, 248, 239));
    cbOrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbOrd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    cbOrd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    cbOrd.setOpaque(false);
    cbOrd.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
    cbOrd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbOrdActionPerformed(evt);
      }
    });

    jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/grafic.png"))); // NOI18N
    jButton1.setText("VENTAS");
    jButton1.setToolTipText("Grafica Ventas $ Clientes");
    jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(20, 20, 20)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jTiT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addComponent(btnSal)
                .addGap(45, 45, 45)
                .addComponent(lanCan, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
              .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
            .addGap(22, 22, 22))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jFed, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(cbTip, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbOrd))
              .addGroup(layout.createSequentialGroup()
                .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(btnRep, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labRutEdo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(btnSel, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(6, 6, 6))))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(12, 12, 12)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cbTip, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addComponent(cbOrd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(btnSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 0, Short.MAX_VALUE)))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnRep, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jFed, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
              .addComponent(jFeh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(labRutEdo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 0, Short.MAX_VALUE)))
        .addGap(18, 18, 18)
        .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lanCan, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnSal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void txtFedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFedMouseClicked
    //jDesde.setSelectionStart(0);
    //jDesde.setSelectionEnd(5);
  }//GEN-LAST:event_txtFedMouseClicked

  private void txtFedMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFedMouseReleased

  }//GEN-LAST:event_txtFedMouseReleased

  private void txtFedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFedActionPerformed

  }//GEN-LAST:event_txtFedActionPerformed

  private void txtFedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFedKeyReleased

  }//GEN-LAST:event_txtFedKeyReleased

  private void jFehMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jFehMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jFehMouseClicked

  private void jFehMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jFehMouseExited
    // TODO add your handling code here:
  }//GEN-LAST:event_jFehMouseExited

  private void txtFehMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFehMouseClicked
    //jHasta.setSelectionStart(0);
    //jHasta.setSelectionEnd(5);
  }//GEN-LAST:event_txtFehMouseClicked

  private void txtFehMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFehMouseReleased

  }//GEN-LAST:event_txtFehMouseReleased

  private void txtFehActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFehActionPerformed

  }//GEN-LAST:event_txtFehActionPerformed

  private void txtFehKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFehKeyReleased

  }//GEN-LAST:event_txtFehKeyReleased

  private void txtBusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBusMouseClicked
    modelo.clear();
    txtBus.setText(" ");
    if (txtBus.getText().length() == 0) {
      fil = "";
      //ListaDatos();
    } else {
      txtBus.setText(" ");
    }
  }//GEN-LAST:event_txtBusMouseClicked

  private void txtBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusActionPerformed
    if (txtBus.getText().length() == 0) {
      fil = "";
    }
  }//GEN-LAST:event_txtBusActionPerformed

  private void txtBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusKeyReleased
    fil = txtBus.getText().toUpperCase().trim();
    //ListaDatos();
  }//GEN-LAST:event_txtBusKeyReleased

  private void btnBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusActionPerformed
    ListaDatos();
  }//GEN-LAST:event_btnBusActionPerformed

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    ResSdo.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void jListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListMouseClicked

  }//GEN-LAST:event_jListMouseClicked

  private void cbTipItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTipItemStateChanged
    int idx = cbTip.getSelectedIndex();
    if (idx >= 0 && evt.getSource() == cbTip && evt.getStateChange() == 1) {
      String str = (String) cbTip.getSelectedItem().toString();
      if (str != null) {
        if (str.length() > 0) {
          str = str.trim();
          sta = "T";
          if (str.equals("Pendientes")) {
            sta = "P";
          } else {
            if (str.equals("Cobradas")) {
              sta = "C";
            }
          }
          //ListaDatos();
        }
      }
    }
  }//GEN-LAST:event_cbTipItemStateChanged

  private void cbTipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbTipActionPerformed

  private void btnRepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRepActionPerformed
    labRutEdo.setBackground(new java.awt.Color(242, 247, 247));
    if (Ref.length() >= 51) {
      String coc = Ref.substring(41, 51).trim();
      if (isNumeric(coc)) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/ok.png"));
        String vax = "Desea Generar El Estado de Cuenta ?";
        String[] options = {"SI", "NO"};
        int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
        if (opcion == 0) {
          new Pdf_EstadoCuenta(coc, Fed, Feh);
        }
      }
    }
  }//GEN-LAST:event_btnRepActionPerformed

  private void labRutEdoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labRutEdoMouseClicked
    labRutEdo.setOpaque(false);
    labRutEdo.setBackground(new java.awt.Color(242, 247, 247));
    String vax = "rep\\pdf\\edocta\\";
    File folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }
    Runtime aplicacion = Runtime.getRuntime();
    try {
      aplicacion.exec("cmd.exe /K start " + vax);
    } catch (IOException ex) {
      System.out.println("Error no existe " + vax);
    }
  }//GEN-LAST:event_labRutEdoMouseClicked

  private void cbOrdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOrdActionPerformed
    Ord = "0";
    if (cbOrd.isSelected() == true) {
      Ord = "1";
    }
  }//GEN-LAST:event_cbOrdActionPerformed

  private void btnSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelActionPerformed
    Seleccionar();
  }//GEN-LAST:event_btnSelActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    if (GrfVen != null) {
      GrfVen.dispose();
    }
    GrfVen = new GraficaVentasBarraCte(Fed, Feh);   // Grafica Ventas
    GrfVen.setExtendedState(NORMAL);
    GrfVen.pack();
    RefineryUtilities.centerFrameOnScreen(GrfVen);
    GrfVen.setVisible(true);


  }//GEN-LAST:event_jButton1ActionPerformed

  /**
   * @param args the command line arguments
   */
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
      java.util.logging.Logger.getLogger(Consulta_EstadoCuentaR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Consulta_EstadoCuentaR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Consulta_EstadoCuentaR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Consulta_EstadoCuentaR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Consulta_EstadoCuentaR().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Cliente1;
  private javax.swing.JButton btnBus;
  private javax.swing.JButton btnRep;
  private javax.swing.JButton btnSal;
  private javax.swing.JButton btnSel;
  private javax.swing.JCheckBox cbOrd;
  private javax.swing.JComboBox cbTip;
  private javax.swing.JButton jButton1;
  private com.toedter.calendar.JDateChooser jFed;
  private com.toedter.calendar.JDateChooser jFeh;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JList jList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel jTiT;
  public static javax.swing.JLabel labRutEdo;
  private javax.swing.JLabel lanCan;
  private javax.swing.JTextField txtBus;
  private javax.swing.JTextField txtFed;
  private javax.swing.JTextField txtFeh;
  // End of variables declaration//GEN-END:variables
}
