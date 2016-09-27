package spring.boot.pay.framework.base;


public interface PayTask {

    int STATUS_ACCEPTED = 0;//已接收
    int STATUS_TRYING = 1;  //正在进行
    int STATUS_FAIL = 2;    //失败
    int STATUS_SUCCESS = 3; //成功
    int STATUS_FirstFailed_pre=4;//未得到第三方支付响应提供的预支付id
    int STATUS_FirstFailed_qc=5;//未得到第三方支付响应提供的codeurl
    
  

   
}
