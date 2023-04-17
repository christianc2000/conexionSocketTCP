/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.conexionsockettcp;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Christian
 */
public class HiloConexion extends Thread {

    protected EventListenerList listenerList = new EventListenerList();
    ServerSocket serverSocket;

    public HiloConexion(ServerSocket s) {
        this.serverSocket = s;
    }

    public void conexion() {
        // Aceptar conexiones de clientes
        Socket socket;
        while (true) {
            try {
                // Aceptar conexiones de clientes
                socket = serverSocket.accept();
                EventConexion evento = new EventConexion(socket,this);
                this.fireMyEvent(evento);
                //System.out.println("Después del evento en HiloConexion");
            } catch (IOException e) {
                System.err.println("Servidor: Error en la conexión con el cliente: " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        conexion();
    }

    //****************PARA LLAMAR A LOS EVENTOS
    public void addMyEventListener(InterfaceSocketListener listener) {
        listenerList.add(InterfaceSocketListener.class, listener);
    }

    public void removeMyEventListener(InterfaceSocketListener listener) {
        listenerList.remove(InterfaceSocketListener.class, listener);
    }

    void fireMyEvent(EventConexion evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceSocketListener.class) {
                ((InterfaceSocketListener) listeners[i + 1]).nuevoClienteConectado(evt);
            }
        }
    }
}
