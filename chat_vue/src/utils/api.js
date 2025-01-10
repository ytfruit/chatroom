import axios from "axios"
import { Message } from 'element-ui';
import router from '../router'
import { showLoading,hideLoading } from "@/loading";
import store from "@/store";
import response from "core-js/internals/is-forced";
//import { resolve } from "core-js/fn/promise";

const instance =axios.create({
    baseURL:"http://localhost:8080/api",
    timeout:5000,
})
// 请求超时时间
axios.defaults.timeout = 10000;
//axios请求拦截器
function getJWTToken(){
    let token;
    token = localStorage.getItem("token");
    return token;
}
axios.defaults.headers['Content-Type']='application/x-www-form-urlencoded;charset=utf-8'
// 请求拦截器
instance.interceptors.request.use(
    config => {
        // 如果有 token，添加到请求头
        const token = getJWTToken();
        if (token) {
            config.headers["token"] = token;
            config.headers["Authorization"] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);
// 响应拦截器
instance.interceptors.response.use(
    response => {
        // 处理成功的响应
        const { data } = response;
        if (data.code === "200") {
            return Promise.resolve(data); // 返回数据给调用者
        } else {
            // 后端返回的非200代码，显示错误信息
            Message.error(data.msg || "请求失败");
            return Promise.reject(data); // 统一捕获错误并返回
        }
    },
    error => {
        // 处理失败的响应
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    router.replace({
                        path: "/login",
                        query: { redirect: router.currentRoute.fullPath }, // 将当前页面的路径作为参数传递给登录页面
                    });
                    Message.error("未登录，正在跳转登录页面...");
                    break;
                case 403:
                    Message.error("登录过期，请重新登录");
                    localStorage.removeItem("token");
                    store.commit("loginSuccess", null); // 清空 Vuex 中的 token
                    setTimeout(() => {
                        router.replace({
                            path: "/login",
                            query: { redirect: router.currentRoute.fullPath },
                        });
                    }, 1000);
                    break;
                case 404:
                    Message.error("请求的接口不存在");
                    break;
                case 413:
                    Message.error("文件超出服务器负载");
                    break;
                default:
                    Message.error(error.response.data.msg || "网络异常");
            }
        } else {
            Message.error("网络请求失败，请检查您的网络连接");
        }
        return Promise.reject(error); // 统一处理错误
    }
);

const createRequest = (method,url,data = {},config={})=>{
    return instance({
        url,
        method,
        data,
        ...config,
    })

}

//传送Json格式的post请求
export const postRequest=(url,data={},config={})=>{
    return createRequest('post',url,data,config)
}

//传送Json的put请求
export const putRequest=(url,data={},config={})=>{
    return createRequest('put',url,data,config)
}

//传送Json的get请求
export const getRequest=(url,data={},config={})=>{
    return createRequest('get',url,data,config)
}

//传送Json的delete请求
export const deleteRequest=(url,params={},config={})=>{
    return createRequest('delete',url,{params,...config});
}
