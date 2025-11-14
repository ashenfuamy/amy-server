package site.ashenstation.amyserver;

import com.mybatisflex.core.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.ashenstation.amyserver.entity.TestPo;
import site.ashenstation.amyserver.entity.UserPo;
import site.ashenstation.amyserver.entity.table.TestPoTableDef;
import site.ashenstation.amyserver.mapper.TestMapper;
import site.ashenstation.amyserver.mapper.UserMapper;

import java.util.List;

@SpringBootTest
public class MybatisFlexTestApplicationTests {

    @Autowired
    private TestMapper testMapper;
    @Autowired
    private UserMapper userMapper;

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

    @Test
    void customerQueryGenerator() {
        List<UserPo> userPos = userMapper.selectAll();

        System.out.println(userPos);
    }
}
