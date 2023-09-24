package gestionFZ;

import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.isNumeric;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import modelo.ConexionSQL;
import modelo.Departamento;
import modelo.EmpaqueProducto;
import static gestionFZ.Registro_CatalogoProductos.regEmp;

public class Registro_EmpaqueProd extends javax.swing.JFrame {

  int indok = 0;
  String cod = "";
  String nom = "";
  String tx1 = "";
  String ref = "";
  String unm = "";
  String fil = "";
  String dep = "";
  String ine = "N";
  String pox = "0";
  int indunv = 0;
  double por = 0;

  ImageIcon icon;
  DefaultTableModel model;

  // Metodo Constructor - Inicializa valores
  public Registro_EmpaqueProd() {
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/logosysven.jpg")));
    initComponents();
    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    txtCod.setBackground(new java.awt.Color(237, 235, 235));
    txtNom.setBackground(new java.awt.Color(237, 235, 235));

    model = (DefaultTableModel) jTabla.getModel();

    jTabla.setRowHeight(25);//tamaño de las celdas
    jTabla.setGridColor(new java.awt.Color(0, 0, 0));
    jTabla.setSelectionBackground(new Color(151, 193, 215));
    jTabla.setSelectionForeground(Color.blue);
    jTabla.getTableHeader().setReorderingAllowed(true);  // activa movimiefnto columnas
    jTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Ajusta tamaño Columnas
    TableColumnModel columnModel = jTabla.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(10);   // Codigo   
    columnModel.getColumn(1).setPreferredWidth(180);  // Empaque
    columnModel.getColumn(2).setPreferredWidth(10);   // unmd
    columnModel.getColumn(3).setPreferredWidth(100);  // desc codigo 
    columnModel.getColumn(4).setPreferredWidth(100);  // Ref precio

    //Alinea Columnas Header
    AlinCampoH(0, 2); // 2=Cent
    AlinCampoH(1, 0); // 0=Izq
    AlinCampoH(2, 2); // 2=Cent
    AlinCampoH(3, 0); // 0=Izq
    AlinCampoH(4, 0); // 0=Izq

    //Alinea Columnas Detail
    AlinCampoD(0, 2); // 2=Cent
    AlinCampoD(1, 0); // 0=Izq
    AlinCampoD(2, 2); // 2=Cent
    AlinCampoD(3, 0); // 0=Izq
    AlinCampoD(4, 0); // 0=Izq

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        regEmp.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        regEmp.dispose();
      }
    });

    txtBus.setEditable(true);
    txtCap.setEnabled(false);
    txtBus.requestFocus();
    cargaDepartamentos();
    cargaUnm();

  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    jTabla = new javax.swing.JTable();
    jPanel1 = new javax.swing.JPanel();
    txtBus = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    txtNom = new javax.swing.JTextField();
    txtCap = new javax.swing.JTextField();
    jLabel6 = new javax.swing.JLabel();
    txtCod = new javax.swing.JTextField();
    btnBus = new javax.swing.JButton();
    btnGra = new javax.swing.JButton();
    cbDep = new javax.swing.JComboBox();
    jLabel7 = new javax.swing.JLabel();
    cbExi = new javax.swing.JCheckBox();
    jLabel12 = new javax.swing.JLabel();
    jLabUnm = new javax.swing.JLabel();
    jLabel8 = new javax.swing.JLabel();
    txtRef = new javax.swing.JTextField();
    cbUnm = new javax.swing.JComboBox();
    jButton1 = new javax.swing.JButton();
    labCan = new javax.swing.JLabel();
    txtMsg = new javax.swing.JLabel();
    labojo = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("EMPAQUE PRODUCTOS");

    jTabla.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jTabla.setForeground(new java.awt.Color(51, 51, 51));
    jTabla.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "CODIGO", "EMPAQUE", "UNID MEDIDA", "DESCRIPCION", "Referencia"
      }
    ) {
      boolean[] canEdit = new boolean [] {
        false, false, false, false, false
      };

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    jTabla.setRowHeight(25);
    jTabla.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jTablaMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(jTabla);
    jTabla.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    if (jTabla.getColumnModel().getColumnCount() > 0) {
      jTabla.getColumnModel().getColumn(0).setResizable(false);
      jTabla.getColumnModel().getColumn(0).setPreferredWidth(8);
      jTabla.getColumnModel().getColumn(1).setResizable(false);
      jTabla.getColumnModel().getColumn(1).setPreferredWidth(80);
      jTabla.getColumnModel().getColumn(2).setResizable(false);
      jTabla.getColumnModel().getColumn(2).setPreferredWidth(8);
      jTabla.getColumnModel().getColumn(3).setResizable(false);
      jTabla.getColumnModel().getColumn(3).setPreferredWidth(30);
      jTabla.getColumnModel().getColumn(4).setResizable(false);
      jTabla.getColumnModel().getColumn(4).setPreferredWidth(100);
    }

    jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    txtBus.setBackground(new java.awt.Color(249, 249, 249));
    txtBus.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtBus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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
      public void keyPressed(java.awt.event.KeyEvent evt) {
        txtBusKeyPressed(evt);
      }
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtBusKeyReleased(evt);
      }
    });

    jLabel3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(51, 51, 102));
    jLabel3.setText("CODIGO");

    jLabel2.setBackground(new java.awt.Color(217, 226, 226));
    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/empaque.jpg"))); // NOI18N
    jLabel2.setText("  EMPAQUE PRODUCTOS   ( Catalogo de Precios )");
    jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jLabel2.setOpaque(true);

    jLabel4.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(51, 51, 102));
    jLabel4.setText("DESCRIPCON EMPAQUE");

    txtNom.setEditable(false);
    txtNom.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
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

    txtCap.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtCap.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtCapMouseClicked(evt);
      }
    });
    txtCap.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtCapActionPerformed(evt);
      }
    });

    jLabel6.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel6.setForeground(new java.awt.Color(51, 51, 102));
    jLabel6.setText("BUSCAR PRODUCTO");

    txtCod.setEditable(false);
    txtCod.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtCod.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtCod.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtCodMouseClicked(evt);
      }
    });
    txtCod.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtCodActionPerformed(evt);
      }
    });

    btnBus.setBackground(new java.awt.Color(204, 204, 204));
    btnBus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    btnBus.setMnemonic('B');
    btnBus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnBus.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnBusActionPerformed(evt);
      }
    });

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    btnGra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGra.setMnemonic('A');
    btnGra.setText("Grabar");
    btnGra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnGra.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGraActionPerformed(evt);
      }
    });

    cbDep.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    cbDep.setMaximumRowCount(20);
    cbDep.setPreferredSize(new java.awt.Dimension(35, 30));
    cbDep.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbDepItemStateChanged(evt);
      }
    });
    cbDep.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbDepActionPerformed(evt);
      }
    });

    jLabel7.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel7.setForeground(new java.awt.Color(51, 51, 102));
    jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel7.setText("DEP");
    jLabel7.setToolTipText("Departamento");

    cbExi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbExi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbExiActionPerformed(evt);
      }
    });

    jLabel12.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel12.setForeground(new java.awt.Color(51, 51, 102));
    jLabel12.setText("Sin Empaque");
    jLabel12.setToolTipText("Buscar articulos sin Empaque");

    jLabUnm.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    jLabUnm.setForeground(new java.awt.Color(0, 0, 102));
    jLabUnm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabUnm.setText("Unid Med Emp");
    jLabUnm.setToolTipText("");
    jLabUnm.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabUnmMouseClicked(evt);
      }
    });

    jLabel8.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel8.setForeground(new java.awt.Color(51, 51, 102));
    jLabel8.setText("OBSERVACION P.U.");

    txtRef.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtRef.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    txtRef.setText(" ");
    txtRef.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtRef.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtRef.setPreferredSize(new java.awt.Dimension(7, 30));
    txtRef.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtRefMouseClicked(evt);
      }
    });
    txtRef.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtRefActionPerformed(evt);
      }
    });
    txtRef.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtRefKeyReleased(evt);
      }
    });

    cbUnm.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    cbUnm.setMaximumRowCount(20);
    cbUnm.setPreferredSize(new java.awt.Dimension(35, 30));
    cbUnm.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbUnmItemStateChanged(evt);
      }
    });
    cbUnm.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbUnmActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
            .addComponent(txtCap, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabUnm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGap(18, 18, 18)
            .addComponent(cbUnm, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(50, 50, 50)
            .addComponent(btnGra, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(34, 34, 34))
          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(txtNom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 768, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))
                  .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(cbDep, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(151, 151, 151)
                    .addComponent(jLabel12)
                    .addGap(18, 18, 18)
                    .addComponent(cbExi))))
              .addComponent(txtRef, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 0, Short.MAX_VALUE)))
        .addGap(24, 24, 24))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(11, 11, 11)
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(19, 19, 19)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(cbExi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(cbDep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addGap(8, 8, 8)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtCap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnGra, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabUnm, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(cbUnm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(txtRef, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    jButton1.setText("Salir");
    jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    labCan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labCan.setForeground(new java.awt.Color(0, 0, 102));
    labCan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labCan.setText(" ");
    labCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labCan.setPreferredSize(new java.awt.Dimension(8, 25));

    txtMsg.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtMsg.setForeground(new java.awt.Color(204, 0, 0));
    txtMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    txtMsg.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    labojo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labojo.setForeground(new java.awt.Color(102, 102, 102));
    labojo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojop.png"))); // NOI18N
    labojo.setToolTipText("Notificacion de Mensaje");
    labojo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(labojo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(103, 103, 103)
            .addComponent(labCan, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(113, 113, 113))
          .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jScrollPane1))
        .addContainerGap(15, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(txtMsg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
          .addComponent(labojo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
          .addComponent(labCan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaMouseClicked
    presenta();
  }//GEN-LAST:event_jTablaMouseClicked

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed
    cod = txtCod.getText().toUpperCase().trim();
    tx1 = txtCap.getText().toUpperCase().trim();
    ref = txtRef.getText().toUpperCase().trim();
    pox = "0";
    por = 0;
    if (isNumeric(pox)) {
      por = Double.valueOf(pox);
    }

    if (validaCampos() == true) {
      //Incluir / Modificar
      icon = new ImageIcon(getClass().getResource("/img/salvar.png"));
      String vax = "Desea Grabar \n( " + nom + " ) ?";
      String[] options = {"SI", "NO"};
      int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
      if (opcion == 0) {
        // Borra registro
        int c = 0;
        EmpaqueProducto e = new EmpaqueProducto();
        if (e.existeProducto(cod)) {
          c = 1;
        }
        por = 0;
        e = new EmpaqueProducto(cod, tx1, ref, unm, por, 1);
        if (c == 0) {
          if (e.insertarEmpaque()) {
            txtMsg.setText("- Se Creo registro -");
          }
        } else {
          if (e.modificarEmpaque(cod)) {
            txtMsg.setText("- Se modifico registro -");
          }
        }
        int row = jTabla.getSelectedRow();    // Fila Selecciuonada
        int col = jTabla.getSelectedColumn(); // Columna Seleccionada
        if ((row != -1) && (col != -1)) {
          model.setValueAt(tx1, jTabla.getSelectedRow(), 1);
          model.setValueAt(unm, jTabla.getSelectedRow(), 2);
          model.setValueAt(ref, jTabla.getSelectedRow(), 4);
          //model.setValueAt(MtoEs(por, 2).replace(",00", ""), jTabla.getSelectedRow(), 5);
        }
        IniciaCampos();
      }
    }
  }//GEN-LAST:event_btnGraActionPerformed

  private void txtBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusActionPerformed
    btnBus.doClick();
  }//GEN-LAST:event_txtBusActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    regEmp.dispose();
  }//GEN-LAST:event_jButton1ActionPerformed

  private void txtBusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBusMouseClicked
    IniciaCampos();
    cbDep.setSelectedIndex(-1);
    model.setRowCount(0);
  }//GEN-LAST:event_txtBusMouseClicked

  private void txtNomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNomMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNomMouseClicked

  private void txtNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomActionPerformed
    txtCap.requestFocus();
  }//GEN-LAST:event_txtNomActionPerformed

  private void txtCapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCapMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCapMouseClicked

  private void txtCapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCapActionPerformed
    txtRef.requestFocus();
  }//GEN-LAST:event_txtCapActionPerformed

  private void txtCodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCodMouseClicked

  private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed

  }//GEN-LAST:event_txtCodActionPerformed

  private void txtBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusKeyReleased
    //fil = txtBus.getText().toUpperCase().trim();
    //ListaDatos();
  }//GEN-LAST:event_txtBusKeyReleased

  private void btnBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusActionPerformed
    fil = txtBus.getText().toUpperCase().trim();
    ListaDatos();
    txtBus.requestFocus();
  }//GEN-LAST:event_btnBusActionPerformed

  private void txtBusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusKeyPressed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtBusKeyPressed

  private void cbDepItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbDepItemStateChanged
    dep = "";
    int idx = cbDep.getSelectedIndex();
    if (idx >= 0 && evt.getSource() == cbDep && evt.getStateChange() == 1) {
      String str = (String) cbDep.getSelectedItem().toString();
      if (str != null) {
        if (indunv == 1) {
          if (str.length() > 0) {
            str = str.trim();
            dep = str;
          }
          txtBus.setText("");
          fil = "";
          if (idx > 0) {
            ListaDatos();
          } else {
            IniciaCampos();
            model.setRowCount(0);
          }

        } else {
          indunv = 1;
        }
      }
    }
  }//GEN-LAST:event_cbDepItemStateChanged

  private void cbDepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDepActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbDepActionPerformed

  private void cbExiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbExiActionPerformed
    if (cbExi.isSelected() == true) {
      ine = "S";
    } else {
      ine = "N";
    }
    ListaDatos();
  }//GEN-LAST:event_cbExiActionPerformed

  private void jLabUnmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabUnmMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabUnmMouseClicked

  private void txtRefMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRefMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtRefMouseClicked

  private void txtRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRefActionPerformed
    btnGra.requestFocus();
  }//GEN-LAST:event_txtRefActionPerformed

  private void txtRefKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRefKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtRefKeyReleased

  private void cbUnmItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbUnmItemStateChanged
    int idx = cbUnm.getSelectedIndex();
    if (idx >= 0 && evt.getSource() == cbUnm && evt.getStateChange() == 1) {
      String str = (String) cbUnm.getSelectedItem().toString();
      if (str != null) {
        if (str.length() > 0) {
          str = str.trim();
          unm = str;
          txtRef.requestFocus();
        }
      }
    }
  }//GEN-LAST:event_cbUnmItemStateChanged

  private void cbUnmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbUnmActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbUnmActionPerformed

  // Alinea Campos Columnas Header
  private void AlinCampoH(int col, int ind) {
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
    DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
    if (ind == 0) {
      cellRenderer.setHorizontalAlignment(JLabel.LEFT);
      headerRenderer.setHorizontalAlignment(JLabel.LEFT);
    }
    if (ind == 1) {
      cellRenderer.setHorizontalAlignment(JLabel.RIGHT);
      headerRenderer.setHorizontalAlignment(JLabel.RIGHT);
    }
    if (ind == 2) {
      cellRenderer.setHorizontalAlignment(JLabel.CENTER);
      headerRenderer.setHorizontalAlignment(JLabel.CENTER);
    }
    headerRenderer.setBackground(new java.awt.Color(0, 153, 140));
    headerRenderer.setForeground(Color.WHITE);
    jTabla.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
    jTabla.getColumnModel().getColumn(col).setHeaderRenderer(headerRenderer);
  }

  // Alinea Campos Columnas Detalle
  private void AlinCampoD(int col, int ind) {
    DefaultTableCellRenderer Alinea = new DefaultTableCellRenderer();
    if (ind == 1) {
      Alinea.setHorizontalAlignment(SwingConstants.RIGHT);
    }
    if (ind == 2) {
      Alinea.setHorizontalAlignment(SwingConstants.CENTER);
    }
    jTabla.getColumnModel().getColumn(col).setCellRenderer(Alinea);
  }

  // Presenta Datos al pulsar mouse en detalle columna
  public void presenta() {
    txtMsg.setText("");
    txtCap.setEnabled(false);
    cod = model.getValueAt(jTabla.getSelectedRow(), 0).toString();
    tx1 = model.getValueAt(jTabla.getSelectedRow(), 1).toString();
    unm = model.getValueAt(jTabla.getSelectedRow(), 2).toString();
    nom = model.getValueAt(jTabla.getSelectedRow(), 3).toString();
    ref = model.getValueAt(jTabla.getSelectedRow(), 4).toString();
    int[] sel = jTabla.getSelectedRows();
    if (sel.length == 1) {
      txtCod.setText(cod);
      txtNom.setText(nom);
      txtCap.setText(tx1);
      txtRef.setText(ref);
      cbUnm.setSelectedItem(unm);
      txtCap.requestFocus();
      txtCap.setEnabled(true);
      cbUnm.setEnabled(true);
      txtRef.setEnabled(true);
    } else {
      IniciaCampos();
    }
  }

  // Inicia Campos
  public void IniciaCampos() {
    cod = "";
    nom = "";
    tx1 = "";
    unm = "";
    ref = "";
    txtBus.setText("");
    txtCod.setText("");
    txtNom.setText("");
    txtCap.setText("");
    txtRef.setText("");
    txtCap.setEnabled(false);
    cbUnm.setEnabled(false);
    txtRef.setEnabled(false);
    cbUnm.setEnabled(false);
    txtBus.requestFocus();
  }

  public void ListaDatos() {
    model.setRowCount(0);
    int can = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      Statement st2 = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT cop,nom "
              + "FROM listaprecios "
              + "where sta='0'"
              + "  and dep like '%" + dep + "%'"
              + "  and (nom like '%" + fil + "%' or cop like '%" + fil + "%')"
              + "order by dep,cop";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        cod = rs.getString("cop");
        nom = rs.getString("nom");
        tx1 = "";
        String sql2 = "SELECT tx1,unm,ref,por "
                + "FROM empaqueproducto "
                + "where cop='" + cod + "'";
        ResultSet rs2 = st2.executeQuery(sql2);
        while (rs2.next()) {
          tx1 = rs2.getString("tx1");
          ref = rs2.getString("ref");
          unm = rs2.getString("unm");
          por = rs2.getDouble("por");
        }
        if (ine.equals("N")) {
            model.addRow(new Object[]{cod, tx1, unm, nom, ref, MtoEs(por, 2).replace(",00", "")});
          can++;
        } else {
          if (tx1.length() == 0) {
            model.addRow(new Object[]{cod, tx1, unm, nom, ref, MtoEs(por, 2).replace(",00", "")});
            can++;
          }
        }
      }
      labCan.setText("c = " + can);
      if (can == 0) {
        txtMsg.setText(" - No hay conincidencias -");
      }
      txtCap.setEnabled(false);
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_EmpaqueProd.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public boolean validaCampos() {
    indok = 0;
    txtMsg.setText("");
    // Valida capacidad
    if (tx1.length() <= 40) {
      indok = 1;
    } else {
      txtMsg.setText(" - Maximo 40 Caracteres");
      txtCap.requestFocus();
    }
    // Valida refer
    if (ref.length() <= 20) {
      indok = 1;
    } else {
      indok = 0;
      txtMsg.setText(" - Maximo 20 Caracteres");
      txtRef.requestFocus();
    }
    // Valida unidad medida
    if (unm.length() > 0) {
      indok = 1;
    } else {
      indok = 0;
      txtMsg.setText("- Ingresar Unid medida Empaque");
      cbUnm.requestFocus();
    }

    if (indok == 1) {
      return true;
    } else {
      return false;
    }
  }

  public void cargaDepartamentos() {
    // ObservableList modelo tabla departamento
    ObservableList<Departamento> obsDep;
    Departamento d = new Departamento();
    obsDep = d.getDepartamento();
    cbDep.removeAllItems();
    cbDep.addItem("");
    for (Departamento dep : obsDep) {
      cbDep.addItem(dep.getDep());
    }
  }

  public void cargaUnm() {
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {

      cbUnm.removeAllItems();
      cbUnm.addItem("");

      Statement st = con.createStatement();
      String sql = "SELECT unm from unidmed "
              + "order by sec";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        cbUnm.addItem(rs.getString("unm"));
      }
      cbUnm.setSelectedIndex(-1);
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
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
      java.util.logging.Logger.getLogger(Registro_EmpaqueProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    /*
     /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registro_EmpaqueProd().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnBus;
  private javax.swing.JButton btnGra;
  private javax.swing.JComboBox cbDep;
  private javax.swing.JCheckBox cbExi;
  private javax.swing.JComboBox cbUnm;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabUnm;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  public javax.swing.JTable jTabla;
  private javax.swing.JLabel labCan;
  private javax.swing.JLabel labojo;
  private javax.swing.JTextField txtBus;
  private javax.swing.JTextField txtCap;
  private javax.swing.JTextField txtCod;
  private javax.swing.JLabel txtMsg;
  private javax.swing.JTextField txtNom;
  private javax.swing.JTextField txtRef;
  // End of variables declaration//GEN-END:variables
}
