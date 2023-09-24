package gestionFZ;
//
// Registro Importadora
//

import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.isNumeric;
import static comun.MetodosComunes.validaEmail;
import static comun.MetodosComunes.ymd_dmy;
import static gestionFZ.Menu.RegImp;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafx.collections.ObservableList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import modelo.Importadora;

public class Registro_Importadora extends javax.swing.JFrame {

  int indok = 0;
  String rif = "", nom = "", dir = "", tlf = "", eml = "", fec = "", iva = "";
  int dcr = 0;
  int np1 = 0;
  int np2 = 0;
  int nr1 = 0;
  int nr2 = 0;
  int ne1 = 0;
  int ne2 = 0;
  int nc1 = 0;
  int nc2 = 0;
  double ppp = 0, poi = 0;

  ImageIcon icon;

  // Metodo Constructor - Inicializa valores
  public Registro_Importadora() {
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/logosysven.jpg")));
    initComponents();
    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        RegImp.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        RegImp.dispose();
      }
    });
    presentaDatos();
  }

  public void presentaDatos() {

    // ObservableList modelo tabla pedidoD
    ObservableList<Importadora> obsImportadora;
    Importadora i = new Importadora();
    obsImportadora = i.getDatosImportadora();
    for (Importadora imp : obsImportadora) {
      rif = imp.getRif();
      nom = imp.getNom();
      dir = imp.getDir();
      tlf = imp.getTlf();
      eml = imp.getEml();
      fec = imp.getFec();
      ppp = imp.getPor();
      poi = imp.getIva();
      dcr = imp.getDcr();
      np1 = imp.getNp1();
      np2 = imp.getNp2();
      nr1 = imp.getNr1();
      nr2 = imp.getNr2();
      ne1 = imp.getNe1();
      ne2 = imp.getNe2();
      nc1 = imp.getNc1();
      nc2 = imp.getNc2();

      txtRif.setText(rif);
      txtNom.setText(nom);
      txtDir.setText(dir);
      txtTlf.setText(tlf);
      txtEml.setText(eml);

      labFec.setText(ymd_dmy(fec));
      txtPpp.setText(MtoEs(ppp, 2).replace(",00", ""));
      txtIva.setText(MtoEs(poi, 2).replace(",00", ""));
      txtCrd.setText(MtoEs(dcr, 0));
      txtNp1.setText(MtoEs(np1, 0).replace(".", ""));
      txtNp2.setText(MtoEs(np2, 0).replace(".", ""));
      txtRc1.setText(MtoEs(nr1, 0).replace(".", ""));
      txtRc2.setText(MtoEs(nr2, 0).replace(".", ""));
      txtNe1.setText(MtoEs(ne1, 0).replace(".", ""));
      txtNe2.setText(MtoEs(ne2, 0).replace(".", ""));
      txtNc1.setText(MtoEs(nc1, 0).replace(".", ""));
      txtNc2.setText(MtoEs(nc2, 0).replace(".", ""));

    }

    txtRif.requestFocus();

  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    txtPpp = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    jMsg = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    txtDir = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    txtNom = new javax.swing.JTextField();
    jLabel6 = new javax.swing.JLabel();
    labFec = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    txtTlf = new javax.swing.JTextField();
    jLabel8 = new javax.swing.JLabel();
    txtRif = new javax.swing.JTextField();
    jLabel9 = new javax.swing.JLabel();
    txtEml = new javax.swing.JTextField();
    txtIva = new javax.swing.JTextField();
    jLabel14 = new javax.swing.JLabel();
    labojo = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    jLabel12 = new javax.swing.JLabel();
    txtNp1 = new javax.swing.JTextField();
    txtNp2 = new javax.swing.JTextField();
    jLabel15 = new javax.swing.JLabel();
    jLabel13 = new javax.swing.JLabel();
    txtRc1 = new javax.swing.JTextField();
    txtRc2 = new javax.swing.JTextField();
    jLabel11 = new javax.swing.JLabel();
    txtNe1 = new javax.swing.JTextField();
    jLabel16 = new javax.swing.JLabel();
    txtNe2 = new javax.swing.JTextField();
    jLabel19 = new javax.swing.JLabel();
    txtNc1 = new javax.swing.JTextField();
    jLabel20 = new javax.swing.JLabel();
    txtNc2 = new javax.swing.JTextField();
    jLabel17 = new javax.swing.JLabel();
    jLabel18 = new javax.swing.JLabel();
    txtCrd = new javax.swing.JTextField();
    jButton1 = new javax.swing.JButton();
    btnGra = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("UNIDADES");
    getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    txtPpp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtPpp.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtPpp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtPpp.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtPppMouseClicked(evt);
      }
    });
    txtPpp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtPppActionPerformed(evt);
      }
    });

    jLabel3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(51, 51, 102));
    jLabel3.setText("NOMBRE");

    jMsg.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jMsg.setForeground(new java.awt.Color(204, 0, 0));
    jMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jMsg.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabel2.setBackground(new java.awt.Color(217, 226, 226));
    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Import.png"))); // NOI18N
    jLabel2.setText("   REGISTRO - IMPORTADORA");
    jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jLabel2.setOpaque(true);

    jLabel4.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(51, 51, 102));
    jLabel4.setText("FECHA LISTA PRECIOS");

    txtDir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    txtDir.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    txtDir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtDir.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtDirMouseClicked(evt);
      }
    });
    txtDir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtDirActionPerformed(evt);
      }
    });

    jLabel5.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(51, 51, 102));
    jLabel5.setText("DIRECCION");

    txtNom.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtNom.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNom.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNomMouseClicked(evt);
      }
    });
    txtNom.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNomActionPerformed(evt);
      }
    });

    jLabel6.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel6.setForeground(new java.awt.Color(51, 51, 102));
    jLabel6.setText("%  PRONTO PAGO");

    labFec.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labFec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labFec.setText(" ");
    labFec.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabel7.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel7.setForeground(new java.awt.Color(51, 51, 102));
    jLabel7.setText("TELEFONO");

    txtTlf.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtTlf.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtTlf.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtTlfMouseClicked(evt);
      }
    });
    txtTlf.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtTlfActionPerformed(evt);
      }
    });

    jLabel8.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel8.setForeground(new java.awt.Color(51, 51, 102));
    jLabel8.setText("RIF");

    txtRif.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtRif.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtRif.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtRifMouseClicked(evt);
      }
    });
    txtRif.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtRifActionPerformed(evt);
      }
    });

    jLabel9.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel9.setForeground(new java.awt.Color(51, 51, 102));
    jLabel9.setText("EMAIL");

    txtEml.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtEml.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtEml.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtEmlMouseClicked(evt);
      }
    });
    txtEml.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtEmlActionPerformed(evt);
      }
    });

    txtIva.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtIva.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtIva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtIva.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtIvaMouseClicked(evt);
      }
    });
    txtIva.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtIvaActionPerformed(evt);
      }
    });

    jLabel14.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel14.setForeground(new java.awt.Color(51, 51, 102));
    jLabel14.setText("DIAS CREDITO");

    labojo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labojo.setForeground(new java.awt.Color(102, 102, 102));
    labojo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojop.png"))); // NOI18N
    labojo.setToolTipText("Notificacion de Mensaje");
    labojo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));

    jLabel12.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel12.setForeground(new java.awt.Color(102, 0, 0));
    jLabel12.setText("Rango  No Pedidos ");

    txtNp1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtNp1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtNp1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNp1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNp1MouseClicked(evt);
      }
    });
    txtNp1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNp1ActionPerformed(evt);
      }
    });

    txtNp2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtNp2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtNp2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNp2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNp2MouseClicked(evt);
      }
    });
    txtNp2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNp2ActionPerformed(evt);
      }
    });

    jLabel15.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel15.setForeground(new java.awt.Color(51, 51, 102));
    jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel15.setText("al");

    jLabel13.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel13.setForeground(new java.awt.Color(102, 0, 0));
    jLabel13.setText("Rango No Recibo Cobro");

    txtRc1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtRc1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtRc1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtRc1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtRc1MouseClicked(evt);
      }
    });
    txtRc1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtRc1ActionPerformed(evt);
      }
    });

    txtRc2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtRc2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtRc2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtRc2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtRc2MouseClicked(evt);
      }
    });
    txtRc2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtRc2ActionPerformed(evt);
      }
    });

    jLabel11.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel11.setForeground(new java.awt.Color(102, 0, 0));
    jLabel11.setText("Rango  No Not Entrega");

    txtNe1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtNe1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtNe1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNe1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNe1MouseClicked(evt);
      }
    });
    txtNe1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNe1ActionPerformed(evt);
      }
    });

    jLabel16.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel16.setForeground(new java.awt.Color(51, 51, 102));
    jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel16.setText("al");

    txtNe2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtNe2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtNe2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNe2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNe2MouseClicked(evt);
      }
    });
    txtNe2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNe2ActionPerformed(evt);
      }
    });

    jLabel19.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel19.setForeground(new java.awt.Color(102, 0, 0));
    jLabel19.setText("Rango  No Not Credito");

    txtNc1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtNc1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtNc1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNc1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNc1MouseClicked(evt);
      }
    });
    txtNc1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNc1ActionPerformed(evt);
      }
    });

    jLabel20.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel20.setForeground(new java.awt.Color(51, 51, 102));
    jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel20.setText("al");

    txtNc2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtNc2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtNc2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNc2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNc2MouseClicked(evt);
      }
    });
    txtNc2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNc2ActionPerformed(evt);
      }
    });

    jLabel17.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel17.setForeground(new java.awt.Color(51, 51, 102));
    jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel17.setText("al");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addGap(16, 16, 16)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(txtNp1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNp2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(64, 64, 64)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(txtRc1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRc2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(txtNe1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(txtNe2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(67, 67, 67)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(txtNc1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNc2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(8, 8, 8)))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtNp1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtRc1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtNp2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtRc2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(1, 1, 1)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtNe1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtNe2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtNc1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtNc2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jLabel18.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel18.setForeground(new java.awt.Color(51, 51, 102));
    jLabel18.setText("% IVA");

    txtCrd.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    txtCrd.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtCrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtCrd.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtCrdMouseClicked(evt);
      }
    });
    txtCrd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtCrdActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(20, 20, 20)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(txtDir)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(211, 211, 211)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(194, 194, 194)
                    .addComponent(jLabel4))
                  .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addComponent(txtRif, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(37, 37, 37)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(242, 242, 242)
                        .addComponent(labFec, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addComponent(txtTlf, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addComponent(txtPpp, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(31, 31, 31)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                      .addComponent(txtEml, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                          .addComponent(txtIva, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                          .addComponent(txtCrd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(0, 33, Short.MAX_VALUE)))
            .addContainerGap())
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(labojo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
              .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 0, Short.MAX_VALUE))))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(14, 14, 14)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(11, 11, 11)
            .addComponent(labFec, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(txtRif, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtDir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
          .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtTlf, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtEml, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtPpp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtIva, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtCrd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(labojo, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 11, 690, -1));

    jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    jButton1.setText("Salir");
    jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });
    getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 594, 89, -1));

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnGra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGra.setMnemonic('A');
    btnGra.setText("Grabar");
    btnGra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnGra.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGraActionPerformed(evt);
      }
    });
    getContentPane().add(btnGra, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 593, -1, -1));

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/FondoMain.png"))); // NOI18N
    getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 630));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed

    if (validaCampos()) {

      icon = new ImageIcon(getClass().getResource("/img/salvar.png"));
      String vax = "Desea Grabar Registro ?";
      String[] options = {"SI", "NO"};
      int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
      if (opcion == 0) {

        Importadora imp = new Importadora();
        fec = imp.getFecLis();

        // Elimina
        if (imp.eliminarImportadora()) {
          jMsg.setText(" - Se Elimino registro");
        }

        if (rif.length() > 12) {
          rif = rif.substring(0, 12);
        }
        if (nom.length() > 50) {
          nom = nom.substring(0, 50);
        }
        if (dir.length() > 160) {
          dir = dir.substring(0, 160);
        }
        if (tlf.length() > 40) {
          tlf = tlf.substring(0, 40);
        }
        if (eml.length() > 50) {
          eml = eml.substring(0, 40);
        }

        // Incluir
        imp = new Importadora(rif, nom, dir, tlf, eml, fec, ppp, poi, dcr, np1, np2, nr1, nr2, ne1, ne2, nc1, nc2);
        if (imp.insertarImportadora()) {
          jMsg.setText(" - Se grabo con Exito");
        }

      }
    }
  }//GEN-LAST:event_btnGraActionPerformed


  private void txtPppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPppActionPerformed
    txtIva.requestFocus();
  }//GEN-LAST:event_txtPppActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    RegImp.dispose();
  }//GEN-LAST:event_jButton1ActionPerformed

  private void txtPppMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPppMouseClicked
    String mox = txtPpp.getText().trim();
    mox = GetCurrencyDouble(mox);
    txtPpp.setText(mox + "  ");
    txtPpp.setSelectionStart(0);
    txtPpp.setSelectionEnd(txtPpp.getText().length());
  }//GEN-LAST:event_txtPppMouseClicked

  private void txtDirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDirMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDirMouseClicked

  private void txtDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDirActionPerformed
    txtTlf.requestFocus();
  }//GEN-LAST:event_txtDirActionPerformed

  private void txtNomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNomMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNomMouseClicked

  private void txtNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomActionPerformed
    txtDir.requestFocus();
  }//GEN-LAST:event_txtNomActionPerformed

  private void txtTlfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTlfMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTlfMouseClicked

  private void txtTlfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTlfActionPerformed
    txtEml.requestFocus();
  }//GEN-LAST:event_txtTlfActionPerformed

  private void txtRifMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRifMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtRifMouseClicked

  private void txtRifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRifActionPerformed
    txtNom.requestFocus();
  }//GEN-LAST:event_txtRifActionPerformed

  private void txtEmlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtEmlMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtEmlMouseClicked

  private void txtEmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmlActionPerformed
    txtPpp.requestFocus();
  }//GEN-LAST:event_txtEmlActionPerformed

  private void txtRc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRc1ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtRc1ActionPerformed

  private void txtRc1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRc1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtRc1MouseClicked

  private void txtNe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNe1ActionPerformed
    txtRc1.requestFocus();
  }//GEN-LAST:event_txtNe1ActionPerformed

  private void txtNe1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNe1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNe1MouseClicked

  private void txtNp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNp1ActionPerformed
    txtNe1.requestFocus();
  }//GEN-LAST:event_txtNp1ActionPerformed

  private void txtNp1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNp1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNp1MouseClicked

  private void txtIvaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtIvaMouseClicked
    String mox = txtIva.getText().trim();
    mox = GetCurrencyDouble(mox);
    txtIva.setText(mox + "  ");
    txtIva.setSelectionStart(0);
    txtIva.setSelectionEnd(txtIva.getText().length());
  }//GEN-LAST:event_txtIvaMouseClicked

  private void txtIvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIvaActionPerformed
    txtCrd.requestFocus();
  }//GEN-LAST:event_txtIvaActionPerformed

  private void txtNp2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNp2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNp2MouseClicked

  private void txtNp2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNp2ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNp2ActionPerformed

  private void txtNe2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNe2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNe2MouseClicked

  private void txtNe2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNe2ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNe2ActionPerformed

  private void txtRc2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRc2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtRc2MouseClicked

  private void txtRc2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRc2ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtRc2ActionPerformed

  private void txtNc1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNc1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNc1MouseClicked

  private void txtNc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNc1ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNc1ActionPerformed

  private void txtNc2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNc2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNc2MouseClicked

  private void txtNc2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNc2ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNc2ActionPerformed

  private void txtCrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCrdMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCrdMouseClicked

  private void txtCrdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCrdActionPerformed
    txtNp1.requestFocus();
  }//GEN-LAST:event_txtCrdActionPerformed

  public boolean validaCampos() {
    indok = 0;
    rif = txtRif.getText().toUpperCase().trim();
    nom = txtNom.getText().toUpperCase().trim();
    dir = txtDir.getText();
    tlf = txtTlf.getText().trim();
    eml = txtEml.getText().toLowerCase().trim();
    txtNp1.setText(MtoEs(np1, 0).replace(".", ""));

    jMsg.setText("");
    // Valida Rif
    if (rif.length() > 0) {
      indok = 1;
    } else {
      jMsg.setText(" - Debe Ingresar Rif");
      txtRif.requestFocus();
    }
    //Valida Nombre
    if (indok == 1) {
      indok = 0;
      if (nom.length() > 0) {
        indok = 1;
      } else {
        jMsg.setText(" - Debe ingresar Nombre");
        txtNom.requestFocus();
      }
      //Valida Email
      if (eml.length() > 0) {
        indok = 0;
        if (validaEmail(eml)) {
          indok = 1;
        } else {
          jMsg.setText(" - Email Invalido");
          txtEml.requestFocus();
        }
      }

      // Valida porcentaje pronto pago
      if (indok == 1) {
        indok = 0;
        String mox = txtPpp.getText();
        if (isNumeric(mox)) {
          mox = GetCurrencyDouble(mox);
          ppp = GetMtoDouble(mox);
          indok = 1;
        } else {
          jMsg.setText(" - % Descuento PP Invalido");
          txtPpp.setSelectionStart(0);
          txtPpp.setSelectionEnd(txtPpp.getText().length());
          txtPpp.requestFocus();
        }
      }

      // Valida porcentaje Iva
      if (indok == 1) {
        indok = 0;
        String mox = txtIva.getText();
        if (isNumeric(mox)) {
          mox = GetCurrencyDouble(mox);
          poi = GetMtoDouble(mox);
          indok = 1;
        } else {
          jMsg.setText(" - % Iva Invalido");
          txtIva.setSelectionStart(0);
          txtIva.setSelectionEnd(txtIva.getText().length());
          txtIva.requestFocus();
        }
      }

      // Valida Dias Credito
      if (indok == 1) {
        indok = 0;
        String mox = txtCrd.getText();
        if (isNumeric(mox)) {
          dcr = Integer.parseInt(mox);
          indok = 1;
          System.out.println("spm dcr=" + dcr);
        } else {
          jMsg.setText(" - Dias Credito Invalido");
          txtCrd.setSelectionStart(0);
          txtCrd.setSelectionEnd(txtCrd.getText().length());
          txtCrd.requestFocus();
        }
      }

      // Valida numero pedido
      if (indok == 1) {
        indok = 0;
        String mox = txtNp1.getText();
        if (isNumeric(mox)) {
          np1 = Integer.parseInt(mox);
          indok = 1;
        } else {
          jMsg.setText(" - pedido desde Invalido");
          txtNp1.setSelectionStart(0);
          txtNp1.setSelectionEnd(txtNp1.getText().length());
          txtNp1.requestFocus();
        }
      }

      if (indok == 1) {
        indok = 0;
        String mox = txtNp2.getText();
        if (isNumeric(mox)) {
          np2 = Integer.parseInt(mox);
          indok = 1;
        } else {
          jMsg.setText(" - pedido hasta invalido");
          txtNp2.setSelectionStart(0);
          txtNp2.setSelectionEnd(txtNp2.getText().length());
          txtNp2.requestFocus();
        }
      }

      // Valida numero recibo
      if (indok == 1) {
        indok = 0;
        String mox = txtRc1.getText();
        if (isNumeric(mox)) {
          nr1 = Integer.parseInt(mox);
          indok = 1;
        } else {
          jMsg.setText(" - Recibo desde Invalido");
          txtRc1.setSelectionStart(0);
          txtRc1.setSelectionEnd(txtRc1.getText().length());
          txtRc1.requestFocus();
        }
      }
      if (indok == 1) {
        indok = 0;
        String mox = txtRc2.getText();
        if (isNumeric(mox)) {
          nr2 = Integer.parseInt(mox);
          indok = 1;
        } else {
          jMsg.setText(" - recibo hasta invalido");
          txtRc2.setSelectionStart(0);
          txtRc2.setSelectionEnd(txtRc2.getText().length());
          txtRc2.requestFocus();
        }
      }

      // Valida numero not entrega
      if (indok == 1) {
        indok = 0;
        String mox = txtNe1.getText();
        if (isNumeric(mox)) {
          ne1 = Integer.parseInt(mox);
          indok = 1;
        } else {
          jMsg.setText(" - Not Ent desde Invalido");
          txtNe1.setSelectionStart(0);
          txtNe1.setSelectionEnd(txtNe1.getText().length());
          txtNe1.requestFocus();
        }
      }
      if (indok == 1) {
        indok = 0;
        String mox = txtNe2.getText();
        if (isNumeric(mox)) {
          ne2 = Integer.parseInt(mox);
          indok = 1;
        } else {
          jMsg.setText(" - Not Ent hasta Invalido");
          txtNe2.setSelectionStart(0);
          txtNe2.setSelectionEnd(txtNe2.getText().length());
          txtNe2.requestFocus();
        }
      }

      // Valida numero not credito
      if (indok == 1) {
        indok = 0;
        String mox = txtNc1.getText();
        if (isNumeric(mox)) {
          nc1 = Integer.parseInt(mox);
          indok = 1;
        } else {
          jMsg.setText(" - Not Cred desde Invalido");
          txtNc1.setSelectionStart(0);
          txtNc1.setSelectionEnd(txtNc1.getText().length());
          txtNc1.requestFocus();
        }
      }
      if (indok == 1) {
        indok = 0;
        String mox = txtNc2.getText();
        if (isNumeric(mox)) {
          nc2 = Integer.parseInt(mox);
          indok = 1;
        } else {
          jMsg.setText(" - Not Cred hasta Invalido");
          txtNc2.setSelectionStart(0);
          txtNc2.setSelectionEnd(txtNc2.getText().length());
          txtNc2.requestFocus();
        }
      }

    }
    if (indok == 1) {
      return true;
    } else {
      return false;
    }
  }

// Metodo Principal
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
      java.util.logging.Logger.getLogger(Registro_Importadora.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    /*
     /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registro_Importadora().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnGra;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel15;
  private javax.swing.JLabel jLabel16;
  private javax.swing.JLabel jLabel17;
  private javax.swing.JLabel jLabel18;
  private javax.swing.JLabel jLabel19;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel20;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JLabel jMsg;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JLabel labFec;
  private javax.swing.JLabel labojo;
  private javax.swing.JTextField txtCrd;
  private javax.swing.JTextField txtDir;
  private javax.swing.JTextField txtEml;
  private javax.swing.JTextField txtIva;
  private javax.swing.JTextField txtNc1;
  private javax.swing.JTextField txtNc2;
  private javax.swing.JTextField txtNe1;
  private javax.swing.JTextField txtNe2;
  private javax.swing.JTextField txtNom;
  private javax.swing.JTextField txtNp1;
  private javax.swing.JTextField txtNp2;
  private javax.swing.JTextField txtPpp;
  private javax.swing.JTextField txtRc1;
  private javax.swing.JTextField txtRc2;
  private javax.swing.JTextField txtRif;
  private javax.swing.JTextField txtTlf;
  // End of variables declaration//GEN-END:variables
}
