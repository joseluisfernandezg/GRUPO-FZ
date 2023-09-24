package gestionFZ;

import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.dmy_ymd;
import static comun.MetodosComunes.isNumeric;
import static comun.MetodosComunes.validaEmail;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymdhoy;
import static gestionFZ.Menu.RegCte;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
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
import modelo.Clientes;

public class Registro_Clientes extends javax.swing.JFrame {

  int indok = 0;
  String cod = "", rif = "", nom = "", dir = "", edo = "", ciu = "", tlf = "";
  String fei = "", act = "0", ret = "", dcc = "", fil = "";
  String cto = "", eml = "", fac = "0", tip = "1", cop = "1", sta = "0";
  double pre = 0, dic = 0;

  ImageIcon icon;
  DefaultTableModel model;

  ObservableList<Clientes> obsClientes;  // ObservableList modelo tabla Clientes

  // Metodo Constructor - Inicializa valores
  public Registro_Clientes() {

    initComponents();
    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/logosysven.jpg")));

    model = (DefaultTableModel) tabCli.getModel();

    tabCli.setRowHeight(25);//tamaño de las celdas
    tabCli.setGridColor(new java.awt.Color(0, 0, 0));
    tabCli.setSelectionBackground(new Color(151, 193, 215));
    tabCli.setSelectionForeground(Color.blue);
    tabCli.getTableHeader().setReorderingAllowed(true);  // activa movimiefnto columnas
    tabCli.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tabCli.setAutoCreateRowSorter(true); // sorting of the rows on a particular column

    // Ajusta tamaño Columnas
    TableColumnModel columnModel = tabCli.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(3);   // Tipo
    columnModel.getColumn(1).setPreferredWidth(15);  // Codigo
    columnModel.getColumn(2).setPreferredWidth(240); // Nombre  
    columnModel.getColumn(3).setPreferredWidth(40);  // Telefono
    columnModel.getColumn(4).setPreferredWidth(55);  // Contacto
    columnModel.getColumn(5).setPreferredWidth(3);   // dias cred
    columnModel.getColumn(6).setPreferredWidth(3);   // ret iva

    //Alinea Columnas Header
    AlinCampoH(0, 2); // 2=Center 0 = IZq
    AlinCampoH(1, 0);
    AlinCampoH(2, 0);
    AlinCampoH(3, 0);
    AlinCampoH(4, 0);
    AlinCampoH(5, 2);
    AlinCampoH(6, 2);

    //Alinea Columnas Detail
    AlinCampoD(0, 2);
    AlinCampoD(1, 0);
    AlinCampoD(2, 0);
    AlinCampoD(3, 0);
    AlinCampoD(4, 0);
    AlinCampoD(5, 2);
    AlinCampoD(6, 2);

    fei = ymdhoy();
    jFin.setFocusable(true);
    jFin.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        fei = "";
        if (e.getPropertyName().trim().equals("date")) {
          SimpleDateFormat ff = new SimpleDateFormat("dd-MM-yyyy");
          txtFin.setText(ff.format(jFin.getCalendar().getTime()));
          fei = (txtFin.getText().replaceAll("-", ""));
          if (fei.length() == 8) {
            fei = dmy_ymd(fei).replace("-", "");
          }
        }
      }
    });

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        RegCte.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        RegCte.dispose();
      }
    });
    bloqueaCampos();
    listaObsList();
    txtBus.setText(" ");
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    tabCli = new javax.swing.JTable();
    jPanel1 = new javax.swing.JPanel();
    txtCod = new javax.swing.JTextField();
    jLabel1 = new javax.swing.JLabel();
    txtNom = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    txtDir = new javax.swing.JTextField();
    txtTlf = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    txtCon = new javax.swing.JTextField();
    jLabel7 = new javax.swing.JLabel();
    cbTip = new javax.swing.JComboBox();
    jLabel8 = new javax.swing.JLabel();
    cbCop = new javax.swing.JComboBox();
    txtEml = new javax.swing.JTextField();
    jLabel9 = new javax.swing.JLabel();
    jLabel10 = new javax.swing.JLabel();
    jFin = new com.toedter.calendar.JDateChooser();
    txtFin = new javax.swing.JTextField();
    jLabel12 = new javax.swing.JLabel();
    cbFac = new javax.swing.JCheckBox();
    txtBus = new javax.swing.JTextField();
    jLabel13 = new javax.swing.JLabel();
    cbSta = new javax.swing.JComboBox();
    jLabel14 = new javax.swing.JLabel();
    jLabel15 = new javax.swing.JLabel();
    txtCiu = new javax.swing.JTextField();
    jLabel16 = new javax.swing.JLabel();
    txtEdo = new javax.swing.JTextField();
    txtRif = new javax.swing.JTextField();
    jLabel17 = new javax.swing.JLabel();
    jLabel18 = new javax.swing.JLabel();
    txtRet = new javax.swing.JTextField();
    btnNvo = new javax.swing.JButton();
    txtDic = new javax.swing.JTextField();
    jLabel20 = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();
    btnEli = new javax.swing.JButton();
    btnGra = new javax.swing.JButton();
    jLabel11 = new javax.swing.JLabel();
    cbAct = new javax.swing.JComboBox();
    labCan = new javax.swing.JLabel();
    txtMsg = new javax.swing.JLabel();
    labojo = new javax.swing.JLabel();
    jLabel19 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("CLIENTES");
    getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    tabCli.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    tabCli.setForeground(new java.awt.Color(51, 51, 51));
    tabCli.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "TIPO", "CODIGO", "NOMBRE", "TELEFONO", "CONTACTO", "DIAS CR", "%RET IVA"
      }
    ) {
      boolean[] canEdit = new boolean [] {
        false, false, false, false, false, false, false
      };

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    tabCli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    tabCli.setRowHeight(25);
    tabCli.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tabCliMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(tabCli);
    tabCli.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    if (tabCli.getColumnModel().getColumnCount() > 0) {
      tabCli.getColumnModel().getColumn(0).setResizable(false);
      tabCli.getColumnModel().getColumn(0).setPreferredWidth(4);
      tabCli.getColumnModel().getColumn(1).setResizable(false);
      tabCli.getColumnModel().getColumn(1).setPreferredWidth(10);
      tabCli.getColumnModel().getColumn(2).setResizable(false);
      tabCli.getColumnModel().getColumn(2).setPreferredWidth(200);
      tabCli.getColumnModel().getColumn(3).setResizable(false);
      tabCli.getColumnModel().getColumn(3).setPreferredWidth(25);
      tabCli.getColumnModel().getColumn(4).setResizable(false);
      tabCli.getColumnModel().getColumn(4).setPreferredWidth(40);
      tabCli.getColumnModel().getColumn(5).setResizable(false);
      tabCli.getColumnModel().getColumn(5).setPreferredWidth(4);
      tabCli.getColumnModel().getColumn(6).setResizable(false);
      tabCli.getColumnModel().getColumn(6).setPreferredWidth(5);
    }

    getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 292, 1009, 275));

    jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    txtCod.setEditable(false);
    txtCod.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtCod.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtCod.setText(" ");
    txtCod.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtCod.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtCod.setPreferredSize(new java.awt.Dimension(7, 30));
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

    jLabel1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel1.setForeground(new java.awt.Color(51, 51, 102));
    jLabel1.setText("CODIGO");

    txtNom.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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

    jLabel3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(51, 51, 102));
    jLabel3.setText("NOMBRE");

    jLabel2.setBackground(new java.awt.Color(217, 226, 226));
    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(0, 0, 102));
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Clte.png"))); // NOI18N
    jLabel2.setText("   REGISTRO - CLIENTES");
    jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jLabel2.setOpaque(true);

    jLabel4.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(51, 51, 102));
    jLabel4.setText("DIRECCION");

    txtDir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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

    txtTlf.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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

    jLabel5.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(51, 51, 102));
    jLabel5.setText("TELEFONOS");

    jLabel6.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel6.setForeground(new java.awt.Color(51, 51, 102));
    jLabel6.setText("CONTACTO");

    txtCon.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtCon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtCon.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtConMouseClicked(evt);
      }
    });
    txtCon.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtConActionPerformed(evt);
      }
    });

    jLabel7.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel7.setForeground(new java.awt.Color(51, 51, 102));
    jLabel7.setText("TIP CLIENTE");

    cbTip.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    cbTip.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DETAL", "MAYORISTA" }));
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

    jLabel8.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel8.setForeground(new java.awt.Color(51, 51, 102));
    jLabel8.setText("COND PAGO");

    cbCop.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    cbCop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PREPAGADO", "CREDITO" }));
    cbCop.setPreferredSize(new java.awt.Dimension(35, 28));
    cbCop.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbCopItemStateChanged(evt);
      }
    });
    cbCop.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbCopActionPerformed(evt);
      }
    });

    txtEml.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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

    jLabel9.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel9.setForeground(new java.awt.Color(51, 51, 102));
    jLabel9.setText("EMAIL");

    jLabel10.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel10.setForeground(new java.awt.Color(51, 51, 102));
    jLabel10.setText("F. INGRESO");

    jFin.setForeground(new java.awt.Color(0, 0, 102));
    jFin.setToolTipText("Seleccione Dia a Procesar");
    jFin.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
    jFin.setPreferredSize(new java.awt.Dimension(42, 25));

    txtFin.setBackground(new java.awt.Color(252, 247, 228));
    txtFin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtFin.setForeground(new java.awt.Color(51, 51, 51));
    txtFin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtFin.setText(" ");
    txtFin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    txtFin.setFocusable(false);
    txtFin.setPreferredSize(new java.awt.Dimension(10, 25));
    txtFin.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtFinMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtFinMouseReleased(evt);
      }
    });
    txtFin.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtFinActionPerformed(evt);
      }
    });
    txtFin.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtFinKeyReleased(evt);
      }
    });

    jLabel12.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel12.setForeground(new java.awt.Color(51, 51, 102));
    jLabel12.setText("Con FACTURA");
    jLabel12.setToolTipText("Cliente con factura");

    cbFac.setBackground(new java.awt.Color(240, 248, 239));
    cbFac.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbFac.setOpaque(false);
    cbFac.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbFacActionPerformed(evt);
      }
    });

    txtBus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtBus.setToolTipText("Buscar Clientes");
    txtBus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
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

    jLabel13.setBackground(new java.awt.Color(204, 204, 204));
    jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    jLabel13.setText(" ");
    jLabel13.setToolTipText("Buscar Clientes");
    jLabel13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabel13.setOpaque(true);

    cbSta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    cbSta.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SI", "NO" }));
    cbSta.setPreferredSize(new java.awt.Dimension(35, 25));
    cbSta.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbStaItemStateChanged(evt);
      }
    });
    cbSta.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbStaActionPerformed(evt);
      }
    });

    jLabel14.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel14.setForeground(new java.awt.Color(51, 51, 102));
    jLabel14.setText("ACTIVO");

    jLabel15.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel15.setForeground(new java.awt.Color(51, 51, 102));
    jLabel15.setText("ESTADO");

    txtCiu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtCiu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtCiu.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtCiuMouseClicked(evt);
      }
    });
    txtCiu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtCiuActionPerformed(evt);
      }
    });

    jLabel16.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel16.setForeground(new java.awt.Color(51, 51, 102));
    jLabel16.setText("CIUDAD");

    txtEdo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtEdo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtEdo.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtEdoMouseClicked(evt);
      }
    });
    txtEdo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtEdoActionPerformed(evt);
      }
    });

    txtRif.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtRif.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtRif.setText(" ");
    txtRif.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtRif.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtRif.setPreferredSize(new java.awt.Dimension(7, 30));
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

    jLabel17.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel17.setForeground(new java.awt.Color(51, 51, 102));
    jLabel17.setText("RIF");

    jLabel18.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel18.setForeground(new java.awt.Color(51, 51, 102));
    jLabel18.setText("% RET IVA");

    txtRet.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtRet.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtRet.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtRetMouseClicked(evt);
      }
    });
    txtRet.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtRetActionPerformed(evt);
      }
    });

    btnNvo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnNvo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
    btnNvo.setText("Nuevo");
    btnNvo.setToolTipText("Incluir nuevo Registro");
    btnNvo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnNvo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnNvoActionPerformed(evt);
      }
    });

    txtDic.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtDic.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtDic.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtDicMouseClicked(evt);
      }
    });
    txtDic.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtDicActionPerformed(evt);
      }
    });

    jLabel20.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel20.setForeground(new java.awt.Color(51, 51, 102));
    jLabel20.setText("DIAS C.");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
              .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(14, 14, 14)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(158, 158, 158)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtRif, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFin, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFin, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(18, 18, 18)
                    .addComponent(cbFac))
                  .addComponent(cbCop, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                  .addComponent(txtTlf, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                  .addComponent(txtEdo, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLabel9))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(txtCiu, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(txtEml, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGap(2, 2, 2)
                    .addComponent(txtDic, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(txtRet, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addComponent(txtCon, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
              .addComponent(txtDir)
              .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(161, 161, 161)
                    .addComponent(jLabel13)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(76, 76, 76)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(cbSta, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                  .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(btnNvo)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbTip, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))))))))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel2)
            .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cbTip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnNvo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtRif, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(cbCop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtFin, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
            .addComponent(jFin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(cbSta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addGap(0, 15, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(cbFac, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
              .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtDir, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
          .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtEdo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtCiu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtCon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtTlf, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtEml, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtDic, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(16, Short.MAX_VALUE))
    );

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 11, 1009, 270));

    jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    jButton1.setText("Salir");
    jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });
    getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 578, -1, 28));

    btnEli.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnEli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/elim.png"))); // NOI18N
    btnEli.setMnemonic('E');
    btnEli.setText("Eliminar");
    btnEli.setToolTipText("Eliminar Registro");
    btnEli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnEli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEliActionPerformed(evt);
      }
    });
    getContentPane().add(btnEli, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 578, -1, 28));

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnGra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGra.setMnemonic('A');
    btnGra.setText("Grabar");
    btnGra.setToolTipText("Actualizar registro");
    btnGra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnGra.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGraActionPerformed(evt);
      }
    });
    getContentPane().add(btnGra, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 578, 102, 28));

    jLabel11.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel11.setForeground(new java.awt.Color(51, 51, 102));
    jLabel11.setText("LISTAR CLIENTES");
    getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(796, 581, 126, 23));

    cbAct.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    cbAct.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ACTIVOS", "INACTIVOS" }));
    cbAct.setPreferredSize(new java.awt.Dimension(35, 25));
    cbAct.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbActItemStateChanged(evt);
      }
    });
    cbAct.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbActActionPerformed(evt);
      }
    });
    getContentPane().add(cbAct, new org.netbeans.lib.awtextra.AbsoluteConstraints(926, 581, 104, -1));

    labCan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labCan.setForeground(new java.awt.Color(0, 0, 102));
    labCan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labCan.setText(" ");
    labCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labCan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labCan.setPreferredSize(new java.awt.Dimension(8, 25));
    getContentPane().add(labCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(685, 580, 93, -1));

    txtMsg.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtMsg.setForeground(new java.awt.Color(204, 0, 0));
    txtMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    txtMsg.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    getContentPane().add(txtMsg, new org.netbeans.lib.awtextra.AbsoluteConstraints(149, 578, 273, 28));

    labojo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labojo.setForeground(new java.awt.Color(102, 102, 102));
    labojo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojop.png"))); // NOI18N
    labojo.setToolTipText("Notificacion de Mensaje");
    labojo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    getContentPane().add(labojo, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 578, 27, 28));

    jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/FondoMain.png"))); // NOI18N
    getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1040, 620));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void tabCliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabCliMouseClicked
    presentaRegistro();
  }//GEN-LAST:event_tabCliMouseClicked

  private void btnNvoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNvoActionPerformed
    model.setRowCount(0);
    txtCod.setEditable(true);
    bloqueaCampos();
    desbloqueaCampos();
    txtCod.requestFocus();
  }//GEN-LAST:event_btnNvoActionPerformed

  private void btnEliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliActionPerformed
    txtMsg.setText("");
    int[] sel = tabCli.getSelectedRows();
    if (tabCli.getRowCount() > 0) {
      if (tabCli.getSelectedRow() >= 0) {
        Clientes c = new Clientes(cod);
        if (c.tieneNota()) {
          txtMsg.setText(" - Cliente Tiene Movimiento -".toUpperCase());
        } else {
          icon = new ImageIcon(getClass().getResource("/img/elim.png"));
          String vax = "Desea Eliminar\nCliente  " + cod + " ?";
          if (sel.length > 1) {
            vax = "Desea Eliminar Registros Seleccionados ?";
          }
          String[] options = {"SI", "NO"};
          int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
          if (opcion == 0) {
            //Borra Seleccionadas
            int numRows = tabCli.getSelectedRows().length;
            for (int i = 0; i < numRows; i++) {
              cod = model.getValueAt(tabCli.getSelectedRow(), 1).toString();
              model.removeRow(tabCli.getSelectedRow());
              if (c.eliminarCliente()) {
                bloqueaCampos();
                txtMsg.setText(" - Se elimino cliente " + cod);
              }
            }
          }
        }
      } else {
        txtMsg.setText(" - Debe Seleccionar un registro");
      }
    } else {
      txtMsg.setText(" - Tabla Vacia");
    }
  }//GEN-LAST:event_btnEliActionPerformed

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed
    txtMsg.setText("");
    if (validaCampos()) {
      String vax = "";
      icon = new ImageIcon(getClass().getResource("/img/salvar.png"));
      vax = "Desea Grabar Cliente \n( " + nom + " ) ?";
      String[] options = {"SI", "NO"};
      int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
      if (opcion == 0) {
        Clientes c = new Clientes(cod);
        // Verifico registro no este en la lista obsCategorias
        //obsClientes.contains(c)) 
        if (c.existeCliente()) {
          c = new Clientes(cod, nom, rif, tlf, dir, edo, ciu, cto, tip, cop, eml, pre, fac, fei, dic, sta);
          if (c.modificarCliente()) {
            txtMsg.setText("- Se Grabo Registro -");
            int row = tabCli.getSelectedRow();    // Fila Selecciuonada
            int col = tabCli.getSelectedColumn(); // Columna Seleccionada
            if ((row != -1) && (col != -1)) {
              String tix = "May";
              if (tip.equals("1")) {
                tix = "Det";
              }
              model.setValueAt(tix, tabCli.getSelectedRow(), 0);
              model.setValueAt(cod, tabCli.getSelectedRow(), 1);
              model.setValueAt(nom, tabCli.getSelectedRow(), 2);
              model.setValueAt(tlf, tabCli.getSelectedRow(), 3);
              model.setValueAt(cto, tabCli.getSelectedRow(), 4);
              model.setValueAt(MtoEs(dic, 0), tabCli.getSelectedRow(), 5);
              model.setValueAt(MtoEs(pre, 2).replace(",00", "") + "%", tabCli.getSelectedRow(), 6);
            }
          }
        } else {
          c = new Clientes(cod, nom, rif, tlf, dir, edo, ciu, cto, tip, cop, eml, pre, fac, fei, dic, sta);
          if (c.insertarCliente()) {
            model.addRow(new Object[]{cod, nom, tlf, cto});
            txtMsg.setText("- Se creo Registro -");
          }
        }
        bloqueaCampos();
      }
    }
  }//GEN-LAST:event_btnGraActionPerformed

  // Busca codigo en lista jtabla
  public void MarcarColumna() {
    int rows = tabCli.getRowCount();    // Cantidad Filas
    String cox = "";
    //Recorre filas (rows)
    for (int i = 0; i < rows; i++) {
      cox = tabCli.getValueAt(i, 0).toString().trim();
      if (cox.equals(cod)) {
        tabCli.getSelectionModel().setSelectionInterval(i, i); // Marca fila como seleccionada 
      }
    }
  }

  private void txtNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomActionPerformed
    txtDir.requestFocus();
  }//GEN-LAST:event_txtNomActionPerformed

  private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
    cod = txtCod.getText().trim();
    txtCod.setText(cod.toUpperCase());
    cod = cod.replace(".", "");
    if (!isNumeric(cod) || cod.length() <= 8) {
      txtMsg.setText("- Codigo invalido -");
      txtCod.setSelectionStart(0);
      txtCod.setSelectionEnd(txtCod.getText().length());
      txtCod.requestFocus();
      txtCod.requestFocus();
    } else {
      cod = txtCod.getText().trim();
      if (getDatosCliente(cod)) {
        txtMsg.setText("- Cliente ya existe -");
        txtCod.setEditable(false);
        txtCod.setSelectionStart(0);
        txtCod.setSelectionEnd(txtCod.getText().length());
        txtCod.requestFocus();
      } else {
        txtRif.requestFocus();
      }
    }
  }//GEN-LAST:event_txtCodActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    RegCte.dispose();
  }//GEN-LAST:event_jButton1ActionPerformed

  private void txtNomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNomMouseClicked
    cod = txtCod.getText().trim();
    if (getDatosCliente(cod)) {
      txtCod.setEditable(false);
      txtMsg.setText("- Cliente ya existe -");
    }
  }//GEN-LAST:event_txtNomMouseClicked

  private void txtCodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodMouseClicked
    txtMsg.setText("");
    txtCod.setSelectionStart(0);
    txtCod.setSelectionEnd(txtCod.getText().length());
  }//GEN-LAST:event_txtCodMouseClicked

  private void txtDirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDirMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDirMouseClicked

  private void txtDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDirActionPerformed
    txtEdo.requestFocus();
  }//GEN-LAST:event_txtDirActionPerformed

  private void txtTlfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTlfMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTlfMouseClicked

  private void txtTlfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTlfActionPerformed
    txtCon.requestFocus();
  }//GEN-LAST:event_txtTlfActionPerformed

  private void txtConMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtConMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtConMouseClicked

  private void txtConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtConActionPerformed
    txtEml.requestFocus();
  }//GEN-LAST:event_txtConActionPerformed

  private void cbTipItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTipItemStateChanged
    if (cbTip.getSelectedIndex() >= 0 && evt.getSource() == cbTip && evt.getStateChange() == 1) {
      String str = (String) cbTip.getSelectedItem().toString();
      if (str != null) {
        if (str.length() > 0) {
          str = str.trim();
          if (str.equals("MAYORISTA")) {
            tip = "0";
          } else {
            tip = "1";
          }
        }
      }
    }
  }//GEN-LAST:event_cbTipItemStateChanged

  private void cbTipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbTipActionPerformed

  private void cbCopItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbCopItemStateChanged
    int idx = cbCop.getSelectedIndex();
    if (idx >= 0 && evt.getSource() == cbCop && evt.getStateChange() == 1) {
      String str = (String) cbCop.getSelectedItem().toString();
      if (str != null) {
        if (str.length() > 0) {
          if (str.equals("CREDITO")) {
            cop = "0";
          } else {
            cop = "1";
          }
        }
      }
    }
  }//GEN-LAST:event_cbCopItemStateChanged

  private void cbCopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCopActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbCopActionPerformed

  private void txtEmlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtEmlMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtEmlMouseClicked

  private void txtEmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmlActionPerformed
    txtRet.requestFocus();
  }//GEN-LAST:event_txtEmlActionPerformed

  private void txtFinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFinMouseClicked
    //jDesde.setSelectionStart(0);
    //jDesde.setSelectionEnd(5);
  }//GEN-LAST:event_txtFinMouseClicked

  private void txtFinMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFinMouseReleased

  }//GEN-LAST:event_txtFinMouseReleased

  private void txtFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFinActionPerformed

  }//GEN-LAST:event_txtFinActionPerformed

  private void txtFinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFinKeyReleased

  }//GEN-LAST:event_txtFinKeyReleased

  private void cbActItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbActItemStateChanged
    int idx = cbAct.getSelectedIndex();
    if (idx >= 0 && evt.getSource() == cbAct && evt.getStateChange() == 1) {
      String str = (String) cbAct.getSelectedItem().toString();
      if (str != null) {
        if (str.length() > 0) {
          str = str.trim();
          if (str.equals("INACTIVOS")) {
            act = "1";
          } else {
            act = "0";
          }
          txtMsg.setText("");
          listaObsList();
          txtBus.requestFocus();
        }
      }
    }
  }//GEN-LAST:event_cbActItemStateChanged

  private void cbActActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbActActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbActActionPerformed

  private void cbFacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFacActionPerformed
    fac = "0";
    if (cbFac.isSelected() == false) {
      fac = "1";
    }
  }//GEN-LAST:event_cbFacActionPerformed

  private void txtBusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBusMouseClicked
    model.setRowCount(0);
    bloqueaCampos();
    if (txtBus.getText().length() == 0) {
      fil = "";
      listaObsList();
    }
    txtBus.setText(" ");
    txtBus.requestFocus();
  }//GEN-LAST:event_txtBusMouseClicked

  private void txtBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusActionPerformed
    if (txtBus.getText().length() == 0) {
      fil = "";
      listaObsList();
    }
  }//GEN-LAST:event_txtBusActionPerformed

  private void txtBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusKeyReleased
    fil = txtBus.getText().toUpperCase().trim();
    listaObsList();
  }//GEN-LAST:event_txtBusKeyReleased

  private void cbStaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStaItemStateChanged
    int idx = cbSta.getSelectedIndex();
    if (idx >= 0 && evt.getSource() == cbSta && evt.getStateChange() == 1) {
      String str = (String) cbSta.getSelectedItem().toString();
      if (str != null) {
        if (str.length() > 0) {
          str = str.trim();
          if (str.equals("NO")) {
            sta = "1";
          } else {
            sta = "0";
          }
        }
      }
    }
  }//GEN-LAST:event_cbStaItemStateChanged

  private void cbStaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStaActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbStaActionPerformed

  private void txtCiuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCiuMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCiuMouseClicked

  private void txtCiuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCiuActionPerformed
    txtTlf.requestFocus();
  }//GEN-LAST:event_txtCiuActionPerformed

  private void txtEdoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtEdoMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtEdoMouseClicked

  private void txtEdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEdoActionPerformed
    txtCiu.requestFocus();
  }//GEN-LAST:event_txtEdoActionPerformed

  private void txtRifMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRifMouseClicked

  }//GEN-LAST:event_txtRifMouseClicked

  private void txtRifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRifActionPerformed
    rif = txtRif.getText().trim();
    if (validaRif(rif)) {
      txtNom.requestFocus();
    } else {
      txtMsg.setText(" - Rif Cliente Invalido");
      txtRif.setSelectionStart(0);
      txtRif.setSelectionEnd(txtRif.getText().length());
      txtRif.requestFocus();
    }
  }//GEN-LAST:event_txtRifActionPerformed

  private void txtRetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRetMouseClicked
    txtRet.setSelectionStart(0);
    txtRet.setSelectionEnd(txtRet.getText().length());
    txtRet.requestFocus();
  }//GEN-LAST:event_txtRetMouseClicked

  private void txtRetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRetActionPerformed
    jFin.requestFocus();
  }//GEN-LAST:event_txtRetActionPerformed

  private void txtDicMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDicMouseClicked
    txtDic.setSelectionStart(0);
    txtDic.setSelectionEnd(txtDic.getText().length());
    txtDic.requestFocus();
  }//GEN-LAST:event_txtDicMouseClicked

  private void txtDicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDicActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDicActionPerformed

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
    tabCli.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
    tabCli.getColumnModel().getColumn(col).setHeaderRenderer(headerRenderer);
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
    tabCli.getColumnModel().getColumn(col).setCellRenderer(Alinea);
  }

  // Presenta Datos al pulsar mouse en detalle columna
  public void presentaRegistro() {

    int selectedRow = tabCli.getSelectedRow();
    if (selectedRow >= 0) {
      Object clx = model.getValueAt(tabCli.convertRowIndexToModel(selectedRow), 1);
      cod = clx.toString();
      getDatosCliente(cod);
    }

    /*
    cod = model.getValueAt(tabCli.getSelectedRow(), 1).toString();
    int[] sel = tabCli.getSelectedRows();
    if (sel.length == 1) {
      getDatosCliente(cod);
    }
     */
  }

  public void bloqueaCampos() {
    jFin.setEnabled(false);
    cbSta.setEnabled(false);
    btnGra.setEnabled(false);
    btnEli.setEnabled(false);
    txtCod.setEnabled(false);
    txtRif.setEnabled(false);
    txtNom.setEnabled(false);
    txtDir.setEnabled(false);
    txtEdo.setEnabled(false);
    txtCiu.setEnabled(false);
    txtCon.setEnabled(false);
    txtTlf.setEnabled(false);
    txtEml.setEnabled(false);
    txtRet.setEnabled(false);
    txtDic.setEnabled(false);
    txtFin.setEnabled(false);
    cbTip.setEnabled(false);
    cbCop.setEnabled(false);
    cbFac.setEnabled(false);
    //txtMsg.setText("");
    txtBus.setText("");
    txtCod.setText("");
    txtRif.setText("");
    txtNom.setText("");
    txtDir.setText("");
    txtCiu.setText("");
    txtEdo.setText("");
    txtCon.setText("");
    txtTlf.setText("");
    txtEml.setText("");
    txtRet.setText("");
    txtDic.setText("");
    txtFin.setText("");
    cbTip.setSelectedIndex(0);
    cbCop.setSelectedIndex(0);
    cbSta.setSelectedIndex(0);
    cbFac.setSelected(false);
    txtCod.setEditable(true);
    txtCod.setBackground(new java.awt.Color(237, 235, 235));
    txtBus.requestFocus();
  }

  public void desbloqueaCampos() {
    txtMsg.setText("");
    jFin.setEnabled(true);
    cbSta.setEnabled(true);
    btnGra.setEnabled(true);
    btnEli.setEnabled(true);
    txtCod.setEnabled(true);
    txtRif.setEnabled(true);
    txtNom.setEnabled(true);
    txtDir.setEnabled(true);
    txtEdo.setEnabled(true);
    txtCiu.setEnabled(true);
    txtCon.setEnabled(true);
    txtTlf.setEnabled(true);
    txtEml.setEnabled(true);
    txtFin.setEnabled(true);
    txtDic.setEnabled(true);
    txtRet.setEnabled(true);
    cbTip.setEnabled(true);
    cbCop.setEnabled(true);
    cbFac.setEnabled(true);
    txtNom.requestFocus();
  }

  public void listaObsList() {
    model.setRowCount(0);
    int can = 0;
    Clientes c = new Clientes();
    obsClientes = c.getObsListClientes(fil, "", act);
    for (Clientes cte : obsClientes) {
      cod = cte.getCoc();
      nom = cte.getNom();
      rif = cte.getRif();
      tlf = cte.getTlf();
      cto = cte.getCon();
      eml = cte.getEml();
      tip = cte.getTip();
      dic = cte.getDic();
      pre = cte.getPre();
      String tix = "May";
      if (tip.equals("1")) {
        tix = "Det";
      }
      model.addRow(new Object[]{tix, cod, nom, tlf, cto, MtoEs(dic, 0), MtoEs(pre, 2).replace(",00", "") + "%"});
      can++;
    }
    labCan.setText("c = " + can);
    txtBus.requestFocus();
  }

  // Busca Datos  lis
  public boolean getDatosCliente(String cod) {
    int can = 0;
    /*
    // Lista Array List <Categorias>
    Clientes c = new Clientes();
    List<String> names = c.getDatosCliente(cod);
    int i = 0;
    for (String nam : names) {
      System.out.println("names="+names.get(i));
      i++;
    }
     */
    Clientes c = new Clientes(cod);
    obsClientes = c.getObsListClientes("", cod, "");
    for (Clientes cte : obsClientes) {
      cod = cte.getCoc();
      nom = cte.getNom();
      rif = cte.getRif();
      tlf = cte.getTlf();
      dir = cte.getDir();
      ciu = cte.getCiu();
      edo = cte.getEdo();
      cto = cte.getCon();
      tip = cte.getTip();
      cop = cte.getCop();
      eml = cte.getEml();
      fac = cte.getFac();
      pre = cte.getPre();
      dic = cte.getDic();
      fei = cte.getFec();
      sta = cte.getSta();

      txtCod.setText(cod);
      txtRif.setText(rif);
      txtNom.setText(nom);
      txtDir.setText(dir);
      txtEdo.setText(edo);
      txtCiu.setText(ciu);
      txtTlf.setText(tlf);
      txtCon.setText(cto);
      txtEml.setText(eml);
      txtCon.setText(cto);
      txtDic.setText(MtoEs(dic, 0));
      txtRet.setText(MtoEs(pre, 2).replace(",00", ""));

      if (fei.length() >= 8) {
        txtFin.setText(ymd_dmy(fei).replace("/", "-"));
      } else {
        txtFin.setText("");
      }
      if (tip.equals("0")) {
        cbTip.setSelectedIndex(1);
      } else {
        cbTip.setSelectedIndex(0);
      }
      if (cop.equals("0")) {
        cbCop.setSelectedIndex(1);
      } else {
        cbCop.setSelectedIndex(0);
      }
      if (fac.equals("0")) {
        cbFac.setSelected(true);
      } else {
        cbFac.setSelected(false);
      }
      if (sta.equals("0")) {
        cbSta.setSelectedIndex(0);
      } else {
        cbSta.setSelectedIndex(1);
      }
      desbloqueaCampos();
      can = 1;
    }
    if (can == 0) {
      txtCod.setEditable(false);
      return false;
    } else {
      txtCod.setEditable(false);
      return true;
    }

  }

  public boolean validaCampos() {
    cod = txtCod.getText().toUpperCase().trim();
    rif = txtRif.getText().toUpperCase().trim();
    nom = txtNom.getText().toUpperCase().trim();
    dir = txtDir.getText().toUpperCase().trim();
    edo = txtEdo.getText().toUpperCase().trim();
    ciu = txtCiu.getText().toUpperCase().trim();
    tlf = txtTlf.getText().trim();
    cto = txtCon.getText().toUpperCase().trim();
    eml = txtEml.getText().toLowerCase().trim();
    fei = (txtFin.getText().replaceAll("-", ""));
    ret = txtRet.getText();
    dcc = txtDic.getText();
    pre = 0;
    dic = 0;
    if (isNumeric(ret)) {
      pre = Integer.valueOf(ret);
    }
    if (isNumeric(dcc)) {
      dic = Integer.valueOf(dcc);
    }
    if (cod.length() > 9) {
      cod = cod.substring(0, 9);
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
    if (edo.length() > 20) {
      edo = edo.substring(0, 20);
    }
    if (ciu.length() > 20) {
      ciu = ciu.substring(0, 20);
    }
    if (tlf.length() > 40) {
      tlf = tlf.substring(0, 40);
    }
    if (cto.length() > 40) {
      cto = cto.substring(0, 40);
    }
    if (eml.length() > 50) {
      eml = eml.substring(0, 40);
    }

    if (fei.length() == 8) {
      fei = dmy_ymd(fei);
    } else {
      fei = "";
    }
    fei = fei.replaceAll("-", "");
    indok = 0;
    txtMsg.setText("");
    // Valida  
    cod = cod.replace(".", "");
    if (isNumeric(cod)) {
      indok = 1;
    } else {
      indok = 0;
      txtMsg.setText(" - Codigo Cliente Invalido");
      txtCod.setSelectionStart(0);
      txtCod.setSelectionEnd(txtCod.getText().length());
      txtCod.requestFocus();
    }
    if (indok == 1 && validaRif(rif)) {
      indok = 1;
    } else {
      indok = 0;
      txtMsg.setText(" - Rif Cliente Invalido");
      txtRif.setSelectionStart(0);
      txtRif.setSelectionEnd(txtRif.getText().length());
      txtRif.requestFocus();
    }
    //Valida Nombre
    if (indok == 1) {
      if (nom.length() == 0) {
        txtMsg.setText(" - Ingresar Nombre Cliente");
        txtNom.requestFocus();
        indok = 0;
      } else {
        //Valida Email
        if (eml.length() > 0) {
          indok = 0;
          if (validaEmail(eml)) {
            indok = 1;
          } else {
            txtMsg.setText(" - Email Invalido");
            txtEml.setSelectionStart(0);
            txtEml.setSelectionEnd(txtEml.getText().length());
            txtEml.requestFocus();
          }
        }
      }
    }

    //Valida Direccion
    if (indok == 1) {
      if (dir.length() == 0) {
        txtMsg.setText(" - Ingresar Direccion Cliente");
        txtDir.requestFocus();
        indok = 0;
      }
    }

    //Valida Estado
    if (indok == 1) {
      if (edo.length() == 0) {
        txtMsg.setText(" - Ingresar Estado Cliente");
        txtEdo.requestFocus();
        indok = 0;
      }
    }

    //Valida Ciudad
    if (indok == 1) {
      if (ciu.length() == 0) {
        txtMsg.setText(" - Ingresar Ciudad Cliente");
        txtCiu.requestFocus();
        indok = 0;
      }
    }

    //Valida % retencion
    if (indok == 1) {
      if (pre == 0 || pre == 75 || pre == 100) {
        jFin.requestFocus();
      } else {
        indok = 0;
        txtMsg.setText(" -  % Retencion Iva Invalido");
        txtRet.setSelectionStart(0);
        txtRet.setSelectionEnd(txtRet.getText().length());
        txtRet.requestFocus();
      }
    }

    if (indok == 1) {
      return true;
    } else {
      return false;
    }
  }

  // Valida RIF  ^[JGVE][-][0-9]{8}[-][0-9]$
  public boolean validaRif(String sRif) {
    boolean bResultado = false;

    if (sRif.startsWith("V") || sRif.startsWith("E") || sRif.startsWith("P")) {
      return true;
    }

    if (sRif.length() > 0) {
      int iFactor = 0;
      sRif = sRif.replace("-", "").trim();
      if (sRif.length() <= 10) {
        if (isNumeric(sRif.substring(1, sRif.length()))) {
          sRif = sRif.toUpperCase().substring(0, 1) + String.format("%09d", Integer.parseInt(sRif.substring(1, sRif.length())));
          String sPrimerCaracter = sRif.substring(0, 1).toUpperCase();
          switch (sPrimerCaracter) {
            case "V":
              iFactor = 1;
              break;
            case "E":
              iFactor = 2;
              break;
            case "J":
              iFactor = 3;
              break;
            case "P":
              iFactor = 4;
              break;
            case "G":
              iFactor = 5;
              break;
          }
          if (iFactor > 0) {
            int suma = ((Integer.parseInt(sRif.substring(8, sRif.length() - 1))) * 2)
                    + ((Integer.parseInt(sRif.substring(7, sRif.length() - 2))) * 3)
                    + ((Integer.parseInt(sRif.substring(6, sRif.length() - 3))) * 4)
                    + ((Integer.parseInt(sRif.substring(5, sRif.length() - 4))) * 5)
                    + ((Integer.parseInt(sRif.substring(4, sRif.length() - 5))) * 6)
                    + ((Integer.parseInt(sRif.substring(3, sRif.length() - 6))) * 7)
                    + ((Integer.parseInt(sRif.substring(2, sRif.length() - 7))) * 2)
                    + ((Integer.parseInt(sRif.substring(1, sRif.length() - 8))) * 3)
                    + (iFactor * 4);
            float dividendo = suma / 11;
            int DividendoEntero = (int) dividendo;
            int resto = 11 - (suma - DividendoEntero * 11);
            if (resto >= 10 || resto < 1) {
              resto = 0;
            }
            if (sRif.substring(9, sRif.length()).equals(Integer.toString(resto))) {
              bResultado = true;
            }
          }
        }
      }
    }
    return bResultado;
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
      java.util.logging.Logger.getLogger(Registro_Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    /*
     /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registro_Clientes().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnEli;
  private javax.swing.JButton btnGra;
  private javax.swing.JButton btnNvo;
  private javax.swing.JComboBox cbAct;
  private javax.swing.JComboBox cbCop;
  private javax.swing.JCheckBox cbFac;
  private javax.swing.JComboBox cbSta;
  private javax.swing.JComboBox cbTip;
  private javax.swing.JButton jButton1;
  private com.toedter.calendar.JDateChooser jFin;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
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
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel labCan;
  private javax.swing.JLabel labojo;
  public javax.swing.JTable tabCli;
  private javax.swing.JTextField txtBus;
  private javax.swing.JTextField txtCiu;
  private javax.swing.JTextField txtCod;
  private javax.swing.JTextField txtCon;
  private javax.swing.JTextField txtDic;
  private javax.swing.JTextField txtDir;
  private javax.swing.JTextField txtEdo;
  private javax.swing.JTextField txtEml;
  private javax.swing.JTextField txtFin;
  private javax.swing.JLabel txtMsg;
  private javax.swing.JTextField txtNom;
  private javax.swing.JTextField txtRet;
  private javax.swing.JTextField txtRif;
  private javax.swing.JTextField txtTlf;
  // End of variables declaration//GEN-END:variables
}
