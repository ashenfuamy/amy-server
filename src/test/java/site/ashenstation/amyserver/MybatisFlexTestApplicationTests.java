package site.ashenstation.amyserver;

import com.mybatisflex.core.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.ashenstation.amyserver.entity.MdaTagPo;
import site.ashenstation.amyserver.entity.TestPo;
import site.ashenstation.amyserver.entity.table.MdaTagPoTableDef;
import site.ashenstation.amyserver.entity.table.TestPoTableDef;
import site.ashenstation.amyserver.mapper.MdaTagMapper;
import site.ashenstation.amyserver.mapper.TestMapper;

@SpringBootTest
public class MybatisFlexTestApplicationTests {

    @Autowired
    private TestMapper testMapper;
    @Test
    void contextLoads() {
        QueryWrapper where = QueryWrapper.create()
                .select()
                .where(TestPoTableDef.TEST_PO.TEST_BOOLEAN.eq(true));

        TestPo testPo = testMapper.selectOneByQuery(where);

        System.out.println(testPo);
    }

    @Test
    void customerIdKeyGenerator() {
        TestPo testPo = new TestPo();

        testPo.setTestInt(185521);
        testPo.setTestBoolean(false);

        testMapper.insert(testPo);

        System.out.println(testPo);
    }
}
