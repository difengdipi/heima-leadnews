package com.heima.utils.Thread;

import com.heima.model.wemedia.pojos.WmUser;

/**
 * @author: 周海
 * @Create : 2024/3/24
 **/
public class WmThreadLocalUtil {
    private final static ThreadLocal<WmUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 存入线程中
     *
     * @param wmUser
     */
    public static void setUser(WmUser wmUser) {
        WM_USER_THREAD_LOCAL.set(wmUser);
    }

    /**
     * 从线程中获取数据
     *
     * @return
     */
    public static WmUser getUser() {

        return WM_USER_THREAD_LOCAL.get();
    }

    /**
     * 清理线程中的数据
     */
    public static void clear() {
        WM_USER_THREAD_LOCAL.remove();
    }
}
