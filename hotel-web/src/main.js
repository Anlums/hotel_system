import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './assets/main.css' // 确保路径正确

// ... 其他引入
// createApp(App).mount('#app')
import axios from 'axios'
import { ElMessageBox } from 'element-plus'
// 设置基础 URL (这样你 App.vue 里就不用写 http://localhost:8080 了)
axios.defaults.baseURL = 'http://localhost:8080'
// 添加响应拦截器
axios.interceptors.response.use(
    response => {
        // 1. 检查后端返回的业务状态码
        const res = response.data;
        if (res.code && res.code !== 200) {
            // 关键修改：使用 res.msg 获取后端传回的具体错误信息
            ElMessageBox.alert(res.msg || '操作无法完成', '业务提醒', {
                confirmButtonText: '确定',
                type: 'warning', // 业务错误建议用 warning 颜色
                center: true,
                draggable: true
            });
            return Promise.reject(new Error(res.msg || 'Error'));
        }
        return response;
    },
    error => {
        // 2. 处理非 200 系列的网络请求错误（如 500, 404 等）
        // 尝试获取后端异常处理器抛出的 JSON 信息
        const errorMsg = error.response?.data?.msg || '服务器连接异常，请检查网络';

        ElMessageBox.alert(errorMsg, '系统错误', {
            confirmButtonText: '好的',
            type: 'error',
            center: true
        });
        return Promise.reject(error);
    }
);

const app = createApp(App)
app.use(ElementPlus) // --- 还有这一行 ---
app.mount('#app')