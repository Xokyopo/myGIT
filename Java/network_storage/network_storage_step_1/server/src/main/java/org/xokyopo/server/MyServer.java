package org.xokyopo.server;

import io.netty.channel.Channel;
import org.xokyopo.clientservercommon.executors.*;
import org.xokyopo.clientservercommon.executors.messages.FileOperationMessage;
import org.xokyopo.clientservercommon.network.netty.NettyServerConnection;
import org.xokyopo.clientservercommon.simples.MyHandlerFactory;
import org.xokyopo.clientservercommon.utils.FileUtil;
import org.xokyopo.server.dao.DataBaseManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyServer {
    private final String repository = "server_repository";
    private final int serverPort = 8999;

    private AuthorizationExecutor authorizationExecutor;
    private FileOperationExecutor fileOperationExecutor;
    private FilePartExecutor filePartExecutor;
    private FileListExecutor fileListExecutor;

    private NettyServerConnection nettyServerConnection;

    private final Map<Channel, String> userDirs;
    private boolean printTransferFileLog = false;

    public MyServer() {
        this.createExecutors();
        this.constructingServer();
        this.userDirs = new ConcurrentHashMap<>();
    }

    public void run() throws InterruptedException {
        DataBaseManager.connection();
        nettyServerConnection.run(this.serverPort);
        DataBaseManager.disconnection();
    }

    private void constructingServer() {
        this.nettyServerConnection = new NettyServerConnection(
                new MyHandlerFactory(
                        (ch)->{},
                        this::ifDisconnect,
                        this.authorizationExecutor,
                        this.fileListExecutor,
                        this.fileOperationExecutor,
                        this.filePartExecutor
                )
        );
    }

    private void createExecutors() {
        this.authorizationExecutor = new AuthorizationExecutor(null, this::authorisationMethod);

        this.fileListExecutor = new FileListExecutor(this::getUserRepository, null);

        this.fileOperationExecutor = new FileOperationExecutor(this::getUserRepository, null);

        this.filePartExecutor = new FilePartExecutor(
                this::getUserRepository,
                (ch)->this.fileOperationExecutor.sendResponse(FileOperationMessage.OType.COPY,"","", ch),
                (fileName,fileFullLength,fileCurrentLength) -> this.printStatistic("Получено", fileName,fileFullLength,fileCurrentLength),
                (fileName,fileFullLength,fileCurrentLength) -> this.printStatistic("Отправлено", fileName,fileFullLength,fileCurrentLength)
        );
    }

    private boolean authorisationMethod(String login, String password, Channel channel) {
        boolean auth = this.checkLoginAndPassword(login, password);
        if (auth) {
            this.userDirs.put(channel, login);
            System.out.println("Подключился пользователь: \t" + login);
        }
        return auth;
    }

    private boolean checkLoginAndPassword(String login, String password) {
        String pass = DataBaseManager.getUserPassword(login);
        if (pass == null) {
            DataBaseManager.addClient(login, password);
            return true;
        } else {
            return pass.equals(password);
        }
    }

    private String getUserRepository(Channel channel) {
        Path path = Paths.get(this.repository, this.userDirs.get(channel));
        if (!path.toFile().exists()) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path.toString();
    }

    private void printStatistic(String msg, String filename, long fileFullLength, long fileCurrentLength) {
        if (this.printTransferFileLog) {
            System.out.println(String.format(
                    "%s %s из %s файла %s",
                    msg,
                    FileUtil.getHumanFileLength(fileFullLength),
                    FileUtil.getHumanFileLength(fileCurrentLength),
                    filename
            ));
        }
    }

    public void ifDisconnect(Channel channel) {
        System.out.println("Отключился пользователь:\t" + this.userDirs.get(channel));
        this.userDirs.remove(channel);
    }

    public static void main(String[] args) {
        MyServer myServer = new MyServer();
        try {
            System.out.println("запускаю сервер");
            myServer.run();
        } catch (InterruptedException e) {
            System.out.println("Server start error");
            e.printStackTrace();
        }
    }
}
