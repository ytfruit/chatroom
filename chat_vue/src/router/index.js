// 此文件专门负责项目的路由

import VueRouter from "vue-router"

// 引入组件
import Login from '@/views/login/Login.vue'
import Register from '@/views/register/Register.vue'
import Home from '@/views/home/Home.vue'
import { Message } from "element-ui";
import Router from "vue-router";
import Vue from "vue";
import store from "@/store";
import chatSpace from "@/views/chat/chatSpace.vue";
import chatWindow from "@/views/chat/chatWindow.vue";
import groupWindow from "@/views/group/groupWindow.vue";
import groupInfo from "@/views/group/groupInfo.vue";
Vue.use(Router)
// 创建并暴露一个路由器
const router = new VueRouter({
    mode: 'history',    // 路由模式，该模式不会在地址中显示井号#
    routes: [
        {
            path: '/',          // 路径
            redirect: '/login'  // 重定向
        },
        {
            path: '/login',     // 路径
            meta:{
                title:"vue-chat"
            },
            component: Login    // 跳转到的组件
        },
        {
            path: '/register',     // 路径
            component: Register,    // 跳转到的组件
            meta:{
                title:"注册"
            },
        },
        {
            path: '/home',     // 路径
            component: Home,    // 跳转到的组件
            redirect: '/chatspace', // 重定向
            children:[
                {
                    path: '/chatspace',     // 路径
                    component: chatWindow,    // 跳转到的组件
                    meta:{
                        title:"聊天室"
                    },
                },
                {
                    path: '/group',     // 路径
                    component: groupWindow,    // 跳转到的组件
                    meta:{
                        title:"群组"
                    },

                },
                {
                    path: '/groupInfo/:roomId/:roomName',
                    name: 'groupInfo',
                    component: groupInfo
                }
            ]
        },
    ]
})

router.beforeEach((to, from, next) => {
    let isAuthenticated = !!sessionStorage.getItem('token')
    // 如果路由要跳转到除了登录和注册的界面的话就判断是否已经登录，如果没有登录就强制跳到登录界面
    if (to.path !== '/login' && to.path !== '/register' && !isAuthenticated) {
        next({ path: '/login' })
        Message({
            message: '请先登录！',
            type: "warning",
        });
    } else next()
    if(to.meta.title){
        document.title=to.meta.title
    }else next()
    if (to.matched.some(r => r.meta.requireAuth)) {
        if (store.state.token) {
            next(); //有token,进行request请求，后台还会验证token
        } else {
            next({
                path: "/login",
                // 将刚刚要去的路由path（却无权限）作为参数，方便登录成功后直接跳转到该路由，这要进一步在登陆页面判断
                query: { redirect: to.fullPath }
            });
        }
    } else {
        next(); //如果无需token,那么随它去吧
    }
})
export default router;

/*解决导航栏路由多次跳转问题*/
//先把VueRouter原型对象的push,先保存一份
let originPush = VueRouter.prototype.push;
let originReplace=VueRouter.prototype.replace;

//第一个参数: 告诉原来push 方法，你往哪里跳转(传递哪些参数)
VueRouter.prototype.push = function(location, resolve, reject) {
    //第一个形参：路由跳转的配置对象（query|params）
    //第二个参数：undefined|箭头函数（成功的回调）
    //第三个参数:undefined|箭头函数（失败的回调）
    if (resolve && reject) {
        //push方法传递第二个参数|第三个参数（箭头函数）
        //originPush：利用call修改上下文，变为(路由组件.$router)这个对象，第二参数：配置对象、第三、第四个参数：成功和失败回调函数
        originPush.call(this, location, resolve, reject);
    } else {
        //push方法没有产地第二个参数|第三个参数
        originPush.call(
            this,
            location,
            () => {},
            () => {}
        );
    }
};

//重写VueRouter.prototype身上的replace方法了
VueRouter.prototype.replace = function(location, resolve, reject) {
    if (resolve && reject) {
        originReplace.call(this, location, resolve, reject);
    } else {
        originReplace.call(
            this,
            location,
            () => {},
            () => {}
        );
    }
};
