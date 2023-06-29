public class Until {
    public static void printData(int width, int height, byte[] imageData){
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                System.out.print(imageData[i*width+j]);
            }
            System.out.println();
        }
    }

    //打印byte数组的内容
    public static void printByte(byte[] ch){
        for (byte b : ch) {
            for (int i = 0; i < 8; i++) {
                int bitValue = (b >> i) & 1;
                System.out.print(bitValue);
            }
        }
    }
    // bytes数组转化为字符串
    public static String byteToString(byte[] ch) {
        StringBuilder sb = new StringBuilder();
        for (byte b : ch) {
            char c = (char) b;
            sb.append(c);
        }
        return sb.toString();
    }

    public static byte[] intToBytes(int value){
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) (value >> 24);
        byteArray[1] = (byte) (value >> 16);
        byteArray[2] = (byte) (value >> 8);
        byteArray[3] = (byte) value;
        return byteArray;
    }

    public static int bytesToInt(byte[] byteArray){
        int value = (byteArray[0] << 24) |
                ((byteArray[1] & 0xFF) << 16) |
                ((byteArray[2] & 0xFF) << 8) |
                (byteArray[3] & 0xFF);
        return value;
    }
    public static Integer[] bytesToInts(byte[] byteArray){
        Integer[] result = new Integer[byteArray.length];
        for(int i=0;i<byteArray.length;i++){
            result[i] = (int)byteArray[i];
        }
        return result;
    }
}
