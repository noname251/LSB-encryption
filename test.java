import java.util.Random;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        String filename = "test3.BMP";
        BMPImage image = BMPImage.readBMP(filename);
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入要加密的信息: ");
        String input = scanner.nextLine();
        byte[] secretInfo = input.getBytes();
        // 加密并获取加密后的data
        byte[] encryptData = EncryptionAndDecryption.encrypt8(secretInfo, image.getData(), 113);
        // 存储加密后的图片
        image.setData(encryptData);
        BMPImage.writeBMP(image, "encrypt_with_key_"+filename);


        // 解密灰度图
        System.out.println("解密中...");
        // 读取图片
        BMPImage encryptImage = BMPImage.readBMP("encrypt_with_key_"+filename);
        BMPImage originImage = BMPImage.readBMP(filename);
        // 解密图片信息并打印
        if(encryptImage != null && originImage != null){
            System.out.println('\n'+Until.byteToString(EncryptionAndDecryption.decrypt8(originImage.getData(), encryptImage.getData(), 113)));
        }else {
            System.out.println("文件名有误");
        }
    }
}
