package archon.tp_yarr_app.Database;

public class SubredditItem {
    private int position;
    private String sname;
    private String ID36;

    public int getPosition(){
        return position;
    }

    public String getSname() {
        return sname;
    }

    public String getID36() {
        return ID36;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }
    public void setID36(String ID36) {
        this.ID36 = ID36;
    }
    public String toString() {
        return sname;
    }
}
