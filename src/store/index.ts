import {createStore} from 'vuex'
import jwtDecode from 'jwt-decode';

export default createStore({
  state: {
    token: null,
    loginUser: {
      'userId': null,
      'userName': '',
      'nickName': '',
      'phone': '',
      'userGender': '',
      'userArea': '',
      'userIntro': '',
      'avatar': 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      'email': '',
    },
    loginDialogVisible: false
  },

  getters: {
  },

  mutations: {
    setToken(state, newToken){
      const decode = jwtDecode(newToken)
      // @ts-ignore
      const {userId, userName, nickName, phone, userGender,userArea, userIntro, avatar, email} = decode;
      state.loginUser.userId = userId
      state.loginUser.userName = userName
      state.loginUser.nickName = nickName
      state.loginUser.phone = phone
      state.loginUser.userGender = userGender
      state.loginUser.userArea = userArea
      state.loginUser.userIntro = userIntro
      state.loginUser.avatar = avatar
      state.loginUser.email = email
      state.token = newToken
    },
    removeToken(state){
      state.token = null
      state.loginUser = {
        'userId': null,
        'userName': '',
        'nickName': '',
        'phone': '',
        'userGender': '',
        'userArea': '',
        'userIntro': '',
        'avatar': 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        'email': '',
      }
    },
    changeLoginDialogVisible(state){
      state.loginDialogVisible = !state.loginDialogVisible
    }
  },
  actions: {
  },
  modules: {
  }
})
