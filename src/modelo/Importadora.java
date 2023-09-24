package modelo;

import static comun.MetodosComunes.isNumeric;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

public class Importadora {

  //importadora
  private String rif; // Rif importadora
  private String nom; // nombre importadora
  private String dir; // Direccion
  private String tlf; // Telefono
  private String eml; // Email
  private String fec; // fecha lista dd/mm/yyyyy
  private double pod; // % descuento ppago con factura
  private double iva; // % Iva Actual
  private int dcr;    // Dias Credito
  private int np1;    // pedido desde
  private int np2;    // pedido hasta
  private int nr1;    // recibo desde
  private int nr2;    // recibo hasta
  private int ne1;    // not ent desde
  private int ne2;    // not ent hasta
  private int nc1;    // nc desde
  private int nc2;    // nc hasta

  public Importadora() {

  }

  public Importadora(String rif, String nom, String dir, String tlf, String eml, String fec, double pod,
          double iva, int dcr, int np1, int np2, int nr1, int nr2, int ne1, int ne2, int nc1, int nc2) {
    this.rif = rif;
    this.nom = nom;
    this.dir = dir;
    this.tlf = tlf;
    this.eml = eml;
    this.fec = fec;
    this.pod = pod;
    this.iva = iva;
    this.dcr = dcr;
    this.np1 = np1;
    this.np2 = np2;
    this.nr1 = nr1;
    this.nr2 = nr2;
    this.ne1 = ne1;
    this.ne2 = ne2;
    this.nc1 = nc1;
    this.nc2 = nc2;
  }

  // getObsListUnidmed
  public ObservableList<Importadora> getDatosImportadora() {
    ObservableList<Importadora> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        ResultSet rs = null;
        String sql = "SELECT rif,nom,dir,tlf,eml,fec,pod,iva,dcr,np1,np2,nr1,nr2,ne1,ne2,nc1,nc2 "
                + "FROM Importadora";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          rif = rs.getString("rif");
          nom = rs.getString("nom");
          dir = rs.getString("dir");
          tlf = rs.getString("tlf");
          eml = rs.getString("eml");
          fec = rs.getString("fec");
          pod = rs.getDouble("pod");
          iva = rs.getDouble("iva");
          dcr = rs.getInt("dcr");
          np1 = rs.getInt("np1");
          np2 = rs.getInt("np2");
          nr1 = rs.getInt("nr1");
          nr2 = rs.getInt("nr2");
          ne1 = rs.getInt("ne1");
          ne2 = rs.getInt("ne2");
          nc1 = rs.getInt("nc1");
          nc2 = rs.getInt("nc2");
          Importadora imp = new Importadora(rif, nom, dir, tlf, eml, fec, pod, iva, dcr, np1, np2, nr1, nr2, ne1, ne2, nc1, nc2);
          obs.add(imp);
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

  // insertarImportadora()
  public boolean insertarImportadora() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "Insert into Importadora "
                + "(rif,nom,dir,tlf,eml,fec,pod,iva,dcr,np1,np2,nr1,nr2,ne1,ne2,nc1,nc2) "
                + "VALUES ("
                + "'" + rif + "',"
                + "'" + nom + "',"
                + "'" + dir + "',"
                + "'" + tlf + "',"
                + "'" + eml + "',"
                + "'" + fec + "',"
                + "" + pod + ","
                + "" + iva + ","
                + "" + dcr + ","
                + "" + np1 + ","
                + "" + np2 + ","
                + "" + nr1 + ","
                + "" + nr2 + ","
                + "" + ne1 + ","
                + "" + ne2 + ","
                + "" + nc1 + ","
                + "" + nc2 + ")";
        st.executeUpdate(sql);
        //System.out.println(" sql="+sql);
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

  // eliminarImportadora
  public boolean eliminarImportadora() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
          String sql = "DELETE FROM Importadora";
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
  
  // Fecha ultima lista yyyymmdd
  public String getFecLis() {
    String fec = "";
    ConexionSQL bdsql = new ConexionSQL();
    Connection con = bdsql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "select fec "
              + "from importadora ";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        if (rs.getString("fec") != null) {
          fec = rs.getString("fec");
        }
      }
      con.close(); // cerramos la conexion
      // caso errores sql
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
    }
    return fec;
  }

  // Dias credito
  public int getDiasCred() {
    int dcr = 0;
    String dcx = "0";
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT dcr from importadora";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        dcx = rs.getString("dcr");
      }
      dcx = dcx.trim();
      if (isNumeric(dcx)) {
        dcr = Integer.valueOf(dcx);
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return dcr;
  }

  // % Descuento
  public double getPor() {
    double por = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT pod from importadora "
              + "FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        por = rs.getDouble("pod");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return por;
  }

  // Iva
  public double getIva() {
    double iva = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT iva from importadora "
              + "FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        iva = rs.getDouble("iva");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return iva;
  }

  // no pedido desde
  public int getNp1() {
    int np1 = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT np1 from importadora "
              + "FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        np1 = rs.getInt("np1");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return np1;
  }

  // no pedido hasta
  public int getNp2() {
    int np2 = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT np2 from importadora "
              + "FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        np2 = rs.getInt("np2");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return np2;
  }

  // no recibo desde
  public int getNr1() {
    int nr1 = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT nr1 from importadora "
              + "FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        nr1 = rs.getInt("nr1");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return nr1;
  }

  // no recibo hasta
  public int getNr2() {
    int nr2 = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT nr2 from importadora "
              + "FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        nr2 = rs.getInt("nr2");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return nr2;
  }

  // no not ent desde
  public int getNe1() {
    int ne1 = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT ne1 from importadora "
              + "FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        ne1 = rs.getInt("ne1");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return ne1;
  }

  // no not ent hasta
  public int getNe2() {
    int ne2 = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT ne2 from importadora "
              + "FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        ne2 = rs.getInt("ne2");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return ne2;
  }

  // no nc desde
  public int getNc1() {
    int nc1 = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT nc1 from importadora "
              + "FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        nc1 = rs.getInt("nc1");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return nc1;
  }

  // no nc hasta
  public int getNc2() {
    int nc2 = 0;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT nc2 from importadora "
              + "FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        nc2 = rs.getInt("nc2");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return nc2;
  }

  // Codigo vendedor
  public String getCodVend() {
    String cod = "";
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT cod from vendedor "
              + "FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        cod = rs.getString("cod");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return cod;
  }

  // Codigo vendedor
  public String getNomVend() {
    String nom = "";
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT nom from vendedor "
              + "FETCH FIRST 1 ROWS ONLY";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        nom = rs.getString("nom");
      }
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
    return nom;
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

  public String getTlf() {
    return tlf;
  }

  public void setTlf(String tlf) {
    this.tlf = tlf;
  }

  public String getEml() {
    return eml;
  }

  public void setEml(String eml) {
    this.eml = eml;
  }

  public String getFec() {
    return fec;
  }

  public void setFec(String fec) {
    this.fec = fec;
  }

  public double getPod() {
    return pod;
  }

  public void setPod(double pod) {
    this.pod = pod;
  }

  public int getDcr() {
    return dcr;
  }

  public void setDcr(int dcr) {
    this.dcr = dcr;
  }

}
