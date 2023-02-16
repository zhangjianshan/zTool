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
     * �����
     *
     * @param tableRow ������
     * @param data     ����
     */
    @Override
    public void writeTableData(XWPFTableRow tableRow, String data) {
        PersonVO personVO = JSONObject.parseObject(data, PersonVO.class);
        //��ʼʱ��
        tableRow.getCell(0).setText(WordTool.nullToEmpty(personVO.getName()));
    }

    /**
     * ��ʼ��
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
