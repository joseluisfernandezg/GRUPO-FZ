package modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

public class ReciboCobro {

  //recibocobroH
  private int nrc;    // nro recibo cobro
  private String frc; // fecha recibo cobro
  private String coc; // codigo cliente
  private String noc; // nombre Cliente
  private String sta; // status 0=pendiente 1= cerrado
  private String fre; // fecha registro  
  private String obs; // observaciones recibo
  private double tod; // total notas
  private double tpd; // total pagos $
  private double tpb; // total pagos Bs Ret iva

  //recibocobroD 
  private int tid;    // tipo docum 0=not / ent 1=not cred 2=not deb
  private int nno;    // numero nota  
  private int nfa;    // numero factura
  private String fno; // fecha nota 
  private String fen; // fecha entrega
  private String fev; // fecha vence
  private double tdo; // tota documento $
  private double tdn; // total descuento nota
  private double toi; // total iva Bs
  private double trd; // total retencion 25% $

  //recibocobroP
  private String fep; // fecha pago 
  private String tip; // tipo pago 0=abono,1=pago, 2=pago Ret Iva, 3=Saldo a favor, 4 Reintegro
  private String bce; // Banco emisor 
  private String bcr; // Banco receptor
  private String ref; // referencia pago
  private double tpa; // total pago
  private double tas; // Tasa cambio Bs

  // Constructor
  public ReciboCobro() {

  }

  // getReciboH
  public ReciboCobro(int nrc, String frc, String noc, double tod, double tpd, double tpb) {
    this.nrc = nrc;
    this.frc = frc;
    this.noc = noc;
    this.tod = tod;
    this.tpd = tpd;
    this.tpb = tpb;
  }

  // getReciboH
  public ObservableList<ReciboCobro> getReciboH(String Fed, String Feh, String fil, String sta, String ord) {
    ObservableList<ReciboCobro> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      if (con != null) {
        Statement st = con.createStatement();
        ResultSet rs = null;

        String vax = "";
        vax = " delete from recibocobroH where (nrc not in (select nrc from recibocobroP) and  nrc not in (select nrc from recibocobroD))";
        //st.execute(vax);
        vax = " delete from recibocobroD where nrc not in (select nrc from recibocobroH) ";
        st.execute(vax);
        vax = " delete from recibocobroP where nrc not in (select nrc from recibocobroH) ";
        //st.execute(vax);

        vax = " ";
        // Estatus Recibos
        if (sta.equals("P")) {
          vax = " and (tod-tpd) >0 ";
        }
        if (sta.equals("C")) {
          vax = " and  (tod-tpd) <=0  ";
        }
        // Orden Recibos
        String vax2 = "order by noc,nrc desc";
        if (ord.contains("0")) {
          vax2 = "order by nrc desc";
        }
        String sql = "SELECT nrc,frc,"
                + "(select nom from clientes where clientes.coc=recibocobroH.coc) noc,"
                + "tod,tpd,tpb "
                + "FROM recibocobroH "
                + "where frc between '" + Fed + "' and '" + Feh + "' "
                + "  and (noc like '%" + fil + "%' or coc like '%" + fil + "%') "
                + vax
                + vax2;
        //System.out.println("sql="+sql);
        rs = st.executeQuery(sql);
        while (rs.next()) {
          nrc = rs.getInt("nrc");
          frc = rs.getString("frc");
          noc = rs.getString("noc");
          tod = rs.getDouble("tod");
          tpd = rs.getDouble("tpd");
          tpb = rs.getDouble("tpb");
          ReciboCobro r = new ReciboCobro(nrc, frc, noc, tod, tpd, tpb);
          obs.add(r);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(ReciboCobro.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obs;
  }

  // getMaxRecCob
  public int getMaxRecCob() {
    int max = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT max(nrc) nrc "
              + "FROM recibocobroH";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        max = rs.getInt("nrc");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    return max;
  }

  // modificarReciboCobroH
  public ReciboCobro(int nrc, String frc, String obs) {
    this.nrc = nrc;
    this.frc = frc;
    this.obs = obs;
  }

  // modificarReciboCobroH
  public boolean modificarReciboCobroH(int ind) {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "update recibocobroH "
                + "set obs='" + obs + "' "
                + "Where nrc=" + nrc;
        if (ind == 1) {
          sql = "update recibocobroH "
                  + "set frc='" + frc + "',"
                  + "    obs='" + obs + "' "
                  + "Where nrc=" + nrc;
        }
        st.executeUpdate(sql);
        //System.out.println("N-modificarNotaEntrega()=" + sql);
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      cnn.close();
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  
 
  
  
  // Getter / Setter
  public int getNrc() {
    return nrc;
  }

  public void setNrc(int nrc) {
    this.nrc = nrc;
  }

  public String getFrc() {
    return frc;
  }

  public void setFrc(String frc) {
    this.frc = frc;
  }

  public String getCoc() {
    return coc;
  }

  public void setCoc(String coc) {
    this.coc = coc;
  }

  public String getNoc() {
    return noc;
  }

  public void setNoc(String noc) {
    this.noc = noc;
  }

  public String getSta() {
    return sta;
  }

  public void setSta(String sta) {
    this.sta = sta;
  }

  public String getFre() {
    return fre;
  }

  public void setFre(String fre) {
    this.fre = fre;
  }

  public double getTod() {
    return tod;
  }

  public void setTod(double tod) {
    this.tod = tod;
  }

  public double getTpd() {
    return tpd;
  }

  public void setTpd(double tpd) {
    this.tpd = tpd;
  }

  public double getTpb() {
    return tpb;
  }

  public void setTpb(double tpb) {
    this.tpb = tpb;
  }

  public int getTid() {
    return tid;
  }

  public void setTid(int tid) {
    this.tid = tid;
  }

  public int getNno() {
    return nno;
  }

  public void setNno(int nno) {
    this.nno = nno;
  }

  public int getNfa() {
    return nfa;
  }

  public void setNfa(int nfa) {
    this.nfa = nfa;
  }

  public String getFno() {
    return fno;
  }

  public void setFno(String fno) {
    this.fno = fno;
  }

  public String getFen() {
    return fen;
  }

  public void setFen(String fen) {
    this.fen = fen;
  }

  public String getFev() {
    return fev;
  }

  public void setFev(String fev) {
    this.fev = fev;
  }

  public double getTdo() {
    return tdo;
  }

  public void setTdo(double tdo) {
    this.tdo = tdo;
  }

  public double getTdn() {
    return tdn;
  }

  public void setTdn(double tdn) {
    this.tdn = tdn;
  }

  public double getToi() {
    return toi;
  }

  public void setToi(double toi) {
    this.toi = toi;
  }

  public double getTrd() {
    return trd;
  }

  public void setTrd(double trd) {
    this.trd = trd;
  }

  public String getFep() {
    return fep;
  }

  public void setFep(String fep) {
    this.fep = fep;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public String getBce() {
    return bce;
  }

  public void setBce(String bce) {
    this.bce = bce;
  }

  public String getBcr() {
    return bcr;
  }

  public void setBcr(String bcr) {
    this.bcr = bcr;
  }

  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public double getTpa() {
    return tpa;
  }

  public void setTpa(double tpa) {
    this.tpa = tpa;
  }

  public double getTas() {
    return tas;
  }

  public void setTas(double tas) {
    this.tas = tas;
  }

  public String getObs() {
    return obs;
  }

  public void setObs(String obs) {
    this.obs = obs;
  }

}
/*  recibocobroH
     + "nrc int,          " // nro recibo cobro
     + "frc VARCHAR(08),  " // fecha recibo cobro
     + "coc VARCHAR(12),  " // codigo cliente
     + "noc VARCHAR(50),  " // nombre Cliente
     + "tod decimal(15,2)," // total notas
     + "tpd decimal(15,2)," // total pagos $
     + "tpb decimal(15,2)," // total pagos Bs Ret iva
     + "sta VARCHAR(01),  " // status 0=pendiente 1= cerrado
     + "fre VARCHAR(12),  " // fecha registro  
  
  
    recibocobroD 
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
  
    recibocobroP
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
 */
