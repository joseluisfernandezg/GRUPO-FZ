//
//  Coneccion Base de datos (SQL Derby Local)
//
package modelo;

import java.sql.*;
import javax.swing.JOptionPane;

public class ConexionSQL {

  public String home = "";
  public String base = "";
  public String db = "BD";
  public String drv = "org.apache.derby.jdbc.EmbeddedDriver";
  public Connection con = null;

  public Connection Conectar() {
    home = "";
    base = "jdbc:derby:" + db + ";create=true";
    //System.out.println("base=" + base);
    Connection link = null;
    try {
      //Cargamos el Driver MySQL
      Class.forName(drv);
      //Creamos un enlace hacia la base de datos
      link = DriverManager.getConnection(base);
    } catch (ClassNotFoundException e) {
      JOptionPane.showMessageDialog(null, e);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e);
    }
    return link;
  }

  // Base datos
  public String getBasedatos() {
    return db;
  }

}
