import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;

public class EncryptionAndDecryption {
    // 加密灰度图
    public static byte[] encrypt8(byte[] secretInfo, byte[] imageData){
        byte[] encryptData = new byte[imageData.length];
        byte[] secretLength = Until.intToBytes(secretInfo.length);
        System.arraycopy(imageData, 0, encryptData, 0, imageData.length);
        // 对于灰度图来说有多少个像素就能存储多少bits的数据
        // 长度验证
        int maxInfoBits = imageData.length/8-4;
        System.out.println("当前信息字节数为:"+secretInfo.length);
        if(secretInfo.length > maxInfoBits){
            System.out.println("长度超出限制");
            return new byte[0];
        }
        System.out.println('\n');
        // 将char中每个位的值与int的最后一位进行异或操作
        // 加密信息格式为 信息长度length + data
        for(int i=-4; i<secretInfo.length; i++){
            byte ch;
            if(i < 0){
                ch = secretLength[i+4];
            }else {
                ch = secretInfo[i];
            }
            for (int j = 0; j < 8; j++) {
                byte bitValue = (byte) ((ch >> j) & 1);
                encryptData[(i+4)*8+j] = (byte) (bitValue ^ imageData[(i+4)*8+j]);
            }
        }
        return encryptData;
    }
    public static byte[] decrypt8(byte[] originImageData, byte[] encryptImageData) {
        byte[] secretInfo = new byte[encryptImageData.length/8];
        int secretLength;
        byte[] secretLengthBytes = new byte[4];
        Arrays.fill(secretInfo, (byte) 0);
        // 先获取加密信息长度，然后获取信息
        for(int i=0;i<4;i++){
            byte info=0;
            for(int j=0; j<8; j++){
                int temp = originImageData[i*8+j] ^ encryptImageData[i*8+j];
                info |= (temp) << j;
            }
            secretLengthBytes[i] = info;
        }
        secretLength = Until.bytesToInt(secretLengthBytes);
        // 获取加密信息
        for(int i=0;i<secretLength;i++){
            byte info = 0;
            for(int j=0; j<8; j++){
                int temp = originImageData[(i+4)*8+j] ^ encryptImageData[(i+4)*8+j];
                info |= (temp) << j;
            }
            secretInfo[i] = info;
        }
        secretInfo = Arrays.copyOfRange(secretInfo, 0, secretLength);
        return secretInfo;
    }

    public static byte[] encrypt8(byte[] secretInfo, byte[] imageData, int key){
        Random random = new Random(key);
        byte[] encryptData = new byte[imageData.length];
        byte[] secretLength = Until.intToBytes(secretInfo.length);
        Integer[] sequence = new Integer[(int)((secretInfo.length+4)*1.2)];
        // 生成序列
        for(int i=0; i<sequence.length; i++){
            sequence[i] = random.nextInt(0, imageData.length/8);
        }
        // 对序列去除重复，同时保持原有顺序
        LinkedHashSet<Integer> set = new LinkedHashSet<>(Arrays.asList(sequence));
        sequence = set.toArray(new Integer[0]);
        System.out.println("加密随机序列为：");
        System.out.println(Arrays.toString(sequence));
        System.arraycopy(imageData, 0, encryptData, 0, imageData.length);
        // 对于灰度图来说有多少个像素就能存储多少bits的数据
        // 长度验证
        int maxInfoBits = imageData.length/8;
        System.out.println("当前信息字节数为:"+secretInfo.length);
        if(secretInfo.length > maxInfoBits){
            System.out.println("长度超出限制");
            return new byte[0];
        }
        System.out.println('\n');
        // 将char中每个位的值与int的最后一位进行异或操作
        // 加密信息格式为 信息长度length + data
        for(int i=-4; i<secretInfo.length; i++){
            byte ch;
            if(i < 0){
                ch = secretLength[i+4];
            }else {
                ch = secretInfo[i];
            }
            for (int j = 0; j < 8; j++) {
                byte bitValue = (byte) ((ch >> j) & 1);
                encryptData[sequence[i+4]*8+j] = (byte) (bitValue ^ imageData[(sequence[i+4])*8+j]);
            }
        }
        return encryptData;
    }

    public static byte[] decrypt8(byte[] originImageData, byte[] encryptImageData, int key) {
        Random random = new Random(key);
        int secretLength;
        byte[] secretLengthBytes = new byte[4];
        Integer[] lengthIndex = new Integer[4];
        // 获取加密信息长度
        for(int i=0;i<4;i++){
            int index = random.nextInt(0, originImageData.length/8);
            lengthIndex[i] = index;
            byte info=0;
            for(int j=0; j<8; j++){
                int temp = originImageData[index*8+j] ^ encryptImageData[index*8+j];
                info |= (temp) << j;
            }
            secretLengthBytes[i] = info;
        }
        secretLength = Until.bytesToInt(secretLengthBytes);
        System.out.println(secretLength);
        byte[] secretInfo = new byte[secretLength+2];
        Arrays.fill(secretInfo, (byte) 0);
        // 生成随机序列
        Integer[] sequence = new Integer[(int)((secretLength+4)*1.2)-4];
        for(int i=0; i<sequence.length; i++){
            sequence[i] = random.nextInt(0, originImageData.length/8);
        }
        // 去除重复
        LinkedHashSet<Integer> set = new LinkedHashSet<>(Arrays.asList(sequence));
        sequence = set.toArray(new Integer[0]);
        Integer[] sequence1 = new Integer[sequence.length + 4];
        System.arraycopy(lengthIndex, 0, sequence1, 0, 4);
        System.arraycopy(sequence, 0, sequence1, 4, sequence.length);
        System.out.println("解密随机序列为：");
        System.out.println(Arrays.toString(sequence1));
        // 获取加密信息
        for(int i=0;i<secretLength;i++){
            byte info = 0;
            for(int j=0; j<8; j++){
                int temp = originImageData[(sequence[i])*8+j] ^ encryptImageData[(sequence[i])*8+j];
                info |= (temp) << j;
            }
            secretInfo[i] = info;
        }
        secretInfo = Arrays.copyOfRange(secretInfo, 0, secretLength);
        return secretInfo;
    }

}
