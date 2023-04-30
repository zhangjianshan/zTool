package com.ztool.excel.select;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangjianshan on 2023-04-30
 */
public class CascadeSelectTool {
    public static void main(String[] args) {
        Map<String, List<String>> areaList = new LinkedHashMap<>();
        areaList.put("势力", CollectionUtil.newArrayList("蜀国", "魏国", "吴国"));
        areaList.put("蜀国", CollectionUtil.newArrayList("刘备", "关羽", "张飞"));
        areaList.put("魏国", CollectionUtil.newArrayList("曹操", "许褚", "典韦"));
        areaList.put("吴国", CollectionUtil.newArrayList("孙权", "黄盖", "周瑜"));
        areaList.put("关羽", CollectionUtil.newArrayList("关兴"));

        XSSFWorkbook book = new XSSFWorkbook();
        book.createSheet("下拉框模板");
        setCascadeDropDownBox(book, "下拉框模板", "data", areaList, 0, 1);
        SimpleSelectTool.writeFile(book);
    }

    /**
     * 设置二级级联下拉框数据
     *
     * @param wb              表格对象
     * @param typeName        要渲染的sheet名称
     * @param hiddenSheetName 数据字典sheet名称
     * @param values          级联下拉数据
     * @param fatherCol       父级下拉区域
     * @param sonCol          子级下拉区域
     */
    public static void setCascadeDropDownBox(XSSFWorkbook wb, String typeName, String hiddenSheetName, Map<String, List<String>> values,
                                             Integer fatherCol, Integer sonCol) {
        //获取所有sheet页个数
        int sheetTotal = wb.getNumberOfSheets();
        //处理下拉数据
        if (values != null && values.size() != 0) {
            //新建一个sheet页
            XSSFSheet hiddenSheet = wb.getSheet(hiddenSheetName);
            if (hiddenSheet == null) {
                hiddenSheet = wb.createSheet(hiddenSheetName);
                sheetTotal++;
            }
            // 获取数据起始行
            int startRowNum = hiddenSheet.getLastRowNum() + 1;
            int endRowNum = startRowNum;
            Set<String> keySet = values.keySet();
            for (String key : keySet) {
                XSSFRow fRow = hiddenSheet.createRow(endRowNum++);
                fRow.createCell(0).setCellValue(key);
                List<String> sons = values.get(key);
                for (int i = 1; i <= sons.size(); i++) {
                    fRow.createCell(i).setCellValue(sons.get(i - 1));
                }
                // 添加名称管理器
                String range = getRange(1, endRowNum, sons.size());
                Name name = wb.createName();
                //key不可重复
                name.setNameName(key);
                String formula = hiddenSheetName + "!" + range;
                name.setRefersToFormula(formula);
            }
            //将数据字典sheet页隐藏掉
            wb.setSheetHidden(sheetTotal - 1, true);

            // 设置父级下拉
            //获取新sheet页内容
            String mainFormula = hiddenSheetName + "!$A$" + ++startRowNum + ":$A$" + endRowNum;
            XSSFSheet mainSheet = wb.getSheet(typeName);
            // 设置下拉列表值绑定到主sheet页具体哪个单元格起作用
            mainSheet.addValidationData(setDataValidation(wb, mainFormula, 1, 65535, fatherCol, fatherCol));

            // 设置子级下拉
            // 当前列为子级下拉框的内容受父级哪一列的影响
            String indirectFormula = "INDIRECT($" + decimalToTwentyHex(fatherCol + 1) + "2)";
            mainSheet.addValidationData(setDataValidation(wb, indirectFormula, 1, 65535, sonCol, sonCol));

            // 设置子级下拉
            // 当前列为子级下拉框的内容受父级哪一列的影响
            String sonIndirectFormula = "INDIRECT($" + decimalToTwentyHex(sonCol+ 1) + "2)";
            mainSheet.addValidationData(setDataValidation(wb, sonIndirectFormula, 1, 65535, 2, 2));
        }
    }

    /**
     * 计算formula
     *
     * @param offset   偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId    第几行
     * @param colCount 一共多少列
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     */
    public static String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            // 26-51之间，包括边界（仅两次字母表计算）
            if ((colCount - 25) / 26 == 0 || colCount == 51) {
                // 边界值
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }

    /**
     * 返回类型 DataValidation
     *
     * @param wb         表格对象
     * @param strFormula formula
     * @param firstRow   起始行
     * @param endRow     终止行
     * @param firstCol   起始列
     * @param endCol     终止列
     * @return 返回类型 DataValidation
     */
    public static DataValidation setDataValidation(Workbook wb, String strFormula, int firstRow, int endRow, int firstCol, int endCol) {
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) wb.getSheet("data"));
        DataValidationConstraint formulaListConstraint = dvHelper.createFormulaListConstraint(strFormula);
        return dvHelper.createValidation(formulaListConstraint, regions);
    }

    /**
     * 十进制转二十六进制
     */
    public static String decimalToTwentyHex(int decimalNum) {
        StringBuilder result = new StringBuilder();
        while (decimalNum > 0) {
            int remainder = decimalNum % 26;
            //大写A的ASCII码值为65
            result.append((char) (remainder + 64));
            decimalNum = decimalNum / 26;
        }
        return result.reverse().toString();
    }
}
