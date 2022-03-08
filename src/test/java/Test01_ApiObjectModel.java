import api.ApiObjectModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class Test01_ApiObjectModel {

    @Test
    public void testRun() throws IOException {
        // 实参
        ArrayList<String> actualParameter = new ArrayList<>();
        actualParameter.add("ww5ef451bf006ec894");
        actualParameter.add("EcEIog2OJ8AtO7PDaqt_yuVZS3NeYF3kcko9Vd6i9EE");
        // 取出接口对象中的Action（接口动作对象）进行接口请求（执行用例）
        ApiObjectModel.load("src/main/resources/api/object/token/getToken.yaml").run(actualParameter);
    }
}
