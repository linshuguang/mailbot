package com.me.utils;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by kenya on 2017/11/24.
 */
public class FileUtils {

    private static String readFromStream(InputStream is) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0) {
                break;
            }
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    public static String readStringInClassPath(String name) throws IOException {
        if(!name.startsWith("/")){
            name = "/" + name;
        }
        InputStream is = FileUtils.class.getResourceAsStream(name);
        return readFromStream(is);
    }

    public static InputStream readStreamInClassPath(String name) throws IOException{
        String content = readStringInClassPath(name);
        return  new ByteArrayInputStream(content.getBytes());
    }

    public static String readStringInFileSystem(String pathName) throws IOException {
        /*
        String defaultEncoding = "UTF-8";
        InputStream inputStream = new FileInputStream(initialFile);
        try {
            BOMInputStream bOMInputStream = new BOMInputStream(inputStream);
            ByteOrderMark bom = bOMInputStream.getBOM();
            String charsetName = bom == null ? defaultEncoding : bom.getCharsetName();
            InputStreamReader reader = new InputStreamReader(new BufferedInputStream(bOMInputStream), charsetName);
            StringBuffer sb = new StringBuffer();
            reader.
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            //use reader
        } finally {
            inputStream.close();
        }
        */

        File initialFile = new File(pathName);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(initialFile), "UTF8"));

        InputStream targetStream = new FileInputStream(initialFile);
        return readFromStream(targetStream);
        //return sb.toString();
    }

    public static InputStream readStreamInFileSystem(String pathName) throws IOException {
        String content = readStringInFileSystem(pathName);
        return  new ByteArrayInputStream(content.getBytes());
    }

    public static File createTempDir(String prefix)
            throws IOException
    {
        String tmpDirStr = System.getProperty("java.io.tmpdir");
        if (tmpDirStr == null) {
            throw new IOException(
                    "System property 'java.io.tmpdir' does not specify a tmp dir");
        }

        File tmpDir = new File(tmpDirStr);
        if (!tmpDir.exists()) {
            boolean created = tmpDir.mkdirs();
            if (!created) {
                throw new IOException("Unable to create tmp dir " + tmpDir);
            }
        }

        File resultDir = null;
        int suffix = (int)System.currentTimeMillis();
        int failureCount = 0;
        do {
            resultDir = new File(tmpDir, prefix + suffix % 10000);
            suffix++;
            failureCount++;
        }
        while (resultDir.exists() && failureCount < 50);

        if (resultDir.exists()) {
            throw new IOException(failureCount +
                    " attempts to generate a non-existent directory name failed, giving up");
        }
        boolean created = resultDir.mkdir();
        if (!created) {
            throw new IOException("Failed to create tmp directory");
        }

        return resultDir;
    }

    private static final int BUFFER_SIZE = 4096;
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param srcZip
     * @param destDirectory
     * @throws IOException
     */
    public static void unzip(InputStream srcZip, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(srcZip);
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            File destinationPath = new File(destDirectory, entry.getName());
            destinationPath.getParentFile().mkdirs();

            if (entry.isDirectory()) {
                File dir = new File(filePath);
                dir.mkdir();
            } else {
                extractFile(zipIn, filePath);
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    /*
    public static void unzip(InputStream srcZip, String destDir) throws Exception {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            ZipInputStream zis = new ZipInputStream(srcZip);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){

                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("destDir:"+destDir);
                System.out.println("fileName:"+fileName);

                if(ze.isDirectory()){
                    newFile.mkdirs();
                }else {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    private static void realZipFile(String path, String srcFile, ZipOutputStream zipOut) throws IOException {
        File file = new File(srcFile);
        String filePath = "".equals(path) ? file.getName() : path + "/" + file.getName();

        if (file.isDirectory()) {
            for (String fileName : file.list()) {
                realZipFile(filePath, srcFile + "/" + fileName, zipOut);
            }
        } else {
            zipOut.putNextEntry(new ZipEntry(filePath));
            FileInputStream in = new FileInputStream(srcFile);

            byte[] buffer = new byte[1024 * 4];
            int len;
            while ((len = in.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
            }
        }
    }

    public static void zipFile(String fileToZip, String dstZip) throws IOException {

        FileOutputStream fos = new FileOutputStream(new File(dstZip));
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File srcFile = new File(fileToZip);

        if(srcFile.isDirectory()) {
            for(String fn : srcFile.list()) {
                realZipFile("", fileToZip + "/" + fn, zipOut);
            }
        } else {
            realZipFile("", fileToZip, zipOut);
        }
        //realZipFile(fileToZip, fileName, zipOut);

        zipOut.flush();
        zipOut.close();
        fos.close();
    }



    public static void zip(String srcDirectory, String dstPath){
            File directoryToZip = new File(srcDirectory);
            List<File> fileList = new ArrayList<File>();
            //System.out.println("---Getting references to all files in: " + directoryToZip.getCanonicalPath());
            getAllFiles(directoryToZip, fileList);
            //System.out.println("---Creating zip file");
            writeZipFile(directoryToZip, fileList);
            //System.out.println("---Done");

    }


    private static void getAllFiles(File dir, List<File> fileList) {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                fileList.add(file);
                if (file.isDirectory()) {
                    System.out.println("directory:" + file.getCanonicalPath());
                    getAllFiles(file, fileList);
                } else {
                    System.out.println("     file:" + file.getCanonicalPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeZipFile(File directoryToZip, List<File> fileList) {

        try {
            FileOutputStream fos = new FileOutputStream(directoryToZip.getName() + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (File file : fileList) {
                if (!file.isDirectory()) { // we only zip files, not directories
                    addToZip(directoryToZip, file, zos);
                }
            }

            zos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,
            IOException {

        FileInputStream fis = new FileInputStream(file);

        // we want the zipEntry's path to be a relative path that is relative
        // to the directory being zipped, so chop off the rest of the path
        String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
                file.getCanonicalPath().length());
        System.out.println("Writing '" + zipFilePath + "' to zip file");
        ZipEntry zipEntry = new ZipEntry(zipFilePath);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }


}
