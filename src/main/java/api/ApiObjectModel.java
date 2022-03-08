package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import util.GlobalVariables;
import util.PlaceholderUtil;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

/**
 * 接口对象
 */
@Data
@Slf4j
public class ApiObjectModel {

    // 属性名要与接口对象文件中的字段名保持一致
    private String name;  // 接口名称
    private String url;  // 默认请求地址
    private String post;  // post 请求地址
    private String get;  // get 请求地址
    private String requestMethod = "get";  // 请求方法
    private String contentType;  // 请求参数格式
    private String body;  // post 请求体
    private HashMap<String, String> query;  // get 请求参数
    private HashMap<String, String> headers;  // 请求头
    private ArrayList<String> formalParam;  // 形参列表
    private HashMap<String,String> caseVariables = new HashMap<>();  // 存储用例级别的参数化
    private Response response;  // 响应对象

    // 加载接口文件中的接口对象
    public static ApiObjectModel load(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        // 返回序列化后的对象
        return objectMapper.readValue(new File(path), ApiObjectModel.class);
    }

    // 获取响应对象
    public Response run(ArrayList<String> actualParams) {
        // 初始化请求地址和请求参数
        String runUrl = this.url;
        HashMap<String, String> runQuery = new HashMap<>();
        String runBody = this.body;
        // 1. 确定请求方法和请求URL
        if(post!=null) {
            runUrl = this.post;
            requestMethod = "post";
        } else if (get!=null) {
            runUrl = this.get;
            requestMethod = "get";
        }
        // 2. 全局变量参数化（请求参数、URL等）
        runUrl = PlaceholderUtil.resolveString(runUrl, GlobalVariables.getGlobalVariables());
        if (query!=null) {
            runQuery.putAll(PlaceholderUtil.resolveMap(query, GlobalVariables.getGlobalVariables()));
        }
        if (body!=null) {
            runBody = PlaceholderUtil.resolveString(runBody, GlobalVariables.getGlobalVariables());
        }
        // 3. 用例级别的参数化
        if (formalParam!=null && actualParams!=null && formalParam.size()>0 && actualParams.size()>0) {
            // 将形参与实参对应成键值对，存入caseVariables
            for (int i = 0; i< formalParam.size(); i++) {
                caseVariables.put(formalParam.get(i), actualParams.get(i));
            }
            // 参数化
            runUrl = PlaceholderUtil.resolveString(runUrl, caseVariables);
            if (query!=null) {
                runQuery.putAll(PlaceholderUtil.resolveMap(query, caseVariables));
            }
            if (body!=null) {
                runBody = PlaceholderUtil.resolveString(runBody, caseVariables);
            }
        }
        // 4. 使用上述变量替换后的请求数据，进行接口请求并返回响应对象
        RequestSpecification requestSpecification = given().log().all();  // RequestSpecification：请求体封装
        if (contentType!=null) {
            requestSpecification.contentType(contentType);
        }
        if (headers!=null) {
            requestSpecification.headers(headers);
        }
        if (formalParam !=null && formalParam.size()>0) {
            requestSpecification.formParams(runQuery);
        }
        if (body!=null) {
            requestSpecification.body(runBody);
        }
        response = requestSpecification.request(requestMethod, runUrl).
                then().log().all().extract().response();

        return response;
    }

}
