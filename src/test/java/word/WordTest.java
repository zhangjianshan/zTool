package word;

import cn.hutool.core.collection.CollectionUtil;
import com.ztool.word.tools.WordTool;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;

/**
 * @author zhangjianshan on 2023-02-16
 */
public class WordTest {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        XWPFDocument document = new XWPFDocument();
        WordTool.addCustomHeadingStyle(document, WordTool.TITLE_FIRST, 1);
        WordTool.addCustomHeadingStyle(document, WordTool.TITLE_SECOND, 2);
        WordTool.addCustomHeadingStyle(document, WordTool.TITLE_THIRD, 3);
        WordTool.addCustomHeadingStyle(document, WordTool.TITLE_FOURTH, 4);
        //创建标题
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setPageBreak(true);
        paragraph.setStyle("一、级标题");
        XWPFRun run = paragraph.createRun();
        run.setFontFamily("黑体");
        run.setFontSize(22);
        run.setText("一级标题");

        XWPFParagraph campParagraph = document.createParagraph();
        campParagraph.setFirstLineIndent(600);
        campParagraph.setStyle("二、级标题");
        XWPFRun campRun = campParagraph.createRun();
        campRun.setFontFamily("黑体");
        campRun.setText("二级标题");
        campRun.setFontSize(14);

        WordTool.createText(document, "\t\t223", ParagraphAlignment.LEFT);
        WordTool.createText(document, "224");
        //创建表格
        XWPFTable table = WordTool.createTable(document, 10, 2);
        int row = 0;
        //填充标题
        XWPFTableRow firstRow = table.getRow(row);
        firstRow.getCell(0).setText("专题报1告名称");
        firstRow.getCell(1).setText("13");
        //插入图片
        WordTool.createPicture(document, "D://123.png");
        //插入饼状图
        WordTool.createPieChart(document, CollectionUtil.newArrayList("甲", "乙")
                , CollectionUtil.newArrayList(1, 2), "股份图", "投资比例");
        WordTool.write(document, "d://123.docx");
    }
}
