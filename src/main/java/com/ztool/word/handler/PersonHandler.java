package com.ztool.word.handler;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.ztool.demo.stream.PersonVO;
import com.ztool.word.base.WordTableHandlerAbstract;
import com.ztool.word.tools.WordTool;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangjianshan on 2023-02-16
 */
@Service
public class PersonHandler extends WordTableHandlerAbstract {
    /**
     * 填充表格
     *
     * @param tableRow 数据行
     * @param data     数据
     */
    @Override
    public void writeTableData(XWPFTableRow tableRow, String data) {
        PersonVO personVO = JSONObject.parseObject(data, PersonVO.class);
        //开始时间
        tableRow.getCell(0).setText(WordTool.nullToEmpty(personVO.getName()));
    }

    /**
     * 初始化
     */
    @Override
    public void init() {

    }


    /**
     * code
     *
     * @return dataCode
     */
    @Override
    public List<String> dataCode() {
        return CollectionUtil.newArrayList("person");
    }
}
