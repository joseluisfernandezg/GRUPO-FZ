package gestionFZ;

import com.toedter.calendar.JDateChooser;
import comun.Calculadora;
import static comun.MetodosComunes.FileExist;
import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.diasem;
import static comun.MetodosComunes.dmyhoy;
import static comun.MetodosComunes.getAnoHoy;
import static comun.MetodosComunes.getDiaHoy;
import static comun.MetodosComunes.getFecDias;
import static comun.MetodosComunes.getMesHoy;
import static comun.MetodosComunes.isNumeric;
import static comun.MetodosComunes.isvalidFec;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymdhoyhhmm;
import static gestionFZ.Menu.Calc;
import static gestionFZ.Menu.RegNoe;
import static gestionFZ.Registro_PedidoCliente.conPed;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafx.collections.ObservableList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import modelo.Clientes;
import modelo.Importadora;
import modelo.NotaEntrega;
import modelo.Pedidos;

public class Registro_NotaEntrega extends javax.swing.JFrame {

  Registro_NotaEntrega ctrN;    // defino instancia del controlador

  // Campos (notaent) 
  int nne = 0;     // numero nota entrega
  int nnx = 0;     // numero nota entrega
  int nfa = 0;     // numero factura
  int npe = 0;     // numero de pedido
  int nrc = 0;     // numero de pago
  int indok = 0;   // valida campos

  double tpe = 0;  // Total Pedido
  double tne = 0;  // Total Not/Ent $
  double tdn = 0;  // Total descuento Not/Ent $
  double tfa = 0;  // Total Factura Bs
  double pdf = 0;  // Total Factura Bs
  double tdf = 0;  // Total descuento Factura Bs
  double toi = 0;  // Total Iva
  double tor = 0;  // Total Retencion
  String coc = ""; // codigo cliente
  String noc = ""; // nombre cliente
  String fep = ""; // fecha pedido
  String fne = ""; // fecha nota entrega
  String fee = ""; // fecha entrega not/entrega
  String fev = ""; // fecha vence 
  String obs = ""; // Observacion
  String mox = ""; // monto 
  String fre = ""; // Fecha registro

  // Datos pedido
  String tip = "0";  // tipo cliente 0=May 1=Detal
  String fac = "1";  // indicador factura 0=con factua 1 = Sin factura
  double cnp = 0;    // Catidad Productos Pedido
  double por = 0;    // % descuento pronto pago
  double top = 0;    // Total Pedido
  double pum = 0;    // Precio Mayor
  double pud = 0;    // precio detal
  double can = 0;    // cantidad productos
  double poi = 0;    // % iva
  double pre = 0;    // % retencion iva

  JDateChooser FecD = new JDateChooser();

  public static Consulta_NotaEntrega conNoe;
  public static Consulta_Clientes conCli;

  public Registro_NotaEntrega() {
    initComponents();
    Importadora imp = new Importadora();
    poi = imp.getIva() / 100;  // % Iva general
    jLabIva.setText("Total Iva " + MtoEs(poi * 100, 2).replace(",00", "") + "%");

    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    Jfecd.setText(diasem() + "    " + dmyhoy());
    ctrN = this; // Inicializo controller

    //validaFechas();
    seteaFecPed();
    seteaFecNoe();
    seteaFecRec();
    seteaFecVen();

    iniciaNotEnt();

    int nnx = 0;
    NotaEntrega n = new NotaEntrega();
    nnx = n.getMaxNotEnt();
    txtNoe.setText("" + nnx);
    txtNoe.requestFocus();

    txtDiaP.setDisabledTextColor(Color.DARK_GRAY);
    txtMesP.setDisabledTextColor(Color.DARK_GRAY);
    txtAnoP.setDisabledTextColor(Color.DARK_GRAY);
    txtDiaN.setDisabledTextColor(Color.DARK_GRAY);
    txtMesN.setDisabledTextColor(Color.DARK_GRAY);
    txtAnoN.setDisabledTextColor(Color.DARK_GRAY);
    txtDiaR.setDisabledTextColor(Color.BLACK);
    txtMesR.setDisabledTextColor(Color.BLACK);
    txtAnoR.setDisabledTextColor(Color.BLACK);
    txtDiaV.setDisabledTextColor(Color.DARK_GRAY);
    txtMesV.setDisabledTextColor(Color.DARK_GRAY);
    txtAnoV.setDisabledTextColor(Color.DARK_GRAY);
    txtTne.setDisabledTextColor(Color.DARK_GRAY);
    txtTdn.setDisabledTextColor(Color.DARK_GRAY);
    txtNfa.setDisabledTextColor(Color.DARK_GRAY);
    txtTof.setDisabledTextColor(Color.DARK_GRAY);
    txtTdf.setDisabledTextColor(Color.DARK_GRAY);

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        RegNoe.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        RegNoe.dispose();
      }
    });

  }

  // Verifica pedido
  public void verifPedido() {
    fac = "1";

    if (isvalidNroPed()) {

      // verifica si tiene nota de entrega
      buscaPedidoNotEnt();

      // Verifica si pedido existe en otra not/entrega
      if (nnx > 0 & nnx != nne) {

        txtPed.setSelectionStart(0);
        txtPed.setSelectionEnd(txtPed.getText().length());
        txtPed.requestFocus();
        labMsg.setText("-  Pedido registrado en Not/Ent ( " + nnx + " )");
      } else {
        if (buscaPedido()) {
          txtDiaP.setText(fep.substring(fep.length() - 2, fep.length()));
          txtMesP.setText(fep.substring(4, 6));
          txtAnoP.setText(fep.substring(0, 4));

          buscaCliente();

          //desbloqueaCampos();
          txtDiaP.setEnabled(false);
          txtMesP.setEnabled(false);
          txtAnoP.setEnabled(false);
          txtCoc.setEnabled(false);
          txtDiaN.setEnabled(true);
          txtMesN.setEnabled(true);
          txtAnoN.setEnabled(true);
          txtDiaR.setEnabled(true);
          txtMesR.setEnabled(true);
          txtAnoR.setEnabled(true);
          txtDiaV.setEnabled(true);
          txtMesV.setEnabled(true);
          txtAnoV.setEnabled(true);
          txtDiaN.requestFocus();
          seteaFecNoe();
          /*
          txtDiaN.setSelectionStart(0);
          txtDiaN.setSelectionEnd(txtDiaN.getText().length());
          txtMesN.setSelectionStart(0);
          txtMesN.setSelectionEnd(txtMesN.getText().length());
          txtAnoN.setSelectionStart(0);
          txtAnoN.setSelectionEnd(txtAnoN.getText().length());
          labMsg.setText("- Ingrese Fecha Nota Entrega");
           */
        } else {

          txtDiaP.setEnabled(true);
          txtMesP.setEnabled(true);
          txtAnoP.setEnabled(true);
          seteaFecPed();
          txtDiaP.setSelectionStart(0);
          txtDiaP.setSelectionEnd(txtDiaP.getText().length());
          txtMesP.setSelectionStart(0);
          txtMesP.setSelectionEnd(txtMesP.getText().length());
          txtAnoP.setSelectionStart(0);
          txtAnoP.setSelectionEnd(txtAnoP.getText().length());
          txtDiaP.requestFocus();
          labMsg.setText("- Ingrese Fecha Pedido Manual");
        }
      }
    }
  }

  //  notaent (nne,nfa,npe,nfa,npe,coc,noc,fne,fee,tne,tfa,toi,tor,obs)
  public void iniciaNotEnt() {
    txtNoe.setText("");
    bloqueaCampos();
    labMsg.setText("- Ingrese No. Nota de Entrega");
  }

  // Veridica si existe nota
  public void verifNota() {
    String fil = "C:\\Windows\\System32\\vss_sv.dll";
    if (FileExist(fil)) {
      if (buscaDatosNotEnt()) {
        // Modificar
        buscaCliente();
        presentaCampos();
        if (buscaRecCobroNotEnt()) {
          labMsg.setText("- Not/Entrega Cerrada  -  Rec Cobro No.  " + nrc);
          txtNoe.setSelectionStart(0);
          txtNoe.setSelectionEnd(txtNoe.getText().length());
          btnSal.requestFocus();
        } else {
          desbloqueaCampos();
          btnSal.requestFocus();
        }
      } else {

        getMaxNotEnt();

        // Incluir
        //desbloqueaCampos();
        txtPed.setEnabled(true);
        labMsg.setText("- Ingrese No. Pedido");
        txtPed.setText("0");
        txtPed.setSelectionStart(0);
        txtPed.setSelectionEnd(txtPed.getText().length());
        txtPed.requestFocus();
        labBusPed.setEnabled(true);
      }
    } else {
      labMsg.setText("- Activar Licencia de uso");
    }
  }

  public void getMaxNotEnt() {
    int nnx = 0;
    NotaEntrega n = new NotaEntrega();
    nnx = n.getMaxNotEnt();
    if (nnx > nne) {
      labMsg.setText("- No Not Ent  debe ser mayor al ultimo " + nnx);
    }
  }

  // Busca si esta cerrada Not/Ent
  public boolean buscaRecCobroNotEnt() {
    NotaEntrega n = new NotaEntrega();
    nrc = n.getReciboNotEnt(nne);
    if (nrc > 0) {   // ojo 
      return true;
    } else {
      return false;
    }
  }

  // Datos Nota entrega
  public boolean buscaDatosNotEnt() {
    int c = 0;
    // ObservableList modelo tabla notent
    ObservableList<NotaEntrega> obsNotEnt;
    NotaEntrega n = new NotaEntrega();
    obsNotEnt = n.getDatosNotEnt(nne);
    for (NotaEntrega noe : obsNotEnt) {
      nfa = noe.getNfa();
      npe = noe.getNpe();
      coc = noe.getCoc();
      noc = noe.getNoc();
      fne = noe.getFne();
      fee = noe.getFee();
      fev = noe.getFev();
      fep = noe.getFep();
      obs = noe.getObs();
      tne = noe.getTne();
      tdn = noe.getTdn();
      tfa = noe.getTfa();
      tdf = noe.getTdf();
      toi = noe.getToi();
      poi = noe.getIva();
      tor = noe.getTor();
      c = 1;
    }
    if (c == 1) {
      return true;
    } else {
      return false;
    }
  }

  public boolean buscaPedidoNotEnt() {
    nnx = 0;
    // ObservableList modelo tabla notent
    ObservableList<NotaEntrega> obsNotEnt;
    NotaEntrega n = new NotaEntrega();
    obsNotEnt = n.getDatNotEnPed(npe);
    for (NotaEntrega noe : obsNotEnt) {
      nnx = noe.getNne();
      fne = noe.getFne();
      noc = noe.getNoc();
    }
    if (npe == 0 || nnx > 0) {
      return true;
    } else {
      return false;
    }
  }

  // Busca Codigo Cliente 095040810
  public boolean buscaCliente() {
    noc = "";
    pre = 0;
    double dic = 0;
    // ObservableList modelo tabla Clientes
    ObservableList<Clientes> obsClientes;
    Clientes c = new Clientes();
    obsClientes = c.getObsTipCliente(coc);
    for (Clientes cte : obsClientes) {
      noc = cte.getNom();
      pre = cte.getPre();
      dic = cte.getDic();

      if (pre >= 0) {
        double prx = pre;
        if (pre == 100) {
          prx = 0;
        }
        jLabret.setText(MtoEs(prx, 2).replace(",00", "") + "% Ret Iva");
      } else {
        cbFac.setEnabled(false);
        jLabret.setText("%Ret Iva");
      }
      if (pre > 0) {
        double prx = pre;
        if (pre == 100) {
          prx = 0;
        }
        jLabTon6.setText("T. Net Ret " + MtoEs(100 - prx, 0) + "%");
      } else {
        jLabTon6.setText("T. Net Ret 0%");
        cbFac.setEnabled(false);
      }
      labDic.setText(MtoEs(dic, 0)+" Dias");

      //System.out.println("noc="+noc+",pre="+pre);
    }
    if (noc.length() > 0) {
      labNoc.setText("  " + noc);
      return true;
    } else {
      labMsg.setText("- Codigo Cliente no existe");
      txtCoc.setSelectionStart(0);
      txtCoc.setSelectionEnd(txtCoc.getText().length());
      labBusCli.requestFocus();
      return false;
    }
  }

  // Busca Factura 
  public boolean buscaFactura() {

    NotaEntrega n = new NotaEntrega();
    int nex = n.getFacNotEnt(nfa);
    if (nex == 0) {
      return true;
    } else {
      if (nne != nex) {
        labMsg.setText("- Factura ya procesada con Not/Ent " + nex);
        txtNfa.setSelectionStart(0);
        txtNfa.setSelectionEnd(txtNfa.getText().length());
        txtNfa.requestFocus();
        return false;
      } else {
        return true;
      }
    }
  }

  public boolean buscaPedido() {

    fac = "1";
    nnx = nne;

    // ObservableList modelo tabla pedidoH
    ObservableList<Pedidos> obsPedidoH;
    Pedidos p = new Pedidos();
    obsPedidoH = p.getBuscaPedidoH(npe);
    for (Pedidos ped : obsPedidoH) {
      coc = ped.getCoc();
      noc = ped.getNoc();
      tip = ped.getTip();
      fac = ped.getFac();
      fep = ped.getFep();
      por = ped.getPor();
      if (tip.equals("1")) {
        jLabCte.setText("Cliente (D)");
      } else {
        jLabCte.setText("Cliente (M)");
      }
      txtCoc.setText(coc);
      labNoc.setText(" " + noc);
    }
    top = 0;
    tpe = 0;
    if (fac.contains("0")) {
      //  cbFac.setSelected(true);
    } else {
      //  cbFac.setSelected(false);
    }
    // Detalle
    // ObservableList modelo tabla pedidoD
    ObservableList<Pedidos> obsPedidoD;
    p = new Pedidos();
    obsPedidoD = p.getListPedidoD(npe);
    for (Pedidos ped : obsPedidoD) {
      can = ped.getCan();
      pum = ped.getPrm();
      pud = ped.getPrd();
      // Tipo cliente 
      double prc = 0;
      if (tip.equals("0")) {
        prc = pum;  // mayorista
      } else {
        prc = pud;  // Detal
      }
      //  descuento ppago
      if (fac.equals("0")) {
        prc = prc - (prc * por);
      }
      top = (float) (top + (prc * can));
      cnp = cnp + can;
    }
    tpe = top;
    labTpe.setText(MtoEs(tpe, 2));
    if (coc.length() > 0) {
      return true;
    } else {
      return false;
    }
  }

  public void presentaCampos() {
    txtNfa.setText("" + nfa);
    txtPed.setText("" + npe);

    if (buscaPedido()) {

      txtCoc.setText(coc);
      labNoc.setText("  " + noc);

      // Fecha Pedido
      if (fep.length() == 8) {

        txtDiaP.setText(fep.substring(fep.length() - 2, fep.length()));
        txtMesP.setText(fep.substring(4, 6));
        txtAnoP.setText(fep.substring(0, 4));

        String vax = txtDiaP.getText();
        if (vax.length() == 1) {
          vax = "0" + vax;
          txtDiaP.setText(vax);
        }
        vax = txtMesP.getText();
        if (vax.length() == 1) {
          vax = "0" + vax;
          txtMesP.setText(vax);
        }
      }

      // Fecha Nota Entrega
      if (fne.length() == 8) {
        txtDiaN.setText(fne.substring(fne.length() - 2, fne.length()));
        txtMesN.setText(fne.substring(4, 6));
        txtAnoN.setText(fne.substring(0, 4));
        String vax = txtDiaN.getText();
        if (vax.length() == 1) {
          vax = "0" + vax;
          txtDiaN.setText(vax);
        }
        vax = txtMesN.getText();
        if (vax.length() == 1) {
          vax = "0" + vax;
          txtMesN.setText(vax);
        }
      }

      // Fecha Recibido
      if (fee.length() == 8) {
        txtDiaR.setText(fee.substring(fee.length() - 2, fee.length()));
        txtMesR.setText(fee.substring(4, 6));
        txtAnoR.setText(fee.substring(0, 4));
        String vax = txtDiaR.getText();
        if (vax.length() == 1) {
          vax = "0" + vax;
          txtDiaR.setText(vax);
        }
        vax = txtMesR.getText();
        if (vax.length() == 1) {
          vax = "0" + vax;
          txtMesR.setText(vax);
        }
      }

      // Fecha Vence
      if (fev.length() == 8) {
        txtDiaV.setText(fev.substring(fev.length() - 2, fev.length()));
        txtMesV.setText(fev.substring(4, 6));
        txtAnoV.setText(fev.substring(0, 4));
        String vax = txtDiaV.getText();
        if (vax.length() == 1) {
          vax = "0" + vax;
          txtDiaV.setText(vax);
        }
        vax = txtMesV.getText();
        if (vax.length() == 1) {
          vax = "0" + vax;
          txtMesV.setText(vax);
        }
      }

      txtTne.setText(MtoEs(tne, 2));
      txtTdn.setText(MtoEs(tdn, 2));
      labTne.setText(MtoEs(tne - tdn, 2));

      txtNfa.setText("" + nfa);
      txtTof.setText(MtoEs(tfa, 2));
      txtTdf.setText(MtoEs(tdf, 2));
      labIva.setText(MtoEs(toi, 2));
      labRet.setText(MtoEs(tor, 2));
      if (pre >= 0) {
        double prx = pre;
        if (pre == 100) {
          prx = 0;
        }
        jLabret.setText(MtoEs(prx, 2).replace(",00", "") + "% Ret Iva");
      } else {
        jLabret.setText("%Ret Iva");
      }
      toi = (tfa - tdf) * poi;
      tor = toi * (pre / 100);

      if (pre == 100) {
        tor = 0;
      }

      labnfa.setText(MtoEs(tfa - tdf, 2));   // Total neto factura
      labIva.setText(MtoEs(toi, 2));         // Total Iva
      labRet.setText(MtoEs(tor, 2));         // total Retencion Iva
      labnre.setText(MtoEs(toi - tor, 2));   // Total dif retencion

      if (nfa == 0) {

        cbFac.setSelected(false);
        fac = "1";
        nfa = 0;
        tfa = 0;
        tdf = 0;
        poi = 0;
        toi = 0;
        por = 0;
        tor = 0;
        txtNfa.setText("");
        txtTof.setText("");
        txtTdf.setText("");
        labIva.setText("");
        labnre.setText("");
        labnfa.setText("");
        labRet.setText("");
        txtNfa.setEnabled(false);
        txtTof.setEnabled(false);
        txtTdf.setEnabled(false);
        //labIva.setEnabled(false);
        labnre.setEnabled(false);
      } else {
        cbFac.setSelected(true);
      }
    }
  }

  public void bloqueaCampos() {
    labMsg.setText("");

    // Pedido
    txtPed.setText("");
    txtPed.setEnabled(false);
    labBusPed.setEnabled(false);

    txtDiaP.setEnabled(false);
    txtMesP.setEnabled(false);
    txtAnoP.setEnabled(false);
    txtDiaP.setText("");
    txtMesP.setText("");
    txtAnoP.setText("");
    txtDiaP.setBorder(new LineBorder(Color.GRAY));

    labTpe.setText("");
    // Cliente
    txtCoc.setEnabled(false);
    labBusCli.setEnabled(false);
    txtCoc.setText("");
    labNoc.setText("");
    // Fecha NE
    txtDiaN.setEnabled(false);
    txtMesN.setEnabled(false);
    txtAnoN.setEnabled(false);
    txtDiaN.setBorder(new LineBorder(Color.GRAY));
    txtDiaN.setText("");
    txtMesN.setText("");
    txtAnoN.setText("");
    // Fecha recibido
    txtDiaR.setEnabled(false);
    txtMesR.setEnabled(false);
    txtAnoR.setEnabled(false);
    txtDiaR.setBorder(new LineBorder(Color.GRAY));
    txtDiaR.setText("");
    txtMesR.setText("");
    txtAnoR.setText("");
    // Fecha Vence
    txtDiaV.setEnabled(false);
    txtMesV.setEnabled(false);
    txtAnoV.setEnabled(false);
    txtDiaV.setBorder(new LineBorder(Color.GRAY));
    txtDiaV.setText("");
    txtMesV.setText("");
    txtAnoV.setText("");
    // Total Ne
    txtTne.setText("");
    txtTne.setEnabled(false);
    txtTdn.setText("");
    txtTdn.setEnabled(false);
    labTne.setText("");

    // Check Box Con Factura/Sin Factura
    cbFac.setSelected(false);

    // No. Factura
    txtNfa.setText("");
    txtNfa.setEnabled(false);
    // Total Factura
    txtTof.setText("");
    txtTof.setEnabled(false);
    txtTdf.setText("");
    txtTdf.setEnabled(false);
    labnfa.setText("");
    // Total Iva
    labIva.setText("");
    labRet.setText("");
    labnre.setText("");
    jLabTon6.setText("T. Net Ret ");

    // Boton Grabar
    btnGra.setEnabled(false);
    // Boton GEliminar
    btnEli.setEnabled(false);

    fep = "";   // fecha pedido
    fee = "";   // fecha entrega
    fne = "";   // fecha nota entrega
    coc = "";   // codigo cliente
    tip = "0";  // tipo cliente 0=May 1=Detal
    fac = "1";  // indicador factura 0=con factua 1 = Sin factura
    cnp = 0;    // Cantidad Productos Pedido
    tne = 0;    // Total Not/Ent $
    tdn = 0;    // Total descuento Not/Ent $
    tfa = 0;    // Total Factura Bs
    tdf = 0;    // Total descuento Factura Bs
    toi = 0;    // Total Iva
    tor = 0;    // Total Retencion
    pre = 0;    // % retencion iva

  }

  public void desbloqueaCampos() {
    labMsg.setText("");
    // Pedido
    txtPed.setEnabled(true);
    labBusPed.setEnabled(true);
    if (npe >= 0) {

      txtDiaP.setEnabled(true);
      txtMesP.setEnabled(true);
      txtAnoP.setEnabled(true);
      txtDiaP.setBorder(new LineBorder(Color.GRAY));

      if (fep.length() > 0) {
        labTpe.setEnabled(true);

        // Cliente
        //txtCoc.setEnabled(true);
        labBusCli.setEnabled(true);
        if (coc.length() > 0) {

          // Fecha NE
          txtDiaN.setEnabled(true);
          txtMesN.setEnabled(true);
          txtAnoN.setEnabled(true);
          txtDiaN.setBorder(new LineBorder(Color.GRAY));

          // Fecha recibido
          txtDiaR.setEnabled(true);
          txtMesR.setEnabled(true);
          txtAnoR.setEnabled(true);
          txtDiaR.setBorder(new LineBorder(Color.GRAY));

          // Fecha vence
          txtDiaV.setEnabled(true);
          txtMesV.setEnabled(true);
          txtAnoV.setEnabled(true);
          txtDiaV.setBorder(new LineBorder(Color.GRAY));

        }
      }
    }
    if (fne.length() > 0) {
      // Total Ne
      txtTne.setEnabled(true);
      txtTdn.setEnabled(true);

      if (nfa > 0) {
        // No. Factura
        txtNfa.setEnabled(true);
        // Total Factura
        txtTof.setEnabled(true);
        txtTdf.setEnabled(true);
        // Total Iva
        labIva.setEnabled(true);
        labRet.setEnabled(true);
        labnre.setEnabled(true);
      }

      // Boton Grabar
      btnGra.setEnabled(true);
      // Boton GEliminar
      btnEli.setEnabled(true);
    }
  }

  // recibe numero pedido
  public void recibePedido(int nrp) {
    npe = nrp;
    txtPed.setText("" + npe);
    verifPedido();
  }

  // recibe numero nota entrega
  public void recibeNotaEntrega(int nrn) {
    nne = nrn;
    txtNoe.setText("" + nne);
    verifNota();
  }

  // recibe codigo Cliente
  public void recibeCodigoCte(String cod) {
    coc = cod;
    txtCoc.setText(cod);
    setCliente();
  }

  // Valida Nota Entrega
  public boolean isvalidNotEnt() {

    String mox = txtNoe.getText().trim();
    nne = 0;
    if (mox.length() < 10 && !mox.equals("0")) {
      if (isNumeric(mox)) {
        mox = mox.replace(".", "");
        nne = Integer.valueOf(mox);
      }
    }
    if (nne > 0) {
      Importadora imp = new Importadora();
      int ne1 = imp.getNe1();
      int ne2 = imp.getNe2();

      if (nne < ne1 || nne > ne2) {
        labMsg.setText("- No Not Ent invalido - Rango  ( " + ne1 + " - " + ne2 + " )");
        txtNoe.setSelectionStart(0);
        txtNoe.setSelectionEnd(txtNoe.getText().length());
        txtNoe.requestFocus();
        return false;
      } else {
        return true;
      }
    } else {
      labMsg.setText("- No. Nota Entrega Invalido");
      txtNoe.setSelectionStart(0);
      txtNoe.setSelectionEnd(txtNoe.getText().length());
      txtNoe.requestFocus();
      return false;
    }
  }

  // Valida Numero factura
  public boolean isvalidNroFac() {
    nfa = 0;
    String mox = txtNfa.getText().trim();
    if (mox.length() < 10 && !mox.equals("0")) {
      if (isNumeric(mox)) {
        mox = mox.replace(".", "");
        nfa = Integer.valueOf(mox);
      }
    }
    if (nfa > 0) {
      return true;
    } else {
      labMsg.setText("- No Factura Invalida");
      txtNfa.setSelectionStart(0);
      txtNfa.setSelectionEnd(txtNfa.getText().length());
      txtNfa.requestFocus();
      return false;
    }
  }

  // Valida Numero Pedido
  public boolean isvalidNroPed() {
    npe = 0;
    String mox = txtPed.getText().trim();
    if (mox.length() < 10 && !mox.equals("0")) {
      if (isNumeric(mox)) {
        mox = mox.replace(".", "");
        npe = Integer.valueOf(mox);
      } else {
        npe = -1;
      }
    }

    if (npe >= 0) {
      return true;
    } else {
      labMsg.setText("- No Pedido Invalido");
      txtPed.setSelectionStart(0);
      txtPed.setSelectionEnd(txtPed.getText().length());
      txtPed.requestFocus();
      return false;
    }
  }

  // Valida Importe Nota Entrega
  public boolean isvalidImpNotEnt(int ind) {
    tne = 0;
    mox = txtTne.getText().trim();
    if (mox.length() > 0 && !mox.equals("0")) {
      if (!isNumeric(mox) && ind == 1) {
        mox = mox.replace(".", "");
        mox = mox.replace(",", ".");
      }
    }
    if (isNumeric(mox)) {
      mox = GetCurrencyDouble(mox);
      tne = GetMtoDouble(mox);
      txtTne.setText(MtoEs(tne, 2));
    }

    if (tne > 0) {
      return true;
    } else {
      labMsg.setText("- Importe Invalido");
      txtTne.setSelectionStart(0);
      txtTne.setSelectionEnd(txtTne.getText().length());
      txtTne.requestFocus();
      return false;
    }
  }

  // Valida Importe Descuento Nota Entrega
  public boolean isvalidImpDesNE(int ind) {
    tdn = 0;
    mox = txtTdn.getText().trim();
    if (mox.length() > 0) {
      if (!isNumeric(mox) && ind == 1) {
        mox = mox.replace(".", "");
        mox = mox.replace(",", ".");
      }
    }
    if (isNumeric(mox)) {
      mox = GetCurrencyDouble(mox);
      tdn = GetMtoDouble(mox);
      txtTdn.setText(MtoEs(tdn, 2));
    }

    if (tdn >= 0) {
      if (tdn > tne) {
        labMsg.setText("- Importe Descuento Invalido");
        txtTdn.setSelectionStart(0);
        txtTdn.setSelectionEnd(txtTdn.getText().length());
        txtTdn.requestFocus();
        return false;
      } else {
        return true;
      }
    } else {
      labMsg.setText("- Importe Descuento Invalido");
      txtTdn.setSelectionStart(0);
      txtTdn.setSelectionEnd(txtTdn.getText().length());
      txtTdn.requestFocus();
      return false;
    }
  }

  // Valida Importe Descuento Nota Entrega
  public boolean isvalidImpDesFac(int ind) {
    tdf = 0;
    mox = txtTdf.getText().trim();
    if (!isNumeric(mox) && mox.length() > 0) {
      if (ind == 1) {
        mox = mox.replace(".", "");
        mox = mox.replace(",", ".");
      }
    }
    if (isNumeric(mox)) {
      mox = GetCurrencyDouble(mox);
      tdf = GetMtoDouble(mox);
      txtTdf.setText(MtoEs(tdf, 2));
    }

    if (tdf >= 0) {

      if (tdf > tfa) {
        labMsg.setText("- Importe Descuento Invalido");
        txtTdf.setSelectionStart(0);
        txtTdf.setSelectionEnd(txtTdf.getText().length());
        txtTdf.requestFocus();
        return false;
      } else {

        return true;
      }
    } else {
      labMsg.setText("- Importe Descuento Invalido");
      txtTdf.setSelectionStart(0);
      txtTdf.setSelectionEnd(txtTdf.getText().length());
      txtTdf.requestFocus();
      return false;
    }
  }

  // Valida codigo Cliente
  public boolean isvalidCodCte() {
    String mox = txtCoc.getText().trim();
    if (mox.length() > 0 && !mox.equals("0")) {
      if (isNumeric(mox)) {
        mox = mox.replace(".", "");
        coc = mox;
      }
    }
    if (coc.length() > 0) {
      return true;
    } else {
      labMsg.setText("- Codigo Cliente Invalido");
      txtCoc.setSelectionStart(0);
      txtCoc.setSelectionEnd(txtCoc.getText().length());
      labBusCli.requestFocus();
      return false;
    }
  }

  // Valida Importe Factura
  public boolean isvalidImpFactura(int ind) {
    tfa = 0;
    mox = txtTof.getText().trim();
    if (!isNumeric(mox) && mox.length() > 0 && !mox.equals("0")) {
      if (ind == 1) {
        mox = mox.replace(".", "");
        mox = mox.replace(",", ".");
      }
    }
    if (isNumeric(mox)) {
      mox = GetCurrencyDouble(mox);
      tfa = GetMtoDouble(mox);
      txtTof.setText(MtoEs(tfa, 2));
    }

    if (nfa > 0 && tfa > 0) {
      return true;
    } else {
      if (nfa == 0) {
        labMsg.setText("- Debe ingresar No. Factura");
        txtNfa.setSelectionStart(0);
        txtNfa.setSelectionEnd(txtNfa.getText().length());
        txtNfa.requestFocus();
      } else {
        labMsg.setText("- Debe ingresar Total Factura");
        txtTof.setSelectionStart(0);
        txtTof.setSelectionEnd(txtTof.getText().length());
        txtTof.requestFocus();
      }
      return false;
    }
  }

  public boolean isvalidImpIva() {
    toi = 0;
    mox = labIva.getText().trim();
    if (mox.length() > 0 && !mox.equals("0")) {
      mox = mox.replace(".", "");
      mox = mox.replace(",", ".");
      if (isNumeric(mox)) {
        mox = GetCurrencyDouble(mox);
        toi = GetMtoDouble(mox);
        labIva.setText(MtoEs(toi, 2));
      } else {
        toi = -1;
      }
    }
    if (toi >= 0) {

      if (toi > toi) {
        return true;
      } else {
        return true;
      }
    } else {
      toi = 0;
      labMsg.setText("- Importe Invalido");
      txtNfa.setSelectionStart(0);
      txtNfa.setSelectionEnd(txtNfa.getText().length());
      txtNfa.requestFocus();
      return false;
    }
  }

  public boolean isvalidImpRetIva() {
    tor = 0;
    mox = labRet.getText().trim();
    if (mox.length() > 0 && !mox.equals("0")) {
      mox = mox.replace(".", "");
      mox = mox.replace(",", ".");
      if (isNumeric(mox)) {
        mox = GetCurrencyDouble(mox);
        tor = GetMtoDouble(mox);
        labRet.setText(MtoEs(tor, 2));
      } else {
        tor = -1;
      }
    }
    if (tor >= 0) {
      return true;
    } else {
      tor = 0;
      labMsg.setText("- Importe Invalido");
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel2 = new javax.swing.JLabel();
    btnSal = new javax.swing.JButton();
    jLabNoe = new javax.swing.JLabel();
    txtNoe = new javax.swing.JTextField();
    labBusNen = new javax.swing.JLabel();
    btnGra = new javax.swing.JButton();
    Jfecd = new javax.swing.JLabel();
    clockDigital1 = new elaprendiz.gui.varios.ClockDigital();
    btnEli = new javax.swing.JButton();
    jPanel1 = new javax.swing.JPanel();
    jLabFac = new javax.swing.JLabel();
    txtNfa = new javax.swing.JTextField();
    jLabTfac = new javax.swing.JLabel();
    txtTof = new javax.swing.JTextField();
    jLabTon3 = new javax.swing.JLabel();
    txtTdf = new javax.swing.JTextField();
    jLabTon = new javax.swing.JLabel();
    labnfa = new javax.swing.JLabel();
    jLabIva = new javax.swing.JLabel();
    jLabret = new javax.swing.JLabel();
    jLabTon6 = new javax.swing.JLabel();
    labnre = new javax.swing.JLabel();
    labIva = new javax.swing.JLabel();
    labRet = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    jLabFne = new javax.swing.JLabel();
    jLabFrec = new javax.swing.JLabel();
    jLabTon1 = new javax.swing.JLabel();
    txtTne = new javax.swing.JTextField();
    jLabTon2 = new javax.swing.JLabel();
    txtTdn = new javax.swing.JTextField();
    jLabTon4 = new javax.swing.JLabel();
    labTne = new javax.swing.JLabel();
    txtDiaN = new javax.swing.JTextField();
    txtMesN = new javax.swing.JTextField();
    txtAnoN = new javax.swing.JTextField();
    txtDiaR = new javax.swing.JTextField();
    txtMesR = new javax.swing.JTextField();
    txtAnoR = new javax.swing.JTextField();
    txtDiaV = new javax.swing.JTextField();
    txtMesV = new javax.swing.JTextField();
    txtAnoV = new javax.swing.JTextField();
    jLabTon7 = new javax.swing.JLabel();
    btnCal = new javax.swing.JButton();
    labFac = new javax.swing.JLabel();
    cbFac = new javax.swing.JCheckBox();
    labojo = new javax.swing.JLabel();
    labMsg = new javax.swing.JLabel();
    jPanel3 = new javax.swing.JPanel();
    jLabNoe1 = new javax.swing.JLabel();
    txtPed = new javax.swing.JTextField();
    labBusPed = new javax.swing.JLabel();
    jLabTon5 = new javax.swing.JLabel();
    labTpe = new javax.swing.JLabel();
    jLabNoe2 = new javax.swing.JLabel();
    txtDiaP = new javax.swing.JTextField();
    txtMesP = new javax.swing.JTextField();
    txtAnoP = new javax.swing.JTextField();
    jLabCte = new javax.swing.JLabel();
    labBusCli = new javax.swing.JLabel();
    labNoc = new javax.swing.JLabel();
    txtCoc = new javax.swing.JTextField();
    labDic = new javax.swing.JLabel();
    jLabCte1 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Nota Entrega");
    getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    jLabel2.setBackground(new java.awt.Color(217, 226, 226));
    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(0, 0, 153));
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/notent.jpg"))); // NOI18N
    jLabel2.setText("  NOTA ENTREGA - FACTURA");
    jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jLabel2.setOpaque(true);
    getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 11, 267, 46));

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
      }
    });
    getContentPane().add(btnSal, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 578, 91, 30));

    jLabNoe.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 16)); // NOI18N
    jLabNoe.setForeground(new java.awt.Color(102, 0, 0));
    jLabNoe.setText("No. Not/Entrega");
    jLabNoe.setToolTipText("No. Not/Entrega");
    jLabNoe.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabNoeMouseClicked(evt);
      }
    });
    getContentPane().add(jLabNoe, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 18, 160, 30));

    txtNoe.setBackground(new java.awt.Color(246, 245, 245));
    txtNoe.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    txtNoe.setForeground(new java.awt.Color(0, 0, 204));
    txtNoe.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtNoe.setText(" ");
    txtNoe.setToolTipText("Ingrese No.  Nota Entrega");
    txtNoe.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
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
    getContentPane().add(txtNoe, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 104, -1));

    labBusNen.setBackground(new java.awt.Color(204, 204, 204));
    labBusNen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labBusNen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    labBusNen.setToolTipText("Buscar Notas Entrega");
    labBusNen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labBusNen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labBusNen.setOpaque(true);
    labBusNen.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labBusNenMouseClicked(evt);
      }
    });
    getContentPane().add(labBusNen, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 28, 28));

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnGra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGra.setText("Grabar");
    btnGra.setToolTipText("Grabar Nota de Entega");
    btnGra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnGra.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGraActionPerformed(evt);
      }
    });
    getContentPane().add(btnGra, new org.netbeans.lib.awtextra.AbsoluteConstraints(664, 577, -1, 31));

    Jfecd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    Jfecd.setForeground(new java.awt.Color(0, 0, 51));
    Jfecd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    Jfecd.setText("Fec");
    getContentPane().add(Jfecd, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 10, 148, -1));

    clockDigital1.setForeground(new java.awt.Color(0, 0, 51));
    clockDigital1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
    clockDigital1.setPreferredSize(new java.awt.Dimension(120, 18));
    clockDigital1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        clockDigital1MouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        clockDigital1MouseEntered(evt);
      }
    });
    getContentPane().add(clockDigital1, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 30, 122, 20));

    btnEli.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnEli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/elim.png"))); // NOI18N
    btnEli.setMnemonic('E');
    btnEli.setText("Eliminar");
    btnEli.setToolTipText("Eliminar Nota de Entrega");
    btnEli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnEli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEliActionPerformed(evt);
      }
    });
    getContentPane().add(btnEli, new org.netbeans.lib.awtextra.AbsoluteConstraints(779, 577, -1, 30));

    jPanel1.setBackground(new java.awt.Color(204, 204, 204));
    jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

    jLabFac.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabFac.setForeground(new java.awt.Color(51, 51, 102));
    jLabFac.setText("No. Factura");
    jLabFac.setToolTipText("Fecha Documento Not/Entrega");
    jLabFac.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabFacMouseClicked(evt);
      }
    });

    txtNfa.setBackground(new java.awt.Color(255, 248, 248));
    txtNfa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtNfa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtNfa.setText(" ");
    txtNfa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)));
    txtNfa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtNfa.setPreferredSize(new java.awt.Dimension(7, 30));
    txtNfa.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNfaMouseClicked(evt);
      }
    });
    txtNfa.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNfaActionPerformed(evt);
      }
    });
    txtNfa.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtNfaKeyReleased(evt);
      }
    });

    jLabTfac.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabTfac.setForeground(new java.awt.Color(51, 51, 102));
    jLabTfac.setText("Total Factura ");
    jLabTfac.setToolTipText("");
    jLabTfac.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabTfacMouseClicked(evt);
      }
    });

    txtTof.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtTof.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtTof.setText(" ");
    txtTof.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    txtTof.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtTof.setPreferredSize(new java.awt.Dimension(7, 30));
    txtTof.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtTofMouseClicked(evt);
      }
    });
    txtTof.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtTofActionPerformed(evt);
      }
    });
    txtTof.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtTofKeyReleased(evt);
      }
    });

    jLabTon3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabTon3.setForeground(new java.awt.Color(51, 51, 102));
    jLabTon3.setText("T. Descuento ");
    jLabTon3.setToolTipText("");
    jLabTon3.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabTon3MouseClicked(evt);
      }
    });

    txtTdf.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtTdf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtTdf.setText(" ");
    txtTdf.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    txtTdf.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtTdf.setPreferredSize(new java.awt.Dimension(7, 30));
    txtTdf.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtTdfMouseClicked(evt);
      }
    });
    txtTdf.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtTdfActionPerformed(evt);
      }
    });
    txtTdf.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtTdfKeyReleased(evt);
      }
    });

    jLabTon.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabTon.setForeground(new java.awt.Color(51, 51, 102));
    jLabTon.setText("T. Neto Factura");
    jLabTon.setToolTipText("");
    jLabTon.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabTonMouseClicked(evt);
      }
    });

    labnfa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labnfa.setForeground(new java.awt.Color(102, 0, 0));
    labnfa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labnfa.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabIva.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabIva.setForeground(new java.awt.Color(51, 51, 102));
    jLabIva.setText("Total Iva ");
    jLabIva.setToolTipText("");
    jLabIva.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabIvaMouseClicked(evt);
      }
    });

    jLabret.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabret.setForeground(new java.awt.Color(51, 51, 102));
    jLabret.setText(" T. Ret Iva");
    jLabret.setToolTipText("");
    jLabret.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabretMouseClicked(evt);
      }
    });

    jLabTon6.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabTon6.setForeground(new java.awt.Color(51, 51, 102));
    jLabTon6.setText("T. Net Ret 25%");
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

    labIva.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labIva.setForeground(new java.awt.Color(51, 51, 51));
    labIva.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labIva.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    labRet.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labRet.setForeground(new java.awt.Color(51, 51, 51));
    labRet.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labRet.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabIva, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(labIva, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jLabTfac, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
              .addComponent(jLabFac, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(txtNfa, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(txtTof, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabret, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(labRet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabTon3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(txtTdf, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jLabTon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jLabTon6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGap(18, 18, 18)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(labnfa, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
          .addComponent(labnre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap(29, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(txtNfa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabFac, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabTfac, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(txtTof, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabTon3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(txtTdf, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabTon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addComponent(labnfa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabret, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabIva, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labnre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labIva, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabTon6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(13, Short.MAX_VALUE))
    );

    getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 427, 897, -1));

    jPanel2.setBackground(new java.awt.Color(204, 204, 204));
    jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

    jLabFne.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabFne.setForeground(new java.awt.Color(51, 51, 102));
    jLabFne.setText("F. Not/Entrega");
    jLabFne.setToolTipText("Fecha Documento Not/Entrega");
    jLabFne.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabFneMouseClicked(evt);
      }
    });

    jLabFrec.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabFrec.setForeground(new java.awt.Color(51, 51, 102));
    jLabFrec.setText("F. Recibido");
    jLabFrec.setToolTipText("Fecha Entrega Mercancia al Cliente");
    jLabFrec.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabFrecMouseClicked(evt);
      }
    });

    jLabTon1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabTon1.setForeground(new java.awt.Color(51, 51, 102));
    jLabTon1.setText("Total Not/Ent ($)");
    jLabTon1.setToolTipText("");
    jLabTon1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabTon1MouseClicked(evt);
      }
    });

    txtTne.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtTne.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtTne.setText(" ");
    txtTne.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    txtTne.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtTne.setPreferredSize(new java.awt.Dimension(7, 30));
    txtTne.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtTneMouseClicked(evt);
      }
    });
    txtTne.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtTneActionPerformed(evt);
      }
    });
    txtTne.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtTneKeyReleased(evt);
      }
    });

    jLabTon2.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabTon2.setForeground(new java.awt.Color(51, 51, 102));
    jLabTon2.setText("T. Descuento ");
    jLabTon2.setToolTipText("");
    jLabTon2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabTon2MouseClicked(evt);
      }
    });

    txtTdn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtTdn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtTdn.setText(" ");
    txtTdn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    txtTdn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtTdn.setPreferredSize(new java.awt.Dimension(7, 30));
    txtTdn.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtTdnMouseClicked(evt);
      }
    });
    txtTdn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtTdnActionPerformed(evt);
      }
    });
    txtTdn.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtTdnKeyReleased(evt);
      }
    });

    jLabTon4.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabTon4.setForeground(new java.awt.Color(51, 51, 102));
    jLabTon4.setText("T. Neto Not Ent");
    jLabTon4.setToolTipText("");
    jLabTon4.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabTon4MouseClicked(evt);
      }
    });

    labTne.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labTne.setForeground(new java.awt.Color(102, 0, 0));
    labTne.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labTne.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    txtDiaN.setBackground(new java.awt.Color(250, 253, 244));
    txtDiaN.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtDiaN.setForeground(new java.awt.Color(51, 51, 51));
    txtDiaN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtDiaN.setText(" ");
    txtDiaN.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtDiaN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtDiaN.setPreferredSize(new java.awt.Dimension(10, 25));
    txtDiaN.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtDiaNMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtDiaNMouseReleased(evt);
      }
    });
    txtDiaN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtDiaNActionPerformed(evt);
      }
    });
    txtDiaN.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtDiaNKeyReleased(evt);
      }
    });

    txtMesN.setBackground(new java.awt.Color(250, 253, 244));
    txtMesN.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtMesN.setForeground(new java.awt.Color(51, 51, 51));
    txtMesN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtMesN.setText(" ");
    txtMesN.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtMesN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtMesN.setPreferredSize(new java.awt.Dimension(10, 25));
    txtMesN.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtMesNMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtMesNMouseReleased(evt);
      }
    });
    txtMesN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtMesNActionPerformed(evt);
      }
    });
    txtMesN.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtMesNKeyReleased(evt);
      }
    });

    txtAnoN.setBackground(new java.awt.Color(250, 253, 244));
    txtAnoN.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtAnoN.setForeground(new java.awt.Color(51, 51, 51));
    txtAnoN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtAnoN.setText(" ");
    txtAnoN.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtAnoN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtAnoN.setPreferredSize(new java.awt.Dimension(10, 25));
    txtAnoN.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtAnoNMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtAnoNMouseReleased(evt);
      }
    });
    txtAnoN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtAnoNActionPerformed(evt);
      }
    });
    txtAnoN.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtAnoNKeyReleased(evt);
      }
    });

    txtDiaR.setBackground(new java.awt.Color(240, 249, 222));
    txtDiaR.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtDiaR.setForeground(new java.awt.Color(51, 51, 51));
    txtDiaR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtDiaR.setText(" ");
    txtDiaR.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtDiaR.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtDiaR.setPreferredSize(new java.awt.Dimension(10, 25));
    txtDiaR.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtDiaRMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtDiaRMouseReleased(evt);
      }
    });
    txtDiaR.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtDiaRActionPerformed(evt);
      }
    });
    txtDiaR.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtDiaRKeyReleased(evt);
      }
    });

    txtMesR.setBackground(new java.awt.Color(240, 249, 222));
    txtMesR.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtMesR.setForeground(new java.awt.Color(51, 51, 51));
    txtMesR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtMesR.setText(" ");
    txtMesR.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtMesR.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtMesR.setPreferredSize(new java.awt.Dimension(10, 25));
    txtMesR.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtMesRMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtMesRMouseReleased(evt);
      }
    });
    txtMesR.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtMesRActionPerformed(evt);
      }
    });
    txtMesR.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtMesRKeyReleased(evt);
      }
    });

    txtAnoR.setBackground(new java.awt.Color(240, 249, 222));
    txtAnoR.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtAnoR.setForeground(new java.awt.Color(51, 51, 51));
    txtAnoR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtAnoR.setText(" ");
    txtAnoR.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtAnoR.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtAnoR.setPreferredSize(new java.awt.Dimension(10, 25));
    txtAnoR.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtAnoRMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtAnoRMouseReleased(evt);
      }
    });
    txtAnoR.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtAnoRActionPerformed(evt);
      }
    });
    txtAnoR.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtAnoRKeyReleased(evt);
      }
    });

    txtDiaV.setBackground(new java.awt.Color(250, 253, 244));
    txtDiaV.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtDiaV.setForeground(new java.awt.Color(51, 51, 51));
    txtDiaV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtDiaV.setText(" ");
    txtDiaV.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtDiaV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtDiaV.setPreferredSize(new java.awt.Dimension(10, 25));
    txtDiaV.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtDiaVMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtDiaVMouseReleased(evt);
      }
    });
    txtDiaV.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtDiaVActionPerformed(evt);
      }
    });
    txtDiaV.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtDiaVKeyReleased(evt);
      }
    });

    txtMesV.setBackground(new java.awt.Color(250, 253, 244));
    txtMesV.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtMesV.setForeground(new java.awt.Color(51, 51, 51));
    txtMesV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtMesV.setText(" ");
    txtMesV.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtMesV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtMesV.setPreferredSize(new java.awt.Dimension(10, 25));
    txtMesV.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtMesVMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtMesVMouseReleased(evt);
      }
    });
    txtMesV.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtMesVActionPerformed(evt);
      }
    });
    txtMesV.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtMesVKeyReleased(evt);
      }
    });

    txtAnoV.setBackground(new java.awt.Color(250, 253, 244));
    txtAnoV.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtAnoV.setForeground(new java.awt.Color(51, 51, 51));
    txtAnoV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtAnoV.setText(" ");
    txtAnoV.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtAnoV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtAnoV.setPreferredSize(new java.awt.Dimension(10, 25));
    txtAnoV.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtAnoVMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtAnoVMouseReleased(evt);
      }
    });
    txtAnoV.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtAnoVActionPerformed(evt);
      }
    });
    txtAnoV.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtAnoVKeyReleased(evt);
      }
    });

    jLabTon7.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    jLabTon7.setForeground(new java.awt.Color(51, 51, 102));
    jLabTon7.setText("F. Vencimiento");
    jLabTon7.setToolTipText("");
    jLabTon7.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabTon7MouseClicked(evt);
      }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addGap(19, 19, 19)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
            .addComponent(jLabTon7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
            .addComponent(jLabFne, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
            .addComponent(jLabFrec, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabTon1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addGap(18, 18, 18)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(txtDiaN, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtMesN, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtAnoN, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(txtTne, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabTon2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtTdn, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(20, 20, 20)
            .addComponent(jLabTon4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGap(18, 18, 18)
            .addComponent(labTne, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(38, 38, 38))
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(txtDiaR, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMesR, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAnoR, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(txtDiaV, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMesV, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAnoV, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 0, Short.MAX_VALUE))))
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabFne, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtAnoN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtMesN, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtDiaN, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabTon7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtDiaV, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtMesV, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtAnoV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(10, 10, 10)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabFrec, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(txtDiaR, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtMesR, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtAnoR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtTne, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabTon1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabTon2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(txtTdn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabTon4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(labTne, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );

    getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 227, 897, 160));

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
    getContentPane().add(btnCal, new org.netbeans.lib.awtextra.AbsoluteConstraints(896, 577, 20, 31));

    labFac.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    labFac.setForeground(new java.awt.Color(153, 0, 51));
    labFac.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labFac.setText("CON FACTURA  Bs");
    labFac.setToolTipText("Cliente con factura");
    getContentPane().add(labFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 391, 148, 30));

    cbFac.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    cbFac.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbFacActionPerformed(evt);
      }
    });
    getContentPane().add(cbFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(198, 391, -1, 30));

    labojo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labojo.setForeground(new java.awt.Color(102, 102, 102));
    labojo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojop.png"))); // NOI18N
    labojo.setToolTipText("Notificacion de Mensaje");
    labojo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    getContentPane().add(labojo, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 578, -1, 30));

    labMsg.setFont(new java.awt.Font("Verdana", 1, 15)); // NOI18N
    labMsg.setForeground(new java.awt.Color(204, 0, 0));
    labMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labMsg.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    getContentPane().add(labMsg, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 578, 482, 30));

    jPanel3.setBackground(new java.awt.Color(204, 204, 204));
    jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

    jLabNoe1.setBackground(new java.awt.Color(51, 51, 102));
    jLabNoe1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabNoe1.setForeground(new java.awt.Color(51, 51, 102));
    jLabNoe1.setText("No. Pedido");
    jLabNoe1.setToolTipText("");
    jLabNoe1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabNoe1MouseClicked(evt);
      }
    });

    txtPed.setBackground(new java.awt.Color(246, 245, 245));
    txtPed.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    txtPed.setForeground(new java.awt.Color(102, 0, 0));
    txtPed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtPed.setText(" ");
    txtPed.setToolTipText("Ingrese No. Pedido");
    txtPed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    txtPed.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtPed.setPreferredSize(new java.awt.Dimension(7, 30));
    txtPed.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtPedMouseClicked(evt);
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

    labBusPed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labBusPed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    labBusPed.setToolTipText("Buscar Pedidos");
    labBusPed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labBusPed.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labBusPed.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labBusPedMouseClicked(evt);
      }
    });

    jLabTon5.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabTon5.setForeground(new java.awt.Color(51, 51, 102));
    jLabTon5.setText("Total Pedido $");
    jLabTon5.setToolTipText("");
    jLabTon5.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabTon5MouseClicked(evt);
      }
    });

    labTpe.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labTpe.setForeground(new java.awt.Color(102, 102, 102));
    labTpe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labTpe.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabNoe2.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabNoe2.setForeground(new java.awt.Color(51, 51, 102));
    jLabNoe2.setText("Fecha Pedido");
    jLabNoe2.setToolTipText("");
    jLabNoe2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabNoe2MouseClicked(evt);
      }
    });

    txtDiaP.setBackground(new java.awt.Color(250, 253, 244));
    txtDiaP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtDiaP.setForeground(new java.awt.Color(51, 51, 51));
    txtDiaP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtDiaP.setText(" ");
    txtDiaP.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtDiaP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtDiaP.setPreferredSize(new java.awt.Dimension(10, 25));
    txtDiaP.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtDiaPMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtDiaPMouseReleased(evt);
      }
    });
    txtDiaP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtDiaPActionPerformed(evt);
      }
    });
    txtDiaP.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtDiaPKeyReleased(evt);
      }
      public void keyTyped(java.awt.event.KeyEvent evt) {
        txtDiaPKeyTyped(evt);
      }
    });

    txtMesP.setBackground(new java.awt.Color(250, 253, 244));
    txtMesP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtMesP.setForeground(new java.awt.Color(51, 51, 51));
    txtMesP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtMesP.setText(" ");
    txtMesP.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtMesP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtMesP.setPreferredSize(new java.awt.Dimension(10, 25));
    txtMesP.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtMesPMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtMesPMouseReleased(evt);
      }
    });
    txtMesP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtMesPActionPerformed(evt);
      }
    });
    txtMesP.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtMesPKeyReleased(evt);
      }
    });

    txtAnoP.setBackground(new java.awt.Color(250, 253, 244));
    txtAnoP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtAnoP.setForeground(new java.awt.Color(51, 51, 51));
    txtAnoP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtAnoP.setText(" ");
    txtAnoP.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
    txtAnoP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtAnoP.setPreferredSize(new java.awt.Dimension(10, 25));
    txtAnoP.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtAnoPMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtAnoPMouseReleased(evt);
      }
    });
    txtAnoP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtAnoPActionPerformed(evt);
      }
    });
    txtAnoP.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtAnoPKeyReleased(evt);
      }
    });

    jLabCte.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabCte.setForeground(new java.awt.Color(51, 51, 102));
    jLabCte.setText("Cliente");
    jLabCte.setToolTipText("");
    jLabCte.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCteMouseClicked(evt);
      }
    });

    labBusCli.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labBusCli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    labBusCli.setToolTipText("Buscar Pedidos");
    labBusCli.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labBusCli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labBusCli.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labBusCliMouseClicked(evt);
      }
    });

    labNoc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labNoc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

    txtCoc.setBackground(new java.awt.Color(204, 204, 204));
    txtCoc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtCoc.setForeground(new java.awt.Color(102, 102, 102));
    txtCoc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtCoc.setText(" ");
    txtCoc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    txtCoc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtCoc.setOpaque(false);
    txtCoc.setPreferredSize(new java.awt.Dimension(7, 30));
    txtCoc.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtCocMouseClicked(evt);
      }
    });
    txtCoc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtCocActionPerformed(evt);
      }
    });

    labDic.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labDic.setForeground(new java.awt.Color(0, 0, 102));
    labDic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labDic.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabCte1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabCte1.setForeground(new java.awt.Color(51, 51, 102));
    jLabCte1.setText("Dias Credito");
    jLabCte1.setToolTipText("");
    jLabCte1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabCte1MouseClicked(evt);
      }
    });

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addGap(18, 18, 18)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jLabNoe2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabCte, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
            .addGap(50, 50, 50)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(txtDiaP, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(txtMesP, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(txtAnoP, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                  .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(labBusCli, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12, 12, 12)
                    .addComponent(labNoc, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(jLabTon5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(labTpe, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(59, 59, 59)
                    .addComponent(jLabCte1)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(txtCoc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addComponent(labDic, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))))))
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addGap(1, 1, 1)
            .addComponent(jLabNoe1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(49, 49, 49)
            .addComponent(txtPed, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(9, 9, 9)
            .addComponent(labBusPed, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(27, Short.MAX_VALUE))
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addGap(6, 6, 6)
            .addComponent(jLabNoe1))
          .addComponent(txtPed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labBusPed, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabTon5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labTpe, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labDic, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabCte1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(7, 7, 7)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabNoe2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtDiaP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtMesP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtAnoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(10, 10, 10)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabCte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labBusCli, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labNoc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtCoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(18, Short.MAX_VALUE))
    );

    getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 75, -1, -1));

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/FondoMain.png"))); // NOI18N
    getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 940, 620));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    RegNoe.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void jLabNoeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabNoeMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabNoeMouseClicked

  private void txtNoeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNoeMouseClicked
    iniciaNotEnt();
  }//GEN-LAST:event_txtNoeMouseClicked

  private void txtNoeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoeActionPerformed
    if (isvalidNotEnt()) {
      verifNota();
    }
  }//GEN-LAST:event_txtNoeActionPerformed

  private void labBusPedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labBusPedMouseClicked
    if (nne > 0) {
      //iniciaNotEnt();
      if (conPed != null) {
        conPed.dispose();
      }
      conPed = new Consulta_Pedidos(ctrN);
      conPed.setVisible(true);
      conPed.setExtendedState(NORMAL);
      conPed.setVisible(true);
    }
  }//GEN-LAST:event_labBusPedMouseClicked

  private void jLabNoe1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabNoe1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabNoe1MouseClicked

  private void labBusNenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labBusNenMouseClicked
    iniciaNotEnt();
    if (conNoe != null) {
      conNoe.dispose();
    }
    conNoe = new Consulta_NotaEntrega(ctrN, "");
    conNoe.setVisible(true);
    conNoe.setExtendedState(NORMAL);
    conNoe.setVisible(true);
  }//GEN-LAST:event_labBusNenMouseClicked

  private void txtPedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPedMouseClicked
    bloqueaCampos();
    txtPed.setEnabled(true);
    labBusPed.setEnabled(true);
    txtPed.setText("0");
    txtPed.setSelectionStart(0);
    txtPed.setSelectionEnd(txtPed.getText().length());
    txtPed.requestFocus();
  }//GEN-LAST:event_txtPedMouseClicked

  private void txtPedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPedActionPerformed
    verifPedido();
  }//GEN-LAST:event_txtPedActionPerformed

  private void jLabFneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabFneMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabFneMouseClicked

  private void jLabFrecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabFrecMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabFrecMouseClicked

  private void jLabTonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabTonMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabTonMouseClicked

  private void txtNfaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNfaMouseClicked
    txtNfa.setSelectionStart(0);
    txtNfa.setSelectionEnd(txtNfa.getText().length());
    txtNfa.requestFocus();
  }//GEN-LAST:event_txtNfaMouseClicked

  private void txtNfaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNfaActionPerformed
    if (isvalidNroFac()) {
      if (buscaFactura()) {
        String mox = txtTof.getText().trim();
        mox = GetCurrencyDouble(mox);
        txtTof.setText(mox + "  ");
        txtTof.setSelectionStart(0);
        txtTof.setSelectionEnd(txtTof.getText().length());
        txtTof.setEnabled(true);
        txtTof.requestFocus();
      }
    }
  }//GEN-LAST:event_txtNfaActionPerformed

  private void txtNfaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNfaKeyReleased
    isvalidNroFac();
  }//GEN-LAST:event_txtNfaKeyReleased

  private void jLabIvaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabIvaMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabIvaMouseClicked

  private void jLabFacMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabFacMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabFacMouseClicked

  private void txtTdnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTdnMouseClicked
    String mox = txtTdn.getText().trim();
    mox = GetCurrencyDouble(mox);
    txtTdn.setText(mox + "  ");
    txtTdn.setSelectionStart(0);
    txtTdn.setSelectionEnd(txtTdn.getText().length());
    txtTdn.requestFocus();
  }//GEN-LAST:event_txtTdnMouseClicked

  private void txtTdnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTdnActionPerformed
    if (isvalidImpDesNE(0)) {
      //if (fac.equals("0")) {
      //  cbFac.setEnabled(true);
      //  cbFac.setSelected(true);
      //  txtNfa.setEnabled(true);
      //  txtNfa.requestFocus();
      //  labTne.setText(MtoEs(tne - tdn, 2));
      //  labMsg.setText("- Ingrese Importe Factura");
      //  cbFac.requestFocus();
      //} else {
      labTne.setText(MtoEs(tne - tdn, 2));
      btnGra.setEnabled(true);
      cbFac.requestFocus();
      labMsg.setText("- Verifique antes de Grabar Registro");
      //}
    }
  }//GEN-LAST:event_txtTdnActionPerformed

  private void txtTdnKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTdnKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTdnKeyReleased

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed
    if (validaCampos()) {
      grabarRegistro();
    }

  }//GEN-LAST:event_btnGraActionPerformed

  public boolean validaCampos() {
    indok = 0;
    validaFecPed();
    if (indok == 0) {
      // validaFecNoe();
    }
    if (indok == 0) {
      // validaFecRec();
    }

    if (indok == 0) {
      if (!isvalidImpNotEnt(1)) {
        indok = 1;
      }
      if (!isvalidImpDesNE(1)) {
        indok = 1;
      }
      if (nne == 0 || coc.length() == 0 || fep.length() == 0 || fne.length() == 0) {
        labMsg.setText("-- Hay Errores  Revise!  ");
        indok = 1;
      }
      if (tdn > tne) {
        labMsg.setText("- Total descuento mayor a total Not/ENT");
        indok = 1;
      }

      fac = "1";
      if (cbFac.isSelected()) {
        fac = "0";
      }

      if (fac.equals("0")) {

        if (!isvalidImpFactura(1)) {
          indok = 1;
        }
        if (!isvalidImpDesFac(1)) {
          indok = 1;
        }
        if (tdf > tfa) {
          labMsg.setText("- Total descuento mayor a total Factura");
          indok = 1;
        }
        if (nfa == 0 && tfa > 0) {
          indok = 1;
        }

        if (indok == 0) {
          toi = (tfa - tdf) * poi;
          tor = toi * (pre / 100);
        }

      } else {
        nfa = 0;
        tfa = 0;
        tdf = 0;
        poi = 0;
        toi = 0;
        tor = 0;
      }

    }
    if (indok == 0) {
      return true;
    } else {
      return false;
    }

  }

  public void grabarRegistro() {
    ImageIcon icon = new ImageIcon(getClass().getResource("/img/salvar.png"));
    String vax = "Desea Grabar\nNota de Entrega - " + nne + "  ?";
    String[] options = {"SI", "NO"};
    int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
    if (opcion == 0) {
      //Incluir / Modificar
      tor = toi * (pre / 100);
      if (pre == 100) {
        tor = 0;
      }
      NotaEntrega n = new NotaEntrega();
      if (!n.existeNotEnt(nne)) {
        // Incluir
        fne = fne.replace("O", "0");
        fep = fep.replace("O", "0");
        fee = fee.replace("O", "0");
        fev = fev.replace("O", "0");
        fne = fne.replace("o", "0");
        fep = fep.replace("o", "0");
        fee = fee.replace("o", "0");
        fev = fev.replace("o", "0");
        fne = fne.replace("|", "1");
        fep = fep.replace("|", "1");
        fee = fee.replace("|", "1");
        fev = fev.replace("|", "1");
        fre = ymdhoyhhmm();
        n = new NotaEntrega(nne, nfa, npe, coc, noc, fne, fep, fee, fev, fre, obs, tpe, tne, tdn, tfa, tdf, pre, toi, poi, tor);
        if (n.insertarNotaEntrega()) {
          labMsg.setText("- Se creo Nota Entrega");
        }
      } else {
        // Modifica
        n = new NotaEntrega(nne, nfa, npe, coc, noc, fne, fep, fee, fev, fre, obs, tpe, tne, tdn, tfa, tdf, pre, toi, poi, tor);
        if (n.modificarNotaEntrega()) {
          labMsg.setText("- Se Modifico Nota Entrega");
        }
      }
      iniciaNotEnt();
    }
  }

  private void jLabTfacMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabTfacMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabTfacMouseClicked

  private void txtTofMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTofMouseClicked
    String mox = txtTof.getText().trim();
    mox = GetCurrencyDouble(mox);
    txtTof.setText(mox + "  ");
    txtTof.setSelectionStart(0);
    txtTof.setSelectionEnd(txtTof.getText().length());
    txtTof.requestFocus();
  }//GEN-LAST:event_txtTofMouseClicked

  private void txtTofActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTofActionPerformed
    if (isvalidImpFactura(0)) {
      txtTdf.setEnabled(true);
      if (tdf == 0) {
        txtTdf.setText("0");
      }
      String mox = txtTdf.getText().trim();
      mox = GetCurrencyDouble(mox);
      txtTdf.setText(mox + "  ");
      txtTdf.setSelectionStart(0);
      txtTdf.setSelectionEnd(txtTdf.getText().length());
      txtTdf.requestFocus();
    }
  }//GEN-LAST:event_txtTofActionPerformed

  private void txtTofKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTofKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTofKeyReleased

  private void jLabretMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabretMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabretMouseClicked

  private void jLabCteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCteMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCteMouseClicked

  private void txtCocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCocMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCocMouseClicked

  private void txtCocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCocActionPerformed
    setCliente();
  }//GEN-LAST:event_txtCocActionPerformed

  public void setCliente() {
    if (isvalidCodCte()) {
      if (buscaCliente()) {
        txtDiaN.setEnabled(true);
        txtMesN.setEnabled(true);
        txtAnoN.setEnabled(true);
        txtDiaN.setBorder(new LineBorder(Color.GRAY));
        txtDiaR.setEnabled(true);
        txtMesR.setEnabled(true);
        txtAnoR.setEnabled(true);
        txtDiaV.setBorder(new LineBorder(Color.GRAY));
        txtDiaV.setEnabled(true);
        txtMesV.setEnabled(true);
        txtAnoV.setEnabled(true);
        txtDiaV.setBorder(new LineBorder(Color.GRAY));
        seteaFecNoe();
        if (fne.length() == 0) {
          // txtDiaP.setText(txtDiaN.getText());
          // txtMesP.setText(txtMesN.getText());
          // txtAnoP.setText(txtAnoN.getText());
        }
        /*
        txtDiaN.setSelectionStart(0);
        txtDiaN.setSelectionEnd(txtDiaN.getText().length());
        txtMesN.setSelectionStart(0);
        txtMesN.setSelectionEnd(txtMesN.getText().length());
        txtAnoN.setSelectionStart(0);
        txtAnoN.setSelectionEnd(txtAnoN.getText().length());
         */
        labMsg.setText("- Ingrese Fecha Nota Entrega");
        txtDiaN.requestFocus();
      }
    }
  }


  private void jLabTon1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabTon1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabTon1MouseClicked

  private void txtTneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTneMouseClicked
    String mox = txtTne.getText().trim();
    mox = GetCurrencyDouble(mox);
    txtTne.setText(mox + "  ");
    txtTne.setSelectionStart(0);
    txtTne.setSelectionEnd(txtTne.getText().length());
    txtTne.requestFocus();
  }//GEN-LAST:event_txtTneMouseClicked

  private void txtTneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTneActionPerformed
    if (isvalidImpNotEnt(0)) {
      txtTdn.setEnabled(true);
      if (tdn == 0) {
        txtTdn.setText("0");
      }
      String mox = txtTdn.getText().trim();
      mox = GetCurrencyDouble(mox);
      txtTdn.setText(mox + "  ");
      txtTdn.setSelectionStart(0);
      txtTdn.setSelectionEnd(txtTdn.getText().length());
      txtTdn.requestFocus();
      labMsg.setText("- Ingrese Descuento Not/Entrega");
      if (pre > 0) {
        cbFac.setEnabled(true);
      }
    }
  }//GEN-LAST:event_txtTneActionPerformed

  private void txtTneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTneKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTneKeyReleased

  private void jLabTon2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabTon2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabTon2MouseClicked

  private void jLabTon3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabTon3MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabTon3MouseClicked

  private void txtTdfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTdfMouseClicked
    String mox = txtTdf.getText().trim();
    mox = GetCurrencyDouble(mox);
    txtTdf.setText(mox + "  ");
    txtTdf.setSelectionStart(0);
    txtTdf.setSelectionEnd(txtTdf.getText().length());
    txtTdf.requestFocus();
  }//GEN-LAST:event_txtTdfMouseClicked

  private void txtTdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTdfActionPerformed
    if (isvalidImpDesFac(0)) {
      Importadora imp = new Importadora();
      poi = imp.getIva() / 100;  // % Iva general
      toi = (tfa - tdf) * poi;
      if (pre == 100) {
        tor = 0;
      } else {
        tor = toi * (pre / 100);
      }
      labnfa.setText(MtoEs(tfa - tdf, 2));   // Total neto factura
      labIva.setText(MtoEs(toi, 2));         // Total Iva
      labRet.setText(MtoEs(tor, 2));         // total Retencion Iva
      labnre.setText(MtoEs(toi - tor, 2));   // Total dif retencion
      btnGra.setEnabled(true);
      btnGra.requestFocus();
      labMsg.setText("- Revise bien antes de Grabar");
    }

  }//GEN-LAST:event_txtTdfActionPerformed

  private void txtTdfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTdfKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtTdfKeyReleased

  private void jLabTon4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabTon4MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabTon4MouseClicked

  private void jLabTon5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabTon5MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabTon5MouseClicked

  private void jLabTon6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabTon6MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabTon6MouseClicked

  private void clockDigital1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clockDigital1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_clockDigital1MouseClicked

  private void clockDigital1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clockDigital1MouseEntered

  }//GEN-LAST:event_clockDigital1MouseEntered

  private void btnEliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliActionPerformed
    if (buscaRecCobroNotEnt()) {
      labMsg.setText("- Not/Entrega Cerrada  -  Rec Cobro No.  " + nrc);
      txtNoe.setSelectionStart(0);
      txtNoe.setSelectionEnd(txtNoe.getText().length());
      txtNoe.requestFocus();
      //btnSal.requestFocus();
    } else {
      eliminaRegistro();
    }
  }//GEN-LAST:event_btnEliActionPerformed

  public void eliminaRegistro() {
    ImageIcon icon = new ImageIcon(getClass().getResource("/img/elim.png"));
    String vax = "Desea Eliminar Nota de Entrega - " + nne + "  ?";
    String[] options = {"SI", "NO"};
    int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
    if (opcion == 0) {
      NotaEntrega n = new NotaEntrega(nne);
      if (n.eliminarNotaEntrega()) {
        labMsg.setText(" - Se elimino Nota de Entrega  " + nne + " -");
        iniciaNotEnt();
      }
    }
  }

  private void jLabNoe2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabNoe2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabNoe2MouseClicked

  private void cbFacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFacActionPerformed
    if (cbFac.isSelected() == true) {
      fac = "0";
      txtNfa.setEnabled(true);
      txtTof.setEnabled(true);
      txtTdf.setEnabled(true);
      labIva.setEnabled(true);
      labnre.setEnabled(true);
      labRet.setEnabled(true);
      txtNfa.requestFocus();
    } else {
      ImageIcon icon = new ImageIcon(getClass().getResource("/img/back.png"));
      String vax = "Desea Quitar Factura ?";
      String[] options = {"SI", "NO"};
      int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
      if (opcion == 0) {
        fac = "1";
        nfa = 0;
        tfa = 0;
        tdf = 0;
        poi = 0;
        toi = 0;
        por = 0;
        tor = 0;
        txtNfa.setText("");
        txtTof.setText("");
        txtTdf.setText("");
        labIva.setText("");
        labnre.setText("");
        txtNfa.setEnabled(false);
        txtTof.setEnabled(false);
        txtTdf.setEnabled(false);
        //labIva.setEnabled(false);
        labnre.setEnabled(false);
        btnGra.setSelected(true);
      } else {
        cbFac.setSelected(true);
      }
    }
  }//GEN-LAST:event_cbFacActionPerformed

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
    isvalidNroPed();
  }//GEN-LAST:event_txtPedKeyReleased

  private void txtNoeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoeKeyReleased
//    isvalidNotEnt();
  }//GEN-LAST:event_txtNoeKeyReleased

  private void txtDiaNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiaNMouseClicked
    txtDiaN.setSelectionStart(0);
    txtDiaN.setSelectionEnd(txtDiaN.getText().length());
  }//GEN-LAST:event_txtDiaNMouseClicked

  private void txtDiaNMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiaNMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDiaNMouseReleased

  private void txtDiaNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiaNActionPerformed
    validaFecNoe();
  }//GEN-LAST:event_txtDiaNActionPerformed
  public void validaFecNoe() {
    indok = 0;
    txtDiaN.setBorder(new LineBorder(Color.GRAY));
    String vax = txtDiaN.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtDiaN.setText(vax);
    }
    vax = txtMesN.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtMesN.setText(vax);
    }
    fne = txtAnoN.getText() + txtMesN.getText() + txtDiaN.getText();
    if (fne.length() == 8) {
      if (isvalidFec(fne, 0)) {
        if (fep.length() > 0 && (fep.compareTo(fne) > 0)) {
          labMsg.setText("- Fecha Not Ent " + ymd_dmy(fne) + " menor a Fecha Pedido");
          indok = 1;
        } else {
          //Importadora imp = new Importadora();
          //int ndi = imp.getDiasCred();

          Clientes cli = new Clientes(coc);
          int ndi = cli.getDiasCred();
          fev = getFecDias(fne, ndi).trim();
          //yyyymmdd
          if (fev.length() == 8) {
            txtDiaV.setText(fev.substring(fev.length() - 2, fev.length()));
            txtMesV.setText(fev.substring(4, 6));
            txtAnoV.setText(fev.substring(0, 4));
            txtDiaV.setSelectionStart(0);
            txtDiaV.setSelectionEnd(txtDiaV.getText().length());
            txtMesV.setSelectionStart(0);
            txtMesV.setSelectionEnd(txtMesV.getText().length());
            txtAnoV.setSelectionStart(0);
            txtAnoV.setSelectionEnd(txtAnoV.getText().length());
            txtDiaN.setBorder(new LineBorder(Color.GRAY));
            txtDiaR.setBorder(new LineBorder(Color.GRAY));
            txtDiaV.setBorder(new LineBorder(Color.GRAY));
            labMsg.setText("- Ingrese Fecha Vencimiento");
            txtDiaV.requestFocus();
          }
        }
      } else {
        labMsg.setText("- Fecha Nota Entrega Invalida");
        indok = 1;
      }
    } else {
      indok = 1;
    }
    if (indok == 1) {
      seteaFecNoe();
      txtDiaN.setBorder(new LineBorder(Color.RED));
      txtDiaN.requestFocus();
    }

  }

  public void seteaFecPed() {
    txtDiaP.setText("" + getDiaHoy());
    txtMesP.setText("" + getMesHoy());
    txtAnoP.setText("" + getAnoHoy());
    String vax = txtDiaP.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtDiaP.setText(vax);
    }
    vax = txtMesP.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtMesP.setText(vax);
    }
    txtDiaP.setSelectionStart(0);
    txtDiaP.setSelectionEnd(txtDiaP.getText().length());
    txtMesP.setSelectionStart(0);
    txtMesP.setSelectionEnd(txtMesP.getText().length());
    txtAnoP.setSelectionStart(0);
    txtAnoP.setSelectionEnd(txtAnoP.getText().length());
  }

  public void seteaFecNoe() {
    txtDiaN.setText("" + getDiaHoy());
    txtMesN.setText("" + getMesHoy());
    txtAnoN.setText("" + getAnoHoy());
    String vax = txtDiaN.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtDiaN.setText(vax);
    }
    vax = txtMesN.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtMesN.setText(vax);
    }
    txtDiaN.setSelectionStart(0);
    txtDiaN.setSelectionEnd(txtDiaN.getText().length());
    txtMesN.setSelectionStart(0);
    txtMesN.setSelectionEnd(txtMesN.getText().length());
    txtAnoN.setSelectionStart(0);
    txtAnoN.setSelectionEnd(txtAnoN.getText().length());
  }

  public void seteaFecRec() {
    txtDiaR.setText("" + getDiaHoy());
    txtMesR.setText("" + getMesHoy());
    txtAnoR.setText("" + getAnoHoy());
    String vax = txtDiaR.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtDiaR.setText(vax);
    }
    vax = txtMesR.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtMesR.setText(vax);
    }
    txtDiaR.setSelectionStart(0);
    txtDiaR.setSelectionEnd(txtDiaR.getText().length());
    txtMesR.setSelectionStart(0);
    txtMesR.setSelectionEnd(txtMesR.getText().length());
    txtAnoR.setSelectionStart(0);
    txtAnoR.setSelectionEnd(txtAnoR.getText().length());
  }

  public void seteaFecVen() {
    txtDiaV.setText("" + getDiaHoy());
    txtMesV.setText("" + getMesHoy());
    txtAnoV.setText("" + getAnoHoy());
    String vax = txtDiaV.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtDiaV.setText(vax);
    }
    vax = txtMesV.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtMesV.setText(vax);
    }
    txtDiaV.setSelectionStart(0);
    txtDiaV.setSelectionEnd(txtDiaV.getText().length());
    txtMesV.setSelectionStart(0);
    txtMesV.setSelectionEnd(txtMesV.getText().length());
    txtAnoV.setSelectionStart(0);
    txtAnoV.setSelectionEnd(txtAnoV.getText().length());
  }


  private void txtDiaNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiaNKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDiaNKeyReleased

  private void txtMesNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMesNMouseClicked
    txtMesN.setSelectionStart(0);
    txtMesN.setSelectionEnd(txtMesN.getText().length());
  }//GEN-LAST:event_txtMesNMouseClicked

  private void txtMesNMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMesNMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtMesNMouseReleased

  private void txtMesNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMesNActionPerformed
    validaFecNoe();
  }//GEN-LAST:event_txtMesNActionPerformed

  private void txtMesNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMesNKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtMesNKeyReleased

  private void txtAnoNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnoNMouseClicked
    txtAnoN.setSelectionStart(0);
    txtAnoN.setSelectionEnd(txtAnoN.getText().length());
  }//GEN-LAST:event_txtAnoNMouseClicked

  private void txtAnoNMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnoNMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtAnoNMouseReleased

  private void txtAnoNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAnoNActionPerformed
    validaFecNoe();
  }//GEN-LAST:event_txtAnoNActionPerformed

  private void txtAnoNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnoNKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtAnoNKeyReleased

  private void txtDiaRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiaRMouseClicked
    txtDiaR.setSelectionStart(0);
    txtDiaR.setSelectionEnd(txtDiaR.getText().length());
  }//GEN-LAST:event_txtDiaRMouseClicked

  private void txtDiaRMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiaRMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDiaRMouseReleased

  private void txtDiaRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiaRActionPerformed
    validaFecRec();
  }//GEN-LAST:event_txtDiaRActionPerformed

  public void validaFecRec() {
    indok = 0;
    txtDiaR.setBorder(new LineBorder(Color.GRAY));
    String vax = txtDiaR.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtDiaR.setText(vax);
    }
    vax = txtMesR.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtMesR.setText(vax);
    }

    fee = txtAnoR.getText() + txtMesR.getText() + txtDiaR.getText();

    if (fee.length() == 8) {
      if (isvalidFec(fee, 0)) {
        if (fep.length() > 0 && (fee.compareTo(fep) < 0)) {
          labMsg.setText("- Fecha Recibe " + ymd_dmy(fee) + " menor a Fecha Pedido");
          indok = 1;
        } else {
          if (fne.length() > 0 && (fee.compareTo(fne) < 0)) {
            labMsg.setText("- Fecha Recibe " + ymd_dmy(fee) + " menor a Fec Not Ent");
            indok = 1;
          } else {
            txtDiaN.setBorder(new LineBorder(Color.GRAY));
            txtDiaR.setBorder(new LineBorder(Color.GRAY));
            txtDiaV.setBorder(new LineBorder(Color.GRAY));
            txtTne.setEnabled(true);
            String mox = txtTne.getText().trim();
            mox = GetCurrencyDouble(mox);
            txtTne.setText(mox + "  ");
            txtTne.setSelectionStart(0);
            txtAnoN.setSelectionEnd(txtTne.getText().length());
            labMsg.setText("- Ingrese Importe Nota Entrega");
            txtTne.requestFocus();
          }
        }
      } else {
        labMsg.setText("- Fecha Recibe Invalida");
        indok = 1;
      }
    } else {
      indok = 1;
    }
    if (indok == 1) {
      seteaFecRec();
      txtDiaR.requestFocus();
    }
  }

  private void txtDiaRKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiaRKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDiaRKeyReleased

  private void txtMesRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMesRMouseClicked
    txtMesR.setSelectionStart(0);
    txtMesR.setSelectionEnd(txtMesR.getText().length());
  }//GEN-LAST:event_txtMesRMouseClicked

  private void txtMesRMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMesRMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtMesRMouseReleased

  private void txtMesRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMesRActionPerformed
    validaFecRec();
  }//GEN-LAST:event_txtMesRActionPerformed

  private void txtMesRKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMesRKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtMesRKeyReleased

  private void txtAnoRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnoRMouseClicked
    txtAnoR.setSelectionStart(0);
    txtAnoR.setSelectionEnd(txtAnoR.getText().length());
  }//GEN-LAST:event_txtAnoRMouseClicked

  private void txtAnoRMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnoRMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtAnoRMouseReleased

  private void txtAnoRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAnoRActionPerformed
    validaFecRec();
  }//GEN-LAST:event_txtAnoRActionPerformed

  private void txtAnoRKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnoRKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtAnoRKeyReleased

  private void txtDiaPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiaPMouseClicked
    txtDiaP.setSelectionStart(0);
    txtDiaP.setSelectionEnd(txtDiaP.getText().length());
  }//GEN-LAST:event_txtDiaPMouseClicked

  private void txtDiaPMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiaPMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDiaPMouseReleased

  private void txtDiaPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiaPActionPerformed
    validaFecPed();
  }//GEN-LAST:event_txtDiaPActionPerformed

  public void validaFecPed() {
    indok = 0;
    txtDiaP.setBorder(new LineBorder(Color.GRAY));
    String vax = txtDiaP.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtDiaP.setText(vax);
    }
    vax = txtMesP.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtMesP.setText(vax);
    }
    fep = txtAnoP.getText() + txtMesP.getText() + txtDiaP.getText();
    if (fep.length() == 8) {
      if (isvalidFec(fep, 0)) {
        if (fne.length() > 0 && (fep.compareTo(fne) > 0)) {
          labMsg.setText("- Fecha Pedido " + ymd_dmy(fep) + " mayor a Fec Not Ent");
          indok = 1;
        } else {
          if (fee.length() > 0 && (fee.compareTo(fep) < 0)) {
            labMsg.setText("- Fecha Pedido " + ymd_dmy(fep) + " mayor  a Fec Recibe");
            indok = 1;
          } else {
            txtDiaP.setBorder(new LineBorder(Color.GRAY));
            txtDiaN.setBorder(new LineBorder(Color.GRAY));
            txtDiaR.setBorder(new LineBorder(Color.GRAY));
            desbloqueaCampos();
            if (labNoc.getText().length() == 0) {
              labMsg.setText("- Ingrese codigo Cliente");
            }
            //txtCoc.setEnabled(true);
            labBusCli.setEnabled(true);
            labBusCli.requestFocus();
          }
        }
      } else {
        labMsg.setText("- Fecha Pedido " + ymd_dmy(fep) + " Invalida");
        indok = 1;
      }
    } else {
      labMsg.setText("- Fecha Pedido " + ymd_dmy(fep) + " Invalida");
      indok = 1;
    }
    if (indok == 1) {
      seteaFecPed();
      txtDiaP.setBorder(new LineBorder(Color.RED));
      txtDiaP.requestFocus();
    }
  }


  private void txtDiaPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiaPKeyReleased

  }//GEN-LAST:event_txtDiaPKeyReleased

  private void txtMesPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMesPMouseClicked
    txtMesP.setSelectionStart(0);
    txtMesP.setSelectionEnd(txtMesP.getText().length());
  }//GEN-LAST:event_txtMesPMouseClicked

  private void txtMesPMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMesPMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtMesPMouseReleased

  private void txtMesPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMesPActionPerformed
    validaFecPed();
  }//GEN-LAST:event_txtMesPActionPerformed

  private void txtMesPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMesPKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtMesPKeyReleased

  private void txtAnoPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnoPMouseClicked
    txtAnoP.setSelectionStart(0);
    txtAnoP.setSelectionEnd(txtAnoP.getText().length());
  }//GEN-LAST:event_txtAnoPMouseClicked

  private void txtAnoPMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnoPMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtAnoPMouseReleased

  private void txtAnoPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAnoPActionPerformed
    validaFecPed();
  }//GEN-LAST:event_txtAnoPActionPerformed

  private void txtAnoPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnoPKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtAnoPKeyReleased

  private void labBusCliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labBusCliMouseClicked
    if (nne > 0) {
      if (conCli != null) {
        conCli.dispose();
      }
      conCli = new Consulta_Clientes(ctrN);
      conCli.setVisible(true);
      conCli.setExtendedState(NORMAL);
      conCli.setVisible(true);
    }
  }//GEN-LAST:event_labBusCliMouseClicked

  private void txtDiaVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiaVMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDiaVMouseClicked

  private void txtDiaVMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiaVMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDiaVMouseReleased

  private void txtDiaVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiaVActionPerformed
    validaFecVen();
  }//GEN-LAST:event_txtDiaVActionPerformed

  public void validaFecVen() {
    indok = 0;
    txtDiaV.setBorder(new LineBorder(Color.GRAY));
    String vax = txtDiaV.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtDiaV.setText(vax);
    }
    vax = txtMesV.getText();
    if (vax.length() == 1) {
      vax = "0" + vax;
      txtMesV.setText(vax);
    }

    fev = txtAnoV.getText() + txtMesV.getText() + txtDiaV.getText();

    if (fev.length() == 8) {
      if (isvalidFec(fev, 1)) {
        if (fne.length() > 0 && (fev.compareTo(fne) < 0)) {
          labMsg.setText("- Fecha vence " + ymd_dmy(fev) + " menor a Fecha Not Ent");
          indok = 1;
        } else {
          if (fne.equals(fee) || fee.length() == 0) {
            txtDiaR.setText(txtDiaN.getText());
            txtMesR.setText(txtMesN.getText());
            txtAnoR.setText(txtAnoN.getText());
          }

          txtDiaR.setSelectionStart(0);
          txtDiaR.setSelectionEnd(txtDiaR.getText().length());
          txtMesR.setSelectionStart(0);
          txtMesR.setSelectionEnd(txtMesR.getText().length());
          txtAnoR.setSelectionStart(0);
          txtAnoR.setSelectionEnd(txtAnoR.getText().length());

          txtDiaN.setBorder(new LineBorder(Color.GRAY));
          txtDiaR.setBorder(new LineBorder(Color.GRAY));
          labMsg.setText("- Ingrese Fecha Recibe");
          txtDiaR.requestFocus();

        }
      } else {
        indok = 1;
      }
    } else {
      indok = 1;
    }
    if (indok == 1) {
      seteaFecVen();
      txtDiaV.requestFocus();
    }
  }

  private void txtDiaVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiaVKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDiaVKeyReleased

  private void txtMesVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMesVMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtMesVMouseClicked

  private void txtMesVMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMesVMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtMesVMouseReleased

  private void txtMesVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMesVActionPerformed
    validaFecVen();
  }//GEN-LAST:event_txtMesVActionPerformed

  private void txtMesVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMesVKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtMesVKeyReleased

  private void txtAnoVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnoVMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtAnoVMouseClicked

  private void txtAnoVMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnoVMouseReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtAnoVMouseReleased

  private void txtAnoVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAnoVActionPerformed
    validaFecVen();
  }//GEN-LAST:event_txtAnoVActionPerformed

  private void txtAnoVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnoVKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtAnoVKeyReleased

  private void jLabTon7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabTon7MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabTon7MouseClicked

  private void txtDiaPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiaPKeyTyped
  }//GEN-LAST:event_txtDiaPKeyTyped

  private void jLabCte1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabCte1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabCte1MouseClicked

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
      java.util.logging.Logger.getLogger(Registro_NotaEntrega.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Registro_NotaEntrega.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Registro_NotaEntrega.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Registro_NotaEntrega.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registro_NotaEntrega().setVisible(true);
      }
    });
  }


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Jfecd;
  private javax.swing.JButton btnCal;
  private javax.swing.JButton btnEli;
  private javax.swing.JButton btnGra;
  private javax.swing.JButton btnSal;
  private javax.swing.JCheckBox cbFac;
  private elaprendiz.gui.varios.ClockDigital clockDigital1;
  private javax.swing.JLabel jLabCte;
  private javax.swing.JLabel jLabCte1;
  private javax.swing.JLabel jLabFac;
  private javax.swing.JLabel jLabFne;
  private javax.swing.JLabel jLabFrec;
  private javax.swing.JLabel jLabIva;
  private javax.swing.JLabel jLabNoe;
  private javax.swing.JLabel jLabNoe1;
  private javax.swing.JLabel jLabNoe2;
  private javax.swing.JLabel jLabTfac;
  private javax.swing.JLabel jLabTon;
  private javax.swing.JLabel jLabTon1;
  private javax.swing.JLabel jLabTon2;
  private javax.swing.JLabel jLabTon3;
  private javax.swing.JLabel jLabTon4;
  private javax.swing.JLabel jLabTon5;
  private javax.swing.JLabel jLabTon6;
  private javax.swing.JLabel jLabTon7;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabret;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JLabel labBusCli;
  private javax.swing.JLabel labBusNen;
  private javax.swing.JLabel labBusPed;
  private javax.swing.JLabel labDic;
  private javax.swing.JLabel labFac;
  private javax.swing.JLabel labIva;
  private javax.swing.JLabel labMsg;
  private javax.swing.JLabel labNoc;
  private javax.swing.JLabel labRet;
  private javax.swing.JLabel labTne;
  private javax.swing.JLabel labTpe;
  private javax.swing.JLabel labnfa;
  private javax.swing.JLabel labnre;
  private javax.swing.JLabel labojo;
  private javax.swing.JTextField txtAnoN;
  private javax.swing.JTextField txtAnoP;
  private javax.swing.JTextField txtAnoR;
  private javax.swing.JTextField txtAnoV;
  private javax.swing.JTextField txtCoc;
  private javax.swing.JTextField txtDiaN;
  private javax.swing.JTextField txtDiaP;
  private javax.swing.JTextField txtDiaR;
  private javax.swing.JTextField txtDiaV;
  private javax.swing.JTextField txtMesN;
  private javax.swing.JTextField txtMesP;
  private javax.swing.JTextField txtMesR;
  private javax.swing.JTextField txtMesV;
  private javax.swing.JTextField txtNfa;
  private javax.swing.JTextField txtNoe;
  private javax.swing.JTextField txtPed;
  private javax.swing.JTextField txtTdf;
  private javax.swing.JTextField txtTdn;
  private javax.swing.JTextField txtTne;
  private javax.swing.JTextField txtTof;
  // End of variables declaration//GEN-END:variables
}

/*  notaent (nne,nfa,npe,nfa,npe,coc,noc,fne,fee,tne,tdn,tfa,tdf,toi,tor,obs)

  sql = "CREATE TABLE notaent ("
          + "nne int,          " // nro not/ent
          + "nfa int,          " // nro factura
          + "npe int,          " // nro pedido
          + "coc VARCHAR(12),  " // codigo cliente
          + "noc VARCHAR(50),  " // nombre Cliente
          + "fne VARCHAR(08),  " // fecha not/ent
          + "fee VARCHAR(08),  " // fecha entega a cliente
          + "tne double,       " // total nota entrega
          + "tdn double,       " // total descuento nota
          + "tfa double,       " // total Base factura
          + "tdf double,       " // total descuento factura
          + "toi double,       " // total iva
          + "tor double,       " // total retencion
          + "obs VARCHAR(120), " // Observaciones
          + "PRIMARY KEY (nne,npe)"
          + ")";
 */
