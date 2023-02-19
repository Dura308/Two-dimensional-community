<template>
  <div>
    <el-dropdown>
      <el-link class = "header-nav-bar" href = "#" :underline = "false">
        <el-avatar :src="avatarUrl" :size="50"/>
      </el-link>

      <template v-if="tokenIsExist" #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="toPersonalCenter">个人中心</el-dropdown-item>
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
  import {computed, reactive, ref} from "vue";
  import {newGetRequest, newPostRequest} from "@/utils/api";
  import {ElMessage, ElNotification, FormInstance} from "element-plus";
  import {useRouter} from "vue-router";
  import HomeLogo from '@/assets/tofu社区.png'

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
    console.log(token)
    store.commit('parseToken', token)
    localStorage.setItem('token', token)
    changeLoginDialogVisible()
    loginLoading.value = false
    location.reload()
    ElNotification({
      title: '登录成功',
      message: '欢迎回来,' + store.state.loginInfo.nickName,
      duration: 3000,
    })

    newGetRequest('/user/getUserInfo', {
      token: token
    }).then((response) => {
      console.log(response)
      if(response.code === 200){
        localStorage.setItem('userInfo', response.data)
      }
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

</script>

<style scoped>

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
