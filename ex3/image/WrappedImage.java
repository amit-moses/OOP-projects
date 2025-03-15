package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The WrappedImage class extends the Image class and provides additional
 * functionality for handling images with padding. It ensures that the image
 * dimensions are powers of two by adding padding around the original image.
 *
 *  @author Emmanuelle Schnitzer
 *  @author Amit Moses
 */
public class WrappedImage extends Image {
    /**
     * The base for the power of two calculation.
     */
    private static final int BASE = 2;

    /**
     * The padding height added to the original image.
     */
    private final int rH;

    /**
     * The padding width added to the original image.
     */
    private final int rW;

    /**
     * Constructs a WrappedImage object by loading an image from the specified file
     * and calculating the padding.
     *
     * @param filename the name of the file to load the image from
     * @throws IOException if an error occurs while reading the file
     */
    public WrappedImage(String filename) throws IOException {
        super(filename);
        this.rW = (newSize(super.getWidth()) - super.getWidth())/BASE;
        this.rH = (newSize(super.getHeight()) - super.getHeight())/BASE;
    }

    /**
     * Constructs a WrappedImage object with the specified pixel array, width, and height,
     * and calculates the padding.
     *
     * @param pixelArray a 2D array of Color objects representing the pixels of the image
     * @param width the width of the image
     * @param height the height of the image
     */
    public WrappedImage(Color[][] pixelArray, int width, int height) {
        super(pixelArray, width, height);
        this.rW = (newSize(super.getWidth()) - super.getWidth())/BASE;
        this.rH = (newSize(super.getHeight()) - super.getHeight())/BASE;
    }

    /**
     * Calculates the new size of the image to be a power of two.
     *
     * @param size the original size of the image
     * @return the new size of the image, which is a power of two
     */
    private static int newSize(int size){
        double log2Value = Math.log(size) / Math.log(BASE);
        int result = (int) Math.ceil(log2Value);
        return (int)Math.pow(BASE,result);
    }

    /**
     * Returns the width of the image including the padding.
     *
     * @return the width of the image including the padding
     */
    @Override
    public int getWidth() {
        return super.getWidth() + (BASE * rW);
    }

    /**
     * Returns the height of the image including the padding.
     *
     * @return the height of the image including the padding
     */
    @Override
    public int getHeight() {
        return super.getHeight() + (BASE * rH);
    }

    /**
     * Saves the image to a file with the specified name, including the padding.
     * The image is saved in JPEG format.
     *
     * @param fileName the name of the file to save the image to
     */
    @Override
    public void saveImage(String fileName){
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(),
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < getHeight(); x++) {
            for (int y = 0; y < getWidth(); y++) {
                bufferedImage.setRGB(y, x, getPixel(x, y).getRGB());
            }
        }
        File outputfile = new File(fileName+".jpeg");
        try {
            ImageIO.write(bufferedImage, "jpeg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the color of the pixel at the specified coordinates, considering the padding.
     * If the coordinates are within the padding area, it returns white color.
     *
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @return the color of the pixel at the specified coordinates
     */
    public Color getPixel(int x, int y){
        int originalWidth = super.getWidth();
        int originalHeight = super.getHeight();
        return rH <= x && x < rH + originalHeight && rW <= y && y < rW + originalWidth ?
                super.getPixel(x - rH, y - rW) : Color.WHITE;
    }
}