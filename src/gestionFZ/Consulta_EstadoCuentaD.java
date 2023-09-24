package gestionFZ;

import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.getdiasFec;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymd_dmyc;
import static comun.MetodosComunes.ymdhoy;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import modelo.ConexionSQL;
import modelo.SaldosClientes;
import static gestionFZ.Consulta_EstadoCuentaR.DetSdo;

public class Consulta_EstadoCuentaD extends javax.swing.JFrame {

  private DefaultListModel modelo;
  String Ref = "";

  String Fed = "", Feh = "";
  String ord = "0";
  String fmt0 = " %1$-12s %2$-6s %3$-6s %4$-6s %5$-6s %6$6s %7$5s %8$12s %9$12s %10$12s %11$12s %12$12s %13$12s";
  String fmt1 = " %1$-12s %2$-6s %3$-6s %4$-6s %5$-6s %6$6s %7$5s %8$12s %9$12s %10$12s";
  String fmt2 = " %1$10s %2$12s %3$12s";

  int nno = 0, nfa, ndi = 0, nrc = 0;
  String noc = "", coc = "", fno = "", fre = "", fve = "", fpa = "";

  double tno = 0, tdn, toi = 0, tor = 0, tre = 0;
  double tbd = 0, tbb = 0;
  double tsid = 0;
  double tsib = 0;
  double tsfd = 0;
  double tsfb = 0;
  double tgsd = 0;
  double tgsb = 0;

  double tgb = 0, tgd = 0;

  public Consulta_EstadoCuentaD(String cox, String nox, String fdx, String fhx) {

    coc = cox;
    noc = nox;
    Fed = fdx;
    Feh = fhx;

    initComponents();

    txtcli.setText(" " + noc);
    txtFed.setText(ymd_dmy(fdx));
    txtFeh.setText(ymd_dmy(fhx));

    setLocationRelativeTo(null); // centramos la ventana en la pantalla
    setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    setLayout(null);

    String vax = String.format(fmt1, " Nota / Fact", " F.Not", " F.Ven", " F.Rec", " F.Pag", " R.Cob", " Dias", "  T.Nota $", "  T.Pago $", "  T.Saldo $");
    jTi1.setText(vax);
    vax = String.format(fmt2, " T.Ret Bs", " T.Pag Bs", " T.Saldo Bs");
    jTi2.setText(vax);

    modelo = new DefaultListModel();

    jList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    //jList.setSelectionBackground(new java.awt.Color(0, 153, 180));
    //jList.setSelectionForeground(Color.WHITE);
    jList.setSelectionBackground(Color.CYAN);
    //jList.setSelectionForeground(Color.BLUE);

    //Seccion Mouse (Doble Click)
    jList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2 && modelo.size() > 0) {
          //Seleccionar();
        }
      }
    });

    jList.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index,
              boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Consulta_EstadoCuentaD.jListColor) {
          Consulta_EstadoCuentaD.jListColor nextRegistro = (Consulta_EstadoCuentaD.jListColor) value;
          String nam = nextRegistro.name;
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

    ListaDatos();

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        DetSdo.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        DetSdo.dispose();
      }
    });

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

    modelo.clear();

    nno = 0;
    nrc = 0;
    nfa = 0;
    ndi = 0;
    fno = "";
    fre = "";
    fve = "";
    fpa = "";
    tno = 0;
    tdn = 0;
    toi = 0;
    tor = 0;
    tbd = 0;
    tbb = 0;
    tgb = 0;
    tgd = 0;
    tsid = 0;
    tsib = 0;
    tsfd = 0;
    tsfb = 0;
    tgsb = 0;
    tgsd = 0;

    getSaldoInicial(coc);

    ObservableList<SaldosClientes> obsMovCte;
    SaldosClientes s = new SaldosClientes();
    obsMovCte = s.getMovClienteD(coc, Fed, Feh);
    for (SaldosClientes mov : obsMovCte) {
      coc = mov.getCoc();
      fno = mov.getFno();
      fre = mov.getFre();
      fve = mov.getFve();
      fpa = mov.getFpa();

      nno = mov.getNno();
      nfa = mov.getNfa();
      ndi = mov.getNdi();
      nrc = mov.getNrc();

      tno = mov.getTno();
      tdn = mov.getTdn();
      toi = mov.getToi();
      tor = mov.getTor();
      tbd = mov.getTbd();
      tbb = mov.getTbb();

      tre = toi - tor;

      String mox = MtoEs(tre, 2);
      mox = GetCurrencyDouble(mox);
      tre = GetMtoDouble(mox);

      // Fecha Nota
      String fnx = "";
      if (fno.length() == 8) {
        fnx = ymd_dmyc(fno);
        fnx = fnx.substring(0, 5);
      } else {
        fnx = fno;
      }

      // Fecha recibe
      if (fre == null) {
        fre = "";
      }
      String frx = "";
      if (fre.length() == 8) {
        frx = ymd_dmyc(fre);
        frx = frx.substring(0, 5);
      } else {
        fre = fno;
      }

      // Fecha vence
      if (fve == null) {
        fve = "";
      }
      String fvx = "";
      if (fve.length() == 8) {
        fvx = ymd_dmyc(fve);
        fvx = fvx.substring(0, 5);
      }

      // Fecha pago
      if (fpa == null) {
        fpa = "";
      }
      String fpx = "";
      if (fpa.length() == 8) {
        fpx = ymd_dmyc(fpa);
        fpx = fpx.substring(0, 5);
      }

      tgd = tgd + (tno - tdn);
      tgb = tgb + tre;

      String feh = ymdhoy();
      ndi = 0;
      if (fre.length() == 0) {
        fre = fno;
      }
      if (fpa.length() > 0) {
        ndi = getdiasFec(fpa, fre);
      } else {
        ndi = getdiasFec(feh, fre);
      }

      if (ndi < 0) {
        ndi = 0;
      }

      String txn$ = "0,00";
      String txnB = "0,00";
      String txs$ = "0,00";
      String txsB = "0,00";
      String txp$ = "0,00";
      String txpB = "0,00";

      //System.out.println("--nno=" + nno + ",nfa=" + nfa + ",tno=" + tno + ",tre=" + tre + ",tbb=" + tbb);
      String nfx = "";
      if (nfa > 0) {
        nfx = "" + nfa;
      }

      String ft = "%1$-6s%2$-6s";
      String dc = String.format(ft, nno, nfx);

      // Detalle Documentos
      tsfd = (tno - tdn) - tbd;   // total $
      tsfb = tre - tbb;           // total Ret Bs

      tsfd = tsfd + tsid;
      tsfb = tsfb + tsib;

      // Total Notas$
      txn$ = MtoEs(tno - tdn, 2);
      txnB = MtoEs(tre, 2);

      //pagos
      txp$ = MtoEs((tno - tdn) - tsfd, 2);
      txpB = MtoEs(tre - tsfb, 2);

      // Saldo Final
      txs$ = MtoEs(tsfd, 2);
      txsB = MtoEs(tsfb, 2);

      tgsd = tgsd + tsfd;
      tgsb = tgsb + tsfb;

      String nrx = "";
      if (nrc > 0) {
        nrx = "" + nrc;
      }

      String ndx = "";
      if (tno > 0) {
        ndx = "" + ndi;
      }
      if (tno < 0) {
        ndx = "NC";
      }

      if (fnx.equals("*SF*")) {
        fnx = "";
        dc = "*Sdo a Favor";
      }

      String vax = String.format(fmt0, dc, fnx, fvx, frx, fpx, nrx, ndx, txn$, txp$, txs$, txnB, txpB, txsB);
      modelo.addElement(new Consulta_EstadoCuentaD.jListColor(vax, 0, false));

    }

    jList.setModel(modelo);
    if (!modelo.isEmpty()) {
      //jList.setSelectedIndex(0);
      labCan.setText("Cant =  " + modelo.size());

      String tx1 = MtoEs(tgd, 2);
      String tx2 = MtoEs(tgd - tgsd, 2);
      String tx3 = MtoEs(tgsd, 2);

      String tx4 = MtoEs(tgb, 2);
      String tx5 = MtoEs(tgb - tgsb, 2);
      String tx6 = MtoEs(tgsb, 2);

      String vax = String.format(fmt0, "", "", "", "", "", "", "TOTAL", tx1, tx2, tx3, tx4, tx5, tx6);
      modelo.addElement(new Consulta_EstadoCuentaD.jListColor(vax, 1, false));
    }
    txtcli.requestFocus();
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

    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.SaldosD ("
              + "nno  int,"
              + "nfa  int,"
              + "tno  Decimal(15,2),"
              + "tre  Decimal(15,2),"
              + "nrc  int)"
              + "NOT LOGGED ON COMMIT PRESERVE ROWS";
      st.execute(sql);

      sql = "insert into SESSION.SaldosD "
              + "SELECT nne nno,nfa,sum(tne-tdn) tno,sum(toi-tor) tre,0 nrc "
              + "FROM notaent "
              + "where fne< '" + Fed + "' "
              + "  and coc= '" + cox + "' "
              + "group by nne,nfa";
      st.execute(sql);

      sql = "insert into SESSION.SaldosD "
              + "SELECT ncr nno,0 nfa,sum(tnc*-1) tno,0 tre,0 nrc "
              + "FROM notaCred "
              + "where fnc < '" + Fed + "'"
              + "  and coc= '" + cox + "' "
              + "group by ncr ";
      st.execute(sql);

      // Nusca nro recibo pago
      sql = "UPDATE SESSION.SaldosD "
              + "Set nrc = (select nullif(nrc,0) from recibocobroD y Where SESSION.SaldosD.nno=y.nno)";
      st.execute(sql);

      sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.SaldosR ("
              + "nrc  int,"
              + "tno  Decimal(15,2),"
              + "tre  Decimal(15,2),"
              + "tbd  Decimal(15,2),"
              + "tbb  Decimal(15,2)) "
              + "NOT LOGGED ON COMMIT PRESERVE ROWS";
      st.execute(sql);

      sql = "insert into SESSION.SaldosR "
              + "SELECT nrc,sum(tno) tno,sum(tre) tre,0,0 "
              + "FROM SESSION.SaldosD "
              + "group by nrc";
      st.execute(sql);

      sql = "UPDATE SESSION.SaldosR "
              + "Set tbd = (select nullif(sum(tpa/tas),0) from recibocobroP y Where SESSION.SaldosR.nrc=y.nrc and tip<>'2' and tip<>'4'),"
              + "    tbb = (select nullif(sum(tpa),0)     from recibocobroP y Where SESSION.SaldosR.nrc=y.nrc and (tip='2' or tip='4'))";
      st.execute(sql);

      sql = "Select sum(tno) tno,sum(tre) tre,sum(tbd) tbd,sum(tbb) tbb "
              + "from SESSION.SaldosR";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        tno = rs.getDouble("tno");
        tre = rs.getDouble("tre");
        tbd = rs.getDouble("tbd");
        tbb = rs.getDouble("tbb");
        tsid = tno - tbd;   // total $
        tsib = tre - tbb;   // total Ret Bs
      }

      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Consulta_EstadoCuentaR.class.getName()).log(Level.SEVERE, null, ex);
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
    jTi1 = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jList = new javax.swing.JList();
    jLabel1 = new javax.swing.JLabel();
    txtFed = new javax.swing.JTextField();
    jLabel2 = new javax.swing.JLabel();
    txtFeh = new javax.swing.JTextField();
    Cliente1 = new javax.swing.JLabel();
    txtcli = new javax.swing.JTextField();
    btnSal = new javax.swing.JButton();
    labCan = new javax.swing.JLabel();
    jTi2 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/clientes.png"))); // NOI18N
    jLabel3.setText(" ESTADO DE CUENTA ");
    jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jTi1.setBackground(new java.awt.Color(0, 102, 102));
    jTi1.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
    jTi1.setForeground(new java.awt.Color(255, 255, 255));
    jTi1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
    jTi1.setOpaque(true);

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

    txtFed.setBackground(new java.awt.Color(252, 247, 228));
    txtFed.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtFed.setForeground(new java.awt.Color(51, 0, 153));
    txtFed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtFed.setText(" ");
    txtFed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
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

    txtFeh.setBackground(new java.awt.Color(252, 247, 228));
    txtFeh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtFeh.setForeground(new java.awt.Color(51, 0, 153));
    txtFeh.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtFeh.setText(" ");
    txtFeh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
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

    txtcli.setEditable(false);
    txtcli.setBackground(new java.awt.Color(204, 204, 204));
    txtcli.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtcli.setToolTipText("Buscar Clientes");
    txtcli.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtcli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtcli.setOpaque(false);
    txtcli.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtcliMouseClicked(evt);
      }
    });
    txtcli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtcliActionPerformed(evt);
      }
    });
    txtcli.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtcliKeyReleased(evt);
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

    labCan.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    labCan.setForeground(new java.awt.Color(51, 51, 102));
    labCan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labCan.setText(" ");
    labCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jTi2.setBackground(new java.awt.Color(102, 51, 0));
    jTi2.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
    jTi2.setForeground(new java.awt.Color(255, 255, 255));
    jTi2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
    jTi2.setOpaque(true);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(20, 20, 20)
            .addComponent(btnSal)
            .addGap(36, 36, 36)
            .addComponent(labCan, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtcli, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1092, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(layout.createSequentialGroup()
                .addComponent(jTi1, javax.swing.GroupLayout.PREFERRED_SIZE, 754, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTi2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtcli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2)
          .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jTi1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jTi2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(11, 11, 11)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(labCan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnSal))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

  private void txtcliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtcliMouseClicked
  }//GEN-LAST:event_txtcliMouseClicked

  private void txtcliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcliActionPerformed
  }//GEN-LAST:event_txtcliActionPerformed

  private void txtcliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcliKeyReleased
  }//GEN-LAST:event_txtcliKeyReleased

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    DetSdo.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void jListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jListMouseClicked

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
      java.util.logging.Logger.getLogger(Consulta_EstadoCuentaD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Consulta_EstadoCuentaD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Consulta_EstadoCuentaD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Consulta_EstadoCuentaD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
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
        //new Consulta_EstadoCuentaD("297426736", "FEDEVEN", "20210101", "20220204").setVisible(true);
        new Consulta_EstadoCuentaD("315186225", "FEDEVEN", "20220101", "20220214").setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Cliente1;
  private javax.swing.JButton btnSal;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JList jList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel jTi1;
  private javax.swing.JLabel jTi2;
  private javax.swing.JLabel labCan;
  private javax.swing.JTextField txtFed;
  private javax.swing.JTextField txtFeh;
  private javax.swing.JTextField txtcli;
  // End of variables declaration//GEN-END:variables
}
