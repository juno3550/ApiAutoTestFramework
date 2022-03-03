package testcase.step;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@Slf4j
public class TestStepResult {

    private ArrayList<Executable> assertList;  // 存储一个step中的断言表达式（可能有多个）
    private HashMap<String, String> stepVariables = new HashMap<>();  // 存储用例级别的变量（测试步骤间的变量传递）
}
