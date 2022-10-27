package com.billion.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author renjian
 */
@Slf4j
public class Excel {

    public static <T> List<T> readExcel(String path, Class<T> clazz, String sheetName) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        FileInputStream inputStream = new FileInputStream(new File(path));
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet(sheetName);
        List<T> resultList = new ArrayList<>();
        Row headRow = sheet.getRow(0);
        Map<Integer, String> headMap = new HashMap<>();
        for (int i = 0; ; i++) {
            Cell cell = headRow.getCell(i);
            if (Objects.isNull(cell)) {
                break;
            }
            headMap.put(i, headRow.getCell(i).getStringCellValue());
        }

        for (int rowNum = 1; ; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (Objects.isNull(row)) {
                break;
            }
            T t = clazz.getDeclaredConstructor().newInstance();
            for (int cellNum = 0; cellNum < headMap.keySet().size(); cellNum++) {
                row.getCell(cellNum).setCellType(Cell.CELL_TYPE_STRING);
                String cell = row.getCell(cellNum).getStringCellValue();
                Method method = getClassMethod(clazz, headMap.get(cellNum));
                if (method == null) {
                    continue;
                }
                method.invoke(t, cell);
            }
            resultList.add(t);
        }

        return resultList;
    }


    public static <T> List<T> readMergeColumnExcel(String path, Class<T> clazz, String sheetName) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        FileInputStream inputStream = new FileInputStream(new File(path));
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet(sheetName);
        Map<String, Integer> results = new HashMap<>();
        List<T> resultList = new ArrayList<>();
        Row headRow = sheet.getRow(0);
        Map<Integer, String> headMap = new HashMap<>();
        for (int i = 0; ; i++) {
            Cell cell = headRow.getCell(i);
            if (Objects.isNull(cell)) {
                break;
            }
            headMap.put(i, headRow.getCell(i).getStringCellValue());
        }

        for (int rowNum = 1; ; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (Objects.isNull(row)) {
                break;
            }
            T t = clazz.getDeclaredConstructor().newInstance();
            for (int cellNum = 0; cellNum < headMap.keySet().size(); cellNum++) {
                row.getCell(cellNum).setCellType(Cell.CELL_TYPE_STRING);
                String cell = row.getCell(cellNum).getStringCellValue();
                if (isMergedColumn(sheet, rowNum, cellNum)) {
                    cell = getMergedColumnValue(sheet, rowNum, cellNum);
                }
                Method method = getClassMethod(clazz, headMap.get(cellNum));
                if (method == null) {
                    continue;
                }
                method.invoke(t, cell);
            }
            resultList.add(t);
        }

        return resultList;
    }


    public static <T> boolean writeExcel(String path, List<T> dataList, String sheetName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if (dataList.size() < 1) {
            return true;
        }
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        Field[] fields = dataList.get(0).getClass().getDeclaredFields();
        // 生成表头
        Row headRow = sheet.createRow(0);
        for (int fieldNum = 0; fieldNum < fields.length; fieldNum++) {
            CellStyle headStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            headStyle.setFont(font);
            Cell cell = headRow.createCell(fieldNum);
            cell.setCellValue(fields[fieldNum].getName());
            cell.setCellStyle(headStyle);
        }
        for (int i = 1; i <= dataList.size(); i++) {
            T t = dataList.get(i - 1);
            Class clazz = t.getClass();
            Row row = sheet.createRow(i);
            for (int fieldNum = 0; fieldNum < fields.length; fieldNum++) {
                Cell cell = row.createCell(fieldNum);
                Field field = fields[fieldNum];
                Method method = getClassGetMethod(clazz, field.getName());
                String value = String.valueOf(method.invoke(t));
                cell.setCellValue(value);
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        workbook.write(fileOutputStream);
        //关闭流
        fileOutputStream.close();
        return true;
    }


    public static <T> boolean writeMergeColumnExcel(String path, List<T> dataList, String sheetName, String mergeColumnName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if (0 == dataList.size()) {
            return true;
        }

        Map<String, List<T>> dataMap = new HashMap<>(dataList.size());
        Field[] fields = dataList.get(0).getClass().getDeclaredFields();
        //记录要合并的列在字段数组中的位置
        Integer mergeFieldSite = 0;
        //根据合并字段组装数据
        for (int i = 0; i < dataList.size(); i++) {
            T t = dataList.get(i);
            for (int fieldNum = 0; fieldNum < fields.length; fieldNum++) {
                Field field = fields[fieldNum];
                Method method = getClassGetMethod(t.getClass(), field.getName());
                String value = String.valueOf(method.invoke(t));
                if (field.getName().equalsIgnoreCase(mergeColumnName)) {
                    mergeFieldSite = fieldNum;
                    List<T> valueList = new ArrayList<>();
                    if (dataMap.containsKey(value)) {
                        valueList = dataMap.get(value);
                    }
                    valueList.add(t);
                    dataMap.put(value, valueList);
                    break;
                }
            }
        }

        if (mergeFieldSite != 0) {
            //如果要合并的字段不是在第一个，则将其移动到第一个
            Field sitField = fields[mergeFieldSite];
            fields[mergeFieldSite] = fields[0];
            fields[0] = sitField;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // 生成表头
        Row headRow = sheet.createRow(0);
        for (int fieldNum = 0; fieldNum < fields.length; fieldNum++) {
            CellStyle cellStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            cellStyle.setFont(font);
            Cell cell = headRow.createCell(fieldNum);
            cell.setCellValue(fields[fieldNum].getName());
            cell.setCellStyle(cellStyle);
        }

        //构建表格数据
        Integer rowNum = 1;
        List<List<Integer>> mergeParams = new ArrayList<>();
        for (List<T> itemList : dataMap.values()) {
            //构建要合并的单元格信息
            List<Integer> mergeParam = new ArrayList<>(4);
            mergeParam.add(rowNum);
            mergeParam.add(rowNum + itemList.size() - 1);
            mergeParam.add(0);
            mergeParam.add(0);
            mergeParams.add(mergeParam);

            for (T data : itemList) {
                Row row = sheet.createRow(rowNum);
                rowNum++;
                for (int fieldNum = 0; fieldNum < fields.length; fieldNum++) {
                    Cell cell = row.createCell(fieldNum);
                    Field field = fields[fieldNum];
                    Method method = getClassGetMethod(data.getClass(), field.getName());
                    String value = String.valueOf(method.invoke(data));
                    cell.setCellValue(value);
                }
            }
        }

        for (List<Integer> list : mergeParams) {
            sheet.addMergedRegion(new CellRangeAddress(list.get(0), list.get(1), list.get(2), list.get(3)));
        }

        var file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(path);
        workbook.write(fileOutputStream);
        //关闭流
        fileOutputStream.close();
        return true;
    }

    private static boolean isMergedColumn(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (column == firstColumn && column == lastColumn) {
                if (row >= firstRow && row <= lastRow) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getMergedColumnValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if (column >= firstColumn && column <= lastColumn) {
                if (row >= firstRow && row <= lastRow) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    fCell.setCellType(Cell.CELL_TYPE_STRING);
                    return fCell.getStringCellValue();
                }
            }
        }
        return null;
    }


    private static Method getClassGetMethod(Class clazz, String fieldName) throws NoSuchMethodException {
        return clazz.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
    }

    private static Method getClassMethod(Class aClazz, String methodName) {
        methodName = "set" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        for (Method method : aClazz.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

}