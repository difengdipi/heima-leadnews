package com.heima.utils.Thread;

import com.heima.model.user.pojos.ApUser;

/**
 * @author: 周海
 * @Create : 2024/4/8
 **/
public class AppThreadLocalUtil {
    private final static ThreadLocal<ApUser> App_USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 存入线程中
     *
     * @param apUser
     */
    public static void setUser(ApUser apUser) {
        App_USER_THREAD_LOCAL.set(apUser);
    }

    /**
     * 从线程中获取数据
     *
     * @return
     */
    public static ApUser getUser() {
        return App_USER_THREAD_LOCAL.get();
    }

    /**
     * 清理线程中的数据
     */
    public static void clear() {
        App_USER_THREAD_LOCAL.remove();
    }
}
