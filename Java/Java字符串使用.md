字符串操作是计算机程序设计中最常见的行为，下面谈谈与字符串相关的知识。

我们先要记住三者的特征：
>|   类 |    类型 | 可变  |	线性安全|操作|
| :-----: | :--------:| :--: |:--:|:--:|
| String  | 字符串常量 |  不可变 |安全 |产生一个新对象|
| StringBuffer | 字符串变量    |   可变 | 安全 |内容发生改变|
| StringBuilder   |字符串变量   | 可变 | 不安全 |内容发生改变|

##一 、定义
![Alt text](./1451733257740.png)

查看 API 会发现，String、StringBuffer、StringBuilder 都实现了 CharSequence 接口，内部都是用一个char数组实现，虽然它们都与字符串相关，但是其处理机制不同。

#### 1. String
>`不可改变的量，也就是创建后就不能在修改了。`
`tring并不是基本数据类型，而是一个对象，并且是不可变的对`。String类为final型的（当然也不可被继承），几乎每一个修改String对象的操作，实际上都是创建了一个全新的String对象。

>字符串为对象，那么在初始化之前，它的值为null，到这里就有必要提下””、null、new String()三者的区别。null 表示string还没有new ，也就是说对象的引用还没有创建，也没有分配内存空间给他，而””、new String()则说明了已经new了，只不过内部为空，但是它创建了对象的引用，是需要分配内存空间的。打个比方：一个空玻璃杯，你不能说它里面什么都没有，因为里面有空气，当然也可以把它弄成真空，null与" "、new String()的区别就象真空与空气一样。

>在字符串中存在一个非常特殊的地方，那就是字符串池。每当我们创建一个字符串对象时，首先就会检查字符串池中是否存在面值相等的字符串，如果有，则不再创建，直接放回字符串池中对该对象的引用，若没有则创建然后放入到字符串池中并且返回新建对象的引用。这个机制是非常有用的，因为可以提高效率，减少了内存空间的占用。所以在使用字符串的过程中，推荐使用直接赋值（即String s=”aa”），除非有必要才会新建一个String对象（即String s = new String(”aa”)）。

#### 2.  StringBuffer
>一个可变字符串序列，它与 String 一样，在内存中保存的都是一个有序的字符串序列（char 类型的数组），不同点是 `StringBuffer 对象的值都是可变的`。

>由于他们内部的实现方式不同，导致他们所使用的范围不同，对于StringBuffer而言，他在处理字符串时，若是对其进行修改操作，它并不会产生一个新的字符串对象，所以说在内存使用方面它是优于String的。

>其实在使用方法，StringBuffer的许多方法和String类都差不多，所表示的功能几乎一模一样，只不过在修改时StringBuffer都是修改自身，而String类则是产生一个新的对象，这是他们之间最大的区别。

>同时StringBuffer是不能使用=进行初始化的，它必须要产生StringBuffer实例，也就是说你必须通过它的构造方法进行初始化。

#### 3. StringBuilder
>与 StringBuffer 类基本相同，都是可变字符换字符串序列，不同点是 `StringBuffer 是线程安全的，StringBuilder 是线程不安全的`。
StringBuilder也是一个可变的字符串对象，他与StringBuffer不同之处就在于它是线程不安全的，基于这点，它的速度一般都比StringBuffer快。与StringBuffer一样，StringBuider的主要操作也是append与insert方法。这两个方法都能有效地将给定的数据转换成字符串，然后将该字符串的字符添加或插入到字符串生成器中。


##二 、使用场景

+ 使用 String 类的场景：在字符串不经常变化的场景中可以使用 String 类，例如常量的声明、少量的变量运算。

+ 使用 StringBuffer 类的场景：在频繁进行字符串运算（如拼接、替换、删除等），并且运行在多线程环境中，则可以考虑使用 StringBuffer，例如 XML 解析、HTTP 参数解析和封装。

+ 使用 StringBuilder 类的场景：在频繁进行字符串运算（如拼接、替换、和删除等），并且运行在单线程的环境中，则可以考虑使用 StringBuilder，如 SQL 语句的拼装、JSON 封装等。

## 三、分析

在性能方面，由于 String 类的操作是产生新的 String 对象，而 StringBuilder 和 StringBuffer 只是一个字符数组的扩容而已，所以 String 类的操作要远慢于 StringBuffer 和 StringBuilder。

简要的说， String 类型和 StringBuffer 类型的主要性能区别其实在于 String 是不可变的对象, 因此在每次对 String 类型进行改变的时候其实都等同于生成了一个新的 String 对象，然后将指针指向新的 String 对象。所以经常改变内容的字符串最好不要用 String ，因为每次生成对象都会对系统性能产生影响，特别当内存中无引用对象多了以后， JVM 的 GC 就会开始工作，那速度是一定会相当慢的。

而如果是使用 StringBuffer 类则结果就不一样了，每次结果都会对 StringBuffer 对象本身进行操作，而不是生成新的对象，再改变对象引用。所以在一般情况下我们推荐使用 StringBuffer ，特别是字符串对象经常改变的情况下。

而在某些特别情况下， String 对象的字符串拼接其实是被 JVM 解释成了 StringBuffer 对象的拼接，所以这些时候 String 对象的速度并不会比 StringBuffer 对象慢，而特别是以下的字符串对象生成中， String 效率是远要比 StringBuffer 快的：
```
String S1 = “This is only a" + “ simple" + “ test";
StringBuffer Sb = new StringBuilder(“This is only a").append(“ simple").append(“ test");
```
你会很惊讶的发现，生成 String S1 对象的速度简直太快了，而这个时候 StringBuffer 居然速度上根本一点都不占优势。其实这是 JVM 的一个把戏，在 JVM 眼里，这个
```
String S1 = “This is only a" + “ simple" + “test";
```
其实就是：
```
String S1 = “This is only a simple test";
```
所以当然不需要太多的时间了。但大家这里要注意的是，如果你的字符串是来自另外的 String 对象的话，速度就没那么快了，譬如：
```
String S2 = "This is only a";
String S3 = "simple";
String S4 = "test";
String S1 = S2 +S3 + S4;
```
这时候 JVM 会规规矩矩的按照原来的方式去做。


### 关于 equal 和 ==

== 用于比较两个对象的时候，是来check 是否两个引用指向了同一块内存。
```
String str1 = new String("xyz");
String str2 = new String("xyz");
if(str1 == str2)
	Sysout.out.println("str1 == str2 is TRUE");
else
	Sysout.out.println("str1 == str2 is FASLE");
```
这个输出就是false
```
String str1 = new String("xyz");
//now str1 and str2 reference the same place in memory
String str2 = str1;
if(str1 == str2)
	Sysout.out.println("str1 == str2 is TRUE");
else
	Sysout.out.println("str1 == str2 is FASLE");
```
这个输出是true

一个特殊情况 ：
```
String str1 = "xyz";
String str2 = "xyz";
System.out.println("str1 == str2");
```
这个输出是true

这是因为：
>字符串缓冲池：程序在运行的时候会创建一个字符串缓冲池。
当使用 String s1 = “xyz”; 这样的表达是创建字符串的时候（非new这种方式），程序首先会在这个 String 缓冲池中寻找相同值的对象，
在 String str1 = “xyz”; 中，s1 先被放到了池中，所以在 s2 被创建的时候，程序找到了具有相同值的 str1
并将 s2 引用 s1 所引用的对象 “xyz”

#### equals()
equals() 是object的方法，默认情况下，它与== 一样，比较的地址。
但是当equal被重载之后，根据设计，equal 会比较对象的value。而这个是java希望有的功能。String 类就重写了这个方法
```
String str1 = new String("xyz");
String str2 = new String("xyz");
if(str1 == str2)
	Sysout.out.println("str1 == str2 is TRUE");
else
	Sysout.out.println("str1 == str2 is FASLE");
```

结果返回true

总的说，String 有个特点： 如果程序中有多个String对象，都包含相同的字符串序列，那么这些String对象都映射到同一块内存区域，所以两次new String(“hello”)生成的两个实例，虽然是相互独立的，但是对它们使用hashCode()应该是同样的结果。Note: 字符串数组并非这样，只有String是这样。即hashCode对于String，是基于其内容的。
```
public class StringHashCode {
       public static void main(String[] args) {
            \\输出结果相同
            String[] hellos = "Hello Hello".split(" " );
            System.out.println(""+hellos[0].hashCode());
            System.out.println(""+hellos[1].hashCode());
            \\输出结果相同
            String a = new String("hello");
            String b = new String("hello");
            System.out.println(""+a.hashCode());
            System.out.println(""+b.hashCode());
      }
}
```

##四、String部分源码
```
public final class String{
    private final char value[]; // used for character storage
    private int the hash; // cache the hash code for the string
}
```
成员变量只有两个：
final的char类型数组
int类型的hashcode

#### 构造函数
```
public String()
public String(String original){
    this.value = original.value;
    this.hash = original.hash;
}
public String(char value[]){
    this.value = Arrays.copyOf(value, value.length);
}
public String(char value[], int offset, int count){
    // 判断offset，count,offset+count是否越界之后
    this.value = Arrays.copyOfRange(value, offset, offset+count);
}
```
这里用到了一些工具函数
+ copyOf(source[],length); 从源数组的0位置拷贝length个；
这个函数是用System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength))实现的。

+ copyOfRange(T[] original, int from, int to)。

构造函数还可以用StringBuffer/StringBuilder类型初始化String,
```java
public String(StringBuffer buffer) {
       synchronized(buffer) {
           this.value = Arrays.copyOf(buffer.getValue(), buffer.length());
       }
   }
  public String(StringBuilder builder) {
       this.value = Arrays.copyOf(builder.getValue(), builder.length());
   }
```
除了构造方法，String类的方法有很多，
length,isEmpty,可以通过操作value.length来实现。
#### charAt(int index):
通过操作value数组得到。注意先判断index的边界条件
```java
public char charAt(int index) {
       if ((index < 0) || (index >= value.length)) {
           throw new StringIndexOutOfBoundsException(index);
       }
       return value[index];
   }
```
#### getChars方法
```java
public void getChars(int srcBegin, int srcEnd,
     char dst[], int dstBegin)
     {
     \\边界检测
     System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
     }
```
#### equals方法
根据语义相等（内容相等，而非指向同一块内存），重新定义了equals
```java
public boolean equals(Object anObject) {
       if (this == anObject) {
           return true;
       }
       if (anObject instanceof String) {
           String anotherString = (String)anObject;
           int n = value.length;
           if (n == anotherString.value.length) {
               char v1[] = value;
               char v2[] = anotherString.value;
               int i = 0;
               while (n-- != 0) {
                   if (v1[i] != v2[i])
                       return false;
                   i++;
               }
               return true;
           }
       }
       return false;
   }
```
如果比较的双方指向同一块内存，自然相等；（比较==即可）
如果内容相等，也相等，比较方法如下：
首先anObject得是String类型（用关键字instanceof）
然后再比较长度是否相等；
如果长度相等，则挨个元素进行比较，如果每个都相等，则返回true.

还有现成安全的与StringBuffer内容比较
contentEquals(StringBuffer sb),实现是在sb上使用同步。

#### compareTo()
如果A大于B，则返回大于0的数；
A小于B，则返回小于0的数；
A=B，则返回0
```java
public int compareTo(String anotherString) {
       int len1 = value.length;
       int len2 = anotherString.value.length;
       int lim = Math.min(len1, len2);
       char v1[] = value;
       char v2[] = anotherString.value;
 
       int k = 0;
       while (k < lim) {
           char c1 = v1[k];
           char c2 = v2[k];
           if (c1 != c2) {
               return c1 - c2;
           }
           k++;
       }
       return len1 - len2;
   }
```
#### regionMatches
如果两个字符串的区域都是平等的，
```
public boolean regionMatches(int toffset, String other, int ooffset,
            int len)
   {
    //判断边界条件
            while (len-- > 0) {
            if (ta[to++] != pa[po++]) {
                return false;
            }
        }
            }
 public boolean regionMatches(boolean ignoreCase, int toffset,
            String other, int ooffset, int len) 
{    
    while (len-- > 0) {
            char c1 = ta[to++];
            char c2 = pa[po++];
            if (c1 == c2) {
                continue;
            }
            if (ignoreCase) {
                // If characters don't match but case may be ignored,
                // try converting both characters to uppercase.
                // If the results match, then the comparison scan should
                // continue.
                char u1 = Character.toUpperCase(c1);
                char u2 = Character.toUpperCase(c2);
                if (u1 == u2) {
                    continue;
                }
                // Unfortunately, conversion to uppercase does not work properly
                // for the Georgian alphabet, which has strange rules about case
                // conversion.  So we need to make one last check before
                // exiting.
                if (Character.toLowerCase(u1) == Character.toLowerCase(u2)) {
                    continue;
                }
            }
            return false;
        }
        return true;
}
```
#### startsWith(String prefix, int toffset)
#### startsWith(String prefix)
#### endsWith(String suffix)
```
{
    return startsWith(suffix, value.length 
    - suffix.value.length);
    }
```
#### substring(int beginIndex,int endIndex)
除了条件判断：
```
return (beginIndex == 0) ? this : new String(value, beginIndex, subLen);
```
#### 字符串连接concat(String str)
```java
concat(String str){
int otherLen = str.length();
       if (otherLen == 0) {
           return this;
       }
       int len = value.length;
       char buf[] = Arrays.copyOf(value, len + otherLen);
       str.getChars(buf, len);
       return new String(buf, true);
}
```
#### 对于StringBuffer和StringBuilder
StringBuffer 和 StringBuilder 都是继承于 AbstractStringBuilder, 底层的逻辑（比如append）都包含在这个类中。
```java

       if (str == null) str = "null";
       int len = str.length();
       ensureCapacityInternal(count + len);//查看使用空间满足，不满足扩展空间
       str.getChars(0, len, value, count);//getChars就是利用native的array copy,性能高效
       count += len;
       return this;
   }
```
StringBuffer 底层也是 char[], 数组初始化的时候就定下了大小, 如果不断的 append 肯定有超过数组大小的时候，我们是不是定义一个超大容量的数组，太浪费空间了。就像 ArrayList 的实现，采用动态扩展，每次 append 首先检查容量，容量不够就先扩展，然后复制原数组的内容到扩展以后的数组中。

## 五、常用操作(String类)
#### 1. 字符串查找
String提供了两种查找字符串的方法，即indexOf与lastIndexOf方法。
 + indexOf（String s） 
    该方法用于返回参数字符串s在指定字符串中首次出现的索引位置，当调用字符串的indexOf()方法时，会从当前字符串的开始位置搜索s的位置；如果没有检索到字符串s，该方法返回-1。
```
String str ="We are students";
int size = str.indexOf("a"); // 变量size的值是3
```
+ lastIndexOf(String str)
    该方法用于返回字符串最后一次出现的索引位置。当调用字符串的lastIndexOf()方法时，会从当前字符串的开始位置检索参数字符串str，并将最后一次出现str的索引位置返回。如果没有检索到字符串str，该方法返回-1。
	如果lastIndexOf方法中的参数是空字符串"" ，，则返回的结果与length方法的返回结果相同。

#### 2. 获取指定索引位置的字符
使用charAt()方法可将指定索引处的字符返回。
```
String str = "hello word";
char mychar =  str.charAt(5);  // mychar的结果是w
```
#### 3. 获取子字符串
   通过String类的substring()方法可对字符串进行截取。这些方法的共同点就是都利用字符串的下标进行截取，且应明确字符串下标是从0开始的。在字符串中空格占用一个索引位置。

+ substring(int beginIndex)
该方法返回的是从指定的索引位置开始截取知道该字符串结尾的子串。
```
String str = "Hello word";
String substr = str.substring(3); //获取字符串，此时substr值为lo word
```
+ substring(int beginIndex,  int endIndex)
    beginIndex : 开始截取子字符串的索引位置
    endIndex：子字符串在整个字符串中的结束位置
```
String str = "Hello word";
String substr = str.substring(0,3); //substr的值为hel
```
#### 4. 去除空格
trim()方法返回字符串的副本，忽略前导空格和尾部空格。
#### 5. 字符串替换
replace()方法可实现将指定的字符或字符串替换成新的字符或字符串
+  oldChar：要替换的字符或字符串
+  newChar：用于替换原来字符串的内容

如果要替换的字符oldChar在字符串中重复出现多次，replace()方法会将所有oldChar全部替换成newChar。需要注意的是，要替换的字符oldChar的大小写要与原字符串中字符的大小写保持一致。
```
String str= "address";
String newstr = str.replace("a", "A");// newstr的值为Address
```
#### 6. 判断字符串的开始与结尾
`startsWith()`方法与`endsWith()`方法分别用于判断字符串是否以指定的内容开始或结束。这两个方法的返回值都为`boolean`类型。
  + startsWith(String prefix) 
      该方法用于判断当前字符串对象的前缀是否是参数指定的字符串。

  + endsWith(String suffix) 
     该方法用于判断当前字符串是否以给定的子字符串结束

#### 7. 判断字符串是否相等
+ equals(String otherstr)
如果两个字符串具有相同的字符和长度，则使用`equals()`方法比较时，返回`true`。同时`equals()`方法比较时区分大小写。

+ equalsIgnoreCase(String otherstr)
    equalsIgnoreCase()方法与equals()类型，不过在比较时忽略了大小写。

#### 8. 按字典顺序比较两个字符串
compareTo()方法为按字典顺序比较两个字符串，该比较基于字符串中各个字符的Unicode值，按字典顺序将此String对象表示的字符序列与参数字符串所表示的字符序列进行比较。如果按字典顺序此String对象位于参数字符串之前，则比较结果为一个负整数；如果按字典顺序此String对象位于参数字符串之后，则比较结果为一个正整数；如果这两个字符串相等，则结果为0.

+ str.compareTo(String otherstr);
字母大小写转换
    字符串的toLowerCase()方法可将字符串中的所有字符从大写字母改写为小写字母，而tuUpperCase()方法可将字符串中的小写字母改写为大写字母。
```
str.toLowerCase();
str.toUpperCase();
```
#### 9. 字符串分割
使用split()方法可以使字符串按指定的分隔字符或字符串对内容进行分割，并将分割后的结果存放在字符数组中。
+ str.split(String sign);
sign为分割字符串的分割符，也可以使用正则表达式。
没有统一的对字符串进行分割的符号，如果想定义多个分割符，可使用符号“|”。例如，“,|=”表示分割符分别为“，”和“=”。

+ str.split(String sign, in limit)；
该方法可根据给定的分割符对字符串进行拆分，并限定拆分的次数

##六、最常见Java字符串问题Top10
#### 1. 如何比较字符串？使用“==”还是使用equals() ？

>简单地说，“==”测试引用同一地址，而equals()测试值是否相等。除非需要检查两个字符串是否是同一个对象，否则应该总是使用equals()方法。

>如果你知道字符串驻留（string interning）的概念的就更好了。

>译注：字符串驻留是指为每个独立的String值只保留一个不可改变的拷贝，详细解释可参见String interning词条。

#### 2. 为什么为在处理私密信息的时候，选择char [ ]比String好？

>String是不可改变的。这意味着一旦String对象被创建，那个地址上的值将保持不变，直到垃圾收集器有空来做自动清理。而使用char[ ]可以（在用完后）明确地修改它的元素。这种一来，私密信息（例如密码）就不会在系统的任何地方出现。

#### 3. 我们可以在switch语句中用String作分支条件吗？

>从JDK 7开始是可以的。我们可以使用String符串作为Switch条件。第JDK 6之前，我们不能使用String作为Switch条件。
```java
// java 7 only!
switch (str.toLowerCase()) {
      case "a":
           value = 1;
           break;
      case "b":
           value = 2;
           break;
}
```

#### 4. 如何转换String为int？
```
int n = Integer.parseInt("10");
```
>调用很简单，使用如此频繁以致有时会被忽略。

#### 5. 如何用空白符分割字符串？

>可以简单地使用正则表达式做分割。“ \s ”代表所有空格符，如“ ”、 “ \ ”、 “ \ r”、“ \ n ”。
```
String[] strArray = aString.split("\\s+");
```
####6. substring() 具体干了些什么？

>在JDK 6中，substring() 提供了一个显示已有字符串char[]的接口，但不创建新字符串。如果需要创建一个新的char[]表示的字符串，可以像下面一样与一个空字符串相加：
```
str.substring(m, n) + ""
```
>这样会创建一个新的字符数组，表示新的字符串。示例方法有时可以使代码运行更快，因为垃圾收集器可以收集未使用的大字符串只保留子串。

>在Oracle JDK 7中 ，substring()会创建一个新的字符数组而不是使用现有的。

####7. String vs StringBuilder vs StringBuffer

>String 与StringBuilder的区别：StringBuilder的是可变的，这意味着可以在创建以后再作修改。
StringBuilder与StringBuffer的区别: StringBuffer的是同步的，这意味着它是线程安全的，但速度比StringBuilder慢。
#### 8. 如何重复一个字符串？

>在Python中，我们可以乘以一个数字来重复字符串。在Java中，我们可以使用Apache公共语言包(Apache Commons Lang package)中的repeat()。
```
String str = "abcd";
String repeated = StringUtils.repeat(str,3);
//abcdabcdabcd
```
#### 9. 如何转换字符串为日期？
```java
String str = "Sep 17, 2013";
Date date = new SimpleDateFormat("MMMM d, yy", Locale.ENGLISH).parse(str);
System.out.println(date);
//Tue Sep 17 00:00:00 EDT 2013
```
#### 10. 如何统计某字符在一个字符串中的出现次数？

>使用apache公共语言包中的StringUtils：
```
int n = StringUtils.countMatches("11112222", "1");
System.out.println(n);
```

#### 11. String s = new String(“xyz”); 创建了几个字符串对象？ 
>两个对象，一个静态存储区“xyz”, 一个用new创建在堆上的对象。



## 七、字符串拼接技巧
遇到需要拼接如下格式的字符串：

1,2,3,4,5,6,7,8,9,10,11,12,12,12,12,34,234,2134,1234,1324,1234,123

这个字符串的特点：`多个数据之间通过某一个特殊符号分割`。

#### 不推荐的方法：
```java
public class StringTest
{
    public static void main(String[] args)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 10; i++)
        {　　
　　　　//以下这个判断对于有强迫症的人来说确实有点不爽，每次循环进来都要进行判断以下，如果循环的次数很多呢，我们都会想到会不会对性能有影响呢？
            if (i != 0)
            {
                sb.append(",");
            }
            sb.append(i);
        }
        System.out.println(sb.toString());
    }
}
```
更不用说用“+”拼接了。

#### 推荐方法：
方法一：首先取出集合的第一个元素，通过StringBuilder的构造方法，传入第一个元素，这样是不是后面的每一次循环就不需要判断了啊？
```java
public class StringTest
{
    public static void main(String[] args)
    {
        StringBuilder sb = new StringBuilder("0");
        for (int i = 1; i <= 10; i++)
        {
            sb.append(",");
            sb.append(i);
        }
        System.out.println(sb.toString());
    }
}
```

方式二：（《Java编程思想第四版》P286）
```java
public class StringTest
{
    public static void main(String[] args)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 10; i++)
        {
            sb.append(i);
            sb.append(",");
        }
        sb.delete(sb.length() - 1, sb.length());//这里就看你知不知道这个api接口了
        System.out.println(sb.toString());
    }
}
```

 


