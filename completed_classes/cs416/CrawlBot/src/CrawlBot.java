
public class CrawlBot implements Robot {

    int xPosition;
    int yPosition;
    int direction;

    CrawlBot(){
        xPosition = 0;
        yPosition = 0;
        direction = 0;
    }

    CrawlBot(int x, int y, int dir){
        xPosition = x;
        yPosition = y;
        direction = dir;
    }

    int getX(){
        return xPosition;
    }

    int getY(){
        return yPosition;
    }
    int getDirection(){
        return direction;
    }
    public void moveForward(int dist) {
        if (direction == 90) {
            yPosition = yPosition + dist;
        }
        if (direction == 270) {
            yPosition = yPosition - dist;
        }
        if (direction == 180) {
            xPosition = xPosition - dist;
        }
        if (direction == 0) {
            xPosition = xPosition + dist;
        }
    }


    public void moveBackward(int dist) {
        if (direction == 90) {
            yPosition = yPosition - dist;
        }
        if (direction == 270) {
            yPosition = yPosition + dist;
        }
        if (direction == 180) {
            xPosition = xPosition + dist;
        }
        if (direction == 0) {
            xPosition = xPosition - dist;
        }
    }


    public void turnLeft() {
        direction = direction + 90;
        if(direction == 360){
            direction = 0;
        }
    }


    public void turnRight() {
        direction = direction - 90;
        if (direction == -90){
            direction = 270;
        }
    }

    public String toString() {
        return "CrawlBot at (" + xPosition + ", " + yPosition + ") heading " + direction + " degrees";
    }

    public static void main(String[] args){
        CrawlBot bot = new CrawlBot(100, 100, 90);
        bot.moveBackward(10);
        System.out.println(bot.toString());

    }
}
