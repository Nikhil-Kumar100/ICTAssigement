import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageToTextConverter {
    public static void main(String[] args) {
        String imagePath = "Targetimage.jpg";
        String outputTextFile = "Targettext.txt";

        try {
            // Read the image
            BufferedImage image = ImageIO.read(new File(imagePath));

            // Get the image dimensions
            int width = image.getWidth();
            int height = image.getHeight();

            // Open the text file for writing
            FileWriter writer = new FileWriter(outputTextFile);

            // Write pixel values to the text file
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Get the RGB value of the pixel
                    Color color = new Color(image.getRGB(x, y));

                    // Write the RGB values to the text file
                    writer.write(color.getRed() + " " + color.getGreen() + " " + color.getBlue() + "\n");
                }
            }

            // Close the writer
            writer.close();

            System.out.println("Image converted to text successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
