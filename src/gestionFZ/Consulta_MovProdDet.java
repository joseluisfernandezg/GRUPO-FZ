package gestionFZ;

import static comun.MetodosComunes.FileExist;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.pintarImagen;
import static comun.MetodosComunes.ymd_dmy;
import static gestionFZ.Consulta_MovProd.DetPrd;
import static gestionFZ.Registro_CatalogoProductos.ImgPrd;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import modelo.ConexionSQL;

public class Consulta_MovProdDet extends javax.swing.JFrame {

  private DefaultListModel modelo;
  String Ref = "";

  String Fed = "", Feh = "";
  String ord = "0";
  String format = " %1$-35s %2$-10s %3$-10s %4$8s  %5$-4s %6$17s\n";

  int npe = 0;
  String cop = "", nop = "", unm = "", noc = "", fep = "", ncl = "";

  double tpe = 0, tpeg = 0, por = 0;
  double can = 0, cang = 0;

  public Consulta_MovProdDet(String cox, String nox, String unx, String ncx, String fdx, String fhx) {

    cop = cox;
    nop = nox;
    unm = unx;
    Fed = fdx;
    Feh = fhx;
    ncl = ncx;

    initComponents();

    txtFed.setText(ymd_dmy(fdx));
    txtFeh.setText(ymd_dmy(fhx));

    txtCop.setText(cop);
    txtPrd.setText(" " + nop);

    String img = "imgprd\\" + cop + ".png";
    if (FileExist(img)) {
      pintarImagen(labImg, img);
    } else {
      img = "imgcia\\SinImagen.png";
      pintarImagen(labImg, img);
    }

    setLocationRelativeTo(null); // centramos la ventana en la pantalla
    setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    setLayout(null);

    String vax = String.format(format, " Cliente", " N.Pedido", " F.Pedido", "   Cant", " UM", "  T.Pedido $");
    jTiT.setText(vax);

    modelo = new DefaultListModel();

    jList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    jList.setSelectionBackground(Color.CYAN);
    jList.setSelectionForeground(Color.BLUE);

    //Seccion Mouse (Doble Click)
    jList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2 && modelo.size() > 0) {
          //Seleccionar();
        }
      }
    });

    jList.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index,
              boolean isSelected, boolean cellHasFocus) {

        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Consulta_MovProdDet.jListColor) {
          Consulta_MovProdDet.jListColor nextRegistro = (Consulta_MovProdDet.jListColor) value;
          String nam = nextRegistro.name;
          setText(nam);

          if (nextRegistro.loggedIn) {
            setForeground(new java.awt.Color(153, 0, 51)); // Rojo (Negativo)
          } else {
            if (nam.indexOf("Gastos") > 0) {
              //setForeground(new java.awt.Color(0, 0, 102)); // Azul Oscuro
              setForeground(new java.awt.Color(153, 0, 51)); // Rojo (Negativo)
              //setBackground(Color.LIGHT_GRAY);
            } else {
              if (nam.indexOf("------") == -1 && nam.indexOf("───") == -1) {
                //setForeground(new java.awt.Color(0, 0, 102)); // Azul Oscuro
                setForeground(Color.BLACK);
              } else {
                setForeground(Color.BLACK);
              }
            }
          }

          if (nam.indexOf("TOTAL") >= 0) {
            setBackground(Color.GRAY);
            //setForeground(new java.awt.Color(0, 0, 102)); // Azul Oscuro
            setForeground(Color.WHITE); // Azul Oscuro
          }
          // setForeground(new java.awt.Color(153, 0, 102)); // Rojo (Negativo)

          if (isSelected) {
            Ref = nam;
            //setBackground(getBackground().darker());
            //setBackground(Color.ORANGE);
          }
        } else {
          setText(null);
        }
        return c;
      }
    });

    ListaDatos();

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        DetPrd.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        DetPrd.dispose();
      }
    });

  }

  //Color Jlist
  static class jListColor {

    String name = "NN";
    boolean loggedIn = false;

    public jListColor(String name, double mto, boolean loggedIn) {
      this.name = name;
      this.loggedIn = loggedIn;
      if (mto < 0) {
        this.loggedIn = true;
      }
    }
  }

  // Lista Datos
  public void ListaDatos() {

    modelo.clear();

    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.MovPrdDet ("
              + "npe  int,"
              + "noc  varchar(50),"
              + "fep  varchar(10),"
              + "can  int,"
              + "unm  varchar(3),"
              + "por  Decimal(6,2),"
              + "tpe  Decimal(15,2))"
              + "NOT LOGGED ON COMMIT PRESERVE ROWS";
      st.execute(sql);

      sql = "insert into SESSION.MovPrdDet "
              + "SELECT x.npe,"
              + "(select nom from clientes where clientes.coc=x.coc) noc,"
              + "fep,can,y.unm,x.por,(case when tip='0' then (can*prm) else (can*prd) end) tpe "
              + "FROM pedidoH x,pedidoD y "
              + "where x.npe=y.npe "
              + "  and fep between '" + Fed + "' and '" + Feh + "' "
              + "  and cop = '" + cop + "' "
              //+ "  and noc like '%" + ncl + "%' "
              + "order by noc,can desc";
      st.execute(sql);

      sql = "SELECT npe,noc,fep,por,can,unm,tpe "
              + "FROM SESSION.MovPrdDet ";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        npe = rs.getInt("npe");
        can = rs.getDouble("can");
        fep = rs.getString("fep");
        noc = rs.getString("noc");
        unm = rs.getString("unm");
        tpe = rs.getDouble("tpe");
        por = rs.getDouble("por");
        tpe = tpe - (tpe * por);
        cang = cang + can;
        tpeg = tpeg + tpe;
        if (noc.length() > 35) {
          noc = noc.substring(0, 35);
        }

        String vax = String.format(format, noc, npe, ymd_dmy(fep), MtoEs(can, 0), unm, MtoEs(tpe, 0));
        modelo.addElement(new Consulta_MovProdDet.jListColor(vax, 0, false));
      }

      jList.setModel(modelo);
      if (!modelo.isEmpty()) {
        jList.setSelectedIndex(0);
        labReg.setText("Cant =  " + modelo.size());
        labcan.setText(MtoEs(cang, 0) + "  ");
        labTot.setText(MtoEs(tpeg, 0) + "  ");
        //String vax = String.format(format, "", "", "", "TOTAL", cang, MtoEs(tpeg, 2));
        //modelo.addElement(new Consulta_MovProdDet.jListColor(vax, 1, false));
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Consulta_MovProdDet.class.getName()).log(Level.SEVERE, null, ex);
    }
    txtPrd.requestFocus();
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
   * content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel3 = new javax.swing.JLabel();
    jTiT = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jList = new javax.swing.JList();
    jLabel1 = new javax.swing.JLabel();
    txtFed = new javax.swing.JTextField();
    jLabel2 = new javax.swing.JLabel();
    txtFeh = new javax.swing.JTextField();
    Cliente1 = new javax.swing.JLabel();
    txtPrd = new javax.swing.JTextField();
    btnSal = new javax.swing.JButton();
    labReg = new javax.swing.JLabel();
    txtCop = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    labcan = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    labTot = new javax.swing.JLabel();
    labImg = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/BusPrd.jpg"))); // NOI18N
    jLabel3.setText("DETALLE MOVIMIENTO PRODUCTO");
    jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jTiT.setBackground(new java.awt.Color(0, 102, 102));
    jTiT.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
    jTiT.setForeground(new java.awt.Color(255, 255, 255));
    jTiT.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
    jTiT.setOpaque(true);

    jList.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
    jList.setOpaque(false);
    jList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jListMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(jList);

    jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel1.setForeground(new java.awt.Color(0, 0, 102));
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Del");

    txtFed.setBackground(new java.awt.Color(252, 247, 228));
    txtFed.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtFed.setForeground(new java.awt.Color(51, 0, 153));
    txtFed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtFed.setText(" ");
    txtFed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    txtFed.setFocusable(false);
    txtFed.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtFedMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtFedMouseReleased(evt);
      }
    });
    txtFed.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtFedActionPerformed(evt);
      }
    });
    txtFed.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtFedKeyReleased(evt);
      }
    });

    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(0, 51, 102));
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setText("al");

    txtFeh.setBackground(new java.awt.Color(252, 247, 228));
    txtFeh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtFeh.setForeground(new java.awt.Color(51, 0, 153));
    txtFeh.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtFeh.setText(" ");
    txtFeh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    txtFeh.setFocusable(false);
    txtFeh.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtFehMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtFehMouseReleased(evt);
      }
    });
    txtFeh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtFehActionPerformed(evt);
      }
    });
    txtFeh.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtFehKeyReleased(evt);
      }
    });

    Cliente1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    Cliente1.setForeground(new java.awt.Color(51, 51, 102));
    Cliente1.setText("Producto");

    txtPrd.setEditable(false);
    txtPrd.setBackground(new java.awt.Color(204, 204, 204));
    txtPrd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtPrd.setToolTipText("Buscar Clientes");
    txtPrd.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtPrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtPrd.setOpaque(false);
    txtPrd.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtPrdMouseClicked(evt);
      }
    });
    txtPrd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtPrdActionPerformed(evt);
      }
    });
    txtPrd.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtPrdKeyReleased(evt);
      }
    });

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
      }
    });

    labReg.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    labReg.setForeground(new java.awt.Color(51, 51, 102));
    labReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labReg.setText(" ");
    labReg.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    txtCop.setEditable(false);
    txtCop.setBackground(new java.awt.Color(204, 204, 204));
    txtCop.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    txtCop.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtCop.setToolTipText("Buscar Clientes");
    txtCop.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtCop.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtCop.setOpaque(false);
    txtCop.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtCopMouseClicked(evt);
      }
    });
    txtCop.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtCopActionPerformed(evt);
      }
    });
    txtCop.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtCopKeyReleased(evt);
      }
    });

    jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(0, 51, 102));
    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel5.setText("Cant Pedida");

    labcan.setBackground(new java.awt.Color(204, 204, 204));
    labcan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labcan.setForeground(new java.awt.Color(0, 0, 102));
    labcan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labcan.setText(" ");
    labcan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labcan.setOpaque(true);
    labcan.setPreferredSize(new java.awt.Dimension(3, 30));

    jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(0, 51, 102));
    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel4.setText("Tot $");

    labTot.setBackground(new java.awt.Color(204, 204, 204));
    labTot.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labTot.setForeground(new java.awt.Color(0, 0, 102));
    labTot.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labTot.setText(" ");
    labTot.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labTot.setOpaque(true);
    labTot.setPreferredSize(new java.awt.Dimension(3, 30));

    labImg.setText(" ");
    labImg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    labImg.setOpaque(true);
    labImg.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labImgMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(20, 20, 20)
        .addComponent(btnSal)
        .addGap(36, 36, 36)
        .addComponent(labReg, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jLabel5)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(labcan, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(2, 2, 2)
        .addComponent(labTot, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(29, 29, 29))
      .addGroup(layout.createSequentialGroup()
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCop, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtPrd, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(18, 18, 18)
            .addComponent(labImg, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(21, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jScrollPane1)
              .addComponent(jTiT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(15, 15, 15))))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2)
                .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(txtPrd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(txtCop, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(layout.createSequentialGroup()
            .addComponent(labImg, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE)))
        .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
            .addComponent(labcan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
            .addComponent(labTot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(labReg, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnSal)))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void txtFedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFedMouseClicked
    //jDesde.setSelectionStart(0);
    //jDesde.setSelectionEnd(5);
  }//GEN-LAST:event_txtFedMouseClicked

  private void txtFedMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFedMouseReleased

  }//GEN-LAST:event_txtFedMouseReleased

  private void txtFedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFedActionPerformed

  }//GEN-LAST:event_txtFedActionPerformed

  private void txtFedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFedKeyReleased

  }//GEN-LAST:event_txtFedKeyReleased

  private void txtFehMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFehMouseClicked
    //jHasta.setSelectionStart(0);
    //jHasta.setSelectionEnd(5);
  }//GEN-LAST:event_txtFehMouseClicked

  private void txtFehMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFehMouseReleased

  }//GEN-LAST:event_txtFehMouseReleased

  private void txtFehActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFehActionPerformed

  }//GEN-LAST:event_txtFehActionPerformed

  private void txtFehKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFehKeyReleased

  }//GEN-LAST:event_txtFehKeyReleased

  private void txtPrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPrdMouseClicked
  }//GEN-LAST:event_txtPrdMouseClicked

  private void txtPrdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrdActionPerformed
  }//GEN-LAST:event_txtPrdActionPerformed

  private void txtPrdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrdKeyReleased
  }//GEN-LAST:event_txtPrdKeyReleased

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    DetPrd.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void jListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jListMouseClicked

  private void txtCopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCopMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCopMouseClicked

  private void txtCopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCopActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCopActionPerformed

  private void txtCopKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCopKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCopKeyReleased

  private void labImgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labImgMouseClicked
    String rut = "imgprd\\" + cop + ".png";
    if (FileExist(rut)) {
      // Ver Imagen Producto
      if (ImgPrd != null) {
        ImgPrd.dispose();
      }
      ImgPrd = new Consulta_ImagenProd(rut, cop);   // Paso Controller
      ImgPrd.setVisible(true);
      ImgPrd.setExtendedState(NORMAL);
      ImgPrd.setAlwaysOnTop(true);
      ImgPrd.setVisible(true);
    }
  }//GEN-LAST:event_labImgMouseClicked

  public void setImagenCia() {
    String img = "imgcia\\logoimp2.png";
    pintarImagen(labImg, img);
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(Consulta_MovProdDet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Consulta_MovProdDet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Consulta_MovProdDet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Consulta_MovProdDet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Consulta_MovProdDet("", "", "", "", "", "").setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Cliente1;
  private javax.swing.JButton btnSal;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JList jList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel jTiT;
  private javax.swing.JLabel labImg;
  private javax.swing.JLabel labReg;
  private javax.swing.JLabel labTot;
  private javax.swing.JLabel labcan;
  private javax.swing.JTextField txtCop;
  private javax.swing.JTextField txtFed;
  private javax.swing.JTextField txtFeh;
  private javax.swing.JTextField txtPrd;
  // End of variables declaration//GEN-END:variables
}
