package modelo;

import static comun.MetodosComunes.getdiasFec;
import static comun.MetodosComunes.ymd_dmyc;
import static comun.MetodosComunes.ymdhoy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

public class Pedidos {

  // pedidoH
  private int npe;   // nro pedido cliente
  private String coc; // codigo cliente
  private String noc; // nombre Cliente
  private String tip; // tipo 0=Mayorista 1=Detal
  private String fac; // Con Factura S/N
  private String fep; // fecha pedido
  private String fel; // fecha lista precios
  private String obs; // Observaciones
  private String fre; // fecha registro  
  private double por; // % descuento ppago
  private double iva; // % Iva
  private double pre; // % retencion Iva
  private double ppm; // % Promocion global

  // pedidoD
  private String cop; // codigo producto
  private String dep; // descripcion producto
  private String unm; // unidad Medida venta
  private double can; // cantidad producto
  private double prm; // precio Mayor
  private double prd; // precio Detal

  //Otros
  private int nne;    // no nota entrega
  private int ndi;    // Dias Pedido - Nota Entrega
  private String fne; // fecha nota entrega         
  private String grp; // Grupo Promocion
  private String unx; // unidad medida a modificar
  private double cnn; // Cantidad notas
  private double cat; // Cantidad Promocion
  private double poi; // % Iva 
  private double cax; // Cantidad pedido modificar
  private double ppp; // % pronto pago
  private float tpe;  // total pedido

  public Pedidos() {
  }

  // existePedido(npe) / eliminarPedidoD(npe)
  public Pedidos(int npe, String cop) {
    this.npe = npe;
    this.cop = cop;
  }

  //  existePedido
  public boolean existePedido(int npe) {
    int can = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT count(*) can from pedidoH "
              + "Where npe=" + npe;
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

  // eliminarPedidoD
  public boolean eliminarPedidoD() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "delete from pedidoD "
                + "where npe=" + npe + " "
                + "  and cop =  '" + cop + "' ";
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

  // eliminarPedidoH(npe)
  public Pedidos(int npe) {
    this.npe = npe;
  }

  // eliminarPedidH()
  public boolean eliminarPedidoH() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "delete from pedidoH "
                + "where npe=" + npe;
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

  // getBuscaPedido(npe)
  public Pedidos(int npe, String coc, String noc, String tip, String obs, String fep, String fel, String fac, double ppm, double por) {
    this.npe = npe;
    this.coc = coc;
    this.noc = noc;
    this.tip = tip;
    this.obs = obs;
    this.fep = fep;
    this.fel = fel;
    this.fac = fac;
    this.ppm = ppm;
    this.por = por;
  }

  // getBuscaPedido 
  public ObservableList<Pedidos> getBuscaPedidoH(int npe) {
    ObservableList<Pedidos> obl = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      if (con != null) {
        Statement st = con.createStatement();
        ResultSet rs = null;

        String sql = "SELECT coc,"
                + "(select nom from clientes where clientes.coc=pedidoH.coc) noc,"
                + "tip,fep,fel,fac,ppm,obs,por "
                + "FROM pedidoH "
                + "where npe =  " + npe + " ";

        rs = st.executeQuery(sql);
        while (rs.next()) {
          coc = rs.getString("coc");
          noc = rs.getString("noc");
          tip = rs.getString("tip");
          obs = rs.getString("obs");
          fep = rs.getString("fep");
          fel = rs.getString("fel");
          fac = rs.getString("fac");
          ppm = rs.getDouble("ppm");
          por = rs.getDouble("por");
          Pedidos p = new Pedidos(npe, coc, noc, tip, obs, fep, fel, fac, ppm, por);
          obl.add(p);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obl;
  }

  // getPedidoH(npe)   
  public Pedidos(int npe, String fep, String noc, int nne, String fne, int ndi, double cnn, float tpe) {
    this.npe = npe;
    this.fep = fep;
    this.noc = noc;
    this.nne = nne;
    this.fne = fne;
    this.ndi = ndi;
    this.cnn = cnn;
    this.tpe = tpe;
  }

  // getPedidoH
  public ObservableList<Pedidos> getPedidoH(String Fed, String Feh, String fil, String sta, String ord) {
    ObservableList<Pedidos> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      if (con != null) {
        Statement st = con.createStatement();
        Statement st2 = con.createStatement();
        ResultSet rs = null;
        ResultSet rs2 = null;

        double prc, pum = 0, pud = 0, can = 0, por = 0, cnn = 0;
        float tpe = 0;

        String vax = "";
        if (sta.equals("P")) {
          vax = " and npe not in (select npe from notaent) ";
        } else {
          if (sta.equals("C")) {
            vax = " and npe  in (select npe from notaent) ";
          }
        }
        // Orden listado
        String vax2 = "order by noc,npe desc";
        if (ord.contains("0")) {
          vax2 = "order by npe desc";
        }
        String sqlh = "SELECT npe,(select nom from clientes where clientes.coc=pedidoH.coc) noc,fep,tip,por "
                + "FROM pedidoH "
                + "where fep between '" + Fed + "' and '" + Feh + "' "
                + "  and noc like '%" + fil + "%' "
                + vax
                + vax2;
        rs = st.executeQuery(sqlh);
        while (rs.next()) {
          npe = rs.getInt("npe");
          fep = rs.getString("fep");
          noc = rs.getString("noc");
          tip = rs.getString("tip");
          por = rs.getDouble("por");

          String sqld = "SELECT npe,can,prm,prd "
                  + "from pedidoD "
                  + "where npe=" + npe;
          cnn = 0;
          tpe = 0;
          rs2 = st2.executeQuery(sqld);
          while (rs2.next()) {
            can = rs2.getDouble("can");
            pum = rs2.getDouble("prm");
            pud = rs2.getDouble("prd");
            // porcentaje descuento (5% pp)
            if (por > 0) {
              pum = pum - (pum * por);
              pud = pud - (pud * por);
            }
            prc = pum; // Precio Mayor
            if (tip.equals("1")) {
              prc = pud;     // Detal
            }
            tpe = (float) (tpe + (can * prc));
            cnn = cnn + can;
          }
          String fne = "";
          int nne = 0;
          // Busca no Nota entrega y fecha
          sqld = "SELECT nne,fne "
                  + "from notaent "
                  + "where npe=" + npe;
          rs2 = st2.executeQuery(sqld);
          while (rs2.next()) {
            nne = rs2.getInt("nne");
            fne = rs2.getString("fne");
          }
          String fe2 = fne;
          if (fne.length() == 0 || fne == null) {
            fe2 = ymdhoy();
          }
          int ndi = getdiasFec(fe2, fep);
          if (fep.length() == 8) {
            fep = ymd_dmyc(fep);
          }
          if (fne.length() == 8) {
            fne = ymd_dmyc(fne);
          }
          if (noc.length() > 31) {
            noc = noc.substring(0, 31);
          }
          noc = noc.toUpperCase();

          Pedidos p = new Pedidos(npe, fep, noc, nne, fne, ndi, cnn, tpe);
          obs.add(p);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obs;
  }

  // getListPedidoD(npe)
  public Pedidos(int npe, String cop, String dep, String unm, double can, double prm, double prd, double por) {
    this.npe = npe;
    this.cop = cop;
    this.dep = dep;
    this.unm = unm;
    this.can = can;
    this.prm = prm;
    this.prd = prd;
    this.por = por;
  }

  // getListPedidoD
  public ObservableList<Pedidos> getListPedidoD(int npe) {
    ObservableList<Pedidos> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      if (con != null) {
        Statement st = con.createStatement();
        ResultSet rs = null;
        String sql = "SELECT npe,cop,dep,unm,can,prm,prd,por "
                + "FROM pedidoD "
                + "where npe =  " + npe;
        rs = st.executeQuery(sql);
        while (rs.next()) {
          npe = rs.getInt("npe");
          cop = rs.getString("cop");
          dep = rs.getString("dep");
          unm = rs.getString("unm");
          can = rs.getDouble("can");
          prm = rs.getDouble("prm");
          prd = rs.getDouble("prd");
          por = rs.getDouble("por");
          Pedidos p = new Pedidos(npe, cop, dep, unm, can, prm, prd, por);
          obs.add(p);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obs;
  }

  // getPromocProd(npe)
  public Pedidos(int npe, String grp, double por, double cat, double can) {
    this.npe = npe;
    this.grp = grp;
    this.por = por;
    this.cat = cat;
    this.can = can;
  }

  // getPromocProd
  public ObservableList<Pedidos> getPromocProd(int npe) {
    ObservableList<Pedidos> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      if (con != null) {
        Statement st = con.createStatement();
        Statement st2 = con.createStatement();
        ResultSet rs = null;

        String grp = "";
        double can = 0, cat = 0, por = 0;

        // Inicaliza % desc
        String sql = "update pedidoD "
                + "set por = 0 "
                + "where npe = " + npe;
        st.execute(sql);

        sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.promoD ("
                + "grp  varchar(10),"
                + "por  int,"
                + "cat  int,"
                + "cop  varchar(10),"
                + "can  int) "
                + "NOT LOGGED ON COMMIT PRESERVE ROWS";
        st.execute(sql);

        sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.promoR ("
                + "grp  varchar(10),"
                + "por  int,"
                + "cat  int,"
                + "can  int) "
                + "NOT LOGGED ON COMMIT PRESERVE ROWS";
        st.execute(sql);

        sql = "insert into SESSION.promoD "
                + "SELECT y.grp, y.por, y.can cat,  x.cop, sum(x.can) can "
                + "FROM pedidoD x, PromCombo y "
                + "where x.cop = y.cop "
                + "and npe = " + npe + " "
                + "group by y.grp,y.por,y.can,x.cop "
                + "order by y.grp,x.cop ";
        st.execute(sql);

        sql = "insert into SESSION.promoR "
                + "select grp,por,cat,sum(can) can from SESSION.promoD "
                + "group by grp,por,cat "
                + "order by grp,cat";
        st.execute(sql);

        sql = "SELECT distinct grp,por,cat,can "
                + "FROM SESSION.promoR "
                + "order by grp ";

        rs = st.executeQuery(sql);
        while (rs.next()) {
          grp = rs.getString("grp");
          por = rs.getDouble("por");
          cat = rs.getDouble("cat");
          can = rs.getDouble("can");
          if (can >= cat) {
            String sql2 = "update pedidoD "
                    + "set por = " + por + " "
                    + "where npe = " + npe + " "
                    + "  and cop || '" + grp + "' in (select cop || grp from SESSION.promoD)";
            st2.execute(sql2);
          }
          Pedidos p = new Pedidos(npe, grp, por, cat, can);
          obs.add(p);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obs;
  }

  // insertarPedidoH()
  public Pedidos(int npe, String coc, String noc, String tip,
          String fac, String fep, String fel, String obs, String fre,
          double poi, double pre, double ppp, double ppm) {
    this.npe = npe;
    this.coc = coc;
    this.noc = noc;
    this.tip = tip;
    this.fac = fac;
    this.fep = fep;
    this.fel = fel;
    this.obs = obs;
    this.fre = fre;
    this.poi = poi;
    this.pre = pre;
    this.ppp = ppp;
    this.ppm = ppm;
  }

  // insertarPedidoH
  public boolean insertarPedidoH() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "Insert into pedidoH "
                + "(npe,coc,noc,tip,fac,fep,fel,obs,fre,iva,pre,por,ppm) "
                + "VALUES ("
                + npe + ","
                + "'" + coc + "',"
                + "'" + noc + "',"
                + "'" + tip + "',"
                + "'" + fac + "',"
                + "'" + fep + "',"
                + "'" + fel + "',"
                + "'" + obs + "',"
                + "'" + fre + "',"
                + "" + poi + ","
                + "" + pre + ","
                + "" + ppp + ","
                + ppm + ")";
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

  // insertarPedidoD()
  public Pedidos(int npe, String cop, String dep, String unm, double can, double prm, double prd) {
    this.npe = npe;
    this.cop = cop;
    this.dep = dep;
    this.unm = unm;
    this.can = can;
    this.prm = prm;
    this.prd = prd;
  }

  // insertarPedidoD()
  public boolean insertarPedidoD() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "Insert into pedidoD "
                + "(npe,cop,dep,unm,can,prm,prd) "
                + "VALUES ("
                + npe + ","
                + "'" + cop + "',"
                + "'" + dep + "',"
                + "'" + unm + "',"
                + can + ","
                + prm + ","
                + prd + ")";
        //System.out.println("insertarPedidoD()=" + sql);
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

  // modificarPedidoD()
  public Pedidos(int npe, String cop, String unm, double can, String unx, double cax) {
    this.npe = npe;
    this.cop = cop;
    this.unm = unm;
    this.can = can;
    this.unx = unx;
    this.cax = cax;
  }

  // modificarPedidoD
  public boolean modificarPedidoD() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "update pedidoD "
                + "set unm='" + unx + "',"
                + "    can=" + cax + " "
                + "where npe=" + npe + " "
                + "  and cop='" + cop + "' "
                + "  and unm='" + unm + "' "
                + "  and can=" + can;
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

  // modificarPedidoH
  public Pedidos(int npe, String coc, String noc, String tip,
          String fac, String fep, String obs, double ppp, double ppm) {
    this.npe = npe;
    this.coc = coc;
    this.noc = noc;
    this.tip = tip;
    this.fac = fac;
    this.fep = fep;
    this.obs = obs;
    this.ppp = ppp;
    this.ppm = ppm;
  }

  // modificarPedidoH
  public boolean modificarPedidoH() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "update pedidoH "
                + "set coc='" + coc + "',"
                + "noc='" + noc + "',"
                + "tip='" + tip + "',"
                + "fac='" + fac + "',"
                + "fep='" + fep + "',"
                + "obs='" + obs + "',"
                + "por=" + ppp + ", "
                + "ppm=" + ppm + " "
                + "where npe=" + npe;
        st.executeUpdate(sql);
        //System.out.println("modificarPedidoH()=" + sql);
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

  // Busca ultimo No. Pedido
  public int getMaxPed() {
    int mxp = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT max(npe) npe "
              + "FROM pedidoH ";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        mxp = rs.getInt("npe");
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
    }
    return mxp;
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

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public String getFac() {
    return fac;
  }

  public void setFac(String fac) {
    this.fac = fac;
  }

  public String getFep() {
    return fep;
  }

  public void setFep(String fep) {
    this.fep = fep;
  }

  public String getFel() {
    return fel;
  }

  public void setFel(String fel) {
    this.fel = fel;
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

  public double getPor() {
    return por;
  }

  public void setPor(double por) {
    this.por = por;
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

  public double getPpm() {
    return ppm;
  }

  public void setPpm(double ppm) {
    this.ppm = ppm;
  }

  public String getCop() {
    return cop;
  }

  public void setCop(String cop) {
    this.cop = cop;
  }

  public String getDep() {
    return dep;
  }

  public void setDep(String dep) {
    this.dep = dep;
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

  public double getPrm() {
    return prm;
  }

  public void setPrm(double prm) {
    this.prm = prm;
  }

  public double getPrd() {
    return prd;
  }

  public void setPrd(double prd) {
    this.prd = prd;
  }

  public int getNne() {
    return nne;
  }

  public void setNne(int nne) {
    this.nne = nne;
  }

  public String getFne() {
    return fne;
  }

  public void setFne(String fne) {
    this.fne = fne;
  }

  public int getNdi() {
    return ndi;
  }

  public void setNdi(int ndi) {
    this.ndi = ndi;
  }

  public double getCnn() {
    return cnn;
  }

  public void setCnn(double cnn) {
    this.cnn = cnn;
  }

  public float getTpe() {
    return tpe;
  }

  public void setTpe(float tpe) {
    this.tpe = tpe;
  }

  public String getGrp() {
    return grp;
  }

  public void setGrp(String grp) {
    this.grp = grp;
  }

  public double getCat() {
    return cat;
  }

  public void setCat(double cat) {
    this.cat = cat;
  }

  public double getCax() {
    return cax;
  }

  public void setCax(double cax) {
    this.cax = cax;
  }

  public String getUnx() {
    return unx;
  }

  public void setUnx(String unx) {
    this.unx = unx;
  }

  public double getPoi() {
    return poi;
  }

  public void setPoi(double poi) {
    this.poi = poi;
  }

  public double getPpp() {
    return ppp;
  }

  public void setPpp(double ppp) {
    this.ppp = ppp;
  }

}


/*
 pedidoH
+ "npe int,          " // nro pedido cliente
+ "coc VARCHAR(12),  " // codigo cliente
+ "noc VARCHAR(50),  " // nombre Cliente
+ "tip VARCHAR(01),  " // tipo 0=Mayorista 1=Detal
+ "fac VARCHAR(01),  " // Con Factura S/N
+ "fep VARCHAR(08),  " // fecha pedido
+ "fel VARCHAR(08),  " // fecha lista precios
+ "obs VARCHAR(160), " // Observaciones
+ "por double , " // % descuento ppago
+ "iva double , " // % Iva
+ "pre double , " // % retencion Iva
+ "fre VARCHAR(12),  " // fecha registro  
+ "ppm double , " // % Promocion global

pedidoD 
+ "npe int,          " // nro pedido cliente
+ "cop VARCHAR(10),  " // codigo producto
+ "dep VARCHAR(60),  " // descripcion producto
+ "unm VARCHAR(10),  " // unidad Medida venta
+ "can double , " // cantidad producto
+ "prm double , " // precio Mayor
+ "prd double        " // precio Detal
+ "por double        " // % promocion
 */
