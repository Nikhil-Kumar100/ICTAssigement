import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CipherToImageConverter {
    public static void main(String[] args) {
        String desCipherFile = "DES_Encrypted.txt";
        String tripleDesCipherFile = "3DES_Encrypted.txt";
        String aesCipherFile = "AES_Encrypted.txt";
        String rsaCipherFile = "RSA_Encrypted.txt";

        try {
            // Read cipher data from files
            byte[] desCipherData = readFile(desCipherFile);
            byte[] tripleDesCipherData = readFile(tripleDesCipherFile);
            byte[] aesCipherData = readFile(aesCipherFile);
            //byte[] rsaCipherData = readFile(rsaCipherFile);

            // Generate images from cipher data
            generateImage(desCipherData, "DES_CipherImage.jpg");
            generateImage(tripleDesCipherData, "3DES_CipherImage.jpg");
            generateImage(aesCipherData, "AES_CipherImage.jpg");
            //generateImage(rsaCipherData, "RSA_CipherImage.jpg");

            System.out.println("Cipher images generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] readFile(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] data = new byte[(int) file.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(data);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    private static void generateImage(byte[] data, String outputPath) throws IOException {
        int imageSize = (int) Math.sqrt(data.length / 3);
        BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < imageSize; y++) {
            for (int x = 0; x < imageSize; x++) {
                int startIndex = (y * imageSize + x) * 3;
                int red = data[startIndex] & 0xFF;
                int green = data[startIndex + 1] & 0xFF;
                int blue = data[startIndex + 2] & 0xFF;

                int rgb = (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, rgb);
            }
        }

        File outputImage = new File(outputPath);
        ImageIO.write(image, "jpg", outputImage);
    }
}
