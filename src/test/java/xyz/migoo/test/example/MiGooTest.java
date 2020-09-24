package xyz.migoo.test.example;

import org.junit.jupiter.api.Test;
import xyz.migoo.Migoo;

/**
 * @author xiaomi
 * @date 2018/7/25 15:10
 */
public class MiGooTest {

    public Migoo runner = new Migoo();

    @Test
    public void testDefault(){
        runner.run("./case/migoo-default.yml");
    }
    @Test
    public void testFileImport(){
        runner.run("./case/migoo-file_import.yml");
    }
}
