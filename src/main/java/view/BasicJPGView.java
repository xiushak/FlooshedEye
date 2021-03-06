package view;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.SimpleModel;
import utils.ErrorCheckers;

/**
 * A basic implementation of the {@link SimpleView} interface but saves as a jpg to save space.
 */

public class BasicJPGView implements SimpleView {

  private final SimpleModel model;

  /**
   * Creates a view with the given model
   *
   * @param model the model to be set
   * @throws IllegalArgumentException if a null model is given
   */
  public BasicJPGView(SimpleModel model) throws IllegalArgumentException {
    ErrorCheckers.checkNull(model, "null model given");
    this.model = model;
  }

  @Override
  public void outputImage(String filename) throws IllegalArgumentException, IOException {
    ErrorCheckers.checkNull(filename, "null filename given");
    File outFile = new File(filename);
    if (outFile.getParentFile() != null) {
      outFile.getParentFile().mkdirs();
    }

    // remove alpha channel for jpg
    BufferedImage originalImg = model.getImage();
    BufferedImage newImage = new BufferedImage(originalImg.getWidth(), originalImg.getHeight(),
        BufferedImage.TYPE_INT_RGB);
    newImage.createGraphics().drawImage(originalImg, 0, 0, Color.black, null);

    ImageIO.write(newImage, "jpg", outFile);
  }
}
