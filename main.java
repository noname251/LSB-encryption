import java.util.Arrays;
import java.util.Scanner;

public class main{
    public static void main(String[] args) {
        String filename = "4X4.BMP";
        BMPImage image = BMPImage.readBMP(filename);

        if (image != null) {
            int width = image.getWidth();
            System.out.println("Width: " + width);
            int height = image.getHeight();
            System.out.println("Height: " + height);
            int bitsPerPixel = image.getBitsPerPixel();
            System.out.println("Bits Per Pixel: " + bitsPerPixel);
            // 处理图像数据
            byte[] imageData = image.getData();
            System.out.println("加密中...");
            // 加密灰度图
                //读取加密信息
            Scanner scanner = new Scanner(System.in);
            System.out.print("请输入要加密的信息: ");
            String input = scanner.nextLine();
            byte[] secretInfo = input.getBytes();
                // 加密并获取加密后的data
            byte[] encryptData = EncryptionAndDecryption.encrypt8(secretInfo, imageData);
                // 存储加密后的数据
            image.setData(encryptData);
            BMPImage.writeBMP(image, "encrypt_"+filename);

            // 解密灰度图
            System.out.println("解密中...");
            BMPImage image1 = BMPImage.readBMP("encrypt_"+filename);
            System.out.println('\n'+Until.byteToString(EncryptionAndDecryption.decrypt8(imageData, image1.getData())));
        }
    }

}

