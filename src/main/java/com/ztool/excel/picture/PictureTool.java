package com.ztool.excel.picture;

import cn.hutool.core.io.FileUtil;
import com.ztool.excel.select.SimpleSelectTool;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author zhangjianshan on 2023-04-30
 */
public class PictureTool {
    public static void main(String[] args) {
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet("sheetName");
        try {
            //第一种方式: url地址
            BufferedImage bufferImg = ImageIO.read(FileUtil.getInputStream("E:\\template\\fire-1842140.jpg"));
            // 将图片写入流中
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            insertImage(book, patriarch, getImageData(bufferImg), 0, 2, 0, 4);
            SimpleSelectTool.writeFile(book);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void insertImage(HSSFWorkbook wb, HSSFPatriarch pa, byte[] data, int startRow, int endRow, int startColumn, int endColumn) {
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 100, (short) startColumn, startRow, (short) endColumn, endRow);
        anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
        pa.createPicture(anchor, wb.addPicture(data, HSSFWorkbook.PICTURE_TYPE_JPEG));
    }

    private static byte[] getImageData(BufferedImage bi) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ImageIO.write(bi, "PNG", bout);
            return bout.toByteArray();
        } catch (Exception exe) {
            exe.printStackTrace();
            return null;
        }
    }
}
