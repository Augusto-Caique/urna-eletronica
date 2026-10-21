package com.lumi2021.urna.controller.poolWorker;

import com.lumi2021.urna.platform.NetworkUtils;
import io.javalin.Javalin;

import java.net.SocketException;

public class EmbeddedPoolWorkerController implements PoolWorkerController {
    private final Javalin server;

    public EmbeddedPoolWorkerController() {
        server = Javalin.create();

        server.ws("/ws", ws -> {
            ws.onConnect(ctx -> {
                System.out.println("Cliente conectado: " + ctx.sessionId());
                ctx.send("Conectado à urna!");
            });

            ws.onMessage(ctx -> {
                String message = ctx.message();
                System.out.println("Mensagem recebida: " + message);
                ctx.send("Recebido: " + message);
            });

            ws.onClose(ctx -> {
                System.out.println("Conexão encerrada: " + ctx.status());
            });

            ws.onError(ctx -> {
                System.err.println("Erro no WebSocket: " + ctx.error());
            });
        });

        server.start("0.0.0.0", 8080);

        System.out.println("Servidor da urna iniciado em 8080");
        System.out.println("Ouvindo em:");
        try {
            for (var ip : NetworkUtils.getLocalIPv4Addresses()) {
                System.out.println(" - " + ip.getHostName() + " - " + ip.getHostAddress());
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        server.stop();
    }
}