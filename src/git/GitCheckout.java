/**

 * Copyright(c) Guangzhou JiaxinCloud Science & Technology Ltd. 
 */
package git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;

import conf.LocalEnv;

/**
 * <pre>
git checkout branch 
操作
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
public class GitCheckout {

	
	public static void main(String[] args) throws Exception {
		
		List<String>projectNames=new ArrayList<String>();
		String branch="master";
		
		projectNames.add("jiaxin_lib_core");
		projectNames.add("jiaxin_gw_config");
		projectNames.add("jiaxin_web_devcenter");
		projectNames.add("jiaxin_web_agent");
		projectNames.add("jiaxin_gw_order");
		projectNames.add("jiaxin_gw_rest");

		for (int i = 0; i < projectNames.size(); i++) {
               String projectName=projectNames.get(i);
        		String filePath = LocalEnv.path +projectName;
    			File f = new File(filePath);
        	   Git git = null;
       		try {
       			git = Git.open(f);
       		} catch (IOException e) {
       			System.out.println("["+projectName+"]"+"git  checkout  error");
       			e.printStackTrace();
       		}
       		git.checkout().setName(branch).call();

			System.out.println("["+projectName+"]"+"git  checkout  success,branch ->"+branch);
			}
		}


}
