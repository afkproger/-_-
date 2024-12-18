package org.example.aproximationproject.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class ExcelReader {
    public Map<Double, ArrayList<Double>> readCoordinates(File file) throws IOException {
        Map<Double, ArrayList<Double>> coordinates = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Берем первый лист
            for (Row row : sheet) {
                Cell xCell = row.getCell(0); // Первая колонка (x)
                Cell yCell = row.getCell(1); // Вторая колонка (y)
                Cell zCell = row.getCell(2);

                if (xCell != null && yCell != null && zCell != null) {
                    Double x = getCellValueAsDouble(xCell);
                    Double y = getCellValueAsDouble(yCell);
                    Double z = getCellValueAsDouble(zCell);
                    if (validateCoordinate(x ,y, z)) {
                        ArrayList<Double> point = new ArrayList<>();
                        point.add(y);
                        point.add(z);
                        coordinates.put(x, point);
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


    private Boolean validateCoordinate(Double x ,  Double y, Double z) {
        if (x != null && y != null && z!=null && z > (-273.15)) {
            return true;
        } else {
            return false;
        }
    }


}
