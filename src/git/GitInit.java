/**
 * Copyright(c) Guangzhou JiaxinCloud Science & Technology Ltd. 
 */
package git;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.URIish;

/**
 * <pre>
 *git init 
 *用于第一次创建本地repo 关联远程repo
 *并push 到远程repo
 *有bug 仍需测试
 * </pre>
 * 
 * @author 王文辉 wangwenhui@jiaxincloud.com
 * @version 1.00.00
 * @date 2017年9月26日
 * 
 *       <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *       </pre>
 */
public class GitInit {

	// 工程所在目录
	private static final String PROJECT_PATH = "D:\\project\\jGit";

	private static final String REMOTE_REPO_PATH = "git@github.com:wenthywang/jGit.git";

	public static void main(String[] args) throws Exception {
		
		File dir = new File(PROJECT_PATH);

		Git git = Git.init().setDirectory(dir).call();

		git.add().addFilepattern(".").call();
		git.commit().setCommitter("wenthywang", "946374340@qq.com").setMessage("使用jgit 进行git init操作").call();
		URIish remoteUrl = new URIish(REMOTE_REPO_PATH);
		RemoteAddCommand rac = git.remoteAdd();
		rac.setUri(remoteUrl);
		rac.setName("origin");
		rac.call();
		Iterable<PushResult> resultIterable = git.push().call();
	}
}
