package gestionFZ;

import comun.Calculadora;
import comun.Mensaje;
import static comun.MetodosComunes.FileExist;
import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.diasem;
import static comun.MetodosComunes.dmy_ymd;
import static comun.MetodosComunes.dmyhoy;
import static comun.MetodosComunes.getAnoHoy;
import static comun.MetodosComunes.getDiaHoy;
import static comun.MetodosComunes.getMesHoy;
import static comun.MetodosComunes.getdiasFec;
import static comun.MetodosComunes.isNumeric;
import static comun.MetodosComunes.isvalidFec;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymdhoy;
import static comun.MetodosComunes.ymdhoyhhmm;
import static gestionFZ.Menu.Calc;
import static gestionFZ.Menu.RegCob;
import static gestionFZ.Registro_NotaCredito.conNcr;
import static gestionFZ.Registro_NotaEntrega.conNoe;
import static gestionFZ.Registro_PagosClientes.labMsgR;
import java.awt.Color;
import static java.awt.Frame.NORMAL;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import modelo.Bancos;
import modelo.Clientes;
import modelo.ConexionSQL;
import modelo.Importadora;
import modelo.NotaEntrega;
import modelo.ReciboCobro;

public class Registro_PagosClientes extends javax.swing.JFrame {

  String sta = ""; // Estatus reciboe 0 = Pendiente 1 = cerrado
  String coc = ""; // Codigo Cliente
  String cor = ""; // Codigo Cliente recibo
  String noc = ""; // Nombre Cliente
  String fno = ""; // fecha nota
  String fen = ""; // fecha entrega
  String fev = ""; // fecha vence
  String rnf = ""; // ref not/fac
  String frc = ""; // fecha recibo Cobro
  String fre = ""; // fecha registro
  String nep = ""; // nota entrega cobrada
  String fil = ""; // filtro busqueda cliente

  String fec = ""; // Fecha recibo
  String fep = ""; // Fecha pago
  String tip = ""; // Tipo pago
  String bce = ""; // banco emisor
  String bcr = ""; // banco receptor
  String ref = ""; // referencia pago
  String obs = ""; // Observacion Recibo

  double tdo = 0; // total nota
  double tdn = 0; // total descuento nota
  double tfa = 0; // Total factura
  double tdf = 0; // Total Descuento factura
  double iva = 0; // %Iva
  double toi = 0; // Total Iva
  double tas = 1; // Tasa
  double por = 0; // %Ret Iva cliente
  double tor = 0; // Total Retencion Iva
  double trd = 0; // Total 25% Ret Iva $

  double tpa = 0; // Total pago
  double tp$ = 0; // Total pago $

  double ts$ = 0; // Total Grl Saldo $
  double tsB = 0; // Total Grl Saldo Bs

  double tgd$ = 0; // Total Grl Saldo $
  double tgdB = 0; // Total Grl Saldo Bs
  double tgp$ = 0; // Total Grl Pago $
  double tgpB = 0; // Total Grl Pafo Bs

  int nrc = 0;  // No Rec Cobro
  int nrx = 0;  // No Rec Cobro
  int nno = 0;  // No NE / NC
  int nnc = 0;  // No NC
  int nfa = 0;  // No factura
  int tpd = 0;  // Tipo documento 0=NE 1=NC
  int inc = 0;  // indicadoir rec cliente
  int nop = 0;  // nota pagada
  int unv = 0;  // una vex

  ImageIcon icon;

  DefaultTableModel modTabDoc;
  DefaultTableModel modTabPag;

  Registro_PagosClientes ctrN;  // defino instancia del controlador

  public static Registro_Banco regBco;
  public static Consulta_ReciboCobro conNrc;
  public static Consulta_PagosClientes conPag;

  public Registro_PagosClientes() {

    initComponents();
    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    btnEli.setVisible(false);

    cbCte.setRenderer(new DefaultListCellRenderer() {
      public void paint(Graphics g) {
        setForeground(new java.awt.Color(0, 0, 0));
        super.paint(g);
      }
    });

    ctrN = this; // Inicializo controller

    Jfecd.setText(diasem() + "    " + dmyhoy());
    labFer.setText(dmyhoy());
    fec = ymdhoy();
    frc = ymdhoy();
    jFer.setMaxSelectableDate(new Date());
    jFer.setFocusable(true);
    jFer.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().trim().equals("date")) {
          SimpleDateFormat ff = new SimpleDateFormat("dd-MM-yyyy");
          labFer.setText(ff.format(jFer.getCalendar().getTime()));
          fec = (labFer.getText().replaceAll("-", ""));
          if (fec.length() == 8) {
            fec = dmy_ymd(fec).replace("-", "");
            frc = fec;
            if (fec.compareTo(ymdhoy()) > 0) {
              labFer.setText(dmyhoy());
              fec = ymdhoy();
              jFer.requestFocus();
            } else {
              updateFecHeader(1);
            }
          }
        }
      }
    });

    modTabDoc = (DefaultTableModel) tabDoc.getModel();
    tabDoc.setRowHeight(25);//tamaño de las celdas
    tabDoc.setGridColor(new java.awt.Color(0, 0, 0));
    tabDoc.setSelectionBackground(new Color(151, 193, 215));
    tabDoc.setSelectionForeground(Color.BLACK);
    tabDoc.getTableHeader().setReorderingAllowed(true);  // activa movimiento columnas
    tabDoc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Ajusta tamaño Columnas
    TableColumnModel columnModel = tabDoc.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(5);  // tipo doc NE / NC 
    columnModel.getColumn(1).setPreferredWidth(20);  // F.Emision  
    columnModel.getColumn(2).setPreferredWidth(20);  // F.Entrega
    columnModel.getColumn(3).setPreferredWidth(20);  // Fecha pago
    columnModel.getColumn(4).setPreferredWidth(10);  // dias Pago
    columnModel.getColumn(5).setPreferredWidth(15);  // Factura
    columnModel.getColumn(6).setPreferredWidth(40);  // total Iva
    columnModel.getColumn(7).setPreferredWidth(40);  // tot %ret25 Iva
    columnModel.getColumn(8).setPreferredWidth(15);  // Nota
    columnModel.getColumn(9).setPreferredWidth(40);  // total $

    // Alinear Columnas Header  ( 0=Izq 1=Der 2=Cent )
    AlinCampoH(0, 0, 2);
    AlinCampoH(0, 1, 2);
    AlinCampoH(0, 2, 2);
    AlinCampoH(0, 3, 2);
    AlinCampoH(0, 4, 2);
    AlinCampoH(0, 5, 2);
    AlinCampoH(0, 6, 1);
    AlinCampoH(0, 7, 1);
    AlinCampoH(0, 8, 2);
    AlinCampoH(0, 9, 1);

    // Alinear Columnas Detail  ( 0=Izq 1=Der 2=Cent )
    AlinCampoD(0, 0, 2);
    AlinCampoD(0, 1, 2);
    AlinCampoD(0, 2, 2);
    AlinCampoD(0, 3, 2);
    AlinCampoD(0, 4, 2);
    AlinCampoD(0, 5, 2);
    AlinCampoD(0, 6, 1);
    AlinCampoD(0, 7, 1);
    AlinCampoD(0, 8, 2);
    AlinCampoD(0, 9, 1);

    modTabPag = (DefaultTableModel) tabPag.getModel();

    tabPag.setAutoscrolls(false);
    tabPag.setRowHeight(25);//tamaño de las celdas
    tabPag.setGridColor(new java.awt.Color(0, 0, 0));
    tabPag.setSelectionBackground(new Color(151, 193, 215));
    tabPag.setSelectionForeground(Color.BLACK);
    tabPag.getTableHeader().setReorderingAllowed(true);
    tabPag.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tabPag.setShowGrid(false);

    // Ajusta tamaño Columnas
    columnModel = tabPag.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(40);  // Concepto 
    columnModel.getColumn(1).setPreferredWidth(15);  // Fecha Nota
    columnModel.getColumn(2).setPreferredWidth(10);  // Nota
    columnModel.getColumn(3).setPreferredWidth(30);  // Banco Emisor
    columnModel.getColumn(4).setPreferredWidth(30);  // Banco Receptor
    columnModel.getColumn(5).setPreferredWidth(70);  // Referencia
    columnModel.getColumn(6).setPreferredWidth(20);  // Importe
    columnModel.getColumn(7).setPreferredWidth(10);  // tasa
    columnModel.getColumn(8).setPreferredWidth(20);  // total $

    // Alinear Columnas Header  ( 0=Izq 1=Der 2=Cent )
    AlinCampoH(1, 0, 2);
    AlinCampoH(1, 1, 2);
    AlinCampoH(1, 2, 2);
    AlinCampoH(1, 3, 3);
    AlinCampoH(1, 4, 3);
    AlinCampoH(1, 5, 3);
    AlinCampoH(1, 6, 1);
    AlinCampoH(1, 7, 1);
    AlinCampoH(1, 8, 1);

    // Alinear Columnas Detail  ( 0=Izq 1=Der 2=Cent )
    AlinCampoD(1, 0, 2);
    AlinCampoD(1, 1, 2);
    AlinCampoD(1, 2, 2);
    AlinCampoD(1, 3, 3);
    AlinCampoD(1, 4, 3);
    AlinCampoD(1, 5, 3);
    AlinCampoD(1, 6, 1);
    AlinCampoD(1, 7, 1);
    AlinCampoD(1, 8, 1);

    cargaNoEFac("0");
    cargaClientes();
    cargaBancos();

    seteaFecPago();
    iniciaRecCob();
    ReciboCobro r = new ReciboCobro();
    int nrx = r.getMaxRecCob();
    //nrx=nrx+1;
    txtNrc.setText("" + nrx);

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        RegCob.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        RegCob.dispose();
      }
    });

  }

  // Actualiza Fecha Header pedido
  public void updateFecHeader(int ind) {
    if (nrc > 0) {
      obs = txtObs.getText().trim();
      if (obs.length() > 200) {
        obs = obs.substring(0, 200);
      }
      ReciboCobro r = new ReciboCobro(nrc, frc, obs);
      if (r.modificarReciboCobroH(ind)) {
        labMsgR.setText("- Se Actualizo registro -");
      }
    }
  }

  public void seteaFecPago() {
    txtDia.setText("" + getDiaHoy());
    txtMes.setText("" + getMesHoy());
    txtAno.setText("" + getAnoHoy());
    String vax = txtDia.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtDia.setText(vax);
    }
    vax = txtMes.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtMes.setText(vax);
    }
    txtDia.setSelectionStart(0);
    txtDia.setSelectionEnd(txtDia.getText().length());
    txtMes.setSelectionStart(0);
    txtMes.setSelectionEnd(txtMes.getText().length());
    txtAno.setSelectionStart(0);
    txtAno.setSelectionEnd(txtAno.getText().length());
  }

  public void cargaClientes() {
    cbCte.removeAllItems();
    cbCte.addItem("");
    String coc = "", noc = "";
    // ObservableList modelo tabla Clientes
    ObservableList<Clientes> obsClientes;
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

  public static void cargaBancos() {
    cbBce.removeAllItems();
    cbBce.addItem("");
    cbBcr.removeAllItems();
    cbBcr.addItem("");
    ObservableList<Bancos> obsBancos;
    Bancos b = new Bancos();
    obsBancos = b.getBancos();
    for (Bancos bco : obsBancos) {
      cbBce.addItem(bco.getBco());
      cbBcr.addItem(bco.getBco());
    }
    cbBce.setSelectedIndex(-1);
    cbBcr.setSelectedIndex(-1);
  }

  // Carga Numeros Nota Ent / Factura
  public void cargaNoEFac(String tip) {
    cbNe.removeAllItems();
    cbNe.addItem("");
    ObservableList<NotaEntrega> obsNotaEntrega;
    NotaEntrega n = new NotaEntrega();
    obsNotaEntrega = n.getNoeFac(nrc, tip);
    for (NotaEntrega noe : obsNotaEntrega) {
      cbNe.addItem(noe.getNne());
    }
    cbNe.setSelectedIndex(-1);
  }

// recibe numero nota Credito
  public void recibeReciboCobro(int nrn) {
    nrc = nrn;
    txtNrc.setText("" + nrc);
    verifRecCob();
  }

  // recibe numero nota Credito
  public void recibeNotaCredito(int nrn) {
    nno = nrn;
    txtNot.setText("" + nno);
    aplicaNota();
  }

  // recibe numero nota Entrega
  public void recibeNotaEntregaRC(int nrn) {
    tpd = 0;
    nno = nrn;
    txtNot.setText("" + nno);
    aplicaNota();
  }

  public void iniciaRecCob() {
    txtNrc.setText("");
    bloqueaCampos();
    labMsgR.setText("- Ingrese No. Recibo de Cobro");
    cbTip.setEnabled(false);
    cbNE.setEnabled(false);
    cbNC.setEnabled(false);
    txtNot.setEnabled(false);
    txtObs.setEnabled(false);
    jLabCua.setText("");
    txtNrc.requestFocus();
  }

  public void bloqueaCampos() {
    labMsgR.setText("");
    tdo = 0; // total documento
    nno = 0; // No NE / NC
    nfa = 0; // No factura
    iva = 0; // % Iva
    por = 0; // %Ret Iva
    toi = 0; // Total Iva
    tor = 0; // Total Retencion Iva
    nop = 0; // nota de entraga cbNE
    fno = ""; // fecha nota
    fre = ""; // fecha registro
    txtObs.setText(" ");
    labFer.setText("");
    labSi$.setText("");
    labSib.setText("");
    labTsd$.setText("");
    labTsdBs.setText("");
    jFer.setEnabled(false);
    txtCte.setEnabled(false);
    cbCte.setEnabled(false);
    cbTip.setEnabled(false);
    cbNe.setEnabled(false);
    cbNE.setEnabled(false);
    cbNC.setEnabled(false);
    txtNot.setEnabled(false);

    txtDia.setEnabled(false);
    txtMes.setEnabled(false);
    txtAno.setEnabled(false);

    txtTop.setEnabled(false);
    txtTap.setEnabled(false);
    txtTpd.setEnabled(false);
    cbBce.setEnabled(false);
    cbBcr.setEnabled(false);
    txtRef.setEnabled(false);
    jLabCua.setVisible(true);
    txtObs.setText(" ");
    txtDia.setText("");
    txtMes.setText("");
    txtAno.setText("");

    txtNot.setText("");
    //labTNe.setText("");
    //labNfa.setText("");
    //labTFa.setText("");
    //labIva.setText("");

    txtTop.setText("");
    txtTap.setText("");
    txtTpd.setText("");
    txtRef.setText("");
    cbCte.setSelectedIndex(-1);
    cbTip.setSelectedIndex(-1);
    cbNe.setSelectedIndex(-1);
    cbBce.setSelectedIndex(-1);
    cbBcr.setSelectedIndex(-1);

    // Inicializa tablas
    modTabDoc.setRowCount(0);
    modTabPag.setRowCount(0);

    btnGrp.setEnabled(false);
    btnEli.setEnabled(false);
    btnEld.setEnabled(false);
    btnPdf.setEnabled(false);
    txtDia.setBorder(new LineBorder(Color.GRAY));
    tabDoc.getSelectionModel().clearSelection();  // Quitar Seleccion
    labRutPdfCob.setBackground(new java.awt.Color(242, 247, 247));
    labSi$.setOpaque(false);
    labSib.setOpaque(false);
    labSib.setBackground(new java.awt.Color(240, 240, 240));
    labSi$.setBackground(new java.awt.Color(240, 240, 240));
    labSib.setBackground(new java.awt.Color(240, 240, 240));

    labTsd$.setBackground(new java.awt.Color(204, 204, 204));
    jLabSd$1.setText("Saldo $");

    labTsdBs.setBackground(new java.awt.Color(204, 204, 204));
    jLabSdB.setText("T. Sdo Ret Bs");
  }

  // Alinea Campos Columnas Header
  private void AlinCampoH(int ntb, int col, int ind) {
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
    if (ntb == 0) {
      headerRenderer.setBackground(new java.awt.Color(59, 120, 97));   // #3B7861
      headerRenderer.setForeground(Color.WHITE);
      tabDoc.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
      tabDoc.getColumnModel().getColumn(col).setHeaderRenderer(headerRenderer);
    }
    if (ntb == 1) {
      headerRenderer.setBackground(new java.awt.Color(116, 78, 59));   // #744E3B
      headerRenderer.setForeground(Color.WHITE);
      tabPag.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
      tabPag.getColumnModel().getColumn(col).setHeaderRenderer(headerRenderer);
    }
  }

  // Alinea Campos Columnas Detalle
  private void AlinCampoD(int ntb, int col, int ind) {
    DefaultTableCellRenderer Alinea = new DefaultTableCellRenderer();
    if (ind == 0) {
      Alinea.setHorizontalAlignment(SwingConstants.LEFT);
    }
    if (ind == 1) {
      Alinea.setHorizontalAlignment(SwingConstants.RIGHT);
    }
    if (ind == 2) {
      Alinea.setHorizontalAlignment(SwingConstants.CENTER);
    }
    if (ntb == 0) {
      tabDoc.getColumnModel().getColumn(col).setCellRenderer(Alinea);
    }
    if (ntb == 1) {
      tabPag.getColumnModel().getColumn(col).setCellRenderer(Alinea);
    }
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    buttonGroup1 = new javax.swing.ButtonGroup();
    buttonGroup2 = new javax.swing.ButtonGroup();
    jLabel2 = new javax.swing.JLabel();
    jLabNoe = new javax.swing.JLabel();
    txtNrc = new javax.swing.JTextField();
    labBusNpg = new javax.swing.JLabel();
    Jfecd = new javax.swing.JLabel();
    clockDigital1 = new elaprendiz.gui.varios.ClockDigital();
    jScrollPane1 = new javax.swing.JScrollPane();
    tabDoc = new javax.swing.JTable();
    jScrollPane2 = new javax.swing.JScrollPane();
    tabPag = new javax.swing.JTable();
    jLabCte1 = new javax.swing.JLabel();
    jPanel1 = new javax.swing.JPanel();
    jLabNoe2 = new javax.swing.JLabel();
    cbNe = new javax.swing.JComboBox();
    jLabCte6 = new javax.swing.JLabel();
    jLabCte7 = new javax.swing.JLabel();
    txtTop = new javax.swing.JTextField();
    txtTpd = new javax.swing.JTextField();
    txtTap = new javax.swing.JTextField();
    jLabCte10 = new javax.swing.JLabel();
    cbBce = new javax.swing.JComboBox();
    jLabCte9 = new javax.swing.JLabel();
    cbBcr = new javax.swing.JComboBox();
    jLabCte11 = new javax.swing.JLabel();
    txtRef = new javax.swing.JTextField();
    jLabCte16 = new javax.swing.JLabel();
    jLabCte8 = new javax.swing.JLabel();
    btnBco = new javax.swing.JButton();
    btnGrp = new javax.swing.JButton();
    jLabCte21 = new javax.swing.JLabel();
    cbTip = new javax.swing.JComboBox();
    btnCal = new javax.swing.JButton();
    txtDia = new javax.swing.JTextField();
    txtMes = new javax.swing.JTextField();
    txtAno = new javax.swing.JTextField();
    btnEld = new javax.swing.JButton();
    jLabCua = new javax.swing.JLabel();
    labTsd$ = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();
    labMsgR = new javax.swing.JLabel();
    btnEli = new javax.swing.JButton();
    txtCte = new javax.swing.JTextField();
    jLabel6 = new javax.swing.JLabel();
    cbCte = new javax.swing.JComboBox();
    labFer = new javax.swing.JLabel();
    jLabNoe1 = new javax.swing.JLabel();
    labojo = new javax.swing.JLabel();
    btnPdf = new javax.swing.JButton();
    jPanel2 = new javax.swing.JPanel();
    cbNE = new javax.swing.JCheckBox();
    jLabCte2 = new javax.swing.JLabel();
    cbNC = new javax.swing.JCheckBox();
    jLabCte3 = new javax.swing.JLabel();
    jLabCte = new javax.swing.JLabel();
    labBusNen = new javax.swing.JLabel();
    txtNot = new javax.swing.JTextField();
    jLabSdB = new javax.swing.JLabel();
    labTsdBs = new javax.swing.JLabel();
    labRutPdfCob = new javax.swing.JLabel();
    jLabSiD = new javax.swing.JLabel();
    labSi$ = new javax.swing.JLabel();
    labSib = new javax.swing.JLabel();
    jLabSiB = new javax.swing.JLabel();
    labCom = new javax.swing.JLabel();
    jFer = new com.toedter.calendar.JDateChooser();
    txtObs = new javax.swing.JTextField();
    jLabNot = new javax.swing.JLabel();
    jLabSd$1 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);
    getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    jLabel2.setBackground(new java.awt.Color(217, 226, 226));
    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(0, 0, 153));
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pagos.jpg"))); // NOI18N
    jLabel2.setText(" MODULO DE COBRANZA");
    jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jLabel2.setOpaque(true);
    getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 11, 243, 36));

    jLabNoe.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabNoe.setForeground(new java.awt.Color(102, 0, 0));
    jLabNoe.setText("No. Recibo Cobro");
    jLabNoe.setToolTipText("No. Not/Entrega");
    jLabNoe.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabNoeMouseClicked(evt);
      }
    });
    getContentPane().add(jLabNoe, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, -1, 30));

    txtNrc.setBackground(new java.awt.Color(246, 245, 245));
    txtNrc.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    txtNrc.setForeground(new java.awt.Color(0, 0, 204));
    txtNrc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtNrc.setText(" ");
    txtNrc.setToolTipText("Ingrese No. Recibo Cobro");
    txtNrc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    txtNrc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNrc.setPreferredSize(new java.awt.Dimension(7, 30));
    txtNrc.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNrcMouseClicked(evt);
      }
    });
    txtNrc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNrcActionPerformed(evt);
      }
    });
    txtNrc.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtNrcKeyReleased(evt);
      }
    });
    getContentPane().add(txtNrc, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 101, -1));

    labBusNpg.setBackground(new java.awt.Color(204, 204, 204));
    labBusNpg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labBusNpg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    labBusNpg.setToolTipText("Buscar Recibos de Cobro");
    labBusNpg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labBusNpg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labBusNpg.setOpaque(true);
    labBusNpg.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labBusNpgMouseClicked(evt);
      }
    });
    getContentPane().add(labBusNpg, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 28, 30));

    Jfecd.setFont(new java.awt.Font("Cambria Math", 1, 12)); // NOI18N
    Jfecd.setForeground(new java.awt.Color(51, 51, 51));
    Jfecd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    Jfecd.setText("Fec");
    getContentPane().add(Jfecd, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 10, 160, -1));

    clockDigital1.setForeground(new java.awt.Color(51, 51, 51));
    clockDigital1.setFont(new java.awt.Font("Arial", 3, 11)); // NOI18N
    clockDigital1.setPreferredSize(new java.awt.Dimension(120, 18));
    clockDigital1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        clockDigital1MouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        clockDigital1MouseEntered(evt);
      }
    });
    getContentPane().add(clockDigital1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 30, 139, 11));

    tabDoc.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
    tabDoc.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
    tabDoc.setForeground(new java.awt.Color(51, 51, 51));
    tabDoc.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "TIP", "F. EMISION", "F. ENTREGA", "F.PAGO", "DIAS PAGO", "No.FACT Bs", "TOTAL IVA Bs", "T.RET IVA Bs", "No.NOTA $", "TOTAL $  "
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
      };
      boolean[] canEdit = new boolean [] {
        false, false, false, false, false, false, false, false, false, false
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    tabDoc.setToolTipText("Doble Click - Elimina Documento");
    tabDoc.setGridColor(new java.awt.Color(51, 51, 51));
    tabDoc.setRowHeight(25);
    tabDoc.getTableHeader().setReorderingAllowed(false);
    tabDoc.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tabDocMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(tabDoc);
    if (tabDoc.getColumnModel().getColumnCount() > 0) {
      tabDoc.getColumnModel().getColumn(0).setResizable(false);
      tabDoc.getColumnModel().getColumn(0).setPreferredWidth(5);
      tabDoc.getColumnModel().getColumn(1).setResizable(false);
      tabDoc.getColumnModel().getColumn(1).setPreferredWidth(15);
      tabDoc.getColumnModel().getColumn(2).setResizable(false);
      tabDoc.getColumnModel().getColumn(2).setPreferredWidth(15);
      tabDoc.getColumnModel().getColumn(3).setResizable(false);
      tabDoc.getColumnModel().getColumn(3).setPreferredWidth(15);
      tabDoc.getColumnModel().getColumn(4).setResizable(false);
      tabDoc.getColumnModel().getColumn(4).setPreferredWidth(15);
      tabDoc.getColumnModel().getColumn(5).setResizable(false);
      tabDoc.getColumnModel().getColumn(5).setPreferredWidth(15);
      tabDoc.getColumnModel().getColumn(6).setResizable(false);
      tabDoc.getColumnModel().getColumn(6).setPreferredWidth(40);
      tabDoc.getColumnModel().getColumn(7).setResizable(false);
      tabDoc.getColumnModel().getColumn(7).setPreferredWidth(40);
      tabDoc.getColumnModel().getColumn(8).setResizable(false);
      tabDoc.getColumnModel().getColumn(8).setPreferredWidth(15);
      tabDoc.getColumnModel().getColumn(9).setResizable(false);
      tabDoc.getColumnModel().getColumn(9).setPreferredWidth(40);
    }

    getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 159, 1244, 151));

    tabPag.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
    tabPag.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
    tabPag.setForeground(new java.awt.Color(51, 51, 51));
    tabPag.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "CONCEPTO PAGO", "FECHA", "NOTA / FACT", "BANCO EMISOR", "BANCO RECEPTOR", "REFERENCIA", "IMPORTE Bs", "TASA", "IMPORTE $"
      }
    ) {
      boolean[] canEdit = new boolean [] {
        false, false, false, false, false, false, false, false, false
      };

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    tabPag.setToolTipText("Doble Click - Elimina Pago");
    tabPag.setRowHeight(25);
    tabPag.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tabPagMouseClicked(evt);
      }
    });
    jScrollPane2.setViewportView(tabPag);
    if (tabPag.getColumnModel().getColumnCount() > 0) {
      tabPag.getColumnModel().getColumn(0).setResizable(false);
      tabPag.getColumnModel().getColumn(0).setPreferredWidth(50);
      tabPag.getColumnModel().getColumn(1).setResizable(false);
      tabPag.getColumnModel().getColumn(1).setPreferredWidth(10);
      tabPag.getColumnModel().getColumn(2).setResizable(false);
      tabPag.getColumnModel().getColumn(2).setPreferredWidth(15);
      tabPag.getColumnModel().getColumn(3).setResizable(false);
      tabPag.getColumnModel().getColumn(3).setPreferredWidth(40);
      tabPag.getColumnModel().getColumn(4).setResizable(false);
      tabPag.getColumnModel().getColumn(4).setPreferredWidth(40);
      tabPag.getColumnModel().getColumn(5).setResizable(false);
      tabPag.getColumnModel().getColumn(5).setPreferredWidth(80);
      tabPag.getColumnModel().getColumn(6).setResizable(false);
      tabPag.getColumnModel().getColumn(6).setPreferredWidth(20);
      tabPag.getColumnModel().getColumn(7).setResizable(false);
      tabPag.getColumnModel().getColumn(7).setPreferredWidth(10);
      tabPag.getColumnModel().getColumn(8).setResizable(false);
      tabPag.getColumnModel().getColumn(8).setPreferredWidth(20);
    }

    getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 419, 1244, 177));

    jLabCte1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabCte1.setForeground(new java.awt.Color(0, 0, 102));
    jLabCte1.setText("Cliente");
    jLabCte1.setToolTipText("");
    jLabCte1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCte1MouseClicked(evt);
      }
    });
    getContentPane().add(jLabCte1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 55, -1, 35));

    jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
    jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jPanel1MouseClicked(evt);
      }
    });

    jLabNoe2.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabNoe2.setForeground(new java.awt.Color(102, 0, 51));
    jLabNoe2.setText("Concepto  ");
    jLabNoe2.setToolTipText("");
    jLabNoe2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabNoe2MouseClicked(evt);
      }
    });

    cbNe.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    cbNe.setMaximumRowCount(20);
    cbNe.setToolTipText("Seleccione Nota Entrega a aplicar");
    cbNe.setPreferredSize(new java.awt.Dimension(35, 30));
    cbNe.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbNeItemStateChanged(evt);
      }
    });
    cbNe.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbNeActionPerformed(evt);
      }
    });

    jLabCte6.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabCte6.setForeground(new java.awt.Color(102, 0, 51));
    jLabCte6.setText("Tot");
    jLabCte6.setToolTipText("");
    jLabCte6.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCte6MouseClicked(evt);
      }
    });

    jLabCte7.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabCte7.setForeground(new java.awt.Color(102, 0, 51));
    jLabCte7.setText("Tasa");
    jLabCte7.setToolTipText("");
    jLabCte7.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCte7MouseClicked(evt);
      }
    });

    txtTop.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtTop.setForeground(new java.awt.Color(51, 51, 51));
    txtTop.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtTop.setText(" ");
    txtTop.setToolTipText("Coloque Importe  del Pago Realizado");
    txtTop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    txtTop.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtTop.setOpaque(false);
    txtTop.setPreferredSize(new java.awt.Dimension(7, 30));
    txtTop.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtTopMouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        txtTopMouseEntered(evt);
      }
    });
    txtTop.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtTopActionPerformed(evt);
      }
    });
    txtTop.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtTopKeyReleased(evt);
      }
    });

    txtTpd.setEditable(false);
    txtTpd.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtTpd.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtTpd.setText(" ");
    txtTpd.setToolTipText("Total Importe en $");
    txtTpd.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtTpd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtTpd.setOpaque(false);
    txtTpd.setPreferredSize(new java.awt.Dimension(7, 30));
    txtTpd.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtTpdMouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        txtTpdMouseEntered(evt);
      }
    });
    txtTpd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtTpdActionPerformed(evt);
      }
    });
    txtTpd.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtTpdKeyReleased(evt);
      }
    });

    txtTap.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtTap.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtTap.setText(" ");
    txtTap.setToolTipText("Coloque tasa de cambio  (  Monto $ la tasa = 1  /  Monto Ret Bs  tasa = 1  Monto Ret $ tasa  mayor a 1 )");
    txtTap.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtTap.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtTap.setOpaque(false);
    txtTap.setPreferredSize(new java.awt.Dimension(7, 30));
    txtTap.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtTapMouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        txtTapMouseEntered(evt);
      }
    });
    txtTap.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtTapActionPerformed(evt);
      }
    });
    txtTap.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtTapKeyReleased(evt);
      }
    });

    jLabCte10.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabCte10.setForeground(new java.awt.Color(102, 0, 51));
    jLabCte10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Bco.png"))); // NOI18N
    jLabCte10.setText("Banco Emisor");
    jLabCte10.setToolTipText("");
    jLabCte10.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCte10MouseClicked(evt);
      }
    });

    cbBce.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    cbBce.setMaximumRowCount(20);
    cbBce.setToolTipText("Seleccione Banco Emisor");
    cbBce.setPreferredSize(new java.awt.Dimension(35, 30));
    cbBce.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbBceItemStateChanged(evt);
      }
    });
    cbBce.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbBceActionPerformed(evt);
      }
    });

    jLabCte9.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabCte9.setForeground(new java.awt.Color(102, 0, 51));
    jLabCte9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Bco.png"))); // NOI18N
    jLabCte9.setText("Banco Receptor");
    jLabCte9.setToolTipText("");
    jLabCte9.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCte9MouseClicked(evt);
      }
    });

    cbBcr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    cbBcr.setMaximumRowCount(20);
    cbBcr.setToolTipText("Seleccione Banco Receptor");
    cbBcr.setPreferredSize(new java.awt.Dimension(35, 30));
    cbBcr.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbBcrItemStateChanged(evt);
      }
    });
    cbBcr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbBcrActionPerformed(evt);
      }
    });

    jLabCte11.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabCte11.setForeground(new java.awt.Color(102, 0, 51));
    jLabCte11.setText("Ref");
    jLabCte11.setToolTipText("");
    jLabCte11.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCte11MouseClicked(evt);
      }
    });

    txtRef.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtRef.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    txtRef.setText(" ");
    txtRef.setToolTipText("Coloque No. Referencia  Bancaria");
    txtRef.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtRef.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtRef.setOpaque(false);
    txtRef.setPreferredSize(new java.awt.Dimension(7, 30));
    txtRef.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtRefMouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        txtRefMouseEntered(evt);
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

    jLabCte16.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabCte16.setForeground(new java.awt.Color(102, 0, 51));
    jLabCte16.setText("Tot $");
    jLabCte16.setToolTipText("");
    jLabCte16.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCte16MouseClicked(evt);
      }
    });

    jLabCte8.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabCte8.setForeground(new java.awt.Color(102, 0, 51));
    jLabCte8.setText("Fec");
    jLabCte8.setToolTipText("Fecha Pago");
    jLabCte8.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCte8MouseClicked(evt);
      }
    });

    btnBco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    btnBco.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Bancos.png"))); // NOI18N
    btnBco.setToolTipText("Actualizar Bancos");
    btnBco.setBorder(null);
    btnBco.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnBco.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnBcoActionPerformed(evt);
      }
    });

    btnGrp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnGrp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGrp.setText("Grabar");
    btnGrp.setToolTipText("Grabar Registro del Pago");
    btnGrp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnGrp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGrpActionPerformed(evt);
      }
    });

    jLabCte21.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    jLabCte21.setForeground(new java.awt.Color(102, 0, 51));
    jLabCte21.setText("NE/FC");
    jLabCte21.setToolTipText("");
    jLabCte21.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCte21MouseClicked(evt);
      }
    });

    cbTip.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    cbTip.setMaximumRowCount(20);
    cbTip.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pago", "Abono", "Pago Ret Iva", "Saldo a Favor $", "Dev Ret Iva", "Ajuste Saldo" }));
    cbTip.setToolTipText("Seleccione tipo de Transaccion a realizar");
    cbTip.setPreferredSize(new java.awt.Dimension(35, 30));
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

    txtDia.setBackground(new java.awt.Color(250, 253, 244));
    txtDia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtDia.setForeground(new java.awt.Color(51, 51, 51));
    txtDia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtDia.setText(" ");
    txtDia.setToolTipText("Coloque Fecha dia Pago");
    txtDia.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtDia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtDia.setPreferredSize(new java.awt.Dimension(10, 25));
    txtDia.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtDiaMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtDiaMouseReleased(evt);
      }
    });
    txtDia.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtDiaActionPerformed(evt);
      }
    });
    txtDia.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtDiaKeyReleased(evt);
      }
    });

    txtMes.setBackground(new java.awt.Color(250, 253, 244));
    txtMes.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtMes.setForeground(new java.awt.Color(51, 51, 51));
    txtMes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtMes.setText(" ");
    txtMes.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtMes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtMes.setPreferredSize(new java.awt.Dimension(10, 25));
    txtMes.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtMesMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtMesMouseReleased(evt);
      }
    });
    txtMes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtMesActionPerformed(evt);
      }
    });
    txtMes.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtMesKeyReleased(evt);
      }
    });

    txtAno.setBackground(new java.awt.Color(250, 253, 244));
    txtAno.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtAno.setForeground(new java.awt.Color(51, 51, 51));
    txtAno.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtAno.setText(" ");
    txtAno.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtAno.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtAno.setPreferredSize(new java.awt.Dimension(10, 25));
    txtAno.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtAnoMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtAnoMouseReleased(evt);
      }
    });
    txtAno.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtAnoActionPerformed(evt);
      }
    });
    txtAno.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtAnoKeyReleased(evt);
      }
    });

    btnEld.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/elim.png"))); // NOI18N
    btnEld.setMnemonic('E');
    btnEld.setToolTipText("Eliminar registro marcado ");
    btnEld.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnEld.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEldActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(19, 19, 19)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(btnEld, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(11, 11, 11)
            .addComponent(jLabNoe2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jLabCte10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(cbBce, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(11, 11, 11)
            .addComponent(cbTip, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addComponent(jLabCte9)
            .addGap(6, 6, 6))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addComponent(jLabCte8)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(txtDia, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(2, 2, 2)
            .addComponent(txtMes, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(2, 2, 2)
            .addComponent(txtAno, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(4, 4, 4)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabCte21, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(cbNe, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(cbBcr, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabCte6)
          .addComponent(jLabCte11, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addComponent(txtRef, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(txtTop, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabCte7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(11, 11, 11)))
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
          .addComponent(txtTap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnGrp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jLabCte16, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(btnBco, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnCal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(txtTpd, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(15, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabNoe2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtTop, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtTpd, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtTap, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabCte7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabCte6)
            .addComponent(jLabCte16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabCte21)
            .addComponent(cbNe, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtMes, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtDia, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabCte8)
            .addComponent(cbTip, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(btnEld, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabCte10)
            .addComponent(cbBce, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtRef, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabCte11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cbBcr, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabCte9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(btnGrp, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(2, 2, 2)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(btnBco, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(btnCal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addContainerGap())
    );

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 321, 1240, -1));

    jLabCua.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabCua.setForeground(new java.awt.Color(51, 0, 255));
    jLabCua.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabCua.setText(" ");
    jLabCua.setToolTipText("");
    jLabCua.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCuaMouseClicked(evt);
      }
    });
    getContentPane().add(jLabCua, new org.netbeans.lib.awtextra.AbsoluteConstraints(616, 607, 95, 30));

    labTsd$.setBackground(new java.awt.Color(204, 204, 204));
    labTsd$.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labTsd$.setForeground(new java.awt.Color(51, 51, 51));
    labTsd$.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labTsd$.setToolTipText("Saldo del Recibo en $");
    labTsd$.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 102), 1, true));
    labTsd$.setOpaque(true);
    labTsd$.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        labTsd$MouseEntered(evt);
      }
    });
    getContentPane().add(labTsd$, new org.netbeans.lib.awtextra.AbsoluteConstraints(1138, 612, 115, 25));

    jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    jButton1.setText("Salir");
    jButton1.setToolTipText("Salir Modulo de Cobranza");
    jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });
    getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 650, -1, 30));

    labMsgR.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
    labMsgR.setForeground(new java.awt.Color(255, 0, 0));
    labMsgR.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labMsgR.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)));
    getContentPane().add(labMsgR, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 607, 523, 30));

    btnEli.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnEli.setForeground(new java.awt.Color(102, 102, 102));
    btnEli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/elim.png"))); // NOI18N
    btnEli.setMnemonic('E');
    btnEli.setText(" ");
    btnEli.setToolTipText("Eliminar Recibo de Cobro Completo");
    btnEli.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    btnEli.setBorderPainted(false);
    btnEli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnEli.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    btnEli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEliActionPerformed(evt);
      }
    });
    getContentPane().add(btnEli, new org.netbeans.lib.awtextra.AbsoluteConstraints(828, 11, -1, 30));

    txtCte.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtCte.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    txtCte.setToolTipText("Coloque Cliente a buscar");
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
    getContentPane().add(txtCte, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 60, 98, -1));

    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    jLabel6.setToolTipText("Buscar Clientes");
    jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabel6MouseClicked(evt);
      }
    });
    getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(88, 55, 28, 35));

    cbCte.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
    cbCte.setMaximumRowCount(30);
    cbCte.setToolTipText("Seleccione Cliente");
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
    getContentPane().add(cbCte, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 563, -1));

    labFer.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labFer.setForeground(new java.awt.Color(51, 51, 51));
    labFer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labFer.setToolTipText("Fecha Recibo Dia");
    labFer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    getContentPane().add(labFer, new org.netbeans.lib.awtextra.AbsoluteConstraints(704, 11, 106, 31));

    jLabNoe1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabNoe1.setForeground(new java.awt.Color(102, 0, 0));
    jLabNoe1.setText("Fecha");
    jLabNoe1.setToolTipText("No. Not/Entrega");
    jLabNoe1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabNoe1MouseClicked(evt);
      }
    });
    getContentPane().add(jLabNoe1, new org.netbeans.lib.awtextra.AbsoluteConstraints(603, 11, -1, 31));

    labojo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labojo.setForeground(new java.awt.Color(102, 102, 102));
    labojo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojop.png"))); // NOI18N
    labojo.setToolTipText("Linea de Mensajes");
    labojo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    getContentPane().add(labojo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 607, 27, 30));

    btnPdf.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pdf0.png"))); // NOI18N
    btnPdf.setText("Recibo");
    btnPdf.setToolTipText("Genera Recibo Pago en PDF");
    btnPdf.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnPdf.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnPdfActionPerformed(evt);
      }
    });
    getContentPane().add(btnPdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(885, 60, 97, 30));

    jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
    jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jPanel2MouseClicked(evt);
      }
    });

    cbNE.setBackground(new java.awt.Color(240, 248, 239));
    cbNE.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbNE.setOpaque(false);
    cbNE.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbNEActionPerformed(evt);
      }
    });

    jLabCte2.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    jLabCte2.setForeground(new java.awt.Color(0, 0, 102));
    jLabCte2.setText("Not Credito");
    jLabCte2.setToolTipText("");
    jLabCte2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCte2MouseClicked(evt);
      }
    });

    cbNC.setBackground(new java.awt.Color(240, 248, 239));
    cbNC.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbNC.setOpaque(false);
    cbNC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbNCActionPerformed(evt);
      }
    });

    jLabCte3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabCte3.setForeground(new java.awt.Color(102, 102, 102));
    jLabCte3.setText("No. Nota");
    jLabCte3.setToolTipText("");
    jLabCte3.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCte3MouseClicked(evt);
      }
    });

    jLabCte.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    jLabCte.setForeground(new java.awt.Color(0, 0, 102));
    jLabCte.setText("Not Entrega");
    jLabCte.setToolTipText("");
    jLabCte.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCteMouseClicked(evt);
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

    txtNot.setBackground(new java.awt.Color(246, 245, 245));
    txtNot.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    txtNot.setForeground(new java.awt.Color(0, 0, 204));
    txtNot.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtNot.setText(" ");
    txtNot.setToolTipText("Coloque No. Not Entrega  ó  Not Credito");
    txtNot.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
    txtNot.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNot.setPreferredSize(new java.awt.Dimension(7, 30));
    txtNot.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNotMouseClicked(evt);
      }
    });
    txtNot.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNotActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(jLabCte, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(cbNE))
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(jLabCte2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(cbNC)))
        .addGap(14, 14, 14)
        .addComponent(labBusNen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(12, 12, 12)
        .addComponent(jLabCte3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(txtNot, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(19, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addComponent(labBusNen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(cbNE, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
              .addComponent(jLabCte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabCte2)
              .addComponent(cbNC, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addGap(0, 0, Short.MAX_VALUE))
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addGap(10, 10, 10)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(txtNot, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabCte3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );

    getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 97, -1, 51));

    jLabSdB.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    jLabSdB.setForeground(new java.awt.Color(51, 51, 51));
    jLabSdB.setText("T. Sdo Ret Bs");
    jLabSdB.setToolTipText("");
    jLabSdB.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabSdBMouseClicked(evt);
      }
    });
    getContentPane().add(jLabSdB, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 610, 112, 25));

    labTsdBs.setBackground(new java.awt.Color(204, 204, 204));
    labTsdBs.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labTsdBs.setForeground(new java.awt.Color(102, 0, 0));
    labTsdBs.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labTsdBs.setToolTipText("Saldo Bs Retencion Iva");
    labTsdBs.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 1, true));
    labTsdBs.setOpaque(true);
    getContentPane().add(labTsdBs, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 610, 117, 25));

    labRutPdfCob.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labRutPdfCob.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rutpdf.png"))); // NOI18N
    labRutPdfCob.setToolTipText("Ruta Recibos de Cobro generados en PDF");
    labRutPdfCob.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    labRutPdfCob.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labRutPdfCob.setPreferredSize(new java.awt.Dimension(43, 30));
    labRutPdfCob.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labRutPdfCobMouseClicked(evt);
      }
    });
    getContentPane().add(labRutPdfCob, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 62, 51, 28));

    jLabSiD.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabSiD.setForeground(new java.awt.Color(51, 51, 51));
    jLabSiD.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabSiD.setText("Saldo inicial $");
    jLabSiD.setToolTipText("");
    jLabSiD.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabSiDMouseClicked(evt);
      }
    });
    getContentPane().add(jLabSiD, new org.netbeans.lib.awtextra.AbsoluteConstraints(1025, 123, 116, 25));

    labSi$.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labSi$.setForeground(new java.awt.Color(51, 51, 51));
    labSi$.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labSi$.setToolTipText("Total Saldo Inicial en $");
    labSi$.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
    labSi$.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    getContentPane().add(labSi$, new org.netbeans.lib.awtextra.AbsoluteConstraints(1151, 123, 113, 25));

    labSib.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labSib.setForeground(new java.awt.Color(102, 0, 0));
    labSib.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labSib.setToolTipText("Total Saldo inicia Bs ");
    labSib.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 0, 0), 1, true));
    getContentPane().add(labSib, new org.netbeans.lib.awtextra.AbsoluteConstraints(885, 123, 114, 25));

    jLabSiB.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabSiB.setForeground(new java.awt.Color(51, 51, 51));
    jLabSiB.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabSiB.setText("Saldo Inic Ret Bs");
    jLabSiB.setToolTipText("");
    jLabSiB.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabSiBMouseClicked(evt);
      }
    });
    getContentPane().add(jLabSiB, new org.netbeans.lib.awtextra.AbsoluteConstraints(747, 123, 128, 25));

    labCom.setBackground(new java.awt.Color(204, 204, 204));
    labCom.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
    labCom.setForeground(new java.awt.Color(0, 0, 102));
    labCom.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labCom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Copa.png"))); // NOI18N
    labCom.setText("Dias Pago");
    labCom.setToolTipText("Reporte Calculo Dias pago ");
    labCom.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 102), 1, true));
    labCom.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labCom.setOpaque(true);
    labCom.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labComMouseClicked(evt);
      }
    });
    getContentPane().add(labCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(1167, 63, 97, 28));

    jFer.setForeground(new java.awt.Color(0, 0, 102));
    jFer.setToolTipText("Seleccione Dia a Procesar");
    jFer.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
    jFer.setPreferredSize(new java.awt.Dimension(42, 25));
    getContentPane().add(jFer, new org.netbeans.lib.awtextra.AbsoluteConstraints(652, 11, -1, 31));

    txtObs.setBackground(new java.awt.Color(251, 254, 234));
    txtObs.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtObs.setForeground(new java.awt.Color(0, 0, 102));
    txtObs.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    txtObs.setToolTipText("Coloque Cliente a buscar");
    txtObs.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtObs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtObs.setOpaque(false);
    txtObs.setPreferredSize(new java.awt.Dimension(7, 30));
    txtObs.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtObsMouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        txtObsMouseEntered(evt);
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
    getContentPane().add(txtObs, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 650, 1087, -1));

    jLabNot.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabNot.setForeground(new java.awt.Color(102, 0, 51));
    jLabNot.setText("Nota:");
    jLabNot.setToolTipText("");
    jLabNot.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabNotMouseClicked(evt);
      }
    });
    getContentPane().add(jLabNot, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 652, 51, 25));

    jLabSd$1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    jLabSd$1.setForeground(new java.awt.Color(51, 51, 51));
    jLabSd$1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabSd$1.setText("T. Saldo $");
    jLabSd$1.setToolTipText("");
    jLabSd$1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabSd$1MouseClicked(evt);
      }
    });
    getContentPane().add(jLabSd$1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1013, 612, 115, 25));

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/FondoMain.png"))); // NOI18N
    getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1290, 690));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jLabNoeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabNoeMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabNoeMouseClicked

  private void txtNrcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNrcMouseClicked
    iniciaRecCob();
  }//GEN-LAST:event_txtNrcMouseClicked

  private void txtNrcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNrcActionPerformed
    if (isvalidRecCob()) {
      verifRecCob();
    }
  }//GEN-LAST:event_txtNrcActionPerformed

  // Valida Nota Entrega
  public boolean isvalidRecCob() {
    String mox = txtNrc.getText().trim();
    nrc = 0;
    if (mox.length() < 10 && !mox.equals("0")) {
      if (isNumeric(mox)) {
        mox = mox.replace(".", "");
        nrc = Integer.valueOf(mox);
      }
    }
    if (nrc > 0) {
      Importadora imp = new Importadora();
      int nr1 = imp.getNr1();
      int nr2 = imp.getNr2();
      if (nrc < nr1 || nrc > nr2) {
        labMsgR.setText("- No Recibo invalido - Rango  ( " + nr1 + " - " + nr2 + " )");
        txtNrc.setSelectionStart(0);
        txtNrc.setSelectionEnd(txtNrc.getText().length());
        txtNrc.requestFocus();
        return false;
      } else {
        return true;
      }

    } else {
      labMsgR.setText("- No. Recibo Cobro Invalido");
      txtNrc.setSelectionStart(0);
      txtNrc.setSelectionEnd(txtNrc.getText().length());
      txtNrc.requestFocus();
      return false;
    }
  }

  public void verifRecCob() {
    inc = 0;
    bloqueaCampos();
    bloqueaPagos();
    if (buscaRecCob()) {
      getSaldosRec();
    } else {
      getMaxRecCob();
      // Incluir  
      labMsgR.setText("- Ingrese Cliente");
      jFer.setEnabled(true);
      txtCte.setEnabled(true);
      cbCte.setEnabled(true);
      txtObs.setEnabled(false);
      txtCte.requestFocus();
    }
  }

  public boolean getMaxRecCob() {
    ReciboCobro r = new ReciboCobro();
    int nrx = r.getMaxRecCob();
    if (nrx > nrc) {
      labMsgR.setText("- No Recibo debe ser mayor al ultimo " + nrx);
      ImageIcon icon = new ImageIcon(getClass().getResource("/img/ojo.png"));
      String tit = "* AVISO *";
      long tim = 1000;
      Toolkit.getDefaultToolkit().beep();
      String vax = "- No Recibo debe ser\nmayor al ultimo recibo No " + nrx;
      Mensaje msg = new Mensaje(vax, tit, tim, icon);
      return true;
    } else {
      return false;
    }
  }

  public boolean buscaRecCob() {
    coc = "";
    noc = "";
    frc = "";
    tdo = 0;
    toi = 0;
    trd = 0;
    nop = 0;
    tas = 0;
    ts$ = 0;
    tsB = 0;
    obs = "";

    String mx1 = "";
    String mx2 = "";
    String mx3 = "";
    String mx4 = "";
    String nnx = "";
    String nfx = "";
    String dix = "";
    String tix = "";
    String fnt = "";
    String fee = "";
    txtObs.setText(" ");

    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      String vax = "";
      vax = " delete from recibocobroH where (nrc not in (select nrc from recibocobroP) "
              + "and  nrc not in (select nrc from recibocobroD)) "
              + "and nrc= " + nrc;
      st.execute(vax);
      vax = " delete from recibocobroD where nrc not in (select nrc from recibocobroH) "
              + "and nrc= " + nrc;
      st.execute(vax);
      vax = " delete from recibocobroP where nrc not in (select nrc from recibocobroH) "
              + "and nrc= " + nrc;
      st.execute(vax);

      // Header Recibo Cobro - recibocobroH
      String sql = "SELECT frc,coc,(select nom from clientes where clientes.coc=recibocobroH.coc) noc,obs "
              + "FROM recibocobroH "
              + "where nrc =  " + nrc + " ";

      rs = st.executeQuery(sql);
      while (rs.next()) {
        frc = rs.getString("frc");
        coc = rs.getString("coc");
        noc = rs.getString("noc");
        obs = rs.getString("obs");
        obs = obs.trim();
      }

      if (frc.length() == 0) {
        frc = ymdhoy();
        updateFecHeader(1);
      }

      if (coc.length() > 0) {

        // Saldos Iniciales  recibocobroD
        sql = "SELECT nrc,tdo,trd  "
                + "FROM recibocobroD "
                + "where coc = '" + coc + "'"
                + "  and nrc < " + nrc;
        rs = st.executeQuery(sql);
        while (rs.next()) {
          nrx = rs.getInt("nrc");
          tdo = rs.getDouble("tdo");
          trd = rs.getDouble("trd");
          ts$ = ts$ + tdo;   // total $
          tsB = tsB + trd;   // total Bs
        }

        // Saldos Pagos recibocobroP
        sql = "SELECT nrc,tip,tpa,tas "
                + "FROM recibocobroP "
                + "where coc = '" + coc + "'"
                + "  and nrc < " + nrc;
        rs = st.executeQuery(sql);
        while (rs.next()) {
          nrx = rs.getInt("nrc");
          tpa = rs.getDouble("tpa");     // Pago  
          tas = rs.getDouble("tas");     // Tasa
          String tpp = rs.getString("tip");   // Tipo Pago

          tp$ = tpa / tas;

          // Pago retenciones
          if (tpp.equals("2") || tpp.equals("4")) {
            tsB = tsB - (tpa);
          } else {
            ts$ = ts$ - tp$;
          }
        }

        // Inicializa con  saldos iniciales
        double tsi$ = ts$ * -1;
        double tsib = tsB * -1;

        double tb$ = ts$ * -1;
        double tbb = tsB * -1;

        //System.out.println("0- tbb =" + MtoEs(tbb, 2) + ",tb$ =" + MtoEs(tb$, 2));
        // Detalle Documentos - recibocobroD
        sql = "SELECT nrc,tdo,trd  "
                + "FROM recibocobroD "
                + "where coc = '" + coc + "'"
                + "  and nrc= " + nrc;
        rs = st.executeQuery(sql);
        while (rs.next()) {
          nrx = rs.getInt("nrc");
          tdo = rs.getDouble("tdo");
          trd = rs.getDouble("trd");
          ts$ = ts$ + tdo;   // total $
          tsB = tsB + trd;   // total Bs
        }
        // Detalle  Pagos recibocobroP
        sql = "SELECT nrc,tip,tpa,tas "
                + "FROM recibocobroP "
                + "where coc = '" + coc + "'"
                + "  and nrc = " + nrc;
        rs = st.executeQuery(sql);
        while (rs.next()) {
          nrx = rs.getInt("nrc");
          tpa = rs.getDouble("tpa");     // Pago  
          tas = rs.getDouble("tas");     // Tasa
          String tpp = rs.getString("tip");   // Tipo Pago

          tp$ = tpa / tas;

          // PagoSALDO retenciones
          if (tpp.equals("2") || tpp.equals("4")) {
            tpa = tpa;
            tsB = tsB - (tpa);
          } else {
            ts$ = ts$ - tp$;
          }
        }

        //-------------------------
        btnPdf.setEnabled(true);
        btnEli.setEnabled(true);
        jFer.setEnabled(true);
        modTabDoc.setRowCount(0);
        inc = 1;
        labFer.setText(ymd_dmy(frc));
        String nox = " " + coc + " - " + noc;
        cbCte.setSelectedItem(nox);
        txtObs.setText(" " + obs);
        tgdB = 0;
        tgd$ = 0;

        // Detalle Documentos - recibocobroD
        sql = "SELECT tpd,nno,nfa,fno,fen,fev,tdo,toi,trd,fpa,fre "
                + "FROM recibocobroD "
                + "where nrc =  " + nrc
                + " Order by nno,tpd ";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          tpd = rs.getInt("tpd");
          nno = rs.getInt("nno");
          nfa = rs.getInt("nfa");
          fno = rs.getString("fno");
          fen = rs.getString("fen");
          fev = rs.getString("fev");
          tdo = rs.getDouble("tdo");
          toi = rs.getDouble("toi");
          trd = rs.getDouble("trd");
          fep = rs.getString("fpa");
          fre = rs.getString("fre");
          rnf = "" + nno;

          fep = fep.replace("O", "0");
          fep = fep.replace("|", "1");

          tix = "NE";
          if (tpd == 1) {
            tix = "NC";
          }

          if (nfa > 0) {
            rnf = "" + nno + " / " + nfa;
          }
          if (fen == null) {
            fen = "";
          }
          if (fep == null) {
            fep = "";
          }

          fnt = ymd_dmy(fno);
          fee = "";
          if (fen.length() > 0) {
            fee = ymd_dmy(fen);
          }

          String fpg = "";
          if (fep.length() > 0) {
            fpg = ymd_dmy(fep);
          }

          String fex = fno;
          if (fen.length() > 0) {
            fex = fen;
          }

          int dias = 0;
          String feH = ymdhoy();
          if (fep.length() == 0) {
            dias = getdiasFec(feH, fex);
          }
          if (fep.length() > 0) {
            dias = getdiasFec(fep, fex);
          }

          nnx = "" + nno;
          nfx = "" + nfa;
          mx1 = MtoEs(tdo, 2);
          mx2 = MtoEs(toi, 2);
          mx3 = MtoEs(trd, 2);
          dix = "" + dias;
          tgd$ = tgd$ + tdo;
          tgdB = tgdB + trd;

          if (fpg == null || fpg.equals("null")) {
            fpg = "";
          }

          if (tpd == 1) {
            dix = "";
          }

          if (fpg.length() > 0) {
            tix = "<html><font color=\"blue\">" + tix + "</font></html>";
            fnt = "<html><font color=\"blue\">" + fnt + "</font></html>";
            fee = "<html><font color=\"blue\">" + fee + "</font></html>";
            nnx = "<html><font color=\"blue\">" + nnx + "</font></html>";
            nfx = "<html><font color=\"blue\">" + nfx + "</font></html>";
            mx1 = "<html><font color=\"blue\">" + mx1 + "  </font></html>";
            mx2 = "<html><font color=\"blue\">" + mx2 + "</font></html>";
            mx3 = "<html><font color=\"blue\">" + mx3 + "  </font></html>";
            fpg = "<html><font color=\"blue\">" + fpg + "</font></html>";
            dix = "<html><font color=\"blue\">" + dix + "</font></html>";
          } else {
            tix = "<html><font color=\"black\">" + tix + "</font></html>";
            fnt = "<html><font color=\"black\">" + fnt + "</font></html>";
            fee = "<html><font color=\"black\">" + fee + "</font></html>";
            nnx = "<html><font color=\"black\">" + nnx + "</font></html>";
            nfx = "<html><font color=\"black\">" + nfx + "</font></html>";
            mx1 = "<html><font color=\"black\">" + mx1 + "  </font></html>";
            mx2 = "<html><font color=\"black\">" + mx2 + "</font></html>";
            mx3 = "<html><font color=\"black\">" + mx3 + "  </font></html>";
            fpg = "<html><font color=\"black\">" + fpg + "</font></html>";
            dix = "<html><font color=\"black\">" + dix + "</font></html>";
          }
          modTabDoc.addRow(new Object[]{tix, fnt, fee, fpg, dix, nfx, mx2, mx3, nnx, mx1});
        }

        mx1 = MtoEs(tgd$, 2);
        mx3 = MtoEs(tgdB, 2);
        mx1 = "<html><b><font color=\"black\">" + mx1 + "  </font></b></html>";
        mx3 = "<html><b><font color=\"black\">" + mx3 + "  </font></b></html>";
        mx2 = "<html><b><font color=\"black\">TOTAL</font></b></html>";
        modTabDoc.addRow(new Object[]{"", "", "", "", "", mx2, "", mx3, " ", mx1 + "  "});

        // Posiciona ultima file tabDoc;
        int i = tabDoc.getRowCount() - 1;    // Cantidad Filas
        //tabDoc.getSelectionModel().setSelectionInterval(i, i); // Marca fila como seleccionada 
        Rectangle oRect = tabDoc.getCellRect(i, 0, true);
        tabDoc.scrollRectToVisible(oRect);

        fep = "";
        tip = "";
        bce = "";
        bcr = "";
        tpa = 0;
        tas = 0;
        tp$ = 0;
        nop = 0;
        tgp$ = 0; // Total Grl Pago $
        tgpB = 0; // Total Grl Pago Bs

        seteaFecPago();

        //ts$ * -1;
        //tsB * -1;
        // Detalle  Pagos recibocobroP
        modTabPag.setRowCount(0);

        // Añade registros Saldos Iniciales
        if (!MtoEs(tsi$, 2).equals("0,00") || !MtoEs(tsib, 2).equals("0,00")) {
          tix = "*Saldo Inicial*";
          if (tsi$ > 0 || tsib > 0) {
            tix = "*Saldo a Favor*";
          }
          mx1 = MtoEs(tsib, 2);
          mx3 = MtoEs(tsi$, 2);
          tix = "<html><font color=\"black\">" + tix + "</font><></html>";
          mx1 = "<html><font color=\"balck\">" + mx1 + "</font><></html>";
          mx3 = "<html><font color=\"black\">" + mx3 + "  </font><></html>";
          modTabPag.addRow(new Object[]{"", "", "", "", "", tix, mx1, "", mx3 + "  "});
        }

        sql = "SELECT nrc,fep,tip,bce,bcr,ref,tpa,tas,nno,fre "
                + "FROM recibocobroP "
                + "where nrc =  " + nrc + " "
                + "Order by nno,tip,fep";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          nrc = rs.getInt("nrc");
          fep = rs.getString("fep");
          tip = rs.getString("tip");
          bce = rs.getString("bce");
          bcr = rs.getString("bcr");
          ref = rs.getString("ref");
          tpa = rs.getDouble("tpa");
          tas = rs.getDouble("tas");
          nop = rs.getInt("nno");
          fre = rs.getString("fre");

          fep = fep.replace("|", "1");
          fep = fep.replace("O", "0");
          fep = fep.replace("o", "0");

          tix = "Pago";
          if (tip.equals("1")) {
            tix = "Abono";
          }
          if (tip.equals("2")) {
            tix = "Pago Ret Iva";
          }
          if (tip.equals("3")) {
            tix = "Saldo a Favor $";
          }
          if (tip.equals("4")) {
            tix = "Dev Ret Iva";
          }
          if (tip.equals("5")) {
            tix = "Ajuste Saldo";
          }

          tp$ = tpa / tas;

          mx1 = MtoEs(tpa, 2);
          mx2 = MtoEs(tas, 2);
          mx3 = MtoEs(tp$, 2);

          //System.out.println("nop=" + nop + ",tip=" + tip + ",tpa=" + tpa + ",tas=" + tas+",tp$="+tp$);
          //  Pago retencion iva
          if (tip.equals("2") || tip.equals("4")) {
            tbb = tbb + tpa;  // total base Bs
            if (tas == 1) {
              tgpB = tgpB + tpa;  // Bs
              mx2 = "";
              mx3 = "";
            }
          } else {
            //System.out.println("nop=" + nop + ",tp$=" + MtoEs(tp$, 2) + ",tb$=" + MtoEs(tb$, 2));
            tb$ = tb$ + tp$;  // total base $
            if (tas == 1) {
              tgp$ = tgp$ + tp$;
              mx1 = "";
              mx2 = "";
            } else {
              tgpB = tgpB + tpa;  // Bs
            }
          }

          //System.out.println("nop=" + nop + ",tip=" + tip + ",tpa=" + tpa + ",tas=" + tas + ",tp$=" + tp$ + ",tbb=" + tbb);
          modTabPag.addRow(new Object[]{tix, ymd_dmy(fep), nop, bce, bcr, ref, mx1, mx2, mx3 + "  "});

        }

        if (MtoEs(tgp$, 2).indexOf("9,99") >= 0) {
          tgp$ = tgp$ + 0.01;
        }
        if (MtoEs(tgpB, 2).indexOf("9,99") >= 0) {
          tgpB = tgp$ + 0.01;
        }

        mx1 = "<html><b><font color=\"black\">T.BASE</font></b></html>";
        mx2 = "<html><b><font color=\"black\">" + MtoEs(tbb, 2) + "</font></b></html>";
        mx3 = "";
        mx4 = "<html><b><font color=\"black\">" + MtoEs(tb$, 2) + "  </font></b></html>";
        modTabPag.addRow(new Object[]{"", "", "", "", "", mx1, mx2, mx3, mx4});

        mx1 = "<html><b><font color=\"gray\">T.BANCO</font></b></html>";
        mx2 = "<html><b><font color=\"gray\">" + MtoEs(tgpB, 2) + "</font></b></html>";
        mx3 = "";
        mx4 = "<html><b><font color=\"gray\">" + MtoEs(tgp$, 2) + "  </font></b></html>";
        modTabPag.addRow(new Object[]{"", "", "", "", "", mx1, mx2, mx3, mx4});

        int neg = 0;
        if (tgd$ < 0) {
          neg = 1;
          tgd$ = tgd$ * -1;
        }
        String mox = MtoEs(tgd$, 2);
        mox = GetCurrencyDouble(mox);
        tgd$ = GetMtoDouble(mox);
        if (neg == 1) {
          tgd$ = tgd$ * -1;
        }

        neg = 0;
        if (ts$ < 0) {
          neg = 1;
          ts$ = ts$ * -1;
        }
        mox = MtoEs(ts$, 2);
        mox = GetCurrencyDouble(mox);
        ts$ = GetMtoDouble(mox);
        if (neg == 1) {
          ts$ = ts$ * -1;
        }

        neg = 0;
        if (tsB < 0) {
          neg = 1;
          tsB = tsB * -1;
        }
        mox = MtoEs(tsB, 2);
        mox = GetCurrencyDouble(mox);
        tsB = GetMtoDouble(mox);
        if (neg == 1) {
          tsB = tsB * -1;
        }

        mox = MtoEs(tgd$ - ts$, 2);
        mox = GetCurrencyDouble(mox);
        double top = GetMtoDouble(mox);
        // update total pagos $
        sql = "update recibocobroH "
                + "set tod=" + tgd$ + ","
                + "    tpd=" + top + ","
                + "    tpb=" + tsB + " "
                + "Where nrc=" + nrc;
        //st.execute(sql);

        // Posiciona ultima file tabDoc;
        i = tabPag.getRowCount() - 1;    // Cantidad Filas
        //tabPag.getSelectionModel().setSelectionInterval(i, i); // Marca fila como seleccionada 
        oRect = tabPag.getCellRect(i, 0, true);
        tabPag.scrollRectToVisible(oRect);

        labMsgR.setText("");

        if (tdo != 0 || !MtoEs(ts$, 2).equals("0,00") || !MtoEs(tsB, 2).equals("0,00")) {

          btnEli.setEnabled(true);
          btnPdf.setEnabled(true);
          bloqueaPagos();
          cbTip.setEnabled(true);
          txtObs.setEnabled(true);
          cbTip.setSelectedIndex(0);
          txtDia.setEnabled(true);
          txtMes.setEnabled(true);
          txtAno.setEnabled(true);
          cbNe.setEnabled(true);
          if (fep.length() > 0) {
            txtDia.setBorder(new LineBorder(Color.GRAY));
            txtDia.requestFocus();
          } else {
            txtNot.requestFocus();
          }
        }

        cargaNoEFac("0");

      } else {
        labFer.setText(dmyhoy());
      }
      rs.close();
      con.close();

    } catch (SQLException ex) {
      Logger.getLogger(Registro_PagosClientes.class
              .getName()).log(Level.SEVERE, null, ex);
    }
    if (coc.length() > 0) {
      return true;
    } else {
      return false;
    }
  }

  public void getSaldosRec() {

    ts$ = 0; // Total Grl Saldo $
    tsB = 0; // Total Grl Saldo Bs
    tas = 1; // tasa

    labSi$.setOpaque(false);
    labSib.setOpaque(false);
    labSi$.setBackground(new java.awt.Color(240, 240, 240));
    labSib.setBackground(new java.awt.Color(240, 240, 240));

    int nrx = 0;
    String tpp = "0";

    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      // -- Saldos Iniciales --
      // Detalle Documentos - recibocobroD
      String sql = "SELECT nrc,tdo,trd  "
              + "FROM recibocobroD "
              + "where coc = '" + coc + "'"
              + "  and nrc < " + nrc;

      rs = st.executeQuery(sql);
      while (rs.next()) {
        nrx = rs.getInt("nrc");
        tdo = rs.getDouble("tdo");
        trd = rs.getDouble("trd");
        ts$ = ts$ + tdo;   // total $
        tsB = tsB + trd;   // total Bs
      }

      // Detalle  Pagos recibocobroP
      sql = "SELECT nrc,tip,tpa,tas "
              + "FROM recibocobroP "
              + "where coc = '" + coc + "'"
              + "  and nrc < " + nrc;
      rs = st.executeQuery(sql);
      while (rs.next()) {
        nrx = rs.getInt("nrc");
        tpa = rs.getDouble("tpa");    // Pago  
        tas = rs.getDouble("tas");    // Tasa
        tpp = rs.getString("tip");    // Tipo Pago
        tp$ = tpa / tas;
        // Pago retenciones
        if (tpp.equals("2") || tpp.equals("4")) {
          tsB = tsB - tpa;
        } else {
          ts$ = ts$ - tp$;
        }
      }

      //System.out.println("1- tsB =" + MtoEs(tsB, 2) + ",ts$ =" + MtoEs(ts$, 2));
      /// Totales Saldos Iniciales
      if (!MtoEs(ts$, 2).equals("0,00")) {
        labSi$.setOpaque(true);
        labSi$.setBackground(new java.awt.Color(240, 233, 158));
      }
      if (!MtoEs(tsB, 2).equals("0,00")) {
        labSib.setOpaque(true);
        labSib.setBackground(new java.awt.Color(240, 233, 158));
      }

      if (tsB < 0) {
        jLabSiB.setText("Saldo a Favor Bs");
        labSib.setText(MtoEs(tsB * -1, 2) + "  ");
      } else {
        jLabSiB.setText("Saldo Inic Ret Bs");
        labSib.setText(MtoEs(tsB, 2) + "  ");
      }

      if (ts$ < 0) {
        jLabSiD.setText("Saldo a Favor $");
        labSi$.setText(MtoEs(ts$ * -1, 2) + "  ");
      } else {
        jLabSiD.setText("Saldo inicial $");
        labSi$.setText(MtoEs(ts$, 2) + "  ");
      }

      // -- Saldos recibo  --
      tdo = 0; // total nota $
      trd = 0; // total ret Bs
      tpa = 0; // Total pago
      tp$ = 0; // Total pago $

      //ts$ = 0;
      //tsB = 0;
      tgd$ = 0;

      // Detalle Documentos - recibocobroD
      sql = "SELECT nrc,tdo,trd  "
              + "FROM recibocobroD "
              + "where coc = '" + coc + "'"
              + "  and nrc= " + nrc;
      rs = st.executeQuery(sql);

      while (rs.next()) {
        nrx = rs.getInt("nrc");
        tdo = rs.getDouble("tdo");
        trd = rs.getDouble("trd");
        tgd$ = tgd$ + tdo;

        ts$ = ts$ + tdo;   // total $
        tsB = tsB + trd;   // total Bs
      }

      //System.out.println("2- tsB =" + MtoEs(tsB, 2) + ",ts$ =" + MtoEs(ts$, 2));
      // Detalle  Pagos recibocobroP
      sql = "SELECT nrc,tip,tpa,tas "
              + "FROM recibocobroP "
              + "where coc = '" + coc + "'"
              + "  and nrc = " + nrc;
      rs = st.executeQuery(sql);
      while (rs.next()) {
        nrx = rs.getInt("nrc");
        tpa = rs.getDouble("tpa");   // Pago Bs
        tas = rs.getDouble("tas");   // Tasa
        tpp = rs.getString("tip");   // Tipo Pago

        tp$ = tpa / tas;

        // PagoSALDO retenciones
        if (tpp.equals("2") || tpp.equals("4")) {
          tsB = tsB - tpa;
        } else {
          ts$ = ts$ - tp$;
        }
      }

      //System.out.println("3- tsB =" + MtoEs(tsB, 2) + ",ts$ =" + MtoEs(ts$, 2));
      int neg = 0;
      if (tgd$ < 0) {
        neg = 1;
        tgd$ = tgd$ * -1;
      }
      String mox = MtoEs(tgd$, 2);
      mox = GetCurrencyDouble(mox);
      tgd$ = GetMtoDouble(mox);
      if (neg == 1) {
        tgd$ = tgd$ * -1;
      }

      neg = 0;
      if (ts$ < 0) {
        neg = 1;
        ts$ = ts$ * -1;
      }
      mox = MtoEs(ts$, 2);
      mox = GetCurrencyDouble(mox);
      ts$ = GetMtoDouble(mox);
      if (neg == 1) {
        ts$ = ts$ * -1;
      }

      neg = 0;
      if (tsB < 0) {
        neg = 1;
        tsB = tsB * -1;
      }
      mox = MtoEs(tsB, 2);
      mox = GetCurrencyDouble(mox);
      tsB = GetMtoDouble(mox);
      if (neg == 1) {
        tsB = tsB * -1;
      }

      // update total pagos $
      sql = "update recibocobroH "
              + "set tod=" + tgd$ + ","
              + "    tpd=" + ts$ + ","
              + "    tpb=" + tsB + " "
              + "Where nrc=" + nrc;
      st.execute(sql);

      //System.out.println("SD -pag ts$ = " + ts$ + " tsB = " + tsB);
      labTsd$.setBackground(new java.awt.Color(204, 204, 204));
      jLabSd$1.setText("Saldo $");
      if (!MtoEs(ts$, 2).equals("0,00")) {
        labTsd$.setBackground(new java.awt.Color(240, 200, 120));
        if (ts$ < 0) {
          jLabSd$1.setText("Sdo $ a Favor");
          labTsd$.setBackground(new java.awt.Color(240, 233, 150));
          //ts$ = ts$ * -1;
        }
      }
      if (ts$ < 0) {
        labTsd$.setText(MtoEs(ts$ * -1, 2) + "  ");
      } else {
        labTsd$.setText(MtoEs(ts$, 2) + "  ");
      }

      labTsdBs.setBackground(new java.awt.Color(204, 204, 204));
      jLabSdB.setText("T. Sdo Ret Bs");
      if (!MtoEs(tsB, 2).equals("0,00")) {
        labTsdBs.setBackground(new java.awt.Color(240, 200, 120));
        if (tsB < 0) {
          jLabSdB.setText("Sdo Bs a Favor");
          labTsdBs.setBackground(new java.awt.Color(240, 233, 150));
          //tsB = tsB * -1;
        }
      }
      if (tsB < 0) {
        labTsdBs.setText(MtoEs(tsB * -1, 2) + "  ");
      } else {
        labTsdBs.setText(MtoEs(tsB, 2) + "  ");
      }

      if (MtoEs(ts$, 2).equals("0,00") && MtoEs(tsB, 2).equals("0,00")) {
        jLabCua.setVisible(true);
        jLabCua.setText("- Listo -");
      } else {
        jLabCua.setVisible(false);
      }

      if (!MtoEs(ts$, 2).equals("0,00") || !MtoEs(tsB, 2).equals("0,00")) {

        btnEli.setEnabled(true);
        btnPdf.setEnabled(true);
        bloqueaPagos();
        cbTip.setEnabled(true);
        txtObs.setEnabled(true);
        cbTip.setSelectedIndex(0);
        seteaFecPago();

        txtDia.setEnabled(true);
        txtMes.setEnabled(true);
        txtAno.setEnabled(true);
        cbNe.setEnabled(true);
        if (fep.length() > 0) {
          txtDia.setBorder(new LineBorder(Color.GRAY));
          txtDia.requestFocus();
        } else {
          txtNot.requestFocus();
        }
      }

      labMsgR.setText("");
      rs.close();
      con.close();

    } catch (SQLException ex) {
      Logger.getLogger(Registro_PagosClientes.class
              .getName()).log(Level.SEVERE, null, ex);
    }

  }

  public String getFecNota(int nno) {
    String fex = "";
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      // Fecha nota
      String sql = "SELECT min(fno) fno "
              + "FROM recibocobroD "
              + "where nrc = " + nrc
              + "  and nno = " + nno;
      rs = st.executeQuery(sql);
      while (rs.next()) {
        fex = rs.getString("fno");
      }
      rs.close();
      con.close();

    } catch (SQLException ex) {
      Logger.getLogger(Registro_PagosClientes.class
              .getName()).log(Level.SEVERE, null, ex);
    }

    return fex;
  }

  public void getTotNota(int nno) {
    tdo = 0; // total nota
    tp$ = 0; // total pago
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      // Total Nota 
      String sql = "SELECT sum(tdo) tdo "
              + "FROM recibocobroD "
              + "where nrc = " + nrc
              + "  and tpd =0"
              + "  and nno = " + nno;
      //Tot Ret25% Iva

      if (tip.equals("2")) {

        sql = "SELECT sum(trd) tdo "
                + "FROM recibocobroD "
                + "where nrc = " + nrc
                + "  and tpd =0"
                + "  and nfa = " + nno;
      }

      rs = st.executeQuery(sql);
      while (rs.next()) {
        tdo = rs.getDouble("tdo");
      }

      // Verifica si tiene Nota credito
      if (tip.equals("2")) {
        double tre = 0;
        sql = "SELECT sum(toi-tor) tre "
                + "FROM notacred "
                + "where nne = " + nno + " "
                + "  and ncr in (select nno from recibocobroD where nrc=" + nrc + " and tpd=1)";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          tre = rs.getDouble("tre");
          tdo = tdo - tre;
          String mox = MtoEs(tdo, 2);
          mox = GetCurrencyDouble(mox);
          //tdo = GetMtoDouble(mox);
        }
      }

      // Aplica Ret25% Iva
      if (!tip.equals("2")) {
        // Verifica si tiene Nota credito
        double tnc = 0;
        // Descuento Nota Credito
        sql = "SELECT sum(tnc) tnc "
                + "FROM notacred "
                + "where nne = " + nno + " "
                + "  and ncr in (select nno from recibocobroD where nrc=" + nrc + " and tpd=1)";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          tnc = rs.getDouble("tnc");
          tdo = tdo - tnc;
        }

        // total pagos
        sql = "SELECT tip,tpa,tas "
                + "FROM recibocobroP "
                + "where nrc = " + nrc
                + "  and nno = " + nno;
        rs = st.executeQuery(sql);
        while (rs.next()) {
          tpa = rs.getDouble("tpa");     // Pago  
          tas = rs.getDouble("tas");     // Tasa
          tp$ = tpa / tas;
          tdo = tdo - tpa;
        }
      }

      if (tdo < 0) {
        tdo = 0;
      }

      rs.close();
      con.close();

    } catch (SQLException ex) {
      Logger.getLogger(Registro_PagosClientes.class
              .getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void presentaCampos() {

  }

  private void labBusNpgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labBusNpgMouseClicked
    iniciaRecCob();
    labMsgR.setText("- BUSCANDO PEDIDOS ESPERE !");
    Thread hilo = new Thread() {
      public void run() {
        if (conNrc != null) {
          conNrc.dispose();
        }
        conNrc = new Consulta_ReciboCobro(ctrN, coc);
        conNrc.setEnabled(true);
        conNrc.setExtendedState(NORMAL);
        conNrc.setVisible(true);
      }
    };
    hilo.start();


  }//GEN-LAST:event_labBusNpgMouseClicked

  private void clockDigital1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clockDigital1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_clockDigital1MouseClicked

  private void clockDigital1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clockDigital1MouseEntered

  }//GEN-LAST:event_clockDigital1MouseEntered

  private void tabDocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabDocMouseClicked

    if (evt.getClickCount() == 1) {
      if (tabDoc.getRowCount() > 0) {
        if (tabDoc.getSelectedRow() >= 0) {
          btnEld.setEnabled(true);
          tabPag.getSelectionModel().clearSelection();  // Quitar Seleccion 
        }
      }
    }
    if (evt.getClickCount() == 2) {
      eliminaDoc();
    }
  }//GEN-LAST:event_tabDocMouseClicked

  public void eliminaDoc() {
    int[] sel = tabDoc.getSelectedRows();
    labMsgR.setText("");
    if (tabDoc.getRowCount() > 0) {
      if (tabDoc.getSelectedRow() >= 0) {

        String vax = "";
        int row = tabDoc.getSelectedRow();    // Fila Selecciuonada
        int col = tabDoc.getSelectedColumn(); // Columna Seleccionada
        if ((row != -1) && (col != -1)) {
          vax = modTabDoc.getValueAt(tabDoc.getSelectedRow(), 1).toString();
        }

        if (vax.length() > 0) {
          icon = new ImageIcon(getClass().getResource("/img/elim.png"));
          vax = "Desea Eliminar Documento Seleccionado ?";
          if (sel.length > 1) {
            vax = "Desea Eliminar Documentos Seleccionados ?";
          }
          String[] options = {"SI", "NO"};
          int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
          if (opcion == 0) {
            //Borra Seleccionadas
            int c = 0;
            try {
              ConexionSQL bdsql = new ConexionSQL();
              Connection con = bdsql.Conectar();
              Statement st = con.createStatement();
              int numRows = tabDoc.getSelectedRows().length;

              for (int i = 0; i < numRows; i++) {

                String tid = modTabDoc.getValueAt(tabDoc.getSelectedRow(), 0).toString();
                String fec = modTabDoc.getValueAt(tabDoc.getSelectedRow(), 1).toString();
                String doc = modTabDoc.getValueAt(tabDoc.getSelectedRow(), 8).toString();
                String tod = modTabDoc.getValueAt(tabDoc.getSelectedRow(), 9).toString();

                tid = tid.replace("<html><font color=\"blue\">", "").replace("</font></html>", "").trim();
                fec = fec.replace("<html><font color=\"blue\">", "").replace("</font></html>", "").trim();
                doc = doc.replace("<html><font color=\"blue\">", "").replace("</font></html>", "").trim();
                tod = tod.replace("<html><font color=\"blue\">", "").replace("</font></html>", "").trim();

                tid = tid.replace("<html><font color=\"black\">", "").replace("</font></html>", "").trim();
                fec = fec.replace("<html><font color=\"black\">", "").replace("</font></html>", "").trim();
                doc = doc.replace("<html><font color=\"black\">", "").replace("</font></html>", "").trim();
                tod = tod.replace("<html><font color=\"black\">", "").replace("</font></html>", "").trim();

                //System.out.println("tid=" + tid + ",fec=" + fec + ",doc=" + doc + ",tod=" + tod);
                int tpd = 0;
                if (tid.equals("NC")) {
                  tpd = 1;
                }

                fec = fec.replace("-", "").replace("/", "");
                fec = dmy_ymd(fec);
                doc = doc.trim();
                nop = 0;

                if (isNumeric(doc)) {

                  nop = Integer.valueOf(doc);

                  // verifica documento no tenga pagos
                  String sql = "SELECT count(*) can "
                          + "FROM recibocobroP "
                          + "where nrc =  " + nrc + " "
                          + "and nno = " + nop;
                  ResultSet rs = st.executeQuery(sql);
                  while (rs.next()) {
                    c = rs.getInt("can");
                  }

                  if (c == 0) {
                    // remueve fila tabla
                    modTabDoc.removeRow(tabDoc.getSelectedRow());
                    // Elimina Detalle Documentos (recibocobroD)
                    sql = "delete from recibocobroD "
                            + "where nrc = " + nrc
                            + "  and fno = '" + fec + "'"
                            + "  and tpd = " + tpd
                            + "  and nno = " + nop;
                    st.execute(sql);
                    txtNot.requestFocus();
                    buscaRecCob();
                  }
                }
              }
              btnEld.setEnabled(false);
              st.close();
              con.close();

              if (c > 0) {
                labMsgR.setText("- Nota ( " + nop + " )  tiene pago aplicado");
              } else {
                cargaNoEFac("0");
                getSaldosRec();
              }

            } catch (SQLException ex) {
              Logger.getLogger(Registro_PagosClientes.class
                      .getName()).log(Level.SEVERE, null, ex);
            }

          }
        } else {
          labMsgR.setText("- Debe Seleccionar un registro");
        }
      } else {
        labMsgR.setText("- Tabla Vacia");
      }
    }
  }


  private void cbNeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbNeItemStateChanged

    txtTop.setEnabled(false);
    txtTop.setText("");
    nop = 0;
    int idx = cbNe.getSelectedIndex();
    if (idx > 0 && evt.getSource() == cbNe && evt.getStateChange() == 1) {
      String str = (String) cbNe.getSelectedItem().toString();
      if (str != null) {
        if (str.length() > 0) {
          str = str.trim();
          if (isNumeric(str)) {
            String fnx = "";
            nop = Integer.valueOf(str);

            String vax = txtDia.getText();
            if (vax.length() == 1) {
              vax = "0" + vax;
              txtDia.setText(vax);
            }
            vax = txtMes.getText();
            if (vax.length() == 1) {
              vax = "0" + vax;
              txtMes.setText(vax);
            }
            fep = txtAno.getText() + txtMes.getText() + txtDia.getText();
            fep = fep.replace("|", "1");
            fep = fep.replace("O", "0");
            fep = fep.replace("o", "0");

            if (!tip.contains("2") && !tip.contains("4")) {
              fnx = getFecNota(nop);
            } else {
              fnx = fep;
            }

            if (fep.compareTo(fnx) < 0) {

              labMsgR.setText("- Fecha Pago es menor a fecha nota");

              txtDia.setSelectionStart(0);
              txtDia.setSelectionEnd(txtDia.getText().length());
              txtMes.setSelectionStart(0);
              txtMes.setSelectionEnd(txtMes.getText().length());
              txtAno.setSelectionStart(0);
              txtAno.setSelectionEnd(txtAno.getText().length());
              cbNe.setSelectedIndex(-1);
              txtDia.requestFocus();

            } else {
              tdo = 0;

              getTotNota(nop);

              txtTop.setEnabled(true);
              labMsgR.setText("- Ingrese Monto pago");
              if (tip.contains("4")) {
                txtTop.setText(MtoEs(tsB * -1, 2).replace(".", "").replace(",", ".") + "  ");
              } else {
                txtTop.setText(MtoEs(tdo, 2).replace(".", "").replace(",", ".") + "  ");
              }
              txtTop.setEnabled(true);
              txtTop.setSelectionStart(0);
              txtTop.setSelectionEnd(txtTop.getText().length());
              txtTop.requestFocus();
            }
          }
        }
      }
    }
  }//GEN-LAST:event_cbNeItemStateChanged

  private void cbNeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNeActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbNeActionPerformed

  private void jLabNoe2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabNoe2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabNoe2MouseClicked

  private void tabPagMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPagMouseClicked

    if (evt.getClickCount() == 1) {
      if (tabPag.getRowCount() > 0) {
        if (tabPag.getSelectedRow() >= 0) {
          tabDoc.getSelectionModel().clearSelection();  // Quitar Seleccion 
          btnEld.setEnabled(true);
        }
      }
    }

    if (evt.getClickCount() == 2) {
      eliminaPag();
    }

  }//GEN-LAST:event_tabPagMouseClicked

  public void eliminaPag() {
    int[] sel = tabPag.getSelectedRows();
    labMsgR.setText("");
    if (tabPag.getRowCount() > 0) {
      if (tabPag.getSelectedRow() >= 0) {

        String vax = "";
        int row = tabPag.getSelectedRow();    // Fila Selecciuonada
        int col = tabPag.getSelectedColumn(); // Columna Seleccionada
        if ((row != -1) && (col != -1)) {
          vax = modTabPag.getValueAt(tabPag.getSelectedRow(), 1).toString();
        }

        if (vax.length() > 0) {

          icon = new ImageIcon(getClass().getResource("/img/elim.png"));
          vax = "Desea Eliminar Pago Seleccionado ?";
          if (sel.length > 1) {
            vax = "Desea Eliminar Pagos Seleccionados ?";
          }
          String[] options = {"SI", "NO"};
          int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
          if (opcion == 0) {
            //Borra Seleccionadas
            try {
              ConexionSQL bdsql = new ConexionSQL();
              Connection con = bdsql.Conectar();
              Statement st = con.createStatement();
              int numRows = tabPag.getSelectedRows().length;
              for (int i = 0; i < numRows; i++) {
                String tid = modTabPag.getValueAt(tabPag.getSelectedRow(), 0).toString();
                String fec = modTabPag.getValueAt(tabPag.getSelectedRow(), 1).toString();
                String doc = modTabPag.getValueAt(tabPag.getSelectedRow(), 2).toString();

                tip = "0";
                if (tid.contains("Abono")) {
                  tip = "1";
                }
                if (tid.contains("Pago Ret Iva")) {
                  tip = "2";
                }
                if (tid.contains("Saldo a Favor $")) {
                  tip = "3";
                }
                if (tid.contains("Dev Ret Iva")) {
                  tip = "4";
                }
                if (tid.contains("Ajuste Saldo")) {
                  tip = "5";
                }
                fec = fec.replace("-", "").replace("/", "");
                fec = dmy_ymd(fec);

                if (isNumeric(doc)) {

                  nop = Integer.valueOf(doc);

                  // remueve fila tabla
                  modTabPag.removeRow(tabPag.getSelectedRow());
                  // Elimina Detalle Documentos (recibocobroD)
                  String sql = "delete from recibocobroP "
                          + "where nrc = " + nrc
                          + "  and tip = '" + tip + "'"
                          + "  and fep = '" + fec + "'"
                          + "  and nno = " + nop;
                  st.execute(sql);

                  // selecciona ultima fec por nota
                  String fex = "";
                  sql = "SELECT max(fep) fep from recibocobroP "
                          + "where nrc=" + nrc + " "
                          + "  and nno= " + nop + " ";
                  ResultSet rs = st.executeQuery(sql);
                  while (rs.next()) {
                    fex = rs.getString("fep");
                  }
                  if (fex == null || fex.equals("null")) {
                    fex = "";
                  }
                  // Actualiza fecha pago y monto en documento
                  sql = "update recibocobroD "
                          + "set fpa='" + fex + "',"
                          + "tpa=0 "
                          + "Where nrc=" + nrc + " "
                          + "  and tpd=0 "
                          + "  and nno=" + nop;
                  st.execute(sql);
                  buscaRecCob();
                  getSaldosRec();
                  txtNot.requestFocus();
                }
              }
              btnEld.setEnabled(false);
              st.close();
              con.close();

            } catch (SQLException ex) {
              Logger.getLogger(Registro_PagosClientes.class
                      .getName()).log(Level.SEVERE, null, ex);
            }
            cargaNoEFac("0");
          }
        } else {
          labMsgR.setText("- Debe Seleccionar el Pago");
        }
      } else {
        labMsgR.setText("- Tabla Vacia");
      }
    }
  }

  private void jLabCteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCteMouseClicked

  }//GEN-LAST:event_jLabCteMouseClicked

  private void labBusNenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labBusNenMouseClicked
    if (coc.length() > 0) {
      if (cbNE.isSelected()) {
        if (conNoe != null) {
          conNoe.dispose();
        }
        conNoe = new Consulta_NotaEntrega(ctrN, coc);
        conNoe.setEnabled(true);
        conNoe.setExtendedState(NORMAL);
        conNoe.setVisible(true);
      } else {
        if (conNcr != null) {
          conNcr.dispose();
        }
        conNcr = new Consulta_NotaCredito(ctrN, coc);
        conNcr.setEnabled(true);
        conNcr.setExtendedState(NORMAL);
        conNcr.setVisible(true);
      }
    }
  }//GEN-LAST:event_labBusNenMouseClicked

  private void jLabCte1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCte1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCte1MouseClicked

  private void txtNotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNotMouseClicked
    iniciaDoc();
  }//GEN-LAST:event_txtNotMouseClicked

  public void iniciaDoc() {
    txtNot.setText("");
    txtNot.requestFocus();
    tabDoc.getSelectionModel().clearSelection();  // Quitar Seleccion 
  }

  private void txtNotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNotActionPerformed
    if (cbNE.isSelected() == true) {
      tpd = 0;
    } else {
      tpd = 1;
    }
    aplicaNota();
  }//GEN-LAST:event_txtNotActionPerformed

  public void aplicaNota() {
    if (isvalidNota()) {

      if (buscaNotaRecCob()) {
        if (tpd == 0) {
          labMsgR.setText("-  Not/Ent ya fue registrada con Rec Cobro ( " + nrx + " )");
          txtNot.setSelectionStart(0);
          txtNot.setSelectionEnd(txtNot.getText().length());
          txtNot.requestFocus();
        }
      } else {

        if (buscaNota()) {
          trd = toi - tor;
          String mox = MtoEs(trd, 2);
          mox = GetCurrencyDouble(mox);
          trd = GetMtoDouble(mox);
          grabaDocumento();
          cargaNoEFac("0");
          txtNot.setText("");
          labMsgR.setText("-  Ingrese Nota Entrega");
          txtNot.setSelectionStart(0);
          txtNot.setSelectionEnd(txtNot.getText().length());
          // Busca Nota Credito 
          if (tpd == 0 && buscaNC()) {
            tpd = 1;
            nno = nnc;
            txtNot.setText("" + nno);
            aplicaNota();
            txtNot.setText("");
          }
          tpd = 0;
          txtNot.requestFocus();
        }
      }
    }
  }

  public boolean buscaNotaRecCob() {
    nrx = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT distinct nrc "
              + "FROM recibocobroD "
              + "where  tpd=" + tpd + " and nno = " + nno;
      rs = st.executeQuery(sql);
      while (rs.next()) {
        nrx = rs.getInt("nrc");
      }
      rs.close();
      con.close();

    } catch (SQLException ex) {
      Logger.getLogger(Registro_PagosClientes.class
              .getName()).log(Level.SEVERE, null, ex);
    }
    if (nrx > 0) {
      labFer.setText(dmyhoy());
      return true;
    } else {
      labFer.setText(dmyhoy());
      return false;
    }
  }

  // Busca Nota de Entrega 
  public boolean buscaNota() {
    nfa = 0;
    tdo = 0;
    tfa = 0;
    tdf = 0;
    iva = 0;
    toi = 0;
    por = 0;
    tor = 0;
    trd = 0;
    fen = "";
    fno = "";
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT nfa,fne fno,fee fen,fev,tne-tdn tdo,tdn,tfa,tdf,toi,iva,pre por,tor "
              + "FROM notaent "
              + "where coc='" + coc + "' and nne = " + nno + "";
      // Nota Credito
      if (tpd == 1) {
        sql = "SELECT nfa,fnc fno,'' fen,'' fev,tnc*-1 tdo,0 tdn,0 tfa,0 tdf,toi*-1 toi,iva*-1 iva,pre*-1 por,tor*-1 tor "
                + "FROM notacred "
                + "where coc='" + coc + "' and ncr = " + nno + "";
      }

      rs = st.executeQuery(sql);
      while (rs.next()) {
        fno = rs.getString("fno");
        fen = rs.getString("fen");
        fev = rs.getString("fev");
        nfa = rs.getInt("nfa");
        tdo = rs.getDouble("tdo");
        tdn = rs.getDouble("tdn");
        tfa = rs.getDouble("tfa");
        tdf = rs.getDouble("tdf");
        iva = rs.getDouble("iva");
        toi = rs.getDouble("toi");
        por = rs.getDouble("por");
        tor = rs.getDouble("tor");
      }
      rs.close();
      con.close();

    } catch (SQLException ex) {
      Logger.getLogger(Registro_PagosClientes.class
              .getName()).log(Level.SEVERE, null, ex);
    }
    if (tdo == 0) {
      if (tpd == 0) {
        labMsgR.setText("- Nota Entrega no registrada al cliente");
      } else {
        labMsgR.setText("- Nota Credito no registrada al cliente");
      }
      txtNot.setSelectionStart(0);
      txtNot.setSelectionEnd(txtNot.getText().length());
      txtNot.requestFocus();
      return false;
    } else {
      return true;
    }
  }

  // Busca Nota credito 
  public boolean buscaNC() {
    nnc = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT ncr "
              + "FROM notacred "
              + "where coc='" + coc + "' and nne = " + nno + "";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        nnc = rs.getInt("ncr");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_PagosClientes.class
              .getName()).log(Level.SEVERE, null, ex);
    }
    if (nnc == 0) {
      return false;
    } else {
      return true;
    }
  }

  // Valida campo Nota de entrega 
  public boolean isvalidNota() {
    nno = 0;
    String mox = txtNot.getText().trim();
    if (mox.length() > 0 && !mox.equals("0")) {
      if (isNumeric(mox)) {
        mox = mox.replace(".", "");
        nno = Integer.valueOf(mox);
      }
    }
    if (nno > 0) {
      return true;
    } else {
      if (tpd == 0) {
        labMsgR.setText("- Nota Entrega Invalida");
      } else {
        labMsgR.setText("- Nota Credito Invalida");
      }
      txtNot.setSelectionStart(0);
      txtNot.setSelectionEnd(txtNot.getText().length());
      txtNot.requestFocus();
      return false;
    }
  }

  public void grabaDocumento() {
    if (coc.length() > 0) {

      try {

        fre = ymdhoyhhmm();
        int c = 0;

        ConexionSQL bdsql = new ConexionSQL();
        Connection con = bdsql.Conectar();
        Statement st = con.createStatement();
        ResultSet rs = null;

        // Graba Header recibocobroH
        String sql = "SELECT count(*) can from recibocobroH "
                + "Where nrc=" + nrc;
        rs = st.executeQuery(sql);
        while (rs.next()) {
          c = rs.getInt("can");
        }
        if (c == 0) {

          sta = "0";
          if (obs.length() > 200) {
            obs = obs.substring(0, 200);
          }
          sql = "Insert into recibocobroH "
                  + "(nrc,frc,coc,noc,tod,tpd,tpb,sta,obs,fre) "
                  + "VALUES ("
                  + nrc + ","
                  + "'" + frc + "',"
                  + "'" + coc + "',"
                  + "'" + noc + "',"
                  + "0,"
                  + "0,"
                  + "0,"
                  + "'" + sta + "',"
                  + "'" + obs + "',"
                  + "'" + fre + "')";
          st.execute(sql);
        }

        c = 0;
        // Graba Detalle Documentos  recibocobroD
        sql = "SELECT count(*) can from recibocobroD "
                + "Where nrc=" + nrc
                + "  and tpd=" + tpd
                + "  and nno=" + nno;
        rs = st.executeQuery(sql);
        while (rs.next()) {
          c = rs.getInt("can");
        }
        // Graba Detalle Pedido
        if (c == 0) {

          fre = ymdhoyhhmm();

          if (coc.length() != 0 && fno.length() != 0 && tdo != 0) {
            sql = "Insert into recibocobroD "
                    + "(nrc,coc,tpd,nno,nfa,fno,fen,fev,tdo,tdn,toi,trd,fpa,tpa,fre) "
                    + "VALUES ("
                    + nrc + ","
                    + "'" + coc + "',"
                    + tpd + ","
                    + nno + ","
                    + nfa + ","
                    + "'" + fno + "',"
                    + "'" + fen + "',"
                    + "'" + fev + "',"
                    + tdo + ","
                    + tdn + ","
                    + toi + ","
                    + trd + ","
                    + "'',0,"
                    + "'" + fre + "')";
            st.execute(sql);

            String tix = "NE";
            if (tpd == 1) {
              tix = "NC";
            }

            rnf = "" + nno;
            if (nfa > 0) {
              rnf = "" + nno + " / " + nfa;
            }

            if (fen.length() == 0) {
              fen = fno;
            }

            String fex = "";
            if (fen.length() > 0) {
              fex = ymd_dmy(fen);
            }

            seteaFecPago();

            modTabDoc.addRow(new Object[]{tix, ymd_dmy(fno), fex, "", "", nfa, MtoEs(toi, 2), MtoEs(trd, 2), "", "", nno, MtoEs(tdo, 2) + "  "});
            labMsgR.setText("- Se grabo registro - ");

          }

          // Posiciona ultima file tabDoc;
          int i = tabDoc.getRowCount() - 1;    // Cantidad Filas
          tabDoc.getSelectionModel().setSelectionInterval(i, i); // Marca fila como seleccionada 
          Rectangle oRect = tabDoc.getCellRect(i, 0, true);
          tabDoc.scrollRectToVisible(oRect);
          getSaldosRec();
          bloqueaPagos();

          txtDia.setEnabled(true);
          txtMes.setEnabled(true);
          txtAno.setEnabled(true);

          cbTip.setEnabled(true);
          txtObs.setEnabled(true);
          cbTip.requestFocus();
          inc = 1;

        }

        //labMsgP.setText("- Se actualizo  ( " + cop + " )");
        rs.close();
        con.commit();
        con.close();

        // Actualiza total en recibocobroH 
        btnEli.setEnabled(true);
        btnPdf.setEnabled(true);

        buscaRecCob();

      } catch (SQLException ex) {
        Logger.getLogger(Registro_PagosClientes.class
                .getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      labMsgR.setText("- Debe colocar Cliente -");
    }
  }


  private void jLabCte6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCte6MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCte6MouseClicked

  private void jLabCte7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCte7MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCte7MouseClicked

  private void txtTopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTopMouseClicked
    String mox = txtTop.getText().trim();
    mox = GetCurrencyDouble(mox);
    txtTop.setText(mox + "  ");
    txtTop.setSelectionStart(0);
    txtTop.setSelectionEnd(txtTop.getText().length());
    txtTop.requestFocus();
  }//GEN-LAST:event_txtTopMouseClicked

  private void txtTopMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTopMouseEntered
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTopMouseEntered

  private void txtTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTopActionPerformed
    if (isvalidTop(0)) {
      labMsgR.setText("- Ingrese Tasa");
      txtTap.setEnabled(true);
      txtTap.setText("1");
      txtTap.setSelectionStart(0);
      txtTap.setSelectionEnd(txtTap.getText().length());
      txtTap.requestFocus();
    }
  }//GEN-LAST:event_txtTopActionPerformed

  // Valida Total Pago
  public boolean isvalidTop(int ind) {
    tpa = 0;
    tas = 1;
    String mox = txtTop.getText().trim();
    String mox2 = "";
    int i = mox.indexOf("*");
    if (i >= 0) {
      mox2 = mox.substring(i + 1, mox.length());
      if (mox2.length() > 0) {
        if (isNumeric(mox2)) {
          mox2 = GetCurrencyDouble(mox2);
          tas = GetMtoDouble(mox2);
        }
      }
      mox = mox.substring(0, i);
    }
    if (mox.length() > 0 && !mox.equals("0")) {
      if (!isNumeric(mox) && ind == 1) {
        mox = mox.replace(".", "");
        mox = mox.replace(",", ".");
      }
    }

    int indn = 0;
    if ((tip.equals("4") || tip.equals("5")) && mox.indexOf("-") >= 0) {
      indn = 1;
      mox = mox.replace("-", "");
    }

    if (isNumeric(mox)) {
      mox = GetCurrencyDouble(mox);
      tpa = GetMtoDouble(mox);
      if (i >= 0) {
        tpa = tpa * tas;
      }
      txtTop.setText(MtoEs(tpa, 2) + "  ");
    }

    //System.out.println("tpa=" + tpa + ",tip=" + tip);
    if (tpa > 0) {
      if ((tip.equals("4") || tip.equals("5")) && indn == 1) {
        tpa = tpa * -1;
        txtTop.setText(MtoEs(tpa, 2) + "  ");
      }
      return true;
    } else {
      labMsgR.setText("- Total pago invalido");
      txtTop.setSelectionStart(0);
      txtTop.setSelectionEnd(txtTop.getText().length());
      txtTop.requestFocus();
      return false;
    }
  }

  private void txtTopKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTopKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTopKeyReleased

  private void txtTpdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTpdMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTpdMouseClicked

  private void txtTpdMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTpdMouseEntered
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTpdMouseEntered

  private void txtTpdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTpdActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTpdActionPerformed

  private void txtTpdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTpdKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTpdKeyReleased

  private void cbBcrItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbBcrItemStateChanged
    bcr = "";
    int idx = cbBcr.getSelectedIndex();
    if (idx > 0 && evt.getSource() == cbBcr && evt.getStateChange() == 1) {
      String sel = (String) cbBcr.getSelectedItem();  //valor item seleccionado
      if (sel != null) {
        if (sel.length() > 0) {
          bcr = sel.trim();
          txtRef.setEnabled(true);
          txtRef.requestFocus();
        }
      }
    }
  }//GEN-LAST:event_cbBcrItemStateChanged

  private void cbBcrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBcrActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbBcrActionPerformed

  private void jLabCte9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCte9MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCte9MouseClicked

  private void jLabCte10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCte10MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCte10MouseClicked

  private void cbBceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbBceItemStateChanged
    bce = "";
    int idx = cbBce.getSelectedIndex();
    if (idx > 0 && evt.getSource() == cbBce && evt.getStateChange() == 1) {
      String sel = (String) cbBce.getSelectedItem();  //valor item seleccionado
      if (sel != null) {
        if (sel.length() > 0) {
          bce = sel.trim();
          cbBcr.setEnabled(true);
          cbBcr.requestFocus();
        }
      }
    }
  }//GEN-LAST:event_cbBceItemStateChanged

  private void cbBceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBceActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbBceActionPerformed

  private void jLabCte11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCte11MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCte11MouseClicked

  private void txtRefMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRefMouseClicked

  }//GEN-LAST:event_txtRefMouseClicked

  private void txtRefMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRefMouseEntered
    // TODO add your handling code here:
  }//GEN-LAST:event_txtRefMouseEntered

  private void txtRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRefActionPerformed
    ref = txtRef.getText().toUpperCase().trim();
    if (ref.length() > 20) {
      ref = ref.substring(0, 20);
    }
    if (ref.length() == 0) {
      txtRef.requestFocus();
      labMsgR.setText("-  Ingrese No.Referencia");
    } else {
      txtObs.setEnabled(true);
      btnGrp.setEnabled(true);
      btnGrp.requestFocus();
    }
  }//GEN-LAST:event_txtRefActionPerformed

  private void txtRefKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRefKeyReleased
    ref = txtRef.getText().toUpperCase().trim();
    if (ref.length() > 20) {
      ref = ref.substring(0, 20);
      txtRef.requestFocus();
    }
    if (ref.length() > 0) {
      txtObs.setEnabled(true);
      btnGrp.setEnabled(true);
    } else {
      btnGrp.setEnabled(false);
    }

  }//GEN-LAST:event_txtRefKeyReleased

  private void txtTapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTapMouseClicked
    String mox = txtTap.getText().trim();
    mox = GetCurrencyDouble(mox);
    txtTap.setText(mox + "  ");
    txtTap.setSelectionStart(0);
    txtTap.setSelectionEnd(txtTap.getText().length());
    txtTap.requestFocus();
  }//GEN-LAST:event_txtTapMouseClicked

  private void txtTapMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTapMouseEntered
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTapMouseEntered

  private void txtTapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTapActionPerformed
    if (isvalidTasaPago(0)) {
      tp$ = tpa / tas;
      txtTpd.setEnabled(true);
      txtTpd.setText(MtoEs(tp$, 2) + "  ");
      cbBce.setEnabled(true);
      labMsgR.setText("-  Ingrese Banco Emisor");
      cbBce.requestFocus();

      if (tip.equals("0") || tip.equals("1")) {
        btnGrp.setEnabled(false);
      } else {
        txtRef.setEnabled(true);
        btnGrp.setEnabled(true);
        if (tip.equals("3") || tip.equals("4")) {
          txtRef.requestFocus();
        } else {
          cbBce.requestFocus();
        }
      }
    }
  }//GEN-LAST:event_txtTapActionPerformed

  public void grabaPago() {
    labMsgR.setBackground(Color.LIGHT_GRAY);

    if (coc.length() > 0) {

      if (validaReferencia()) {

        try {

          fre = ymdhoyhhmm();
          int c = 0;

          ConexionSQL bdsql = new ConexionSQL();
          Connection con = bdsql.Conectar();
          Statement st = con.createStatement();

          // Graba Detalle Pedido
          if (coc.length() != 0 && fep.length() != 0 && tpa != 0) {

            fre = ymdhoyhhmm();
            if (ref.length() > 20) {
              ref = ref.substring(0, 20);
            }
            // Saldo a favor
            if (tip.contains("3")) {
              if (tpa > 0) {
                tpa = tpa * -1;
              }
            }

            if (tip.contains("4")) {
              tpa = tpa * -1;
            }

            String fex = fep;

            tp$ = tpa / tas;

            fep = fep.replace("|", "1");
            fep = fep.replace("O", "0");
            fep = fep.replace("o", "0");

            String sql = "Insert into recibocobroP "
                    + "(nrc,coc,fep,tip,bce,bcr,ref,tpa,tas,tpd,nno,fre) "
                    + "VALUES ("
                    + nrc + ","
                    + "'" + coc + "',"
                    + "'" + fep + "',"
                    + "'" + tip + "',"
                    + "'" + bce + "',"
                    + "'" + bcr + "',"
                    + "'" + ref + "',"
                    + tpa + ","
                    + tas + ","
                    + tp$ + ","
                    + nop + ","
                    + "'" + fre + "')";
            st.execute(sql);

            // selecciona ultima fec por nota
            sql = "SELECT max(fep) fep from recibocobroP "
                    + "where nrc=" + nrc + " "
                    + "  and nno= " + nop + " ";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
              fex = rs.getString("fep");
            }

            if (fex == null || fex.equals("null")) {
              fex = "";
            }

            // Actualiza fecha pago y monto en documento
            sql = "update recibocobroD "
                    + "set fpa='" + fex + "',"
                    + "tpa=" + tp$ + " "
                    + "Where nrc=" + nrc + " "
                    + "  and nno=" + nop;
            st.execute(sql);

          }

          con.commit();
          con.close();

          if (tip.equals("4")) {
            grabaDocumento();
          }
          getSaldosRec();
          buscaRecCob();

          bloqueaPagos();
          cbTip.setEnabled(true);
          txtObs.setEnabled(true);
          cbTip.requestFocus();
          labMsgR.setText("-  Se grabo Pago");

        } catch (SQLException ex) {
          Logger.getLogger(Registro_PagosClientes.class
                  .getName()).log(Level.SEVERE, null, ex);
        }
      } else {
        labMsgR.setBackground(Color.yellow);
        labMsgR.setText("*** Referencia ya Existe ***".toUpperCase());
      }
    } else {
      labMsgR.setBackground(Color.yellow);
      labMsgR.setText("- Debe colocar Cliente -");
    }

  }

  public boolean validaReferencia() {
    int can = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      String sql = "SELECT count(*) can from recibocobroP "
              + "where bcr='" + bcr + "'"
              + "  and ref='" + ref + "'";
      System.out.println("-sql=" + sql);
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        can = rs.getInt("can");
      }
    } catch (SQLException ex) {
      Logger.getLogger(Registro_PagosClientes.class
              .getName()).log(Level.SEVERE, null, ex);
    }
    if (ref.length() == 0 || bcr.length() == 0) {
      can = 0;
    }
    if (can == 0) {
      return true;
    } else {
      return false;
    }
  }

  // Valida Tasa Iva
  public boolean isvalidTasaPago(int ind) {
    tas = 0;
    String mox = txtTap.getText().trim();
    if (mox.length() > 0 && !mox.equals("0")) {
      if (ind == 1) {
        mox = mox.replace(".", "");
        mox = mox.replace(",", ".");
      }
      if (isNumeric(mox)) {
        mox = GetCurrencyDouble(mox);
        tas = GetMtoDouble(mox);
        txtTap.setText(MtoEs(tas, 2));
      }
    }
    if (tas > 0) {
      return true;
    } else {
      labMsgR.setText("- Tasa Invalida");
      txtTap.setSelectionStart(0);
      txtTap.setSelectionEnd(txtTap.getText().length());
      txtTap.requestFocus();
      return false;
    }
  }

  private void txtTapKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTapKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTapKeyReleased

  private void jLabCuaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCuaMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCuaMouseClicked

  private void jLabCte16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCte16MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCte16MouseClicked

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

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    RegCob.dispose();
  }//GEN-LAST:event_jButton1ActionPerformed

  private void jLabCte8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCte8MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCte8MouseClicked

  private void cbNEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNEActionPerformed
    if (cbNE.isSelected() == true) {
      tpd = 0; //Not Ent
      cbNC.setSelected(false);
    } else {
      tpd = 1; //Not Cred
      cbNC.setSelected(true);
    }
    txtNot.requestFocus();
  }//GEN-LAST:event_cbNEActionPerformed

  private void jLabCte2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCte2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCte2MouseClicked

  private void cbNCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNCActionPerformed
    if (cbNC.isSelected() == true) {
      tpd = 1; //Not Cred
      cbNE.setSelected(false);
    } else {
      tpd = 0; //Not Entrega
      cbNE.setSelected(true);
    }
    txtNot.requestFocus();
  }//GEN-LAST:event_cbNCActionPerformed

  private void btnEliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliActionPerformed
    eliminaRegistro();
  }//GEN-LAST:event_btnEliActionPerformed

  public void eliminaRegistro() {
    ImageIcon icon = new ImageIcon(getClass().getResource("/img/ojo.png"));
    String vax = "Desea Eliminar Completo\n\nEl Recibo de Cobro - " + nrc + " ?\n\nVerifique antes de Eliminarlo\n";
    String[] options = {"SI", "NO"};
    int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
    if (opcion == 0) {
      try {
        ConexionSQL bdsql = new ConexionSQL();
        Connection con = bdsql.Conectar();
        Statement st = con.createStatement();
        // Elimina Header (recibocobroH)
        String sql = "delete from recibocobroH "
                + "where nrc =  " + nrc + " ";
        st.execute(sql);

        // Elimina Detalle Documentos (recibocobroD)
        sql = "delete from recibocobroD "
                + "where nrc =  " + nrc + " ";
        st.execute(sql);

        // Elimina Detalle Pagoss (recibocobroP)
        sql = "delete from recibocobroP "
                + "where nrc =  " + nrc + " ";
        st.execute(sql);

        iniciaRecCob();

        labMsgR.setText("- Se elimino Nota  " + nrc + " -");
        st.close();
        con.commit();
        con.close();

      } catch (SQLException ex) {
        Logger.getLogger(Registro_PagosClientes.class
                .getName()).log(Level.SEVERE, null, ex);
      }

    }
  }

  private void txtCteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCteMouseClicked
    if (inc == 0 && nrc > 0) {
      filtraCliente();
      cbCte.setSelectedIndex(-1);
      txtCte.requestFocus();
      bloqueaCampos();
      txtCte.setEnabled(true);
      cbCte.setEnabled(true);
      txtCte.setText(" ");
      txtCte.requestFocus();
    }
  }//GEN-LAST:event_txtCteMouseClicked

  private void txtCteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCteMouseEntered

  }//GEN-LAST:event_txtCteMouseEntered

  private void txtCteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCteActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCteActionPerformed

  private void txtCteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCteKeyReleased
    if (nrc > 0) {
      filtraCliente();
    }
    //cbCte.firePopupMenuWillBecomeVisible();
  }//GEN-LAST:event_txtCteKeyReleased

  public void filtraCliente() {
    fil = txtCte.getText().toUpperCase().trim();
    cargaClientes();
  }

  private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
    if (nrc > 0) {
      cbCte.setPopupVisible(true);
    }
  }//GEN-LAST:event_jLabel6MouseClicked

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
            labMsgR.setText("- Ingrese Nota de Entrega");
            txtCte.setText(" ");
            cbNE.setEnabled(true);
            cbNC.setEnabled(true);
            cbNE.setSelected(true);
            cbNC.setSelected(false);
            txtNot.setEnabled(true);
            if (!existeRecCob()) {
              getSaldosRec();
            }
            txtNot.requestFocus();
          }
        }
      }
    } else {
      txtCte.requestFocus();
    }
  }//GEN-LAST:event_cbCteItemStateChanged

  // Verifica si existe recibo cobro
  public boolean existeRecCob() {

    int can = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT count(*) can "
              + "FROM recibocobroH "
              + "where nrc =  " + nrc + " ";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        can = rs.getInt("can");
      }
      st.close();
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_PedidoCliente.class
              .getName()).log(Level.SEVERE, null, ex);
    }
    if (can == 0) {
      return false;
    } else {
      return true;
    }
  }

  private void cbCteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbCteMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_cbCteMouseClicked

  private void cbCteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbCteMouseEntered
    //cbCte.setPopupVisible(true);
  }//GEN-LAST:event_cbCteMouseEntered

  private void cbCteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCteActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbCteActionPerformed

  private void jLabNoe1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabNoe1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabNoe1MouseClicked

  private void jLabCte3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCte3MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCte3MouseClicked

  public void bloqueaPagos() {

    tip = "0"; // Pago
    nop = 0;
    tas = 0;
    tpa = 0;
    tp$ = 0;

    fep = "";
    bce = "";
    bcr = "";
    ref = "";

    txtTop.setText("");
    txtTap.setText("");
    txtTpd.setText("");
    txtRef.setText("");

    // jFPag.setEnabled(false);
    txtDia.setEnabled(false);
    txtMes.setEnabled(false);
    txtAno.setEnabled(false);

    txtTop.setEnabled(false);
    txtTap.setEnabled(false);
    txtTpd.setEnabled(false);
    cbBce.setEnabled(false);
    cbBcr.setEnabled(false);
    txtRef.setEnabled(false);
    btnGrp.setEnabled(false);
    cbBce.setSelectedIndex(-1);
    cbBcr.setSelectedIndex(-1);
    labRutPdfCob.setOpaque(false);
    txtDia.setBorder(new LineBorder(Color.GRAY));
    tabPag.getSelectionModel().clearSelection();  // Quitar Seleccion
  }

  private void btnPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfActionPerformed
    labRutPdfCob.setBackground(new java.awt.Color(242, 247, 247));
    if (nrc > 0) {
      icon = new ImageIcon(getClass().getResource("/img/ok.png"));
      String vax = "Desea Generar Recibo de Cobro ?";
      String[] options = {"SI", "NO"};
      int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
      if (opcion == 0) {
        updateFecHeader(0);
        new Pdf_ReciboCobro(nrc, 0);
      }
    } else {
      labMsgR.setText("- Debe Colocar no. Recibo");
    }
  }//GEN-LAST:event_btnPdfActionPerformed

  private void btnBcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBcoActionPerformed
    //RegistroCobrosClientes regBco
    if (regBco != null) {
      regBco.dispose();
    }
    regBco = new Registro_Banco();
    regBco.setEnabled(true);
    regBco.setExtendedState(NORMAL);
    regBco.setVisible(true);
  }//GEN-LAST:event_btnBcoActionPerformed

  private void btnGrpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrpActionPerformed

    grabaPago();
  }//GEN-LAST:event_btnGrpActionPerformed

  private void btnEldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEldActionPerformed
    // Elimina Documento
    if (tabDoc.getRowCount() > 0) {
      if (tabDoc.getSelectedRow() >= 0) {
        eliminaDoc();
      }
    }

    // Elimina Pago
    if (tabPag.getRowCount() > 0) {
      if (tabPag.getSelectedRow() >= 0) {
        eliminaPag();
      }
    }


  }//GEN-LAST:event_btnEldActionPerformed

  private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
    tabPag.getSelectionModel().clearSelection();  // Quitar Seleccion
  }//GEN-LAST:event_jPanel1MouseClicked

  private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
    tabDoc.getSelectionModel().clearSelection();  // Quitar Seleccion
  }//GEN-LAST:event_jPanel2MouseClicked

  private void jLabCte21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCte21MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCte21MouseClicked

  private void cbTipItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTipItemStateChanged
    //0=Pago 1=Abono 2=Pago Ret Iva, 3=Saldo a Favor 4=Saldo Deuda
    bloqueaPagos();

    tip = "0"; // Pago
    int idx = cbTip.getSelectedIndex();
    if (idx >= 0 && evt.getSource() == cbTip && evt.getStateChange() == 1) {
      String sel = (String) cbTip.getSelectedItem();  //valor item seleccionado
      if (sel != null) {
        tip = "0";
        cbNe.setEnabled(true);
        txtTop.setEnabled(false);
        if (sel.length() > 0) {
          sel = sel.trim();
          if (sel.contains("Abono")) {
            tip = "1";
          }
          if (sel.contains("Pago Ret Iva")) {
            tip = "2";
          }
          if (sel.contains("Saldo a Favor $")) {
            tip = "3";
            txtRef.setEnabled(true);
          }
          if (sel.contains("Dev Ret Iva")) {
            tip = "4";
            txtRef.setEnabled(true);
            txtTop.setEnabled(true);
            /*
            txtTop.setText(MtoEs(tsB, 2));
            String mox = txtTop.getText().trim();
            if (mox.length() > 0) {
              mox = GetCurrencyDouble(mox);
              txtTop.setText(mox + "  ");
            }
            txtTop.setSelectionStart(0);
            txtTop.setSelectionEnd(txtTop.getText().length());
            fep = txtAno.getText() + txtMes.getText() + txtDia.getText();
            txtTop.requestFocus();
             */
          }
          if (sel.contains("Ajuste Saldo")) {
            tip = "5";
            txtRef.setEnabled(true);
            txtTop.setEnabled(true);
            txtTop.setText("0");
            txtTop.setSelectionStart(0);
            txtTop.setSelectionEnd(txtTop.getText().length());
            fep = txtAno.getText() + txtMes.getText() + txtDia.getText();
            txtTop.requestFocus();
          }
          txtDia.setEnabled(true);
          txtMes.setEnabled(true);
          txtAno.setEnabled(true);

          if (nop > 0) {
            cbNe.setEnabled(true);
          }
          if (tas > 0) {
            txtTap.setEnabled(true);
          }
          if (fep.length() > 0) {

            txtTop.setEnabled(true);
            txtTop.requestFocus();
          } else {

            cargaNoEFac(tip);
            txtDia.setSelectionStart(0);
            txtDia.setSelectionEnd(txtDia.getText().length());
            txtMes.setSelectionStart(0);
            txtMes.setSelectionEnd(txtMes.getText().length());
            txtAno.setSelectionStart(0);
            txtAno.setSelectionEnd(txtAno.getText().length());

            txtDia.requestFocus();

          }
        }
      }
    } else {
      txtCte.requestFocus();
    }
  }//GEN-LAST:event_cbTipItemStateChanged

  private void cbTipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbTipActionPerformed

  private void jLabSdBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabSdBMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabSdBMouseClicked

  private void labRutPdfCobMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labRutPdfCobMouseClicked
    labRutPdfCob.setOpaque(false);
    labRutPdfCob.setBackground(new java.awt.Color(242, 247, 247));
    String vax = "rep\\pdf\\recibos\\";
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
  }//GEN-LAST:event_labRutPdfCobMouseClicked

  private void jLabSiDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabSiDMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabSiDMouseClicked

  private void jLabSiBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabSiBMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabSiBMouseClicked

  private void txtNrcKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNrcKeyReleased
    // isvalidRecCob();
  }//GEN-LAST:event_txtNrcKeyReleased

  private void txtDiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiaMouseClicked
    cbNe.setSelectedIndex(-1);
    txtDia.setSelectionStart(0);
    txtDia.setSelectionEnd(txtDia.getText().length());
  }//GEN-LAST:event_txtDiaMouseClicked

  private void txtDiaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiaMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDiaMouseReleased

  private void txtDiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiaActionPerformed
    validaFecPag();
  }//GEN-LAST:event_txtDiaActionPerformed

  public void validaFecPag() {
    txtDia.setBorder(new LineBorder(Color.GRAY));
    String vax = txtDia.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtDia.setText(vax);
    }
    vax = txtMes.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtMes.setText(vax);
    }
    fep = txtAno.getText() + txtMes.getText() + txtDia.getText();

    if (fep.length() == 8) {
      if (isvalidFec(fep, 0)) {
        labMsgR.setText("- Seleccione La Nota a aplicar");
        cbNe.setEnabled(true);
        cbNe.requestFocus();
        //cbNe.setPopupVisible(true);
      } else {
        labMsgR.setText("- Fecha " + ymd_dmy(fep) + "  Invalida");
        seteaFecPago();
        txtDia.requestFocus();
      }
    }
  }

  private void txtDiaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiaKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDiaKeyReleased

  private void txtMesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMesMouseClicked
    txtMes.setSelectionStart(0);
    txtMes.setSelectionEnd(txtMes.getText().length());
  }//GEN-LAST:event_txtMesMouseClicked

  private void txtMesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMesMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtMesMouseReleased

  private void txtMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMesActionPerformed
    validaFecPag();
  }//GEN-LAST:event_txtMesActionPerformed

  private void txtMesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMesKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtMesKeyReleased

  private void txtAnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnoMouseClicked
    txtAno.setSelectionStart(0);
    txtAno.setSelectionEnd(txtAno.getText().length());
  }//GEN-LAST:event_txtAnoMouseClicked

  private void txtAnoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnoMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtAnoMouseReleased

  private void txtAnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAnoActionPerformed
    validaFecPag();
  }//GEN-LAST:event_txtAnoActionPerformed

  private void txtAnoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnoKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtAnoKeyReleased

  private void labComMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labComMouseClicked
    if (conPag != null) {
      conPag.dispose();
    }
    conPag = new Consulta_PagosClientes();
    conPag.setEnabled(true);
    conPag.setExtendedState(NORMAL);
    conPag.setVisible(true);
  }//GEN-LAST:event_labComMouseClicked

  private void labTsd$MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labTsd$MouseEntered
    // TODO add your handling code here:
  }//GEN-LAST:event_labTsd$MouseEntered

  private void txtObsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtObsMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtObsMouseClicked

  private void txtObsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtObsMouseEntered
    // TODO add your handling code here:
  }//GEN-LAST:event_txtObsMouseEntered

  private void txtObsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtObsActionPerformed
    updateFecHeader(0);
  }//GEN-LAST:event_txtObsActionPerformed

  private void txtObsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObsKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtObsKeyReleased

  private void jLabNotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabNotMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabNotMouseClicked

  private void jLabSd$1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabSd$1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabSd$1MouseClicked

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
      java.util.logging.Logger.getLogger(Registro_PagosClientes.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Registro_PagosClientes.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Registro_PagosClientes.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Registro_PagosClientes.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registro_PagosClientes().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Jfecd;
  private javax.swing.JButton btnBco;
  private javax.swing.JButton btnCal;
  private javax.swing.JButton btnEld;
  private javax.swing.JButton btnEli;
  private javax.swing.JButton btnGrp;
  private javax.swing.JButton btnPdf;
  private javax.swing.ButtonGroup buttonGroup1;
  private javax.swing.ButtonGroup buttonGroup2;
  public static javax.swing.JComboBox cbBce;
  public static javax.swing.JComboBox cbBcr;
  private javax.swing.JComboBox cbCte;
  private javax.swing.JCheckBox cbNC;
  private javax.swing.JCheckBox cbNE;
  private javax.swing.JComboBox cbNe;
  private javax.swing.JComboBox cbTip;
  private elaprendiz.gui.varios.ClockDigital clockDigital1;
  private javax.swing.JButton jButton1;
  private com.toedter.calendar.JDateChooser jFer;
  private javax.swing.JLabel jLabCte;
  private javax.swing.JLabel jLabCte1;
  private javax.swing.JLabel jLabCte10;
  private javax.swing.JLabel jLabCte11;
  private javax.swing.JLabel jLabCte16;
  private javax.swing.JLabel jLabCte2;
  private javax.swing.JLabel jLabCte21;
  private javax.swing.JLabel jLabCte3;
  private javax.swing.JLabel jLabCte6;
  private javax.swing.JLabel jLabCte7;
  private javax.swing.JLabel jLabCte8;
  private javax.swing.JLabel jLabCte9;
  private javax.swing.JLabel jLabCua;
  private javax.swing.JLabel jLabNoe;
  private javax.swing.JLabel jLabNoe1;
  private javax.swing.JLabel jLabNoe2;
  private javax.swing.JLabel jLabNot;
  private javax.swing.JLabel jLabSd$1;
  private javax.swing.JLabel jLabSdB;
  private javax.swing.JLabel jLabSiB;
  private javax.swing.JLabel jLabSiD;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JLabel labBusNen;
  private javax.swing.JLabel labBusNpg;
  private javax.swing.JLabel labCom;
  private javax.swing.JLabel labFer;
  public static javax.swing.JLabel labMsgR;
  public static javax.swing.JLabel labRutPdfCob;
  private javax.swing.JLabel labSi$;
  private javax.swing.JLabel labSib;
  private javax.swing.JLabel labTsd$;
  private javax.swing.JLabel labTsdBs;
  private javax.swing.JLabel labojo;
  public javax.swing.JTable tabDoc;
  public javax.swing.JTable tabPag;
  private javax.swing.JTextField txtAno;
  private javax.swing.JTextField txtCte;
  private javax.swing.JTextField txtDia;
  private javax.swing.JTextField txtMes;
  private javax.swing.JTextField txtNot;
  private javax.swing.JTextField txtNrc;
  private javax.swing.JTextField txtObs;
  private javax.swing.JTextField txtRef;
  private javax.swing.JTextField txtTap;
  private javax.swing.JTextField txtTop;
  private javax.swing.JTextField txtTpd;
  // End of variables declaration//GEN-END:variables
}
