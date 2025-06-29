<template>
  <div class="login-container">
    <div class="login-box">
      <h1 class="title">{{ settingsStore.systemName }}</h1>
      <p class="subtitle">欢迎使用，请登录</p>

      <form @submit.prevent="handleLoginSubmit" class="login-form">
        <!-- 角色选择 -->
        <div class="form-group">
          <label for="role"><i class="fas fa-user-shield"></i> 登录身份</label>
          <div class="select-wrapper">
            <select id="role" v-model="form.role">
              <option value="admin">管理员</option>
              <option value="careworker">护工</option>
              <option value="elder">老人</option>
            </select>
          </div>
        </div>

        <!-- 账号输入 -->
        <div class="form-group">
          <label for="username"><i class="fas fa-user"></i> 账号</label>
          <input type="text" id="username" v-model="form.username" placeholder="请输入账号" required />
        </div>

        <!-- 密码输入 -->
        <div class="form-group">
          <label for="password"><i class="fas fa-lock"></i> 密码</label>
          <input type="password" id="password" v-model="form.password" placeholder="请输入密码" required />
        </div>

        <!-- 错误信息展示 -->
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        
        <!-- 登录按钮 -->
        <button 
          type="submit" 
          class="login-button" 
          :disabled="loading" 
          @mousemove="handleMouseMove" 
          @mouseleave="handleMouseLeave" 
          @click="handleClickEffect"
          ref="loginButtonRef"
        >
          <span v-if="!loading">{{ '登 录' }}</span>
          <div v-else class="loading-spinner"></div>
          <span v-if="clickRippleActive" class="click-ripple-circle" :style="clickRippleStyle"></span>
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useAuthStore } from '@/stores/auth';
import { useSettingsStore } from '@/stores/settings';

// --- 状态与实例 ---
const router = useRouter();
const authStore = useAuthStore();
const settingsStore = useSettingsStore(); // 获取系统设置，用于显示系统名称

const form = reactive({
  username: '',
  password: '',
  role: 'admin', // 默认登录角色
});

const loading = ref(false);
const errorMessage = ref('');
const loginButtonRef = ref(null);

// --- 登录逻辑 ---
const handleLoginSubmit = async () => {
  if (loading.value) return;

  loading.value = true;
  errorMessage.value = '';

  try {
    // 调用 authStore 中的 login action
    await authStore.login({ 
      account: form.username, 
      password: form.password,
      role: form.role
    });

    ElMessage.success('登录成功！即将进入系统...');
    
    // 延迟一小段时间，让用户看到成功提示
    setTimeout(() => {
      router.push('/dashboard/home');
    }, 1000);

  } catch (error) {
    console.error('登录失败:', error);
    errorMessage.value = error.message || '用户名或密码错误';
    loading.value = false;
    handleMouseLeave(); // 失败时清理按钮效果
  }
};


// --- 按钮交互效果 (代码保持不变) ---
let lastMouseMoveTime = 0;
const MOUSE_RIPPLE_INTERVAL = 150;

const handleMouseMove = (event) => {
  if (loading.value) return;
  const now = Date.now();
  if (now - lastMouseMoveTime > MOUSE_RIPPLE_INTERVAL) {
    createMouseRipple(event);
    lastMouseMoveTime = now;
  }
};

const createMouseRipple = (event) => {
  const button = loginButtonRef.value;
  if (!button) return;
  
  const ripple = document.createElement('span');
  ripple.className = 'mouse-ripple';
  
  const rect = button.getBoundingClientRect();
  const mouseX = event.clientX - rect.left;
  const mouseY = event.clientY - rect.top;

  ripple.style.left = `${mouseX}px`;
  ripple.style.top = `${mouseY}px`;
  button.appendChild(ripple);
  
  setTimeout(() => ripple.classList.add('animate'), 10);
  setTimeout(() => ripple.parentNode?.removeChild(ripple), 1000);
};

const handleMouseLeave = () => {
  if (loading.value) return;
  const ripples = document.querySelectorAll('.mouse-ripple');
  ripples.forEach(ripple => ripple.parentNode?.removeChild(ripple));
};

const clickRippleActive = ref(false);
const clickRippleStyle = reactive({
  left: '0px', top: '0px', width: '0px', height: '0px', opacity: 0,
});

const handleClickEffect = (event) => {
  if (loading.value) return;
  const button = event.currentTarget;
  const rect = button.getBoundingClientRect();
  const clickX = event.clientX - rect.left;
  const clickY = event.clientY - rect.top;
  const size = Math.max(rect.width, rect.height) * 2;

  clickRippleActive.value = true;
  Object.assign(clickRippleStyle, {
    left: `${clickX}px`, top: `${clickY}px`,
    width: '0px', height: '0px', opacity: 0.7,
  });

  requestAnimationFrame(() => {
    Object.assign(clickRippleStyle, {
      width: `${size}px`, height: `${size}px`, opacity: 0,
    });
  });

  setTimeout(() => { clickRippleActive.value = false; }, 600);
};

// --- 生命周期钩子 ---
onUnmounted(() => {
  handleMouseLeave();
});
</script>

<style scoped>
/* --- 整体布局与背景 --- */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #e0eafc 0%, #cfdef3 100%);
  overflow: hidden;
}

/* --- 登录框样式 --- */
.login-box {
  width: 400px;
  padding: 40px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  text-align: center;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.title {
  font-size: 26px;
  color: #2c3e50;
  margin-bottom: 10px;
  font-weight: 600;
}
.subtitle {
  font-size: 16px;
  color: #7f8c8d;
  margin-bottom: 35px;
}

/* --- 表单样式 --- */
.login-form {
  display: flex;
  flex-direction: column;
}
.form-group {
  margin-bottom: 20px;
  text-align: left;
  position: relative;
}
.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #34495e;
  font-weight: 500;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.form-group input, .form-group select {
  width: 100%;
  padding: 12px;
  padding-left: 15px; /* 为图标留出空间 */
  border: 1px solid #bdc3c7;
  border-radius: 8px;
  box-sizing: border-box;
  font-size: 16px;
  transition: all 0.2s ease;
}
.form-group input:focus, .form-group select:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2);
}
.select-wrapper {
  position: relative;
}
.select-wrapper::after {
  content: '\f078'; /* Font Awesome Chevron Down */
  font-family: 'Font Awesome 5 Free';
  font-weight: 900;
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #7f8c8d;
  pointer-events: none;
}
.form-group select {
  appearance: none; /* 移除默认箭头 */
}

/* --- 登录按钮 --- */
.login-button { 
  width: 100%; 
  padding: 14px; 
  color: white; 
  border: none; 
  border-radius: 8px; 
  font-size: 18px; 
  font-weight: 600;
  letter-spacing: 2px;
  cursor: pointer; 
  position: relative; 
  overflow: hidden; 
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #409eff 0%, #207be5 100%);
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.4);
}
.login-button:hover:not(:disabled) {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(64, 158, 255, 0.5);
}
.login-button:disabled { 
  background: #a0cfff;
  cursor: not-allowed; 
  box-shadow: none;
  transform: translateY(0);
}

/* --- 按钮加载中 spinner --- */
.loading-spinner {
  width: 24px;
  height: 24px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto;
}
@keyframes spin {
  to { transform: rotate(360deg); }
}

/* --- 错误信息 --- */
.error-message { 
  color: #e74c3c; 
  margin-top: -10px;
  margin-bottom: 15px; 
  font-weight: 500;
  min-height: 20px;
}

/* --- 按钮波纹效果 (保持不变) --- */
.mouse-ripple {
  position: absolute;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.4) 0%, rgba(255, 255, 255, 0) 70%);
  transform: translate(-50%, -50%) scale(0);
  pointer-events: none;
  z-index: 1;
  opacity: 0;
}
.mouse-ripple.animate {
  opacity: 1;
  animation: mouseRippleAnimation 1s ease-out forwards;
}
@keyframes mouseRippleAnimation {
  0% { transform: translate(-50%, -50%) scale(0); opacity: 0.5; }
  100% { transform: translate(-50%, -50%) scale(1.5); opacity: 0; }
}
.click-ripple-circle {
  position: absolute;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.7);
  transform: translate(-50%, -50%);
  transition: width 0.6s ease-out, height 0.6s ease-out, opacity 0.6s ease-out;
  pointer-events: none;
  z-index: 2;
}
</style>