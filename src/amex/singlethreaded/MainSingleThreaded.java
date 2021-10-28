package amex.singlethreaded;
import java.io.FileNotFoundException;

public class MainSingleThreaded {

    public static void main(String[] args) throws Exception {
        FileProcessor pf = new FileProcessor();

        // if number of arguments passed != 2, throw an exception
        if (args.length != 2){
            throw new IllegalArgumentException(" 2 arguments needs to be passed, 1. input file location 2. Boolean argument indicating  if file has a header");
        }

        Boolean hasHeader = true;
        if (args[1].equals("false")) {
            hasHeader = false;
        }
        String input = args[0];
        pf.processFile(input, hasHeader);
        System.out.print(" Final Formatted Result : " + pf.finalHM);
    }
}
