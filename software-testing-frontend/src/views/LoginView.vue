<template>
  <div class="login-container">
    <div class="login-box">
      <h1 class="title">养老院信息管理系统</h1>
      <form @submit.prevent="handleLoginSubmit">
        <div class="form-group">
          <label for="username">账号</label>
          <input type="text" id="username" v-model="form.username" placeholder="请输入账号" required />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input type="password" id="password" v-model="form.password" placeholder="请输入密码" required />
        </div>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        
        <!-- 按钮：绑定事件，禁用状态由 loading 控制 -->
        <button 
          type="submit" 
          class="login-button" 
          :disabled="loading" 
          @mousemove="handleMouseMove" 
          @mouseleave="handleMouseLeave" 
          @click="handleClickEffect"
          ref="loginButtonRef"
        >
          {{ loading ? '登录中...' : '登录' }}
          <!-- 点击波浪效果的元素：由 v-if 控制显示 -->
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

const router = useRouter();
const authStore = useAuthStore();

const form = reactive({
  username: '',
  password: '',
});
const loading = ref(false);
const errorMessage = ref('');
const loginButtonRef = ref(null); // 引用登录按钮元素

// --- 鼠标移动波纹效果状态 ---
let lastMouseMoveTime = 0;
const MOUSE_RIPPLE_INTERVAL = 150; // 鼠标移动波纹生成间隔(ms)

// 鼠标移动处理
const handleMouseMove = (event) => {
  if (loading.value) return; // 登录中禁用效果

  const now = Date.now();
  if (now - lastMouseMoveTime > MOUSE_RIPPLE_INTERVAL) {
    createMouseRipple(event);
    lastMouseMoveTime = now;
  }
};

// 创建鼠标波纹
const createMouseRipple = (event) => {
  const button = loginButtonRef.value;
  if (!button) {
    console.error('登录按钮元素未找到，无法创建波纹');
    return;
  }
  
  const ripple = document.createElement('span');
  ripple.className = 'mouse-ripple'; // 鼠标移动波纹的类名
  
  const rect = button.getBoundingClientRect();
  const mouseX = event.clientX - rect.left;
  const mouseY = event.clientY - rect.top;

  ripple.style.left = `${mouseX}px`;
  ripple.style.top = `${mouseY}px`;
  
  button.appendChild(ripple);
  
  // 强制重排后添加动画类
  setTimeout(() => {
    ripple.classList.add('animate');
  }, 10);
  
  // 动画结束后移除元素
  setTimeout(() => {
    if (ripple.parentNode) {
      ripple.parentNode.removeChild(ripple);
    }
  }, 1000); // 动画持续时间1秒
};

// 鼠标离开处理
const handleMouseLeave = () => {
  if (loading.value) return;
  
  // 清理所有波纹
  const ripples = document.querySelectorAll('.mouse-ripple');
  ripples.forEach(ripple => {
    if (ripple.parentNode) {
      ripple.parentNode.removeChild(ripple);
    }
  });
};

// --- 点击波浪效果状态 ---
const clickRippleActive = ref(false);
const clickRippleStyle = reactive({
  left: '0px',
  top: '0px',
  width: '0px',
  height: '0px',
  opacity: 0,
});

// 点击处理
const handleClickEffect = (event) => {
  if (loading.value) return;

  const button = event.currentTarget;
  const rect = button.getBoundingClientRect();
  
  const clickX = event.clientX - rect.left;
  const clickY = event.clientY - rect.top;

  const size = Math.max(rect.width, rect.height) * 2;

  clickRippleActive.value = true;
  clickRippleStyle.left = `${clickX}px`;
  clickRippleStyle.top = `${clickY}px`;
  clickRippleStyle.width = '0px';
  clickRippleStyle.height = '0px';
  clickRippleStyle.opacity = 0.7;

  requestAnimationFrame(() => {
    clickRippleStyle.width = `${size}px`;
    clickRippleStyle.height = `${size}px`;
    clickRippleStyle.opacity = 0;
  });

  setTimeout(() => {
    clickRippleActive.value = false;
  }, 600);
};

// --- 登录逻辑 ---
const handleLoginSubmit = async () => {
  if (loading.value) return;

  loading.value = true;
  errorMessage.value = '';

  try {
    await authStore.login({ 
      username: form.username, 
      password: form.password 
    });

    ElMessage.success('登录成功！');
    
    setTimeout(() => {
      router.push('/dashboard/home');
    }, 1500);

  } catch (error) {
    console.error('登录失败:', error);
    errorMessage.value = error.message || '用户名或密码错误';
    loading.value = false;
    handleMouseLeave(); // 清理波纹
  }
};

// --- 生命周期钩子 ---
onMounted(() => {
  // 确保按钮元素已加载
  console.log('登录按钮元素已加载:', loginButtonRef.value);
});

onUnmounted(() => {
  // 清理所有波纹元素
  const ripples = document.querySelectorAll('.mouse-ripple, .click-ripple-circle');
  ripples.forEach(ripple => {
    if (ripple.parentNode) {
      ripple.parentNode.removeChild(ripple);
    }
  });
});
</script>

<style scoped>
/* 通用布局 */
.login-container { display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f2f5; }
.login-box { width: 400px; padding: 40px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); text-align: center; }
.title { font-size: 24px; color: #333; margin-bottom: 30px; }
.form-group { margin-bottom: 20px; text-align: left; }
.form-group label { display: block; margin-bottom: 8px; color: #555; font-weight: bold; }
.form-group input { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }

/* 登录按钮样式 */
.login-button { 
  width: 100%; 
  padding: 14px; 
  color: white; 
  border: none; 
  border-radius: 12px; 
  font-size: 16px; 
  cursor: pointer; 
  position: relative; 
  overflow: hidden; 
  transition: all 0.3s ease;
  
  /* 按钮渐变色 */
  background: linear-gradient(135deg, #409eff 0%, #207be5 100%);
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.4);
}

.login-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(64, 158, 255, 0.5);
}

.login-button:disabled { 
  background: linear-gradient(135deg, #a0cfff 0%, #80b8ff 100%);
  cursor: not-allowed; 
  box-shadow: none;
  transform: translateY(0);
}

/* 鼠标移动波纹效果 (粉白色) */
.mouse-ripple {
  position: absolute;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 240, 245, 0.8) 0%, rgba(255, 240, 245, 0) 70%);
  transform: translate(-50%, -50%) scale(0);
  pointer-events: none;
  z-index: 1;
  opacity: 0; /* 初始不可见 */
}

.mouse-ripple.animate {
  opacity: 1; /* 显示波纹 */
  animation: mouseRippleAnimation 1s ease-out forwards;
}

@keyframes mouseRippleAnimation {
  0% { transform: translate(-50%, -50%) scale(0); opacity: 0.8; }
  100% { transform: translate(-50%, -50%) scale(1.5); opacity: 0; }
}

/* 点击波浪效果 (白色) */
.click-ripple-circle {
  position: absolute;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.5);
  transform: translate(-50%, -50%);
  transition: width 0.6s ease-out, height 0.6s ease-out, opacity 0.6s ease-out;
  pointer-events: none;
  z-index: 2;
}

.error-message { color: #f56c6c; margin-bottom: 15px; }
</style>    