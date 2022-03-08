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
            if(path.isFile()) {
                try {
                    apiObjectModels.add(ApiObjectModel.load(path.getAbsolutePath()));
                    log.info(String.format("接口对象文件读取成功【%s】", path));
                    log.info(String.format("接口对象【%s】加载成功【%s】", apiObjectModels.get(apiObjectModels.size()-1).getName(), apiObjectModels.get(apiObjectModels.size()-1)));
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error(String.format("接口对象文件读取失败【%s】", path));
                    log.error(String.format("接口对象加载失败【%s】", apiObjectModels.get(apiObjectModels.size()-1).getName()));
                }
            } else {
                ApiLoader.load(path.getAbsolutePath());
            }
        });
    }

    // 获取接口对象
    public static ApiObjectModel getApiObject(String apiName){
        final ApiObjectModel[] finalApiObjectModel = {new ApiObjectModel()};
        apiObjectModels.forEach(apiObjectModel -> {
            if (apiObjectModel.getName().equals(apiName)) {
                finalApiObjectModel[0] = apiObjectModel;
            }
        });
        if (finalApiObjectModel[0]!=null) {
            return finalApiObjectModel[0];
        } else {
            log.error("没有找到接口对象【"+apiName+"】");
            return null;
        }
    }
}
