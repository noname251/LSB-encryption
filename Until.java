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
}
