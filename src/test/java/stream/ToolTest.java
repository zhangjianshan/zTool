package stream;

import cn.hutool.core.collection.CollectionUtil;
import com.ztool.demo.stream.PersonVO;
import com.ztool.stream.StreamTool;

import java.util.List;
import java.util.Map;

public class ToolTest {
    public static void main(String[] args) {
        List<PersonVO> personVOS = CollectionUtil.newArrayList(
                new PersonVO("张三", 1),
                new PersonVO("张四", 11),
                new PersonVO("张五", 12),
                new PersonVO("张六", 14),
                new PersonVO("张七", 15),
                new PersonVO("张七", 15)
        );
        //取某个属性
        List<String> nameList = StreamTool.toList(personVOS, PersonVO::getName);
        System.out.println(nameList);
        List<Integer> ageList = StreamTool.toList(personVOS, PersonVO::getAge, Integer::valueOf);
        System.out.println(ageList);
        //rt.jar底层如果key相同会报错，修改了逻辑，用此方法不会报错
        //方法1：Exception in thread "main" java.lang.IllegalStateException: Duplicate key PersonVO(name=张七, age=15)
        //Map<Integer, PersonVO> personByAgeMap = personVOS.stream().collect(Collectors.toMap(PersonVO::getAge, Function.identity()));
        //方法2：不会报错
        //Map<Integer, PersonVO> personByAgeMap = personVOS.stream().collect(Collectors.toMap(PersonVO::getAge, Function.identity(), (oldValue, newValue) -> newValue));
        //方法3:不会报错
        Map<Integer, PersonVO> personByAgeMap = StreamTool.toMap(personVOS, PersonVO::getAge);
        System.out.println(personByAgeMap);
    }
}
