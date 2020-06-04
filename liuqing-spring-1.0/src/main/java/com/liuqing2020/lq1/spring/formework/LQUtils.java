package com.liuqing2020.lq1.spring.formework;

/**
 * <p> </p>
 *
 * @className LQUtils
 * @Description TODO
 * @Author liuqing
 * @Date 2019/11/11 1:27
 * @Version 1.0.0
 * @see
 */
public  final class   LQUtils {

    /**
     * 首字母小写
     * <p>如果类名字是小写的会出问题</p>
     * <p>所以类必须驼峰命名</p>
     * @param simpleName
     * @return
     */
    public static String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        //+32 是因为大写小写的字母的ASCII码相差32，
        // 大写字母的ASCII码要少于小写的ASCII码
        //在java中对char做算学运算，实际就是对ASCII码做算学运算
        chars[0] += 32;
        return new String(chars);
    }

    public static Object caseStringValue(String value, Class<?> paramsType) {
        if(String.class == paramsType){
            return value.toString();
        }
        //如果是int
        if(Integer.class == paramsType){
            return Integer.valueOf(value);
        }
        else if(Double.class == paramsType){
            return Double.valueOf(value);
        }else {
            if(value != null){
                return value;
            }
            return null;
        }
        //如果还有double或者其他类型，继续加if
        //这时候，我们应该想到策略模式了
        //在这里暂时不实现，希望小伙伴自己来实现
    }

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }
}
