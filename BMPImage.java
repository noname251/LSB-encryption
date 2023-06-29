import java.io.*;
import java.nio.channels.FileChannel;

public class BMPImage {
    private int width;
    private int height;
    private int bitsPerPixel;
    private byte[] header;
    private byte[] body;
    private byte[] tail;

    public BMPImage(int width, int height, int bitsPerPixel,byte[] header, byte[] data, byte[] body) {
        this.width = width;
        this.height = height;
        this.bitsPerPixel = bitsPerPixel;
        this.header = header;
        this.tail = data;
        this.body = body;
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
        return tail;
    }

    public byte[] getHeader(){
        return header;
    }

    public byte[] getBody(){
        return body;
    }

    public void setData(byte[] data){
        this.tail = data;
    }

    public static BMPImage readBMP(String filename) {
        try (FileInputStream file = new FileInputStream(filename)) {
            // 读取BMP文件头
            byte[] header = new byte[54];
            file.read(header);
            // 解析BMP文件头
            int width = byteArrayToInt(header, 18, 4);
            int height = byteArrayToInt(header, 22, 4);
            //biBitCount 用来判断是灰度图还是全彩图
            int bitsPerPixel = byteArrayToInt(header, 28, 2);

            // 计算每行像素的字节数， 这样可以处理不仅仅是灰度图和全彩图， 妙啊
            int rowSize = (width * bitsPerPixel + 31) / 32 * 4;

            // 读取图像数据
            byte[] imageData = new byte[rowSize * height];
            file.read(imageData);

            // 读取body部分
            int dataLength = (int) (file.getChannel().size() - 54 - imageData.length);
            byte[] body = new byte[dataLength];
            file.read(body);
            return new BMPImage(width, height, bitsPerPixel, header, imageData,body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeBMP(BMPImage image, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             DataOutputStream dos = new DataOutputStream(fos)) {
            // 写入文件头部字节数组
            dos.write(image.getHeader());
            // 写入图像数据字节数组
            dos.write(image.getData());
            dos.write(image.getBody());
            System.out.println("BMP文件写入成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
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