
<template >
  <div class="leftSide">
    <!--header-->
    <div class="header">
      <div class="userimg">
        <img src="@/assets/chat.png" class="cover">
      </div>
    </div>
    <!--search-->
    <div class="search_chat">
      <div>
        <el-form
            :model="roomform">
          <el-form-item>
            <el-input v-model="roomform.roomId" class="sinput" prefix-icon="el-icon-search" placeholder="输入聊天室ID(只输入数字）" ></el-input>
          </el-form-item>
          <el-form-item>
            <button @click="searchRooms('roomform.roomId')"  class="sbutton" type="button" >搜索</button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <!--ChatList-->
    <div class="chatlist">
      <div class="group-item" @click="groupInfomation()">
        <ul>
          <li v-for="room in chatRooms" :key="room.roomId">
            <div class="sname">
              <img src="@/assets/bgc.jpg" class="himg">
              <span style="font-size:17px;margin-top: 15px"> {{ room.roomName}}</span>
              <el-button @click="joinRooms(room.roomId)" type="button" class="joinbutton">加入群聊</el-button>
<!--              <p style="font-size: 13px">{{ room.roomId }}</p>-->
            </div>
          </li>
        </ul>
      </div>
      <!--        <span>名字: </span>-->
      <!--        <span>ID:</span>-->
    </div>
  </div>

</template>

<script>
import axios from "axios";
import {ref, reactive} from "vue";
import {Message} from "element-ui";
import groupInfo from "@/views/group/groupInfo.vue";
import {eventBus} from "@/utils/eventBus";

export default {
  name: "chatGroup",
  computed: {
    groupInfo() {
      return groupInfo
    }
  },

  data() {
    return {
      roomform: {
        roomId: "",
      },
      chatRooms: [],
      currentUserRoom: {
        uid: "",
        roomId: "",
      },
      posvalue:{
        roomId:0,
        roomName:'',
        show:false,
      }
    }
  },

  methods: {
    searchRooms(formId) {
      let formdata = new FormData();
      formdata.append("roomId", this.roomform.roomId);
      this.getRequest('/chatroom/searchrooms', {}, {params: {roomId: this.roomform.roomId}}).then(resp => {
        if (resp.code === "200") {
          let newData = resp.data.map(room => ({
            roomId: room[0],
            roomName: room[1]
          }));
          this.chatRooms = newData;
          if(this.chatRooms.length==0){
            Message.error("聊天室不存在")
          }
          else {
            this.posvalue.roomId=this.chatRooms[0].roomId;
            this.posvalue.roomName=this.chatRooms[0].roomName;
          }
        }
      });
    },

    joinRooms(roomsId,roomsName) {
      try{
        this.currentUserRoom.uid=sessionStorage.getItem("userId");
        this.currentUserRoom.roomId=roomsId;
        console.log(this.currentUserRoom);
        this.postRequest('/chatroom/joinrooms',{},{params:{uid:this.currentUserRoom.uid,roomId:this.currentUserRoom.roomId}}).then((resp)=>{
          if(resp.code === "200"){
            console.log(this.chatRooms.roomId);
            Message.success("成功加入聊天室");
          }
          else if(resp.code==="500"){
            Message.error("用户已经在聊天室中");
          }else{
            Message.error("未知错误")
          }
        }).catch((error) => {
          console.error('Caught error in postRequest:', error.message);
        });
      }catch (error){
        console.error('Caught async error:', error.message);
      }
    },
    groupInfomation(){
      this.posvalue.show=true;
      // console.log(this.posvalue.roomId)
      eventBus.$emit('send-roomId', this.posvalue.roomId);
      eventBus.$emit('send-roomName', this.posvalue.roomName);
      eventBus.$emit('send-show', this.posvalue.show);
    }
  },
}
</script>
<style scoped="less">
@import url('https://fonts.googleapis.com css2?family-Open+Sans:wght@300;400;00;600;700&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Open Sans', sans-serif;
}

.leftSide {
  position: relative;
  width: 25%;
  background: #fff;
  border-right: 1px solid rgba(0, 0, 0, 0.2);
}
.header {
  position: relative;
  width: 100%;
  height: 60px;
  background-color: #faf8f8;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 15px;
  margin-bottom: 10px;
}

.userimg {
  position: relative;
  width: 40px;
  height: 40px;
  overflow: hidden;
  border-radius: 50%;
  cursor: pointer;
}

.cover {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.nav_icons li {
  display: flex;
  list-style: none;
  cursor: pointer;
  color: #51585c;
  font-size: 1.5em;
  margin-left: 22px;
}

.search_chat {
  position: relative;
  width: 100%;
  height: 50px;
  background: #f6f6f6;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 15px;
}

.search_chat div {
  width: 100%;
}
.group-item{
  height: 70px;
  background-color: #faf8f8;
}
.group-item:hover{
  background-color: #f5e99d;
  opacity: 0.7;
}
.search_chat div input {
  width: 100%;
  outline: none;
  border: none;
  background: #fff;
  padding: 6px;
  height: 38px;
  border-radius: 30px;
  font-size: 14px;
  padding-left: 40px;
}

.sinput {
  position: relative;
  width: 400px;
  height: 30px;
}

.sbutton {
  position: absolute;
  width: 60px;
  height: 38px;
  border-radius: 5px;
  border: 1px solid #ffffff;
  top: -60px;
  left: 250px;
  background-color: white;
}

.sname {
  display: flex;
  flex-direction: column;
  margin-left: -90px;
}

.himg {
  width: 50px;
  height: 50px;
  position: absolute;
  left: 20px;
  top: 8px;
  border-radius: 50%;
}

.search_chat div input::placeholder {
  color: #bbb;
}

.search_chat div img {
  position: absolute;
  left: 30px;
  top: 22px;
  font-size: 1.5em;
}

.chatlist {
  position: relative;
  height: calc(100% - 150px);
  overflow-y: auto;
  background-color: #faf8f8;
}
.chatlist .block .details .listHead h4 {
  font-size: 1.1em;
  font-weight: 600;
  color: #111;
}
/deep/ .el-button {
  width: max-content;
  height: 30px;
  position: absolute;
  font-size: 10px;
  left: 70%;
  top:20px;
  text-align: center;
  background-color: #fce9a7;
}


</style>
