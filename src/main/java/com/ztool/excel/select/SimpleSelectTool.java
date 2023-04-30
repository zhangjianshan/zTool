package com.ztool.excel.select;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangjianshan on 2023-04-30
 */
public class SimpleSelectTool {


    private final Workbook workbook;

    private XSSFSheet sheet;

    private int firstRow = 1;

    private List<String> selectDateList;

    private String hiddenSheetName = "hidden";

    public SimpleSelectTool(Workbook book) {
        this.workbook = book;
    }

    public SimpleSelectTool createSheet(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (Objects.nonNull(sheet)) {
            this.sheet = (XSSFSheet) sheet;
        } else {
            this.sheet = (XSSFSheet) workbook.createSheet(sheetName);
        }
        return this;
    }

    public SimpleSelectTool createSelectDateList(List<String> selectDateList) {
        this.selectDateList = selectDateList;
        return this;
    }

    public SimpleSelectTool createHiddenName(String hiddenSheetName) {
        this.hiddenSheetName = hiddenSheetName;
        return this;
    }

    public SimpleSelectTool createFirstRow(int firstRow) {
        this.firstRow = firstRow;
        return this;
    }

    /**
     * 基于数据有效性序列设置下拉(字符数有限制)
     *
     * @param columnIndex 第几列从0开始
     */
    public SimpleSelectTool effectivenessSelectData(Integer columnIndex) {
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        DataValidationConstraint provConstraint = dvHelper.createExplicitListConstraint(selectDateList.toArray(new String[0]));
        setSelectParameter(sheet, dvHelper, provConstraint, columnIndex, columnIndex);
        return this;
    }


    /**
     * 基于数据有效性序列设置下拉
     *
     * @param columnIndex 第几列从0开始
     */
    public SimpleSelectTool sheetSelectData(Integer columnIndex) {
        //创建隐藏sheet
        this.createHiddenSheet();
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        DataValidationConstraint provConstraint = dvHelper.createFormulaListConstraint(hiddenSheetName + "!$A:$A");
        setSelectParameter(sheet, dvHelper, provConstraint, columnIndex, columnIndex);
        return this;
    }

    public void setSelectParameter(XSSFSheet sheet, XSSFDataValidationHelper dvHelper, DataValidationConstraint provConstraint, int firstCol, int lastCol) {
        //四个参数分别是起始行、终止行、起始列、终止列
        CellRangeAddressList proRangeAddressList = new CellRangeAddressList(firstRow, 65535, firstCol, lastCol);
        DataValidation provinceDataValidation = dvHelper.createValidation(provConstraint, proRangeAddressList);
        //验证
        provinceDataValidation.createErrorBox("error", "请选择正确的类型");
        provinceDataValidation.setShowErrorBox(true);
        provinceDataValidation.setSuppressDropDownArrow(true);
        sheet.addValidationData(provinceDataValidation);
    }

    public void createHiddenSheet() {
        XSSFSheet hiddenSheet = (XSSFSheet) workbook.getSheet(hiddenSheetName);
        if (Objects.isNull(hiddenSheet)) {
            hiddenSheet = (XSSFSheet) workbook.createSheet(hiddenSheetName);
        }
        //填充数据前设置隐藏列
        workbook.setSheetHidden(workbook.getSheetIndex(hiddenSheetName), true);
        for (int i = 0; i < selectDateList.size(); i++) {
            XSSFRow row = hiddenSheet.createRow(i);
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(selectDateList.get(i));
        }
    }

    public void writeFile() {
        writeFile(workbook);
    }


    public static void writeFile(Workbook book) {
        try {
            String storeName = System.currentTimeMillis() + ".xlsx";
            String folder = "template/" + DateUtil.format(DateUtil.date(), "yyMMdd") + "/";
            String attachmentFolder = "E://" + File.separator;
            String address = folder + storeName;
            FileUtil.mkdir(attachmentFolder + folder);
            FileOutputStream fileOut = new FileOutputStream(attachmentFolder + address);
            book.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
