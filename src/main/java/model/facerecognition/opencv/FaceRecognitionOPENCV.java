package model.facerecognition.opencv;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import model.facerecognition.FaceRecognition;
import model.facerecognition.Rectangle;

/**
 * Uses OpenCV in python to find a face. Returns a rectangle that contains the whole image if no
 * face is found
 */

public class FaceRecognitionOPENCV implements FaceRecognition {

  public Rectangle findFace(BufferedImage bi) {
    String outFile = "opencv-outFile.jpg";
    File file = new File(outFile);
    try {
      BufferedImage newImage = new BufferedImage(bi.getWidth(), bi.getHeight(),
          BufferedImage.TYPE_INT_RGB);
      newImage.createGraphics().drawImage(bi, 0, 0, Color.black, null);
      ImageIO.write(newImage, "jpg", file);
    }
    catch (IOException e) {
      System.out.println("failed to find face: error outputing image to python script");
      return new Rectangle(0, 0, bi.getWidth(), bi.getHeight());
    }
    String script = "src/main/java/model/facerecognition/opencv/FaceRecognitionOPENCVPython.py";

    ProcessBuilder processBuilder = new ProcessBuilder("python", script);
    processBuilder.redirectErrorStream(true);
    Process process;
    try {
      process = processBuilder.start();
    }
    catch (IOException e) {
      System.out.println("failed to start python script");
      return new Rectangle(0, 0, bi.getWidth(), bi.getHeight());
    }
    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
    int x, y, w, h;
    try {
      x = Integer.parseInt(in.readLine());
      y = Integer.parseInt(in.readLine());
      w = Integer.parseInt(in.readLine());
      h = Integer.parseInt(in.readLine());
    }
    catch (IOException | NumberFormatException e) {
      System.out.println("failed to read python script output");
      return new Rectangle(0, 0, bi.getWidth(), bi.getHeight());
    }

    file.delete();

    return new
        Rectangle(x, y, w, h);
  }
}
