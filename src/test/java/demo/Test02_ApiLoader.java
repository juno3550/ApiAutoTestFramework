package demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.ApiLoader;

import java.util.ArrayList;

public class Test02_ApiLoader {

    /**
     * 加载所有API对象到内存中
     */
    @BeforeAll
    public static void loadApiObject() {

    }

    @Test
    public void testLoad() {
        ApiLoader.load("src/main/resources/api/object");
        System.out.println("before load done ~");
    }
}
