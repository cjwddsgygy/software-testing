// src/stores/auth.js

import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { loginAPI } from '@/api/admin';
import apiClient from '@/api/request';

export const useAuthStore = defineStore('auth', () => {
    const token = ref('');
    const user = ref(null);

    const isLoggedIn = computed(() => !!token.value);

    const login = async ({ username, password }) => {
        // loginAPI 返回的是 AxiosResponse 对象，其 data 属性才是后端返回的 Result 对象
        const response = await loginAPI({ account: username, password: password }); 
        
        // 从 Axios Response 中提取出后端返回的 Result 对象
        const result = response.data; // result 现在是 { code, msg, data (token字符串) }

        // 检查后端业务码和数据
        if (result && result.code === 0 && typeof result.data === 'string') {
            // ✅ 核心修改：从 result.data 中获取真正的 token 字符串
            token.value = result.data; 
            
            // 动态更新 Axios 请求头
            apiClient.defaults.headers.common['Authorization'] = `Bearer ${token.value}`;
        } else {
            // 登录失败，抛出错误
            throw new Error(result?.msg || '登录失败');
        }
    };

    const logout = () => {
        token.value = '';
        user.value = null;
        delete apiClient.defaults.headers.common['Authorization'];
    };

    return { token, user, isLoggedIn, login, logout };

}, {
    persist: true, 
});