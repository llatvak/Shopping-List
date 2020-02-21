package tuni.tiko.gui;

/**
 * Class for shop items and attributes in shopping list table.
 *
 * @author Lauri Latva-Kyyny
 * @version 1.0
 */
public class ShopItem {
    private String name;
    private int amount;

    /**
     * Constructor to initialize shop item attributes.
     *
     * @param name shop item name to be added as a String
     * @param amount shop item amount to be added as an int
     */
    public ShopItem(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    /**
     * Returns shop item name as a String.
     *
     * @return shop item name as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets shop item name.
     *
     * @param name shop item name to be set as a String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns shop item amount as an int.
     *
     * @return shop item amount as an int
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets shop item amount.
     *
     * @param amount shop item amount to be added as an int
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Returns object field values.
     *
     * @return object field values as one String seperated by ':'
     */
    @Override
    public String toString() {
        return name + ":" + amount;
    }
}
