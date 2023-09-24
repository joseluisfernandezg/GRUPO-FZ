package gestionFZ;

import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.dmy_ymd;
import static comun.MetodosComunes.getAnoHoy;
import static comun.MetodosComunes.getdiasFec;
import static comun.MetodosComunes.isNumeric;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymd_dmyc;
import static comun.MetodosComunes.ymdhoy;
import static gestionFZ.Registro_NotaEntrega.conNoe;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import modelo.Clientes;
import modelo.ConexionSQL;

public class Consulta_NotaEntrega extends javax.swing.JFrame {

  private DefaultListModel modelo;

  int opc = 0, indp = 0;

  String Fed = "", Feh = "", coc = "";
  String fil = "";
  String sta = "T";
  String ord = "0";
  String format = " %1$-7s %2$-7s %3$-7s %4$-7s %5$-7s %6$-7s %7$7s %8$5s  %9$-7s %10$-7s %11$5s   %12$-30s %13$14s";

  List<String> vecLis; // Vector  lista Notas

  Registro_NotaEntrega ctrlNE;
  Registro_NotaCredito ctrlNC;
  Registro_PagosClientes ctrlNEC;

  ObservableList<Clientes> obsNotaEnt;  // ObservableList modelo tabla notaent

  public Consulta_NotaEntrega(Registro_NotaEntrega ctrl, String cox) {
    ctrlNE = ctrl;
    coc = cox;
    ConsultaNotaEntrega(1);
  }

  public Consulta_NotaEntrega(Registro_NotaCredito ctrl, String cox) {
    ctrlNC = ctrl;
    coc = cox;
    ConsultaNotaEntrega(2);
  }

  public Consulta_NotaEntrega(Registro_PagosClientes ctrl, String cox) {
    ctrlNEC = ctrl;
    coc = cox;
    ConsultaNotaEntrega(3);
  }

  public void ConsultaNotaEntrega(int opx) {

    opc = opx;

    initComponents();
    setLocationRelativeTo(null); // centramos la ventana en la pantalla
    setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    txtBus.setText(coc);
    btnSel.setVisible(false);

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

    String vax = String.format(format, " Nota", " N.Fac ", " F.Not", " F.Ven", " F.Rec", " F.Ped", "  Pedido", " Dias", "R.Cob", "F.Pag", " Dias", "Cliente", "    T.Nota $");
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

    //Eventos teclas
    jList.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        //System.out.println("tecla e.getKeyCode() =" + e.getKeyCode());
        if (modelo.size() > 0) {
          // Enter (Imprimir/Eliminar Pedido)
          if (indp == 1) {
            if (e.getKeyCode() == 10) {
              Seleccion(e.getKeyCode());
            }
          }
          indp = 1;
          // Barra Espaciadora (no funciona)
          if (e.getKeyCode() == 32) {
            Seleccion(e.getKeyCode());
          }
          // Refresh  F5
          if (e.getKeyCode() == 116) {
            //Seleccion(e.getKeyCode());
          }
          // Salir <- F12
          if (e.getKeyCode() == 123) {
            //Seleccion(e.getKeyCode());
          }
          // Flecha <-
          if (e.getKeyCode() == 37) {
            Seleccion(e.getKeyCode());
          }
          // Flecha ->
          if (e.getKeyCode() == 39) {
            //Seleccion(e.getKeyCode());
          }
        }
      }
    });

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

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        conNoe.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        conNoe.dispose();
      }
    });

    cbTip.setSelectedIndex(2);
    sta = "T";
    Fed = (getAnoHoy() - 1) + "0101";
    txtFed.setText(ymd_dmy(Fed));
    ListaDatos();
  }

  // Seleccion Item
  public void Seleccion(int tec) {
    int idx = jList.getSelectedIndex();
    if (tec == 32) {
      idx = idx - 1;
      jList.setSelectedIndex(idx);
    }
    String Ref = String.valueOf(modelo.getElementAt(idx));
    Ref = Ref.trim();
    if (Ref.length() >= 8) {
      String rec = Ref.substring(0, 8).trim();
      if (isNumeric(rec)) {
        int nne = Integer.parseInt(rec);
        if (opc == 1) {
          ctrlNE.recibeNotaEntrega(nne);
        }
        if (opc == 2) {
          ctrlNC.recibeNotaEntrega(nne);
        }
        if (opc == 3) {
          ctrlNEC.recibeNotaEntregaRC(nne);
        }
        conNoe.dispose();
      }
    }
  }

  // Selecciona Registro
  public void Seleccionar() {
    int[] x = jList.getSelectedIndices();
    int idx = x.length;
    idx = idx - 1;
    if (idx >= 0) {
      String Ref = String.valueOf(modelo.getElementAt(x[idx]));
      Ref = Ref.trim();
      String rec = "";
      if (Ref.length() >= 8) {
        rec = Ref.substring(0, 8).trim();
        if (isNumeric(rec)) {
          int nne = Integer.parseInt(rec);
          if (opc == 1) {
            ctrlNE.recibeNotaEntrega(nne);
          }
          if (opc == 2) {
            ctrlNC.recibeNotaEntrega(nne);
          }
          if (opc == 3) {
            ctrlNEC.recibeNotaEntregaRC(nne);
          }
          conNoe.dispose();
        }
      }
    }
  }

  // Lista Datos
  public void ListaDatos() {

    fil = txtBus.getText().toUpperCase().trim();

    modelo.clear();

    vecLis = new ArrayList<>();

    int nne = 0, npe = 0, nfa = 0, nrc = 0, nd1 = 0, nd2 = 0;
    String noc = "", fne = "", fpe = "", fre = "", fev = "", fpa = "";
    double tne = 0, tog = 0;

    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      String vax = "";
      if (sta.equals("P")) {
        vax = " and nne not in (select nno from recibocobroD where tpd=0) ";
      } else {
        if (sta.equals("C")) {
          vax = " and nne in (select nno from recibocobroD where tpd=0  ) ";
        }
      }
      // Orden listado
      String vax2 = "order by noc,nne desc";
      if (ord.contains("0")) {
        vax2 = "order by nne desc";
      }

      String sql = "SELECT nne,fne,fee,fev,nfa,"
              + "(select nom from clientes where clientes.coc=notaent.coc) noc,"
              + "npe,fep,tne-tdn tne,tpe,"
              + "(select nrc from recibocobroD y where y.tpd=0 and nno=nne FETCH FIRST 1 ROWS ONLY ) nrc,"
              + "(select nullif(fpa,'') from recibocobroD y where y.tpd=0 and nno=nne FETCH FIRST 1 ROWS ONLY ) fpa "
              + "FROM notaent "
              + "where fne between '" + Fed + "' and '" + Feh + "' "
              + "  and (noc like '%" + fil + "%' or coc like '%" + fil + "%') "
              + vax
              + vax2;
      rs = st.executeQuery(sql);
      while (rs.next()) {
        nne = rs.getInt("nne");
        npe = rs.getInt("npe");
        nfa = rs.getInt("nfa");
        noc = rs.getString("noc");
        fne = rs.getString("fne");
        fev = rs.getString("fev");
        fre = rs.getString("fee");
        fpe = rs.getString("fep");
        fpa = rs.getString("fpa");
        tne = rs.getDouble("tne");
        nrc = rs.getInt("nrc");

        if (noc.length() > 30) {
          noc = noc.substring(0, 30);
        }
        noc = noc.toUpperCase();

        tog = tog + tne;

        String nfx = "" + nfa;
        if (nfa == 0) {
          nfx = "";
        }

        String nrx = "";
        if (nrc > 0) {
          nrx = "" + nrc;
        }

        // Fecha Nota Entrega
        String fnx = "";
        if (fne.length() == 8) {
          fnx = ymd_dmyc(fne);
          fnx = fnx.substring(0, 5);
        }

        // Fecha Vence
        String fvx = "";
        if (fev.length() == 8) {
          fvx = ymd_dmyc(fev);
          fvx = fvx.substring(0, 5);
        }

        // Fecha Recibe
        String frx = "";
        if (fre.length() == 8) {
          frx = ymd_dmyc(fre);
          frx = frx.substring(0, 5);
        }

        // Fecha Pedido
        String fpx = "";
        if (fpe.length() == 8) {
          fpx = ymd_dmyc(fpe);
          fpx = fpx.substring(0, 5);
        }

        // Fecha cobro
        String fcx = "";
        if (fpa == null) {
          fpa = "";
        }

        nd1 = 0;
        if (fne.length() == 8 && fpe.length() == 8) {
          nd1 = getdiasFec(fne, fpe);
        }

        // Fecha pago
        nd2 = 0;
        String feh = ymdhoy();
        if (fpa.length() == 8) {
          fcx = ymd_dmyc(fpa);
          fcx = fcx.substring(0, 5);
          if (fre.length() == 8) {
            nd2 = getdiasFec(fpa, fre);
          } else {
            nd2 = getdiasFec(fpa, fne);
          }
        } else {
          nd2 = getdiasFec(feh, fne);
        }

        vax = String.format(format, nne, nfx, fnx, fvx, frx, fpx, npe, nd1, nrx, fcx, nd2, noc, MtoEs(tne, 0));
        modelo.addElement(vax);

        //vecLis.add(vax);
      }
      jList.setModel(modelo);
      btnSel.setVisible(false);
      if (!modelo.isEmpty()) {
        btnSel.setVisible(true);
        jList.setSelectedIndex(0);
        labCan.setText("Cant =  " + modelo.size());

        //Lista Vec
        /*
        for (String lin : vecLis) {
          System.out.println("lin=" + lin);
        }
         */
      }
      jToT.setText(MtoEs(tog, 0) + "  ");
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Consulta_NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    txtBus.requestFocus();
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jFeh = new com.toedter.calendar.JDateChooser();
    jLabel2 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    txtFed = new javax.swing.JTextField();
    txtFeh = new javax.swing.JTextField();
    jFed = new com.toedter.calendar.JDateChooser();
    jScrollPane1 = new javax.swing.JScrollPane();
    jList = new javax.swing.JList();
    jTiT = new javax.swing.JLabel();
    btnSal = new javax.swing.JButton();
    btnBus = new javax.swing.JButton();
    btnSel = new javax.swing.JButton();
    jToT = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    txtBus = new javax.swing.JTextField();
    jLabel7 = new javax.swing.JLabel();
    cbTip = new javax.swing.JComboBox();
    Cliente1 = new javax.swing.JLabel();
    labCan = new javax.swing.JLabel();
    cbOrd = new javax.swing.JCheckBox();
    jLabel8 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(0, 51, 102));
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setText("al");

    jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel1.setForeground(new java.awt.Color(0, 0, 102));
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Del");

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/notent.jpg"))); // NOI18N
    jLabel3.setText("CONSULTA NOTAS DE ENTREGAS");
    jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

    jFed.setForeground(new java.awt.Color(0, 0, 102));
    jFed.setToolTipText("Seleccione Dia a Procesar");
    jFed.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
    jFed.setPreferredSize(new java.awt.Dimension(42, 10));

    jList.setFont(new java.awt.Font("Courier New", 1, 13)); // NOI18N
    jList.setOpaque(false);
    jScrollPane1.setViewportView(jList);

    jTiT.setBackground(new java.awt.Color(0, 102, 102));
    jTiT.setFont(new java.awt.Font("Courier New", 1, 13)); // NOI18N
    jTiT.setForeground(new java.awt.Color(255, 255, 255));
    jTiT.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
    jTiT.setOpaque(true);

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
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

    btnSel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ok2.png"))); // NOI18N
    btnSel.setText("Seleccionar");
    btnSel.setPreferredSize(new java.awt.Dimension(77, 28));
    btnSel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSelActionPerformed(evt);
      }
    });

    jToT.setBackground(new java.awt.Color(204, 204, 204));
    jToT.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    jToT.setForeground(new java.awt.Color(0, 0, 102));
    jToT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jToT.setText(" ");
    jToT.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToT.setOpaque(true);
    jToT.setPreferredSize(new java.awt.Dimension(3, 30));

    jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(0, 51, 102));
    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel4.setText("Total  Notas $");

    txtBus.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtBus.setToolTipText("Buscar Clientes");
    txtBus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtBus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

    Cliente1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    Cliente1.setForeground(new java.awt.Color(51, 51, 102));
    Cliente1.setText(" Cliente");

    labCan.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    labCan.setForeground(new java.awt.Color(51, 51, 102));
    labCan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labCan.setText(" ");
    labCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    cbOrd.setToolTipText("Consulta Stock en cero");
    cbOrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbOrd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbOrdActionPerformed(evt);
      }
    });

    jLabel8.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel8.setForeground(new java.awt.Color(51, 51, 102));
    jLabel8.setText("Ordenar x Cliente");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(19, 19, 19)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(btnSal)
            .addGap(31, 31, 31)
            .addComponent(labCan, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel4)
            .addGap(28, 28, 28)
            .addComponent(jToT, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(37, 37, 37))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addGroup(layout.createSequentialGroup()
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
                    .addComponent(jFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(cbTip, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(48, 48, 48)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(cbOrd)))
                .addGap(18, 18, 18)
                .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
                .addComponent(btnSel, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(jScrollPane1)
              .addComponent(jTiT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap(20, Short.MAX_VALUE))))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel3)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
            .addComponent(cbOrd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(cbTip, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jFed, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnSel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addGap(2, 2, 2)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(7, 7, 7)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(btnSal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(labCan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jToT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jFehMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jFehMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jFehMouseClicked

  private void jFehMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jFehMouseExited
    // TODO add your handling code here:
  }//GEN-LAST:event_jFehMouseExited

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

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    conNoe.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void btnBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusActionPerformed
    ListaDatos();
  }//GEN-LAST:event_btnBusActionPerformed

  private void btnSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelActionPerformed
    Seleccionar();
  }//GEN-LAST:event_btnSelActionPerformed

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
    jList.requestFocus();
  }//GEN-LAST:event_txtBusActionPerformed

  private void txtBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusKeyReleased
    fil = txtBus.getText().toUpperCase().trim();
    ListaDatos();
  }//GEN-LAST:event_txtBusKeyReleased

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
          ListaDatos();
        }
      }
    }
  }//GEN-LAST:event_cbTipItemStateChanged

  private void cbTipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbTipActionPerformed

  private void cbOrdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOrdActionPerformed
    if (cbOrd.isSelected() == true) {
      ord = "1";
    } else {
      ord = "0";
    }
    ListaDatos();
  }//GEN-LAST:event_cbOrdActionPerformed

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
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Consulta_NotaEntrega.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      Registro_NotaEntrega ctrl = new Registro_NotaEntrega();

      public void run() {
        new Consulta_NotaEntrega(ctrl, "").setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Cliente1;
  private javax.swing.JButton btnBus;
  private javax.swing.JButton btnSal;
  private javax.swing.JButton btnSel;
  private javax.swing.JCheckBox cbOrd;
  private javax.swing.JComboBox cbTip;
  private com.toedter.calendar.JDateChooser jFed;
  private com.toedter.calendar.JDateChooser jFeh;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JList jList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel jTiT;
  private javax.swing.JLabel jToT;
  private javax.swing.JLabel labCan;
  private javax.swing.JTextField txtBus;
  private javax.swing.JTextField txtFed;
  private javax.swing.JTextField txtFeh;
  // End of variables declaration//GEN-END:variables
}
