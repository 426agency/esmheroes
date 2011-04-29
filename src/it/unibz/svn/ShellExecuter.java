package it.unibz.svn;

import java.io.*;

public class ShellExecuter {

	public static void main(String args[]) {
		execute("ls -l");
	}

	private static void execute(String command) {
		try {
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(command);
			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line=null;

			while((line=input.readLine()) != null) {
				System.out.println(line);
			}
			//int exitVal = pr.waitFor();
			//System.out.println("Exited with error code "+exitVal);
		} catch(Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
}