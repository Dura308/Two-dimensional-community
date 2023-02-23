<template>
  <div>
    <div v-if="tokenIsExist">
      <el-popover placement="bottom"
                  class="user-popover"
                  :show-arrow="false"
                  :show-after="200"
                  :hide-after="100"
                  transition="el-zoom-in-top"
                  :popper-style="popperStyle"
                  :offset="0"
                  :width="250"
                  trigger="hover">
        <template #reference>
          <el-link class = "header-nav-bar" :underline = "false">
            <el-avatar :class="avatarClass"
                       :src="avatarUrl"
                       @mouseover="avatarMouseOver"
                       @mouseout="avatarMouseOut"
                       :size="50"/>
          </el-link>
        </template>
        <div class="user-popover-content"
             @mouseover="avatarMouseOver"
             @mouseout="avatarMouseOut"
             style="padding: 20px 10px 0 10px;">
          <div class="user-popover-main">
            <div class="user-popover-nickName">
              <span>{{ userInfo.nickName }}</span>
            </div>
            <div class="user-popover-social-info">
              <div class="user-popover-info">
                <span class="user-popover-info-num">{{ userInfo.attentionNum }}</span>
                <span class="user-popover-info-text">关注</span>
              </div>
              <div class="user-popover-info">
                <span class="user-popover-info-num">{{ userInfo.fansNum }}</span>
                <span class="user-popover-info-text">粉丝</span>
              </div>
              <div class="user-popover-info">
                <span class="user-popover-info-num">{{ userInfo.allLikesNum }}</span>
                <span class="user-popover-info-text">获赞</span>
              </div>
            </div>

            <div class="user-popover-btn user-center" @click="toPersonalCenter">
              <div class="user-popover-btn-icon">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-wode"></use>
                </svg>
              </div>
              <span class="user-popover-btn-text">个人中心</span>
            </div>

            <div class="user-popover-btn user-center">
              <div class="user-popover-btn-icon">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-fabu"></use>
                </svg>
              </div>
              <span class="user-popover-btn-text">发布管理</span>
            </div>

            <div class="user-popover-btn user-center">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-tuijian"></use>
              </svg>
              <span class="user-popover-btn-text">推荐服务</span>
            </div>
            <el-divider />
            <div class="user-popover-btn user-center" @click="logOut">
              <div class="user-popover-btn-icon">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-tuichu"></use>
                </svg>
              </div>
              <span class="user-popover-btn-text">退出登录</span>
            </div>
          </div>
        </div>
      </el-popover>
    </div>

    <div v-else>
      <el-dropdown>
        <el-link class = "header-nav-bar" href = "#" :underline = "false">
          <el-avatar :src="avatarUrl" :size="50"/>
        </el-link>

        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="changeLoginDialogVisible">登录</el-dropdown-item>
            <el-dropdown-item @click="registDialogVisible = true">注册</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

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
            <el-input
              v-model="loginFormByVfc.loginVfc"
              placeholder="短信验证码"
              size="large"/>
            <div class="input-extra">
              <el-button
                class="btn-getVfc"
                type="primary"
                @click="getLoginVfc"
                :disabled="loginVfcBtnDisabled">
                {{ loginVfcBtnDisabled === false ? '点击获取' : '重新获取(' + loginCountdownValue + ')'}}
              </el-button>
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
            <el-input
              v-model="loginFormByPassword.loginAccount"
              placeholder="请输入手机号或邮箱账号"
              size="large"/>
          </el-form-item>

          <el-form-item>
            <el-input
              v-model="loginFormByPassword.password"
              placeholder="请输入账户密码"
              size="large"
              type="password"
              show-password/>
          </el-form-item>

          <div class="login-actions">
            <span @click="loginFormByVfcVisible = true">手机验证码登录</span>
            <el-link type="primary" :underline = "false" style="font-size: 14px">忘记密码?</el-link>
          </div>

          <el-button
            class="btn-login-submit"
            @click = loginByPassword
            type="primary"
            size="large">
            登&emsp;&emsp;录
          </el-button>

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
              <el-button class="btn-getVfc" type="primary" @click="getRegisterVfc" :disabled="registerVfcBtnDisabled">
                {{ registerVfcBtnDisabled === false ? '点击获取' : '重新获取(' + registerCountdownValue + ')'}}
              </el-button>
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
  </div>
</template>

<script lang = "ts" setup>

  import {useStore} from "vuex";
  import {computed, onMounted, reactive, ref} from "vue";
  import {newGetRequest, newPostRequest} from "@/utils/api";
  import {ElMessage, ElNotification, FormInstance} from "element-plus";
  import {useRouter} from "vue-router";
  import HomeLogo from '@/assets/tofu社区.png'
  import "@/icon.js"

  const store = useStore()
  const router = useRouter()
  const ruleFormRef = ref<FormInstance>()
  const registDialogVisible = ref(false)
  const loginFormByVfcVisible = ref(true)
  const loginDialogVisible = computed(() => {
    return store.state.loginDialogVisible
  })

  const loginFormByVfc = reactive({
    loginAccount: '',
    loginVfc: ''
  })

  const registForm = reactive({
    registAccount: '',
    registVfc: ''
  })

  const loginFormByPassword = reactive({
    loginAccount: '19852260071',
    password: '123456'
  })

  /**监听用户token是否已存在*/
  const tokenIsExist = computed(() => {
    return store.state.token !== null
  })
  const avatarUrl = computed(() => {
    return store.state.loginInfo.avatar
  })
  const userInfo = ref({})

  const changeLoginDialogVisible = () => {
    store.commit('changeLoginDialogVisible')
  }

  const loginDialogClose = (done:() => void) => {
    changeLoginDialogVisible()
    done()
  }

  const loginVfcBtnDisabled = ref(false)
  const registerVfcBtnDisabled = ref(false)
  const loginCountdownValue = ref()
  const registerCountdownValue = ref()

  /** 登录加载 */
  const loginLoading = ref(false)

  /** 验证码倒计时 */
  const countdown = (type) => {
    console.log(type)
    if(type === 'login'){
      loginVfcBtnDisabled.value = true
      loginCountdownValue.value = 60
      const start = setInterval(() => {
        if(loginCountdownValue.value > 1){
          loginCountdownValue.value--
        }else {
          loginVfcBtnDisabled.value = false
          clearInterval(start)
        }
      }, 1000)
    }
    if(type === 'register'){
      registerVfcBtnDisabled.value = true
      registerCountdownValue.value = 60
      const start = setInterval(() => {
        if(registerCountdownValue.value > 1){
          registerCountdownValue.value--
        }else {
          registerVfcBtnDisabled.value = false
          clearInterval(start)
        }
      }, 1000)
    }
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
        localStorage.removeItem('userInfo')
        ElMessage({
          message: response.data,
          type: 'success'
        })
        setTimeout(() => {
          location.reload()
        },500)
      }
    })
  }

  /** 获取登录验证码 */
  const getLoginVfc = () => {
    countdown('login')
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
    countdown('register')
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

  /** 登录校验 */
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

  /** 密码登陆 */
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

  /** 登录后的处理 */
  const finishLogin = (token) => {
    store.commit('parseToken', token)
    localStorage.setItem('token', token)
    changeLoginDialogVisible()
    loginLoading.value = false
    console.log('loginLoading>>>' + loginLoading.value)
    ElNotification({
      title: '登录成功',
      message: '欢迎回来,' + store.state.loginInfo.nickName,
      duration: 3000,
    })

    getUserInfo(token).then(response => {
      if(response === true){
        location.reload()
      }
    })
  }

  const getUserInfo = (token) => {
    return new Promise(resolve => {
      newGetRequest('/user/getUserInfo', {
        token: token
      }).then((response) => {
        if(response.code === 200){
          localStorage.setItem('userInfo', JSON.stringify(response.data))
          resolve(true)
        }
      })
    })
  }

  /** 转到个人中心 */
  const toPersonalCenter = () => {
    const newPage = router.resolve({
      path: '/personalCenter',
      query: {
        'userId': store.state.loginInfo.userId,
      }
    })
    window.open(newPage.href,'_blank')
  }

  const avatarClass = ref()
  const avatarMouseOver = () => {
    avatarClass.value = 'big-avatar'
  }
  const avatarMouseOut = () => {
    avatarClass.value = 'user-avatar'
  }

  const popperStyle = {
    borderRadius: '8px',
  }

  onMounted(() => {
    if(localStorage.getItem('userInfo')){
      userInfo.value = JSON.parse(localStorage.getItem('userInfo'))
    }
  })
</script>

<style scoped>

  :deep(.el-divider--horizontal){
    margin: 0 0 8px 0;
  }

  .user-popover-main{
    display: flex;
    flex-direction: column;
  }

  .user-popover-nickName{
    display: flex;
    justify-content: center;
  }

  .user-popover-nickName > span{
    font-weight: bolder;
    font-size: 20px
  }

  .user-popover-social-info{
    display: flex;
    justify-content: space-around;
  }

  .big-avatar{
    transition: all 0.3s;
    transform: scale(1.4) translate(0, 10px);
    z-index: 20000;
  }

  .user-avatar{
    transition: all 0.3s;
    z-index: 20000;
  }

  .user-avatar:hover{
    transform: scale(1.4) translate(0, 10px);
  }

  .user-popover-main > .user-popover-nickName,
  .user-popover-social-info, .user-popover-btn{
    margin-bottom: 8px;
  }

  .user-popover-btn{
    cursor: pointer;
    border-radius: 10px;
    color: #61666d;
    height: 35px;
    display: flex;
    align-items: center;
  }

  .user-popover-btn:hover{
    background-color: #E3E5E7;
  }

  .user-popover-info{
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
  }

  .user-popover-info:hover{
    color: #43A0FF;
  }

  .user-popover-info > .user-popover-info-num{
    font-size: 18px;
  }

  .user-popover-info > .user-popover-info-text{
    font-size: 10px;
  }

  .icon{
    width: 3em;
    height: 1.1em;
    vertical-align: -0.15em;
    fill: currentColor;
    overflow: hidden;
    font-weight: bold;
  }

  .user-popover-btn > .user-popover-btn-text{
    font-size: 14px;
  }

  .user-popover-btn > .user-popover-btn-icon{
    display: flex;
    align-items: center;
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
