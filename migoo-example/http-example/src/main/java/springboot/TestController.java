package springboot;

import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;
import xyz.migoo.framework.common.pojo.Result;

import java.util.Map;

@RestController("user")
public class TestController {


    private final Map<String, Map<String, Object>> users = Maps.newHashMap();

    @GetMapping
    public Result<?> getUsers() {
        return Result.getSuccessful(users.values());
    }

    @GetMapping("{id}")
    public Result<?> getUser(@PathVariable("id") String id) {
        return Result.getSuccessful(users.get(id));
    }

    @PostMapping
    public Result<?> addUser(@RequestBody Map<String, Object> user) {
        return Result.getSuccessful(users.put(user.get("id").toString(), user));
    }

    @PutMapping
    public Result<?> updateUser(@RequestBody Map<String, Object> user) {
        return Result.getSuccessful(users.put(user.get("id").toString(), user));
    }


    @PostConstruct
    public void dd() {
        for (int i = 0; i < 10; i++) {
            users.put("" + i, Maps.newHashMap(Map.of("id", i, "name", "name_" + i, "age", 18)));
        }
    }
}
