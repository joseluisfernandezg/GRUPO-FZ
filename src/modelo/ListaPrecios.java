package modelo;

import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

public class ListaPrecios {

  // listaprecios
  private String dep; // Departamento
  private String cop; // codigo producto
  private String cof; // codigo producto Busqueda
  private String nom; // nombre producto
  private double pum; // precio unitario Mayor
  private double pud; // precio unitario Detal
  private double stk; // stock existencia
  private String man; // art manual 0=lista, 1=manual
  private String pag; // Status  0=no 1=salta pagina
  private String sta; // Status  0=activo  1=inactivo
  private String fre; // fecha registro  

  public ListaPrecios() {
  }

  // getCargaProductosAct
  public ListaPrecios(String cop, String nom) {
    this.cop = cop;
    this.nom = nom;
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
              + "FROM Listaprecios "
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

  // getCargaProductosAct
  public ObservableList<ListaPrecios> getCargaProductosAct(String fil) {
    ObservableList<ListaPrecios> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        ResultSet rs = null;
        String sql = "SELECT cop,nom "
                + "FROM listaprecios "
                + "where (cof like '%" + fil + "%' or cop like '%" + fil + "%' or nom like '%" + fil + "%')  "
                + "and sta ='0' "
                + "order by nom";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          cop = rs.getString("cop");
          nom = rs.getString("nom");
          ListaPrecios p = new ListaPrecios(cop, nom);
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

  // getPreciosProductos
  public ListaPrecios(String cop, double pum, double pud) {
    this.cop = cop;
    this.pum = pum;
    this.pud = pud;
  }

  // getPreciosProductos
  public ObservableList<ListaPrecios> getPreciosProductos(String cop) {
    ObservableList<ListaPrecios> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        ResultSet rs = null;
        String sql = "SELECT cop,pum,pud from listaprecios "
                + "Where cop='" + cop + "'";
        rs = st.executeQuery(sql);
        while (rs.next()) {
          cop = rs.getString("cop");
          pum = rs.getDouble("pum");
          pud = rs.getDouble("pud");
          ListaPrecios p = new ListaPrecios(cop, pum, pud);
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

  // getProducto
  public String getProducto(String cod) {
    String nop = "";
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT nom "
              + "FROM listaprecios "
              + "where (cop = '" + cod + "' "
              + "    or cof = '" + cod + "')";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        nop = rs.getString("nom");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(ListaPrecios.class.getName()).log(Level.SEVERE, null, ex);
    }
    return nop;
  }

  // getTipProd
  public String getTipProd(String cod) {
    String man = "";
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT man "
              + "FROM listaprecios "
              + "where cop = '" + cod + "'";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        man = rs.getString("man");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(ListaPrecios.class.getName()).log(Level.SEVERE, null, ex);
    }
    return man;
  }

  // getExistencia
  public double getExistencia(String cop) {
    double stk = 0;
    ConexionSQL bdsql = new ConexionSQL();
    Connection con = bdsql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT stk from listaprecios "
              + "Where cop='" + cop + "'";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        stk = rs.getDouble("stk");
      }
      con.close(); // cerramos la conexion
      // caso errores sql
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
    }
    return stk;
  }

  // getPrecio
  public double getPrecio(String cop, String tip, double por) {
    double prc = 0;
    double pum = 0;
    double pud = 0;
    ConexionSQL bdsql = new ConexionSQL();
    Connection con = bdsql.Conectar();
    try {
      Statement st = con.createStatement();
      String sql = "SELECT pum, pud from listaprecios "
              + "Where cop='" + cop + "'";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        pum = rs.getDouble("pum");
        pud = rs.getDouble("pud");
      }
      prc = pum;              // precio mayor
      if (tip.equals("1")) {  // precio detal
        prc = pud;
      }
      double dsc = prc * por;
      String mox = MtoEs(dsc, 2);
      mox = GetCurrencyDouble(mox);
      dsc = GetMtoDouble(mox);
      prc = prc - dsc;

      con.close(); // cerramos la conexion
      // caso errores sql
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
    }
    return prc;
  }

  // eliminarProducto
  public boolean eliminarProducto(String cop) {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "delete from Listaprecios "
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

  public ListaPrecios(String dep, String cop, String cof, String nom, double pum, double pud, double stk, String man, String pag, String sta, String fre) {
    this.dep = dep;
    this.cop = cop;
    this.cof = cof;
    this.nom = nom;
    this.pum = pum;
    this.pud = pud;
    this.stk = stk;
    this.man = man;
    this.pag = pag;
    this.sta = sta;
    this.fre = fre;
  }

  // insertarProducto
  public boolean insertarProducto() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "Insert into Listaprecios "
                + "(dep,cop,cof,nom,pum,pud,stk,man,pag,fre,sta) "
                + "VALUES ("
                + "'" + dep + "',"
                + "'" + cop + "',"
                + "'" + cof + "',"
                + "'" + nom + "',"
                + "" + pum + ","
                + "" + pud + ","
                + "" + stk + ","
                + "'" + man + "',"
                + "'" + pag + "',"
                + "'" + fre + "'"
                + "'" + sta + "')";
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

  // modificarProdManual
  public boolean modificarProdAut(String cop) {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "update Listaprecios set "
                + "nom ='" + nom + "',"
                + "pag ='" + pag + "',"
                + "sta ='" + sta + "' "
                + "where cop =  '" + cop + "' ";
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

  // modificarProdManual
  public boolean modificarProdManual(String cop) {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "update Listaprecios set "
                + "dep ='" + dep + "',"
                + "nom ='" + nom + "',"
                + "pum =" + pum + ","
                + "pud =" + pud + ","
                + "stk =" + stk + ","
                + "man ='" + man + "',"
                + "pag ='" + pag + "',"
                + "sta ='" + sta + "' "
                + "where cop =  '" + cop + "' ";
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

  public String getDep() {
    return dep;
  }

  public void setDep(String dep) {
    this.dep = dep;
  }

  public String getCop() {
    return cop;
  }

  public void setCop(String cop) {
    this.cop = cop;
  }

  public String getCof() {
    return cof;
  }

  public void setCof(String cof) {
    this.cof = cof;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public double getPum() {
    return pum;
  }

  public void setPum(double pum) {
    this.pum = pum;
  }

  public double getPud() {
    return pud;
  }

  public void setPud(double pud) {
    this.pud = pud;
  }

  public double getStk() {
    return stk;
  }

  public void setStk(double stk) {
    this.stk = stk;
  }

  public String getMan() {
    return man;
  }

  public void setMan(String man) {
    this.man = man;
  }

  public String getPag() {
    return pag;
  }

  public void setPag(String pag) {
    this.pag = pag;
  }

  public String getSta() {
    return sta;
  }

  public void setSta(String sta) {
    this.sta = sta;
  }

  public String getFre() {
    return fre;
  }

  public void setFre(String fre) {
    this.fre = fre;
  }

}
