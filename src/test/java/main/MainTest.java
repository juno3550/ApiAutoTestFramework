package main;

import io.qameta.allure.Allure;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testcase.TestCaseModel;
import util.ApiLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
public class MainTest {

    @org.junit.jupiter.params.ParameterizedTest(name="{1}")  // junit5和allure的用例名称：入参name
    @MethodSource  // 默认引用同名方法的返回值作为入参
    @Severity(SeverityLevel.CRITICAL)
    void apiTest(TestCaseModel testCaseModel, String name) {
        // 初始化测试步骤说明
        final int[] stepNo = {1};
        testCaseModel.getSteps().forEach(testStepModel -> {
            Allure.parameter("测试步骤"+ stepNo[0], testStepModel.getDescription());
            stepNo[0]++;
        });
        // 动态加载用例信息
        log.info(String.format("开始执行用例【%s】", name));
        Allure.step("用例执行");
        log.info("用例描述："+testCaseModel.getDescription());
        Allure.description(testCaseModel.getDescription());
        log.info("用例所属模块："+testCaseModel.getModule());
        Allure.feature(testCaseModel.getModule());
        testCaseModel.run();
    }

    // 数据源
    static List<Arguments> apiTest() {
        // 加载接口对象
        ApiLoader.load("src/main/resources/api/object");
        // 加载测试用例对象
        String testCasePath = "src/test/resources/testcase";
        List<Arguments> testcase = new ArrayList<>();
        Arrays.stream(new File(testCasePath).listFiles()).forEach(testCaseDir -> {
            if(testCaseDir.isFile()) {
                try {
                    TestCaseModel testCaseModel = TestCaseModel.load(testCaseDir.getAbsolutePath());
                    testcase.add(arguments(testCaseModel, testCaseModel.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Arrays.stream(testCaseDir.listFiles()).forEach(nextTestCaseDir -> {
                    try {
                        TestCaseModel testCaseModel = TestCaseModel.load(nextTestCaseDir.getAbsolutePath());
                        testcase.add(arguments(testCaseModel, testCaseModel.getName()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        return testcase;
    }

}
