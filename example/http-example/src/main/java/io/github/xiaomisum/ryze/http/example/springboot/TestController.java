package io.github.xiaomisum.ryze.http.example.springboot;

import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;
import xyz.migoo.framework.common.pojo.Result;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class TestController {


    private final Map<String, Map<String, Object>> users = Maps.newHashMap();

    @GetMapping
    public Result<?> getUsers() {
        return Result.getSuccessful(users.values());
    }

    @GetMapping("/{id}")
    public Result<?> getUser(@PathVariable("id") String id) {
        return Result.getSuccessful(users.get(id));
    }

    @PostMapping
    public Result<?> addUser(@RequestBody Map<String, Object> user) {
        users.put(user.get("id").toString(), user);
        return Result.getSuccessful(user);
    }

    @PutMapping
    public Result<?> updateUser(@RequestBody Map<String, Object> user) {
        users.put(user.get("id").toString(), user);
        return Result.getSuccessful(user);
    }


    @PostConstruct
    public void init() {
        for (int i = 0; i < 10; i++) {
            users.put("" + i, Maps.newHashMap(Map.of("id", i, "name", "name_" + i, "age", 18)));
        }
    }
}
