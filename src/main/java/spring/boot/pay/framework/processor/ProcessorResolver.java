package spring.boot.pay.framework.processor;

import spring.boot.pay.framework.base.PayTask;

public interface ProcessorResolver<T extends PayTask>{

    Class<? extends Processor> resolve(T task);

}
