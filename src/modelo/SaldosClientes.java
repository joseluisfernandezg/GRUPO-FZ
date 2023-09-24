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

public class SaldosClientes {

  String coc;
  int tpd;
  int nno;
  int nfa;
  int nrc;
  int ndi;
  int can;
  String noc;
  String fno;
  String fre;
  String fve;
  String fpa;
  double tno;
  double tdn;
  double toi;
  double tor;
  double tre;
  double tbd;
  double tbb;
  double tsid = 0;
  double tsib = 0;

  public SaldosClientes() {
  }

  // Saldo Inicial Cliente
  public SaldosClientes(String coc, double tsid, double tsib) {
    this.tsid = tsid;
    this.tbb = tbb;
  }

  public SaldosClientes(String coc, int nno, int nfa, int nrc, int ndi, String fno, String fre, String fve, String fpa, double tno, double tdn, double toi, double tor, double tbd, double tbb) {
    this.coc = coc;
    this.nno = nno;
    this.nfa = nfa;
    this.nrc = nrc;
    this.ndi = ndi;
    this.fno = fno;
    this.fre = fre;
    this.fve = fve;
    this.fpa = fpa;
    this.tno = tno;
    this.tdn = tdn;
    this.toi = toi;
    this.tor = tor;
    this.tbd = tbd;
    this.tbb = tbb;
  }

  public SaldosClientes(String coc, String noc, int can, double tno, double tre, double tbd, double tbb) {
    this.coc = coc;
    this.noc = noc;
    this.can = can;
    this.tno = tno;
    this.tre = tre;
    this.tbd = tbd;
    this.tbb = tbb;
  }

  // getSaldoIncialClienteR
  public SaldosClientes(double tno, double tre, double tbd, double tbb, double tsid, double tsib) {
    this.tno = tno;
    this.tre = tre;
    this.tbd = tbd;
    this.tbb = tbb;
    this.tsid = tsid;
    this.tsib = tsib;
  }

  // getSaldoIncialClienteR
  public ObservableList<SaldosClientes> getSaldoIncialClienteR(String coc, String Fed) {
    ObservableList<SaldosClientes> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {

        Statement st = cnn.createStatement();

        String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.SaldosD ("
                + "nno  int,"
                + "nfa  int,"
                + "tno  Decimal(15,2),"
                + "tre  Decimal(15,2),"
                + "nrc  int)"
                + "NOT LOGGED ON COMMIT PRESERVE ROWS";
        st.execute(sql);

        sql = "insert into SESSION.SaldosD "
                + "SELECT nne nno,nfa,sum(tne-tdn) tno,sum(toi-tor) tre,0 nrc "
                + "FROM notaent "
                + "where fne< '" + Fed + "' "
                + "  and coc= '" + coc + "' "
                + "group by nne,nfa";
        st.execute(sql);

        sql = "insert into SESSION.SaldosD "
                + "SELECT ncr nno,0 nfa,sum(tnc*-1) tno,0 tre,0 nrc "
                + "FROM notaCred "
                + "where fnc < '" + Fed + "'"
                + "  and coc= '" + coc + "' "
                + "group by ncr ";
        st.execute(sql);

        // Nusca nro recibo pago
        sql = "UPDATE SESSION.SaldosD "
                + "Set nrc = (select nullif(nrc,0) from recibocobroD y Where SESSION.SaldosD.nno=y.nno)";
        st.execute(sql);

        sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.SaldosR ("
                + "nrc  int,"
                + "tno  Decimal(15,2),"
                + "tre  Decimal(15,2),"
                + "tbd  Decimal(15,2),"
                + "tbb  Decimal(15,2)) "
                + "NOT LOGGED ON COMMIT PRESERVE ROWS";
        st.execute(sql);

        sql = "insert into SESSION.SaldosR "
                + "SELECT nrc,sum(tno) tno,sum(tre) tre,0,0 "
                + "FROM SESSION.SaldosD "
                + "group by nrc";
        st.execute(sql);

        sql = "UPDATE SESSION.SaldosR "
                + "Set tbd = (select nullif(sum(tpa/tas),0) from recibocobroP y Where SESSION.SaldosR.nrc=y.nrc and tip<>'2' and tip<>'4'),"
                + "    tbb = (select nullif(sum(tpa),0)     from recibocobroP y Where SESSION.SaldosR.nrc=y.nrc and (tip='2' or tip='4'))";
        st.execute(sql);

        sql = "Select sum(tno) tno,sum(tre) tre,sum(tbd) tbd,sum(tbb) tbb "
                + "from SESSION.SaldosR";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
          tno = rs.getDouble("tno");
          tre = rs.getDouble("tre");
          tbd = rs.getDouble("tbd");
          tbb = rs.getDouble("tbb");
          tsid = tno - tbd;   // total $
          tsib = tre - tbb;   // total Ret Bs
          SaldosClientes s = new SaldosClientes(tno, tre, tbd, tbb, tsid, tsib);
          obs.add(s);
        }
        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      cnn.close();
    } catch (SQLException ex) {
      Logger.getLogger(SaldosClientes.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obs;
  }

  // getMovClienteR
  public ObservableList<SaldosClientes> getMovClienteR(String ord, String sta, String fil, String coc, String Fed, String Feh) {
    ObservableList<SaldosClientes> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String vax1 = "", vax2 = "";

        if (sta.equals("P")) {
          vax1 = " and nne not in (select nno from recibocobroD where tpd=0) ";
          vax2 = " and ncr not in (select nno from recibocobroD where tpd=1) ";
        } else {
          if (sta.equals("C")) {
            vax1 = " and nne in (select nno from recibocobroD where tpd=0) ";
            vax2 = " and ncr in (select nno from recibocobroD where tpd=1) ";
          }
        }

        String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.SaldosD ("
                + "noc  varchar(50),"
                + "coc  varchar(10),"
                + "nno  int,"
                + "can  int,"
                + "tno  Decimal(15,2),"
                + "tre  Decimal(15,2),"
                + "nrc  int)"
                + "NOT LOGGED ON COMMIT PRESERVE ROWS";
        st.execute(sql);

        sql = "insert into SESSION.SaldosD "
                + "SELECT '' noc,"
                + "coc,nne nno,1 cant,sum(tne-tdn) tno,sum(toi-tor) tre,0 nrc "
                + "FROM notaent "
                + "where fne between '" + Fed + "' and '" + Feh + "' "
                + vax1
                + "  and (noc like '%" + fil + "%' or coc like '%" + fil + "%')"
                + "group by coc,nne";
        st.execute(sql);

        sql = "insert into SESSION.SaldosD "
                + "SELECT '' noc,"
                + "coc,ncr nno,1 cant,sum(tnc*-1) tno,0 tre,0 nrc "
                + "FROM notaCred "
                + "where fnc between '" + Fed + "' and '" + Feh + "' "
                + vax2
                + "  and (noc like '%" + fil + "%' or coc like '%" + fil + "%')"
                + "group by coc,ncr ";
        st.execute(sql);

        // Nusca nro recibo pago
        sql = "UPDATE SESSION.SaldosD "
                + "Set noc=(select nom from clientes where clientes.coc=SESSION.SaldosD.coc),"
                + "nrc = (select nullif(nrc,0) from recibocobroD y Where SESSION.SaldosD.nno=y.nno)";
        st.execute(sql);

        sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.SaldosR ("
                + "noc  varchar(50),"
                + "coc  varchar(10),"
                + "nrc  int,"
                + "can  int,"
                + "tno  Decimal(15,2),"
                + "tre  Decimal(15,2),"
                + "tbd  Decimal(15,2),"
                + "tbb  Decimal(15,2)) "
                + "NOT LOGGED ON COMMIT PRESERVE ROWS";
        st.execute(sql);

        sql = "insert into SESSION.SaldosR "
                + "SELECT noc,coc,nrc,sum(can) can,sum(tno) tno,sum(tre) tre,0,0 "
                + "FROM SESSION.SaldosD "
                + "group by noc,coc,nrc";
        st.execute(sql);

        sql = "UPDATE SESSION.SaldosR "
                + "Set tbd = (select nullif(sum(tpa/tas),0) from recibocobroP y Where SESSION.SaldosR.nrc=y.nrc and tip<>'2' and tip<>'4'),"
                + "    tbb = (select nullif(sum(tpa),0)     from recibocobroP y Where SESSION.SaldosR.nrc=y.nrc and (tip='2' or tip='4'))";
        st.execute(sql);

        String vax = "Order by tno desc";
        if (ord.equals("1")) {
          vax = "Order by noc";
        }

        sql = "Select noc,coc,sum(can) can,sum(tno) tno,sum(tre) tre,sum(tbd) tbd,sum(tbb) tbb "
                + "from SESSION.SaldosR "
                + "Group by noc,coc "
                + vax;

        ResultSet rs = null;
        rs = st.executeQuery(sql);
        while (rs.next()) {
          can = rs.getInt("can");
          noc = rs.getString("noc");
          coc = rs.getString("coc");
          tno = rs.getDouble("tno");
          tre = rs.getDouble("tre");
          tbd = rs.getDouble("tbd");
          tbb = rs.getDouble("tbb");

          SaldosClientes s = new SaldosClientes(coc, nno, nfa, nrc, ndi, fno, fre, fve, fpa, tno, tdn, toi, tor, tbd, tbb);
          obs.add(s);

        }

        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      cnn.close();
    } catch (SQLException ex) {
      Logger.getLogger(SaldosClientes.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obs;
  }

  // getMovClienteD
  public ObservableList<SaldosClientes> getMovClienteD(String coc, String Fed, String Feh) {
    ObservableList<SaldosClientes> obs = FXCollections.observableArrayList();
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.Saldos ("
                + "coc  varchar(10),"
                + "nno  int,"
                + "nfa  int,"
                + "fno  varchar(08),"
                + "fre  varchar(08),"
                + "fve  varchar(08),"
                + "fpa  varchar(08),"
                + "nrc  int,"
                + "ndi  int,"
                + "tno  Decimal(15,2),"
                + "tdn  Decimal(15,2),"
                + "toi  Decimal(15,2),"
                + "tor  Decimal(15,2),"
                + "tbd  Decimal(15,2),"
                + "tbb  Decimal(15,2))"
                + "NOT LOGGED ON COMMIT PRESERVE ROWS";
        st.execute(sql);

        sql = "insert into SESSION.Saldos "
                + "SELECT coc,nne nno,nfa,fne,fee,fev,'' fpa,0 ndi,0 nrc,tne tno,tdn,toi,tor,0 tbd,0 tbb "
                + "FROM notaent "
                + "where coc = '" + coc + "' "
                + "  and fne between '" + Fed + "' and '" + Feh + "'";
        st.execute(sql);

        sql = "insert into SESSION.Saldos "
                + "SELECT coc,ncr nno,nfa,fnc fne,'' fee,'' fev,'' fpa,0 nrc,0 ndi,tnc*-1 tno,0 tdn,0 toi,0 tor,0 tbd,0 tbb "
                + "FROM notaCred "
                + "where coc ='" + coc + "' "
                + "  and fnc between '" + Fed + "' and '" + Feh + "'";
        st.execute(sql);

        sql = "UPDATE SESSION.Saldos "
                + "Set fpa =  (select nullif(max(fpa),'') from recibocobroD y "
                + "Where SESSION.Saldos.nno=y.nno),"
                + "    nrc =  (select nullif(nrc,0) from recibocobroD y "
                + "Where SESSION.Saldos.nno=y.nno)";
        st.execute(sql);

        sql = "insert into SESSION.Saldos "
                + "SELECT coc,0 nno,0 nfa,'*SF*' fne,'' fee,'' fev,'' fpa,nrc,0 ndi,0 tno,0 tdn,0 toi,0 tor,(tpa/tas) tbd,0 tbb "
                + "FROM recibocobroP "
                + "where coc ='" + coc + "' "
                + "  and nno=0 "
                + "  and nrc >0 "
                + "  and nrc in (select nrc from SESSION.Saldos) "
                + "  and tip='5'";
        st.execute(sql);

        sql = "insert into SESSION.Saldos "
                + "SELECT coc,0 nno,0 nfa,'*SF*' fne,'' fee,'' fev,'' fpa,nrc,0 ndi,0 tno,0 tdn,0 toi,0 tor,0 tbd,tpa tbb "
                + "FROM recibocobroP "
                + "where coc ='" + coc + "' "
                + "  and nno=0 "
                + "  and nrc >0 "
                + "  and nrc in (select nrc from SESSION.Saldos) "
                + "  and tip='4'";
        st.execute(sql);

        sql = "UPDATE SESSION.Saldos "
                + "Set tbd = (select nullif(sum(tpa/tas),0) from recibocobroP y Where SESSION.Saldos.nno=y.nno and tip<>'2' and tip<>'4'),"
                + "    tbb = (select nullif(sum(tpa),0)     from recibocobroP y Where SESSION.Saldos.nfa=y.nno and (tip='2' or tip='4'))";
        st.execute(sql);

        sql = "Select "
                + "coc,nno,fno,nfa,fre,fve,fpa,nrc,ndi,tno,tdn,toi,tor,tbd,tbb "
                + "from SESSION.Saldos "
                + "Order by nno";

        ResultSet rs = null;
        rs = st.executeQuery(sql);
        while (rs.next()) {

          coc = rs.getString("coc");
          fno = rs.getString("fno");
          fre = rs.getString("fre");
          fve = rs.getString("fve");
          fpa = rs.getString("fpa");

          nno = rs.getInt("nno");
          nfa = rs.getInt("nfa");
          ndi = rs.getInt("ndi");
          nrc = rs.getInt("nrc");

          tno = rs.getDouble("tno");
          tdn = rs.getDouble("tdn");
          toi = rs.getDouble("toi");
          tor = rs.getDouble("tor");
          tbd = rs.getDouble("tbd");
          tbb = rs.getDouble("tbb");

          SaldosClientes s = new SaldosClientes(coc, nno, nfa, nrc, ndi, fno, fre, fve, fpa, tno, tdn, toi, tor, tbd, tbb);
          obs.add(s);

        }

        rs.close();
      } else {
        JOptionPane.showMessageDialog(null, "Hubo un error al conectarse con la base de datos ( " + mysql.getBasedatos() + " )", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      cnn.close();
    } catch (SQLException ex) {
      Logger.getLogger(SaldosClientes.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obs;
  }

  // insertarBanco
  public boolean creaTempSaldosClientes() {
    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection cnn = mysql.Conectar();
      if (cnn != null) {
        Statement st = cnn.createStatement();
        String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.Saldos ("
                + "coc  varchar(10),"
                + "nno  int,"
                + "nfa  int,"
                + "fno  varchar(08),"
                + "fre  varchar(08),"
                + "fve  varchar(08),"
                + "fpa  varchar(08),"
                + "nrc  int,"
                + "ndi  int,"
                + "tno  Decimal(15,2),"
                + "tdn  Decimal(15,2),"
                + "toi  Decimal(15,2),"
                + "tor  Decimal(15,2),"
                + "tbd  Decimal(15,2),"
                + "tbb  Decimal(15,2))"
                + "NOT LOGGED ON COMMIT PRESERVE ROWS";
        st.execute(sql);

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

  public int getCan() {
    return can;
  }

  public void setCan(int can) {
    this.can = can;
  }

  public double getTre() {
    return tre;
  }

  public void setTre(double tre) {
    this.tre = tre;
  }

  public int getTpd() {
    return tpd;
  }

  public void setTpd(int tpd) {
    this.tpd = tpd;
  }

  public int getNno() {
    return nno;
  }

  public void setNno(int nno) {
    this.nno = nno;
  }

  public int getNfa() {
    return nfa;
  }

  public void setNfa(int nfa) {
    this.nfa = nfa;
  }

  public int getNrc() {
    return nrc;
  }

  public void setNrc(int nrc) {
    this.nrc = nrc;
  }

  public int getNdi() {
    return ndi;
  }

  public void setNdi(int ndi) {
    this.ndi = ndi;
  }

  public String getFno() {
    return fno;
  }

  public void setFno(String fno) {
    this.fno = fno;
  }

  public String getFre() {
    return fre;
  }

  public void setFre(String fre) {
    this.fre = fre;
  }

  public String getFve() {
    return fve;
  }

  public void setFve(String fve) {
    this.fve = fve;
  }

  public String getFpa() {
    return fpa;
  }

  public void setFpa(String fpa) {
    this.fpa = fpa;
  }

  public double getTno() {
    return tno;
  }

  public void setTno(double tno) {
    this.tno = tno;
  }

  public double getTdn() {
    return tdn;
  }

  public void setTdn(double tdn) {
    this.tdn = tdn;
  }

  public double getToi() {
    return toi;
  }

  public void setToi(double toi) {
    this.toi = toi;
  }

  public double getTor() {
    return tor;
  }

  public void setTor(double tor) {
    this.tor = tor;
  }

  public double getTbd() {
    return tbd;
  }

  public void setTbd(double tbd) {
    this.tbd = tbd;
  }

  public double getTbb() {
    return tbb;
  }

  public void setTbb(double tbb) {
    this.tbb = tbb;
  }

  public double getTsid() {
    return tsid;
  }

  public void setTsid(double tsid) {
    this.tsid = tsid;
  }

  public double getTsib() {
    return tsib;
  }

  public void setTsib(double tsib) {
    this.tsib = tsib;
  }

}
