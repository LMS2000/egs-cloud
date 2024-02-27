package com.lms.generator.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class StaticGenerator {

    private static void copyFileByRecursive(File inputFile, File outputFile) throws IOException {

      if(inputFile.isDirectory()){
          System.out.println(inputFile.getName());
          File destOutputFile = new File(outputFile, inputFile.getName());
          if (!destOutputFile.exists()) {
              destOutputFile.mkdirs();
          }
          // 获取目录下的所有文件和子目录
          File[] files = inputFile.listFiles();
          // 无子文件，直接结束
          if (ArrayUtil.isEmpty(files)) {
              return;
          }
          for (File file : files) {
              // 递归拷贝下一层文件
              copyFileByRecursive(file, destOutputFile);
          }
      }else {
          // 是文件，直接复制到目标目录下
          Path destPath = outputFile.toPath().resolve(inputFile.getName());
          Files.copy(inputFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
      }


    }
    public static void copyFileByRecursive(String inputPath, String outputPath) throws IOException {

        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);

        try {
            copyFileByRecursive(inputFile, outputFile);
        } catch (Exception e) {
            System.err.println("文件复制失败");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //获取整个根目录
        String property = System.getProperty("user.dir");
        File file=new File(property).getParentFile();
        //输入目录，ACM实列模板的目录
        String inputPath=new File(file,"lmszi-generator-demo-projects/acm-template").getAbsolutePath();
        String ouputPath=property;
        //拷贝到当前目录
        copeFilesByHuTool(inputPath,ouputPath);


    }
    public static  void copeFilesByHuTool(String inputPath,String ouputPath){
        FileUtil.copy(inputPath,ouputPath,true);
    }}


