package nicelee.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketServer {
	
	int portServerListening;
	
	boolean isRun = true;
	public static ExecutorService httpThreadPool;
	ServerSocket serverSocket;
	
	public SocketServer(int portServerListening) {
		this.portServerListening = portServerListening;
		httpThreadPool = Executors.newFixedThreadPool(20);
	}
	public SocketServer(int portServerListening, int threadPoolSize) {
		this.portServerListening = portServerListening;
		httpThreadPool = Executors.newFixedThreadPool(threadPoolSize);
	}
	
	/**
	 *  关闭服务器
	 */
	public void stopServer() {
		try {
			isRun = false;
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("正在关闭 SocketServer: 服务器... ");
	}
	
	/**
	 *  打开服务器
	 */
	public void startServer() {
		Socket socket = null;
		System.out.println("SocketServer: 服务器监听开始... ");
		try {
			serverSocket = new ServerSocket(portServerListening);
			//serverSocket.setSoTimeout(300000);
			
			while (isRun) {
				try {
					socket = serverSocket.accept();
				}catch (SocketTimeoutException e) {
					continue;
				}catch (SocketException e) {
					break;
				}
				//System.out.println("收到新连接: " + socket.getInetAddress() + ":" + socket.getPort());
				SocketDealer dealer = new SocketDealer(socket);
				httpThreadPool.execute(dealer);
			}
			httpThreadPool.shutdownNow();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
				}
			}
		}
		System.out.println("SocketServer: 服务器已经关闭... ");
	}
}
