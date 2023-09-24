package gestionFZ;

import comun.Mensaje;
import static comun.MetodosComunes.FileExist;
import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.isNumeric;
import static comun.MetodosComunes.pintarImagen;
import static comun.MetodosComunes.ymd_dmy;
import static gestionFZ.Menu.RegLis;
import static gestionFZ.Menu.getCanImgDep;
import static gestionFZ.Menu.getCanImgPrd;
import static gestionFZ.Menu.pdfdet;
import static gestionFZ.Menu.pdfmay;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
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
import modelo.Importadora;
import modelo.ListaPrecios;
import modelo.UnidadMedida;

public class Registro_CatalogoProductos extends javax.swing.JFrame {

  int indok = 0, indcbDep = 0;
  String cod = "", nom = "", dep = "", emp = "", man = "1", pag = "0", sta = "0", app = "0";
  String fil = "", act = "0", ine = "N", sim = "N", feclis = "", unm = "";

  double pum = 0, pud = 0, stk = 0;

  ImageIcon icon;
  public static Consulta_ImagenProd ImgPrd;
  public static Registro_Departamento comDep;
  public static Registro_EmpaqueProd regEmp;
  DefaultTableModel model;

  public Registro_CatalogoProductos() {

    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/logosysven.jpg")));
    initComponents();
    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes
    cargaUnm();
    Importadora imp = new Importadora();
    feclis = imp.getFecLis();
    labFec.setText(ymd_dmy(feclis));
    model = (DefaultTableModel) tabPrd.getModel();

    tabPrd.setRowHeight(25);//tamaño de las celdas
    tabPrd.setGridColor(new java.awt.Color(0, 0, 0));
    tabPrd.setSelectionBackground(new Color(151, 193, 215));
    tabPrd.setSelectionForeground(Color.blue);
    tabPrd.getTableHeader().setReorderingAllowed(true);  // activa movimiefnto columnas
    tabPrd.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    txtCod.setDisabledTextColor(Color.BLACK);
    txtPum.setDisabledTextColor(Color.GRAY);
    //TableRowSorter<TableModel> Ord = new TableRowSorter<TableModel>((TableModel) model);
    //jTabla.setRowSorter(Ord);
    // Ajusta tamaño Columnas
    TableColumnModel columnModel = tabPrd.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(10);  // Codigo
    columnModel.getColumn(1).setPreferredWidth(400); // Nombre  
    columnModel.getColumn(2).setPreferredWidth(14);  // P.mayor
    columnModel.getColumn(3).setPreferredWidth(14);  // P.Detal
    columnModel.getColumn(4).setPreferredWidth(14);  // Existencia
    columnModel.getColumn(5).setPreferredWidth(01);  // Imagen

    //Alinea Columnas Header
    AlinCampoH(0, 2); // 2=Center
    AlinCampoH(1, 0); // 0=Izq
    AlinCampoH(2, 2); // 2=Center
    AlinCampoH(3, 2); // 2=Center
    AlinCampoH(4, 2); // 2=Center
    AlinCampoH(5, 2); // 2=Center

    //Alinea Columnas Detail
    AlinCampoD(0, 2); // 2=Center
    AlinCampoD(1, 0); // 0=Izq
    AlinCampoD(2, 2); // 2=Center
    AlinCampoD(3, 2); // 2=Center
    AlinCampoD(4, 2); // 2=Center
    AlinCampoD(5, 2); // 2=Center

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        RegLis.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        RegLis.dispose();
      }
    });
    cargaDepartamentos();
    txtCod.setBackground(new java.awt.Color(237, 235, 235));
    bloqueaCampos();
    fil = "";

    cbPor.setSelected(true);
    app = "0";   // con descuento

    ListaDatos();
    txtBus.requestFocus();
  }

  public void setImagenCia() {
    String img = "imgcia\\logoimp2.png";
    pintarImagen(labImg, img);
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

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    tabPrd = new javax.swing.JTable();
    jPanel1 = new javax.swing.JPanel();
    txtCod = new javax.swing.JTextField();
    jLabel1 = new javax.swing.JLabel();
    txtNom = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    txtPud = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    cbDep = new javax.swing.JComboBox();
    txtBus = new javax.swing.JTextField();
    jLabel13 = new javax.swing.JLabel();
    jLabel8 = new javax.swing.JLabel();
    txtPum = new javax.swing.JTextField();
    txtStk = new javax.swing.JTextField();
    jLabel15 = new javax.swing.JLabel();
    labImg = new javax.swing.JLabel();
    labFec = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    cbExi = new javax.swing.JCheckBox();
    jLabel9 = new javax.swing.JLabel();
    cbSta = new javax.swing.JCheckBox();
    jLabel12 = new javax.swing.JLabel();
    cbImg = new javax.swing.JCheckBox();
    jLabel14 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    txtEmp = new javax.swing.JTextField();
    cbUnm = new javax.swing.JComboBox();
    jLabel16 = new javax.swing.JLabel();
    txtRef = new javax.swing.JTextField();
    jLabel17 = new javax.swing.JLabel();
    jLabel18 = new javax.swing.JLabel();
    jLabel20 = new javax.swing.JLabel();
    cbPor = new javax.swing.JCheckBox();
    jButton1 = new javax.swing.JButton();
    btnEli = new javax.swing.JButton();
    btnGra = new javax.swing.JButton();
    btnNvo = new javax.swing.JButton();
    jLabel11 = new javax.swing.JLabel();
    cbAct = new javax.swing.JComboBox();
    labCan = new javax.swing.JLabel();
    btnCat = new javax.swing.JButton();
    labMsgL = new javax.swing.JLabel();
    labRutPdf = new javax.swing.JLabel();
    labImgD = new javax.swing.JLabel();
    labCap = new javax.swing.JLabel();
    labDep = new javax.swing.JLabel();
    labImgP = new javax.swing.JLabel();
    jLabel10 = new javax.swing.JLabel();
    labojo = new javax.swing.JLabel();
    jLabel19 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Catalogo Productos");
    getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    tabPrd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    tabPrd.setForeground(new java.awt.Color(51, 51, 51));
    tabPrd.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "CODIGO", "DESCRIPCIOM", "P.MAYOR", "P.DETAL", "STOCK", "Img"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
      };
      boolean[] canEdit = new boolean [] {
        false, false, false, false, false, false
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    tabPrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    tabPrd.setRowHeight(25);
    tabPrd.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tabPrdMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(tabPrd);
    tabPrd.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    if (tabPrd.getColumnModel().getColumnCount() > 0) {
      tabPrd.getColumnModel().getColumn(0).setResizable(false);
      tabPrd.getColumnModel().getColumn(0).setPreferredWidth(10);
      tabPrd.getColumnModel().getColumn(1).setResizable(false);
      tabPrd.getColumnModel().getColumn(1).setPreferredWidth(250);
      tabPrd.getColumnModel().getColumn(2).setResizable(false);
      tabPrd.getColumnModel().getColumn(2).setPreferredWidth(14);
      tabPrd.getColumnModel().getColumn(3).setResizable(false);
      tabPrd.getColumnModel().getColumn(3).setPreferredWidth(14);
      tabPrd.getColumnModel().getColumn(4).setResizable(false);
      tabPrd.getColumnModel().getColumn(4).setPreferredWidth(14);
      tabPrd.getColumnModel().getColumn(5).setResizable(false);
      tabPrd.getColumnModel().getColumn(5).setPreferredWidth(1);
    }

    getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 242, 1040, 356));

    jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    txtCod.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtCod.setForeground(new java.awt.Color(0, 0, 102));
    txtCod.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtCod.setText(" ");
    txtCod.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtCod.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtCod.setOpaque(false);
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

    txtNom.setEditable(false);
    txtNom.setBackground(new java.awt.Color(204, 204, 204));
    txtNom.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtNom.setForeground(new java.awt.Color(0, 0, 102));
    txtNom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    txtNom.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNom.setOpaque(false);
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
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/catalogoProd2.png"))); // NOI18N
    jLabel2.setText(" CATALOGO PRODUCTOS   ");
    jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jLabel2.setOpaque(true);

    txtPud.setBackground(new java.awt.Color(204, 204, 204));
    txtPud.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtPud.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtPud.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
    txtPud.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtPud.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtPudMouseClicked(evt);
      }
    });
    txtPud.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtPudActionPerformed(evt);
      }
    });

    jLabel5.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(51, 51, 102));
    jLabel5.setText("P. MAYOR");

    jLabel7.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel7.setForeground(new java.awt.Color(51, 51, 102));
    jLabel7.setText("DEP");
    jLabel7.setToolTipText("Departamento");

    cbDep.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    cbDep.setMaximumRowCount(20);
    cbDep.setToolTipText("Clasificacion Departamentos Producots");
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

    txtBus.setBackground(new java.awt.Color(255, 253, 249));
    txtBus.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtBus.setToolTipText("Buscar Articulos ( Enter o Doble Click busca Todos )");
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

    jLabel13.setBackground(new java.awt.Color(204, 204, 204));
    jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    jLabel13.setText(" ");
    jLabel13.setToolTipText("Buscar Articulos ( Enter o Doble Click busca Todos )");
    jLabel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jLabel13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabel13.setOpaque(true);

    jLabel8.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel8.setForeground(new java.awt.Color(51, 51, 102));
    jLabel8.setText("P. DETAL");

    txtPum.setBackground(new java.awt.Color(204, 204, 204));
    txtPum.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtPum.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtPum.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
    txtPum.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtPum.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtPumMouseClicked(evt);
      }
    });
    txtPum.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtPumActionPerformed(evt);
      }
    });

    txtStk.setBackground(new java.awt.Color(204, 204, 204));
    txtStk.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtStk.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtStk.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
    txtStk.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtStk.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtStkMouseClicked(evt);
      }
    });
    txtStk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtStkActionPerformed(evt);
      }
    });

    jLabel15.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel15.setForeground(new java.awt.Color(51, 51, 102));
    jLabel15.setText("EXISTENCIA");

    labImg.setToolTipText("Pulse Click para ver Imagen en Grande");
    labImg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    labImg.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labImgMouseClicked(evt);
      }
    });

    labFec.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
    labFec.setForeground(new java.awt.Color(0, 0, 153));
    labFec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labFec.setText(" ");
    labFec.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jLabel6.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
    jLabel6.setText("F.Lista");

    cbExi.setToolTipText("Consulta Stock en cero");
    cbExi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbExi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbExiActionPerformed(evt);
      }
    });

    jLabel9.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel9.setForeground(new java.awt.Color(51, 51, 102));
    jLabel9.setText("E0");
    jLabel9.setToolTipText("Consulta Existencia en cero");

    cbSta.setToolTipText("Consulta Stock en cero");
    cbSta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbSta.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbStaActionPerformed(evt);
      }
    });

    jLabel12.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel12.setForeground(new java.awt.Color(51, 51, 102));
    jLabel12.setText("Activo");
    jLabel12.setToolTipText("Estatus Activo/Inactivo");

    cbImg.setToolTipText("Consulta Productos sin Imagenes");
    cbImg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbImg.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbImgActionPerformed(evt);
      }
    });

    jLabel14.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel14.setForeground(new java.awt.Color(51, 51, 102));
    jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel14.setText("I0");
    jLabel14.setToolTipText("Consulta Catalogos sin Imagenes");

    jLabel4.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(51, 51, 102));
    jLabel4.setText("EMPAQUE");

    txtEmp.setBackground(new java.awt.Color(255, 247, 236));
    txtEmp.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
    txtEmp.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    txtEmp.setText(" ");
    txtEmp.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtEmp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtEmp.setOpaque(false);
    txtEmp.setPreferredSize(new java.awt.Dimension(7, 30));
    txtEmp.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtEmpMouseClicked(evt);
      }
    });
    txtEmp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtEmpActionPerformed(evt);
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

    jLabel16.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel16.setForeground(new java.awt.Color(51, 51, 102));
    jLabel16.setText("UM");
    jLabel16.setToolTipText("Departamento");

    txtRef.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtRef.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    txtRef.setText(" ");
    txtRef.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtRef.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtRef.setOpaque(false);
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

    jLabel17.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel17.setForeground(new java.awt.Color(51, 51, 102));
    jLabel17.setText("REF");
    jLabel17.setToolTipText("Departamento");

    jLabel18.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
    jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel18.setText("Imagen");

    jLabel20.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel20.setForeground(new java.awt.Color(51, 51, 102));
    jLabel20.setText("% Desc");
    jLabel20.setToolTipText("Lista Productos Con/Sin % Descuento  ");
    jLabel20.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabel20MouseClicked(evt);
      }
    });

    cbPor.setBackground(new java.awt.Color(204, 204, 0));
    cbPor.setSelected(true);
    cbPor.setToolTipText("Productos con  % Descuento");
    cbPor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbPor.setOpaque(false);
    cbPor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbPorActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                  .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(txtPum)
                  .addComponent(txtCod, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                  .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGap(27, 27, 27)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbExi)
                .addGap(18, 18, 18)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbImg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbDep, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(txtPud, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(86, 86, 86)
                    .addComponent(jLabel16)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(cbUnm, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(txtStk, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(14, 14, 14)
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(labFec, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(txtRef, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(jPanel1Layout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(cbSta))
                  .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(cbPor)))))
            .addGap(10, 10, 10))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(labImg, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(14, 14, 14)
            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cbDep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel18))
              .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(cbImg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(cbExi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtPum, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtPud, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtStk, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel6)
                .addComponent(labFec)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(cbSta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cbUnm, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtRef, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(cbPor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(0, 0, Short.MAX_VALUE)
            .addComponent(labImg, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(13, Short.MAX_VALUE))
    );

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 11, -1, -1));

    jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    jButton1.setText("Salir");
    jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });
    getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 605, 89, -1));

    btnEli.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
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
    getContentPane().add(btnEli, new org.netbeans.lib.awtextra.AbsoluteConstraints(702, 209, 99, -1));

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
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
    getContentPane().add(btnGra, new org.netbeans.lib.awtextra.AbsoluteConstraints(566, 208, 102, -1));

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
    getContentPane().add(btnNvo, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 605, -1, -1));

    jLabel11.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel11.setForeground(new java.awt.Color(51, 51, 102));
    jLabel11.setText("LISTAR ");
    jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 600, -1, 23));

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
    getContentPane().add(cbAct, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 600, 109, -1));

    labCan.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    labCan.setForeground(new java.awt.Color(0, 0, 102));
    labCan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labCan.setText(" ");
    labCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labCan.setPreferredSize(new java.awt.Dimension(8, 25));
    getContentPane().add(labCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(735, 604, 91, -1));

    btnCat.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    btnCat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pdf0.png"))); // NOI18N
    btnCat.setMnemonic('B');
    btnCat.setText("Reporte PDF");
    btnCat.setToolTipText("Generar Lista de Precios en Pdf");
    btnCat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnCat.setPreferredSize(new java.awt.Dimension(49, 32));
    btnCat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCatActionPerformed(evt);
      }
    });
    getContentPane().add(btnCat, new org.netbeans.lib.awtextra.AbsoluteConstraints(849, 208, 134, 28));

    labMsgL.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
    labMsgL.setForeground(new java.awt.Color(153, 0, 0));
    labMsgL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labMsgL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labMsgL.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labMsgLMouseClicked(evt);
      }
    });
    getContentPane().add(labMsgL, new org.netbeans.lib.awtextra.AbsoluteConstraints(267, 604, 420, 26));

    labRutPdf.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labRutPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rutpdf.png"))); // NOI18N
    labRutPdf.setToolTipText("Ruta Reportes Lista de Precios en Pdf");
    labRutPdf.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labRutPdf.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labRutPdf.setOpaque(true);
    labRutPdf.setPreferredSize(new java.awt.Dimension(43, 30));
    labRutPdf.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labRutPdfMouseClicked(evt);
      }
    });
    getContentPane().add(labRutPdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(1001, 208, 51, 28));

    labImgD.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    labImgD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labImgD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rutimg.png"))); // NOI18N
    labImgD.setText("Dep");
    labImgD.setToolTipText("Imagenes Departamento ");
    labImgD.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labImgD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labImgD.setMaximumSize(new java.awt.Dimension(50, 35));
    labImgD.setMinimumSize(new java.awt.Dimension(50, 35));
    labImgD.setPreferredSize(new java.awt.Dimension(50, 30));
    labImgD.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labImgDMouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        labImgDMouseEntered(evt);
      }
    });
    getContentPane().add(labImgD, new org.netbeans.lib.awtextra.AbsoluteConstraints(183, 208, 79, 28));

    labCap.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    labCap.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labCap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/empaque.jpg"))); // NOI18N
    labCap.setText("Empaque ");
    labCap.setToolTipText("Actualizar Unidades x Empaque");
    labCap.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labCap.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labCap.setMaximumSize(new java.awt.Dimension(50, 30));
    labCap.setMinimumSize(new java.awt.Dimension(50, 30));
    labCap.setPreferredSize(new java.awt.Dimension(50, 30));
    labCap.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labCapMouseClicked(evt);
      }
    });
    getContentPane().add(labCap, new org.netbeans.lib.awtextra.AbsoluteConstraints(428, 208, 97, 28));

    labDep.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    labDep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labDep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editar.png"))); // NOI18N
    labDep.setText("Com Dep");
    labDep.setToolTipText("Actualizar Comentarios x Departamento");
    labDep.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labDep.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labDep.setMaximumSize(new java.awt.Dimension(50, 35));
    labDep.setMinimumSize(new java.awt.Dimension(50, 35));
    labDep.setPreferredSize(new java.awt.Dimension(50, 30));
    labDep.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labDepMouseClicked(evt);
      }
    });
    getContentPane().add(labDep, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 208, 101, 28));

    labImgP.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    labImgP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labImgP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rutimg.png"))); // NOI18N
    labImgP.setText("Prd");
    labImgP.setToolTipText("Imagenes Productos");
    labImgP.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labImgP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labImgP.setMaximumSize(new java.awt.Dimension(50, 35));
    labImgP.setMinimumSize(new java.awt.Dimension(50, 35));
    labImgP.setPreferredSize(new java.awt.Dimension(50, 30));
    labImgP.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labImgPMouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        labImgPMouseEntered(evt);
      }
    });
    getContentPane().add(labImgP, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 208, 74, 28));

    jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/excel.png"))); // NOI18N
    jLabel10.setText(" ");
    jLabel10.setToolTipText("Genera Listado en Excel");
    jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jLabel10.setPreferredSize(new java.awt.Dimension(26, 28));
    jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabel10MouseClicked(evt);
      }
    });
    getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 208, 35, -1));

    labojo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labojo.setForeground(new java.awt.Color(102, 102, 102));
    labojo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojop.png"))); // NOI18N
    labojo.setToolTipText("Notificacion de Mensaje");
    labojo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    getContentPane().add(labojo, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 604, 27, 26));

    jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/FondoMain.png"))); // NOI18N
    jLabel19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1070, 640));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void tabPrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPrdMouseClicked
    presenta();
  }//GEN-LAST:event_tabPrdMouseClicked

  private void btnNvoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNvoActionPerformed
    String fil = "C:\\Windows\\System32\\vss_sv.dll";
    if (FileExist(fil)) {
      labMsgL.setText("");
      model.setRowCount(0);
      bloqueaCampos();
      man = "1";
      desbloCampos();
      txtCod.setBackground(new java.awt.Color(255, 255, 255));
      txtCod.setEnabled(true);
      txtNom.setEnabled(true);
      txtNom.setEditable(true);
      txtCod.requestFocus();
    } else {
      labMsgL.setText("- Activar Licencia de Uso");
    }
  }//GEN-LAST:event_btnNvoActionPerformed


  private void btnEliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliActionPerformed
    labMsgL.setText("");
    int[] sel = tabPrd.getSelectedRows();
    if (tabPrd.getRowCount() > 0) {
      if (tabPrd.getSelectedRow() >= 0) {
        icon = new ImageIcon(getClass().getResource("/img/elim.png"));
        String vax = "Desea Eliminar Registro ?";
        String[] options = {"SI", "NO"};
        int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
        if (opcion == 0) {
          //Borra Seleccionadas
          int numRows = tabPrd.getSelectedRows().length;
          for (int i = 0; i < numRows; i++) {
            cod = model.getValueAt(tabPrd.getSelectedRow(), 0).toString();
            // Buscar  
            ListaPrecios l = new ListaPrecios();
            String max = l.getTipProd(cod);
            if (max.equals("1")) {
              // Elimina producto manual
              model.removeRow(tabPrd.getSelectedRow());
              if (l.eliminarProducto(cod)) {
                labMsgL.setText(" - Se elimino Registro -");
              }
              // Elimina Empaque
              EmpaqueProducto e = new EmpaqueProducto();
              if (e.eliminarEmpaque(cod)) {
                labMsgL.setText(" - Se elimino Registro -");
              }
              bloqueaCampos();
            } else {
              labMsgL.setText(" - No se Puede Eliminar -");
            }
          }
        }
      } else {
        labMsgL.setText(" - Debe Seleccionar un registro");
      }
    } else {
      labMsgL.setText(" - Tabla Vacia");
    }
  }//GEN-LAST:event_btnEliActionPerformed

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed
    labMsgL.setText("");
    if (validaCampos()) {

      String vax = "";
      //Incluir / Modificar
      try {
        ConexionSQL bdsql = new ConexionSQL();
        Connection con = bdsql.Conectar();
        Statement st = con.createStatement();
        ResultSet rs = null;
        // Buscar  
        int can = 0;
        ListaPrecios l = new ListaPrecios();
        if (l.existeProducto(cod)) {
          can = 1;
        }
        String sql = "";

        // Precio mayor
        String mox = txtPum.getText();
        if (isNumeric(mox)) {
          mox = GetCurrencyDouble(mox);
          pum = GetMtoDouble(mox);
        } else {
          mox = mox.replace(".", "");
          mox = mox.replace(",", ".");
          if (isNumeric(mox)) {
            pum = GetMtoDouble(mox);
          } else {
            pum = 0;
          }
        }

        // precio detal
        mox = txtPud.getText();
        if (isNumeric(mox)) {
          mox = GetCurrencyDouble(mox);
          pud = GetMtoDouble(mox);
        } else {
          mox = mox.replace(".", "");
          mox = mox.replace(",", ".");
          if (isNumeric(mox)) {
            pud = GetMtoDouble(mox);
          } else {
            pud = 0;
          }
        }
        if (pum == 0) {
          txtPum.requestFocus();
          labMsgL.setText("- Precio invalido ");
        }
        if (pud == 0) {
          txtPud.requestFocus();
          labMsgL.setText("- Precio invalido ");
        }

        // Stock
        mox = txtStk.getText();
        if (isNumeric(mox)) {
          mox = GetCurrencyDouble(mox);
          stk = GetMtoDouble(mox);
        } else {
          mox = mox.replace(".", "");
          mox = mox.replace(",", ".");
          if (isNumeric(mox)) {
            stk = GetMtoDouble(mox);
          } else {
            stk = 0;
          }
        }

        //System.out.println("pum=" + MtoEs(pum, 2) + ", pun=" + MtoEs(pud, 2) + " stk=" + MtoEs(stk, 0));
        if (pum > 0 && pud > 0) {

          if (can == 0) {  // Incluir
            man = "1";  // nuevo manual
            sql = "Insert into Listaprecios "
                    + "(dep,cop,nom,pum,pud,stk,man,pag,sta) "
                    + "VALUES ("
                    + "'" + dep + "',"
                    + "'" + cod + "',"
                    + "'" + nom + "',"
                    + "" + pum + ","
                    + "" + pud + ","
                    + "" + stk + ","
                    + "'" + man + "',"
                    + "'" + pag + "',"
                    + "'" + sta + "')";
          } else {  // Modificar
            if (man.equals("0")) {
              sql = "update Listaprecios set "
                      + "nom ='" + nom + "',"
                      + "pum =" + pum + ","
                      + "pud =" + pud + ","
                      + "stk =" + stk + ","
                      + "pag ='" + pag + "',"
                      + "sta ='" + sta + "' "
                      + "where cop =  '" + cod + "' ";
            } else {
              sql = "update Listaprecios set "
                      + "dep ='" + dep + "',"
                      + "nom ='" + nom + "',"
                      + "pum =" + pum + ","
                      + "pud =" + pud + ","
                      + "stk =" + stk + ","
                      + "man ='" + man + "',"
                      + "pag ='" + pag + "',"
                      + "sta ='" + sta + "' "
                      + "where cop =  '" + cod + "' ";
            }
          }
          icon = new ImageIcon(getClass().getResource("/img/salvar.png"));
          vax = "Desea Grabar Producto \n( " + cod + " ) ?";
          String[] options = {"SI", "NO"};
          int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
          if (opcion == 0) {
            if (can == 0) {
              model.addRow(new Object[]{cod, nom, MtoEs(pum, 2), MtoEs(pud, 2), MtoEs(stk, 0)});  // add jtable
              MarcarColumna();
              System.out.println("sss");
            } else {
              int row = tabPrd.getSelectedRow();    // Fila Selecciuonada
              int col = tabPrd.getSelectedColumn(); // Columna Seleccionada
              // Presenta Seleccion
              if ((row != -1) && (col != -1)) {
                model.setValueAt(nom, tabPrd.getSelectedRow(), 1);
                model.setValueAt(MtoEs(pum, 2), tabPrd.getSelectedRow(), 2);
                model.setValueAt(MtoEs(pud, 2), tabPrd.getSelectedRow(), 3);
                model.setValueAt(MtoEs(stk, 0), tabPrd.getSelectedRow(), 4);
              }

            }
            st.execute(sql);

            // Actualiza Capacidad/um y Referencia
            String tx1 = "";
            tx1 = txtEmp.getText().toUpperCase().trim();
            if (tx1.length() > 40) {
              tx1 = tx1.substring(0, 40);
            }

            String ref = "";
            ref = txtRef.getText().toUpperCase().trim();
            if (ref.length() > 20) {
              ref = ref.substring(0, 20);
            }

            double por = 0;
            if (app.equals("1")) {
              por = 1;
            }

            int c = 0;

            sql = "SELECT count(*) cant "
                    + "FROM empaqueproducto "
                    + "where cop =  '" + cod + "' ";
            rs = st.executeQuery(sql);
            while (rs.next()) {
              c = rs.getInt("cant");
            }

            if (c == 0) {  // Incluir
              sql = "Insert into empaqueproducto "
                      + "(cop,tx1,unm,ref,por,can) "
                      + "VALUES ("
                      + "'" + cod + "',"
                      + "'" + tx1 + "',"
                      + "'" + unm + "',"
                      + "'" + ref + "',"
                      + "" + por + ","
                      + "1" + ")";
            } else {
              sql = "Update empaqueproducto "
                      + "set "
                      + "tx1='" + tx1 + "',"
                      + "ref='" + ref + "',"
                      + "unm='" + unm + "',"
                      + "por=" + por + " "
                      + "where cop='" + cod + "'";
            }
            st.execute(sql);

            bloqueaCampos();
            if (can == 0) {
              labMsgL.setText("- Se creo Registro -");
              cbDep.setSelectedIndex(-1);
              man = "0";
            } else {
              labMsgL.setText("- Se Grabo Registro -");
            }

          }

          //ListaDatos();
          indcbDep = 0;
          st.close();
          rs.close();
        }
        con.close();
      } catch (SQLException ex) {
        Logger.getLogger(Registro_CatalogoProductos.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }//GEN-LAST:event_btnGraActionPerformed

  public void MarcarColumna() {
    int rows = tabPrd.getRowCount();    // Cantidad Filas
    String cox = "";
    //Recorre filas (rows)
    for (int i = 0; i < rows; i++) {
      cox = tabPrd.getValueAt(i, 0).toString().trim();
      if (cox.equals(cod)) {
        tabPrd.getSelectionModel().setSelectionInterval(i, i); // Marca fila como seleccionada 
      }
    }
  }

  private void txtNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomActionPerformed
    if (man.equals("0")) {
      txtEmp.requestFocus();
    } else {
      txtPum.requestFocus();
    }
  }//GEN-LAST:event_txtNomActionPerformed

  private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
    cod = txtCod.getText().trim();
    txtCod.setText(cod.toUpperCase());
    if (BuscaDatos(cod)) {
      labMsgL.setText("- Producto ya existe -");
    }
    txtNom.setText(" ");
    txtNom.requestFocus();
  }//GEN-LAST:event_txtCodActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    RegLis.dispose();
  }//GEN-LAST:event_jButton1ActionPerformed

  private void txtNomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNomMouseClicked
    nom = txtNom.getText().trim();
    if (nom.length() == 0) {
      cod = txtCod.getText().trim();
      BuscaDatos(cod);
    }
  }//GEN-LAST:event_txtNomMouseClicked

  private void txtCodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodMouseClicked
    labMsgL.setText("");
    txtCod.setSelectionStart(0);
    txtCod.setSelectionEnd(txtCod.getText().length());
  }//GEN-LAST:event_txtCodMouseClicked

  private void txtPudMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPudMouseClicked
    if (txtPud.getText().length() > 0) {
      txtPud.setEnabled(true);
      txtPud.setSelectionStart(0);
      txtPud.setSelectionEnd(txtPud.getText().length());
      txtPud.requestFocus();
    }
  }//GEN-LAST:event_txtPudMouseClicked

  private void txtPudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPudActionPerformed
    txtStk.requestFocus();
  }//GEN-LAST:event_txtPudActionPerformed

  private void cbDepItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbDepItemStateChanged
    if (indcbDep == 0) {
      dep = "";
      fil = "";
      int idx = cbDep.getSelectedIndex();
      if (idx >= 0 && evt.getSource() == cbDep && evt.getStateChange() == 1) {
        String str = (String) cbDep.getSelectedItem().toString();
        if (str != null) {
          str = str.trim();
          //System.out.println("indcbDep=" + indcbDep + ",dx=" + idx + ",evt=" + evt.getStateChange() + ",str=" + str);
          if (str.length() > 0) {
            dep = str;
          } else {
            fil = "??";
          }
          if (man.equals("0")) {
            ListaDatos();
            bloqueaCampos();
          }
          indcbDep = 0;
        }
      }
    }

  }//GEN-LAST:event_cbDepItemStateChanged

  private void cbDepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDepActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbDepActionPerformed

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
          fil = "";
          ListaDatos();
          txtBus.setText(" ");
          txtBus.requestFocus();
        }
      }
    }
  }//GEN-LAST:event_cbActItemStateChanged

  private void cbActActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbActActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbActActionPerformed

  private void txtBusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBusMouseClicked
    cbDep.setSelectedIndex(-1);
    model.setRowCount(0);
    bloqueaCampos();
    if (evt.getClickCount() == 2) {
      fil = "";
      ListaDatos();
    } else {
      if (txtBus.getText().length() == 0) {
        fil = "??";
        ListaDatos();
      }
    }
    txtBus.setText(" ");
    txtBus.requestFocus();
  }//GEN-LAST:event_txtBusMouseClicked

  private void txtBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtBusActionPerformed

  private void txtBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusKeyReleased
    fil = txtBus.getText().toUpperCase().trim();
    ListaDatos();
  }//GEN-LAST:event_txtBusKeyReleased

  private void txtPumMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPumMouseClicked
    if (txtPum.getText().length() > 0) {
      txtPum.setEnabled(true);
      txtPum.setSelectionStart(0);
      txtPum.setSelectionEnd(txtPum.getText().length());
      txtPum.requestFocus();
    }
  }//GEN-LAST:event_txtPumMouseClicked

  private void txtPumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPumActionPerformed
    txtPud.requestFocus();    // TODO add your handling code here:
  }//GEN-LAST:event_txtPumActionPerformed

  private void txtStkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtStkMouseClicked
    if (txtStk.getText().length() > 0) {
      txtStk.setEnabled(true);
      txtStk.setSelectionStart(0);
      txtStk.setSelectionEnd(txtStk.getText().length());
      txtStk.requestFocus();
    }
  }//GEN-LAST:event_txtStkMouseClicked

  private void txtStkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStkActionPerformed
    txtEmp.requestFocus();
  }//GEN-LAST:event_txtStkActionPerformed

  private void labImgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labImgMouseClicked
    String rut = "imgprd\\" + cod + ".png";
    if (FileExist(rut)) {
      // Ver Imagen Producto
      if (ImgPrd != null) {
        ImgPrd.dispose();
      }
      ImgPrd = new Consulta_ImagenProd(rut, cod);   // Paso Controller 
      ImgPrd.setVisible(true);
      ImgPrd.setExtendedState(NORMAL);
      ImgPrd.setAlwaysOnTop(true);
      ImgPrd.setVisible(true);
    }
  }//GEN-LAST:event_labImgMouseClicked

  private void btnCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCatActionPerformed

    System.out.println("pdfmay=" + pdfmay + ",pdfdel=" + pdfdet);

    Importadora imp = new Importadora();
    double por = imp.getPor();
    int opcion = 0;
    icon = new ImageIcon(getClass().getResource("/img/ok.png"));
    Object opc = JOptionPane.showInputDialog(null, "Seleccione Tipo Reporte",
            "TIPO REPORTE", JOptionPane.QUESTION_MESSAGE, icon,
            new Object[]{"", "CON " + MtoEs(por, 2).replace(",00", "") + "% DESCUENTO ", "SIN % DESCUENTO", "AMBAS"}, "");

    if (opc != null) {

      String op = String.valueOf(opc);

      if (op.length() > 0) {
        if (op.indexOf("CON") >= 0) {
          opcion = 0;
        }
        if (op.indexOf("SIN") >= 0) {
          opcion = 1;
          por = 0;
        }
        if (op.indexOf("AMBAS") >= 0) {
          opcion = 2;
        }

        if (opcion == 0 || opcion == 1 || opcion == 2) {
          labRutPdf.setBackground(new java.awt.Color(242, 247, 247));

          if (feclis.length() >= 8) {
            feclis = feclis.replace("/", "").replace("-", "");
          }
          labMsgL.setText("- Generando Catalogo Precios - Espere!");
          icon = new ImageIcon(getClass().getResource("/img/catalog2.png"));
          String tit = "* AVISO *";
          long tim = 1500;
          Toolkit.getDefaultToolkit().beep();
          String vax = "Generando Catalogo Precios";
          Mensaje msg = new Mensaje(vax, tit, tim, icon);
          String exi = "S";

          // Lista Mayor
          if (pdfmay.equals("S")) {
            if (opcion == 2) {
              por = imp.getPor();
              new Pdf_CatalogoPrecios("M", feclis, por, exi, dep);
              por = 0;
            }
            new Pdf_CatalogoPrecios("M", feclis, por, exi, dep);
          }
          // Lista Detal
          if (pdfdet.equals("S")) {
            if (opcion == 2) {
              por = imp.getPor();
              new Pdf_CatalogoPrecios("D", feclis, por, exi, dep);
              por = 0;
            }
            new Pdf_CatalogoPrecios("D", feclis, por, exi, dep);
          }
        }
      }
    }

  }//GEN-LAST:event_btnCatActionPerformed


  private void labMsgLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labMsgLMouseClicked

  }//GEN-LAST:event_labMsgLMouseClicked

  private void labRutPdfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labRutPdfMouseClicked
    labRutPdf.setBackground(new java.awt.Color(242, 247, 247));
    String vax = "rep\\pdf\\catalogo\\";
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
  }//GEN-LAST:event_labRutPdfMouseClicked

  private void labImgDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labImgDMouseClicked
    String vax = "imgdep\\";
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
  }//GEN-LAST:event_labImgDMouseClicked

  private void labCapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labCapMouseClicked
    //  Empaque
    if (regEmp != null) {
      regEmp.dispose();
    }
    regEmp = new Registro_EmpaqueProd();
    regEmp.setVisible(true);
    regEmp.setExtendedState(NORMAL);
    regEmp.setVisible(true);
  }//GEN-LAST:event_labCapMouseClicked

  private void labDepMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labDepMouseClicked
    // Comentarios Departamento
    if (comDep != null) {
      comDep.dispose();
    }
    comDep = new Registro_Departamento();   // Paso Controller 
    comDep.setVisible(true);
    comDep.setExtendedState(NORMAL);
    comDep.setVisible(true);
  }//GEN-LAST:event_labDepMouseClicked

  private void txtEmpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtEmpMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtEmpMouseClicked

  private void txtEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpActionPerformed
    txtRef.requestFocus();
  }//GEN-LAST:event_txtEmpActionPerformed

  private void labImgPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labImgPMouseClicked
    String vax = "imgprd\\";
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
  }//GEN-LAST:event_labImgPMouseClicked

  private void cbExiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbExiActionPerformed
    if (cbExi.isSelected() == true) {
      ine = "S";
    } else {
      ine = "N";
    }
    ListaDatos();
    txtBus.requestFocus();
  }//GEN-LAST:event_cbExiActionPerformed

  private void labImgPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labImgPMouseEntered
    labImgP.setToolTipText("Imagenes Productos cant = " + getCanImgPrd());
  }//GEN-LAST:event_labImgPMouseEntered

  private void labImgDMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labImgDMouseEntered
    labImgD.setToolTipText("Imagenes Departamentos cant = " + getCanImgDep());
  }//GEN-LAST:event_labImgDMouseEntered

  private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
    icon = new ImageIcon(getClass().getResource("/img/excel.jpg"));
    String vax = "Desea Generar el Excel ?";
    String[] options = {"SI", "NO"};
    int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
    if (opcion == 0) {
      String fec = feclis;
      if (fec.length() >= 8) {
        fec = fec.replace("/", "").replace("-", "");
        ImageIcon icon;
        icon = new ImageIcon(getClass().getResource("/img/excel.jpg"));
        String tit = "* AVISO *";
        long tim = 1000;
        Toolkit.getDefaultToolkit().beep();
        vax = "Generando Excel, espere...!";
        Mensaje msg = new Mensaje(vax, tit, tim, icon);
        new Reporte_ExcelListaPrecios(fec, 1);
      }
    }
  }//GEN-LAST:event_jLabel10MouseClicked

  private void cbStaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStaActionPerformed
    if (cbSta.isSelected() == true) {
      sta = "0";
    } else {
      sta = "1";
    }
    txtBus.requestFocus();
  }//GEN-LAST:event_cbStaActionPerformed

  private void cbImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbImgActionPerformed
    fil = "";
    if (cbImg.isSelected() == true) {
      sim = "S";
    } else {
      sim = "N";
    }
    ListaDatos();
    txtBus.requestFocus();
  }//GEN-LAST:event_cbImgActionPerformed

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

  private void txtRefMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRefMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtRefMouseClicked

  private void txtRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRefActionPerformed
    //txtPor.requestFocus();
  }//GEN-LAST:event_txtRefActionPerformed

  private void txtRefKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRefKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtRefKeyReleased

  private void cbPorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPorActionPerformed
    if (cbPor.isSelected() == true) {
      app = "0";   // con descuento
    } else {
      app = "1";   // Sin descuento
    }
  }//GEN-LAST:event_cbPorActionPerformed

  private void jLabel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseClicked
    ListaDatos();
    txtBus.requestFocus();
  }//GEN-LAST:event_jLabel20MouseClicked

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
    tabPrd.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
    tabPrd.getColumnModel().getColumn(col).setHeaderRenderer(headerRenderer);
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
    tabPrd.getColumnModel().getColumn(col).setCellRenderer(Alinea);
  }

  // Presenta Datos al pulsar mouse en detalle columna
  public void presenta() {
    indcbDep = 0;
    cod = model.getValueAt(tabPrd.getSelectedRow(), 0).toString();
    int[] sel = tabPrd.getSelectedRows();
    if (sel.length == 1) {
      BuscaDatos(cod);
    }
  }

  public void ListaDatos() {
    String str = "", str2 = "";
    if (ine.equals("S")) {
      str = "and stk <=0 ";
    }

    // Sin Productos sin descuento
    if (app.equals("1")) {
      str2 = "and cop in (select cop from EMPAQUEPRODUCTO where por=1) ";
    }

    //cbPor.setSelected(false);
    //app = "0";   // con descuento
    model.setRowCount(0);
    int can = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT cop,nom,pum,pud,stk,pag "
              + "FROM Listaprecios "
              + "where sta='" + act + "' "
              + "and (nom like '%" + fil + "%' or cop like '%" + fil + "%') "
              + "and dep like '%" + dep + "%' "
              + str
              + str2
              + "order by dep,cop";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        String cop = rs.getString("cop");
        nom = rs.getString("nom");
        pag = rs.getString("pag");
        pum = rs.getDouble("pum");
        pud = rs.getDouble("pud");
        stk = rs.getDouble("stk");

        if (stk < 0) {
          stk = 0;
        }
        // Imagen
        String img = "- No -";
        String fil = "imgprd\\" + cop + ".png";
        if (FileExist(fil)) {
          img = "";
        }
        int ind = 0;
        //System.out.println("sim=" + sim + ", img=" + img);
        if (sim.equals("S")) {
          ind = 1;
          if (img.length() > 0) {
            model.addRow(new Object[]{cop, nom, MtoEs(pum, 2), MtoEs(pud, 2), MtoEs(stk, 0), img});
            can++;
          }
        }
        if (ind == 0) {
          model.addRow(new Object[]{cop, nom, MtoEs(pum, 2), MtoEs(pud, 2), MtoEs(stk, 0), img});
          can++;
        }
      }
      labCan.setText("c = " + can);
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_CatalogoProductos.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  // Busca Datos  
  public boolean BuscaDatos(String cod) {
    int can = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      nom = "";
      String sql = "SELECT dep,cop,nom,pum,pud,stk ,man,sta "
              + "FROM Listaprecios "
              + "where cop =  '" + cod + "' ";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        dep = rs.getString("dep");
        nom = rs.getString("nom");
        man = rs.getString("man");
        sta = rs.getString("sta");
        pum = rs.getDouble("pum");
        pud = rs.getDouble("pud");
        stk = rs.getDouble("stk");
        txtCod.setText(cod);
        txtNom.setText("  " + nom);
        txtPum.setText(MtoEs(pum, 2));
        txtPud.setText(MtoEs(pud, 2));
        txtStk.setText(MtoEs(stk, 2));
        indcbDep = 1;  // no ejecuta cbDep
        cbDep.setSelectedItem(dep);
        indcbDep = 0;

        if (sta.equals("0")) {
          cbSta.setSelected(true);
        } else {
          cbSta.setSelected(false);
        }

        String img = "imgprd\\" + cod + ".png";
        if (FileExist(img)) {
          pintarImagen(labImg, img);
        } else {
          img = "imgcia\\SinImagen.png";
          pintarImagen(labImg, img);
        }
        txtNom.requestFocus();
        can = 1;
      }
      // Busca Empaque
      String tx1 = "";
      String ref = "";
      String unm = "";
      double por = 0;
      sql = "SELECT tx1,unm,ref,por "
              + "FROM empaqueproducto "
              + "where cop='" + cod + "'";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        tx1 = rs.getString("tx1");
        unm = rs.getString("unm");
        ref = rs.getString("ref");
        por = rs.getDouble("por");
      }
      txtEmp.setText("  " + tx1);
      txtRef.setText("  " + ref);
      cbUnm.setSelectedItem(unm);

      if (por == 0) {
        cbPor.setSelected(true);
      } else {
        cbPor.setSelected(false);
      }

      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_CatalogoProductos.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (can > 0) {
      // txtCod.setEnabled(false);
      desbloCampos();
      return true;
    } else {
      return false;
    }
  }

  public void desbloCampos() {
    //txtNom.setBackground(new java.awt.Color(255, 255, 255));
    txtEmp.setBackground(new java.awt.Color(255, 255, 255));
    txtNom.setEnabled(true);
    txtEmp.setEnabled(true);
    txtRef.setEnabled(true);
    cbUnm.setEnabled(true);
    if (man.contains("1")) {
      txtPum.setBackground(new java.awt.Color(255, 255, 255));
      txtPud.setBackground(new java.awt.Color(255, 255, 255));
      txtStk.setBackground(new java.awt.Color(255, 255, 255));
      txtPum.setEnabled(true);
      txtPud.setEnabled(true);
      txtStk.setEnabled(true);
    }
    btnGra.setEnabled(true);
    btnEli.setEnabled(true);
  }

  public void bloqueaCampos() {
    cbSta.setEnabled(false);
    //txtCod.setBackground(new java.awt.Color(237, 235, 235));
    //txtNom.setBackground(new java.awt.Color(237, 235, 235));
    txtPum.setBackground(new java.awt.Color(237, 235, 235));
    txtPud.setBackground(new java.awt.Color(237, 235, 235));
    txtStk.setBackground(new java.awt.Color(237, 235, 235));
    txtEmp.setBackground(new java.awt.Color(237, 235, 235));
    txtCod.setEnabled(false);
    txtNom.setEnabled(false);
    txtEmp.setEnabled(false);
    txtRef.setEnabled(false);
    txtPum.setEnabled(false);
    txtPud.setEnabled(false);
    txtStk.setEnabled(false);
    cbUnm.setEnabled(false);
    fil = "";
    cod = "";
    nom = "";
    emp = "";
    sta = "0";
    man = "0";
    pag = "0";
    pum = 0;
    pud = 0;
    stk = 0;
    labMsgL.setText("");
    txtBus.setText("");
    txtCod.setText("");
    txtNom.setText("");
    txtEmp.setText("");
    txtRef.setText("");
    txtPum.setText("");
    txtPud.setText("");
    txtStk.setText("");
    setImagenCia();
    btnGra.setEnabled(false);
    btnEli.setEnabled(false);
    txtBus.requestFocus();
  }

  public boolean validaCampos() {
    cod = txtCod.getText().toUpperCase().trim();
    nom = txtNom.getText().toUpperCase().trim();
    pum = 0;
    pud = 0;
    indok = 0;
    labMsgL.setText("");
    // Valida  
    if (cod.length() > 0) {
      if (cod.length() > 10) {
        indok = 0;
        labMsgL.setText(" - Codigo debe menor a 10 Digitos");
        txtCod.setSelectionStart(0);
        txtCod.setSelectionEnd(txtCod.getText().length());
        txtCod.requestFocus();
      } else {
        indok = 1;
      }
    } else {
      labMsgL.setText(" - Debe Ingresar Codigo Producto");
      txtCod.requestFocus();
    }
    //Valida Nombre
    if (indok == 1) {
      if (nom.length() == 0) {
        labMsgL.setText(" - Debe Ingresar Nombre Producto");
        txtNom.requestFocus();
        indok = 0;
      } else {
        if (nom.length() > 60) {
          nom = nom.substring(0, 60);
        }
      }
    }

    if (man.equals("1")) {
      //Valida Departamento
      if (indok == 1) {
        if (dep.length() == 0) {
          labMsgL.setText(" - Departamento en Blanco");
          txtNom.requestFocus();
          indok = 0;
        }
      }
      // Valida precio mayor
      if (indok == 1) {
        indok = 0;
        String mox = txtPum.getText();
        if (isNumeric(mox)) {
          mox = GetCurrencyDouble(mox);
          pum = GetMtoDouble(mox);
          indok = 1;
        } else {
          labMsgL.setText(" - Precio Mayor debe ser numerico");
          txtPum.setSelectionStart(0);
          txtPum.setSelectionEnd(txtPum.getText().length());
          txtPum.requestFocus();
        }
      }

      // Valida precio detal
      if (indok == 1) {
        indok = 0;
        String mox = txtPud.getText();
        if (isNumeric(mox)) {
          mox = GetCurrencyDouble(mox);
          pud = GetMtoDouble(mox);
          indok = 1;
        } else {
          labMsgL.setText(" - Precio Detal debe ser numerico");
          txtPud.setSelectionStart(0);
          txtPud.setSelectionEnd(txtPud.getText().length());
          txtPud.requestFocus();
        }
      }

      // Valida stock
      if (indok == 1) {
        indok = 0;
        String mox = txtStk.getText();
        if (isNumeric(mox)) {
          mox = GetCurrencyDouble(mox);
          stk = GetMtoDouble(mox);
          indok = 1;
        } else {
          labMsgL.setText(" - Stock debe ser numerico");
          txtStk.setSelectionStart(0);
          txtStk.setSelectionEnd(txtStk.getText().length());
          txtStk.requestFocus();
        }
      }
    }

    if (indok == 1) {
      return true;
    } else {
      return false;
    }
  }

  public void cargaUnm() {
    // ObservableList modelo tabla unidmed
    ObservableList<UnidadMedida> obsUnm;
    UnidadMedida u = new UnidadMedida();
    obsUnm = u.getObsListUnidmed();
    cbUnm.removeAllItems();
    cbUnm.addItem("");
    for (UnidadMedida unm : obsUnm) {
      cbUnm.addItem(unm.getUnm());
    }
    cbUnm.setSelectedIndex(-1);
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
      java.util.logging.Logger.getLogger(Registro_CatalogoProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    /*
     /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registro_CatalogoProductos().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnCat;
  private javax.swing.JButton btnEli;
  private javax.swing.JButton btnGra;
  private javax.swing.JButton btnNvo;
  private javax.swing.JComboBox cbAct;
  private javax.swing.JComboBox cbDep;
  private javax.swing.JCheckBox cbExi;
  private javax.swing.JCheckBox cbImg;
  private javax.swing.JCheckBox cbPor;
  private javax.swing.JCheckBox cbSta;
  private javax.swing.JComboBox cbUnm;
  private javax.swing.JButton jButton1;
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
  private javax.swing.JLabel labCap;
  private javax.swing.JLabel labDep;
  private javax.swing.JLabel labFec;
  private javax.swing.JLabel labImg;
  private javax.swing.JLabel labImgD;
  private javax.swing.JLabel labImgP;
  public static javax.swing.JLabel labMsgL;
  public static javax.swing.JLabel labRutPdf;
  private javax.swing.JLabel labojo;
  public javax.swing.JTable tabPrd;
  private javax.swing.JTextField txtBus;
  private javax.swing.JTextField txtCod;
  private javax.swing.JTextField txtEmp;
  private javax.swing.JTextField txtNom;
  private javax.swing.JTextField txtPud;
  private javax.swing.JTextField txtPum;
  private javax.swing.JTextField txtRef;
  private javax.swing.JTextField txtStk;
  // End of variables declaration//GEN-END:variables
}

//ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(labImg.getWidth(), labImg.getWidth(), Image.SCALE_DEFAULT));
//labImg.setIcon(imageIcon);

