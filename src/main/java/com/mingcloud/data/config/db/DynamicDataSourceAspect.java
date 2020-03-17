package com.mingcloud.data.config.db;

import com.mingcloud.data.common.DBChange;
import com.mingcloud.data.entity.DataSourceEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Order(-1)  // 该切面应当先于 @Transactional 执行
@Component
public class DynamicDataSourceAspect {

    @Pointcut("@annotation(com.mingcloud.data.common.DBChange)")
    private void pointcut() {

    }
    @Autowired
    private DynamicDataSource dynamicDataSource;


    @Before("pointcut()")
    public void beforeSwitchDS(JoinPoint point){
        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();
        //获得访问的方法名
        String methodName = point.getSignature().getName();
        Object[] objects=point.getArgs();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
        String dataSource = null;
        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);
            Parameter[] parameters = method.getParameters();
            // 判断是否存在注解
            for (int i = 0; i <parameters.length; i++) {
                Parameter parameter = parameters[i];
                if (parameter.isAnnotationPresent(DBChange.class)) {
                    DataSourceEntity dataSourceEntity1 =(DataSourceEntity) objects[i];
                    dynamicDataSource.createDataSourceWithCheck(dataSourceEntity1);
                    dataSource = dataSourceEntity1.getId();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 切换数据源
            DBContextHolder.setDataSource(dataSource);
        }
    }

    @After("pointcut()")
    public void afterSwitchDS(JoinPoint point){
        DBContextHolder.clearDataSource();
    }

}
