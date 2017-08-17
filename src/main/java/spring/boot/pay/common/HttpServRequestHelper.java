package spring.boot.pay.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

public class HttpServRequestHelper {

    public static String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String inputLine = reader.readLine();
        while (inputLine != null) {
            sb.append(inputLine);
            inputLine = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }


    public static SortedMap<String,String> genSortedMap(HttpServletRequest request){

        SortedMap<String,String> params = new TreeMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (String name: requestParams.keySet()) {

            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

}
