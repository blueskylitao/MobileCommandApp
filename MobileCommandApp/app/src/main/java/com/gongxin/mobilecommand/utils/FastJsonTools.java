package com.gongxin.mobilecommand.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FastJsonTools {

    /**
     * 用fastjson 将json字符串解析为一个 JavaBean
     *
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> T getBean(String jsonString, Class<T> cls) {
        T t = null;
        try {
            t = JSON.parseObject(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 用fastjson 将json字符串 解析成为一个 List<JavaBean> 及 List<String>
     *
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> List<T> getBeans(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            list = JSON.parseArray(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

    /**
     * 用fastjson 将jsonString 解析成 List<Map<String,Object>>
     *
     * @param jsonString
     * @return
     */
    public static List<Map<String, Object>> getListMap(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            // 两种写法
            // list = JSON.parseObject(jsonString, new
            // TypeReference<List<Map<String, Object>>>(){}.getType());

            list = JSON.parseObject(jsonString,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;

    }

    /**
     * 用fastjson 将jsonString 解析成 Map<String,Object>
     *
     * @param jsonString
     * @return
     */
    public static HashMap<String, Object> getMap(String jsonString) {
        HashMap<String, Object> list = new HashMap<String, Object>();
        try {
            // 两种写法
            // list = JSON.parseObject(jsonString, new
            // TypeReference<List<Map<String, Object>>>(){}.getType());

            list = JSON.parseObject(jsonString,
                    new TypeReference<HashMap<String, Object>>() {
                    });
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

    public static LinkedHashMap<String, String> getMapAuthen(String jsonString) {
        LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();
        try {
            // 两种写法
            // list = JSON.parseObject(jsonString, new
            // TypeReference<List<Map<String, Object>>>(){}.getType());

            list = JSON.parseObject(jsonString,
                    new TypeReference<LinkedHashMap<String, String>>() {
                    });
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;

    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        Map<String, Object> list = FastJsonTools.getMap("{'问题1':{'5万内':'0','30-50万内':'0','50-300万内':'1'},'问题2':{'5万内':'0','30-50万内':'0','50-300万内':'1'}}");
        System.out.println(list.size());
        ;
        List<QAsw> awslist = new ArrayList<QAsw>();
        for (Map.Entry<String, Object> item : list.entrySet()) {
            System.out.println("问题:" + item.getKey());
            Map<String, Object> aws = FastJsonTools.getMap(item.getValue().toString());
            for (Map.Entry<String, Object> awitem : aws.entrySet()) {
                QAsw qa = new FastJsonTools().new QAsw();
                qa.setQname(item.getKey());
                qa.setAsw(awitem.getKey());
                qa.setVa(awitem.getValue().toString());
                awslist.add(qa);
                System.out.println(item.getKey() + " 答案:" + awitem.getKey() + "   答案标识:" + awitem.getValue());
            }

            //System.out.println("|||");

        }

        List<QAsw> awslist1 = new ArrayList<QAsw>();
        for (QAsw qAsw : awslist) {
            if (qAsw.getQname() == "问题1") {
                awslist1.add(qAsw);

            }
        }

    }

    public class QAsw {
        /**
         * 那个问题 -- 多个相同问题来对应答案
         */
        private String qname;

        public String getQname() {
            return qname;
        }

        public void setQname(String qname) {
            this.qname = qname;
        }

        /**
         * 答案
         */
        private String asw;

        public String getAsw() {
            return asw;
        }

        public void setAsw(String asw) {
            this.asw = asw;
        }

        public String getVa() {
            return va;
        }

        public void setVa(String va) {
            this.va = va;
        }

        /**
         * 答案标示符
         */
        private String va;
    }
}


