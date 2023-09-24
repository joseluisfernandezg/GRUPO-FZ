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

public class EmpaqueProducto {

  // empaqueproducto 
  private String cop; // codigo
  private String emp; // capacidad
  private String ref; // referencia p.U. x Empaque
  private String unm; // unidad Medida venta
  private double can; // cantidad empaque
  private double por; // % comision

  public EmpaqueProducto() {
  }

  // getEmpaquePrd
  public EmpaqueProducto(String cop, String emp, String ref, String unm, double por, double can) {
    this.cop = cop;
    this.emp = emp;
    this.ref = ref;
    this.unm = unm;
    this.por = por;
    this.can = can;
  }

  //  existeProducto
  public boolean existeProducto(String cop) {
    int can = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT count(*) can "
              + "FROM empaqueproducto "
              + "where cop =  '" + cop + "' ";
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

  // getEmpaquePrd
  public ObservableList<EmpaqueProducto> getEmpaquePrd(String cop) {
    ObservableList<EmpaqueProducto> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        ResultSet rs = null;
        String sql = "SELECT tx1 emp,unm,ref,por,can "
                + "from empaqueproducto "
                + "Where cop='" + cop + "'";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          emp = rs.getString("emp");
          unm = rs.getString("unm");
          ref = rs.getString("ref");
          por = rs.getDouble("por");
          can = rs.getDouble("can");
          EmpaqueProducto u = new EmpaqueProducto(cop, emp, ref, unm, por, can);
          obs.add(u);
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

  // modificarUmEmp
  public EmpaqueProducto(String cop, String unm) {
    this.cop = cop;
    this.unm = unm;
  }

  // insertarEmpaque
  public boolean insertarEmpaque() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "Insert into empaqueproducto "
                + "(cop,tx1,unm,ref,por,can) "
                + "VALUES ("
                + "'" + cop + "',"
                + "'" + emp + "',"
                + "'" + unm + "',"
                + "'" + ref + "',"
                + "" + por + ","
                + "1" + ")";
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

  // modificarEmpaque
  public boolean modificarEmpaque(String cop) {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "Update empaqueproducto "
                + "set "
                + "tx1='" + emp + "',"
                + "ref='" + ref + "',"
                + "unm='" + unm + "',"
                + "por=" + por + " "
                + "where cop='" + cop + "'";
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

  // modificarUmEmp
  public boolean modificarUmEmp() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "Update empaqueproducto "
                + "set unm='" + unm + "' "
                + "where cop='" + cop + "'";
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

  // eliminarEmpaque
  public boolean eliminarEmpaque(String cop) {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "delete from empaqueproducto "
                + "where cop='" + cop + "'";
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

  public String getCop() {
    return cop;
  }

  public void setCop(String cop) {
    this.cop = cop;
  }

  public String getEmp() {
    return emp;
  }

  public void setEmp(String emp) {
    this.emp = emp;
  }

  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public String getUnm() {
    return unm;
  }

  public void setUnm(String unm) {
    this.unm = unm;
  }

  public double getCan() {
    return can;
  }

  public void setCan(double can) {
    this.can = can;
  }

  public double getPor() {
    return por;
  }

  public void setPor(double por) {
    this.por = por;
  }

}
