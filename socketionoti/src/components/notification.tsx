"use client";

import { useEffect, useState } from "react";
import { io, Socket } from "socket.io-client";


interface NotificationProps {
    message: string;
    time: string;
}

export default function Notification() {
    const [socket, setSocket] = useState<Socket | null>(null);
    const [notifications, setNotifications] = useState<NotificationProps[]>([]);
    
    useEffect(() => {
        const newSocket = io("http://localhost:8081", {
            transports: ["websocket"]
        });

        newSocket.on("connect", () => {
            console.log("Connected to server");
        });

        newSocket.on("notification", (data: NotificationProps) => {
            console.log("Notification received: ", data.message);
            const notification = {
                message: data.message,
                time: data.time
            };
            setNotifications((prevNotifications) => [...prevNotifications, notification]);
        });

        setSocket(newSocket);

        return () => {
            newSocket.close();
        };
    }, []);

    useEffect(() => {
        if (socket) {
            socket.emit('join', "Connected to server");
        }
    }, [socket]);

    const [hiddenNotifications, setHiddenNotifications] = useState<number[]>([]);

    const handleNotificationClick = (index: number) => {
        if(socket) {
            socket.emit('message', notifications[index].time);
        }
        setHiddenNotifications((prev) => [...prev, index]);
    };

    return (
        <div className="flex flex-col gap-2">
            {notifications.map((notification, index) => (
                !hiddenNotifications.includes(index) && (
                    <div 
                        key={index}
                        onClick={() => handleNotificationClick(index)}
                        onKeyDown={(e) => e.key === 'Enter' && handleNotificationClick(index)}
                        className="cursor-pointer p-4 bg-white shadow rounded hover:bg-gray-50 transition-colors"
                        role="button"
                        tabIndex={0}
                        aria-label={`Notification: ${notification.message}. Click to dismiss.`}
                    >
                        <p>{notification.message}</p>
                        <p>{notification.time}</p>
                    </div>
                )
            ))}
        </div>
    );
    
    
}

