package test;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
/**
 * 获取更新过ip地址的config.xml 文件
 * @author tangwei
 *
 */
public class XMLUtil {
	/**
	 * 解析xml并更新IP地址
	 * @param xmlStr
	 * @param ipName
	 * @return
	 */
	public static String parseXML(String xmlStr, String ipName) {
		String filePath = null;
		try {
			Document document = DocumentHelper.parseText(xmlStr);
			Element root = document.getRootElement();
			Map<String, Element> modifyNodes = getNodes(root);
			if (modifyNodes != null && modifyNodes.size() > 0) {
				Element shell = modifyNodes.get("shell");
				Element batchFile = modifyNodes.get("batchFile");

				if (shell != null && shell.hasContent()) {
					String text = "";
					String shellText = shell.getText();
					String[] contents = shellText.split(" ");
					for (int i = 0; i < contents.length; i++) {
						if (contents[i].contains("ip=")) {
							text = text + " ip=" + ipName;
						} else {
							text = text + " " + contents[i];
						}
					}
					shell.setText(text);
				}
				if (batchFile != null && batchFile.hasContent()) {
					String text = "";
					String batchFileText = batchFile.getText();
					String[] contents = batchFileText.split(" |\\n");
					for (int i = 0; i < contents.length; i++) {
						if (contents[i].contains("ip=")) {
							text = text + " ip=" + ipName;
						} else {
							text = text + " " + contents[i];
						}
					}
					batchFile.setText(text);
				}
			}
			String dirPath =createDir();
			if(dirPath != null){
				filePath = dirPath + "/config.xml";
				XMLWriter writer = new XMLWriter(new FileWriter(filePath));
				writer.write(document);
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return filePath;
	}
	/**
	 * 创建临时文件夹
	 * @param destDirName
	 * @return
	 */
	public static String createDir() { 
		String dirName = System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "");
        File dir = new File(dirName);  
        if (dir.exists()) {  
            return dir.getPath();  
        }    
        //创建目录  
        if (dir.mkdirs()) {  
            return dirName;  
        } else {  
            return null;  
        }  
    } 

	/**
	 * 获取命令行节点
	 * 
	 * @param root
	 * @return
	 */
	public static Map<String, Element> getNodes(Element root) {
		Map<String, Element> map = new HashMap<String, Element>();
		Element builders = root.element("builders");
		if (builders.hasContent()) {
			Element shell = builders.element("hudson.tasks.Shell");
			Element batchFile = builders.element("hudson.tasks.BatchFile");
			if (shell != null && shell.hasContent()) {
				Element command = shell.element("command");
				if (command != null && command.hasContent()) {
					map.put("shell", command);
				}
			}
			if (batchFile != null && batchFile.hasContent()) {
				Element command = batchFile.element("command");
				if (command != null && command.hasContent()) {
					map.put("batchFile", command);
				}
			}
		} else {

		}
		return map;
	}

	public static void main(String[] args) throws Exception {
		parseXML(JenkinsAPI.getJobConfig("modifyIp"), "1921637");
	}
}
