package com.ztool.word.base;

import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangjianshan1992
 */
@Service
public abstract class WordTableHandlerAbstract<T> {

    /**
     * 填充表格
     *
     * @param tableRow 数据行
     * @param data     数据
     */
    public abstract void writeTableData(XWPFTableRow tableRow, String data);

    /**
     * 初始化
     */
    public abstract void init();

    /**
     * code
     *
     * @return dataCode
     */
    public abstract List<String> dataCode();
}
