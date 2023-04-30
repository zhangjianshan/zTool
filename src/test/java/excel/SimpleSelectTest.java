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
        XSSFSheet sheet = (XSSFSheet) book.createSheet("������ģ��");
        List<String> selectDateList = CollectionUtil.newArrayList("���", "κ��", "���");
        //����������Ч��������������(�����ַ�������)
        SimpleSelectTool.effectivenessSelectData(sheet, selectDateList, 0);

        //����sheet
        String dataSheet = SimpleSelectTool.createHiddenSheet(book, "dataSheet", selectDateList);
        SimpleSelectTool.sheetSelectData(sheet, dataSheet, 1);
        SimpleSelectTool.writeFile(book);
    }
}
