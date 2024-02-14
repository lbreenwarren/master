/**A program to compare 2 playing cards.
 * @author Liam Warren
 * @version 2/21/22*/
public class Card implements Comparable<Card> {
    String value;
    String suit;

    /**Card constructor.
     * @param suit suit input
     * @param value value input*/
    Card(String suit, String value) {
        this.value = value;
        this.suit = suit;
    }

    /**getter for value parameter.
     * @return value*/
    String getValue() {
        return value;
    }

    /**getter for suit parameter.
     * @return suit*/
    String getSuit() {
        return suit;
    }


    /**simplified equals method checking if two cards are equal. guaranteed valid input so this is all we need.
     * @param obj object which is being compared
     *@return true if equal, false if not equal*/
    public boolean equals(Card obj) {

        return obj == this;
    }

    @Override
    public int compareTo(Card o) {
        int num;
        if (value.equals("Jack")) {
            num = 11;
        } else if (value.equals("Queen")) {
            num = 12;
        } else if (value.equals("King")) {
            num = 13;
        } else if (value.equals("Ace")) {
            num = 14;
        } else {
            num = Integer.parseInt(value);
        }

        int numO;
        if (o.getValue().equals("Jack")) {
            numO = 11;
        } else if (o.getValue().equals("Queen")) {
            numO = 12;
        } else if (o.getValue().equals("King")) {
            numO = 13;
        } else if (o.getValue().equals("Ace")) {
            numO = 14;
        } else {
            numO = Integer.parseInt(o.getValue());
        }
        if (num > numO) {
            return 1;
        }
        if (num < numO) {
            return -1;
        }
        if (numO == num) {
            return 0;
        }
        return -2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        int varcode1 = null == value ? 0 : value.hashCode();
        int varcode2 = null == suit ? 0 : suit.hashCode();
        hash = (31 * hash) + varcode1 + varcode2;
        return hash;
    }

    @Override
    public String toString() {

        if (value.equals("2")) {
            value = "Two";
        }
        if (value.equals("3")) {
            value = "Three";
        }
        if (value.equals("4")) {
            value = "Four";
        }
        if (value.equals("5")) {
            value = "Five";
        }
        if (value.equals("6")) {
            value = "Six";
        }
        if (value.equals("7")) {
            value = "Seven";
        }
        if (value.equals("8")) {
            value = "Eight";
        }
        if (value.equals("9")) {
            value = "Nine";
        }
        if (value.equals("10")) {
            value = "Ten";
        }
        return value + " of " + suit;
    }
}
