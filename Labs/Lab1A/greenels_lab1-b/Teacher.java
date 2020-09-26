public class Teacher {
    private String TlastName;
    private String TfirstName;
    private int classRoom;

    public Teacher(String TlastName, String TfirstName, int classRoom)
    {
        this.TlastName = TlastName;
        this.TfirstName = TfirstName;
        this.classRoom = classRoom;
    }

    public String getTlastName(){ return TlastName;}
    public String getTfirstName(){ return TfirstName;}
    public int getClassRoom(){ return classRoom;}
}
