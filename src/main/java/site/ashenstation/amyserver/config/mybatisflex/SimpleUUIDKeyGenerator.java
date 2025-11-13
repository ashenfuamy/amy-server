package site.ashenstation.amyserver.config.mybatisflex;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.keygen.IKeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class SimpleUUIDKeyGenerator implements IKeyGenerator {
    @Override
    public Object generate(Object o, String s) {
        return IdUtil.fastSimpleUUID();
    }
}
