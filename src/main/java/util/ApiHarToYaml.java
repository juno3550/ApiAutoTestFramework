package util;

import api.ApiObjectModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarHeader;
import de.sstoehr.harreader.model.HarRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class ApiHarToYaml {

    private static ArrayList<ApiObjectModel> apis = new ArrayList<>();
    private static String apiObjectName;

    public static ArrayList<ApiObjectModel> harToApiObjectYaml(String harPath, String yamlGeneratedPath) {
        HarReader harReader = new HarReader();
        // 遍历har文件
        Arrays.stream(new File(harPath).listFiles()).forEach(filePath -> {
            try {
                // 读取har文件
                Har har = harReader.readFromFile(filePath);
                // 初始化相关API对象
                ApiObjectModel apiObjectModel = new ApiObjectModel();
                HashMap<String, String> query = new HashMap<>();
                HashMap<String, String> body = new HashMap<>();
                HashMap<String, String> headers = new HashMap<>();
                // 遍历har文件中的entries数组（其元素为接口信息，即接口动作Action对象）
                har.getLog().getEntries().forEach(entrie -> {
                    // 获取请求接口
                    HarRequest harRequest = entrie.getRequest();
                    // 获取请求方法和URL
                    String method = harRequest.getMethod().toString();
                    String url = harRequest.getUrl();
                    apiObjectName = getRequestName(url);
                    List<HarHeader> harHeaders = harRequest.getHeaders();
                    // 封装接口动作对象（Action）
                    if(method.equalsIgnoreCase("get")) {
                        apiObjectModel.setGet(url);
                        // 获取请求参数
                        harRequest.getQueryString().forEach(queryMap -> {
                            query.put(queryMap.getName(), queryMap.getValue());
                            apiObjectModel.setQuery(query);
                        });
                    } else {
                        apiObjectModel.setPost(url);
                        apiObjectModel.setHeaders(headers);
                        String bodyData = harRequest.getPostData().toString();
                        apiObjectModel.setBody(bodyData);
                    }
                });
                // 封装接口对象
                apiObjectModel.setName(apiObjectName);  // 自定义接口对象名称
                ObjectMapper mapper =new ObjectMapper(new YAMLFactory());
                String generatedFileName = yamlGeneratedPath+"/"+apiObjectName+".yaml";
                mapper.writeValue(new File(generatedFileName), apiObjectModel);
                apis.add(apiObjectModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return apis;
    }

    // 获取URL地址中的接口动作名称
    // 如"https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=..."的"gettoken"
    public static String getRequestName(String url) {
        String[] subUrl = url.split("\\u003F")[0].split("/");
        return subUrl[subUrl.length-1];  // 取最后一个元素
    }

}
