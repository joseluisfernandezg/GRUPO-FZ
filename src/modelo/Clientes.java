package modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

public class Clientes {

  // Tabla Clientes
  private String coc; // codigo cliente
  private String rif; // rif  cliente
  private String nom; // nombre cliente
  private String dir; // direccion
  private String edo; // Estado
  private String ciu; // Ciudad
  private String tlf; // telefono
  private String con; // contacto
  private String eml; // email
  private String tip; // tipo 0=Mayorista 1=Detal
  private String cop; // 0=Prepago 1=credito 30 dias, 2 credito 15 dias 
  private String fac; // 0=si 1=no (5%) Lista precios
  private String fec; // fecha creacion
  private String sta; // 0=activo, 1=inactivo
  private double pre; // % Retencion iva
  private double dic; // dias credito

  public Clientes() {
  }

  public Clientes(String coc) {
    this.coc = coc;
  }

  // Dias credito
  public int getDiasCred() {
    int dcr = 0;
    double dcx = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT dic from clientes "
              + "where coc='" + this.coc + "'";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        dcx = rs.getDouble("dic");
      }
      dcr = (int) dcx;
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return dcr;
  }

  // getObsListClientes
  public Clientes(String coc, String nom, String rif, String tlf, String dir, String edo, String ciu, String con,
          String tip, String cop, String eml, double pre, String fac, String fec, double dic, String sta) {
    this.coc = coc;
    this.nom = nom;
    this.rif = rif;
    this.tlf = tlf;
    this.dir = dir;
    this.edo = edo;
    this.ciu = ciu;
    this.con = con;
    this.tip = tip;
    this.cop = cop;
    this.eml = eml;
    this.pre = pre;
    this.fac = fac;
    this.fec = fec;
    this.dic = dic;
    this.sta = sta;
  }

  //getObsListClientes
  public ObservableList<Clientes> getObsListClientes(String fil, String cod, String sta) {
    ObservableList<Clientes> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        ResultSet rs = null;

        String vax = "where (nom like '%" + fil + "%' or coc like '%" + fil + "%') "
                + "and sta like '%" + sta + "%' order by nom";
        if (cod.length() > 0) {
          vax = "where coc =  '" + cod + "' ";
        }
        String sql = "SELECT coc,nom,rif,tlf,dir,edo,ciu,con,tip,cop,eml,pre,fac,fec,dic,sta "
                + "FROM clientes "
                + vax;

        rs = st.executeQuery(sql);
        while (rs.next()) {
          coc = rs.getString("coc");
          nom = rs.getString("nom");
          rif = rs.getString("rif");
          tlf = rs.getString("tlf");
          dir = rs.getString("dir");
          edo = rs.getString("edo");
          ciu = rs.getString("ciu");
          con = rs.getString("con");
          tip = rs.getString("tip");
          cop = rs.getString("cop");
          eml = rs.getString("eml");
          fac = rs.getString("fac");
          fec = rs.getString("fec");
          sta = rs.getString("sta");
          pre = rs.getDouble("pre");
          dic = rs.getDouble("dic");
          Clientes p = new Clientes(coc, nom, rif, tlf, dir, edo, ciu, con, tip, cop, eml, pre, fac, fec, dic, sta);
          obs.add(p);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      cnn.close();
    } catch (SQLException ex) {
      Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obs;
  }

  // getObsTipCliente
  public Clientes(String coc, String nom, String tip, String fac, double pre, double dic) {
    this.coc = coc;
    this.nom = nom;
    this.tip = tip;
    this.fac = fac;
    this.pre = pre;
    this.dic = dic;
  }

  // getObsTipCliente
  public ObservableList<Clientes> getObsTipCliente(String coc) {
    ObservableList<Clientes> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        ResultSet rs = null;
        String sql = "SELECT coc,nom,tip,fac,pre,dic "
                + "FROM clientes where coc='" + coc + "'";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          coc = rs.getString("coc");
          nom = rs.getString("nom");
          tip = rs.getString("tip");
          fac = rs.getString("fac");
          pre = rs.getDouble("pre");
          dic = rs.getDouble("dic");
          Clientes p = new Clientes(coc, nom, tip, fac, pre,dic);
          obs.add(p);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      cnn.close();
    } catch (SQLException ex) {
      Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obs;
  }

  //  Existe Cliente
  public boolean existeCliente() {
    String cod = "";
    ConexionSQL mysql = new ConexionSQL();
    Connection cnn = mysql.Conectar();
    if (cnn != null) {
      try {
        Statement st = cnn.createStatement();
        String sql = "SELECT coc from Clientes "
                + "Where coc='" + coc + "'";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
          cod = rs.getString("coc");
        }
        rs.close();
        cnn.close(); // cerramos la conexion
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT coc from Clientes", JOptionPane.ERROR_MESSAGE);
      }
    }
    if (cod.length() > 0) {
      return true;
    } else {
      return false;
    }
  }

  // Crea Clienbte Nuevo
  public boolean insertarCliente() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "Insert into Clientes "
                + "(coc,rif,nom,dir,edo,ciu,tlf,con,eml,tip,cop,fac,fec,pre,dic,sta) "
                + "VALUES ("
                + "'" + coc + "',"
                + "'" + rif + "',"
                + "'" + nom + "',"
                + "'" + dir + "',"
                + "'" + edo + "',"
                + "'" + ciu + "',"
                + "'" + tlf + "',"
                + "'" + con + "',"
                + "'" + eml + "',"
                + "'" + tip + "',"
                + "'" + cop + "',"
                + "'" + fac + "',"
                + "'" + fec + "',"
                + " " + pre + ","
                + " " + dic + ","
                + "'" + sta + "')";
        //System.out.println("--insertarCliente()=" + sql);
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

  // Modificar Cliente
  public boolean modificarCliente() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        // Modificar
        String sql = "update Clientes set "
                + "rif ='" + rif + "',"
                + "nom ='" + nom + "',"
                + "dir ='" + dir + "',"
                + "edo ='" + edo + "',"
                + "ciu ='" + ciu + "',"
                + "tlf ='" + tlf + "',"
                + "con ='" + con + "',"
                + "eml ='" + eml + "',"
                + "tip ='" + tip + "',"
                + "cop ='" + cop + "',"
                + "fac ='" + fac + "',"
                + "fec ='" + fec + "',"
                + "pre =" + pre + ","
                + "dic =" + dic + ","
                + "sta ='" + sta + "' "
                + "where coc =  '" + coc + "' ";
        //System.out.println("modificarCliente()=" + sql);
        st.executeUpdate(sql);

        String cox = rif.replace("-", "");
        cox = cox.substring(1, cox.length());
        if (!coc.equals(cox)) {
          st.execute("update Clientes     set coc ='" + cox + "' where coc =  '" + coc + "' ");
          st.execute("update pedidoH      set coc ='" + cox + "' where coc =  '" + coc + "' ");
          st.execute("update notaent      set coc ='" + cox + "' where coc =  '" + coc + "' ");
          st.execute("update notacred     set coc ='" + cox + "' where coc =  '" + coc + "' ");
          st.execute("update recibocobroH set coc ='" + cox + "' where coc =  '" + coc + "' ");
          st.execute("update recibocobroD set coc ='" + cox + "' where coc =  '" + coc + "' ");
          st.execute("update recibocobroP set coc ='" + cox + "' where coc =  '" + coc + "' ");
        }

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

  // Eliminar CLiente
  public boolean eliminarCliente() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "DELETE from  Clientes "
                + "Where coc='" + coc + "'";
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

  // Cliente con Movimiento
  public boolean tieneNota() {
    int can = 0;
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "SELECT count(*) can  from notaent  "
                + "Where coc='" + coc + "'";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
          can = rs.getInt("can");
        }

        st.executeUpdate(sql);
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      cnn.close();
    } catch (Exception e) {
    }
    if (can == 0) {
      return false;
    } else {
      return true;
    }
  }

  // Array Clientes
  public List<String> getDatosCliente(String cod) {
    List<String> list = new ArrayList<>();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        ResultSet rs = null;
        String sql = "SELECT coc,rif,nom,dir,edo,ciu,tlf,con,eml,tip,cop,fac,fec,pre,sta "
                + "FROM Clientes "
                + "where coc =  '" + cod + "' ";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          list.add(0, rs.getString(1));
          list.add(1, rs.getString(2));
          list.add(2, rs.getString(3));
          list.add(3, rs.getString(4));
          list.add(4, rs.getString(5));
          list.add(5, rs.getString(6));
          list.add(6, rs.getString(7));
          list.add(7, rs.getString(8));
          list.add(8, rs.getString(9));
          list.add(9, rs.getString(10));
          list.add(10, rs.getString(11));
          list.add(11, rs.getString(12));
          list.add(12, rs.getString(13));
          list.add(13, rs.getString(14));
          list.add(14, rs.getString(15));
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      cnn.close();

    } catch (SQLException ex) {
      Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
    }
    return list;
  }

  public String getCoc() {
    return coc;
  }

  public void setCoc(String coc) {
    this.coc = coc;
  }

  public String getRif() {
    return rif;
  }

  public void setRif(String rif) {
    this.rif = rif;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getDir() {
    return dir;
  }

  public void setDir(String dir) {
    this.dir = dir;
  }

  public String getEdo() {
    return edo;
  }

  public void setEdo(String edo) {
    this.edo = edo;
  }

  public String getCiu() {
    return ciu;
  }

  public void setCiu(String ciu) {
    this.ciu = ciu;
  }

  public String getTlf() {
    return tlf;
  }

  public void setTlf(String tlf) {
    this.tlf = tlf;
  }

  public String getCon() {
    return con;
  }

  public void setCon(String con) {
    this.con = con;
  }

  public String getEml() {
    return eml;
  }

  public void setEml(String eml) {
    this.eml = eml;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public String getCop() {
    return cop;
  }

  public void setCop(String cop) {
    this.cop = cop;
  }

  public String getFac() {
    return fac;
  }

  public void setFac(String fac) {
    this.fac = fac;
  }

  public String getFec() {
    return fec;
  }

  public void setFec(String fec) {
    this.fec = fec;
  }

  public String getSta() {
    return sta;
  }

  public void setSta(String sta) {
    this.sta = sta;
  }

  public double getPre() {
    return pre;
  }

  public void setPre(double pre) {
    this.pre = pre;
  }

  public double getDic() {
    return dic;
  }

}
