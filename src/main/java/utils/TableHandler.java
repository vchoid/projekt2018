package main.java.utils;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableHandler {
	
	@FXML private TableView table;
	private String name;
	
	
	public TableHandler(TableView table, String name) {
		this.name = name;
		this.table = table;
		createColoumn();
	}
	
	private void createColoumn() {
		
		TableColumn col = new TableColumn(name);
		
		table.getColumns().add(col);
		
		
	}
	
	
	
}
