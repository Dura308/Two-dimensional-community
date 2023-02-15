<template>
  <el-affix :offset="0" >
    <el-header class="tofu-header">
      <el-row :gutter="20">
        <el-col :span="3">
          <el-link class = "header-nav-bar" href = "#" :underline = "false">
            <el-image :src = HomeLogo></el-image>
          </el-link>
        </el-col>
        <el-col :span="6">
          <ul class="left-entry">
            <li><el-link class = "header-nav-bar" href = "#">推荐</el-link></li>
            <li><el-link class = "header-nav-bar" href = "#">关注</el-link></li>
            <li><el-link class = "header-nav-bar" href = "#">榜单</el-link></li>
            <li><el-link class = "header-nav-bar" href = "#">问答</el-link></li>
            <li><el-link class = "header-nav-bar" href = "#">cos</el-link></li>
          </ul>
        </el-col>
        <el-col :span="6">
          <div class="center-search">
            <el-form id="searchForm">
              <input
                v-model="input1"
                class="input-search"
                placeholder="Please Input"/>
              <el-button class="btn_search" :icon="Search" circle text/>
            </el-form>
          </div>
        </el-col>
        <el-col :span="9">
          <ul class="right-entry">
            <li>
              <el-dropdown>
                <el-link class = "header-nav-bar" href = "#" :underline = "false">
                  <el-avatar :src="avatarUrl" />
                </el-link>

                <template v-if="tokenIsExist" #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item >个人中心</el-dropdown-item>
                    <el-dropdown-item >设置</el-dropdown-item>
                    <el-dropdown-item @click="logOut">退出</el-dropdown-item>
                  </el-dropdown-menu>
                </template>

                <template v-else #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="changeLoginDialogVisible">登录</el-dropdown-item>
                    <el-dropdown-item @click="registDialogVisible = true">注册</el-dropdown-item>
                  </el-dropdown-menu>
                </template>

              </el-dropdown>
            </li>
            <li><el-link class = "header-nav-bar" href = "#" :icon = House>首页</el-link></li>
            <li><el-link class = "header-nav-bar" href = "#" :icon = Bell>消息</el-link></li>
            <li>
              <el-dropdown placement="bottom-end">
                <el-button class="btn-release" type="primary" color="#FC84A6" size="large" :icon = "Edit" round>发布</el-button>
                <template #dropdown>
                  <el-dropdown-menu class="release-menu">
                    <el-dropdown-item>
                      <el-link class="publish-item" :underline = "false" @click="newPicture">
                        <el-image :src = Picture></el-image>
                        <div class="text">
                          <p class="type">图片</p>
                          <p class="short">绘画、COS、手办、汉服、表情包…</p>
                        </div>
                      </el-link>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-link class="publish-item" :underline = "false" @click="newText">
                        <el-image :src = Word></el-image>
                        <div class="text">
                          <p class="type">文字</p>
                          <p class="short">小说、漫评、段子、文章、碎碎念…</p>
                        </div>
                      </el-link>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-link class="publish-item" :underline = "false" @click="newQuestion">
                        <el-image :src = Question></el-image>
                        <div class="text">
                          <p class="type">提问</p>
                          <p class="short">如何评价？如何反驳？为什么讨厌？</p>
                        </div>
                      </el-link>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-link class="publish-item" :underline = "false" @click="newVideo">
                        <el-image :src = Video></el-image>
                        <div class="text">
                          <p class="type">视频</p>
                          <p class="short">舞蹈、剪辑、游戏、MAD、生活...</p>
                        </div>
                      </el-link>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </li>
          </ul>
        </el-col>
      </el-row>
    </el-header>
  </el-affix>

  <!-- 登录对话框 -->
  <el-dialog
    v-model="loginDialogVisible"
    :before-close="loginDialogClose"
    title=""
    width="400"
    align-center
    center
    style="border-radius: 8px"
  >
    <template #header>
      <el-image :src = HomeLogo style="height: 72px"></el-image>
    </template>
    <div>
      <el-form
        class="login-form"
        ref="ruleFormRef"
        :model="loginFormByVfc"
        :hidden = "!loginFormByVfcVisible">
        <el-form-item
          prop="loginUid"
          :rules="[
            { required: true, message: '请填写手机号码', trigger: 'blur' },
            { min: 11, max: 11, message: '手机号码不低于11位数', trigger: 'blur' },
            { pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/, message: '请输入正确的手机号码' }
            ]">
          <el-input v-model="loginFormByVfc.loginAccount" placeholder="请输入手机号或者邮箱" size="large"/>
        </el-form-item>
        <el-form-item>
          <el-input v-model="loginFormByVfc.loginVfc" placeholder="短信验证码" size="large"/>
          <div class="input-extra">
            <el-button class="btn-getVfc" type="primary" @click="getLoginVfc">点击获取</el-button>
          </div>
        </el-form-item>
        <div class="login-actions">
          <span @click="loginFormByVfcVisible = false">账号密码登录</span>
          <el-link type="primary" :underline = "false" style="font-size: 14px">忘记密码?</el-link>
        </div>
        <el-button
          class="btn-login-submit"
          @click = loginByVfc
          type="primary"
          size="large">登&emsp;&emsp;录</el-button>

        <div class="regist">
          <el-link
            type="primary"
            @click="registDialogVisible = true; changeLoginDialogVisible"
            :underline = "false"
            style="font-size: 14px">注册账号</el-link>
        </div>
      </el-form>

      <el-form
        v-loading="loginLoading"
        element-loading-text="正在登录中..."
        class="login-form"
        ref="ruleFormRef"
        :model="loginFormByPassword"
        :hidden = "loginFormByVfcVisible">
        <el-form-item>
          <el-input v-model="loginFormByPassword.loginAccount" placeholder="请输入手机号或邮箱账号" size="large"/>
        </el-form-item>
        <el-form-item>
          <el-input v-model="loginFormByPassword.password" placeholder="请输入账户密码" size="large"/>
        </el-form-item>
        <div class="login-actions">
          <span @click="loginFormByVfcVisible = true">手机验证码登录</span>
          <el-link type="primary" :underline = "false" style="font-size: 14px">忘记密码?</el-link>
        </div>
        <el-button
          class="btn-login-submit"
          @click = loginByPassword
          type="primary"
          size="large">登&emsp;&emsp;录</el-button>

        <div class="regist">
          <el-link
            type="primary"
            @click="registDialogVisible = true; changeLoginDialogVisible"
            :underline = "false"
            style="font-size: 14px">注册账号</el-link>
        </div>
      </el-form>
    </div>
    <template #footer>
      <span class="dialog-footer">
      </span>
    </template>
  </el-dialog>

  <!-- 注册对话框 -->
  <el-dialog
    v-model="registDialogVisible"
    title=""
    width="400"
    align-center
    center
    style="border-radius: 8px"
  >
    <template #header>
      <el-image :src = HomeLogo style="height: 72px"></el-image>
    </template>
    <div>
      <el-form
        class="regist-form"
        :model="registForm">
        <el-form-item>
          <el-input v-model="registForm.registAccount" placeholder="请输入手机号或者邮箱" size="large"/>
        </el-form-item>
        <el-form-item>
          <el-input v-model="registForm.registVfc" placeholder="短信验证码" size="large"/>
          <div class="input-extra">
            <el-button class="btn-getVfc" type="primary" @click="getRegisterVfc">点击获取</el-button>
          </div>
        </el-form-item>
        <el-button
          class="btn-login-submit"
          @click = regist
          type="primary"
          size="large">注&emsp;&emsp;册</el-button>
      </el-form>
    </div>
    <template #footer>
      <span class="dialog-footer">
      </span>
    </template>
  </el-dialog>
</template>

<script lang = "ts" setup>
  import {ref, reactive, provide, computed, onMounted} from 'vue'
  import {
    Search,
    House,
    Bell,
    UserFilled,
    Edit
  } from '@element-plus/icons-vue'
  import Picture from '@/assets/img-picture.png'
  import Question from '@/assets/img-question.png'
  import Word from '@/assets/img-word.png'
  import Video from '@/assets/img-video.png'
  import HomeLogo from '@/assets/tofu社区.png'
  import type { FormInstance } from 'element-plus'
  import axios from "axios";
  import url from '@/index'
  import {useStore} from "vuex";
  import {useRouter} from "vue-router";
  import  {ElMessage, ElNotification} from "element-plus";
  import {newGetRequest, newPostRequest} from "../../../utils/api";

  const store = useStore()
  const router = useRouter()
  const ruleFormRef = ref<FormInstance>()
  const input1 = ref('')

  const loginDialogVisible = computed(() => {
    return store.state.loginDialogVisible
  })
  const changeLoginDialogVisible = () => {
    store.commit('changeLoginDialogVisible')
  }
  const loginDialogClose = (done:() => void) => {
    changeLoginDialogVisible()
    done()
  }
  const registDialogVisible = ref(false)
  const loginFormByVfcVisible = ref(true)
  const loginFormByVfc = reactive({
    loginAccount: '',
    loginVfc: ''
  })
  const loginFormByPassword = reactive({
    loginAccount: '',
    password: ''
  })
  const registForm = reactive({
    registAccount: '',
    registVfc: ''
  })


  /**监听用户token是否已存在*/
  const tokenIsExist = computed(() => {
    return store.state.token !== null
  })
  const loginUser = computed(() => {
    return store.state.loginUser
  })
  const avatarUrl = computed(() => {
    return loginUser.value.avatar
  })

  /** 登录加载 */
  const loginLoading = ref(false)

  /**密码登陆*/
  const loginByPassword = () => {
    loginLoading.value = true
    let formData = new FormData()
    formData.append('loginAccount', loginFormByPassword.loginAccount)
    formData.append('password', loginFormByPassword.password)
    newPostRequest('/home/loginByPwd', formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then(response => {
      if (response.code == 200) {
        finishLogin(response.data)
      }
      if(response.code === -1){
        loginLoading.value = false
      }
    })
  }

  /**登录校验*/
  const loginByVfc = () => {
    let formData = new FormData()
    formData.append('loginAccount', loginFormByVfc.loginAccount)
    formData.append('vfc', loginFormByVfc.loginVfc)
    newPostRequest('/home/loginByVfc', formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then(response => {
      if(response.code === 200){
        finishLogin(response.data)
      }
      if(response.code === -1){
        loginLoading.value = false
      }
    })
  }

  const getLoginVfc = () => {
    let formData = new FormData()
    formData.append('loginAccount', loginFormByVfc.loginAccount)
    newPostRequest('/home/sendLoginVfc', formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then(response => {
      if(response.code === 200){
        ElMessage({
          message: response.data
        })
      }
    })
  }

  /** 获取注册验证码 */
  const getRegisterVfc = () => {
    let formData = new FormData()
    formData.append('registAccount', registForm.registAccount)
    newPostRequest('/home/sendRegisterVfc', formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then(response => {
      if(response.code === 200){
        ElMessage({
          message: response.data
        })
      }
    })
  }

  /** 注册 */
  const regist = () => {
    let formData = new FormData()
    formData.append('registAccount', registForm.registAccount)
    formData.append('vfc', registForm.registVfc)
    newPostRequest('/home/register', formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then(response => {
      if(response.code === 200){
        ElMessage({
          message: response.data
        })
      }
    })
  }

  const finishLogin = (token) => {

    console.log(token)
    store.commit('setToken', token)
    localStorage.setItem('token', token)
    changeLoginDialogVisible()
    loginLoading.value = false
    location.reload()
    ElNotification({
      title: '登录成功',
      message: '欢迎回来,' + store.state.loginUser.nickName,
      duration: 3000,
    })
  }

  /** 登出 */
  const logOut = () => {
    newGetRequest('/home/logOut', {
      token: store.state.token
    }).then(response => {
      if(response.code === 200){
        store.commit('removeToken')
        localStorage.removeItem('store')
        localStorage.removeItem('token')
        location.reload()
      }
    })
  }

  /** 发布图片 */
  const newPicture = () => {
    const newPage = router.resolve({
      path: '/newPicture',
      query: {
        'token': store.state.token,
        'userId': store.state.loginUser.userId,
        'nickName': store.state.loginUser.nickName
      }
    })
    window.open(newPage.href,'_blank')
  }

  /** 发布文字 */
  const newText = () => {
    const newPage = router.resolve({
      path: '/newText',
    })
    window.open(newPage.href,'_blank')
  }

  /** 发布提问 */
  const newQuestion = () => {
    const newPage = router.resolve({
      path: '/newQuestion',
    })
    window.open(newPage.href,'_blank')
  }

  /** 发布视频 */
  const newVideo = () => {
    const newPage = router.resolve({
      path: '/newVideo',
    })
    window.open(newPage.href,'_blank')
  }



</script>

<style scoped>
  .tofu-header{
    min-width: 1200px;
    height: 64px;
    padding-top: 1px;
    background-color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    border-bottom: solid 1px var(--el-border-color);
    box-shadow: var(--el-box-shadow-light);
  }

  ul{
    list-style-type: none;
    padding-left: 0;
  }

  ul li{
    padding: 10px;
  }

  .el-link{
    font-size: 16px;
  }

  .tofu-header .left-entry{
    display: flex;
    align-items: center;
    flex-shrink: 0;
  }

  .tofu-header .right-entry{
    display: flex;
    align-items: center;
    flex-shrink: 0;
  }

  .left-entry li, .right-entry li{
    width: 48px;
    text-align: center;
  }

  .el-row{
    align-items: center;
    display: flex;
  }

  .center-search{

  }

  #searchForm{
    display: flex;
    align-items: center;
    background-color: white;
    border: solid 1px var(--el-border-color);
    border-radius: 50px 50px 50px 50px;
  }

  .input-search{
    height: 25px;
    width: 80%;
    margin-left: 10px;
    border-radius: 8px;
    border: none;
    outline: none;
  }

  .btn_search{
    margin-left: 10px;
    margin-right: 10px;
  }

  .btn-release{
    font-weight: lighter;
    font-size: 16px;
    color: white;
  }

  .btn-release:hover{
    color: white;
  }

  .publish-item{
    width: 280px;
    height: 66px;
    padding: 5px 15px 5px 5px;
    margin-bottom: 5px;
    border-radius: 6px;
  }

  .text{
    margin-left: 10px;
  }

  .text .type{
    font-size: 16px;
    line-height: 1;
    color: #252526;
  }

  .text .short{
    font-size: 12px;
    line-height: 1;
    color: #a1a1a6;
    margin-top: 6px;
  }

  .input-extra{
    position: absolute;
    right: 0;
    top: 0;
  }

  .btn-getVfc{
    margin-top: 4px;
    margin-right: 4px;
  }

  .login-actions{
    display: flex;
    justify-content: space-between;
    margin-bottom: 28px;
  }

  .login-actions span{
    font-size: 14px;
    color: #43A0FF;
    cursor: pointer;
  }

  .btn-login-submit{
    width: 100%;
    text-align: center;
  }

  .regist{
    float: right;
    margin-top: 20px;
  }
</style>
