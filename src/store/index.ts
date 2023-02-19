import {createStore} from 'vuex'
import jwtDecode from 'jwt-decode';

export default createStore({
  state: {
    token: null,

    loginInfo: {
      'userId': null,
      'nickName': '',
      'avatar': 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
    },
    userInfo:{},
    loginDialogVisible: false
  },

  getters: {
  },

  mutations: {
    parseToken(state, newToken){
      const decode = jwtDecode(newToken)
      // @ts-ignore
      const {userId, nickName, avatar} = decode;
      state.loginInfo.userId = userId
      state.loginInfo.nickName = nickName
      state.loginInfo.avatar = avatar
      state.token = newToken
    },
    removeToken(state){
      state.token = null
      state.loginInfo = {
        'userId': null,
        'nickName': '',
        'avatar': 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      }
    },
    setUserInfo(state, userInfo){
      state.userInfo = userInfo
    },
    updateAvatar(state, avatar){
      state.loginInfo.avatar = avatar
    },
    changeLoginDialogVisible(state){
      state.loginDialogVisible = !state.loginDialogVisible
    },

  },
  actions: {
  },
  modules: {
  }
})
