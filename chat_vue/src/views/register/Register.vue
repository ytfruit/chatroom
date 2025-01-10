<template>
  <div class="container">
    <el-card class="box-card">
      <h2 style="color: white">欢迎加入vue-chat</h2>
      <el-form
        :model="registerForm"
        status-icon
        :rules="rules"
        ref="registerForm"
        label-position="left"
        label-width="80px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="uname">
          <el-input v-model="registerForm.uname"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pass">
          <el-input
            type="password"
            v-model="registerForm.pass"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="password">
          <el-input
            type="password"
            v-model="registerForm.password"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <div class="btnGroup">
        <el-button type="primary" @click="submitForm('registerForm')"  v-loading="loading" class="button1">注册</el-button>
<!--        <el-button @click="resetForm('registerForm')">重置</el-button>-->
        <el-button type="text" @click="goBack" style="font-size: 10px;color: white">已有账号，返回加入聊天室</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.registerForm.checkPass !== "") {
          this.$refs.registerForm.validateField("checkPass");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.registerForm.pass) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      registerForm: {
        uname: "",
        pass: "",
        password: "",
      },
      rules: {
        uname: [
          { required: true, message: "用户名不能为空！", trigger: "blur" },
        ],
        pass: [{ required: true, validator: validatePass, trigger: "blur" }],
        password: [
          { required: true, validator: validatePass2, trigger: "blur" },
        ],
      },
      loading: false
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        this.loading = true;  // 提交按钮显示加载动画
        if (valid) {
          let _this = this;
          this. postRequest('user/register',_this.registerForm).then((res) => { // 当收到后端的响应时执行该括号内的代码，res 为响应信息，也就是后端返回的信息
            if (res.code === '200') {  // 当响应的编码为 200 时，说明注册成功
              //存储用户和token
              _this.$store.commit("setToken", res);
              _this.$store.commit("setUserInfo", res);
              this.$message.success("注册成功！")
              setTimeout(()=>{
                this.$router.replace('/');
              },3000)
              // 显示后端响应的成功信息
            }
            // 不管响应成功还是失败，收到后端响应的消息后就不再让登录按钮显示加载动画了
            _this.loading = false;
            console.log(res);
          });
        } else { // 如果账号或密码有一个没填，就直接提示必填，不向后端请求
          console.log("error submit!!");
          this.loading = false;
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<style scoped>
/* 设置登录面板居中，宽度为400px */
.box-card {
  margin: auto auto;
  width: 400px;
  background-color: rgb(234, 212, 74);;
  border-radius: 15px;
}
/deep/ .el-input__inner {
  width: 70%;
  height: 47px;
  border: 0;
}
/* 设置登录面板中的表单居中 */
.button1 {
  background: #faf8f8;
  color: #2c2d2c;
  border: 1px solid #126ca0;
  opacity: 1;
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
