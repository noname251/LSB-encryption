import java.util.Scanner;

public class main{
    public static void main(String[] args) {
        String filename = "test3.BMP";
        BMPImage image = BMPImage.readBMP(filename);
        //加密
        if (image != null) {
            int width = image.getWidth();
            System.out.println("Width: " + width);
            int height = image.getHeight();
            System.out.println("Height: " + height);
            int bitsPerPixel = image.getBitsPerPixel();
            System.out.println("Bits Per Pixel: " + bitsPerPixel);
            // 处理图像数据
            byte[] imageData = image.getData();
            System.out.println("加密中...\n最长加密长度为:"+imageData.length/8+"字节");
            // 加密灰度图
                //读取加密信息
            Scanner scanner = new Scanner(System.in);
            System.out.print("请输入要加密的信息: ");
            String input = scanner.nextLine();
            byte[] secretInfo = input.getBytes();
                // 加密并获取加密后的data
            byte[] encryptData = EncryptionAndDecryption.encrypt8(secretInfo, imageData);
            if(encryptData.length==0){
                System.out.println("加密失败");
                return ;
            }
                // 存储加密后的图片
            image.setData(encryptData);
            BMPImage.writeBMP(image, "encrypt_"+filename);
        }else {
            System.out.println("文件名有误");
        }

        // 解密灰度图
        System.out.println("解密中...");
        // 读取图片
        BMPImage encryptImage = BMPImage.readBMP("encrypt_"+filename);
        BMPImage originImage = BMPImage.readBMP(filename);
        // 解密图片信息并打印
        if(encryptImage != null && originImage != null){
            System.out.println('\n'+Until.byteToString(EncryptionAndDecryption.decrypt8(originImage.getData(), encryptImage.getData())));
        }else {
            System.out.println("文件名有误");
        }
    }

}

