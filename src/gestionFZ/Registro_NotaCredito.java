package gestionFZ;

import comun.Calculadora;
import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.dmy_ymd;
import static comun.MetodosComunes.isNumeric;
import static comun.MetodosComunes.ymdhoy;
import static comun.MetodosComunes.ymdhoyhhmm;
import static gestionFZ.Menu.Calc;
import static gestionFZ.Menu.RegNcr;
import static gestionFZ.Registro_NotaEntrega.conNoe;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import modelo.ConexionSQL;
import modelo.Importadora;

public class Registro_NotaCredito extends javax.swing.JFrame {
  
  int ncr = 0;     // numero nota credito
  int ncx = 0;     // numero nota credito 
  int nne = 0;     // numero nota entrega
  int nfa = 0;     // numero factura
  int nrc = 0;     // numero recibo cobro
  int indok = 0;   // valida campos

  double tnc = 0;  // Total Not/Credito
  double tne = 0;  // Total Not/Entrega
  double poi = 0;  // % iva 
  double pre = 0;  // % Ret iva cliente
  double toi = 0;  // Total Iva
  double tor = 0;  // Total ret Iva

  String coc = ""; // codigo cliente
  String noc = ""; // nombre cliente
  String fnc = ""; // fecha nota credito
  String fre = ""; // fecha  registro
  String fil = ""; // filtro busqueda

  Registro_NotaCredito ctrN;    // defino instancia del controlador

  public static Consulta_NotaCredito conNcr;
  
  public Registro_NotaCredito() {
    initComponents();
    
    ctrN = this; // Inicializo controller

    bloqueaCampos();
    int ncx = getMaxNcr();
    txtNcr.setText("" + ncx);
    txtNcr.requestFocus();
    validaFecNotCred();
    txtNoe.setDisabledTextColor(Color.DARK_GRAY);
    
    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        RegNcr.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        RegNcr.dispose();
      }
    });
    
  }
  
  public void validaFecNotCred() {
    // Fecha Pedido
    jFnc.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().trim().equals("date")) {
          SimpleDateFormat ff = new SimpleDateFormat("dd-MM-yyyy");
          txtFnc.setText(ff.format(jFnc.getCalendar().getTime()));
          fnc = (txtFnc.getText().replaceAll("-", ""));
          if (fnc.length() == 8) {
            fnc = dmy_ymd(fnc).replace("-", "");
            if (fnc.compareTo(ymdhoy()) > 0) {
              jFnc.requestFocus();
              labMsg.setText("- Fecha NC Invalida es mayor a hoy");
              txtFnc.setBorder(new LineBorder(Color.RED));
              txtFnc.requestFocus();
              txtNoe.setEnabled(false);
            } else {
              txtFnc.setBorder(new LineBorder(Color.GRAY));
              labBusNen.setEnabled(true);
              labMsg.setText("- Ingrese Importe Not Credito");
              txtTcr.setEnabled(true);
              txtTiv.setEnabled(true);
              txtTcr.setSelectionStart(0);
              txtTcr.setSelectionEnd(txtTcr.getText().length());
              txtTcr.setEnabled(true);
              txtTcr.requestFocus();
              
            }
          }
        }
      }
    });
  }

  // recibe numero nota Credito
  public void recibeNotaCredito(int nrn) {
    ncr = nrn;
    txtNcr.setText("" + ncr);
    verifNota();
  }

  // recibe numero nota Entrega
  public void recibeNotaEntrega(int nrn) {
    nne = nrn;
    txtNoe.setText("" + nne);
    validNor();
  }

  // Valida Nota Entrega
  public boolean isvalidNotCrd() {
    String mox = txtNcr.getText().trim();
    ncr = 0;
    if (mox.length() < 10 && !mox.equals("0")) {
      if (isNumeric(mox)) {
        mox = mox.replace(".", "");
        ncr = Integer.valueOf(mox);
      }
    }
    if (ncr > 0) {
      Importadora imp = new Importadora();
      int nc1 = imp.getNc1();
      int nc2 = imp.getNc2();
      
      if (ncr < nc1 || ncr > nc2) {
        labMsg.setText("- No NC invalido - Rango = " + nc1 + "-" + nc2);
        txtNcr.setSelectionStart(0);
        txtNcr.setSelectionEnd(txtNcr.getText().length());
        txtNcr.requestFocus();
        return false;
      } else {
        return true;
      }
      
    } else {
      labMsg.setText("- No. Nota Entrega Invalida");
      txtNcr.setSelectionStart(0);
      txtNcr.setSelectionEnd(txtNcr.getText().length());
      txtNcr.requestFocus();
      return false;
    }
  }

  //  notaent (nne,nfa,npe,nfa,npe,coc,noc,fne,fee,tne,tfa,toi,tor,obs)
  public void iniciaNotCrd() {
    txtNcr.setText("");
    bloqueaCampos();
    labMsg.setText("- Ingrese No. Nota de Entrega");
  }
  
  public void bloqueaCampos() {
    txtNoe.setText("");
    labTne.setText("");
    labNfa.setText("");
    labNoc.setText("");
    txtFnc.setText("");
    txtTcr.setText("");
    txtTiv.setText("");
    labRet.setText("");
    labnre.setText("");
    
    txtNoe.setEnabled(false);
    labBusNen.setEnabled(false);
    txtFnc.setEnabled(false);
    txtTcr.setEnabled(false);
    txtTiv.setEnabled(false);
    btnGra.setEnabled(false);
    btnEli.setEnabled(false);
  }
  
  public void desbloqueaCampos() {
    txtNoe.setEnabled(true);
    labBusNen.setEnabled(true);
    jLabTne.setEnabled(true);
    labTne.setEnabled(true);
    //labNfa.setEnabled(true);
    labNoc.setEnabled(true);
    jFnc.setEnabled(true);
    txtFnc.setEnabled(true);
    txtTcr.setEnabled(true);
    txtTiv.setEnabled(true);
    btnGra.setEnabled(true);
    btnEli.setEnabled(true);
  }
  
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel2 = new javax.swing.JLabel();
    jLabNoe = new javax.swing.JLabel();
    labBusNcr = new javax.swing.JLabel();
    txtNcr = new javax.swing.JTextField();
    labMsg = new javax.swing.JLabel();
    btnGra = new javax.swing.JButton();
    btnSal = new javax.swing.JButton();
    btnEli = new javax.swing.JButton();
    jPanel1 = new javax.swing.JPanel();
    jLabFac = new javax.swing.JLabel();
    txtNoe = new javax.swing.JTextField();
    labBusNen = new javax.swing.JLabel();
    jLabTne = new javax.swing.JLabel();
    labTne = new javax.swing.JLabel();
    jLabCte = new javax.swing.JLabel();
    labNoc = new javax.swing.JLabel();
    labojo = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    jLabFne = new javax.swing.JLabel();
    jFnc = new com.toedter.calendar.JDateChooser();
    txtFnc = new javax.swing.JTextField();
    jLabFac1 = new javax.swing.JLabel();
    labNfa = new javax.swing.JLabel();
    jLabFac2 = new javax.swing.JLabel();
    txtTiv = new javax.swing.JTextField();
    jLabret = new javax.swing.JLabel();
    labRet = new javax.swing.JLabel();
    jLabTon6 = new javax.swing.JLabel();
    labnre = new javax.swing.JLabel();
    btnCal = new javax.swing.JButton();
    jLabTon1 = new javax.swing.JLabel();
    txtTcr = new javax.swing.JTextField();
    jLabel1 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    jLabel2.setBackground(new java.awt.Color(217, 226, 226));
    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(0, 0, 153));
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/dev.png"))); // NOI18N
    jLabel2.setText(" REGISTRO  NOTA CREDITO");
    jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jLabel2.setOpaque(true);
    getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 11, 267, 46));

    jLabNoe.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 16)); // NOI18N
    jLabNoe.setForeground(new java.awt.Color(102, 0, 0));
    jLabNoe.setText("Nota / Credito");
    jLabNoe.setToolTipText("No. Not/Entrega");
    jLabNoe.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabNoeMouseClicked(evt);
      }
    });
    getContentPane().add(jLabNoe, new org.netbeans.lib.awtextra.AbsoluteConstraints(331, 18, 139, 30));

    labBusNcr.setBackground(new java.awt.Color(204, 204, 204));
    labBusNcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labBusNcr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    labBusNcr.setToolTipText("Buscar Notas Entrega");
    labBusNcr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labBusNcr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labBusNcr.setOpaque(true);
    labBusNcr.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labBusNcrMouseClicked(evt);
      }
    });
    getContentPane().add(labBusNcr, new org.netbeans.lib.awtextra.AbsoluteConstraints(488, 21, 28, 28));

    txtNcr.setBackground(new java.awt.Color(246, 245, 245));
    txtNcr.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    txtNcr.setForeground(new java.awt.Color(0, 0, 204));
    txtNcr.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtNcr.setText(" ");
    txtNcr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    txtNcr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNcr.setPreferredSize(new java.awt.Dimension(7, 30));
    txtNcr.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNcrMouseClicked(evt);
      }
    });
    txtNcr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNcrActionPerformed(evt);
      }
    });
    txtNcr.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtNcrKeyReleased(evt);
      }
    });
    getContentPane().add(txtNcr, new org.netbeans.lib.awtextra.AbsoluteConstraints(534, 21, 104, -1));

    labMsg.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labMsg.setForeground(new java.awt.Color(204, 0, 0));
    labMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labMsg.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    getContentPane().add(labMsg, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 430, 353, 31));

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnGra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGra.setText("Grabar");
    btnGra.setToolTipText("Grabar Nota de Credito");
    btnGra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnGra.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGraActionPerformed(evt);
      }
    });
    getContentPane().add(btnGra, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 430, -1, 31));

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
      }
    });
    getContentPane().add(btnSal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 89, 30));

    btnEli.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnEli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/elim.png"))); // NOI18N
    btnEli.setMnemonic('E');
    btnEli.setText("Eliminar");
    btnEli.setToolTipText("Eliminar Nota de Credito");
    btnEli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnEli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEliActionPerformed(evt);
      }
    });
    getContentPane().add(btnEli, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 430, 105, 31));

    jPanel1.setBackground(new java.awt.Color(204, 204, 204));
    jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabFac.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 16)); // NOI18N
    jLabFac.setForeground(new java.awt.Color(0, 0, 102));
    jLabFac.setText("Not Entrega");
    jLabFac.setToolTipText("Fecha Documento Not/Entrega");
    jLabFac.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabFacMouseClicked(evt);
      }
    });

    txtNoe.setBackground(new java.awt.Color(255, 248, 248));
    txtNoe.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtNoe.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtNoe.setText(" ");
    txtNoe.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)));
    txtNoe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNoe.setPreferredSize(new java.awt.Dimension(7, 30));
    txtNoe.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNoeMouseClicked(evt);
      }
    });
    txtNoe.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNoeActionPerformed(evt);
      }
    });
    txtNoe.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtNoeKeyReleased(evt);
      }
    });

    labBusNen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labBusNen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    labBusNen.setToolTipText("Buscar Notas Entrega");
    labBusNen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labBusNen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labBusNen.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labBusNenMouseClicked(evt);
      }
    });

    jLabTne.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 16)); // NOI18N
    jLabTne.setForeground(new java.awt.Color(0, 0, 102));
    jLabTne.setText("T.Nota  $");
    jLabTne.setToolTipText("Fecha Documento Not/Entrega");
    jLabTne.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabTneMouseClicked(evt);
      }
    });

    labTne.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labTne.setForeground(new java.awt.Color(51, 51, 51));
    labTne.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labTne.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabCte.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    jLabCte.setForeground(new java.awt.Color(0, 0, 102));
    jLabCte.setText("Cliente");
    jLabCte.setToolTipText("");
    jLabCte.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCteMouseClicked(evt);
      }
    });

    labNoc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labNoc.setForeground(new java.awt.Color(51, 51, 51));
    labNoc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabCte)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(labNoc, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabFac, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(txtNoe, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labBusNen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabTne, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labTne, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 0, Short.MAX_VALUE)))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabFac, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(txtNoe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labBusNen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labTne, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabTne, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabCte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labNoc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(18, Short.MAX_VALUE))
    );

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 70, 723, -1));

    labojo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labojo.setForeground(new java.awt.Color(102, 102, 102));
    labojo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojop.png"))); // NOI18N
    labojo.setToolTipText("Notificacion de Mensaje");
    labojo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    getContentPane().add(labojo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 430, 27, 30));

    jPanel2.setBackground(new java.awt.Color(204, 204, 204));
    jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

    jLabFne.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 16)); // NOI18N
    jLabFne.setForeground(new java.awt.Color(0, 0, 102));
    jLabFne.setText("Fecha NC");
    jLabFne.setToolTipText("Fecha Documento Not/Entrega");
    jLabFne.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabFneMouseClicked(evt);
      }
    });

    jFnc.setForeground(new java.awt.Color(0, 0, 102));
    jFnc.setToolTipText("Seleccione Dia a Procesar");
    jFnc.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
    jFnc.setPreferredSize(new java.awt.Dimension(42, 25));

    txtFnc.setBackground(new java.awt.Color(252, 247, 228));
    txtFnc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtFnc.setForeground(new java.awt.Color(51, 0, 153));
    txtFnc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtFnc.setText(" ");
    txtFnc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    txtFnc.setFocusable(false);
    txtFnc.setPreferredSize(new java.awt.Dimension(10, 25));
    txtFnc.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtFncMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtFncMouseReleased(evt);
      }
    });
    txtFnc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtFncActionPerformed(evt);
      }
    });
    txtFnc.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtFncKeyReleased(evt);
      }
    });

    jLabFac1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 16)); // NOI18N
    jLabFac1.setForeground(new java.awt.Color(0, 0, 102));
    jLabFac1.setText("No. Fact");
    jLabFac1.setToolTipText("Fecha Documento Not/Entrega");
    jLabFac1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabFac1MouseClicked(evt);
      }
    });

    labNfa.setBackground(new java.awt.Color(252, 252, 252));
    labNfa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labNfa.setForeground(new java.awt.Color(102, 0, 0));
    labNfa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labNfa.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabFac2.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 16)); // NOI18N
    jLabFac2.setForeground(new java.awt.Color(0, 0, 102));
    jLabFac2.setText("T.Iva Bs");
    jLabFac2.setToolTipText("Fecha Documento Not/Entrega");
    jLabFac2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabFac2MouseClicked(evt);
      }
    });

    txtTiv.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtTiv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtTiv.setText(" ");
    txtTiv.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    txtTiv.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtTiv.setPreferredSize(new java.awt.Dimension(7, 30));
    txtTiv.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtTivMouseClicked(evt);
      }
    });
    txtTiv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtTivActionPerformed(evt);
      }
    });
    txtTiv.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtTivKeyReleased(evt);
      }
    });

    jLabret.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 16)); // NOI18N
    jLabret.setForeground(new java.awt.Color(0, 0, 102));
    jLabret.setText("T.Ret Iva");
    jLabret.setToolTipText("");
    jLabret.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabretMouseClicked(evt);
      }
    });

    labRet.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labRet.setForeground(new java.awt.Color(51, 51, 51));
    labRet.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labRet.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabTon6.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    jLabTon6.setForeground(new java.awt.Color(0, 0, 102));
    jLabTon6.setText("T. Net Ret25%");
    jLabTon6.setToolTipText("");
    jLabTon6.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabTon6MouseClicked(evt);
      }
    });

    labnre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labnre.setForeground(new java.awt.Color(102, 0, 0));
    labnre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labnre.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    btnCal.setFont(new java.awt.Font("Dialog", 1, 9)); // NOI18N
    btnCal.setForeground(new java.awt.Color(0, 102, 51));
    btnCal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/calc.jpg"))); // NOI18N
    btnCal.setToolTipText("Calculadora");
    btnCal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnCal.setFocusable(false);
    btnCal.setMaximumSize(new java.awt.Dimension(105, 25));
    btnCal.setMinimumSize(new java.awt.Dimension(105, 25));
    btnCal.setPreferredSize(new java.awt.Dimension(105, 25));
    btnCal.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        btnCalMouseClicked(evt);
      }
    });
    btnCal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCalActionPerformed(evt);
      }
    });

    jLabTon1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    jLabTon1.setForeground(new java.awt.Color(0, 0, 102));
    jLabTon1.setText("Total  NC");
    jLabTon1.setToolTipText("");
    jLabTon1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabTon1MouseClicked(evt);
      }
    });

    txtTcr.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtTcr.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtTcr.setText(" ");
    txtTcr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    txtTcr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtTcr.setPreferredSize(new java.awt.Dimension(7, 30));
    txtTcr.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtTcrMouseClicked(evt);
      }
    });
    txtTcr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtTcrActionPerformed(evt);
      }
    });
    txtTcr.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtTcrKeyReleased(evt);
      }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addGap(20, 20, 20)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(jLabFac1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(labNfa, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(56, 56, 56)
            .addComponent(jLabFac2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(53, 53, 53)
            .addComponent(txtTiv, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(jLabFne, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jFnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(6, 6, 6)
            .addComponent(txtFnc, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabret, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labRet, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabTon1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtTcr, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(56, 56, 56)
            .addComponent(jLabTon6)
            .addGap(53, 53, 53)
            .addComponent(labnre, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(32, 32, 32)
            .addComponent(btnCal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(45, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnCal, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabFne, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jFnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(txtFnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabFac1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(labNfa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabFac2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(txtTiv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabret, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabTon6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(labnre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtTcr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabTon1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 730, 190));

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/FondoMain.png"))); // NOI18N
    getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 770, 470));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jLabNoeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabNoeMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabNoeMouseClicked

  private void labBusNcrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labBusNcrMouseClicked
    if (conNcr != null) {
      conNcr.dispose();
    }
    conNcr = new Consulta_NotaCredito(ctrN, "");
    conNcr.setEnabled(true);
    conNcr.setExtendedState(NORMAL);
    conNcr.setVisible(true);
  }//GEN-LAST:event_labBusNcrMouseClicked

  private void txtNcrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNcrMouseClicked
    iniciaNotCrd();
  }//GEN-LAST:event_txtNcrMouseClicked

  private void txtNcrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNcrActionPerformed
    if (isvalidNotCrd()) {
      verifNota();
    }
  }//GEN-LAST:event_txtNcrActionPerformed

  // Veridica si existe nota
  public void verifNota() {
    bloqueaCampos();
    if (buscaNotCrd()) {
      buscaCliente();
      presentaCampos();
      if (buscaReciboCobro()) {
        labMsg.setText("*** Not/Credito Cerrada  -  Rec Cobro  " + nrc + "   *** ");
        btnSal.requestFocus();
      } else {
        desbloqueaCampos();
        txtNoe.setSelectionStart(0);
        txtNoe.setSelectionEnd(txtNoe.getText().length());
        txtNoe.requestFocus();
        txtNoe.setEnabled(false);
      }
    } else {
      // Incluir
      labBusNen.setEnabled(true);
      txtNoe.setEnabled(true);
      labMsg.setText("- Ingrese Nota Entrega Afectada");
      txtNoe.requestFocus();
    }
  }
  
  public void presentaCampos() {
    txtNoe.setEnabled(false);
    txtNoe.setText("" + nne);
    labTne.setText(MtoEs(tne, 2));
    if (nfa == 0) {
      labNfa.setText("- Sin Factura -");
    } else {
      labNfa.setText("" + nfa);
    }
    labNoc.setText(" " + noc);
    txtFnc.setText(fnc);
    txtTcr.setText(MtoEs(tnc, 2));
    txtTiv.setText(MtoEs(toi, 2));
    labRet.setText(MtoEs(tor, 2));
    labnre.setText(MtoEs(toi - tor, 2));
  }
  
  public boolean buscaNENotCrd() {
    
    ncx = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT distinct ncr "
              + "FROM notacred "
              + "where nne = " + nne;
      rs = st.executeQuery(sql);
      while (rs.next()) {
        ncx = rs.getInt("ncr");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (ncx > 0) {
      return true;
    } else {
      return false;
    }
    
  }
  
  public boolean buscaNotCrd() {
    nne = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT ncr,nne,nfa,coc,(select nom from clientes where clientes.coc=notacred.coc) noc,"
              + "fnc,tne,tnc,toi,iva,pre,tor "
              + "FROM notacred "
              + "where ncr =  " + ncr + " ";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        nne = rs.getInt("nne");
        nfa = rs.getInt("nfa");
        coc = rs.getString("coc");
        noc = rs.getString("noc");
        fnc = rs.getString("fnc");
        tne = rs.getDouble("tne");
        tnc = rs.getDouble("tnc");
        toi = rs.getDouble("toi");
        poi = rs.getDouble("iva");
        pre = rs.getDouble("pre");
        tor = rs.getDouble("tor");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (nne > 0) {
      return true;
    } else {
      return false;
    }
  }
  
  public int getMaxNcr() {
    int ncx = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT max(ncr) ncr "
              + "FROM notacred";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        ncx = rs.getInt("ncr");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    return ncx;
  }
  
  public boolean buscaReciboCobro() {
    nrc = 0;
    labMsg.setText("");
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT nrc  "
              + "FROM recibocobroD "
              + "where tpd=1 and nno =  " + ncr + " ";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        nrc = rs.getInt("nrc");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (nrc > 0) {
      txtNoe.setEnabled(false);
      return true;
    } else {
      return false;
    }
  }
  

  private void jLabCteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCteMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCteMouseClicked

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed
    if (validaCampos()) {
      grabarRegistro();
    }
  }//GEN-LAST:event_btnGraActionPerformed
  
  public boolean validaCampos() {
    indok = 0;
    if (indok == 0) {
      
      return true;
    } else {
      return false;
    }
  }
  
  public void grabarRegistro() {
    ImageIcon icon = new ImageIcon(getClass().getResource("/img/salvar.png"));
    String vax = "Desea Grabar Nota de Credito - " + ncr + "  ?";
    String[] options = {"SI", "NO"};
    int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
    if (opcion == 0) {
      
      try {
        
        vax = "";
        //Incluir / Modificar

        ConexionSQL bdsql = new ConexionSQL();
        Connection con = bdsql.Conectar();
        Statement st = con.createStatement();

        // Buscar  
        int can = 0;
        String sql = "SELECT count(*) cant "
                + "FROM notacred "
                + "where ncr =  " + ncr + " ";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
          can = rs.getInt("cant");
        }
        /*
         notacred  
          + "ncr int,          " // nro not/cred
          + "nne int,          " // nro nota entrega
          + "nfa int,          " // nro factura
          + "coc VARCHAR(12),  " // codigo cliente
          + "noc VARCHAR(50),  " // nombre cliente
          + "fnc VARCHAR(08),  " // fecha not/ent
          + "tne decimal(15,2)," // total Nota Entrega
          + "tnc decimal(15,2)," // total Nota Credito
          + "toi double ,      " // total iva
          + "iva double ,      " // % Iva
          + "pre double ,      " // % retencion
          + "tor double ,      " // total retencion
          + "fre VARCHAR(12),  " // fecha registro  
         */
        if (can == 0) {
          // Incluir
          fre = ymdhoyhhmm();
          sql = "Insert into notacred "
                  + "(ncr,nne,nfa,coc,noc,fnc,tne,tnc,toi,iva,pre,tor,fre) "
                  + "VALUES ("
                  + "" + ncr + ","
                  + "" + nne + ","
                  + "" + nfa + ","
                  + "'" + coc + "',"
                  + "'" + noc + "',"
                  + "'" + fnc + "',"
                  + "" + tne + ","
                  + "" + tnc + ","
                  + "" + toi + ","
                  + "" + poi + ","
                  + "" + pre + ","
                  + "" + tor + ","
                  + "'" + fre + "')";
        } else {
          // Modifica
          sql = "update notacred set "
                  + "nne =" + nne + ","
                  + "nfa =" + nfa + ","
                  + "coc ='" + coc + "',"
                  + "noc ='" + noc + "',"
                  + "fnc ='" + fnc + "',"
                  + "tne =" + tne + ","
                  + "tnc =" + tnc + ","
                  + "toi =" + toi + ","
                  + "iva =" + poi + ","
                  + "pre =" + pre + ","
                  + "tor =" + tor + " "
                  + "where ncr =  " + ncr;
        }
        st.execute(sql);
        labMsg.setText("- Se Grabo Nota de Credito -  " + ncr);

        //ListaDatos();
        con.commit();
        con.close();
        
      } catch (SQLException ex) {
        Logger.getLogger(Registro_Banco.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      iniciaNotCrd();
      
    }
  }
  

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    RegNcr.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void jLabFneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabFneMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabFneMouseClicked

  private void txtFncMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFncMouseClicked
    //jDesde.setSelectionStart(0);
    //jDesde.setSelectionEnd(5);
  }//GEN-LAST:event_txtFncMouseClicked

  private void txtFncMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFncMouseReleased

  }//GEN-LAST:event_txtFncMouseReleased

  private void txtFncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFncActionPerformed

  }//GEN-LAST:event_txtFncActionPerformed

  private void txtFncKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFncKeyReleased

  }//GEN-LAST:event_txtFncKeyReleased

  private void jLabFacMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabFacMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabFacMouseClicked

  private void txtNoeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNoeMouseClicked
    txtNoe.setSelectionStart(0);
    txtNoe.setSelectionEnd(txtNoe.getText().length());
    txtNoe.requestFocus();
  }//GEN-LAST:event_txtNoeMouseClicked

  private void txtNoeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoeActionPerformed
    validNor();
  }//GEN-LAST:event_txtNoeActionPerformed
  
  public void validNor() {
    if (isvalidNotEnt()) {
      if (buscaNENotCrd()) {
        labTne.setText("");
        labNfa.setText("");
        labNoc.setText("");
        txtFnc.setText("");
        txtTcr.setText("");
        txtNoe.setEnabled(false);
        labBusNen.setEnabled(true);
        labMsg.setText("- Not/Ent registrada con Not/Cred ( " + ncx + " )");
        txtNoe.setSelectionStart(0);
        txtNoe.setSelectionEnd(txtNoe.getText().length());
        txtNoe.requestFocus();
      } else {
        if (buscaNotEnt()) {
          buscaCliente();
          jFnc.setEnabled(true);
          txtFnc.setEnabled(true);
          txtFnc.setBorder(new LineBorder(Color.BLUE));
          labMsg.setText("- Ingrese Fecha Not Credito");
          jFnc.requestFocus();
        }
      }
    }
  }

  // Busca Nota de Entrega 
  public boolean buscaNotEnt() {
    nfa = 0;
    tne = 0;
    coc = "";
    noc = "";
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT coc,(select nom from clientes where clientes.coc=notaent.coc) noc,nfa,tne "
              + "FROM notaent "
              + "where nne = " + nne + "";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        coc = rs.getString("coc");
        noc = rs.getString("noc");
        nfa = rs.getInt("nfa");
        tne = rs.getDouble("tne");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (tne == 0) {
      labMsg.setText("-  Nota Entega no Existe");
      txtNoe.setSelectionStart(0);
      txtNoe.setSelectionEnd(txtNoe.getText().length());
      txtNoe.requestFocus();
      return false;
      
    } else {
      
      labNfa.setEnabled(true);
      jLabTne.setEnabled(true);
      labTne.setEnabled(true);
      labNoc.setEnabled(true);
      
      labNoc.setText(" " + noc);
      labTne.setText(MtoEs(tne, 2));
      
      if (nfa == 0) {
        labNfa.setText("- Sin Factura -");
      } else {
        labNfa.setText("" + nfa);
      }
      
      labMsg.setText("-  Ingrese importe Nota Credito");
      if (ncr == 0) {
        txtTcr.setText("0");
      }
      txtTcr.setSelectionStart(0);
      txtTcr.setSelectionEnd(txtTcr.getText().length());
      txtTcr.requestFocus();
      return true;
      
    }
  }

  // Valida campo Nota de entrega 
  public boolean isvalidNotEnt() {
    labNfa.setEnabled(false);
    jLabTne.setEnabled(false);
    labTne.setEnabled(false);
    nne = 0;
    String mox = txtNoe.getText().trim();
    if (mox.length() > 0 && !mox.equals("0")) {
      if (isNumeric(mox)) {
        mox = mox.replace(".", "");
        nne = Integer.valueOf(mox);
      }
    }
    if (nne > 0) {
      return true;
    } else {
      labMsg.setText("- Nota Entrega Invalida");
      txtNoe.setSelectionStart(0);
      txtNoe.setSelectionEnd(txtNoe.getText().length());
      txtNoe.requestFocus();
      return false;
    }
  }
  

  private void txtNoeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoeKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNoeKeyReleased

  private void jLabTon1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabTon1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabTon1MouseClicked

  private void txtTcrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTcrMouseClicked
    String mox = txtTcr.getText().trim();
    mox = GetCurrencyDouble(mox);
    txtTcr.setText(mox + "  ");
    txtTcr.setSelectionStart(0);
    txtTcr.setSelectionEnd(txtTcr.getText().length());
    txtTcr.requestFocus();
  }//GEN-LAST:event_txtTcrMouseClicked

  private void txtTcrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTcrActionPerformed
    if (isvalidImpNotCred()) {
      if (nfa > 0) {
        labMsg.setText("- Ingrese total iva");
        txtTiv.setSelectionStart(0);
        txtTiv.setSelectionEnd(txtTiv.getText().length());
        txtTiv.requestFocus();
      } else {
        btnGra.requestFocus();
        labMsg.setText("- Revise antes de Grabar");
      }
      btnGra.setEnabled(true);
    }
  }//GEN-LAST:event_txtTcrActionPerformed

  // Valida Importe Nota Entrega
  public boolean isvalidImpNotCred() {
    tnc = 0;
    String mox = txtTcr.getText().trim();
    if (!isNumeric(mox) && mox.length() > 0 && !mox.equals("0")) {
      mox = mox.replace(".", "");
      mox = mox.replace(",", ".");
    }
    if (isNumeric(mox)) {
      mox = GetCurrencyDouble(mox);
      tnc = GetMtoDouble(mox);
      txtTcr.setText(MtoEs(tnc, 2));
    }
    
    if (tnc > 0) {
      
      if (tnc > tne) {
        labMsg.setText("- Importe mayor a Not Entega");
        txtTcr.setSelectionStart(0);
        txtTcr.setSelectionEnd(txtTcr.getText().length());
        txtTcr.requestFocus();
        return false;
      } else {
        return true;
      }
      
    } else {
      labMsg.setText("- Importe Invalido");
      txtTcr.setSelectionStart(0);
      txtTcr.setSelectionEnd(txtTcr.getText().length());
      txtTcr.requestFocus();
      return false;
    }
  }

  private void txtTcrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTcrKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTcrKeyReleased

  private void btnEliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliActionPerformed
    if (buscaReciboCobro()) {
      labMsg.setText("*** Not/Credito Cerrada  -  Rec Cobro  " + nrc + "   *** ");
      btnSal.requestFocus();
    } else {
      eliminaRegistro();
    }
  }//GEN-LAST:event_btnEliActionPerformed

  private void labBusNenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labBusNenMouseClicked
    if (conNoe != null) {
      conNoe.dispose();
    }
    conNoe = new Consulta_NotaEntrega(ctrN, coc);
    conNoe.setEnabled(true);
    conNoe.setExtendedState(NORMAL);
    conNoe.setVisible(true);
  }//GEN-LAST:event_labBusNenMouseClicked

  private void jLabTneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabTneMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabTneMouseClicked

  private void txtNcrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNcrKeyReleased
    //isvalidNotCrd();
  }//GEN-LAST:event_txtNcrKeyReleased

  private void jLabFac1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabFac1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabFac1MouseClicked

  private void txtTivMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTivMouseClicked
    String mox = txtTiv.getText().trim();
    mox = GetCurrencyDouble(mox);
    txtTiv.setText(mox + "  ");
    txtTiv.setSelectionStart(0);
    txtTiv.setSelectionEnd(txtTiv.getText().length());
    txtTiv.requestFocus();
  }//GEN-LAST:event_txtTivMouseClicked

  private void txtTivActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTivActionPerformed
    if (isvalidImpIvaFac(0)) {
      Importadora imp = new Importadora();
      poi = imp.getIva() / 100;  // % Iva general
      tor = toi * (pre / 100);
      labRet.setText(MtoEs(tor, 2));         // total Retencion Iva
      labnre.setText(MtoEs(toi - tor, 2));   // Total dif retencion
      btnGra.setEnabled(true);
      btnGra.requestFocus();
      labMsg.setText("- Revise bien antes de Grabar");
    }
  }//GEN-LAST:event_txtTivActionPerformed

  // Valida Importe Descuento Nota Entrega
  public boolean isvalidImpIvaFac(int ind) {
    toi = 0;
    String mox = txtTiv.getText().trim();
    if (!isNumeric(mox) && mox.length() > 0) {
      if (ind == 1) {
        mox = mox.replace(".", "");
        mox = mox.replace(",", ".");
      }
    }
    if (isNumeric(mox)) {
      mox = GetCurrencyDouble(mox);
      toi = GetMtoDouble(mox);
      txtTiv.setText(MtoEs(toi, 2));
      return true;
    } else {
      labMsg.setText("- Total Iva Invalido");
      txtTiv.setSelectionStart(0);
      txtTiv.setSelectionEnd(txtTiv.getText().length());
      txtTiv.requestFocus();
      return false;
    }
  }
  

  private void txtTivKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTivKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTivKeyReleased

  private void jLabFac2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabFac2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabFac2MouseClicked

  private void jLabretMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabretMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabretMouseClicked

  private void jLabTon6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabTon6MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabTon6MouseClicked

  private void btnCalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalMouseClicked

  }//GEN-LAST:event_btnCalMouseClicked

  private void btnCalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalActionPerformed
    if (Calc == null) {
      // Calc = new Calculadora();
    } else {
      Calc.dispose();
    }
    Calc = new Calculadora();
    Calc.setVisible(true);
    Calc.setExtendedState(NORMAL);
    Calc.setAlwaysOnTop(true);
  }//GEN-LAST:event_btnCalActionPerformed
  
  public void eliminaRegistro() {
    ImageIcon icon = new ImageIcon(getClass().getResource("/img/elim.png"));
    String vax = "Desea Eliminar Nota de Credito - " + ncr + "  ?";
    String[] options = {"SI", "NO"};
    int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
    if (opcion == 0) {
      try {
        ConexionSQL bdsql = new ConexionSQL();
        Connection con = bdsql.Conectar();
        Statement st = con.createStatement();
        String sql = "delete from notacred "
                + "where ncr =  " + ncr + " ";
        st.execute(sql);
        labMsg.setText(" - Se elimino Nota de Credito  " + ncr + " -");
        st.close();
        con.commit();
        con.close();
      } catch (SQLException ex) {
        Logger.getLogger(Registro_Banco.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      iniciaNotCrd();
    }
  }
  
  public boolean buscaCliente() {
    pre = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT nom,pre "
              + "FROM clientes "
              + "where coc =  '" + coc + "'";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        pre = rs.getDouble("pre");
      }
      if (pre > 0) {
        jLabret.setText(MtoEs(pre, 2).replace(",00", "") + "% Ret Iva");
      } else {
        jLabret.setText("%Ret Iva");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }

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
      java.util.logging.Logger.getLogger(Registro_NotaCredito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Registro_NotaCredito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Registro_NotaCredito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Registro_NotaCredito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        new Registro_NotaCredito().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnCal;
  private javax.swing.JButton btnEli;
  private javax.swing.JButton btnGra;
  private javax.swing.JButton btnSal;
  private com.toedter.calendar.JDateChooser jFnc;
  private javax.swing.JLabel jLabCte;
  private javax.swing.JLabel jLabFac;
  private javax.swing.JLabel jLabFac1;
  private javax.swing.JLabel jLabFac2;
  private javax.swing.JLabel jLabFne;
  private javax.swing.JLabel jLabNoe;
  private javax.swing.JLabel jLabTne;
  private javax.swing.JLabel jLabTon1;
  private javax.swing.JLabel jLabTon6;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabret;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JLabel labBusNcr;
  private javax.swing.JLabel labBusNen;
  private javax.swing.JLabel labMsg;
  private javax.swing.JLabel labNfa;
  private javax.swing.JLabel labNoc;
  private javax.swing.JLabel labRet;
  private javax.swing.JLabel labTne;
  private javax.swing.JLabel labnre;
  private javax.swing.JLabel labojo;
  private javax.swing.JTextField txtFnc;
  private javax.swing.JTextField txtNcr;
  private javax.swing.JTextField txtNoe;
  private javax.swing.JTextField txtTcr;
  private javax.swing.JTextField txtTiv;
  // End of variables declaration//GEN-END:variables
}
