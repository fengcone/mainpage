package fengcone.mainpage.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CodeLines {
	static BufferedReader reader;
	static int lines = 0;

	public static void main(String[] args) throws Exception {
		String path = System.getProperty("java.class.path");
		Integer index = path.indexOf(";");
		path = path.substring(0, index);
		index = path.indexOf("target\\classes");
		path = path.substring(0, index) + "src";
		System.out.println(path);
		long start = System.currentTimeMillis();
		get(new File(path));
		System.out.println(lines);
		System.out.println(System.currentTimeMillis()-start);
		reader.close();
	}

	public static void get(File file) throws Exception {
		File[] files = file.listFiles();
		for (File file2 : files) {
			if (file2.isDirectory()) {
				get(file2);
			} else {
				if (file2.getName().contains("jquery")||file2.getName().contains(".jpg")) {
					continue;
				}
				reader = new BufferedReader(new FileReader(file2));
				String string = null;
				while ((string = reader.readLine()) != null) {
					if (string.length() > 2) {
						if (!"}".equals(string.substring(string.length() - 1,
								string.length() ))
								&& !string.startsWith("import")
								&& !string.startsWith("package")
								) {
							System.out.println(string);
							lines++;
						}
					}
				}
			}
		}
	}
}
