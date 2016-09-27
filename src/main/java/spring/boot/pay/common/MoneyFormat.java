package spring.boot.pay.common;

import java.math.BigDecimal;

public class MoneyFormat {

    /**
     * 将分转化为元
     */
    public static final String fen2yuan(BigDecimal money){
        int yuan = money.intValue()/100;
        int fen = money.intValue() %100;

        if(fen<10){
            return yuan+".0"+fen;
        }

        return yuan+"."+fen;
    }

    /**
     * 将元转化为分
     */
    public static int yuan2fen(String money) {
        int yuan = 0;
        int fen = 0;
        int index = money.indexOf(".");
        if(index != -1){
            yuan = Integer.parseInt(money.substring(0,index));
            String fenStr = money.substring(index+1);
            fenStr = fenStr.length()==1 ? fenStr+"0" : fenStr ;
            fen = Integer.parseInt(fenStr);
        }else {
            yuan = Integer.parseInt(money);
        }
        return yuan*100 + fen;
    }

}
