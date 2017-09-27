/**
 * Copyright(c) Guangzhou JiaxinCloud Science & Technology Ltd. 
 */
package git;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;

import conf.LocalEnv;
import projects.Modules;

/**
 * <pre>
 * java git client。
 * </pre>
 * 
 * @author 王文辉 wangwenhui@jiaxincloud.com
 * @version 1.00.00
 * @date 2017年8月29日
 * 
 *       <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *       </pre>
 */
public class GitPush {

	// 工程所在目录
	private static final String PROJECT_PATH = LocalEnv.path;
	// 工程名称
	private static final String PROJECT_NAME = Modules.project.jiaxin_gw_statistics.name();
	// 提交备注
	private static final String COMMIT_MSG = "关联im会话添加微信个人号的渠道设置";

	public static void main(String[] args) throws Exception {
		String filePath = PROJECT_PATH + PROJECT_NAME;
		File f = new File(filePath);
		Git git = Git.open(f);

		// git status
		Status status = git.status().call();
		Set<String> m = status.getModified();
		Set<String> u = status.getUntracked();
		Set<String> finalFile = new HashSet<String>();
		if (m.isEmpty() && u.isEmpty()) {
			System.out.println("没有文件修改，不需要提交");
			return;
		}
		if (!m.isEmpty()) {
			System.out.println("修改的文件列表：");
			for (String s : m) {
				// 过滤dubbox和log4j文件 不提交
				if (s.contains("x_dubbox.xml") || s.contains("log4j.xml")) {
					continue;
				}
				System.out.println(s);
				finalFile.add(s);
				git.add().addFilepattern(s).call();
			}
		}

		System.out.println("");

		if (!u.isEmpty()) {
			System.out.println("新增的文件列表：");
			for (String s : u) {
				System.out.println(s);
				finalFile.add(s);
				git.add().addFilepattern(s).call();
			}
		}
		if (finalFile.isEmpty() || finalFile.size() == 0) {
			System.out.println("不存在需要提交的文件！");
			return;
		}
		// git add
		// DirCache dirCache =
		System.out.println("文件add成功！");
		// RevCommit commit =
		git.commit().setMessage(COMMIT_MSG).setInsertChangeId(true).call();
		System.out.println("文件commit成功！");
		Iterable<PushResult> iterable = git.push().add("HEAD:refs/for/master").call();
		RemoteRefUpdate remoteUpdate = iterable.iterator().next().getRemoteUpdate("refs/for/master");
		if (remoteUpdate != null && remoteUpdate.getStatus() != null && remoteUpdate.getStatus().name().equals("OK")) {
			System.out.println("push成功！");
		} else {
			System.out.println("push失败！");
			System.out.println("执行reset HEAD^！");
			git.reset().setRef("HEAD^").call();
			System.out.println("reset HEAD^ 成功！");
		}

	}

}
