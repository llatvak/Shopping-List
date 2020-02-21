package tuni.tiko.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tuni.tiko.parser.*;

import java.io.*;
import java.util.Map;

/**
 * Main class for initializing main elements and layouts for shopping list.
 * <p>
 * Generates main JavaFX elements and layouts for shopping list.
 * Also holds the button actions.
 * </p>
 *
 * @author Lauri Latva-Kyyny
 * @version 1.0
 */
public class GUI extends Application {

    private ShoppingList shoppingList;
    private Button addButton;
    private Button removeButton;
    private Button clearButton;
    private TableView<ShopItem> shopTable;
    private TextField itemNameInput;
    private TextField itemAmountInput;
    private Stage window;

    /**
     *  Initializes GUI by adding JavaFX elements and creates a view.
     * <p>
     * Creates a view to GUI and adds all necessary elements.
     * Holds actions for when buttons are clicked.
     * </p>
     *
     * @param window primary stage for GUI
     */
    @Override
    public void start(Stage window) {
        System.out.println("Author: Lauri Latva-Kyyny");
        window.setTitle("Shopping list");

        BorderPane borderPane = new BorderPane();
        addButton = new Button("Add");
        removeButton = new Button("Remove");
        clearButton = new Button("Clear");

        shoppingList = new ShoppingList();
        shopTable = shoppingList.getShoppingList();
        VBox vbox = new VBox(shopTable);

        // Create menu
        Menu fileMenu = new Menu("File");

        // Menu items
        MenuItem save = new MenuItem("Save");
        MenuItem open = new MenuItem("Open");
        fileMenu.getItems().add(save);
        fileMenu.getItems().add(open);

        // Main menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu);
        borderPane.setTop(menuBar);

        borderPane.setCenter(vbox);
        itemNameInput = new TextField();
        itemAmountInput = new TextField();
        itemNameInput.setPromptText("Item name");
        itemAmountInput.setPromptText("Item amount");

        HBox group = new HBox();
        group.setPadding(new Insets(10,10,10,10));
        group.setSpacing(10);
        group.getChildren().addAll(itemNameInput, itemAmountInput, addButton, removeButton, clearButton);
        borderPane.setBottom(group);

        removeButton.setOnAction(e -> shoppingList.remove());
        clearButton.setOnAction(e -> shoppingList.removeAll());

        addButton.setOnAction( e -> {
            shoppingList.add(itemNameInput.getText(), itemAmountInput.getText());
            itemNameInput.clear();
            itemAmountInput.clear();
        });

        Scene content = new Scene(borderPane, 800, 400);

        save.setOnAction(e -> {
            try {
                fileSaveClicked();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        open.setOnAction(e -> fileOpenClicked());

        window.setResizable(false);
        window.setScene(content);
        window.show();
        this.window = window;
    }

    /**
     * When save menu item is clicked writes to a user input file from filechooser.
     * <p>
     * When save menu item is clicked opens up a pc directory using FileChooser.
     * User can choose txt or json file type, if txt file type is chosen writes ShoppingList one
     * row at a time using ShopItem toString method to .txt file.
     * If JSON file type is chosen writes ShoppingList to JSON format to .json file type using JSONparser write method.
     * </p>
     *
     */
    public void fileSaveClicked() throws IOException {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save file");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File file = fileChooser.showSaveDialog(this.window);
            if(file != null && fileChooser.getSelectedExtensionFilter().getDescription().equals("Text Files")) {
                this.writeTextFileFormat(file);
            } else if(file != null && fileChooser.getSelectedExtensionFilter().getDescription().equals("JSON Files")){
                JSONParser parser = new JSONParser();
                JSONArray ja = new JSONArray();
                for(int i=shoppingList.getData().size() - 1; i >= 0 ; i--) {
                    Map<String, Object> map = parser.pojoToMap(shoppingList.getData().get(i));
                    JSONObject jo = new JSONObject();
                    jo.put(map);
                    ja.add(jo);
                }
                ja.writeToFile(file);
            }
    }

    /**
     * Writes all table items to text file using ShopItem toString method.
     *
     * @param file received file to write to
     */
    public void writeTextFileFormat(File file) {
        String path = file.getAbsolutePath();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path), "utf-8"))) {
            for(int i=0; i<shoppingList.getData().size(); i++) {
                writer.write(shoppingList.getData().get(i).toString());
                if(i+1 != shoppingList.getData().size()) {
                    writer.write("\n");
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * When open menu item is clicked opens a file to read from to table.
     */
    public void fileOpenClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(this.window);
        if (selectedFile != null) {
            shoppingList.removeAll();
                try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        // Go through the line
                        // When index i+1 equals : mark
                        // -> save name to name
                        // save amount by assuming format is banana:5 so plus one index from :
                        String name = "";
                        String amount = "";
                        String savedChars = "";
                        for(int i=0; i<line.length(); i++) {
                            savedChars += line.charAt(i) + "";
                            if(i+1 < line.length() && line.charAt(i+1) == ':') {
                                name = savedChars;
                                savedChars = "";
                                amount = line.charAt(i+2) + "";
                            }
                        }
                        shoppingList.add(name, amount);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * Main methods for launching the application.
     *
     * @param args console line parameters
     */
    public static void main(String args[]) {
        launch(args);
    }
}
