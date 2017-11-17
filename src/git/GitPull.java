/**

 * Copyright(c) Guangzhou JiaxinCloud Science & Technology Ltd. 
 */
package git;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.util.StringUtils;

import conf.LocalEnv;

/**
 * <pre>
 * stash 
 * fetch
 * rebase
 * stash pop
 * 产生冲突会出现异常
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
public class GitPull {

	private static ExecutorService executor = Executors.newFixedThreadPool(20);
	
	public static void main(String[] args) throws Exception {
		File dir = new File(LocalEnv.path);
		// 过滤佳信的工程出来
		String[] jiaxinProjects = dir.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.contains("jiaxin")) {
					return true;
				} else {
					return false;
				}
			}
		});

		for (int i = 0; i < jiaxinProjects.length; i++) {
			final String projectName = jiaxinProjects[i];
			// 不需要demo project 的更新
			if ("jiaxin_demo_projects".equals(projectName)) {
				continue;
			}
			executor.execute(()->{
				gitFetch(projectName);
			});
			
			}
		//上面提交的任务 等待执行完 再 shutdown
		//shutdown只是拒绝新的任务
		//执行到这里已经没有新的任务添加
		//所以这个位置关闭是正常的
		executor.shutdown();
		}


	/**
	 * 调用git fetch gitRebase 合并代码
	 * 
	 * @param projectName
	 *            工程名称
	 */
	private static void gitFetch(String projectName) {
		System.out.println("------------------------------------");
		System.out.println("["+projectName+"]" + "......." + "start fetch......");
		String filePath = LocalEnv.path + projectName;
		File f = new File(filePath);
		Git git = null;
		try {
			git = Git.open(f);
		} catch (IOException e) {
			System.out.println("["+projectName+"]"+"git  init  error");
			e.printStackTrace();
		}
		
		//checkout 修改过的这些配置文件 避免冲突
    try {
    	List<String>paths=new ArrayList<String>();
    	paths.add("WebRoot/META-INF/x_config.xml");
    	paths.add("WebRoot/META-INF/x_dubbox.xml");
    	paths.add("WebRoot/META-INF/log4j.xml");
		git.checkout().addPaths(paths).call();
	} catch (GitAPIException e3) {
		// TODO Auto-generated catch block
		e3.printStackTrace();
	}
		
		
		
		Status status = null;
		try {
			status = git.status().call();
		} catch (NoWorkTreeException | GitAPIException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Set<String> m = status.getModified();

		if (!m.isEmpty()) {
			for (String s : m) {
				// 过滤dubbox和log4j文件 不提交
				if (s.contains("x_dubbox.xml") || s.contains("log4j.xml")) {
				   git.checkout().addPath(s);
				}
			}
		}
		
		
		// git status
		RevCommit revCommit = null;
		try {
			revCommit = git.stashCreate().call();
		} catch (GitAPIException e) {
			System.out.println("["+projectName+"]"+ " git stash  error");
			e.printStackTrace();
		}
		if (revCommit != null) {
			System.out.println("["+projectName+"]"+"stash 成功");
		} else {
			System.out.println("["+projectName+"]"+"没有做任何修改，不用stash");
		}
		FetchResult fr = null;
		try {
			fr = git.fetch().call();
		} catch (GitAPIException e) {
			System.out.println("["+projectName+"]"+"git fetch error");
			e.printStackTrace();
		}

		if (fr != null) {
			String msg = fr.getMessages();
			if (!StringUtils.isEmptyOrNull(msg)) {
				System.out.println("["+projectName+"]"+"update->" + fr.getTrackingRefUpdates().size());
				System.out.println("["+projectName+"]"+"message->" + msg);
			}
		}
		try {
			git.rebase().setUpstream("refs/remotes/origin/master").call();
		} catch (GitAPIException e1) {
			System.out.println("["+projectName+"]"+"git rebase error！");
			e1.printStackTrace();
		}
		if (revCommit != null) {
			try {
				git.stashApply().call();
			} catch (GitAPIException e) {
				System.out.println("["+projectName+"]"+"stash pop error,产生冲突了！");
				e.printStackTrace();
			}
		}
		System.out.println("["+projectName+"]"+"merge 成功");
		System.out.println("["+projectName+"]" + "......." + "end fetch......");
	}

}
