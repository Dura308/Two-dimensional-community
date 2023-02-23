import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
// @ts-ignore
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import 'element-plus/dist/index.css'
import './utils/axios'


createApp(App)
  .use(store)
  .use(router)
  .use(ElementPlus, {
    locale: zhCn
  })
  .mount('#app')
