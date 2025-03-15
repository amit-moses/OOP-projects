package ascii_art;

import image.Image;
import image.SubImages;
import image_char_matching.SubImgCharMatcher;

/**
 * The AsciiArtAlgorithm class is responsible for converting an image into an ASCII art representation.
 * It uses a specified resolution and character set to map image brightness to ASCII characters.
 *
 *  @author Emmanuelle Schnitzer
 *  @author Amit Moses
 */
public class AsciiArtAlgorithm {
    /**
     * The error message for an incorrect resolution format.
     */
    private static final String ERROR_RESOLUTION = "Did not change resolution due to incorrect format.";

    /**
     * The image to be converted to ASCII art.
     */
    private final Image image;

    /**
     * The resolution of the ASCII art.
     */
    private int resolution;

    /**
     * The SubImages object that holds the brightness values of sub-images.
     */
    private SubImages subImages;

    /**
     * The SubImgCharMatcher object that maps brightness values to ASCII characters.
     */
    private final SubImgCharMatcher subImgCharMatcher;

    /**
     * Constructs an AsciiArtAlgorithm with the specified image, resolution, and character set.
     *
     * @param image the image to be converted to ASCII art
     * @param resolution the resolution of the ASCII art
     * @param charset the character set used for mapping brightness to characters
     */
    public AsciiArtAlgorithm(Image image, int resolution, char[] charset) {
        this.image = image;
        this.resolution = resolution;
        Snapshot snapshot = AsciiArtDB.getInstance().rollback();
        this.subImgCharMatcher = snapshot != null &&
                equalsArr(snapshot.subImgCharMatcher, charset) ?
                snapshot.subImgCharMatcher : new SubImgCharMatcher(charset);
    }

    /**
     * Checks if the given SubImgCharMatcher object matches the specified character set.
     *
     * @param sub the SubImgCharMatcher object to check
     * @param charset the character set to compare
     * @return true if the SubImgCharMatcher matches the character set, false otherwise
     */
    private static boolean equalsArr(SubImgCharMatcher sub, char[] charset) {
        if (sub == null || charset.length != sub.getSize()) {
            return false;
        }
        for (char c : charset) {
            if (!sub.isCharContain(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the current resolution of the ASCII art.
     *
     * @return the current resolution
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * Sets a new resolution for the ASCII art.
     *
     * @param newResolution the new resolution to set
     * @throws IllegalArgumentException if the new resolution is invalid
     */
    public void setResolution(int newResolution) {
        if (newResolution > image.getWidth() ||
                newResolution <= Math.min(1, image.getWidth() / image.getHeight())) {
            throw new IllegalArgumentException(ERROR_RESOLUTION);
        }
        this.resolution = newResolution;
    }

    /**
     * Gets the SubImgCharMatcher object used for mapping brightness to characters.
     *
     * @return the SubImgCharMatcher object
     */
    public SubImgCharMatcher getSubImgCharMatcher() {
        return subImgCharMatcher;
    }

    /**
     * Sets a new rounding value for brightness to character mapping.
     *
     */
    public void setRound(int round) {
        subImgCharMatcher.setTypeRound(round);
    }

    /**
     * Updates the SubImages object with the brightness values of sub-images.
     */
    private void updateSubImageArr() {
        Snapshot snapshot = AsciiArtDB.getInstance().rollback();
        if (snapshot != null && snapshot.image.equals(image) &&
                snapshot.resolution == resolution) {
            this.subImages = snapshot.subImages;
            return;
        }

        this.subImages = new SubImages(image, resolution);
    }

    /**
     * Runs the ASCII art conversion algorithm and returns the resulting ASCII art.
     *
     * @return a 2D char array representing the ASCII art
     */
    public char[][] run() {
        int subImageSize = image.getWidth() / resolution;
        int numRows = image.getHeight() / subImageSize;

        updateSubImageArr();

        char[][] asciiImage = new char[numRows][resolution];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < resolution; j++) {
                double brightness = subImages.getBrightness(i, j);
                asciiImage[i][j] = subImgCharMatcher.getCharByImageBrightness(brightness);
            }
        }

        AsciiArtDB.getInstance().commit(createSnapshot());
        return asciiImage;
    }

    /**
     * Creates a snapshot of the current state of the AsciiArtAlgorithm.
     *
     * @return a Snapshot object representing the current state
     */
    public Snapshot createSnapshot() {
        return new Snapshot(image, resolution, subImages, subImgCharMatcher);
    }

    /**
     * The Snapshot class represents a snapshot of the state of the AsciiArtAlgorithm.
     */
    public static class Snapshot {
        /**
         * The image used in the snapshot.
         */
        private final Image image;

        /**
         * The resolution used in the snapshot.
         */
        private final int resolution;

        /**
         * The SubImages object used in the snapshot.
         */
        private final SubImages subImages;

        /**
         * The SubImgCharMatcher object used in the snapshot.
         */
        private final SubImgCharMatcher subImgCharMatcher;

        /**
         * Constructs a Snapshot with the specified image, resolution, SubImages, and SubImgCharMatcher.
         *
         * @param image the image used in the snapshot
         * @param resolution the resolution used in the snapshot
         * @param subImages the SubImages object used in the snapshot
         * @param subImgCharMatcher the SubImgCharMatcher object used in the snapshot
         */
        public Snapshot(Image image, int resolution, SubImages subImages,
                        SubImgCharMatcher subImgCharMatcher) {
            this.image = image;
            this.resolution = resolution;
            this.subImages = subImages;
            this.subImgCharMatcher = subImgCharMatcher;
        }
    }
}