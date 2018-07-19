package cn.rivamed.model;

/**
 * @Author 郝小鹏
 * @Description
 * @Date: Created in 2018-07-11 13:15
 * @Modyfied By :
 */
public class TagInfo {
    int ant;
    int rssi;
    String pc;

    public int getAnt() {
        return ant;
    }

    public void setAnt(int ant) {
        this.ant = ant;
    }


    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public TagInfo(){}

    public TagInfo(int rssi,int ant,String pc){
        this.rssi=rssi;
        this.ant=ant;
        this.pc=pc;
    }

}
