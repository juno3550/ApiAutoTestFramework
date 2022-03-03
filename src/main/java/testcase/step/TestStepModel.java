package testcase.step;

import io.restassured.response.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.function.Executable;
import testcase.assertion.AssertModel;
import util.PlaceholderUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * 测试用例中的测试步骤对象
 */
@Data
@Slf4j
public class TestStepModel {

    private String api;  // 接口名称
    private ArrayList<String> actualParameter;  // 实参
    private HashMap<String, String> saveGlobal;  // 需要存储的全局变量（测试用例中的占位符变量）
    private HashMap<String, String> save;  // 需要存储的用例级别变量（测试用例中的占位符变量）
    private ArrayList<AssertModel> asserts;  // 存储多组断言
    private ArrayList<String> finalActualParameter =  new ArrayList<>();  // 参数化后的实参列表
    private HashMap<String, String> stepVariable = new HashMap<>();  // 用例级别的变量提取列表
    private ArrayList<Executable> assertList = new ArrayList<>();  // 存储多组断言
    private TestStepResult stepResult = new TestStepResult();  // 测试步骤执行结果

    // 获取测试步骤执行结果
    public TestStepResult run(HashMap<String, String> testCaseVariables) {

        // 实参的占位符变量替换
        if (actualParameter!=null) {
            finalActualParameter.addAll(PlaceholderUtil.resolveList(actualParameter, testCaseVariables));
        }
        // 执行action
        Response response = ApiLoader.getAction(api, action).run(finalActualParameter);
        // 存储save（用例级别变量）
        if (save!=null) {
            save.forEach((variableName, path) -> {
                String variableValue = response.path(path).toString();
                stepVariable.put(variableName, variableValue);
                logger.info("step变量更新：【"+variableName+": "+variableValue+"】");
            });
        }
        // 存储saveGlobal（全局变量）
        if (saveGlobal!=null) {
            saveGlobal.forEach((variableName, path) -> {
                String variableValue = response.path(path);
                GlobalVariables.getGlobalVariables().put(variableName, variableValue);
                logger.info("全局变量更新：【"+variableName+": "+variableValue+"】");
            });
        }
        // 存储断言表达式
        if(asserts != null){
            // 遍历每组断言
            asserts.stream().forEach(assertModel -> {
                assertList.add(() -> {
                    assertThat(assertModel.getReason(),
                            response.path(assertModel.getActual()).toString(),
                            equalTo(assertModel.getExpect())
                    );
                });
            });
        }
        // 组装StepResult
        stepResult.setAssertList(assertList);
        stepResult.setStepVariables(stepVariable);
        return stepResult;
    }

}
