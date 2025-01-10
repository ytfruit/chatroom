<template>
  <div class="rightSide">
    <!-- chatbox -->
    <div class="chat-box">
      <header>
        <span class="head_span">{{ roomName }}</span></header>
      <div class="msg-box" ref="msg-box">
        <div v-if="list.length > 0">
          <div v-for="(i, index) in list" :key="index" class="msg" :style="i.userId === userId ? 'flex-direction: row-reverse' : ''">
            <div class="user-head">
              <img :src="i.avatar" height="30" width="30" :title="i.username" />
            </div>
            <div class="user-msg">
              <span :style="i.userId === userId ? 'float: right;' : ''" :class="i.userId === userId ? 'right' : 'left'">
               <!-- 判断内容是图片还是文件类型 -->
               <div v-if="i.fileUrl">
                  <template v-if="isImage(i.fileType)">
                    <img :src="i.fileUrl" alt="image" class="message-img" />
                      </template>
                        <template v-else>
                          <a :href="i.fileUrl" target="_blank">{{ i.fileName }}</a>
                      </template>
               </div>
               <div v-else>{{ i.content }}</div>
            </span>
            </div>
          </div>
        </div>
        <div v-else>
          <p>当前没有消息</p>
        </div>
      </div>
      <div class="input-box">
        <i class="el-icon-picture" @click="triggerImageUpload"></i>
        <i class="el-icon-files" @click="triggerFileUpload"></i>
        <input type="text" ref="sendMsg" v-model="contentText" @keyup.enter="sendText()" />
        <div class="btn" :class="{['btn-active']: contentText}" @click="sendText()">发送</div>
      </div>
    </div>

    <!-- 图片上传组件 -->
    <el-upload
        ref="imageUpload"
        action="http://localhost:8082/file/upload"
        :show-file-list="false"
        :accept="'image/jpeg, image/png'"
        :before-upload="beforeImageUpload"
        :on-change="handleImageChange"
        :auto-upload="false"
    ></el-upload>

    <!-- 文件上传组件 -->
    <el-upload
        ref="fileUpload"
        action="http://localhost:8082/file/upload"
        :show-file-list="false"
        :accept="'*/*'"
        :before-upload="beforeFileUpload"
        :on-change="handleFileChange"
        :auto-upload="false"
    ></el-upload>
  </div>
</template>

<script>
import chatGroup from "@/views/chat/chatGroup.vue";
import { Message } from "element-ui";
import {eventBus} from "@/utils/eventBus";

let socket;

export default {
  name: "chatSpace",
  components: {
    chatGroup,
  },
  data() {
    return {
      roomId: sessionStorage.getItem("roomId"),
      roomName: '默认聊天室',
      ws: null,
      count: 0,
      userId: Number(sessionStorage.getItem("userId")), // 当前用户ID
      username: sessionStorage.getItem("userName"), // 当前用户昵称
      avatar:sessionStorage.getItem('watchStorage'),
      list: [], // 聊天记录的数组
      contentText: "", // input输入的值
      uploadData: {
        fileUrl: '',
        fileType: '',
        fileName: '',
        userId: sessionStorage.getItem("userId"),
        roomId: sessionStorage.getItem("roomId"),
      },
      chatRooms:[],
    };
  },
  mounted() {
    this.initWebSocket();
    this.loadHistoryMessages();  // 加载历史消息
  },
  destroyed() {
    // 离开页面时关闭websocket连接
    this.ws.onclose(undefined);
    // 清除事件监听器
    window.removeEventListener('setItem', this.handleStorageChange);
  },
  created() {
    // 监听 roomId 变化
    this.$watch('roomId', (newRoomId, oldRoomId) => {
      if (newRoomId !== oldRoomId) {
        // console.log(`房间 ID 发生变化：从 ${oldRoomId} 变为 ${newRoomId}`);
        // 如果 roomId 改变，关闭当前 WebSocket 连接并重新连接
        this.initWebSocket();  // 重新初始化 WebSocket 连接
        this.loadRoomData();    // 重新加载房间数据
      }
    });
    // 监听 'changeRoom' 事件，获取传递的 index
    eventBus.$on('changeRoom', (data) => {
      this.roomId = data;
      sessionStorage.setItem("roomId", data);  // 确保 sessionStorage 中的值也更新
      this.loadRoomData();  // 每次房间切换时加载相应的房间数据
    });
    this.loadRoomData();  // 每次房间切换时加载相应的房间数据
    // 监听 StorageEvent 事件
    window.addEventListener('setItem', this.handleStorageChange);
    // 获取初始的头像数据
    this.avatar = sessionStorage.getItem('watchStorage') || '';
  },
  methods: {
    // 根据 roomId 加载房间数据
    loadRoomData() {
      this.getRequest('/chatroom/searchrooms', {}, {params: {roomId: this.roomId}}).then(resp => {
        if (resp.code === "200") {
          this.roomName = resp.data[0][1] || '默认聊天室';  // 更新房间名称
          this.list = [];  // 清空当前历史记录
          this.loadHistoryMessages();  // 加载该房间的历史消息
          if(resp.data.length==0){
            Message.error("聊天室不存在")
          }
        } else {
          this.roomName = '未知聊天室';  // 如果房间数据加载失败，显示默认名称
          this.$message.error('加载房间数据失败');
        }
      });
    },
    // 进入页面创建websocket连接
    initWebSocket() {
      // 如果已有 WebSocket 连接，先关闭
      if (this.ws) {
        this.ws.close();
        // console.log("WebSocket 连接已关闭");
      }
      let _this = this;
      if (window.WebSocket) {
        const serverHost = window.location.hostname;
        const roomId = this.roomId;
        const userId = this.userId;
        const url = `ws://${serverHost}:8082/chat/${roomId}/${userId}`;
        console.log(url)
        _this.ws = new WebSocket(url);

        _this.ws.onopen = function () {
          console.log("WebSocket连接成功: " + url);
        };

        _this.ws.onclose = function () {
          console.log("WebSocket连接关闭: " + url);
        };

        _this.ws.onerror = function () {
          console.log("WebSocket连接出错: " + url);
        };

        _this.ws.onmessage = function (e) {
          try {
            const resData = JSON.parse(e.data);
            // console.log(resData)
            if (resData.count !== undefined) {
              _this.count = resData.count; // 更新在线人数
            } else if (resData.msg && resData.userId) {
              console.log(resData)
              _this.list.push({
                userId: Number(resData.userId),
                username: resData.username,
                avatar: resData.avatar,
                content: resData.msg,
                fileUrl: resData.fileUrl,
                fileType: resData.fileType,
                fileName: resData.fileName, // 文件名
              });
              console.log(_this.list);
              // this.resetSetItem('watchStorage', resData.avatar);
            }
          } catch (error) {
            console.error("消息解析失败", error);
          }

          _this.scrollBottom(); // 滚动到底部
        };
      }
    },
    loadHistoryMessages() {
      this.getRequest('/messages', {}, { params: { roomId: this.roomId } }).then(resp => {
        if (resp.code === "200") {
          // 获取历史消息并处理
          this.list = resp.data.content.map(item => {
            // console.log('Loaded Message:', item);  // 打印每条消息的内容
            return {
              userId: item.user.uid,  // 用户ID
              username: item.user.uname,  // 用户名
              avatar: item.user.avatarUrl,  // 用户头像
              content: item.messageContent,  // 消息内容
              fileUrl: item.fileData ? item.fileData.filePath : null,  // 文件URL，如果有
              fileType: item.fileData ? item.fileData.fileType : null,  // 文件类型
              fileName: item.fileData ? item.fileData.fileName : '',  // 文件名
            };
          });
          // // 确保数据更新后再打印
          // this.$nextTick(() => {
          //   console.log('Updated list after loading history messages:', this.list);
          // });
          this.scrollBottom();  // 滚动到底部
        } else {
          this.$message.error("加载历史消息失败");
        }
      });
    },
    // 点击上传图片
    triggerImageUpload() {
      this.$refs.imageUpload.$el.querySelector('input').click();
    },

    // 点击上传文件
    triggerFileUpload() {
      this.$refs.fileUpload.$el.querySelector('input').click();
    },

    // 上传前的验证（图片）
    beforeImageUpload(file) {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
      if (!isJpgOrPng) {
        this.$message.error('上传文件必须是 JPG 或 PNG 格式');
      }
      return isJpgOrPng;
    },

    // 上传前的验证（文件）
    beforeFileUpload(file) {
      const isNotImage = file.type !== 'image/jpeg' && file.type !== 'image/png';
      if (!isNotImage) {
        this.$message.error('上传文件不能是 JPG 或 PNG 格式的图片');
      }
      return isNotImage;
    },

    // 图片上传文件变化时调用
    handleImageChange(file, fileList) {
      const imageFile = file.raw || file;
      if (imageFile instanceof File) {
        this.uploadFile(imageFile);
      } else {
        Message.error("上传的文件无效");
      }
    },

    // 文件上传文件变化时调用
    handleFileChange(file, fileList) {
      const realFile = file.raw || file;
      if (realFile instanceof File) {
        this.uploadFile(realFile);
      } else {
        Message.error("上传的文件无效");
      }
    },

    // 上传文件
    uploadFile(file) {
      if (file instanceof File) {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("userId", this.uploadData.userId);
        formData.append("roomId", this.uploadData.roomId);

        this.postRequest('file/upload', formData, { headers: { 'Content-Type': 'multipart/form-data' } }).then(resp => {
          if (resp.code === "200") {
            console.log(resp.data);
            this.uploadData.fileUrl = resp.data.fileUrl;
            this.uploadData.fileType = resp.data.fileType;
            this.uploadData.fileName = resp.data.fileName;
            this.sendMessage(`文件上传成功: ${this.uploadData.fileName}`, this.uploadData.fileUrl, this.uploadData.fileType);
          } else {
            Message.error("文件上传失败");
          }
        });
      } else {
        Message.error("上传的文件无效");
      }
    },

    // 发送消息
    sendMessage(msgContent, fileUrl = null, fileType = null) {
      const msg = {
        userId: this.userId,
        username: this.username,
        avatar: this.avatar,
        msg: msgContent,
        roomId: this.roomId,
        fileUrl: fileUrl, // 文件的URL
        fileType: fileType, // 文件类型
        fileName: this.uploadData.fileName, // 文件名称
      };
      this.ws.send(JSON.stringify(msg)); // 通过WebSocket发送消息
    },

    // 发送文本消息
    sendText() {
      if (!this.contentText) {
        return;
      }
      this.sendMessage(this.contentText);
      this.contentText = '';
    },

    // 滚动到底部
    scrollBottom() {
      let _this = this;
      this.$nextTick(() => {
        let el = _this.$refs["msg-box"];
        if (el) {
          el.scrollTop = el.scrollHeight;
        }
      });
    },

    // 判断是否为图片
    isImage(fileType) {
      return fileType === 'image/jpeg' || fileType === 'image/png';
    }
  },
};
</script>


<style scoped="less">
@import url('https://fonts.googleapis.com/css2?family=Open+Sans:wght@300;400;500;600;700&display=swap');
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Open Sans', sans-serif;
}

.rightSide {
  position: relative;
  flex: 70%;
  height: 100%;
  background: #e5ddd5;
}

.rightSide::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
.userimg {
  position: relative;
  width: 40px;
  height: 40px;
  overflow: hidden;
  border-radius: 50%;
  cursor: pointer;
}
.chat-box {
  margin: 0 auto;
  background: #fafafa;
  position: absolute;
  height: 100%;
  width: 100%;
  //max-width: 700px;
  header {
    position: fixed;
    width: 100%;
    height: 3rem;
    background: #f1e4a8;
    //max-width: 700px;
    display: flex;
    //justify-content: center;
    align-items: center;
    font-weight: bold;
    color: white;
    font-size: 1rem;
    align-content: center;
  }
  .head_span{
    margin-left: 20px;
  }
  .msg-box {
    position: absolute;
    height: calc(100% - 6.5rem);
    width: 100%;
    margin-top: 3rem;
    overflow-y: scroll;
    background-image: url("@/assets/im6_bg.jpg");
    opacity: 0.5;
    .msg {
      width: 95%;
      min-height: 2.5rem;
      margin: 1rem 0.5rem;
      position: relative;
      display: flex;
      justify-content: flex-start !important;
      .user-head {
        min-width: 2.5rem;
        width: 20%;
        width: 2.5rem;
        height: 2.5rem;
        border-radius: 50%;
        background: #f1f1f1;
        display: flex;
        justify-content: center;
        align-items: center;
        .head {
          width: 1.2rem;
          height: 1.2rem;
        }
      }
      .user-msg {
        //width: 80%;
        word-break: break-all;
        position: relative;
        z-index: 5;
        span {
          display: inline-block;
          padding: 0.5rem 0.7rem;
          border-radius: 0.5rem;
          margin-top: 0.2rem;
          font-size: 0.88rem;
        }
        .left {
          background: #0266fd;
          color: #f4f5f4;
          animation: toLeft 0.5s ease both 1;
        }
        .right {
          background: #faf8f8;
          color: #2c2d2c;
          animation: toright 0.5s ease both 1;
        }
        @keyframes toLeft {
          0% {
            opacity: 0;
            transform: translateX(-10px);
          }
          100% {
            opacity: 1;
            transform: translateX(0px);
          }
        }
        @keyframes toright {
          0% {
            opacity: 0;
            transform: translateX(10px);
          }
          100% {
            opacity: 1;
            transform: translateX(0px);
          }
        }
      }
    }
  }
  .input-box {
    padding: 0 0.5rem;
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 3.5rem;
    background: #fafafa;
    box-shadow: 0 0 5px #ccc;
    display: flex;
    justify-content: space-between;
    align-items: center;
    i{
      margin-right: 15px;
    }
    i:hover{
      background-color: #efe78e;
    }
    input {
      height: 2.3rem;
      display: inline-block;
      width: 100%;
      padding: 0.5rem;
      border: none;
      border-radius: 0.2rem;
      font-size: 0.88rem;
    }
    .btn {
      height: 2.3rem;
      min-width: 4rem;
      background: #e0e0e0;
      padding: 0.5rem;
      font-size: 0.88rem;
      color: white;
      text-align: center;
      border-radius: 0.2rem;
      margin-left: 0.5rem;
      transition: 0.5s;
    }
    .btn-active {
      background: #fdea1e;
    }
  }
  /* 省略其他样式 */
  .file-img {
    margin-top: 10px;
    display: inline-block;
  }
  /* 省略其他样式 */
  .message-img {
    max-width: 200px;
    max-height: 200px;
    display: block;
    margin-top: 10px;
  }
}
</style>
