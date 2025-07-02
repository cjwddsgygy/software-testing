// 文件路径: src/stores/auth.js

import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { loginAPI } from '@/api/admin'; // 假设你的通用登录API封装在这里
import apiClient from '@/api/request'; // 导入你的 axios 实例

export const useAuthStore = defineStore('auth', () => {
    // --- State ---
    const token = ref(null);
    const user = ref(null);
    const role = ref(null);

    // --- Getters ---
    const isLoggedIn = computed(() => !!token.value);
    const isAdmin = computed(() => role.value === 'admin');
    const isCareWorker = computed(() => role.value === 'careworker');
    const isElder = computed(() => role.value === 'elder');

    // --- Actions ---
    const login = async ({ account, password, role: userRole }) => {
        try {
            const response = await loginAPI({ account, password, role: userRole }); 
            const result = response.data;

            if (result && result.code === 0 && result.data && result.data.token) {
                // 1. 更新 Pinia state
                token.value = result.data.token;
                user.value = result.data.user;
                role.value = result.data.role;
                
                // 2. 核心修复：立即、手动地更新 apiClient 的默认请求头
                apiClient.defaults.headers.common['Authorization'] = `Bearer ${token.value}`;

                return Promise.resolve();
            } else {
                throw new Error(result?.msg || '登录失败，服务器返回数据格式不正确');
            }
        } catch (error) {
            // 登录失败时，确保清除了所有旧状态
            logout();
            console.error('Auth Store Login Error:', error.response?.data?.msg || error.message);
            throw new Error(error.response?.data?.msg || '网络错误或凭证无效');
        }
    };

    const logout = () => {
        // 手动将所有 state 属性重置为初始值
        token.value = null;
        user.value = null;
        role.value = null;

        // 清除 apiClient 的请求头
        delete apiClient.defaults.headers.common['Authorization'];
        
        // 清除本地存储
        localStorage.removeItem('auth');
    };



    return { 
        token, user, role,
        isLoggedIn, isAdmin, isCareWorker, isElder,
        login, logout,
    };

}, {
    // 开启持久化，token, user, role 会被自动保存到 localStorage
    persist: true, 
});