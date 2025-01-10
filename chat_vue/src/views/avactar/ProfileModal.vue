<template>
  <el-dialog title="编辑个人信息" :visible.sync="visible" width="30%">
    <el-form :model="form" label-width="80px" :rules="rules" ref="form">
      <el-form-item label="头像" prop="avatarUrl">
        <!-- 图片上传组件 -->
        <el-upload
            ref="imageUpload"
            action="http://localhost:8082/update/avactar"
            :show-file-list="false"
            :accept="'image/jpeg, image/png'"
            :before-upload="beforeAvatarUpload"
            :on-change="handleAvatarSuccess"
            :auto-upload="false"
        >
          <img v-if="imageUrl" :src="imageUrl" class="avatar" />
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <div class="upload-tip">建议上传 100x100 像素的图片，大小不超过 2MB</div>
      </el-form-item>
      <el-form-item label="昵称" prop="uname">
        <el-input v-model="form.uname" placeholder="请输入昵称"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="form.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input type="password" v-model="form.confirmPassword" placeholder="请再次输入密码"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select v-model="form.sex" placeholder="请选择性别">
          <el-option label="男" value="男"></el-option>
          <el-option label="女" value="女"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model.number="form.age" placeholder="请输入年龄"></el-input>
      </el-form-item>
      <el-form-item label="个性签名" prop="signature">
        <el-input type="textarea" v-model="form.signature" placeholder="请输入个性签名"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitForm" :loading="loading">保存</el-button>
       <el-button type="info" @click="showUserDetails">详情</el-button>
    </span>
    <!-- 引入 UserDetailsModal 组件 -->
    <UserDetailsModal ref="userDetailsModal" />
  </el-dialog>

</template>

<script>
import {Message} from "element-ui";
import UserDetailsModal from "@/views/avactar/UserDetailsModal.vue"; // 引入 UserDetailsModal 组件
export default {
  components: {
    UserDetailsModal, // 注册 UserDetailsModal 组件
  },
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value !== this.form.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };
    return {
      visible: false,
      imageUrl:undefined,
      form: {
        uid: null,
        uname: "",
        password: "",
        confirmPassword: "",
        sex: "",
        age: null,
        signature: "",
      },
      rules: {
        uname: [
          { required: true, message: '请输入昵称', trigger: 'blur' },
          { min: 2, max: 10, message: '昵称长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validatePassword, trigger: 'blur' }
        ],
        sex: [], // 性别为非必填
        age: [], // 年龄为非必填
        signature: [] // 个性签名为非必填
      },
      loading: false,
    };
  },
  methods: {
    open(user) {
      this.form = { ...user, confirmPassword: '' };
      this.visible = true;
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true;
          this.form.uid=sessionStorage.getItem("userId");
          this.postRequest("/update/userInfo", this.form).then(resp => {
            if (resp.code === "200") {
              this.$message.success("用户信息更新成功");
              this.visible = false;
            } else {
              this.$message.error(resp.data.msg || "用户信息更新失败");
            }
          }).catch(err => {
            this.$message.error("请求失败，请检查网络或稍后重试");
          }).finally(() => {
            this.loading = false;
          });
        } else {
          this.$message.error("请检查表单输入是否正确");
        }
      });
    },

    handleAvatarSuccess(file, fileList) {
      const realFile = file.raw || file;
      if (realFile instanceof File) {
        this.upload(realFile);
      } else {
        Message.error("上传的文件无效");
      }
    },
    upload(file){
      let uid=sessionStorage.getItem("userId");
      const formData = new FormData();
      formData.append("file", file);
      formData.append("userId",uid);
      this.postRequest('/update/avactar',formData).then((resp)=>{
        if(resp.code === "200"){
          this.imageUrl=resp.data.fileUrl;
          this.resetSetItem('watchStorage', this.imageUrl);
        }
        else{
          Message.error(resp.msg);
        }
      })
    },
    beforeAvatarUpload(file) {
      const isImage = file.type.startsWith("image/");
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error("只能上传图片文件！");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB！");
      }
      return isImage && isLt2M;
    },
    showUserDetails() {
      this.form.uid=sessionStorage.getItem("userId");
      // 从数据库中获取最新的用户信息
      this.getRequest(`/update/user/details/${this.form.uid}`,this.form).then(resp => {
        if (resp.code === "200") {
          this.$refs.userDetailsModal.open(resp.data);
          console.log(resp.data);
        } else {
          this.$message.error("获取用户信息失败");
        }
      }).catch(err => {
        this.$message.error("请求失败，请检查网络或稍后重试");
      });
    },
  }
};
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: blue;
}
.avatar {
  width: 100px;
  height: 100px;
  display: block;
}
.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}
</style>
