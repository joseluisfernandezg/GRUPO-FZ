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

public class Departamento {

  //Departamento
  private String dep; // Departamento
  private String nom; // Nombre Nuevo Departamento
  private String ob1; // observacion 1
  private String ob2; // observacion 2

  public Departamento() {
  }

  public Departamento(String dep, String nom, String ob1, String ob2) {
    this.dep = dep;
    this.nom = nom;
    this.ob1 = ob1;
    this.ob2 = ob2;
  }

  // getObsListDepartd
  public ObservableList<Departamento> getDepartamento() {
    ObservableList<Departamento> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        ResultSet rs = null;
        String sql = "SELECT distinct dep,nom,ob1,ob2 "
                + "from departamento "
                + "order by dep";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          dep = rs.getString("dep");
          nom = rs.getString("nom");
          ob1 = rs.getString("ob1");
          ob2 = rs.getString("ob2");
          Departamento u = new Departamento(dep, nom, ob1, ob2);
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

  public String getDep() {
    return dep;
  }

  public void setDep(String dep) {
    this.dep = dep;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getOb1() {
    return ob1;
  }

  public void setOb1(String ob1) {
    this.ob1 = ob1;
  }

  public String getOb2() {
    return ob2;
  }

  public void setOb2(String ob2) {
    this.ob2 = ob2;
  }

}
