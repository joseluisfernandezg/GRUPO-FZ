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

public class NotaEntrega {

  //notaent
  private int nne; // nro not/ent
  private int nfa; // nro factura
  private int npe; // nro pedido
  private String coc; // codigo cliente
  private String noc; // nombre Cliente
  private String fne; // fecha not/ent
  private String fep; // fecha pedido
  private String fee; // fecha entrega a cliente
  private String fev; // fecha vence
  private double tpe; // total pedido
  private double tne; // total nota entrega
  private double tdn; // total descuento nota
  private double tfa; // total Base factura
  private double tdf; // total descuento factura
  private double toi; // total iva
  private double iva; // % Iva
  private double pre; // % retencion
  private double tor; // total retencion
  private String obs; // Observaciones
  private String fre; // fecha registro  

  public NotaEntrega() {
  }

  public NotaEntrega(int nne) {
    this.nne = nne;
  }

  // Busca ultimo No. NotEnt
  public int getMaxNotEnt() {
    int max = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT max(nne) nne "
              + "FROM notaent";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        max = rs.getInt("nne");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    return max;
  }

  // existeNotEnt
  public boolean existeNotEnt(int nne) {
    int can = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT count(*) can "
              + "from notaent "
              + "Where nne=" + nne;
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

  // getReciboNotEnt
  public int getReciboNotEnt(int nne) {
    int nrc = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT nrc "
              + "FROM recibocobroD "
              + "where tpd=0 "
              + "  and nno = " + nne + " ";
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
  public int getFacNotEnt(int nfa) {
    int nne = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT nne  "
              + "FROM notaent "
              + "where nfa =  " + nfa + "";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        nne = rs.getInt("nne");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    return nne;
  }

  // getDatNotEnPed 
  public NotaEntrega(int nne, String fne, String noc) {
    this.nne = nne;
    this.fne = fne;
    this.noc = noc;
  }

  // getDatNotEnPed(npe)
  public ObservableList<NotaEntrega> getDatNotEnPed(int npe) {
    ObservableList<NotaEntrega> obl = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      if (con != null) {
        Statement st = con.createStatement();
        ResultSet rs = null;
        String sql = "SELECT nne,fne,"
                + "(select nom from clientes where clientes.coc=notaent.coc) noc "
                + "FROM notaent "
                + "where npe =  " + npe
                + "  and npe<>0";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          nne = rs.getInt("nne");
          noc = rs.getString("noc");
          fne = rs.getString("fne");
          NotaEntrega n = new NotaEntrega(nne, fne, noc);
          obl.add(n);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obl;
  }

  // getDatosNotEnt
  public NotaEntrega(int nne, int nfa, int npe, String coc, String noc, String fne,
          String fep, String fee, String fev, String fre, String obs, double tpe, double tne, double tdn,
          double tfa, double tdf, double pre, double toi, double iva, double tor) {
    this.nne = nne;
    this.nfa = nfa;
    this.npe = npe;
    this.coc = coc;
    this.noc = noc;
    this.fne = fne;
    this.fep = fep;
    this.fee = fee;
    this.fev = fev;
    this.fre = fre;
    this.obs = obs;
    this.tpe = tpe;
    this.tne = tne;
    this.tdn = tdn;
    this.tfa = tfa;
    this.tdf = tdf;
    this.pre = pre;
    this.toi = toi;
    this.iva = iva;
    this.tor = tor;
  }

  // getDatNotEnPed
  public ObservableList<NotaEntrega> getDatosNotEnt(int nne) {
    ObservableList<NotaEntrega> obl = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      if (con != null) {
        Statement st = con.createStatement();
        ResultSet rs = null;
        String sql = "SELECT nne,nfa,npe,coc,"
                + "(select nom from clientes where clientes.coc=notaent.coc) noc,"
                + "fne,fee,fev,fep,tpe,tne,tdn,tfa,tdf,toi,iva,pre,tor,obs,fre "
                + "FROM notaent "
                + "where nne =  " + nne + " ";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          nfa = rs.getInt("nfa");
          npe = rs.getInt("npe");
          coc = rs.getString("coc");
          noc = rs.getString("noc");
          fne = rs.getString("fne");
          fee = rs.getString("fee");
          fev = rs.getString("fev");
          fep = rs.getString("fep");
          fre = rs.getString("fre");
          obs = rs.getString("obs");
          tpe = rs.getDouble("tpe");
          tne = rs.getDouble("tne");
          tdn = rs.getDouble("tdn");
          tfa = rs.getDouble("tfa");
          tdf = rs.getDouble("tdf");
          pre = rs.getDouble("pre");
          toi = rs.getDouble("toi");
          iva = rs.getDouble("iva");
          tor = rs.getDouble("tor");
          NotaEntrega n = new NotaEntrega(nne, nfa, npe, coc, noc, fne, fep, fee, fev, fre, obs, tpe, tne, tdn, tfa, tdf, pre, toi, iva, tor);
          obl.add(n);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obl;
  }

  // getNoeFac
  public ObservableList<NotaEntrega> getNoeFac(int nrc, String tip) {
    ObservableList<NotaEntrega> obl = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      if (con != null) {
        // Nota $
        String sql = "SELECT distinct nno "
                + "FROM recibocobroD "
                + "Where nrc=" + nrc + " "
                + "  and tpd=0 "
                + "  and nno>0 "
                + "order by nno";
        // Factura Bs
        if (tip.equals("2") || tip.equals("4")) {
          sql = "SELECT distinct nfa nno "
                  + "FROM recibocobroD "
                  + "Where nrc=" + nrc + " "
                  + "  and tpd=0 "
                  + "  and nfa>0 "
                  + "order by nfa";
        }
        Statement st = con.createStatement();
        ResultSet rs = null;
        rs = st.executeQuery(sql);
        while (rs.next()) {
          nne = rs.getInt("nno");
          NotaEntrega n = new NotaEntrega(nne);
          obl.add(n);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(NotaEntrega.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obl;
  }

  // insertarNotaEntrega
  public boolean insertarNotaEntrega() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "Insert into notaent "
                + "(nne,nfa,npe,coc,noc,fne,fep,fee,fev,tpe,tne,tdn,tfa,tdf,toi,iva,pre,tor,obs,fre) "
                + "VALUES ("
                + "" + nne + ","
                + "" + nfa + ","
                + "" + npe + ","
                + "'" + coc + "',"
                + "'" + noc + "',"
                + "'" + fne + "',"
                + "'" + fep + "',"
                + "'" + fee + "',"
                + "'" + fev + "',"
                + "" + tpe + ","
                + "" + tne + ","
                + "" + tdn + ","
                + "" + tfa + ","
                + "" + tdf + ","
                + "" + toi + ","
                + "" + iva + ","
                + "" + pre + ","
                + "" + tor + ","
                + "'" + obs + "',"
                + "'" + fre + "')";
        st.executeUpdate(sql);
        //System.out.println("N-insertarNotaEntrega()=" + sql);
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

  // modificarNotaEntrega
  public boolean modificarNotaEntrega() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "update notaent set "
                + "nfa =" + nfa + ","
                + "npe =" + npe + ","
                + "coc ='" + coc + "',"
                + "noc ='" + noc + "',"
                + "fne ='" + fne + "',"
                + "fep ='" + fep + "',"
                + "fee ='" + fee + "',"
                + "fev ='" + fev + "',"
                + "tpe =" + tpe + ","
                + "tne =" + tne + ","
                + "tdn =" + tdn + ","
                + "tfa =" + tfa + ","
                + "tdf =" + tdf + ","
                + "iva =" + iva + ","
                + "toi =" + toi + ","
                + "pre =" + pre + ","
                + "tor =" + tor + ","
                + "obs ='" + obs + "' "
                + "where nne =  " + nne;
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

  // eliminarNotaEntrega()
  public boolean eliminarNotaEntrega() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "delete from notaent "
                + "where nne=" + nne;
        st.executeUpdate(sql);
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

  public int getNne() {
    return nne;
  }

  public void setNne(int nne) {
    this.nne = nne;
  }

  public int getNfa() {
    return nfa;
  }

  public void setNfa(int nfa) {
    this.nfa = nfa;
  }

  public int getNpe() {
    return npe;
  }

  public void setNpe(int npe) {
    this.npe = npe;
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

  public String getFne() {
    return fne;
  }

  public void setFne(String fne) {
    this.fne = fne;
  }

  public String getFep() {
    return fep;
  }

  public void setFep(String fep) {
    this.fep = fep;
  }

  public String getFee() {
    return fee;
  }

  public void setFee(String fee) {
    this.fee = fee;
  }

  public String getFev() {
    return fev;
  }

  public void setFev(String fev) {
    this.fev = fev;
  }

  public double getTpe() {
    return tpe;
  }

  public void setTpe(double tpe) {
    this.tpe = tpe;
  }

  public double getTne() {
    return tne;
  }

  public void setTne(double tne) {
    this.tne = tne;
  }

  public double getTdn() {
    return tdn;
  }

  public void setTdn(double tdn) {
    this.tdn = tdn;
  }

  public double getTfa() {
    return tfa;
  }

  public void setTfa(double tfa) {
    this.tfa = tfa;
  }

  public double getTdf() {
    return tdf;
  }

  public void setTdf(double tdf) {
    this.tdf = tdf;
  }

  public double getToi() {
    return toi;
  }

  public void setToi(double toi) {
    this.toi = toi;
  }

  public double getIva() {
    return iva;
  }

  public void setIva(double iva) {
    this.iva = iva;
  }

  public double getPre() {
    return pre;
  }

  public void setPre(double pre) {
    this.pre = pre;
  }

  public double getTor() {
    return tor;
  }

  public void setTor(double tor) {
    this.tor = tor;
  }

  public String getObs() {
    return obs;
  }

  public void setObs(String obs) {
    this.obs = obs;
  }

  public String getFre() {
    return fre;
  }

  public void setFre(String fre) {
    this.fre = fre;
  }
}
