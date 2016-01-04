原文出处参考：[java那些事](http://mp.weixin.qq.com/s?__biz=MjM5MTM0NjQ2MQ==&amp;mid=210342222&amp;idx=3&amp;sn=1ea5ccf51c45225e457a905b4d52c99c&amp;scene=1#rd) [luoweifu](http://blog.csdn.net/luoweifu/article/details/46613015)  [码农网](http://www.codeceo.com/article/java-mult-thread-interview.html) 

　　多线程能满足程序员编写非常有效率的程序来达到充分利用CPU的目的，因为CPU的空闲时间能够保持在最低限度。有效利用多线程的关键是理解程序是并发执行而不是串行执行的。例如：程序中有两个子系统需要`并发执行`，这时候就需要利用多线程编程。线程的运行中需要使用计算机的内存资源和CPU。

## 一、 进程与线程的概念
　　`进程`：进程是一个独立的活动的实体，是系统资源分配的基本单元。它可以申请和拥有系统资源。每个进程都具有独立的代码和数据空间（进程上下文）。进程的切换会有较大的开销。进程，是一个“执行中的程序”。程序是一个静态的没有生命的实体，只有处理器赋予程序生命时（操作系统执行之），它才能成为一个活动的实体，我们称其为进程。也就是说，进程是正在`运行的程序的实例`（an instance of a computer program that is being executed）。例如，你运行一个qq，就会启动一个进程，再次运行qq，就会再启动一个进程。

　


　　`线程`： 其实，60年代，进程不仅是资源分配的基本单元，还是资源调度的基本单元。然而随着计算机技术的发展，进程出现了很多弊端，一是由于进程是资源拥有者，创建、撤消与切换存在较大的时空开销，因此需要引入轻型进程；二是由于对称多处理机（SMP）出现，可以满足多个运行单位，而多个进程并行开销过大。因此在80年代，出现了能独立运行的基本单位——线程（Threads）。也就是说，现在，线程才是`资源（cpu）调度的基本单元`，它是一个程序内部的控制流程。线程是进程内部的更小的单元，它基本不占用系统资源。一个进程内的多个线程是为了协同工作来处理一件事情。
![Alt text](./1451812470845.png)


　　简单总结来说就是，进程是为了分配得到资源，然后由它里面的线程利用资源来处理事情。进程是一个壳子，实际干事的都是线程。（例如，我们的main函数作为主线程）。二者较为深入一点的总结：http://wangzhipeng0713.blog.163.com/blog/static/1944751652015522359459/

　　`单线程`:任何程序至少有一个线程，即使你没有主动地创建线程，程序从一开始执行就有一个默认的线程，被称为主线程，只有一个线程的程序称为单线程程序。如下面这一简单的代码，没有显示地创建一个线程，程序从main开始执行，main本身就是一个线程(主线程)，单个线程从头执行到尾。
```java
public static void main(String args[]) {
   System.out.println("输出从1到100的数:");
   for (int i = 0; i < 100; i ++) {
      System.out.println(i + 1);
   }
}
```

## 二、 线程的状态转换
- 创建：一个新的线程被创建，等待该线程被调用执行；
- 就绪：时间片已用完，此线程被强制暂停，等待下一个属于他的时间片到来；
- 运行：此线程正在执行，正在占用时间片；
- 阻塞：也叫等待状态，等待某一事件(如IO或另一个线程)执行完；
- 退出：一个线程完成任务或者其他终止条件发生，该线程终止进入退出状态，退出状态释放该线程所分配的资源。

![Alt text](./1451916369822.png)

　　这里需要注意的是，线程调用start()方法后，是进入“就绪状态”，而不是“运行状态”。也就是说，是线程告诉操作系统，我已准被调度所需要的一切事物，只有在被调度后线程才进入到运行状态。
## 三、 线程的创建和启动
#### 方式一：线程类实现Runnable接口
##### 定义线程类：
```java
/**
* 定义线程类（实现Runnable接口）
*
* @author wangzhipeng
*
*/
public class Runner1 implements Runnable {
	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			System.out.println("New Thread Runner1-->" + i);
		}
	}
}
```
##### 测试类：
```java
public class TestThread1 {
	// 1、main方法为主线程
	public static void main(String[] args) {
		// 2、启动第二个线程
		Runner1 runner1 = new Runner1();
		Thread thread = new Thread(runner1, "zhipeng");// 注意，Runner类实现了Runnable接口，启动线程时需要用一个Thread类将其包起来
		thread.start();// 调用start方法，使得线程进入“就绪”状态
		for (int i = 0; i < 100; i++) {
			System.out.println("【Main】 Thread-->" + i);
		}
	}
}
```
##### 运行结果：

 ![Alt text](./1451812912481.png)

#### 方式二：线程类继承Thread类并重写其run方法

##### 定义线程类：
```java
/**
* 定义线程类（继承Thread类）
*
* @author wangzhipeng
*
*/
public class Runner2 extends Thread {
	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			System.out.println("New Thread Runner1-->" + i);
		}
	}
}
```

#####测试类：
```java
public class TestThread2 {
	// main方法为主线程
	public static void main(String[] args) {
		// 1、线程类继承Thread类的start写法
		Runner2 runner2 = new Runner2();
		runner2.start();// 这里需要注意：因为Runner2已经是一个线程类了，所以不需要再对它进行包装，直接调用start即可
		// 2、线程类实现Runnable接口的start写法（需要用Thread类包装）
		// Thread thread = new Thread(runner1);
		// thread.start();
		for (int i = 0; i < 100; i++) {
			System.out.println("【Main】 Thread-->" + i);
		}
	}
}
```
##### 运行结果：
![Alt text](./1451813158864.png)
####  小结
　　Thread是JDK实现的对线程支持的类，Thread类本身实现了Runnable接口，所以Runnable是显示创建线程必须实现的接口; Runnable只有一个run方法，所以不管通过哪种方式创建线程，都必须实现run方法。
　　虽然两种方式都能达到相同的效果，但我们一般不采用继承的方式实现多线程，因为一旦继承了Thread类，你的类就无法再继承其它的类。而实现了Runnable接口后，你还可以实现其它接口或继承其它类。也就是说面向接口编程比较灵活。

## 四、 线程同步与Synchronized的用法
### 线程同步
线程与线程之间的关系，有几种:
`模型一`：简单的线程，多个线程同时执行，但各个线程处理的任务毫不相干，没有数据和资源的共享，不会出现争抢资源的情况。这种情况下不管有多少个线程同时执行都是安全的，其执行模型如下：
![Alt text](./1451913778922.png)

`模型二`：复杂的线程，多个线程共享相同的数据或资源，就会出现多个线程争抢一个资源的情况。这时就容易造成数据的非预期(错误)处理，是线程不安全的，其模型如下:
![Alt text](./1451913827431.png)

　　在出现模型二的情况时就要考虑线程的同步，确保线程的安全。Java中对线程同步的支持，最常见的方式是添加synchronized同步锁。

　　我们通过一个例子来看一下线程同步的应用。

　　买火车票是大家春节回家最为关注的事情，我们就简单模拟一下火车票的售票系统(为使程序简单，我们就抽出最简单的模型进行模拟)：有500张从北京到赣州的火车票，在8个窗口同时出售，保证系统的稳定性和数据的原子性。

![Alt text](./1451913864497.png)

线程类：
```java
/**
 * 模拟服务器的类
 */
class Service {
   private String ticketName;    //票名
   private int totalCount;       //总票数
   private int remaining;        //剩余票数
 
   public Service(String ticketName, int totalCount) {
      this.ticketName = ticketName;
      this.totalCount = totalCount;
      this.remaining = totalCount;
   }
 
   public synchronized int saleTicket(int ticketNum) {
      if (remaining > 0) {
         remaining -= ticketNum;
         try {        
			//暂停0.1秒，模拟真实系统中复杂计算所用的时间
            Thread.sleep(100);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
 
         if (remaining >= 0) {
            return remaining;
         } else {
            remaining += ticketNum;
            return -1;
         }
      }
      return -1;
   }
 
   public synchronized int getRemaining() {
      return remaining;
   }
 
   public String getTicketName() {
      return this.ticketName;
   }
 
}
 
/**
 * 售票程序
 */
class TicketSaler implements Runnable {
   private String name;
   private Service service;
 
   public TicketSaler(String windowName, Service service) {
      this.name = windowName;
      this.service = service;
   }
 
   @Override
   public void run() {
      while (service.getRemaining() > 0) {
         synchronized (this)
         {
            System.out.print(Thread.currentThread().getName() + "出售第" + service.getRemaining() + "张票，");
            int remaining = service.saleTicket(1);
            if (remaining >= 0) {
               System.out.println("出票成功!剩余" + remaining + "张票.");
            } else {
               System.out.println("出票失败！该票已售完。");
            }
         }
      }
   }
}
```

测试程序：
```java
/**
 * 测试类
 */
public class TicketingSystem {
   public static void main(String args[]) {
      Service service = new Service("北京-->赣州", 500);
      TicketSaler ticketSaler = new TicketSaler("售票程序", service);
      
	 //创建8个线程，以模拟8个窗口
     Thread threads[] = new Thread[8];
     for (int i = 0; i < threads.length; i++) {
         threads[i] = new Thread(ticketSaler, "窗口" + (i + 1));
         System.out.println("窗口" + (i + 1) + "开始出售 " + service.getTicketName() + " 的票...");
         threads[i].start();
     }
   }
}
``` 
结果如下：
```java
窗口1开始出售 北京–>赣州 的票… 
窗口2开始出售 北京–>赣州 的票… 
窗口3开始出售 北京–>赣州 的票… 
窗口4开始出售 北京–>赣州 的票… 
窗口5开始出售 北京–>赣州 的票… 
窗口6开始出售 北京–>赣州 的票… 
窗口7开始出售 北京–>赣州 的票… 
窗口8开始出售 北京–>赣州 的票… 
窗口1出售第500张票，出票成功!剩余499张票. 
窗口1出售第499张票，出票成功!剩余498张票. 
窗口6出售第498张票，出票成功!剩余497张票. 
窗口6出售第497张票，出票成功!剩余496张票. 
窗口1出售第496张票，出票成功!剩余495张票. 
窗口1出售第495张票，出票成功!剩余494张票. 
窗口1出售第494张票，出票成功!剩余493张票. 
窗口2出售第493张票，出票成功!剩余492张票. 
窗口2出售第492张票，出票成功!剩余491张票. 
窗口2出售第491张票，出票成功!剩余490张票. 
窗口2出售第490张票，出票成功!剩余489张票. 
窗口2出售第489张票，出票成功!剩余488张票. 
窗口2出售第488张票，出票成功!剩余487张票. 
窗口6出售第487张票，出票成功!剩余486张票. 
窗口6出售第486张票，出票成功!剩余485张票. 
窗口3出售第485张票，出票成功!剩余484张票. 
……
```
在上面的例子中，涉及到数据的更改的Service类saleTicket方法和TicketSaler类run方法都用了synchronized同步锁进行同步处理，以保证数据的准确性和原子性。

### Synchronized的用法

synchronized是Java中的关键字，是一种同步锁。它修饰的对象有以下几种： 
1. 修饰一个代码块，被修饰的代码块称为同步语句块，其作用的范围是大括号{}括起来的代码，作用的对象是调用这个代码块的对象； 
2. 修饰一个方法，被修饰的方法称为同步方法，其作用的范围是整个方法，作用的对象是调用这个方法的对象； 
3. 修改一个静态的方法，其作用的范围是整个静态方法，作用的对象是这个类的所有对象； 
4. 修改一个类，其作用的范围是synchronized后面括号括起来的部分，作用主的对象是这个类的所有对象。

#### 修饰一个代码块

一个线程访问一个对象中的synchronized(this)同步代码块时，其他试图访问该对象的线程将被阻塞。我们看下面一个例子：
【Demo1】：synchronized的用法
```java
/**
 * 同步线程
 */
class SyncThread implements Runnable {
   private static int count;

   public SyncThread() {
      count = 0;
   }

   public  void run() {
      synchronized(this) {
         for (int i = 0; i < 5; i++) {
            try {
               System.out.println(Thread.currentThread().getName() + ":" + (count++));
               Thread.sleep(100);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
      }
   }

   public int getCount() {
      return count;
   }
}
```
SyncThread的调用：
```java
SyncThread syncThread = new SyncThread();
Thread thread1 = new Thread(syncThread, "SyncThread1");
Thread thread2 = new Thread(syncThread, "SyncThread2");
thread1.start();
thread2.start();
```
结果如下：
```java
SyncThread1:0 
SyncThread1:1 
SyncThread1:2 
SyncThread1:3 
SyncThread1:4 
SyncThread2:5 
SyncThread2:6 
SyncThread2:7 
SyncThread2:8 
SyncThread2:9
```
当两个并发线程(thread1和thread2)访问同一个对象(syncThread)中的synchronized代码块时，在同一时刻只能有一个线程得到执行，另一个线程受阻塞，必须等待当前线程执行完这个代码块以后才能执行该代码块。Thread1和thread2是互斥的，因为在执行synchronized代码块时会锁定当前的对象，只有执行完该代码块才能释放该对象锁，下一个线程才能执行并锁定该对象。 

我们再把SyncThread的调用稍微改一下：
```
Thread thread1 = new Thread(new SyncThread(), "SyncThread1");
Thread thread2 = new Thread(new SyncThread(), "SyncThread2");
thread1.start();
thread2.start();
```
结果如下：
```java
SyncThread1:0 
SyncThread2:1 
SyncThread1:2 
SyncThread2:3 
SyncThread1:4 
SyncThread2:5 
SyncThread2:6 
SyncThread1:7 
SyncThread1:8 
SyncThread2:9
```
不是说一个线程执行synchronized代码块时其它的线程受阻塞吗？为什么上面的例子中thread1和thread2同时在执行。这是因为synchronized只锁定对象，每个对象只有一个锁（lock）与之相关联，而上面的代码等同于下面这段代码：
```java
SyncThread syncThread1 = new SyncThread();
SyncThread syncThread2 = new SyncThread();
Thread thread1 = new Thread(syncThread1, "SyncThread1");
Thread thread2 = new Thread(syncThread2, "SyncThread2");
thread1.start();
thread2.start();
```
这时创建了两个SyncThread的对象syncThread1和syncThread2，线程thread1执行的是syncThread1对象中的synchronized代码(run)，而线程thread2执行的是syncThread2对象中的synchronized代码(run)；我们知道synchronized锁定的是对象，这时会有两把锁分别锁定syncThread1对象和syncThread2对象，而这两把锁是互不干扰的，不形成互斥，所以两个线程可以同时执行。

2.当一个线程访问对象的一个synchronized(this)同步代码块时，另一个线程仍然可以访问该对象中的非synchronized(this)同步代码块。 
【Demo2】：多个线程访问synchronized和非synchronized代码块
```java
class Counter implements Runnable{
   private int count;

   public Counter() {
      count = 0;
   }

   public void countAdd() {
      synchronized(this) {
         for (int i = 0; i < 5; i ++) {
            try {
               System.out.println(Thread.currentThread().getName() + ":" + (count++));
               Thread.sleep(100);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
      }
   }

   //非synchronized代码块，未对count进行读写操作，所以可以不用synchronized
   public void printCount() {
      for (int i = 0; i < 5; i ++) {
         try {
            System.out.println(Thread.currentThread().getName() + " count:" + count);
            Thread.sleep(100);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

   public void run() {
      String threadName = Thread.currentThread().getName();
      if (threadName.equals("A")) {
         countAdd();
      } else if (threadName.equals("B")) {
         printCount();
      }
   }
}
```
调用代码:
```java
Counter counter = new Counter();
Thread thread1 = new Thread(counter, "A");
Thread thread2 = new Thread(counter, "B");
thread1.start();
thread2.start();
```
结果如下：
```
A:0 
B count:1 
A:1 
B count:2 
A:2 
B count:3 
A:3 
B count:4 
A:4 
B count:5
```
上面代码中countAdd是一个synchronized的，printCount是非synchronized的。从上面的结果中可以看出一个线程访问一个对象的synchronized代码块时，别的线程可以访问该对象的非synchronized代码块而不受阻塞。

指定要给某个对象加锁
【Demo3】:指定要给某个对象加锁
```java
/**
 * 银行账户类
 */
class Account {
   String name;
   float amount;

   public Account(String name, float amount) {
      this.name = name;
      this.amount = amount;
   }
   //存钱
   public  void deposit(float amt) {
      amount += amt;
      try {
         Thread.sleep(100);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
   //取钱
   public  void withdraw(float amt) {
      amount -= amt;
      try {
         Thread.sleep(100);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

   public float getBalance() {
      return amount;
   }
}
/**
 * 账户操作类
 */
class AccountOperator implements Runnable{
   private Account account;
   public AccountOperator(Account account) {
      this.account = account;
   }

   public void run() {
      synchronized (account) {
         account.deposit(500);
         account.withdraw(500);
         System.out.println(Thread.currentThread().getName() + ":" + account.getBalance());
      }
   }
}
```
调用代码:
```
Account account = new Account("zhang san", 10000.0f);
AccountOperator accountOperator = new AccountOperator(account);

final int THREAD_NUM = 5;
Thread threads[] = new Thread[THREAD_NUM];
for (int i = 0; i < THREAD_NUM; i ++) {
   threads[i] = new Thread(accountOperator, "Thread" + i);
   threads[i].start();
}
```
结果如下：
```
Thread3:10000.0 
Thread2:10000.0 
Thread1:10000.0 
Thread4:10000.0 
Thread0:10000.0
```
在AccountOperator 类中的run方法里，我们用synchronized 给account对象加了锁。这时，当一个线程访问account对象时，其他试图访问account对象的线程将会阻塞，直到该线程访问account对象结束。也就是说谁拿到那个锁谁就可以运行它所控制的那段代码。 
当有一个明确的对象作为锁时，就可以用类似下面这样的方式写程序。
```java
public void method3(SomeObject obj)
{
   //obj 锁定的对象
   synchronized(obj)
   {
      // todo
   }
}
```
当没有明确的对象作为锁，只是想让一段代码同步时，可以创建一个特殊的对象来充当锁：
```java
class Test implements Runnable
{
   private byte[] lock = new byte[0];  // 特殊的instance变量
   public void method()
   {
      synchronized(lock) {
         // todo 同步代码块
      }
   }

   public void run() {

   }
}
```
说明：零长度的byte数组对象创建起来将比任何对象都经济――查看编译后的字节码：生成零长度的byte[]对象只需3条操作码，而Object lock = new Object()则需要7行操作码。

#### 修饰一个方法

Synchronized修饰一个方法很简单，就是在方法的前面加synchronized，public synchronized void method(){//todo}; synchronized修饰方法和修饰一个代码块类似，只是作用范围不一样，修饰代码块是大括号括起来的范围，而修饰方法范围是整个函数。如将【Demo1】中的run方法改成如下的方式，实现的效果一样。

*【Demo4】：synchronized修饰一个方法
```java
public synchronized void run() {
   for (int i = 0; i < 5; i ++) {
      try {
         System.out.println(Thread.currentThread().getName() + ":" + (count++));
         Thread.sleep(100);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}
```
Synchronized作用于整个方法的写法。 
写法一：
```java
public synchronized void method()
{
   // todo
}
```
写法二：
```java
public void method()
{
   synchronized(this) {
      // todo
   }
}
```
写法一修饰的是一个方法，写法二修饰的是一个代码块，但写法一与写法二是等价的，都是锁定了整个方法时的内容。

在用synchronized修饰方法时要注意以下几点： 
1. synchronized关键字不能继承。 
虽然可以使用synchronized来定义方法，但synchronized并不属于方法定义的一部分，因此，synchronized关键字不能被继承。如果在父类中的某个方法使用了synchronized关键字，而在子类中覆盖了这个方法，在子类中的这个方法默认情况下并不是同步的，而必须显式地在子类的这个方法中加上synchronized关键字才可以。当然，还可以在子类方法中调用父类中相应的方法，这样虽然子类中的方法不是同步的，但子类调用了父类的同步方法，因此，子类的方法也就相当于同步了。这两种方式的例子代码如下： 
在子类方法中加上synchronized关键字
```java
class Parent {
   public synchronized void method() { }
}
class Child extends Parent {
   public synchronized void method() { }
}
```
在子类方法中调用父类的同步方法
```java
class Parent {
   public synchronized void method() {   }
}
class Child extends Parent {
   public void method() { super.method();   }
} 
```
在定义接口方法时不能使用synchronized关键字。
构造方法不能使用synchronized关键字，但可以使用synchronized代码块来进行同步。 
#### 修饰一个静态的方法

Synchronized也可修饰一个静态方法，用法如下：
```java
public synchronized static void method() {
   // todo
}
```
我们知道静态方法是属于类的而不属于对象的。同样的，synchronized修饰的静态方法锁定的是这个类的所有对象。我们对Demo1进行一些修改如下：

【Demo5】：synchronized修饰静态方法
```java
/**
 * 同步线程
 */
class SyncThread implements Runnable {
   private static int count;

   public SyncThread() {
      count = 0;
   }

   public synchronized static void method() {
      for (int i = 0; i < 5; i ++) {
         try {
            System.out.println(Thread.currentThread().getName() + ":" + (count++));
            Thread.sleep(100);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

   public synchronized void run() {
      method();
   }
}
```

调用代码:
```java
SyncThread syncThread1 = new SyncThread();
SyncThread syncThread2 = new SyncThread();
Thread thread1 = new Thread(syncThread1, "SyncThread1");
Thread thread2 = new Thread(syncThread2, "SyncThread2");
thread1.start();
thread2.start();
```
结果如下：
```java
SyncThread1:0 
SyncThread1:1 
SyncThread1:2 
SyncThread1:3 
SyncThread1:4 
SyncThread2:5 
SyncThread2:6 
SyncThread2:7 
SyncThread2:8 
SyncThread2:9
```
syncThread1和syncThread2是SyncThread的两个对象，但在thread1和thread2并发执行时却保持了线程同步。这是因为run中调用了静态方法method，而静态方法是属于类的，所以syncThread1和syncThread2相当于用了同一把锁。这与Demo1是不同的。

#### 修饰一个类

Synchronized还可作用于一个类，用法如下：
```java
class ClassName {
   public void method() {
      synchronized(ClassName.class) {
         // todo
      }
   }
}
```
我们把Demo5再作一些修改。 
【Demo6】:修饰一个类
```java
/**
 * 同步线程
 */
class SyncThread implements Runnable {
   private static int count;

   public SyncThread() {
      count = 0;
   }

   public static void method() {
      synchronized(SyncThread.class) {
         for (int i = 0; i < 5; i ++) {
            try {
               System.out.println(Thread.currentThread().getName() + ":" + (count++));
               Thread.sleep(100);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
      }
   }

   public synchronized void run() {
      method();
   }
}
```
其效果和【Demo4】是一样的，synchronized作用于一个类T时，是给这个类T加锁，T的所有对象用的是同一把锁。

总结：

A. 无论synchronized关键字加在方法上还是对象上，如果它作用的对象是非静态的，则它取得的锁是对象；如果synchronized作用的对象是一个静态方法或一个类，则它取得的锁是对类，该类所有的对象同一把锁。 
B. 每个对象只有一个锁（lock）与之相关联，谁拿到这个锁谁就可以运行它所控制的那段代码。 
C. 实现同步是要很大的系统开销作为代价的，甚至可能造成死锁，所以尽量避免无谓的同步控制。

 
## 五、 线程控制的基本方法
　　这里主要简单介绍sleep/join/yield/以及线程的优先级，至于wait与notify/notifyAll这一对重要的方法会在后面一篇文章，线程同步问题中详细介绍。

 ![Alt text](./1451814115855.png)
 
### 线程等待(wait、notify、notifyAll)
在Java的Object类中有三个final的方法允许线程之间进行资源对象锁的通信，他们分别是： wait(), notify() and notifyAll()。

>Wait：使当前的线程处于等待状态；

>Notify：唤醒其中一个等待线程；

>notifyAll：唤醒所有等待线程。
　　

调用这些方法的当前线程必须拥有此对象监视器，否则将会报java.lang.IllegalMonitorStateException exception异常。

- wait

>Object的wait方法有三个重载方法，其中一个方法wait() 是无限期(一直)等待，直到其它线程调用notify或notifyAll方法唤醒当前的线程；另外两个方法wait(long timeout) 和wait(long timeout, int nanos)允许传入 当前线程在被唤醒之前需要等待的时间，timeout为毫秒数，nanos为纳秒数。

- notify

>notify方法只唤醒一个等待（对象的）线程并使该线程开始执行。所以如果有多个线程等待一个对象，这个方法只会唤醒其中一个线程，选择哪个线程取决于操作系统对多线程管理的实现。

- notifyAll

>notifyAll 会唤醒所有等待(对象的)线程，尽管哪一个线程将会第一个处理取决于操作系统的实现。

这些方法可以使用于“生产者-消费者”问题，消费者是在队列中等待对象的线程，生产者是在队列中释放对象并通知其他线程的线程。

让我们来看一个多线程作用于同一个对象的例子，我们使用wait, notify and notifyAll方法。

#### 通过实例来理解

##### Message

一个java bean类，线程将会使用它并调用wait和notify方法。

Message.java
```java
package com.journaldev.concurrency;

public class Message {
    private String msg;

    public Message(String str){
        this.msg=str;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String str) {
        this.msg=str;
    }

}
```

##### Waiter

一个Waiter类，等待其它的线程调用notify方法以唤醒线程完成处理。注意等待线程必须通过加synchronized同步锁拥有Message对象的监视器。

Waiter.java
```java
package com.journaldev.concurrency;

public class Waiter implements Runnable{

    private Message msg;

    public Waiter(Message m){
        this.msg=m;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        synchronized (msg) {
            try{
                System.out.println(name+" waiting to get notified at time:"+System.currentTimeMillis());
                msg.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(name+" waiter thread got notified at time:"+System.currentTimeMillis());
            //process the message now
            System.out.println(name+" processed: "+msg.getMsg());
        }
    }

}
```
##### Notifier

一个Notifier类，处理Message对象并调用notify方法唤醒等待Message对象的线程。注意synchronized代码块被用于持有Message对象的监视器。

Notifier.java
```java
package com.journaldev.concurrency;

public class Notifier implements Runnable {

    private Message msg;

    public Notifier(Message msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name+" started");
        try {
            Thread.sleep(1000);
            synchronized (msg) {
                msg.setMsg(name+" Notifier work done");
                msg.notify();
                // msg.notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
```
##### WaitNotifyTest

一个测试类，交付创建多个等待线程和一个通过线程，并启动这些线程。

WaitNotifyTest.java
```java
package com.journaldev.concurrency;

public class WaitNotifyTest {

    public static void main(String[] args) {
        Message msg = new Message("process it");
        Waiter waiter = new Waiter(msg);
        new Thread(waiter,"waiter").start();

        Waiter waiter1 = new Waiter(msg);
        new Thread(waiter1, "waiter1").start();

        Notifier notifier = new Notifier(msg);
        new Thread(notifier, "notifier").start();
        System.out.println("All the threads are started");
    }

}
```
当我们调用以上的代码时可以看到以下的输出，但并没有结束(完成)，因为有两个线程等待同一个Message对象，但notify()方法只能唤醒一个线程，另一个线程仍然在等待被唤醒。

notify()
```java
waiter waiting to get notified at time:1356318734009
waiter1 waiting to get notified at time:1356318734010
All the threads are started
notifier started
waiter waiter thread got notified at time:1356318735011
waiter processed: notifier Notifying work done
如果我们注释掉Notifier类中的notify() 方法的调用，并打开notifyAll() 方法的调用，将会有以下的输出信息。
```
notifyAll()
```java
waiter waiting to get notified at time:1356318917118
waiter1 waiting to get notified at time:1356318917118
All the threads are started
notifier started
waiter1 waiter thread got notified at time:1356318918120
waiter1 processed: notifier Notifying work done
waiter waiter thread got notified at time:1356318918120
waiter processed: notifier Notifying work done
```
一旦notifyAll()方法唤醒所有的Waiter线程，程序将会执行完成并退出。
4.1 sleep方法
　　很简单很常用，是Thread类的静态方法：
 ![Alt text](./1451814168760.png)

　　示例程序
　　线程类：
```java
import java.util.Date;

/**
* 通过继承Thread类实现线程类
*
* @author wangzhipeng
*
*/
public class MyThread extends Thread {
	public void run() {
		/**
		* 每一秒钟输出一下当前日期
		*/
		while (true) {
			System.out.println("---->" + new Date());
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
```
　　Sleep测试类：
```java
public class TestInterrupt {
	public static void main(String[] argStrings) {
		MyThread myThread = new MyThread();
		myThread.start();// 启动第二个线程
		try {
			Thread.sleep(5000);// 主线程5秒钟后终止第二个线程
			myThread.interrupt();// 一般不用这种方式终止线程--比较粗暴
			// myThread.stop();//更加不用--更加粗暴
		} catch (InterruptedException e) {
			return;
		}
	}
}
```
　　这里需要注意“终止线程”的方式，上面提到了interrupt()与stop()两种方式都是比较粗暴的方式，即强行终止，一般不采用。而是在线程类中定义一个信号量，然后客户端通过给该信号量赋值来“温和”地控制线程的终止。例如给下面的线程类中的flag赋值false即可终止线程。
```java
public class MyThread extends Thread {
	boolean flag = true;// 定义信号量来控制线程的终止
	public void run() {
		/**
		* 每一秒钟输出一下当前日期
		*/
		while (flag) {
			System.out.println("---->" + new Date());
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
```
### 线程终止（interrupt）
在Java提供的线程支持类Thread中，有三个用于线程中断的方法：

>public void interrupt(); 中断线程。

>public static boolean interrupted(); 是一个静态方法，用于测试当前线程是否已经中断，并将线程的中断状态 清除。所以如果线程已经中断，调用两次interrupted，第二次时会返回false，因为第一次返回true后会清除中断状态。

>public boolean isInterrupted(); 测试线程是否已经中断。
示例程序
线程类：
```java
/**
 *打印线程
 */
 class Printer implements Runnable {
	public void run(){
		while(!Thread.currentThread().isInterrupted()){
			//如果当前线程未被中断，则执行打印工作
	         System.out.println(Thread.currentThread().getName() + "打印中… …");
	    }
	    if (Thread.currentThread().isInterrupted()) {
	        System.out.println("interrupted:" +  Thread.interrupted());       
		//返回当前线程的状态，并清除状态
         System.out.println("isInterrupted:" +  Thread.currentThread().isInterrupted());
	      }
	}
 }
```
测试类:
```java
Printer printer = new Printer();
Thread printerThread = new Thread(printer, "打印线程");
printerThread.start();
try {
   Thread.sleep(100);
} catch (InterruptedException e) {
   e.printStackTrace();
}
System.out.println("有紧急任务出现，需中断打印线程.");
System.out.println("中断前的状态：" + printerThread.isInterrupted());
printerThread.interrupt();       
// 中断打印线程
System.out.println("中断前的状态：" + printerThread.isInterrupted());
```
测试结果
```java
打印线程打印中… … 
… … 
打印线程打印中… … 
有紧急任务出现，需中断打印线程. 
打印线程打印中… … 
中断前的状态：false
打印线程打印中… … 
中断前的状态：true
interrupted:true
isInterrupted:false
```

### 线程合并
所谓合并，就是等待其它线程执行完，再执行当前线程，执行起来的效果就好像把其它线程合并到当前线程执行一样。其执行关系如下：
![Alt text](./1451911991551.png)
>public final void join()
等待该线程终止

>public final void join(long millis);
等待该线程终止的时间最长为 millis 毫秒。超时为 0 意味着要一直等下去。

>public final void join(long millis, int nanos)
等待该线程终止的时间最长为 millis 毫秒 + nanos 纳秒
　　

　　join()代表将第二线程合并到主线程，也就是将第二线程与主线程顺序执行，而不是并发执行。
　　join(5000) 代表前5秒钟将第二线程合并到主线程，5秒过后，第二线程与主线程并发执行。
　　示例程序
　　线程类：
```java
/**
* 定义线程类（继承Thread类）
*
* @author wangzhipeng
*
*/
public class Mythread2 extends Thread {
	Mythread2(String s) {
		super(s);
	}

	/**
	* 每一秒钟输出一下当前线程的名称
	*/
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.println("I am " + getName());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
```
　　join方法测试类：
```java
/**
* join方法测试
*
* @author wangzhipeng
*
*/
public class TestJoin {
	public static void main(String[] args) {
		Mythread2 mythread2 = new Mythread2("zhpeng");
		mythread2.start();// 启动第二线程
		try {
			mythread2.join();// 将第二线程合并到主线程，也就是将第二线程与主线程顺序执行，而不是并发执行
			// mythread2.join(5000);// 前5秒钟将第二线程合并到主线程，5秒过后，第二线程与主线程并发执行
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/**
		* 主线程：循环输出一句话
		*/
		for (int i = 0; i < 10; i++) {
			System.out.println("I am Main Thread");
		}
	}
}
```


　　运行结果：

 ![Alt text](./1451814582633.png)

　　4.3 yield方法
　　Thread类的静态方法。暂停当前正在执行的线程对象，并执行其他线程。也就是高风亮节，自己先暂停一下，让给别人先执行一下下。
　　示例代码
　　线程类：
```java
[java] view plaincopy

/**

* 定义线程类（继承Thread类）

*

* @author wangzhipeng

*

*/

public class Mythread3 extends Thread {

public Mythread3(String s) {

super(s);

}



public void run() {

for (int i = 0; i < 100; i++) {

System.out.println(getName() + ": " + i);

// 如果i能被10整除，则让出cpu执行主线程，也就是说输出结果的子线程i为0、5、10、15等时，下一个输出必定为主线程的输出（只要主线程没有执行完毕）

if (i % 5 == 0) {

yield();

}

}

}

}
```


　　yield方法测试类：
```java
[java] view plaincopy

/**

* yield方法测试类

*

* @author wangzhipeng

*

*/

public class TestYield {

public static void main(String[] argStrings) {

Mythread3 mythread3 = new Mythread3("------zhipeng");

mythread3.start();

// 主线程

for (int i = 0; i < 100; i++) {

System.out.println("-----MainThread " + i);

}

}

}
```


　　输出结果：

 ![Alt text](./1451814641020.png)


###线程的优先级Priority
　　线程优先级是指获得CPU资源的优先程序。优先级高的容易获得CPU资源，优先级底的较难获得CPU资源，表现出来的情况就是优先级越高执行的时间越多。
　　 Java中通过getPriority和setPriority方法获取和设置线程的优先级。Thread类提供了三个表示优先级的常量：MIN_PRIORITY优先级最低，为1；NORM_PRIORITY是正常的优先级；为5，MAX_PRIORITY优先级最高，为10。我们创建线程对象后，如果不显示的设置优先级的话，默认为5。
　　具有较高优先级的线程对程序更重要，并且应该在低优先级的线程之前分配处理器时间。然而，线程优先级不能保证线程执行的顺序，而且非常依赖于平台。


　　示例程序
　　定义两个线程类：
```java
public class T1 implements Runnable {
	@Override
	public void run() {
		for (int i = 0; i < 1000; i++) {
			System.out.println("----------T1");
		}
	}
}

public class T2 implements Runnable {
	@Override
	public void run() {
		for (int i = 0; i < 1000; i++) {
			System.out.println("T2");
		}
	}
}
```
　　测试类：
```java
public class TestPriority {
	public static void main(String[] args) {
	Thread thread1 = new Thread(new T1());
	Thread thread2 = new Thread(new T2());
	thread1.setPriority(Thread.NORM_PRIORITY + 5);// 给thread1线程的优先级加5，这样它被调度的机会就会比thread2大很多
	thread1.start();
	thread2.start();
	}
}
```


　　运行结果：
![Alt text](./1451814679993.png)

 


五、 总结


进程是系统资源分配的基本单元，它是程序的运行示例，可以独立存在。线程是cpu调度的基本单元，是一个程序内部的控制流程，不能独立存在，必须依附于进程。一个进程包含多个线程，进程是为了得到系统资源，但实际上运作这些系统资源的都是线程。
通过对多线程的使用，可以编写出非常高效的程序。不过请注意，如果你创建太多的线程，程序执行的效率实际上是降低了，而不是提升了。因为，上下文的切换开销也很重要，如果你创建了太多的线程，CPU花费在上下文的切换的时间将多于执行程序的时间！


本文来自于CDSN博主：王志鹏


## 六、常见面试问题
### Java多线程面试问题
多线程和并发问题是Java技术面试中面试官比较喜欢问的问题之一。在这里，从面试的角度列出了大部分重要的问题，但是你仍然应该牢固的掌握java多线程基础知识来对应日后碰到的问题。

#### 1. 进程和线程之间有什么不同？

一个进程是一个独立(self contained)的运行环境，它可以被看作一个程序或者一个应用。而线程是在进程中执行的一个任务。Java运行环境是一个包含了不同的类和程序的单一进程。线程可以被称为轻量级进程。线程需要较少的资源来创建和驻留在进程中，并且可以共享进程中的资源。

#### 2. 多线程编程的好处是什么？

在多线程程序中，多个线程被并发的执行以提高程序的效率，CPU不会因为某个线程需要等待资源而进入空闲状态。多个线程共享堆内存(heap memory)，因此创建多个线程去执行一些任务会比创建多个进程更好。举个例子，Servlets比CGI更好，是因为Servlets支持多线程而CGI不支持。

#### 3. 用户线程和守护线程有什么区别？

当我们在Java程序中创建一个线程，它就被称为用户线程。一个守护线程是在后台执行并且不会阻止JVM终止的线程。当没有用户线程在运行的时候，JVM关闭程序并且退出。一个守护线程创建的子线程依然是守护线程。

#### 4. 我们如何创建一个线程？

有两种创建线程的方法：一是实现Runnable接口，然后将它传递给Thread的构造函数，创建一个Thread对象；二是直接继承Thread类。若想了解更多可以阅读这篇关于如何在Java中创建线程的文章。

#### 5. 有哪些不同的线程生命周期？

当我们在Java程序中新建一个线程时，它的状态是New。当我们调用线程的start()方法时，状态被改变为Runnable。线程调度器会为Runnable线程池中的线程分配CPU时间并且讲它们的状态改变为Running。其他的线程状态还有Waiting，Blocked 和Dead。读这篇文章可以了解更多关于线程生命周期的知识。

#### 6. 可以直接调用Thread类的run()方法么？

当然可以，但是如果我们调用了Thread的run()方法，它的行为就会和普通的方法一样，为了在新的线程中执行我们的代码，必须使用Thread.start()方法。

#### 7. 如何让正在运行的线程暂停一段时间？

我们可以使用Thread类的Sleep()方法让线程暂停一段时间。需要注意的是，这并不会让线程终止，一旦从休眠中唤醒线程，线程的状态将会被改变为Runnable，并且根据线程调度，它将得到执行。

#### 8. 你对线程优先级的理解是什么？

每一个线程都是有优先级的，一般来说，高优先级的线程在运行时会具有优先权，但这依赖于线程调度的实现，这个实现是和操作系统相关的(OS dependent)。我们可以定义线程的优先级，但是这并不能保证高优先级的线程会在低优先级的线程前执行。线程优先级是一个int变量(从1-10)，1代表最低优先级，10代表最高优先级。

#### 9. 什么是线程调度器(Thread Scheduler)和时间分片(Time Slicing)？

线程调度器是一个操作系统服务，它负责为Runnable状态的线程分配CPU时间。一旦我们创建一个线程并启动它，它的执行便依赖于线程调度器的实现。时间分片是指将可用的CPU时间分配给可用的Runnable线程的过程。分配CPU时间可以基于线程优先级或者线程等待的时间。线程调度并不受到Java虚拟机控制，所以由应用程序来控制它是更好的选择（也就是说不要让你的程序依赖于线程的优先级）。

#### 10. 在多线程中，什么是上下文切换(context-switching)？

上下文切换是存储和恢复CPU状态的过程，它使得线程执行能够从中断点恢复执行。上下文切换是多任务操作系统和多线程环境的基本特征。

#### 11. 你如何确保main()方法所在的线程是Java程序最后结束的线程？

我们可以使用Thread类的joint()方法来确保所有程序创建的线程在main()方法退出前结束。这里有一篇文章关于Thread类的joint()方法。

#### 12.线程之间是如何通信的？

当线程间是可以共享资源时，线程间通信是协调它们的重要的手段。Object类中wait()\notify()\notifyAll()方法可以用于线程间通信关于资源的锁的状态。点击这里有更多关于线程wait, notify和notifyAll.

#### 13.为什么线程通信的方法wait(), notify()和notifyAll()被定义在Object类里？

Java的每个对象中都有一个锁(monitor，也可以成为监视器) 并且wait()，notify()等方法用于等待对象的锁或者通知其他线程对象的监视器可用。在Java的线程中并没有可供任何对象使用的锁和同步器。这就是为什么这些方法是Object类的一部分，这样Java的每一个类都有用于线程间通信的基本方法

#### 14. 为什么wait(), notify()和notifyAll()必须在同步方法或者同步块中被调用？

当一个线程需要调用对象的wait()方法的时候，这个线程必须拥有该对象的锁，接着它就会释放这个对象锁并进入等待状态直到其他线程调用这个对象上的notify()方法。同样的，当一个线程需要调用对象的notify()方法时，它会释放这个对象的锁，以便其他在等待的线程就可以得到这个对象锁。由于所有的这些方法都需要线程持有对象的锁，这样就只能通过同步来实现，所以他们只能在同步方法或者同步块中被调用。

#### 15. 为什么Thread类的sleep()和yield()方法是静态的？

Thread类的sleep()和yield()方法将在当前正在执行的线程上运行。所以在其他处于等待状态的线程上调用这些方法是没有意义的。这就是为什么这些方法是静态的。它们可以在当前正在执行的线程中工作，并避免程序员错误的认为可以在其他非运行线程调用这些方法。

#### 16.如何确保线程安全？

在Java中可以有很多方法来保证线程安全——同步，使用原子类(atomic concurrent classes)，实现并发锁，使用Volatile关键字，使用不变类和线程安全类。在线程安全教程中，你可以学到更多。

#### 17. volatile关键字在Java中有什么作用？

当我们使用volatile关键字去修饰变量的时候，所以线程都会直接读取该变量并且不缓存它。这就确保了线程读取到的变量是同内存中是一致的。

#### 18. 同步方法和同步块，哪个是更好的选择？

同步块是更好的选择，因为它不会锁住整个对象（当然你也可以让它锁住整个对象）。同步方法会锁住整个对象，哪怕这个类中有多个不相关联的同步块，这通常会导致他们停止执行并需要等待获得这个对象上的锁。

#### 19.如何创建守护线程？

使用Thread类的setDaemon(true)方法可以将线程设置为守护线程，需要注意的是，需要在调用start()方法前调用这个方法，否则会抛出IllegalThreadStateException异常。

#### 20. 什么是ThreadLocal?

ThreadLocal用于创建线程的本地变量，我们知道一个对象的所有线程会共享它的全局变量，所以这些变量不是线程安全的，我们可以使用同步技术。但是当我们不想使用同步的时候，我们可以选择ThreadLocal变量。

每个线程都会拥有他们自己的Thread变量，它们可以使用get()\set()方法去获取他们的默认值或者在线程内部改变他们的值。ThreadLocal实例通常是希望它们同线程状态关联起来是private static属性。在ThreadLocal例子这篇文章中你可以看到一个关于ThreadLocal的小程序。

#### 21. 什么是Thread Group？为什么建议使用它？

ThreadGroup是一个类，它的目的是提供关于线程组的信息。

ThreadGroup API比较薄弱，它并没有比Thread提供了更多的功能。它有两个主要的功能：一是获取线程组中处于活跃状态线程的列表；二是设置为线程设置未捕获异常处理器(ncaught exception handler)。但在Java 1.5中Thread类也添加了setUncaughtExceptionHandler(UncaughtExceptionHandler eh) 方法，所以ThreadGroup是已经过时的，不建议继续使用。

t1.setUncaughtExceptionHandler(new UncaughtExceptionHandler(){

@Override
public void uncaughtException(Thread t, Throwable e) {
System.out.println("exception occured:"+e.getMessage());
}

});
#### 22. 什么是Java线程转储(Thread Dump)，如何得到它？

线程转储是一个JVM活动线程的列表，它对于分析系统瓶颈和死锁非常有用。有很多方法可以获取线程转储——使用Profiler，Kill -3命令，jstack工具等等。我更喜欢jstack工具，因为它容易使用并且是JDK自带的。由于它是一个基于终端的工具，所以我们可以编写一些脚本去定时的产生线程转储以待分析。读这篇文档可以了解更多关于产生线程转储的知识。

#### 23. 什么是死锁(Deadlock)？如何分析和避免死锁？

死锁是指两个以上的线程永远阻塞的情况，这种情况产生至少需要两个以上的线程和两个以上的资源。

分析死锁，我们需要查看Java应用程序的线程转储。我们需要找出那些状态为BLOCKED的线程和他们等待的资源。每个资源都有一个唯一的id，用这个id我们可以找出哪些线程已经拥有了它的对象锁。

避免嵌套锁，只在需要的地方使用锁和避免无限期等待是避免死锁的通常办法，阅读这篇文章去学习如何分析死锁。

#### 24. 什么是Java Timer类？如何创建一个有特定时间间隔的任务？

java.util.Timer是一个工具类，可以用于安排一个线程在未来的某个特定时间执行。Timer类可以用安排一次性任务或者周期任务。

java.util.TimerTask是一个实现了Runnable接口的抽象类，我们需要去继承这个类来创建我们自己的定时任务并使用Timer去安排它的执行。

这里有关于java Timer的例子。

#### 25. 什么是线程池？如何创建一个Java线程池？

一个线程池管理了一组工作线程，同时它还包括了一个用于放置等待执行的任务的队列。

java.util.concurrent.Executors提供了一个 java.util.concurrent.Executor接口的实现用于创建线程池。线程池例子展现了如何创建和使用线程池，或者阅读ScheduledThreadPoolExecutor例子，了解如何创建一个周期任务。

### Java并发面试问题

#### 1. 什么是原子操作？在Java Concurrency API中有哪些原子类(atomic classes)？

原子操作是指一个不受其他操作影响的操作任务单元。原子操作是在多线程环境下避免数据不一致必须的手段。

int++并不是一个原子操作，所以当一个线程读取它的值并加1时，另外一个线程有可能会读到之前的值，这就会引发错误。

为了解决这个问题，必须保证增加操作是原子的，在JDK1.5之前我们可以使用同步技术来做到这一点。到JDK1.5，java.util.concurrent.atomic包提供了int和long类型的装类，它们可以自动的保证对于他们的操作是原子的并且不需要使用同步。可以阅读这篇文章来了解Java的atomic类。

#### 2. Java Concurrency API中的Lock接口(Lock interface)是什么？对比同步它有什么优势？

Lock接口比同步方法和同步块提供了更具扩展性的锁操作。他们允许更灵活的结构，可以具有完全不同的性质，并且可以支持多个相关类的条件对象。

它的优势有：

可以使锁更公平
可以使线程在等待锁的时候响应中断
可以让线程尝试获取锁，并在无法获取锁的时候立即返回或者等待一段时间
可以在不同的范围，以不同的顺序获取和释放锁
阅读更多关于锁的例子

#### 3. 什么是Executors框架？

Executor框架同java.util.concurrent.Executor 接口在Java 5中被引入。Executor框架是一个根据一组执行策略调用，调度，执行和控制的异步任务的框架。

无限制的创建线程会引起应用程序内存溢出。所以创建一个线程池是个更好的的解决方案，因为可以限制线程的数量并且可以回收再利用这些线程。利用Executors框架可以非常方便的创建一个线程池，阅读这篇文章可以了解如何使用Executor框架创建一个线程池。

#### 4. 什么是阻塞队列？如何使用阻塞队列来实现生产者-消费者模型？

java.util.concurrent.BlockingQueue的特性是：当队列是空的时，从队列中获取或删除元素的操作将会被阻塞，或者当队列是满时，往队列里添加元素的操作会被阻塞。

阻塞队列不接受空值，当你尝试向队列中添加空值的时候，它会抛出NullPointerException。

阻塞队列的实现都是线程安全的，所有的查询方法都是原子的并且使用了内部锁或者其他形式的并发控制。

BlockingQueue 接口是java collections框架的一部分，它主要用于实现生产者-消费者问题。

阅读这篇文章了解如何使用阻塞队列实现生产者-消费者问题。

#### 5. 什么是Callable和Future?

Java 5在concurrency包中引入了java.util.concurrent.Callable 接口，它和Runnable接口很相似，但它可以返回一个对象或者抛出一个异常。

Callable接口使用泛型去定义它的返回类型。Executors类提供了一些有用的方法去在线程池中执行Callable内的任务。由于Callable任务是并行的，我们必须等待它返回的结果。java.util.concurrent.Future对象为我们解决了这个问题。在线程池提交Callable任务后返回了一个Future对象，使用它我们可以知道Callable任务的状态和得到Callable返回的执行结果。Future提供了get()方法让我们可以等待Callable结束并获取它的执行结果。

阅读这篇文章了解更多关于Callable，Future的例子。

#### 6. 什么是FutureTask?

FutureTask是Future的一个基础实现，我们可以将它同Executors使用处理异步任务。通常我们不需要使用FutureTask类，单当我们打算重写Future接口的一些方法并保持原来基础的实现是，它就变得非常有用。我们可以仅仅继承于它并重写我们需要的方法。阅读Java FutureTask例子，学习如何使用它。

#### 7.什么是并发容器的实现？

Java集合类都是快速失败的，这就意味着当集合被改变且一个线程在使用迭代器遍历集合的时候，迭代器的next()方法将抛出ConcurrentModificationException异常。

并发容器支持并发的遍历和并发的更新。

主要的类有ConcurrentHashMap, CopyOnWriteArrayList 和CopyOnWriteArraySet，阅读这篇文章了解如何避免ConcurrentModificationException。

#### 8. Executors类是什么？

Executors为Executor，ExecutorService，ScheduledExecutorService，ThreadFactory和Callable类提供了一些工具方法。

Executors可以用于方便的创建线程池。





