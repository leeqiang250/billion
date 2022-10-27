package com.billion.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
        for (int i = 0;;i++) {
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
            for (int cellNum = 0; cellNum < headMap.keySet().size(); cellNum ++) {
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
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        for (int i = 0; i < dataList.size(); i++) {
            T t = dataList.get(i);
            Class clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();
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


    /**
     * 判断合并了行
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private static boolean isMergedColumn(Sheet sheet, int row ,int column) {
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

    public static String getMergedColumnValue(Sheet sheet ,int row , int column) {
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


    private static Method getClassGetMethod(Class clazz, String fieldName) throws NoSuchMethodException{
        return  clazz.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
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