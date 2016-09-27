package spring.boot.pay.config.properties;


import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class SecurityProperties {

    //ip白名单
    private static Set<String> whiteList;

    public static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANZNrY6KXMqrWZaY" +
            "oFVNRoroHHQ4Y9B11yUXy3Gcz6OfspVxDEhfVdnluC9kJ4JHM87IiNuVfIx4KxrZ" +
            "jJF6MiBi1LJtZ1KsNySpbUjzswzZJSKg6xDLYj+mnP1gjcSsGxLGjk+VSPHvukIC" +
            "7vqDU9AIlKskz4wLlRROU18BnTkzAgMBAAECgYEAqKlL7gIxU4SWJrMKhPgji9Fw" +
            "C3wR+o+z+sqGYG1U8ecPCxSE4TS/AZYNZqcBfUQCnz0ukPtOodM1wZT0T86HQYsQ" +
            "7vq7JgrwytbxMSqh9KPa6DqCqdjUzANFcARiGp/gRzpqlBOux8hvMJeuvmMVT3GR" +
            "UFsDwE2qecI8J+dRRikCQQDw/DsUNsfGWzCc3COOaWyqQCbOQjHO/8ehJJ09IvrB" +
            "UpJcdJHiQFZYyKqu53xPAttE0aBmxFLZpL3ehgDkxQ+VAkEA46fdoMJattAySikv" +
            "YfJcPfcVkz6e6aZtM3zBtP5yGOhC0SOH9LQGy4PDk4rdfCQdix9TAXvtdCKJ9jZy" +
            "90gTpwJAWxXLMsm92tBu4GsomVRpuTQENuO4Ndhh2RugvxHwm4+dOIrq9QyCVo6K" +
            "TQ+74ZVf0XQ2X4GHGzJ0fZ5CyjVdvQJBAJ5OjBKPNE+uBVqGuzlMNJKucCVdRVpK" +
            "7eMA3R7EVYk2cchHAfLLAXKiJO1DgnMiFh05YhpFGEFLB07cXCPSzj8CQDDfjBRd" +
            "S2o0dUyCTcnRd3yjk41OAEPYHVHa2oec902HriHTLuIvY2VRimZVIK0YmTaujNvQ" +
            "aZK8v7LFpOyFxmE=";

    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDWTa2OilzKq1mWmKBVTUaK6Bx0" +
            "OGPQddclF8txnM+jn7KVcQxIX1XZ5bgvZCeCRzPOyIjblXyMeCsa2YyRejIgYtSy" +
            "bWdSrDckqW1I87MM2SUioOsQy2I/ppz9YI3ErBsSxo5PlUjx77pCAu76g1PQCJSr" +
            "JM+MC5UUTlNfAZ05MwIDAQAB";

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
