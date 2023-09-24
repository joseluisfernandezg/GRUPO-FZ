package gestionFZ;

import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.dmy_ymd;
import static comun.MetodosComunes.getAnoHoy;
import static comun.MetodosComunes.isNumeric;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymd_dmyc;
import static gestionFZ.Registro_PagosClientes.conNrc;
import static gestionFZ.Registro_PagosClientes.labMsgR;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javafx.collections.ObservableList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import modelo.ReciboCobro;

public class Consulta_ReciboCobro extends javax.swing.JFrame {

  private DefaultListModel modelo;

  String Fed = "", Feh = "", coc = "";
  String fil = "";
  String sta = "T";
  String ord = "0";
  String format = " %1$-10s %2$-10s %3$-30s %4$12s %5$12s %6$3s %7$12s %8$3s";
  int indp = 0;
  int indl = 0;

  Registro_PagosClientes ctrN;

  ObservableList<ReciboCobro> obsReciboH;  // ObservableList modelo tabla recibocobroH

  public Consulta_ReciboCobro(Registro_PagosClientes ctrl, String cox) {
    ctrN = ctrl;
    coc = cox;
    ConsultaReciboCobro();
  }

  public void ConsultaReciboCobro() {

    initComponents();
    setLocationRelativeTo(null); // centramos la ventana en la pantalla
    setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    txtBus.setText(" " + coc);
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

    String vax = String.format(format, " Recibo", " Fecha", " Cliente ", "  T.Notas $", "  T.Saldo $", " ", "  T.Saldo Bs", " ");
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

    //Seccion Mouse (Doble Click)
    jList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2 && modelo.size() > 0) {
          Seleccionar();
        }
      }
    });

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

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        conNrc.dispose();
      }
    });
    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        conNrc.dispose();
      }
    });
    cbTip.setSelectedIndex(2);
    Fed = (getAnoHoy() - 1) + "0101";
    txtFed.setText(ymd_dmy(Fed));
    listaObsList();
    indl = 1;
    labMsgR.setText("");
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
    String rec = "";
    if (Ref.length() >= 20) {
      rec = Ref.substring(0, 10).trim();
      if (isNumeric(rec)) {
        int nrc = Integer.parseInt(rec);
        ctrN.recibeReciboCobro(nrc);
        conNrc.dispose();
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
      if (Ref.length() >= 20) {
        rec = Ref.substring(0, 10).trim();
        if (isNumeric(rec)) {
          int nrc = Integer.parseInt(rec);
          ctrN.recibeReciboCobro(nrc);
          conNrc.dispose();
        }
      }
    }
  }

  // Lista Datos
  public void listaObsList() {

    modelo.clear();

    int nrc = 0;
    double tod = 0;
    double tpd = 0;
    double tsb = 0;
    double tgd = 0;
    double tgp = 0;
    String frc = "", noc = "";

    ReciboCobro r = new ReciboCobro();
    obsReciboH = r.getReciboH(Fed, Feh, fil, sta, ord);

    for (ReciboCobro rec : obsReciboH) {

      nrc = rec.getNrc();
      frc = rec.getFrc();
      noc = rec.getNoc();
      tod = rec.getTod();
      tpd = rec.getTpd();
      tsb = rec.getTpb();

      if (noc.length() > 30) {
        noc = noc.substring(0, 30);
      }
      noc = noc.toUpperCase();

      int neg = 0;
      if (tpd < 0) {
        neg = 1;
        tpd = tpd * -1;
      }
      String mox = MtoEs(tpd, 2);
      mox = GetCurrencyDouble(mox);
      tpd = GetMtoDouble(mox);
      if (neg == 1) {
        tpd = tpd * -1;
      }

      neg = 0;
      if (tsb < 0) {
        neg = 1;
        tsb = tsb * -1;
      }
      mox = MtoEs(tsb, 2);
      mox = GetCurrencyDouble(mox);
      tsb = GetMtoDouble(mox);
      if (neg == 1) {
        tsb = tsb * -1;
      }

      tgd = tgd + tod;
      tgp = tgp + tpd;

      String mx1 = MtoEs(tod, 0);
      String mx2 = MtoEs(tpd, 0);
      String mx3 = MtoEs(tsb, 0);

      String sf1 = "";
      String sf2 = "";

      if ((tpd) < 0) {
        mx2 = mx2.replace("-", "");
        sf1 = "(F)";
      }
      if (tsb < 0) {
        mx3 = mx3.replace("-", "");
        sf2 = "(F)";
      }

      if (mx2.equals("0,00")) {
        sf1 = "";
      }

      if (mx3.equals("0,00")) {
        sf2 = "";
      }

      String vax = String.format(format, nrc, ymd_dmyc(frc), noc, mx1, mx2, sf1, mx3, sf2);
      modelo.addElement(vax);

    }

    jList.setModel(modelo);
    btnSel.setVisible(false);
    if (!modelo.isEmpty()) {
      btnSel.setVisible(true);
      jList.setSelectedIndex(0);
      lanCan.setText("Cant =  " + modelo.size());
    }
    labTod.setText(MtoEs(tgd, 0) + "  ");
    labTop.setText(MtoEs(tgp, 0) + "  ");
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
    labTod = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    txtBus = new javax.swing.JTextField();
    jLabel7 = new javax.swing.JLabel();
    cbTip = new javax.swing.JComboBox();
    Cliente = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    labTop = new javax.swing.JLabel();
    cbOrd = new javax.swing.JCheckBox();
    jLabel8 = new javax.swing.JLabel();
    lanCan = new javax.swing.JLabel();
    labmsg = new javax.swing.JLabel();

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
    jLabel3.setText("CONSULTA RECIBOS DE COBROS");
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

    jList.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
    jList.setOpaque(false);
    jScrollPane1.setViewportView(jList);

    jTiT.setBackground(new java.awt.Color(0, 102, 102));
    jTiT.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
    jTiT.setForeground(new java.awt.Color(255, 255, 255));
    jTiT.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
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

    btnSel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    btnSel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ok2.png"))); // NOI18N
    btnSel.setText("Seleccionar");
    btnSel.setPreferredSize(new java.awt.Dimension(77, 25));
    btnSel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSelActionPerformed(evt);
      }
    });

    labTod.setBackground(new java.awt.Color(204, 204, 204));
    labTod.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labTod.setForeground(new java.awt.Color(51, 51, 51));
    labTod.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labTod.setText(" ");
    labTod.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labTod.setOpaque(true);
    labTod.setPreferredSize(new java.awt.Dimension(3, 30));

    jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(0, 51, 102));
    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel4.setText("T. Notas $");

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
    cbTip.setForeground(new java.awt.Color(153, 0, 0));
    cbTip.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pendientes", "Cerrados", "Todos" }));
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

    Cliente.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    Cliente.setForeground(new java.awt.Color(51, 51, 102));
    Cliente.setText(" Cliente");

    jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(0, 51, 102));
    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel5.setText("T. Saldo $");

    labTop.setBackground(new java.awt.Color(204, 204, 204));
    labTop.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labTop.setForeground(new java.awt.Color(51, 51, 51));
    labTop.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labTop.setText(" ");
    labTop.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labTop.setOpaque(true);
    labTop.setPreferredSize(new java.awt.Dimension(3, 30));

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

    lanCan.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    lanCan.setForeground(new java.awt.Color(51, 51, 102));
    lanCan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lanCan.setText(" ");
    lanCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    labmsg.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    labmsg.setForeground(new java.awt.Color(102, 102, 102));
    labmsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labmsg.setText("(F) = Saldo a Favor");
    labmsg.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(19, 19, 19)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12, 12, 12)
                    .addComponent(jFed, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(9, 9, 9)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(txtFeh)
                  .addComponent(cbTip, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cbOrd))
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(33, 33, 33)
                    .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(41, 41, 41)
                    .addComponent(btnSel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))))
              .addGroup(layout.createSequentialGroup()
                .addComponent(btnSal)
                .addGap(42, 42, 42)
                .addComponent(lanCan, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(labmsg, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labTod, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labTop, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1047, Short.MAX_VALUE)
              .addComponent(jTiT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(0, 15, Short.MAX_VALUE))))
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
              .addComponent(jLabel7)
              .addComponent(cbTip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jFed, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnSel, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addGap(2, 2, 2)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labTod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
          .addComponent(labTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnSal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lanCan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labmsg, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    conNrc.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void btnBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusActionPerformed
    fil = txtBus.getText().toUpperCase().trim();
    listaObsList();
  }//GEN-LAST:event_btnBusActionPerformed

  private void btnSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelActionPerformed
    Seleccionar();
  }//GEN-LAST:event_btnSelActionPerformed

  private void txtBusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBusMouseClicked
    if (txtBus.getText().length() == 1) {
      fil = "";
      listaObsList();
    } else {
      txtBus.setText(" ");
    }
  }//GEN-LAST:event_txtBusMouseClicked

  private void txtBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusActionPerformed
    if (txtBus.getText().length() == 1) {
      fil = "";
    }
    jList.requestFocus();
  }//GEN-LAST:event_txtBusActionPerformed

  private void txtBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusKeyReleased
    fil = txtBus.getText().toUpperCase().trim();
    listaObsList();
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
            if (str.equals("Cerrados")) {
              sta = "C";
            }
          }
          if (indl == 1) {
            listaObsList();
          }
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
    listaObsList();
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
      java.util.logging.Logger.getLogger(Consulta_ReciboCobro.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      Registro_PagosClientes ctrl = new Registro_PagosClientes();

      public void run() {
        new Consulta_ReciboCobro(ctrl, "").setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Cliente;
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
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JList jList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel jTiT;
  private javax.swing.JLabel labTod;
  private javax.swing.JLabel labTop;
  private javax.swing.JLabel labmsg;
  private javax.swing.JLabel lanCan;
  private javax.swing.JTextField txtBus;
  private javax.swing.JTextField txtFed;
  private javax.swing.JTextField txtFeh;
  // End of variables declaration//GEN-END:variables
}
