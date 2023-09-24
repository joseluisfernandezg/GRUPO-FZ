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

public class Usuarios {

  private String idu; // id iusuario
  private String cve; // clave
  private String rol; // rol usuario

  public Usuarios() {
  }

  public Usuarios(String idu) {
    this.idu = idu;
  }

  public Usuarios(String idu, String cve) {
    this.idu = idu;
    this.cve = cve;
  }

  // getUsuarios()
  public ObservableList<Usuarios> getUsuarios() {
    ObservableList<Usuarios> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        ResultSet rs = null;
        String sql = "SELECT idu "
                + "FROM Usuarios "
                + "Order by idu";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          idu = rs.getString("idu");
          Usuarios u = new Usuarios(idu);
          obs.add(u);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      cnn.close();
    } catch (SQLException ex) {
      Logger.getLogger(Usuarios.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obs;
  }

  // Clave accesos
  public boolean verificarClave() {
    boolean acceso = false;
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT idu, cve, rol "
              + "FROM Usuarios "
              + "Where idu='" + idu + "' "
              + "  and cve='" + cve + "'";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        idu = rs.getString("idu");
        cve = rs.getString("rol");
        acceso = true;
      }
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT idu,cve,rol FROM Usuarios", JOptionPane.ERROR_MESSAGE);
    }
    return acceso;
  }

  // modificarUsuario
  public boolean modificarUsuario() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();

        String sql = "update Usuarios "
                + "set cve='" + cve + "' "
                + "Where idu='" + idu + "' ";
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

  public String getRol() {
    return rol;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  public String getIdu() {
    return idu;
  }

  public void setIdu(String idu) {
    this.idu = idu;
  }

  public String getCve() {
    return cve;
  }

  public void setCve(String cve) {
    this.cve = cve;
  }

}

/*
Usuarios 
+ "idu VARCHAR(10),  " // id iusuario
+ "cve VARCHAR(10),  " // clave
+ "rol int,          " // rol usuario
 */
