import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Excel {
    private static final String DEFAULT_FILE_LOCATION = ".\\input\\";
    private static final String DEFAULT_FILE_NAME = "Input_VSP.xlsx";

    private static Excel instance;

    public static Excel getInstance(){
        if(instance == null)
            instance = new Excel();

        return instance;
    }

    public Workbook openWorkbook(String fileLocation){
        InputStream file;
        Workbook workbook;

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();

        file = classLoader.getResourceAsStream(fileLocation);

        try
        {
            if (file == null)
            {
                try{
                    String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation()
                            .toURI().getPath();

                    File jarFile = new File(jarPath);
                            file = new FileInputStream(new File(jarFile.getParent() + "\\input\\" + DEFAULT_FILE_NAME));
                } catch (IOException e){
                    System.err.println(e.getMessage());
                }
            }

            workbook = new XSSFWorkbook(file);
            return workbook;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Workbook defaultWorkbook(){

        return openWorkbook(DEFAULT_FILE_LOCATION+DEFAULT_FILE_NAME);
    }

    public Map<Integer, List<String>> getDataFrom(Workbook workbook, int sheetIndex){
        Sheet sheet = workbook.getSheetAt(sheetIndex);

        Map<Integer, List<String>> data = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<>());
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING: data.get(i).add(cell.getStringCellValue()); break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            data.get(i).add(dateToInt(cell.getDateCellValue()) + "");
                        } else {
                            data.get(i).add(cell.getNumericCellValue() + "");
                        } break;
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
