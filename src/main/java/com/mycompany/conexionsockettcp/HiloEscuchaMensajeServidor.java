/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.conexionsockettcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian
 */
public class HiloEscuchaMensajeServidor extends Thread {

    Socket socket;
    BufferedReader in;

    public HiloEscuchaMensajeServidor(Socket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String mensajeServidor;
            while (!Cliente.isDisconnected()) {
                mensajeServidor = in.readLine();
                System.out.println(mensajeServidor); // Mostrar mensajes del servidor en la consola
            }

        } catch (IOException e) {
            if (e.getMessage().equals("Socket closed")) {
                // El socket ya ha sido cerrado previamente, no es necesario tomar ninguna acción
            } else {
                e.printStackTrace();
            }
        } finally {
            System.out.println("Cliente desconectado desde el hilo escucha mensaje, cerrando el hilo...");
            // Cerrar el socket del cliente cuando el hilo se cierra
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
