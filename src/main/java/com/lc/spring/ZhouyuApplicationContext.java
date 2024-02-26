package com.lc.spring;

/**
 * @author by licheng01
 * @date 2024/2/26 9:02
 * @description
 */
public class ZhouyuApplicationContext {

    private Class configClass;

    public ZhouyuApplicationContext(Class configClass) {
        this.configClass = configClass;

        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();

            path = path.replace(".", "/");

            ClassLoader classLoader = ZhouyuApplicationContext.class.getClassLoader();

        }
    }

    public Object getBean(String beanName) {
        return null;
    }


}
