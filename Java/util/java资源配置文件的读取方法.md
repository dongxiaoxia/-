代码如下：
```
            public class PropertiesUtilsTest {
                public static void main() throws Exception {
                    Properties properties = new Properties();

                    properties.load(PropertiesUtilsTest.class.getResourceAsStream("/jdbc.properties"));//路径前面加"/"表示classpath绝对路径根下获取，不加"/"表示当前类的路径
                    //静态方法不能用this
                    //properties.load(this.getClass().getResourceAsStream("/jdbc.properties"));

                    Properties properties1 = new Properties(properties);
                    properties1.load(PropertiesUtilsTest.class.getClassLoader().getResourceAsStream("jdbc.properties"));//默认从ClassPath根下获取，不能以’/'开头
                    //静态方法不能用this
                    //properties1.load(this.getClass().getClassLoader().getResourceAsStream("jdbc.properties"));

                    Properties properties2 = new Properties();
                    properties2.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties"));

                    System.out.println(properties);
                }
            }
```

其次，getResourceAsStream 用法大致有以下几种：

第一： 要加载的文件和.class文件在同一目录下，例如：com.x.y 下有类me.class ,同时有资源文件myfile.xml

那么，应该有如下代码：

me.class.getResourceAsStream("myfile.xml");

第二：在me.class目录的子目录下，例如：com.x.y 下有类me.class ,同时在 com.x.y.file 目录下有资源文件myfile.xml

那么，应该有如下代码：

me.class.getResourceAsStream("file/myfile.xml");

第三：不在me.class目录下，也不在子目录下，例如：com.x.y 下有类me.class ,同时在 com.x.file 目录下有资源文件myfile.xml

那么，应该有如下代码：

me.class.getResourceAsStream("/com/x/file/myfile.xml");

总结一下，可能只是两种写法

第一：前面有 “   / ”

“ / ”代表了工程的根目录，例如工程名叫做myproject，“ / ”代表了myproject

me.class.getResourceAsStream("/com/x/file/myfile.xml");

第二：前面没有 “   / ”

代表当前类的目录

me.class.getResourceAsStream("myfile.xml");

me.class.getResourceAsStream("file/myfile.xml");

最后，自己的理解：
getResourceAsStream读取的文件路径只局限与工程的源文件夹中，包括在工程src根目录下，以及类包里面任何位置，但是如果配置文件路径是在除了源文件夹之外的其他文件夹中时，该方法是用不了的。
