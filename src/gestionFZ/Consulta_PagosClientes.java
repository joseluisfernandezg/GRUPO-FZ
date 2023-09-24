package gestionFZ;

import comun.Mensaje;
import static comun.MetodosComunes.FileExist;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.dmy_ymd;
import static comun.MetodosComunes.getAnoHoy;
import static comun.MetodosComunes.getdiasFec;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymd_dmyc;
import static comun.MetodosComunes.ymdhoy;
import static gestionFZ.Registro_PagosClientes.conPag;
import java.awt.Color;
import java.awt.Toolkit;
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
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import modelo.ConexionSQL;

public class Consulta_PagosClientes extends javax.swing.JFrame {

  private DefaultListModel modelo;

  String Fed = "";  // fecha desde
  String Feh = "";  // fecha hasta
  String coc = "";  // cod cliente
  String noc = "";  // nombre cliente
  String fno = "";  // fecha nota
  String fen = "";  // fecha entrega
  String fev = "";  // fecha vence
  String fpa = "";  // fecha pago
  String sta = "0";  // estatus pendiente pago

  double tno = 0;   // total Nota c/iva
  double tdn = 0;   // total descuento
  double tgn = 0;   // total  general neto

  int nrc = 0;      // no  recibo
  int nno = 0;      // no nota entrega
  int dip = 0;      // dias al pago
  int dia = 0;      // dias atraso

  String fil = "";

  String format = " %1$-40s %2$-8s %3$-8s %4$-7s %5$-7s %6$-7s %7$-7s %8$6s %9$9s %10$14s";

  public Consulta_PagosClientes() {
    CuadrePagosClientes();
  }

  public void CuadrePagosClientes() {

    initComponents();
    setLocationRelativeTo(null); // centramos la ventana en la pantalla
    setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    txtBus.setText(coc);
    jLabel4.setVisible(false);
    jTba.setVisible(false);

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

    String vax = String.format(format, " Cliente", " Recib", " Nota", " F.Not", " F.Ven", " F.Rec", "F.Pago", " D.Pago", " D.Atraso", "   T.Nota $");
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

    txtBus.requestFocus();

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        conPag.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        conPag.dispose();
      }
    });

    Fed = (getAnoHoy() - 1) + "0101";
    txtFed.setText(ymd_dmy(Fed));
    ListaDatos();
  }

  // Selecciona Registro
  public void Seleccionar() {
    int[] x = jList.getSelectedIndices();
    int idx = x.length;
    idx = idx - 1;
    String Ref = String.valueOf(modelo.getElementAt(x[idx]));
    Ref = Ref.trim();
  }

  // Lista Datos
  public void ListaDatos() {
    labMsgPg.setText("");
    fil = txtBus.getText().toUpperCase().trim();

    String vax = " ";
    modelo.clear();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      String sql = "SELECT "
              + "x.nrc, x.coc,(select noc from clientes where x.coc=clientes.coc) noc,"
              + "nno,nfa,fno,fen,fev,fpa,tdo,tdn "
              + "FROM recibocobroD x,recibocobroH y "
              + "where x.nrc=y.nrc "
              + "  and frc between '" + Fed + "' and '" + Feh + "' "
              + "  and (noc like '%" + fil + "%' or x.coc like '%" + fil + "%') "
              + "order by noc,x.nrc,nno";
      tgn = 0;

      rs = st.executeQuery(sql);
      while (rs.next()) {
        nrc = rs.getInt("nrc");
        nno = rs.getInt("nno");
        coc = rs.getString("coc");
        noc = rs.getString("noc");
        fno = rs.getString("fno");
        fen = rs.getString("fen");
        fev = rs.getString("fev");
        fpa = rs.getString("fpa");
        tno = rs.getDouble("tdo");
        tdn = rs.getDouble("tdn");

        if (noc.length() > 40) {
          noc = noc.substring(0, 40);
        }
        noc = noc.toUpperCase();

        tgn = tgn + tno;

        dip = 0;
        dia = 0;

        // Fecha Nota
        String fnx = "";
        if (fno.length() == 8) {
          fnx = ymd_dmyc(fno);
        }

        // Fecha Vence
        String fvx = "";
        if (fev.length() == 8) {
          fvx = ymd_dmyc(fev);
        }

        // Fecha recibe
        String fex = "";
        if (fen.length() == 8) {
          fex = ymd_dmyc(fen);
        }

        // Fecha pago
        String feh = ymdhoy();
        String fpx = "";
        String frx = fno;
        if (fen.length() == 8) {
          frx = fen;
        }
        if (fpa.length() == 8) {
          dip = getdiasFec(fpa, frx);
          fpx = ymd_dmyc(fpa);
        } else {
          dip = getdiasFec(feh, frx);
        }

        int div = 0;
        if (fno.length() == 8 && fev.length() == 8) {
          div = getdiasFec(fev, fno);
        }

        if (dip < 0) {
          dip = 0;
        }

        dia = div - dip;
        if (dia > 0) {
          dia = 0;
        }

        if (fnx.length() == 8) {
          fnx = fnx.substring(0, 5);
        }
        if (fex.length() == 8) {
          fex = fex.substring(0, 5);
        }
        if (fvx.length() == 8) {
          fvx = fvx.substring(0, 5);
        }
        if (fpx.length() == 8) {
          fpx = fpx.substring(0, 5);
        }

        if (tno < 0) {
          dip = 0;
          dia = 0;
        }

        if (dia < 0) {
          dia = dia * -1;
        }

        vax = String.format(format, noc, nrc, nno, fnx, fvx, fex, fpx, dip, dia, MtoEs(tno, 2));
        modelo.addElement(vax);

      }
      jList.setModel(modelo);
      if (!modelo.isEmpty()) {
        //jList.setSelectedIndex(0);
        lanCan.setText("can = " + modelo.size());
      } else {
        labMsgPg.setText("- No Hay Registos -");
      }
      jTne.setText(MtoEs(tgn, 2) + "  ");
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Consulta_PagosClientes.class.getName()).log(Level.SEVERE, null, ex);
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
    jTba = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    txtBus = new javax.swing.JTextField();
    Cliente = new javax.swing.JLabel();
    lanCan = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jTne = new javax.swing.JLabel();
    labMsgPg = new javax.swing.JLabel();
    jLabel10 = new javax.swing.JLabel();
    labRutPdfP = new javax.swing.JLabel();

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
    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/calculos.png"))); // NOI18N
    jLabel3.setText("CUADRE DIAS PAGOS CLIENTES");
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

    jList.setFont(new java.awt.Font("Courier New", 1, 15)); // NOI18N
    jList.setOpaque(false);
    jScrollPane1.setViewportView(jList);

    jTiT.setBackground(new java.awt.Color(0, 102, 102));
    jTiT.setFont(new java.awt.Font("Courier New", 1, 15)); // NOI18N
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

    jTba.setBackground(new java.awt.Color(204, 204, 204));
    jTba.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jTba.setForeground(new java.awt.Color(0, 0, 102));
    jTba.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jTba.setText(" ");
    jTba.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jTba.setOpaque(true);
    jTba.setPreferredSize(new java.awt.Dimension(3, 30));

    jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(0, 51, 102));
    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel4.setText("Total  Base $");

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

    Cliente.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    Cliente.setForeground(new java.awt.Color(51, 51, 102));
    Cliente.setText(" Cliente");

    lanCan.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    lanCan.setForeground(new java.awt.Color(51, 51, 102));
    lanCan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lanCan.setText(" ");
    lanCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(0, 51, 102));
    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel5.setText("Total  Neto $");

    jTne.setBackground(new java.awt.Color(204, 204, 204));
    jTne.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jTne.setForeground(new java.awt.Color(0, 0, 102));
    jTne.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jTne.setText(" ");
    jTne.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jTne.setOpaque(true);
    jTne.setPreferredSize(new java.awt.Dimension(3, 30));

    labMsgPg.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
    labMsgPg.setForeground(new java.awt.Color(204, 0, 0));
    labMsgPg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labMsgPg.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabel10.setBackground(new java.awt.Color(204, 204, 204));
    jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/excel.png"))); // NOI18N
    jLabel10.setText("  Reporte ");
    jLabel10.setToolTipText("Genera Listado en Excel");
    jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
    jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    jLabel10.setOpaque(true);
    jLabel10.setPreferredSize(new java.awt.Dimension(26, 28));
    jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabel10MouseClicked(evt);
      }
    });

    labRutPdfP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labRutPdfP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rutexcel.png"))); // NOI18N
    labRutPdfP.setToolTipText("Ruta Reporte Excel  Cuadre Dias Pagos");
    labRutPdfP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labRutPdfP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labRutPdfP.setOpaque(true);
    labRutPdfP.setPreferredSize(new java.awt.Dimension(43, 30));
    labRutPdfP.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labRutPdfPMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(21, 21, 21)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(btnSal)
                .addGap(18, 18, 18)
                .addComponent(labMsgPg, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(lanCan, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTba, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTne, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
              .addComponent(jScrollPane1)
              .addComponent(jTiT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(16, 16, 16))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(14, 14, 14)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jFed, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(12, 12, 12)
            .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(Cliente)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(labRutPdfP, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jFed, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2))
              .addComponent(jFeh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
          .addComponent(labRutPdfP, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(21, 21, 21)
        .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(btnSal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lanCan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jTba, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jTne, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(labMsgPg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    conPag.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void btnBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusActionPerformed
    ListaDatos();
  }//GEN-LAST:event_btnBusActionPerformed

  private void txtBusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBusMouseClicked
    labMsgPg.setText("");
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

  private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
    ImageIcon icon = new ImageIcon(getClass().getResource("/img/excel.jpg"));
    String vax = "Desea Generar el Excel ?";
    String[] options = {"SI", "NO"};
    int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
    if (opcion == 0) {
      labMsgPg.setText("- Generando Excel -");
      icon = new ImageIcon(getClass().getResource("/img/excel.jpg"));
      String tit = "* AVISO *";
      long tim = 1000;
      Toolkit.getDefaultToolkit().beep();
      vax = "Generando Excel, espere...!";
      Mensaje msg = new Mensaje(vax, tit, tim, icon);
      new Reporte_ExcelPagos(Fed, Feh);
    }
  }//GEN-LAST:event_jLabel10MouseClicked

  private void labRutPdfPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labRutPdfPMouseClicked
    labRutPdfP.setBackground(new java.awt.Color(242, 247, 247));
    String vax = "exc\\";
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
  }//GEN-LAST:event_labRutPdfPMouseClicked

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
      java.util.logging.Logger.getLogger(Consulta_PagosClientes.class
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
        new Consulta_PagosClientes().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Cliente;
  private javax.swing.JButton btnBus;
  private javax.swing.JButton btnSal;
  private com.toedter.calendar.JDateChooser jFed;
  private com.toedter.calendar.JDateChooser jFeh;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JList jList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel jTba;
  private javax.swing.JLabel jTiT;
  private javax.swing.JLabel jTne;
  public static javax.swing.JLabel labMsgPg;
  public static javax.swing.JLabel labRutPdfP;
  private javax.swing.JLabel lanCan;
  private javax.swing.JTextField txtBus;
  private javax.swing.JTextField txtFed;
  private javax.swing.JTextField txtFeh;
  // End of variables declaration//GEN-END:variables
}
