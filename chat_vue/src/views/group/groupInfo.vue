<template>
  <div class="rightSide" v-if="show">
    <!--chatbox-->
    <img src="@/assets/bgc.jpg" class="group-img">
    <br><br>
    <el-tag type="success" class="item">聊天室Id:{{roomId}}</el-tag>
    <el-tag type="success" class="item">聊天室名称:{{roomName}}</el-tag>
  </div>
</template>
<script>
import group from "@/views/group/group.vue";
import {eventBus} from "@/utils/eventBus";
export default {
  name: "chatSpace",
  components: {
    group,
  },
  data() {
    return {
      roomId:0,
      roomName:"",
      show:false
    }
  },
  methods: {},
  mounted() {
    // 通过事件总线的 $on 方法监听名为 'data-send' 的自定义事件，当事件触发时，回调函数会接收到传递过来的数据
    eventBus.$on('send-roomId', (data) => {
      this.roomId = data;
    });
    eventBus.$on('send-roomName', (data) => {
      this.roomName = data;
    });
    eventBus.$on('send-show', (data) => {
      this.show= data;
    });
  }
}
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
}

.rightSide::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
.group-img{
  width: 400px;
  height: 400px;
  margin: 20px auto 0;
}
.item{
  margin-left: 50px;
  width: 130px;
}
</style>
