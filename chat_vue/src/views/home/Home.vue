<template>
  <el-container>
    <el-aside width="80px">
      <el-menu
          :default-active="this.$route.path"
          class="el-menu-vertical-demo"
          @select="handleSelect"
          router
          style="height:100%;"
      >
        <!-- 传递用户头像 URL 给 acvatar 组件 -->
        <acvatar  @open-profile-modal="openProfileModal"></acvatar>
        <el-menu-item index="/chatspace">
          <i class="el-icon-chat-round"></i>
        </el-menu-item>
        <el-menu-item index="/group">
          <img src="@/assets/group.png" class="icon">
        </el-menu-item>
        <i class="el-icon-switch-button" @click="logout"></i>
      </el-menu>
    </el-aside>
    <el-main>
      <router-view></router-view>
    </el-main>

    <!-- 引入 ProfileModal 组件 -->
    <ProfileModal ref="profileModal"/>

  </el-container>
</template>

<script>
import acvatar from "@/views/avactar/acvatar.vue";
import ProfileModal from "@/views/avactar/ProfileModal.vue";

export default {
  data() {
    return {
      user: {
        uid: 0,
        uname: "",
        password: null,
        avatar_url: '',
      },
    };
  },
  components: {
    acvatar,
    ProfileModal,
  },
  methods: {
    handleSelect(key, keyPath) {
      this.$router.push(key);
    },
    logout() {
      // 移除本地用户登录信息
      sessionStorage.removeItem('userName');
      sessionStorage.removeItem('passWord');
      this.$store.commit("delToken")
      // 跳转页面到登录页
      this.$router.push('/login');
    },
    openProfileModal() {
      // console.log("Profile modal opened!"); // 确保方法被调用
      this.$refs.profileModal.open(this.user); // 打开模态框
    },
  },

  created() {
    // 将用户信息存储到 sessionStorage 中
    let _this = this;
    _this.user.uid = sessionStorage.getItem('uid');
    _this.user.uname = sessionStorage.getItem('userName');
    _this.user.password = sessionStorage.getItem('passWord');
    this.user.avatar_url = sessionStorage.getItem("avatar_url");
  },
};
</script>

<style scoped="less">

.el-aside {
  background-color: #0c4a9f;
  color: #333;
  text-align: center;
  height: 100%;
  display: flex; /* 使用Flexbox布局 */
  flex-direction: column; /* 子元素垂直排列 */
  justify-content: space-between; /* 元素分布在顶部和底部 */
}

.el-main {
  //background-color: #dee4e7;
  color: #333;
  text-align: center;
  height: 100%;
  padding: 0;
}

body > .el-container {
  margin-bottom: 40px;
}
.el-container{
  height: 97vh;
}
.el-menu-vertical-demo{
  background-color: #eee2a7;
}
.el-menu-item{
  margin-top: 15px;
}
.el-menu-item:hover{
  background-color: #f8efc3;
}
.el-icon-switch-button{
  position: absolute; /* 绝对定位 */
  bottom: 30px; /* 底部对齐 */
  left: 50%; /* 水平居中 */
  transform: translateX(-50%); /* 修正水平居中 */
}
.el-icon-switch-button:hover{
  background-color: #f8d52c;
}
.avatar-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
  cursor: pointer;
}

.icon {
  width: 20px;
  height: 20px;
  border-radius: 50%
}
.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}
</style>
