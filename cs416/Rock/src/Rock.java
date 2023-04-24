public class Rock {
    public String name;
    public double pounds;
    public double volume;

    //getters and setters for each field
    public Rock(String rockname) {
        name = rockname;
    }
    String getName(){
        return name;
    }
    public void setName(String rockname){
        name = rockname;
    }
    void setVolume(double vol) {
        volume = vol;
    }

    public void setNumPounds(double weight) {
        pounds = weight;
    }
    public double getNumPounds(){
        return pounds;
    }
    public double getVolume(){
        return volume;
    }

    //calculate density as an integer using pounds and volume
    public int calculateDensity(){
        return (int)pounds/(int)volume;
    }

    //increase pounds value by a given increment
    public void increasePounds(double increment){
        pounds =  pounds + increment;
    }

    //decrease pounds value by a given increment
    public double decreasePounds(double decrement){
        pounds = pounds - decrement;
        return pounds;
    }

    //print class info
    public String toString() {
        int density = calculateDensity();
        return String.format("Rock %s weighs %1.3f pounds with a density of %d", name, pounds, density);
    }


}