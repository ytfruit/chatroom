<template>
<div>
  <div class="container">
    <group></group>
    <groupInfo></groupInfo>
  </div>
</div>

</template>
<script>


import group from "@/views/group/group.vue";
import groupInfo from "@/views/group/groupInfo.vue";

export default {
  name: "chatGroupMessage",
  components:{
    group,
    groupInfo
  },
  data(){
    return{

    }
  },
  methods:{

  }
}
</script>
<style scoped="less">
.container
{
  position: relative;
  width:193vh;
  height:96vh;
  background:#fff;
  box-shadow: 0 1px 1px 0 rgba(0,0,0,0.06),0 2px 5px 0 rgba(0,0,0,0.06);
  display: flex;
}

</style>
