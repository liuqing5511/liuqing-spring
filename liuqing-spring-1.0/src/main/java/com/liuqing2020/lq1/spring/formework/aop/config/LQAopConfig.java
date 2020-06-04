package com.liuqing2020.lq1.spring.formework.aop.config;

import lombok.Data;

/**
 * <p> </p>
 *
 * @className LQAopConfig
 * @Description TODO
 * @Author liuqing
 * @Date 2020/2/27 22:28
 * @Version 1.0.0
 * @see
 */
@Data
public class LQAopConfig {
    public final static String POINT_CUT="pointCut";
    public final static String ASPECT_BEFORE="aspectBefore";
    public final static String ASPECT_AFTER="aspectAfter";
    public final static String ASPECT_CLASS="aspectClass";
    public final static String ASPECT_AFTER_THROW="aspectAfterThrow";
    public final static String ASPECT_AFTER_THROWING_NAME="aspectAfterThrowingName";
    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;
}
