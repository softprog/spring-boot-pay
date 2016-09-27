package spring.boot.pay.framework.processor;

import spring.boot.pay.framework.base.PayTask;
import spring.boot.pay.framework.base.ResultModel;

public interface Processor<T extends PayTask> {

    ResultModel process(T t);

}
