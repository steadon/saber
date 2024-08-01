package com.steadon.saber.util;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Data
@Component
public class AccessTokenUtils {

    private static final Random random = new Random();

    public static String getAccessToken(String appId) {
        String accessToken = DigestUtils.sha1Hex(UUID.randomUUID().toString());
        RedisUtils.setKey(accessToken, appId, Duration.ofDays(10));
        return accessToken;
    }

    /**
     * 根据appId+uuid使用sha1加密生成appSecret
     *
     * @param appId app_id
     * @return appSecret
     */
    public static String getAppSecret(String appId) {
        String uuid = UUID.randomUUID().toString();
        return DigestUtils.sha1Hex(appId + uuid);
    }

    /**
     * 校验accessToken
     *
     * @param accessToken accessToken
     * @return Optional<String> appId
     */
    public static Optional<String> checkAccessToken(String accessToken) {
        return RedisUtils.getKey(accessToken, String.class);
    }
}
