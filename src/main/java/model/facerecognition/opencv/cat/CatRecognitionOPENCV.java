package model.facerecognition.opencv.cat;

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

public class CatRecognitionOPENCV implements FaceRecognition {

  public Rectangle findFace(BufferedImage bi) {
    String outFile = "lib/opencv-cat/outFile.jpg";
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
    String script = "lib/opencv-cat/FaceRecognitionOPENCVCatPython.py";

    ProcessBuilder processBuilder = new ProcessBuilder("python3", script);
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
    int[] rect = new int[4];
    int x, y, w, h;
    String s;
    String out = "";
    try {
      for (int i = 0; i < 4; i++) {
        s = in.readLine();
        out += s;
        try {
          rect[i] = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
          while ((s = in.readLine()) != null) {
            out += s;
          }
          System.out.println("invalid output from python script:");
          System.out.println(out);
        }
      }
    }
    catch (IOException e) {
      System.out.println("failed to read python script output");
      return new Rectangle(0, 0, bi.getWidth(), bi.getHeight());
    }

    file.delete();

    return new
        Rectangle(rect[0], rect[1], rect[2], rect[3]);
  }
}
