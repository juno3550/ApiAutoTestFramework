package testcase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.function.Executable;
import testcase.step.TestStepModel;
import testcase.step.TestStepResult;
import util.FakerUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * 测试用例对象
 */
@Data
@Slf4j
public class TestCaseModel {

    private String name;  // 用例名称
    private String description;  // 用例描述
    private String module;  // 用例模块
    private ArrayList<TestStepModel> steps;  // 用例中的所有测试步骤
    private ArrayList<Executable> assertList = new ArrayList<>();  // 用例中所有测试步骤的断言表达式
    private HashMap<String,String> testCaseVariables =  new HashMap<>();  // 用例中步骤间的变量传递

    // 加载测试用例文件中的测试用例对象
    public static TestCaseModel load(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        // 反序列化
        return objectMapper.readValue(new File(path), TestCaseModel.class);
    }

    // 执行测试用例
    public void run() {
        // 替换保留关键字（占位符）变量
        this.testCaseVariables.put("getTimeStamp", FakerUtil.getTimeStamp());

        // 遍历执行step
        steps.forEach(step -> {
            TestStepResult stepResult = step.run(testCaseVariables);
            // 追加save（用例级别）变量
            if (stepResult.getStepVariables().size()>0) {
                testCaseVariables.putAll(stepResult.getStepVariables());
            }
            // 追加step中的断言表达式
            if(stepResult.getAssertList().size()>0){
                assertList.addAll(stepResult.getAssertList());
            }
        });

        // 将一个测试用例对象中的所有测试步骤，进行统一（软）断言
        assertAll("", assertList.stream());
    }

}
