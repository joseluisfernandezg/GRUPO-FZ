package gestionFZ;

import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.dmy_ymd;
import static comun.MetodosComunes.getAnoHoy;
import static comun.MetodosComunes.ymd_dmy;
import static gestionFZ.Registro_PedidoCliente.conMov;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import modelo.ConexionSQL;
import org.jfree.ui.RefineryUtilities;

public class Consulta_MovProd extends javax.swing.JFrame {

  private DefaultListModel modelo;

  String Fed = "", Feh = "";
  String filP = "", noc = "";
  String Ord = "0";
  String Ord2 = "0";
  String format = " %1$-10s  %2$-60s  %3$-6s  %4$10s %5$22s\n";
  public static Consulta_MovProdDet DetPrd;    // Movimiento Productos
  public static GraficaVentasBarraPrd GrfPrd;    // Grafica Ventas

  public Consulta_MovProd() {

    initComponents();

    setLocationRelativeTo(null); // centramos la ventana en la pantalla
    setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    GregorianCalendar calendario = new GregorianCalendar();
    final SimpleDateFormat ff = new SimpleDateFormat("dd-MM-yyyy");

    Fed = ff.format(calendario.getTime());
    Fed = "01" + Fed.substring(2, Fed.length());

    //txtFed.setText(Fed);
    //Fed = (txtFed.getText().replace("-", ""));
    //Fed = dmy_ymd(Fed).replace("-", "");
    Fed = (getAnoHoy() - 1) + "0101";
    txtFed.setText(ymd_dmy(Fed));

    txtFeh.setText(ff.format(calendario.getTime()));
    Feh = (txtFeh.getText().replace("-", ""));
    Feh = dmy_ymd(Feh).replace("-", "");

    String vax = String.format(format, " Codigo", " Descripcion Producto", " ", "   Cantidad", "   T. Pedido $");
    jTiT.setText(vax);

    modelo = new DefaultListModel();

    txtPrd.requestFocus();
    btnSel.setVisible(false);

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

    //Seccion Mouse (Doble Click)
    jList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2 && modelo.size() > 0) {
          Seleccionar();
        }
      }
    });

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        conMov.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        conMov.dispose();
      }
    });
    Ord = "1";
    cbCan.setSelected(true);
    cargaClientes();
  }

  public void cargaClientes() {
    cbCte.removeAllItems();
    cbCte.addItem("");
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT nom "
              + "FROM clientes "
              + "where sta = '0' "
              + "order by nom";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        cbCte.addItem(rs.getString("nom"));
      }
      cbCte.setSelectedIndex(-1);
      st.close();
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_PedidoCliente.class
              .getName()).log(Level.SEVERE, null, ex);
    }
  }

  // Selecciona Registro
  public void Seleccionar() {
    if (!modelo.isEmpty()) {
      int[] x = jList.getSelectedIndices();
      int idx = x.length;
      if (idx > 0) {
        idx = idx - 1;
        String Ref = String.valueOf(modelo.getElementAt(x[idx]));
        if (Ref.length() >= 80) {
          Ref = Ref.trim();
          String cod = Ref.substring(0, 11).trim();
          String dep = Ref.substring(12, 70).trim();
          String unm = Ref.substring(74, 80).trim();
          if (DetPrd != null) {
            DetPrd.dispose();
          }
          DetPrd = new Consulta_MovProdDet(cod, dep, unm, noc, Fed, Feh);
          DetPrd.setVisible(true);
          DetPrd.setExtendedState(NORMAL);
          DetPrd.setVisible(true);
        }
      }
    }
  }

  // Lista Datos
  public void ListaDatos() {

    modelo.clear();

    String cop = "";
    String dep = "";
    String unm = "";

    double can = 0, cang = 0;
    double tpe = 0, tpeg = 0;

    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.MovPrd ("
              + "cop  varchar(10),"
              + "dep  varchar(80),"
              + "unm  varchar(06),"
              + "tip  varchar(01),"
              + "can  int,"
              + "por  Decimal(6,2),"
              + "tpe  Decimal(15,2))"
              + "NOT LOGGED ON COMMIT PRESERVE ROWS";
      st.execute(sql);

      String vax = "order by dep";
      if (Ord.equals("1")) {
        vax = "order by can desc,dep";
      }
      if (Ord2.equals("1")) {
        vax = "order by tpe desc,dep";
      }

      sql = "insert into SESSION.MovPrd "
              + "SELECT cop,dep,' ' unm,tip,sum(can) can,x.por,(case when tip='0' then sum(can*prm) else sum(can*prd) end) tpe "
              + "FROM pedidoH x,pedidoD y "
              + "where x.npe=y.npe "
              + "  and fep between '" + Fed + "' and '" + Feh + "' "
              + "  and (cop like '%" + filP + "%' or dep like '%" + filP + "%' ) "
              + "  and noc like '%" + noc + "%' "
              + "group by cop,dep,tip,x.por ";
              //+ vax;
      st.execute(sql);

      sql = "SELECT cop,dep,unm,sum(can) can,sum(tpe-(tpe*por)) tpe "
              + "FROM SESSION.MovPrd "
              + "group by cop,dep,unm "
              + vax;

      rs = st.executeQuery(sql);
      while (rs.next()) {
        cop = rs.getString("cop");
        dep = rs.getString("dep");
        unm = rs.getString("unm");
        can = rs.getDouble("can");
        tpe = rs.getDouble("tpe");
        if (dep.length() > 60) {
          dep = dep.substring(0, 60);
        }
        dep = dep.toUpperCase();
        cang = cang + can;
        tpeg = tpeg + tpe;

        vax = String.format(format, cop, dep, unm, MtoEs(can, 0), MtoEs(tpe, 0));
        modelo.addElement(vax);
      }
      btnSel.setVisible(false);
      if (!modelo.isEmpty()) {
        btnSel.setVisible(true);
        labReg.setText("Regs =  " + modelo.size());
      }
      jList.setModel(modelo);
      labcan.setText(MtoEs(cang, 0) + "  ");
      labTot.setText(MtoEs(tpeg, 0) + "  ");
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Consulta_MovProd.class.getName()).log(Level.SEVERE, null, ex);
    }
    txtPrd.requestFocus();
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel3 = new javax.swing.JLabel();
    Cliente1 = new javax.swing.JLabel();
    txtPrd = new javax.swing.JTextField();
    Cliente = new javax.swing.JLabel();
    btnBus = new javax.swing.JButton();
    jTiT = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jList = new javax.swing.JList();
    btnSal = new javax.swing.JButton();
    labReg = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    labcan = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    labTot = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    jFed = new com.toedter.calendar.JDateChooser();
    txtFed = new javax.swing.JTextField();
    jLabel2 = new javax.swing.JLabel();
    jFeh = new com.toedter.calendar.JDateChooser();
    txtFeh = new javax.swing.JTextField();
    jLabel12 = new javax.swing.JLabel();
    cbCan = new javax.swing.JCheckBox();
    btnSel = new javax.swing.JButton();
    jLabel13 = new javax.swing.JLabel();
    cbPrc = new javax.swing.JCheckBox();
    cbCte = new javax.swing.JComboBox();
    jButton1 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/BusPrd.jpg"))); // NOI18N
    jLabel3.setText("MOVIMIENTO PRODUCTOS");
    jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    Cliente1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    Cliente1.setForeground(new java.awt.Color(51, 51, 102));
    Cliente1.setText("Producto");

    txtPrd.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtPrd.setToolTipText("Buscar Clientes");
    txtPrd.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtPrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtPrd.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtPrdMouseClicked(evt);
      }
    });
    txtPrd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtPrdActionPerformed(evt);
      }
    });
    txtPrd.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtPrdKeyReleased(evt);
      }
    });

    Cliente.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    Cliente.setForeground(new java.awt.Color(51, 51, 102));
    Cliente.setText(" Cliente");

    btnBus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnBus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    btnBus.setText("Buscar");
    btnBus.setPreferredSize(new java.awt.Dimension(77, 30));
    btnBus.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnBusActionPerformed(evt);
      }
    });

    jTiT.setBackground(new java.awt.Color(0, 102, 102));
    jTiT.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
    jTiT.setForeground(new java.awt.Color(255, 255, 255));
    jTiT.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jTiT.setOpaque(true);

    jList.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
    jList.setOpaque(false);
    jScrollPane1.setViewportView(jList);

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
      }
    });

    labReg.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    labReg.setForeground(new java.awt.Color(51, 51, 102));
    labReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labReg.setText(" ");
    labReg.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(0, 51, 102));
    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel5.setText("Cant Pedida");

    labcan.setBackground(new java.awt.Color(204, 204, 204));
    labcan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labcan.setForeground(new java.awt.Color(0, 0, 102));
    labcan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labcan.setText(" ");
    labcan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labcan.setOpaque(true);
    labcan.setPreferredSize(new java.awt.Dimension(3, 30));

    jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(0, 51, 102));
    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel4.setText("Tot $");

    labTot.setBackground(new java.awt.Color(204, 204, 204));
    labTot.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labTot.setForeground(new java.awt.Color(0, 0, 102));
    labTot.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labTot.setText(" ");
    labTot.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labTot.setOpaque(true);
    labTot.setPreferredSize(new java.awt.Dimension(3, 30));

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

    jLabel12.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel12.setForeground(new java.awt.Color(51, 51, 102));
    jLabel12.setText("Orden x Cant Mayor");
    jLabel12.setToolTipText("Cliente con factura");

    cbCan.setBackground(new java.awt.Color(240, 248, 239));
    cbCan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbCan.setOpaque(false);
    cbCan.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbCanActionPerformed(evt);
      }
    });

    btnSel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ok2.png"))); // NOI18N
    btnSel.setText("Seleccionar");
    btnSel.setPreferredSize(new java.awt.Dimension(77, 28));
    btnSel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSelActionPerformed(evt);
      }
    });

    jLabel13.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel13.setForeground(new java.awt.Color(51, 51, 102));
    jLabel13.setText("Orden x Tot $ Mayor");
    jLabel13.setToolTipText("Cliente con factura");

    cbPrc.setBackground(new java.awt.Color(240, 248, 239));
    cbPrc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbPrc.setOpaque(false);
    cbPrc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbPrcActionPerformed(evt);
      }
    });

    cbCte.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
    cbCte.setMaximumRowCount(30);
    cbCte.setToolTipText("Seleccione Cliente");
    cbCte.setMinimumSize(new java.awt.Dimension(32, 30));
    cbCte.setOpaque(false);
    cbCte.setPreferredSize(new java.awt.Dimension(32, 30));
    cbCte.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbCteItemStateChanged(evt);
      }
    });
    cbCte.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        cbCteMouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        cbCteMouseEntered(evt);
      }
    });
    cbCte.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbCteActionPerformed(evt);
      }
    });

    jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/grafic.png"))); // NOI18N
    jButton1.setText("Cantidad");
    jButton1.setToolTipText("Grafica  Cantidad Productos Pedido");
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
        .addGap(21, 21, 21)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jTiT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(txtPrd, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addGroup(layout.createSequentialGroup()
                .addComponent(Cliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbCte, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(13, 13, 13))
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                  .addComponent(cbPrc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(cbCan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jFed, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 0, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(btnSal)
            .addGap(56, 56, 56)
            .addComponent(btnSel, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(labReg, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(52, 52, 52)
            .addComponent(jLabel5)
            .addGap(18, 18, 18)
            .addComponent(labcan, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(30, 30, 30)
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(labTot, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(21, 21, 21)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel3)
              .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(txtFed)
                  .addComponent(jLabel2))
                .addComponent(jFed, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(txtPrd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(cbCte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtFeh)
                .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(jFeh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(cbCan, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(cbPrc, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel13))))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnSal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labReg, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(labcan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(labTot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnSel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );

    jButton1.getAccessibleContext().setAccessibleDescription("Grafica Cantidad Pedida x Productos");

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void txtPrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPrdMouseClicked
    modelo.clear();
    cbCte.setSelectedIndex(-1);
    txtPrd.setText(" ");
  }//GEN-LAST:event_txtPrdMouseClicked

  private void txtPrdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrdActionPerformed
    filP = txtPrd.getText().toUpperCase().trim();
  }//GEN-LAST:event_txtPrdActionPerformed

  private void txtPrdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrdKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtPrdKeyReleased

  private void btnBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusActionPerformed
    filP = txtPrd.getText().toUpperCase().trim();
    ListaDatos();
  }//GEN-LAST:event_btnBusActionPerformed

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    conMov.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

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

  private void cbCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCanActionPerformed
    Ord = "0";
    if (cbCan.isSelected() == true) {
      Ord = "1";
      Ord2 = "0";
      cbPrc.setSelected(false);
    }
  }//GEN-LAST:event_cbCanActionPerformed

  private void btnSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelActionPerformed
    Seleccionar();
  }//GEN-LAST:event_btnSelActionPerformed

  private void cbPrcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPrcActionPerformed
    Ord2 = "0";
    if (cbPrc.isSelected() == true) {
      Ord2 = "1";
      Ord = "0";
      cbCan.setSelected(false);
    }
  }//GEN-LAST:event_cbPrcActionPerformed

  private void cbCteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbCteItemStateChanged
    modelo.clear();
    noc = "";
    int idx = cbCte.getSelectedIndex();
    if (idx > 0 && evt.getSource() == cbCte && evt.getStateChange() == 1) {
      String sel = (String) cbCte.getSelectedItem();  //valor item seleccionado
      if (sel != null) {
        if (sel.length() > 0) {
          noc = sel.trim();
          btnBus.requestFocus();
        }
      }
    } else {
      txtPrd.requestFocus();
    }
  }//GEN-LAST:event_cbCteItemStateChanged

  private void cbCteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbCteMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_cbCteMouseClicked

  private void cbCteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbCteMouseEntered
    //cbCte.setPopupVisible(true);
  }//GEN-LAST:event_cbCteMouseEntered

  private void cbCteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCteActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbCteActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    if (GrfPrd != null) {
      GrfPrd.dispose();
    }
    GrfPrd = new GraficaVentasBarraPrd(Fed, Feh);   // Grafica Ventas
    GrfPrd.setExtendedState(NORMAL);
    GrfPrd.pack();
    RefineryUtilities.centerFrameOnScreen(GrfPrd);
    GrfPrd.setVisible(true);

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
      java.util.logging.Logger.getLogger(Consulta_MovProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Consulta_MovProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Consulta_MovProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Consulta_MovProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        new Consulta_MovProd().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Cliente;
  private javax.swing.JLabel Cliente1;
  private javax.swing.JButton btnBus;
  private javax.swing.JButton btnSal;
  private javax.swing.JButton btnSel;
  private javax.swing.JCheckBox cbCan;
  private javax.swing.JComboBox cbCte;
  private javax.swing.JCheckBox cbPrc;
  private javax.swing.JButton jButton1;
  private com.toedter.calendar.JDateChooser jFed;
  private com.toedter.calendar.JDateChooser jFeh;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JList jList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel jTiT;
  private javax.swing.JLabel labReg;
  private javax.swing.JLabel labTot;
  private javax.swing.JLabel labcan;
  private javax.swing.JTextField txtFed;
  private javax.swing.JTextField txtFeh;
  private javax.swing.JTextField txtPrd;
  // End of variables declaration//GEN-END:variables
}
