package site.ashenstation;

import com.mybatisflex.core.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import site.ashenstation.entity.Actor;
import site.ashenstation.entity.ActorTag;

import java.util.List;

@SpringBootTest
public class TestOne {

    @Test
    void contextLoads() {
        QueryWrapper.create()
                .select();
    }

    static class ActorTagWithActor extends ActorTag {
        private List<Actor> actors;

    }
}
