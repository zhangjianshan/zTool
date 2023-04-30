package excel;

import cn.hutool.core.collection.CollectionUtil;
import com.ztool.excel.select.SimpleSelectTool;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * @author zhangjianshan on 2023-04-30
 */
public class SimpleSelectTest {
    public static void main(String[] args) {
        Workbook book = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) book.createSheet("下拉框模板");
        List<String> selectDateList = CollectionUtil.newArrayList("蜀国", "魏国", "吴国");
        //基于数据有效性序列设置下拉(下拉字符有限制)
        SimpleSelectTool.effectivenessSelectData(sheet, selectDateList, 0);

        //基于sheet
        String dataSheet = SimpleSelectTool.createHiddenSheet(book, "dataSheet", selectDateList);
        SimpleSelectTool.sheetSelectData(sheet, dataSheet, 1);
        SimpleSelectTool.writeFile(book);
    }
}
