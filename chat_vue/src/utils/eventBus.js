// main.js （假设项目入口文件是 main.js，你可以按需调整位置）
import Vue from 'vue';

// 创建一个新的Vue实例作为事件总线
const eventBus = new Vue();

// 将事件总线导出，方便其他组件引入使用
export {
    eventBus
};
