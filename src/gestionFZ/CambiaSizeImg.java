package gestionFZ;

import static comun.MetodosComunes.FileExist;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import modelo.ConexionSQL;

public class CambiaSizeImg {

  public static void main(String[] args) throws IOException, SQLException {

    String cop = "", rut1 = "", rut2 = "";

    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      String sql = "SELECT cop "
              + "FROM listaprecios "
              + "order by cop";

      System.out.println("sql=" + sql);

      rs = st.executeQuery(sql);
      while (rs.next()) {

        cop = rs.getString("cop");

        rut1 = "imgprd/" + cop + ".png";
        rut2 = "imgprd2/" + cop + ".png";

        //System.out.println("rut1=" + rut1);
        if (FileExist(rut1)) {

          //System.out.println("si rut1=" + rut1);
          String imgD = rut1;
          String imgH = rut2;

          int scaledWidth = 100;
          int scaledHeight = 100;

          // read input image
          BufferedImage inputImage = ImageIO.read(new File(imgD));

          // create output image
          BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

          // scales the input image to the output image
          java.awt.Graphics2D g2d = outputImage.createGraphics();
          g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
          g2d.dispose();

          // writes to output file
          ImageIO.write(outputImage, "png", new File(imgH));

        }

      }

      System.out.println("Listo!");
      
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(CambiaSizeImg.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

}
