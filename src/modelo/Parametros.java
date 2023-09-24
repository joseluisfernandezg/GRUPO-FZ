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

public class Parametros {

  String p01;
  String p02;
  String p03;
  String p04;
  String p05;
  String p06;
  String p07;
  String p08;
  String p09;
  String p10;
  String p11;
  String p12;
  String p13;
  String p14;
  String p15;
  String p16;

  public Parametros() {
  }

  public Parametros(String p01, String p02, String p03, String p04, String p05, String p06, String p07, String p08, String p09, String p10, String p11, String p12, String p13, String p14, String p15, String p16) {
    this.p01 = p01;
    this.p02 = p02;
    this.p03 = p03;
    this.p04 = p04;
    this.p05 = p05;
    this.p06 = p06;
    this.p07 = p07;
    this.p08 = p08;
    this.p09 = p09;
    this.p10 = p10;
    this.p11 = p11;
    this.p12 = p12;
    this.p13 = p13;
    this.p14 = p14;
    this.p15 = p15;
    this.p16 = p16;
  }

  // getBuscaParametros 
  public ObservableList<Parametros> getBuscaParametros() {
    ObservableList<Parametros> obl = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      if (con != null) {
        Statement st = con.createStatement();
        ResultSet rs = null;
        String sql = "SELECT p01, p02, p03, p04, p05, p06, p07, p08, p09, p10, p11, p12, p13, p14, p15, p16 "
                + "FROM parametros";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          p01 = rs.getString("p01");
          p02 = rs.getString("p02");
          p03 = rs.getString("p03");
          p04 = rs.getString("p04");
          p05 = rs.getString("p05");
          p06 = rs.getString("p06");
          p07 = rs.getString("p07");
          p08 = rs.getString("p08");
          p09 = rs.getString("p09");
          p10 = rs.getString("p10");
          p11 = rs.getString("p11");
          p12 = rs.getString("p12");
          p13 = rs.getString("p13");
          p14 = rs.getString("p14");
          p15 = rs.getString("p15");
          p16 = rs.getString("p16");
          Parametros p = new Parametros(p01, p02, p03, p04, p05, p06, p07, p08, p09, p10, p11, p12, p13, p14, p15, p16);
          obl.add(p);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Parametros.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obl;
  }

  // modificarparametros
  public boolean modificarParametros() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "update parametros "
                + "set "
                + "p01='" + p01 + "',"
                + "p02='" + p02 + "',"
                + "p03='" + p03 + "',"
                + "p04='" + p04 + "',"
                + "p05='" + p05 + "',"
                + "p06='" + p06 + "',"
                + "p07='" + p07 + "',"
                + "p08='" + p08 + "',"
                + "p09='" + p09 + "',"
                + "p10='" + p10 + "',"
                + "p11='" + p11 + "',"
                + "p12='" + p12 + "',"
                + "p13='" + p13 + "',"
                + "p14='" + p14 + "',"
                + "p15='" + p15 + "',"
                + "p16='" + p16 + "'";
        st.executeUpdate(sql);
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      cnn.close();
    } catch (Exception e) {
      System.out.println("e=" + e.getMessage());
      return false;
    }
    return true;
  }

  public String getP01() {
    return p01;
  }

  public void setP01(String p01) {
    this.p01 = p01;
  }

  public String getP02() {
    return p02;
  }

  public void setP02(String p02) {
    this.p02 = p02;
  }

  public String getP03() {
    return p03;
  }

  public void setP03(String p03) {
    this.p03 = p03;
  }

  public String getP04() {
    return p04;
  }

  public void setP04(String p04) {
    this.p04 = p04;
  }

  public String getP05() {
    return p05;
  }

  public void setP05(String p05) {
    this.p05 = p05;
  }

  public String getP06() {
    return p06;
  }

  public void setP06(String p06) {
    this.p06 = p06;
  }

  public String getP07() {
    return p07;
  }

  public void setP07(String p07) {
    this.p07 = p07;
  }

  public String getP08() {
    return p08;
  }

  public void setP08(String p08) {
    this.p08 = p08;
  }

  public String getP09() {
    return p09;
  }

  public void setP09(String p09) {
    this.p09 = p09;
  }

  public String getP10() {
    return p10;
  }

  public void setP10(String p10) {
    this.p10 = p10;
  }

  public String getP11() {
    return p11;
  }

  public void setP11(String p11) {
    this.p11 = p11;
  }

  public String getP12() {
    return p12;
  }

  public void setP12(String p12) {
    this.p12 = p12;
  }

  public String getP13() {
    return p13;
  }

  public void setP13(String p13) {
    this.p13 = p13;
  }

  public String getP14() {
    return p14;
  }

  public void setP14(String p14) {
    this.p14 = p14;
  }

  public String getP15() {
    return p15;
  }

  public void setP15(String p15) {
    this.p15 = p15;
  }

  public String getP16() {
    return p16;
  }

  public void setP16(String p16) {
    this.p16 = p16;
  }

}
