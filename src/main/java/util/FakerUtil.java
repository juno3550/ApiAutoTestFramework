/**
 * projectName: autobots
 * fileName: FakerUtils.java
 * packageName: com.autobots.utils.faker
 * date: 2019-11-27 下午4:17
 */
package util;

import java.util.Random;

/**
 * 数据伪造工具类
 **/
public class FakerUtil {
    private final static int delta = 0x9fa5 - 0x4e00 + 1;

    /**
     * @param: [lengh] 获取的随机数的长度
     * @return: int 随机数
     */
    public static int getRandomInt(int lengh) {

        lengh = lengh - 1;
        int randomint = (int) ((Math.random() * 9 + 1) * Math.pow(10, (double) lengh));
        return randomint;
    }

    /**
     * @param min 获取的随机数左边界
     * @param max 获取的随机数右边界
     * @methodsName: getRandomInt
     * @return: int 随机数
     */
    public static int getRandomInt(int min, int max) {

        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;

    }
    /**
     * @methodsName: getTimeStamp
     * @param:  []
     * @return: java.lang.String
     */
    public static String getTimeStamp(){
        return  String.valueOf(System.currentTimeMillis());

    }
    /**
     * @methodsName: getNum
     * @param: [start, end]
     * @return: int
     */
    public static int getNum(
            int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    /**
     * @methodsName: orderNo
     * @description:14位订单号生成器
     * @param: []
     * @return: java.lang.String
     */
    public static String orderNo() {
        String cardNo = "123456";
        for (int i = 0; i < 8; i++) {
            cardNo += getNum(0, 9);
        }
        return cardNo;
    }

    /**
     * 电话号码生成器
     */
    private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,188,185,181".split(",");

    /**
     * @methodsName: getTel
     * @description:
     * @return: java.lang.String
     */
    public static String getTel() {
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String thrid = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + thrid;
    }

    /**
     * @methodsName: getRandomHan
     * @description: 获取随机汉字
     * @return: char
     */
    public static char getRandomHan(int num) {
        Random ran = new Random();
        return (char) (0x4e00 + ran.nextInt(delta));
    }


}