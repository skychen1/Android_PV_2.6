package android.fingeralg;


public class FingerAlg {

    static {
        System.loadLibrary("finalg");
    }


    public FingerAlg() {
        super();
    }

    public FingerAlg getInstance(){
        return new FingerAlg();
    }

    public native int AlgMatch(String pRegBuf, String pVerBuf, int nLevel);

    public native int ImageToFea(byte[] img, RetFea retval);

    public native int ThreeFeaGetTmp(String Fea1, String Fea2, String Fea3, RetFea retval);

    public static void main(String[] args) {
        System.out.println("ok");
    }
}