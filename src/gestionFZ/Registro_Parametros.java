package gestionFZ;

import static gestionFZ.Menu.RegPar;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafx.collections.ObservableList;
import javax.swing.WindowConstants;
import modelo.Parametros;

public class Registro_Parametros extends javax.swing.JFrame {

  String pp01 = "", pp02 = "", pp03 = "", pp04 = "", pp05 = "", pp06 = "", pp07 = "", pp08 = "", pp09 = "", pp10 = "", pp11 = "", pp12 = "", pp13 = "", pp14 = "", pp15 = "", pp16 = "",sm="0";

  public Registro_Parametros() {
    initComponents();
    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        RegPar.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        RegPar.dispose();
      }
    });

    cargaParametros();
    btnSal.requestFocus();
  }

  public void cargaParametros() {
    Parametros p = new Parametros();
    ObservableList<Parametros> obsParam = p.getBuscaParametros();
    for (Parametros par : obsParam) {
      p01.setText(" " + par.getP01());
      p02.setText(" " + par.getP02());
      p03.setText(" " + par.getP03());
      p04.setText(" " + par.getP04());
      p05.setText(" " + par.getP05());
      p06.setText(" " + par.getP06());
      p07.setText(" " + par.getP07());
      p08.setText(" " + par.getP08());
      p09.setText(" " + par.getP09());
      p10.setText(" " + par.getP10());
      p11.setText(" " + par.getP11());
      p12.setText(" " + par.getP12());
      p13.setText(" " + par.getP13());
      p14.setText(" " + par.getP14());
      p15.setText(" " + par.getP15());
      p16.setText(" " + par.getP16());
    }

    p01.setEnabled(false);
    p02.setEnabled(false);
    p03.setEnabled(false);

  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    lab01 = new javax.swing.JLabel();
    p01 = new javax.swing.JTextField();
    p04 = new javax.swing.JTextField();
    p05 = new javax.swing.JTextField();
    lab02 = new javax.swing.JLabel();
    p02 = new javax.swing.JTextField();
    lab3 = new javax.swing.JLabel();
    p03 = new javax.swing.JTextField();
    lab2 = new javax.swing.JLabel();
    p06 = new javax.swing.JTextField();
    p07 = new javax.swing.JTextField();
    p08 = new javax.swing.JTextField();
    p09 = new javax.swing.JTextField();
    p10 = new javax.swing.JTextField();
    p11 = new javax.swing.JTextField();
    p12 = new javax.swing.JTextField();
    p13 = new javax.swing.JTextField();
    btnSal = new javax.swing.JButton();
    btnGra = new javax.swing.JButton();
    jParam1 = new javax.swing.JLabel();
    p14 = new javax.swing.JTextField();
    p15 = new javax.swing.JTextField();
    p16 = new javax.swing.JTextField();
    labMsg = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    lab01.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    lab01.setForeground(new java.awt.Color(0, 0, 102));
    lab01.setText("Tamaño prom imagen");
    lab01.setToolTipText("");
    lab01.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        lab01MouseClicked(evt);
      }
    });

    p01.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    p01.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    p01.setText(" ");
    p01.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p01.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p01.setPreferredSize(new java.awt.Dimension(7, 30));
    p01.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p01MouseClicked(evt);
      }
    });
    p01.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p01ActionPerformed(evt);
      }
    });
    p01.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p01KeyReleased(evt);
      }
    });

    p04.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p04.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p04.setText(" ");
    p04.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p04.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p04.setPreferredSize(new java.awt.Dimension(7, 30));
    p04.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p04MouseClicked(evt);
      }
    });
    p04.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p04ActionPerformed(evt);
      }
    });
    p04.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p04KeyReleased(evt);
      }
    });

    p05.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p05.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p05.setText(" ");
    p05.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p05.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p05.setPreferredSize(new java.awt.Dimension(7, 30));
    p05.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p05MouseClicked(evt);
      }
    });
    p05.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p05ActionPerformed(evt);
      }
    });
    p05.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p05KeyReleased(evt);
      }
    });

    lab02.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    lab02.setForeground(new java.awt.Color(0, 0, 102));
    lab02.setText("Tamaño img Cat");
    lab02.setToolTipText("");
    lab02.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        lab02MouseClicked(evt);
      }
    });

    p02.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    p02.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    p02.setText(" ");
    p02.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p02.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p02.setPreferredSize(new java.awt.Dimension(7, 30));
    p02.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p02MouseClicked(evt);
      }
    });
    p02.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p02ActionPerformed(evt);
      }
    });
    p02.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p02KeyReleased(evt);
      }
    });

    lab3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    lab3.setForeground(new java.awt.Color(0, 0, 102));
    lab3.setText("Selecciona um");
    lab3.setToolTipText("");
    lab3.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        lab3MouseClicked(evt);
      }
    });

    p03.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    p03.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    p03.setText(" ");
    p03.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p03.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p03.setPreferredSize(new java.awt.Dimension(7, 30));
    p03.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p03MouseClicked(evt);
      }
    });
    p03.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p03ActionPerformed(evt);
      }
    });
    p03.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p03KeyReleased(evt);
      }
    });

    lab2.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    lab2.setForeground(new java.awt.Color(0, 0, 102));
    lab2.setText("Observacion Pedido:");
    lab2.setToolTipText("");
    lab2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        lab2MouseClicked(evt);
      }
    });

    p06.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p06.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p06.setText(" ");
    p06.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p06.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p06.setPreferredSize(new java.awt.Dimension(7, 30));
    p06.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p06MouseClicked(evt);
      }
    });
    p06.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p06ActionPerformed(evt);
      }
    });
    p06.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p06KeyReleased(evt);
      }
    });

    p07.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p07.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p07.setText(" ");
    p07.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p07.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p07.setPreferredSize(new java.awt.Dimension(7, 30));
    p07.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p07MouseClicked(evt);
      }
    });
    p07.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p07ActionPerformed(evt);
      }
    });
    p07.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p07KeyReleased(evt);
      }
    });

    p08.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p08.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p08.setText(" ");
    p08.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p08.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p08.setPreferredSize(new java.awt.Dimension(7, 30));
    p08.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p08MouseClicked(evt);
      }
    });
    p08.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p08ActionPerformed(evt);
      }
    });
    p08.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p08KeyReleased(evt);
      }
    });

    p09.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p09.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p09.setText(" ");
    p09.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p09.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p09.setPreferredSize(new java.awt.Dimension(7, 30));
    p09.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p09MouseClicked(evt);
      }
    });
    p09.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p09ActionPerformed(evt);
      }
    });
    p09.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p09KeyReleased(evt);
      }
    });

    p10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p10.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p10.setText(" ");
    p10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p10.setPreferredSize(new java.awt.Dimension(7, 30));
    p10.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p10MouseClicked(evt);
      }
    });
    p10.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p10ActionPerformed(evt);
      }
    });
    p10.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p10KeyReleased(evt);
      }
    });

    p11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p11.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p11.setText(" ");
    p11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p11.setPreferredSize(new java.awt.Dimension(7, 30));
    p11.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p11MouseClicked(evt);
      }
    });
    p11.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p11ActionPerformed(evt);
      }
    });
    p11.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p11KeyReleased(evt);
      }
    });

    p12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p12.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p12.setText(" ");
    p12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p12.setPreferredSize(new java.awt.Dimension(7, 30));
    p12.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p12MouseClicked(evt);
      }
    });
    p12.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p12ActionPerformed(evt);
      }
    });
    p12.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p12KeyReleased(evt);
      }
    });

    p13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p13.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p13.setText(" ");
    p13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p13.setPreferredSize(new java.awt.Dimension(7, 30));
    p13.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p13MouseClicked(evt);
      }
    });
    p13.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p13ActionPerformed(evt);
      }
    });
    p13.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p13KeyReleased(evt);
      }
    });

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
      }
    });

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnGra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGra.setText("Grabar");
    btnGra.setToolTipText("Actualizar Datos ");
    btnGra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnGra.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGraActionPerformed(evt);
      }
    });

    jParam1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jParam1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jParam1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/param.png"))); // NOI18N
    jParam1.setText("PARAMETROS");
    jParam1.setToolTipText("Parametros Sistema");
    jParam1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jParam1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jParam1.setPreferredSize(new java.awt.Dimension(106, 30));
    jParam1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jParam1MouseClicked(evt);
      }
    });

    p14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p14.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p14.setText(" ");
    p14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p14.setPreferredSize(new java.awt.Dimension(7, 30));
    p14.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p14MouseClicked(evt);
      }
    });
    p14.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p14ActionPerformed(evt);
      }
    });
    p14.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p14KeyReleased(evt);
      }
    });

    p15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p15.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p15.setText(" ");
    p15.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p15.setPreferredSize(new java.awt.Dimension(7, 30));
    p15.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p15MouseClicked(evt);
      }
    });
    p15.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p15ActionPerformed(evt);
      }
    });
    p15.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p15KeyReleased(evt);
      }
    });

    p16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    p16.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    p16.setText(" ");
    p16.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    p16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    p16.setPreferredSize(new java.awt.Dimension(7, 30));
    p16.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        p16MouseClicked(evt);
      }
    });
    p16.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p16ActionPerformed(evt);
      }
    });
    p16.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        p16KeyReleased(evt);
      }
    });

    labMsg.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
    labMsg.setForeground(new java.awt.Color(204, 0, 0));
    labMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labMsg.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(21, 21, 21)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(p04, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p05, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lab2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p06, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p07, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p08, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p09, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p10, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p11, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p12, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p13, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(btnSal, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(54, 54, 54)
            .addComponent(btnGra)
            .addGap(29, 29, 29)
            .addComponent(labMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jParam1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(28, 28, 28)
            .addComponent(lab01, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p01, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(34, 34, 34)
            .addComponent(lab02)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(p02, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(37, 37, 37)
            .addComponent(lab3)
            .addGap(18, 18, 18)
            .addComponent(p03, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(p14, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p15, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p16, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(13, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jParam1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lab01, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(p01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lab02, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lab3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(lab2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(p04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p06, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p07, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p08, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p09, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(btnSal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(btnGra, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addComponent(labMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void lab01MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lab01MouseClicked

  }//GEN-LAST:event_lab01MouseClicked

  private void p01MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p01MouseClicked
    p01.setEnabled(true);
  }//GEN-LAST:event_p01MouseClicked

  private void p01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p01ActionPerformed
  }//GEN-LAST:event_p01ActionPerformed

  private void p01KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p01KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p01KeyReleased

  private void p04MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p04MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p04MouseClicked

  private void p04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p04ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p04ActionPerformed

  private void p04KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p04KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p04KeyReleased

  private void p05MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p05MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p05MouseClicked

  private void p05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p05ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p05ActionPerformed

  private void p05KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p05KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p05KeyReleased

  private void lab02MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lab02MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_lab02MouseClicked

  private void p02MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p02MouseClicked
    p02.setEnabled(true);
  }//GEN-LAST:event_p02MouseClicked

  private void p02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p02ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p02ActionPerformed

  private void p02KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p02KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p02KeyReleased

  private void lab3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lab3MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_lab3MouseClicked

  private void p03MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p03MouseClicked
    p03.setEnabled(true);    // TODO add your handling code here:
  }//GEN-LAST:event_p03MouseClicked

  private void p03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p03ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p03ActionPerformed

  private void p03KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p03KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p03KeyReleased

  private void lab2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lab2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_lab2MouseClicked

  private void p06MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p06MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p06MouseClicked

  private void p06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p06ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p06ActionPerformed

  private void p06KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p06KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p06KeyReleased

  private void p07MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p07MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p07MouseClicked

  private void p07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p07ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p07ActionPerformed

  private void p07KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p07KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p07KeyReleased

  private void p08MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p08MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p08MouseClicked

  private void p08ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p08ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p08ActionPerformed

  private void p08KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p08KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p08KeyReleased

  private void p09MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p09MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p09MouseClicked

  private void p09ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p09ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p09ActionPerformed

  private void p09KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p09KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p09KeyReleased

  private void p10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p10MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p10MouseClicked

  private void p10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p10ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p10ActionPerformed

  private void p10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p10KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p10KeyReleased

  private void p11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p11MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p11MouseClicked

  private void p11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p11ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p11ActionPerformed

  private void p11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p11KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p11KeyReleased

  private void p12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p12MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p12MouseClicked

  private void p12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p12ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p12ActionPerformed

  private void p12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p12KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p12KeyReleased

  private void p13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p13MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p13MouseClicked

  private void p13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p13ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p13ActionPerformed

  private void p13KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p13KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p13KeyReleased

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    RegPar.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed
    modificaParametros();
  }//GEN-LAST:event_btnGraActionPerformed

  public void modificaParametros() {

    pp01 = p01.getText().trim();
    pp02 = p02.getText().trim();
    pp03 = p03.getText().trim();
    pp04 = p04.getText().trim();
    pp05 = p05.getText().trim();
    pp06 = p06.getText().trim();
    pp07 = p07.getText().trim();
    pp08 = p08.getText().trim();
    pp09 = p09.getText().trim();
    pp10 = p10.getText().trim();
    pp11 = p11.getText().trim();
    pp12 = p12.getText().trim();
    pp13 = p13.getText().trim();
    pp14 = p14.getText().trim();
    pp15 = p15.getText().trim();
    pp16 = p16.getText().trim();

    Parametros p = new Parametros(pp01, pp02, pp03, pp04, pp05, pp06, pp07, pp08, pp09, pp10, pp11, pp12, pp13, pp14, pp15, pp16);
    if (p.modificarParametros()) {
      labMsg.setText("- Se Actualizo registro -");
    }
  }

  private void jParam1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jParam1MouseClicked
  }//GEN-LAST:event_jParam1MouseClicked

  private void p14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p14MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p14MouseClicked

  private void p14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p14ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p14ActionPerformed

  private void p14KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p14KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p14KeyReleased

  private void p15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p15MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p15MouseClicked

  private void p15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p15ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p15ActionPerformed

  private void p15KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p15KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p15KeyReleased

  private void p16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p16MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_p16MouseClicked

  private void p16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p16ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_p16ActionPerformed

  private void p16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p16KeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_p16KeyReleased

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
      java.util.logging.Logger.getLogger(Registro_Parametros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Registro_Parametros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Registro_Parametros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Registro_Parametros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registro_Parametros().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnGra;
  private javax.swing.JButton btnSal;
  private javax.swing.JLabel jParam1;
  private javax.swing.JLabel lab01;
  private javax.swing.JLabel lab02;
  private javax.swing.JLabel lab2;
  private javax.swing.JLabel lab3;
  private javax.swing.JLabel labMsg;
  private javax.swing.JTextField p01;
  private javax.swing.JTextField p02;
  private javax.swing.JTextField p03;
  private javax.swing.JTextField p04;
  private javax.swing.JTextField p05;
  private javax.swing.JTextField p06;
  private javax.swing.JTextField p07;
  private javax.swing.JTextField p08;
  private javax.swing.JTextField p09;
  private javax.swing.JTextField p10;
  private javax.swing.JTextField p11;
  private javax.swing.JTextField p12;
  private javax.swing.JTextField p13;
  private javax.swing.JTextField p14;
  private javax.swing.JTextField p15;
  private javax.swing.JTextField p16;
  // End of variables declaration//GEN-END:variables
}
