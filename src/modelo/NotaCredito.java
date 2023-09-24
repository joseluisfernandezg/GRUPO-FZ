package modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotaCredito {

  //notacred
  private int ncr; // nro ncred
  private int nne; // nro notent
  private int nfa; // nro factura
  private String coc; // codigo cliente
  private String noc; // nombre Cliente
  private String fnc; // fecha ncred
  private double tne; // total notent
  private double tnc; // total ncred
  private double toi; // total iva
  private double iva; // % Iva
  private double pre; // % retencion
  private double tor; // total retencion
  private String fre; // fecha registro 

  public NotaCredito() {
  }

  public NotaCredito(int ncr, int nne, int nfa, String coc, String noc, String fnc, double tne, double tnc, double toi, double iva, double pre, double tor, String fre) {
    this.ncr = ncr;
    this.nne = nne;
    this.nfa = nfa;
    this.coc = coc;
    this.noc = noc;
    this.fnc = fnc;
    this.tne = tne;
    this.tnc = tnc;
    this.toi = toi;
    this.iva = iva;
    this.pre = pre;
    this.tor = tor;
    this.fre = fre;
  }

  
  // getMaxNotCred
  public int getMaxNotCred() {
    int max = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT max(ncr) ncr "
              + "FROM notacred";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        max = rs.getInt("ncr");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    return max;
  }
  
  
  // existeNotCred
  public boolean existeNotCred(int ncr) {
    int can = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT count(*) can "
              + "from notacred "
              + "Where ncr=" + ncr;
      rs = st.executeQuery(sql);
      while (rs.next()) {
        can = rs.getInt("can");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (can > 0) {
      return true;
    } else {
      return false;
    }
  }
  
  // getReciboNotCred
  public int getReciboNotCred(int bcr) {
    int nrc = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT nrc "
              + "FROM recibocobroD "
              + "where tpd=1 "
              + "  and nno = " + ncr + " ";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        nrc = rs.getInt("nrc");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    return nrc;
  }
  
  
  //  getFacNotEnt
  public int getFacNotCred(int nfa) {
    int ncr = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT ncr  "
              + "FROM notacred "
              + "where nfa =  " + nfa + "";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        ncr = rs.getInt("ncr");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    return ncr;
  }
  
  
  
}


