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

public class UnidadMedida {
  private String unm;
  private int sec;

  public UnidadMedida() {
  }

  public UnidadMedida(String unm, int sec) {
    this.unm = unm;
    this.sec = sec;
  }

    //  Existe Cliente
  public boolean existeUnidadMedida() {
    String cod = "";
    ConexionSQL mysql = new ConexionSQL();
    Connection cnn = mysql.Conectar();
    if (cnn != null) {
      try {
        Statement st = cnn.createStatement();
        String sql = "SELECT unm from unidmed "
                + "Where unm='" + unm + "'";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
          cod = rs.getString("unm");
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
  
  // getObsListUnidmed
  public ObservableList<UnidadMedida> getObsListUnidmed() {
    ObservableList<UnidadMedida> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        ResultSet rs = null;
        String sql = "SELECT unm,sec from unidmed "
                + "order by sec";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          unm = rs.getString("unm");
          sec = rs.getInt("sec");
         UnidadMedida u = new UnidadMedida(unm, sec);
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

  public String getUnm() {
    return unm;
  }

  public void setUnm(String unm) {
    this.unm = unm;
  }

  public int getSec() {
    return sec;
  }

  public void setSec(int sec) {
    this.sec = sec;
  }

}

