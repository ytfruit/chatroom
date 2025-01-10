
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
    state:{
        // 存储token
        token:"",
        userId:0,
        userName:"",// 可选
        passWord:"",
        avatar_url:" ",
        roomId:1,
    },
   getters:{

   },
    mutations:{
        // 修改token，并将token存入sessionStorage
        setToken(state,userdata) {
            state.token = userdata.token;
            localStorage.setItem("token",userdata.token);
            sessionStorage.setItem("token",userdata.token);
            console.log('store、localstorage保存token成功！');
        },
        delToken(state) {
            state.token = "";
            localStorage.clear();
            sessionStorage.clear();
        },
        // 可选
        setUserInfo(state, userdata) {
            state.uid = userdata.data.uid;
            state.userName = userdata.data.uname;
            state.passWord=userdata.data.password;
            state.avatar_url=userdata.data.avatarUrl;
            sessionStorage.setItem("userId",state.uid)
            sessionStorage.setItem("userName",state.userName)
            sessionStorage.setItem("password",state.passWord);
            sessionStorage.setItem("roomId",state.roomId);
        },
    },
    actions: {

    },
    modules: {

    },

    // plugins:[
    //     createPresistedstate({
    //         storage:window.sessionStorage,
    //         reducer(val){
    //             return{
    //                 iframeSrc:'https://www.scratch5.com/scratch/',
    //                 ruleForm:val.ruleForm,
    //                 routes:val.routes,
    //                 newStu:val.newStu,
    //                 id_section:val.id_section,
    //                 id_course:val.id_course,
    //                 id_qs:val.id_qs,
    //                 id_q:val.id_q,
    //                 id_Q:val.id_Q,
    //                 id_c:val.id_c,
    //                 id_homework:val.id_homework,
    //                 question:val.question,
    //                 grade:val.grade,
    //                 video_yet:val.video_yet,
    //                 section_c:val.section_c,
    //                 sectiondetail:val.sectiondetail,
    //                 timeendUp:val.timeendUp,
    //             }
    //         }
    //     })
    // ]

})



