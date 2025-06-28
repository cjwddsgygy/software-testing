// src/stores/auth.js

import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { loginAPI } from '@/api/admin'; // 您的登录 API 封装
import apiClient from '@/api/request'; // 您的 axios 实例

export const useAuthStore = defineStore('auth', () => {
    // state: 存储在 Pinia 中的响应式数据
    const token = ref(''); // JWT token 字符串
    const user = ref(null); // 用户信息对象

    // getters: 计算属性
    const isLoggedIn = computed(() => !!token.value); // 判断是否登录

    // actions: 异步操作和状态修改
    const login = async ({ username, password }) => {
        try {
            // 调用登录 API。假设 loginAPI 返回的是 AxiosResponse 对象，其 data 属性才是后端返回的 Result 对象
            const response = await loginAPI({ account: username, password: password }); 
            
            // 从 Axios Response 中提取出后端返回的 Result 对象
            // 假设后端返回的 result.data 是一个包含 token 字符串和 user 对象的对象
            // 例如：{ code: 0, msg: "", data: { token: "your_jwt_token", user: {id:1, name:"..."} } }
            const result = response.data; 

            // 检查后端业务码和数据结构
            if (result && result.code === 0 && typeof result.data === 'string') {
                // 更新 Pinia state
                token.value = result.data

                // Pinia 的 persist 插件会自动将 state (token 和 user) 持久化到 localStorage 的 'auth' 键中
                // 格式会是 {"token":"...", "user":null}，这正是您要求的格式。
                
                // 动态更新 apiClient 的默认请求头，用于后续的所有请求
                apiClient.defaults.headers.common['Authorization'] = `Bearer ${token.value}`;

                return Promise.resolve(); // 登录成功
            } else {
                // 登录失败，抛出错误，交给 LoginView 处理
                const errorMsg = result?.msg || '登录失败，服务器返回数据格式不正确或缺少Token';
                throw new Error(errorMsg);
            }
        } catch (error) {
            // 捕获网络错误或后端返回非 2xx 的响应
            console.error('Auth Store Login Error:', error.response?.data?.msg || error.message);
            // 重新抛出错误，让 LoginView 组件来显示错误信息
            throw new Error(error.response?.data?.msg || '网络错误，请稍后再试');
        }
    };

    const logout = () => {
        // 清除 Pinia store 中的状态
        token.value = '';
        user.value = null;

        // 清除 apiClient 默认请求头中的 Authorization
        delete apiClient.defaults.headers.common['Authorization'];

        // ✅ 核心修改：手动从 localStorage 中移除 'auth' 键
        // 虽然 persist 插件会尝试将空状态写入，但手动 removeItem 确保键被完全移除
        localStorage.removeItem('auth');
    };

    // 返回要暴露给组件的状态和动作
    return { token, user, isLoggedIn, login, logout };

}, {
    // Pinia 持久化插件配置
    persist: true, 
});