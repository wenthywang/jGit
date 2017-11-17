/**
 * Copyright(c) Guangzhou JiaxinCloud Science & Technology Ltd. 
 */
package git;

import java.io.File;

import org.eclipse.jgit.api.Git;

import conf.LocalEnv;

/**
 * <pre>
 * git clone。
 * </pre>
 * 
 * @author 王文辉 wangwenhui@jiaxincloud.com
 * @version 1.00.00
 * @date 2017年9月27日
 * 
 *       <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *       </pre>
 */
public class GitClone {
	//远程仓库
	private static final String remotePath = "git@github.com:eclipse/jgit.git";
	//本地保存路径
	private static final String localPath = LocalEnv.path + "jGitSource";

	public static void main(String[] args) throws Exception {
		File localRepo=new File(localPath);
     if(localRepo.isDirectory()){
    	 deleteDirectory(localPath);
     }else{
    	 localRepo.deleteOnExit();
     }
		
		Git git=Git.cloneRepository().setURI(remotePath).
		setBranch("master").
		setDirectory(localRepo).call();
		System.out.println(git.getRepository().getConfig().toText());
	}
	
    // 删除目录  
    public static boolean deleteDirectory(String filePath) {  
        boolean flag = false;  
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!filePath.endsWith(File.separator)) {  
            filePath = filePath + File.separator;  
        }  
        File dirFile = new File(filePath);  
        // 如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {  
            return false;  
        }  
        flag = true;  
        // 删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            // 删除子文件  
            if (files[i].isFile()) {  
                flag = deleteFile(files[i].getAbsolutePath());  
                if (!flag)  
                    break;  
            } // 删除子目录  
            else {  
                flag = deleteDirectory(files[i].getAbsolutePath());  
                if (!flag)  
                    break;  
            }  
        }  
        if (!flag)  
            return false;  
        // 删除当前目录  
        if (dirFile.delete()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
  

// 删除文件  
public static boolean deleteFile(String filePath) {  
    boolean flag = false;  
    File file = new File(filePath);  
    // 路径为文件且不为空则进行删除  
    if (file.isFile() && file.exists()) {  
        file.delete();  
        flag = true;  
    }  
    return flag;  
}  
}
