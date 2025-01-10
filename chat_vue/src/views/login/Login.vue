<template>
  <div class="container">
    <el-card class="box-card">
      <h2 style="color: #faf8f8">欢迎加入vue-chat</h2>
      <el-form
        :model="loginform"
        status-icon
        :rules="rules"
        ref="loginform"
        class="login-from"
      >
        <el-form-item  prop="uname">
          <el-input v-model="loginform.uname" placeholder="请输入用户昵称"></el-input>
        </el-form-item>
        <el-form-item  prop="password">
          <el-input
            type="password"
            v-model="loginform.password"
            autocomplete="off"
            placeholder="请输入用户密码"
          ></el-input>
        </el-form-item>
      </el-form>
      <div class="btnGroup">
        <el-button
          type="primary"
          @click="submitForm('loginform')"
          v-loading="loading"
          class="button1"
          >加入聊天室</el-button
        >
<!--        <el-button @click="resetForm('loginform')">重置</el-button>-->
        <router-link to="/register">
          <el-button type="text" style="margin-left: 10px;font-size: 10px;color: white">前去注册</el-button>
        </router-link>
      </div>
    </el-card>
  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  data() {
    return {
      loginform: {
        uname: "",
        password: "",
      },
      rules: {
        uname: [
          { required: true, message: "用户名不能为空！", trigger: "blur" },
        ],
        password: [
          { required: true, message: "密码不能为空！", trigger: "blur" },
        ],
      },
      loading: false, // 是否显示加载动画
    };
  },
  methods: {
    submitForm(formName) {
      // 验证表单中的账号密码是否有效，因为在上面rules中定义为了必填 required: true
      this.$refs[formName].validate((valid) => {
        // 点击登录后，让登录按钮开始转圈圈（展示加载动画）
        this.loading = true;
        // 如果经过校验，账号密码都不为空，则发送请求到后端登录接口
        if (valid) {
          let _this = this;
          let formdata=new FormData();
          formdata.append("uname",this.loginform.uname);
          formdata.append("password",this.loginform.password);
          // 使用 axios 将登录信息发送到后端
          this. postRequest('user/login',formdata).then(resp=>{
            if(resp.code==="200"){
              // console.log("token是"+resp.data);
              //存储用户和token
              _this.$store.commit("setToken", resp);
              _this.$store.commit("setUserInfo", resp);
              //跳转首页
                  setTimeout(()=>{
                    this.$router.replace('/home');
                  },3000)

                  this.$message.success("欢迎加入vue-chat");
            }
            // 不管响应成功还是失败，收到后端响应的消息后就不再让登录按钮显示加载动画了
            _this.loading = false;
            console.log(resp);
          }).catch(err=>{
            Message.error("账号或密码错误")
          })
        } else {  // 如果账号或密码有一个没填，就直接提示必填，不向后端请求
          this.$message.error("error submit!")
          this.loading = false;
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
};
</script>

<style scoped="less">
/* 设置登录面板居中，宽度为400px */
.box-card {
  margin: auto auto;
  width: 400px;
  background-color: rgb(234, 212, 74);;
  border-radius: 15px;
}
/deep/ .el-input__inner {
  height: 47px;
  border: 0;
}
/* 设置登录面板中的表单居中 */
.login-from {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-content: center;
}
.button1 {
  background: #faf8f8;
  color: #2c2d2c;
  border: 1px solid #126ca0;
  opacity: 1
}

.button1:active {
  background: #126c9e !important;
  font-weight: bold;
}
/*按钮悬浮*/
.button1:hover {
  background: #126c9e !important;
  color: white !important;
  font-weight: bold;
  border-color: #01a8f9 !important;
}
/*按钮点击*/
.button1:focus {
  background: #126c9e !important;
  color: white !important;
  font-weight: bold;

  border-color: #01a8f9 !important;
}

.container{
  background-image: url("@/assets/bg.jpg");
  height: 96vh;
  display: flex;
  background-size: cover; /* 背景图像铺满整个容器 */
  background-position: center; /* 背景图像居中显示 */
}
</style>
