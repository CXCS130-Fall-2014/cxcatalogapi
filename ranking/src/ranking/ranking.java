package ranking;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

import java.io.*;
import java.io.File;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Vector;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mac on 14-11-23.
 */

public class ranking {
    public static final int MAX_LIFETIME = 2;
    public static final int FREQUENT_SH = 3;
    public static final int WAIT_TIME = 1800000;
    public void run() {
        try {
            int ctry = 10;
            // scheduling
            System.out.println("reading");
            brandnames = readingExcel("src/ranking/brandname.xlsx", "new");
            for (int i = 0; i < brandnames.size(); i++) {
                scores.put(brandnames.elementAt(i), 0);
            }
            while(ctry >= 0){
                rank();
                System.out.println("try" + ctry);
                ctry -= 1;
                cleanup();
                System.out.println(populartags);
                Thread.sleep(2000);
                lifetime --;
            }
        } catch(Exception e) {
            System.out.println("error");
            System.out.println(e);
        }
    }

    public Vector<String> readingExcel(String filename, String sheetname) throws IOException
    {
        InputStream ExcelFileToRead = new FileInputStream(filename);
        XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);

        XSSFWorkbook test = new XSSFWorkbook();
        XSSFSheet sheet;
        if (sheetname == "new") {
            sheet = wb.getSheetAt(0);
        }
        else {
            sheet = wb.getSheet(sheetname);
        }
        sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        Iterator rows = sheet.rowIterator();

        while(rows.hasNext())
        {
            row = (XSSFRow) rows.next();
            try {
                cell = row.getCell(0);
                if (cell != null) {
                    brandnames.add(cell.toString().substring(1));
                }
            } catch(Exception e) {

        }
        }
        return brandnames;
    }
    public void writingExcel(String filename) throws IOException {
        String sheetName = "mutable";//name of sheet
        System.out.println("1");
        System.out.println("2");
        InputStream ExcelFileToRead = new FileInputStream(filename);
        System.out.println("3");
        XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);
        System.out.println("4");
        XSSFSheet sheet = wb.createSheet(sheetName) ;
        System.out.println("5");

        int r = 0;
        for (String key: populartags.keySet())
        {
            if (key.length() > 1 ) {
                XSSFRow row = sheet.createRow(r);

                XSSFCell cell = row.createCell(0);

                cell.setCellValue(key);

                r++;
            }
        }
        FileOutputStream fileOut = new FileOutputStream(filename);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    private Vector<String> getTags(String keyword, String api_key, Vector<String> old_tags, int count) throws Exception{
        TumblrTags ttags = new TumblrTags();
        old_tags  = ttags.tumblrcalls(keyword, api_key, old_tags, count);
        return old_tags;
    }

    public Vector<String> getNewTags() throws Exception {
        Vector<String> new_tags = new Vector<String>();
        new_tags = getTags("clothes", "YW6bwCsUWy31u7ZWNkOGoBAeI4sqyKEgWT8Pnkhug2Z3y2MVcf", new_tags, 10);
        int size = new_tags.size();
        for(int i = 0; i < size; i++){
            String new_keywords = new_tags.get(i).toString();
            new_tags = getTags(new_keywords, "YW6bwCsUWy31u7ZWNkOGoBAeI4sqyKEgWT8Pnkhug2Z3y2MVcf", new_tags, 1);
        }

        System.out.println(new_tags);
        return new_tags;
    }

    public void cleanup() throws Exception {
        for (String key: scores.keySet()) {
            int count = scores.get(key);
            //System.out.println(key);
            //System.out.println(scores.get(key));
            if (count >=  FREQUENT_SH) {
                populartags.put(key, scores.get(key));
            }
        }
        if (lifetime < 0 ) {

            lifetime = maxlifetime;
            System.out.println("writing");
            writingExcel("src/ranking/brandname.xlsx");
            scores.clear();
            Vector<String> new_brandnames = readingExcel("src/ranking/brandname.xlsx", "mutable");
            for (int i = 0; i < brandnames.size(); i++) {
                scores.put(brandnames.elementAt(i), 0);
            }
            for (int i = 0; i < new_brandnames.size(); i++) {
                scores.put(new_brandnames.elementAt(i), 0);
            }
        }
    }

    public void rank() throws Exception {
        Vector<String> new_tags = getNewTags();
//        new_tags.add("Pink Tartan");
//        new_tags.add("Vix");
//        new_tags.add("Oilily");
//        new_tags.add("Po Campo");
//        new_tags.add("weatshirts");
        for (int i = 0; i <new_tags.size(); i ++) {
            boolean match = false;
//            System.out.println("????????");
//            System.out.println(isAlpha(new_tags.elementAt(i)));
            //if (!isAlpha(new_tags.elementAt(i))) continue;
            String new_tag = new_tags.elementAt(i);
//            System.out.println("!!!!");
//            System.out.println(i);
//            System.out.println(new_tag);
//            System.out.println("!!!!");
            for (String key: scores.keySet()) {
                double match_percent = LevenshteinDistance(key, new_tag);

                if (match_percent > 0.7 && match_percent <= 1.0) {
                    int count = 0;
                    if (scores.get(key) != null) {
                        count = scores.get(key);
                    }
                    scores.put(key, count+1);
                    match = true;
                }
            }
            if (match == false) {
                scores.put(new_tag, 0);
            }
        }
    }

//    public boolean isAlpha(String tag) {
//        char[] chars = tag.toCharArray();
//        boolean isASCII = true;
//        for (int i = 0; i < input.length(); i++) {
//            int c = input.charAt(i);
//            if (c > 0x7F) {
//                isASCII = false;
//                break;
//            }
//        }
//        return isASCII;
//        // System.out.println(tag);
//
//        return true;
//    }

    private int getmin(int a, int b, int c)
    {
        return Math.min(Math.min(a, b), c);
    }

    public double LevenshteinDistance(String s, String t){
        double result = 0.0;
        String ps = preparingString(s);
        String pt = preparingString(t);
        int n = ps.length();
        int m = pt.length();
        //if s contains t or t contains s, set result = 1.0
        if(n == 0)
        {
            result = m;

        }
        else if (m==0)
        {
            result = n;

        }
        //not vise versa need find a better algorithm here!!!!
        else if (ps.contains(pt))
        {
            boolean containflag = false;
            String [] sarr =s.split(" ");
            for(String ss : sarr)
            {
                if(pt.contains(ss))
                {
                    result =1.0;
                    containflag = true;
                    break;
                }

            }
            if(!containflag)
            {
                double x = compare(ps, n, pt, m);
                result = 1.0 - x/(Math.max(n,m));
            }


        }
        else
        {
            double x = compare(ps, n, pt, m);
            result = 1.0 - x/(Math.max(n,m));

        }
        //System.out.println(result);
        return result;

    }

    private double compare(String s, int n, String t, int m)
    {
        int matrix[][] = new int[n+1][m+1];
        for(int i = 0; i<=n; i++)
        {
            matrix[i][0] = i;
        }
        for(int i =0; i<=m; i++)
        {
            matrix[0][i]=i;

        }
        for(int i =1; i<=n; i++)
        {
            int si = s.codePointAt(i-1);
            for(int j =1; j<=m;j++)
            {
                int tj=t.codePointAt(j-1);
                int cost = si == tj?0:1;
                matrix[i][j]=Math.min(Math.min(matrix[i-1][j]+1, matrix[i][j-1]+1),matrix[i-1][j-1]+cost);

            }


        }
        return matrix[n][m];

    }

    private String preparingString(String s)
    {
        //to lower and get ride of space
        return s.toLowerCase().replaceAll(" ", "");

    }
    // right now use it as private member variable, later change to db acess
    private Vector<String> tags = new Vector<String>();
    private Map<String, Integer> scores = new HashMap<String, Integer>();
    private Vector<String> brandnames = new Vector<String>();
    private Map<String, Integer> populartags = new HashMap<String, Integer>();
    private int lifetime = MAX_LIFETIME;
    private int maxlifetime = MAX_LIFETIME;
}
