package model.facerecognition;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

/**
 * Uses OpenCV in python to find a face. Returns a rectangle that contains the whole image if no face is
 * found
 */

public class FaceRecognitionOPENCV implements FaceRecognition {

  public Rectangle findFace(BufferedImage bi) {
    //    // Used resource below to learn how to do this
    //    // https://www.geeksforgeeks.org/image-processing-java-set-9-face-detection/
    //    String currentLocation = (new File(".")).getAbsolutePath();
    //    String pathToDllfolder = currentLocation + "\\lib\\opencv\\x64\\opencv_java453.dll";
    //    System.load(pathToDllfolder);
    ////    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    //    CascadeClassifier faceDetector = new CascadeClassifier();
    //    faceDetector.load("haarcascade_frontalface_alt.xml");
    //
    //    // removes alpha values to convert BufferedImage into OpenCV Mat
    //    BufferedImage rgbImage = new BufferedImage(bi.getWidth(), bi.getHeight(),
    //        BufferedImage.TYPE_3BYTE_BGR);
    //    rgbImage.getGraphics().drawImage(bi, 0, 0, null);
    //
    //    byte[] pixels = ((DataBufferByte) rgbImage.getRaster().getDataBuffer()).getData();
    //    Mat image = new Mat(rgbImage.getHeight(), bi.getWidth(), CvType.CV_8UC3);
    //
    //    image.put(0, 0, pixels);
    //
    //    MatOfRect faceDetections = new MatOfRect();
    //    faceDetector.detectMultiScale(image, faceDetections);
    //
    //    Rect[] rects = faceDetections.toArray();
    //    // choose a random face
    //    int rectCount = rects.length;
    //    // if no face found
    //    if (rects.length == 0) {
    //      ;
    //      return new Rectangle(0, 0, bi.getWidth() / 2, bi.getHeight() / 2);
    //    }
    //    Random r = new Random();
    //    int face = r.nextInt(rects.length);
    //    Rect rect = rects[face];
    //    // returns rectangle
    String outFile = "src/main/java/model/facerecognition/outFile.jpg";
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
    String script = "src/main/java/model/facerecognition/FaceRecognitionOPENCVPython.py";

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
