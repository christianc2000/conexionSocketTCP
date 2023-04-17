/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.conexionsockettcp;

/**
 *
 * @author Christian
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

public class HiloMensaje extends Thread {

    Socket socket;
    private boolean clienteDesconectado = false;

    protected EventListenerList listenerList = new EventListenerList();

    public HiloMensaje(Socket socket) {
        this.socket = socket;
    }

    public void setClienteDesconectado(boolean clienteDesconectado) {
        this.clienteDesconectado = clienteDesconectado;
    }

    @Override
    public void run() {
        // Obtener los flujos de entrada y salida del socket
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                //System.out.println("Mensaje recibido del cliente: " + mensaje);
                //  salida.println("Respuesta del servidor: " + mensaje.toUpperCase());       
                EventMensaje evento = new EventMensaje(mensaje);
                this.fireMyEvent(evento);
            }

            // Cierra los flujos y el socket cuando se termina la comunicación con el cliente
            entrada.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cerrar el hilo cuando el cliente se desconecta
            System.out.println("Cerrando el hilo Mensaje del servidor con el cliente...");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //****************PARA LLAMAR A LOS EVENTOS

    public void addMyEventListener(InterfaceSocketListener listener) {
        listenerList.add(InterfaceSocketListener.class, listener);
    }

    public void removeMyEventListener(InterfaceSocketListener listener) {
        listenerList.remove(InterfaceSocketListener.class, listener);
    }

    void fireMyEvent(EventMensaje evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceSocketListener.class) {
                ((InterfaceSocketListener) listeners[i + 1]).leerMensaje(evt);
            }
        }
    }
}
