package excel;

import cn.hutool.core.collection.CollectionUtil;
import com.ztool.excel.select.CascadeSelectTool;
import com.ztool.excel.select.SimpleSelectTool;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjianshan on 2023-04-30
 */
public class CascadeSelectTest {
    public static void main(String[] args) {
        //������
        Map<String, List<String>> areaList = new LinkedHashMap<>();
        areaList.put("����", CollectionUtil.newArrayList("���", "κ��", "���"));
        areaList.put("���", CollectionUtil.newArrayList("����", "����", "�ŷ�"));
        areaList.put("κ��", CollectionUtil.newArrayList("�ܲ�", "����", "��Τ"));
        areaList.put("���", CollectionUtil.newArrayList("��Ȩ", "�Ƹ�", "���"));
        areaList.put("����", CollectionUtil.newArrayList("����"));
        areaList.put("����", CollectionUtil.newArrayList("��ĳ"));
        //����������
        List<Integer> selectColList = CollectionUtil.newArrayList(0, 1, 2, 3);
        XSSFWorkbook book = new XSSFWorkbook();
        book.createSheet("������ģ��");
        CascadeSelectTool.setCascadeDropDownBox(book, "������ģ��", "data", "����", areaList, selectColList);
        SimpleSelectTool.writeFile(book);
    }
}
