/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats.excell;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author DOWES
 */
public class Document {

    private final InputStream inputStream;
    private final List<Row> data;
    private List<String> header;

    public Document(InputStream inputStream) {
        this.inputStream = inputStream;
        this.data = new ArrayList<>();
        this.header = new ArrayList<>();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public List<Row> getData() {
        return data;
    }

    public List<String> getHeader() {
        return header;
    }

    public static Document create(InputStream inputStream) {
        return new Document(inputStream).create();
    }

    public Document create() {
        try {
            this.data.clear();
            this.header.clear();
            //création du worbook
            Workbook workbook = WorkbookFactory.create(this.inputStream);
            if (workbook == null) {
                return this;
            }
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return this;
            }
            this.header = getHeaderFormStream(sheet.getRow(0));
            if (this.header == null || this.header.isEmpty()) {
                return this;
            }
            int sizeRow = sheet.getLastRowNum();
            Row row;
            for (int i = 1; i <= sizeRow; i++) {
                row = getDataFormStream(i, sheet.getRow(i));
                if (row != null) {
                    this.data.add(row);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    public int size() {
        return this.header != null ? this.header.size() : 0;
    }

    public Row getRow(int index) {
        return this.data != null ? (index > -1 && this.data.size() > index) ? this.data.get(index) : null : null;
    }

    private String getStringFromCell(Cell cell) throws Exception {
        if (cell == null) {
            return null;
        }
        try {
            return cell.getStringCellValue().trim();
        } catch (Exception ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            return (((int) cell.getNumericCellValue()) + "").trim();
        }
    }

    private Object getValueFromCell(Cell cell) throws Exception {
        try {
            return cell.getBooleanCellValue();
        } catch (Exception e1) {
            try {
                return cell.getStringCellValue().trim();
            } catch (Exception e2) {
                try {
                    Double value = cell.getNumericCellValue();
                    return ((value - value.intValue()) > 0) ? cell.getNumericCellValue() : ((int) cell.getNumericCellValue());
                } catch (Exception e3) {
                    try {
                        return cell.getDateCellValue();
                    } catch (Exception e4) {
                        Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, e4);
                    }
                }
            }
        }
        return null;
    }

    private List<String> getHeaderFormStream(org.apache.poi.ss.usermodel.Row row) throws Exception {
        List<String> result = null;
        if (row != null) {
            final int sizeCell = row.getLastCellNum();
            if (sizeCell > 0) {
                result = new ArrayList<>();
                String value;
                for (int i = 0; i < sizeCell; i++) {
                    value = getStringFromCell(row.getCell(i));
                    if (value != null) {
                        result.add(value);
                    }
                }
            }
        }
        return result;
    }

    private Row getDataFormStream(int index, org.apache.poi.ss.usermodel.Row row) throws Exception {
        Row result = null;
        if (row != null && this.header.size() > 0) {
            result = new Row(index);
            for (int i = 0; i < this.header.size(); i++) {
                result.getColumns().add(new Column(this.header.get(i), getValueFromCell(row.getCell(i))));
            }
        }
        return result;
    }

}
