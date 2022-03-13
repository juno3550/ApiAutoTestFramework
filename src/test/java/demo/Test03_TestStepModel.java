package demo;

import org.junit.jupiter.api.Test;
import testcase.assertion.AssertModel;
import testcase.step.TestStepModel;
import util.ApiLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Test03_TestStepModel {

    @Test
    public void testRun() throws IOException {
        ApiLoader.load("src/main/resources/api/object");
        // 实参
        ArrayList<String> actualParameter =new ArrayList<>();
        actualParameter.add("ww5ef451bf006ec894");
        actualParameter.add("EcEIog2OJ8AtO7PDaqt_yuVZS3NeYF3kcko9Vd6i9EE");
        // 断言
        ArrayList<AssertModel> asserts = new ArrayList<>();
        AssertModel assertModel = new AssertModel();
        assertModel.setKey("errcode");
        assertModel.setReason("getToken错误码校验01！");
        assertModel.setMatcher("equalTo");
        assertModel.setExpect("0");
        asserts.add(assertModel);

        // save
        HashMap<String,String> save = new HashMap<>();
        save.put("accesstoken", "access_token");

        // globalsave
        HashMap<String,String> saveGlobal = new HashMap<>();
        saveGlobal.put("accesstoken", "access_token");

        TestStepModel stepModel = new TestStepModel();
        stepModel.setApiName("getToken");
        stepModel.setActualParameter(actualParameter);
        stepModel.setAsserts(asserts);
        stepModel.setSave(save);
        stepModel.setSaveGlobal(saveGlobal);
        stepModel.run(null);
    }

}
