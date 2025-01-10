<template>
  <el-dialog title="用户详情" :visible.sync="visible" width="30%">
    <el-form :model="userDetails" label-width="100px">
      <el-form-item label="头像">
        <img :src="userDetails.avatarUrl" class="avatar" />
      </el-form-item>
      <el-form-item label="用户ID">
        <el-input v-model="userDetails.uid" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="userDetails.uname" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-input v-model="userDetails.sex" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="年龄">
        <el-input v-model="userDetails.age" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="个性签名">
        <el-input type="textarea" v-model="userDetails.signature" :disabled="true"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      userDetails: {
        uid: null,
        uname: "",
        avatarUrl: "",
        sex: "",
        age: null,
        signature: "",
      },
    };
  },
  methods: {
    open(user) {
      this.userDetails = { ...user };
      this.visible = true;
    },
  },
};
</script>

<style scoped>
.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}
</style>