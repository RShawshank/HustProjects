package demo;

import java.io.*;

public class Solution {

    public static void main(String[] args) {
        File file=new File("D:\\java_program\\leecode\\out\\production\\leecode\\demo\\triangle_test.txt");
        try
        {
            FileInputStream fileInputStream=new FileInputStream(file);
            int[] number=new int[3];
            int j=0;
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            line = bufferedReader.readLine();
            while(line!=null)
            {
                line=line.replaceAll(" ","");
                if(line.charAt(0)=='/')
                {
                    System.out.println();
                    System.out.println(line.substring(2));
                }
               else {
                  for(int i=0;i<line.length();)
                  {
                      //取出负数
                      if(line.charAt(i)=='-')
                      {
                          number[j++]=Integer.parseInt(line.substring(i,i+2));
                          i=i+2;
                      }
                      else
                      {
                          number[j++]=line.charAt(i++)-'0';
                      }
                      if(j==3)//取三个数字
                          break;
                  }
                    System.out.print(triangle_type(number[0], number[1], number[2])+" ");

                }
                line = bufferedReader.readLine();
                j=0;
            }

        }
        catch (FileNotFoundException e) {

            System.out.println("文件不存在或者文件不可读或者文件是目录");
        }
        catch (IOException e)
        {
        }

    }
    public static int triangle_type(int a,int b,int c)
    {
        if(a<=0||b<=0||c<=0||a>=b+c||b>=a+c||c>=a+b)
            return 0;
        else
        {
            if(a==b&&b==c)
                return 3;
            else if((a==b&&b!=c)||(a==c&&a!=b)||(b==c&&a!=b))
                return 2;
            else
                return 1;
        }
    }

}