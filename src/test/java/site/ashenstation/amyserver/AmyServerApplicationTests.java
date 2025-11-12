package site.ashenstation.amyserver;

import com.mybatisflex.core.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.ashenstation.amyserver.entity.MdaTagPo;
import site.ashenstation.amyserver.entity.table.MdaTagPoTableDef;
import site.ashenstation.amyserver.mapper.MdaTagMapper;

@SpringBootTest
class AmyServerApplicationTests {

    @Autowired
    private MdaTagMapper mdaTagMapper;

    @Test
    void contextLoads() {
        QueryWrapper where = QueryWrapper.create()
                .select()
                .where(MdaTagPoTableDef.MDA_TAG_PO.ID.eq("100224"));

        MdaTagPo mdaTagPo = mdaTagMapper.selectOneByQuery(where);

        System.out.println(mdaTagPo);
    }

}
