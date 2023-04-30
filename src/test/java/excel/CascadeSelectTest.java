package excel;

import cn.hutool.core.collection.CollectionUtil;
import com.ztool.excel.select.CascadeSelectTool;
import com.ztool.excel.select.SimpleSelectTool;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjianshan on 2023-04-30
 */
public class CascadeSelectTest {
    public static void main(String[] args) {
        //级联下
        Map<String, List<String>> areaList = new LinkedHashMap<>();
        areaList.put("势力", CollectionUtil.newArrayList("蜀国", "魏国", "吴国"));
        areaList.put("蜀国", CollectionUtil.newArrayList("刘备", "关羽", "张飞"));
        areaList.put("魏国", CollectionUtil.newArrayList("曹操", "许褚", "典韦"));
        areaList.put("吴国", CollectionUtil.newArrayList("孙权", "黄盖", "周瑜"));
        areaList.put("关羽", CollectionUtil.newArrayList("关兴"));
        areaList.put("关兴", CollectionUtil.newArrayList("关某"));
        //下拉框区域
        List<Integer> selectColList = CollectionUtil.newArrayList(0, 1, 2, 3);
        XSSFWorkbook book = new XSSFWorkbook();
        book.createSheet("下拉框模板");
        CascadeSelectTool.setCascadeDropDownBox(book, "下拉框模板", "data", "势力", areaList, selectColList);
        SimpleSelectTool.writeFile(book);
    }
}
