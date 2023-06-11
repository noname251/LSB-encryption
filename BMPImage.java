import java.io.FileInputStream;
import java.io.IOException;

public class BMPImage {
    private int width;
    private int height;
    private int bitsPerPixel;
    private byte[] data;

    public BMPImage(int width, int height, int bitsPerPixel, byte[] data) {
        this.width = width;
        this.height = height;
        this.bitsPerPixel = bitsPerPixel;
        this.data = data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBitsPerPixel() {
        return bitsPerPixel;
    }

    public byte[] getData() {
        return data;
    }

    public static BMPImage readBMP(String filename) {
        try (FileInputStream file = new FileInputStream(filename)) {
            // 读取BMP文件头
            byte[] header = new byte[54];
            file.read(header);

            // 解析BMP文件头
            int width = byteArrayToInt(header, 18, 4);
            int height = byteArrayToInt(header, 22, 4);
            //biBitCount 用来判断是
            int bitsPerPixel = byteArrayToInt(header, 28, 2);

            // 计算每行像素的字节数， 这样可以处理不仅仅是灰度图和全彩图， 妙啊
            int rowSize = (width * bitsPerPixel + 31) / 32 * 4;

            // 读取图像数据
            byte[] imageData = new byte[rowSize * height];
            file.read(imageData);

            return new BMPImage(width, height, bitsPerPixel, imageData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int byteArrayToInt(byte[] bytes, int offset, int length) {
        int value = 0;
        for (int i = 0; i < length; i++) {
            // 0xFF   11111111 就是一个byte的全1，(bytes[offset + i] & 0xFF)是一个右值
            // 小端存储表示小的数据存储在低地址
            // 数组的前面对应的是低地址。 例如int 1 在二进制数组中的数据为 0x01 0x00 0x00 0x00
            // 例如这个循环中，如果是大段存储，就应该把 8*i 改成 8 * (3-i)
            value |= (bytes[offset + i] & 0xFF) << (8 * i);
        }
        return value;
    }
}