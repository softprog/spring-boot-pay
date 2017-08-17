package spring.boot.pay.config.properties;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class SecurityProperties {

    //ip白名单
    private static Set<String> whiteList;

      public static void init() throws IOException {

        Set<String> set = new HashSet<String>();

        InputStream in= Object.class.getResourceAsStream("/config/whitelist.txt");
        
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(reader);
        for(String str = bufferedReader.readLine();
                str != null;
                str = bufferedReader.readLine()){
            set.add(str);
        }

        whiteList = set;

        in.close();
    }

    public static Set<String> getWhiteList(){
        return whiteList;
    }

}
