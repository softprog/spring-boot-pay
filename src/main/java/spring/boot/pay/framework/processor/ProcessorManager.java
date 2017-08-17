package spring.boot.pay.framework.processor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import spring.boot.pay.framework.base.PayTask;
import spring.boot.pay.framework.base.ResultModel;

@Component
public class ProcessorManager implements BeanPostProcessor {

    private static final Class DEF_TASK_TYPE = Object.class;

    private Map<Object,Processor> processorMap = new HashMap<Object,Processor>();
    private Map<Object,ProcessorResolver> resolverMap = new HashMap<Object,ProcessorResolver>();


    public ResultModel process(PayTask payTask)  {

        Class taskType = payTask.getClass();

        ProcessorResolver resolver = resolverMap.get(taskType);
        if(resolver == null){
            resolver = resolverMap.get(DEF_TASK_TYPE);
        }
        if(resolver == null){
            throw new RuntimeException("could not find processor to process task:"+ JSON.toJSONString(payTask));
        }

        Class processorClass = resolver.resolve(payTask);
        if(processorClass == null){
            throw new RuntimeException("could not find processor to process task:"+ JSON.toJSONString(payTask));
        }

        return processorMap.get(processorClass).process(payTask);
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if(bean instanceof Processor){
            Processor processor = (Processor)bean;
            processorMap.put(processor.getClass(),processor);
        }

        if(bean instanceof ProcessorResolver){
            Class taskType = getGeneric(bean.getClass(),ProcessorResolver.class);
            if(taskType == null){
                taskType = DEF_TASK_TYPE;
            }

            ProcessorResolver processorResolver = resolverMap.get(taskType);
            //每种任务类型只能有一个ProcessorResolver
            if(processorResolver != null){
                throw new RuntimeException("each taskType could not mapping more then one ProcessorResolver");
            }
            resolverMap.put(taskType,(ProcessorResolver)bean);
        }

        return bean;
    }

    public static Class getGeneric(Class clazz,Class superClass){
        Type[] types = clazz.getGenericInterfaces();
        for( Type type : types ){
            if( !( type.getTypeName().contains(superClass.getTypeName()) ) ){
                continue;
            }
            if( !(type instanceof ParameterizedType) ){
                continue;
            }

            ParameterizedType parameterizedType = (ParameterizedType)type;
            return (Class)parameterizedType.getActualTypeArguments()[0];
        }
        return null;
    }

}
