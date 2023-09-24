package gestionFZ;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import comun.Calculadora;
import comun.LaVieja;
import static comun.MetodosComunes.FileExist;
import static comun.MetodosComunes.FileSave;
import static comun.MetodosComunes.diasem;
import static comun.MetodosComunes.dmy_ymd;
import static comun.MetodosComunes.dmyhoy;
import static comun.MetodosComunes.isNumeric;
import static comun.MetodosComunes.version;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymdhoyhhmm;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;
import modelo.ConexionSQL;
import modelo.Importadora;
import modelo.Parametros;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Menu extends javax.swing.JFrame {

  public static int tamimg;
  public static int proimg;
  public static String selunm;

  public String fecact = "";
  public String fre = "";   // Fecha registro
  public String linerr = "";

  public String usr = System.getProperty("user.name").toLowerCase();
  public static String pdfmay = "N";
  public static String pdfdet = "N";

  public static String[] VecPar;

  public static Calculadora Calc;      // Calculadora
  public LaVieja Vieja;         // Juego La Vieja 

  public static Registro_Importadora RegImp;      // Registro Importadora
  public static Registro_Vendedor RegVen;         // Registro Vendedor
  public static Registro_Clientes RegCte;         // Registro Cliente
  public static Registro_CatalogoProductos RegLis;// Gestion Produstos
  public static Registro_PedidoCliente RegPed;    // Registro Pedidos
  public static Registro_NotaEntrega RegNoe;      // Registro Nota Entrega
  public static Registro_NotaCredito RegNcr;      // Registro Nota Credito
  public static Registro_PagosClientes RegCob;    // Registro Nota Credito
  public static Registro_Copa RegCop;             // Registro Copa
  public static Registro_Parametros RegPar;       // Registro Parametros
  public static Consulta_EstadoCuentaR ResSdo;    // Saldo Cartera Clientes
  public static Registra_Existencias RegStk;      // Registra Existencias
  public static Consulta_PedVsNotR PedNot;        // Pedidos vs Notas

  //Iterator cells;
  XSSFRow row;
  XSSFCell cell;

  private ImageIcon icon;

  public Menu(String Usr) {
    usr = Usr;
    initComponents();

    setResizable(false);
    setLocationRelativeTo(null);
    UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("ARIAL", java.awt.Font.BOLD, 16)));
    UIManager UI = new UIManager();
    UI.put("OptionPane.messageFont", new Font("verdana", java.awt.Font.BOLD, 20));
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/logosysven.jpg")));
    Jfecd.setText(diasem() + "    " + dmyhoy());
    setTitle(usr);

    labVer.setText(version());
    btnCop.setVisible(false);
    jPass.setVisible(false);
    jLabCon.setText("");

    //crear |tablas
    String bd = "BD";
    File fil = new File(bd);
    if (!fil.exists()) {
      System.out.println("***creando tablas**");
      crearTablas();
    } else {
      System.out.println("BD Creada=" + fil);
    }
    //crearTablas();
    crearCarpetas();
    pdfsCargados();

    System.out.println("proimg=" + proimg + " tamimg=" + tamimg + ",selunm =" + selunm);
    System.out.println("pdfmay=" + pdfmay + ", pdfdet=" + pdfdet);

    Importadora i = new Importadora();
    fecact = i.getFecLis();
    if (fecact.length() == 8) {
      labFec.setText("Fecha Lista Precios  " + ymd_dmy(fecact));
    }
    int can = getCanImgPrd();
    can = getCanRepPdf();
    can = getCanRepExc();
    labExc.setText("" + can);
    can = getCanPdfCga();
    labPdf0.setText("" + can);
    btnSalir.requestFocus();
    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        btnSalir.doClick();
      }
    });
    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        btnSalir.doClick();
      }
    });
  }

  public void crearCarpetas() {
    // Ruta Parametros
    String vax = "par\\";
    File folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

    // Ruta Tablas Pdf
    vax = "pdf\\";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }
    // Ruta Tablas Pdf
    vax = "pdf/anterior";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

    // Ruta Tablas Referencia
    vax = "ref\\";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }
    // Ruta Excel
    vax = "exc\\";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }
    // Ruta Reportes
    vax = "rep\\";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

    // Ruta Reportes Pdf
    vax = "rep/pdf";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

    // Ruta Reportes Pdf
    vax = "rep/pdf/anterior";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

    vax = "rep/pdf/pedidos";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

    vax = "rep/pdf/recibos";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

    vax = "rep/pdf/edocta";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }
    // Ruta Reportes Excel
    vax = "rep/exc";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

    // Ruta Reportes Excel
    vax = "rep/exc/anterior";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

    // Ruta imagenes dep
    vax = "imgcia\\";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

    // Ruta imagenes dep
    vax = "imgdep\\";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

    // Ruta imagenes productos
    vax = "imgprd\\";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

    // Ruta imagenes productos
    vax = "imgprd/inactivos";
    folder = new File(vax);
    if (!FileExist(vax)) {
      folder.mkdir();
    }

  }

  //Creacion Tablas BD (SQL Derby)
  private void crearTablas() {

    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();

      int x = 0;
      if (x == 1) {

        // Promocion Producto
        String sql = "DROP TABLE PromCombo";
        st.execute(sql);
        sql = "CREATE TABLE PromCombo ("
                + "cop VARCHAR(10)," // codigo producto
                + "grp VARCHAR(10)," // Grupo
                + "por double,     " // % descuento 
                + "can int,        " // Cantidad Unds
                + "PRIMARY KEY (cop))";
        st.execute(sql);

        // Recibo Cobro (Header)
        sql = "DROP TABLE recibocobroH";
        st.execute(sql);
        sql = "CREATE TABLE recibocobroH ("
                + "nrc int,          " // nro recibo cobro
                + "frc VARCHAR(08),  " // fecha recibo cobro
                + "coc VARCHAR(12),  " // codigo cliente
                + "noc VARCHAR(50),  " // nombre Cliente
                + "tod decimal(15,2)," // total notas
                + "tpd decimal(15,2)," // total pagos $
                + "tpb decimal(15,2)," // total pagos Bs Ret iva
                + "sta VARCHAR(01),  " // status 0=pendiente 1= cerrado
                + "fre VARCHAR(12),  " // fecha registro  
                + "PRIMARY KEY (nrc)"
                + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxrcH0 ON recibocobroH (nrc)");
        st.execute("CREATE INDEX idxrcH1 ON recibocobroH (coc,nrc)");
        st.execute("CREATE INDEX idxrcH2 ON recibocobroH (frc,noc)");

        // Recibo Cobro (Detalle Documentos)
        sql = "DROP TABLE recibocobroD";
        st.execute(sql);
        sql = "CREATE TABLE recibocobroD ("
                + "nrc int,          " // nro recibo cobro
                + "coc VARCHAR(12),  " // codigo cliente
                + "tpd int,          " // tipo docum 0=not / ent 1=not cred 2=not deb
                + "nno int,          " // numero nota  
                + "nfa int,          " // numero factura
                + "fno VARCHAR(08),  " // fecha nota 
                + "fen VARCHAR(08),  " // fecha entrega
                + "fev VARCHAR(08),  " // fecha vence
                + "tdo decimal(15,2)," // tota documento $
                + "tdn decimal(15,2)," // total descuento nota
                + "toi decimal(15,2)," // total iva Bs
                + "trd decimal(15,2)," // total retencion 25% $
                + "fpa VARCHAR(08),  " // fecha pago 
                + "tpa decimal(15,2)," // total pago
                + "fre VARCHAR(12),  " // fecha registro  
                + "PRIMARY KEY (nrc,tpd,nno)"
                + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxrcD0 ON recibocobroD (coc,nrc,tpd,nno)");
        st.execute("CREATE INDEX idxrcD1 ON recibocobroD (nno)");
        st.execute("CREATE INDEX idxrcD2 ON recibocobroD (fno)");

        // Recibo Cobro (Detalle Pagos)
        sql = "DROP TABLE recibocobroP";
        st.execute(sql);
        sql = "CREATE TABLE recibocobroP ("
                + "nrc int,          " // nro recibo cobro
                + "coc VARCHAR(12),  " // codigo cliente
                + "fep VARCHAR(08),  " // fecha pago 
                + "tip VARCHAR(01),  " // tipo pago 0=abono,1=pago, 2=pago Ret Iva, 3=Saldo a favor, 4 Reintegro
                + "bce VARCHAR(20),  " // Banco emisor 
                + "bcr VARCHAR(20),  " // Banco receptor
                + "ref VARCHAR(20),  " // referencia pago
                + "tpa decimal(15,2)," // total pago
                + "tas decimal(15,2)," // Tasa cambio Bs
                + "tpd decimal(15,2)," // Total pago $
                + "nno int,          " // numero nota afectada
                + "fre VARCHAR(12))  "; // Fecha registro
        st.execute(sql);
        st.execute("CREATE INDEX idxrcP0 ON recibocobroP (nrc,fep,tip)");
        st.execute("CREATE INDEX idxrcP1 ON recibocobroP (nno)");
        st.execute("CREATE INDEX idxrcP2 ON recibocobroP (coc,fep,nrc,tip,nno)");

        // Nota de Cred/Devolucion
        sql = "DROP TABLE notacred";
        st.execute(sql);
        sql = "CREATE TABLE notacred ("
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
                + "PRIMARY KEY (coc,ncr,nne)"
                + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxnc0 ON notacred (nne)");

        // Tasa Cambio 
        sql = "DROP TABLE TasaCambio";
        st.execute(sql);
        sql = "CREATE TABLE TasaCambio ("
                + "tas VARCHAR(03)," // Divisa USD
                + "fee VARCHAR(08)," // Fecha dia
                + "mon double     ," // Tasa Cambio
                + "PRIMARY KEY (tas,fee)"
                + ")";
        st.execute(sql);

        sql = "DROP TABLE pedidoH";
        st.execute(sql);
        // Pedidos Header
        // pedidoH (npe,coc,tip,fac,fep,fel,obs,por)
        sql = "CREATE TABLE pedidoH ("
                + "npe int,          " // nro pedido cliente
                + "coc VARCHAR(12),  " // codigo cliente
                + "noc VARCHAR(50),  " // nombre Cliente
                + "tip VARCHAR(01),  " // tipo 0=Mayorista 1=Detal
                + "fac VARCHAR(01),  " // Con Factura S/N
                + "fep VARCHAR(08),  " // fecha pedido
                + "fel VARCHAR(08),  " // fecha lista precios
                + "obs VARCHAR(160), " // Observaciones
                + "por double , " // % descuento ppago
                + "iva double , " // % Iva
                + "pre double , " // % retencion Iva
                + "fre VARCHAR(12),  " // fecha registro  
                + "ppm double , " // % Promocion global
                + "PRIMARY KEY (npe)"
                + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxpd0 ON pedidoH (noc,npe)");
        st.execute("CREATE INDEX idxpd1 ON pedidoH (fep,noc)");

        sql = "DROP TABLE pedidoD";
        st.execute(sql);
        // Pedidos Detail
        sql = "CREATE TABLE pedidoD ("
                + "npe int,          " // nro pedido cliente
                + "cop VARCHAR(10),  " // codigo producto
                + "dep VARCHAR(60),  " // descripcion producto
                + "unm VARCHAR(10),  " // unidad Medida venta
                + "can double , " // cantidad producto
                + "prm double , " // precio Mayor
                + "prd double        " // precio Detal
                + "por double        " // % promocion
                + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxP0 ON pedidoD (npe)");

        // Nota de Entrega
        sql = "DROP TABLE notaent";
        st.execute(sql);
        sql = "CREATE TABLE notaent ("
                + "nne int,          " // nro not/ent
                + "nfa int,          " // nro factura
                + "npe int,          " // nro pedido
                + "coc VARCHAR(12),  " // codigo cliente
                + "noc VARCHAR(50),  " // nombre Cliente
                + "fne VARCHAR(08),  " // fecha not/ent
                + "fep VARCHAR(08),  " // fecha pedido
                + "fee VARCHAR(08),  " // fecha entrega a cliente
                + "fev VARCHAR(08),  " // fecha vence
                + "tpe double , " // total pedido
                + "tne double , " // total nota entrega
                + "tdn double , " // total descuento nota
                + "tfa double , " // total Base factura
                + "tdf double , " // total descuento factura
                + "toi double , " // total iva
                + "iva double , " // % Iva
                + "pre double , " // % retencion
                + "tor double , " // total retencion
                + "obs VARCHAR(120), " // Observaciones
                + "fre VARCHAR(12),  " // fecha registro  
                + "PRIMARY KEY (nne,npe)"
                + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxn1 ON notaent (npe)");
        st.execute("CREATE INDEX idxn2 ON notaent (noc,nne)");
        st.execute("CREATE INDEX idxn3 ON notaent (fne)");
        st.execute("CREATE INDEX idxn4 ON notaent (nfa)");

        // Condicion Pago
        sql = "DROP TABLE Copa";
        st.execute(sql);
        sql = "CREATE TABLE Copa ("
                + "cop VARCHAR(01)," // 0=Prepago 1=credito 30 dias, 2 credito 15 dias
                + "nom VARCHAR(20)," // descripcion
                + "ndi VARCHAR(03)," // dias credito
                + "PRIMARY KEY (cop))";
        st.execute(sql);

        //--------------------------- aqui -------------------------
        // Departamentos
        sql = "DROP TABLE Departamento";
        st.execute(sql);
        sql = "CREATE TABLE Departamento ("
                + "dep VARCHAR(50)," // Departamento
                + "nom VARCHAR(50)," // Nombre Nuevo Departamento
                + "ob1 VARCHAR(80)," // observacion 1
                + "ob2 VARCHAR(80))"; // observacion 2
        st.execute(sql);
        st.execute("CREATE INDEX idxdpD1 ON Departamento (dep)");

        // Lista Precios
        sql = "DROP TABLE listaprecios";
        st.execute(sql);
        sql = "CREATE TABLE listaprecios ("
                + "dep VARCHAR(50),  " // Departamento
                + "cop VARCHAR(10),  " // codigo producto
                + "cof VARCHAR(10),  " // codigo producto Busqueda
                + "nom VARCHAR(60),  " // nombre producto
                + "pum double , " // precio unitario Mayor
                + "pud double , " // precio unitario Detal
                + "stk double , " // stock existencia
                + "man VARCHAR(01),  " // art manual 0=lista, 1=manual
                + "pag VARCHAR(01),  " // Status  0=no 1=salta pagina
                + "sta VARCHAR(01),  " // Status  0=activo  1=inactivo
                + "fre VARCHAR(12),  " // fecha registro  
                + "PRIMARY KEY (dep,cop))";
        st.execute(sql);
        st.execute("CREATE INDEX idxL0 ON listaprecios (cop)");
        st.execute("CREATE INDEX idxL1 ON listaprecios (cof)");
        st.execute("CREATE INDEX idxL2 ON listaprecios (nom)");

        // Lista PreciosT
        sql = "DROP TABLE listapreciosT";
        st.execute(sql);
        sql = "CREATE TABLE listapreciosT ("
                + "fel VARCHAR(08),  " // fecha lista
                + "dep VARCHAR(50),  " // Departamento
                + "cop VARCHAR(10),  " // codigo producto
                + "cof VARCHAR(10),  " // codigo producto Busqueda
                + "nom VARCHAR(60),  " // nombre producto
                + "pum double , " // precio unitario Mayor
                + "pud double , " // precio unitario Detal
                + "stk double , " // stock existencia
                + "man VARCHAR(01),  " // art manual 0=lista, 1=manual
                + "pag VARCHAR(01),  " // Status  0=no 1=salta pagina
                + "sta VARCHAR(01),  " // Status  0=activo  1=inactivo
                + "fre VARCHAR(12),  " // fecha registro  
                + "PRIMARY KEY (fel,dep,cop))";
        st.execute(sql);

        // ----------------aqui---------------------------------------
        sql = "CREATE TABLE unidmed ("
                + "unm VARCHAR(10),  " // unidad medida
                + "sec int,          " // secuencia
                + "PRIMARY KEY (unm))";
        st.execute(sql);

        // Tablas Temporales
        // Departamentos
        sql = "DROP TABLE DepartamentoT";
        st.execute(sql);
        sql = "CREATE TABLE DepartamentoT ("
                + "fel VARCHAR(08),  " // fecha lista
                + "dep VARCHAR(50),  " // Departamento
                + "nom VARCHAR(50),  " // Nombre Nuevo Departamento
                + "ob1 VARCHAR(80),  " // observacion 1
                + "ob2 VARCHAR(80),  " // observacion 2
                + "PRIMARY KEY (fel,dep))";
        st.execute(sql);

        sql = "CREATE TABLE empaquepu ("
                + "cop VARCHAR(10),  " // codigo
                + "ref VARCHAR(20),  " // referencia p.U. x Empaque
                + "PRIMARY KEY (cop))";
        st.execute(sql);

        sql = "DROP TABLE empaqueproducto";
        st.execute(sql);
        // Empaque productos
        sql = "CREATE TABLE empaqueproducto ("
                + "cop VARCHAR(10),  " // codigo
                + "tx1 VARCHAR(40),  " // capacidad
                + "ref VARCHAR(20),  " // referencia p.U. x Empaque
                + "unm VARCHAR(10),  " // unidad Medida venta
                + "can double , " // cantidad empaque
                + "por double , " // % comision
                + "PRIMARY KEY (cop))";
        st.execute(sql);
        st.execute("CREATE INDEX idx3 ON empaqueproducto (tx1)");

        // Importadora
        sql = "DROP TABLE importadora";
        st.execute(sql);

        sql = "CREATE TABLE importadora ("
                + "rif VARCHAR(12),  " // rif importadora
                + "nom VARCHAR(50),  " // nombre importadora
                + "dir VARCHAR(160), " // Direccion
                + "tlf VARCHAR(40),  " // Telefono
                + "eml VARCHAR(50),  " // Email
                + "fec VARCHAR(10),  " // fecha lista dd/mm/yyyyy
                + "pod double ,      " // % descuento ppago con factura
                + "iva double ,      " // % Iva Actual
                + "dcr int,          " // Dias Credito
                + "np1 int,          " // pedido desde
                + "np2 int,          " // pedido hasta
                + "nr1 int,          " // recibo desde
                + "nr2 int,          " // recibo hasta
                + "ne1 int,          " // not ent desde
                + "ne2 int,          " // not ent hasta
                + "nc1 int,          " // nc desde
                + "nc2 int,          " // nc hasta
                + "PRIMARY KEY (rif) "
                + ")";
        st.execute(sql);

        // Clientes
        sql = "DROP TABLE clientes";
        st.execute(sql);
        sql = "CREATE TABLE clientes ("
                + "coc VARCHAR(12),  " // codigo cliente
                + "rif VARCHAR(12),  " // rif  cliente
                + "nom VARCHAR(50),  " // nombre cliente
                + "dir VARCHAR(160), " // direccion
                + "edo VARCHAR(20),  " // Estado
                + "ciu VARCHAR(20),  " // Ciudad
                + "tlf VARCHAR(40),  " // telefono
                + "con VARCHAR(40),  " // contacto
                + "eml VARCHAR(50),  " // email
                + "tip VARCHAR(01),  " // tipo 0=Mayorista 1=Detal
                + "cop VARCHAR(01),  " // 0=Prepago 1=credito 30 dias, 2 credito 15 dias 
                + "fac VARCHAR(01),  " // 0=si 1=no (5%) Lista precios
                + "fec VARCHAR(08),  " // fecha creacion
                + "pre double     ,  " // % Retencion iva
                + "sta VARCHAR(01),  " // 0=activo, 1=inactivo
                + "PRIMARY KEY (coc) "
                + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxc ON clientes (nom)");

        sql = "DROP TABLE importadora";
        st.execute(sql);

        // Vendedor
        sql = "CREATE TABLE vendedor ("
                + "cod VARCHAR(10),  " // codigo vendedor   00018
                + "nom VARCHAR(30),  " // nombre vendedor
                + "car VARCHAR(30),  " // cargo
                + "eml VARCHAR(50),  " // Email
                + "poc double , " // % comision vendedor
                + "PRIMARY KEY (cod) "
                + ")";
        st.execute(sql);

        // Bancos
        sql = "CREATE TABLE Bancos ("
                + "bco VARCHAR(20),  " // Banco
                + "PRIMARY KEY (bco))";
        st.execute(sql);

      }

      con.close(); // cerramos la conexion

    } catch (SQLException se) {
      System.out.println("error = " + se);
    }
  }

  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        img = new javax.swing.JLabel();
        Jfecd = new javax.swing.JLabel();
        clockDigital1 = new elaprendiz.gui.varios.ClockDigital();
        jMsg = new javax.swing.JLabel();
        labFec = new javax.swing.JLabel();
        jLabCia = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        btnCop = new javax.swing.JButton();
        btnVen = new javax.swing.JButton();
        btnCte = new javax.swing.JButton();
        btnCar = new javax.swing.JButton();
        btnCge = new javax.swing.JButton();
        btnCpm = new javax.swing.JButton();
        btnPed = new javax.swing.JButton();
        btnPrd = new javax.swing.JButton();
        btnNen = new javax.swing.JButton();
        btnSdo1 = new javax.swing.JButton();
        btnSdo = new javax.swing.JButton();
        btnNcr = new javax.swing.JButton();
        btnPag = new javax.swing.JButton();
        labPdf = new javax.swing.JLabel();
        labPdf0 = new javax.swing.JLabel();
        jrutExc = new javax.swing.JLabel();
        labExc = new javax.swing.JLabel();
        btnImp = new javax.swing.JButton();
        labVer = new javax.swing.JLabel();
        jrutDoc = new javax.swing.JLabel();
        jAct = new javax.swing.JLabel();
        jPass = new javax.swing.JPasswordField();
        jLabCon = new javax.swing.JLabel();
        jParam1 = new javax.swing.JLabel();
        btnVja = new javax.swing.JButton();
        btnCal = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        img.setBackground(new java.awt.Color(204, 204, 204));
        img.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logofz.jpg"))); // NOI18N
        img.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 51, 0)));
        getContentPane().add(img, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        Jfecd.setBackground(new java.awt.Color(248, 248, 248));
        Jfecd.setFont(new java.awt.Font("Cambria Math", 1, 12)); // NOI18N
        Jfecd.setForeground(new java.awt.Color(204, 204, 204));
        Jfecd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Jfecd.setText("Fec");
        getContentPane().add(Jfecd, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, 173, 18));

        clockDigital1.setBackground(new java.awt.Color(255, 255, 255));
        clockDigital1.setForeground(new java.awt.Color(204, 204, 204));
        clockDigital1.setFont(new java.awt.Font("Cambria Math", 1, 12)); // NOI18N
        clockDigital1.setPreferredSize(new java.awt.Dimension(120, 18));
        clockDigital1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clockDigital1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clockDigital1MouseEntered(evt);
            }
        });
        getContentPane().add(clockDigital1, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 120, 93, -1));

        jMsg.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMsg.setForeground(new java.awt.Color(255, 255, 255));
        jMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMsg.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
        jMsg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMsgMouseClicked(evt);
            }
        });
        getContentPane().add(jMsg, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 550, 380, 25));

        labFec.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labFec.setForeground(new java.awt.Color(255, 255, 255));
        labFec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labFec.setText(" ");
        labFec.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        getContentPane().add(labFec, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 210, 27));

        jLabCia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/sisven.jpg"))); // NOI18N
        jLabCia.setToolTipText("joseluisfernandezg@gmail.com    0424-140.32.28");
        jLabCia.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 102), 1, true));
        getContentPane().add(jLabCia, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, -1, -1));

        btnSalir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setToolTipText("Salir del Sistema");
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 550, -1, -1));

        btnCop.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCop.setForeground(new java.awt.Color(51, 51, 51));
        btnCop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Copa.png"))); // NOI18N
        btnCop.setText("CP");
        btnCop.setToolTipText("Actualizar Condiciones de pago");
        btnCop.setBorder(new javax.swing.border.MatteBorder(null));
        btnCop.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopActionPerformed(evt);
            }
        });
        getContentPane().add(btnCop, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 70, 30));

        btnVen.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnVen.setForeground(new java.awt.Color(51, 51, 51));
        btnVen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Vend.png"))); // NOI18N
        btnVen.setText("Registro Vendedor");
        btnVen.setToolTipText("Registro Vendedor");
        btnVen.setBorder(new javax.swing.border.MatteBorder(null));
        btnVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVenActionPerformed(evt);
            }
        });
        getContentPane().add(btnVen, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 195, 40));

        btnCte.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCte.setForeground(new java.awt.Color(51, 51, 51));
        btnCte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/usr.png"))); // NOI18N
        btnCte.setText("  Registro Clientes");
        btnCte.setToolTipText("  Registro Clientes");
        btnCte.setBorder(new javax.swing.border.MatteBorder(null));
        btnCte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCteActionPerformed(evt);
            }
        });
        getContentPane().add(btnCte, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 195, 40));

        btnCar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCar.setForeground(new java.awt.Color(51, 51, 0));
        btnCar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pdf0.png"))); // NOI18N
        btnCar.setText("  Cargar Lista Precios");
        btnCar.setToolTipText("Procesar Carga Lista de Precios Actualizada");
        btnCar.setBorder(new javax.swing.border.MatteBorder(null));
        btnCar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 150, 190, 25));

        btnCge.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCge.setForeground(new java.awt.Color(51, 51, 0));
        btnCge.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/excel.png"))); // NOI18N
        btnCge.setText("   Cargar   Existencias");
        btnCge.setToolTipText("Cargar Existencias Actualizadas  Productos");
        btnCge.setBorder(new javax.swing.border.MatteBorder(null));
        btnCge.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCgeActionPerformed(evt);
            }
        });
        getContentPane().add(btnCge, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 190, 190, 25));

        btnCpm.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCpm.setForeground(new java.awt.Color(51, 51, 0));
        btnCpm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/excel.png"))); // NOI18N
        btnCpm.setText("    Cargar Promocion");
        btnCpm.setToolTipText("Cargar Promociones de Productos");
        btnCpm.setBorder(new javax.swing.border.MatteBorder(null));
        btnCpm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCpm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCpmActionPerformed(evt);
            }
        });
        getContentPane().add(btnCpm, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 230, 190, 25));

        btnPed.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPed.setForeground(new java.awt.Color(51, 51, 51));
        btnPed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/compras.png"))); // NOI18N
        btnPed.setText(" Pedidos Clientes");
        btnPed.setToolTipText("Registro Pedidos Clientes");
        btnPed.setBorder(new javax.swing.border.MatteBorder(null));
        btnPed.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPedActionPerformed(evt);
            }
        });
        getContentPane().add(btnPed, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 280, 190, 40));

        btnPrd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrd.setForeground(new java.awt.Color(51, 51, 51));
        btnPrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/catalog2.png"))); // NOI18N
        btnPrd.setText(" Catalogo Precios");
        btnPrd.setToolTipText(" Gestion de Catalogo Precios");
        btnPrd.setBorder(new javax.swing.border.MatteBorder(null));
        btnPrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrdActionPerformed(evt);
            }
        });
        getContentPane().add(btnPrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 325, 195, 40));

        btnNen.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNen.setForeground(new java.awt.Color(51, 51, 51));
        btnNen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/notent.jpg"))); // NOI18N
        btnNen.setText(" Nota de Entrega");
        btnNen.setToolTipText("Registro Notas de Entrega Clientes");
        btnNen.setBorder(new javax.swing.border.MatteBorder(null));
        btnNen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNenActionPerformed(evt);
            }
        });
        getContentPane().add(btnNen, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 350, 190, 40));

        btnSdo1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSdo1.setForeground(new java.awt.Color(51, 51, 51));
        btnSdo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/grafic.png"))); // NOI18N
        btnSdo1.setText(" Pedidos Vs Notas");
        btnSdo1.setToolTipText("Reporte Comparativo Pedidos Vs Notas de Entrega");
        btnSdo1.setBorder(new javax.swing.border.MatteBorder(null));
        btnSdo1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSdo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSdo1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnSdo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 195, 40));

        btnSdo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSdo.setForeground(new java.awt.Color(51, 51, 51));
        btnSdo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/clientes.png"))); // NOI18N
        btnSdo.setText(" Saldos Clientes");
        btnSdo.setToolTipText("Reporte Saldos Clientes");
        btnSdo.setBorder(new javax.swing.border.MatteBorder(null));
        btnSdo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSdoActionPerformed(evt);
            }
        });
        getContentPane().add(btnSdo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 480, 195, 40));

        btnNcr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNcr.setForeground(new java.awt.Color(51, 51, 51));
        btnNcr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/dev.png"))); // NOI18N
        btnNcr.setText(" Nota de Credito");
        btnNcr.setToolTipText("Registro Notas de Credito");
        btnNcr.setBorder(new javax.swing.border.MatteBorder(null));
        btnNcr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNcr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNcrActionPerformed(evt);
            }
        });
        getContentPane().add(btnNcr, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 420, 190, 40));

        btnPag.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPag.setForeground(new java.awt.Color(51, 51, 51));
        btnPag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pagos.jpg"))); // NOI18N
        btnPag.setText("Cobros Clientes");
        btnPag.setToolTipText("Modulo de Cobranza de Cliente");
        btnPag.setBorder(new javax.swing.border.MatteBorder(null));
        btnPag.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagActionPerformed(evt);
            }
        });
        getContentPane().add(btnPag, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 480, 190, 40));

        labPdf.setBackground(new java.awt.Color(248, 246, 244));
        labPdf.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labPdf.setForeground(new java.awt.Color(51, 51, 51));
        labPdf.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rutpdf.jpg"))); // NOI18N
        labPdf.setText("Ruta Carga Listas FZ");
        labPdf.setToolTipText("Ruta Carga Lista de Precios  -  Carga Existencias  - Carga Promociones");
        labPdf.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        labPdf.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labPdf.setOpaque(true);
        labPdf.setPreferredSize(new java.awt.Dimension(87, 35));
        labPdf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labPdfMouseClicked(evt);
            }
        });
        getContentPane().add(labPdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 170, 200, 41));

        labPdf0.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labPdf0.setForeground(new java.awt.Color(51, 51, 51));
        labPdf0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(labPdf0, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 210, 30, 17));

        jrutExc.setBackground(new java.awt.Color(242, 247, 247));
        jrutExc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jrutExc.setForeground(new java.awt.Color(51, 51, 51));
        jrutExc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jrutExc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rutexc.png"))); // NOI18N
        jrutExc.setText("Ruta Reportes Excel ");
        jrutExc.setToolTipText("Ruta Reporte Excel  Lista de Precios FZ  Generado");
        jrutExc.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jrutExc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrutExc.setOpaque(true);
        jrutExc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrutExcMouseClicked(evt);
            }
        });
        getContentPane().add(jrutExc, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 250, 200, 40));

        labExc.setBackground(new java.awt.Color(102, 102, 102));
        labExc.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labExc.setForeground(new java.awt.Color(51, 51, 51));
        labExc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(labExc, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 290, 30, 17));

        btnImp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnImp.setForeground(new java.awt.Color(51, 51, 51));
        btnImp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Import.png"))); // NOI18N
        btnImp.setText("Ajustes Importadora");
        btnImp.setToolTipText("Actualizacion - Consultas");
        btnImp.setActionCommand("Ajustes Importadora FZ");
        btnImp.setBorder(new javax.swing.border.MatteBorder(null));
        btnImp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImpMouseClicked(evt);
            }
        });
        btnImp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpActionPerformed(evt);
            }
        });
        getContentPane().add(btnImp, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 390, 200, 42));

        labVer.setBackground(new java.awt.Color(51, 51, 51));
        labVer.setFont(new java.awt.Font("Cambria Math", 1, 9)); // NOI18N
        labVer.setForeground(new java.awt.Color(204, 204, 204));
        labVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/autor2.png"))); // NOI18N
        labVer.setToolTipText("joseluisfernandezg@gmail.com    Cel 424-140.32.28");
        labVer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labVer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labVerMouseClicked(evt);
            }
        });
        labVer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                labVerKeyPressed(evt);
            }
        });
        getContentPane().add(labVer, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 460, 130, -1));

        jrutDoc.setBackground(new java.awt.Color(242, 247, 247));
        jrutDoc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jrutDoc.setForeground(new java.awt.Color(51, 51, 51));
        jrutDoc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jrutDoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rut.png"))); // NOI18N
        jrutDoc.setText("  Ruta Documentos");
        jrutDoc.setToolTipText("Ruta  Documentos de Trabajo");
        jrutDoc.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jrutDoc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrutDoc.setOpaque(true);
        jrutDoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrutDocMouseClicked(evt);
            }
        });
        getContentPane().add(jrutDoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 320, 200, 40));

        jAct.setBackground(new java.awt.Color(0, 0, 51));
        jAct.setFont(new java.awt.Font("Tahoma", 3, 9)); // NOI18N
        jAct.setForeground(new java.awt.Color(204, 204, 204));
        jAct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ok2.png"))); // NOI18N
        jAct.setText("Cambios");
        jAct.setToolTipText("Actualizacion Cambios BD");
        jAct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jAct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jActMouseClicked(evt);
            }
        });
        getContentPane().add(jAct, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 500, 70, 20));

        jPass.setText("jPasswordField1");
        jPass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPassMouseClicked(evt);
            }
        });
        jPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPassActionPerformed(evt);
            }
        });
        getContentPane().add(jPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 500, 30, -1));

        jLabCon.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabCon.setForeground(new java.awt.Color(255, 255, 255));
        jLabCon.setText(" ");
        getContentPane().add(jLabCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 500, 26, 20));

        jParam1.setBackground(new java.awt.Color(255, 255, 255));
        jParam1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jParam1.setForeground(new java.awt.Color(255, 255, 255));
        jParam1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jParam1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/param.png"))); // NOI18N
        jParam1.setText(" Parametros");
        jParam1.setToolTipText("Parametros Sistema");
        jParam1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jParam1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jParam1.setPreferredSize(new java.awt.Dimension(106, 30));
        jParam1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jParam1MouseClicked(evt);
            }
        });
        getContentPane().add(jParam1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 540, -1, -1));

        btnVja.setFont(new java.awt.Font("Dialog", 1, 9)); // NOI18N
        btnVja.setForeground(new java.awt.Color(0, 102, 51));
        btnVja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lavieja.png"))); // NOI18N
        btnVja.setToolTipText("Pausas Activas ( La Vieja )");
        btnVja.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        btnVja.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVja.setFocusable(false);
        btnVja.setMaximumSize(new java.awt.Dimension(105, 25));
        btnVja.setMinimumSize(new java.awt.Dimension(105, 25));
        btnVja.setPreferredSize(new java.awt.Dimension(105, 25));
        btnVja.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVjaMouseClicked(evt);
            }
        });
        btnVja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVjaActionPerformed(evt);
            }
        });
        getContentPane().add(btnVja, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 540, 20, -1));

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
        getContentPane().add(btnCal, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 540, 20, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/FondoAzul.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 830, 580));

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void clockDigital1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clockDigital1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_clockDigital1MouseClicked

  private void clockDigital1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clockDigital1MouseEntered

  }//GEN-LAST:event_clockDigital1MouseEntered

  private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
    icon = new ImageIcon(getClass().getResource("/img/salir.png"));
    String vax = "DESEA SALIR DEL SISTEMA ?";
    String[] options = {"SI", "NO"};
    int opcion = 0;
    opcion = JOptionPane.showOptionDialog(null, vax, "** Aviso **", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
    if (opcion == 0) {
      String comando = "cmd /c del %temp% /Q";

      Process pr;
      try {
        pr = Runtime.getRuntime().exec(comando);
        System.out.println(comando);
      } catch (Exception ex) {
        System.out.println("Ha ocurrido un error al ejecutar el comando. Error: " + ex);
      }
      comando = "cmd /c taskkill  /fi  \"imagename eq javaw.exe\"";
      for (int i = 0; i <= 5; i++) {
        try {
          pr = Runtime.getRuntime().exec(comando);
          System.out.println(comando);
        } catch (Exception ex) {
          System.out.println("Ha ocurrido un error al ejecutar el comando. Error: " + ex);
        }
      }
      System.gc();
      System.exit(0);
    }
  }//GEN-LAST:event_btnSalirActionPerformed

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

  private void labVerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labVerMouseClicked
  }//GEN-LAST:event_labVerMouseClicked

  private void labVerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_labVerKeyPressed

  }//GEN-LAST:event_labVerKeyPressed

  private void jMsgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMsgMouseClicked
    if (evt.getClickCount() == 2) {
      jAct.setVisible(true);
      jPass.setVisible(true);
    }
  }//GEN-LAST:event_jMsgMouseClicked

  private void jActMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jActMouseClicked
    jMsg.setText("");
    if (jPass.isVisible()) {
      jPass.setEnabled(false);
      jPass.setVisible(false);
    } else {
      jPass.setEnabled(true);
      jPass.setVisible(true);
      jLabCon.setEnabled(true);
      jPass.setText("");
      jPass.requestFocus();
      jMsg.setText("- Ingrese codigo modificacion -");
    }
  }//GEN-LAST:event_jActMouseClicked

  private void jPassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPassMouseClicked
    jPass.setSelectionStart(0);
    jPass.setSelectionEnd(jPass.getText().length());
  }//GEN-LAST:event_jPassMouseClicked

  private void jPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPassActionPerformed
    String pas = jPass.getText();

    System.out.println("pas=" + pas);

    if (pas.equals("*1*")) {
      try {
        ConexionSQL mysql = new ConexionSQL();
        Connection con = mysql.Conectar();
        Statement st = con.createStatement();
        st.execute("ALTER TABLE clientes "
                + "ADD COLUMN dic double)");
        st.execute("UPDATE clientes "
                + "set dic =0");
        jLabCon.setText("OK!");
        jPass.setVisible(false);
        st.close();
        con.close();
      } catch (SQLException ex) {
        System.out.println("-problema con sql: " + ex);
        jLabCon.setText("Error!");
        jPass.setVisible(false);
      }
    }

    if (pas.equals("*0*")) {
      try {
        ConexionSQL mysql = new ConexionSQL();
        Connection con = mysql.Conectar();
        Statement st = con.createStatement();

        st.execute("UPDATE pedidoH      set coc='500460449' where coc='095042502'");
        st.execute("UPDATE notaent      set coc='500460449' where coc='095042502'");
        st.execute("UPDATE notacred     set coc='500460449' where coc='095042502'");
        st.execute("UPDATE recibocobroH set coc='500460449' where coc='095042502'");
        st.execute("UPDATE recibocobroD set coc='500460449' where coc='095042502'");
        st.execute("UPDATE recibocobroP set coc='500460449' where coc='095042502'");

        /*
        String sql = "CREATE TABLE usuarios ("
                + "idu VARCHAR(10),  " // id iusuario
                + "cve VARCHAR(10),  " // clave
                + "rol int,          " // rol usuario
                + "PRIMARY KEY (idu) "
                + ")";
        st.execute(sql);

        sql = "Insert into usuarios "
                + "(idu,cve,rol) "
                + "VALUES ('admin','admin',0)";
        st.execute(sql);

        sql = "Insert into usuarios "
                + "(idu,cve,rol) "
                + "VALUES ('neide','neide',0)";
        st.execute(sql);

        sql = "CREATE TABLE parametros ("
                + "p01 VARCHAR(100),"
                + "p02 VARCHAR(100),"
                + "p03 VARCHAR(100),"
                + "p04 VARCHAR(100),"
                + "p05 VARCHAR(100),"
                + "p06 VARCHAR(100),"
                + "p07 VARCHAR(100),"
                + "p08 VARCHAR(100),"
                + "p09 VARCHAR(100),"
                + "p10 VARCHAR(100),"
                + "p11 VARCHAR(100),"
                + "p12 VARCHAR(100),"
                + "p13 VARCHAR(100),"
                + "p14 VARCHAR(100),"
                + "p15 VARCHAR(100),"
                + "p16 VARCHAR(100),"
                + "p17 VARCHAR(100),"
                + "p18 VARCHAR(100),"
                + "p19 VARCHAR(100),"
                + "p20 VARCHAR(100)"
                + ")";
        st.execute(sql);

        sql = "Insert into parametros "
                + "(p01,p02,p03,p04,p05,p06,p07,p08,p09,p10,p11,p12,p13,p14,p15,p16) "
                + "VALUES ("
                + "'100',"
                + "'640',"
                + "'S',"
                + "'Pasada 72 Horas, No Aceptamos Devoluciones',"
                + "'Para Depositos o Transferencias en Bs',"
                + "'- Banesco    Cta Cte  0134-0354-69-3541018107 Titular: Grupo FZ  Rif: J-40604173-4',"
                + "'- Mercantil   Cta Cte  0105-0132-64-1132161975 Titular: Grupo FZ  Rif: J-40604173-4',"
                + "'Para Depositos Cuentas Monedas Extranjera',"
                + "'Bancos Extranjeros:',"
                + "'- Zelle: Bank Of America   Cta  026009593 Titular: Grupo FZ INC email: grupofzusa@hotmailcom',"
                + "'- Banesco Panama            Cta  2210-2075-2951 Titular: Zuannong Feng',"
                + "'Bancos Nacionales:',"
                + "'- Mercatil  Dolares        Cta  0105-0132-62-5132007589 Titular: Grupo FZ  Rif: J-40604173-4',"
                + "'- Mercantil Euros          Cta  0105-0132-68-5132007619 Titular: Grupo FZ  Rif: J-40604173-4',"
                + "'- BNC Divisas Tipo A   Cta  0191-0020-19-2320002493 Titular: Grupo FZ  Rif: J-40604173-4',"
                + "'- Banesco Cuenta Verde     Solo Titular: Zhuhong feng  CI: E-83.022.912'"
                + ")";
        st.execute(sql);

    
        String rif = "J-40604173-4";
        String nom = "GRUPO FZ, C.A.";
        String dir = "Av Urb Palmar Este, Boulevar Montecarlo entre av la Costanera y av la playa, Local Ferreteria Ip-5";
        String fel = "20220208";
        String tlf = "0243-269.58.67";
        String eml = "grupofzoficina@hotmail.com";
        double ppp = 5;
        double poi = 16;
        int dcr = 30;
        int np1 = 13600;
        int np2 = 15000;
        int nr1 = 14000;
        int nr2 = 15000;
        int ne1 = 11100;
        int ne2 = 25000;
        int nc1 = 6000;
        int nc2 = 9000;

        // Importadora
        String sql = "DROP TABLE importadora";
        st.execute(sql);

        sql = "CREATE TABLE importadora ("
                + "rif VARCHAR(12),  " // rif importadora
                + "nom VARCHAR(50),  " // nombre importadora
                + "dir VARCHAR(160), " // Direccion
                + "tlf VARCHAR(40),  " // Telefono
                + "eml VARCHAR(50),  " // Email
                + "fec VARCHAR(10),  " // fecha lista dd/mm/yyyyy
                + "pod double ,      " // % descuento ppago con factura
                + "iva double ,      " // % Iva Actual
                + "dcr int,          " // Dias Credito
                + "np1 int,          " // pedido desde
                + "np2 int,          " // pedido hasta
                + "nr1 int,          " // recibo desde
                + "nr2 int,          " // recibo hasta
                + "ne1 int,          " // not ent desde
                + "ne2 int,          " // not ent hasta
                + "nc1 int,          " // nc desde
                + "nc2 int,          " // nc hasta
                + "PRIMARY KEY (rif) "
                + ")";
        st.execute(sql);

        sql = "Insert into Importadora "
                + "(rif,nom,dir,tlf,eml,fec,pod,iva,dcr,np1,np2,nr1,nr2,ne1,ne2,nc1,nc2) "
                + "VALUES ("
                + "'" + rif + "',"
                + "'" + nom + "',"
                + "'" + dir + "',"
                + "'" + tlf + "',"
                + "'" + eml + "',"
                + "'" + fel + "',"
                + "" + ppp + ","
                + "" + poi + ","
                + "" + dcr + ","
                + "" + np1 + ","
                + "" + np2 + ","
                + "" + nr1 + ","
                + "" + nr2 + ","
                + "" + ne1 + ","
                + "" + ne2 + ","
                + "" + nc1 + ","
                + "" + nc2 + ")";
        st.execute(sql);
         */
        jLabCon.setText("OK!");
        jPass.setVisible(false);
        st.close();
        con.close();
      } catch (SQLException ex) {
        System.out.println("-problema con sql: " + ex);
        jLabCon.setText("Error!");
        jPass.setVisible(false);
      }
    }

    /*
    
    
    if (pas.equals("*8*")) {
      try {
        ConexionSQL mysql = new ConexionSQL();
        Connection con = mysql.Conectar();
        Statement st = con.createStatement();
        // Nota de Cred/Devolucion
        String sql = "DROP TABLE notacred";
        st.execute(sql);
        sql = "CREATE TABLE notacred ("
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
          + "PRIMARY KEY (coc,ncr,nne)"
          + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxnc0 ON notacred (nne)");
        // Campo % Promocion
        st.execute("ALTER TABLE pedidoD ADD COLUMN por DECIMAL(6,2)");
        st.execute("UPDATE pedidoD set por =0");
        jLabCon.setText("OK!");
        jPass.setVisible(false);
        st.close();
        con.close();
      } catch (SQLException ex) {
        System.out.println("-problema con sql: " + ex);
        jLabCon.setText("Error!");
        jPass.setVisible(false);
      }
    }
    
    if (pas.equals("*0*")) {
      try {
        ConexionSQL mysql = new ConexionSQL();
        Connection con = mysql.Conectar();
        Statement st = con.createStatement();

        // Promocion Producto
        String sql = "DROP TABLE PromCombo";
        //st.execute(sql);
        sql = "CREATE TABLE PromCombo ("
          + "cop VARCHAR(10)," // codigo producto
          + "grp VARCHAR(10)," // Grupo
          + "por double,     " // % descuento 
          + "can int,        " // Cantidad Unds
          + "PRIMARY KEY (cop))";
        st.execute(sql);

        st.execute("ALTER TABLE recibocobroH "
          + "ADD COLUMN obs varchar(200)");
        st.execute("UPDATE recibocobroH set obs=''");
        jLabCon.setText("OK!");
        jPass.setVisible(false);
        st.close();
        con.close();
      } catch (SQLException ex) {
        System.out.println("-problema con sql: " + ex);
        jLabCon.setText("Error!");
        jPass.setVisible(false);
      }
    }

   
    if (pas.equals("*1*")) {
      try {
        ConexionSQL mysql = new ConexionSQL();
        Connection con = mysql.Conectar();
        Statement st = con.createStatement();
        st.execute("ALTER TABLE empaqueproducto "
          + "ADD COLUMN por DECIMAL(6,2)");
        st.execute("UPDATE empaqueproducto "
          + "set por =0");
        jLabCon.setText("OK!");
        jPass.setVisible(false);
        st.close();
        con.close();
      } catch (SQLException ex) {
        System.out.println("-problema con sql: " + ex);
        jLabCon.setText("Error!");
        jPass.setVisible(false);
      }
    }

    if (pas.equals("*2*")) {
      try {
        ConexionSQL mysql = new ConexionSQL();
        Connection con = mysql.Conectar();
        Statement st = con.createStatement();
        st.execute("ALTER TABLE pedidoH "
          + "ADD COLUMN ppm DECIMAL(6,2)");
        st.execute("UPDATE pedidoH "
          + "set ppm =0");
        jLabCon.setText("OK!");
        jPass.setVisible(false);
        st.close();
        con.close();
      } catch (SQLException ex) {
        System.out.println("-problema con sql: " + ex);
        jLabCon.setText("Error!");
        jPass.setVisible(false);
      }
    }

    if (pas.equals("*3*")) {
      try {
        ConexionSQL mysql = new ConexionSQL();
        Connection con = mysql.Conectar();
        Statement st = con.createStatement();
        String sql = "CREATE TABLE PromComboH ("
          + "grp VARCHAR(10)," // Grupo
          + "por double,     " // % descuento 
          + "can int,        " // Cantidad Unds
          + "sta VARCHAR(1), " // Estatus 0=activo, 1= inactivo
          + "PRIMARY KEY (grp))";
        st.execute(sql);

        sql = "CREATE TABLE PromComboD ("
          + "grp VARCHAR(10)," // Grupo
          + "cop VARCHAR(10)," // codigo producto
          + "PRIMARY KEY (grp,cop))";
        st.execute(sql);
        st.execute("CREATE INDEX idxgp1 ON PromComboD (cop,grp)");

        jLabCon.setText("OK!");
        jPass.setVisible(false);
        st.close();
        con.close();
      } catch (SQLException ex) {
        System.out.println("-problema con sql: " + ex);
        jLabCon.setText("Error!");
        jPass.setVisible(false);
      }
    }

    if (pas.equals("*4*")) {
      // st.execute("CREATE INDEX idxP0 ON pedidoD (npe)");
      try {
        ConexionSQL mysql = new ConexionSQL();
        Connection con = mysql.Conectar();
        Statement st = con.createStatement();
        st.execute("DROP INDEX idxP0");
        st.execute("CREATE INDEX idxP0 ON pedidoD (npe)");
        jLabCon.setText("OK!");
        jPass.setVisible(false);
        st.close();
        con.close();
      } catch (SQLException ex) {
        System.out.println("-problema con sql: " + ex);
        jLabCon.setText("Error!");
        jPass.setVisible(false);
      }
    }

    if (pas.equals("*5*")) {
      try {
        ConexionSQL mysql = new ConexionSQL();
        Connection con = mysql.Conectar();
        Statement st = con.createStatement();
        st.execute("DROP INDEX idxrcD1");
        st.execute("DROP INDEX idxrcP1");
        st.execute("CREATE INDEX idxrcD1 ON recibocobroD (nno)");
        st.execute("CREATE INDEX idxrcP1 ON recibocobroP (nno)");
        jLabCon.setText("OK!");
        jPass.setVisible(false);
        st.close();
        con.close();
      } catch (SQLException ex) {
        System.out.println("-problema con sql: " + ex);
        jLabCon.setText("Error!");
        jPass.setVisible(false);
      }
    }

    // alter decimal(15,2)
    if (pas.equals("*6*")) {

      try {
        ConexionSQL mysql = new ConexionSQL();
        Connection con = mysql.Conectar();
        Statement st = con.createStatement();

        // Recibo Cobro (Header)
        String sql = "DROP TABLE recibocobroH";
        st.execute(sql);
        sql = "CREATE TABLE recibocobroH ("
          + "nrc int,          " // nro recibo cobro
          + "frc VARCHAR(08),  " // fecha recibo cobro
          + "coc VARCHAR(12),  " // codigo cliente
          + "noc VARCHAR(50),  " // nombre Cliente
          + "tod decimal(15,2)," // total notas
          + "tpd decimal(15,2)," // total pagos $
          + "tpb decimal(15,2)," // total pagos Bs Ret iva
          + "sta VARCHAR(01),  " // status 0=pendiente 1= cerrado
          + "obs varchar(200), " // Observacion
          + "fre VARCHAR(12),  " // fecha registro  
          + "PRIMARY KEY (nrc)"
          + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxrcH0 ON recibocobroH (nrc)");
        st.execute("CREATE INDEX idxrcH1 ON recibocobroH (coc,nrc)");
        st.execute("CREATE INDEX idxrcH2 ON recibocobroH (frc,noc)");

        // Recibo Cobro (Detalle Documentos)
        sql = "DROP TABLE recibocobroD";
        st.execute(sql);
        sql = "CREATE TABLE recibocobroD ("
          + "nrc int,          " // nro recibo cobro
          + "coc VARCHAR(12),  " // codigo cliente
          + "tpd int,          " // tipo docum 0=not / ent 1=not cred 2=not deb
          + "nno int,          " // numero nota  
          + "nfa int,          " // numero factura
          + "fno VARCHAR(08),  " // fecha nota 
          + "fen VARCHAR(08),  " // fecha entrega
          + "fev VARCHAR(08),  " // fecha vence
          + "tdo decimal(15,2)," // tota documento $
          + "tdn decimal(15,2)," // total descuento nota
          + "toi decimal(15,2)," // total iva Bs
          + "trd decimal(15,2)," // total retencion 25% $
          + "fpa VARCHAR(08),  " // fecha pago 
          + "tpa decimal(15,2)," // total pago
          + "fre VARCHAR(12),  " // fecha registro  
          + "PRIMARY KEY (nrc,tpd,nno)"
          + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxrcD0 ON recibocobroD (coc,nrc,tpd,nno)");
        st.execute("CREATE INDEX idxrcD1 ON recibocobroD (nno)");
        st.execute("CREATE INDEX idxrcD2 ON recibocobroD (fno)");

        // Recibo Cobro (Detalle Pagos)
        sql = "DROP TABLE recibocobroP";
        st.execute(sql);
        sql = "CREATE TABLE recibocobroP ("
          + "nrc int,          " // nro recibo cobro
          + "coc VARCHAR(12),  " // codigo cliente
          + "fep VARCHAR(08),  " // fecha pago 
          + "tip VARCHAR(01),  " // tipo pago 0=abono,1=pago, 2=pago Ret Iva, 3=Saldo a favor, 4 Saldo Deuda
          + "bce VARCHAR(20),  " // Banco emisor 
          + "bcr VARCHAR(20),  " // Banco receptor
          + "ref VARCHAR(20),  " // referencia pago
          + "tpa decimal(15,2)," // total pago
          + "tas decimal(15,2)," // Tasa cambio Bs
          + "tpd decimal(15,2)," // Total pago $
          + "nno int,          " // numero nota afectada
          + "fre VARCHAR(12))  "; // Fecha registro
        st.execute(sql);
        st.execute("CREATE INDEX idxrcP0 ON recibocobroP (nrc,fep,tip)");
        st.execute("CREATE INDEX idxrcP1 ON recibocobroP (nno)");
        st.execute("CREATE INDEX idxrcP2 ON recibocobroP (coc,fep,nrc,tip,nno)");

        // Nota de Cred/Devolucion
        sql = "DROP TABLE notacred";
        st.execute(sql);
        sql = "CREATE TABLE notacred ("
          + "ncr int,          " // nro not/cred
          + "nne int,          " // nro nota entrega
          + "nfa int,          " // nro factura
          + "coc VARCHAR(12),  " // codigo cliente
          + "noc VARCHAR(50),  " // nombre cliente
          + "fnc VARCHAR(08),  " // fecha not/ent
          + "tne decimal(15,2)," // total Nota Entrega
          + "tnc decimal(15,2)," // total Nota Credito
          + "fre VARCHAR(12),  " // fecha registro  
          + "PRIMARY KEY (coc,ncr,nne)"
          + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxnc0 ON notacred (nne)");
        //
        // --------------------------------------
        //
        jLabCon.setText("-1!");

        sql = "DROP TABLE pedidoH2";
        //st.execute(sql);
        sql = "CREATE TABLE pedidoH2 ("
          + "npe int,          " // nro pedido cliente
          + "coc VARCHAR(12),  " // codigo cliente
          + "noc VARCHAR(50),  " // nombre Cliente
          + "tip VARCHAR(01),  " // tipo 0=Mayorista 1=Detal
          + "fac VARCHAR(01),  " // Con Factura S/N
          + "fep VARCHAR(08),  " // fecha pedido
          + "fel VARCHAR(08),  " // fecha lista precios
          + "obs VARCHAR(160), " // Observaciones
          + "por decimal(15,2) , " // % descuento ppago
          + "iva decimal(15,2) , " // % Iva
          + "pre decimal(15,2) , " // % retencion Iva
          + "fre VARCHAR(12),  " // fecha registro  
          + "ppm decimal(15,2) , " // % Promocion global
          + "PRIMARY KEY (npe)"
          + ")";
        st.execute(sql);

        sql = "insert into pedidoH2 "
          + "select * from pedidoH";
        st.execute(sql);

        // Pedidos Header
        sql = "DROP TABLE pedidoH";
        st.execute(sql);
        sql = "CREATE TABLE pedidoH ("
          + "npe int,          " // nro pedido cliente
          + "coc VARCHAR(12),  " // codigo cliente
          + "noc VARCHAR(50),  " // nombre Cliente
          + "tip VARCHAR(01),  " // tipo 0=Mayorista 1=Detal
          + "fac VARCHAR(01),  " // Con Factura S/N
          + "fep VARCHAR(08),  " // fecha pedido
          + "fel VARCHAR(08),  " // fecha lista precios
          + "obs VARCHAR(160), " // Observaciones
          + "por decimal(15,2), " // % descuento ppago
          + "iva decimal(15,2), " // % Iva
          + "pre decimal(15,2), " // % retencion Iva
          + "fre VARCHAR(12),   " // fecha registro  
          + "ppm decimal(15,2), " // % Promocion global
          + "PRIMARY KEY (npe)"
          + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxpd0 ON pedidoH (noc,npe)");
        st.execute("CREATE INDEX idxpd1 ON pedidoH (fep,noc)");

        sql = "insert into pedidoH "
          + "select * from pedidoH2";
        st.execute(sql);

        sql = "DROP TABLE pedidoH2";
        st.execute(sql);

        jLabCon.setText("-2!");

        // Pedidos Detail
        sql = "CREATE TABLE pedidoD2 ("
          + "npe int,          " // nro pedido cliente
          + "cop VARCHAR(10),  " // codigo producto
          + "dep VARCHAR(60),  " // descripcion producto
          + "unm VARCHAR(10),  " // unidad Medida venta
          + "can decimal(15,2) , " // cantidad producto
          + "prm decimal(15,2) , " // precio Mayor
          + "prd decimal(15,2)        " // precio Detal
          + ")";
        st.execute(sql);

        sql = "insert into pedidoD2 "
          + "select * from pedidoD";
        st.execute(sql);

        sql = "DROP TABLE pedidoD";
        st.execute(sql);
        // Pedidos Detail
        sql = "CREATE TABLE pedidoD ("
          + "npe int,          " // nro pedido cliente
          + "cop VARCHAR(10),  " // codigo producto
          + "dep VARCHAR(60),  " // descripcion producto
          + "unm VARCHAR(10),  " // unidad Medida venta
          + "can decimal(15,2) , " // cantidad producto
          + "prm decimal(15,2) , " // precio Mayor
          + "prd decimal(15,2)        " // precio Detal
          + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxP0 ON pedidoD (npe)");

        sql = "insert into pedidoD "
          + "select * from pedidoD2";
        st.execute(sql);

        sql = "DROP TABLE pedidoD2";
        st.execute(sql);

        jLabCon.setText("-3!");

        sql = "CREATE TABLE notaent2 ("
          + "nne int,          " // nro not/ent
          + "nfa int,          " // nro factura
          + "npe int,          " // nro pedido
          + "coc VARCHAR(12),  " // codigo cliente
          + "noc VARCHAR(50),  " // nombre Cliente
          + "fne VARCHAR(08),  " // fecha not/ent
          + "fep VARCHAR(08),  " // fecha pedido
          + "fee VARCHAR(08),  " // fecha entrega a cliente
          + "fev VARCHAR(08),  " // fecha vence
          + "tpe decimal(15,2) , " // total pedido
          + "tne decimal(15,2) , " // total nota entrega
          + "tdn decimal(15,2) , " // total descuento nota
          + "tfa decimal(15,2) , " // total Base factura
          + "tdf decimal(15,2) , " // total descuento factura
          + "toi decimal(15,2) , " // total iva
          + "iva decimal(15,2) , " // % Iva
          + "pre decimal(15,2) , " // % retencion
          + "tor decimal(15,2) , " // total retencion
          + "obs VARCHAR(120), " // Observaciones
          + "fre VARCHAR(12),  " // fecha registro  
          + "PRIMARY KEY (nne,npe)"
          + ")";
        st.execute(sql);

        sql = "insert into notaent2 "
          + "select * from notaent";
        st.execute(sql);

        // Nota de Entrega
        sql = "DROP TABLE notaent";
        st.execute(sql);
        sql = "CREATE TABLE notaent ("
          + "nne int,          " // nro not/ent
          + "nfa int,          " // nro factura
          + "npe int,          " // nro pedido
          + "coc VARCHAR(12),  " // codigo cliente
          + "noc VARCHAR(50),  " // nombre Cliente
          + "fne VARCHAR(08),  " // fecha not/ent
          + "fep VARCHAR(08),  " // fecha pedido
          + "fee VARCHAR(08),  " // fecha entrega a cliente
          + "fev VARCHAR(08),  " // fecha vence
          + "tpe decimal(15,2) , " // total pedido
          + "tne decimal(15,2) , " // total nota entrega
          + "tdn decimal(15,2) , " // total descuento nota
          + "tfa decimal(15,2) , " // total Base factura
          + "tdf decimal(15,2) , " // total descuento factura
          + "toi decimal(15,2) , " // total iva
          + "iva decimal(15,2) , " // % Iva
          + "pre decimal(15,2) , " // % retencion
          + "tor decimal(15,2) , " // total retencion
          + "obs VARCHAR(120), " // Observaciones
          + "fre VARCHAR(12),  " // fecha registro  
          + "PRIMARY KEY (nne,npe)"
          + ")";
        st.execute(sql);
        st.execute("CREATE INDEX idxn1 ON notaent (npe)");
        st.execute("CREATE INDEX idxn2 ON notaent (noc,nne)");
        st.execute("CREATE INDEX idxn3 ON notaent (fne)");
        st.execute("CREATE INDEX idxn4 ON notaent (nfa)");

        sql = "insert into notaent "
          + "select * from notaent2";
        st.execute(sql);

        sql = "DROP TABLE notaent2";
        st.execute(sql);

        jLabCon.setText("OK!");
        jPass.setVisible(false);

        st.close();
        con.close();
      } catch (SQLException ex) {
        System.out.println("-problema con sql: " + ex);
        jLabCon.setText("Error!");
        jPass.setVisible(false);
      }
    }
     */
 /*
    int diahoy = getDiaHoy();
    String diaM = Integer.toString(diahoy);
    if (diaM.length() == 1) {
      diaM = "0" + diaM;
    }
    String LetI = "I";
    if (diahoy >= 15) {
      LetI = "A";
    }
    String LetF = "H";
    if (diahoy < 15) {
      LetF = "F";
    }
    String diaM2 = LetF + diaM + LetI + ".";
    diaM = LetI + diaM + "." + LetF;
    System.out.println("diaM=" + diaM);
    System.out.println("diaM2=" + diaM2);

    // Procesa Carga reportes Unidades
    ConexionSQL bdsql = new ConexionSQL();
    Connection con = bdsql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "";
      // Activar
      if (pas.equals(diaM) || pas.equals("...")) {
        // Control pruebas
        sql = "update cia set conpro=-1";
        st.execute(sql);
        canpro = 0;
        jPass.setVisible(false);
        jAct.setVisible(false);
        jLabCon.setVisible(false);
        //btnGen.setEnabled(true);
        jMsg.setText("- Listo - Licencia liberada -");
        // caso errores sql
      } else {
        jMsg.setText("- SERIAL  INCORRECTO -");
        jPass.setSelectionStart(0);
        jPass.setSelectionEnd(jPass.getText().length());
        jPass.requestFocus();
      }
      // Desactivar - perido prueba
      if (pas.equals(diaM2)) {
        sql = "update cia set conpro=0";
        st.execute(sql);
        canpro = 0;
        jLabCon.setText(canpro + "/30");
        jMsg.setText("- Listo - Periodo prueba -");
      }
      con.close(); // cerramos la conexio
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
    }
     */
  }//GEN-LAST:event_jPassActionPerformed


  private void labPdfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labPdfMouseClicked
    jMsg.setText("");
    String vax = "pdf\\";
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
  }//GEN-LAST:event_labPdfMouseClicked

  private void btnCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCarActionPerformed
    jMsg.setText("");
    String fil = "C:/Windows/System32/vss_sv.dll";
    if (FileExist(fil)) {
      pdfsCargados();
      icon = new ImageIcon(getClass().getResource("/img/ok.png"));
      String vax = "DESEA CARGAR LA NUEVA\nLISTA DE PRECIOS ?";
      String[] options = {"SI", "NO"};
      int opcion = 0;
      opcion = JOptionPane.showOptionDialog(null, vax, "** Aviso **", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
      if (opcion == 0) {
        //jrutExc.setBackground(Color.LIGHT_GRAY);
        jrutExc.setBackground(new java.awt.Color(242, 247, 247));
        Thread hilo = new Thread() {
          public void run() {
            btnCar.setEnabled(false);
            cargaListasPdf();
          }
        };
        hilo.start();
      }
    } else {
      jMsg.setText("- Activar Licencia de Uso");
    }
  }//GEN-LAST:event_btnCarActionPerformed

  private void jrutExcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrutExcMouseClicked
    jMsg.setText("");
    jrutExc.setBackground(new java.awt.Color(242, 247, 247));
    String vax = "rep\\exc\\";
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
  }//GEN-LAST:event_jrutExcMouseClicked

  private void btnPrdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrdActionPerformed
    jMsg.setText("");
    busParametros();
    selunm = "N";
    //Lista Parametros
    for (int i = 0; i < VecPar.length; i++) {
      String vax = VecPar[i].trim();
      // Tamaño promedio
      if (i == 0) {
        if (isNumeric(vax)) {
          if (isNumeric(vax)) {
            proimg = Integer.valueOf(vax);
          }
        }
      }
      // Tamaño Imagen
      if (i == 1) {
        if (isNumeric(vax)) {
          if (isNumeric(vax)) {
            tamimg = Integer.valueOf(vax);
          }
        }
      }
      // Selecciona unm
      if (i == 2) {
        selunm = vax;
      }
    }
    if (selunm == null) {
      selunm = "N";
    }
    pdfsCargados();
    if (RegLis != null) {
      RegLis.dispose();
    }
    RegLis = new Registro_CatalogoProductos();   // Registro Productos
    RegLis.setVisible(true);
    RegLis.setExtendedState(NORMAL);
    RegLis.setVisible(true);
  }//GEN-LAST:event_btnPrdActionPerformed

  private void btnImpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpActionPerformed
    jMsg.setText("");
    if (RegImp != null) {
      RegImp.dispose();
    }
    RegImp = new Registro_Importadora();   // Registro Productos
    RegImp.setVisible(true);
    RegImp.setExtendedState(NORMAL);
    //RegLis.setAlwaysOnTop(true);
    RegImp.setVisible(true);
  }//GEN-LAST:event_btnImpActionPerformed

  private void btnVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVenActionPerformed
    jMsg.setText("");
    if (RegVen != null) {
      RegVen.dispose();
    }
    RegVen = new Registro_Vendedor();   // Registro Vendedor
    RegVen.setVisible(true);
    RegVen.setExtendedState(NORMAL);
    //RegLis.setAlwaysOnTop(true);
    RegVen.setVisible(true);
  }//GEN-LAST:event_btnVenActionPerformed

  private void btnCteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCteActionPerformed
    jMsg.setText("");
    if (RegCte != null) {
      RegCte.dispose();
    }
    RegCte = new Registro_Clientes();   // Registro Vendedor
    RegCte.setVisible(true);
    RegCte.setExtendedState(NORMAL);
    RegCte.setVisible(true);
  }//GEN-LAST:event_btnCteActionPerformed

  private void btnPedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPedActionPerformed
    jMsg.setText("");
    busParametros();
    if (RegPed != null) {
      RegPed.dispose();
    }
    RegPed = new Registro_PedidoCliente();   // Registro Pedidos
    RegPed.setVisible(true);
    RegPed.setExtendedState(NORMAL);
    RegPed.setVisible(true);
  }//GEN-LAST:event_btnPedActionPerformed

  private void jParam1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jParam1MouseClicked
    jMsg.setText("");
    if (RegPar != null) {
      RegPar.dispose();
    }
    RegPar = new Registro_Parametros();   // Pedisos Vs Notas
    RegPar.setVisible(true);
    RegPar.setExtendedState(NORMAL);
    RegPar.setVisible(true);
  }//GEN-LAST:event_jParam1MouseClicked

  private void btnNenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNenActionPerformed
    jMsg.setText("");
    if (RegNoe != null) {
      RegNoe.dispose();
    }
    RegNoe = new Registro_NotaEntrega();   // Registro Nota Entrega
    RegNoe.setVisible(true);
    RegNoe.setExtendedState(NORMAL);
    RegNoe.setVisible(true);
  }//GEN-LAST:event_btnNenActionPerformed

  private void btnNcrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNcrActionPerformed
    jMsg.setText("");
    if (RegNcr != null) {
      RegNcr.dispose();
    }
    RegNcr = new Registro_NotaCredito();   // Registro Nota Entrega
    RegNcr.setVisible(true);
    RegNcr.setExtendedState(NORMAL);
    RegNcr.setVisible(true);
  }//GEN-LAST:event_btnNcrActionPerformed

  private void btnPagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagActionPerformed
    jMsg.setText("");
    if (RegCob != null) {
      RegCob.dispose();
    }
    RegCob = new Registro_PagosClientes();   // Registro Cobros Clientes
    RegCob.setVisible(true);
    RegCob.setExtendedState(NORMAL);
    RegCob.setVisible(true);
  }//GEN-LAST:event_btnPagActionPerformed

  private void btnCopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopActionPerformed
    jMsg.setText("");
    if (RegCop != null) {
      RegCop.dispose();
    }
    RegCop = new Registro_Copa();   // Registro Copa
    RegCop.setVisible(true);
    RegCop.setExtendedState(NORMAL);
    RegCop.setVisible(true);
  }//GEN-LAST:event_btnCopActionPerformed

  private void jrutDocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrutDocMouseClicked
    jMsg.setText("");
    String vax = "rep//doc\\";
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
  }//GEN-LAST:event_jrutDocMouseClicked

  private void btnSdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSdoActionPerformed
    jMsg.setText("");
    if (ResSdo != null) {
      ResSdo.dispose();
    }
    ResSdo = new Consulta_EstadoCuentaR();   // Saldos Clientes
    ResSdo.setVisible(true);
    ResSdo.setExtendedState(NORMAL);
    ResSdo.setVisible(true);
  }//GEN-LAST:event_btnSdoActionPerformed

  private void btnCgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCgeActionPerformed
    jMsg.setText("");
    icon = new ImageIcon(getClass().getResource("/img/excel.jpg"));
    String vax = "DESEA CARGAR\nLA LISTA CON EXISTENCIAS ?";
    String[] options = {"SI", "NO"};
    int opcion = 0;
    opcion = JOptionPane.showOptionDialog(null, vax, "** Aviso **", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
    if (opcion == 0) {
      //jrutExc.setBackground(Color.LIGHT_GRAY);
      //jrutExc.setBackground(new java.awt.Color(242, 247, 247));
      Thread hilo = new Thread() {
        public void run() {
          btnCge.setEnabled(false);
          cargaExcelStk();
        }
      };
      hilo.start();
    }
  }//GEN-LAST:event_btnCgeActionPerformed

  private void btnCpmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCpmActionPerformed
    jMsg.setText("");
    icon = new ImageIcon(getClass().getResource("/img/excel.jpg"));
    String vax = "DESEA CARGAR\nGRUPOS PRODUCTOS\nCON PROMOCIONES ?";
    String[] options = {"SI", "NO"};
    int opcion = 0;
    opcion = JOptionPane.showOptionDialog(null, vax, "** Aviso **", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
    if (opcion == 0) {
      Thread hilo = new Thread() {
        public void run() {
          btnCpm.setEnabled(false);
          cargaExcelPrm();
        }
      };
      hilo.start();
    }
  }//GEN-LAST:event_btnCpmActionPerformed

  private void btnSdo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSdo1ActionPerformed
    jMsg.setText("");
    if (PedNot != null) {
      PedNot.dispose();
    }
    PedNot = new Consulta_PedVsNotR();   // Pedisos Vs Notas
    PedNot.setVisible(true);
    PedNot.setExtendedState(NORMAL);
    PedNot.setVisible(true);
  }//GEN-LAST:event_btnSdo1ActionPerformed

  private void btnImpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImpMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_btnImpMouseClicked

  private void btnVjaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVjaMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_btnVjaMouseClicked

  private void btnVjaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVjaActionPerformed
    if (Vieja == null) {
      // Calc = new Calculadora();
    } else {
      Vieja.dispose();
    }
    Vieja = new LaVieja(usr);
    Vieja.setVisible(true);
  }//GEN-LAST:event_btnVjaActionPerformed

  public void cargaExcelPrm() {
    String rut = "pdf\\";
    if (FileExist(rut)) {
      File folder = new File(rut);
      File[] listOfFiles = folder.listFiles();
      //Lista Pdfs
      int c = 0;
      // Verifica listas Mayor / Detal
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
          String exc = listOfFiles[i].getName().toLowerCase();
          String src = listOfFiles[i].getAbsolutePath().toLowerCase();
          if (exc.endsWith(".xlsx".toLowerCase())) {
            if (listOfFiles[i].canRead()) {
              exc = exc.replace(" ", "_").replace("  ", "_").replace("-", "").replace("/", "");
              if (exc.indexOf("promo") >= 0) {
                cargaPromociones(src, exc);
                c = 1;
              }
            } else {
              jMsg.setText("- No se puede leer Excel " + exc);
            }
          }
        }
      }
      btnCpm.setEnabled(true);
      if (c == 0) {
        jMsg.setText("- No Hay Excel Promociones");
        // inicializa d
        ConexionSQL mysql = new ConexionSQL();
        Connection con = mysql.Conectar();
        try {
          Statement st = con.createStatement();
          String sql = "delete from PromCombo ";
          st.execute(sql);
          con.close(); // cerramos la conexion
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
        }
      }
    } else {
      btnCpm.setEnabled(true);
      jMsg.setText("- No existe ruta" + rut);
    }
  }

  public void cargaPromociones(String src, String fil) {

    String rutref = src;

    if (FileExist(rutref)) {

      int i = 0;
      List<String> prods = new ArrayList<>();

      try {

        InputStream fi = null;
        try {

          ConexionSQL mysql = new ConexionSQL();
          Connection con = mysql.Conectar();
          try {
            Statement st = con.createStatement();
            String sql = "delete from PromCombo ";
            st.execute(sql);

            fi = new FileInputStream(rutref);
            XSSFWorkbook wb = new XSSFWorkbook(fi);
            XSSFSheet sheet = wb.getSheetAt(0);  // Hoja 1
            //XSSFRow row;
            //XSSFCell cell;
            String cod = "", grp = "", pox = "", cax = "";
            int idxfil = 0;             // Fila (row)
            int idxcol = 0;             // Columna

            Iterator rows = sheet.rowIterator();
            while (rows.hasNext()) {
              cod = "";
              grp = "";
              pox = "";
              cax = "";
              row = (XSSFRow) rows.next();   // -> recorre filas
              Iterator cells = row.cellIterator();
              while (cells.hasNext()) {      // -> recorre columnas celdas
                cell = (XSSFCell) cells.next();
                idxfil = row.getRowNum();        // Fila
                idxcol = cell.getColumnIndex();  // Columna
                if (idxfil >= 1 && (idxcol == 0 || idxcol == 1 || idxcol == 2 || idxcol == 3)) {
                  // codigo
                  if (idxcol == 0) {
                    cod = getCelda();
                  }
                  // grupo
                  if (idxcol == 1) {
                    grp = getCelda();
                  }
                  // %desc
                  if (idxcol == 2) {
                    pox = getCelda();
                  }
                  // cantidad
                  if (idxcol == 3) {
                    cax = getCelda();
                  }
                }
              }
              // Graba registro
              if (cod.length() > 0 && cod.toUpperCase().indexOf("CODIGO") == -1) {

                cod = cod.trim();
                grp = grp.trim();

                if (cod.length() > 10) {
                  cod = cod.substring(0, 10);
                }
                cod = cod.toUpperCase().trim();
                if (grp.length() > 10) {
                  grp = grp.substring(0, 10);
                }
                grp = grp.toUpperCase().trim();

                cax = cax.replace(",", "").replace(".", "");
                pox = pox.replace(",", "");

                if (isNumeric(pox) && isNumeric(cax)) {
                  cod = cod.replace(";", "");
                  grp = grp.replace(";", "");
                  String vax = cod + ";" + grp + ";" + pox + ";" + cax;
                  prods.add(vax);

                  int can = Integer.parseInt(cax);
                  double por = Double.parseDouble(pox);

                  sql = "SELECT count(*) can from PromCombo "
                          + "where cop='" + cod + "'";
                  ResultSet rs = st.executeQuery(sql);
                  int c = 0;
                  while (rs.next()) {
                    c = rs.getInt("can");
                  }
                  if (c == 0) {
                    sql = "insert into PromCombo "
                            + "(cop,grp,por,can) "
                            + "values('" + cod + "','" + grp + "'," + por + "," + can + ")";
                    st.execute(sql);
                  }
                } else {
                  linerr = linerr + "\n  -Cantidad  (" + cod + ") invalida";
                }
                i++;
              }
            }
            jMsg.setText("Listo! - Se cargo Promociones.");
            fi.close();
            wb.unLockWindows();

            con.close(); // cerramos la conexion
          } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
          }

        } finally {
          if (fi != null) {
            fi.close();         // Cierra Excel (uso)
          }
        }

        // Lista
        if (i > 0) {

        }

      } catch (IOException ex) {
        Logger.getLogger(Menu.class
                .getName()).log(Level.SEVERE, null, ex);
      }
    }

  }

  public void cargaExcelStk() {
    String rut = "pdf/";
    if (FileExist(rut)) {
      File folder = new File(rut);
      File[] listOfFiles = folder.listFiles();
      //Lista Pdfs
      int c = 0;
      // Verifica listas Mayor / Detal
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
          String exc = listOfFiles[i].getName().toLowerCase();
          String src = listOfFiles[i].getAbsolutePath().toLowerCase();
          if (exc.endsWith(".xlsx".toLowerCase())) {
            c = 1;
            if (listOfFiles[i].canRead()) {
              exc = exc.replace(" ", "_").replace("  ", "_").replace("-", "").replace("/", "");
              if (exc.indexOf("repo") >= 0) {
                cargaExistencias(src, exc);
                c = 2;
              }
            } else {
              jMsg.setText("- No se puede leer Excel " + exc);
            }
          }
        }
      }
      if (c == 0) {
        jMsg.setText("- No Hay Excel Reposiciones");
        btnCar.setEnabled(true);
      }
      if (c == 1) {
        jMsg.setText("- nombre Excel no es Reposicion");
        btnCge.setEnabled(true);
      } else {
        btnCge.setEnabled(true);
      }
    } else {
      btnCar.setEnabled(true);
      jMsg.setText("- No existe ruta" + rut);
    }
  }

  public void cargaExistencias(String src, String fil) {

    DecimalFormat df = new DecimalFormat("##0");
    String rutref = src;

    if (FileExist(rutref)) {

      int i = 0;
      List<String> prods = new ArrayList<>();

      try {

        InputStream fi = null;
        try {

          fi = new FileInputStream(rutref);
          XSSFWorkbook wb = new XSSFWorkbook(fi);
          XSSFSheet sheet = wb.getSheetAt(0);  // Hoja 1
          //XSSFRow row;
          //XSSFCell cell;
          String cod = "", des = "", can = "1";
          int idxfil = 0;             // Fila (row)
          int idxcol = 0;             // Columna

          Iterator rows = sheet.rowIterator();
          while (rows.hasNext()) {
            cod = "";
            des = "";
            can = "";
            row = (XSSFRow) rows.next();   // -> recorre filas
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {      // -> recorre columnas celdas
              cell = (XSSFCell) cells.next();
              idxfil = row.getRowNum();        // Fila
              idxcol = cell.getColumnIndex();  // Columna
              if (idxfil >= 1 && (idxcol == 0 || idxcol == 1 || idxcol == 2)) {
                // codigo
                if (idxcol == 0) {
                  cod = getCelda();
                }
                // nombre
                if (idxcol == 1) {
                  des = getCelda();
                }
                // cantidad
                if (idxcol == 2) {
                  can = getCelda();
                }

              }
            }
            // Graba registro
            if (cod.length() > 0 && cod.toUpperCase().indexOf("CODIGO") == -1) {
              cod = cod.trim();
              des = des.trim();
              cod = cod.trim();
              if (cod.length() > 10) {
                cod = cod.substring(0, 10);
              }
              cod = cod.toUpperCase().trim();
              if (des.length() > 40) {
                des = des.substring(0, 40);
              }
              des = des.toUpperCase().trim();

              can = can.replace(",", "");
              can = can.replace(".", "");

              if (isNumeric(can)) {
                cod = cod.replace(";", "");
                des = des.replace(";", "");
                String vax = cod + ";" + des + ";" + can;
                prods.add(vax);
              } else {
                linerr = linerr + "\n  -Cantidad  (" + cod + ") invalida";
              }
              i++;
            }
          }

          //fi.close();
          wb.unLockWindows();
        } finally {
          if (fi != null) {
            fi.close();         // Cierra Excel (uso)
          }
        }

        // Lista
        if (i > 0) {
          if (RegStk != null) {
            RegStk.dispose();
          }
          RegStk = new Registra_Existencias(prods, fil);
          RegStk.setVisible(true);
          RegStk.setExtendedState(NORMAL);
          RegStk.setVisible(true);

          //new Registra_Existencias(prods).setVisible(true);
        }

      } catch (IOException ex) {
        Logger.getLogger(Menu.class
                .getName()).log(Level.SEVERE, null, ex);
      }
    }

  }

  // Obtiene valor celda
  public String getCelda() {
    String cam = "";
    switch (cell.getCellType()) {
      // Celda valor String
      case XSSFCell.CELL_TYPE_STRING:
        if (cell.getStringCellValue() == null) {
          cam = "";
        } else {
          cam = cell.getStringCellValue().trim();
        }
        break;
      // Celda valor numerico
      case XSSFCell.CELL_TYPE_NUMERIC:
        if (cell.getRawValue() == null) {
          cam = "";
        } else {
          cam = cell.getRawValue().trim();
        }
        break;
      // Celda con Formula
      case XSSFCell.CELL_TYPE_FORMULA:
        switch (cell.getCachedFormulaResultType()) {
          case XSSFCell.CELL_TYPE_NUMERIC:
            if (cell.getRawValue() == null) {
              cam = "";
            } else {
              cam = cell.getRawValue().trim();
            }
            break;
          case XSSFCell.CELL_TYPE_STRING:
            if (cell.getRawValue() == null) {
              cam = "";
            } else {
              cam = cell.getRawValue().trim();
            }
            break;
          default:
        }
    }
    return cam;
  }

  public void cargaListasPdf() {

    fecact = "";
    String rut = "pdf/";
    if (FileExist(rut)) {
      File folder = new File(rut);
      File[] listOfFiles = folder.listFiles();
      //Lista Pdfs
      int c = 0;
      // Verifica listas Mayor / Detal
      int can = 0;
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
          String pdf = listOfFiles[i].getName().toLowerCase();
          String src = listOfFiles[i].getAbsolutePath().toLowerCase();
          if (pdf.endsWith(".pdf".toLowerCase())) {
            c = 1;
            if (listOfFiles[i].canRead()) {
              pdf = pdf.replace(" ", "").replace("  ", "").replace("-", "").replace("/", "");
              if (pdf.indexOf("detal") >= 0 || pdf.indexOf("mayor") >= 0 || pdf.indexOf("(d)") >= 0 || pdf.indexOf("(m)") >= 0) {
                cargaListaPreciosBD(src, can);
                can++;
                c = 2;
              }
            } else {
              jMsg.setText("- No se puede leer PDF " + pdf);
            }
          }
        }
      }

      if (c == 0) {
        jMsg.setText("- No Hay Listas Precios");
        btnCar.setEnabled(true);
      }
      if (c == 1) {
        jMsg.setText("- nombre pdf no es mayor o detal");
        btnCar.setEnabled(true);
      } else {

        // Procesa
        //jMsg.setText("- Cargando Lista de Precios ");
        //eliminaEmpaque();   // obsoletas
        //eliminaImagenes();  // obsoletas
        Importadora i = new Importadora();
        fecact = i.getFecLis();
        new Reporte_ExcelListaPrecios(fecact.replace("/", "").replace("-", ""), 0);
        btnCar.setEnabled(true);

      }
    } else {
      btnCar.setEnabled(true);
      jMsg.setText("- No existe ruta" + rut);
    }
  }

  public void eliminaImagenes() {
    String rut = "imgprd/";
    if (FileExist(rut)) {

      File folder = new File(rut);
      File[] listOfFiles = folder.listFiles();

      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      try {
        Statement st = con.createStatement();

        int c = 0;
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            String img = listOfFiles[i].getName().toUpperCase().trim();
            if (img.endsWith(".JPG") || img.endsWith(".PNG")) {
              String cop = img.replace(".JPG", "").replace(".PNG", "");
              c = 0;
              String sql = "select count(*) can "
                      + "from listaprecios "
                      + "where cop='" + cop + "'";
              ResultSet rs = st.executeQuery(sql);
              while (rs.next()) {
                c = rs.getInt("can");
              }
              if (c == 0) {
                String org = "imgprd/" + img;
                String des = "imgprd/inactivos/" + img;
                des = des.replace(".JPG", ".PNG");
                FileChannel inputChannel = null;
                FileChannel outputChannel = null;
                try {
                  inputChannel = new FileInputStream(org).getChannel();
                  outputChannel = new FileOutputStream(des).getChannel();
                  outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                  c = 1;

                } catch (FileNotFoundException ex) {
                  Logger.getLogger(Menu.class
                          .getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                  Logger.getLogger(Menu.class
                          .getName()).log(Level.SEVERE, null, ex);
                } finally {
                  try {
                    inputChannel.close();
                    outputChannel.close();
                    if (c == 1) {
                      // elimina
                      if (FileSave(org)) {
                        System.out.println("se elimino " + org);
                      }
                    }
                  } catch (IOException ex) {
                    Logger.getLogger(Menu.class
                            .getName()).log(Level.SEVERE, null, ex);
                  }
                }
              }
            }
          }
        }
        con.close(); // cerramos la conexion
        // caso errores sql
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
      }

    }
  }

  // Elimina empaque codigo  
  public void eliminaEmpaque() {
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "delete from empaqueproducto "
              + "where cop not in (select cop from listaprecios)";
      st.execute(sql);
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
  }

  // cantidad reportes carga pdf
  public int getCanPdf() {
    int can = 0;
    String rut = "pdf/";
    if (FileExist(rut)) {
      File folder = new File(rut);
      File[] listOfFiles = folder.listFiles();
      can = listOfFiles.length - 1;
    }
    return can;
  }

  // Cantidad imagenes Pro
  public static int getCanImgPrd() {
    int can = 0;
    String rut = "imgprd/";
    if (FileExist(rut)) {
      File folder = new File(rut);
      File[] listOfFiles = folder.listFiles();
      can = listOfFiles.length - 1;
    }
    return can;
  }

  // Cantidad imagenes Pro
  public static int getCanImgDep() {
    int can = 0;
    String rut = "imgdep/";
    if (FileExist(rut)) {
      File folder = new File(rut);
      File[] listOfFiles = folder.listFiles();
      can = listOfFiles.length;
    }
    return can;
  }

  // Cantidad reportes Pdf Carga
  public int getCanPdfCga() {
    int can = 0;
    String rut = "pdf/";
    if (FileExist(rut)) {
      File folder = new File(rut);
      File[] listOfFiles = folder.listFiles();
      can = listOfFiles.length - 1;
    }
    return can;
  }

  // Cantidad reportes Pdf
  public int getCanRepPdf() {
    int can = 0;
    String rut = "rep/pdf/";
    if (FileExist(rut)) {
      File folder = new File(rut);
      File[] listOfFiles = folder.listFiles();
      can = listOfFiles.length - 1;
    }
    return can;
  }

  // Cantidad reportes Pdf
  public int getCanRepExc() {
    int can = 0;
    String rut = "rep/exc/";
    if (FileExist(rut)) {
      File folder = new File(rut);
      File[] listOfFiles = folder.listFiles();
      can = listOfFiles.length - 1;
    }
    return can;
  }

  // Carga Precios bd
  public void cargaListaPreciosBD(String src, int can) {

    // Tipo lista
    int tip = 0;     // tipo lista 0=mayor, 1=detal

    if (src.indexOf("detal") >= 0 || src.indexOf("(d)") >= 0) {
      tip = 1;
      jMsg.setText("- Cargando Lista Precios - ( DETAL )");
    } else {
      jMsg.setText("- Cargando Lista Precios - ( MAYOR )");
    }
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();

      if (can == 0) {

        fre = ymdhoyhhmm();
        // Ultima Lista Cargada yyyymmdd
        Importadora i = new Importadora();
        fecact = i.getFecLis();
        // Guarda lista Actual
        // Graba departamentoT  Actual
        String sql = "delete from departamentoT ";
        //        + "where fel='" + fecact + "'";
        st.execute(sql);
        sql = "insert into departamentoT "
                + "select '" + fecact + "',dep,nom,ob1,ob2 from departamento";
        st.execute(sql);

        // Graba listapreciosT Actual
        sql = "delete from listapreciosT ";
        //        + "where fel='" + fecact + "'";
        st.execute(sql);
        sql = "insert into listapreciosT "
                + "select '" + fecact + "',dep,cop,cof,nom,pum,pud,stk,man,pag,sta,fre from listaprecios";
        st.execute(sql);

        // Inicializa Tablas
        sql = "delete from listaprecios";
        st.execute(sql);
        sql = "delete from departamento";
        st.execute(sql);
      }

      try {
        String cx = "";
        String depA = "";
        String tx = "";
        String ln = "";
        String fec = "";   // fecha lista
        String dep = "";   // departamento
        String cop = "";   // codigo producto
        String cof = "";   // codigo producto Busqueda sin "-"
        String nom = "";   // nombre producto
        String prc = "";   // precio det/may
        String exi = "";   // existencia
        String pud = "";   // precio detal
        String pum = "";   // precio mayor

        int cc = 1;
        PdfReader pr = new PdfReader(src);
        int pNum = pr.getNumberOfPages();
        for (int page = 1; page <= pNum; page++) {
          tx = "";
          ln = "";
          dep = "";
          cop = "";
          cof = "";
          nom = "";
          prc = "";
          exi = "";
          pud = "";
          pum = "";
          int det = 0;
          String ln2 = "";

          tx = PdfTextExtractor.getTextFromPage(pr, page);
          String[] tokentx = tx.split("\n");
          int tk = tokentx.length;

          //System.out.println("tx=" + tx);
          for (int i = 0; i < tokentx.length; i++) {

            ln = tokentx[i].trim();
            ln2 = "";

            // Fecha Lista
            String str = "Fecha :";
            int j = ln.indexOf(str);
            if (j >= 0) {
              fec = ln.substring(j + 8, ln.length()).trim();
              fec = fec.replace("/", "").replace("-", "");
              fec = dmy_ymd(fec); // yyyymmdd
              labFec.setText("Fecha Lista Precios  " + ymd_dmy(fec));
              System.out.println("importadora fec=" + fec + ", fecact=" + fecact);
              if (can == 0) {
                // Actualiza nueva fecha Lista Precios
                String sql = "update importadora "
                        + "set fec= '" + fec + "'";
                st.execute(sql);
              }
            }
            // Departamento
            int pos = 15;
            str = "Departamento";
            j = ln.indexOf(str);
            if (j == -1) {
              str = "Categorias";
              j = ln.indexOf(str);
              pos = 12;
            }
            if (j >= 0) {
              det = 0;
              ln2 = "";
              dep = ln.substring(j + pos, ln.length()).trim();

              //System.out.println("dep="+dep);
              if (can == 0 && !dep.equals(depA)) {
                depA = dep;
                cx = Integer.toString(cc);
                if (cx.length() == 1) {
                  cx = "0" + cx;
                }

                dep = cx + "-" + dep;
                System.out.println("dep=" + dep);
                if (dep.length() > 40) {
                  dep = dep.substring(0, 40);
                }
                jMsg.setText("- Cargando ( " + dep + " )");
                // Actualiza tabla departamento
                String sql = "delete from departamento where dep='" + dep + "'";
                st.execute(sql);

                sql = "INSERT INTO departamento VALUES ("
                        + "'" + dep + "','" + dep + "','','')";
                st.execute(sql);
                //sql=INSERT INTO departamento VALUES ('01-BOMBILLOS','01-BOMBILLOS','','')
                System.out.println("sql=" + sql);
                cc++;
              } else {
                dep = cx + "-" + dep;
                if (dep.length() > 50) {
                  dep = dep.substring(0, 50);
                }
              }
            }

            // Detalle
            if (ln.indexOf("Código") >= 0) {
              det = 1;
            }

            if (det == 1) {
              if (ln.indexOf("Código") == -1 && ln.indexOf("Registros") == -1) {

                // Valida campos Existencia
                exi = "";
                int l = ln.length();
                int c = l;
                while (c >= 1) {
                  if (ln.substring(c - 1, c).equals(" ")) {
                    exi = ln.substring(c, l).trim();
                    c--;
                    break;
                  }
                  c--;
                }
                // valida existencia numerica
                exi = exi.replace(".", "").replace(",", "").replace("-", "").trim();
                if (!isNumeric(exi)) {
                  i++;
                  if (i < tk) {
                    ln2 = tokentx[i].trim();
                  }
                  ln = ln.replace(",", ".");
                  ln = ln + " " + ln2;   // Une registro siguiente con anterior
                } else {
                  //precio numerico
                  String lx = ln.substring(0, c);
                  prc = "";
                  l = lx.length();
                  c = l;
                  while (c >= 1) {
                    if (ln.substring(c - 1, c).equals(" ")) {
                      prc = ln.substring(c, l).trim();
                      c--;
                      break;
                    }
                    c--;
                  }
                  // prc numerico
                  prc = prc.replace(".", "").replace(",", "").replace("-", "").trim();
                  if (!isNumeric(prc)) {
                    i++;
                    if (i < tk) {
                      ln2 = tokentx[i].trim();
                    }
                    ln = ln.replace(",", ".");
                    ln = ln + " " + ln2;    // Une registro siguiente con anterior
                  }
                }

                ln = ln.replace("'", "");

                // Campo Existencia
                exi = "";
                l = ln.length();
                c = ln.length();
                while (c >= 1) {
                  if (ln.substring(c - 1, c).equals(" ")) {
                    exi = ln.substring(c, l).trim();
                    c--;
                    break;
                  }
                  c--;
                }
                exi = exi.replace(".", "");
                exi = exi.replace(",", "");

                // Valida existencia sea numerico
                if (isNumeric(exi.replace("-", ""))) {

                  // Campo Precio
                  l = c;
                  prc = "";
                  while (c >= 1) {
                    if (ln.substring(c - 1, c).equals(" ")) {
                      prc = ln.substring(c, l).trim();
                      c--;
                      break;
                    }
                    c--;
                  }
                  if (prc.length() > 3) {
                    String dec = prc.substring(prc.length() - 3, prc.length());
                    //System.out.println("dec=" + dec);
                    if (dec.indexOf(",") >= 0) {
                      prc = prc.replace(".", "");
                      prc = prc.replace(",", ".");
                    } else {
                      prc = prc.replace(",", "");
                    }
                  }

                  // Valida precio sea numerico
                  if (isNumeric(prc.replace("-", ""))) {

                    // Campo Codigo Produto
                    j = ln.indexOf(" ");
                    if (j >= 0) {

                      cop = ln.substring(0, j);

                      // Campo Descripcion Producto
                      l = c;
                      if (l >= j) {
                        nom = ln.substring(j, l).trim();
                        nom = nom.replace("'", "").replace(",", ".");
                        pum = "0";
                        pud = "0";

                        // Precio mayor
                        if (tip == 0) {
                          pum = prc;
                          pud = "0";
                        } else {
                          // Precio detal
                          pud = prc;
                          pum = "0";
                        }

                        if (dep.length() > 50) {
                          dep = dep.substring(0, 50);
                        }
                        if (cop.length() > 10) {
                          cop = cop.substring(0, 10);
                        }
                        if (nom.length() > 60) {
                          nom = nom.substring(0, 60);
                        }

                        //jMsg.setText("Producto ( " + cop + " )");
                        //pud="0";
                        // Crear registro
                        if (can == 0) {
                          if (dep.length() > 0) {
                            cof = cop.replace("-", "");
                            String sql = "INSERT INTO listaprecios VALUES ("
                                    + "'" + dep + "',"
                                    + "'" + cop + "',"
                                    + "'" + cof + "',"
                                    + "'" + nom + "',"
                                    + "" + pum + ","
                                    + "" + pud + ","
                                    + "" + exi + ","
                                    + "'0',"
                                    + "'0',"
                                    + "'0',"
                                    + "'" + fre + "'"
                                    + ")";
                            st.execute(sql);
                          }
                        } else {
                          // Actualiza precios
                          String sql = "";
                          if (tip == 0) {
                            sql = "update listaprecios "
                                    + "set pum=" + prc + " "
                                    + "where cop='" + cop + "'";
                            // Precio mayor
                            // jMsg.setText("Precio - " + cop);
                          } else {
                            // jMsg.setText("Precio - " + cop);
                            // Precio Detal
                            sql = "update listaprecios "
                                    + "set pud=" + prc + " "
                                    + "where cop='" + cop + "'";
                          }
                          st.execute(sql);
                        }

                        // Estatus inactivo
                        String sql = "update listaprecios "
                                + "set sta='1' "
                                + "where pum=0 "
                                + "  and pud=0";
                        st.execute(sql);

                        // Elimina inactivos
                        sql = "delete from  listaprecios "
                                + "where sta='1'";
                        //st.execute(sql);

                        /*
                                // Actualiza Comentarios Departamentos
   
                         + "dep VARCHAR(50)," // Departamento
                         + "nom VARCHAR(50)," // Nombre Nuevo Departamento
                         + "ob1 VARCHAR(80)," // observacion 1
                         + "ob2 VARCHAR(80))"; // observacion 2
         
                         */
                        // Actualiza comentarios Departamento
                        sql = "update Departamento "
                                + "set nom = ( select nom from DepartamentoT where Departamento.dep=DepartamentoT.dep "
                                + " order by fel desc FETCH FIRST 1 ROWS ONLY ),"
                                + "     ob1 = ( select ob1 from DepartamentoT where Departamento.dep=DepartamentoT.dep "
                                + " order by fel desc FETCH FIRST 1 ROWS ONLY ),"
                                + "     ob2 = ( select ob2 from DepartamentoT where Departamento.dep=DepartamentoT.dep "
                                + " order by fel desc FETCH FIRST 1 ROWS ONLY )";
                        st.execute(sql);

                      }

                    }

                  } else {
                    System.out.println("precio no numerioc");
                  }

                } else {
                  System.out.println("Existencia no numerioc");
                }

              }
            }

            //int x = 1;
          }
        }

        jMsg.setText("Lista Precios Cargada!");
        con.commit();
        con.close(); // cerramos la conexion  

      } catch (IOException ex) {
        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
      }
    } catch (SQLException ex) {
      Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void busParametros() {

    VecPar = new String[16];
    Parametros p = new Parametros();
    ObservableList<Parametros> obsParam = p.getBuscaParametros();
    for (Parametros par : obsParam) {
      VecPar[0] = par.getP01().trim();
      VecPar[1] = par.getP02().trim();
      VecPar[2] = par.getP03().trim();
      VecPar[3] = par.getP04().trim();
      VecPar[4] = par.getP05().trim();
      VecPar[5] = par.getP06().trim();
      VecPar[6] = par.getP07().trim();
      VecPar[7] = par.getP08().trim();
      VecPar[8] = par.getP09().trim();
      VecPar[9] = par.getP10().trim();
      VecPar[10] = par.getP11().trim();
      VecPar[11] = par.getP12().trim();
      VecPar[12] = par.getP13().trim();
      VecPar[13] = par.getP14().trim();
      VecPar[14] = par.getP15().trim();
      VecPar[15] = par.getP16().trim();
    }

    //Lista Parametros
    //for (int i = 0; i < VecPar.length; i++) {
    //  System.out.println("i=" + i + "," + VecPar[i].trim());
    //}

    /*
    String vax = "par/parametros.txt";
    File txt = new File(vax);
    if (!txt.exists()) {
      icon = new ImageIcon("img/param.png");
      vax = "No existe archivo parametros.txt en " + txt;
      String[] options = {"Salir"};
      JOptionPane.showOptionDialog(null, vax, "** Aviso **", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[0]);

    } else {
      try {
        BufferedReader Flee = new BufferedReader(new InputStreamReader(new FileInputStream(txt), "ISO-8859-1"));
        int i = 0;
        String Slinea;
        while ((Slinea = Flee.readLine()) != null) {
          //System.out.println("Slinea ="+Slinea );
          VecPar = Arrays.copyOf(VecPar, i + 1);
          VecPar[i] = Slinea.substring(21, Slinea.length()).trim();
          i++;
        }
        Flee.close();
      } catch (FileNotFoundException ex) {
      } catch (IOException ex) {
      }
    }
     */
  }

  // Pdf Cargados (Mayor/Detal)
  public void pdfsCargados() {
    pdfmay = "N";
    pdfdet = "N";
    double pum = 0, pud = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT pum,pud from listaprecios "
              + "where sta='0' and (pum<>0 or pud<>0) FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);

      while (rs.next()) {
        pum = rs.getDouble("pum");
        pud = rs.getDouble("pud");
        if (pum > 0) {
          pdfmay = "S";
        }
        if (pud > 0) {
          pdfdet = "S";
        }
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
  }

  //  Lista SQL
  public void listaSql() {
    String dex = "";
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT dep from departamento "
              + "order by dep ";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        dex = rs.getString("dep");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
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
      java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        new Menu("JL").setVisible(true);
      }
    });
  }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Jfecd;
    private javax.swing.JButton btnCal;
    public static javax.swing.JButton btnCar;
    public static javax.swing.JButton btnCge;
    private javax.swing.JButton btnCop;
    public static javax.swing.JButton btnCpm;
    private javax.swing.JButton btnCte;
    private javax.swing.JButton btnImp;
    private javax.swing.JButton btnNcr;
    private javax.swing.JButton btnNen;
    private javax.swing.JButton btnPag;
    private javax.swing.JButton btnPed;
    public static javax.swing.JButton btnPrd;
    private javax.swing.JButton btnSalir;
    public static javax.swing.JButton btnSdo;
    public static javax.swing.JButton btnSdo1;
    private javax.swing.JButton btnVen;
    private javax.swing.JButton btnVja;
    private elaprendiz.gui.varios.ClockDigital clockDigital1;
    private javax.swing.JLabel img;
    public static javax.swing.JLabel jAct;
    private javax.swing.JLabel jLabCia;
    private javax.swing.JLabel jLabCon;
    private javax.swing.JLabel jLabel1;
    public static javax.swing.JLabel jMsg;
    private javax.swing.JLabel jParam1;
    private javax.swing.JPasswordField jPass;
    public static javax.swing.JLabel jrutDoc;
    public static javax.swing.JLabel jrutExc;
    private javax.swing.JLabel labExc;
    private javax.swing.JLabel labFec;
    private javax.swing.JLabel labPdf;
    private javax.swing.JLabel labPdf0;
    private javax.swing.JLabel labVer;
    // End of variables declaration//GEN-END:variables
}
