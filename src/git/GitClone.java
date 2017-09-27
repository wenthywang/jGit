/**
 * Copyright(c) Guangzhou JiaxinCloud Science & Technology Ltd. 
 */
package git;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

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

	public static void main(String[] args) throws InvalidRemoteException, TransportException, GitAPIException {
		Git.cloneRepository().setURI(remotePath).setBranch("master").setDirectory(new File(localPath)).call();
	}
}
