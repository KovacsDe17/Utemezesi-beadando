import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelHandler {
    private static final String DEFAULT_FILE_LOCATION = ".\\src\\main\\resources\\input\\Input_VSP.xlsx";

    public static Workbook openWorkbook(String fileLocation){
        FileInputStream file;
        Workbook workbook = null;

        try{
            file = new FileInputStream(new File(fileLocation));
            workbook = new XSSFWorkbook(file);
        } catch (IOException e){
            System.err.println(e.getMessage());
        }

        return workbook;
    }

    public static Workbook openWorkbook(){
        return openWorkbook(DEFAULT_FILE_LOCATION);
    }

    public static Map<Integer, List<String>> getDataFrom(Workbook workbook, int sheetIndex){
        Sheet sheet = workbook.getSheetAt(sheetIndex);

        Map<Integer, List<String>> data = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<>());
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING: data.get(i).add(cell.getStringCellValue()); break;
                    case NUMERIC: if (DateUtil.isCellDateFormatted(cell)) {
                        data.get(i).add(dateToInt(cell.getDateCellValue()) + "");
                    } else {
                        data.get(i).add(cell.getNumericCellValue() + "");
                    }; break;
                    case BOOLEAN: data.get(i).add(String.valueOf(cell.getBooleanCellValue())); break;
                    case FORMULA: data.get(i).add(cell.getCellFormula()); break;
                    default: data.get(i).add(" ");
                }
            }
            i++;
        }

        return data;
    }

    private static int dateToInt(java.util.Date date){
        int number = 0;

        number += date.getHours()*60;
        number += date.getMinutes();

        return number;
    }
}
