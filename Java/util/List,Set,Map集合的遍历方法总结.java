import java.util.*;

/**
 * Created by dongxiaoxia on 2016/1/7.
 *
 * List,Set,Map集合的遍历方法总结
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("===================List的遍历==========");
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd");
        //1.通过增强for循环遍历
        for (String s : list) {
            System.out.println("通过增强for循环遍历:"+s);
        }
        //2.通过下标遍历
        for (int i = 0; i < list.size(); i++) {
            System.out.println("通过下标遍历:" + list.get(i));
        }
        //3.通过Iterator迭代器遍历
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println("通过Iterator迭代器遍历"+iterator.next());
        }
        System.out.println("===================Set的遍历==========");
        Set<String> set = new HashSet(list);
        //1.通过增强for循环遍历
        for (String s : set) {
            System.out.println("通过增强for循环遍历:"+s);
        }
        //2.通过下标遍历
        for (int i = 0; i < list.size(); i++) {
            System.out.println("通过下标遍历:" + list.get(i));
        }
        //3.通过Iterator迭代器遍历
        Iterator<String> setIt = list.iterator();
        while (setIt.hasNext()) {
            System.out.println("通过Iterator迭代器遍历"+setIt.next());
        }
        System.out.println("===================Map的遍历==========");
        Map<Integer, String> map = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            map.put(i, list.get(i));
        }
        //1.通过entry遍历
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println("通过entry遍历:"+entry.getKey()+":"+entry.getValue());
        }
        //2.通过keySet遍历
        Set<Integer> keySet = map.keySet();
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            int key = (int) it.next();
            System.out.println("通过keySet遍历:"+key+":"+map.get(key));
        }
    }
}
