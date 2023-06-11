import java.util.Arrays;

public class main{
    public static void main(String[] args) {
        String filename = "4X2.BMP";
        BMPImage image = BMPImage.readBMP(filename);

        if (image != null) {
            System.out.println("Width: " + image.getWidth());
            System.out.println("Height: " + image.getHeight());
            System.out.println("Bits Per Pixel: " + image.getBitsPerPixel());
            // 处理图像数据
            byte[] imageData = image.getData();
            printData(imageData);
            System.out.println("加密中...");
            // 加密灰度图
            String secretInfo = "?";
            byte[] encryptData = encrypt8(secretInfo, imageData);
            printData(encryptData);
            // 解密灰度图
            System.out.println("解密中...");
            System.out.println((char) decrypt8(imageData, encryptData)[0]);
        }
    }
    // 加密灰度图
    public static byte[] encrypt8(String secretInfo, byte[] imageData){
        byte[] encryptData = new byte[imageData.length];
        // 对于灰度图来说有多少个像素就能存储多少bits的数据
        // 长度验证
        int maxInfoByte = imageData.length/8;
        byte ch = (byte)secretInfo.charAt(0);
        // 提取byte中每个位的值
        printByte(ch);
        System.out.println('\n');
        // 将char中每个位的值与int的最后一位进行异或操作
        for (int i = 0; i < 8; i++) {
            byte bitValue = (byte) ((ch >> i) & 1);
            encryptData[i] = (byte) (bitValue ^ imageData[i]);
        }
        return encryptData;
    }
    public static byte[] decrypt8(byte[] originImageData, byte[] encryptImageData) {
        byte[] secretInfo = new byte[encryptImageData.length/8];
        for(int i=0;i<secretInfo.length;i++){
            byte info = 0;
            for(int j=0; j<8; j++){
                int temp = originImageData[i*8+j] ^ encryptImageData[i*8+j];
                System.out.println(temp);
                info |= (temp) << j;
            }
            secretInfo[i] = info;
        }
        printByte(secretInfo[0]);
        return secretInfo;
    }

    public static void printData(byte[] imageData){
        for(int i=0; i<imageData.length; i++ ){
            System.out.println(imageData[i]);
        }
    }

    public static void printByte(byte ch){
        for (int i = 0; i < 8; i++) {
            int bitValue = (ch >> i) & 1;
            System.out.print(bitValue);
        }
    }
}

