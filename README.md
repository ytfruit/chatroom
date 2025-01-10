# vue_chat
本项目是基于vue+springboot+websocket的多人聊天室，包括登陆注册，群聊通信，文件图片上传，创建群聊，加入退出群聊，个人信息展示和修改
效果展示：
登陆注册：
![image](https://github.com/user-attachments/assets/9098f9ae-f024-4b32-b908-8a43fe00a3e7)
![image](https://github.com/user-attachments/assets/b3e2409f-ec7e-4188-8878-f42d9b6a7894)
首页：
![image](https://github.com/user-attachments/assets/2fbb5515-e374-4d24-a9b6-ecae864c5db9)
用户信息显示：
![image](https://github.com/user-attachments/assets/7ea49451-858a-4d77-b95e-15a0846a2c87)
创建群聊：
![image](https://github.com/user-attachments/assets/e4c9b54d-e082-4d10-a437-019e17f91590)
搜索群聊：
![image](https://github.com/user-attachments/assets/2db29c20-f4df-4a73-9cdc-44c845512976)
本地运行：
前端执行：npm install
后端执行：application.properties的数据库的账号密码改成自己的，本地部署好minio后，将minio的账号密码也改成自己的。
云服务器部署：
云服务器搭建宝塔面板：
前端打包dist文件上传，后端打包jar文件上传，导入数据库
在网站中搭建java项目，配置springboot；在html中搭建vue项目。
安全组放行运行端口，启动后即可通过云服务器ip地址访问到vue-chat项目。






