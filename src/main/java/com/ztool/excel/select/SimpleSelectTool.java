package com.ztool.excel.select;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author zhangjianshan on 2023-04-30
 */
public class SimpleSelectTool {

    public static void main(String[] args) {
        Workbook book = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) book.createSheet("下拉框模板");
        List<String> selectDateList = CollectionUtil.newArrayList("蜀国", "魏国", "吴国");
        //基于数据有效性序列设置下拉(下拉字符有限制)
        effectivenessSelectData(sheet, selectDateList, 0);

        //基于sheet
        String dataSheet = createHiddenSheet(book, "dataSheet", selectDateList);
        sheetSelectData(sheet, dataSheet, 1);
        writeFile(book);
    }

    /**
     * 基于数据有效性序列设置下拉(字符数有限制)
     *
     * @param sheet       sheet页
     * @param selDateList 下拉数据
     * @param columnIndex 第几列从0开始
     */
    public static void effectivenessSelectData(XSSFSheet sheet, List<String> selDateList, Integer columnIndex) {
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        DataValidationConstraint provConstraint = dvHelper.createExplicitListConstraint(selDateList.toArray(new String[0]));
        setSelectParameter(sheet, dvHelper, provConstraint, 1, 300, columnIndex, columnIndex);
    }


    /**
     * 基于数据有效性序列设置下拉
     *
     * @param sheet       sheet页
     * @param columnIndex 第几列从0开始
     */
    public static void sheetSelectData(XSSFSheet sheet, String sheetName, Integer columnIndex) {
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        DataValidationConstraint provConstraint = dvHelper.createFormulaListConstraint(sheetName + "!$A:$A");
        setSelectParameter(sheet, dvHelper, provConstraint, 1, 300, columnIndex, columnIndex);
    }

    public static void setSelectParameter(XSSFSheet sheet, XSSFDataValidationHelper dvHelper, DataValidationConstraint provConstraint, int firstRow, int lastRow, int firstCol, int lastCol) {
        //四个参数分别是起始行、终止行、起始列、终止列
        CellRangeAddressList proRangeAddressList = new CellRangeAddressList(1, 300, firstCol, lastCol);
        DataValidation provinceDataValidation = dvHelper.createValidation(provConstraint, proRangeAddressList);
        //验证
        provinceDataValidation.createErrorBox("error", "请选择正确的类型");
        provinceDataValidation.setShowErrorBox(true);
        provinceDataValidation.setSuppressDropDownArrow(true);
        sheet.addValidationData(provinceDataValidation);
    }

    public static String createHiddenSheet(Workbook book, String hiddenSheetName, List<String> selectDataList) {
        String realitySheetName = hiddenSheetName + "_base_data";
        XSSFSheet hiddenSheet = (XSSFSheet) book.createSheet(realitySheetName);
        //填充数据前设置隐藏列
        book.setSheetHidden(book.getSheetIndex(realitySheetName), true);
        for (int i = 0; i < selectDataList.size(); i++) {
            XSSFRow row = hiddenSheet.createRow(i);
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(selectDataList.get(i));
        }
        return realitySheetName;
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
