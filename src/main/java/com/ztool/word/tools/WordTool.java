package com.ztool.word.tools;


import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.SneakyThrows;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.chart.*;
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
     * 创建饼状
     *
     * @param document     文档
     * @param xDataList    横坐标数据
     * @param yDataList    纵坐标数据
     * @param titleText    图表标题
     * @param pieTitleText 系列提示标题
     */
    @SneakyThrows
    public static void createPieChart(XWPFDocument document, List<String> xDataList,
                                      List<Integer> yDataList, String titleText,
                                      String pieTitleText) {
        //1、创建表
        XWPFChart chart = document.createChart(15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);
        //2、图表相关设置
        //图表标题
        chart.setTitleText(titleText);
        //图例是否覆盖标题
        chart.setTitleOverlay(false);

        //3、图例设置
        XDDFChartLegend legend = chart.getOrAddLegend();
        // 图例位置:上下左右
        legend.setPosition(LegendPosition.RIGHT);

        //4、X轴(分类轴)相关设置:饼图中的图例显示
        String[] xAxisData = xDataList.toArray(new String[0]);
        // 设置分类数据
        XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromArray(xAxisData);

        //5、Y轴(值轴)相关设置:饼图中的圆形显示
        Integer[] yAxisData = yDataList.toArray(new Integer[0]);
        // 设置值数据
        XDDFNumericalDataSource<Integer> yAxisSource = XDDFDataSourcesFactory.fromArray(yAxisData);

        //6、创建饼图对象,饼状图不需要X,Y轴,只需要数据集即可
        XDDFPieChartData pieChart = (XDDFPieChartData) chart.createData(ChartTypes.PIE, null, null);
        pieChart.setVaryColors(true);
        //7、加载饼图数据集
        XDDFPieChartData.Series pieSeries = (XDDFPieChartData.Series) pieChart.addSeries(xAxisSource, yAxisSource);
        //系列提示标题
        pieSeries.setTitle(pieTitleText, null);
        pieSeries.setShowLeaderLines(true);
        //8、绘制饼图
        chart.plot(pieChart);

    }


    /**
     * 添加word中的标记数据 标记方式为 ${text}
     *
     * @param document word对象
     * @param textMap  需要替换的信息集合
     */
    public void changeParagraphText(XWPFDocument document, Map<String, String> textMap) {
        //获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            //判断此段落时候需要进行替换
            String text = paragraph.getText();
            if (checkText(text)) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    //替换模板原来位置
                    run.setText(changeValue(run.toString(), textMap), 0);
                }
            }
        }
    }

    /**
     * 替换表格中标记的数据 标记方式为 ${text}
     * 这里有个奇怪的问题 输入${}符号的时候需要把输入法切换到中文
     * ${}中间不能用数字,不能有下划线
     *
     * @param document      word对象
     * @param tableTextList 需要替换的信息集合
     */
    public static void changeTableText(XWPFDocument document, List<Map<String, String>> tableTextList) {
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            Map<String, String> textMap = tableTextList.get(i);
            //只处理行数大于等于2的表格
            XWPFTable table = tables.get(i);
            if (table.getRows().size() > 1) {
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if (checkText(table.getText())) {
                    List<XWPFTableRow> rows = table.getRows();
                    //遍历表格,并替换模板
                    eachTable(rows, textMap);
                }
            }
        }
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
