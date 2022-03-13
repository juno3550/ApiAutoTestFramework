package demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testcase.TestCaseModel;
import util.ApiLoader;

import java.io.IOException;

public class Test04_TestCaseModel {

    @BeforeAll
    static void loadApi(){
        ApiLoader.load("src/main/resources/api/object");
    }

    @Test
    void runTest() throws IOException {
        TestCaseModel apiTestCaseModel = TestCaseModel.load("src/test/resources/testcase/departmentCrud.yaml");
        apiTestCaseModel.run();
    }

}
