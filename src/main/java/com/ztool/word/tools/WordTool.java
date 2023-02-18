package com.ztool.word.tools;


import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.SneakyThrows;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * @author zhangjianshan1992
 */
public class WordTool {
    public static final String TITLE_FIRST = "一、级标题";
    public static final String TITLE_SECOND = "二、级标题";
    public static final String TITLE_THIRD = "三、级标题";
    public static final String TITLE_FOURTH = "四、级标题";

    @SneakyThrows
    public static void write(XWPFDocument document, String path) {
        FileOutputStream fileOut = new FileOutputStream(path);
        document.write(fileOut);
        fileOut.close();
    }

    public static void createTitle(XWPFDocument document, String style, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setPageBreak(false);
        paragraph.setStyle(style);
        XWPFRun run = paragraph.createRun();
        run.setFontFamily("黑体");
        run.setFontSize(22);
        run.setText(text);
    }

    /**
     * 创建文本
     *
     * @param document 文档
     * @param txt      内容
     */
    public static void createText(XWPFDocument document, String txt) {
        XWPFParagraph remark = document.createParagraph();
        remark.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun remarkRun = remark.createRun();
        remarkRun.setFontFamily("宋体");
        remarkRun.setBold(true);
        remarkRun.setFontSize(20);
        remarkRun.setText(txt);
    }

    public static void createText(XWPFDocument document, String txt, boolean bold, int fontSize, ParagraphAlignment paragraphAlignment) {
        XWPFParagraph remark = document.createParagraph();
        remark.setAlignment(paragraphAlignment);
        XWPFRun remarkRun = remark.createRun();
        remarkRun.setFontFamily("宋体");
        remarkRun.setBold(bold);
        remarkRun.setFontSize(fontSize);
        remarkRun.setText(txt);
    }

    public static void createText(XWPFDocument document, String txt, ParagraphAlignment paragraphAlignment) {
        XWPFParagraph remark = document.createParagraph();
        remark.setAlignment(paragraphAlignment);
        XWPFRun remarkRun = remark.createRun();
        remarkRun.setFontFamily("宋体");
        remarkRun.setBold(false);
        remarkRun.setFontSize(20);
        remarkRun.setText(txt);
    }

    /**
     * 创建表格
     *
     * @param document doc
     * @param rows     行数
     * @param cols     列数
     * @return table
     */
    public static XWPFTable createTable(XWPFDocument document, int rows, int cols) {
        XWPFTable table = document.createTable(rows, cols);
        table.setCellMargins(200, 200, 200, 200);
        CTTbl bl = table.getCTTbl();
        //设置表格总宽度
        CTTblPr pr = bl.getTblPr() == null ? bl.addNewTblPr() : bl.getTblPr();
        CTTblWidth tblWidth = pr.addNewTblW();
        tblWidth.setW(BigInteger.valueOf(9000));
        tblWidth.setType(STTblWidth.DXA);
        return table;
    }

    /**
     * 跨行合并
     *
     * @param table   表格
     * @param col     列
     * @param fromRow 开始行
     * @param toRow   结束行
     */
    public static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if (rowIndex == fromRow) {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }


    /**
     * 跨列合并单元格
     *
     * @param table    表格
     * @param row      列
     * @param fromCell 开始行
     * @param toCell   结束行
     */
    public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
            if (cellIndex == fromCell) {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 插入图片
     *
     * @param document doc
     * @param path     path
     */
    @SneakyThrows
    public static void createPicture(XWPFDocument document, String path) {
        File pictureFile = FileUtil.file(path);
        if (!pictureFile.exists()) {
            return;
        }
        InputStream inputStream = new FileInputStream(pictureFile);
        XWPFParagraph paragraph = document.createParagraph();
        //居中
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.createRun().addPicture(inputStream, XWPFDocument.PICTURE_TYPE_PNG, "picture", Units.toEMU(200), Units.toEMU(200));
        closeStream(inputStream);
    }

    /**
     * 关闭输入流
     *
     * @param closeAbles
     */
    public static void closeStream(Closeable... closeAbles) {
        for (Closeable c : closeAbles) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {
        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);
        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);
        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));
        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);
        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);
        // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull);
        // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);
        XWPFStyle style = new XWPFStyle(ctStyle);
        // is a null op if already defined
        XWPFStyles styles = docxDocument.createStyles();
        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);

    }

    public static String nullToEmpty(Object obj) {
        if (Objects.isNull(obj)) {
            return StringUtils.EMPTY;
        }
        return String.valueOf(obj);
    }

    /**
     * 判断文本中时候包含$
     *
     * @param text 文本
     * @return 包含返回true, 不包含返回false
     */
    public static boolean checkText(String text) {
        boolean check = false;
        if (text.indexOf("$") != -1) {
            check = true;
        }
        return check;
    }

    /**
     * 匹配传入信息集合与模板
     *
     * @param value   模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static String changeValue(String value, Map<String, String> textMap) {
        Set<Map.Entry<String, String>> textSets = textMap.entrySet();
        for (Map.Entry<String, String> textSet : textSets) {
            //匹配模板与替换值 格式${key}
            String key = "${" + textSet.getKey() + "}";
            if (value.indexOf(key) != -1) {
                value = textSet.getValue();
            }
        }
        //模板未匹配到区域替换为空
        if (checkText(value)) {
            value = "";
        }
        return value;
    }

    /**
     * 遍历表格,并替换模板
     *
     * @param rows    表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(List<XWPFTableRow> rows, Map<String, String> textMap) {
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if (checkText(cell.getText())) {
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            run.setText(changeValue(run.toString(), textMap), 0);
                        }
                    }
                }
            }
        }
    }

    static CellReference setTitleInDataSheet(XWPFChart chart, String title, int column) throws Exception {
        XSSFWorkbook workbook = chart.getWorkbook();
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(0);
        if (row == null)
            row = sheet.createRow(0);
        XSSFCell cell = row.getCell(column);
        if (cell == null)
            cell = row.createCell(column);
        cell.setCellValue(title);
        return new CellReference(sheet.getSheetName(), 0, column, true, true);
    }
}
