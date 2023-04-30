package excel;

import cn.hutool.core.collection.CollectionUtil;
import com.ztool.excel.select.SimpleSelectTool;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * @author zhangjianshan on 2023-04-30
 */
public class SimpleSelectTest {
    public static void main(String[] args) {
        Workbook book = new XSSFWorkbook();
        List<String> selectDataList = CollectionUtil.newArrayList("蜀国", "魏国", "吴国");
        List<String> selectDataElseList = CollectionUtil.newArrayList("中国", "俄罗斯", "朝鲜");

        new SimpleSelectTool(book)
                .createSheet("下拉框模板")
                .createSelectDateList(selectDataList)
                .effectivenessSelectData(0)
                .createSelectDateList(selectDataElseList)
                .createFirstRow(0)
                .sheetSelectData(1)
                .createSheet("其他模板")
                .createHiddenName("设置其他隐藏页")
                .sheetSelectData(0)
                .writeFile();
    }
}
