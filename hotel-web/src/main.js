import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// createApp(App).mount('#app')
const app = createApp(App)
app.use(ElementPlus) // --- 还有这一行 ---
app.mount('#app')