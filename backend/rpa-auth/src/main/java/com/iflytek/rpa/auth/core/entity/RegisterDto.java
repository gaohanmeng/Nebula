package com.iflytek.rpa.auth.core.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author mjren
 * @date 2025-03-24 11:28
 * @copyright Copyright (c) 2025 mjren
 */
@Data
@Builder
public class RegisterDto {

    private String captcha;

    private String loginName;

    private String phone;

    private String password;

    private String confirmPassword;

    /**
     * 用户选择的组织名（多组织支持）。
     * 为空时使用服务端默认值。
     */
    private String tenantId;
}
