package testcase.assertion;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class AssertModel {

    private String key;  // 断言字段
    private String matcher;  // 断言方式（等于、大于等）
    private String expect;  // 期望值
    private String reason;  // 断言失败原因

}

