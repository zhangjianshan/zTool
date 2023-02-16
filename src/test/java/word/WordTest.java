package word;

import com.ztool.word.tools.WordTool;
import org.apache.poi.xwpf.usermodel.*;

/**
 * @author zhangjianshan on 2023-02-16
 */
public class WordTest {
    public static void main(String[] args) {
        XWPFDocument document = new XWPFDocument();
        WordTool.addCustomHeadingStyle(document, WordTool.TITLE_FIRST, 1);
        WordTool.addCustomHeadingStyle(document, WordTool.TITLE_SECOND, 2);
        WordTool.addCustomHeadingStyle(document, WordTool.TITLE_THIRD, 3);
        WordTool.addCustomHeadingStyle(document, WordTool.TITLE_FOURTH, 4);
        //��������
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setPageBreak(true);
        paragraph.setStyle("һ��������");
        XWPFRun run = paragraph.createRun();
        run.setFontFamily("����");
        run.setFontSize(22);
        run.setText("һ������");

        XWPFParagraph campParagraph = document.createParagraph();
        campParagraph.setFirstLineIndent(600);
        campParagraph.setStyle("����������");
        XWPFRun campRun = campParagraph.createRun();
        campRun.setFontFamily("����");
        campRun.setText("��������");
        campRun.setFontSize(14);

        WordTool.createText(document, "\t\t223", ParagraphAlignment.LEFT);
        WordTool.createText(document, "224");
        //�������
        XWPFTable table = WordTool.createTable(document, 10, 2);
        int row = 0;
        //������
        XWPFTableRow firstRow = table.getRow(row);
        firstRow.getCell(0).setText("ר�ⱨ������");
        firstRow.getCell(1).setText("13");
        //����ͼƬ
        WordTool.createPicture(document, "D://123.png");
        WordTool.write(document, "D://123.docx");
    }
}
