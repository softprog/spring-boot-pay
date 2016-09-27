package spring.boot.pay.framework.base;


public interface PayStatus {

    int STATUS_ACCEPTED = 0;//已接收
    int STATUS_TRYING = 1;  //正在进行
    int STATUS_FAIL = 2;    //失败
    int STATUS_SUCCESS = 3; //成功


}
