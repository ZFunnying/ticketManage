package com.cmit.kapok.constants;

public class RedisPrefixConstants {
    
    public static final String PROJECT_UNIQUE_NAME_PREFIX = "kapok_admin";

    /**
     *  用户jwt
     */
    public static final String SYSTEM_USER_TOKEN_PREFIX = PROJECT_UNIQUE_NAME_PREFIX + "_admin_token:";
    /**
     *
     */
    public static final String TOKEN_REFLASH_PREFIX = PROJECT_UNIQUE_NAME_PREFIX + "_token_reflash:";

    /**
     * @Author lizhitao
     * @Description session锁
     **/
    public static final String SESSION_LOCK_PREFIX = PROJECT_UNIQUE_NAME_PREFIX + "_session_lock_prefix:";

}
