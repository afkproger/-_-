package org.example.aproximationproject.Model;

import java.util.Map;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class ExcelReader {
    public Map<Double, Double> readCoordinates(File file) throws IOException {
        Map<Double, Double> coordinates = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Берем первый лист
            for (Row row : sheet) {
                Cell xCell = row.getCell(0); // Первая колонка (x)
                Cell yCell = row.getCell(1); // Вторая колонка (y)

                if (xCell != null && yCell != null) {
                    Double x = getCellValueAsDouble(xCell);
                    Double y = getCellValueAsDouble(yCell);

                    if (validateCoordinate(x, y)) {
                        coordinates.put(x, y);
                    } else {
                        return null;
                    }
                }
            }
        }

        return coordinates;
    }

    private Double getCellValueAsDouble(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        return null; // Возвращаем null, если значение не число
    }


    private Boolean validateCoordinate(Double x, Double y) {
        if (x != null && y != null && y > (-273.15)) {
            return true;
        } else {
            return false;
        }
    }


}
