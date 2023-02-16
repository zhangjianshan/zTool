package com.ztool.word.base;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

/**
 * @author zhangjianshan1992
 */
@Service
public abstract class WordBasicDataHandlerAbstract {

    /**
     * 处理数据
     *
     * @param document 文档
     */
    public abstract void disposeData(XWPFDocument document);

    /**
     * code
     *
     * @return code
     */
    public abstract String code();
}
