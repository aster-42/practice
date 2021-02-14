package spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

import java.util.Map;

/**
 * spring hook
 * spring bean 实现增强
 *
 * ApplicationContextAware 可以获取 ApplicationContext 的实例
 *   bean 管理, 事件管理等
 *
 * BeanFactoryAware
 *  在 bean 生命周期中的 set 方法执行后，对 BeanFactory 和 BeanName 进行注入
 *
 * BeanFactoryPostProcessor
 *   实现动态代理
 *
 * Ordered
 *  加载顺序
 * */
public class IPowerBean implements ApplicationContextAware, BeanFactoryAware, BeanFactoryPostProcessor, Ordered {
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    public Map<String, ?> beanOfType(Class<?> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
