<template>
  <div class="left-column">
    <div class="avatar-container" @click="openProfileModal" >
      <div class="avatar">
        <img  :src="imageUrl" class="avatar-img" >
      </div>
    </div>
  </div>
</template>

<script>
import {eventBus} from "@/utils/eventBus";
import {ref} from "vue";

export default {
  name: "Avatar",
  data(){
    return{
      imageUrl:'',
    }
  },
  methods: {
    openProfileModal() {
      this.$emit("open-profile-modal"); // 触发父组件的事件
    },

    getavactar(){
      let uid=sessionStorage.getItem("userId");
      this. getRequest('user/get_avatar',{},{params:{userId:uid}}).then(resp=>{
        if(resp.code=200){
          this.imageUrl=resp.data;
          sessionStorage.setItem("watchStorage",this.imageUrl);
        }
      })
      this.$forceUpdate(); // 强制更新视图
    },
    handleStorageChange() {
      // 获取最新的头像数据
      this.imageUrl = sessionStorage.getItem('watchStorage');
    }
  },
  mounted() {
    this.getavactar();
  },

  created() {
    // 监听 StorageEvent 事件
    window.addEventListener('setItem', this.handleStorageChange);
    // 获取初始的头像数据
    this.imageUrl = sessionStorage.getItem('watchStorage') || '';
  },
  destroyed() {
    // 清除事件监听器
    window.removeEventListener('setItem', this.handleStorageChange);
  },
}
</script>

<style scoped="less">
.left-column {
  width: 75px; /* 左侧列的宽度 */
  height: 10%; /* 确保高度占满父元素 */
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}

.avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  overflow: hidden;
  position: relative;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 确保图片填充容器并居中显示 */
  clip-path: circle(38% at 50% 50%); /* 裁剪图片为圆形 */
}
</style>
