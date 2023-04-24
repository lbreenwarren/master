/**a program which manipulates color values.
*@author lbw
*@version 2/9/23*/
public class Color {


    static final Color RED = new Color(255, 0, 0);
    static final Color BLACK = new Color(0, 0, 0);
    static final Color GREEN = new Color(0, 255, 0);
    static final Color YELLOW = new Color(255, 255, 0);
    static final Color BLUE = new Color(0, 0, 255);
    static final Color MAGENTA = new Color(255, 0, 255);
    static final Color CYAN = new Color(0, 255, 255);
    static final Color WHITE = new Color(255, 255, 255);

    private int red;
    private int green;
    private int blue;

    /**class constructor, doesnt allow values less than 0 or greater than 255.
     * @param inblue value beint assigned to blue parameter
     * @param ingreen value being assigned to green parameter
     * @param inred value being assigned to red parameter*/
    public Color(int inred, int ingreen, int inblue) {

        if (inred < 0) {
            inred  = 0;
        }
        if (inred > 255) {
            inred = 255;
        }
        if (inblue < 0) {
            inblue  = 0;
        }
        if (inblue > 255) {
            inblue = 255;
        }
        if (ingreen < 0) {
            ingreen  = 0;
        }
        if (ingreen > 255) {
            ingreen = 255;
        }

        red = inred;
        green = ingreen;
        blue = inblue;

    }

    /**getter for red parameter.
     * @return value of red*/
    public int getred() {
        return red;
    }

    /**setter for red parameter.
     * @param val value for red to be set*/
    public void setred(int val) {

        red = val;

        if (red < 0) {
            red = 0;
        }

        if (red > 255) {
            red = 255;
        }
    }

    /**getter for green paramater.
     * @return value of green*/
    public int getgreen() {
        return green;
    }

    /**setter for green param.
     * @param val  value for green to be set*/
    public void setgreen(int val) {

        green = val;

        if (green < 0) {
            green = 0;
        }
        if (green > 255) {
            green = 255;
        }
    }

    /**getter for blue paramater.
     * @return value of blue*/
    public int getblue() {
        return blue;
    }

    /**setter for blue param.
     * @param val  value for blue to be set*/
    public void setblue(int val) {

        blue = val;

        if (blue < 0) {
            blue = 0;
        }
        if (blue > 255) {
            blue = 255;
        }
    }

    /**converts int value into hex string.
     @return hex value of color as string**/

    public String toString() {
        String hexred = Integer.toHexString(red);
        String hexgrn = Integer.toHexString(green);
        String hexblue = Integer.toHexString(blue);

        if (red == 0) {
            hexred = "00";
        }
        if (green == 0) {
            hexgrn = "00";
        }
        if (blue == 0) {
            hexblue = "00";
        }
        if ((red < 16) && (red > 0)) {
            hexred = "0" + hexred;
        }
        if ((green < 16) && (green > 0)) {
            hexgrn = "0" + hexgrn;
        }
        if ((blue < 16) && (blue > 0)) {
            hexblue = "0" + hexblue;
        }

        return "#" + hexred + "" + hexgrn + "" + hexblue + "";
    }

    /** sums the rgb values of 2 colors together.
     * @param addColor color to be added
     * @return new color with summed rgb values
     */
    public Color add(Color addColor) {

        Color sumColor = new Color(0, 0, 0);
        int redsum = red + addColor.getred();
        sumColor.setred(redsum);

        int greensum = green + addColor.getgreen();
        sumColor.setgreen(greensum);

        int bluesum = blue + addColor.getblue();
        sumColor.setblue(bluesum);

        return sumColor;

    }

    /** subtracts rgb value of one color from another.
     * @param subColor color to be subtracted
     * @return returns new color with subtracted rgb values*/
    public Color sub(Color subColor) {

        Color sumColor = new Color(0, 0, 0);
        int redsum = red - subColor.getred();
        sumColor.setred(redsum);

        int greensum = green - subColor.getgreen();
        sumColor.setgreen(greensum);

        int bluesum = blue - subColor.getblue();
        sumColor.setblue(bluesum);

        return sumColor;

    }

    /**decreases each rgb value of a collor by 20 percent.
     * @return color with dimmed values*/
    public Color dim() {
        Color dimcolor = new Color(0, 0, 0);

        double dimred = red * .8;
        int newred = (int) dimred;
        dimcolor.setred(newred);

        double dimgreen = green * .8;
        int newgreen = (int) dimgreen;
        dimcolor.setgreen(newgreen);

        double dimblue = blue * .8;
        int newblue = (int) dimblue;
        dimcolor.setblue(newblue);
        return dimcolor;
    }

    /**decreases each color value by 32.
     * @return new color with darkened values*/
    public Color darken() {

        Color sumColor = new Color(0, 0, 0);
        int redsum = red - 32;
        sumColor.setred(redsum);

        int greensum = green - 32;
        sumColor.setgreen(greensum);

        int bluesum = blue - 32;
        sumColor.setblue(bluesum);

        return sumColor;

    }

    /**compares two colors to see if theyre the same.
     * @param cmpcolor color to compare to
     * @return true if the same */
    boolean equals(Color cmpcolor) {

        if ((red == (cmpcolor.getred())) && (green == (cmpcolor.getgreen())) && (blue == cmpcolor.getblue())) {
            return true;
        }
        return false;
    }

    /** increase rgb values of color by 32.
     * @return new color with increased values */
    public Color lighten() {

        Color sumColor = new Color(0, 0, 0);
        int redsum = red + 32;
        sumColor.setred(redsum);

        int greensum = green + 32;
        sumColor.setgreen(greensum);

        int bluesum = blue + 32;
        sumColor.setblue(bluesum);

        return sumColor;

    }

    /**increase values of color by 20 percent.
     * @return returns new color with increased values*/
    public Color brighten() {
        Color brightcolor = new Color(0, 0, 0);

        double brightred = red * 1.2;
        int newred = (int) brightred;
        brightcolor.setred(newred);

        double brightgreen = green * 1.2;
        int newgreen = (int) brightgreen;
        brightcolor.setgreen(newgreen);

        double brightblue = blue * 1.2;
        int newblue = (int) brightblue;
        brightcolor.setblue(newblue);
        return brightcolor;
    }


}

