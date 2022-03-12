package util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 配置文件中的占位符替换工具类
 * Date: 15-5-8
 * Time: 下午4:12
 */
@Slf4j
public class PlaceholderUtil {

    /**
     * Prefix for system property placeholders: "${"
     */
    public static final String PLACEHOLDER_PREFIX = "${";
    /**
     * Suffix for system property placeholders: "}"
     */
    public static final String PLACEHOLDER_SUFFIX = "}";


    public static String resolveString(String text, Map<String, String> parameter) {
        if (parameter == null || parameter.isEmpty()||text == null || text.isEmpty()) {
            return text;
        }
        StringBuffer buf = new StringBuffer(text);
        /**
         * 计算变量名开始位置
         **/
        int startIndex = buf.indexOf(PLACEHOLDER_PREFIX);
        while (startIndex != -1) {
            /**
            * 计算变量名结束的位置
            **/
            int endIndex = buf.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
            if (endIndex != -1) {
                /**
                 * 取出要替换的变量名
                 **/
                String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
                int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();
                try {
                    /**
                     * 取出变量map中的真实值
                     **/
                    String propVal = parameter.get(placeholder);
                    if (propVal != null) {
                        /**
                         * 替换变量
                         **/
                        buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propVal);
                        nextIndex = startIndex + propVal.length();
                    } else {
                        log.info("Could not resolve placeholder '" + placeholder + "' in [" + text + "] ");
                    }
                } catch (Exception ex) {
                    log.info("Could not resolve placeholder '" + placeholder + "' in [" + text + "]: " + ex);
                }
                startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
            } else {
                startIndex = -1;
            }
        }
        return buf.toString();
    }

    public static ArrayList<String> resolveList(ArrayList<String> list, Map<String, String> parameter) {
        if (parameter == null || parameter.isEmpty() || list == null || list.isEmpty()) {
            return list;
        }
        ArrayList<String> retureList = new ArrayList<String>();
            list.forEach(str -> {
                if (str.contains(PLACEHOLDER_PREFIX)) {
                    retureList.add(resolveString(str, parameter));
                } else {
                    retureList.add(str);
                }
            });
        return retureList;
    }

    public static HashMap<String, String> resolveMap(HashMap<String, String> map, Map<String, String> parameter) {
        if (parameter == null || parameter.isEmpty() || map == null || map.isEmpty()) {
            return map;
        }
        HashMap<String, String> retureMap = new HashMap<String, String>();
            map.forEach((key, value) -> {
                if (value.contains(PLACEHOLDER_PREFIX)) {
                    retureMap.put(key, resolveString(value, parameter));

                }
            });
        return retureMap;
    }
}