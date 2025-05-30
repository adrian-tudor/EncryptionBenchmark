package gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ResultsTableModel extends AbstractTableModel {
    private final String[] columns = {"Algoritm", "Operatie", "Timp (ms)"};
    private final List<String[]> data = new ArrayList<>();

    public void addResult(String algorithm, String operation, double time) {
        data.add(new String[]{algorithm, operation, String.valueOf(time)});
        fireTableDataChanged();
    }

    public void clearResults() {
        data.clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    public List<String[]> getData() {
        return data;
    }
}
