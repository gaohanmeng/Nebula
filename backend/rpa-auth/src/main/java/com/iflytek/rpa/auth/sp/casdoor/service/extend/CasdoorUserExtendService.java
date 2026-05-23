package com.iflytek.rpa.auth.sp.casdoor.service.extend;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.casbin.casdoor.config.Config;
import org.casbin.casdoor.entity.Organization;
import org.casbin.casdoor.entity.User;
import org.casbin.casdoor.service.OrganizationService;
import org.casbin.casdoor.service.UserService;
import org.casbin.casdoor.util.Map;
import org.casbin.casdoor.util.http.CasdoorResponse;
import org.casbin.casdoor.util.http.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * @desc: 基于casdoor原生服务的用户拓展服务，仅在casdoor profile下生效
 * @author: weilai <laiwei3@iflytek.com>
 * @create: 2025/12/11 10:17
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "rpa.auth.deployment-mode", havingValue = "casdoor", matchIfMissing = true)
public class CasdoorUserExtendService extends UserService {

    public CasdoorUserExtendService(Config config) {
        super(config);
    }

    public User getUserById(String id) throws IOException {
        CasdoorResponse<User, Object> resp =
                doGet("get-user", Map.of("id", id), new TypeReference<CasdoorResponse<User, Object>>() {});
        return objectMapper.convertValue(resp.getData(), User.class);
    }

    public List<User> getUsers(String organizationName) throws IOException {
        CasdoorResponse<List<User>, Object> resp = doGet(
                "get-users",
                Map.of("owner", organizationName),
                new TypeReference<CasdoorResponse<List<User>, Object>>() {});
        return resp.getData();
    }

    public User getUserByPhone(String phone) throws IOException {
        CasdoorResponse<User, Object> resp =
                doGet("get-user", Map.of("phone", phone), new TypeReference<CasdoorResponse<User, Object>>() {});
        return objectMapper.convertValue(resp.getData(), User.class);
    }

    /**
     * 跨所有组织查找同名用户列表。
     */
    private List<User> getUsersByNameAcrossOrgs(String name) throws IOException {
        List<User> result = new ArrayList<>();
        try {
            OrganizationService orgService = new OrganizationService(config);
            List<Organization> orgs = orgService.getOrganizations();
            for (Organization org : orgs) {
                try {
                    CasdoorResponse<User, Object> resp = doGet("get-user",
                            Map.of("id", org.name + "/" + name),
                            new TypeReference<CasdoorResponse<User, Object>>() {});
                    User user = objectMapper.convertValue(resp.getData(), User.class);
                    if (user != null && user.name != null) {
                        result.add(user);
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            // 无法获取组织列表时返回空，交由调用方降级处理
        }
        return result;
    }

    /**
     * 跨组织查找用户，通过密码验证消解重名冲突。
     * 若多个组织存在同名用户，用密码匹配到唯一用户。
     */
    public User findUserByNameAndPassword(String name, String password) throws IOException {
        List<User> candidates = getUsersByNameAcrossOrgs(name);
        User matched = null;
        for (User u : candidates) {
            log.info("Casdoor 预验证失败：测试3，用户名：{}",u);
            u.password = password;
            if (checkUserPassword(u)) {
                if (matched != null) {
                    return null; // 两个不同组织的同名用户密码也相同，无法区分
                }
                matched = u;
            }
        }
        return matched;
    }

    /**
     * 跨所有组织按用户名查找用户（不需要密码），返回第一个匹配。
     */
    public User getUserAcrossOrgs(String name) throws IOException {
        List<User> users = getUsersByNameAcrossOrgs(name);
        return users.isEmpty() ? null : users.get(0);
    }

    /**
     * 全局检查用户名是否已被注册（跨所有组织）
     */
    public boolean isUserNameExistsGlobally(String name) throws IOException {
        return !getUsersByNameAcrossOrgs(name).isEmpty();
    }

    /**
     * 检查用户密码是否正确
     * @param user 用户信息（包含用户名和密码）
     * @return true 如果密码正确，false 如果密码错误
     * @throws IOException 如果发生IO异常
     */
    public boolean checkUserPassword(User user) throws IOException {
        String payload = objectMapper.writeValueAsString(user);

        // 直接调用底层HTTP方法，避免doPost在status != "ok"时抛出异常
        log.info("Casdoor 预验证失败：测试5，用户名：{}，{}", payload,config.endpoint);
        String url = String.format("%s/api/check-user-password", config.endpoint);
        String response = HttpClient.postString(url, payload, credential);
        log.info("Casdoor 预验证失败：测试5，用户名：{}，{}", payload,credential);

        // 手动解析响应
        CasdoorResponse<User, Boolean> resp =
                objectMapper.readValue(response, new TypeReference<CasdoorResponse<User, Boolean>>() {});
        log.info("Casdoor 预验证失败：测试6，用户名：{}", resp);
        // 根据status判断密码是否正确
        if ("ok".equals(resp.getStatus())) {
            return true;
        } else {
            return false;
        }
    }
}
