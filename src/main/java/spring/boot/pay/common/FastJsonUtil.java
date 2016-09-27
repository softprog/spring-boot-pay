package spring.boot.pay.common;

import java.time.LocalDateTime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public final class FastJsonUtil {

	public static final <T> String objToString (T obj){
		
		return JSON.toJSONString(obj);
	}
	
public static final <T> T stringToObj (String data,T obj){
	
		return (T)JSON.parseObject(data, obj.getClass());
	}

public static final JSONObject stringToJSONObj (String data){
	
	return JSON.parseObject(data);
}

public static class VO {

    private LocalDateTime date;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
}


