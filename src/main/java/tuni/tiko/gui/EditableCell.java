package tuni.tiko.gui;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

/**
 * Holds information and actions about cell editing.
 *
 * @author Lauri Latva-Kyyny
 * @version 1.0
 */
public class EditableCell extends TableCell<ShopItem, String> {

        private TextField textField;

        /**
         * Creates a text field for user input if there is data in the cell.
         */
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }

        /**
         * Called when edit is cancelled setting the previous data to the cell.
         */
        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText((String) getItem());
            setGraphic(null);
        }

        /**
         * Sets the inserted data to cell.
         *
         * @param item data to insert
         * @param empty boolean value to check if cell is empty
         */
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        /**
         * Creates text field for data to be added.
         */
        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.focusedProperty()
                    .addListener(
                            (ObservableValue<? extends Boolean> arg0, Boolean arg1,
                             Boolean arg2) -> {
                                if (!arg2) {
                                    commitEdit(textField.getText());
                                }
                            });
        }

        /**
         * Returns item as String if it is not null else returns empty string.
         */
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
}
