package com.lc.spring;

import com.lc.spring.constants.ScopeName;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author by licheng01
 * @date 2024/2/26 9:02
 * @description
 */
public class ZhouyuApplicationContext {

    private Class configClass;

    private ConcurrentHashMap<String, Object> singletonMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();


    public ZhouyuApplicationContext(Class configClass) {
        this.configClass = configClass;
        scan(configClass);

        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if (beanDefinition.getScope().equals(ScopeName.SINGLETON.getType())) {
                Object obj = createBean(beanDefinition);
                singletonMap.put(beanName, obj);
            } else {

            }
        });
    }

    private void scan(Class configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
            // com.lc.service
            String path = componentScanAnnotation.value();
            // com/lc/service
            path = path.replace(".", "/");
            ClassLoader classLoader = ZhouyuApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    // /Users/licheng/ws/github/java/zhouyu_write_spring/target/classes/com/lc/service/UserService.class
                    String filePathName = f.getAbsolutePath();
                    if (!filePathName.endsWith("class")) {
                        continue;
                    }
                    // com/lc/service/UserService.class
                    String clazzName = filePathName.substring(filePathName.indexOf("com"), filePathName.indexOf(".class"));
                    clazzName = clazzName.replace("/", ".");
                    Class cls = null;
                    try {
                        cls = classLoader.loadClass(clazzName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (cls == null) {
                        continue;
                    }
                    if (!cls.isAnnotationPresent(Component.class)) {
                        continue;
                    }
                    Component component = (Component) cls.getDeclaredAnnotation(Component.class);
                    String beanName = getBeanName(component, cls);
                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setClazz(cls);
                    if (cls.isAnnotationPresent(Scope.class)) {
                        Scope scope = (Scope) cls.getDeclaredAnnotation(Scope.class);
                        beanDefinition.setScope(scope.value().getType());
                    } else {
                        beanDefinition.setScope(ScopeName.SINGLETON.getType());
                    }
                    beanDefinitionMap.put(beanName, beanDefinition);
                }
            }

        }
    }

    private <T> String getBeanName(Component component, Class<T> cls) {
        String beanName = component.value();
        if (beanName == null || beanName.equals("")) {
            return cls.getSimpleName().substring(0, 1).toLowerCase() + cls.getSimpleName().substring(1);
        }
        return beanName;
    }

    private Object createBean(BeanDefinition beanDefinition) {

        try {
            return beanDefinition.getClazz().getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    public Object getBean(String beanName) {
        if (beanDefinitionMap.get(beanName) != null) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope() == ScopeName.SINGLETON.getType()) {
                return singletonMap.get(beanName);
            } else {
                return createBean(beanDefinition);
            }
        } else {
            throw new RuntimeException("没有这样的beanName");
        }
    }


}
