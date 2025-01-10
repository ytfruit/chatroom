<template>
  <div class="leftSide">
    <!--header-->
    <div class="header">
      <div class="userimg">
        <img src="@/assets/chat.png" class="cover">
      </div>
      <div class="userimg" style="margin-left: 15px">
        <img src="@/assets/add_group.png" class="cover" @click="dialogFormVisible = true">
      </div>
    </div>
    <el-dialog title="创建群聊" :visible.sync="dialogFormVisible" width="400px">
      <el-form :model="form">
        <el-form-item label="群聊名称" :label-width="formLabelWidth">
          <el-input v-model="form.roomName" autocomplete="off" placeholder="请输入群聊名称"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="createGroup">确 定</el-button>
      </div>
    </el-dialog>
    <!--ChatList-->
    <div class="chatlist" >
      <div v-for="(room, index) in roomData" :key="room.roomId" class="block" @click="showChatSpace(room.roomId)">
        <div class="imgbx">
          <img src="@/assets/bg.jpg" class="cover">
        </div>
        <div class="details">
          <div class="listHead">
            <span >{{ room.roomName }}</span>
            <el-button @click="leaveRooms(room.roomId)">退出聊天室</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {Message} from "element-ui";
import {eventBus} from "@/utils/eventBus";

export default {
  name: "chatGroup",
  data(){
    return{
      userId:sessionStorage.getItem("userId"),
      roomId:1,
      data:[],
      roomData:[],
      currentRoomIndex: null,  // 保存当前选中的房间 index
      dialogFormVisible: false,
      form:{
        roomName:'',
      },
      formLabelWidth: '100px',
      currentUserRoom: {
        uid: "",
        roomId: "",
      },
    }
  },
  methods: {
    leaveRooms(roomsId) {
      try {
        this.currentUserRoom.uid = sessionStorage.getItem("userId");
        this.currentUserRoom.roomId = roomsId;
        console.log(this.currentUserRoom);
        this.postRequest('/chatroom/leaverooms', {}, {
          params: {
            uid: this.currentUserRoom.uid,
            roomId: this.currentUserRoom.roomId
          }
        }).then((resp) => {
          if (resp.code === "200") {
            this.roomData = this.roomData.filter(room => room.roomId !== roomsId);//将所有不等于roomsId的聊天室保存下来
            Message.success("成功退出聊天室");
          } else if (resp.data.code === "300") {
            Message.error("用户不在该聊天室中");
          }
        }).catch((error) => {
          console.error('Caught error in postRequest:', error.message);
        });
      } catch (error) {
        Message.error("退出聊天室失败");
      }
    },
    createGroup(){
      this.getRequest('/chatroom/createRooms', {}, {params: {createdBy:this.userId,roomName:this.form.roomName}}).then(resp => {
        if (resp.code === "200") {
          Message.success("创建聊天室成功！");
          this.dialogFormVisible=false;
          // 直接将新创建的房间信息添加到 roomData 中
          this.roomData.push({
            roomId: resp.data.roomId, // 假设后端返回了新创建房间的 ID
            roomName: this.form.roomName
          });
           // this.getAllRooms();
        }
      }).catch(error => {
        // 处理请求失败（例如网络问题）的错误
        Message.error("聊天室已经存在");
      });
    },
    // 获取房间 ID
    async getRoomId() {
      const _this = this;
      try {
        const resp = await _this.getRequest('/chatroom/user-rooms', {}, { params: { userId: sessionStorage.getItem("userId") } });
        if (resp.code === "200") {
          // 直接赋值给 data 数组
          _this.data = resp.data;
        } else {
          Message.error("获取聊天室Id失败");
        }
      } catch (error) {
        Message.error("请求获取聊天室Id失败");
      }
    },

    // 根据 roomId 获取房间详细信息
    async searchRooms(formId) {
      const _this = this;
      try {
        const resp = await _this.getRequest('/chatroom/searchrooms', {}, { params: { roomId: formId } });
        if (resp.code === "200") {
          // 将房间信息推送到 roomData 数组中
          _this.roomData.push({
            roomId: resp.data[0][0],  // 假设 resp.data[0] 是 [roomId, roomName]
            roomName: resp.data[0][1]  // 假设 resp.data[1] 是房间名称
          });
        } else {
          Message.error("获取聊天室信息失败");
        }
      } catch (error) {
        Message.error("请求获取聊天室信息失败");
      }
    },
    async getAllRooms() {
      const _this = this;
      await _this.getRoomId(); // 确保获取到房间ID后再执行下面的代码

      if (_this.data.length > 0) {
        // 通过 Promise.all 实现并行请求
        const roomRequests = _this.data.map(roomId => _this.searchRooms(roomId));
        await Promise.all(roomRequests);
      } else {
        Message.error("没有获取到房间ID");
      }
    },
    showChatSpace(index){
      eventBus.$emit('changeRoom', index);  // 使用 eventBus 触发事件，传递 index
      console.log(index)
    }
  },
  mounted() {
    this.getAllRooms();
  }
}
</script>
<style scoped="less">
@import url('https://fonts.googleapis.com css2?family-Open+Sans:wght@300;400;00;600;700&display=swap');
*
{
  margin: 0;padding: 0;
  box-sizing: border-box;
  font-family: 'Open Sans' ,sans-serif;
}
.leftSide
{
  position: relative;
  flex:20%;
  background:#fff;
  border-right: 1px solid rgba(0, 0, 0, 0.2);
}
.header
{
  position: relative;
  width: 100%;
  height: 60px;
  background-color: #faf8f8;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 15px;
}
.userimg
{
  position: relative;
  width: 40px;
  height: 40px;
  overflow: hidden;
  border-radius: 50%;
  cursor: pointer;
}
.cover
{
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.chatlist
{
  position: relative;
  height: calc(100%- 110px);
  overflow-y: auto;
}
.chatlist .block
{
  position: relative;
  width: 100%;
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}
.chatlist .block.active{
  background: #ebebeb;
}
.chatlist .block:hover
{
  background:#f5f5f5;
}
.chatlist .block .imgbx
{
  position: relative;
  min-width: 45px;
  height: 45px;
  overflow: hidden;
  border-radius: 50%;
  margin-right: 10px;
}
.chatlist .block .details
{
  position: relative;
  width: 100%;
}
.chatlist .block .details .listHead
{
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}
.chatlist .block .details .listHead span
{
  font-size: 1.1em;
  font-weight: 600;
  color: #111;
}
.el-button{
  font-size: 10px;
  background-color: #fce9a7;
}
.chatlist .block .details .listHead .time
{
  font-size: 0.75em;
  color: #aaa;
}
.chatlist .block .details .listHead .time
{
  color: #111;
}
</style>
