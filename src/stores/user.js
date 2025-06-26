// src/stores/user.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { loginAPI } from '@/api/user';

export const useUserStore = defineStore('user', () => {
    // 定义 token
    const token = ref(localStorage.getItem('token') || '');

    // 定义登录方法
    const login = async ({ username, password }) => {
        const tokenData = await loginAPI({ username, password });
        token.value = tokenData; // 后端直接返回 token 字符串
        // 持久化存储 token
        localStorage.setItem('token', token.value);
    };

    // 退出登录
    const logout = () => {
        token.value = '';
        localStorage.removeItem('token');
    };

    return { token, login, logout };
});