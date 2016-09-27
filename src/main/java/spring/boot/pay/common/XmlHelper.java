package spring.boot.pay.common;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class XmlHelper {


    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     */
    public static SortedMap<String,String> parseXmlToMap(String strXml){
        if (null == strXml || "".equals(strXml)) {
            return new TreeMap<String, String>();
        }

        SortedMap<String, String> m = new TreeMap<String, String>();

        StringReader read = new StringReader(strXml);
        SAXBuilder builder = new SAXBuilder();
        Document doc;
        try {
            doc = builder.build(read);
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Element root = doc.getRootElement();
        List<Element> list = root.getChildren();
        for (Element e : list) {
            String k = e.getName();
            String v = e.getChildren().isEmpty() ? e.getTextNormalize() : getChildrenText(e.getChildren());
            m.put(k, v);
        }

        //关闭流
        read.close();

        return m;
    }

    /**
     * 获取子结点的xml
     * @param children
     * @return String
     */
    private static String getChildrenText(List<Element> children) {
        StringBuilder sb = new StringBuilder();
        if (!children.isEmpty()) {
            for (Element e : children) {
                String name = e.getName();
                String value = e.getTextNormalize();
                List<Element> list = e.getChildren();
                sb.append("<").append(name).append(">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</").append(name).append(">");
            }
        }

        return sb.toString();
    }
}
