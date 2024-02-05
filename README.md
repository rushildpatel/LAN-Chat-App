# LAN Chat Application

A JAVA based client - server chat application like WHATSAPP DESKTOP.

### Features
- This project allows private messaging between two users.
- TCP\IP protocol is used for building this application.
- Register\Login is provided for security purpose.
- Backup functionality is provided and the messages are stored as JSON files both on server and client side.
- Offline user will get messages when he\she comes online. 

#### Private Messaging
- This feature is achieved by server working in between.
- Suppose client A wants to send message client B, client A sends message and name of other client i.e client B to server.
- Server finds that client B is online or not.
- If client B is online, server sends message to client B.
- If client B is offline, message gets stored in json file and when client B comes online, it will recieve the pending.