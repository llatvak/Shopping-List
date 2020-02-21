package tuni.tiko.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

/**
 * Class that holds shopping list data, elements and actions.
 * <p>
 * Holds a shopping list table that has shopping item elements.
 * Data is in list as objects with name and amount data.
 * Initializes the shopping list and adds base table data.
 * </p>
 *
 * @author Lauri Latva-Kyyny
 * @version 1.0
 */
public class ShoppingList {

    private TableView<ShopItem> table;
    private TableColumn<ShopItem,String> itemNameColumn;
    private TableColumn<ShopItem,Integer> itemAmountColumn;
    // Hold the data of the ShoppingTable in a observablelist
    private ObservableList<ShopItem> data = FXCollections.observableArrayList(
            new ShopItem("Carrot", 5),
            new ShopItem("Milk", 10)
    );

    /**
     * Constructor to set up shopping list when created.
     */
    public ShoppingList() {
        setShoppingList();
    }

    /**
     * Initializes the shopping list holds edit info.
     * <p>
     * Creates table view JavaFX element and initializes two columns for item names and amount.
     * Sets data from observablelist to table and holds edit code for cell info modifying.
     * </p>
     */
    public void setShoppingList() {
        table = new TableView();
        table.setEditable(true);

        itemNameColumn = new TableColumn("Name");
        itemAmountColumn = new TableColumn("Amount");
        itemNameColumn.setMinWidth(600);
        itemAmountColumn.setMinWidth(200);


        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        table.setItems(data);
        table.getColumns().addAll(itemNameColumn, itemAmountColumn);

        itemNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        itemNameColumn.setOnEditCommit(
                (CellEditEvent<ShopItem, String> t) -> {
                    ((ShopItem) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setName(t.getNewValue());
                });

        itemAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        itemAmountColumn.setOnEditCommit(
                (CellEditEvent<ShopItem, Integer> t) -> {
                    ((ShopItem) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setAmount(t.getNewValue());
                });

        Callback<TableColumn<ShopItem, String>, TableCell<ShopItem, String>> cellFactory = (
                TableColumn<ShopItem, String> p) -> new EditableCell();
        itemNameColumn.setCellFactory(cellFactory);
    }


    /**
     * Returns shopping list as a table.
     *
     * @return tableview Javafx element
     */
    public TableView getShoppingList() {
        return table;
    }

    /**
     * Adds item with data from text fields to table when add button is clicked.
     *
     * @param name ShopItem name to be added as a String
     * @param amount  ShopItem amount to be added as a String
     */
    public void add(String name, String amount) throws NumberFormatException {
        try {
            data.add(new ShopItem(name, Integer.parseInt(amount)));
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the ShoppingList data.
     *
     * @return objects of ShoppingList in an observable list
     */
    public ObservableList<ShopItem> getData() {
        return data;
    }

    /**
     * Removes selected row ShopItem when remove element is selected and remove button clicked.
     */
    public void remove() {
        Object selectedItem = table.getSelectionModel().getSelectedItem();
        table.getItems().remove(selectedItem);
    }

    /**
     * Removes all table items.
     */
    public void removeAll() {
        table.getItems().removeAll(table.getItems());
    }
}
