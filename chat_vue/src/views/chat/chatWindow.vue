<template>
  <div class="container">
    <chatGroup></chatGroup>
    <chatSpace></chatSpace>
    </div>
</template>
<script>
import chatGroup from "@/views/chat/chatGroup.vue";
import chatSpace from "@/views/chat/chatSpace.vue";

export default {
  name: "chatWindow",
  components:{
    chatGroup,
    chatSpace,
  }
}
</script>
<style scoped="less">
.container
{
  position: relative;
  width:100%;
  height:96vh;
  background:#fff;
  box-shadow: 0 1px 1px 0 rgba(0,0,0,0.06),0 2px 5px 0 rgba(0,0,0,0.06);
  display: flex;
}
</style>
