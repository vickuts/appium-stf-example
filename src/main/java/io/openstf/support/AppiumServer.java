package io.openstf.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.NoSuchElementException;

/**
 * We can start the Appium server dynamically.
 */
public class AppiumServer {

	public static final String HOST_NAME = "127.0.0.1";
	public static final String NODE_HOME = System.getenv("NODE_HOME");
	public static final String APPIUM_HOME = System.getenv("APPIUM_HOME");

	private int[] ports = new int[]{4721, 4722, 4723, 4724, 4725, 4726, 4727, 4728, 4729, 4730};
	private String appiumServerUrl;
	private Process process;

	private static final Logger LOG = LoggerFactory.getLogger(AppiumServer.class);


	public String startAppiumServer() throws IOException, InterruptedException {
		String command;

		final String homeDir = System.getProperty("user.dir") + File.separator + "build";
		if (!new File(homeDir).exists()) new File(homeDir).mkdir();

		final File appiumFilePath = new File(homeDir + File.separator + "logs");
		if (!appiumFilePath.exists()) appiumFilePath.mkdir();

		final File appiumLogsFile = new File(appiumFilePath, "appiumLogs_" + LocalDate.now() + ".log");
		appiumLogsFile.createNewFile();

		String appiumArgs = "--log-level debug --local-timezone --session-override --log " + appiumLogsFile.toString();
		int availablePort = 0;

		if (APPIUM_HOME == null) throw new NoSuchElementException("Environment variable not found for APPIUM_HOME");
		if (NODE_HOME == null) throw new NoSuchElementException("Environment variable not found for NODE_HOME");

		LOG.info("APPIUM_HOME is '{}'", APPIUM_HOME);
		LOG.info("NODE_HOME is '{}'", NODE_HOME);

		for (int i = 0; i < ports.length; i++) {
			if (!isPortInUse(HOST_NAME, ports[i])) {
				availablePort = ports[i];
				LOG.info("Available port found: " + availablePort);
				break;
			}
		}
		if (availablePort == 0) {
			throw new PortUnreachableException("Ports are not available");
		} else {
			command = APPIUM_HOME + " ";
			command = command + " -a " + HOST_NAME + " -p " + availablePort;
			LOG.info("Command to start Appium Server: " + command);
		}

		process = Runtime.getRuntime().exec(command);

		LOG.info("Waiting for Appium server starts...");
		while (!isPortInUse(HOST_NAME, availablePort)) {
			Thread.sleep(1000);
		}
		appiumServerUrl = "http://" + HOST_NAME + ":" + availablePort + "/wd/hub";
		return appiumServerUrl;
	}

	public boolean isPortInUse(String hostName, int portNumber) {
		try {
			Socket socket = new Socket(hostName, portNumber);
			socket.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void killAppiumProcess() {
		if (process != null && process.isAlive()) process.destroy();
	}

	public String getAppiumServerUrl() {
		return appiumServerUrl;
	}

}