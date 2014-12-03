package com.shopzilla.service.shoppingcart.resource;

import java.io.*;
import java.io.File;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Vector;
import java.util.Iterator;
import java.util.List;


public class FuzzyMatch{
	public Vector<String> readingExcel(String filename) throws IOException
	{
			//System.out.println(filename);
			//System.out.println(System.getProperty("user.dir"));
			//for debug
			//File folder = new File("src/main/java/com/shopzilla/service/shoppingcart/resource");
			//File[] listOfFiles = folder.listFiles();

			//for(int i =0 ; i<listOfFiles.length;i++)
			//{
			//	System.out.println(listOfFiles[i].getName());
			//}


			InputStream ExcelFileToRead = new FileInputStream(filename);
			XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);
			
			XSSFWorkbook test = new XSSFWorkbook(); 
			
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row; 
			XSSFCell cell;
	 
			Iterator rows = sheet.rowIterator();
 
		    Vector<String> brandnames = new Vector<String>();

		    while(rows.hasNext())
		    {
		    	row = (XSSFRow) rows.next();
		    	cell = row.getCell(0);
		        if(cell != null) {
		               brandnames.add(cell.toString().substring(1));
		        }

		    }
		return brandnames;
		  
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




}