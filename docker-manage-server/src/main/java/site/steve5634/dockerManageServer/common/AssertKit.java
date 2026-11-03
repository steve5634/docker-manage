package site.steve5634.dockerManageServer.common;

import site.steve5634.dockerManageServer.common.execption.BaseException;
import site.steve5634.dockerManageServer.common.utils.StringUtils;

import java.util.List;

/**
 * AssertKit
 *
 * @author yilabao
 * @date 2021年3月3日
 */
public class AssertKit {

    private AssertKit() {
    }

    /**
     * 参数不为null时抛出异常
     *
     * @author yilabao
     * @date 2021年5月10日
     */
    public static void isNull(Object obj, String msg) {
        if (obj != null) {
            throwException(msg);
        }
    }

    /**
     * 参数为null时抛出异常
     *
     * @author yilabao
     * @date 2021年3月3日
     */
    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throwException(msg);
        }
    }

    /**
     * 参数isBlank时抛出异常
     *
     * @author yilabao
     * @date 2021年3月3日
     */
    public static void notBlank(Object obj, String msg) {
        notNull(obj, msg);
        if (StringUtils.isEmpty(String.valueOf(obj))) {
            throwException(msg);
        }
    }

    /**
     * 参数不为ture抛出异常
     *
     * @author yilabao
     * @date 2021年3月8日
     */
    public static void isTrue(Boolean obj, String msg) {
        notNull(obj, msg);
        if (!obj) {
            throwException(msg);
        }
    }

    /**
     * 参数不为false抛出异常
     *
     * @author yilabao
     * @date 2021年5月10日
     */
    public static void isFalse(Boolean obj, String msg) {
        notNull(obj, msg);
        if (obj) {
            throwException(msg);
        }
    }

    /**
     * 参数为null或空抛出异常
     *
     * @author yilabao
     * @date 2021年4月29日
     */
    public static void notEmpty(List<?> list, String msg) {
        notNull(list, msg);
        if (list.isEmpty()) {
            throwException(msg);
        }
    }

    private static void throwException(String msg) {
        throw new BaseException(msg);
    }

}
