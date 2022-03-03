package util;

import api.ApiObjectModel;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 接口对象加载器（加载配置文件中的接口对象）
 */
@Slf4j
public class ApiLoader {

    // 加载所有的接口对象（到内存中）
    private static List<ApiObjectModel> apiObjectModels = new ArrayList<>();

    // 遍历dir目录下的所有接口对象（文件名）
    public static void load(String dir) {
        Arrays.stream(new File(dir).listFiles()).forEach(path -> {
            try {
                apiObjectModels.add(ApiObjectModel.load(path.getAbsolutePath()));
                log.info(String.format("接口对象加载成功【%s】", path));
            } catch (IOException e) {
                e.printStackTrace();
                log.error(String.format("接口对象加载失败【%s】", path));
            }
        });
    }
}
