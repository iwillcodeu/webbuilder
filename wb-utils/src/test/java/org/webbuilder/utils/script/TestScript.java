package org.webbuilder.utils.script;

import org.junit.Assert;
import org.junit.Test;
import org.webbuilder.utils.script.engine.DynamicScriptEngine;
import org.webbuilder.utils.script.engine.DynamicScriptEngineFactory;
import org.webbuilder.utils.script.engine.ExecuteResult;
import org.webbuilder.utils.script.engine.common.listener.CommonScriptExecuteListener;
import org.webbuilder.utils.script.engine.listener.ExecuteEvent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 浩 on 2015-10-27 0027.
 */
public class TestScript {

    /**
     * 测试执行js脚本
     */
    @Test
    public void testJs() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("js");
        engine.init("var Integer = ".concat(Integer.class.getName()).concat(";"));
        engine.compile("test", "return new Integer(1);");

        ExecuteResult result = engine.execute("test", new HashMap<String, Object>());
        System.out.println(result.getResult());
        Assert.assertEquals(result.getResult(), 1);
    }

    /**
     * 测试执行groovy
     */
    @Test
    public void testGrv() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("groovy");
        //engine.init("i=1;", "i = 20;");
        engine.compile("user", "\n" +
                "public class User {\n" +
                "      {\n" +
                "        System.out.println(1);\n" +
                "    }\n" +
                "\n" +
                "    public String str;\n" +
                "}\n ");
    }

    @Test
    public void testJava() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("java");
        String id = "org.webbuilder.script.Test";
        String code = "package org.webbuilder.script;\n" +
                "import org.webbuilder.utils.script.engine.java.Executor;\n" +
                "import java.util.*;\n" +
                "public class Test implements Executor{\n" +
                "public static Test instance = new Test();\n" +
                "public static Test getInstance(){return instance;};\n" +
                "    public Object execute(Map<String, Object> var) throws Exception {\n" +
                "        return var+\".aaa\";\n" +
                "    }\n" +
                "}";
        //编译动态代码
        engine.compile(id, code);
        //执行动态代码
        ExecuteResult result =  engine.execute(id,new HashMap<String, Object>(){{
            put("param","55555");
        }});
        System.out.println(result.getResult());
    }


    /**
     * 测试执行groovy
     */
    @Test
    public void testGrvClass() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("groovy");
        //engine.init("i=1;", "i = 20;");
        engine.compile("v", "public class AuthInsertBefore{\n" +
                "    public static final String valid(String param){\n" +
                "            return param;\n" +
                "    }\n" +
                "}");
        engine.compile("test", "return AuthInsertBefore.valid(\"呵呵\");");

        ExecuteResult result = engine.execute("test", new HashMap<String, Object>() {
            {
                put("user", 20);
            }
        });
        Assert.assertEquals(result.getResult(), "呵呵");
    }

    /**
     * 测试执行SpEL
     */
    @Test
    public void testSpel() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("spel");
        engine.compile("test", "#user[name]+'('+#user[age]+')'");
        Map<String, Object> root = new HashMap<>();
        root.put("user", new HashMap<Object, Object>() {{
            put("name", "张三");
            put("age", 10);
        }});
        ExecuteResult result = engine.execute("test", root);
        Assert.assertEquals(result.getResult(), "张三(10)");
    }

    /**
     * 测试执行ognl
     */
    @Test
    public void testOgnl() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("ognl");
        engine.compile("test", "user.name");
        Map<String, Object> root = new HashMap<>();
        root.put("user", new HashMap<Object, Object>() {{
            put("name", "张三");
        }});
        ExecuteResult result = engine.execute("test", root);
        Assert.assertEquals(result.getResult(), "张三");
    }

}
