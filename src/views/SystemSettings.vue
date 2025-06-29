<template>
  <div class="system-settings-view">
    <div class="view-header">
      <h2 class="view-title">系统设置</h2>
      <p class="view-description">自定义您的系统外观、功能和安全选项</p>
    </div>

    <div class="settings-layout">
      <!-- 左侧导航菜单 -->
      <div class="settings-nav">
        <a 
          v-for="item in navItems" 
          :key="item.id" 
          :href="`#${item.id}`" 
          class="nav-item"
          :class="{ active: activeSection === item.id }"
          @click.prevent="scrollToSection(item.id)"
        >
          <i :class="item.icon"></i>
          <span>{{ item.title }}</span>
        </a>
      </div>

      <!-- 右侧设置内容 -->
      <div class="settings-content-wrapper" ref="contentArea">
        <div v-if="loading" class="loading-placeholder">
          <div class="loading-spinner"></div>
        </div>
        <form v-else @submit.prevent="saveSettings" class="settings-form">
          
          <!-- 基本设置 -->
          <div id="basic" class="form-section">
            <h3 class="section-title"><i class="fas fa-sliders-h"></i> 基本信息</h3>
            <div class="form-grid">
              <div class="form-field">
                <label for="systemName">系统名称</label>
                <div class="input-wrapper">
                  <!-- ✅ 已修正：v-model="localSettings.systemName" -->
                  <input id="systemName" v-model="localSettings.systemName" type="text" placeholder="养老院信息管理系统" required>
                </div>
                <p class="field-hint">将显示在浏览器标签页和登录页面。</p>
              </div>
              <div class="form-field">
                <label for="copyright">底部版权信息</label>
                 <div class="input-wrapper">
                  <!-- ✅ 已修正：v-model="localSettings.copyright" -->
                  <input id="copyright" v-model="localSettings.copyright" type="text" placeholder="© 2025 Your Company">
                </div>
                <p class="field-hint" v-pre>支持 `{{year}}` 动态年份。例如: `© {{year}} Yusheng`</p>
              </div>
            </div>
          </div>

          <!-- 外观设置 -->
          <div id="appearance" class="form-section">
            <h3 class="section-title"><i class="fas fa-palette"></i> 外观与主题</h3>
            <div class="form-grid">
              <div class="form-field">
                <label>系统主题</label>
                <div class="theme-selector">
					<div class="theme-option" :class="{active: localSettings.theme === 'system'}" @click="setTheme('system')">
					<div class="preview system"><i class="fas fa-desktop"></i></div>
					<span>跟随系统</span>
					</div>
                  <!-- ✅ 已修正：:class active 判断 -->
                  <div class="theme-option" :class="{active: localSettings.theme === 'light'}" @click="setTheme('light')">
                    <div class="preview light"><i class="fas fa-sun"></i></div>
                    <span>浅色模式</span>
                  </div>
                  <div class="theme-option" :class="{active: localSettings.theme === 'dark'}" @click="setTheme('dark')">
                    <div class="preview dark"><i class="fas fa-moon"></i></div>
                    <span>深色模式</span>
                  </div>
                </div>
              </div>
              <div class="form-field">
                 <label>系统 Logo</label>
                 <div class="image-uploader">
                    <!-- ✅ 已修正：v-if="localSettings.logo" -->
                    <img v-if="localSettings.logo" :src="localSettings.logo" class="image-preview"/>
                    <div v-else class="image-placeholder"><i class="far fa-image"></i></div>
                    <div class="uploader-actions">
                      <button type="button" class="btn btn-sm btn-secondary" @click="triggerFileUpload('logo')">上传 Logo</button>
                      <p class="field-hint">用于登录页，建议透明背景PNG</p>
                    </div>
                 </div>
              </div>
              <div class="form-field">
                <label>系统图标 (Favicon)</label>
                <div class="image-uploader">
                    <!-- ✅ 已修正：v-if="localSettings.favicon" -->
                    <img v-if="localSettings.favicon" :src="localSettings.favicon" class="image-preview favicon"/>
                    <div v-else class="image-placeholder favicon"><i class="far fa-star"></i></div>
                    <div class="uploader-actions">
                      <button type="button" class="btn btn-sm btn-secondary" @click="triggerFileUpload('favicon')">上传图标</button>
                      <p class="field-hint">32x32px .ico 或 .png</p>
                    </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 安全设置 -->
          <div id="security" class="form-section">
            <h3 class="section-title"><i class="fas fa-shield-alt"></i> 安全与会话</h3>
            <div class="form-field">
              <label for="sessionTimeout">会话超时时长 (分钟)</label>
              <div class="slider-wrapper">
                 <!-- ✅ 已修正：v-model.number="localSettings.sessionTimeout" -->
                 <input type="range" min="5" max="120" step="5" v-model.number="localSettings.sessionTimeout" class="slider" />
                 <!-- ✅ 已修正：{{ localSettings.sessionTimeout }} -->
                 <span class="slider-value">{{ localSettings.sessionTimeout }} 分钟</span>
              </div>
              <p class="field-hint">用户无操作超过此时长后将自动登出。</p>
            </div>
          </div>
          
          <!-- 危险区域 -->
          <div id="danger" class="form-section danger-zone">
             <h3 class="section-title"><i class="fas fa-exclamation-triangle"></i> 危险区域</h3>
             <div class="danger-item">
                <div>
                  <h4>恢复默认设置</h4>
                  <p>将所有系统设置恢复为出厂默认值，此操作不可撤销。</p>
                </div>
                <button type="button" class="btn btn-danger" @click="confirmResetToDefaults">恢复默认值</button>
             </div>
          </div>
          
          <!-- 悬浮的操作栏 -->
          <div class="floating-actions" :class="{show: hasChanges}">
             <p>检测到未保存的修改</p>
             <div>
                <button type="button" class="btn btn-secondary" @click="resetLocalChanges">撤销修改</button>
                <button type="submit" class="btn btn-primary">
                  <i class="fas fa-save"></i> 保存设置
                </button>
             </div>
          </div>
        </form>
      </div>
    </div>
    
    <!-- 隐藏的文件上传输入框 -->
    <input ref="fileInput" type="file" @change="handleFileSelect" accept="image/png, image/jpeg, image/x-icon, image/svg+xml" style="display: none;" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, computed, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import _ from 'lodash';
import apiClient from '@/api/request';
import { useSettingsStore } from '@/stores/settings';

// --- 状态定义 ---
const settingsStore = useSettingsStore();
const loading = ref(true);
const localSettings = reactive({});
let originalSettings = {};
const hasChanges = computed(() => !_.isEqual(localSettings, originalSettings));
const fileInput = ref(null);
let uploadTarget = null;
const contentArea = ref(null);
const activeSection = ref('basic');
let observer = null;
const navItems = ref([
  { id: 'basic', title: '基本信息', icon: 'fas fa-sliders-h' },
  { id: 'appearance', title: '外观与主题', icon: 'fas fa-palette' },
  { id: 'security', title: '安全与会话', icon: 'fas fa-shield-alt' },
  { id: 'danger', title: '危险区域', icon: 'fas fa-exclamation-triangle' },
]);


// --- 页面与数据处理函数 ---

const syncLocalStateFromStore = () => {
  const stateSnapshot = _.cloneDeep(settingsStore.$state);
  Object.assign(localSettings, stateSnapshot);
  originalSettings = stateSnapshot;
};

const initializePage = async () => {
  loading.value = true;
  try {
    await settingsStore.fetchAndApplySettings();
    syncLocalStateFromStore();
  } catch (error) {
    ElMessage.error('加载系统设置失败，将使用本地缓存。');
    syncLocalStateFromStore();
  } finally {
    loading.value = false;
    // DOM 更新后，再设置滚动监听
    nextTick(() => {
      setupIntersectionObserver();
    });
  }
};

const saveSettings = async () => {
  if (!hasChanges.value) {
    ElMessage.info('没有检测到任何修改。');
    return;
  }
  try {
    await apiClient.post('/api/settings', localSettings);
    settingsStore.applyAllSettings(localSettings);
    originalSettings = _.cloneDeep(localSettings);
    ElMessage.success('系统设置已保存！');
  } catch (error) {
    ElMessage.error('保存失败，请重试。');
  }
};

const resetLocalChanges = () => {
  Object.assign(localSettings, _.cloneDeep(originalSettings));
  settingsStore.previewTheme(originalSettings.theme);
};


// --- UI 交互函数 ---

const setTheme = (theme) => {
  localSettings.theme = theme;
  settingsStore.previewTheme(theme);
};

const confirmResetToDefaults = () => {
  ElMessageBox.confirm(
    '此操作将重置所有系统设置为出厂默认值，且不可撤销。您确定要继续吗？',
    '警告',
    { confirmButtonText: '确定重置', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => {
    try {
      await apiClient.post('/api/settings/reset');
      ElMessage.success('已恢复为默认设置！正在重新加载...');
      await initializePage();
    } catch (error) {
      ElMessage.error('恢复默认设置失败。');
    }
  }).catch(() => {});
};

const triggerFileUpload = (target) => {
  uploadTarget = target;
  fileInput.value.click();
};

const handleFileSelect = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  const formData = new FormData();
  formData.append('file', file);
  ElMessage.info('图片上传中...');
  try {
    // 假设后端上传接口是 /api/files/upload
    const response = await apiClient.post('/api/files/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    if (response.data && response.data.code === 0) {
        localSettings[uploadTarget] = response.data.data.url;
        ElMessage.success(`${uploadTarget === 'logo' ? 'Logo' : '图标'} 上传成功! 请记得保存设置。`);
    } else {
        ElMessage.error(response.data.msg || '上传失败');
    }
  } catch (error) {
    ElMessage.error('文件上传请求失败!');
  }
  fileInput.value.value = '';
};


// ✅✅✅ 滚动相关功能实现 ✅✅✅

// 功能1：点击左侧导航，右侧平滑滚动
const scrollToSection = (id) => {
  const element = document.getElementById(id);
  if (element && contentArea.value) {
    contentArea.value.scrollTo({
      top: element.offsetTop - 20,
      behavior: 'smooth',
    });
    // 点击时手动设置高亮，可以提供更快的反馈，但 IntersectionObserver 最终会校准它
    activeSection.value = id; 
  }
};

// 功能2：滚动右侧内容，自动高亮左侧导航
const setupIntersectionObserver = () => {
  // 如果已经存在一个 observer，先清理掉，防止重复监听
  if (observer) {
    observer.disconnect();
  }

  const callback = (entries) => {
    // 我们只关心当前与视口交叉最显著的那个条目
    // 这可以防止在滚动到两个 section 中间时高亮状态来回跳动
    const intersectingEntry = entries.find(entry => entry.isIntersecting);
    if (intersectingEntry) {
      activeSection.value = intersectingEntry.target.id;
    }
  };

  // ✅ 核心修复：创建并启动 IntersectionObserver
  observer = new IntersectionObserver(callback, {
    root: contentArea.value, // 指定滚动容器
    rootMargin: '-50% 0px -50% 0px', // 将触发线设置在视口的垂直中心
    threshold: 0, // 只要有一像素进入触发区域就触发
  });

  // 让 observer 开始监听所有 .form-section 元素
  const sections = contentArea.value.querySelectorAll('.form-section');
  sections.forEach((section) => {
    observer.observe(section);
  });
};


// --- Vue 生命周期钩子 ---

onMounted(() => {
  initializePage();
});

onBeforeUnmount(() => {
  // 组件卸载前，务必清理 observer，防止内存泄漏
  if (observer) {
    observer.disconnect();
  }
});
</script>

<style scoped>
/* 让组件背景色跟随全局主题变量 */
.system-settings-view {
  background-color: var(--bg-page);
  padding: 25px;
  min-height: calc(100vh - 60px);
}

.view-header {
  text-align: center;
  margin-bottom: 25px;
}
.view-title {
  font-size: 1.75rem;
  font-weight: 600;
  color: var(--text-primary);
}
.view-description {
  font-size: 1rem;
  color: var(--text-regular);
  margin-top: 8px;
}

/* 两栏布局 */
.settings-layout {
  display: flex;
  gap: 25px;
  max-width: 1200px;
  margin: 0 auto;
}

/* 左侧导航 */
.settings-nav {
  width: 220px;
  flex-shrink: 0;
  background-color: var(--bg-card);
  border-radius: 12px;
  padding: 15px;
  align-self: flex-start;
  position: sticky;
  top: 80px; /* 根据你的顶部导航栏高度调整 */
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 15px;
  border-radius: 8px;
  color: var(--text-regular);
  text-decoration: none;
  font-weight: 500;
  transition: all 0.2s ease;
  margin-bottom: 5px;
}
.nav-item i {
  margin-right: 12px;
  width: 20px;
  text-align: center;
}
.nav-item:hover {
  background-color: var(--primary-hover);
  color: white;
}
.nav-item.active {
  background-color: var(--primary-color);
  color: #fff;
  box-shadow: 0 4px 8px rgba(64, 158, 255, 0.2);
}

/* 右侧内容区 */
.settings-content-wrapper {
  flex-grow: 1;
  max-height: calc(100vh - 120px);
  overflow-y: auto;
  padding-right: 15px; /* for scrollbar */
}
.settings-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-section {
  background-color: var(--bg-card);
  border-radius: 12px;
  padding: 25px 30px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  scroll-margin-top: 20px;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 25px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 25px;
}

.form-field {
  display: flex;
  flex-direction: column;
}
.form-field label {
  font-weight: 500;
  color: var(--text-regular);
  margin-bottom: 8px;
}
.input-wrapper {
  position: relative;
}
.input-wrapper input {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.2s;
  box-sizing: border-box;
  background-color: var(--bg-page);
  color: var(--text-primary);
}
.input-wrapper input:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.15);
  outline: none;
}
.field-hint {
  font-size: 0.85rem;
  color: #909399;
  margin-top: 8px;
}

/* 主题选择器 */
.theme-selector {
  display: flex;
  gap: 20px;
}
.theme-option {
  cursor: pointer;
  border: 2px solid transparent;
  padding: 5px;
  border-radius: 10px;
  transition: all 0.2s ease;
}
.theme-option.active {
  border-color: var(--primary-color);
}
.theme-option .preview {
  width: 100px;
  height: 60px;
  border-radius: 8px;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  border: 1px solid var(--border-color);
}
.theme-option .preview.light { background-color: #f5f7fa; color: #e6a23c; }
.theme-option .preview.dark { background-color: #1a1a1a; color: #409eff; }
.theme-option .preview.system { 
  background: linear-gradient(135deg, #f5f7fa 49.5%, #1a1a1a 50.5%); 
  color: var(--text-secondary);
}
.theme-option span {
  display: block;
  text-align: center;
  font-weight: 500;
}

/* 图片上传 */
.image-uploader {
  display: flex;
  align-items: center;
  gap: 15px;
}
.image-preview {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  object-fit: contain;
  background-color: var(--bg-page);
}
.image-preview.favicon { width: 32px; height: 32px; }
.image-placeholder {
  width: 80px; height: 80px;
  border-radius: 8px; border: 2px dashed var(--border-color);
  display: flex; align-items: center; justify-content: center;
  color: var(--text-secondary); font-size: 2rem;
}
.image-placeholder.favicon { width: 32px; height: 32px; font-size: 1rem;}
.uploader-actions {
  display: flex;
  flex-direction: column;
}
.btn.btn-sm { padding: 5px 10px; font-size: 0.9rem; }

/* 滑块 */
.slider-wrapper { display: flex; align-items: center; gap: 20px; }
.slider { flex-grow: 1; -webkit-appearance: none; appearance: none; height: 8px; background: #e9e9e9; border-radius: 5px; outline: none; }
.slider::-webkit-slider-thumb { -webkit-appearance: none; appearance: none; width: 20px; height: 20px; background: var(--primary-color); cursor: pointer; border-radius: 50%; }
.slider-value { font-weight: 600; color: var(--primary-color); }

/* 危险区域 */
.danger-zone { border: 2px solid #fbc4c4; background-color: #fef0f0; }
.danger-zone .section-title { color: var(--danger-color); }
.danger-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.danger-item p { margin: 0; color: #d33a3a; }
.btn.btn-danger { background-color: var(--danger-color); color: white; border: none; }
.btn.btn-danger:hover { background-color: #f78989; }

/* 悬浮操作栏 */
.floating-actions {
  position: sticky;
  bottom: 0;
  left: 0;
  width: 100%;
  background: var(--bg-card);
  backdrop-filter: blur(5px);
  padding: 15px 30px;
  border-radius: 12px;
  box-shadow: 0 -4px 12px rgba(0,0,0,0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.3s ease;
  transform: translateY(200%);
  opacity: 0;
}
.floating-actions.show {
  transform: translateY(0);
  opacity: 1;
}
.floating-actions p { margin: 0; font-weight: 500; color: var(--text-primary); }
.floating-actions div { display: flex; gap: 10px; }

/* 基础按钮样式 */
.btn {
  padding: 10px 20px; border: 1px solid transparent; border-radius: 8px;
  font-size: 1rem; font-weight: 500; cursor: pointer;
  display: inline-flex; align-items: center; justify-content: center; gap: 8px;
  transition: all 0.2s ease;
}
.btn-primary { background-color: var(--primary-color); color: white; }
.btn-primary:hover { opacity: 0.85; }
.btn-secondary { background-color: white; color: var(--text-regular); border-color: var(--border-color); }
.btn-secondary:hover { color: var(--primary-color); border-color: var(--primary-color); background-color: #ecf5ff; }

.loading-placeholder { text-align: center; padding: 50px; }
.loading-spinner { width: 40px; height: 40px; border: 4px solid #f0f2f5; border-top-color: var(--primary-color); border-radius: 50%; animation: spin 1s linear infinite; margin: 0 auto 15px; }
@keyframes spin { to { transform: rotate(360deg); } }
</style>