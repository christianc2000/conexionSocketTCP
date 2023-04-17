/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.conexionsockettcp;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Christian
 */
public class HiloPingCliente extends Thread {

    private Map<String, Map<String, Object>> Clientes;
    protected EventListenerList listenerList = new EventListenerList();

    public HiloPingCliente(Map<String, Map<String, Object>> clientes) {
        this.Clientes = clientes;
    }

    @Override
    public void run() {
        while (true) {
            // Verificar el estado de cada cliente en la lista
            for (Map.Entry<String, Map<String, Object>> entry : Clientes.entrySet()) {
                String key = entry.getKey(); // Obtener la clave del mapa exterior
                Map<String, Object> valor = entry.getValue();
                Socket socket = (Socket) valor.get("socket");
                if (socket.isClosed() || !socket.isConnected()) {
                    EventDesconexion evento=new EventDesconexion(key);
                    NotificarEvento(evento);//notificar evento desconexion al servidor
                    break; // Salir del bucle para evitar ConcurrentModificationException
                }
            }

            try {
                // Esperar 5 segundos antes de la siguiente verificación
                Thread.sleep(5000);
            } catch (InterruptedException e) {
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

    void NotificarEvento(EventDesconexion evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceSocketListener.class) {
                ((InterfaceSocketListener) listeners[i + 1]).ClienteDesconectado(evt);
            }
        }
    }
}
