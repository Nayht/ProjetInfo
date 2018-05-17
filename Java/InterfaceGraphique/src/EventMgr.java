import objects.abstracts.SetOfObjects;

public class EventMgr {
    private SetOfObjects setOfObjects;

    public EventMgr(SetOfObjects setOfObjects) {
        this.setOfObjects = setOfObjects;
    }

    public void manage(String event) {
        if (event.equals("LEFT")) {
            setOfObjects.setToSlideLeft(true);
        } else if (event.equals("RIGHT")) {
            setOfObjects.setToSlideRight(true);
        }
    }
}
