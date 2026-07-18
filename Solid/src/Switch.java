public class Switch {
    private Bulb bulb;

    public Switch(Bulb bulb) { //DIP
        this.bulb = bulb;
    }
    public void press()
    {
        this.bulb.turnOn();
    }
}
