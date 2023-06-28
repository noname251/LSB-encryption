import java.util.Arrays;

public class EncryptionAndDecryption {
    // 加密灰度图
    public static byte[] encrypt8(byte[] secretInfo, byte[] imageData){
        byte[] encryptData = new byte[imageData.length];
        System.arraycopy(imageData, 0, encryptData, 0, imageData.length);
        // 对于灰度图来说有多少个像素就能存储多少bits的数据
        // 长度验证
        int maxInfoBits = imageData.length/8;
        System.out.println("当前信息字节数为:"+secretInfo.length);
        if(secretInfo.length > maxInfoBits){
            System.out.println("长度超出限制");
            return new byte[0];
        }
        // 提取byte中每个位的值
//        printByte(secretInfo);
        System.out.println('\n');
        // 将char中每个位的值与int的最后一位进行异或操作
        // 加密信息格式为 信息长度length + data
        for(int i=-1; i<secretInfo.length; i++){
            byte ch;
            if(i == -1){
                ch = (byte)secretInfo.length;
            }else {
                ch = secretInfo[i];
            }
            for (int j = 0; j < 8; j++) {
                byte bitValue = (byte) ((ch >> j) & 1);
                encryptData[(i+1)*8+j] = (byte) (bitValue ^ imageData[(i+1)*8+j]);
            }
        }
        return encryptData;
    }
    public static byte[] decrypt8(byte[] originImageData, byte[] encryptImageData) {
        byte[] secretInfo = new byte[encryptImageData.length/8];
        int secretLength = 0;
        Arrays.fill(secretInfo, (byte) 0);
        // 先获取加密信息长度，然后获取信息
        for(int j=0; j<8; j++){
            int temp = originImageData[j] ^ encryptImageData[j];
            secretLength |= (temp) << j;
        }
        // 获取加密信息
        for(int i=0;i<secretLength;i++){
            byte info = 0;
            for(int j=0; j<8; j++){
                int temp = originImageData[(i+1)*8+j] ^ encryptImageData[(i+1)*8+j];
                info |= (temp) << j;
            }
            secretInfo[i] = info;
        }
        secretInfo = Arrays.copyOfRange(secretInfo, 0, secretLength);
        return secretInfo;
    }
}
