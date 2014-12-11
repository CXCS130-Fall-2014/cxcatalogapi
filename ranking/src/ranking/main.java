package ranking;

/**
 * Created by mac on 14-11-23.
 */
public class main {
    public static void main(String[] args) {
        String cat = "cars";
        if (args.length == 1 ) {
            cat = args[0];
        }
        ranking ranking_obj = new ranking();
        ranking_obj.run(cat);
    }
}
