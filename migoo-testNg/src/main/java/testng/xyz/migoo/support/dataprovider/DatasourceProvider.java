package testng.xyz.migoo.support.dataprovider;

import org.testng.IDataProviderListener;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;
import org.testng.util.Strings;
import testng.xyz.migoo.support.annotation.AnnotationUtils;

import java.util.Map;
import java.util.Objects;


public class DatasourceProvider implements IDataProviderListener {

    public static final String DATASOURCE_PROVIDER = "__migoo_testng_data_provider__";

    private static final Object[][] EMPTY_DATA = new Object[][]{};

    @DataProvider(name = DATASOURCE_PROVIDER)
    public static Object[][] datasource(ITestNGMethod method) {
        var datasource = AnnotationUtils.getDatasource(method.getConstructorOrMethod().getMethod());
        if (Objects.isNull(datasource) || Strings.isNullOrEmpty(datasource.value())) {
            return EMPTY_DATA;
        }
        // todo 这里要实现数据文件解析
        return null;
    }

    public static void main(String[] args) {
        var objs = new Object[][]{{Map.of("username", "1")}, {Map.of("username", "2")}, {Map.of("username", "3")}};
        for (Object[] obj : objs) {
            for (Object o : obj) {
                System.out.print(o + "  ");
            }
            System.out.println();
        }
    }
}
