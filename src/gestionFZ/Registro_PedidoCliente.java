package gestionFZ;

import comun.Calculadora;
import static comun.MetodosComunes.FileExist;
import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.dmy_ymd;
import static comun.MetodosComunes.dmyhoy;
import static comun.MetodosComunes.isNumeric;
import static comun.MetodosComunes.pintarImagen;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymdhoy;
import static comun.MetodosComunes.ymdhoyhhmm;
import static gestionFZ.Menu.Calc;
import static gestionFZ.Menu.RegPed;
import static gestionFZ.Registro_CatalogoProductos.ImgPrd;
import java.awt.Color;
import static java.awt.Frame.NORMAL;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.ObservableList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import modelo.Clientes;
import modelo.EmpaqueProducto;
import modelo.Importadora;
import modelo.ListaPrecios;
import modelo.NotaEntrega;
import modelo.Pedidos;
import modelo.UnidadMedida;

public class Registro_PedidoCliente extends javax.swing.JFrame {

  int canp = 0;    // cant productos filtro
  int indp = 0;    // indicar pedido
  int npe = 0;     // numero de pedido
  int nne = 0;     // numero not/Entrega
  public static int indprm = 0;  // indicador promocion
  double can = 1;  // cantidad produdcto 
  double pum = 0;  // precio mayor producto
  double pud = 0;  // Precio detal prpducto
  double stk = 0;  // Precio detal prpducto
  double top = 0;  // Total Pedido
  double cnp = 0;  // Catidad Productos Pedido
  double por = 0;  // % descuento pronto pago
  double poi = 0;  // % iva
  double pre = 0;  // % retencion iva
  double ppm = 0;  // % descuento promocion

  String fil = "";   // filtro busqueda
  String fec = "";   // fecha pedido
  String fel = "";   // fecha lista
  String coc = "";   // codigo cliente
  String noc = "";   // nombre cliente
  String tip = "0";  // tipo cliente 0=May 1=Detal
  String fac = "0";  // indicador factura
  String cop = "";   // codigo producto
  String nop = "";   // descripcion producto
  String emp = "";   // Empaque Producto
  String ref = "";   // Referencia precio
  String unm = "";   // Unidad de Medida
  String obs = "";   // Observacion Pedido
  String fre = "";   // Fecha registro
  String fne = "";   // Fecha not/Entrega

  List<String> VecPrm; // Vector Cantidad  promocion

  ImageIcon icon;
  DefaultTableModel model;
  public static Consulta_Pedidos conPed;
  public static Consulta_MovProd conMov;
  public static Registro_PedidoCliente_Mod modPrd;
  public static Consulta_CanPromoc regPrm;

  Registro_PedidoCliente ctrP;    // defino instancia del controlador

  public Registro_PedidoCliente() {
    initComponents();
    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes
    Importadora imp = new Importadora();
    poi = imp.getIva();  // % Iva general
    ctrP = this; // Inicializo controller
    setImagenCia();
    cargaUnm();

    cbCte.setRenderer(new DefaultListCellRenderer() {
      public void paint(Graphics g) {
        setForeground(new java.awt.Color(102, 0, 0));
        super.paint(g);
      }
    });
    model = (DefaultTableModel) tabPrd.getModel();
    tabPrd.setRowHeight(25);//tamaño de las celdas
    tabPrd.setGridColor(new java.awt.Color(0, 0, 0));
    tabPrd.setSelectionBackground(new Color(151, 193, 215));
    tabPrd.setSelectionForeground(Color.blue);
    tabPrd.getTableHeader().setReorderingAllowed(true);  // activa movimiento columnas
    tabPrd.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    txtObs.setDisabledTextColor(Color.DARK_GRAY);
    txtFec.setDisabledTextColor(Color.DARK_GRAY);
    labCan.setBorder(new LineBorder(Color.BLUE));

    // Ajusta tamaño Columnas
    TableColumnModel columnModel = tabPrd.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(6);   // codigo  
    columnModel.getColumn(1).setPreferredWidth(500); // Descripcion
    columnModel.getColumn(2).setPreferredWidth(100); // Capacidad
    columnModel.getColumn(3).setPreferredWidth(6);   // Cantidad  
    columnModel.getColumn(4).setPreferredWidth(6);   // % Dsc 

    //Alinea Columnas Header
    AlinCampoH(0, 2); // 2=Cent
    AlinCampoH(1, 0); // 0=Izq
    AlinCampoH(2, 2); // 0=Izq
    AlinCampoH(3, 2); // 2=Cent
    AlinCampoH(4, 2); // 2=Cent

    //Alinea Columnas Detail
    AlinCampoD(0, 2); // 2=Cent
    AlinCampoD(1, 0); // 0=Izq
    AlinCampoD(2, 2); // 0=Izq
    AlinCampoD(3, 2); // 2=Cent
    AlinCampoD(4, 2); // 2=Cent

    if (fel.length() >= 8) {
      fel = imp.getFecLis().replace("/", "");
    }
    cargaClientes();
    cargaProductos();
    por = imp.getPor();   // % descuento ppago
    if (por > 0) {
      por = por / 100;
    }

    labFac.setText("CON FACTURA " + MtoEs(por * 100, 2).replace(",00", "") + "%");

    txtFec.setText(dmyhoy());
    fec = ymdhoy();
    jFec.setMaxSelectableDate(new Date());
    jFec.setFocusable(true);
    jFec.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().trim().equals("date")) {
          SimpleDateFormat ff = new SimpleDateFormat("dd-MM-yyyy");
          txtFec.setText(ff.format(jFec.getCalendar().getTime()));
          fec = (txtFec.getText().replaceAll("-", ""));
          if (fec.length() == 8) {
            fec = dmy_ymd(fec).replace("-", "");
            if (fec.compareTo(ymdhoy()) > 0) {
              txtFec.setText(dmyhoy());
              fec = ymdhoy();
              jFec.requestFocus();
            } else {
              updateHeader();
            }
          }
        }
      }
    });

    iniciaPedido();

    int nnx = 0;
    Pedidos p = new Pedidos();
    nnx = p.getMaxPed();
    //nnx = nnx + 1;
    txtPed.setText("" + nnx);
    txtPed.requestFocus();

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        RegPed.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        RegPed.dispose();
      }
    });

  }

  // Carga Unidad de medida
  public void cargaUnm() {
    ObservableList<UnidadMedida> obsUnm;  // ObservableList modelo tabla unidmed
    UnidadMedida u = new UnidadMedida();
    obsUnm = u.getObsListUnidmed();
    cbUnm.removeAllItems();
    cbUnm.addItem("");
    for (UnidadMedida unm : obsUnm) {
      cbUnm.addItem(unm.getUnm());
    }
  }

  // recibe numero pedido
  public void recibePedido(int nrp) {
    npe = nrp;
    txtPed.setText("" + npe);
    verifPedido();
  }

  // recibe numero pedido
  public void recibeCambiosProducto(int npx, String cox, String unx, double cax) {
    int row = tabPrd.getSelectedRow();    // Fila Selecciuonada
    int col = tabPrd.getSelectedColumn(); // Columna Seleccionada
    if ((row != -1) && (col != -1)) {
      model.setValueAt(MtoEs(cax, 2).replace(",00", ""), tabPrd.getSelectedRow(), 2);
      model.setValueAt(unx, tabPrd.getSelectedRow(), 3);
      // actualizar pedidoD
      Pedidos p = new Pedidos(npe, cop, unm, can, unx, cax);
      if (p.modificarPedidoD()) {
        PromocProducto(npe);
        labMsgP.setText("- Se Actualizo registro -");
      }
    }
  }

  public void setImagenCia() {
    String img = "imgcia\\logoimp2.png";
    pintarImagen(labImg, img);
  }

  public void bloqueaCampos() {
    labMsgP.setText("");
    txtCte.setEnabled(false);
    cbCte.setEnabled(false);
    txtPro.setEnabled(false);
    txtPrd.setEnabled(false);
    cbPrd.setEnabled(false);
    txtCan.setEnabled(false);
    cbUnm.setEnabled(false);
    txtObs.setEnabled(false);
    btnGra.setEnabled(false);
    btnEli.setEnabled(false);
    btnMod.setEnabled(false);
    txtCte.setText("");
    txtPrd.setText("");
    txtCan.setText("");
    txtObs.setText(" ");
    labTop.setText("");
    labCan.setText("");
    labReg.setText("");
    cbCte.setSelectedIndex(-1);
    cbPrd.setSelectedIndex(-1);
    setImagenCia();
    labTop.setBorder(new LineBorder(Color.GRAY));
    labRutPdfPed.setBackground(new java.awt.Color(242, 247, 247));
    labReg.setBackground(new java.awt.Color(204, 204, 204));
    labcte.setEnabled(true);
    labPrd.setEnabled(true);
    cbFac.setEnabled(true);

  }

  public void desbloqueaCampos() {
    labMsgP.setText("");
    if (indp == 0 && npe > 0) {
      txtCte.setEnabled(true);
      cbCte.setEnabled(true);
      txtPro.setEnabled(true);
    }
    txtPrd.setEnabled(true);
    cbPrd.setEnabled(true);
    txtCan.setEnabled(true);
    txtObs.setEnabled(true);
    btnGra.setEnabled(true);
    cbUnm.setEnabled(true);
    jFec.setEnabled(true);
    txtFec.setEnabled(true);
  }

  public void filtraCliente() {
    fil = txtCte.getText().toUpperCase().trim();
    cargaClientes();
  }

  public void cargaClientes() {
    cbCte.removeAllItems();
    cbCte.addItem("");
    String coc = "", noc = "";
    ObservableList<Clientes> obsClientes;  // ObservableList modelo tabla Clientes
    Clientes c = new Clientes();
    obsClientes = c.getObsListClientes(fil, "", "0");
    for (Clientes cte : obsClientes) {
      coc = cte.getCoc();
      noc = cte.getNom();
      noc = ' ' + coc + " - " + noc;
      cbCte.addItem(noc);
    }
    cbCte.setSelectedIndex(-1);
  }

  public void filtraProductos() {
    fil = txtPrd.getText().toUpperCase().trim();
    cargaProductos();
  }

  // Carga Lista productos Activos
  public void cargaProductos() {
    canp = 0;
    String format = " %1$-10s %2$-50s\n";
    String vax = "";
    cbPrd.removeAllItems();
    cbPrd.addItem("");
    // ObservableList modelo tabla ListaPrecios
    ObservableList<ListaPrecios> obsProductos;
    ListaPrecios p = new ListaPrecios();
    obsProductos = p.getCargaProductosAct(fil);
    for (ListaPrecios prd : obsProductos) {
      cop = prd.getCop();
      nop = prd.getNom();
      nop = nop.trim();
      vax = String.format(format, cop, nop);
      cbPrd.addItem(vax);
      canp = 1;
    }
    cbPrd.setSelectedIndex(-1);
  }

  public void eliminaPrd() {
    labMsgP.setText("");
    int[] sel = tabPrd.getSelectedRows();
    labMsgP.setText("");
    if (tabPrd.getRowCount() > 0) {
      if (tabPrd.getSelectedRow() >= 0) {
        icon = new ImageIcon(getClass().getResource("/img/elim.png"));
        String vax = "Desea Eliminar\n " + cop + " ?";
        if (sel.length > 1) {
          vax = "Desea Eliminar\nRegistros Seleccionados ?";
        }
        String[] options = {"SI", "NO"};
        int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
        if (opcion == 0) {
          //Borra Seleccionadas
          int numRows = tabPrd.getSelectedRows().length;
          for (int i = 0; i < numRows; i++) {
            cop = model.getValueAt(tabPrd.getSelectedRow(), 0).toString();
            model.removeRow(tabPrd.getSelectedRow());
            Pedidos p = new Pedidos(npe, cop);
            if (p.eliminarPedidoD()) {
              labMsgP.setText(" - Se elimino Producto " + cop);
              detallePedido(1);
            }
          }
        }
      } else {
        labMsgP.setText(" - Debe Seleccionar un registro");
      }
    } else {
      labMsgP.setText(" - Tabla Vacia");
    }
  }

  public void presenta(int ind) {
    cop = model.getValueAt(tabPrd.getSelectedRow(), 0).toString();
    nop = model.getValueAt(tabPrd.getSelectedRow(), 1).toString();
    int[] sel = tabPrd.getSelectedRows();
    if (ind == 2 && sel.length == 1) {
      modificaProducto();
    }
    if (ind == 1 && sel.length == 1) {
      String img = "imgprd\\" + cop + ".png";
      if (FileExist(img)) {
        pintarImagen(labImg, img);
      } else {
        img = "imgcia\\SinImagen.png";
        pintarImagen(labImg, img);
      }
    }

  }

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

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel2 = new javax.swing.JLabel();
    txtPed = new javax.swing.JTextField();
    jFec = new com.toedter.calendar.JDateChooser();
    txtFec = new javax.swing.JTextField();
    jScrollPane1 = new javax.swing.JScrollPane();
    tabPrd = new javax.swing.JTable();
    btnSal = new javax.swing.JButton();
    txtPrd = new javax.swing.JTextField();
    labMsgP = new javax.swing.JLabel();
    btnEli = new javax.swing.JButton();
    jLabCte = new javax.swing.JLabel();
    labcte = new javax.swing.JLabel();
    cbCte = new javax.swing.JComboBox();
    jLabPrd = new javax.swing.JLabel();
    labPrd = new javax.swing.JLabel();
    jLabPed = new javax.swing.JLabel();
    txtCte = new javax.swing.JTextField();
    cbPrd = new javax.swing.JComboBox();
    jLabPrv3 = new javax.swing.JLabel();
    jLabCan = new javax.swing.JLabel();
    txtCan = new javax.swing.JTextField();
    labBus = new javax.swing.JLabel();
    btnNvo1 = new javax.swing.JButton();
    labCan = new javax.swing.JLabel();
    jLabel14 = new javax.swing.JLabel();
    labtpe = new javax.swing.JLabel();
    labTop = new javax.swing.JLabel();
    jLabObs = new javax.swing.JLabel();
    txtObs = new javax.swing.JTextField();
    btnGra = new javax.swing.JButton();
    labEmp = new javax.swing.JLabel();
    labImg = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    labRutPdfPed = new javax.swing.JLabel();
    cbUnm = new javax.swing.JComboBox();
    labReg = new javax.swing.JLabel();
    labtpe1 = new javax.swing.JLabel();
    btnCal = new javax.swing.JButton();
    jLabPrv4 = new javax.swing.JLabel();
    txtPro = new javax.swing.JTextField();
    jPanel1 = new javax.swing.JPanel();
    cbFac = new javax.swing.JCheckBox();
    labFac = new javax.swing.JLabel();
    btnMod = new javax.swing.JButton();
    labojo = new javax.swing.JLabel();
    jLabPrm = new javax.swing.JLabel();
    btnMov = new javax.swing.JButton();
    clockDigital1 = new elaprendiz.gui.varios.ClockDigital();
    jLabel3 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Pedidos");
    getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    jLabel2.setBackground(new java.awt.Color(217, 226, 226));
    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(0, 0, 153));
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/compras.png"))); // NOI18N
    jLabel2.setText(" PEDIDO CLIENTE  ");
    jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jLabel2.setOpaque(true);
    getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 11, 188, 40));

    txtPed.setBackground(new java.awt.Color(246, 245, 245));
    txtPed.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    txtPed.setForeground(new java.awt.Color(102, 0, 0));
    txtPed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtPed.setText(" ");
    txtPed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    txtPed.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtPed.setPreferredSize(new java.awt.Dimension(7, 30));
    txtPed.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtPedMouseClicked(evt);
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        txtPedMouseExited(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtPedMouseReleased(evt);
      }
    });
    txtPed.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtPedActionPerformed(evt);
      }
    });
    txtPed.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtPedKeyReleased(evt);
      }
    });
    getContentPane().add(txtPed, new org.netbeans.lib.awtextra.AbsoluteConstraints(381, 18, 114, 28));

    jFec.setForeground(new java.awt.Color(0, 0, 102));
    jFec.setToolTipText("Seleccione Dia a Procesar");
    jFec.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
    jFec.setPreferredSize(new java.awt.Dimension(42, 25));
    getContentPane().add(jFec, new org.netbeans.lib.awtextra.AbsoluteConstraints(561, 18, -1, -1));

    txtFec.setBackground(new java.awt.Color(252, 247, 228));
    txtFec.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtFec.setForeground(new java.awt.Color(51, 0, 153));
    txtFec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtFec.setText(" ");
    txtFec.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    txtFec.setFocusable(false);
    txtFec.setPreferredSize(new java.awt.Dimension(10, 25));
    txtFec.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtFecMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtFecMouseReleased(evt);
      }
    });
    txtFec.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtFecActionPerformed(evt);
      }
    });
    txtFec.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtFecKeyReleased(evt);
      }
    });
    getContentPane().add(txtFec, new org.netbeans.lib.awtextra.AbsoluteConstraints(609, 18, 104, -1));

    tabPrd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    tabPrd.setForeground(new java.awt.Color(51, 51, 51));
    tabPrd.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "CODIGO", "PRODUCTO", "CANTIDAD", "UM", "%DSC"
      }
    ) {
      boolean[] canEdit = new boolean [] {
        false, false, false, false, false
      };

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    tabPrd.setToolTipText("Pulse click en el Producto para su Imagen");
    tabPrd.setRowHeight(25);
    tabPrd.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tabPrdMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(tabPrd);
    if (tabPrd.getColumnModel().getColumnCount() > 0) {
      tabPrd.getColumnModel().getColumn(0).setResizable(false);
      tabPrd.getColumnModel().getColumn(0).setPreferredWidth(6);
      tabPrd.getColumnModel().getColumn(1).setResizable(false);
      tabPrd.getColumnModel().getColumn(1).setPreferredWidth(450);
      tabPrd.getColumnModel().getColumn(2).setResizable(false);
      tabPrd.getColumnModel().getColumn(2).setPreferredWidth(20);
      tabPrd.getColumnModel().getColumn(3).setResizable(false);
      tabPrd.getColumnModel().getColumn(3).setPreferredWidth(6);
      tabPrd.getColumnModel().getColumn(4).setResizable(false);
      tabPrd.getColumnModel().getColumn(4).setPreferredWidth(6);
    }

    getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 226, 1040, 330));

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
      }
    });
    getContentPane().add(btnSal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, 89, 30));

    txtPrd.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtPrd.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    txtPrd.setText(" ");
    txtPrd.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtPrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtPrd.setPreferredSize(new java.awt.Dimension(7, 30));
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
    getContentPane().add(txtPrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 127, 143, -1));

    labMsgP.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
    labMsgP.setForeground(new java.awt.Color(204, 0, 0));
    labMsgP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labMsgP.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    getContentPane().add(labMsgP, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 610, 526, 30));

    btnEli.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnEli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/elim.png"))); // NOI18N
    btnEli.setMnemonic('E');
    btnEli.setToolTipText("Eliminar Registro Producto");
    btnEli.setBorder(null);
    btnEli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnEli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEliActionPerformed(evt);
      }
    });
    getContentPane().add(btnEli, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 190, 38, 30));

    jLabCte.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    jLabCte.setForeground(new java.awt.Color(0, 0, 102));
    jLabCte.setText("Cliente");
    jLabCte.setToolTipText("");
    jLabCte.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCteMouseClicked(evt);
      }
    });
    getContentPane().add(jLabCte, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 77, 104, 35));

    labcte.setBackground(new java.awt.Color(204, 204, 204));
    labcte.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labcte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    labcte.setToolTipText("Buscar Clientes");
    labcte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labcte.setOpaque(true);
    labcte.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labcteMouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        labcteMouseEntered(evt);
      }
    });
    getContentPane().add(labcte, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 82, 30, 30));

    cbCte.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
    cbCte.setForeground(new java.awt.Color(0, 0, 102));
    cbCte.setMaximumRowCount(26);
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
    getContentPane().add(cbCte, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 82, 528, -1));

    jLabPrd.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    jLabPrd.setForeground(new java.awt.Color(0, 0, 102));
    jLabPrd.setText("Producto");
    jLabPrd.setToolTipText("");
    jLabPrd.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabPrdMouseClicked(evt);
      }
    });
    getContentPane().add(jLabPrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 123, -1, 35));

    labPrd.setBackground(new java.awt.Color(204, 204, 204));
    labPrd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labPrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    labPrd.setToolTipText("Buscar Productos");
    labPrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labPrd.setOpaque(true);
    labPrd.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labPrdMouseClicked(evt);
      }
    });
    getContentPane().add(labPrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 128, 28, 30));

    jLabPed.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 16)); // NOI18N
    jLabPed.setForeground(new java.awt.Color(0, 0, 102));
    jLabPed.setText("No. Pedido");
    jLabPed.setToolTipText("");
    jLabPed.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabPedMouseClicked(evt);
      }
    });
    getContentPane().add(jLabPed, new org.netbeans.lib.awtextra.AbsoluteConstraints(224, 18, 101, 28));

    txtCte.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtCte.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    txtCte.setText(" ");
    txtCte.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtCte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtCte.setPreferredSize(new java.awt.Dimension(7, 30));
    txtCte.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtCteMouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        txtCteMouseEntered(evt);
      }
    });
    txtCte.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtCteActionPerformed(evt);
      }
    });
    txtCte.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtCteKeyReleased(evt);
      }
    });
    getContentPane().add(txtCte, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 82, 143, -1));

    cbPrd.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
    cbPrd.setForeground(new java.awt.Color(0, 0, 153));
    cbPrd.setMaximumRowCount(23);
    cbPrd.setMinimumSize(new java.awt.Dimension(32, 25));
    cbPrd.setOpaque(false);
    cbPrd.setPreferredSize(new java.awt.Dimension(32, 30));
    cbPrd.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbPrdItemStateChanged(evt);
      }
    });
    cbPrd.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        cbPrdMouseEntered(evt);
      }
    });
    cbPrd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbPrdActionPerformed(evt);
      }
    });
    getContentPane().add(cbPrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 128, 640, -1));

    jLabPrv3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    jLabPrv3.setForeground(new java.awt.Color(0, 0, 102));
    jLabPrv3.setText("%Prom");
    jLabPrv3.setToolTipText("Filtrar busqueda Proveedores");
    jLabPrv3.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabPrv3MouseClicked(evt);
      }
    });
    getContentPane().add(jLabPrv3, new org.netbeans.lib.awtextra.AbsoluteConstraints(852, 87, -1, -1));

    jLabCan.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    jLabCan.setForeground(new java.awt.Color(0, 0, 102));
    jLabCan.setText("Cantidad");
    jLabCan.setToolTipText("");
    jLabCan.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCanMouseClicked(evt);
      }
    });
    getContentPane().add(jLabCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, 30));

    txtCan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtCan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtCan.setText(" ");
    txtCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtCan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtCan.setPreferredSize(new java.awt.Dimension(7, 30));
    txtCan.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtCanMouseClicked(evt);
      }
    });
    txtCan.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtCanActionPerformed(evt);
      }
    });
    txtCan.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtCanKeyReleased(evt);
      }
    });
    getContentPane().add(txtCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 87, -1));

    labBus.setBackground(new java.awt.Color(204, 204, 204));
    labBus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labBus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    labBus.setToolTipText("Buscar Pedidos");
    labBus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labBus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labBus.setOpaque(true);
    labBus.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labBusMouseClicked(evt);
      }
    });
    getContentPane().add(labBus, new org.netbeans.lib.awtextra.AbsoluteConstraints(343, 18, 28, 28));

    btnNvo1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnNvo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pdf0.png"))); // NOI18N
    btnNvo1.setText("Generar Doc");
    btnNvo1.setToolTipText("Incluir nuevo Pedido");
    btnNvo1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnNvo1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        btnNvo1MouseClicked(evt);
      }
    });
    btnNvo1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnNvo1ActionPerformed(evt);
      }
    });
    getContentPane().add(btnNvo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 190, 130, 30));

    labCan.setBackground(new java.awt.Color(204, 204, 204));
    labCan.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    labCan.setForeground(new java.awt.Color(51, 51, 102));
    labCan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labCan.setText(" ");
    labCan.setToolTipText("Cliente con factura");
    labCan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labCan.setOpaque(true);
    getContentPane().add(labCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 190, 80, 28));

    jLabel14.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel14.setText("Can Prods");
    jLabel14.setToolTipText("Cliente con factura");
    getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 190, -1, 28));

    labtpe.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    labtpe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labtpe.setText("Total Pedido");
    labtpe.setToolTipText("Cliente con factura");
    getContentPane().add(labtpe, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 190, 96, 28));

    labTop.setBackground(new java.awt.Color(204, 204, 204));
    labTop.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    labTop.setForeground(new java.awt.Color(51, 51, 102));
    labTop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labTop.setText(" ");
    labTop.setToolTipText("Cliente con factura");
    labTop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labTop.setOpaque(true);
    getContentPane().add(labTop, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 190, 109, 28));

    jLabObs.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    jLabObs.setForeground(new java.awt.Color(102, 0, 51));
    jLabObs.setText("Nota");
    jLabObs.setToolTipText("Observacion Pedido Cliente");
    jLabObs.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabObsMouseClicked(evt);
      }
    });
    getContentPane().add(jLabObs, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, 49, 30));

    txtObs.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtObs.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    txtObs.setText(" ");
    txtObs.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtObs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtObs.setPreferredSize(new java.awt.Dimension(7, 30));
    txtObs.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtObsMouseClicked(evt);
      }
    });
    txtObs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtObsActionPerformed(evt);
      }
    });
    txtObs.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtObsKeyReleased(evt);
      }
    });
    getContentPane().add(txtObs, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 570, 880, -1));

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnGra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGra.setText("Grabar");
    btnGra.setToolTipText("Actualizar Cambios Pedido");
    btnGra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnGra.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGraActionPerformed(evt);
      }
    });
    getContentPane().add(btnGra, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 610, -1, 28));

    labEmp.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    labEmp.setForeground(new java.awt.Color(102, 102, 102));
    labEmp.setText(" ");
    getContentPane().add(labEmp, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 164, 650, -1));

    labImg.setText(" ");
    labImg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    labImg.setOpaque(true);
    labImg.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labImgMouseClicked(evt);
      }
    });
    getContentPane().add(labImg, new org.netbeans.lib.awtextra.AbsoluteConstraints(966, 71, 90, 90));

    jLabel1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Imagen");
    getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 53, 84, 20));

    labRutPdfPed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labRutPdfPed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rutpdf.png"))); // NOI18N
    labRutPdfPed.setToolTipText("Ruta Reportes Lista de Precios en Pdf");
    labRutPdfPed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labRutPdfPed.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labRutPdfPed.setOpaque(true);
    labRutPdfPed.setPreferredSize(new java.awt.Dimension(43, 30));
    labRutPdfPed.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labRutPdfPedMouseClicked(evt);
      }
    });
    getContentPane().add(labRutPdfPed, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 190, -1, -1));

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
    getContentPane().add(cbUnm, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 190, 68, -1));

    labReg.setBackground(new java.awt.Color(204, 204, 204));
    labReg.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    labReg.setForeground(new java.awt.Color(51, 51, 102));
    labReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labReg.setText(" ");
    labReg.setToolTipText("Cliente con factura");
    labReg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labReg.setOpaque(true);
    getContentPane().add(labReg, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 610, 126, 25));

    labtpe1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    labtpe1.setForeground(new java.awt.Color(51, 51, 102));
    labtpe1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labtpe1.setText("UM");
    labtpe1.setToolTipText("Cliente con factura");
    getContentPane().add(labtpe1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 190, 20, 28));

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
    getContentPane().add(btnCal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 610, 20, -1));

    jLabPrv4.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 16)); // NOI18N
    jLabPrv4.setForeground(new java.awt.Color(0, 0, 102));
    jLabPrv4.setText("Fecha");
    jLabPrv4.setToolTipText("");
    jLabPrv4.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabPrv4MouseClicked(evt);
      }
    });
    getContentPane().add(jLabPrv4, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 18, -1, 25));

    txtPro.setBackground(new java.awt.Color(234, 255, 255));
    txtPro.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtPro.setForeground(new java.awt.Color(0, 0, 153));
    txtPro.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtPro.setText(" ");
    txtPro.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtPro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtPro.setPreferredSize(new java.awt.Dimension(7, 30));
    txtPro.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtProMouseClicked(evt);
      }
    });
    txtPro.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtProActionPerformed(evt);
      }
    });
    txtPro.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtProKeyReleased(evt);
      }
    });
    getContentPane().add(txtPro, new org.netbeans.lib.awtextra.AbsoluteConstraints(912, 82, 40, 30));

    jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 1, true));

    cbFac.setBackground(new java.awt.Color(255, 255, 153));
    cbFac.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbFac.setOpaque(false);
    cbFac.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbFacActionPerformed(evt);
      }
    });

    labFac.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    labFac.setForeground(new java.awt.Color(153, 0, 51));
    labFac.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labFac.setText("CON FACTURA");
    labFac.setToolTipText("Cliente con factura");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(labFac, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(cbFac)
        .addGap(31, 31, 31))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(labFac, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(cbFac, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 10, 210, 40));

    btnMod.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnMod.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editar.png"))); // NOI18N
    btnMod.setToolTipText("Modificar Registro Producto");
    btnMod.setBorder(null);
    btnMod.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnMod.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    btnMod.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnModActionPerformed(evt);
      }
    });
    getContentPane().add(btnMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 190, 34, 30));

    labojo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labojo.setForeground(new java.awt.Color(102, 102, 102));
    labojo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojop.png"))); // NOI18N
    labojo.setToolTipText("Notificacion de Mensaje");
    labojo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    getContentPane().add(labojo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 610, 27, 30));

    jLabPrm.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabPrm.setForeground(new java.awt.Color(0, 0, 102));
    jLabPrm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabPrm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    jLabPrm.setText("Promocion");
    jLabPrm.setToolTipText("Productos con Promocion x Cantidades");
    jLabPrm.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 1, true));
    jLabPrm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabPrm.setOpaque(true);
    jLabPrm.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabPrmMouseClicked(evt);
      }
    });
    getContentPane().add(jLabPrm, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 570, 110, 28));

    btnMov.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    btnMov.setForeground(new java.awt.Color(51, 51, 51));
    btnMov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/grafic.png"))); // NOI18N
    btnMov.setText("Mov");
    btnMov.setToolTipText("Consulta Movimiento Producto");
    btnMov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnMov.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnMovActionPerformed(evt);
      }
    });
    getContentPane().add(btnMov, new org.netbeans.lib.awtextra.AbsoluteConstraints(741, 18, -1, 28));

    clockDigital1.setForeground(new java.awt.Color(0, 0, 51));
    clockDigital1.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
    clockDigital1.setPreferredSize(new java.awt.Dimension(120, 18));
    clockDigital1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        clockDigital1MouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        clockDigital1MouseEntered(evt);
      }
    });
    getContentPane().add(clockDigital1, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 50, 70, 20));

    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/FondoMain.png"))); // NOI18N
    jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 1060, 650));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void txtPedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPedMouseClicked
    iniciaPedido();
  }//GEN-LAST:event_txtPedMouseClicked

  private void txtPedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPedActionPerformed
    verifPedido();
  }//GEN-LAST:event_txtPedActionPerformed

  public void iniciaPedido() {
    nne = 0;
    fne = "";
    cbFac.setSelected(false);
    fac = "1";
    cbCte.setBorder(null);
    txtFec.setText(dmyhoy());
    fec = ymdhoy();
    model.setRowCount(0);
    bloqueaCampos();
    jLabPrm.setVisible(false);
    txtPed.setText("");
    txtPro.setText("");
    txtPed.setSelectionStart(0);
    txtPed.setSelectionEnd(txtPed.getText().length());
    txtPed.requestFocus();
    labRutPdfPed.setBackground(new java.awt.Color(242, 247, 247));
  }

  public void verifPedido() {
    setImagenCia();
    labRutPdfPed.setBackground(new java.awt.Color(242, 247, 247));
    jLabCte.setText("Cliente");
    indp = 0;
    txtPro.setText("");
    model.setRowCount(0);
    String pex = txtPed.getText();
    if (pex.length() >= 10 || pex.equals("0")) {
      labMsgP.setText("- Debe Ingresar No Pedido");
      txtPed.setSelectionStart(0);
      txtPed.setSelectionEnd(txtPed.getText().length());
      txtPed.requestFocus();
    } else {
      if (isNumeric(pex)) {
        pex = pex.replace(".", "");
        npe = Integer.valueOf(pex);
        Importadora imp = new Importadora();
        int np1 = imp.getNp1();
        int np2 = imp.getNp2();

        if (npe < np1 || npe > np2) {
          labMsgP.setText("- No. Pedido invalido - Rango  ( " + np1 + " - " + np2 + " )");
          txtPed.setSelectionStart(0);
          txtPed.setSelectionEnd(txtPed.getText().length());
          txtPed.requestFocus();

        } else {

          //busTipFacCliente();
          if (buscaPedido()) {

            busTipFacCliente();
            detallePedido(0);
            PromocProducto(npe);

            String nox = " " + coc + " - " + noc.trim();
            cbCte.setSelectedItem(nox);
            txtPro.setText(MtoEs(ppm, 2).replace(",00", ""));
            txtFec.setText(ymd_dmy(fec).replace("/", "-"));
            txtObs.setText(" " + obs);

            // Busca Not/Ent - Pedido Cerrado
            if (buscaNotEnt()) {

              labcte.setEnabled(false);
              labPrd.setEnabled(false);
              txtCte.setEnabled(false);
              cbCte.setEnabled(false);
              txtPro.setEnabled(false);
              txtPrd.setEnabled(false);
              cbPrd.setEnabled(false);
              cbUnm.setEnabled(false);
              txtObs.setEnabled(false);
              jFec.setEnabled(false);
              txtFec.setEnabled(false);
              cbFac.setEnabled(false);
              btnEli.setEnabled(false);
              btnMod.setEnabled(false);
              btnGra.setEnabled(false);
              int i = 0;
              tabPrd.getSelectionModel().setSelectionInterval(i, i); // Marca fila como seleccionada 
              Rectangle oRect = tabPrd.getCellRect(i, i, true);
              tabPrd.scrollRectToVisible(oRect);

              labMsgP.setText("-  Pedido Cerrado con Nota Entrega No.   " + nne + "   el   " + ymd_dmy(fne));
              tabPrd.getSelectionModel().clearSelection();  // Quitar Seleccion 
              btnSal.requestFocus();

            } else {

              int c = tabPrd.getRowCount();
              if (tabPrd.getRowCount() > 0) {
                indp = 1;
                desbloqueaCampos();
                txtCte.setEnabled(false);
                cbCte.setEnabled(false);
                //txtPro.setEnabled(false);
                cbCte.setBorder(new LineBorder(Color.DARK_GRAY));
                txtPrd.requestFocus();
              } else {
                eliminaHeader();
                bloqueaCampos();
                txtCte.setEnabled(true);
                cbCte.setEnabled(true);
                txtPro.setEnabled(true);
              }
            }

          } else {
            if (buscaNotEnt()) {
              String nox = " " + coc + " - " + noc;
              cbCte.setSelectedItem(nox);
              labMsgP.setText("-  Pedido Cerrado con Nota Entrega No.   " + nne + "   el   " + ymd_dmy(fne));
              tabPrd.getSelectionModel().clearSelection();  // Quitar Seleccion 
            } else {
              ultimoPedido();
              txtCte.setText(" ");
              txtCte.setEnabled(true);
              cbCte.setEnabled(true);
              txtPro.setEnabled(true);
              txtCte.requestFocus();
              btnEli.setEnabled(false);
              btnMod.setEnabled(false);
            }
          }

        }

      } else {
        labMsgP.setText("- No. Pedido debe ser numerico");
        txtPed.setSelectionStart(0);
        txtPed.setSelectionEnd(txtPed.getText().length());
        txtPed.requestFocus();
      }
    }
  }

  public boolean ultimoPedido() {
    int nnx = 0;
    Pedidos p = new Pedidos();
    nnx = p.getMaxPed();
    if (nnx > npe) {
      labMsgP.setText("- No Pedido debe ser mayor al ultimo " + nnx);
      return true;
    } else {
      return false;
    }
  }

  private void txtFecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFecMouseClicked
    //jDesde.setSelectionStart(0);
    //jDesde.setSelectionEnd(5);
  }//GEN-LAST:event_txtFecMouseClicked

  private void txtFecMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFecMouseReleased

  }//GEN-LAST:event_txtFecMouseReleased

  private void txtFecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFecActionPerformed

  }//GEN-LAST:event_txtFecActionPerformed

  private void txtFecKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFecKeyReleased

  }//GEN-LAST:event_txtFecKeyReleased

  private void tabPrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPrdMouseClicked
    if (evt.getClickCount() == 1) {
      presenta(1);
    }
    if (evt.getClickCount() == 2) {
      presenta(2);
    }
  }//GEN-LAST:event_tabPrdMouseClicked

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    RegPed.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void txtPrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPrdMouseClicked
    if (nne == 0) {
      setImagenCia();
      txtPrd.setText(" ");
      txtCan.setText(" ");
      filtraProductos();
      cbPrd.setSelectedIndex(-1);
      txtPrd.requestFocus();
      txtCan.setEnabled(false);
      cbUnm.setEnabled(false);
    }
  }//GEN-LAST:event_txtPrdMouseClicked

  private void txtPrdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrdActionPerformed
    setImagenCia();
    cop = txtPrd.getText().toUpperCase().trim();
    ListaPrecios l = new ListaPrecios();
    nop = l.getProducto(cop);
    if (nop.length() > 0) {
      txtCan.setText("1");
      txtCan.requestFocus();
      cbPrd.setSelectedIndex(1);
      desbloqueaCampos();
    } else {
      // abre busqueda
      if (canp == 1) {
        cbPrd.setPopupVisible(true);
        cbPrd.setSelectedIndex(0);
        cbPrd.requestFocus();
      } else {
        txtPrd.setSelectionStart(0);
        txtPrd.setSelectionEnd(txtPrd.getText().length());
        txtPrd.requestFocus();
      }
    }
  }//GEN-LAST:event_txtPrdActionPerformed

  public boolean buscaPedido() {
    coc = "";
    // ObservableList modelo tabla pedidoH
    ObservableList<Pedidos> obsPedidoH;
    Pedidos p = new Pedidos();
    obsPedidoH = p.getBuscaPedidoH(npe);
    for (Pedidos ped : obsPedidoH) {
      coc = ped.getCoc();
      noc = ped.getNoc();
      tip = ped.getTip();
      obs = ped.getObs();
      fec = ped.getFep();
      fel = ped.getFel();
      fac = ped.getFac();
      ppm = ped.getPpm();
      // Con factura/sin factura   descuento ppago
      if (fac.equals("0")) {
        cbFac.setSelected(true);
      } else {
        cbFac.setSelected(false);
      }
      if (tip.equals("1")) {
        jLabCte.setText("Cliente (D)");
      } else {
        jLabCte.setText("Cliente (M)");
      }
    }
    if (coc.length() > 0) {
      return true;
    } else {
      return false;
    }
  }

  public boolean buscaNotEnt() {
    nne = 0;
    // ObservableList modelo tabla notent
    ObservableList<NotaEntrega> obsNotEnt;
    NotaEntrega n = new NotaEntrega();
    obsNotEnt = n.getDatNotEnPed(npe);
    for (NotaEntrega noe : obsNotEnt) {
      nne = noe.getNne();
      fne = noe.getFne();
    }
    if (nne > 0) {
      return true;
    } else {
      return false;
    }
  }

  // Lista Productos
  public void PromocProducto(int npe) {
    indprm = 0;
    VecPrm = new ArrayList<>();
    String grp = "";
    double can = 0, cat = 0, por = 0;
    ObservableList<Pedidos> obsPedidoD;  // ObservableList modelo tabla pedidoD
    Pedidos p = new Pedidos();
    obsPedidoD = p.getPromocProd(npe);
    for (Pedidos ped : obsPedidoD) {
      npe = ped.getNpe();
      grp = ped.getGrp();
      por = ped.getPor();
      cat = ped.getCat();
      can = ped.getCan();
      String vax = grp + ";" + por + ";" + cat + ";" + can;
      VecPrm.add(vax);
      if (can >= cat) {
        indprm = 1;
      }
    }
    jLabPrm.setVisible(false);
    jLabPrm.setBackground(new java.awt.Color(242, 247, 247));
    if (!VecPrm.isEmpty()) {
      jLabPrm.setVisible(true);
      if (indprm == 1) {
        jLabPrm.setBackground(Color.green);
      }
    }
  }

  // Lista Produxtos
  public void detallePedido(int ind) {
    top = 0;
    cnp = 0;
    double pop = 0;
    int cc = 0;

    // ObservableList modelo tabla pedidoD
    ObservableList<Pedidos> obsPedidoD;
    Pedidos p = new Pedidos();
    obsPedidoD = p.getListPedidoD(npe);
    for (Pedidos ped : obsPedidoD) {
      cop = ped.getCop();
      nop = ped.getDep();
      unm = ped.getUnm();
      can = ped.getCan();
      pum = ped.getPrm();
      pud = ped.getPrd();
      pop = ped.getPor();

      // Tipo cliente 
      double prc = 0;
      if (tip.equals("0")) {
        prc = pum;  // mayorista
      } else {
        prc = pud;  // Detal
      }
      //  descuento Promocion producto
      if (pop > 0) {
        prc = prc - (prc * (pop / 100));
      } else {
        //  descuento Promocion global
        if (ppm > 0) {
          prc = prc - (prc * (ppm / 100));
        } else {
          //  descuento ppago
          if (fac.equals("0")) {
            prc = prc - (prc * por);
          }
        }
      }
      String mox = MtoEs(prc, 2);
      mox = GetCurrencyDouble(mox);
      prc = GetMtoDouble(mox);

      String pox = "";
      if (pop > 0) {
        pox = MtoEs(pop, 2).replace(",00", "") + "%";
      }

      top = (top + (prc * can));
      cnp = cnp + can;
      if (ind == 0) {
        model.addRow(new Object[]{cop, nop, MtoEs(can, 2).replace(",00", ""), unm, pox});
      }
      cc++;

    }

    if (tabPrd.getRowCount() > 0) {
      btnEli.setEnabled(true);
      btnMod.setEnabled(true);
      ultimoProducto();
    } else {
      btnEli.setEnabled(false);
      btnMod.setEnabled(false);
    }

    labtpe.setText("Total Pedido");
    labTop.setBorder(new LineBorder(Color.GRAY));
    if (fac.equals("0")) {
      labtpe.setText("T.Pedido Fact");
      labTop.setBorder(new LineBorder(Color.RED));
    }
    labTop.setText(MtoEs(top, 2));
    labCan.setText(MtoEs(cnp, 2).replace(",00", ""));
    labReg.setText("Cant Reg = " + MtoEs(cc, 2).replace(",00", ""));
    labReg.setBackground(new java.awt.Color(242, 247, 247));
    if (cc >= 30) {
      labReg.setBackground(Color.yellow);
    }

  }

  public void ultimoProducto() {
    int i = tabPrd.getRowCount() - 1;    // Cantidad Filas
    tabPrd.getSelectionModel().setSelectionInterval(i, i); // Marca fila como seleccionada 
    Rectangle oRect = tabPrd.getCellRect(i, 0, true);
    tabPrd.scrollRectToVisible(oRect);
  }

  private void cbFacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFacActionPerformed
    if (cbFac.isSelected() == true) {
      fac = "0";
      Importadora imp = new Importadora();
      por = imp.getPor();   // % descuento ppago
      if (por > 0) {
        por = por / 100;
      }
      labFac.setText("CON FACTURA " + MtoEs(por * 100, 2).replace(",00", "") + "%");
    } else {
      labFac.setText("SIN FACTURA ");
      por = 0;
      fac = "1";
    }
    detallePedido(1);
    updateHeader();
    txtCte.requestFocus();
  }//GEN-LAST:event_cbFacActionPerformed

  private void btnEliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliActionPerformed
    if (cop.length() > 0) {
      eliminaPrd();
    }
    txtPrd.requestFocus();
  }//GEN-LAST:event_btnEliActionPerformed

  private void jLabCteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCteMouseClicked

  }//GEN-LAST:event_jLabCteMouseClicked

  private void labcteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labcteMouseClicked
    if (txtCte.isEnabled() && npe > 0 && nne == 0) {
      cbCte.setPopupVisible(true);
    }
  }//GEN-LAST:event_labcteMouseClicked

  private void cbCteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbCteItemStateChanged
    coc = "";
    noc = "";
    int idx = cbCte.getSelectedIndex();
    if (idx > 0 && evt.getSource() == cbCte && evt.getStateChange() == 1) {
      String sel = (String) cbCte.getSelectedItem();  //valor item seleccionado
      if (sel != null) {
        if (sel.length() > 0) {
          int x = sel.indexOf("-");
          if (x >= 0) {
            coc = sel.substring(0, x).trim();
            noc = sel.substring(x + 1, sel.length()).trim();
            if (top == 0) {
              busTipFacCliente();
            }
            desbloqueaCampos();
            txtCan.setEnabled(false);
            cbUnm.setEnabled(false);
            txtCte.setText(" ");
            txtPrd.setText(" ");
            txtPrd.requestFocus();
            if (indp == 1) {
              updateHeader();
            }
          }
        }
      }
    } else {
      txtCte.requestFocus();
    }
  }//GEN-LAST:event_cbCteItemStateChanged

  private void cbCteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbCteMouseEntered
    //cbCte.setPopupVisible(true);
  }//GEN-LAST:event_cbCteMouseEntered

  private void cbCteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCteActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbCteActionPerformed

  private void jLabPrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabPrdMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabPrdMouseClicked

  private void labPrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labPrdMouseClicked
    if (txtPrd.isEnabled() && npe > 0 && nne == 0) {
      cbPrd.setPopupVisible(true);
    }
  }//GEN-LAST:event_labPrdMouseClicked

  private void jLabPedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabPedMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabPedMouseClicked

  private void txtCteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCteMouseClicked
    setImagenCia();
    if (indp == 0 && npe > 0) {
      filtraCliente();
      cbCte.setSelectedIndex(-1);
      txtCte.requestFocus();
      bloqueaCampos();
      txtCte.setEnabled(true);
      cbCte.setEnabled(true);
      txtPro.setEnabled(true);
      txtCte.setText(" ");
      txtCte.requestFocus();
    }
  }//GEN-LAST:event_txtCteMouseClicked

  private void txtCteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCteActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCteActionPerformed

  private void cbPrdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbPrdItemStateChanged
    cop = "";
    nop = "";
    emp = "";
    ref = "";
    unm = "UND";
    int idx = cbPrd.getSelectedIndex();
    labEmp.setText("");
    if (idx > 0 && evt.getSource() == cbPrd && evt.getStateChange() == 1) {
      String sel = (String) cbPrd.getSelectedItem();
      if (sel != null) {
        if (sel.length() > 0) {
          //int x = sel.indexOf("-");
          //if (x >= 0) {
          cop = sel.substring(1, 11).trim();
          nop = sel.substring(12, sel.length()).trim();
          String vax = "Prec May ";
          if (tip.equals("1")) {  // precio detal
            vax = "Prec Det ";
          }
          if (getEmpaque(cop)) {
            ListaPrecios l = new ListaPrecios();
            labEmp.setText(emp + "    Existencia     " + MtoEs(l.getExistencia(cop), 2).replace(",00", "") + "       " + vax + "    " + MtoEs(l.getPrecio(cop, tip, por), 2) + "        " + ref);
            //unm = "UND";
            cbUnm.setSelectedItem(unm);
            txtCan.setText("1");
          } else {
            unm = "UND";
            cbUnm.setSelectedItem(unm);
            txtCan.setText("1");
          }
          String img = "imgprd\\" + cop + ".png";
          if (FileExist(img)) {
            pintarImagen(labImg, img);
          } else {
            img = "imgcia\\SinImagen.png";
            pintarImagen(labImg, img);
          }
          desbloqueaCampos();
          txtPrd.setText(" ");
          txtCan.setSelectionStart(0);
          txtCan.setSelectionEnd(txtCan.getText().length());
          txtCan.requestFocus();
          // }
        } else {
          txtPrd.requestFocus();
        }
      }
    } else {
      txtPrd.requestFocus();
    }
  }//GEN-LAST:event_cbPrdItemStateChanged

  private void cbPrdMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbPrdMouseEntered
    // TODO add your handling code here:
  }//GEN-LAST:event_cbPrdMouseEntered

  private void cbPrdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPrdActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbPrdActionPerformed

  private void txtCteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCteMouseEntered

  }//GEN-LAST:event_txtCteMouseEntered

  private void txtCteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCteKeyReleased
    if (npe > 0) {
      filtraCliente();
    }
    //cbCte.firePopupMenuWillBecomeVisible();
  }//GEN-LAST:event_txtCteKeyReleased

  private void jLabPrv3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabPrv3MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabPrv3MouseClicked

  private void txtPrdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrdKeyReleased
    fil = txtPrd.getText().toUpperCase().trim();
    txtPrd.setText(fil);
    filtraProductos();
  }//GEN-LAST:event_txtPrdKeyReleased

  private void jLabCanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCanMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCanMouseClicked

  private void txtCanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCanMouseClicked
    txtCan.setSelectionStart(0);
    txtCan.setSelectionEnd(txtCan.getText().length());
    txtCan.requestFocus();
  }//GEN-LAST:event_txtCanMouseClicked

  private void txtCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCanActionPerformed
    setImagenCia();
    String cax = txtCan.getText();
    if (isNumeric(cax)) {
      cax = GetCurrencyDouble(cax);
      can = GetMtoDouble(cax);
      txtPrd.setText(" ");
      txtCan.setText(" ");
      txtPrd.requestFocus();
      if (coc.length() > 0) {
        grabaPedido(cax);
        PromocProducto(npe);
        detallePedido(1);
        txtCan.setEnabled(false);
        cbUnm.setEnabled(false);
        txtPrd.requestFocus();
      } else {
        labMsgP.setText("- debe colocar cliente");
        txtCte.requestFocus();
      }
    } else {
      labMsgP.setText("- Cantidad invalida");
      txtCan.setSelectionStart(0);
      txtCan.setSelectionEnd(txtCan.getText().length());
      txtCan.requestFocus();
    }

  }//GEN-LAST:event_txtCanActionPerformed

  // Tipo Cliente
  public void busTipFacCliente() {
    fac = "0";
    tip = "0";
    pre = 0;
    if (coc.length() > 0) {
      ObservableList<Clientes> obsClientes;  // ObservableList modelo tabla Clientes
      Clientes c = new Clientes();
      obsClientes = c.getObsTipCliente(coc);
      for (Clientes cte : obsClientes) {
        tip = cte.getTip();
        fac = cte.getFac();
        pre = cte.getPre();
        if (fac.equals("0")) {
          labFac.setText("CON FACTURA " + MtoEs(por * 100, 2).replace(",00", "") + "%");
          cbFac.setSelected(true);
        } else {
          labFac.setText("SIN FACTURA ");
          cbFac.setSelected(false);
        }
        if (tip.equals("1")) {
          jLabCte.setText("Cliente (D)");
        } else {
          jLabCte.setText("Cliente (M)");
        }
      }
    }
  }

  public void grabaPedido(String cax) {
    if (coc.length() > 0) {
      fec = fec.replace("O", "0");
      fec = fec.replace("o", "0");
      fec = fec.replace("|", "1");
      // Graba Header Pedido
      Pedidos p = new Pedidos();
      if (!p.existePedido(npe)) {
        // Crea Pedido
        double ppp = por;
        if (fac.equals("1")) {
          ppp = 0;
        }
        obs = txtObs.getText().trim();
        if (obs.length() > 160) {
          obs = obs.substring(0, 120);
        }
        fre = ymdhoyhhmm();
        p = new Pedidos(npe, coc, noc, tip, fac, fec, fel, obs, fre, poi, pre, ppp, ppm);
        if (p.insertarPedidoH()) {
          indp = 1;
          txtCte.setEnabled(false);
          cbCte.setEnabled(false);
          cbCte.setBorder(new LineBorder(Color.DARK_GRAY));
        }
      }
      // Busca precios mayor/detal
      pum = 0;
      pud = 0;
      // ObservableList modelo tabla ListaPrecios
      ObservableList<ListaPrecios> obsLisPrc;
      ListaPrecios l = new ListaPrecios();
      obsLisPrc = l.getPreciosProductos(cop);
      for (ListaPrecios lis : obsLisPrc) {
        pum = lis.getPum();
        pud = lis.getPud();
      }
      // Graba Detal Pedido
      if (cop.length() > 0 && can > 0) {
        p = new Pedidos(npe, cop, nop, unm, can, pum, pud);
        if (p.insertarPedidoD()) {
          model.addRow(new Object[]{cop, nop, MtoEs(can, 2).replace(",00", ""), unm});
          labMsgP.setText("- Se actualizo  ( " + cop + " )");
        }
      }
    } else {
      labMsgP.setText("- Debe colocar Cliente -");
    }
  }

  // Actualiza Header pedido
  public void updateHeader() {
    if (npe > 0) {
      obs = txtObs.getText().trim();
      if (obs.length() > 160) {
        obs = obs.substring(0, 160);
      }
      obs = obs.toUpperCase().trim();
      double ppp = por;
      if (fac.equals("1")) {
        ppp = 0;
      }
      String mox = txtPro.getText().trim();
      if (isNumeric(mox)) {
        mox = GetCurrencyDouble(mox);
        ppm = Integer.valueOf(mox);
      }
      // actualiza PedidoH
      Pedidos p = new Pedidos(npe, coc, noc, tip, fac, fec, obs, ppp, ppm);
      if (p.modificarPedidoH()) {
        labMsgP.setText("- Se Actualizo registro -");
      }
    }
  }

  // Actualiza Header pedido
  public void eliminaHeader() {
    Pedidos p = new Pedidos(npe);
    if (p.eliminarPedidoH()) {
      labMsgP.setText("- Se Elimino Pedido");
    }
  }

  // Busca empaque
  public boolean getEmpaque(String cop) {
    int c = 0;
    unm = "UND";
    // ObservableList modelo tabla EmpaqueProducto
    ObservableList<EmpaqueProducto> obsEmpPrd;
    EmpaqueProducto e = new EmpaqueProducto();
    obsEmpPrd = e.getEmpaquePrd(cop);
    for (EmpaqueProducto epq : obsEmpPrd) {
      emp = epq.getEmp();
      //if (selunm.equals("S")) {
      unm = epq.getUnm();
      //}
      ref = epq.getRef();
      cnp = epq.getCan();
      c = 1;
    }
    if (c == 1) {
      return true;
    } else {
      return false;
    }
  }


  private void txtCanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCanKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCanKeyReleased

  private void labBusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labBusMouseClicked
    //new ConsultaPedidos(ctrF).setVisible(true);
    iniciaPedido();
    Thread hilo = new Thread() {
      public void run() {
        labMsgP.setText("- BUSCANDO PEDIDOS ESPERE !");
        if (conPed != null) {
          conPed.dispose();
        }
        conPed = new Consulta_Pedidos(ctrP);
        conPed.setVisible(true);
        conPed.setExtendedState(NORMAL);
        conPed.setVisible(true);
      }
    };
    hilo.start();
  }//GEN-LAST:event_labBusMouseClicked

  private void btnNvo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNvo1ActionPerformed
    PromocProducto(npe);
    model.setRowCount(0);
    detallePedido(0);
    labRutPdfPed.setBackground(new java.awt.Color(242, 247, 247));
    if (npe > 0) {
      icon = new ImageIcon(getClass().getResource("/img/ok.png"));
      Object opc = JOptionPane.showInputDialog(null, "Seleccione Destino ",
              "GENERAR DOCUMENTO PEDIDO", JOptionPane.QUESTION_MESSAGE, icon,
              new Object[]{"- PARA GRUPO FZ", "- PARA EL CLIENTE"}, "");
      if (opc != null) {
        String op = String.valueOf(opc);
        if (op.length() > 0) {
          if (op.indexOf("GRUPO FZ") >= 0) {
            new PdF_PedidoCliente(npe, 1);
          }
          if (op.indexOf("CLIENTE") >= 0) {
            new PdF_PedidoCliente(npe, 0);
          }
        }
      }
    } else {
      labMsgP.setText(" - Debe Colocar Pedido");
    }
  }//GEN-LAST:event_btnNvo1ActionPerformed

  private void cbCteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbCteMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_cbCteMouseClicked

  private void jLabObsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabObsMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabObsMouseClicked

  private void txtObsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtObsMouseClicked

  }//GEN-LAST:event_txtObsMouseClicked

  private void txtObsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtObsActionPerformed
    obs = txtObs.getText().trim();
    updateHeader();
  }//GEN-LAST:event_txtObsActionPerformed

  private void txtObsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObsKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtObsKeyReleased

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed
    updateHeader();
    PromocProducto(npe);
    model.setRowCount(0);
    detallePedido(0);
  }//GEN-LAST:event_btnGraActionPerformed

  private void labImgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labImgMouseClicked
    String rut = "imgprd\\" + cop + ".png";
    if (FileExist(rut)) {
      // Ver Imagen Producto
      if (ImgPrd != null) {
        ImgPrd.dispose();
      }
      ImgPrd = new Consulta_ImagenProd(rut, cop);   // Paso Controller 
      ImgPrd.setVisible(true);
      ImgPrd.setExtendedState(NORMAL);
      ImgPrd.setAlwaysOnTop(true);
      ImgPrd.setVisible(true);
    }
  }//GEN-LAST:event_labImgMouseClicked

  private void btnNvo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNvo1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_btnNvo1MouseClicked

  private void labRutPdfPedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labRutPdfPedMouseClicked
    labRutPdfPed.setBackground(new java.awt.Color(242, 247, 247));
    String vax = "rep\\pdf\\pedidos\\";
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
  }//GEN-LAST:event_labRutPdfPedMouseClicked

  private void cbUnmItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbUnmItemStateChanged
    int idx = cbUnm.getSelectedIndex();
    if (idx >= 0 && evt.getSource() == cbUnm && evt.getStateChange() == 2) {
      String str = (String) cbUnm.getSelectedItem().toString();
      if (str != null) {
        if (str.length() > 0) {
          str = str.trim();
          unm = str;
          if (idx > 0) {
            actualizaUM();
          }
          txtCan.setSelectionStart(0);
          txtCan.setSelectionEnd(txtCan.getText().length());
          txtCan.requestFocus();
        }
      }
    }
  }//GEN-LAST:event_cbUnmItemStateChanged

  public void actualizaUM() {
    // actualiza empaqueproducto
    EmpaqueProducto e = new EmpaqueProducto(cop, unm);
    if (e.modificarUmEmp()) {
      labMsgP.setText("- Se Actualizo unm -");
    }
  }

  private void cbUnmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbUnmActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbUnmActionPerformed

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

  private void txtPedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPedKeyReleased
    //verifPedido();
  }//GEN-LAST:event_txtPedKeyReleased

  private void jLabPrv4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabPrv4MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabPrv4MouseClicked

  private void txtProMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtProMouseClicked
    txtPro.setSelectionStart(0);
    txtPro.setSelectionEnd(txtPro.getText().length());
  }//GEN-LAST:event_txtProMouseClicked

  private void txtProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProActionPerformed
    String mox = txtPro.getText().trim();
    if (isNumeric(mox)) {
      mox = GetCurrencyDouble(mox);
      ppm = Integer.valueOf(mox);
      if (ppm > 0) {
        txtObs.setText(" - DESCUENTO PROMOCION " + MtoEs(ppm, 2).replace(",00", "") + "%");
      } else {
        txtObs.setText(" ");
      }
      updateHeader();
      txtCan.requestFocus();
    } else {
      txtObs.setText(" ");
      ppm = 0;
      txtPro.requestFocus();
    }
  }//GEN-LAST:event_txtProActionPerformed

  private void txtProKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtProKeyReleased

  private void btnModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModActionPerformed
    modificaProducto();
  }//GEN-LAST:event_btnModActionPerformed

  private void jLabPrmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabPrmMouseClicked
    PromocProducto(npe);
    jLabPrm.setBackground(new java.awt.Color(242, 247, 247));
    if (!VecPrm.isEmpty()) {
      if (regPrm != null) {
        regPrm.dispose();
      }
      regPrm = new Consulta_CanPromoc(VecPrm);
      regPrm.setVisible(true);
      regPrm.setExtendedState(NORMAL);
      regPrm.setVisible(true);
    }
  }//GEN-LAST:event_jLabPrmMouseClicked

  private void txtPedMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPedMouseReleased

  }//GEN-LAST:event_txtPedMouseReleased

  private void txtPedMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPedMouseExited
    // TODO add your handling code here:
  }//GEN-LAST:event_txtPedMouseExited

  private void btnMovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovActionPerformed
    if (conMov != null) {
      conMov.dispose();
    }
    conMov = new Consulta_MovProd();
    conMov.setVisible(true);
    conMov.setExtendedState(NORMAL);
    conMov.setVisible(true);
  }//GEN-LAST:event_btnMovActionPerformed

  private void clockDigital1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clockDigital1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_clockDigital1MouseClicked

  private void clockDigital1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clockDigital1MouseEntered

  }//GEN-LAST:event_clockDigital1MouseEntered

  private void labcteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labcteMouseEntered
    // TODO add your handling code here:
  }//GEN-LAST:event_labcteMouseEntered

  public void modificaProducto() {
    int[] sel = tabPrd.getSelectedRows();
    labMsgP.setText("");
    if (tabPrd.getRowCount() > 0) {
      if (tabPrd.getSelectedRow() >= 0) {
        String cax = "";
        cop = model.getValueAt(tabPrd.getSelectedRow(), 0).toString();
        nop = model.getValueAt(tabPrd.getSelectedRow(), 1).toString();
        cax = model.getValueAt(tabPrd.getSelectedRow(), 2).toString();
        unm = model.getValueAt(tabPrd.getSelectedRow(), 3).toString();
        can = 0;
        cax = cax.replace(".", "");
        if (isNumeric(cax)) {
          cax = GetCurrencyDouble(cax);
          can = GetMtoDouble(cax);
          if (modPrd != null) {
            modPrd.dispose();
          }
          modPrd = new Registro_PedidoCliente_Mod(ctrP, npe, cop, nop, unm, can);
          modPrd.setVisible(true);
          modPrd.setExtendedState(NORMAL);
          modPrd.setVisible(true);
        } else {
          labMsgP.setText("- Cantidad a modificar invalida-");
        }
      }
    }
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
      java.util.logging.Logger.getLogger(Registro_PedidoCliente.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Registro_PedidoCliente.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Registro_PedidoCliente.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Registro_PedidoCliente.class
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

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registro_PedidoCliente().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnCal;
  private javax.swing.JButton btnEli;
  private javax.swing.JButton btnGra;
  private javax.swing.JButton btnMod;
  private javax.swing.JButton btnMov;
  private javax.swing.JButton btnNvo1;
  private javax.swing.JButton btnSal;
  private javax.swing.JComboBox cbCte;
  private javax.swing.JCheckBox cbFac;
  private javax.swing.JComboBox cbPrd;
  private javax.swing.JComboBox cbUnm;
  private elaprendiz.gui.varios.ClockDigital clockDigital1;
  private com.toedter.calendar.JDateChooser jFec;
  private javax.swing.JLabel jLabCan;
  private javax.swing.JLabel jLabCte;
  private javax.swing.JLabel jLabObs;
  private javax.swing.JLabel jLabPed;
  private javax.swing.JLabel jLabPrd;
  private javax.swing.JLabel jLabPrm;
  private javax.swing.JLabel jLabPrv3;
  private javax.swing.JLabel jLabPrv4;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel labBus;
  private javax.swing.JLabel labCan;
  private javax.swing.JLabel labEmp;
  private javax.swing.JLabel labFac;
  private javax.swing.JLabel labImg;
  public static javax.swing.JLabel labMsgP;
  private javax.swing.JLabel labPrd;
  private javax.swing.JLabel labReg;
  public static javax.swing.JLabel labRutPdfPed;
  private javax.swing.JLabel labTop;
  private javax.swing.JLabel labcte;
  private javax.swing.JLabel labojo;
  private javax.swing.JLabel labtpe;
  private javax.swing.JLabel labtpe1;
  public javax.swing.JTable tabPrd;
  private javax.swing.JTextField txtCan;
  private javax.swing.JTextField txtCte;
  private javax.swing.JTextField txtFec;
  private javax.swing.JTextField txtObs;
  private javax.swing.JTextField txtPed;
  private javax.swing.JTextField txtPrd;
  private javax.swing.JTextField txtPro;
  // End of variables declaration//GEN-END:variables
}
