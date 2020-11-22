package cn.yb.datawaiter.tools;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理工具类
 */
public class FileTool {


    /**
     * @param path
     * @return 获取文件的名字 包含文件后缀名
     */
    public static String getFileName(String path) {
        if (path == null) {
            return null;
        }
        int index = path.lastIndexOf("/");
        if (index == -1) {
            index = path.lastIndexOf("\\");
        }
        if (index != -1 && index != path.length() - 1) {
            return path.substring(index + 1);
        }
        return path;
       /* Path.GetFileNameWithoutExtension
        Path.GetExtension
        Path.GetFileName*/
    }

    /***
     *
     * @param path
     * @return 返回不具有后缀名的 文件名
     */
    public static String getFileNameWithoutExtension(String path) {
        String fileName = getFileName(path);
        if (fileName == null) {
            return null;
        }
        int index = fileName.indexOf(".");
        if (index != -1) {
            return fileName.substring(0, index);
        }
        return null;
    }

    /**
     * @param path
     * @return 返回后缀名
     */
    public static String getExtension(String path) {
        String fileName = getFileName(path);
        if (fileName == null) {
            return null;
        }
        int index = fileName.indexOf(".");
        if (index != -1 && index != fileName.length() - 1) {
            return fileName.substring(index + 1);
        }
        return null;
    }

    /**
     * @param path
     * @return 获取文件路径
     */
    public static String getDir(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            return path;
        } else {
            return file.getParent();
        }
    }

    /**
     * 文件复制
     *
     * @param source
     * @param dest
     * @return
     */
    public static boolean copyFile(String source, String dest) {
        FileChannel input = null;
        FileChannel output = null;
        try {
            File dir = new File(getDir(dest));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            input = new FileInputStream(new File(source)).getChannel();
            output = new FileOutputStream(new File(dest)).getChannel();
            output.transferFrom(input, 0, input.size());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 检查文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean exitFile(String filePath) {
        boolean bl = (filePath == null || filePath.trim() == "") ? false : new File(filePath).exists();
        return bl;

    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 检查文件夹是否存在
     *
     * @param dir
     * @param isCreated
     * @return
     */
    public static boolean exitsDir(File dir, boolean isCreated) {
        if (dir.exists()) {
            return true;
        } else {
            if (isCreated) {
                return dir.mkdirs();
            }
        }
        return false;
    }

    /**
     * 检查文件夹是否存在
     *
     * @param dir
     * @param isCreated
     * @return
     */
    public static boolean exitsDir(String dir, boolean isCreated) {
        File f = new File(dir);
        return exitsDir(f, isCreated);
    }

    /**
     * 下载文件
     *
     * @param response
     * @param filePath 文件路径
     */
    public static void DownLoadFile(HttpServletResponse response, String filePath) {

        response.reset();
        //response.setContentType("application/x-msdownload;charset=utf-8");
        //response.setContentType("image/png");
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            InputStream f = new FileInputStream(filePath);
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((FileTool.getFileName(filePath)).getBytes("gbk"), "iso-8859-1"));//下载文件的名称
            ServletOutputStream sout = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(f);
                bos = new BufferedOutputStream(sout);
                byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
                bos.flush();
                bos.close();
                bis.close();
            } catch (final IOException e) {
                throw e;
            } finally {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

    }

    /**
     * @param multipartFile 数据源
     * @param savePath
     * @return
     */

    public static Boolean savefile(MultipartFile multipartFile, String savePath) {
        try {
            File file = new File(savePath);
            if (file.exists()) {
                file.delete();//如果已经存在，删除文件
            }
            FileTool.exitsDir(file.getParent(), true);
            //根据srcFile大小，准备一个字节数组
            byte[] bytes = multipartFile.getBytes();
            //拼接上传路径
            //Path path = Paths.get(UPLOAD_FOLDER + srcFile.getOriginalFilename());
            //通过项目路径，拼接上传路径
            Path path = Paths.get(savePath);
            //** 开始将源文件写入目标地址
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 删除 所有的文件
     *
     * @param path
     * @return 删除成功 或者 没有文件夹返回 true
     */
    private static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String findFile(String dir, String flag) {
        File dirFile = new File(dir);
        List<File> dirs = new ArrayList<>();
        for (File tem : dirFile.listFiles()) {
            if (tem.isFile()) {
                String name = tem.getName();
                if (name.contains(".")) {
                    if (name.endsWith(flag)) {
                        return tem.getAbsolutePath();
                    }
                }
            } else {
                dirs.add(tem);
            }
        }
        for (File tem : dirs) {
            String path = findFile(tem.getAbsolutePath(), flag);
            if (path != null) {
                return path;
            }
        }
        return null;
    }

    public static void downFile(HttpServletResponse res, String saveZip) throws IOException {
        if (saveZip == null || !new File(saveZip).exists()) {
            return;
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        // 设置信息给客户端不解析
        String type = new MimetypesFileTypeMap().getContentType(saveZip);
        // 设置contenttype，即告诉客户端所发送的数据属于什么类型
        response.setHeader("Content-type", type);
        // 设置编码
        String hehe = new String("VR.zip".getBytes("utf-8"), "iso-8859-1");
        // 设置扩展头，当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
        response.setHeader("Content-Disposition", "attachment;filename=" + hehe);
        // 发送给客户端的数据
        OutputStream outputStream = res.getOutputStream();
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        // 读取filename
        bis = new BufferedInputStream(new FileInputStream(new File(saveZip)));
        int i = bis.read(buff);
        while (i != -1) {
            outputStream.write(buff, 0, buff.length);
            outputStream.flush();
            i = bis.read(buff);
        }
    }

    /**
     * 查出相对路劲
     *
     * @param basePath
     * @param path
     * @return
     */
    public static String findRelativze(String basePath, String path) {
        Path pathAbsolute = Paths.get(path);
        Path pathBase = Paths.get(basePath);
        Path pathRelative = pathBase.relativize(pathAbsolute);
        return pathRelative.toString();
    }

    public static boolean writeStrs(List<String> strs, String path) {
        File text = new File(path);
        try {
            text.createNewFile();
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(text, false);
            for (String str : strs) {
                fileWriter.write(str + "\r\n");
            }
            fileWriter.flush();
            fileWriter.close();
            return  true;
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
    }
}
