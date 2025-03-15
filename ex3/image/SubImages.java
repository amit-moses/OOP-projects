package image;
import java.awt.*;

/**
 * The SubImages class is responsible for dividing an image into sub-images and calculating their brightness.
 * It provides methods to retrieve the brightness of specific sub-images.
 *
 *  @author Emmanuelle Schnitzer
 *  @author Amit Moses
 */
public class SubImages {
    //variables for the RGB to grayscale conversion
    private static final double MAX_RGB = 255;
    private final static double RED = 0.2126;
    private final static double GREEN = 0.7152;
    private final static double BLUE = 0.0722;

    /**
     * A 2D array storing the brightness values of the sub-images.
     */
    private final double[][] brightnessArr;

    /**
     * Constructs a SubImages object with the specified image and resolution.
     * Divides the image into sub-images and calculates their brightness.
     *
     * @param originalImage the original image to be divided
     * @param resolution the resolution of the sub-images
     */
    public SubImages(Image originalImage, int resolution) {
        int subImageSize = originalImage.getWidth() / resolution;
        int numRows = originalImage.getHeight() / subImageSize;
        this.brightnessArr = new double[numRows][resolution];
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < resolution; j++){
                int cordX = i * subImageSize;
                int cordY = j * subImageSize;
                this.brightnessArr[i][j] = calcBrightness(originalImage,cordX, cordY, subImageSize);
            }
        }
    }

    /**
     * Returns the brightness of the sub-image at the specified coordinates.
     *
     * @param x the x-coordinate of the sub-image
     * @param y the y-coordinate of the sub-image
     * @return the brightness of the sub-image
     */
    public double getBrightness(int x, int y){
        return brightnessArr[x][y];
    }

    /**
     * Calculates the brightness of a sub-image starting at the specified coordinates.
     *
     * @param cordX the x-coordinate of the top-left corner of the sub-image
     * @param cordY the y-coordinate of the top-left corner of the sub-image
     * @param sizeSubImage the size of the sub-image
     * @return the brightness of the sub-image
     */
    private double calcBrightness(Image image,int cordX, int cordY, int sizeSubImage){
        double brightnessCalc = 0;
        for(int x = cordX; x < cordX + sizeSubImage; x++){
            for(int y = cordY; y < cordY + sizeSubImage; y++){
                brightnessCalc += getGreyScale(image,x, y);
            }
        }
        return brightnessCalc / (sizeSubImage * sizeSubImage * MAX_RGB) ;
    }

    /**
     * Converts the color of a pixel to its grayscale value.
     *
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @return the grayscale value of the pixel
     */
    private double getGreyScale(Image image, int x, int y){
        Color pixel = image.getPixel(x, y);
        return RED * pixel.getRed() +
                GREEN * pixel.getGreen() +
                BLUE * pixel.getBlue();
    }
}