package gestionFZ;

import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.dmy_ymd;
import static comun.MetodosComunes.getAnoHoy;
import static comun.MetodosComunes.ymd_dmy;
import static gestionFZ.Menu.PedNot;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import modelo.ConexionSQL;

public class Consulta_PedVsNotR extends javax.swing.JFrame {

  private DefaultListModel modelo;

  String Ref = "";
  String Fed = "", Feh = "";
  String fil = "";
  String Ord = "0";

  String noc = "", coc = "";
  String format = " %1$-40s %2$-10s %3$10s %4$10s %5$13s %6$13s %7$13s\n";

  int can = 0;
  int cap = 0;
  int cgn = 0;
  int cgp = 0;

  double tno = 0, tpm = 0, tpd = 0;
  double tgn = 0, tgp = 0;

  public static Consulta_PedVsNotD DetPed; // Resumen Saldo Cartera Clientes

  public Consulta_PedVsNotR() {
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

    String vax = String.format(format,
            " Cliente", " Codigo", "   C.Notas", "  C.Pedidos", "  T.Notas $", " T.Pedidos $", "   T.Dif");
    jTiT.setText(vax);

    modelo = new DefaultListModel();

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

    txtBus.requestFocus();

    //Seccion Mouse (Doble Click)
    jList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2 && modelo.size() > 0) {
          Seleccionar();
        }
      }
    });

    jList.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index,
              boolean isSelected, boolean cellHasFocus) {

        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Consulta_PedVsNotR.jListColor) {
          Consulta_PedVsNotR.jListColor nextRegistro = (Consulta_PedVsNotR.jListColor) value;
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

    Fed = (getAnoHoy() - 1) + "0101";
    txtFed.setText(ymd_dmy(Fed));
    //ListaDatos();
    txtBus.requestFocus();

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        PedNot.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        PedNot.dispose();
      }
    });

  }

  // Selecciona Registro
  public void Seleccionar() {
    if (Ref.length() >= 52) {
      Ref = Ref.trim();
      String nom = Ref.substring(0, 41).trim();
      String cod = Ref.substring(41, 50).trim();
      //System.out.println(" ref=" + Ref + ",cod=" + cod + ",nom=" + nom);
      if (DetPed != null) {
        DetPed.dispose();
      }
      DetPed = new Consulta_PedVsNotD(cod, nom, Fed, Feh);   // Saldos Clientes
      DetPed.setVisible(true);
      DetPed.setExtendedState(NORMAL);
      DetPed.setVisible(true);
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

    tno = 0;
    tpm = 0;
    tpd = 0;
    tgn = 0;
    tgp = 0;

    fil = txtBus.getText().toUpperCase().trim();

    modelo.clear();

    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.Saldos ("
              + "nom  varchar(50),"
              + "cod  varchar(10),"
              + "can  int,"
              + "cap  int,"
              + "ton  Decimal(15,2),"
              + "tpm  Decimal(15,2),"
              + "tpd  Decimal(15,2))"
              + "NOT LOGGED ON COMMIT PRESERVE ROWS";
      st.execute(sql);

      sql = "insert into SESSION.Saldos "
              + "SELECT '' noc,coc,count(*) cann,0 canp, sum(tne) ton,0 tpm,0 tpd "
              + "FROM notaent "
              + "where fne between '" + Fed + "' and '" + Feh + "' "
              + "  and (noc like '%" + fil + "%' or coc like '%" + fil + "%')"
              + "group by coc ";
      st.execute(sql);

      sql = "insert into SESSION.Saldos "
              + "SELECT '' noc,coc,count(*) cann,0 canp,sum(tnc*-1) ton,0 tpm,0 tpd "
              + "FROM notaCred "
              + "where fnc between '" + Fed + "' and '" + Feh + "' "
              + "  and (noc like '%" + fil + "%' or coc like '%" + fil + "%')"
              + "group by coc ";
      st.execute(sql);

      // cantidad pedidos
      sql = "update SESSION.Saldos "
              + "set nom=(select nom from clientes where clientes.coc=SESSION.Saldos.cod),"
              + "cap =  (select nullif(count(*),0) from pedidoH "
              + "where cod=coc and fep between '" + Fed + "' and '" + Feh + "')";
      st.execute(sql);

      // total pedidos clientes mayor
      sql = "update SESSION.Saldos "
              + "set tpm = (select  nullif((sum(can*prm)),0) from pedidoD x,pedidoH y "
              + "where y.tip='0' and x.npe=y.npe and cod=y.coc and fep between '" + Fed + "' and '" + Feh + "')";
      st.execute(sql);

      // total pedidos clientes detal
      sql = "update SESSION.Saldos "
              + "set tpd = (select  nullif((sum(can*prd)),0) from pedidoD x,pedidoH y "
              + "where y.tip='1' and x.npe=y.npe and cod=y.coc and fep between '" + Fed + "' and '" + Feh + "')";
      st.execute(sql);

      String vax = "Order by ton desc";
      if (Ord.equals("1")) {
        vax = "Order by nom";
      }

      sql = "Select nom,cod,sum(can) can,sum(cap) cap, sum(ton) ton,sum(tpm) tpm,sum(tpd) tpd,0 tdf "
              + "from SESSION.Saldos "
              + "Group by nom,cod "
              + vax;

      rs = st.executeQuery(sql);
      while (rs.next()) {
        coc = rs.getString("cod");
        noc = rs.getString("nom");
        can = rs.getInt("can");
        cap = rs.getInt("cap");
        tno = rs.getDouble("ton");
        tpm = rs.getDouble("tpm");
        tpd = rs.getDouble("tpd");

        if (noc.length() > 40) {
          noc = noc.substring(0, 40);
        }
        noc = noc.toUpperCase();

        double tpe = tpm + tpd;

        String tx1 = MtoEs(tno, 0);
        String tx2 = MtoEs(tpe, 0);
        String tx3 = MtoEs(tno - tpe, 0);

        vax = String.format(format, noc, coc, can, cap, tx1, tx2, tx3);
        modelo.addElement(new Consulta_PedVsNotR.jListColor(vax, 0, false));

        cgn = cgn + can;
        cgp = cgp + cap;

        tgn = tgn + tno;
        tgp = tgp + tpe;

      }

      btnSel.setVisible(false);
      jList.setModel(modelo);
      if (!modelo.isEmpty()) {
        btnSel.setVisible(true);

        lanCan.setText("Cant =  " + modelo.size());
        String tx1 = MtoEs(tgn, 0);
        String tx2 = MtoEs(tgp, 0);
        String tx3 = MtoEs(tgn - tgp, 0);
        vax = String.format(format, "", "   TOTAL", " " + cgn, cgp, tx1, tx2, tx3);
        modelo.addElement(new Consulta_PedVsNotR.jListColor(vax, 1, false));

        int i = modelo.getSize() - 1;
        jList.setSelectedIndex(i);
        jList.ensureIndexIsVisible(i);

      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Consulta_PedVsNotR.class.getName()).log(Level.SEVERE, null, ex);
    }
    txtBus.requestFocus();
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
    jLabel12 = new javax.swing.JLabel();
    cbOrd = new javax.swing.JCheckBox();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/grafic.png"))); // NOI18N
    jLabel3.setText(" RESUMEN  -  PEDIDOS Vs  NOTAS ENTREGA ");
    jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jTiT.setBackground(new java.awt.Color(0, 102, 102));
    jTiT.setFont(new java.awt.Font("Courier New", 1, 15)); // NOI18N
    jTiT.setForeground(new java.awt.Color(255, 255, 255));
    jTiT.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
    jTiT.setOpaque(true);

    jList.setFont(new java.awt.Font("Courier New", 1, 15)); // NOI18N
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

    jLabel12.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel12.setForeground(new java.awt.Color(51, 51, 102));
    jLabel12.setText("Ordena x Nombre");
    jLabel12.setToolTipText("");

    cbOrd.setBackground(new java.awt.Color(240, 248, 239));
    cbOrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbOrd.setOpaque(false);
    cbOrd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbOrdActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(23, 23, 23)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(btnSal)
            .addGap(192, 192, 192)
            .addComponent(lanCan, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
            .addComponent(jTiT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1088, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                  .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(12, 12, 12)
                  .addComponent(jFed, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(9, 9, 9)
                  .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(11, 11, 11)
                  .addComponent(jFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                  .addComponent(jLabel12)
                  .addGap(18, 18, 18)
                  .addComponent(cbOrd)
                  .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                  .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                  .addComponent(btnSel, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        .addContainerGap(22, Short.MAX_VALUE))
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
          .addContainerGap(23, Short.MAX_VALUE)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1088, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGap(24, 24, 24)))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(cbOrd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel3)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jFed, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnSel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addGap(2, 2, 2)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(376, 376, 376)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnSal)
          .addComponent(lanCan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addGap(131, 131, 131)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addContainerGap(52, Short.MAX_VALUE)))
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
    if (txtBus.getText().length() == 0) {
      fil = "";
      ListaDatos();
    } else {
      txtBus.setText("");
    }
  }//GEN-LAST:event_txtBusMouseClicked

  private void txtBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusActionPerformed
    if (txtBus.getText().length() == 0) {
      fil = "";
    }
  }//GEN-LAST:event_txtBusActionPerformed

  private void txtBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusKeyReleased
    fil = txtBus.getText().toUpperCase().trim();
    ListaDatos();
  }//GEN-LAST:event_txtBusKeyReleased

  private void btnBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusActionPerformed
    ListaDatos();
  }//GEN-LAST:event_btnBusActionPerformed

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    PedNot.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void jListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListMouseClicked

  }//GEN-LAST:event_jListMouseClicked

  private void btnSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelActionPerformed
    Seleccionar();
  }//GEN-LAST:event_btnSelActionPerformed

  private void cbOrdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOrdActionPerformed
    Ord = "0";
    if (cbOrd.isSelected() == true) {
      Ord = "1";
    }
  }//GEN-LAST:event_cbOrdActionPerformed

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
      java.util.logging.Logger.getLogger(Consulta_PedVsNotR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Consulta_PedVsNotR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Consulta_PedVsNotR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Consulta_PedVsNotR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        new Consulta_PedVsNotR().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Cliente1;
  private javax.swing.JButton btnBus;
  private javax.swing.JButton btnSal;
  private javax.swing.JButton btnSel;
  private javax.swing.JCheckBox cbOrd;
  private com.toedter.calendar.JDateChooser jFed;
  private com.toedter.calendar.JDateChooser jFeh;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JList jList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel jTiT;
  private javax.swing.JLabel lanCan;
  private javax.swing.JTextField txtBus;
  private javax.swing.JTextField txtFed;
  private javax.swing.JTextField txtFeh;
  // End of variables declaration//GEN-END:variables
}
