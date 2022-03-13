package demo;

import api.ApiObjectModel;
import org.junit.jupiter.api.Test;
import util.ApiHarToYaml;

import java.io.IOException;
import java.util.ArrayList;

public class Test05_ApiHarToYaml {

    @Test
    public void testHarToApiObjectYaml() throws IOException {
        ArrayList<ApiObjectModel> apis = ApiHarToYaml.harToApiObjectYaml(
                "src/main/resources/api/har", "src/main/resources/api/object/har_generated");
        apis.forEach(apiObjectModel -> {
            apiObjectModel.run(null);
        });
    }
}
